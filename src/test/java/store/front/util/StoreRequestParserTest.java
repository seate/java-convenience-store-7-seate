package store.front.util;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.global.dto.request.OrderedItem;
import store.global.exception.CustomIllegalArgumentException;

class StoreRequestParserTest {

    @Test
    void 정상_파싱() {
        // given
        String input = "[콜라-3],[에너지바-5],[컵라면-10]";

        // when
        List<OrderedItem> orderedItems = StoreRequestParser.parseOrder(input);// 올바른 형식

        // then
        OrderedItem first = orderedItems.getFirst();
        Assertions.assertThat(first.productName()).isEqualTo("콜라");
        Assertions.assertThat(first.quantity()).isEqualTo(3);
        OrderedItem second = orderedItems.get(1);
        Assertions.assertThat(second.productName()).isEqualTo("에너지바");
        Assertions.assertThat(second.quantity()).isEqualTo(5);
        OrderedItem third = orderedItems.get(2);
        Assertions.assertThat(third.productName()).isEqualTo("컵라면");
        Assertions.assertThat(third.quantity()).isEqualTo(10);
    }

    @ParameterizedTest
    @ValueSource(strings = {"[콜라-3", "[에너지바5]"})
    void 파싱_예외(String input) {
        Assertions.assertThatThrownBy(() -> StoreRequestParser.parseOrder(input))
                .isInstanceOf(CustomIllegalArgumentException.class);
    }
}