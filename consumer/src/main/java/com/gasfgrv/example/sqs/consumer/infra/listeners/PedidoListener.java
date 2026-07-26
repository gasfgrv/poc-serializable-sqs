package com.gasfgrv.example.sqs.consumer.infra.listeners;

import com.gasfgrv.example.sqs.consumer.application.ReceberPedidoUsecase;
import com.gasfgrv.example.sqs.consumer.infra.config.SqsProperties;
import com.gasfgrv.example.sqs.consumer.infra.mappers.PedidoMapper;
import com.gasfgrv.example.sqs.consumer.proto.PedidoEventProto;
import com.google.protobuf.InvalidProtocolBufferException;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoListener {

    private final PedidoMapper mapper;
    private final TaskExecutor taskExecutor;
    private final ReceberPedidoUsecase usecase;
    private final SqsProperties properties;

    @SqsListener("${aws.sqs.queue-url}")
    public void ouvirMensagem(String mensagem) {
        log.info("Nova mensagem em Base64 recebida da fila {}", properties.queueUrl());
        taskExecutor.execute(() -> converterBase64ParaPedido(mensagem));
    }

    private void converterBase64ParaPedido(String mensagem) {
        try {
            var bytes = Base64.getDecoder().decode(mensagem);
            var proto = PedidoEventProto.parseFrom(bytes);
            var pedido = mapper.toDomain(proto);
            log.info("Mensagem recebida e desserializada com sucesso: {}", pedido);
            usecase.receberPedido(pedido);
        } catch (IllegalArgumentException e) {
            log.error("Erro ao decodificar Base64: ", e);
            throw e;
        } catch (InvalidProtocolBufferException e) {
            log.error("Erro ao desserializar Protobuf: ", e);
            throw new RuntimeException("Erro ao processar contrato Protobuf", e);
        }
    }

}
