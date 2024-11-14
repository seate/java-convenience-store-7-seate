package store.back.store.order.domain;

public class Membership {

    private static final Integer MAX_MEMBERSHIP_DISCOUNT = 8000;

    private static final Double MEMBERSHIP_DISCOUNT_RATE = 0.3;

    public static Integer getDiscount(Integer membershipAblePrice) {
        double discountablePrice = membershipAblePrice * MEMBERSHIP_DISCOUNT_RATE;

        if (MAX_MEMBERSHIP_DISCOUNT < discountablePrice) {
            return MAX_MEMBERSHIP_DISCOUNT;
        }
        return (int) discountablePrice;
    }
}
