package store.back.store.storage.domain;

import store.back.store.storage.exception.LackProductException;

public class ProductPromotionInform {

    private static final String NOT_PROMOTED_NAME = "";

    private final String productName;

    private final String promotionName;

    private Integer quantity;


    private ProductPromotionInform(String productName, String promotionName, Integer quantity) {
        this.productName = productName;
        this.promotionName = promotionName;
        this.quantity = quantity;
    }

    public static ProductPromotionInform createPromoted(String productName, String promotionName, Integer quantity) {
        return new ProductPromotionInform(productName, promotionName, quantity);
    }

    public static ProductPromotionInform createNotPromoted(String productName, Integer quantity) {
        return new ProductPromotionInform(productName, NOT_PROMOTED_NAME, quantity);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductPromotionInform inform) {
            return inform.productName.equals(this.productName) && inform.promotionName.equals(this.promotionName);
        }
        return false;
    }

    public String getProductName() {
        return productName;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Boolean isEqualProductName(final String productName) {
        return this.productName.equals(productName);
    }

    public Boolean isPromotedProduct() {
        return !this.promotionName.equals(NOT_PROMOTED_NAME);
    }

    public void decreaseStock(final Integer quantity) {
        if (this.quantity < quantity) {
            throw new LackProductException();
        }
        this.quantity -= quantity;
    }
}
