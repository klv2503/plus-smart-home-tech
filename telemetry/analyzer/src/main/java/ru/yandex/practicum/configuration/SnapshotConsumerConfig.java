package ru.yandex.practicum.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties("analyzer.kafka.consumers.hubs")
public class SnapshotConsumerConfig {
    private final SnapshotConsumerProperties snapshotConsumerProperties;

    public SnapshotConsumerConfig(SnapshotConsumerProperties snapshotConsumerProperties) {
        this.snapshotConsumerProperties = snapshotConsumerProperties;
    }

    @PostConstruct
    public void checkInit() {
        if (this.snapshotConsumerProperties == null) {
            throw new IllegalStateException("SnapshotConsumerConfig: consumer не инициализирован!");
        }
    }
}
