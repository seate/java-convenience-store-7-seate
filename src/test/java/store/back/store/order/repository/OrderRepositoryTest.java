package store.back.store.order.repository;

import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.back.store.order.domain.Order;
import store.global.dto.ProductNameQuantity;
import store.back.store.product.domain.Products;
import store.back.store.product.entity.repository.ProductEntityRepository;
import store.back.store.promotion.domain.Promotions;
import store.back.store.promotion.entity.repository.PromotionEntityRepository;
import store.back.store.storage.domain.Storage;

class OrderRepositoryTest {

    private OrderRepository orderRepository = new OrderRepository();

    private final ProductEntityRepository productEntityRepository = new ProductEntityRepository();

    private final Products products = new Products(productEntityRepository);

    private final Promotions promotions = new Promotions(new PromotionEntityRepository());

    private final Storage storage = new Storage(productEntityRepository);

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();
    }


    @Test
    void 정상_저장() {
        // given
        List<ProductNameQuantity> productNameQuantities = List.of(
                new ProductNameQuantity("콜라", 3),
                new ProductNameQuantity("사이다", 3)
        );
        Order order = new Order(products, promotions, storage, productNameQuantities, false, true, false, false);
        UUID uuid = order.getUuid();

        // when
        orderRepository.save(order);
        Order savedOrder = orderRepository.findById(uuid);

        // then
        Assertions.assertThat(savedOrder).usingRecursiveComparison().isEqualTo(order);
    }
}