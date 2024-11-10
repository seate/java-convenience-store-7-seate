package store.global.exception;

public class ExtraInvalidRequestException extends CustomIllegalArgumentException {

    public ExtraInvalidRequestException() {
        super("잘못된 입력입니다. 다시 입력해 주세요.");
    }
}
