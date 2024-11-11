package store.back.store.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.back.store.order.exception.OrderQuantityUnderZeroException;
import store.back.store.product.domain.Product;
import store.back.store.product.entity.entity.ProductEntity;

class OrderProductTest {

    @Test
    void from() {
        // given
        String name = "name";
        int price = 1000;
        int quantity = 10;
        int promotedQuantity = 6;
        int freeQuantity = 2;
        String promotionName = "promotionName";

        ProductEntity productEntity = new ProductEntity(name, price, quantity, promotionName);
        Product product = Product.from(productEntity);

        Assertions.assertThatThrownBy(() -> OrderProduct.from(product, 0, promotedQuantity, freeQuantity))
                .isInstanceOf(OrderQuantityUnderZeroException.class);
    }
}