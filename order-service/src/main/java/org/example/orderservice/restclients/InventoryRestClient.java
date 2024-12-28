package org.example.orderservice.restclients;

import org.example.orderservice.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(url="http://localhost:8087",name="inventory-service")
public interface InventoryRestClient {

     @GetMapping("/api/products")
     List<Product> getAllProduct();

     @GetMapping("/api/products/{id}")
     Product findProductById(@PathVariable String id);






































































}
