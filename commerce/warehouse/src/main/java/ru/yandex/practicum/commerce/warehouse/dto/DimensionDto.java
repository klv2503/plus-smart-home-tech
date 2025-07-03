package ru.yandex.practicum.commerce.warehouse.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class DimensionDto {

    @Min(1)
    @NotNull
    @Column(name = "width")
    private BigDecimal width;

    @Min(1)
    @NotNull
    @Column(name = "height")
    private BigDecimal height;

    @Min(1)
    @NotNull
    @Column(name = "depth")
    private BigDecimal depth;

    public BigDecimal getVolume() {
        return this.width.multiply(this.height).multiply(this.depth);
    }
}
