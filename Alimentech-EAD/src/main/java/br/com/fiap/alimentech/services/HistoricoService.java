package br.com.fiap.alimentech.services;

import br.com.fiap.alimentech.dtos.PaginationResponseDTO;
import br.com.fiap.alimentech.repositories.InstrucaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistoricoService {

    Logger log = LoggerFactory.getLogger(HistoricoService.class);
    private InstrucaoRepository instrucaoRepository;

    @Autowired
    public HistoricoService(InstrucaoRepository instrucaoRepository) {
        this.instrucaoRepository = instrucaoRepository;
    }

    public PaginationResponseDTO recuperarHistorico(long userId, Pageable pageable) {
        log.info("Buscando historico de instruções do usuário: " + userId);

        var instrucoes = instrucaoRepository.getAllRegisters(userId, pageable);

        return new PaginationResponseDTO(
            instrucoes.getContent(),
            instrucoes.getNumber(),
            instrucoes.getTotalElements(),
            instrucoes.getTotalPages(),
            instrucoes.isFirst(),
            instrucoes.isLast()
        );
    }
}
