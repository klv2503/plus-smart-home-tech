package ru.yandex.practicum.commerce.shoppingcart.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.shoppingcart.entity.ShopCart;
import ru.yandex.practicum.commerce.shoppingcart.entity.ShopCartItem;
import ru.yandex.practicum.dto.CartSpecialDto;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShopCartMapper {

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

    public static ShoppingCartDto shopCartToShoppingCartDto(ShopCart cart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(cart.getCartId().toString())
                .products(ShopCartMapper.mapCartItemsToProductMap(cart.getItems()))
                .build();
    }

    public static List<ShoppingCartDto> cartListToDtoList(List<ShopCart> carts) {
        return carts.stream()
                .map(ShopCartMapper::shopCartToShoppingCartDto)
                .toList();
    }

    public static CartSpecialDto cartToCartSpecial(ShopCart cart) {
        return CartSpecialDto.builder()
                .shoppingCartId(cart.getCartId())
                .userName(cart.getUsername())
                .products(ShopCartMapper.mapCartItemsToProductMap(cart.getItems()))
                .build();
    }
}
