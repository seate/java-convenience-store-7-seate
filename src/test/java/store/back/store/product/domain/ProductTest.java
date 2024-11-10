package store.back.store.product.domain;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.back.store.product.entity.entity.ProductEntity;

class ProductTest {

    @Test
    void createProduct_Success() {
        // given
        String name = "콜라";
        int price = 1000;
        ProductEntity productEntity = new ProductEntity(name, price, 1, "promotionName");

        // when
        Product product = Product.from(productEntity);

        // then
        Assertions.assertThat(product.getName()).isEqualTo(name);
        Assertions.assertThat(product.getPrice()).isEqualTo(price);
    }


    @ParameterizedTest
    @MethodSource("invalidProductSource")
    void createProduct_Fail(String name, int price) {
        // given

        // when, then
        Assertions.assertThatThrownBy(() -> {
            ProductEntity productEntity = new ProductEntity(name, price, 1, "promotionName");
            Product.from(productEntity);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidProductSource() {
        return Stream.of(
                Arguments.of("", 1000),
                Arguments.of("초코바", 0)
        );
    }

}