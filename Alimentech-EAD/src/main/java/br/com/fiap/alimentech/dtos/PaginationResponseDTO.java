package br.com.fiap.alimentech.dtos;

import br.com.fiap.alimentech.models.Instrucao;

import java.util.List;

public record PaginationResponseDTO(
    List<Instrucao> content,
    int number,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last
) {}
