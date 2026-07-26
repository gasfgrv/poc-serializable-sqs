package com.gasfgrv.example.sqs.producer.infrastructure.controllers;

import com.gasfgrv.example.sqs.producer.application.GerarPedidoUsecase;
import com.gasfgrv.example.sqs.producer.infrastructure.dtos.PedidoRequest;
import com.gasfgrv.example.sqs.producer.infrastructure.mappers.PedidoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pedidos")
public class PedidoController {

    private final GerarPedidoUsecase usecase;
    private final PedidoMapper mapper;
    private final TaskExecutor executor;

    @PostMapping
    public ResponseEntity<Map<String, String>> gerarPedido(@RequestBody PedidoRequest request) {
        var pedido = mapper.toPedido(request);
        executor.execute(() -> usecase.gerarPedido(pedido));
        return ResponseEntity.accepted()
                .body(Map.of("resposta", "Pedido gerado com sucesso e enviado para notificação"));
    }

}

