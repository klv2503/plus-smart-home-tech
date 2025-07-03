package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.List;

public class NoProductsInShoppingCartException extends SmartHomeException {

    public NoProductsInShoppingCartException(List<String> missingProductIds) {
        super(
                "Some products from your shopping cart are missing in the warehouse.",
                buildUserMessage(missingProductIds),
                String.valueOf(HttpStatus.BAD_REQUEST)
        );
    }

    private static String buildUserMessage(List<String> productIds) {
        return "The following products from the shopping cart are not available in the warehouse: " +
                String.join(", ", productIds);
    }
}