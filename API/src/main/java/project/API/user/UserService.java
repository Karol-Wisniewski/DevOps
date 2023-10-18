package project.API.user;

import org.springframework.stereotype.Service;
import project.API.user.exception.userNotFoundException;

import java.util.List;
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new userNotFoundException(id));
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User deleteUser(Long id) {
        User toBeDeletedUser = userRepository.findById(id).orElseThrow(() -> new userNotFoundException(id));
        userRepository.deleteById(id);
        return toBeDeletedUser;
    }
}
