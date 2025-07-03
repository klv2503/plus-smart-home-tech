package ru.yandex.practicum.commerce.shoppingcart.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ShoppingCartDto;

@FeignClient(name = "WAREHOUSE", path = "/api/v1/warehouse")
public interface WarehouseCartClient {
    @PostMapping("/check")
    ResponseEntity<BookedProductsDto> checkCart(@RequestBody ShoppingCartDto cart);
}