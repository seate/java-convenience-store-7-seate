package store.back.store.product.entity.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import store.back.global.annotation.Repository;
import store.back.store.product.entity.entity.ProductEntity;
import store.back.store.product.entity.util.ProductEntityParser;
import store.back.global.support.FileLoader;

@Repository
public class ProductEntityRepository {

    private static final String FILE_PATH = "products.md";

    private static final List<ProductEntity> productEntities = new ArrayList<>();


    static {
        init();
    }

    private static void init() {
        try {
            FileLoader.readFile(FILE_PATH)
                    .subList(1, FileLoader.readFile(FILE_PATH).size())
                    .stream()
                    .map(ProductEntityParser::parseEntity)
                    .forEach(ProductEntityRepository::save);
        } catch (Exception e) {
            throw new IllegalStateException("상품 정보를 불러오는데 실패했습니다.");
        }
    }

    private static void save(final ProductEntity productEntity) {
        productEntities.add(productEntity);
    }


    public List<ProductEntity> findAll() {
        return Collections.unmodifiableList(productEntities);
    }
}
