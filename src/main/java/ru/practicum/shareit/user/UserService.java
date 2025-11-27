package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public List<UserDto> getAll() {
        return userMapper.toUserDtoList(userRepository.findAll());
    }

    public UserDto getById(Long id) {
        return userMapper.toDto(userRepository.getReferenceById(id));
    }

    public UserDto add(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        if (!userRepository.findByEmailContainingIgnoreCase(user.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("email:" + user.getEmail() + " is already exists");
        }

        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto update(UserDto userDto) {
        User user = userRepository.getReferenceById(userDto.getId());

        if (!userRepository.findByEmailContainingIgnoreCase(userDto.getEmail()).isEmpty()) {
            throw new IllegalArgumentException("email:" + user.getEmail() + " is already exists");
        }
        userMapper.updateUserFromDto(userDto, user);
        return userMapper.toDto(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
