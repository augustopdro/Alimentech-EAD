package br.com.fiap.alimentech.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public record LoginDTO(
    @NotNull
    @NotBlank
    @Email
    String email, 

    @NotNull 
    @NotBlank
    String senha
) {
    public Authentication toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, senha);
    }
}
