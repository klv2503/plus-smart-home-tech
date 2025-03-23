package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.model.device.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.device.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.device.events.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Service
@Slf4j
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {

    public DeviceRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEvent event) {
        DeviceRemovedEvent thisEvent = (DeviceRemovedEvent) event;
        DeviceRemovedEventAvro devRemAvro = DeviceRemovedEventAvro.newBuilder()
                .setRemovedId(thisEvent.getRemovedId())
                .build();
        log.trace("\nDeviceAddedEventHandler: devAddAvro {}", devRemAvro);
        return devRemAvro;
    }
}
