package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.security.SecureRandom;
import java.util.Random;

@Data
@AllArgsConstructor
@Builder
public class AddressDto {

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String street;

    @NotBlank
    private String house;

    private String flat;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    public AddressDto() {
        this.country = CURRENT_ADDRESS;
        this.city = CURRENT_ADDRESS;
        this.street = CURRENT_ADDRESS;
        this.house = CURRENT_ADDRESS;
        this.flat = CURRENT_ADDRESS;
    }
}
