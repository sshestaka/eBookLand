package mate.academy.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.order.CreateOrderDto;
import mate.academy.onlinebookstore.dto.order.OrderDto;
import mate.academy.onlinebookstore.dto.order.UpdateStatusOrderDto;
import mate.academy.onlinebookstore.dto.orderitem.OrderItemDto;
import mate.academy.onlinebookstore.service.order.OrderService;
import mate.academy.onlinebookstore.service.orderitem.OrderItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenAPIDefinition
@RequiredArgsConstructor
@RequestMapping(value = "/api/orders")
@Tag(name = "Order management", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PostMapping
    @Operation(summary = "Add new item",
            description = "Add new item to the shopping card")
    public OrderDto createOrder(
            @RequestBody @Valid CreateOrderDto createOrderDto,
            Authentication authentication) {
        String userEmail = authentication.getName();
        return orderService.save(userEmail, createOrderDto);
    }

    @GetMapping
    @Operation(summary = "Get all user's orders",
            description = "Get a list of all available user's orders")
    public List<OrderDto> getAll(Authentication authentication) {
        String userEmail = authentication.getName();
        return orderService.getAll(userEmail);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update the order's status by id", description =
            "Update the status of the order by id according to the parameters")
    public OrderDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateStatusOrderDto updateStatusOrderDto
    ) {
        return orderService.updateStatus(id, updateStatusOrderDto);
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "Get all order's items",
            description = "Get a list of all available order's items")
    public List<OrderItemDto> getAllOrderItemsByOrderId(@PathVariable Long id) {
        return orderItemService.getAll(id);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get item by id",
            description = "Get item by itemID and orderId")
    public OrderItemDto getOrderItemByOrderIdAndItemId(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderItemService.findByOrderIdAndItemId(orderId, itemId);
    }
}
