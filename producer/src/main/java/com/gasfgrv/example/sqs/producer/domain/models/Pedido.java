package com.gasfgrv.example.sqs.producer.domain.models;

import org.apache.commons.text.RandomStringGenerator;

public record Pedido(String pedidoId, double valor, PedidoStatus status) {

    public static Pedido gerarPedido(double valor, String status) {
        var id = gerarId();
        var valorStatus = PedidoStatus.obterStatus(status);

        if (valorStatus == null) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }

        return new Pedido(id, valor, valorStatus);
    }

    private static String gerarId() {
        var generator = new RandomStringGenerator.Builder()
                .withinRange('A', 'z')
                .get();
        return "PED-" + generator.generate(10);
    }

}
