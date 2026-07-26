package com.gasfgrv.example.sqs.producer.domain.models;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class PedidoStatusTest {

    @Test
    void deveObterStatusQuandoValorForValido() {
        var statusValido = "criado";

        var status = PedidoStatus.obterStatus(statusValido);

        assertThat(status).isEqualTo(PedidoStatus.CRIADO);
    }

    @Test
    void deveRetornarNuloQuandoObterStatusComValorInvalido() {
        var statusInvalido = "inexistente";

        var status = PedidoStatus.obterStatus(statusInvalido);

        assertThat(status).isNull();
    }

    @Test
    void deveObterValorQuandoStatusForFornecido() {
        var status = PedidoStatus.CONFIRMADO;

        var valor = PedidoStatus.obterValor(status);

        assertThat(valor).isEqualTo("confirmado");
    }

}
