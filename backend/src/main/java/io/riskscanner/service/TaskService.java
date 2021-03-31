package io.riskscanner.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fit2cloud.quartz.service.QuartzManageService;
import io.riskscanner.base.domain.*;
import io.riskscanner.base.mapper.*;
import io.riskscanner.base.mapper.ext.ExtTaskMapper;
import io.riskscanner.commons.constants.CommandEnum;
import io.riskscanner.commons.constants.ResourceOperation;
import io.riskscanner.commons.constants.ResourceTypeConstants;
import io.riskscanner.commons.constants.TaskConstants;
import io.riskscanner.commons.exception.RSException;
import io.riskscanner.commons.utils.*;
import io.riskscanner.dto.QuartzTaskDTO;
import io.riskscanner.i18n.Translator;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author maguohao
 */
@Service
public class TaskService {

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TaskItemMapper taskItemMapper;
    @Resource
    private ExtTaskMapper extTaskMapper;
    @Resource
    private TaskItemLogMapper taskItemLogMapper;
    @Resource
    private TaskItemResourceMapper taskItemResourceMapper;
    @Resource
    private AccountMapper accountMapper;
    @Resource
    private QuartzManageService quartzManageService;
    @Resource
    private AccountService accountService;
    @Resource
    private ResourceRuleMapper resourceRuleMapper;
    @Resource
    private ResourceMapper resourceMapper;
    @Resource
    private OrderService orderService;
    @Resource
    private ResourceItemMapper resourceItemMapper;

    public Task saveManualTask(QuartzTaskDTO quartzTaskDTO, String messageOrderId) {
        try {

            this.validateYaml(quartzTaskDTO);
            return orderService.createTask(quartzTaskDTO, TaskConstants.TASK_STATUS.APPROVED.name(), messageOrderId);
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw new RSException(e.getMessage());
        }
    }

    public boolean morelTask(String taskId)  {
        try {
            Task task = taskMapper.selectByPrimaryKey(taskId);
            if (task != null) {
                task.setStatus(TaskConstants.TASK_STATUS.APPROVED.name());
                taskMapper.updateByPrimaryKeySelective(task);
            } else {
                RSException.throwException("Task not found");
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw e;
        }
        return true;
    }

    public boolean validateYaml (QuartzTaskDTO quartzTaskDTO) {
        try {
            String script = quartzTaskDTO.getScript();
            JSONArray jsonArray = JSON.parseArray(quartzTaskDTO.getParameter());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String key = "${{" + jsonObject.getString("key") + "}}";
                if (script.contains(key)) {
                    script = script.replace(key, jsonObject.getString("defaultValue"));
                }
            }
            Yaml yaml = new Yaml();
            Map map = yaml.load(script);
            if (map != null) {
                List<Map> list = (List) map.get("policies");
                for (Map m : list) m.get("resource").toString();
            }
        } catch (Exception e) {
            RSException.throwException(Translator.get("i18n_compliance_rule_code_error"));
        }
        return true;
    }

