package ru.yandex.practicum.utiliteis;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class KeySetMapper {

    public static Map<String, Integer> mapIndexesUUIDtoString(Map<UUID, Integer> items) {
        return items.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(), Map.Entry::getValue
                ));
    }

    public static Map<UUID, Integer> mapIndexesStringToUUID(Map<String, Integer> items) {
        return items.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> UUID.fromString(e.getKey()), Map.Entry::getValue
                ));
    }
}
