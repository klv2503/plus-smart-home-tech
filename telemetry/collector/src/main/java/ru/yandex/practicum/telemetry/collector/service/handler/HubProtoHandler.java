package ru.yandex.practicum.telemetry.collector.service.handler;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubProtoHandler {

    HubEventProto.PayloadCase getMessageType();

    void handle(HubEventProto request);
}
