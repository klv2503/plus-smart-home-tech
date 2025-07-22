package ru.yandex.practicum.commerce.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.yandex.practicum")
@ConfigurationPropertiesScan
@EnableFeignClients
public class DeliveryMain {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryMain.class, args);
    }
}