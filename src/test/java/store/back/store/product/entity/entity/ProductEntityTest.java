package store.back.store.product.entity.entity;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProductEntityTest {

    @Test
    void createProductEntity_Success() {
        // given
        String name = "콜라";
        int price = 1000;
        int quantity = 10;
        String promotionName = "탄산2+1";

        // when
        ProductEntity productEntity = new ProductEntity(name, price, quantity, promotionName);

        // then
        Assertions.assertThat(productEntity.getName()).isEqualTo(name);
        Assertions.assertThat(productEntity.getPrice()).isEqualTo(price);
        Assertions.assertThat(productEntity.getQuantity()).isEqualTo(quantity);
        Assertions.assertThat(productEntity.getPromotionName().get()).isEqualTo(promotionName);
    }


    @ParameterizedTest
    @MethodSource("invalidCreateProductEntitySource")
    void createProductEntity_Fail(String name, int price, int quantity, String promotionName) {
        // when, then
        Assertions.assertThatThrownBy(() -> new ProductEntity(name, price, quantity, promotionName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidCreateProductEntitySource() {
        return Stream.of(
                Arguments.of("", 1000,  10, "탄산2+1"),
                Arguments.of("콜라", 0,  10, "탄산2+1"),
                Arguments.of("콜라", 1000,  -1, "탄산2+1")
        );
    }

}