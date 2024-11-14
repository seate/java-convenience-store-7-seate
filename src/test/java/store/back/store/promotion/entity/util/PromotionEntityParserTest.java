package store.back.store.promotion.entity.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.back.store.promotion.entity.entity.PromotionEntity;

class PromotionEntityParserTest {

    @ParameterizedTest
    @MethodSource("entitySource")
    void parseEntity(String entity, PromotionEntity expected) {

        PromotionEntity promotionEntity = PromotionEntityParser.parseEntity(entity);

        Assertions.assertThat(promotionEntity).usingRecursiveComparison().isEqualTo(expected);
    }

    private static Stream<Arguments> entitySource() {
        return Stream.of(
                Arguments.of("탄산2+1,2,1,2024-01-01,2024-12-31",
                        new PromotionEntity("탄산2+1", 2, 1,
                                LocalDate.of(2024, 1, 1).atStartOfDay(),
                                LocalDate.of(2024, 12, 31).atTime(LocalTime.MAX))
                ),
                Arguments.of("반짝할인,1,1,2024-11-01,2024-11-30",
                        new PromotionEntity("반짝할인", 1, 1,
                                LocalDate.of(2024, 11, 1).atStartOfDay(),
                                LocalDate.of(2024, 11, 30).atTime(LocalTime.MAX))
                ),
                Arguments.of("MD추천상품,1,1,2024-01-01,2024-12-31",
                        new PromotionEntity("MD추천상품", 1, 1,
                                LocalDate.of(2024, 1, 1).atStartOfDay(),
                                LocalDate.of(2024, 12, 31).atTime(LocalTime.MAX))
                )
        );
    }
}