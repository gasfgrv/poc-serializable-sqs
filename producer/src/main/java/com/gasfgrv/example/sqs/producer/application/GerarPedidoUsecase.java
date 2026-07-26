package com.gasfgrv.example.sqs.producer.application;

import com.gasfgrv.example.sqs.producer.domain.models.Pedido;
import com.gasfgrv.example.sqs.producer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.producer.domain.ports.NotificarPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GerarPedidoUsecase {

    private final NotificarPort notificador;

    public void gerarPedido(Pedido pedido) {
        try {
            pedido = Pedido.gerarPedido(pedido.valor(), PedidoStatus.obterValor(pedido.status()));
            notificador.notificar(pedido);
        } catch (Exception e) {
            log.error("Erro ao gerar pedido", e);
            throw new RuntimeException("Erro ao gerar pedido", e);
        } finally {
            Thread.currentThread().interrupt();
        }
    }
}
