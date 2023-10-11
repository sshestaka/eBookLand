package mate.academy.onlinebookstore.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
