package br.com.fiap.alimentech.controllers;

import br.com.fiap.alimentech.dtos.*;
import br.com.fiap.alimentech.models.*;
import br.com.fiap.alimentech.services.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/agricultura")
@SecurityRequirement(name = "bearer-key")
public class AgriculturaController {

    private HistoricoService historicoService;
    private RecursoService recursoService;
    private InstrucaoService instrucaoService;
    private Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    public AgriculturaController(InstrucaoService instrucaoService, HistoricoService historicoService, RecursoService recursoService) {
        this.instrucaoService = instrucaoService;
        this.historicoService = historicoService;
        this.recursoService = recursoService;
    }

    @PostMapping("{userId}/recurso")
    public ResponseEntity<EntityModel<Recurso>> cadastrarRecurso(@Valid @RequestBody Recurso recurso, @PathVariable long userId)
    {
        log.info("Cadastrando recurso");
        Recurso responseService = recursoService.cadastrarRecurso(recurso, userId);

        var entityModel = EntityModel.of(
                responseService,
                linkTo(methodOn(AgriculturaController.class).cadastrarRecurso(recurso, userId)).withSelfRel(),
                linkTo(methodOn(AgriculturaController.class).recuperarRecurso(userId)).withRel("recuperar")
        );

        return ResponseEntity.created(linkTo(methodOn(AgriculturaController.class).cadastrarRecurso(recurso, userId)).toUri())
                .body(entityModel);
    }

    @GetMapping("{userId}/recurso")
    public EntityModel<Recurso> recuperarRecurso(@PathVariable long userId)
    {
        log.info("Buscando recurso");
        var recurso = recursoService.recuperarRecurso(userId);

        return EntityModel.of(
                recurso,
                linkTo(methodOn(AgriculturaController.class).recuperarRecurso(userId)).withSelfRel(),
                linkTo(methodOn(AgriculturaController.class).cadastrarRecurso(recurso, userId)).withRel("cadastrar")
        );
    }

    @GetMapping("{userId}/instrucao")
    public ResponseEntity<EntityModel<Instrucao>> cadastrarInstrucao(@PathVariable long userId)
    {
        log.info("Cadastrando recurso");
        Instrucao responseService = instrucaoService.cadastrarInstrucao(userId);

        var entityModel = EntityModel.of(
                responseService,
                linkTo(methodOn(AgriculturaController.class).cadastrarInstrucao(userId)).withSelfRel(),
                linkTo(methodOn(AgriculturaController.class).recuperarRecurso(userId)).withRel("recuperar")
        );

        return ResponseEntity.created(linkTo(methodOn(AgriculturaController.class).cadastrarInstrucao(userId)).toUri())
                .body(entityModel);
    }


    @DeleteMapping("{userId}/deletar/{instrucaoId}")
    public ResponseEntity<Instrucao> deletarInstrucao(@PathVariable long userId, @PathVariable long instrucaoId)
    {
        log.info("Deletando instrução");

        instrucaoService.deletarInstrucao(userId, instrucaoId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("{userId}/historico")
    public ResponseEntity<EntityModel<PaginationResponseDTO>> recuperarHistorico(@PathVariable long userId, @PageableDefault(size = 3) Pageable pageable)
    {
        log.info("Buscando historico");

        var historicoEncontrado = historicoService.recuperarHistorico(userId, pageable);

        var entityModel = EntityModel.of(
                historicoEncontrado,
                linkTo(methodOn(AgriculturaController.class).recuperarHistorico(userId, pageable)).withSelfRel(),
                linkTo(methodOn(AgriculturaController.class).recuperarRecurso(userId)).withRel("recurso")
        );

        return ResponseEntity.ok(entityModel);
    }
}
