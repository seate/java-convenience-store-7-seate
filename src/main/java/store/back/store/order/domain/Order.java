package store.back.store.order.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import store.back.store.order.exception.NoProductInOrderException;
import store.back.store.order.exception.OrderQuantityUnderZeroException;
import store.back.store.product.domain.Product;
import store.back.store.product.domain.Products;
import store.back.store.promotion.domain.Promotions;
import store.back.store.storage.domain.Storage;
import store.global.dto.request.OrderedItem;

public class Order {

    private final UUID uuid;

    private final List<OrderLine> orderLines;


    public Order(Products products, Promotions promotions, Storage storage, List<OrderedItem> orderedItems) {
        this.uuid = UUID.randomUUID();

        List<OrderLine> orderLines = generateOrderLines(products, promotions, storage, orderedItems);
        validateOrderLines(orderLines);
        this.orderLines = orderLines;
    }

    private List<OrderLine> generateOrderLines(Products products, Promotions promotions, Storage storage,
                                              List<OrderedItem> orderedItems) {
        return orderedItems.stream().map(orderedItem -> {
            try {
                Product product = products.findByName(orderedItem.productName());
                return storage.findPromotionNameByProductName(product.getName())
                        .map(name -> OrderLine.createPromotionedOrderLine(product, promotions.findByName(name), storage,
                                orderedItem))
                        .orElseGet(() -> OrderLine.createNotPromotionedOrderLine(product, storage, orderedItem));
            } catch (OrderQuantityUnderZeroException e) {
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }

    private void validateOrderLines(List<OrderLine> orderLines) {
        if (orderLines.isEmpty()) {
            throw new NoProductInOrderException();
        }
    }


    public UUID getUuid() {
        return uuid;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
}
