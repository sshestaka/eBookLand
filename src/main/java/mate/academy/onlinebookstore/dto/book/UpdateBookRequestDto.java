package mate.academy.onlinebookstore.dto.book;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateBookRequestDto {
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
