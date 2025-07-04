package ru.yandex.practicum.commerce.shoppingstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.shoppingstore.dto.Pageable;
import ru.yandex.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.shoppingstore.enums.EnumUtils;
import ru.yandex.practicum.commerce.shoppingstore.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.enums.QuantityState;
import ru.yandex.practicum.commerce.shoppingstore.service.ShoppingStoreService;
import ru.yandex.practicum.commerce.shoppingstore.validation.EnumValid;
import ru.yandex.practicum.commerce.shoppingstore.validation.NotEmptyProductDto;
import ru.yandex.practicum.validation.OnCreate;
import ru.yandex.practicum.validation.UUIDValidator;
import ru.yandex.practicum.validation.ValidUUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/shopping-store")
@Validated
public class ShoppingStoreController {

    private final ShoppingStoreService service;
    private final UUIDValidator validator;

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProductList(
            @RequestParam @Valid @EnumValid(enumClass = ProductCategory.class) String category,
            @Valid @ModelAttribute Pageable pageable) {
        log.info("\nShoppingStoreController.getProductList: {}, {}", category, pageable);
        ProductCategory enumCategory = EnumUtils.fromStringOrThrow(category, ProductCategory.class);
        return ResponseEntity.ok().body(service.getProductList(enumCategory, pageable));
    }

    @PutMapping
    public ResponseEntity<ProductDto> addNewProduct(@RequestBody @Validated(OnCreate.class) @Valid ProductDto productDto) {
        log.info("\nShoppingStoreController.addNewProduct: {}", productDto);
        return ResponseEntity.status(HttpStatus.OK).body(service.addNewProduct(productDto));
    }

    @PostMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid @NotEmptyProductDto ProductDto productDto) {
        log.info("\nShoppingStoreController.updateProduct: {}", productDto);
        return ResponseEntity.status(HttpStatus.OK).body(service.updateProduct(productDto));
    }

    @PostMapping("/removeProductFromStore")
    public ResponseEntity<Boolean> removeProduct(@RequestBody String productId) {
        log.info("\nShoppingStoreController.removeProduct: ---{}---", productId);
        //в тестах данные вводятся с кавычками. не имея возможности поменять тесты, убиваю кавычки вручную
        String correctedId = productId.replace("\"", "").trim();
        if (!validator.isValid(correctedId, null)) {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.removeProduct(correctedId));
    }

    @PostMapping("/quantityState")
    public ResponseEntity<Boolean> setProductQuantity(
            @RequestParam String productId,
            @RequestParam @EnumValid(enumClass = QuantityState.class) String quantityState) {
        if (!validator.isValid(productId, null)) {
            return ResponseEntity.badRequest().body(false);
        }
        SetProductQuantityStateRequest request = SetProductQuantityStateRequest.builder()
                .productId(productId)
                .quantityState(quantityState)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(service.setProductQuantity(request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductDetails(@PathVariable @ValidUUID String productId) {
        return ResponseEntity.ok().body(service.getProductDetails(productId));
    }

}
