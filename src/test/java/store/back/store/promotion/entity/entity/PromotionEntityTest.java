package store.back.store.promotion.entity.entity;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PromotionEntityTest {

    @ParameterizedTest
    @MethodSource("createInvalidSource")
    void create(String name, int buyCount, int freeCount, LocalDateTime startDate, LocalDateTime endDate) {
        Assertions.assertThatThrownBy(() -> new PromotionEntity(name, buyCount, freeCount, startDate, endDate))
                .isInstanceOf(IllegalStateException.class);
    }

    private static Stream<Arguments> createInvalidSource() {
        return Stream.of(
                Arguments.of("", 2, 1, LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 12, 31, 23, 59)),
                Arguments.of("반짝할인", 0, 1, LocalDateTime.of(2024, 11, 1, 0, 0), LocalDateTime.of(2024, 11, 30, 23, 59)),
                Arguments.of("반짝할인", 1, 0, LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 12, 31, 23, 59)),
                Arguments.of("반짝할인", 1, 0, LocalDateTime.of(2024, 12, 31, 23, 59), LocalDateTime.of(2024, 1, 1, 0, 0))
        );
    }

}