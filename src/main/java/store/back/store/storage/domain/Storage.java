package store.back.store.storage.domain;

import static java.util.stream.Collectors.counting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import store.back.global.annotation.Component;
import store.back.store.product.entity.repository.ProductEntityRepository;
import store.back.store.storage.exception.LackProductException;
import store.back.store.storage.exception.ProductNotExistException;

@Component
public class Storage {

    private final List<ProductPromotionInform> productPromotionInforms;

    public Storage(ProductEntityRepository productEntityRepository) {
        List<ProductPromotionInform> informs = includeEmptyProducts(productEntityRepository);
        validateDuplicateInform(informs);
        this.productPromotionInforms = informs;
    }

    private List<ProductPromotionInform> includeEmptyProducts(final ProductEntityRepository productEntityRepository) {
        List<ProductPromotionInform> informs = loadFromRepository(productEntityRepository);
        Map<String, Long> counter = informs.stream()
                .collect(Collectors.groupingBy(ProductPromotionInform::getProductName, counting()));
        return informs.stream().flatMap(inform -> {
            if (counter.get(inform.getProductName()) == 1 && inform.isPromotedProduct()) {
                return Stream.of(inform, ProductPromotionInform.createNotPromoted(inform.getProductName(), 0));
            }
            return Stream.of(inform);
        }).collect(Collectors.toCollection(ArrayList::new)); // 변경 가능하도록 ArrayList로 변환
    }

    private List<ProductPromotionInform> loadFromRepository(final ProductEntityRepository productEntityRepository) {
        return productEntityRepository.findAll().stream().map(productEntity -> {
            String productName = productEntity.getName();
            Optional<String> optionalPromotionName = productEntity.getPromotionName();
            Integer quantity = productEntity.getQuantity();

            return optionalPromotionName
                    .map(promotionName -> ProductPromotionInform.createPromoted(productName, promotionName, quantity))
                    .orElseGet(() -> ProductPromotionInform.createNotPromoted(productName, quantity));
        }).toList();
    }

    private void validateDuplicateInform(final List<ProductPromotionInform> informs) {
        if (informs.stream().distinct().count() != informs.size()) {
            throw new IllegalStateException("중복된 상품 정보가 존재합니다.");
        }
    }


    public Integer getQuantity(final String productName, final Boolean isPromoted) {
        return productPromotionInforms
                .stream()
                .filter(inform -> inform.isEqualProductName(productName) && (inform.isPromotedProduct() == isPromoted))
                .findAny()
                .map(ProductPromotionInform::getQuantity)
                .orElse(0);
    }

    public Optional<String> findPromotionNameByProductName(final String productName) {
        return productPromotionInforms
                .stream()
                .filter(inform -> inform.isEqualProductName(productName) && inform.isPromotedProduct())
                .findAny()
                .map(ProductPromotionInform::getPromotionName);
    }

    public void decreaseQuantity(final String productName, final Integer quantity) {
        List<ProductPromotionInform> informs = getProductPromotionInforms(productName);
        validateProductExistence(informs);

        Optional<ProductPromotionInform> optionalProductPromotionInform = findPromotedProduct(informs);

        if (optionalProductPromotionInform.isPresent()) {
            handlePromotedProduct(optionalProductPromotionInform.get(), informs, quantity);
            return;
        }
        handleNotPromotedProduct(informs, quantity);
    }

    private List<ProductPromotionInform> getProductPromotionInforms(final String productName) {
        return productPromotionInforms.stream()
                .filter(inform -> inform.isEqualProductName(productName))
                .toList();
    }

    private void validateProductExistence(final List<ProductPromotionInform> informs) {
        if (informs.isEmpty()) {
            throw new ProductNotExistException();
        }
    }

    private Optional<ProductPromotionInform> findPromotedProduct(final List<ProductPromotionInform> informs) {
        return informs.stream()
                .filter(ProductPromotionInform::isPromotedProduct)
                .findAny();
    }

    private void handlePromotedProduct(final ProductPromotionInform promotedProduct,
                                       final List<ProductPromotionInform> informs,
                                       final Integer quantity) {
        if (promotedProduct.getQuantity() < quantity) {
            handleMixedProduct(promotedProduct, informs, quantity);
            return;
        }
        promotedProduct.decreaseStock(quantity);
        save(promotedProduct);
    }

    private void handleMixedProduct(final ProductPromotionInform promotedProduct,
                                    final List<ProductPromotionInform> informs,
                                    final Integer quantity) {
        ProductPromotionInform notPromotedProduct = findNotPromotedProduct(informs);
        int remainingQuantity = quantity - promotedProduct.getQuantity();
        if (notPromotedProduct.getQuantity() < remainingQuantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }

        notPromotedProduct.decreaseStock(remainingQuantity);
        promotedProduct.decreaseStock(promotedProduct.getQuantity());
        save(promotedProduct);
        save(notPromotedProduct);
    }

    private ProductPromotionInform findNotPromotedProduct(final List<ProductPromotionInform> informs) {
        return informs.stream()
                .filter(inform -> !inform.isPromotedProduct())
                .findFirst()
                .orElseThrow(LackProductException::new);
    }

    private void handleNotPromotedProduct(final List<ProductPromotionInform> informs, final Integer quantity) {
        ProductPromotionInform notPromotedProduct = findNotPromotedProduct(informs);

        if (notPromotedProduct.getQuantity() < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }

        notPromotedProduct.decreaseStock(quantity);
        save(notPromotedProduct);
    }

    private void save(final ProductPromotionInform productPromotionInform) {
        int index = IntStream.range(0, productPromotionInforms.size())
                .filter(i -> productPromotionInforms.get(i).equals(productPromotionInform))
                .findAny()
                .orElseThrow(ProductNotExistException::new);

        productPromotionInforms.set(index, productPromotionInform);
    }

    public List<ProductPromotionInform> getProductPromotionInforms() {
        return Collections.unmodifiableList(productPromotionInforms);
    }
}
