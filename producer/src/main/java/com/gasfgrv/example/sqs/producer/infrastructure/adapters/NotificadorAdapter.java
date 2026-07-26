package com.gasfgrv.example.sqs.producer.infrastructure.adapters;

import java.util.Base64;

import org.springframework.stereotype.Component;

import com.gasfgrv.example.sqs.producer.domain.models.Pedido;
import com.gasfgrv.example.sqs.producer.domain.ports.NotificarPort;
import com.gasfgrv.example.sqs.producer.infrastructure.configs.SqsProperties;
import com.gasfgrv.example.sqs.producer.infrastructure.mappers.PedidoMapper;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificadorAdapter implements NotificarPort {

    private final SqsTemplate publisher;
    private final PedidoMapper mapper;
    private final SqsProperties properties;

    @Override
    public void notificar(Pedido pedido) {
        var proto = mapper.toProto(pedido);
        var byteArray = proto.toByteArray();
        var payload = Base64.getEncoder().encodeToString(byteArray);

        publisher.send(options -> options.queue(properties.queueUrl())
                .payload(payload));

        log.info("Mensagem enviada ao SQS com sucesso! ID: {}, Tamanho binário: {} bytes, Tamanho Base64: {} chars",
                pedido.pedidoId(),
                byteArray.length,
                payload.length());
    }

}
