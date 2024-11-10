package store.back.store.product.entity.entity;

import java.util.Optional;

public class ProductEntity {

    private final String name;

    private final Integer price;

    private final Integer quantity;

    private final String promotionName;


    public ProductEntity(String name, Integer price, Integer quantity, String promotionName) {
        validate(name, price, quantity);

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
    }

    private void validate(String name, Integer price, Integer quantity) {
        validateNameLength(name);
        validatePrice(price);
        validateQuantity(quantity);
    }

    private void validateNameLength(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품 이름은 1자 이상이어야 합니다.");
        }
    }

    private void validatePrice(Integer price) {
        if (price == null || price <= 0) {
            throw new IllegalArgumentException("가격은 0원 초과여야 합니다.");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("재고는 0개 이상이어야 합니다.");
        }
    }


    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Optional<String> getPromotionName() {
        return Optional.ofNullable(promotionName);
    }
}
