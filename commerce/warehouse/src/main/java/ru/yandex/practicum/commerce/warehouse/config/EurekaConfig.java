package ru.yandex.practicum.commerce.warehouse.config;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class EurekaConfig {

    @Bean
    public EurekaInstanceConfig eurekaInstanceConfig(InetUtils inetUtils,
                                                     @Value("${spring.application.name}") String appName) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        config.setAppname(appName);
        config.setInstanceId(appName + ":" + UUID.randomUUID());
        config.setPreferIpAddress(false);
        return config;
    }
}