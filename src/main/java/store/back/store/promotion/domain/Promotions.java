package store.back.store.promotion.domain;

import java.util.List;
import store.back.global.annotation.Component;
import store.back.store.promotion.entity.repository.PromotionEntityRepository;

@Component
public class Promotions {

    private final List<Promotion> promotions;


    public Promotions(PromotionEntityRepository promotionEntityRepository) {
        this.promotions = promotionEntityRepository.findAll()
                .stream().map(promotionEntity -> Promotion.createValidPromotion(
                        promotionEntity.getName(),
                        promotionEntity.getBuyQuantity(),
                        promotionEntity.getFreeQuantity(),
                        promotionEntity.getStartDate(),
                        promotionEntity.getEndDate()))
                .toList();
    }


    public Promotion findByName(String promotionName) {
        return promotions
                .stream()
                .filter(promotion -> promotion.isEqualToName(promotionName))
                .findFirst()
                .orElse(Promotion.getInvalidPromotion());
    }

}
