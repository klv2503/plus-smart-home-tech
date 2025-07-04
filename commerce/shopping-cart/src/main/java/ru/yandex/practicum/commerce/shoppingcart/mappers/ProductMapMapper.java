package ru.yandex.practicum.commerce.shoppingcart.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.shoppingcart.entity.ShopCartItem;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductMapMapper {

    public static Map<UUID, Integer> mapCartItemsToProductMap(List<ShopCartItem> items) {
        if (items == null) {
            return new HashMap<>();
        }

        return new HashMap<>(
                items.stream()
                        .collect(Collectors.toMap(
                                        ShopCartItem::getProductCode,
                                        ShopCartItem::getQuantity
                                )
                        )
        );
    }
}
