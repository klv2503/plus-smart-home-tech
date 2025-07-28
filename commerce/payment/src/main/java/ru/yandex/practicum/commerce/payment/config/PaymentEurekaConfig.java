package ru.yandex.practicum.commerce.payment.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class PaymentEurekaConfig {

    @Autowired
    private EurekaInstanceConfigBean config;

    @PostConstruct
    public void updateInstanceId() {
        config.setInstanceId(config.getAppname() + ":" + UUID.randomUUID());
    }
}