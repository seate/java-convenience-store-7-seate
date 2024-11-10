package store.back.store.order.service;

import java.util.List;
import store.back.global.annotation.Service;
import store.back.store.order.domain.Order;
import store.back.store.order.domain.ProductNameQuantity;
import store.back.store.order.repository.OrderRepository;
import store.back.store.product.domain.Products;
import store.back.store.promotion.domain.Promotions;
import store.back.store.storage.domain.Storage;
import store.global.dto.request.OrderRequestDTO;
import store.global.dto.response.CurrentStorageResponseDTO;
import store.global.dto.response.CurrentStorageResponseDTOs;
import store.global.dto.response.OrderResponseDTO;

@Service
public class StoreServiceImpl implements StoreService {

    private final OrderRepository orderRepository;

    private final Products products;

    private final Promotions promotions;

    private final Storage storage;

    public StoreServiceImpl(OrderRepository orderRepository, Products products, Promotions promotions,
                            Storage storage) {
        this.orderRepository = orderRepository;
        this.products = products;
        this.promotions = promotions;
        this.storage = storage;
    }

    @Override
    public CurrentStorageResponseDTOs currentProductInform() {
        List<CurrentStorageResponseDTO> informs = storage.getProductPromotionInforms().stream().map(inform -> {
            String productName = inform.getProductName();
            Integer price = products.findByName(productName).getPrice();
            return new CurrentStorageResponseDTO(productName, price, inform.getQuantity(), inform.getPromotionName());
        }).toList();

        return new CurrentStorageResponseDTOs(informs);
    }

    @Override
    public OrderResponseDTO order(OrderRequestDTO orderRequestDTO) {
        List<ProductNameQuantity> productNameQuantities = orderRequestDTO.orderItemRequestDTOS().stream()
                .map(orderItemRequestDTO ->
                        new ProductNameQuantity(orderItemRequestDTO.productName(), orderItemRequestDTO.quantity()))
                .toList();
        Order order = new Order(products, promotions, storage, productNameQuantities,
                orderRequestDTO.lackAgreement(), orderRequestDTO.fillLackQuantity(),
                orderRequestDTO.checkedFillable(), orderRequestDTO.checkedLackable());

        orderRepository.save(order);
        return new OrderResponseDTO(order.getUuid());
    }
}
