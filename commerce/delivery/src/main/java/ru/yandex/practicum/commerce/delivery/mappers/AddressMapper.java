package ru.yandex.practicum.commerce.delivery.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.delivery.entity.Address;
import ru.yandex.practicum.dto.AddressDto;

@Component
public class AddressMapper {

    public static AddressDto mapAddressToDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }

    public static Address mapDtoToAddress(AddressDto dto) {
        return Address.builder()
                .country(dto.getCountry())
                .city(dto.getCity())
                .street(dto.getStreet())
                .house(dto.getHouse())
                .flat(dto.getFlat())
                .build();
    }
}
