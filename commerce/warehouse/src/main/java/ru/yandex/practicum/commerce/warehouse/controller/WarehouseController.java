package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.warehouse.dto.*;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.validation.ValidUUID;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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
    public ResponseEntity<ResidueDto> getResidue(@RequestParam @ValidUUID String productId) {
        log.info("\n✅Accepted productId {}", productId);
        return ResponseEntity.ok().body(service.getResidue(productId));
    }

    //Новые методы по ТЗ-22

    @PostMapping("/shipped")
    public ResponseEntity<Void> shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request) {
        log.info("\n✅WarehouseController.shippedToDelivery: accepted {}", request);
        service.shippedToDelivery(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/return")
    //Сигнатуру метода решил исправить. Возможен частичный возврат, поэтому вызвавшему вернем остатки.
    //Кроме того резервы связаны с id заказа и доставки. Исходная сигнатура эту связь не дает
    public ResponseEntity<Map<UUID, Integer>> returnProducts(@RequestBody @Valid ProductReturnRequest request) {
        log.info("\n✅WarehouseController.returnProducts: accepted {}", request);
        return ResponseEntity.ok(service.returnProducts(request));
    }

    @PostMapping("/assembly")
    public ResponseEntity<BookedProductsDto> assemblyProductForOrderFromShoppingCart(
            @RequestBody @Valid AssemblyProductsForOrderRequest request) {
        log.info("\n✅WarehouseController.assemblyProductForOrderFromShoppingCart: accepted {}", request);
        BookedProductsDto cartParams = service.assemblyProductForOrderFromShoppingCart(request);
        log.info("\n✅WarehouseController.assemblyProductForOrderFromShoppingCart: received {}", cartParams);
        return ResponseEntity.ok(cartParams);
    }

    //Добавил метод от себя, чтобы читать продукты для order

    @GetMapping("/reserved")
    public ResponseEntity<List<OrderBookingDto>> getBooked(@RequestParam Set<UUID> orderIds) {
        log.info("\n✅WarehouseController.getBooked: accepted {}", orderIds);
        return ResponseEntity.ok(service.getBooked(orderIds));
    }
}
