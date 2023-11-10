package mate.academy.onlinebookstore.repository.cartitem;

import mate.academy.onlinebookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = "SELECT * FROM cart_items ci "
            + "WHERE ci.shopping_cart_id = ? AND ci.book_id = ?", nativeQuery = true)
    CartItem findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);
}
