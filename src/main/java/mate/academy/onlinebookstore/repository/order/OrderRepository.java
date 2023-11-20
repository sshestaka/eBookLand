package mate.academy.onlinebookstore.repository.order;

import mate.academy.onlinebookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
