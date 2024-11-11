package store.back.store.order.util;

import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.back.store.order.exception.NeedMorePromotedProductException;
import store.back.store.storage.exception.LackProductException;
import store.back.store.storage.exception.LackPromotedProductException;
import store.global.dto.request.OrderedItem;

class OrderQuantityCalculatorTest {

    @ParameterizedTest
    @MethodSource("calculateSource")
    void calculate(int requestQuantity, int promotedQuantity, int notPromotedQuantity, int buyQuantity,
                   int freeQuantity, String productName, Boolean isPromoted, Boolean lackAgreement,
                   Boolean fillLackQuantity, Boolean checkedFillable, Boolean checkedLackable,
                   CalculatedQuantities expected) {
        // when
        CalculatedQuantities calculated = OrderQuantityCalculator.calculate(promotedQuantity,
                notPromotedQuantity, buyQuantity, freeQuantity, isPromoted, new OrderedItem(productName,
                        requestQuantity, lackAgreement, fillLackQuantity, checkedFillable, checkedLackable));

        // then
        Assertions.assertThat(calculated).isEqualTo(expected);
    }

    private static Stream<Arguments> calculateSource() {
        return Stream.of(
                Arguments.of(2, 3, 1, 1, 1, "콜라", true, false, true, false, false, new CalculatedQuantities(2, 2, 1)),
                Arguments.of(3, 4, 1, 2, 1, "콜라", true, false, true, false, false, new CalculatedQuantities(3, 3, 1)),
                Arguments.of(4, 3, 1, 1, 1, "콜라", true, true, true, true, true, new CalculatedQuantities(4, 2, 1)),
                Arguments.of(3, 4, 1, 1, 1, "콜라", true, false, true, true, false, new CalculatedQuantities(4, 4, 2)),
                Arguments.of(5, 10, 10, 1, 1, "콜라", false, false, false, false, false, new CalculatedQuantities(5, 0, 0))
        );
    }


    @ParameterizedTest
    @MethodSource("invalidCalculateSource")
    void calculate_Fail(int requestQuantity, int promotedQuantity, int notPromotedQuantity, int buyQuantity,
                        int freeQuantity, String productName, Boolean isPromoted, Boolean lackAgreement,
                        Boolean fillLackQuantity, Boolean checkedFillable, Boolean checkedLackable,
                        Class<? extends Exception> expectedExceptionClass) {
        // when
        Assertions.assertThatThrownBy(() -> OrderQuantityCalculator.calculate(promotedQuantity,
                notPromotedQuantity, buyQuantity, freeQuantity, isPromoted, new OrderedItem(productName,
                        requestQuantity, lackAgreement, fillLackQuantity, checkedFillable, checkedLackable)))
                .isInstanceOf(expectedExceptionClass);
    }

    private static Stream<Arguments> invalidCalculateSource() {
        return Stream.of(
                Arguments.of(2, 3, 1, 2, 1, "콜라", true, false, true, false, false,
                        NeedMorePromotedProductException.class),
                Arguments.of(6, 4, 1, 2, 1, "콜라", true, false, true, false, false,
                        LackProductException.class),
                Arguments.of(4, 3, 1, 1, 1, "콜라", true, false, true, false, false,
                        LackPromotedProductException.class)
        );
    }
}