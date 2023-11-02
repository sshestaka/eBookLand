package mate.academy.onlinebookstore.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.onlinebookstore.model.Role;

@Data
public class CreateRoleRequestDto {
    @NotNull
    private Role.RoleName roleName;
}
