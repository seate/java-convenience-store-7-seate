package store.back.store.order.domain;

import store.back.store.order.util.OrderQuantityCalculator;
import store.back.store.order.util.CalculatedQuantities;
import store.back.store.product.domain.Product;
import store.back.store.promotion.domain.Promotion;
import store.back.store.storage.domain.Storage;
import store.global.dto.request.OrderedItem;

public class OrderLine {

    private final OrderProduct orderProduct;

    private OrderLine(Product product, Integer totalQuantity,
                      Integer promotedQuantity, Integer freeQuantity) {
        this.orderProduct = OrderProduct.from(product, totalQuantity, promotedQuantity, freeQuantity);
    }

    public static OrderLine createPromotionedOrderLine(Product product, Promotion promotion,
                                                       Storage storage, OrderedItem orderedItem) {
        Integer promotedQuantity = storage.getQuantity(product.getName(), true);
        Integer notPromotedQuantity = storage.getQuantity(product.getName(), false);

        CalculatedQuantities calculatedQuantities = OrderQuantityCalculator.calculate(promotedQuantity,
                notPromotedQuantity, promotion.getBuyQuantity(), promotion.getFreeQuantity(), promotion.isValid(),
                orderedItem);

        return new OrderLine(product,
                calculatedQuantities.totalQuantity(), calculatedQuantities.promotedQuantity(),
                calculatedQuantities.freeQuantity());
    }

    public static OrderLine createNotPromotionedOrderLine(Product product, Storage storage, OrderedItem orderedItem) {
        Integer promotedQuantity = storage.getQuantity(product.getName(), true);
        Integer notPromotedQuantity = storage.getQuantity(product.getName(), false);

        CalculatedQuantities calculatedQuantities = OrderQuantityCalculator.calculate(promotedQuantity,
                notPromotedQuantity, 1, 0, false, orderedItem);

        return new OrderLine(product, calculatedQuantities.totalQuantity(), calculatedQuantities.promotedQuantity(),
                calculatedQuantities.freeQuantity());
    }

    public OrderProduct getOrderProduct() {
        return orderProduct;
    }

}
