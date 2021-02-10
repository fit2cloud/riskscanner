package io.riskscanner.base.rs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.Properties;


public class ProxyUtils {
    protected static Logger log = LoggerFactory.getLogger(ProxyUtils.class);
    private static final String PROPERTIES_PATH = "/opt/fit2cloud/conf/fit2cloud.properties";
    private static Properties properties = null;

    private static void loadProperties() throws Exception {
        FileReader inStream = null;
        try {
            inStream = new FileReader(PROPERTIES_PATH);
            properties = new Properties();
            properties.load(inStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inStream != null) {
                inStream.close();
                inStream = null;
            }
        }
    }

    public static String getProperties(String propertyName) {
        if (properties == null) {
            try {
                loadProperties();
            } catch (Exception e) {
                return null;
            }
        }
        if (properties.containsKey(propertyName)) {
            return properties.getProperty(propertyName);
        }
        return null;
    }

    public static AliyunProxySetting getProxySetting() {
        try {
            AliyunProxySetting proxySetting = new AliyunProxySetting();
            String httpHost = getProperties("plugin.aliyun.http.proxy.addr");
            boolean validSettings = false;
            if (httpHost != null && httpHost.trim().length() > 0) {
                proxySetting.setHttpAddr(httpHost.trim());
                validSettings = true;
            }
            String httpsHost = getProperties("plugin.aliyun.https.proxy.addr");
            if (httpsHost != null && httpsHost.trim().length() > 0) {
                proxySetting.setHttpsAddr(httpsHost.trim());
                validSettings = true;
            }
            if(!validSettings){
                return null;
            }
            String noProxy = getProperties("plugin.aliyun.no.proxy");
            if (noProxy != null && noProxy.trim().length() > 0) {
                proxySetting.setNoProxy(noProxy.trim());
            }
            return proxySetting;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static QCloudProxySetting getQProxySetting() {
        try {
            QCloudProxySetting proxySetting = new QCloudProxySetting();
            String host = getProperties("plugin.qcloud.proxy.host");
            if (host != null && host.trim().length() > 0) {
                proxySetting.setHost(host.trim());
            } else {
                return null;
            }
            String portStr = getProperties("plugin.qcloud.proxy.port");
            if (portStr != null && portStr.trim().length() > 0) {
                int port = Integer.parseInt(portStr);
                proxySetting.setPort(port);
            } else {
                log.info("QCloud proxy port not set!");
                return null;
            }
            String userName = getProperties("plugin.qcloud.proxy.username");
            if (userName != null && userName.trim().length() > 0) {
                proxySetting.setUserName(userName.trim());
            }
            String password = getProperties("plugin.qcloud.proxy.password");
            if (password != null && password.trim().length() > 0) {
                proxySetting.setPassword(password.trim());
            }
            return proxySetting;
        } catch (NumberFormatException e) {
            log.info("Qcloud proxy port setting error!");
            return null;
        }
    }
}
