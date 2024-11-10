package store.front.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.back.store.order.domain.ProductNameQuantity;
import store.front.util.StoreRequestParser;
import store.global.dto.request.OrderRequestDTO;

public class StoreRequestView {

    public static OrderRequestDTO purchase() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        List<ProductNameQuantity> productNameQuantities = StoreRequestParser.parseOrder(Console.readLine());
        System.out.println();

        return OrderRequestDTO.createDefault(productNameQuantities);
    }
}
