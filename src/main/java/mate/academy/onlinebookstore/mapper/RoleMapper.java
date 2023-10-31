package mate.academy.onlinebookstore.mapper;

import mate.academy.onlinebookstore.config.MapperConfig;
import mate.academy.onlinebookstore.dto.role.CreateRoleRequestDto;
import mate.academy.onlinebookstore.dto.role.RoleDto;
import mate.academy.onlinebookstore.model.Role;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RoleMapper {
    RoleDto toDto(Role role);

    Role toModel(CreateRoleRequestDto requestDto);
}
