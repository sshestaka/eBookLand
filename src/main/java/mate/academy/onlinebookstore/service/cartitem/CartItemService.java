package mate.academy.onlinebookstore.service.cartitem;

import java.util.List;
import mate.academy.onlinebookstore.dto.cartitem.CartItemDto;
import mate.academy.onlinebookstore.model.CartItem;

public interface CartItemService {

    CartItemDto save(CartItem cartItem);

    CartItem findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    List<CartItem> findByShoppingCartId(Long shoppingCartId);
}
