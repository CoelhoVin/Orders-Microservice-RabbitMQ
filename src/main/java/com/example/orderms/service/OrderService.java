package com.example.orderms.service;

import com.example.orderms.controller.dto.OrderResponse;
import com.example.orderms.entity.OrderEntity;
import com.example.orderms.entity.OrderItem;
import com.example.orderms.listener.dto.OrderCreatedEvent;
import com.example.orderms.repository.OrderRepository;
import lombok.Data;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@Data
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final MongoTemplate mongoTemplate;

    public void save(OrderCreatedEvent orderCreatedEvent){

        var entity = new OrderEntity();
        entity.setOrderId(orderCreatedEvent.codigoPedido());
        entity.setCustomerId(orderCreatedEvent.codigoCliente());
        entity.setItems(getOrderItems(orderCreatedEvent));
        entity.setTotal(getTotal(orderCreatedEvent));

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );
        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);
        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private BigDecimal getTotal(OrderCreatedEvent orderCreatedEvent) {
        return orderCreatedEvent
                .itens()
                .stream()
                .map(item -> item.preco().multiply(BigDecimal.valueOf(item.quantidade())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent orderCreatedEvent) {
        return orderCreatedEvent.itens().stream().map(
                item -> new OrderItem(
                        item.produto(), item.quantidade(), item.preco()
                )).toList();
    }

}
