package org.example.orderservice.web;

import org.example.orderservice.entities.Order;
import org.example.orderservice.repositories.OrderRepository;
import org.example.orderservice.restclients.InventoryRestClient;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrdersRestControllers {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InventoryRestClient inventoryRestClient;


    @GetMapping("/orders")
    public List<Order> findAllOrders() {
       List<Order> allorders = orderRepository.findAll();
        allorders.forEach(order -> order.getProductItems().forEach(pi -> {
            pi.setProduct(inventoryRestClient.findProductById(pi.getProductId()));
        }));

       return allorders;
    }


    @GetMapping("/orders/{id}")
    public Order findOrderById(@PathVariable String id) {
        // Recherche de la commande par ID
        Order order = orderRepository.findById(id).get();

        // Enrichissement des ProductItems avec les détails des produits
        order.getProductItems().forEach(pi -> {
            pi.setProduct(inventoryRestClient.findProductById(pi.getProductId()));
        });

        return order;
    }



}