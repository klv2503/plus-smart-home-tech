package ru.yandex.practicum.commerce.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private PaymentState state;
}
