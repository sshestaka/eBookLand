package mate.academy.onlinebookstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import mate.academy.onlinebookstore.validation.IsBigDecimal;

@Data
public class CreateBookRequestDto {
    //private Long id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    private String isbn;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    @IsBigDecimal
    private BigDecimal price;
    private String description;
    private String coverImage;
}
