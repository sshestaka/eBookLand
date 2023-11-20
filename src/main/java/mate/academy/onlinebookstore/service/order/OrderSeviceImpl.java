package mate.academy.onlinebookstore.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.order.CreateOrderDto;
import mate.academy.onlinebookstore.dto.order.OrderDto;
import mate.academy.onlinebookstore.dto.order.UpdateStatusOrderDto;
import mate.academy.onlinebookstore.mapper.OrderMapper;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.Order;
import mate.academy.onlinebookstore.model.OrderItem;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.repository.order.OrderRepository;
import mate.academy.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.onlinebookstore.repository.user.UserRepository;
import mate.academy.onlinebookstore.service.cartitem.CartItemService;
import mate.academy.onlinebookstore.service.orderitem.OrderItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSeviceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    
    @Override
    public OrderDto save(String userEmail, CreateOrderDto createOrderDto) {
        User currentUser = findUserByEmail(userEmail);
        ShoppingCart userShoppingCart = shoppingCartRepository.findByUserId(currentUser.getId());
        List<CartItem> existedCartItemsList = cartItemService
                .findByShoppingCartId(userShoppingCart.getId());

        if (existedCartItemsList.size() == 0) {
            throw new UnsupportedOperationException("Can't create new order without "
                    + "any items in the shopping cart");
        }

        Order order = new Order();
        order.setUser(currentUser);
        order.setStatus(Order.Status.PENDING);
        order.setTotal(getTotalItemsPrice(existedCartItemsList));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(createOrderDto.shippingAddress());

        orderRepository.save(order);
        order.setOrderItems(getOrderItems(order, existedCartItemsList));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAll(String userEmail) {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto updateStatus(Long id, UpdateStatusOrderDto updateStatusOrderDto) {
        Order orderById = orderRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find the order bi id " + id));
        orderById.setStatus(Order.Status.valueOf(updateStatusOrderDto.status()));
        return orderMapper.toDto(orderRepository.save(orderById));
    }

    private Set<OrderItem> getOrderItems(Order order, List<CartItem> existedCartItemsList) {
        return existedCartItemsList.stream()
                .map(cartItem -> orderItemService.save(order, cartItem))
                .collect(Collectors.toSet());
    }

    private BigDecimal getTotalItemsPrice(List<CartItem> existedCartItemsList) {
        if (existedCartItemsList.size() == 0) {
            return BigDecimal.valueOf(0L);
        }
        return existedCartItemsList.stream()
                .map(cart -> cart.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail).orElseThrow(() ->
                new NoSuchElementException("Can't find a user by email: " + userEmail));
    }
}
