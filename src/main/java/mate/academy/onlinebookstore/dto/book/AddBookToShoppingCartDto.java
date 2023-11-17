package mate.academy.onlinebookstore.dto.book;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddBookToShoppingCartDto {
    @NotNull
    private Long bookId;
    private String title;
    private String author;
    private int quantity;
}
