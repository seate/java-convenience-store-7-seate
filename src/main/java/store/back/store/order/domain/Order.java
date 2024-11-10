package store.back.store.order.domain;

import java.util.List;
import java.util.UUID;
import store.back.store.product.domain.Product;
import store.back.store.product.domain.Products;
import store.back.store.promotion.domain.Promotions;
import store.back.store.storage.domain.Storage;

public class Order {

    private final UUID uuid;

    private final List<OrderLine> orderLines;

    public Order(Products products, Promotions promotions, Storage storage,
                 List<ProductNameQuantity> productNameQuantities,
                 Boolean lackAgreement, Boolean fillLackQuantity,
                 Boolean checkedFillable, Boolean checkedLackable) {
        this.uuid = UUID.randomUUID();
        this.orderLines = productNameQuantities.stream().map(productNameQuantity -> {
            Product product = products.findByName(productNameQuantity.productName());
            Integer quantity = productNameQuantity.quantity();
            return storage.findPromotionNameByProductName(product.getName())
                    .map(name -> OrderLine.createPromotionedOrderLine(product, promotions.findByName(name),
                            storage, quantity, lackAgreement, fillLackQuantity,
                            checkedFillable, checkedLackable))
                    .orElseGet(() -> OrderLine.createNotPromotionedOrderLine(product, storage, quantity));
        }).toList();
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
}
