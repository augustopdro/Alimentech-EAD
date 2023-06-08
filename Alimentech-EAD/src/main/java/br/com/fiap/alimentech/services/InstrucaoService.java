package br.com.fiap.alimentech.services;

import br.com.fiap.alimentech.exceptions.RestNotFoundException;
import br.com.fiap.alimentech.models.Instrucao;
import br.com.fiap.alimentech.models.Recurso;
import br.com.fiap.alimentech.models.Usuario;
import br.com.fiap.alimentech.repositories.InstrucaoRepository;
import br.com.fiap.alimentech.repositories.UsuarioRepository;
import br.com.fiap.alimentech.utils.ChatRequest;
import br.com.fiap.alimentech.utils.Message;
import br.com.fiap.alimentech.utils.OpenAiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstrucaoService {

    private InstrucaoRepository repository;
    private UsuarioRepository usuarioRepository;

    Logger log = LoggerFactory.getLogger(InstrucaoService.class);

    @Autowired
    public InstrucaoService(InstrucaoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }


    public Instrucao cadastrarInstrucao(long userId) {
        Usuario usuario = recuperarUsuario(userId);
        Recurso recurso = recuperarRecurso(userId);

        String KEY = "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";
        String MODEL = "gpt-3.5-turbo";

        String systemMessage = "Você é um assistente que dá dicas sobre plantio para consumo próprio. " +
                "Lembre-se de fornecer informações sobre técnicas de agricultura sustentável, como agricultura vertical, aquaponia";
        String userMessage = String.format("Eu possuo uma área de %sm² para plantio, moro na região de %s, " +
                        "consigo esperar %s até a colheita e tenho os seguintes recursos: %s",
                recurso.getAreaDoTerreno(), usuario.getCidade(), recurso.getPrazoParaColheita(), recurso.getDinheiroDisponivel());

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", systemMessage));
        messages.add(new Message("user", userMessage));

        ChatRequest chatRequest = new ChatRequest(MODEL, messages);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost("https://api.openai.com/v1/chat/completions");
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + KEY);

            ObjectMapper objectMapper = new ObjectMapper();
            StringEntity entity = new StringEntity(objectMapper.writeValueAsString(chatRequest));
            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("HTTP Error: " + response.getStatusLine().getStatusCode());
                return null;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder responseBuilder = new StringBuilder();
            String output;
            while ((output = reader.readLine()) != null) {
                responseBuilder.append(output);
            }
            String responseText = responseBuilder.toString();

            OpenAiResponse openAiResponse = objectMapper.readValue(responseText, OpenAiResponse.class);
            String answer = openAiResponse.getChoices().get(0).getMessage().getContent();

            Instrucao instrucao = new Instrucao(answer, LocalDate.now());
            instrucao.setUsuario(usuario);
            usuario.getInstrucao().add(instrucao);
            repository.save(instrucao);

            return instrucao;

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    @Transactional
    public void deletarInstrucao(long userId, long instrucaoId) {
        var usuario = recuperarUsuario(userId);

        var instrucao = recuperarInstrucao(instrucaoId);

        if (!instrucao.getUsuario().equals(usuario)) {
            log.info("getid: " + instrucao.getUsuario().getId());
            throw new RestNotFoundException("Instrução informada não pertence ao Usuário informado");
        }

        usuario.getInstrucao().remove(instrucao);
        usuarioRepository.save(usuario);
        repository.delete(instrucao);
    }

    private Usuario recuperarUsuario(long userId) {
        log.info("Recuperando usuario com id: " + userId);

        return usuarioRepository
                .findById(userId)
                .orElseThrow(() -> new RestNotFoundException("Usuario não encontrado"));
    }

    private Instrucao recuperarInstrucao(long instrucaoId) {
        log.info("Recuperando instrução com id: " + instrucaoId);

        return repository
                .findById(instrucaoId)
                .orElseThrow(() -> new RestNotFoundException("Instrução não encontrada"));
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
