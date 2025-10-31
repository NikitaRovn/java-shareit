package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRegisterDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static UserDto mapFromUserToUserDto(User user) {
        if (user == null) {
            return new UserDto();
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User mapFromUserRegisterDtoToUser(UserRegisterDto userRegisterDto) {
        if (userRegisterDto == null) {
            return new User();
        }
        return User.builder()
                .name(userRegisterDto.getName())
                .email(userRegisterDto.getEmail())
                .build();
    }

    public static User mapFromUserUpdateDtoToUser(UserUpdateDto userUpdateDto) {
        if (userUpdateDto == null) {
            return new User();
        }
        return User.builder()
                .id(userUpdateDto.getId())
                .name(userUpdateDto.getName())
                .email(userUpdateDto.getEmail())
                .build();
    }
}
