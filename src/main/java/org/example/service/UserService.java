package org.example.service;

import org.example.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User addUser(User user);

    User updateUserById(Long id, User user);

    void deleteUserById(Long id);
}
