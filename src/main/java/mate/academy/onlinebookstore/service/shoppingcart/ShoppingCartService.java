package mate.academy.onlinebookstore.service.shoppingcart;

import java.util.List;
import mate.academy.onlinebookstore.dto.book.AddBookToShoppingCartDto;
import mate.academy.onlinebookstore.dto.cartitem.ChangeCartItemQuantityDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    ShoppingCart save(User user);

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

    void deleteById(Long cartItemId);

    void deleteAllCartItemsByShoppingCartId(List<CartItem> cartItems);
}
