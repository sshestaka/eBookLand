package mate.academy.onlinebookstore.service.shoppingcart;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.book.AddBookToShoppingCartDto;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.dto.cartitem.ChangeCartItemQuantityDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.mapper.BookMapper;
import mate.academy.onlinebookstore.mapper.ShoppingCartMapper;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.onlinebookstore.repository.user.UserRepository;
import mate.academy.onlinebookstore.service.book.BookService;
import mate.academy.onlinebookstore.service.cartitem.CartItemService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;
    private final UserRepository userRepository;
    private final BookService bookService;
    private final BookMapper bookMapper;

    @Override
    public ShoppingCart save(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto getShoppingCartByUserEmail(String userEmail, Pageable pageable) {
        User userByEmail = findUserByEmail(userEmail);
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userByEmail.getId()));
    }

    @Override
    public ShoppingCartDto addCartItem(AddBookToShoppingCartDto addBookToShoppingCartDto,
                                       String userEmail) {
        User userByEmail = findUserByEmail(userEmail);
        ShoppingCart userShoppingCart = shoppingCartRepository.findByUserId(userByEmail.getId());
        BookDto bookById = bookService.findById(addBookToShoppingCartDto.getBookId());
        List<CartItem> existedCartItemsList = cartItemService
                .findByShoppingCartId(userShoppingCart.getId());
        Optional<CartItem> existedCartItem = existedCartItemsList.stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(bookById.getId()))
                .findFirst();
        if (existedCartItem.isPresent()) {
            return shoppingCartMapper.toDto(increaseQuantityOfExistedCartItem(
                    existedCartItem,
                    existedCartItemsList,
                    addBookToShoppingCartDto,
                    userShoppingCart
            ));
        }
        return shoppingCartMapper.toDto(createNewCartItem(
                userShoppingCart,
                bookById,
                addBookToShoppingCartDto
        ));
    }

    @Override
    public ShoppingCartDto updateQuantityByItemId(
            Long cartItemId,
            ChangeCartItemQuantityDto changeCartItemQuantityDto,
            String userEmail
    ) {
        User userByEmail = findUserByEmail(userEmail);
        ShoppingCart userShoppingCart = shoppingCartRepository.findByUserId(userByEmail.getId());
        return shoppingCartMapper.toDto(updateQuantityOfExistedCartItem(
                        cartItemId,
                        changeCartItemQuantityDto,
                        userShoppingCart
                ));
    }

    @Override
    public void deleteById(Long cartItemId) {
        cartItemService.deleteById(cartItemId);
    }

    private User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail).orElseThrow(() ->
                new NoSuchElementException("Can't find a user by email: " + userEmail));
    }

    private ShoppingCart increaseQuantityOfExistedCartItem(
            Optional<CartItem> existedCartItem,
            List<CartItem> existedCartItemsList,
            AddBookToShoppingCartDto addBookToShoppingCartDto,
            ShoppingCart userShoppingCart
    ) {
        CartItem cartItem = existedCartItem.get();
        int quantity = cartItem.getQuantity();
        cartItem.setQuantity(quantity + addBookToShoppingCartDto.getQuantity());
        cartItemService.save(cartItem);
        userShoppingCart.setCartItems(existedCartItemsList.stream().collect(Collectors.toSet()));
        return userShoppingCart;
    }

    private ShoppingCart updateQuantityOfExistedCartItem(
            Long cartItemId,
            ChangeCartItemQuantityDto changeCartItemQuantityDto,
            ShoppingCart userShoppingCart
    ) {
        CartItem existedCartItem = cartItemService.findById(cartItemId);
        existedCartItem.setQuantity(changeCartItemQuantityDto.quantity());
        cartItemService.save(existedCartItem);
        List<CartItem> existedCartItemsList = cartItemService
                .findByShoppingCartId(userShoppingCart.getId());
        userShoppingCart.setCartItems(existedCartItemsList.stream().collect(Collectors.toSet()));
        return userShoppingCart;
    }

    private ShoppingCart createNewCartItem(
            ShoppingCart userShoppingCart,
            BookDto bookById,
            AddBookToShoppingCartDto addBookToShoppingCartDto
    ) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(userShoppingCart);
        cartItem.setBook(bookMapper.toModelFromBookDto(bookById));
        cartItem.setQuantity(addBookToShoppingCartDto.getQuantity());
        cartItemService.save(cartItem);
        userShoppingCart.getCartItems().add(cartItem);
        return userShoppingCart;
    }
}
