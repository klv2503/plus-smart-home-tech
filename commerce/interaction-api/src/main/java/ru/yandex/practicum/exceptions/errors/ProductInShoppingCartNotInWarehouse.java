package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.UUID;

public class ProductInShoppingCartNotInWarehouse extends SmartHomeException {

    public ProductInShoppingCartNotInWarehouse(UUID productId) {
        super(
                String.format("No product with ID '%s' found in warehouse", productId),
                String.format("Товар '%s' не найден на складе.", productId),
                String.valueOf(HttpStatus.BAD_REQUEST)
        );
    }
}
