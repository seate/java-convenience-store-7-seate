package store.back.store.promotion.entity.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.back.global.annotation.Repository;
import store.back.global.support.FileLoader;
import store.back.store.promotion.entity.entity.PromotionEntity;
import store.back.store.promotion.entity.util.PromotionEntityParser;

@Repository
public class PromotionEntityRepository {

    private static final String FILE_PATH = "promotions.md";

    private static final Map<String, PromotionEntity> promotionEntities = new HashMap<>();


    static {
        init();
    }

    private static void init() {
        try {
            FileLoader.readFile(FILE_PATH)
                    .subList(1, FileLoader.readFile(FILE_PATH).size())
                    .stream()
                    .map(PromotionEntityParser::parseEntity)
                    .forEach(PromotionEntityRepository::save);
        } catch (Exception e) {
            throw new IllegalStateException("프로모션 정보를 불러오는데 실패했습니다.");
        }
    }


    public static void save(final PromotionEntity promotionEntity) {
        promotionEntities.put(promotionEntity.getName(), promotionEntity);
    }

    public List<PromotionEntity> findAll() {
        return promotionEntities.values().stream().toList();
    }
}
