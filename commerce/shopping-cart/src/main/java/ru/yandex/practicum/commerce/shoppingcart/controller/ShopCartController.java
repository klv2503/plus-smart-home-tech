package ru.yandex.practicum.commerce.shoppingcart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.shoppingcart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.shoppingcart.service.CartService;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.validation.ValidUUID;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/shopping-cart")
@Validated
public class ShopCartController {

    private final CartService service;

    @GetMapping
    public ResponseEntity<ShoppingCartDto> getCartByUserName(@RequestParam("username") String userName) {
        log.info("\nShoppingCartController.getCartByUserName: accepted {}", userName);
        ShoppingCartDto cart = service.getCartByUserName(userName);
        log.info("\nShoppingCartController.getCartByUserName: returned {}", cart);
        return ResponseEntity.ok(cart);
    }

    @PutMapping
    public ResponseEntity<ShoppingCartDto> addProductInCart(
            @RequestParam("username") String userName,
            @RequestBody @NotNull Map<@ValidUUID String, @Min(1) Integer> products) {
        log.info("\nShoppingCartController.addProductInCart: accepted {} with {}", userName, products);
        ShoppingCartDto cart = service.addProductInCart(userName, products);
        log.info("\nShoppingCartController.addProductInCart: returned {}", cart);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    public ResponseEntity<Void> deactivateCart(@RequestParam("username") String userName) {
        log.info("\nShoppingCartController.deactivateCart: accepted {}", userName);
        service.deactivateCart(userName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<ShoppingCartDto> removeFromCart(@RequestParam("username") String userName,
                                                          @RequestBody List<@ValidUUID String> ids) {
        log.info("\nShoppingCartController.removeFromCart: accepted {} with {}", userName, ids);
        return ResponseEntity.ok().body(service.removeFromCart(userName, ids));
    }

    @PostMapping("/change-quantity")
    public ResponseEntity<ShoppingCartDto> changeProductQuantity(@RequestParam("username") String userName,
                                                                 @RequestBody @Valid ChangeProductQuantityRequest request) {
        log.info("\nShoppingCartController.changeProductQuantity: accepted {} with {}", userName, request);
        return ResponseEntity.ok().body(service.changeProductQuantity(userName, request));
    }
}
