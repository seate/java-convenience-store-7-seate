package store.back.store.product.entity.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import store.back.store.product.entity.entity.ProductEntity;

class ProductEntityParserTest {

    @Test
    void parseEntity() {
        String name = "name";
        Integer price = 1000;
        Integer quantity = 10;
        String promotionName = "promotionName";

        ProductEntity productEntity = new ProductEntity(name, price, quantity, promotionName);

        ProductEntity parsedEntity = ProductEntityParser.parseEntity(
                String.join(",", name, price.toString(), quantity.toString(), promotionName));

        Assertions.assertThat(parsedEntity).usingRecursiveComparison().isEqualTo(productEntity);
    }
}