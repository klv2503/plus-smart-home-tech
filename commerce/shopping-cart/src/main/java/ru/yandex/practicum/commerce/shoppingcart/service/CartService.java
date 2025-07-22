package ru.yandex.practicum.commerce.shoppingcart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.shoppingcart.client.CartWarehouseFeign;
import ru.yandex.practicum.commerce.shoppingcart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.shoppingcart.entity.CartState;
import ru.yandex.practicum.commerce.shoppingcart.entity.ShopCart;
import ru.yandex.practicum.commerce.shoppingcart.entity.ShopCartItem;
import ru.yandex.practicum.commerce.shoppingcart.mappers.ShopCartMapper;
import ru.yandex.practicum.commerce.shoppingcart.repository.ShopCartRepository;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.CartSpecialDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.exceptions.errors.OperationNotAllowedException;
import ru.yandex.practicum.exceptions.errors.ProductNotFoundException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartService {

    private final ShopCartRepository cartRepository;
    private final CartWarehouseFeign client;


    public ShopCart createNewCart(UUID cartId, String userName) {
        return ShopCart.builder()
                .cartId(cartId)
                .username(userName)
                .state(CartState.ACTIVE)
                .items(new ArrayList<>())
                .build();
    }

    public ShopCart getCart(UUID id) {
        return cartRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Cart", id));
    }

    public ShoppingCartDto getCartByUserName(String userName) {
        ShopCart cart = cartRepository.findByUsername(userName).orElse(null);
        if (cart == null) {
            UUID cartId = UUID.randomUUID();
            cart = createNewCart(cartId, userName);
            cartRepository.save(cart);
        }
        ShoppingCartDto userCart = ShopCartMapper.shopCartToShoppingCartDto(cart);
        log.info("\nCartService.getCartByUserName: {}", userCart);
        return userCart;
    }

    public List<ShoppingCartDto> findCarts(String userName) {
        //метод добавлен для возможности контроля состояния БД. Если userName не задан, то получаем все корзины
        List<ShopCart> carts = Strings.isEmpty(userName) ? cartRepository.findAll() :
                cartRepository.findAllByUsername(userName);
        return ShopCartMapper.cartListToDtoList(carts);
    }

    public ShoppingCartDto addProductInCart(String userName, Map<String, Integer> products) {
        ShopCart cart = cartRepository.findByUsername(userName).orElse(null);
        if (cart == null) {
            UUID cartId = UUID.randomUUID();
            cart = createNewCart(cartId, userName);
        }
        log.info("\nCartService.addProductInCart: before adding {}", cart);
        if (cart.getState().equals(CartState.DEACTIVATED)) {
            throw new OperationNotAllowedException(cart.getCartId(), "Attempt of adding products to deactivated cart",
                    "Adding of products to deactivated cart is prohibited");
        }
        ShoppingCartDto cartDto = ShoppingCartDto.builder()
                .shoppingCartId(cart.getCartId().toString())
                .products(ShopCartMapper.mapCartItemsToProductMap(cart.getItems()))
                .build();
        Map<UUID, Integer> cartProds = cartDto.getProducts();
        for (String ids : products.keySet()) {
            cartProds.put(UUID.fromString(ids), products.get(ids));
        }
        cartDto.setProducts(cartProds);
        BookedProductsDto productsData = client.checkCart(cartDto).getBody();
        List<ShopCartItem> items = cartDto.getProducts().entrySet().stream()
                .map(entry -> ShopCartItem.builder()
                        .productCode(entry.getKey())
                        .quantity(entry.getValue())
                        .build())
                .toList();
        cart.addItems(items);
        cartRepository.save(cart);

        return cartDto;
    }

    public void deactivateCart(String userName) {
        ShopCart cart = cartRepository.findByUsername(userName).orElse(null);
        if (cart == null) {
            UUID cartId = UUID.randomUUID();
            cart = createNewCart(cartId, userName);
        }
        cart.setState(CartState.DEACTIVATED);
        cartRepository.save(cart);
    }

    public ShoppingCartDto removeFromCart(String userName, List<String> ids) {
        ShopCart cart = cartRepository.findByUsername(userName).orElse(null);
        if (cart == null) {
            UUID cartId = UUID.randomUUID();
            cart = createNewCart(cartId, userName);
        }

        List<ShopCartItem> erasedProducts = cart.getItems().stream()
                .filter(item -> ids.contains(item.getProductCode().toString()))
                .toList();
        cart.removeItems(erasedProducts);
        ShoppingCartDto renewedCart = ShoppingCartDto.builder()
                .shoppingCartId(cart.getCartId().toString())
                .products(ShopCartMapper.mapCartItemsToProductMap(cart.getItems()))
                .build();
        log.info("\nCartService.removeFromCart: {}", renewedCart);
        return renewedCart;
    }

    public ShoppingCartDto changeProductQuantity(String userName, ChangeProductQuantityRequest request) {
        ShopCart cart = cartRepository.findByUsername(userName).orElse(null);
        if (cart == null) {
            UUID cartId = UUID.randomUUID();
            cart = createNewCart(cartId, userName);
        }
        ShoppingCartDto cartDto = ShoppingCartDto.builder()
                .shoppingCartId(cart.getCartId().toString())
                .products(ShopCartMapper.mapCartItemsToProductMap(cart.getItems()))
                .build();
        cartDto.getProducts().put(UUID.fromString(request.getProductId()), request.getNewQuantity());
        BookedProductsDto productsData = client.checkCart(cartDto).getBody();
        List<ShopCartItem> items = cartDto.getProducts().entrySet().stream()
                .map(entry -> ShopCartItem.builder()
                        .productCode(entry.getKey())
                        .quantity(entry.getValue())
                        .build())
                .toList();
        cart.addItems(items);
        cartRepository.save(cart);
        return cartDto;
    }

    public CartSpecialDto getCartById(UUID cartId) {
        ShopCart cart = getCart(cartId);
        return ShopCartMapper.cartToCartSpecial(cart);
    }
}
