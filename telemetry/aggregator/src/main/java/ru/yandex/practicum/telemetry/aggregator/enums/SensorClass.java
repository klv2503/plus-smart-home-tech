package ru.yandex.practicum.telemetry.aggregator.enums;

import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Arrays;
import java.util.Optional;

public enum SensorClass {
    CLIMATE(ClimateSensorAvro.class),
    LIGHT(LightSensorAvro.class),
    MOTION(MotionSensorAvro.class),
    SWITCH(SwitchSensorAvro.class),
    TEMPERATURE(TemperatureSensorAvro.class);

    private final Class<?> clazz;

    SensorClass(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static Optional<SensorClass> fromPayload(Object payload) {
        return Arrays.stream(values())
                .filter(e -> e.clazz.equals(payload.getClass()))
                .findFirst();
    }
}
