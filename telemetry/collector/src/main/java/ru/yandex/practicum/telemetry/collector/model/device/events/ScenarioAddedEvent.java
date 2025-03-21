package ru.yandex.practicum.telemetry.collector.model.device.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.device.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.device.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.device.types.DeviceAction;
import ru.yandex.practicum.telemetry.collector.model.device.types.ScenarioCondition;

import java.util.List;

@Getter
@Setter
@ToString
public class ScenarioAddedEvent extends HubEvent {
    private String name;

    private List<ScenarioCondition> conditions;

    private List<DeviceAction> actions;


    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
