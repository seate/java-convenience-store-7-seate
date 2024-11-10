package store.back.store.order.domain;

import store.back.store.product.domain.Product;
import store.back.store.order.exception.NoProductInOrderException;

public class OrderProduct {

    private final String name;

    private final Integer price;

    private final Integer totalQuantity;

    private final Integer promotedQuantity;

    private final Integer freeQuantity;

    private OrderProduct(String name, Integer price, Integer totalQuantity, Integer promotedQuantity,
                         Integer freeQuantity) {
        this.name = name;
        this.price = price;
        validateQuantity(totalQuantity);
        this.totalQuantity = totalQuantity;
        this.promotedQuantity = promotedQuantity;
        this.freeQuantity = freeQuantity;
    }

    private void validateQuantity(final Integer totalQuantity) {
        if (totalQuantity <= 0) {
            throw new NoProductInOrderException();
        }
    }

    public static OrderProduct from(Product product, Integer quantity, Integer promotedQuantity, Integer freeQuantity) {
        return new OrderProduct(product.getName(), product.getPrice(), quantity, promotedQuantity, freeQuantity);
    }


    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public Integer getPromotedQuantity() {
        return promotedQuantity;
    }

    public Integer getFreeQuantity() {
        return freeQuantity;
    }
}
