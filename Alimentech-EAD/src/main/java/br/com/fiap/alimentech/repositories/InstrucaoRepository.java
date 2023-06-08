package br.com.fiap.alimentech.repositories;

import br.com.fiap.alimentech.models.Instrucao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {
    boolean existsByIdAndUsuarioId(long instrucaoId, long usuarioId);

    @Query("SELECT r FROM T_INSTRUCAO r WHERE r.usuario.id = :usuarioId")
    Page<Instrucao> getAllRegisters(long usuarioId , Pageable pageable);
}