package com.gasfgrv.example.sqs.producer.domain.models;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PedidoStatus {

    CRIADO("criado"),
    PENDENTE("pendente"),
    CONFIRMADO("confirmado"),
    CANCELADO("cancelado");

    private final String status;

    public static PedidoStatus obterStatus(String status) {
        return Arrays.stream(values())
                .filter(s -> s.status.equalsIgnoreCase(status))
                .findFirst().orElse(null);
    }

    public static String obterValor(PedidoStatus status) {
        return status.status.toLowerCase();
    }

}