    public void deleteManualTask(String taskId) {
        Task task = taskMapper.selectByPrimaryKey(taskId);
        TaskItemExample taskItemExample = new TaskItemExample();
        taskItemExample.createCriteria().andTaskIdEqualTo(task.getId());
        List<TaskItem> taskItemList = taskItemMapper.selectByExample(taskItemExample);
        try {
            taskItemList.forEach(taskItem -> {
                if (taskItem == null) return;
                taskItemMapper.deleteByPrimaryKey(taskItem.getId());

                TaskItemLogExample taskItemLogExample = new TaskItemLogExample();
                taskItemLogExample.createCriteria().andTaskItemIdEqualTo(taskItem.getId());
                taskItemLogMapper.deleteByExample(taskItemLogExample);

                TaskItemResourceExample taskItemResourceExample = new TaskItemResourceExample();
                taskItemResourceExample.createCriteria().andTaskItemIdEqualTo(taskItem.getId());
                List<TaskItemResource> taskItemResources = taskItemResourceMapper.selectByExample(taskItemResourceExample);
                taskItemResourceMapper.deleteByExample(taskItemResourceExample);

                taskItemResources.forEach(taskItemResource -> {
                    if (taskItemResource == null) return;
                    resourceMapper.deleteByPrimaryKey(taskItemResource.getResourceId());

                    if (taskItemResource.getResourceId()!= null) {
                        ResourceItemExample resourceItemExample = new ResourceItemExample();
                        resourceItemExample.createCriteria().andResourceIdEqualTo(taskItemResource.getResourceId());
                        List<ResourceItem> resourceItems = resourceItemMapper.selectByExample(resourceItemExample);
                        resourceItems.forEach(resourceItem -> {
                            ResourceRuleExample resourceRuleExample = new ResourceRuleExample();
                            if (resourceItem.getResourceId() != null) {
                                resourceRuleExample.createCriteria().andResourceIdEqualTo(resourceItem.getResourceId());
                                resourceRuleMapper.deleteByExample(resourceRuleExample);
                            }
                        });
                    }
                });

            });
            taskMapper.deleteByPrimaryKey(task.getId());
            OperationLogService.log(SessionUtils.getUser(), taskId, task.getDescription(), ResourceTypeConstants.TASK.name(), ResourceOperation.DELETE, "删除任务");
        } catch (Exception e) {
            LogUtil.error("Delete manual task error{} " + e.getMessage());
            RSException.throwException(e.getMessage());
        }
    }

