package store.front.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.back.store.order.domain.ProductNameQuantity;
import store.front.util.StoreRequestParser;
import store.global.dto.request.OrderRequestDTO;
import store.global.status.Agreement;

public class StoreRequestView {

    public static OrderRequestDTO purchase() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        List<ProductNameQuantity> productNameQuantities = StoreRequestParser.parseOrder(Console.readLine());
        System.out.println();

        return OrderRequestDTO.createDefault(productNameQuantities);
    }

    public static Agreement isPromotionProductAddable(final String productName, final Integer quantity) {
        System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n", productName, quantity);
        Agreement agreement = StoreRequestParser.parseAgreement(Console.readLine());
        System.out.println();

        return agreement;
    }

    public static Agreement payWhenLackPromotionProduct(final String productName, final Integer quantity) {
        System.out.printf(
                "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n",
                productName,
                quantity
        );
        Agreement agreement = StoreRequestParser.parseAgreement(Console.readLine());
        System.out.println();

        return agreement;
    }

    public static Agreement isMembershipApplied() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        Agreement agreement = StoreRequestParser.parseAgreement(Console.readLine());
        System.out.println();

        return agreement;
    }
}
