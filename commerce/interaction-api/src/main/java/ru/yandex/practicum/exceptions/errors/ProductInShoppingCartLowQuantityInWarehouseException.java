package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;
import ru.yandex.practicum.exceptions.response.PairOfQuantities;

import java.util.Map;

public class ProductInShoppingCartLowQuantityInWarehouseException extends SmartHomeException {

    public ProductInShoppingCartLowQuantityInWarehouseException(Map<String, PairOfQuantities> productDeficits,
                                                                String message) {
        super(
                message,
                buildUserMessage(productDeficits),
                String.valueOf(HttpStatus.BAD_REQUEST)
        );
    }

    public ProductInShoppingCartLowQuantityInWarehouseException(String message, String userMessage, String httpStatus) {
        super(message, userMessage, httpStatus);
    }


    private static String buildUserMessage(Map<String, PairOfQuantities> productDeficits) {
        StringBuilder message = new StringBuilder(
                "Some products in your shopping cart have insufficient quantity in warehouse:\n"
        );

        productDeficits.forEach((productId, quantities) -> {
            message.append("Product ID: ")
                    .append(productId)
                    .append(" - Wanted: ")
                    .append(quantities.getWanted())
                    .append(", Available: ")
                    .append(quantities.getAvailable())
                    .append("\n");
        });

        return message.toString();
    }
}