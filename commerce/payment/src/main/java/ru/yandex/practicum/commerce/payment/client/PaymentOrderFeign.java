package ru.yandex.practicum.commerce.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;

@FeignClient(name = "ORDER", path = "/api/v1/order")
public interface PaymentOrderFeign {

    @PostMapping("/payment")
    ResponseEntity<OrderDto> paymentSuccess(@RequestBody String orderId);

    @PostMapping("/failed")
    ResponseEntity<OrderDto> paymentFailed(@RequestBody String orderId);

}
