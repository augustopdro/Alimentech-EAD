package br.com.fiap.alimentech.utils;


import lombok.Data;

import java.util.List;

@Data
public class OpenAiResponse {
    private List<Choice> choices;
}
