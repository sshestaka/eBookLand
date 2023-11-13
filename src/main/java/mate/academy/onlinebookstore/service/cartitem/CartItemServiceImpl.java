package mate.academy.onlinebookstore.service.cartitem;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.cartitem.CartItemDto;
import mate.academy.onlinebookstore.mapper.CartItemMapper;
import mate.academy.onlinebookstore.model.CartItem;
import mate.academy.onlinebookstore.repository.cartitem.CartItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemDto save(CartItem cartItem) {
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItem findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId) {
        return cartItemRepository.findByShoppingCartIdAndBookId(shoppingCartId, bookId);
    }

    @Override
    public List<CartItem> findByShoppingCartId(Long shoppingCartId) {
        return cartItemRepository.findByShoppingCartId(shoppingCartId);
    }
}
