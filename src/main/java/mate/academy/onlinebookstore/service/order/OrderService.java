package mate.academy.onlinebookstore.service.order;

import java.util.List;
import mate.academy.onlinebookstore.dto.order.CreateOrderDto;
import mate.academy.onlinebookstore.dto.order.OrderDto;
import mate.academy.onlinebookstore.dto.order.UpdateStatusOrderDto;

public interface OrderService {
    OrderDto save(String userEmail, CreateOrderDto createOrderDto);

    List<OrderDto> getAll(String userEmail);

    OrderDto updateStatus(Long id, UpdateStatusOrderDto updateStatusOrderDto);
}
