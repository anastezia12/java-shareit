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
        return userMapper.toUserDtoList(userRepository.getAll());
        //userRepository.getAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        return userMapper.toDto(userRepository.findById(id));
        //UserMapper.toDto(userRepository.findById(id));
    }

    public UserDto add(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
//        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
//            throw new IllegalArgumentException("email can not be empty");
//        }
        if (userRepository.containsEmail(user.getEmail())) {
            throw new IllegalArgumentException("email:" + user.getEmail() + " is already exists");
        }

        return userMapper.toDto(userRepository.add(user));
    }

    public UserDto update(UserDto userDto) {
        User user = userRepository.findById(userDto.getId());

        if (userRepository.containsEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("email:" + user.getEmail() + " is already exists");
        }
        userMapper.updateUserFromDto(userDto, user);
        return userMapper.toDto(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
