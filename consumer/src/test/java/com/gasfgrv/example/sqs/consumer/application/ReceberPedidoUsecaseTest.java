package com.gasfgrv.example.sqs.consumer.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import com.gasfgrv.example.sqs.consumer.domain.models.Pedido;
import com.gasfgrv.example.sqs.consumer.domain.models.PedidoStatus;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
class ReceberPedidoUsecaseTest {

    @InjectMocks
    private ReceberPedidoUsecase usecase;

    @Test
    void deveReceberPedidoComSucesso(CapturedOutput output) {
        var pedido = new Pedido("PED-123", 10.0, PedidoStatus.CRIADO);

        assertThatCode(() -> usecase.receberPedido(pedido))
                .doesNotThrowAnyException();

        assertThat(output.getOut()).contains("Pedido recebido: ");
    }

}
