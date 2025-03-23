package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.model.device.events.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.device.types.DeviceType;
import ru.yandex.practicum.telemetry.collector.model.device.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.device.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Service
@Slf4j
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {
    public DeviceAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent event) {
        DeviceAddedEvent thisEvent = (DeviceAddedEvent) event;
        DeviceType thisType = thisEvent.getDeviceType();
        DeviceTypeAvro avroType = DeviceTypeAvro.valueOf(thisType.name());
        DeviceAddedEventAvro devAddAvro = DeviceAddedEventAvro.newBuilder()
                .setId(thisEvent.getId())
                .setType(avroType)
                .build();
        log.trace("\nDeviceAddedEventHandler: devAddAvro {}", devAddAvro);
        return devAddAvro;
    }

}
