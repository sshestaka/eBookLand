package mate.academy.onlinebookstore.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
             HttpHeaders headers,
             HttpStatusCode status,
             WebRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST);
        List<String> errorsList = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        body.put("errors", errorsList);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = {NoSuchElementException.class })
    protected ResponseEntity<Object> handleNoSuchElementException(RuntimeException exc) {
        NoSuchElementException noSuchElementException = (NoSuchElementException) exc;
        HttpStatusCode statusCode = HttpStatus.NOT_FOUND;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode);
        body.put("errors", noSuchElementException.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, statusCode);
    }

    @ExceptionHandler(value = {RegistrationException.class })
    protected ResponseEntity<Object> registrationException(RuntimeException exc) {
        RegistrationException registrationException = (RegistrationException) exc;
        HttpStatusCode statusCode = HttpStatus.NOT_ACCEPTABLE;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode);
        body.put("errors", registrationException.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, statusCode);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class })
    protected ResponseEntity<Object> usernameNotFoundException(RuntimeException exc) {
        UsernameNotFoundException usernameNotFoundException = (UsernameNotFoundException) exc;
        HttpStatusCode statusCode = HttpStatus.NOT_FOUND;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", statusCode);
        body.put("errors", usernameNotFoundException.getMessage());
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(body, headers, statusCode);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String defaultMessage = e.getDefaultMessage();
            return field + " " + defaultMessage;
        }
        return e.getDefaultMessage();
    }
}
