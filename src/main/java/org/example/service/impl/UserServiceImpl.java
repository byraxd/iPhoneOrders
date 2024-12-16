package org.example.service.impl;

import jakarta.persistence.OptimisticLockException;
import org.example.dto.UserDto;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public User addUser(UserDto userDto) {
        if (userDto == null) throw new NullPointerException("user is null");

        return userRepository.save(
                User
                        .builder()
                        .username(userDto.getUsername())
                        .password(userDto.getPassword())
                        .email(userDto.getEmail())
                        .walletBalance(userDto.getWalletBalance())
                        .createdAt(Instant.now())
                        .build()
        );
    }

    @Override
    public User updateUserById(Long id, UserDto userDto) {
        if (id == null || userDto == null) throw new NullPointerException("id or user is null");

        try {
            User userForUpdate = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("user not found with following id"));

            userForUpdate.setUsername(userDto.getUsername());
            userForUpdate.setPassword(userDto.getPassword());
            userForUpdate.setEmail(userDto.getEmail());
            userForUpdate.setWalletBalance(userDto.getWalletBalance());

            userForUpdate.setUpdatedAt(Instant.now());

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
