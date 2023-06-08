package br.com.fiap.alimentech.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Alimentech API")
                        .description("Uma API para o sistema de ensinar técnicas de agricultura familiar com ia generativa")
                        .summary("A API do Alimentech serve como a base para um aplicativo móvel que permite aos usuários " +
                                "obterem instruções adaptadas a suas limitações, de como fazer agricultura familiar com os recursos que possuem.")
                        .version("V1")
                        .contact(new Contact()
                                .name("Alimentech")
                                .email("alimentech@fiap.com.br")
                        )
                        .license(new License()
                                .name("MIT Open Soucer")
                                .url("http://alimentech.com/licenca")
                        )
                )
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                                        .bearerFormat("JWT")));
    }

}
