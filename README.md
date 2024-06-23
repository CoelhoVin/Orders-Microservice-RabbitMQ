## Projeto
Um microserviço que consome dados de uma fila RabbitMQ e armazena as informações em um banco de dados NoSQL para poder listar em uma REST API 

```
   {
       "codigoPedido": 1001,
       "codigoCliente":1,
       "itens": [
           {
               "produto": "lápis",
               "quantidade": 100,
               "preco": 1.10
           },
           {
               "produto": "caderno",
               "quantidade": 10,
               "preco": 1.00
           }
       ]
   }
```
## REST API

API REST permite consultar as seguintes informações:
* Valor total do Cliente
* Valor total do pedido
* Quantidade de Pedidos por Cliente
* Lista de pedidos realizados por cliente

![image](https://github.com/CoelhoVin/Orders-Microservice-RabbitMQ/assets/129123809/82ae81b6-da93-45fc-b69d-ee838d0e0f7e, "Response API")




## Tecnologias Utilizadas:
  * Java 21
  * Spring Boot
  * Spring Data MongoDB
  * RabbitMQ
  * Docker


Projeto criado com base nas instruções oferecidas pelo canal Build & Run
