package ru.yandex.practicum.commerce.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.payment.entity.Payment;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByOrderId(UUID orderId);
}
