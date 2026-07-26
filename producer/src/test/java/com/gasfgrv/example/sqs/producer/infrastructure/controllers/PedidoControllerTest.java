package com.gasfgrv.example.sqs.producer.infrastructure.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;

import com.gasfgrv.example.sqs.producer.application.GerarPedidoUsecase;
import com.gasfgrv.example.sqs.producer.domain.models.Pedido;
import com.gasfgrv.example.sqs.producer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.producer.infrastructure.dtos.PedidoRequest;
import com.gasfgrv.example.sqs.producer.infrastructure.mappers.PedidoMapper;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private GerarPedidoUsecase usecase;

    @Mock
    private PedidoMapper mapper;

    @Mock
    private TaskExecutor executor;

    @InjectMocks
    private PedidoController controller;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Test
    void deveGerarPedidoERetornarStatusAccepted() {
        var request = new PedidoRequest("criado", 100.0);
        var pedido = new Pedido("PED-123", 100.0, PedidoStatus.CRIADO);

        when(mapper.toPedido(request)).thenReturn(pedido);

        doAnswer(invocation -> {
            var runnable = (Runnable) invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(executor).execute(any(Runnable.class));

        var response = controller.gerarPedido(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody())
                .isNotNull()
                .containsEntry("resposta", "Pedido gerado com sucesso e enviado para notificação");

        verify(mapper).toPedido(request);
        verify(executor).execute(runnableCaptor.capture());
        verify(usecase).gerarPedido(pedido);
    }

}
