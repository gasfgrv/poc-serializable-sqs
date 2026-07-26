package com.gasfgrv.example.sqs.consumer.infra.listeners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;

import com.gasfgrv.example.sqs.consumer.application.ReceberPedidoUsecase;
import com.gasfgrv.example.sqs.consumer.domain.models.Pedido;
import com.gasfgrv.example.sqs.consumer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.consumer.infra.config.SqsProperties;
import com.gasfgrv.example.sqs.consumer.infra.mappers.PedidoMapper;
import com.gasfgrv.example.sqs.consumer.proto.PedidoEventProto;
import com.google.protobuf.InvalidProtocolBufferException;

@ExtendWith(MockitoExtension.class)
class PedidoListenerTest {

    @Mock
    private PedidoMapper mapper;

    @Mock
    private TaskExecutor taskExecutor;

    @Mock
    private ReceberPedidoUsecase usecase;

    @Mock
    private SqsProperties properties;

    @InjectMocks
    private PedidoListener listener;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Captor
    private ArgumentCaptor<Pedido> pedidoCaptor;

    @Test
    void deveOuvirMensagemEProcessarComSucesso() {
        var protoBytes = PedidoEventProto.newBuilder().build().toByteArray();
        var mensagemBase64 = Base64.getEncoder().encodeToString(protoBytes);
        var pedido = new Pedido("PED-123", 10.0, PedidoStatus.CRIADO);

        when(properties.queueUrl()).thenReturn("http://localhost:4566/000000000000/minha-fila");
        when(mapper.toDomain(any(PedidoEventProto.class))).thenReturn(pedido);

        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(taskExecutor).execute(any(Runnable.class));

        listener.ouvirMensagem(mensagemBase64);

        verify(taskExecutor).execute(runnableCaptor.capture());
        verify(mapper).toDomain(any(PedidoEventProto.class));
        verify(usecase).receberPedido(pedidoCaptor.capture());

        var pedidoCapturado = pedidoCaptor.getValue();
        assertThat(pedidoCapturado).isNotNull();
        assertThat(pedidoCapturado.pedidoId()).isEqualTo("PED-123");
        assertThat(pedidoCapturado.valor()).isEqualTo(10.0);
        assertThat(pedidoCapturado.status()).isEqualTo(PedidoStatus.CRIADO);
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoDecodificarBase64() {
        var mensagemInvalida = "não_é_base64!";
        when(properties.queueUrl()).thenReturn("http://localhost:4566/000000000000/minha-fila");

        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(taskExecutor).execute(any(Runnable.class));

        assertThatThrownBy(() -> listener.ouvirMensagem(mensagemInvalida))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(mapper);
        verifyNoInteractions(usecase);
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoDesserializarProtobuf() {
        var bytesInvalidos = new byte[] { 0x0F };
        var mensagemBase64 = Base64.getEncoder().encodeToString(bytesInvalidos);

        when(properties.queueUrl()).thenReturn("http://localhost:4566/000000000000/minha-fila");

        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(taskExecutor).execute(any(Runnable.class));

        assertThatThrownBy(() -> listener.ouvirMensagem(mensagemBase64))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Erro ao processar contrato Protobuf")
                .hasCauseInstanceOf(InvalidProtocolBufferException.class);

        verifyNoInteractions(mapper);
        verifyNoInteractions(usecase);
    }

}
