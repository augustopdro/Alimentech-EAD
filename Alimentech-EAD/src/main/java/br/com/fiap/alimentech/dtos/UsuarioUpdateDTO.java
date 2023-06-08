package br.com.fiap.alimentech.dtos;

public record UsuarioUpdateDTO
(
    String nome,
    String email,
    String senha,
    String cidade
) {}
