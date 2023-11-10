package mate.academy.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenAPIDefinition
@RequiredArgsConstructor
@RequestMapping(value = "/api/cart/items")
@Tag(name = "Items management",
        description = "Endpoints for managing items in user's shopping cart")
public class CartItemController {
}
