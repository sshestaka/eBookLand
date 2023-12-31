package mate.academy.onlinebookstore.mapper;

import mate.academy.onlinebookstore.config.MapperConfig;
import mate.academy.onlinebookstore.dto.user.UserDto;
import mate.academy.onlinebookstore.dto.user.UserResponseDto;
import mate.academy.onlinebookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    UserResponseDto toUserResponse(User user);
}
