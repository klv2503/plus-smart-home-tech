package ru.yandex.practicum.commerce.order.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.order.client.OrderCartFeign;
import ru.yandex.practicum.dto.CartSpecialDto;
import ru.yandex.practicum.exceptions.errors.ServiceUnavailableException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCartService {

    private final OrderCartFeign orderCartFeign;

    @CircuitBreaker(name = "shoppingCart", fallbackMethod = "fallbackGetCartById")
    public CartSpecialDto getCartById(UUID cartId) {
        return orderCartFeign.getCartById(cartId).getBody();
    }

    public CartSpecialDto fallbackGetCartById(UUID cartId, Throwable ex) {
        log.warn("Fallback for getCartById: {}", cartId, ex);
        if (ex instanceof FeignException feignEx) {
            // Если это 4xx ошибка — передаём как есть
            if (feignEx.status() >= 400 && feignEx.status() < 500) {
                throw feignEx;
            }
        }
        throw new ServiceUnavailableException("Shopping-Cart");
    }
}