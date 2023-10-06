package pl.dashclever.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class MethodArgumentNotValidExceptionHandlerTest {

    @Test
    void shouldMapToProperErrorObject_givenSpringValidationException() {
        // given
        final var exceptionHandler = new MethodArgumentNotValidExceptionHandler();
        final BindingResult bindingResult = Mockito.mock(BindingResult.class);
        final List<ObjectError> errors = List.of(
            new FieldError("someObjectName", "field1", "someMessage1"),
            new FieldError("someObjectName", "field2", "someMessage2")
        );
        Mockito.doReturn(errors).when(bindingResult).getAllErrors();
        final var methodParameter = Mockito.mock(MethodParameter.class);
        final var exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        // when
        final ResponseEntity<InputValidationErrorMessage> result = exceptionHandler.handle(exception);

        // then
        assertThat(result.getBody()).isNotNull();
        final List<ObjectErrors> objectErrorsList = result.getBody().errors();
        assertThat(objectErrorsList).hasSize(1);
        ObjectErrors objectErrors = objectErrorsList.get(0);
        assertThat(objectErrors.objectName()).isEqualTo("someObjectName");
        assertThat(objectErrors.fieldErrors()).hasSize(2);
        assertThat(objectErrors.fieldErrors()).anyMatch(fieldError -> fieldError.field().equals("field1"));
        assertThat(objectErrors.fieldErrors()).anyMatch(fieldError -> fieldError.message().equals("someMessage1"));
        assertThat(objectErrors.fieldErrors()).anyMatch(fieldError -> fieldError.field().equals("field2"));
        assertThat(objectErrors.fieldErrors()).anyMatch(fieldError -> fieldError.message().equals("someMessage2"));
    }
}
