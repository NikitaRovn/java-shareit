package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserRegisterDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User registerUser(UserRegisterDto userRegisterDto) {
        User userToSave = UserMapper.mapFromUserRegisterDtoToUser(userRegisterDto);
        String email = userRegisterDto.getEmail();
        User userWithSameEmail = userRepository.findUserByUserEmail(email);
        if (userWithSameEmail != null) throw new UserFoundException(email);
        return userRepository.saveUser(userToSave);
    }

    @Override
    public User updateUser(UserUpdateDto userUpdateDto) {
        Long userId = userUpdateDto.getId();
        User userToUpdate = userRepository.findUserByUserId(userId);
        if (userToUpdate == null) throw new UserNotFoundException(userId);

        userToUpdate.setName(userUpdateDto.getName() != null ? userUpdateDto.getName() : userToUpdate.getName());

        String email = userUpdateDto.getEmail();
        if (email != null) {
            User userWithSameEmail = userRepository.findUserByUserEmail(email);
            if (userWithSameEmail != null) throw new UserFoundException(email);
        }
        userToUpdate.setEmail(userUpdateDto.getEmail() != null ? userUpdateDto.getEmail() : userToUpdate.getEmail());

        return userRepository.updateUser(userToUpdate);
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findUserByUserId(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        User userToDelete = userRepository.findUserByUserId(userId);
        if (userToDelete == null) throw new UserNotFoundException(userId);

        userRepository.deleteUser(userId);
    }
}
