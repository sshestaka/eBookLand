package mate.academy.onlinebookstore.service.shoppingcart;

import mate.academy.onlinebookstore.dto.book.AddBookToShoppingCartDto;
import mate.academy.onlinebookstore.dto.cartitem.ChangeCartItemQuantityDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    ShoppingCart save(User user);

    void deleteById(Long cartItemId);

    ShoppingCartDto getShoppingCartByUserEmail(String userEmail, Pageable pageable);

    ShoppingCartDto addCartItem(
            AddBookToShoppingCartDto addBookToShoppingCartDto,
            String userEmail
    );

    ShoppingCartDto updateQuantityByItemId(
            Long cartItemId,
            ChangeCartItemQuantityDto changeCartItemQuantityDto,
            String userEmail
    );
}
