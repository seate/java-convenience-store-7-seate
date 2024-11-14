package store.global.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import store.back.store.storage.exception.ProductNotExistException;

public record OrderRequestDTO(List<OrderedItem> orderedItems) {

    public static OrderRequestDTO changeItemByProductName(OrderRequestDTO orderRequestDTO,
                                                          OrderedItem orderedItem) {
        List<OrderedItem> orderedItems = new ArrayList<>(orderRequestDTO.orderedItems());

        int index = IntStream.range(0, orderedItems.size())
                .filter(i -> orderedItems.get(i).productName().equals(orderedItem.productName()))
                .findFirst()
                .orElseThrow(ProductNotExistException::new);
        orderedItems.set(index, orderedItem);

        return new OrderRequestDTO(orderedItems);
    }
}
