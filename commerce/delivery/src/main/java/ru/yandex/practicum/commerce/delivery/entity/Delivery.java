package ru.yandex.practicum.commerce.delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @Column(name = "delivery_id")
    private UUID deliveryId;

    @ManyToOne
    @JoinColumn(name = "from_address")
    private Address fromAddress;

    @ManyToOne
    @JoinColumn(name = "to_address")
    private Address toAddress;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState;

}
