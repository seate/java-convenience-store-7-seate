package store.back.store.order.util;

import store.back.store.storage.exception.LackProductException;
import store.back.store.storage.exception.LackPromotedProductException;
import store.back.store.order.exception.NeedMorePromotedProductException;

public class OrderQuantityCalculator {

    public static CalculatedQuantities calculate(Integer requestQuantity, Integer promotedQuantity,
                                                 Integer notPromotedQuantity, Integer buyQuantity,
                                                 Integer freeQuantity, String productName,
                                                 Boolean isPromoted, Boolean lackAgreement, Boolean fillLackQuantity,
                                                 Boolean checkedFillable, Boolean checkedLackable) {
        validateTotalQuantity(promotedQuantity, notPromotedQuantity, requestQuantity);
        if (!isPromoted) {
            return new CalculatedQuantities(requestQuantity, 0, 0);
        }
        int setQuantity = buyQuantity + freeQuantity;
        return selectByRange(promotedQuantity, requestQuantity, setQuantity, buyQuantity, freeQuantity,
                requestQuantity, productName, checkedLackable, lackAgreement, fillLackQuantity, checkedFillable);
    }

    private static CalculatedQuantities selectByRange(Integer promotedQuantity, Integer requestQuantity,
                                                      Integer setQuantity, Integer buyQuantity, Integer freeQuantity,
                                                      Integer appendedQuantity, String productName,
                                                      Boolean checkedLackable, Boolean lackAgreement,
                                                      Boolean fillLackQuantity, Boolean checkedFillable) {
        int newRequestQuantity = appendedQuantity;
        if (buyQuantity <= requestQuantity % setQuantity) {
            newRequestQuantity = requestQuantity + (setQuantity - (requestQuantity % setQuantity));
        }
        if (promotedQuantity < newRequestQuantity) { // 없어서 못 줘
            return exceedPromotedQuantity(checkedLackable, lackAgreement, requestQuantity, setQuantity,
                    promotedQuantity / setQuantity, freeQuantity, productName);
        }
        return InRangePromotedQuantity(fillLackQuantity, checkedFillable, requestQuantity, setQuantity, buyQuantity,
                newRequestQuantity, requestQuantity % setQuantity, freeQuantity, productName);
    }

    private static void validateTotalQuantity(Integer promotedQuantity, Integer notPromotedQuantity,
                                              Integer requestQuantity) {
        if (promotedQuantity + notPromotedQuantity < requestQuantity) {
            throw new LackProductException();
        }
    }

    private static CalculatedQuantities exceedPromotedQuantity(Boolean checkedLackable, Boolean lackAgreement,
                                                               Integer requestQuantity, Integer setQuantity,
                                                               Integer setCount, Integer freeQuantity,
                                                               String productName) {
        int promotedQuantity = setCount * setQuantity;
        if (!checkedLackable) {
            throw new LackPromotedProductException(productName, requestQuantity - promotedQuantity);
        }

        if (lackAgreement) {
            return new CalculatedQuantities(requestQuantity, promotedQuantity, setCount * freeQuantity);
        }
        return new CalculatedQuantities(promotedQuantity, promotedQuantity, setCount * freeQuantity);
    }

    private static CalculatedQuantities InRangePromotedQuantity(Boolean fillLackQuantity, Boolean checkedFillable,
                                                                Integer requestQuantity, Integer setQuantity,
                                                                Integer buyQuantity, Integer appendedQuantity,
                                                                Integer remainQuantity, Integer freeQuantity,
                                                                String productName) {
        if (fillLackQuantity && buyQuantity <= remainQuantity) { // 더 받을거야 && 더 받을 수 있어
            if (!checkedFillable) {
                throw new NeedMorePromotedProductException(productName, setQuantity - remainQuantity);
            }
            return new CalculatedQuantities(appendedQuantity, appendedQuantity,
                    appendedQuantity / setQuantity * freeQuantity);
        }
        // (개수 안 맞음 && 더 받을 수 없음) || (개수 맞음 && 더 받지 않음)
        return new CalculatedQuantities(requestQuantity, (requestQuantity / setQuantity) * setQuantity,
                (requestQuantity / setQuantity) * freeQuantity);
    }

}
