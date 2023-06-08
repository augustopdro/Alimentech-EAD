package br.com.fiap.alimentech.errors;

public record RestValidationError(
        Integer code,
        String field,
        String message
) {}
