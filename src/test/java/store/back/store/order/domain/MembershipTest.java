package store.back.store.order.domain;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MembershipTest {

    @ParameterizedTest
    @MethodSource("provideMembership")
    void getDiscount(Integer membershipAblePrice, Integer expected) {
        Integer discount = Membership.getDiscount(membershipAblePrice);
        Assertions.assertEquals(expected, discount);
    }

    private static Stream<Arguments> provideMembership() {
        return Stream.of(
                Arguments.of(10000000, 8000),
                Arguments.of(1000, 300)
        );
    }
}