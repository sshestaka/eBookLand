package mate.academy.onlinebookstore.service;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import mate.academy.onlinebookstore.dto.cartitem.CartItemDto;
import mate.academy.onlinebookstore.mapper.CartItemMapper;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.Role;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.repository.cartitem.CartItemRepository;
import mate.academy.onlinebookstore.repository.role.RoleRepository;
import mate.academy.onlinebookstore.service.cartitem.CartItemServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {
    private static final int RED_BOOK_QUANTITY = 10;
    private static final Long NON_EXISTING_ID = 100L;
    @InjectMocks
    private CartItemServiceImpl cartItemService;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

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

    private ShoppingCart getShoppingCart() {
        return new ShoppingCart()
                .setId(1L)
                .setUser(getUserTest1());
    }

    private CartItem getCartitem() {
        return new CartItem()
                .setId(1L)
                .setShoppingCart(getShoppingCart())
                .setBook(getRedBookWithPrice19_99())
                .setQuantity(RED_BOOK_QUANTITY);
    }

    private CartItemDto getCartitemDto() {
        return new CartItemDto()
                .setId(getCartitem().getId())
                .setBookId(getCartitem().getBook().getId())
                .setBookTitle(getCartitem().getBook().getTitle())
                .setQuantity(getCartitem().getQuantity());
    }

    @Test
    @DisplayName("Verify save() method works")
    public void save_GivenValidCartItem_ShouldReturnCartItemDto() {
        CartItem cartItem = getCartitem();
        CartItemDto cartItemDto = getCartitemDto();
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);
        CartItemDto actual = cartItemService.save(cartItem);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, cartItemDto);
    }

    @Test
    @DisplayName("Verify finById method works with non valid id")
    public void findById_GivenNonValidId_ShouldThrowsException() {
        Mockito.when(cartItemRepository.findById(NON_EXISTING_ID))
                .thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> cartItemService.findById(NON_EXISTING_ID)
        );
        String expected = "Can't find an item by id: " + NON_EXISTING_ID;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }
}
