package mate.academy.onlinebookstore.repository;

import java.util.NoSuchElementException;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private static final Long SHOPPING_CART_EXPECTED_ID = 1L;
    private static final Long USER_EXPECTED_ID = 1L;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    private ShoppingCart getExpectedShoppingCart() {
        return shoppingCartRepository.findById(SHOPPING_CART_EXPECTED_ID)
                .orElseThrow(() ->
                        new NoSuchElementException("Can't find a shopping cart by id "
                        + SHOPPING_CART_EXPECTED_ID));
    }

    @Test
    @DisplayName("Check find by user id method")
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
    void findByUserId_GivenValidUserId_ShouldReturnShoppingCart() {
        ShoppingCart actual = shoppingCartRepository.findByUserId(USER_EXPECTED_ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(getExpectedShoppingCart(), actual);
    }
}
