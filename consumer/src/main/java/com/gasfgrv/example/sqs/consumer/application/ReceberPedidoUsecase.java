package com.gasfgrv.example.sqs.consumer.application;

import org.springframework.stereotype.Service;

import com.gasfgrv.example.sqs.consumer.domain.models.Pedido;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceberPedidoUsecase {

    public void receberPedido(Pedido pedido) {
        log.info("Pedido recebido: {}", pedido.toString());
    }

}
