package mate.academy.onlinebookstore.service.orderitem;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.orderitem.OrderItemDto;
import mate.academy.onlinebookstore.mapper.OrderItemMapper;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.Order;
import mate.academy.onlinebookstore.model.OrderItem;
import mate.academy.onlinebookstore.repository.orderitem.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItem save(Order order, CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        return orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItemDto> getAll(Long id) {
        return orderItemRepository.findAllByOrderId(id).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findByOrderIdAndItemId(Long orderId, Long itemId) {
        OrderItem orderByOrderIdAndId = orderItemRepository.findByOrderIdAndId(orderId, itemId);
        if (orderByOrderIdAndId == null) {
            throw new NoSuchElementException("Can't find order item with orderId = "
                    + orderId + " and itemId = " + itemId);
        }
        return orderItemMapper.toDto(orderByOrderIdAndId);
    }
}
