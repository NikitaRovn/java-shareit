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

        userToUpdate.setName(
                userUpdateDto.getName() != null ? userUpdateDto.getName() : userToUpdate.getName()
        );

        String newEmail = userUpdateDto.getEmail();
        if (newEmail != null) {
            User existing = userRepository.findUserByUserEmail(newEmail);
            if (existing != null && !existing.getId().equals(userToUpdate.getId())) {
                throw new UserFoundException(newEmail);
            }
            userToUpdate.setEmail(newEmail);
        }

        return userRepository.updateUser(userToUpdate);
    }

    @Override
    public User getUser(Long userId) {
        User user = userRepository.findUserByUserId(userId);
        if (user == null) throw new UserNotFoundException(userId);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        User userToDelete = userRepository.findUserByUserId(userId);
        if (userToDelete == null) throw new UserNotFoundException(userId);

        userRepository.deleteUser(userId);
    }
}
