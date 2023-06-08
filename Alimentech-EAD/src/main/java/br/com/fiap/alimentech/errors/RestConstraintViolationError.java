package br.com.fiap.alimentech.errors;

public record RestConstraintViolationError(
        int code,
        Object field,
        String message
) {}
