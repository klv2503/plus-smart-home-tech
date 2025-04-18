package ru.yandex.practicum.telemetry.collector.configuration;

import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import ru.yandex.practicum.TopicType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("collector.kafka.producer")
public class CollectorProducerProperties {
    private Map<String, String> properties = new HashMap<>();
    private EnumMap<TopicType, String> topics = new EnumMap<>(TopicType.class);

    @ConstructorBinding
    public CollectorProducerProperties(Map<String, String> properties, Map<String, String> topics) {
        this.properties = properties;
        for (Map.Entry<String, String> entry : topics.entrySet()) {
            this.topics.put(TopicType.fromString(entry.getKey()), entry.getValue());
        }
    }

    @PostConstruct
    public void logConfig() {
        System.out.println("Collector Producer Properties: " + properties);
        System.out.println("Collector Producer Topics: " + topics);
    }

}