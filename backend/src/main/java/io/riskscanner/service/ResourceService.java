package io.riskscanner.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.riskscanner.base.domain.*;
import io.riskscanner.base.mapper.*;
import io.riskscanner.base.mapper.ext.ExtResourceMapper;
import io.riskscanner.base.mapper.ext.ExtScanHistoryMapper;
import io.riskscanner.base.mapper.ext.ExtTaskMapper;
import io.riskscanner.commons.constants.CommandEnum;
import io.riskscanner.commons.constants.ResourceConstants;
import io.riskscanner.commons.constants.TaskConstants;
import io.riskscanner.commons.utils.*;
import io.riskscanner.controller.request.excel.ExcelExportRequest;
import io.riskscanner.controller.request.resource.ResourceRequest;
import io.riskscanner.dto.*;
import io.riskscanner.i18n.Translator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author maguohao
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceService {
    @Resource
    private ExtResourceMapper extResourceMapper;
    @Resource
    private ResourceMapper resourceMapper;
    @Resource
    private ResourceRuleMapper resourceRuleMapper;
    @Resource
    private TaskItemMapper taskItemMapper;
    @Resource
    private TaskItemResourceMapper taskItemResourceMapper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private ExtTaskMapper extTaskMapper;
    @Resource
    private TaskItemLogMapper taskItemLogMapper;
    @Resource
    private TaskService taskService;
    @Resource
    private OrderService orderService;
    @Resource
    private RuleMapper ruleMapper;
    @Resource
    private ResourceItemMapper resourceItemMapper;
    @Resource
    private ScanHistoryMapper scanHistoryMapper;
    @Resource
    private ExtScanHistoryMapper extScanHistoryMapper;
    @Resource
    private ScanTaskHistoryMapper scanTaskHistoryMapper;

    public SourceDTO source (String accountId) {
        return extResourceMapper.source(accountId);
    }

    public List<ResourceDTO> search(ResourceRequest request) {
        List<ResourceDTO> resourceDTOListTmp = new ArrayList<>();
        try {
            List<ResourceDTO> complianceResultList = getComplianceResult(request);
            for (ResourceDTO resourceDTO : complianceResultList) {
                //pretty the json  string
                StringBuffer stringBuffer = new StringBuffer();
                String pretty = JSON.toJSONString(resourceDTO, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
                stringBuffer.append(pretty);
                resourceDTO.setResourceStr(stringBuffer.toString());
                resourceDTOListTmp.add(resourceDTO);
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
        }
        return resourceDTOListTmp;
    }

    public List<ReportDTO> reportList(ResourceRequest request) {
        return extResourceMapper.reportList(request);
    }

    private List<ResourceDTO> getComplianceResult(ResourceRequest resourceRequest) {
        List<ResourceDTO> complianceResult = extResourceMapper.getComplianceResult(resourceRequest);
        return complianceResult;
    }

    public ResourceWithBLOBs saveResource(ResourceWithBLOBs resourceWithBLOBs, TaskItemWithBLOBs taskItem, Task task, TaskItemResourceWithBLOBs taskItemResource) throws Exception {
        try {
            //保存创建的资源
            long now = System.currentTimeMillis();
            resourceWithBLOBs.setCreateTime(now);
            resourceWithBLOBs.setUpdateTime(now);
            JSONArray jsonArray = JSONArray.parseArray(resourceWithBLOBs.getResources());
            resourceWithBLOBs.setReturnSum((long) jsonArray.size());
            //执行去除filter的yaml，取到总数
            resourceWithBLOBs = updateResourceSum(resourceWithBLOBs);

            for (Object obj : jsonArray) {
                //资源详情
                saveResourceItem(resourceWithBLOBs, JSONObject.parseObject(obj.toString()));
            }
            //资源、规则、申请人关联表
            ResourceRule resourceRule = new ResourceRule();
            resourceRule.setResourceId(resourceWithBLOBs.getId());
            resourceRule.setRuleId(taskItem.getRuleId());
            resourceRule.setApplyUser(task.getApplyUser());
            if (resourceRuleMapper.selectByPrimaryKey(resourceWithBLOBs.getId()) != null) {
                resourceRuleMapper.updateByPrimaryKeySelective(resourceRule);
            } else {
                resourceRuleMapper.insertSelective(resourceRule);
            }

            //任务条目和资源关联表
            taskItemResource.setResourceId(resourceWithBLOBs.getId());
            insertTaskItemResource(taskItemResource);

            //计算sum资源总数与扫描的资源数到task
            int resource_sum = extTaskMapper.getResourceSum(task.getId());
            int return_sum = extTaskMapper.getReturnSum(task.getId());
            task.setResourcesSum((long) resource_sum);
            task.setReturnSum((long) return_sum);
            taskMapper.updateByPrimaryKeySelective(task);
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw e;
        }

        return resourceWithBLOBs;
    }

    private ResourceItem saveResourceItem(ResourceWithBLOBs resourceWithBLOBs, JSONObject jsonObject) throws Exception {
        ResourceItem resourceItem = new ResourceItem();
        try{
            String F2CId = jsonObject.getString("F2CId");
            resourceItem.setAccountId(resourceWithBLOBs.getAccountId());
            resourceItem.setUpdateTime(System.currentTimeMillis());
            resourceItem.setPluginIcon(resourceWithBLOBs.getPluginIcon());
            resourceItem.setPluginId(resourceWithBLOBs.getPluginId());
            resourceItem.setPluginName(resourceWithBLOBs.getPluginName());
            resourceItem.setRegionId(resourceWithBLOBs.getRegionId());
            resourceItem.setRegionName(resourceWithBLOBs.getRegionName());
            resourceItem.setResourceId(resourceWithBLOBs.getId());
            resourceItem.setSeverity(resourceWithBLOBs.getSeverity());
            resourceItem.setResourceType(resourceWithBLOBs.getResourceType());
            resourceItem.setF2cId(F2CId);
            resourceItem.setResource(jsonObject.toJSONString());

            ResourceItemExample example = new ResourceItemExample();
            example.createCriteria().andF2cIdEqualTo(F2CId).andResourceIdEqualTo(resourceWithBLOBs.getId());
            List<ResourceItem> resourceItems = resourceItemMapper.selectByExampleWithBLOBs(example);
            if (resourceItems.size() > 0) {
                resourceItem.setId(resourceItems.get(0).getId());
                resourceItemMapper.updateByPrimaryKeySelective(resourceItem);
            } else {
                resourceItem.setId(UUIDUtil.newUUID());
                resourceItem.setCreateTime(System.currentTimeMillis());
                resourceItemMapper.insertSelective(resourceItem);
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw e;
        }
        return resourceItem;
    }

    private ResourceWithBLOBs updateResourceSum(ResourceWithBLOBs resourceWithBLOBs) throws Exception {
        try {
            resourceWithBLOBs = calculateTotal(resourceWithBLOBs);
            AccountWithBLOBs account = accountMapper.selectByPrimaryKey(resourceWithBLOBs.getAccountId());
            resourceWithBLOBs.setPluginIcon(account.getPluginIcon());
            resourceWithBLOBs.setPluginName(account.getPluginName());
            resourceWithBLOBs.setPluginId(account.getPluginId());
            if (resourceWithBLOBs.getReturnSum() > 0) {
                resourceWithBLOBs.setResourceStatus(ResourceConstants.RESOURCE_STATUS.NotFixed.name());
            } else {
                resourceWithBLOBs.setResourceStatus(ResourceConstants.RESOURCE_STATUS.NotNeedFix.name());
            }

            if (resourceWithBLOBs.getId() != null) {
                resourceMapper.updateByPrimaryKeySelective(resourceWithBLOBs);
            } else {
                resourceWithBLOBs.setId(UUIDUtil.newUUID());
                resourceMapper.insertSelective(resourceWithBLOBs);
            }
        } catch (Exception e) {
            LogUtil.error("[{}] Generate updateResourceSum policy.yml file，and custodian run failed:{}", resourceWithBLOBs.getId(), e.getMessage());
            throw e;
        }
        return resourceWithBLOBs;
    }

    public ResourceWithBLOBs calculateTotal(ResourceWithBLOBs resourceWithBLOBs) throws Exception {
        String dirPath = "";
        try {
            String uuid = resourceWithBLOBs.getId() != null ? resourceWithBLOBs.getId() : UUIDUtil.newUUID();
            String resultFile = ResourceConstants.QUERY_ALL_RESOURCE.replace("{resource_name}", resourceWithBLOBs.getDirName());
            resultFile = resultFile.replace("{resource_type}", resourceWithBLOBs.getResourceType());
            dirPath = CommandUtils.saveAsFile(resultFile, uuid);
            LogUtil.info(resourceWithBLOBs.getResourceType() + " ::: count resource sum ::: start");
            Map<String, String> map = PlatformUtils.getAccount(accountMapper.selectByPrimaryKey(resourceWithBLOBs.getAccountId()), resourceWithBLOBs.getRegionId());
            String command = PlatformUtils.fixedCommand(CommandEnum.custodian.getCommand(), CommandEnum.run.getCommand(), dirPath, "policy.yml", map);
            String resultStr = CommandUtils.commonExecCmdWithResult(command, dirPath);
            if (LogUtil.getLogger().isDebugEnabled()) {
                LogUtil.getLogger().debug("resource created: {}", resultStr);
            }
            String resources = ReadFileUtils.readJsonFile(dirPath + "/" + resourceWithBLOBs.getDirName() + "/", TaskConstants.RESOURCES_RESULT_FILE);
            JSONArray jsonArray = JSONArray.parseArray(resources);
            if ((long) jsonArray.size() < resourceWithBLOBs.getReturnSum()) {
                resourceWithBLOBs.setResourcesSum(resourceWithBLOBs.getReturnSum());
            } else {
                resourceWithBLOBs.setResourcesSum((long) jsonArray.size());
            }
            LogUtil.info(resourceWithBLOBs.getResourceType() + " ::: count resource sum ::: end");
            //执行完删除返回目录文件，以便于下一次操作覆盖
            String deleteResourceDir = "rm -rf " + dirPath;
            CommandUtils.commonExecCmdWithResult(deleteResourceDir, dirPath);
        } catch (Exception e) {
            throw e;
        }
        return resourceWithBLOBs;
    }

    private void insertTaskItemResource(TaskItemResourceWithBLOBs taskItemResource) {
        if (taskItemResource.getId() != null) {
            taskItemResourceMapper.updateByPrimaryKeySelective(taskItemResource);
        } else {
            taskItemResourceMapper.insertSelective(taskItemResource);
        }
    }

    public ResourceDetailDTO getResource(String id) {
        ResourceDetailDTO dto = new ResourceDetailDTO();
        ResourceWithBLOBs resource = resourceMapper.selectByPrimaryKey(id);
        resource.setMetadata(toJSONString(resource.getMetadata()));
        resource.setResources(toJSONString2(resource.getResources()));

        BeanUtils.copyBean(dto, resource);
        TaskItemResourceExample example = new TaskItemResourceExample();
        example.createCriteria().andResourceIdEqualTo(id);
        List<TaskItemResource> taskItemResources = taskItemResourceMapper.selectByExample(example);

        TaskItemWithBLOBs taskItemWithBLOBs = taskItemMapper.selectByPrimaryKey(taskItemResources.get(0).getTaskItemId());
        Task task = taskMapper.selectByPrimaryKey(taskItemWithBLOBs.getTaskId());
        TaskDTO taskDTO = new TaskDTO();
        BeanUtils.copyBean(taskDTO, task);
        dto.setTaskDTO(taskDTO);
        dto.setCustomData(taskItemWithBLOBs.getCustomData());

        return dto;
    }

    public String toJSONString(String jsonString) {
        JSONObject object = JSONObject.parseObject(jsonString);
        String pretty = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        return pretty;
    }

    public String toJSONString2(String jsonString) {
        JSONArray jsonArray = JSONArray.parseArray(jsonString);
        String pretty = JSON.toJSONString(jsonArray, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        return pretty;
    }

    public List<String> getResourceIdsByTaskItemId(String taskItemId) {
        TaskItemResourceExample taskItemResourceExample = new TaskItemResourceExample();
        taskItemResourceExample.createCriteria().andTaskItemIdEqualTo(taskItemId);
        return taskItemResourceMapper.selectByExample(taskItemResourceExample)
                .stream()
                .map(TaskItemResource::getResourceId)
                .collect(Collectors.toList());
    }

    public List<ResourceWithBLOBs> getResourceByTaskItemId(String taskItemId) {
        List<ResourceWithBLOBs> list = new LinkedList<>();
        TaskItemResourceExample example = new TaskItemResourceExample();
        example.createCriteria().andTaskItemIdEqualTo(taskItemId);
        List<TaskItemResource> taskItemResources = taskItemResourceMapper.selectByExample(example);
        if (CollectionUtils.size(taskItemResources) != 1) {
            return null;
        }
        for (TaskItemResource itemResource : taskItemResources) {
            ResourceWithBLOBs resourceWithBLOBs = resourceMapper.selectByPrimaryKey(itemResource.getResourceId());
            list.add(resourceWithBLOBs);
        }
        return list;
    }

    /**
     * 执行actions(修复动作)
     * @param id
     * @param operating
     * @return
     * @throws Exception
     */
    public ResourceWithBLOBs operatingResource(String id, String operating) throws Exception {
        ResourceWithBLOBs resourceWithBLOBs = resourceMapper.selectByPrimaryKey(id);
        try {
            operatingDetail(resourceWithBLOBs, operating);
            resourceWithBLOBs = operatingDetail(resourceWithBLOBs, "restart");//执行完actions 操作后获取的数据并未更新
            if (resourceWithBLOBs.getReturnSum() > 0) {
                resourceWithBLOBs.setResourceStatus(ResourceConstants.RESOURCE_STATUS.AlreadyFixed.name());
            } else {
                resourceWithBLOBs.setResourceStatus(ResourceConstants.RESOURCE_STATUS.NotNeedFix.name());
            }
            resourceMapper.updateByPrimaryKeySelective(resourceWithBLOBs);
            taskService.syncTaskSum();//资源（扫描结果）数量变化的话要更新任务表的数据
        } catch (Exception e) {
            resourceWithBLOBs.setResourceStatus(ResourceConstants.RESOURCE_STATUS.Error.name());
            resourceMapper.updateByPrimaryKeySelective(resourceWithBLOBs);
            throw e;
        }
        return resourceWithBLOBs;
    }

    public ResourceWithBLOBs operatingDetail(ResourceWithBLOBs resourceWithBLOBs, String operating) throws Exception {
        try {
            String operation = "";
            String finalScript = "";
            if (StringUtils.equals(operating, "fix")) {
                operation = Translator.get("i18n_bug_fix");
                finalScript = resourceWithBLOBs.getResourceCommandAction();
            } else if (StringUtils.equals(operating, "restart")) {
                operation = Translator.get("i18n_more_resource");
                finalScript = resourceWithBLOBs.getResourceCommand();
            }
            String dirPath = "";
            AccountExample example = new AccountExample();
            example.createCriteria().andPluginNameEqualTo(resourceWithBLOBs.getPluginName()).andStatusEqualTo("VALID");
            String region = resourceWithBLOBs.getRegionId();
            TaskItemResourceExample taskItemResourceExample = new TaskItemResourceExample();
            taskItemResourceExample.createCriteria().andResourceIdEqualTo(resourceWithBLOBs.getId());
            TaskItemResource taskItemResource = taskItemResourceMapper.selectByExample(taskItemResourceExample).get(0);
            TaskItem taskItem = taskItemMapper.selectByPrimaryKey(taskItemResource.getTaskItemId());
            Map<String, String> map = PlatformUtils.getAccount(accountMapper.selectByPrimaryKey(taskItem.getAccountId()), region);

            orderService.saveTaskItemLog(taskItem.getId(), resourceWithBLOBs.getId(), Translator.get("i18n_operation_begin") + ": " + operation, StringUtils.EMPTY, true);

            dirPath = CommandUtils.saveAsFile(finalScript, resourceWithBLOBs.getId());
            String command = PlatformUtils.fixedCommand(CommandEnum.custodian.getCommand(), CommandEnum.run.getCommand(), dirPath, "policy.yml", map);
            String resultStr = CommandUtils.commonExecCmdWithResult(command, dirPath);
            if (!resultStr.isEmpty() && !resultStr.contains("INFO")) {
                throw new Exception(Translator.get("i18n_compliance_rule_error") + ": " + resultStr);
            }
            CommandUtils.commonExecCmdWithResult(command, dirPath);//第一次执行action会修复资源，但是会返回修复之前的数据回来。所以此处再执行一次
            String custodian_run = ReadFileUtils.readJsonFile(dirPath + "/" + taskItemResource.getDirName() + "/", TaskConstants.CUSTODIAN_RUN_RESULT_FILE);
            String metadata = ReadFileUtils.readJsonFile(dirPath + "/" + taskItemResource.getDirName() + "/", TaskConstants.METADATA_RESULT_FILE);
            String resources = ReadFileUtils.readJsonFile(dirPath + "/" + taskItemResource.getDirName() + "/", TaskConstants.RESOURCES_RESULT_FILE);

            resourceWithBLOBs.setCustodianRunLog(custodian_run);
            resourceWithBLOBs.setMetadata(metadata);
            resourceWithBLOBs.setResources(resources);

            JSONArray jsonArray = JSONArray.parseArray(resourceWithBLOBs.getResources());
            resourceWithBLOBs.setReturnSum((long) jsonArray.size());
            resourceWithBLOBs = calculateTotal(resourceWithBLOBs);

            orderService.saveTaskItemLog(taskItem.getId(), resourceWithBLOBs.getId(), Translator.get("i18n_operation_end") + ": " + operation, Translator.get("i18n_cloud_account") + ": " + resourceWithBLOBs.getPluginName() + "，"
                    + Translator.get("i18n_region") + ": " + resourceWithBLOBs.getRegionName() + "，" + Translator.get("i18n_rule_type") + ": " + resourceWithBLOBs.getResourceType() + "，" + Translator.get("i18n_resource_manage") + ": "
                    + resourceWithBLOBs.getResourceName() + "，" + Translator.get("i18n_resource_manage") + ": " + resourceWithBLOBs.getReturnSum() + "/" + resourceWithBLOBs.getResourcesSum(), true);
        } catch (Exception e) {
            throw e;
        }
        return resourceWithBLOBs;
    }

    public List<ResourceLogDTO> getResourceLog(String resourceId) {
        List<ResourceLogDTO> result = new ArrayList<>();
        try {
            TaskItemResourceExample taskItemResourceExample = new TaskItemResourceExample();
            taskItemResourceExample.createCriteria().andResourceIdEqualTo(resourceId);
            TaskItemResource taskItemResource = taskItemResourceMapper.selectByExample(taskItemResourceExample).get(0);
            TaskItemWithBLOBs taskItem = taskItemMapper.selectByPrimaryKey(taskItemResource.getTaskItemId());
            taskItem.setDetails(null);
            taskItem.setCustomData(null);
            ResourceLogDTO resourceLogDTO = new ResourceLogDTO();
            resourceLogDTO.setTaskItem(taskItem);
            Rule rule = ruleMapper.selectByPrimaryKey(taskItem.getRuleId());
            rule.setScript(null);//没有用到暂时置空，以防止翻译总报错warn
            resourceLogDTO.setRule(rule);
            TaskItemLogExample taskItemLogExample = new TaskItemLogExample();
            taskItemLogExample.createCriteria().andTaskItemIdEqualTo(taskItem.getId()).andResourceIdEqualTo(resourceId);
            taskItemLogExample.setOrderByClause("create_time");
            resourceLogDTO.setTaskItemLogList(taskItemLogMapper.selectByExampleWithBLOBs(taskItemLogExample));
            resourceLogDTO.setResource(resourceMapper.selectByPrimaryKey(resourceId));
            result.add(resourceLogDTO);
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
        }

        return result;
    }

    /**
     * 导出excel
     * @param request
     * @return
     * @throws Exception
     */
    public byte[] export(ExcelExportRequest request) throws Exception {
        Map<String, Object> params = request.getParams();
        ResourceRequest resourceRequest = new ResourceRequest();
        if (MapUtils.isNotEmpty(params)) {
            org.apache.commons.beanutils.BeanUtils.populate(resourceRequest, params);
        }
        List<ExcelExportRequest.Column> columns = request.getColumns();
        List<ResourceDTO> resourceDTOS = search(resourceRequest);
        List<List<Object>> data = resourceDTOS.stream().map(resource -> new ArrayList<Object>() {{
            columns.forEach(column -> {
                try {
                    switch (column.getKey()) {
                        case "cloud_account":
                            add(resource.getPluginName());
                            break;
                        case "severity":
                            add(Translator.get(resource.getSeverity()));
                            break;
                        case "return_sum":
                            add(resource.getResourceWithBLOBs().getReturnSum() + "/" + resource.getResourceWithBLOBs().getResourcesSum());
                            break;
                        case "resource_status":
                            add(Translator.get(resource.getResourceWithBLOBs().getResourceStatus()));
                            break;
                        case "bug_fix":
                            if (resource.getResourceWithBLOBs().getReturnSum() > 0 && resource.getResourceWithBLOBs().getResourceCommandAction().contains("actions")) {
                                add(Translator.get("i18n_fix"));
                            } else if (resource.getResourceWithBLOBs().getReturnSum() == 0 && StringUtils.equals("NotNeedFix", resource.getResourceWithBLOBs().getResourceStatus())) {
                                add(Translator.get("NotNeedFix"));
                            } else if (resource.getResourceWithBLOBs().getReturnSum() > 0 && !resource.getResourceWithBLOBs().getResourceCommandAction().contains("actions")) {
                                add(Translator.get("i18n_no_fix"));
                            } else {
                                add("N/A");
                            }
                            break;
                        default:
                            add(MethodUtils.invokeMethod(resource, "get" + StringUtils.capitalize(ExcelExportUtils.underlineToCamelCase(column.getKey()))));
                            break;
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    LogUtil.error("export resource excel error: ", ExceptionUtils.getStackTrace(e));
                }
            });
        }}).collect(Collectors.toList());
        return ExcelExportUtils.exportExcelData("resource detail", request.getColumns().stream().map(ExcelExportRequest.Column::getValue).collect(Collectors.toList()), data);
    }

    @Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, rollbackFor = {RuntimeException.class, Exception.class})
    public void deleteResourceByAccountId (String accountId) {
        TaskExample example = new TaskExample();
        example.createCriteria().andAccountIdEqualTo(accountId);
        List<Task> tasks = taskMapper.selectByExample(example);
        tasks.forEach(task -> {
            taskService.deleteManualTask(task.getId());
            ScanTaskHistoryExample scanTaskHistoryExample = new ScanTaskHistoryExample();
            scanTaskHistoryExample.createCriteria().andTaskIdEqualTo(task.getId());
            scanTaskHistoryMapper.deleteByExample(scanTaskHistoryExample);
        });
        long current = System.currentTimeMillis();
        long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();//当天00点

        ScanHistoryExample scanHistoryExample = new ScanHistoryExample();
        example.createCriteria().andAccountIdEqualTo(accountId).andCreateTimeEqualTo(zero);
        List<ScanHistory> list = scanHistoryMapper.selectByExample(scanHistoryExample);

        ScanHistory history = new ScanHistory();
        history.setResourcesSum(Long.valueOf(0));
        history.setReturnSum(Long.valueOf(0));
        history.setScanScore(100);
        history.setOperator("System");
        if (list.size() > 0) {
            int id = list.get(0).getId();
            history.setId(id);
            extScanHistoryMapper.updateByExampleSelective(history);
        } else {
            history.setAccountId(accountId);
            history.setCreateTime(zero);
            scanHistoryMapper.insertSelective(history);
        }
    }

    public Map<String, String> reportIso (String accountId, String groupId) {
        return extResourceMapper.reportIso(accountId, groupId);
    }

    public List<Map<String, String>> groups (Map<String, Object> params) {
        return extResourceMapper.groups(params);
    }
}
