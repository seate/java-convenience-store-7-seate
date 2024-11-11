package store.front.view;

import java.util.List;
import store.global.dto.response.CurrentStorageResponseDTO;
import store.global.dto.response.CurrentStorageResponseDTOs;
import store.global.dto.response.OrderInformPerProduct;
import store.global.dto.response.OrderResultResponseDTO;

public class StoreResponseView {

    public static void printGreetingMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public static void printProducts(CurrentStorageResponseDTOs currentStorageResponseDTOs) {
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (CurrentStorageResponseDTO inform : currentStorageResponseDTOs.currentProductInforms()) {
            System.out.println(makeProductMessage(inform));
        }
        System.out.println();
    }

    private static String makeProductMessage(CurrentStorageResponseDTO inform) {
        String price = formatMoney(inform.price());
        String quantity = formatQuantity(inform.quantity());
        String promotionName = formatPromotion(inform.promotionName());

        return String.format("- %s %s원 %s %s", inform.productName(), price, quantity, promotionName);
    }

    private static String formatMoney(Integer money) {
        return String.format("%,d", money);
    }

    private static String formatQuantity(Integer quantity) {
        if (quantity == 0) {
            return "재고 없음";
        }
        return quantity + "개";
    }

    private static String formatPromotion(String promotionName) {
        if (promotionName == null) {
            return "";
        }
        return promotionName;
    }

    public static void printOrderResult(OrderResultResponseDTO orderResultResponseDTO) {
        List<OrderInformPerProduct> orderInformPerProducts = orderResultResponseDTO.orderInformPerProducts();

        printReceiptFirst(orderInformPerProducts);
        printReceiptSecond(orderInformPerProducts);
        printReceiptThird(orderResultResponseDTO);
    }

    private static void printReceiptFirst(List<OrderInformPerProduct> orderInformPerProducts) {
        System.out.println("===========W 편의점=============");
        System.out.println("상품명\t\t수량\t금액");
        orderInformPerProducts.stream()
                .map(StoreResponseView::makeReceiptMessage)
                .forEach(System.out::println);
    }

    private static void printReceiptSecond(List<OrderInformPerProduct> orderInformPerProducts) {
        System.out.println("===========증\t정=============");
        orderInformPerProducts.stream()
                .filter(orderInformPerProduct -> 0 < orderInformPerProduct.freeQuantity())
                .map(StoreResponseView::makeFreeMessage)
                .forEach(System.out::println);
    }

    private static void printReceiptThird(OrderResultResponseDTO orderResult) {
        List<OrderInformPerProduct> orderInformPerProducts = orderResult.orderInformPerProducts();
        System.out.println("================================");
        int totalQuantity = orderInformPerProducts.stream().mapToInt(OrderInformPerProduct::totalQuantity).sum();
        System.out.println("총구매액\t\t" + totalQuantity + "\t" + formatMoney(orderResult.totalPrice()));
        int freePrice = calculateFreePrice(orderInformPerProducts);
        System.out.println("행사할인\t\t\t\t-" + formatMoney(freePrice));
        System.out.println("멤버십할인\t\t\t\t-" + formatMoney(orderResult.membershipDiscount()));
        System.out.println("내실돈\t\t\t\t"
                + (formatMoney(orderResult.totalPrice() - freePrice - orderResult.membershipDiscount())));
        System.out.println();
    }

    private static Integer calculateFreePrice(List<OrderInformPerProduct> orderInformPerProducts) {
        return orderInformPerProducts.stream()
                .filter(orderInformPerProduct -> orderInformPerProduct.freeQuantity() > 0)
                .mapToInt(orderInformPerProduct -> orderInformPerProduct.freeQuantity() * orderInformPerProduct.price())
                .sum();
    }

    private static String makeReceiptMessage(OrderInformPerProduct orderInformPerProduct) {
        return String.format("%s\t\t%s\t%s", orderInformPerProduct.productName(), orderInformPerProduct.totalQuantity(),
                formatMoney(orderInformPerProduct.totalQuantity() * orderInformPerProduct.price()));
    }

    private static String makeFreeMessage(OrderInformPerProduct orderInformPerProduct) {
        return String.format("%s\t\t%s", orderInformPerProduct.productName(), orderInformPerProduct.freeQuantity());
    }


    public static void printErrorMessage(String message) {
        System.out.println("[ERROR] " + message);
    }
}
