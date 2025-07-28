package ru.yandex.practicum.commerce.warehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@IdClass(OrderBookingId.class)
@Table(name = "reserved")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderBooking {

    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "quantity")
    private int quantity;

}