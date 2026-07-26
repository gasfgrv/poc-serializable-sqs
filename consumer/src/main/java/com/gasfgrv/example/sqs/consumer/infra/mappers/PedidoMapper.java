package com.gasfgrv.example.sqs.consumer.infra.mappers;

import com.gasfgrv.example.sqs.consumer.domain.models.Pedido;
import com.gasfgrv.example.sqs.consumer.domain.models.PedidoStatus;
import com.gasfgrv.example.sqs.consumer.proto.PedidoEventProto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Pedido toDomain(PedidoEventProto proto);

    @Named("stringToStatus")
    default PedidoStatus stringToStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        return PedidoStatus.obterStatus(status);
    }

}
