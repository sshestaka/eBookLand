package mate.academy.onlinebookstore.dto.book;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddBookToShoppingCartDto {
    @NotNull
    private Long bookId;
    private String title;
    private String author;
    private int quantity;
}
