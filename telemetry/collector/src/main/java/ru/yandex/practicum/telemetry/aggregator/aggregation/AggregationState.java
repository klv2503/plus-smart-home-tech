package ru.yandex.practicum.telemetry.aggregator.aggregation;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.common.kafka.deserializer.SensorEventAvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.common.model.Snapshot;
import ru.yandex.practicum.common.model.device.events.ScenarioAddedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class AggregationState {

    private final Map<String, List<ScenarioAddedEvent>> rawEvents = new ConcurrentHashMap<>();
    private final Map<String, Snapshot> snapshots = new ConcurrentHashMap<>();
    private final SensorEventAvroDeserializer sensorDeserializer;

    public void processRecord(ConsumerRecord<String, SpecificRecordBase> record) {
        switch (record.topic()) {
            case "telemetry.sensors.v1": {
                SensorEventAvro sensorEventAvro = (SensorEventAvro) record.value();
                sensorEventHandle(sensorEventAvro);
                break;
            }
            case "telemetry.hubs.v1": {
                HubEventAvro hubEventAvro = (HubEventAvro) record.value();
                habEventHandle(hubEventAvro);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown topic name" + record.topic());
        }
    }

    private void sensorEventHandle(SensorEventAvro value) {

    }

    private void habEventHandle(HubEventAvro value) {

    }

    public void addEvent(String hubId, ScenarioAddedEvent event) {
        rawEvents.computeIfAbsent(hubId, k -> new ArrayList<>()).add(event);
    }

    public List<ScenarioAddedEvent> getEvents(String hubId) {
        return rawEvents.getOrDefault(hubId, Collections.emptyList());
    }

    public void updateSnapshot(String hubId, Snapshot snapshot) {
        snapshots.put(hubId, snapshot);
    }

    public Snapshot getSnapshot(String hubId) {
        return snapshots.get(hubId);
    }

    public void clearEvents(String hubId) {
        rawEvents.remove(hubId);
    }
}
