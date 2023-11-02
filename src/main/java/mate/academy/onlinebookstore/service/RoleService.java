package mate.academy.onlinebookstore.service;

import java.util.List;
import mate.academy.onlinebookstore.dto.role.CreateRoleRequestDto;
import mate.academy.onlinebookstore.dto.role.RoleDto;
import mate.academy.onlinebookstore.dto.role.UpdateRoleRequestDto;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    RoleDto save(CreateRoleRequestDto createRoleRequestDto);

    RoleDto update(Long id, UpdateRoleRequestDto updateRoleRequestDto);

    List<RoleDto> getAll(Pageable pageable);

    RoleDto findById(Long id);

    void deleteById(Long id);
}
