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
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.cloudresourcemanager.CloudResourceManager;
import com.google.api.services.cloudresourcemanager.model.Project;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.*;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.RegionInfo;
import com.vmware.vim25.mo.Datacenter;
import io.riskscanner.base.domain.AccountWithBLOBs;
import io.riskscanner.base.domain.Proxy;
import io.riskscanner.base.rs.*;
import io.riskscanner.commons.constants.*;
import io.riskscanner.commons.exception.PluginException;
import io.riskscanner.commons.exception.RSException;
import io.riskscanner.i18n.Translator;
import io.riskscanner.proxy.Request;
import io.riskscanner.proxy.aliyun.AliyunCredential;
import io.riskscanner.proxy.aliyun.AliyunRequest;
import io.riskscanner.proxy.aws.AWSCredential;
import io.riskscanner.proxy.aws.AWSRequest;
import io.riskscanner.proxy.azure.AzureBaseRequest;
import io.riskscanner.proxy.azure.AzureClient;
import io.riskscanner.proxy.azure.AzureCredential;
import io.riskscanner.proxy.gcp.GcpBaseRequest;
import io.riskscanner.proxy.gcp.GcpClient;
import io.riskscanner.proxy.gcp.GcpCredential;
import io.riskscanner.proxy.huawei.ClientUtil;
import io.riskscanner.proxy.huawei.HuaweiCloudCredential;
import io.riskscanner.proxy.nuclei.NucleiCredential;
import io.riskscanner.proxy.openstack.OpenStackCredential;
import io.riskscanner.proxy.openstack.OpenStackRequest;
import io.riskscanner.proxy.openstack.OpenStackUtils;
import io.riskscanner.proxy.tencent.QCloudBaseRequest;
import io.riskscanner.proxy.vsphere.VsphereBaseRequest;
import io.riskscanner.proxy.vsphere.VsphereClient;
import io.riskscanner.proxy.vsphere.VsphereCredential;
import io.riskscanner.proxy.vsphere.VsphereRegion;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.ServiceType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
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
    public final static String vsphere = "fit2cloud-vsphere-plugin";
    public final static String openstack = "fit2cloud-openstack-plugin";
    public final static String gcp = "fit2cloud-gcp-plugin";
    public final static String nuclei = "fit2cloud-nuclei-plugin";

    /**
     * 支持的插件（云平台）
     */
    public final static List<String> getPlugin() {
        return Arrays.asList(aws, azure, aliyun, huawei, tencent, vsphere, openstack, gcp);
    }

    /**
     * azure 所有区域
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
    public final static String fixedCommand(String custodian, String behavior, String dirPath, String fileName, Map<String, String> params) throws Exception {
        String type = params.get("type");
        String region = params.get("region");
        String proxyType = params.get("proxyType");
        String proxyIp = params.get("proxyIp");
        String proxyPort = params.get("proxyPort");
        String proxyName = params.get("proxyName");
        String proxyPassword = params.get("proxyPassword");
        String pre = "";
        String _pok = " ";
        String proxy = "";
        if (StringUtils.isNotEmpty(proxyType)) {
            if (StringUtils.equalsIgnoreCase(proxyType, CloudAccountConstants.ProxyType.Http.toString())) {
                if (StringUtils.isNotEmpty(proxyName)) {
                    proxy = "export http_proxy=http://" + proxyIp + ":" + proxyPassword + "@" + proxyIp + ":" + proxyPort + ";" + "\n";
                } else {
                    proxy = "export http_proxy=http://" + proxyIp + ":" + proxyPort + ";" + "\n";
                }
            } else if (StringUtils.equalsIgnoreCase(proxyType, CloudAccountConstants.ProxyType.Https.toString())) {
                if (StringUtils.isNotEmpty(proxyName)) {
                    proxy = "export https_proxy=http://" + proxyIp + ":" + proxyPassword + "@" + proxyIp + ":" + proxyPort + ";" + "\n";
                } else {
                    proxy = "export https_proxy=http://" + proxyIp + ":" + proxyPort + ";" + "\n";
                }
            }
        } else {
            proxy = "unset http_proxy;" + "\n" +
                    "unset https_proxy;" + "\n";
        }

        switch (type) {
            case aws:
                String awsAccessKey = params.get("accessKey");
                String awsSecretKey = params.get("secretKey");
                if (StringUtils.equalsIgnoreCase(custodian, ScanTypeConstants.prowler.name())) {
                    String defaultConfig = "[default]" + "\n"
                            + "region=" + region + "\n";
                    String defaultCredentials = "[default]" + "\n"
                            + "aws_access_key_id=" + awsAccessKey + "\n"
                            + "aws_secret_access_key=" + awsSecretKey + "\n";
                    CommandUtils.saveAsFile(defaultConfig, TaskConstants.PROWLER_CONFIG_FILE_PATH, "config");
                    CommandUtils.saveAsFile(defaultCredentials, TaskConstants.PROWLER_CONFIG_FILE_PATH, "credentials");
                    String config = ReadFileUtils.readToBuffer(TaskConstants.PROWLER_CONFIG_FILE_PATH + "/config");
                    String credentials = ReadFileUtils.readToBuffer(TaskConstants.PROWLER_CONFIG_FILE_PATH + "/credentials");
                    if (!config.contains(region)) {
                        CommandUtils.commonExecCmdWithResult("echo -e '" + defaultConfig + "' >> " + TaskConstants.PROWLER_CONFIG_FILE_PATH + "/config", TaskConstants.PROWLER_CONFIG_FILE_PATH);
                    }
                    if (!credentials.contains(awsAccessKey) && !credentials.contains(awsSecretKey)) {
                        CommandUtils.commonExecCmdWithResult("echo -e '" + defaultCredentials + "' >> " + TaskConstants.PROWLER_CONFIG_FILE_PATH + "/credentials", TaskConstants.PROWLER_CONFIG_FILE_PATH);
                    }
                    return proxy + "./prowler -g " + (StringUtils.isNotEmpty(fileName) ? fileName : "group1") + " -f " + region + " -s -M text > result.txt";
                }
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
            case openstack:
                String oEndpoint = params.get("endpoint");
                String oUserName = params.get("userName");
                String oPassword = params.get("password");
                String oProjectId = params.get("projectId");
                String oDomainId = params.get("domainId");
                try {
                    String clouds =
                            "clouds:\n" +
                                    "  demo:\n" +
                                    "    region_name: " + region + "\n" +
                                    "    auth:\n" +
                                    "      username: " + oUserName + "\n" +
                                    "      password: " + oPassword + "\n" +
                                    "      project_id: " + oProjectId + "\n" +
                                    "      domain_name: " + oDomainId + "\n" +
                                    "      auth_url: " + oEndpoint + "\n";
                    CommandUtils.saveAsFile(clouds, dirPath, "clouds.yml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case vsphere:
                String vUserName = params.get("vUserName");
                String vPassword = params.get("vPassword");
                String vEndPoint = params.get("vEndPoint");
                pre = "VSPHERE_USERNAME=" + vUserName + " " +
                        "VSPHERE_PASSWORD=" + vPassword + " " +
                        "VSPHERE_ENDPOINT=" + vEndPoint + " " +
                        "VSPHERE_DEFAULT_REGION=" + region + " ";
                break;
            case gcp:
                String credential = params.get("credential");
                try {
                    CommandUtils.commonExecCmdWithResult("export GOOGLE_APPLICATION_CREDENTIALS=" + credential, dirPath);
                    CommandUtils.saveAsFile(credential, dirPath, "google_application_credentials.json");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pre = "GOOGLE_CLOUD_PROJECT=" + region + " ";
                break;
            case nuclei:
                try {
                    String nucleiCredential = params.get("nucleiCredential");
                    CommandUtils.saveAsFile(nucleiCredential, dirPath, "urls.txt");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (behavior.equals("validate")) {
                    return "nuclei -t " + dirPath + "/" + fileName + " -validate";
                }
                return "nuclei -l " + dirPath + "/urls.txt -t " + dirPath + "/" + fileName + " -o " + dirPath + "/result.txt";
        }
        switch (behavior) {
            case "run":
                return proxy + pre +
                        custodian + " " + behavior + " -s " + dirPath + _pok + dirPath + "/" + fileName;
            case "validate":
                return proxy + pre +
                        custodian + " " + behavior + " " + dirPath + "/" + fileName;
            case "--dryrun":
                return proxy + pre +
                        custodian + " " + CommandEnum.run.getCommand() + " " + behavior + " -s " + dirPath + " -c " + dirPath + "/" + fileName;
            case "report":
                return proxy + pre +
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
    public final static Map<String, String> getAccount(AccountWithBLOBs account, String region, Proxy proxy) {
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
            case openstack:
                map.put("type", openstack);
                OpenStackCredential openStackCredential = new Gson().fromJson(account.getCredential(), OpenStackCredential.class);
                map.put("endpoint", openStackCredential.getEndpoint());
                map.put("userName", openStackCredential.getUserName());
                map.put("password", openStackCredential.getPassword());
                map.put("projectId", openStackCredential.getProjectId());
                map.put("domainId", openStackCredential.getDomainId());
                map.put("region", region);
                break;
            case vsphere:
                map.put("type", vsphere);
                VsphereCredential vsphereCredential = new Gson().fromJson(account.getCredential(), VsphereCredential.class);
                map.put("vUserName", vsphereCredential.getvUserName());
                map.put("vPassword", vsphereCredential.getvPassword());
                map.put("vEndPoint", vsphereCredential.getvEndPoint());
                map.put("region", region);
                break;
            case gcp:
                map.put("type", gcp);
                GcpCredential gcpCredential = new Gson().fromJson(account.getCredential(), GcpCredential.class);
                map.put("credential", gcpCredential.getCredentials());
                map.put("region", region);
                break;
            case nuclei:
                map.put("type", nuclei);
                NucleiCredential nucleiCredential = new Gson().fromJson(account.getCredential(), NucleiCredential.class);
                map.put("nucleiCredential", nucleiCredential.getTargetAddress());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + account.getPluginId());
        }
        map.put("proxyType", proxy != null ? proxy.getProxyType() : "");
        map.put("proxyIp", proxy != null ? proxy.getProxyIp() : "");
        map.put("proxyPort", proxy != null ? proxy.getProxyPort() : "");
        map.put("proxyName", proxy != null ? proxy.getProxyName() : "");
        map.put("proxyPassword", proxy != null ? proxy.getProxyPassword() : "");
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
    public static final JSONArray _getRegions(AccountWithBLOBs account, Proxy proxy, boolean flag) {
        try {
            JSONArray jsonArray = new JSONArray();
            if (!flag) {
                LogUtil.error(Translator.get("i18n_ex_plugin_validate"));
                return new JSONArray();
            }
            switch (account.getPluginId()) {
                case aws:
                    AWSRequest awsReq = new AWSRequest();
                    awsReq.setCredential(account.getCredential());
                    AmazonEC2Client client = awsReq.getAmazonEC2Client(proxy);
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
                        jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(region.getRegionName(), aws)) ? tranforRegionId2RegionName(region.getRegionName(), aws) : region.getRegionName());
                        if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                    });
                    break;
                case azure:
                    try {
                        AzureBaseRequest req = new AzureBaseRequest();
                        req.setCredential(account.getCredential());
                        AzureClient azureClient = req.getAzureClient(proxy);
                        //此region 不是chinanorth这种我们诠释的region，而是azure本身的中国区、国际区等概念
                        azureClient.getCloudRegions().forEach(region -> {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", region.get("regionId"));
                            jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(region.get("regionId"), azure)) ? tranforRegionId2RegionName(region.get("regionId"), azure) : region.get("regionName"));
                            if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                        });
                    } catch (Exception e) {
                        LogUtil.error(e.getMessage());
                    }
                    break;
                case aliyun:
                    AliyunRequest req = new AliyunRequest();
                    req.setCredential(account.getCredential());
                    IAcsClient aliyunClient = req.getAliyunClient(proxy);
                    DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
                    describeRegionsRequest.setAcceptFormat(FormatType.JSON);
                    try {
                        DescribeRegionsResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeRegionsRequest);
                        List<DescribeRegionsResponse.Region> regions = describeRegionsResponse.getRegions();
                        regions.forEach(region -> {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", region.getRegionId());
                            jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(region.getRegionId(), aliyun)) ? tranforRegionId2RegionName(region.getRegionId(), aliyun) : region.getLocalName());
                            if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                        });
                    } catch (Exception e) {
                        LogUtil.error(e.getMessage());
                    } finally {
                        aliyunClient.shutdown();
                    }
                    break;
                case huawei:
                    try {
                        BusiRequest busiRequest = new BusiRequest();
                        busiRequest.setCredential(account.getCredential());
                        List<Map<String, String>> regions = getRegions(busiRequest, proxy);
                        IamClient iamClient = ClientUtil.getIamClient(busiRequest, proxy);
                        KeystoneListProjectsRequest request = new KeystoneListProjectsRequest();
                        request.setDomainId(busiRequest.getHuaweiCloudCredential().getDomainId());
                        List<ProjectResult> projects = iamClient.keystoneListProjects(request).getProjects();
                        for (Map<String, String> region : regions) {
                            for (ProjectResult project : projects) {
                                if (project.getName().equalsIgnoreCase(region.get("key"))) {
                                    region.put("projectId", project.getId());
                                }
                            }
                        }
                        for (Map<String, String> region : regions) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", region.get("key"));
                            jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(region.get("key"), huawei)) ? tranforRegionId2RegionName(region.get("key"), huawei) : region.get("value"));
                            jsonObject.put("projectId", region.get("projectId"));
                            if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                        }
                    } catch (RSException | PluginException e) {
                        RSException.throwException(e.getMessage());
                    }
                    break;
                case tencent:
                    try {
                        QCloudBaseRequest request = new QCloudBaseRequest();
                        request.setCredential(account.getCredential());
                        CvmClient cvmClient = request.getCvmClient(proxy);
                        com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest regionsRequest = new com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsRequest();
                        com.tencentcloudapi.cvm.v20170312.models.DescribeRegionsResponse resp = cvmClient.DescribeRegions(regionsRequest);
                        RegionInfo[] regionInfos = resp.getRegionSet();
                        for (RegionInfo regionInfo : regionInfos) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", regionInfo.getRegion());
                            jsonObject.put("regionName", StringUtils.isNotEmpty(tranforRegionId2RegionName(regionInfo.getRegion(), tencent)) ? tranforRegionId2RegionName(regionInfo.getRegion(), tencent) : regionInfo.getRegionName());
                            if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                        }
                    } catch (Exception e) {
                        LogUtil.error(e.getMessage());
                    }
                    break;
                case openstack:
                    try {
                        Request openStackReq = new Request();
                        openStackReq.setCredential(account.getCredential());
                        OpenStackRequest openstackRequest = OpenStackUtils.convert2OpenStackRequest(openStackReq);
                        OSClient.OSClientV3 osClient = openstackRequest.getOpenStackClient();
                        List<? extends org.openstack4j.model.identity.v3.Region> regions = osClient.identity().regions().list();

                        if (OpenStackUtils.isAdmin(osClient)) {
                            if (!CollectionUtils.isEmpty(regions)) {
                                if (OpenStackUtils.isSupport(osClient, ServiceType.BLOCK_STORAGE)) {
                                    regions.forEach(region -> {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("regionId", region.getId());
                                        jsonObject.put("regionName", region.getId());
                                        if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                                    });
                                } else {
                                    regions.forEach(region -> {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("regionId", region.getId());
                                        jsonObject.put("regionName", region.getId());
                                        if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                                    });
                                }
                            } else {
                                String region = OpenStackUtils.getRegion(osClient);
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("regionId", region);
                                jsonObject.put("regionName", region);
                                if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                            }
                        }
                    } catch (Exception e) {
                        throw new PluginException(e.getMessage(), e);
                    }
                    break;
                case vsphere:
                    VsphereClient vsphereClient = null;
                    try {
                        Request vsphereRequest = new Request();
                        vsphereRequest.setCredential(account.getCredential());
                        VsphereBaseRequest vsphereBaseRequest = new VsphereBaseRequest(vsphereRequest);
                        vsphereClient = vsphereBaseRequest.getVsphereClient();
                        List<Datacenter> list = vsphereClient.listDataCenters();
                        List<VsphereRegion> regions = new ArrayList<>();
                        for (Datacenter dc : list) {
                            regions.add(new VsphereRegion(dc.getName()));
                        }
                        for (VsphereRegion vsphereRegion : regions) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", vsphereRegion.getName());
                            jsonObject.put("regionName", vsphereRegion.getName());
                            if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                        }
                    } catch (Exception e) {
                        if (e instanceof PluginException) {
                            throw (PluginException) e;
                        }
                        throw new PluginException(e.getMessage(), e);
                    } finally {
                        if (vsphereClient != null) {
                            vsphereClient.closeConnection();
                        }
                    }
                    break;
                case gcp:
                    try {
                        Request gcpRequest = new Request();
                        gcpRequest.setCredential(account.getCredential());
                        GcpBaseRequest gcpBaseRequest = new GcpBaseRequest(gcpRequest);
                        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

                        InputStream inputStream = new ByteArrayInputStream(gcpBaseRequest.getGcpCredential().getCredentials().getBytes(Charset.forName("UTF-8")));
                        GoogleCredential credential = GoogleCredential.fromStream(inputStream)
                                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
                        CloudResourceManager cloudResourceManagerService = new CloudResourceManager.Builder(httpTransport, jsonFactory, credential)
                                .setApplicationName("Google-CloudResourceManagerSample/0.1")
                                .build();
                        JSONObject gcp = JSON.parseObject(gcpBaseRequest.getGcpCredential().getCredentials());
                        String projectId = gcp.getString("quota_project_id");
                        if (projectId == null) projectId = gcp.getString("project_id");
                        CloudResourceManager.Projects.Get request =
                                cloudResourceManagerService.projects().get(projectId);
                        Project response = request.execute();
                        if (response == null) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("regionId", projectId);
                            jsonObject.put("regionName", projectId);
                            if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                            break;
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("regionId", projectId);
                        jsonObject.put("regionName", response.getName());
                        if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                    } catch (Exception e) {
                        Request gcpRequest = new Request();
                        gcpRequest.setCredential(account.getCredential());
                        GcpBaseRequest gcpBaseRequest = new GcpBaseRequest(gcpRequest);
                        JSONObject gcp = JSON.parseObject(gcpBaseRequest.getGcpCredential().getCredentials());
                        String projectId = gcp.getString("quota_project_id");
                        if (projectId == null) projectId = gcp.getString("project_id");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("regionId", projectId);
                        jsonObject.put("regionName", projectId);
                        if (!jsonArray.contains(jsonObject)) jsonArray.add(jsonObject);
                    }
                    break;
                case nuclei:
                    break;
                default:
                    throw new IllegalStateException("Unexpected regions value{}: " + account.getPluginName());
            }
            return jsonArray;
        } catch (Exception e) {
            throw new RSException(e.getMessage());
        }
    }

    public static boolean validateCredential(AccountWithBLOBs account, Proxy proxy) throws IOException, PluginException {
        switch (account.getPluginId()) {
            case aws:
                try {
                    AWSRequest awsReq = new AWSRequest();
                    awsReq.setCredential(account.getCredential());
                    AmazonEC2Client client = awsReq.getAmazonEC2Client(proxy);
                    String region = null;
                    if (region != null && region.trim().length() > 0) {
                        client.setRegion(RegionUtils.getRegion(region));
                    }
                    try {
                        client.describeRegions();
                    } catch (Exception e) {
                        if (region != null && region.trim().length() > 0) {
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
                    AzureClient azureClient = req.getAzureClient(proxy);
                    return azureClient.getCurrentSubscription() != null;
                } catch (Exception e) {
                    LogUtil.error("Account verification failed : " + e.getMessage());
                    break;
                }
            case aliyun:
                AliyunRequest aliyunRequest = new AliyunRequest();
                aliyunRequest.setCredential(account.getCredential());
                IAcsClient aliyunClient = aliyunRequest.getAliyunClient(proxy);
                try {
                    DescribeRegionsRequest describeRegionsRequest = new DescribeRegionsRequest();
                    describeRegionsRequest.setAcceptFormat(FormatType.JSON);
                    DescribeRegionsResponse describeRegionsResponse = aliyunClient.getAcsResponse(describeRegionsRequest);
                    describeRegionsResponse.getRegions();
                    return true;
                } catch (Exception e) {
                    LogUtil.error("Account verification failed : " + e.getMessage());
                    break;
                } finally {
                    aliyunClient.shutdown();
                }
            case huawei:
                try {
                    HuaweiCloudCredential huaweiCloudCredential = new Gson().fromJson(account.getCredential(), HuaweiCloudCredential.class);
                    IamClient iamClient = ClientUtil.getIamClient(account.getCredential(), proxy);
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
                CvmClient client = req.getCvmClient(proxy);
                try {
                    client.DescribeRegions(request);
                    return true;
                } catch (Exception e) {
                    LogUtil.error("Account verification failed : " + e.getMessage());
                    break;
                }
            case openstack:
                try {
                    Request openStackReq = new Request();
                    openStackReq.setCredential(account.getCredential());
                    OpenStackRequest openStackRequest = OpenStackUtils.convert2OpenStackRequest(openStackReq);
                    return openStackRequest.getOpenStackClient() != null;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PluginException("Failed to valid credential：" + e.getMessage());
                }
            case vsphere:
                VsphereClient vsphereClient = null;
                try {
                    Request vsphereRequest = new Request();
                    vsphereRequest.setCredential(account.getCredential());
                    VsphereBaseRequest vsphereBaseRequest = new VsphereBaseRequest(vsphereRequest);
                    vsphereClient = vsphereBaseRequest.getVsphereClient();
                    if (!vsphereClient.isUseCustomSpec()) {
                        throw new PluginException("This version of vCenter is not supported!");
                    }
                    return true;
                } catch (Exception e) {
                    LogUtil.error("Verify that the account has an error！", e);
                    if (e instanceof PluginException) {
                        throw (PluginException) e;
                    }
                    throw new PluginException("Verify that the account has an error!", e);
                } finally {
                    if (vsphereClient != null) {
                        vsphereClient.closeConnection();
                    }
                }
            case gcp:
                GcpClient gcpClient = null;
                try {
                    Request gcpRequest = new Request();
                    gcpRequest.setCredential(account.getCredential());
                    GcpBaseRequest gcpBaseRequest = new GcpBaseRequest(gcpRequest);
                    gcpClient = gcpBaseRequest.getGcpClient();
                    return gcpClient.authExplicit(gcpBaseRequest.getGcpCredential());
                } catch (Exception e) {
                    throw new PluginException("Verify that the account has an error!", e);
                }
            case nuclei:
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + account.getPluginId());
        }
        return false;
    }

    private static List<Map<String, String>> getRegions(BusiRequest busiRequest, Proxy proxy) throws PluginException {
        try {
            IamClient iamClient = ClientUtil.getIamClient(busiRequest, proxy);
            List<ProjectResult> projectResults = ProjectUtil.listProjects(iamClient, AuthUtil.validate(iamClient, busiRequest.getHuaweiCloudCredential().getAk()).getUserId());
            List<Region> regions = RegionUtil.allRegions(iamClient);
            if (CollectionUtils.isEmpty(projectResults)) {
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
            List<Map<String, String>> collect = endpoints.stream().filter(end -> regionMap.containsKey(end.getRegionId()) && regionIdSets.contains(end.getRegionId())).map(endpoint -> {
                HashMap<String, String> tempRegion = new HashMap<>();
                tempRegion.put("key", endpoint.getRegionId());
                RegionLocales regionLocales = regionMap.get(endpoint.getRegionId());
                String name = Optional.ofNullable(regionLocales.getZhCn()).orElse(regionLocales.getEnUs());
                tempRegion.put("value", name);
                return tempRegion;
            }).distinct().collect(Collectors.toList());
            return collect;
        } catch (Exception e) {
            throw new PluginException(e.getMessage(), e);
        }
    }

    public static String tranforRegionId2RegionName(String strEn, String pluginId) {
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
            case vsphere:
                strCn = strEn;
                break;
            case openstack:
                strCn = strEn;
                break;
            case gcp:
                strCn = strEn;
                break;
            case nuclei:
                strCn = strEn;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + pluginId);
        }
        if (StringUtils.isEmpty(strCn)) return strEn;
        return strCn;
    }

    public static boolean checkAvailableRegion(String pluginId, String resource, String region) {
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
                    stringArray = new String[]{"cn-wulanchabu", "cn-heyuan", "cn-guangzhou", "me-east-1", "cn-nanjing"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "aliyun.mongodb")) {
                    // 不支持aliyun.mongodb资源的区域
                    stringArray = new String[]{"cn-guangzhou", "cn-nanjing"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "aliyun.slb")) {
                    // 不支持aliyun.slb资源的区域
                    stringArray = new String[]{"cn-nanjing"};
                    tempList = Arrays.asList(stringArray);
                    return !tempList.contains(region);
                } else if (StringUtils.contains(resource, "aliyun.ram")) {
                    // 因为ram是无区域资源，所以aliyun.ram资源仅绑定北京区，以防重复数据过多
                    stringArray = new String[]{"cn-beijing"};
                    tempList = Arrays.asList(stringArray);
                    return tempList.contains(region);
                } else if (StringUtils.contains(resource, "aliyun.cdn")) {
                    // 因为cdn是无区域资源，所以aliyun.cdn资源仅绑定北京区，以防重复数据过多
                    stringArray = new String[]{"cn-beijing"};
                    tempList = Arrays.asList(stringArray);
                    return tempList.contains(region);
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
                    // 因为iam是无区域资源，所以huawei.iam资源仅绑定北京区，以防重复数据过多
                    stringArray = new String[]{"cn-north-4"};
                    tempList = Arrays.asList(stringArray);
                    return tempList.contains(region);
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
            case vsphere:
                break;
            case openstack:
                break;
            case gcp:
                break;
            case nuclei:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + pluginId);
        }

        return true;
    }

}
