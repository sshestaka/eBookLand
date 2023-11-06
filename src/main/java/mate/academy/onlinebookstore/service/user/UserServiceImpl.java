package mate.academy.onlinebookstore.service.user;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.onlinebookstore.dto.user.UpdateUserRequestDto;
import mate.academy.onlinebookstore.dto.user.UserDto;
import mate.academy.onlinebookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.onlinebookstore.dto.user.UserResponseDto;
import mate.academy.onlinebookstore.exception.RegistrationException;
import mate.academy.onlinebookstore.mapper.UserMapper;
import mate.academy.onlinebookstore.model.Role;
import mate.academy.onlinebookstore.model.User;
import mate.academy.onlinebookstore.repository.role.RoleRepository;
import mate.academy.onlinebookstore.repository.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("User with email " + request.getEmail()
                    + " is already exist.");
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setShippingAddress(request.getShippingAddress());
        user.setRoles(Collections.singleton(roleRepository
                .findByRoleName(Role.RoleName.ROLE_USER)));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public UserDto update(Long id, UpdateUserRequestDto updateUserRequestDto) {
        User updatedUser = userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find a book by id: " + id));
        updatedUser(updatedUser, updateUserRequestDto);
        return userMapper.toDto(userRepository.save(updatedUser));
    }

    @Override
    public List<UserDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto findById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Can't find a book by id: " + id)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    private void updatedUser(User updatedUser, UpdateUserRequestDto updateUserRequestDto) {
        if (updateUserRequestDto.getEmail() != null) {
            updatedUser.setEmail(updateUserRequestDto.getEmail());
        }
        if (updateUserRequestDto.getFirstName() != null) {
            updatedUser.setFirstName(updateUserRequestDto.getFirstName());
        }
        if (updateUserRequestDto.getLastName() != null) {
            updatedUser.setLastName(updateUserRequestDto.getLastName());
        }
        if (updateUserRequestDto.getPassword() != null) {
            updatedUser.setPassword(updateUserRequestDto.getPassword());
        }
        if (updateUserRequestDto.getShippingAddress() != null) {
            updatedUser.setShippingAddress(updateUserRequestDto.getShippingAddress());
        }
    }
}
