package mate.academy.onlinebookstore.service.shoppingcart;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.book.AddBookToShoppingCartDto;
import mate.academy.onlinebookstore.dto.book.BookDto;
import mate.academy.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import mate.academy.onlinebookstore.mapper.BookMapper;
import mate.academy.onlinebookstore.mapper.CartItemMapper;
import mate.academy.onlinebookstore.mapper.ShoppingCartMapper;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.model.ShoppingCart;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.repository.book.BookRepository;
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
    private final UserRepository userRepository;
    private final CartItemService cartItemService;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
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
    public ShoppingCartDto addItem(AddBookToShoppingCartDto addBookToShoppingCartDto,
                                   String userEmail) {
        User userByEmail = findUserByEmail(userEmail);

        ShoppingCart userShoppingCart = shoppingCartRepository.findByUserId(userByEmail.getId());

        BookDto bookById = bookService.findById(addBookToShoppingCartDto.getBookId());

        List<CartItem> cartItemsSet = cartItemService
                .findByShoppingCartId(userShoppingCart.getId());

        if (cartItemsSet.size() != 0) {

            Optional<CartItem> first = cartItemsSet.stream()
                    .filter(cartItem -> cartItem.getBook().getId().equals(bookById.getId()))
                    .findFirst();

            if (first.isPresent()) {
                CartItem cartItem = first.get();
                int quantity = cartItem.getQuantity();
                cartItem.setQuantity(quantity + addBookToShoppingCartDto.getQuantity());
                cartItemService.save(cartItem);

                return shoppingCartMapper.toDto(userShoppingCart);
            }

        }

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(userShoppingCart);
        cartItem.setBook(bookMapper.toModelFromBookDto(bookById));
        cartItem.setQuantity(addBookToShoppingCartDto.getQuantity());
        cartItemService.save(cartItem);

        userShoppingCart.setCartItems(Collections.singleton(cartItem));

        return shoppingCartMapper.toDto(userShoppingCart);
    }

    @Override
    public void deleteById(Long id) {
        ShoppingCart deletedShoppingCart = shoppingCartRepository.findByUserId(id);
        shoppingCartRepository.deleteById(deletedShoppingCart.getId());
    }

    private User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail).orElseThrow(() ->
                new NoSuchElementException("Can't find a user by email: " + userEmail));
    }
}
