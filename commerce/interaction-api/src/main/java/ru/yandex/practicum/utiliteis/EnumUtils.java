package ru.yandex.practicum.utiliteis;

import java.util.Arrays;
import java.util.Optional;

public class EnumUtils {

    public static <E extends Enum<E>> Optional<E> fromString(String input, Class<E> enumClass) {
        if (input == null) return Optional.empty();

        String normalized = input
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase();

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equals(normalized))
                .findFirst();
    }

    public static <E extends Enum<E>> E fromStringOrThrow(String input, Class<E> enumClass) {
        return fromString(input, enumClass).orElseThrow(() ->
                new IllegalArgumentException("Invalid value for enum " + enumClass.getSimpleName() + ": " + input));
    }
}