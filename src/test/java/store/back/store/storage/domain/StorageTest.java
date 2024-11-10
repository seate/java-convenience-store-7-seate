package store.back.store.storage.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.back.store.product.entity.repository.ProductEntityRepository;

class StorageTest {

    private ProductEntityRepository productEntityRepository = new ProductEntityRepository();
    private Storage storage = new Storage(productEntityRepository);

    @BeforeEach
    void setUp() {
        productEntityRepository = new ProductEntityRepository();
        storage = new Storage(productEntityRepository);
    }


    @ParameterizedTest
    @MethodSource("quantitySource")
    void getQuantity(String productName, Boolean isPromotion, Integer quantity) {
        // when
        Integer count = storage.getQuantity(productName, isPromotion);

        // then
        assertEquals(quantity, count);
    }

    private static Stream<Arguments> quantitySource() {
        return Stream.of(
                Arguments.of("콜라", true, 10),
                Arguments.of("콜라", false, 10),
                Arguments.of("탄산수", true, 5),
                Arguments.of("탄산수", false, 0)
        );
    }


    @ParameterizedTest
    @MethodSource("productPromotionSource")
    void findPromotionNameByProductName(String productName, String promotionName) {
        // when
        Optional<String> optionalPromotionName = storage.findPromotionNameByProductName(productName);

        // then
        assertEquals(promotionName, optionalPromotionName.orElse(""));
    }

    private static Stream<Arguments> productPromotionSource() {
        return Stream.of(
                Arguments.of("콜라", "탄산2+1"),
                Arguments.of("초코바", "MD추천상품"),
                Arguments.of("물", "")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"콜라", "탄산수", "물"})
    void decreaseQuantity(String productName) {
        // given
        int before = storage.getQuantity(productName, true) + storage.getQuantity(productName, false);

        int decreaseQuantity = before / 2;

        // when
        storage.decreaseQuantity(productName, decreaseQuantity);

        // then
        int after = storage.getQuantity(productName, true) + storage.getQuantity(productName, false);

        assertEquals(before - decreaseQuantity, after);
    }
}