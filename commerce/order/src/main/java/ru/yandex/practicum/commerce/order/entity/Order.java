package ru.yandex.practicum.commerce.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.OrderState;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "username")
    private String username;

    @Column(name = "cart_id")
    private UUID shoppingCartId;

    @Transient
    private Map<UUID, Integer> products = new HashMap<>();

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @Column(name = "weight")
    private BigDecimal deliveryWeight;

    @Column(name = "volume")
    private BigDecimal deliveryVolume;

    @Column(name = "fragile")
    private Boolean fragile;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    @Column(name = "product_price")
    private BigDecimal productPrice;
}
