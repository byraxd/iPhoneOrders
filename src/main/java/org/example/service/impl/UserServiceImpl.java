package org.example.service.impl;

import jakarta.persistence.OptimisticLockException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        if (id == null) throw new NullPointerException("id is null");

        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("user not found with following id"));
    }

    @Override
    public User addUser(User user) {
        if (user == null) throw new NullPointerException("user is null");

        return userRepository.save(
                User
                        .builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .walletBalance(user.getWalletBalance())
                        .build()
        );
    }

    @Override
    public User updateUserById(Long id, User user) {
        try {
            if (id == null || user == null) throw new NullPointerException("id or user is null");

            User userForUpdate = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("user not found with following id"));

            userForUpdate.setUsername(user.getUsername());
            userForUpdate.setPassword(user.getPassword());
            userForUpdate.setEmail(user.getEmail());
            userForUpdate.setWalletBalance(user.getWalletBalance());

            return userRepository.save(userForUpdate);
        } catch (OptimisticLockException e) {
            throw new OptimisticLockException("Another user already updated this record already", e);
        }
    }

    @Override
    public void deleteUserById(Long id) {
        if (id == null) throw new NullPointerException("id is null");
        if (!userRepository.existsById(id)) throw new NoSuchElementException("user not found with following id");

        userRepository.deleteById(id);
    }
}
