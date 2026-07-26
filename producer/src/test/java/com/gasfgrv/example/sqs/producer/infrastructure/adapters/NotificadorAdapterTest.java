package com.gasfgrv.example.sqs.producer.infrastructure.adapters;

import com.gasfgrv.example.sqs.producer.domain.models.Pedido;
import com.gasfgrv.example.sqs.producer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.producer.infrastructure.configs.SqsProperties;
import com.gasfgrv.example.sqs.producer.infrastructure.mappers.PedidoMapper;
import com.gasfgrv.example.sqs.producer.proto.PedidoEventProto;
import io.awspring.cloud.sqs.operations.SqsSendOptions;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificadorAdapterTest {

    @Mock
    private SqsTemplate publisher;

    @Mock
    private PedidoMapper mapper;

    @Mock
    private SqsProperties properties;

    @InjectMocks
    private NotificadorAdapter adapter;

    @Captor
    private ArgumentCaptor<Consumer<SqsSendOptions<Object>>> optionsCaptor;

    @Test
    void deveNotificarComSucesso() {
        var pedido = new Pedido("PED-123", 150.0, PedidoStatus.CONFIRMADO);

        var protoMock = mock(PedidoEventProto.class);
        var byteArrayMock = "conteudo-proto".getBytes();
        when(mapper.toProto(pedido)).thenReturn(protoMock);
        when(protoMock.toByteArray()).thenReturn(byteArrayMock);

        adapter.notificar(pedido);

        verify(mapper).toProto(pedido);
        verify(publisher).send(optionsCaptor.capture());

        var optionsConsumer = optionsCaptor.getValue();
        assertThat(optionsConsumer).isNotNull();
    }

}
