package store.back.store.product.entity.util;

import java.util.List;
import java.util.Objects;
import store.back.store.product.entity.entity.ProductEntity;

public class ProductEntityParser {

    public static ProductEntity parseEntity(final String productEntity) {
        List<String> infos = List.of(productEntity.split(","));

        if (Objects.equals(infos.get(3), "null")) {
            return createEntity(infos.get(0), Integer.parseInt(infos.get(1)), Integer.parseInt(infos.get(2)), null);
        }
        return createEntity(infos.get(0), Integer.parseInt(infos.get(1)), Integer.parseInt(infos.get(2)), infos.get(3));
    }

    private static ProductEntity createEntity(String name, Integer price, Integer quantity, String promotionName) {
        return new ProductEntity(name, price, quantity, promotionName);
    }
}
