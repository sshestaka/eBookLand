package mate.academy.onlinebookstore.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCategoryDto {
    private String name;
    private String description;
}
