package store.back.store.order.util;

import store.back.store.storage.exception.LackProductException;
import store.back.store.storage.exception.LackPromotedProductException;
import store.back.store.order.exception.NeedMorePromotedProductException;
import store.global.dto.request.OrderedItem;

public class OrderQuantityCalculator {

    public static CalculatedQuantities calculate(Integer promotedQuantity, Integer notPromotedQuantity,
                                                 Integer buyQuantity, Integer freeQuantity,
                                                 Boolean isPromoted, OrderedItem orderedItem) {
        Integer requestQuantity = orderedItem.quantity();
        validateTotalQuantity(promotedQuantity, notPromotedQuantity, requestQuantity);
        if (!isPromoted) {
            return new CalculatedQuantities(requestQuantity, 0, 0);
        }
        return selectByRange(promotedQuantity, buyQuantity + freeQuantity, buyQuantity, freeQuantity,
                orderedItem);
    }

    private static CalculatedQuantities selectByRange(Integer promotedQuantity,
                                                      Integer setQuantity, Integer buyQuantity, Integer freeQuantity,
                                                      OrderedItem orderedItem) {
        Integer requestQuantity = orderedItem.quantity();
        int newRequestQuantity = requestQuantity;
        if (buyQuantity <= requestQuantity % setQuantity) {
            newRequestQuantity = requestQuantity + (setQuantity - (requestQuantity % setQuantity));
        }
        if (promotedQuantity < newRequestQuantity) { // 없어서 못 줘
            return exceedPromotedQuantity(orderedItem, setQuantity, promotedQuantity / setQuantity, freeQuantity);
        }
        return InRangePromotedQuantity(orderedItem, setQuantity, buyQuantity, newRequestQuantity,
                requestQuantity % setQuantity, freeQuantity);
    }

    private static void validateTotalQuantity(Integer promotedQuantity, Integer notPromotedQuantity,
                                              Integer requestQuantity) {
        if (promotedQuantity + notPromotedQuantity < requestQuantity) {
            throw new LackProductException();
        }
    }

    private static CalculatedQuantities exceedPromotedQuantity(OrderedItem orderedItem, Integer setQuantity,
                                                               Integer setCount, Integer freeQuantity) {
        int promotedQuantity = setCount * setQuantity;
        if (!orderedItem.checkedLackable()) {
            throw new LackPromotedProductException(orderedItem.productName(),
                    orderedItem.quantity() - promotedQuantity);
        }

        if (orderedItem.lackAgreement()) {
            return new CalculatedQuantities(orderedItem.quantity(), promotedQuantity, setCount * freeQuantity);
        }
        return new CalculatedQuantities(promotedQuantity, promotedQuantity, setCount * freeQuantity);
    }

    private static CalculatedQuantities InRangePromotedQuantity(OrderedItem orderedItem, Integer setQuantity,
                                                                Integer buyQuantity, Integer appendedQuantity,
                                                                Integer remainQuantity, Integer freeQuantity) {
        Integer requestQuantity = orderedItem.quantity();
        if (orderedItem.fillLackQuantity() && buyQuantity <= remainQuantity) { // 더 받음 && 더 받을 수 있음
            if (!orderedItem.checkedFillable()) {
                throw new NeedMorePromotedProductException(orderedItem.productName(), setQuantity - remainQuantity);
            }
            return new CalculatedQuantities(appendedQuantity, appendedQuantity,
                    appendedQuantity / setQuantity * freeQuantity);
        } // (개수 안 맞음 && 더 받을 수 없음) || (더 받을 수 있음 && 더 받지 않음)
        return new CalculatedQuantities(requestQuantity, (requestQuantity / setQuantity) * setQuantity,
                (requestQuantity / setQuantity) * freeQuantity);
    }

}
