package store.back.store.promotion.entity.entity;

import java.time.LocalDateTime;

public class PromotionEntity {

    private final String name;

    private final Integer buyQuantity;

    private final Integer freeQuantity;

    private final LocalDateTime startDate;

    private final LocalDateTime endDate;


    public PromotionEntity(String name, Integer buyQuantity, Integer freeQuantity, LocalDateTime startDate,
                           LocalDateTime endDate) {
        validate(name, buyQuantity, freeQuantity, startDate, endDate);
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validate(String name,
                          Integer buyQuantity,
                          Integer plusQuantity,
                          LocalDateTime startDate,
                          LocalDateTime endDate) {
        validateNameLength(name);
        validateBuyCount(buyQuantity);
        validatePlusCount(plusQuantity);
        validatePromotionDate(startDate, endDate);
    }

    private void validateNameLength(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("프로모션 이름은 1자 이상이어야 합니다.");
        }
    }

    private void validateBuyCount(Integer buyCount) {
        if (buyCount == null || buyCount < 1) {
            throw new IllegalStateException("구매 개수는 1개 이상이어야 합니다.");
        }
    }

    private void validatePlusCount(Integer plusQuantity) {
        if (plusQuantity == null || plusQuantity < 1) {
            throw new IllegalStateException("추가 개수는 1개 이상이어야 합니다.");
        }
    }

    private void validatePromotionDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalStateException("시작 날짜가 종료 날짜보다 늦을 수 없습니다.");
        }
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
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

}
