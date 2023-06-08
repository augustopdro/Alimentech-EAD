package br.com.fiap.alimentech.services;

import br.com.fiap.alimentech.exceptions.RestNotFoundException;
import br.com.fiap.alimentech.models.Recurso;
import br.com.fiap.alimentech.models.Usuario;
import br.com.fiap.alimentech.repositories.RecursoRepository;
import br.com.fiap.alimentech.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecursoService {

    private RecursoRepository repository;
    private UsuarioRepository usuarioRepository;

    Logger log = LoggerFactory.getLogger(RecursoService.class);

    @Autowired
    public RecursoService(RecursoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }


    public Recurso cadastrarRecurso(Recurso novoRecurso, long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RestNotFoundException("Usuário não encontrado"));

        Recurso recursoExistente = usuario.getRecurso();

        if (recursoExistente != null) {
            recursoExistente.setPrazoParaColheita(novoRecurso.getPrazoParaColheita());
            recursoExistente.setDinheiroDisponivel(novoRecurso.getDinheiroDisponivel());
            recursoExistente.setAreaDoTerreno(novoRecurso.getAreaDoTerreno());

            repository.save(recursoExistente);
        } else {
            novoRecurso.setUsuario(usuario);
            recursoExistente = repository.save(novoRecurso);
            usuario.setRecurso(recursoExistente);
            usuarioRepository.save(usuario);
        }

        return recursoExistente;
    }


    public Recurso recuperarRecurso(long userId)
    {
        log.info("buscando recurso com id: " + userId);

        Usuario usuario = usuarioRepository
                .findById(userId)
                .orElseThrow(() -> new RestNotFoundException("Usuario não encontrado"));

        return usuario.getRecurso();
    }


}
