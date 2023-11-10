package mate.academy.onlinebookstore.service.cartitem;

import java.util.Optional;
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
    public Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId) {
        return Optional.of(cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCartId, bookId));
    }
}
