package store.back.store.order.domain;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import store.back.global.support.ApplicationContext;
import store.back.store.order.exception.NoProductInOrderException;
import store.back.store.product.domain.Products;
import store.back.store.promotion.domain.Promotions;
import store.back.store.storage.domain.Storage;
import store.global.dto.request.OrderedItem;

class OrderTest {

    static Products products;

    static Promotions promotions;

    static Storage storage;

    @BeforeAll
    static void setUp() {
        ApplicationContext.init();

        products = ApplicationContext.getBean(Products.class);
        promotions = ApplicationContext.getBean(Promotions.class);
        storage = ApplicationContext.getBean(Storage.class);
    }

    @Test
    void emptyOrderLine() {
        // given
        List<OrderedItem> orderedItems = List.of();

        // when, then
        Assertions.assertThatThrownBy(() -> new Order(products, promotions, storage, orderedItems))
                .isInstanceOf(NoProductInOrderException.class);
    }

}