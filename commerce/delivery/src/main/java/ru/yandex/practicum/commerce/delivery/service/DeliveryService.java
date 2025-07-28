package ru.yandex.practicum.commerce.delivery.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.delivery.client.DeliveryOrderFeign;
import ru.yandex.practicum.commerce.delivery.client.DeliveryWarehouseFeign;
import ru.yandex.practicum.commerce.delivery.entity.Address;
import ru.yandex.practicum.commerce.delivery.mappers.AddressMapper;
import ru.yandex.practicum.commerce.delivery.mappers.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.repository.AddressRepository;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.commerce.delivery.entity.Delivery;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.exceptions.errors.NoDeliveryFoundException;
import ru.yandex.practicum.exceptions.errors.NotEnoughInfoInOrderToCalculateException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final DeliveryWarehouseFeign warehouseFeign;
    private final DeliveryOrderFeign orderFeign;

    @Transactional(readOnly = true)
    public Delivery getDelivery(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException(deliveryId));
    }

    public Address getOrCreateAddress(Address input) {
        return addressRepository.findByCountryAndCityAndStreetAndHouseAndFlat(
                input.getCountry(),
                input.getCity(),
                input.getStreet(),
                input.getHouse(),
                input.getFlat()
        ).orElseGet(() -> addressRepository.save(input));
    }

    public DeliveryDto planDelivery(DeliveryDto request) {
        Delivery delivery = Delivery.builder()
                .deliveryId(UUID.randomUUID())
                .fromAddress(getOrCreateAddress(AddressMapper.mapDtoToAddress(request.getFromAddress())))
                .toAddress(getOrCreateAddress(AddressMapper.mapDtoToAddress(request.getToAddress())))
                .orderId(UUID.fromString(request.getOrderId()))
                .deliveryState(DeliveryState.CREATED)
                .build();
        deliveryRepository.save(delivery);
        return DeliveryMapper.mapDeliveryToDto(delivery);
    }

    public void makeDeliverySuccessful(String id) {
        UUID deliveryId = UUID.fromString(id);
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        String orderId = delivery.getOrderId().toString();
        orderFeign.delivery(orderId);
        deliveryRepository.save(delivery);
    }

    public void makeDeliveryPicked(String id) {
        UUID deliveryId = UUID.fromString(id);
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        String orderId = delivery.getOrderId().toString();
        warehouseFeign.shippedToDelivery(ShippedToDeliveryRequest.builder()
                        .orderId(orderId)
                        .deliveryId(id)
                .build());
        orderFeign.assemblyOrder(orderId);
        deliveryRepository.save(delivery);
    }

    public void makeDeliveryFailed(String id) {
        UUID deliveryId = UUID.fromString(id);
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        String orderId = delivery.getOrderId().toString();
        orderFeign.deliveryFailed(orderId);
        deliveryRepository.save(delivery);
    }

    public BigDecimal deliveryCost(OrderDto order) {
        if (order.getFragile() == null
                || order.getDeliveryWeight() == null
                || order.getDeliveryVolume() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(UUID.fromString(order.getOrderId()));
        }
        UUID deliveryId = UUID.fromString(order.getDeliveryId());
        Delivery delivery = getDelivery(deliveryId);
        var cost = BigDecimal.valueOf(5);
        cost = delivery.getFromAddress().getStreet().equals("ADDRESS_1") ?
                cost.add(cost) :
                cost.add(cost.multiply(BigDecimal.valueOf(2)));
        if (order.getFragile()) {
            cost = cost.add(cost.multiply(BigDecimal.valueOf(0.2)));
        }
        cost = cost.add(order.getDeliveryWeight().multiply(BigDecimal.valueOf(0.3)));
        cost = cost.add(order.getDeliveryVolume().multiply(BigDecimal.valueOf(0.2)));
        cost = (delivery.getFromAddress().getId().equals(delivery.getToAddress().getId())) ?
                cost : cost.add(cost.multiply(BigDecimal.valueOf(0.2)));
        return cost;
    }
}
