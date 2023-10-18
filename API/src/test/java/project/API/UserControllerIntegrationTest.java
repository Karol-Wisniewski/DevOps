package project.API;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import project.API.user.User;

import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        StringBuilder sb = new StringBuilder("http://localhost:");
        sb.append(port);
        sb.append("/users");
        baseUrl = sb.toString();
    }

    @Test
    @Disabled
    public void testDeleteUserThrowException() {
        ResponseEntity<String> response = restTemplate.getForEntity("/users/1", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(statements = {"INSERT INTO users (id, name, email, password) VALUES (1L, 'test', 'test', 'test')"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {"TRUNCATE TABLE users"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetUserById() {
        ResponseEntity<User> response = restTemplate.getForEntity("/users/1", User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test", response.getBody().getName());
        assertEquals("test", response.getBody().getEmail());
        assertEquals("test", response.getBody().getPassword());
    }


    @Test
    @Sql(statements = {"INSERT INTO users (id, name, email, password) VALUES (1L, 'test', 'test', 'test')"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {"TRUNCATE TABLE users"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange("/users", HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>(){});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @Sql(statements = {"TRUNCATE TABLE users"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAddUser() {
        User user = new User();
        user.setName("test");
        user.setEmail("test");
        user.setPassword("test");

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test", response.getBody().getName());
        assertEquals("test", response.getBody().getEmail());
        assertEquals("test", response.getBody().getPassword());
    }

    @Test
    @Sql(statements = {"INSERT INTO users (id, name, email, password) VALUES (1L, 'test', 'test', 'test')"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {"TRUNCATE TABLE users"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateUser() {
        User user = new User();
        user.setName("test1");
        user.setEmail("test1");
        user.setPassword("test1");
        HttpEntity<User> userEntity = new HttpEntity<>(user);

        ResponseEntity<User> response = restTemplate.exchange("/users/1", HttpMethod.PUT, userEntity, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test1", response.getBody().getName());
        assertEquals("test1", response.getBody().getEmail());
        assertEquals("test1", response.getBody().getPassword());
        assertNotNull(response.getBody().getUpdatedAt());
    }

    @Test
    @Sql(statements = {"INSERT INTO users (id, name, email, password) VALUES (1L, 'test', 'test', 'test')"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = {"TRUNCATE TABLE users"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteUser() {
        ResponseEntity<User> response = restTemplate.exchange("/users/1", HttpMethod.DELETE, null, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", response.getBody().getName());
        response = restTemplate.getForEntity("/users/1", User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
