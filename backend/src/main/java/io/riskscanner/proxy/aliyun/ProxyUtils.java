package io.riskscanner.proxy.aliyun;


import io.riskscanner.base.domain.Proxy;
import io.riskscanner.proxy.tencent.QCloudProxySetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.Properties;


public class ProxyUtils {
    protected static Logger log = LoggerFactory.getLogger(ProxyUtils.class);

    public static AliyunProxySetting getProxySetting(Proxy proxy) {
        try {
            if (proxy == null) return null;
            AliyunProxySetting proxySetting = new AliyunProxySetting();
            String httpHost = "http://" + proxy.getProxyIp() + ":" + proxy.getProxyPort();
            boolean validSettings = false;
            if (httpHost != null && httpHost.trim().length() > 0) {
                proxySetting.setHttpAddr(httpHost.trim());
                validSettings = true;
            }
            String httpsHost = "https://" + proxy.getProxyIp() + ":" + proxy.getProxyPort();
            if (httpsHost != null && httpsHost.trim().length() > 0) {
                proxySetting.setHttpsAddr(httpsHost.trim());
                validSettings = true;
            }
            if(!validSettings){
                return null;
            }
            String noProxy = null;
            if (noProxy != null && noProxy.trim().length() > 0) {
                proxySetting.setNoProxy(noProxy.trim());
            }
            return proxySetting;
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
