package store.global.dto.request;

import java.util.List;
import store.global.dto.ProductNameQuantity;

public record OrderRequestDTO(List<OrderItemRequestDTO> orderItemRequestDTOS,
                              Boolean lackAgreement, Boolean fillLackQuantity,
                              Boolean checkedFillable, Boolean checkedLackable) {

    private static OrderRequestDTO create(List<ProductNameQuantity> productNameQuantities,
                                          Boolean lackAgreement, Boolean fillLackQuantity,
                                          Boolean checkedFillable, Boolean checkedLackable) {
        List<OrderItemRequestDTO> orderItemRequestDTOS = productNameQuantities.stream()
                .map(productNameQuantity -> new OrderItemRequestDTO(productNameQuantity.productName(),
                        productNameQuantity.quantity()))
                .toList();

        return new OrderRequestDTO(orderItemRequestDTOS, lackAgreement, fillLackQuantity,
                checkedFillable, checkedLackable);
    }

    public static OrderRequestDTO createDefault(List<ProductNameQuantity> productNameQuantities) {
        return create(productNameQuantities, false, true, false, false);
    }


    public static OrderRequestDTO fillLackQuantity(OrderRequestDTO orderRequestDTO) {
        return new OrderRequestDTO(orderRequestDTO.orderItemRequestDTOS, false, true, true, false);

    }

    public static OrderRequestDTO notFillLackQuantity(OrderRequestDTO orderRequestDTO) {
        return new OrderRequestDTO(orderRequestDTO.orderItemRequestDTOS, false, false, true, false);
    }

    public static OrderRequestDTO agreeWithLack(OrderRequestDTO orderRequestDTO) {
        return new OrderRequestDTO(orderRequestDTO.orderItemRequestDTOS, true, false, false, true);
    }

    public static OrderRequestDTO disagreeWithLack(OrderRequestDTO orderRequestDTO) {
        return new OrderRequestDTO(orderRequestDTO.orderItemRequestDTOS, false, false, false, true);
    }
}
