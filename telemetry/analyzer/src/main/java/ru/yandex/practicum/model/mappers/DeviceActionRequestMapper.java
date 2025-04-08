package ru.yandex.practicum.model.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.entities.Action;
import ru.yandex.practicum.entities.Scenario;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;

import java.time.Instant;
import java.util.List;

@Component
public class DeviceActionRequestMapper {
    public static DeviceActionRequest map(Scenario scenario, String hubId, String sensorId, Action action) {
        DeviceActionProto.Builder actionBuilder = DeviceActionProto.newBuilder()
                .setSensorId(sensorId)
                .setType(ActionTypeProto.valueOf(action.getType().name())); // тут предполагается, что enum совпадают по имени

        if (action.getValue() != null) {
            actionBuilder.setValue(action.getValue());
        }

        return DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenario.getName())
                .setAction(actionBuilder.build())
                .setTimestamp(currentTimestamp())
                .build();
    }

    public static List<DeviceActionRequest> mapAll(Scenario scenario, String hubId) {
        return scenario.getActions().entrySet().stream()
                .map(entry -> map(scenario, hubId, entry.getKey(), entry.getValue()))
                .toList();
    }

    private static com.google.protobuf.Timestamp currentTimestamp() {
        Instant now = Instant.now();
        return com.google.protobuf.Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
    }
}
