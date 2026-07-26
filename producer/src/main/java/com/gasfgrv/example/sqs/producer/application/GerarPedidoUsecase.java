package com.gasfgrv.example.sqs.producer.application;

import org.springframework.stereotype.Service;

import com.gasfgrv.example.sqs.producer.domain.models.Pedido;
import com.gasfgrv.example.sqs.producer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.producer.domain.ports.NotificarPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GerarPedidoUsecase {

    private final NotificarPort notificador;

    public void gerarPedido(Pedido pedido) {
        try {
            gerarNovoPedidoENotificar(pedido);
        } catch (Exception e) {
            trataErro(e);
        } finally {
            interromperExecucao();
        }
    }

    private void gerarNovoPedidoENotificar(Pedido pedido) {
        var statusNovoPedido = PedidoStatus.obterValor(pedido.status());
        var novoPedido = Pedido.gerarPedido(pedido.valor(), statusNovoPedido);
        notificador.notificar(novoPedido);
    }

    private void trataErro(Exception e) {
        String mensagemErro = "Erro ao gerar pedido";
        log.error(mensagemErro, e);
        throw new RuntimeException(mensagemErro, e);
    }

    private void interromperExecucao() {
        Thread.currentThread().interrupt();
    }

}
