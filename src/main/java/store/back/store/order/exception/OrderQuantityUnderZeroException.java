package store.back.store.order.exception;

import store.global.exception.CustomIllegalArgumentException;

public class OrderQuantityUnderZeroException extends CustomIllegalArgumentException {

    public OrderQuantityUnderZeroException() {
        super("주문 수량이 0입니다.");
    }
}
