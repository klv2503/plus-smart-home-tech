package ru.yandex.practicum.commerce.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.shoppingcart.entity.CartState;
import ru.yandex.practicum.commerce.shoppingcart.entity.ShopCart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopCartRepository extends JpaRepository<ShopCart, UUID> {

    Optional<ShopCart> findByUsername(String username);

    List<ShopCart> findAllByUsername(String username);

    List<ShopCart> findByUsernameAndState(String username, CartState state);

}
