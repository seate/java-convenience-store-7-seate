package store.global.exception;

public class CustomIllegalArgumentException extends IllegalArgumentException {

    public CustomIllegalArgumentException(String message) {
        super(message);
    }

    public Object errorData() {
        return null;
    }
}
