package store.global.dto.response;

public record OrderInformPerProduct(String productName, Integer totalQuantity, Integer freeQuantity, Integer price) {
}
