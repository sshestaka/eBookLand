package mate.academy.onlinebookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.onlinebookstore.dto.book.AddBookToShoppingCartDto;
import mate.academy.onlinebookstore.dto.cartitem.CartItemDto;
import mate.academy.onlinebookstore.dto.cartitem.ChangeCartItemQuantityDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    public static final AddBookToShoppingCartDto ADD_BOOK_TO_SHOPPING_CART_DTO
            = new AddBookToShoppingCartDto()
            .setBookId(1L).setTitle("Red Book").setAuthor("Red Author").setQuantity(10);
    public static final List<CartItemDto> CART_ITEMS = List.of(new CartItemDto()
            .setId(1L)
            .setBookId(1L)
            .setBookTitle("Red Book")
            .setQuantity(10));
    public static final ShoppingCartDto SHOPPING_CART_DTO_EXPECTED = new ShoppingCartDto()
            .setId(1L)
            .setUserId(1L)
            .setCartItems(CART_ITEMS);
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-three-default-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/add-two-test-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/shoppingcarts/add-shopping-cart-user-id-1.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/remove-all-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/users/remove-all-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/shoppingcarts/remove-all-shopping-carts.sql")
            );
        }
    }

    @Test
    @DisplayName("Add new item test")
    @WithMockUser(username = "test1@gmail.com", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/caritems/remove-from-shopping-cart-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void addItemToShoppingCart_GivenValidAddBookToShoppingCartDto_ShouldReturnShoppingCartDto()
            throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(ADD_BOOK_TO_SHOPPING_CART_DTO);
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ShoppingCartDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(SHOPPING_CART_DTO_EXPECTED, actual);
    }

    @Test
    @DisplayName("Check how find Shopping Cart by User works")
    @WithMockUser(username = "test1@gmail.com", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/caritems/add-car-item-for-shopping-cart-1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/caritems/remove-from-shopping-cart-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getShoppingCartByUserEmail_GivenExistingUser_ShouldReturnShoppingCartDto()
            throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ShoppingCartDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(SHOPPING_CART_DTO_EXPECTED, actual);
    }

    @Test
    @DisplayName("Check delete Cart Item from User's Shopping Cart")
    @WithMockUser(username = "test1@gmail.com", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/caritems/add-car-item-for-shopping-cart-1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/caritems/remove-from-shopping-cart-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void delete_GivenValidCartItem_ShouldDeleteCartItemFromUserShoppingCart() throws Exception {
        ShoppingCartDto expectedShoppingCart = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L);
        MvcResult mvcResult = mockMvc.perform(
                        delete("/api/cart/cart-items/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Test update the quantity of the Item by giving itemId")
    @WithMockUser(username = "test1@gmail.com", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/caritems/add-car-item-for-shopping-cart-1.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/caritems/remove-from-shopping-cart-1.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void updateQuantityByItemId_GivenValidParameters_ShouldReturnShoppingCartDto()
            throws Exception {
        List<CartItemDto> redBooks11Pc = List.of(new CartItemDto()
                .setId(1L)
                .setBookId(1L)
                .setBookTitle("Red Book")
                .setQuantity(11));
        ShoppingCartDto shoppingCartDtoExpected = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L)
                .setCartItems(redBooks11Pc);
        ChangeCartItemQuantityDto changeItemsQuantityDto = new ChangeCartItemQuantityDto(11);
        String jsonRequest = objectMapper.writeValueAsString(changeItemsQuantityDto);
        MvcResult mvcResult = mockMvc.perform(
                        patch("/api/cart/cart-items/1")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ShoppingCartDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(shoppingCartDtoExpected, actual);
    }
}
