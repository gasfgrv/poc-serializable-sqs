package com.gasfgrv.example.sqs.producer.infrastructure.controllers;

import com.gasfgrv.example.sqs.producer.infrastructure.configs.containers.TestcontainersConfiguration;
import com.gasfgrv.example.sqs.producer.infrastructure.dtos.PedidoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class PedidoControllerTestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveGerarPedidoComSucessoRetornandoAccepted() throws Exception {
        PedidoRequest request = new PedidoRequest("criado", 100.0);
        String jsonPayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.resposta").value("Pedido gerado com sucesso e enviado para notificação"));
    }
}