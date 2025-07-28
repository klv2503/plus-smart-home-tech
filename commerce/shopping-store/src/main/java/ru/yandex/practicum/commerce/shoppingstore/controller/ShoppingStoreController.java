package ru.yandex.practicum.commerce.shoppingstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.shoppingstore.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.dto.ShortProduct;
import ru.yandex.practicum.utiliteis.EnumUtils;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.enums.QuantityState;
import ru.yandex.practicum.commerce.shoppingstore.service.ShoppingStoreService;
import ru.yandex.practicum.validation.EnumValid;
import ru.yandex.practicum.commerce.shoppingstore.validation.NotEmptyProductDto;
import ru.yandex.practicum.validation.OnCreate;
import ru.yandex.practicum.validation.UUIDValidator;
import ru.yandex.practicum.validation.ValidUUID;

import java.util.List;
import java.util.UUID;

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
        return ResponseEntity.ok(service.getProductList(enumCategory, pageable));
    }

    @PutMapping
    public ResponseEntity<ProductDto> addNewProduct(@RequestBody @Validated(OnCreate.class) @Valid ProductDto productDto) {
        log.info("\nShoppingStoreController.addNewProduct: {}", productDto);
        return ResponseEntity.ok(service.addNewProduct(productDto));
    }

    @PostMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid @NotEmptyProductDto ProductDto productDto) {
        log.info("\nShoppingStoreController.updateProduct: {}", productDto);
        return ResponseEntity.ok(service.updateProduct(productDto));
    }

    @PostMapping("/removeProductFromStore")
    public ResponseEntity<Boolean> removeProduct(@RequestBody String productId) {
        log.info("\nShoppingStoreController.removeProduct: ---{}---", productId);
        //в тестах данные вводятся с кавычками. не имея возможности поменять тесты, убиваю кавычки вручную
        String correctedId = productId.replace("\"", "").trim();
        if (!validator.isValid(correctedId, null)) {
            return ResponseEntity.badRequest().body(false);
        }
        return ResponseEntity.ok(service.removeProduct(correctedId));
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

        return ResponseEntity.ok(service.setProductQuantity(request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductDetails(@PathVariable @ValidUUID String productId) {
        return ResponseEntity.ok(service.getProductDetails(productId));
    }

    @GetMapping("/product")
    public ResponseEntity<List<ShortProduct>> getProduct(@RequestParam List<UUID> ids) {
        //Формально в задании предлагается получить данные по одному продукту,
        //но не получать же их по одному для расчета стоимости продуктов в корзине.
        //Если потребуется получить данные по ровно одному продукту, то такой метод уже есть,
        //а если интересует только цена одного, можно List из одного id передать
        return ResponseEntity.ok(service.getProductByIds(ids));
    }

}
