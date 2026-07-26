package com.gasfgrv.example.sqs.consumer.domain.models;

public record Pedido(String pedidoId, double valor, PedidoStatus status) {
}
