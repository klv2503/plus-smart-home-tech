package ru.yandex.practicum.commerce.shoppingcart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "shopping_cart_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShopCart shoppingCart;

    @Column(name = "product_code")
    private UUID productCode;

    @Column(name = "quantity")
    private Integer quantity;
}