package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.telemetry.collector.model.device.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.device.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.device.events.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.mapper.ActionMapper;
import ru.yandex.practicum.telemetry.collector.service.handler.mapper.ConditionsMapper;

@Service
@Slf4j
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent thisEvent = (ScenarioAddedEvent) event;
        ScenarioAddedEventAvro.Builder builder = ScenarioAddedEventAvro.newBuilder();
        builder.setName(thisEvent.getName());
        builder.setActions(ActionMapper.devAtcListToAvro(thisEvent.getActions()));
        builder.setConditions(ConditionsMapper.condListToAvro(thisEvent.getConditions()));
        return builder.build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
