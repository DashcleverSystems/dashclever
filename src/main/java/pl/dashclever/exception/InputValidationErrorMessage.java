package pl.dashclever.exception;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public record InputValidationErrorMessage(
    List<ObjectErrors> errors,
    @Nullable String message
) {
}

