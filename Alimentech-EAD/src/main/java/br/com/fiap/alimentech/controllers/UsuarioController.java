package br.com.fiap.alimentech.controllers;

import br.com.fiap.alimentech.dtos.*;
import br.com.fiap.alimentech.exceptions.RestBadRequestException;
import br.com.fiap.alimentech.models.Usuario;
import br.com.fiap.alimentech.services.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private UsuarioService usuarioService;


    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @PostMapping("cadastrar")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> cadastrar(@Valid @RequestBody Usuario usuario)
    {
        var usuarioCadastrado = usuarioService.cadastrar(usuario);

        var responseDTO = new UsuarioResponseDTO(usuarioCadastrado.getId(), usuarioCadastrado.getNome(), usuarioCadastrado.getEmail(), usuarioCadastrado.getSenha(), usuarioCadastrado.getCidade());

        var entityModel = EntityModel.of(
                responseDTO,
                linkTo(methodOn(UsuarioController.class).cadastrar(usuario)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).atualizar(new UsuarioUpdateDTO(usuarioCadastrado.getNome(), usuarioCadastrado.getEmail(), usuarioCadastrado.getSenha(), usuarioCadastrado.getCidade()), usuarioCadastrado.getId())).withRel("atualizar"),
                linkTo(methodOn(UsuarioController.class).logar(new LoginDTO(usuarioCadastrado.getEmail(), usuarioCadastrado.getSenha()))).withRel("logar")
        );

        return ResponseEntity.created(linkTo(methodOn(UsuarioController.class).cadastrar(usuario)).toUri())
                .body(entityModel);
    }

    @PutMapping("{id}")
    public EntityModel<UsuarioResponseDTO> atualizar(@RequestBody UsuarioUpdateDTO usuario, @PathVariable long id)
    {
        log.info("Atualizando cadastro de usuario pelo id: " + id);

        UsuarioResponseDTO responseService = usuarioService.atualizar(usuario, id);

        if(responseService == null)
            throw new RestBadRequestException("Atualização não efetuada. Tente novamente com dados diferentes.");

        return EntityModel.of(
                responseService,
                linkTo(methodOn(UsuarioController.class).atualizar(usuario, id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).cadastrar(new Usuario(usuario.nome(), usuario.email(), usuario.senha(), usuario.cidade()))).withRel("cadastrar"),
                linkTo(methodOn(UsuarioController.class).logar(new LoginDTO(usuario.email(), usuario.senha()))).withRel("logar")
        );
    }

    @PostMapping("login")
    public EntityModel<TokenDTO> logar(@Valid @RequestBody LoginDTO credenciais)
    {
        log.info("solicitando validação das credenciais informadas");

        TokenDTO responseService = usuarioService.logar(credenciais);

        return EntityModel.of(
                responseService,
                linkTo(methodOn(UsuarioController.class).logar(credenciais)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).cadastrar(new Usuario())).withRel("cadastrar")
        );
    }
}