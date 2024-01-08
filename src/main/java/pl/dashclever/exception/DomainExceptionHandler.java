package pl.dashclever.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.dashclever.commons.exception.DomainException;

import static pl.dashclever.commons.exception.I18nErrorMessagesKt.DOMAIN_ERROR;

@ControllerAdvice
public class DomainExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorMessage> handleDomainException(DomainException ex) {
        String cause = ex.getCause() != null ? ex.getMessage() : "Unknown reason";
        ErrorMessage errorMessage = new ErrorMessage(DOMAIN_ERROR, cause);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessage);
    }
}
