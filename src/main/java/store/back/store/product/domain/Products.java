package store.back.store.product.domain;

import java.util.List;
import store.back.global.annotation.Component;
import store.back.store.product.entity.repository.ProductEntityRepository;
import store.back.store.storage.exception.ProductNotExistException;

@Component
public class Products {

    private final List<Product> products;


    public Products(ProductEntityRepository productEntityRepository) {
        this.products = productEntityRepository.findAll()
                .stream().map(Product::from)
                .toList();
    }


    public Product findByName(String name) {
        return products
                .stream()
                .filter(product -> product.getName().equals(name))
                .findAny()
                .orElseThrow(ProductNotExistException::new);
    }

}
