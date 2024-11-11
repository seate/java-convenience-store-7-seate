package store.front.view;

import store.global.dto.response.CurrentStorageResponseDTO;
import store.global.dto.response.CurrentStorageResponseDTOs;

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
}
