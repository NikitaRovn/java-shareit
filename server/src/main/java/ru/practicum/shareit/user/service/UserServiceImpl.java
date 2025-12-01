package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
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
    @Transactional
    public User registerUser(UserRegisterDto userRegisterDto) {
        User userToSave = UserMapper.mapFromUserRegisterDtoToUser(userRegisterDto);
        String email = userRegisterDto.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }

        return userRepository.save(userToSave);
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserUpdateDto userUpdateDto) {
        User userToUpdate = getExistingUser(id);

        if (userUpdateDto.getName() != null) {
            userToUpdate.setName(userUpdateDto.getName());
        }

        String newEmail = userUpdateDto.getEmail();
        if (newEmail != null && !newEmail.equals(userToUpdate.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(newEmail, id)) {
                throw new UserAlreadyExistsException(newEmail);
            }
            userToUpdate.setEmail(newEmail);
        }

        return userRepository.save(userToUpdate);
    }

    @Override
    public User getUser(Long id) {
        return getExistingUser(id);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        getExistingUser(id);
        userRepository.deleteById(id);
    }

    private User getExistingUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
