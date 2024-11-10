package store.front.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.back.store.order.domain.ProductNameQuantity;
import store.front.exception.InvalidOrderRequestException;
import store.global.status.Agreement;

public class StoreRequestParser {

    private static final Pattern itemPattern = Pattern.compile("\\[([a-zA-Z가-힣]+)-([1-9]+)]");
    private static final Pattern orderPattern = Pattern
            .compile(String.format("^%s(,%s)*$", itemPattern.pattern(), itemPattern.pattern()));


    public static List<ProductNameQuantity> parseOrder(final String orderString) {
        validateOrderString(orderString);
        return findItems(orderString);
    }

    private static void validateOrderString(final String orderString) {
        final Matcher matcher = orderPattern.matcher(orderString);
        if (!matcher.matches()) {
            throw new InvalidOrderRequestException();
        }
    }

    private static List<ProductNameQuantity> findItems(final String orderString) {
        ArrayList<ProductNameQuantity> productNameQuantities = new ArrayList<>();
        final Matcher itemMatcher = itemPattern.matcher(orderString);
        while (itemMatcher.find()) {
            String productName = itemMatcher.group(1); // 물품 이름
            int quantity = Integer.parseInt(itemMatcher.group(2)); // 물품 수량
            productNameQuantities.add(new ProductNameQuantity(productName, quantity));
        }

        return productNameQuantities;
    }

    public static Agreement parseAgreement(final String isMembership) {
        return Agreement.of(isMembership);
    }
}
