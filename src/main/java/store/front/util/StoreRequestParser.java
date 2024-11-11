package store.front.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.front.exception.InvalidOrderRequestException;
import store.global.dto.request.OrderedItem;
import store.global.status.Agreement;

public class StoreRequestParser {

    private static final Pattern itemPattern = Pattern.compile("\\[([a-zA-Z가-힣]+)-([0-9]+)]");
    private static final Pattern orderPattern = Pattern
            .compile(String.format("^%s(,%s)*$", itemPattern.pattern(), itemPattern.pattern()));


    public static List<OrderedItem> parseOrder(final String orderString) {
        validateOrderString(orderString);
        return findItems(orderString);
    }

    private static void validateOrderString(final String orderString) {
        final Matcher matcher = orderPattern.matcher(orderString);
        if (!matcher.matches()) {
            throw new InvalidOrderRequestException();
        }
    }

    private static List<OrderedItem> findItems(final String orderString) {
        List<OrderedItem> orderItems = new ArrayList<>();
        final Matcher itemMatcher = itemPattern.matcher(orderString);
        while (itemMatcher.find()) {
            String productName = itemMatcher.group(1); // 물품 이름
            int quantity = Integer.parseInt(itemMatcher.group(2)); // 물품 수량
            orderItems.add(OrderedItem.createDefault(productName, quantity));
        }

        return orderItems;
    }

    public static Agreement parseAgreement(final String isMembership) {
        return Agreement.of(isMembership);
    }
}
