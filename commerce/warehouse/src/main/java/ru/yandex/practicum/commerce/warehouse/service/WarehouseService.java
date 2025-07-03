package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.commerce.warehouse.entity.ProductInWarehouse;
import ru.yandex.practicum.commerce.warehouse.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseRepository;
import ru.yandex.practicum.exceptions.errors.NoProductsInShoppingCartException;
import ru.yandex.practicum.exceptions.errors.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.exceptions.errors.ProductNotFoundException;
import ru.yandex.practicum.exceptions.errors.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.exceptions.response.PairOfQuantities;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository repository;

    public ProductInWarehouse getProductInWarehouse(UUID productId) {
        return repository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public void addNewProduct(NewProductInWarehouseRequest request) {
        UUID prodId = UUID.fromString(request.getProductId());
        if (repository.existsById(prodId))
            throw new SpecifiedProductAlreadyInWarehouseException(prodId);
        ProductInWarehouse product = ProductInWarehouse.builder()
                .productId(prodId)
                .isFragile(request.getFragile() != null && request.getFragile())
                .quantity(0)
                .dimensions(request.getDimension())
                .weight(request.getWeight())
                .build();
        repository.save(product);
        //log.info("\n Made Product {}", product);
        //control reading
        log.info("\n✅Added product {}", getProductInWarehouse(product.getProductId()));
    }

    public void addProductQuantity(AddProductToWarehouseRequest request) {
        UUID prodId = UUID.fromString(request.getProductId());
        ProductInWarehouse product = getProductInWarehouse(prodId);
        product.setQuantity(product.getQuantity() + request.getQuantity());
        repository.save(product);
        //control reading
        log.info("\n✅Product with new quantity {}", getProductInWarehouse(prodId));
    }

    public BookedProductsDto checkCart(ShoppingCartDto cart) {
        Map<UUID, Integer> productMap = cart.getProducts();
        Set<UUID> ids = new HashSet<>(productMap.keySet());
        List<ProductInWarehouse> products = repository.findByProductIdIn(ids);
        Map<UUID, ProductInWarehouse> foundProds = products.stream()
                .collect(Collectors.toMap(ProductInWarehouse::getProductId, Function.identity()));

        List<String> noProds = new ArrayList<>();
        Map<String, PairOfQuantities> deficits = new HashMap<>();
        BigDecimal weight = new BigDecimal("0");
        BigDecimal volume = new BigDecimal("0");
        boolean isFragile = false;

        for (UUID id : productMap.keySet()) {
            if (foundProds.containsKey(id)) {
                if (foundProds.get(id).getQuantity() >= productMap.get(id)) {
                    weight = weight.add(BigDecimal.valueOf(productMap.get(id) * foundProds.get(id).getWeight()));
                    volume = volume.add(foundProds.get(id).getDimensions().getVolume()
                            .multiply(BigDecimal.valueOf(productMap.get(id))));
                    isFragile = isFragile || foundProds.get(id).isFragile();
                } else {
                    deficits.put(id.toString(),
                            PairOfQuantities.builder()
                                    .wanted(productMap.get(id))
                                    .available(foundProds.get(id).getQuantity())
                                    .build());
                }
            } else {
                noProds.add(id.toString());
            }
        }

        if (!noProds.isEmpty()) {
            throw new NoProductsInShoppingCartException(noProds);
        }
        if (!deficits.isEmpty())
            throw new ProductInShoppingCartLowQuantityInWarehouseException(deficits);
        return BookedProductsDto.builder()
                .deliveryWeight(weight)
                .deliveryVolume(volume)
                .fragile(isFragile)
                .build();
    }

    public ResidueDto getResidue(String productId) {
        ProductInWarehouse product = getProductInWarehouse(UUID.fromString(productId));
        log.info("\n✅Received {}", product);
        return ResidueDto.builder()
                .productId(product.getProductId())
                .quantity(product.getQuantity())
                .build();
    }
}
