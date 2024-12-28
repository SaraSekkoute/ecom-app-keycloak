package org.example.orderservice;

import org.example.orderservice.entities.Order;
import org.example.orderservice.entities.OrderState;
import org.example.orderservice.entities.ProductItem;
import org.example.orderservice.models.Product;
import org.example.orderservice.repositories.OrderRepository;
import org.example.orderservice.repositories.ProductItemRepository;
import org.example.orderservice.restclients.InventoryRestClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.Local;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(OrderRepository orderRepository,
                                        ProductItemRepository productItemRepository,
                                        InventoryRestClient inventoryRestClient) {

        return args -> {
            // List<Product> allProducts = inventoryRestClient.getAllProduct();
            List<String> productsIds=List.of("P01","P02","P03");
            // Chaque commande (Order) sera créée, puis pour chaque produit de allProducts, un ProductItem correspondant sera généré et enregistré dans la base de données.
            for(int i=0;i<5;i++) {
                Order order = Order.builder()
                        .id(UUID.randomUUID().toString())
                        .date(LocalDate.now())
                        .state(OrderState.PENDING)
                        .build();
                Order savedOrder=orderRepository.save(order);

                productsIds.forEach(p -> {
                    ProductItem productItem =ProductItem.builder()
                            .productId(p)
                            .quantity(new Random().nextInt(20))
                            .price(Math.random()*6000+100)
                            .order(savedOrder)
                            .build();
                    productItemRepository.save(productItem);

                });
            }

        };

    }

















































}
