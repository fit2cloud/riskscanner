package io.riskscanner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author maguohao
 * @date 2020-10-30 16:17
 */
@PropertySource(value = {
        "classpath:properties/global.properties",
        "classpath:properties/quartz.properties"
}, encoding = "UTF-8", ignoreResourceNotFound = true)
@Configuration
public class CommonConfig {

}
