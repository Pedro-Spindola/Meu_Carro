package com.drivenote.config;

import com.drivenote.exception.ApiError;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI baseOpenAPI() {
        var resolved = ModelConverters.getInstance()
                .readAllAsResolvedSchema(new AnnotatedType(ApiError.class));

        Map<String, Schema> schemas = new HashMap<>();
        if (resolved.referencedSchemas != null) {
            schemas.putAll(resolved.referencedSchemas);
        }
        schemas.put("ApiError", resolved.schema);

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .schemas(schemas)
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer globalResponsesCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    ApiResponses responses = operation.getResponses();

                    responses.addApiResponse("400", buildResponse("Bad Request", example400()));
                    responses.addApiResponse("401", buildResponse("Unauthorized", example401()));
                    responses.addApiResponse("403", buildResponse("Forbidden", example403()));
                    responses.addApiResponse("404", buildResponse("Not Found", example404()));
                    responses.addApiResponse("500", buildResponse("Internal Server Error", example500()));
                })
        );
    }

    private ApiResponse buildResponse(String description, Example example) {
        Schema<?> schema = new Schema<>().$ref("#/components/schemas/ApiError");

        MediaType mediaType = new MediaType()
                .schema(schema)
                .addExamples("default", example);

        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType("application/json", mediaType));
    }

    private Example example400() {
        return new Example()
                .summary("Erro de validação")
                .value(Map.of(
                        "status", 400,
                        "error", "Bad Request",
                        "message", "Erro de validação",
                        "path", "/api/usuarios",
                        "correlationId", "8a4e1f0e-2c77-4b7e-9a9e-1a2b3c4d5e6f",
                        "timestamp", "2026-05-18T22:10:00",
                        "fieldErrors", List.of(
                                Map.of("field", "email", "message", "deve ser um e-mail válido")
                        )
                ));
    }

    private Example example401() {
        return new Example()
                .summary("Token ausente/ inválido")
                .value(Map.of(
                        "status", 401,
                        "error", "Unauthorized",
                        "message", "Token inválido ou ausente",
                        "path", "/api/abastecimentos",
                        "correlationId", "7b1d4a2c-5f11-4d2b-8f02-123456789abc",
                        "timestamp", "2026-05-18T22:11:00",
                        "fieldErrors", List.of()
                ));
    }

    private Example example403() {
        return new Example()
                .summary("Sem permissão")
                .value(Map.of(
                        "status", 403,
                        "error", "Forbidden",
                        "message", "Você não tem permissão para acessar este recurso",
                        "path", "/api/abastecimentos",
                        "correlationId", "2f9c7b1a-4d88-4a7e-9b1d-abcdef123456",
                        "timestamp", "2026-05-18T22:12:00",
                        "fieldErrors", List.of()
                ));
    }

    private Example example404() {
        return new Example()
                .summary("Recurso não encontrado")
                .value(Map.of(
                        "status", 404,
                        "error", "Not Found",
                        "message", "Recurso não encontrado",
                        "path", "/api/veiculos/999",
                        "correlationId", "c1a2b3c4-d5e6-7890-abcd-ef1234567890",
                        "timestamp", "2026-05-18T22:13:00",
                        "fieldErrors", List.of()
                ));
    }

    private Example example500() {
        return new Example()
                .summary("Erro interno")
                .value(Map.of(
                        "status", 500,
                        "error", "Internal Server Error",
                        "message", "Erro interno no servidor",
                        "path", "/api/relatorios",
                        "correlationId", "e7d6c5b4-a321-4f9e-8d77-1234567890ab",
                        "timestamp", "2026-05-18T22:14:00",
                        "fieldErrors", List.of()
                ));
    }
}