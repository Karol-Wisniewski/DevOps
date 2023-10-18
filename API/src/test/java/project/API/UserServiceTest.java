package project.API;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.API.user.User;
import project.API.user.UserRepository;
import project.API.user.UserService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers() {
        User user1 = new User();
        User user2 = new User();

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        userService.getAllUsers();

        Mockito.verify(userRepository).findAll();
    }

    @Test
    void getUserById() {
        User user1 = new User();
        user1.setId(1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user1));

        userService.getUserById(1L);

        Mockito.verify(userRepository).findById(1L);
    }

    @Test
    void addUser() {
        User user1 = new User();
        user1.setId(1L);

        Mockito.when(userRepository.save(user1)).thenReturn(user1);

        userService.addUser(user1);

        Mockito.verify(userRepository).save(user1);
    }

    @Test
    void updateUser() {
        User user1 = new User();
        user1.setId(1L);

        Mockito.when(userRepository.save(user1)).thenReturn(user1);

        userService.updateUser(user1);

        Mockito.verify(userRepository).save(user1);
    }

    @Test
    void deleteUser() {
        User user1 = new User();
        user1.setId(1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user1));

        userService.deleteUser(1L);

        Mockito.verify(userRepository).deleteById(1L);
    }
}