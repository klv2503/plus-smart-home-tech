package ru.yandex.practicum.commerce.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.shoppingstore.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.shoppingstore.entities.Product;
import ru.yandex.practicum.commerce.shoppingstore.mapper.ProductMapper;
import ru.yandex.practicum.commerce.shoppingstore.repository.ProductRepository;
import ru.yandex.practicum.dto.ShortProduct;
import ru.yandex.practicum.enums.*;
import ru.yandex.practicum.exceptions.errors.ProductNotFoundException;
import ru.yandex.practicum.utiliteis.EnumUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShoppingStoreService {

    private final ProductRepository repository;

    public Product getProduct(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product", id));
    }

    public Page<ProductDto> getProductList(ProductCategory category, Pageable pageable) {
        SortParam param =
                (pageable.getSort() == null) ? SortParam.UNSORTED : SortParam.valueOf(pageable.getSort().toUpperCase());
        Page<Product> products = switch (param) {
            case PRODUCTNAME -> {
                Sort sort = Sort.by(Sort.Direction.ASC, "productName");
                org.springframework.data.domain.Pageable page =
                        PageRequest.of(pageable.getPage(), pageable.getSize(), sort);
                yield repository.findByProductCategory(category, page);
            }
            case PRICE -> {
                Sort sort = Sort.by(Sort.Direction.ASC, "price");
                org.springframework.data.domain.Pageable page =
                        PageRequest.of(pageable.getPage(), pageable.getSize(), sort);
                yield repository.findByProductCategory(category, page);
            }
            default -> {
                org.springframework.data.domain.Pageable page =
                        PageRequest.of(pageable.getPage(), pageable.getSize(), Sort.unsorted());
                yield repository.findByProductCategory(category, page);
            }
        };
        return products.map(ProductMapper::mapProductToDto);
        //return ProductMapper.mapProductListToDtoList(products.getContent());
    }

    public ProductDto addNewProduct(ProductDto productDto) {
        UUID prodId = UUID.randomUUID();
        productDto.setProductId(prodId.toString());
        Product product = ProductMapper.mapDtoToProduct(productDto);
        repository.save(product);
        log.info("\nShoppingStoreService.addNewProduct: {}", product);
        return ProductMapper.mapProductToDto(product);
    }

    public ProductDto updateProduct(ProductDto dto) {
        UUID prodId = UUID.fromString(dto.getProductId());
        Product product = getProduct(prodId);
        if (Strings.isNotEmpty(dto.getProductName()))
            product.setProductName(dto.getProductName());
        if (Strings.isNotEmpty(dto.getDescription()))
            product.setDescription(dto.getDescription());
        if (Strings.isNotEmpty(dto.getImageSrc()))
            product.setImageSrc(dto.getImageSrc());
        if (dto.getProductCategory() != null)
            product.setProductCategory(EnumUtils.fromStringOrThrow(dto.getProductCategory(), ProductCategory.class));
        if (dto.getProductState() != null)
            product.setProductState(EnumUtils.fromStringOrThrow(dto.getProductState(), ProductState.class));
        if (dto.getQuantityState() != null)
            product.setQuantityState(EnumUtils.fromStringOrThrow(dto.getQuantityState(), QuantityState.class));
        if (dto.getPrice() != null)
            product.setPrice(dto.getPrice());
        repository.save(product);
        return dto;
    }

    public Boolean removeProduct(String productId) {
        UUID prodId = UUID.fromString(productId);
        Product product = getProduct(prodId);
        product.setProductState(ProductState.DEACTIVATE);
        repository.save(product);
        return true;
    }

    public Boolean setProductQuantity(SetProductQuantityStateRequest request) {
        UUID prodId = UUID.fromString(request.getProductId());
        Product product = getProduct(prodId);
        product.setQuantityState(EnumUtils.fromStringOrThrow(request.getQuantityState(), QuantityState.class));
        repository.save(product);
        return true;
    }

    public ProductDto getProductDetails(String productId) {
        UUID prodId = UUID.fromString(productId);
        Product product = getProduct(prodId);
        return ProductMapper.mapProductToDto(product);
    }

    public List<ShortProduct> getProductByIds(List<UUID> ids) {
        return ProductMapper.mapProductListToShorts(repository.findByProductIdIn(ids));
    }
}
