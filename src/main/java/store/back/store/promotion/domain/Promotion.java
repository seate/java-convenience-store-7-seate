package store.back.store.promotion.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;

public class Promotion {

    private static final Promotion INVALID_PROMOTION = Promotion.createInvalidPromotion();

    private final String name;

    private final Integer buyQuantity;

    private final Integer freeQuantity;

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;

    private final PromotionStatus status;


    private Promotion(String name, Integer buyQuantity, Integer freeQuantity,
                      LocalDateTime startDate, LocalDateTime endDate,
                      PromotionStatus status) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
        if (status == PromotionStatus.VALID && !isTimeValid()) {
            this.status = PromotionStatus.INVALID;
            return;
        }

        this.status = status;
    }

    public static Promotion createValidPromotion(String name, Integer buyQuantity, Integer freeQuantity,
                                                 LocalDateTime startDate, LocalDateTime endDate) {
        return new Promotion(name, buyQuantity, freeQuantity, startDate, endDate, PromotionStatus.VALID);
    }

    private static Promotion createInvalidPromotion() {
        return new Promotion("", 1, 0, null, null, PromotionStatus.INVALID);
    }

    public static Promotion getInvalidPromotion() {
        return INVALID_PROMOTION;
    }


    public Boolean isEqualToName(final String name) {
        return this.name.equals(name);
    }

    public String getName() {
        return name;
    }

    public Integer getBuyQuantity() {
        return buyQuantity;
    }

    public Integer getFreeQuantity() {
        return freeQuantity;
    }

    public Boolean isValid() {
        return (this.status == PromotionStatus.VALID) && isTimeValid();
    }

    private Boolean isTimeValid() {
        LocalDateTime now = DateTimes.now();
        return now.isAfter(this.startDate) && now.isBefore(this.endDate);
    }


}
