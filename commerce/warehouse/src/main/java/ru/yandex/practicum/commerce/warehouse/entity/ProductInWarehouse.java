package ru.yandex.practicum.commerce.warehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.commerce.warehouse.dto.DimensionDto;

import java.util.UUID;

@Entity
@Table(name = "warehouse")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInWarehouse {

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "is_fragile")
    private boolean isFragile;

    @Column(name = "quantity")
    private int quantity;

    @Embedded
    private DimensionDto dimensions;

    @Column(name = "weight")
    private double weight;
}
