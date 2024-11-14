package store.back.store.order.exception;

import store.back.store.storage.exception.LackProductData;
import store.global.exception.CustomIllegalArgumentException;

public class NeedMorePromotedProductException extends CustomIllegalArgumentException {

    private final LackProductData data;


    public NeedMorePromotedProductException(String productName, Integer quantity) {
        super("상품을 추가할 수 있습니다.");
        this.data = new LackProductData(productName, quantity);
    }

    @Override
    public Object errorData() {
        return data;
    }

}
