package pl.dashclever.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    private static final String ERROR_INPUT_MESSAGE = "error.form.validationError";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InputValidationErrorMessage> handle(MethodArgumentNotValidException ex) {
        final var objectErrors = ex.getAllErrors().stream()
            .filter(FieldError.class::isInstance)
            .map(FieldError.class::cast)
            .collect(Collectors.groupingBy(FieldError::getObjectName))
            .entrySet().stream()
            .map(entry -> {
                final List<ObjectFieldError> fieldErrors = entry.getValue().stream().map(this::map).toList();
                return new ObjectErrors(
                    entry.getKey(),
                    fieldErrors
                );
            }).toList();
        final InputValidationErrorMessage response = new InputValidationErrorMessage(objectErrors, ERROR_INPUT_MESSAGE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ObjectFieldError map(FieldError fieldError) {
        return new ObjectFieldError(
            fieldError.getField(),
            fieldError.getDefaultMessage()
        );
    }
}
