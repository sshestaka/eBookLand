package mate.academy.onlinebookstore.service.orderitem;

import java.util.List;
import mate.academy.onlinebookstore.dto.orderitem.OrderItemDto;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.Order;
import mate.academy.onlinebookstore.model.OrderItem;

public interface OrderItemService {
    OrderItem save(Order order, CartItem cartItem);

    List<OrderItemDto> getAll(Long id);

    OrderItemDto findByOrderIdAndItemId(Long orderId, Long itemId);
}
