package store.back.store.order.service;

import java.util.List;
import store.back.global.annotation.Service;
import store.back.store.order.domain.Membership;
import store.back.store.order.domain.Order;
import store.back.store.order.domain.OrderProduct;
import store.back.store.order.repository.OrderRepository;
import store.back.store.product.domain.Products;
import store.back.store.promotion.domain.Promotions;
import store.back.store.storage.domain.Storage;
import store.global.dto.request.OrderRequestDTO;
import store.global.dto.request.PayRequestDTO;
import store.global.dto.response.CurrentStorageResponseDTO;
import store.global.dto.response.CurrentStorageResponseDTOs;
import store.global.dto.response.OrderInformPerProduct;
import store.global.dto.response.OrderResponseDTO;
import store.global.dto.response.OrderResultResponseDTO;

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
        Order order = new Order(products, promotions, storage, orderRequestDTO.orderedItems());

        orderRepository.save(order);
        return new OrderResponseDTO(order.getUuid());
    }

    @Override
    public OrderResultResponseDTO calculateOrderPrice(PayRequestDTO payRequestDTO) {
        Order order = orderRepository.findById(payRequestDTO.uuid());

        int membershipAblePrice = calculateMembershipAblePrice(order, payRequestDTO);
        int totalPrice = calculateTotalPrice(order);
        List<OrderInformPerProduct> orderInformPerProducts = createOrderInformPerProducts(order);

        decreaseQuantities(orderInformPerProducts);
        return new OrderResultResponseDTO(orderInformPerProducts, totalPrice,
                Membership.getDiscount(membershipAblePrice));
    }

    private Integer calculateMembershipAblePrice(final Order order, final PayRequestDTO payRequestDTO) {
        if (!payRequestDTO.isMembershipApplied()) {
            return 0;
        }

        return order.getOrderLines().stream().mapToInt(orderLine -> {
            OrderProduct orderProduct = orderLine.getOrderProduct();
            return orderProduct.getPrice() * (orderProduct.getTotalQuantity() - orderProduct.getPromotedQuantity());
        }).sum();
    }

    private Integer calculateTotalPrice(final Order order) {
        return order.getOrderLines().stream().mapToInt(orderLine -> {
            OrderProduct orderProduct = orderLine.getOrderProduct();
            return orderProduct.getPrice() * orderProduct.getTotalQuantity();
        }).sum();
    }

    private List<OrderInformPerProduct> createOrderInformPerProducts(final Order order) {
        return order.getOrderLines().stream().map(orderLine -> {
            OrderProduct orderProduct = orderLine.getOrderProduct();
            return new OrderInformPerProduct(orderProduct.getName(),
                    orderProduct.getTotalQuantity(), orderProduct.getFreeQuantity(), orderProduct.getPrice());
        }).toList();
    }


    private void decreaseQuantities(final List<OrderInformPerProduct> orderInformPerProducts) {
        for (OrderInformPerProduct orderInformPerProduct : orderInformPerProducts) {
            storage.decreaseQuantity(orderInformPerProduct.productName(), orderInformPerProduct.totalQuantity());
        }
    }
}
