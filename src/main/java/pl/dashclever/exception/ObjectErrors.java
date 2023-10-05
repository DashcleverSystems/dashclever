package pl.dashclever.exception;

import java.util.List;

public record ObjectErrors(
    String objectName,
    List<ObjectFieldError> fieldErrors
) {
}

