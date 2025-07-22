package ru.yandex.practicum.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.warehouse.entity.OrderBooking;
import ru.yandex.practicum.commerce.warehouse.entity.OrderBookingId;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface OrderBookingRepository extends JpaRepository<OrderBooking, OrderBookingId> {
    List<OrderBooking> findAllByOrderId(UUID orderId);

    List<OrderBooking> findByOrderIdIn(Set<UUID> orderIds);
}
