package org.example.service;

import org.example.dto.UserDto;
import org.example.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User addUser(UserDto userDto);

    User updateUserById(Long id, UserDto userDto);

    void deleteUserById(Long id);
}
