package com.gasfgrv.example.sqs.consumer.infra.listeners;

import com.gasfgrv.example.sqs.consumer.application.ReceberPedidoUsecase;
import com.gasfgrv.example.sqs.consumer.domain.models.Pedido;
import com.gasfgrv.example.sqs.consumer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.consumer.infra.config.containers.TestcontainersConfiguration;
import com.gasfgrv.example.sqs.consumer.proto.PedidoEventProto;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = {"aws.sqs.queue-url=pedidos"})
class PedidoListenerIntegrationTest {

    @Autowired
    private SqsTemplate sqsTemplate;

    @MockitoSpyBean
    private ReceberPedidoUsecase usecase;

    @Captor
    private ArgumentCaptor<Pedido> pedidoCaptor;

    @Test
    void deveOuvirMensagemDaFilaEProcessarComSucesso() {
        var protoBytes = PedidoEventProto.newBuilder()
                .setPedidoId("PED-999")
                .setValor(25.50)
                .setStatus("criado")
                .build()
                .toByteArray();

        var mensagemBase64 = Base64.getEncoder().encodeToString(protoBytes);

        sqsTemplate.send("pedidos", mensagemBase64);

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(usecase).receberPedido(pedidoCaptor.capture());
            var pedido = pedidoCaptor.getValue();
            assertThat(pedido).isNotNull();
            assertThat(pedido.pedidoId()).isEqualTo("PED-999");
            assertThat(pedido.valor()).isEqualTo(25.50);
            assertThat(pedido.status()).isEqualTo(PedidoStatus.CRIADO);
        });
    }

}