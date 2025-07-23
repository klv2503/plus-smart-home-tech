package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 70)
    private String country;

    @NotBlank
    @Size(min = 3, max = 70)
    private String city;

    @NotBlank
    @Size(min = 3, max = 200)
    private String street;

    @NotBlank
    @Size(min = 1, max = 15)
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
