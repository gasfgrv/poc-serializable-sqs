package com.gasfgrv.example.sqs.producer.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gasfgrv.example.sqs.producer.domain.models.Pedido;
import com.gasfgrv.example.sqs.producer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.producer.domain.ports.NotificarPort;

@ExtendWith(MockitoExtension.class)
class GerarPedidoUsecaseTest {

    @Mock
    private NotificarPort notificador;

    @InjectMocks
    private GerarPedidoUsecase usecase;

    @Captor
    private ArgumentCaptor<Pedido> pedidoCaptor;

    @Test
    void deveGerarPedidoENotificarComSucesso() {
        var pedido = new Pedido(null, 10.0, PedidoStatus.CRIADO);

        usecase.gerarPedido(pedido);

        verify(notificador).notificar(pedidoCaptor.capture());
        var pedidoCapturado = pedidoCaptor.getValue();

        assertThat(pedidoCapturado).isNotNull();
        assertThat(pedidoCapturado.valor()).isEqualTo(10.0);
        assertThat(pedidoCapturado.status()).isEqualTo(PedidoStatus.CRIADO);
        assertThat(pedidoCapturado.pedidoId()).startsWith("PED-").hasSize(14);
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoNotificar() {
        var pedido = new Pedido(null, 10.0, PedidoStatus.CRIADO);
        doThrow(new RuntimeException("Erro de conexão")).when(notificador).notificar(any(Pedido.class));

        assertThatThrownBy(() -> usecase.gerarPedido(pedido))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Erro ao gerar pedido")
                .hasCauseInstanceOf(RuntimeException.class);

        verify(notificador).notificar(any(Pedido.class));
    }

}
