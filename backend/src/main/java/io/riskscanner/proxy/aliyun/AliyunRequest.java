package io.riskscanner.proxy.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.http.HttpClientType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import io.riskscanner.base.domain.Proxy;
import io.riskscanner.proxy.azure.Request;

public class AliyunRequest extends Request {
    private AliyunCredential aliyunCredential;

    public AliyunRequest() {
        super("", "");
    }

    public AliyunRequest(Request req) {
        super(req.getCredential(), req.getRegionId());
        setCredential(req.getCredential());
        setRegionId(req.getRegionId());
    }

    public AliyunCredential getAliyunCredential() {
        if (aliyunCredential == null) {
            aliyunCredential = new Gson().fromJson(getCredential(), AliyunCredential.class);
        }
        return aliyunCredential;
    }

    public void setAliyunCredential(AliyunCredential aliyunCredential) {
        this.aliyunCredential = aliyunCredential;
    }

    public String getSecretKey() {
        aliyunCredential = getAliyunCredential();
        if (aliyunCredential != null) {
            return aliyunCredential.getSecretKey();
        }
        return null;
    }

    public String getAccessKey() {
        aliyunCredential = getAliyunCredential();
        if (aliyunCredential != null) {
            return aliyunCredential.getAccessKey();
        }
        return null;
    }

    public IAcsClient getAliyunClient(Proxy proxy) {
        HttpClientConfig httpClientConfig = new HttpClientConfig();
        if (getAccessKey() != null && getAccessKey().trim().length() > 0 && getSecretKey() != null && getSecretKey().trim().length() > 0) {
            String defaultRegionId = "cn-hangzhou";
            if (getRegionId() != null && getRegionId().trim().length() > 0) {
                defaultRegionId = getRegionId();
            }
            IClientProfile profile = DefaultProfile.getProfile(defaultRegionId, getAccessKey(), getSecretKey());
            if (profile.getHttpClientConfig() == null) {
                profile.setHttpClientConfig(HttpClientConfig.getDefault());
            }
            httpClientConfig = profile.getHttpClientConfig();
            httpClientConfig.setReadTimeoutMillis(1800000L);//15m
            httpClientConfig.setWriteTimeoutMillis(300000L);//5m
            httpClientConfig.setConnectionTimeoutMillis(30000L);//30s
            httpClientConfig.setIgnoreSSLCerts(true);
            httpClientConfig.setCompatibleMode(false);
            httpClientConfig.setClientType(HttpClientType.ApacheHttpClient);
            AliyunProxySetting proxySetting = ProxyUtils.getProxySetting(proxy);
            if(proxySetting != null){
                httpClientConfig.setHttpProxy(proxySetting.getHttpAddr());
                httpClientConfig.setHttpsProxy(proxySetting.getHttpsAddr());
                httpClientConfig.setNoProxy(proxySetting.getNoProxy());
            }

            DefaultAcsClient client = new DefaultAcsClient(profile);
            client.setAutoRetry(false);
            client.setMaxRetryNumber(0);
            return client;
        }
        return null;
    }
}