    public boolean dryRun(QuartzTaskDTO quartzTaskDTO) {
        //validate && dryrun
        String uuid = UUIDUtil.newUUID();
        try {
            String script = quartzTaskDTO.getScript();
            JSONArray jsonArray = JSON.parseArray(quartzTaskDTO.getParameter());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String key = "${{" + jsonObject.getString("key") + "}}";
                if (script.contains(key)) {
                    script = script.replace(key, jsonObject.getString("defaultValue"));
                }
            }
            final String finalScript = script;
            String dirPath;
            AccountExample example = new AccountExample();
            example.createCriteria().andPluginIdEqualTo(quartzTaskDTO.getPluginId()).andStatusEqualTo("VALID");
            AccountWithBLOBs account = accountMapper.selectByExampleWithBLOBs(example).get(0);
            JSONObject regionObj = (JSONObject) PlatformUtils._getRegions(account, accountService.validate(account.getId())).get(0);
            Map<String, String> map = PlatformUtils.getAccount(account, regionObj.get("regionId").toString());
            dirPath = CommandUtils.saveAsFile(finalScript, uuid);
            String command = PlatformUtils.fixedCommand(CommandEnum.custodian.getCommand(), CommandEnum.validate.getCommand(), dirPath, "policy.yml", map);
            String resultStr = CommandUtils.commonExecCmdWithResult(command, dirPath);
            if (!resultStr.isEmpty() && !resultStr.contains("INFO")) {
                LogUtil.error(Translator.get("i18n_compliance_rule_error")+ " {validate}:" + resultStr);
                RSException.throwException(Translator.get("i18n_compliance_rule_error"));
            }
            String command2 = PlatformUtils.fixedCommand(CommandEnum.custodian.getCommand(), CommandEnum.dryrun.getCommand(), dirPath, "policy.yml", map);
            String resultStr2 = CommandUtils.commonExecCmdWithResult(command2, dirPath);
            if (!resultStr.isEmpty() && !resultStr2.contains("INFO")) {
                LogUtil.error(Translator.get("i18n_compliance_rule_error")+ " {dryrun}:" + resultStr);
                RSException.throwException(Translator.get("i18n_compliance_rule_error"));
            }
        } catch (Exception e) {
            LogUtil.error("[{}] validate && dryrun generate policy.yml file failed:{}", uuid, e.getMessage());
            RSException.throwException(Translator.get("i18n_compliance_rule_error"));
        }
        return true;
    }

    public List<Task> selectQuartzTasks(Map<String, Object> params) {

        TaskExample example = new TaskExample();
        TaskExample.Criteria criteria = example.createCriteria();
        if (params.get("name") != null && StringUtils.isNotEmpty(params.get("name").toString())) {
            criteria.andTaskNameLike("%" + params.get("name").toString() + "%");
        }
        if (params.get("type") != null && StringUtils.isNotEmpty(params.get("type").toString())) {
            criteria.andTypeEqualTo(params.get("type").toString());
        }
        if (params.get("accountId") != null && StringUtils.isNotEmpty(params.get("accountId").toString())) {
            criteria.andAccountIdEqualTo(params.get("accountId").toString());
        }
        if (params.get("cron") != null && StringUtils.isNotEmpty(params.get("cron").toString())) {
            criteria.andCronLike(params.get("cron").toString());
        }
        if (params.get("status") != null && StringUtils.isNotEmpty(params.get("status").toString())) {
            criteria.andStatusEqualTo(params.get("status").toString());
        }
        if (params.get("severity") != null && StringUtils.isNotEmpty(params.get("severity").toString())) {
            criteria.andSeverityEqualTo(params.get("severity").toString());
        }
        if (params.get("pluginName") != null && StringUtils.isNotEmpty(params.get("pluginName").toString())) {
            criteria.andPluginNameEqualTo(params.get("pluginName").toString());
        }
        if (params.get("ruleTag") != null && StringUtils.isNotEmpty(params.get("ruleTag").toString())) {
            criteria.andRuleTagsLike("%" + params.get("ruleTag").toString() + "%");
        }
        if (params.get("resourceType") != null && StringUtils.isNotEmpty(params.get("resourceType").toString())) {
            criteria.andResourceTypesLike("%" + params.get("resourceType").toString() + "%");
        }
        example.setOrderByClause("FIELD(`status`, 'PROCESSING', 'APPROVED', 'FINISHED', 'WARNING', 'ERROR'), return_sum desc, create_time desc");
        return taskMapper.selectByExample(example);
    }

    public boolean saveQuartzTask(QuartzTaskDTO quartzTaskDTO) throws Exception {
        try {
            this.validateYaml(quartzTaskDTO);
            Task task = orderService.createTask(quartzTaskDTO, TaskConstants.TASK_STATUS.RUNNING.name(), null);
            Trigger trigger = addQuartzTask(task);
            task.setLastFireTime(trigger.getNextFireTime().getTime());
            if (trigger.getPreviousFireTime() != null) task.setPrevFireTime(trigger.getPreviousFireTime().getTime());
            task.setTriggerId("quartz-task" + task.getId());
            taskMapper.updateByPrimaryKeySelective(task);
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            throw e;
        }
        return true;
    }


    private Trigger addQuartzTask(Task quartzTask) throws Exception {
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("quartz-task" + quartzTask.getId())
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzTask.getCron()).withMisfireHandlingInstructionDoNothing())
                .build();

        quartzManageService.addJob("orderService", "createQuartzTask", trigger, quartzTask.getId());
        if (quartzTask.getStatus().equalsIgnoreCase(QuartzTaskStatus.PAUSE)) {
            quartzManageService.pauseJob(trigger.getJobKey());
        }
        return quartzManageService.getTrigger(trigger.getKey());

    }


    public void reAddQuartzOnStart() {
        TaskExample example = new TaskExample();
        example.createCriteria().andTypeEqualTo(TaskConstants.TaskType.quartz.name());
        List<Task> quartzTasks = taskMapper.selectByExample(example);
        quartzTasks.forEach(quartzTask -> {
            try {
                if (quartzManageService.getTrigger(new TriggerKey(quartzTask.getTriggerId())) == null) {
                    Trigger trigger = addQuartzTask(getResources(quartzTask.getId()));
                    quartzTask.setLastFireTime(trigger.getNextFireTime().getTime());
                    if (trigger.getPreviousFireTime() != null)
                        quartzTask.setPrevFireTime(trigger.getPreviousFireTime().getTime());
                    quartzTask.setTriggerId("quartz-task" + quartzTask.getId());
                    taskMapper.updateByPrimaryKeySelective(quartzTask);
                }
            } catch (Exception e) {
                RSException.throwException(e);
            }
        });
    }

    public Task getResources(String quartzTaskId) {
        return taskMapper.selectByPrimaryKey(quartzTaskId);
    }

    public void syncTriggerTime() {
        TaskExample example = new TaskExample();
        example.createCriteria().andTypeEqualTo(TaskConstants.TaskType.quartz.name());
        List<Task> quartzTasks = taskMapper.selectByExample(example);
        quartzTasks.forEach(quartzTask -> {
            Trigger trigger = null;
            try {
                trigger = quartzManageService.getTrigger(new TriggerKey(quartzTask.getTriggerId()));
            } catch (Exception e) {
                RSException.throwException(e);
            }
            quartzTask.setLastFireTime(trigger.getNextFireTime().getTime());
            if (trigger.getPreviousFireTime() != null) quartzTask.setPrevFireTime(trigger.getPreviousFireTime().getTime());
            taskMapper.updateByPrimaryKeySelective(quartzTask);

        });

    }

    public void syncTaskSum() {
        TaskExample example = new TaskExample();
        example.createCriteria().andStatusIn(Arrays.asList(TaskConstants.TASK_STATUS.FINISHED.name(), TaskConstants.TASK_STATUS.RUNNING.name()));
        List<Task> tasks = taskMapper.selectByExample(example);
        tasks.forEach(task -> {
            if (task.getResourcesSum() != null && task.getReturnSum() != null) {
                int resourceSum = extTaskMapper.getResourceSum(task.getId());
                int returnSum = extTaskMapper.getReturnSum(task.getId());
                task.setResourcesSum((long) resourceSum);
                task.setReturnSum((long) returnSum);
                taskMapper.updateByPrimaryKeySelective(task);
            }
        });
    }

    public void changeQuartzStatus(String quartzId, final String action) throws Exception {
        Task quartzTask = taskMapper.selectByPrimaryKey(quartzId);
        String triggerId = quartzTask.getTriggerId();
        Trigger trigger = quartzManageService.getTrigger(new TriggerKey(triggerId));
        String taskStatus = null;
        switch (action) {
            case QuartzTaskAction.PAUSE:
                quartzManageService.pauseJob(trigger.getJobKey());
                taskStatus = QuartzTaskStatus.PAUSE;
                break;

            case QuartzTaskAction.RESUME:
                quartzManageService.resumeJob(trigger.getJobKey());
                taskStatus = QuartzTaskStatus.RUNNING;
                break;
            default:
                RSException.throwException("action is not invalid");
        }
        quartzTask.setStatus(taskStatus);
        taskMapper.updateByPrimaryKeySelective(quartzTask);
    }


    public void deleteQuartzTask(String quartzTaskId) {
        Task quartzTask = taskMapper.selectByPrimaryKey(quartzTaskId);
        String triggerId = quartzTask.getTriggerId();
        Trigger trigger;
        try {
            trigger = quartzManageService.getTrigger(new TriggerKey(triggerId));
            quartzManageService.deleteJob(trigger.getJobKey());
        } catch (Exception e) {
            LogUtil.warn("Scheduled task not found！");
        } finally {
            this.deleteManualTask(quartzTaskId);
        }
    }

}

class QuartzTaskStatus {
    protected final static String ERROR = "ERROR";
    protected final static String PAUSE = "PAUSE";
    protected final static String RUNNING = "RUNNING";
}

class QuartzTaskAction {
    protected final static String PAUSE = "pause";
    protected final static String RESUME = "resume";
}

