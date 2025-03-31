package ru.yandex.practicum.common.model;

import lombok.*;
import ru.yandex.practicum.common.model.sensor.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Snapshot {

    private ClimateSensorEvent climateSensorEvent;

    private LightSensorEvent lightSensorEvent;

    private MotionSensorEvent motionSensorEvent;

    private SwitchSensorEvent switchSensorEvent;

    private TemperatureSensorEvent temperatureSensorEvent;
}
