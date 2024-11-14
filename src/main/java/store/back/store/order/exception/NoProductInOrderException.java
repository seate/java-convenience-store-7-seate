package store.back.store.order.exception;

import store.global.exception.CustomIllegalArgumentException;

public class NoProductInOrderException extends CustomIllegalArgumentException {

    public NoProductInOrderException() {
        super("주문할 상품이 없습니다.");
    }
}
