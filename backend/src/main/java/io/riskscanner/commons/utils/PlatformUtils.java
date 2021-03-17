package io.riskscanner.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeRegionsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.*;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.RegionInfo;
import io.riskscanner.base.rs.*;
import io.riskscanner.base.domain.AccountWithBLOBs;
import io.riskscanner.commons.constants.CommandEnum;
import io.riskscanner.commons.constants.RegionsConstants;
import io.riskscanner.commons.exception.PluginException;
import io.riskscanner.commons.exception.RSException;
import io.riskscanner.i18n.Translator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author maguohao
 * @desc 与云平台相关的公共方法统一在此文件
 */
public class PlatformUtils {

    public final static String aws = "fit2cloud-aws-plugin";
    public final static String azure = "fit2cloud-azure-plugin";
    public final static String aliyun = "fit2cloud-aliyun-plugin";
    public final static String huawei = "fit2cloud-huawei-plugin";
    public final static String tencent = "fit2cloud-qcloud-plugin";

    /**
     * 支持的插件（云平台）
     *
     */
    public final static List<String> getPlugin() {
        return Arrays.asList(aws, azure, aliyun, huawei, tencent);
    }

    /**
     * azure 所有区域
     *
     */
    public final static List<String> getAzureResions() {
        return Arrays.asList("AzureCloud", "AzureChinaCloud", "AzureGermanyCloud", "AzureUSGov");
    }

    /**
     * 需要执行的custodian命令
     *
     * @param custodian
     * @param behavior
     * @param dirPath
     * @param fileName
     * @param params
     * @return
     */
    public final static String fixedCommand(String custodian, String behavior, String dirPath, String fileName, Map<String, String> params) {
        String type = params.get("type");
        String region = params.get("region");
        String pre = "";
        String _pok = " ";
        switch (type) {
            case aws:
                String awsAccessKey = params.get("accessKey");
                String awsSecretKey = params.get("secretKey");
                pre = "AWS_ACCESS_KEY_ID=" + awsAccessKey + " " +
                        "AWS_SECRET_ACCESS_KEY=" + awsSecretKey + " " +
                        "AWS_DEFAULT_REGION=" + region + " ";
                break;
            case azure:
                String tenant = params.get("tenant");
                String subscriptionId = params.get("subscription");
                String client = params.get("client");
                String key = params.get("key");
                pre = "AZURE_TENANT_ID=" + tenant + " " +
                        "AZURE_SUBSCRIPTION_ID=" + subscriptionId + " " +
                        "AZURE_CLIENT_ID=" + client + " " +
                        "AZURE_CLIENT_SECRET=" + key + " ";
                _pok = " --region=" + region + " ";
                break;
            case aliyun:
                String aliAccessKey = params.get("accessKey");
                String aliSecretKey = params.get("secretKey");
                pre = "ALIYUN_ACCESSKEYID=" + aliAccessKey + " " +
                        "ALIYUN_ACCESSSECRET=" + aliSecretKey + " " +
                        "ALIYUN_DEFAULT_REGION=" + region + " ";
                break;
            case huawei:
                String huaweiAccessKey = params.get("ak");
                String huaweiSecretKey = params.get("sk");
                String projectId = params.get("projectId");
                pre = "HUAWEI_AK=" + huaweiAccessKey + " " +
                        "HUAWEI_SK=" + huaweiSecretKey + " " +
                        "HUAWEI_PROJECT=" + projectId + " " +
                        "HUAWEI_DEFAULT_REGION=" + region + " ";
                break;
            case tencent:
                String qSecretId = params.get("secretId");
                String qSecretKey = params.get("secretKey");
                pre = "TENCENT_SECRETID=" + qSecretId + " " +
                        "TENCENT_SECRETKEY=" + qSecretKey + " " +
                        "TENCENT_DEFAULT_REGION=" + region + " ";
                break;
        }
        switch (behavior) {
            case "run":
                return pre +
                        custodian + " " + behavior + " -s " + dirPath + _pok + dirPath + "/" + fileName;
            case "validate":
                return pre +
                        custodian + " " + behavior + " " + dirPath + "/" + fileName;
            case "--dryrun":
                return pre +
                        custodian + " " + CommandEnum.run.getCommand() + " " + behavior + " -s " + dirPath + " -c " + dirPath + "/" + fileName;
            case "report":
                return pre +
                        custodian + " " + behavior + " -s " + dirPath + "/" + fileName;
            default:
                throw new IllegalStateException("Unexpected value: " + behavior);
        }
    }

