package ru.yandex.practicum.commerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.math.BigDecimal;

@FeignClient(name = "PAYMENT", path = "/api/v1/payment")
public interface OrderPaymentFeign {

    @PostMapping("/productCost")
    ResponseEntity<BigDecimal> productCost(@RequestBody OrderDto order);

    @PostMapping("/totalCost")
    ResponseEntity<BigDecimal> getTotalCost(@RequestBody OrderDto order);

    @PostMapping
    ResponseEntity<PaymentDto> payment(@RequestBody OrderDto order);

}
