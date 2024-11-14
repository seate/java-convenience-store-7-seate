package store.back.store.storage.exception;

import store.global.exception.CustomIllegalArgumentException;

public class ProductNotExistException extends CustomIllegalArgumentException {

    public ProductNotExistException() {
        super("존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }
}
