package store.back.store.product.domain;

import store.back.store.product.entity.entity.ProductEntity;

public class Product {

    private final String name;

    private final Integer price;


    private Product(String name, Integer price) {
        validate(name, price);
        this.name = name;
        this.price = price;
    }

    private void validate(final String name, final Integer price) {
        validateNameLength(name);
        validatePrice(price);
    }

    private void validateNameLength(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("상품 이름은 1자 이상이어야 합니다.");
        }
    }

    private void validatePrice(final Integer price) {
        if (price == null || price <= 0) {
            throw new IllegalStateException("가격은 0원 초과여야 합니다.");
        }
    }

    public static Product from(ProductEntity productEntity) {
        return new Product(productEntity.getName(), productEntity.getPrice());
    }


    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
