package ru.yandex.practicum.commerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface OrderWarehouseFeign {
    @PostMapping("/assembly")
    ResponseEntity<BookedProductsDto> assemblyProductForOrderFromShoppingCart(
            @RequestBody AssemblyProductsForOrderRequest request);

    @GetMapping("/address")
    ResponseEntity<AddressDto> getWarehouseAddress();

    @PostMapping("/return")
    ResponseEntity<Map<UUID, Integer>> returnProducts(@RequestBody ProductReturnRequest request);

    @GetMapping("/reserved")
    ResponseEntity<List<OrderBookingDto>> getBooked(@RequestParam Set<UUID> orderIds);

}
