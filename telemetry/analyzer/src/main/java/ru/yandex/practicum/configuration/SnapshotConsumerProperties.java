package ru.yandex.practicum.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.TopicType;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties("analyzer.kafka.consumers.snapshots")
public class SnapshotConsumerProperties {
    private final Map<String, String> properties;
    private final EnumMap<TopicType, String> topics;

    public SnapshotConsumerProperties(Map<String, String> properties, Map<String, String> topics) {
        this.properties = properties;
        this.topics = new EnumMap<>(TopicType.class);
        for (Map.Entry<String, String> entry : topics.entrySet()) {
            this.topics.put(TopicType.fromString(entry.getKey()), entry.getValue());
        }
    }

}
