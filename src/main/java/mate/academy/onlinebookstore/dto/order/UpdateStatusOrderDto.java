package mate.academy.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.onlinebookstore.validation.IsOrderStatus;

@Data
public class UpdateStatusOrderDto {
    @NotNull
    @IsOrderStatus
    private String status;
}
