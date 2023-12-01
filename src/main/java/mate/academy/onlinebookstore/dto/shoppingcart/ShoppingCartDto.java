package mate.academy.onlinebookstore.dto.shoppingcart;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import mate.academy.onlinebookstore.dto.cartitem.CartItemDto;

@Data
@Accessors(chain = true)
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
}
