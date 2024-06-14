package pl.dashclever.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RuntimeExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RuntimeExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException ex) {
        LOG.error("Unhandled runtime exception", ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "No message";
        String cause = ex.getCause() != null ? ex.getCause().getMessage() : "no reason";
        ErrorMessage errorMessage = new ErrorMessage(message, cause);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
