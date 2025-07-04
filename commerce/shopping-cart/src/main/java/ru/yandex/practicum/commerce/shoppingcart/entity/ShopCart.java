package ru.yandex.practicum.commerce.shoppingcart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shopping_cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCart {
    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "cart_id", updatable = false, nullable = false)
    private UUID cartId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private CartState state;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShopCartItem> items;

    public void addItems(List<ShopCartItem> newItems) {
        if (CollectionUtils.isEmpty(newItems)) {
            return;
        }
        for (ShopCartItem item : newItems) {
            item.setShoppingCart(this);
        }
        this.items.addAll(newItems);
    }

    public void removeItems(List<ShopCartItem> removedItems) {
        if (CollectionUtils.isEmpty(removedItems)) {
            return;
        }
        for (ShopCartItem item : removedItems) {
            if (items.remove(item)) {
                item.setShoppingCart(null);
            }
        }
    }
}