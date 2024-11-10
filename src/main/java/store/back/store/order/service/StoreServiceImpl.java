package store.back.store.order.service;

import java.util.List;
import store.back.global.annotation.Service;
import store.back.store.product.domain.Products;
import store.back.store.storage.domain.Storage;
import store.global.dto.response.CurrentStorageResponseDTO;
import store.global.dto.response.CurrentStorageResponseDTOs;

@Service
public class StoreServiceImpl implements StoreService {

    private final Products products;

    private final Storage storage;

    public StoreServiceImpl(Products products, Storage storage) {
        this.products = products;
        this.storage = storage;
    }

    @Override
    public CurrentStorageResponseDTOs currentProductInform() {
        List<CurrentStorageResponseDTO> informs = storage.getProductPromotionInforms().stream().map(inform -> {
            String productName = inform.getProductName();
            Integer price = products.findByName(productName).getPrice();
            return new CurrentStorageResponseDTO(productName, price, inform.getQuantity(), inform.getPromotionName());
        }).toList();

        return new CurrentStorageResponseDTOs(informs);
    }
}
