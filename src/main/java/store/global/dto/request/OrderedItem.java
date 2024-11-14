package store.global.dto.request;

public record OrderedItem(String productName, Integer quantity,
                          Boolean lackAgreement, Boolean fillLackQuantity,
                          Boolean checkedFillable, Boolean checkedLackable) {

    public static OrderedItem createDefault(String productName, Integer quantity) {
        return new OrderedItem(productName, quantity, false, false, false, false);
    }

    public static OrderedItem fillLackQuantity(String productName, Integer quantity) {
        return new OrderedItem(productName, quantity, false, true, true, false);

    }

    public static OrderedItem notFillLackQuantity(String productName, Integer quantity) {
        return new OrderedItem(productName, quantity, false, false, true, false);
    }

    public static OrderedItem agreeWithLack(String productName, Integer quantity) {
        return new OrderedItem(productName, quantity, true, false, false, true);
    }

    public static OrderedItem disagreeWithLack(String productName, Integer quantity) {
        return new OrderedItem(productName, quantity, false, false, false, true);
    }
}
