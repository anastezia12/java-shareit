package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        return UserMapper.toDto(userRepository.findById(id));
    }

    public UserDto add(UserDto userDto) {// add checking
        User user = UserMapper.fromDto(userDto);
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("email can not be empty");
        }
        if (userRepository.containsEmail(user.getEmail())) {
            throw new IllegalArgumentException("email:" + user.getEmail() + " is already exists");
        }

        return UserMapper.toDto(userRepository.add(user));
    }

    public UserDto update(UserDto userDto) {
        User user = UserMapper.fromDto(userDto);

        if (userRepository.containsEmail(user.getEmail())) {
            throw new IllegalArgumentException("email:" + user.getEmail() + " is already exists");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            user.setEmail(getById(user.getId()).getEmail());
        }
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            user.setName(getById(user.getId()).getName());
        }
        return UserMapper.toDto(userRepository.update(user));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
