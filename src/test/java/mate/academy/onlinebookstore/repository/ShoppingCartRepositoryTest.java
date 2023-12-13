package mate.academy.onlinebookstore.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
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
public class ShoppingCartRepositoryTest {
    private static final Long SHOPPING_CART_EXPECTED_ID = 1L;
    private static final Long USER_EXPECTED_ID = 1L;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

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
    @DisplayName("Check find by user id method")
    @Sql(
            scripts = "classpath:database/caritems/add-data-before-cart-item-repository-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/caritems/remove-data-after-cart-item-repository-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void findByUserId_GivenValidUserId_ShouldReturnShoppingCart() {
        ShoppingCart actual = shoppingCartRepository.findByUserId(USER_EXPECTED_ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(getExpectedShoppingCart(), actual);
    }

    private ShoppingCart getExpectedShoppingCart() {
        return shoppingCartRepository.findById(SHOPPING_CART_EXPECTED_ID)
                .orElseThrow(() ->
                        new NoSuchElementException("Can't find a shopping cart by id "
                                + SHOPPING_CART_EXPECTED_ID));
    }
}
