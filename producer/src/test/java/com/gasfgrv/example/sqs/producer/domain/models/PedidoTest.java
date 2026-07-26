package com.gasfgrv.example.sqs.producer.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class PedidoTest {

    @Test
    void deveGerarPedidoQuandoDadosForemValidos() {
        var valor = 100.50;
        var status = "pendente";

        var pedido = Pedido.gerarPedido(valor, status);

        assertThat(pedido).isNotNull();
        assertThat(pedido.valor()).isEqualTo(valor);
        assertThat(pedido.status()).isEqualTo(PedidoStatus.PENDENTE);
        assertThat(pedido.pedidoId()).startsWith("PED-").hasSize(14);
    }

    @Test
    void deveLancarExcecaoQuandoGerarPedidoComStatusInvalido() {
        var valor = 50.0;
        var statusInvalido = "desconhecido";

        assertThatThrownBy(() -> Pedido.gerarPedido(valor, statusInvalido))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Status inválido: " + statusInvalido);
    }

}
