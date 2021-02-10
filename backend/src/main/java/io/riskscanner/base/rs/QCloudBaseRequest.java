package io.riskscanner.base.rs;

import com.google.gson.Gson;
import com.tencentcloudapi.cbs.v20170312.CbsClient;
import com.tencentcloudapi.clb.v20180317.ClbClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.monitor.v20180724.MonitorClient;
import com.tencentcloudapi.vpc.v20170312.VpcClient;
import io.riskscanner.commons.exception.PluginException;
import io.riskscanner.commons.exception.RSException;
import org.apache.commons.lang3.StringUtils;

public class QCloudBaseRequest extends Request {
    private static String DEFAULT_REGION = "ap-guangzhou";

    public QCloudBaseRequest() {
        super("", "");
    }

    public QCloudBaseRequest(Request req) {
        super(req.getCredential(), req.getRegionId());
        setCredential(req.getCredential());
        setRegionId(req.getRegionId());
    }

    public Credential getQCloudCredential() {
        return new Gson().fromJson(getCredential(), Credential.class);
    }

    public CvmClient getCvmClient() throws RSException {
        Credential qCloudCredential = getQCloudCredential();
        String region = getRegionId();
        if(StringUtils.isBlank(region)){
            region = DEFAULT_REGION;
        }
        return new CvmClient(qCloudCredential, region, getClientProfile());
    }

    public ClbClient getClbClient() throws PluginException {
        Credential qCloudCredential = getQCloudCredential();
        String region = getRegionId();
        if(StringUtils.isBlank(region)){
            region = DEFAULT_REGION;
        }
        return new ClbClient(qCloudCredential, region, getClientProfile());
    }

    public MonitorClient getMonitorClient() throws PluginException {
        Credential qCloudCredential = getQCloudCredential();
        String region = getRegionId();
        if(StringUtils.isBlank(region)){
            region = DEFAULT_REGION;
        }
        return new MonitorClient(qCloudCredential, region, getClientProfile());
    }

    public CbsClient getCbsClient() throws PluginException {
        Credential qCloudCredential = getQCloudCredential();
        String region = getRegionId();
        if(StringUtils.isBlank(region)){
            region = DEFAULT_REGION;
        }
        return new CbsClient(qCloudCredential, region, getClientProfile());
    }

    public VpcClient getVpcClient() throws PluginException {
        Credential qCloudCredential = getQCloudCredential();
        String region = getRegionId();
        if(StringUtils.isBlank(region)){
            region = DEFAULT_REGION;
        }
        return new VpcClient(qCloudCredential, region, getClientProfile());
    }

    private ClientProfile getClientProfile() {
        ClientProfile clientProfile = new ClientProfile();
        QCloudProxySetting proxySetting = ProxyUtils.getQProxySetting();
        if(proxySetting != null) {
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setProxyHost(proxySetting.getHost());
            httpProfile.setProxyPort(proxySetting.getPort());
            httpProfile.setProxyUsername(proxySetting.getUserName());
            httpProfile.setProxyPassword(proxySetting.getPassword());
            clientProfile.setHttpProfile(httpProfile);
        }
        return clientProfile;
    }
}
