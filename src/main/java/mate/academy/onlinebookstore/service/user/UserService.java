package mate.academy.onlinebookstore.service.user;

import java.util.List;
import mate.academy.onlinebookstore.dto.user.UpdateUserRequestDto;
import mate.academy.onlinebookstore.dto.user.UserDto;
import mate.academy.onlinebookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.onlinebookstore.dto.user.UserResponseDto;
import mate.academy.onlinebookstore.exception.RegistrationException;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;

    UserDto update(Long id, UpdateUserRequestDto updateUserRequestDto);

    List<UserDto> getAll(Pageable pageable);

    UserDto findById(Long id);

    void deleteById(Long id);
}
