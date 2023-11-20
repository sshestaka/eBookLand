package mate.academy.onlinebookstore.repository.orderitem;

import java.util.List;
import mate.academy.onlinebookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderId(Long id);

    OrderItem findByOrderIdAndId(Long orderId, Long itemId);
}
