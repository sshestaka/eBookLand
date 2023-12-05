package mate.academy.onlinebookstore.repository;

import java.util.List;
import java.util.NoSuchElementException;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.repository.cartitem.CartItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {
    private static final Long SHOPPING_CART_EXPECTED_ID = 1L;
    private static final Long CART_ITEM_EXPECTED_ID = 1L;
    @Autowired
    private CartItemRepository cartItemRepository;

    private List<CartItem> getExpectedCartItemsList() {
        return List.of(cartItemRepository.findById(CART_ITEM_EXPECTED_ID).orElseThrow(() ->
                new NoSuchElementException("Can't find a Cart Item by id "
                        + CART_ITEM_EXPECTED_ID)));
    }

    @Test
    @DisplayName("Find all Car Items by Shopping Cart id")
    @Sql(
            scripts = "classpath:database/books/add-three-default-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/add-two-test-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/shoppingcarts/add-shopping-cart-user-id-1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/caritems/add-car-item-for-shopping-cart-1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/caritems/remove-from-shopping-cart-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/shoppingcarts/remove-all-shopping-carts.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/remove-all-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/shoppingcarts/remove-all-shopping-carts.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findByShoppingCartId_GivenValidId_ShouldReturnListCartItems() {
        List<CartItem> expectedCartItemsList = getExpectedCartItemsList();
        List<CartItem> actualCartItemsList = cartItemRepository
                .findByShoppingCartId(SHOPPING_CART_EXPECTED_ID);
        Assertions.assertNotNull(actualCartItemsList);
        Assertions.assertEquals(actualCartItemsList, expectedCartItemsList);
    }
}
