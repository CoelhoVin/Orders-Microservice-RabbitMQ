package com.example.orderms.listener;

import com.example.orderms.listener.dto.OrderCreatedEvent;
import com.example.orderms.service.OrderService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.example.orderms.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
@Data
public class OrderCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    @Autowired
    private final OrderService orderService;

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message){
        logger.info("Message consumed: {}", message.getPayload());

        orderService.save(message.getPayload());
    }
}
