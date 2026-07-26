package com.gasfgrv.example.sqs.producer.infrastructure.adapters;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.gasfgrv.example.sqs.producer.domain.models.Pedido;
import com.gasfgrv.example.sqs.producer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.producer.infrastructure.configs.SqsProperties;
import com.gasfgrv.example.sqs.producer.infrastructure.configs.containers.TestcontainersConfiguration;

import io.awspring.cloud.sqs.operations.SqsTemplate;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = { "aws.sqs.queue-url=pedidos" })
class NotificadorAdapterIntegrationTest {

    @Autowired
    private NotificadorAdapter adapter;

    @Autowired
    private SqsTemplate sqsTemplate;

    @Autowired
    private SqsProperties properties;

    @Test
    void deveNotificarPedidoParaSqs() {
        Pedido pedido = new Pedido("PED-123456", 250.75, PedidoStatus.CRIADO);

        adapter.notificar(pedido);

        await().pollInterval(Duration.ofSeconds(1))
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    var message = sqsTemplate.receive(options -> options.queue(properties.queueUrl()));
                    assertThat(message).isPresent();
                });
    }

}
