package ru.yandex.practicum.commerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.math.BigDecimal;

@FeignClient(name = "DELIVERY", path = "/api/v1/delivery")
public interface OrderDeliveryFeign {

    @PutMapping
    ResponseEntity<DeliveryDto> planDelivery(@RequestBody DeliveryDto deliveryDto);

    @PostMapping("/cost")
    ResponseEntity<BigDecimal> deliveryCost(@RequestBody OrderDto order);

}
