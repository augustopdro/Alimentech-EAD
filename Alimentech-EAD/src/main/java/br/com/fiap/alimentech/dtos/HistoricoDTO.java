package br.com.fiap.alimentech.dtos;

import br.com.fiap.alimentech.models.Instrucao;

import java.util.List;

public record HistoricoDTO(List<Instrucao> registros) {}

