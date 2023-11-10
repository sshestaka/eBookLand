package mate.academy.onlinebookstore.service.shoppingcart;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
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
        // get user from userEmail
        User userByEmail = findUserByEmail(userEmail);
        // get shoppingCart by userId
        ShoppingCart userShoppingCart = shoppingCartRepository.findByUserId(userByEmail.getId());
        //get book by id
        BookDto bookById = bookService.findById(addBookToShoppingCartDto.getBookId());
        //get cartItems and convert them into List for next check
        List<CartItem> cartItemList = userShoppingCart.getCartItems()
                .stream()
                .toList();
        //if cartItemList isn't empty
        if (!cartItemList.isEmpty()) {
            for (CartItem cartItem : cartItemList) {
                if (cartItem.getBook().getId().equals(bookById.getId())) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                }
            }
            Set<CartItem> newCartItemSet = cartItemList.stream().collect(Collectors.toSet());
            userShoppingCart.setCartItems(newCartItemSet);
            return shoppingCartMapper.toDto(userShoppingCart);
        }
        //if cartItemList is empty - create new cartItem and add it to shopping cart
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(userShoppingCart);
        cartItem.setBook(bookMapper.toModelFromBookDto(bookById));
        cartItem.setQuantity(addBookToShoppingCartDto.getQuantity());
        cartItemService.save(cartItem);
        userShoppingCart.setCartItems(Collections.singleton(cartItem));
        ShoppingCartDto dto = shoppingCartMapper.toDto(shoppingCartRepository
                .save(userShoppingCart));
        return dto;
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
