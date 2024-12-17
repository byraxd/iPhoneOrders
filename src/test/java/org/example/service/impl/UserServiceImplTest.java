package org.example.service.impl;

import org.example.dto.UserDto;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void test_getAllUsers_shouldReturnListOfAllUsers() {
        User user = new User();
        user.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        List<User> users = List.of(user, user2);

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> allUsers = userService.getAllUsers();

        Assertions.assertEquals(users, allUsers);

        Mockito.verify(userRepository).findAll();
    }

    @Test
    void test_getUserById_shouldReturnUserById() {
        User user = new User();
        user.setId(1L);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Assertions.assertEquals(user, userService.getUserById(user.getId()));

        Mockito.verify(userRepository).findById(user.getId());
    }

    @Test
    void test_addUser_shouldAddUser_WhenUserDtoNotNull() {
        UserDto request = UserDto
                .builder()
                .username("some username")
                .password("some password")
                .email("some@gmail.com")
                .walletBalance(10.00)
                .build();

        User excpetedUser = User
                .builder()
                .username("some username")
                .password("some password")
                .email("some@gmail.com")
                .walletBalance(10.00)
                .build();

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(excpetedUser);

        User result = userService.addUser(request);

        Mockito.verify(userRepository).save(Mockito.any(User.class));

        Assertions.assertEquals(excpetedUser.getUsername(), result.getUsername());
        Assertions.assertEquals(excpetedUser.getEmail(), result.getEmail());
        Assertions.assertEquals(excpetedUser.getWalletBalance(), result.getWalletBalance());
    }

    @Test
    void test_updateUserById_ShouldUpdateUser_WhenUserExists() {
        Long id = 1L;

        UserDto request = UserDto
                .builder()
                .username("some username")
                .password("some password")
                .email("some@gmail.com")
                .walletBalance(10.00)
                .build();

        User excpetedUser = User
                .builder()
                .id(id)
                .username("some username")
                .password("some password")
                .email("some@gmail.com")
                .walletBalance(10.00)
                .build();

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(excpetedUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(excpetedUser);

        User result = userService.updateUserById(id, request);

        Mockito.verify(userRepository).save(Mockito.any(User.class));

        Assertions.assertEquals(excpetedUser.getUsername(), result.getUsername());
        Assertions.assertEquals(excpetedUser.getEmail(), result.getEmail());
        Assertions.assertEquals(excpetedUser.getWalletBalance(), result.getWalletBalance());

    }

    @Test
    void test_deleteUserById_shouldDeleteUserById_WhenUserExists() {
        Long id = 1L;

        Mockito.when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUserById(id);

        Mockito.verify(userRepository).deleteById(id);
    }
}
