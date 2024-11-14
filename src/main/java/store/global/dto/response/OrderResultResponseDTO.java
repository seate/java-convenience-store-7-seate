package store.global.dto.response;

import java.util.List;

public record OrderResultResponseDTO(List<OrderInformPerProduct> orderInformPerProducts,
                                     Integer totalPrice, Integer membershipDiscount) {
}
