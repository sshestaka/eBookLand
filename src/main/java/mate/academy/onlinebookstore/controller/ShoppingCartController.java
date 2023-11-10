package mate.academy.onlinebookstore.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.book.AddBookToShoppingCartDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.service.shoppingcart.ShoppingCartService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenAPIDefinition
@RequiredArgsConstructor
@RequestMapping(value = "/api/cart")
@Tag(name = "Shopping cart management",
        description = "Endpoints for managing user's shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get user's shopping cart",
            description = "Get user's shopping cart with all available items")
    public ShoppingCartDto getShoppingCartByUserEmail(Authentication authentication,
                                                      Pageable pageable) {
        String userEmail = authentication.getName();
        return shoppingCartService.getShoppingCartByUserEmail(userEmail, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add new item",
            description = "Add new item to the shopping card")
    public ShoppingCartDto addItem(
            @RequestBody @Valid AddBookToShoppingCartDto addBookToShoppingCartDto,
                                   Authentication authentication) {
        String userEmail = authentication.getName();
        return shoppingCartService.addItem(addBookToShoppingCartDto, userEmail);
    }
}
