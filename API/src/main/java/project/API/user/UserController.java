package project.API.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin // Allow all requests from all domains to access this API
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public User addUser(@RequestBody User user) {
        return this.userService.addUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable Long id) {
        return this.userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user) {
        return this.userService.updateUser(user);
    }
}
