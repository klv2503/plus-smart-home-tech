package ru.yandex.practicum.commerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.CartSpecialDto;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface OrderCartFeign {

        @GetMapping("/all")
        ResponseEntity<List<ShoppingCartDto>> findCarts(
                @RequestParam(value = "username", required = false) String userName);

        @GetMapping("/cart")
        ResponseEntity<CartSpecialDto> getCartById(@RequestParam UUID cartId);
}
