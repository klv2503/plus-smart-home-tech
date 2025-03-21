package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.device.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.device.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EventController {
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;


    public EventController(List<SensorEventHandler> sensorEventHandlers, List<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));

    }

    @PostMapping("/sensors")
    public void postSensorEvent(@Valid @RequestBody SensorEvent request) {
        log.trace("\nEventController.postSensorEvent: accepted {}", request);
        if (sensorEventHandlers.containsKey(request.getType()))
            sensorEventHandlers.get(request.getType()).handle(request);
        else {
            throw new IllegalArgumentException("Handler for request" + request + " not found.");
        }
    }

    @PostMapping("/hubs")
    public void postHubEvent(@Valid @RequestBody HubEvent request) {
        log.trace("\nEventController.postHubEvent: accepted {}, type {}", request, request.getType());
        if (hubEventHandlers.containsKey(request.getType()))
            hubEventHandlers.get(request.getType()).handle(request);
        else {
            throw new IllegalArgumentException("Handler for request" + request + " not found.");
        }
    }

}
