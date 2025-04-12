package ru.yandex.practicum.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class HubsConsumerConfig {
    private HubsConsumerProperties hubsConsumerProperties;

    public HubsConsumerConfig(HubsConsumerProperties hubsConsumerProperties) {
        this.hubsConsumerProperties = hubsConsumerProperties;
    }

    @PostConstruct
    public void checkInit() {
        if (this.hubsConsumerProperties == null) {
            throw new IllegalStateException("HubsConsumerConfig: consumer не инициализирован!");
        }
    }

}