package ru.yandex.practicum.commerce.delivery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.validation.ValidUUID;

@FeignClient(name = "ORDER", path = "/api/v1/order")
public interface DeliveryOrderFeign {

    @PostMapping("/delivery/failed")
    ResponseEntity<OrderDto> deliveryFailed(@RequestBody String orderId);

    @PostMapping("/delivery")
    ResponseEntity<OrderDto> delivery(@RequestBody String orderId);

    @PostMapping("/calculate/assembly")
    ResponseEntity<OrderDto> assemblyOrder(@RequestBody @ValidUUID String orderId);

}
