package mate.academy.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import mate.academy.onlinebookstore.dto.book.AddBookToShoppingCartDto;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.dto.cartitem.CartItemDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.mapper.BookMapper;
import mate.academy.onlinebookstore.mapper.ShoppingCartMapper;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.Role;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.repository.book.BookRepository;
import mate.academy.onlinebookstore.repository.role.RoleRepository;
import mate.academy.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.onlinebookstore.repository.user.UserRepository;
import mate.academy.onlinebookstore.service.book.BookService;
import mate.academy.onlinebookstore.service.cartitem.CartItemService;
import mate.academy.onlinebookstore.service.shoppingcart.ShoppingCartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static final int RED_BOOK_QUANTITY = 10;
    private static final int GREEN_BOOK_QUANTITY = 20;
    private static final String NON_EXISTING_USER_EMAIL = "user@email";
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Verify save() method works")
    public void save_GivenValidUser_ShouldReturnShoppingCart() {
        User user = getUserTest1();
        userRepository.save(user);
        ShoppingCart shoppingCart = getShoppingCart(user);
        Mockito.when(shoppingCartRepository.save(any())).thenReturn(shoppingCart);
        ShoppingCart actualShoppingCart = shoppingCartService.save(user);
        assertEquals(shoppingCart, actualShoppingCart);
    }

    @Test
    @DisplayName("Verify save() method add cart items works when given an existing cart item")
    public void addCartItem_GivenCartItemWithExistingBook_ShouldIncreaseQuantity() {
        User user = getUserTest1();
        userRepository.save(user);

        ShoppingCart shoppingCart = getShoppingCart(user);
        shoppingCartRepository.save(shoppingCart);

        Book redBook = getRedBookWithPrice19_99();
        bookRepository.save(redBook);

        CartItem cartItem = getCartItem(shoppingCart, redBook, RED_BOOK_QUANTITY);
        cartItemService.save(cartItem);

        shoppingCart.setCartItems(Collections.singleton(cartItem));

        AddBookToShoppingCartDto addBookToShoppingCartDto =
                getAddBookToShoppingCartDto(redBook, RED_BOOK_QUANTITY);

        CartItemDto cartItemDto = getCartItemDto(cartItem);

        ShoppingCartDto shoppingCartDto = getShoppingCartDto(shoppingCart, List.of(cartItemDto));

        BookDto bookDto = getRedBookDtoWithPrice19_99();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);
        Mockito.when(bookService.findById(addBookToShoppingCartDto.getBookId()))
                .thenReturn(bookDto);
        doReturn(List.of(cartItem)).when(cartItemService)
                .findByShoppingCartId(shoppingCart.getId());
        Mockito.when(cartItemService.save(cartItem)).thenReturn(cartItemDto);
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        ShoppingCartDto shoppingCartDtoActual = shoppingCartService
                .addCartItem(addBookToShoppingCartDto, user.getEmail());
        assertNotNull(shoppingCartDtoActual);
        assertEquals(shoppingCartDtoActual, shoppingCartDto);
    }

    @Test
    @DisplayName("Verify save() method add cart items works when given an existing cart item")
    public void addCartItem_GivenCartItemWithNewBook_ShouldAddNewCartItem() {
        User user = getUserTest1();
        userRepository.save(user);

        ShoppingCart shoppingCart = getShoppingCart(user);
        shoppingCartRepository.save(shoppingCart);

        Book redBook = getRedBookWithPrice19_99();
        bookRepository.save(redBook);

        CartItem redCartItem = getCartItem(shoppingCart, redBook, RED_BOOK_QUANTITY);
        cartItemService.save(redCartItem);

        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.getCartItems().add(redCartItem);

        Book greenBook = getGreenBookWithPrice19_99();

        CartItem greenCartItem = getCartItem(shoppingCart, greenBook, GREEN_BOOK_QUANTITY);
        cartItemService.save(greenCartItem);

        CartItemDto greenCartItemDto = getCartItemDto(greenCartItem);
        BookDto greenBookDto = getGreenBookDtoWithPrice19_99();
        AddBookToShoppingCartDto addBookToShoppingCartDto =
                getAddBookToShoppingCartDto(greenBook, GREEN_BOOK_QUANTITY);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);

        doReturn(greenBookDto).when(bookService).findById(addBookToShoppingCartDto.getBookId());
        doReturn(List.of(redCartItem)).when(cartItemService)
                .findByShoppingCartId(shoppingCart.getId());
        Mockito.when(bookMapper.toModelFromBookDto(greenBookDto)).thenReturn(greenBook);
        Mockito.when(cartItemService.save(any())).thenReturn(greenCartItemDto);

        CartItemDto redCartItemDto = getCartItemDto(redCartItem);
        ShoppingCartDto shoppingCartDto =
                getShoppingCartDto(shoppingCart, List.of(redCartItemDto, greenCartItemDto));

        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        ShoppingCartDto shoppingCartDtoActual = shoppingCartService
                .addCartItem(addBookToShoppingCartDto, user.getEmail());
        assertNotNull(shoppingCartDtoActual);
        assertEquals(shoppingCartDtoActual, shoppingCartDto);
    }

    @Test
    @DisplayName("Verify get Shopping Cart by User's email works when given an existing user email")
    public void getShoppingCartByUserEmail_GivenValidUserEmail_ShouldReturnShoppingCart() {
        User user = getUserTest1();
        userRepository.save(user);
        ShoppingCart shoppingCart = getShoppingCart(user);
        shoppingCartRepository.save(shoppingCart);
        Book redBook = getRedBookWithPrice19_99();
        CartItem redCartItem = getCartItem(shoppingCart, redBook, RED_BOOK_QUANTITY);
        cartItemService.save(redCartItem);
        CartItemDto redCartItemDto = getCartItemDto(redCartItem);
        ShoppingCartDto shoppingCartDto = getShoppingCartDto(shoppingCart, List.of(redCartItemDto));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        Pageable pageable = PageRequest.of(0, 10);
        ShoppingCartDto actualShoppingCart =
                shoppingCartService.getShoppingCartByUserEmail(user.getEmail(), pageable);
        assertNotNull(actualShoppingCart);
        assertEquals(actualShoppingCart, shoppingCartDto);
    }

    @Test
    @DisplayName("Verify get Shopping Cart by User's email works "
            + "when given a non existing user email")
    public void getShoppingCartByUserEmail_GivenNonExistingUserEmail_ShouldThrowsException() {
        Pageable pageable = PageRequest.of(0, 10);
        Mockito.when(userRepository.findByEmail(NON_EXISTING_USER_EMAIL))
                .thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> shoppingCartService.getShoppingCartByUserEmail(
                        NON_EXISTING_USER_EMAIL,
                        pageable
                )
        );
        String expected = "Can't find a user by email: " + NON_EXISTING_USER_EMAIL;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    private User getUserTest1() {
        return new User()
                .setId(1L)
                .setPassword(passwordEncoder.encode("1234"))
                .setEmail("test1@gmail.com")
                .setFirstName("Test")
                .setLastName("Test")
                .setShippingAddress("Test address")
                .setRoles(Collections.singleton(roleRepository
                        .findByRoleName(Role.RoleName.ROLE_USER)));
    }

    private Book getRedBookWithPrice19_99() {
        return new Book()
                .setId(1L)
                .setTitle("Red Book")
                .setAuthor("Red Author")
                .setIsbn("Red-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Red description")
                .setCoverImage("Red cover image");

    }

    private BookDto getRedBookDtoWithPrice19_99() {
        return new BookDto()
                .setId(1L)
                .setTitle("Red Book")
                .setAuthor("Red Author")
                .setIsbn("Red-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Red description")
                .setCoverImage("Red cover image");

    }

    private Book getGreenBookWithPrice19_99() {
        return new Book()
                .setId(2L)
                .setTitle("Green Book")
                .setAuthor("Green Author")
                .setIsbn("Green-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Green description")
                .setCoverImage("Green cover image");

    }

    private BookDto getGreenBookDtoWithPrice19_99() {
        return new BookDto()
                .setId(2L)
                .setTitle("Green Book")
                .setAuthor("Green Author")
                .setIsbn("Green-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Green description")
                .setCoverImage("Green cover image");

    }

    private AddBookToShoppingCartDto getAddBookToShoppingCartDto(Book book, int quantity) {
        return new AddBookToShoppingCartDto()
                .setBookId(1L)
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setQuantity(quantity);
    }

    private ShoppingCart getShoppingCart(User user) {
        return new ShoppingCart()
                .setId(1L)
                .setUser(user);
    }

    private CartItem getCartItem(ShoppingCart shoppingCart, Book book, int quantity) {
        return new CartItem()
                .setId(1L)
                .setShoppingCart(shoppingCart)
                .setBook(book)
                .setQuantity(quantity);
    }

    private ShoppingCartDto getShoppingCartDto(
            ShoppingCart shoppingCart,
            List<CartItemDto> cartItemDto
    ) {
        return new ShoppingCartDto()
                .setId(shoppingCart.getId())
                .setUserId(shoppingCart.getUser().getId())
                .setCartItems(cartItemDto);
    }

    private CartItemDto getCartItemDto(CartItem cartItem) {
        return new CartItemDto()
                .setId(cartItem.getId())
                .setBookId(cartItem.getBook().getId())
                .setBookTitle(cartItem.getBook().getTitle())
                .setQuantity(cartItem.getQuantity() + 1);
    }
}
