package mate.academy.onlinebookstore.dto.role;

import lombok.Data;
import mate.academy.onlinebookstore.model.Role;

@Data
public class RoleDto {
    private Long id;
    private Role.RoleName roleName;
}
