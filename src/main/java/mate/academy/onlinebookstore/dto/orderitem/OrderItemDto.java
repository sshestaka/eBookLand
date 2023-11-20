package mate.academy.onlinebookstore.dto.orderitem;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private BigDecimal price;
    private int quantity;
}
