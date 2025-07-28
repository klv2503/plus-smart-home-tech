package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.commerce.warehouse.entity.OrderBooking;
import ru.yandex.practicum.commerce.warehouse.mappers.OrderBookingMapper;
import ru.yandex.practicum.commerce.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.commerce.warehouse.entity.ProductInWarehouse;
import ru.yandex.practicum.commerce.warehouse.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseRepository;
import ru.yandex.practicum.exceptions.errors.NoProductsInShoppingCartException;
import ru.yandex.practicum.exceptions.errors.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.exceptions.errors.ProductNotFoundException;
import ru.yandex.practicum.exceptions.errors.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.exceptions.response.PairOfQuantities;
import ru.yandex.practicum.utiliteis.KeySetMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final OrderBookingRepository orderBookingRepository;

    @Transactional(readOnly = true)
    public ProductInWarehouse getProductInWarehouse(UUID productId) {
        return warehouseRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product", productId));
    }

    @Transactional(readOnly = true)
    public List<OrderBooking> getListByOrderId(UUID id) {
        return orderBookingRepository.findAllByOrderId(id);
    }

    public void addNewProduct(NewProductInWarehouseRequest request) {
        UUID prodId = UUID.fromString(request.getProductId());
        if (warehouseRepository.existsById(prodId))
            throw new SpecifiedProductAlreadyInWarehouseException(prodId);
        ProductInWarehouse product = ProductInWarehouse.builder()
                .productId(prodId)
                .isFragile(request.getFragile() != null && request.getFragile())
                .quantity(0)
                .dimensions(request.getDimension())
                .weight(request.getWeight())
                .build();
        warehouseRepository.save(product);
        //log.info("\n Made Product {}", product);
        //control reading
        log.info("\n✅Added product {}", getProductInWarehouse(product.getProductId()));
    }

    public void addProductQuantity(AddProductToWarehouseRequest request) {
        UUID prodId = UUID.fromString(request.getProductId());
        ProductInWarehouse product = getProductInWarehouse(prodId);
        product.setQuantity(product.getQuantity() + request.getQuantity());
        warehouseRepository.save(product);
        //control reading
        log.info("\n✅Product with new quantity {}", getProductInWarehouse(prodId));
    }

    public BookedProductsDto checkCart(ShoppingCartDto cart) {
        Map<UUID, Integer> productMap = cart.getProducts();
        Set<UUID> ids = new HashSet<>(productMap.keySet());
        List<ProductInWarehouse> products = warehouseRepository.findByProductIdIn(ids);
        Map<UUID, Integer> foundProds = products.stream()
                .collect(Collectors.toMap(ProductInWarehouse::getProductId, ProductInWarehouse::getQuantity));

        List<String> noProds = checkExistence(productMap, foundProds);
        if (!CollectionUtils.isEmpty(noProds)) {
            throw new NoProductsInShoppingCartException(noProds,
                    "Some products from your shopping cart are missing in the warehouse.");
        }
        Map<String, PairOfQuantities> deficits = checkQuantity(productMap, foundProds);
        if (!CollectionUtils.isEmpty(deficits)) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(deficits,
                    "Some products from your shopping cart are not available in the requested quantity.");
        }

        return scoreParameters(productMap, products);
    }

    @Transactional(readOnly = true)
    public ResidueDto getResidue(String productId) {
        ProductInWarehouse product = getProductInWarehouse(UUID.fromString(productId));
        log.info("\n✅Received {}", product);
        return ResidueDto.builder()
                .productId(product.getProductId())
                .quantity(product.getQuantity())
                .build();
    }

    //Новые методы по ТЗ-22
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        List<OrderBooking> prods = getListByOrderId(UUID.fromString(request.getOrderId()));
        for (OrderBooking p : prods) {
            p.setDeliveryId(UUID.fromString(request.getDeliveryId()));
        }
        orderBookingRepository.saveAll(prods);
    }

    public Map<UUID, Integer> returnProducts(ProductReturnRequest request) {
        UUID orderId = UUID.fromString(request.getOrderId());
        Map<UUID, Integer> requestedQuantities = KeySetMapper.mapIndexesStringToUUID(request.getProducts());

        List<OrderBooking> bookedProducts = getListByOrderId(orderId);
        Map<UUID, Integer> bookedQuantities = bookedProducts.stream()
                .collect(Collectors.toMap(OrderBooking::getProductId, OrderBooking::getQuantity));

        log.info("Requested to return: {}", requestedQuantities);
        log.info("Currently booked: {}", bookedQuantities);

        List<String> missingProducts = checkExistence(requestedQuantities, bookedQuantities);
        if (!missingProducts.isEmpty()) {
            throw new NoProductsInShoppingCartException(missingProducts,
                    "Some products from your shopping cart are missing in the reserved.");
        }

        Map<String, PairOfQuantities> insufficientQuantities = checkQuantity(requestedQuantities, bookedQuantities);
        if (!insufficientQuantities.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(insufficientQuantities,
                    "Some products from your order are not available in the requested quantity to return.");
        }

        bookedProducts.forEach(booked -> {
            Integer toReturn = requestedQuantities.get(booked.getProductId());
            if (toReturn != null) {
                booked.setQuantity(booked.getQuantity() - toReturn);
            }
        });
        orderBookingRepository.saveAll(bookedProducts);
        Set<UUID> productIds = requestedQuantities.keySet();
        List<ProductInWarehouse> productsInWarehouse = warehouseRepository.findByProductIdIn(productIds);
        productsInWarehouse.forEach(prod -> {
            Integer toAdd = requestedQuantities.get(prod.getProductId());
            if (toAdd != null) {
                prod.setQuantity(prod.getQuantity() + toAdd);
            }
        });
        warehouseRepository.saveAll(productsInWarehouse);
        return bookedProducts.stream()
                .collect(Collectors.toMap(OrderBooking::getProductId, OrderBooking::getQuantity));
    }

    public BookedProductsDto assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequest request) {
        Map<UUID, Integer> productMap = KeySetMapper.mapIndexesStringToUUID(request.getProducts());
        Set<UUID> ids = new HashSet<>(productMap.keySet());
        List<ProductInWarehouse> products = warehouseRepository.findByProductIdIn(ids);
        Map<UUID, Integer> foundProds = products.stream()
                .collect(Collectors.toMap(ProductInWarehouse::getProductId, ProductInWarehouse::getQuantity));

        List<String> noProds = checkExistence(productMap, foundProds);
        if (!CollectionUtils.isEmpty(noProds)) {
            throw new NoProductsInShoppingCartException(noProds,
                    "Some products from your shopping cart are missing in the warehouse.");
        }
        Map<String, PairOfQuantities> deficits = checkQuantity(productMap, foundProds);
        if (!CollectionUtils.isEmpty(deficits)) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(deficits,
                    "Some products from your shopping cart are not available in the requested quantity.");
        }
        UUID orderId = UUID.fromString(request.getOrderId());

        //сохранить в резерв.
        List<OrderBooking> bookedProducts = productMap.entrySet().stream()
                .map(e -> new OrderBooking(orderId, e.getKey(), null, e.getValue()))
                .toList();
        orderBookingRepository.saveAll(bookedProducts);

        //Уменьшаем доступные остатки
        for (ProductInWarehouse p : products) {
            Integer reserved = productMap.get(p.getProductId());
            if (reserved != null) {
                p.setQuantity(p.getQuantity() - reserved);
            }
        }
        //Сохраняем остатки
        warehouseRepository.saveAll(products);

        return scoreParameters(productMap, products);
    }

    @Transactional(readOnly = true)
    public List<OrderBookingDto> getBooked(Set<UUID> ids) {
        return OrderBookingMapper.mapListBookingToListDto(orderBookingRepository.findByOrderIdIn(ids));
    }

    //Воспользоваться кодом их ревью очень хотелось - как минимум для образца.
    //Разнес проверки и расчеты в три метода - пусть каждый отвечает за конкретную часть
    private List<String> checkExistence(Map<UUID, Integer> tested, Map<UUID, Integer> values) {
        List<String> absent = new ArrayList<>();
        for (UUID id : tested.keySet()) {
            if (!values.containsKey(id)) {
                absent.add(id.toString());
            }
        }
        return absent;
    }

    private Map<String, PairOfQuantities> checkQuantity(Map<UUID, Integer> tested, Map<UUID, Integer> values) {
        Map<String, PairOfQuantities> deficits = new HashMap<>();
        tested.forEach((id, requiredQuantity) -> {
            var availableQuantity = values.get(id);
            if (availableQuantity < requiredQuantity) {
                deficits.put(id.toString(), PairOfQuantities.builder()
                        .wanted(requiredQuantity)
                        .available(availableQuantity)
                        .build());
            }
        });
        return deficits;
    }

    private BookedProductsDto scoreParameters(Map<UUID, Integer> prods, List<ProductInWarehouse> products) {
        var weight = BigDecimal.ZERO;
        var volume = BigDecimal.ZERO;
        var isFragile = false;

        for (ProductInWarehouse product : products) {
            Integer required = prods.get(product.getProductId());
            if (required != null) {
                weight = weight.add(BigDecimal.valueOf(product.getWeight()).multiply(BigDecimal.valueOf(required)));
                volume = volume.add(product.getDimensions().getVolume().multiply(BigDecimal.valueOf(required)));
                isFragile = isFragile || product.isFragile();
            }
        }
        return BookedProductsDto.builder()
                .deliveryWeight(weight)
                .deliveryVolume(volume)
                .fragile(isFragile)
                .build();
    }
}
