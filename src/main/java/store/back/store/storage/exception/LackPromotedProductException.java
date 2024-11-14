package store.back.store.storage.exception;

import store.global.exception.CustomIllegalArgumentException;

public class LackPromotedProductException extends CustomIllegalArgumentException {

    private final LackProductData data;

    public LackPromotedProductException(String productName, Integer quantity) {
        super("프로모션 재고가 부족합니다.");
        this.data = new LackProductData(productName, quantity);
    }

    @Override
    public Object errorData() {
        return data;
    }


}
