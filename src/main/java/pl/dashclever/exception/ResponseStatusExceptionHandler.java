package pl.dashclever.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ResponseStatusExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> handleException(ResponseStatusException ex) {
        String cause = ex.getCause() != null ? ex.getCause().getMessage() : "";
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), cause);
        return ResponseEntity.status(ex.getStatusCode()).body(errorMessage);
    }
}
