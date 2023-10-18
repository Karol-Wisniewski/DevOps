package project.API.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class userNotFoundException extends RuntimeException {
    public userNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}
