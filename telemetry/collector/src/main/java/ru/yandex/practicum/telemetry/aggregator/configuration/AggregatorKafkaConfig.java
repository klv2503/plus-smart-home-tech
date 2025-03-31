package ru.yandex.practicum.telemetry.aggregator.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("aggregator.kafka")
public class AggregatorKafkaConfig {
    private AggregatorProducerConfig producer;
    private AggregatorConsumerConfig consumer;

    @PostConstruct
    public void checkInit() {
        if (this.producer == null) {
            throw new IllegalStateException("AggregatorKafkaConfig: producer не инициализирован!");
        }
        if (this.consumer == null) {
            throw new IllegalStateException("AggregatorKafkaConfig: consumer не инициализирован!");
        }
    }
}
