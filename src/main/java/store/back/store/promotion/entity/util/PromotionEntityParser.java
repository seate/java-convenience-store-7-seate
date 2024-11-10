package store.back.store.promotion.entity.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import store.back.store.promotion.entity.entity.PromotionEntity;

public class PromotionEntityParser {

    public static PromotionEntity parseEntity(final String promotionEntity) {
        List<String> infos = List.of(promotionEntity.split(","));

        return new PromotionEntity(
                infos.get(0),
                Integer.parseInt(infos.get(1)),
                Integer.parseInt(infos.get(2)),
                LocalDate.parse(infos.get(3)).atStartOfDay(),
                LocalDate.parse(infos.get(4)).atTime(LocalTime.MAX)
        );
    }
}
