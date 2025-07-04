package ru.yandex.practicum.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.warehouse.entity.ProductInWarehouse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<ProductInWarehouse, UUID> {
    ProductInWarehouse findByProductId(UUID productId);

    Long findIdByProductId(UUID productId);

    List<ProductInWarehouse> findByProductIdIn(Set<UUID> productIds);
}
