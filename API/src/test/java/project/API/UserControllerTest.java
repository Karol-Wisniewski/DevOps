package project.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import project.API.user.User;
import project.API.user.UserController;
import project.API.user.UserService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.API.user.exception.userNotFoundException;

@EnableWebMvc
@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(new User(1L, "John", "john@example.com", "pswd"), new User(2L, "Alice", "alice@example.com", "pswd"));
        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[1].name", is("Alice")));

        logger.info("Get all users test passed");
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User(1L, "John", "john@example.com", "pswd");
        Mockito.when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        logger.info("Get user by id test passed");
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenThrow(new userNotFoundException(1L));

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        logger.info("Get user by id test passed");
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User(1L, "John", "john@example.com", "pswd");
        Mockito.when(userService.addUser(ArgumentMatchers.any(User.class))).thenReturn(user);

        String userJson = "{\"name\":\"John\",\"email\":\"john@example.com\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        logger.info("Create user test passed");
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User(1L, "John", "john@example.com", "pswd");

        User updatedUser = new User(1L, "Janek", "john@example.com", "pswd");

        Mockito.when(userService.updateUser(ArgumentMatchers.any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Janek")))
                .andExpect(jsonPath("$.email", is("john@example.com")));

        logger.info("Update user test passed");
    }

    @Test
    public void testDeleteUser() throws Exception {
        Long id = 1L;

        User toBeDeletedUser = new User(1L, "Janek", "john@example.com", "pswd", new Date(), new Date());

        Mockito.when(userService.deleteUser(id)).thenReturn(toBeDeletedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/1"))
                .andExpect(jsonPath("$.name", is("Janek")))
                .andExpect(jsonPath("$.email", is("john@example.com")))
                .andExpect(status().isOk());

        logger.info("Delete user test passed");
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        Long id = 1L;
        Mockito.when(userService.deleteUser(id)).thenThrow(new userNotFoundException(id));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/1"))
                .andExpect(status().isNotFound());

        logger.info("Delete user test passed");
    }
}
