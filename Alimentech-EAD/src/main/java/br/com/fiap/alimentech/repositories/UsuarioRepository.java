package br.com.fiap.alimentech.repositories;

import br.com.fiap.alimentech.models.Recurso;
import br.com.fiap.alimentech.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM T_USUARIO u WHERE u.email = ?1 AND u.senha = ?2")
    Optional<Usuario> buscarCredenciais(String email, String senha);

    @Query("SELECT u FROM T_USUARIO u WHERE u.email = ?1")
    Optional<Usuario> buscarEmail(String email);

    @Modifying
    @Query("UPDATE T_USUARIO SET recurso = :recurso WHERE id = :userId")
    void atualizarRecursoUsuario(@Param("recurso") Recurso recurso, @Param("userId") Long userId);
}
