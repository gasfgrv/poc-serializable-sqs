package com.gasfgrv.example.sqs.producer.domain.ports;

import com.gasfgrv.example.sqs.producer.domain.models.Pedido;

public interface NotificarPort {

    void notificar(Pedido pedido);

}
