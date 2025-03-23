package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.model.device.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.device.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.device.events.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Service
@Slf4j
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {
    public ScenarioRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        ScenarioRemovedEvent thisEvent = (ScenarioRemovedEvent) event;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(thisEvent.getName())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
