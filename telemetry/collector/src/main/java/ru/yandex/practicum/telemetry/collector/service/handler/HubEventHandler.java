package ru.yandex.practicum.telemetry.collector.service.handler;

import ru.yandex.practicum.telemetry.collector.model.device.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.device.HubEventType;

public interface HubEventHandler {
    HubEventType getMessageType();

    void handle(HubEvent request);
}
