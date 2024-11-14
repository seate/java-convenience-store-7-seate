package store.back.store.order.repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import store.back.global.annotation.Repository;
import store.back.store.order.domain.Order;

@Repository
public class OrderRepository {

    private final Map<UUID, Order> orders = new ConcurrentHashMap<>();


    public void save(Order order) {
        orders.put(order.getUuid(), order);
    }

    public Order findById(UUID id) {
        return orders.get(id);
    }
}
