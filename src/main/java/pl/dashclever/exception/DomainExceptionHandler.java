package pl.dashclever.exception;

import static pl.dashclever.commons.exception.I18nErrorMessagesKt.DOMAIN_ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import pl.dashclever.commons.exception.DomainException;


@ControllerAdvice
public class DomainExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DaoExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorMessage> handleDomainException(DomainException ex) {
        LOG.error("Domain exception thrown!", ex);
        String cause = ex.getCause() != null ? ex.getMessage() : "Unknown reason";
        ErrorMessage errorMessage = new ErrorMessage(DOMAIN_ERROR, cause);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessage);
    }
}