    /**
     * 获取云平台相关参数
     *
     * @param account
     * @param region
     * @return
     */
    public final static Map<String, String> getAccount(AccountWithBLOBs account, String region) {
        Map<String, String> map = new HashMap<>();
        switch (account.getPluginId()) {
            case aws:
                map.put("type", aws);
                AWSCredential awsCredential = new Gson().fromJson(account.getCredential(), AWSCredential.class);
                map.put("accessKey", awsCredential.getAccessKey());
                map.put("secretKey", awsCredential.getSecretKey());
                map.put("region", region);
                break;
            case azure:
                map.put("type", azure);
                AzureCredential azureCredential = new Gson().fromJson(account.getCredential(), AzureCredential.class);
                map.put("client", azureCredential.getClient());
                map.put("key", azureCredential.getKey());
                map.put("subscription", azureCredential.getSubscription());
                map.put("tenant", azureCredential.getTenant());
                map.put("region", region);
                break;
            case aliyun:
                map.put("type", aliyun);
                AliyunCredential aliyunCredential = new Gson().fromJson(account.getCredential(), AliyunCredential.class);
                map.put("accessKey", aliyunCredential.getAccessKey());
                map.put("secretKey", aliyunCredential.getSecretKey());
                map.put("region", region);
                break;
            case huawei:
                map.put("type", huawei);
                HuaweiCloudCredential huaweiCloudCredential = new Gson().fromJson(account.getCredential(), HuaweiCloudCredential.class);
                map.put("ak", huaweiCloudCredential.getAk());
                map.put("sk", huaweiCloudCredential.getSk());
                JSONArray jsonArray = JSON.parseArray(account.getRegions());
                List arr = jsonArray.stream().filter(str -> StringUtils.equals(region, JSON.parseObject(str.toString()).get("regionId").toString())).collect(Collectors.toList());
                map.put("projectId", JSON.parseObject(arr.get(0).toString()).get("projectId").toString());
                map.put("region", region);
                break;
            case tencent:
                map.put("type", tencent);
                Credential tencentCredential = new Gson().fromJson(account.getCredential(), Credential.class);
                map.put("secretId", tencentCredential.getSecretId());
                map.put("secretKey", tencentCredential.getSecretKey());
                map.put("region", region);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + account.getPluginId());
        }
        return map;
    }

    /**
     * 获取云平台（云账号）的regions
     *
     * @param account
     * @param flag
     * @return
     * @throws ClientException
     */
    public static final JSONArray _getRegions(AccountWithBLOBs account, boolean flag) throws ClientException {
        try {
            JSONArray jsonArray = new JSONArray();
            if (!flag) {
                LogUtil.error(Translator.get("i18n_ex_plugin_validate"));
                return new JSONArray();
            }
            switch (account.getPluginId()) {
                case aws:
                    AWSCredential awsCredential = new Gson().fromJson(account.getCredential(), AWSCredential.class);
                    AmazonEC2Client client = new AmazonEC2Client(new BasicAWSCredentials(awsCredential.getAccessKey(), awsCredential.getSecretKey()));
                    DescribeRegionsResult result;
                    try {
                        client.setRegion(RegionUtils.getRegion("cn-north-1"));
                        result = client.describeRegions();
                    } catch (Exception e) {
                        client.setRegion(RegionUtils.getRegion("ap-northeast-1"));
                        result = client.describeRegions();
                    }
                    result.getRegions().forEach(region -> {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("regionId", region.getRegionName());
                        jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(region.getRegionName(), aws))?tranforRegionId2RegionName(region.getRegionName(), aws):region.getRegionName());
                        jsonArray.add(jsonObject);
                    });
                    break;
                case azure:
                    try {
                        AzureBaseRequest req = new AzureBaseRequest();
                        req.setCredential(account.getCredential());
                        AzureClient azureClient = req.getAzureClient();
                        //此region 不是chinanorth这种我们诠释的region，而是azure本身的中国区、国际区等概念
                        azureClient.getCloudRegions().forEach(region -> {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", region.get("regionId"));
                            jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(region.get("regionId"), azure))?tranforRegionId2RegionName(region.get("regionId"), azure):region.get("regionName"));
                            jsonArray.add(jsonObject);
                        });
                    } catch (Exception e) {
                        LogUtil.error(e.getMessage());
                    }
                    break;
                case aliyun:
                    AliyunRequest req = new AliyunRequest();
                    req.setCredential(account.getCredential());
                    IAcsClient aliyunClient = req.getAliyunClient();
                    DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
                    describeRegionsRequest.setAcceptFormat(FormatType.JSON);
                    try {
                        DescribeRegionsResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeRegionsRequest);
                        List<DescribeRegionsResponse.Region> regions = describeRegionsResponse.getRegions();
                        regions.forEach(region -> {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", region.getRegionId());
                            jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(region.getRegionId(), aliyun))?tranforRegionId2RegionName(region.getRegionId(), aliyun):region.getLocalName());
                            jsonArray.add(jsonObject);
                        });
                    } finally {
                        aliyunClient.shutdown();
                    }
                    break;
                case huawei:
                    try {
                        BusiRequest busiRequest = new BusiRequest();
                        busiRequest.setCredential(account.getCredential());
                        List<Map<String, String>> regions = getRegions(busiRequest);
                        IamClient iamClient = ClientUtil.getIamClient(busiRequest);
                        KeystoneListProjectsRequest request = new KeystoneListProjectsRequest();
                        request.setDomainId(busiRequest.getHuaweiCloudCredential().getDomainId());
                        List<ProjectResult> projects = iamClient.keystoneListProjects(request).getProjects();
                        for (Map<String, String> region : regions) {
                            for (ProjectResult project : projects) {
                                if(project.getName().equalsIgnoreCase(region.get("key"))){
                                    region.put("projectId", project.getId());
                                }
                            }
                        }
                        for (Map<String, String> region : regions) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", region.get("key"));
                            jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(region.get("key"), huawei))?tranforRegionId2RegionName(region.get("key"), huawei):region.get("value"));
                            jsonObject.put("projectId", region.get("projectId"));
                            jsonArray.add(jsonObject);
                        }
                    } catch (RSException | PluginException e) {
                        RSException.throwException(e.getMessage());
                    }
                    break;
                case tencent:
                    try {
                        QCloudBaseRequest request = new QCloudBaseRequest();
                        request.setCredential(account.getCredential());
                        CvmClient cvmClient = request.getCvmClient();
                        com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest regionsRequest = new com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest();
                        com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsResponse resp = cvmClient.DescribeRegions(regionsRequest);
                        RegionInfo[] regionInfos = resp.getRegionSet();
                        for (RegionInfo regionInfo : regionInfos) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", regionInfo.getRegion());
                            jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(regionInfo.getRegion(), tencent))?tranforRegionId2RegionName(regionInfo.getRegion(), tencent):regionInfo.getRegionName());
                            jsonArray.add(jsonObject);
                        }
                    } catch (Exception e) {
                        LogUtil.error(e.getMessage());
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected regions value{}: " + account.getPluginName());
            }
            return jsonArray;
        } catch (Exception e) {
            throw new RSException(e.getMessage());
        }
    }

    public static boolean validateCredential (AccountWithBLOBs account) {
        switch (account.getPluginId()) {
            case aws:
                try {
                    AWSRequest awsReq = new AWSRequest();
                    awsReq.setCredential(account.getCredential());
                    AmazonEC2Client client = awsReq.getAmazonEC2Client();
                    String region = null;
                    if (!region.isEmpty() && region.trim().length() > 0) {
                        client.setRegion(RegionUtils.getRegion(region));
                    }
                    try {
                        client.describeRegions();
                    } catch (Exception e) {
                        if (!region.isEmpty() && region.trim().length() > 0) {
                            if (e instanceof AmazonServiceException) {
                                String errCode = ((AmazonServiceException) e).getErrorCode();
                                if ("AuthFailure".equals(errCode)) {
                                    return false;
                                }
                            } else if (e instanceof AmazonClientException) {
                                String errMsg = e.getMessage();
                                if ("Unable to execute HTTP request: Connection refused".equals(errMsg)) {
                                    return false;
                                }
                            }
                        }
                        client.setRegion(RegionUtils.getRegion("cn-north-1"));
                        client.describeRegions();
                    }
                    return true;
                } catch (Exception e) {
                    LogUtil.error("Account verification failed : " + e.getMessage());
                    break;
                }
            case azure:
                try {
                    AzureBaseRequest req = new AzureBaseRequest();
                    req.setCredential(account.getCredential());
                    AzureClient azureClient = req.getAzureClient();
                    return azureClient.getCurrentSubscription() != null;
                } catch (Exception e) {
                    LogUtil.error("Account verification failed : " + e.getMessage());
                    break;
                }
            case aliyun:
                try {
                    AliyunRequest aliyunRequest = new AliyunRequest();
                    aliyunRequest.setCredential(account.getCredential());
                    IAcsClient aliyunClient = aliyunRequest.getAliyunClient();
                    DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
                    describeRegionsRequest.setAcceptFormat(FormatType.JSON);
                    DescribeRegionsResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeRegionsRequest);
                    describeRegionsResponse.getRegions();
                    aliyunClient.shutdown();
                    return true;
                } catch (Exception e) {
                    LogUtil.error("Account verification failed : " + e.getMessage());
                    break;
                }
            case huawei:
                try {
                    HuaweiCloudCredential huaweiCloudCredential = new Gson().fromJson(account.getCredential(), HuaweiCloudCredential.class);
                    IamClient iamClient = ClientUtil.getIamClient(account.getCredential());
                    assert iamClient != null;
                    ShowCredential showCredential = AuthUtil.validate(iamClient, huaweiCloudCredential.getAk());
                    return null != showCredential;
                } catch (Exception e) {
                    LogUtil.error("Account verification failed : " + e.getMessage());
                    break;
                }
            case tencent:
                com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest request = new com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest();
                QCloudBaseRequest req = new QCloudBaseRequest();
                req.setCredential(account.getCredential());
                CvmClient client = req.getCvmClient();
                try {
                    client.DescribeRegions(request);
                    return true;
                } catch (Exception e) {
                    LogUtil.error("Account verification failed : " + e.getMessage());
                    break;
                }
            default:
                throw new IllegalStateException("Unexpected value: " + account.getPluginId());
        }
        return false;
    }

    private static List<Map<String, String>> getRegions(BusiRequest busiRequest) throws PluginException {
        try {
            IamClient iamClient = ClientUtil.getIamClient(busiRequest);
            List<ProjectResult> projectResults = ProjectUtil.listProjects(iamClient, AuthUtil.validate(iamClient, busiRequest.getHuaweiCloudCredential().getAk()).getUserId());
            List<Region> regions = RegionUtil.allRegions(iamClient);
            if (CollectionUtils.isEmpty(projectResults)){
                List<Map<String, String>> temp = new ArrayList<>();
                for (Region tmp : regions) {//获取全部的Region，带中文
                    Map<String, String> region = new HashMap<>();
                    region.put("key", tmp.getId());

                    List<RegionLocales> l = new ArrayList<>();
                    l.add(tmp.getLocales());
                    String name = RegionUtil.getZHCNName(l);
                    region.put("value", (Strings.isNullOrEmpty(name)) ? tmp.getId() : name);
                    temp.add(region);
                }
                return temp;
            }
            Set<String> regionIdSets = projectResults.stream().map(ProjectResult::getName).collect(Collectors.toSet());

            Map<String, RegionLocales> regionMap = regions.stream().collect(Collectors.toMap(Region::getId, Region::getLocales));
            List<Service> services = ServiceUtil.listServices(iamClient);
            List<Service> myServices = services.stream().filter(svc -> StringUtils.equalsAny(svc.getType(), "compute", "nova")).collect(Collectors.toList());
            List<Endpoint> endpoints = new ArrayList<>();
            myServices.forEach(svc -> {
                KeystoneListEndpointsResponse keystoneListEndpointsResponse = iamClient.keystoneListEndpoints(new KeystoneListEndpointsRequest().withServiceId(svc.getId()));
                endpoints.addAll(keystoneListEndpointsResponse.getEndpoints());
            });
            List<Map<String, String>> collect = endpoints.stream().filter(end-> regionMap.containsKey(end.getRegionId()) && regionIdSets.contains(end.getRegionId())).map(endpoint ->{
                HashMap<String, String> tempRegion = new HashMap<>();
                tempRegion.put("key",endpoint.getRegionId());
                RegionLocales regionLocales = regionMap.get(endpoint.getRegionId());
                String name = Optional.ofNullable(regionLocales.getZhCn()).orElse(regionLocales.getEnUs());
                tempRegion.put("value",name);
                return tempRegion;
            }).distinct().collect(Collectors.toList());
            return collect;
        } catch (Exception e) {
            throw new PluginException(e.getMessage(), e);
        }
    }

    public static String tranforRegionId2RegionName (String strEn, String pluginId) {
        String strCn;
        switch (pluginId) {
            case aws:
                strCn = RegionsConstants.AwsMap.get(strEn);
                break;
            case azure:
                strCn = RegionsConstants.AzureMap.get(strEn);
                break;
            case aliyun:
                strCn = RegionsConstants.AliyunMap.get(strEn);
                break;
            case huawei:
                strCn = RegionsConstants.HuaweiMap.get(strEn);
                break;
            case tencent:
                strCn = RegionsConstants.TencentMap.get(strEn);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + pluginId);
        }
        if (StringUtils.isEmpty(strCn)) return strEn;
        return strCn;
    }

    public static boolean checkAvailableRegion (String pluginId, String resource, String region) {
        String[] stringArray;
        List<String> tempList;
        switch (pluginId) {
            case aws:
                break;
            case azure:
                break;
            case aliyun:
                if (StringUtils.contains(resource, "aliyun.polardb")) {
                    // 不支持aliyun.polardb资源的区域
                    stringArray = new String[]{"cn-wulanchabu", "cn-heyuan", "cn-guangzhou", "me-east-1"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "aliyun.mongodb")) {
                    // 不支持aliyun.mongodb资源的区域
                    stringArray = new String[]{"cn-guangzhou"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                }
                break;
            case huawei:
                if (StringUtils.contains(resource, "huawei.vpc")) {
                    stringArray = new String[]{"cn-northeast-1", "la-south-2", "sa-brazil-1", "na-mexico-1"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "huawei.security-group")) {
                    stringArray = new String[]{"cn-northeast-1", "la-south-2", "sa-brazil-1", "na-mexico-1"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "huawei.iam")) {
                    stringArray = new String[]{"la-south-2", "sa-brazil-1", "na-mexico-1"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "huawei.rds")) {
                    stringArray = new String[]{"la-south-2", "sa-brazil-1", "na-mexico-1"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "huawei.dds")) {
                    stringArray = new String[]{"la-south-2", "sa-brazil-1", "na-mexico-1"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "huawei.redis")) {
                    stringArray = new String[]{"la-south-2", "sa-brazil-1", "na-mexico-1"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "huawei.elb")) {
                    stringArray = new String[]{"la-south-2", "sa-brazil-1", "na-mexico-1"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "huawei.eip")) {
                    stringArray = new String[]{"la-south-2", "sa-brazil-1", "na-mexico-1"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                }
                break;
            case tencent:
                // 不支持资源的区域
                stringArray = new String[]{"ap-shanghai-fsi", "ap-shenzhen-fsi"};
                tempList = Arrays.asList(stringArray);
                // 利用list的包含方法,进行判断
                return !tempList.contains(region);
            default:
        }
        return true;
    }

}
