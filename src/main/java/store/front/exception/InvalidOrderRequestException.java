package store.front.exception;

import store.global.exception.CustomIllegalArgumentException;

public class InvalidOrderRequestException extends CustomIllegalArgumentException {

    public InvalidOrderRequestException() {
        super("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }
}
