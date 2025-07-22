package ru.yandex.practicum.commerce.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.delivery.entity.Delivery;

import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {


}
