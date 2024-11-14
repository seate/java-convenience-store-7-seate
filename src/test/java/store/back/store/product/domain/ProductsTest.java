package store.back.store.product.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import store.back.store.product.entity.repository.ProductEntityRepository;

class ProductsTest {

    private final ProductEntityRepository productEntityRepository = new ProductEntityRepository();

    @Test
    void findByName() {
        // given
        String productName = "콜라";
        int price = 1000;

        // when
        Products products = new Products(productEntityRepository);
        Product product = products.findByName(productName);

        // then
        assertEquals(productName, product.getName());
        assertEquals(price, product.getPrice());
    }

}