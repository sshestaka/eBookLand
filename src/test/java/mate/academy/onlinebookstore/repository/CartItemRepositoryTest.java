package mate.academy.onlinebookstore.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.repository.cartitem.CartItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryTest {
    private static final Long SHOPPING_CART_EXPECTED_ID = 1L;
    private static final Long CART_ITEM_EXPECTED_ID = 1L;
    @Autowired
    private CartItemRepository cartItemRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/remove-all-data-before-tests/remove-all-data-before-tests.sql"
                    )
            );
        }
    }

    @Test
    @DisplayName("Find all Car Items by Shopping Cart id")
    @Sql(
            scripts = "classpath:database/caritems/add-data-before-cart-item-repository-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/caritems/remove-data-after-cart-item-repository-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void findByShoppingCartId_GivenValidId_ShouldReturnListCartItems() {
        List<CartItem> expectedCartItemsList = getExpectedCartItemsList();
        List<CartItem> actualCartItemsList = cartItemRepository
                .findByShoppingCartId(SHOPPING_CART_EXPECTED_ID);
        Assertions.assertNotNull(actualCartItemsList);
        Assertions.assertEquals(actualCartItemsList, expectedCartItemsList);
    }

    private List<CartItem> getExpectedCartItemsList() {
        return List.of(cartItemRepository.findById(CART_ITEM_EXPECTED_ID).orElseThrow(() ->
                new NoSuchElementException("Can't find a Cart Item by id "
                        + CART_ITEM_EXPECTED_ID)));
    }
}
