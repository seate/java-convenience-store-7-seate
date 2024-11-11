package store.back.store.order.domain;

import java.util.List;
import java.util.UUID;
import store.back.store.product.domain.Product;
import store.back.store.product.domain.Products;
import store.back.store.promotion.domain.Promotions;
import store.back.store.storage.domain.Storage;
import store.global.dto.request.OrderedItem;

public class Order {

    private final UUID uuid;

    private final List<OrderLine> orderLines;

    public Order(Products products, Promotions promotions, Storage storage,
                 List<OrderedItem> orderedItems) {
        this.uuid = UUID.randomUUID();
        this.orderLines = orderedItems.stream().map(orderedItem -> {
            Product product = products.findByName(orderedItem.productName());
            return storage.findPromotionNameByProductName(product.getName())
                    .map(name -> OrderLine.createPromotionedOrderLine(product, promotions.findByName(name), storage,
                            orderedItem))
                    .orElseGet(() -> OrderLine.createNotPromotionedOrderLine(product, storage, orderedItem));
        }).toList();
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
}
