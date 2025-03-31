package ru.yandex.practicum.common.model.device.events;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.common.model.device.HubEvent;
import ru.yandex.practicum.common.model.device.HubEventType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DeviceRemovedEvent extends HubEvent {

    @NotBlank
    private String removedId;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
