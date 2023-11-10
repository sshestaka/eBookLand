package mate.academy.onlinebookstore.service.cartitem;

import java.util.Optional;
import mate.academy.onlinebookstore.dto.cartitem.CartItemDto;
import mate.academy.onlinebookstore.model.CartItem;

public interface CartItemService {

    CartItemDto save(CartItem cartItem);

    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);
}
