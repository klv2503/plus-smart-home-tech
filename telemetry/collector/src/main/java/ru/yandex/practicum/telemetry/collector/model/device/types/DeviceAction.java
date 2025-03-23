package ru.yandex.practicum.telemetry.collector.model.device.types;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceAction {

    private String sensorId;

    private ActionType type;

    private Integer value;

}
