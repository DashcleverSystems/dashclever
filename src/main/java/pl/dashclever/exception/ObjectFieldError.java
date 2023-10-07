package pl.dashclever.exception;

public record ObjectFieldError(
    String field,
    String message
) {
}
