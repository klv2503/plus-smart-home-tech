package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.warehouse.dto.*;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;
import ru.yandex.practicum.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ResidueDto;
import ru.yandex.practicum.dto.ShoppingCartDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
@Slf4j
@Validated
public class WarehouseController {

    private final WarehouseService service;

    private final AddressDto addressDto = new AddressDto();

    @PutMapping
    public ResponseEntity<Void> addNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request) {
        log.info("\n✅WarehouseController.addNewProduct: accepted {}", request);
        service.addNewProduct(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check")
    public ResponseEntity<BookedProductsDto> checkCart(@RequestBody @Valid ShoppingCartDto cart) {
        log.info("\n✅WarehouseController.checkCart: accepted {}", cart);
        BookedProductsDto cartParams = service.checkCart(cart);
        log.info("\n✅WarehouseController.checkCart: received {}", cartParams);
        return ResponseEntity.ok(cartParams);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addProductQuantity(@RequestBody @Valid AddProductToWarehouseRequest request) {
        log.info("\n✅WarehouseController.addProductQuantity: accepted {}", request);
        service.addProductQuantity(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/address")
    public ResponseEntity<AddressDto> getWarehouseAddress() {
        log.info("\n✅return current address {}", addressDto);
        return ResponseEntity.ok(addressDto);
    }

    @GetMapping("/residue")
    public ResponseEntity<ResidueDto> getResidue(@RequestParam @NotBlank String productId) {
        log.info("\n✅Accepted productId {}", productId);
        return ResponseEntity.ok().body(service.getResidue(productId));
    }
}
