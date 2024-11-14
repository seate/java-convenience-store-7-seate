package store.back.store.storage.exception;

import store.global.exception.CustomIllegalArgumentException;

public class LackProductException extends CustomIllegalArgumentException {

    public LackProductException() {
        super("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }
}
