package store.global.status;

import store.global.exception.ExtraInvalidRequestException;

public enum Agreement {

    AGREE("Y", true),
    DISAGREE("N", false);

    private final String value;

    private final Boolean isAgreed;

    Agreement(String value, boolean isAgreed) {
        this.value = value;
        this.isAgreed = isAgreed;
    }


    public static Agreement of(String value) {
        for (Agreement agreement : values()) {
            if (agreement.value.equals(value)) {
                return agreement;
            }
        }

        throw new ExtraInvalidRequestException();
    }

    public Boolean isAgreed() {
        return isAgreed;
    }
}
