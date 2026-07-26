package com.gasfgrv.example.sqs.producer.infrastructure.mappers;

import com.gasfgrv.example.sqs.producer.domain.models.Pedido;
import com.gasfgrv.example.sqs.producer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.producer.infrastructure.dtos.PedidoRequest;
import com.gasfgrv.example.sqs.producer.proto.PedidoEventProto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToProto")
    PedidoEventProto toProto(Pedido pedido);


    @Mapping(target = "status", source = "status", qualifiedByName = "statusToPedido")
    Pedido toPedido(PedidoRequest request);

    @Named("statusToProto")
    default String statusToProto(PedidoStatus status) {
        return PedidoStatus.obterValor(status);
    }

    @Named("statusToPedido")
    default PedidoStatus statusToPedido(String status) {
        return PedidoStatus.obterStatus(status);
    }
}
