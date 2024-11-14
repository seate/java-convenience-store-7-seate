package store.back.support;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.back.global.support.ApplicationContext;
import store.back.global.support.BeanConfig;
import store.back.store.order.controller.OrderController;
import store.back.store.order.repository.OrderRepository;
import store.back.store.order.service.StoreService;
import store.back.store.product.entity.repository.ProductEntityRepository;
import store.back.store.promotion.entity.repository.PromotionEntityRepository;

class BeanConfigTest {

    @BeforeAll
    static void setUp() {
        ApplicationContext.init();
    }

    @ParameterizedTest
    @ValueSource(classes = {OrderController.class, StoreService.class, OrderRepository.class,
            ProductEntityRepository.class, PromotionEntityRepository.class})
    void 빈_생성_테스트(Class<?> beanClass) {
        // given
        Object bean = BeanConfig.getBean(beanClass);

        // when, then
        Assertions.assertThat(bean)
                .isNotNull()
                .isInstanceOf(beanClass);
    }
}