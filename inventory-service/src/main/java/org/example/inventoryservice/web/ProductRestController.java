package org.example.inventoryservice.web;

import org.example.inventoryservice.entities.Product;
import org.example.inventoryservice.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
//j'autoruise d'envoyer des requette vers ce domains
//@CrossOrigin("*")
public class ProductRestController {


    private ProductRepository productRepository;

    public ProductRestController (ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
 //   @PreAuthorize("hasAuthority('ADMIN')")
    public List<Product> productList() {
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
   // @PreAuthorize("hasAuthority('USER')") //il faut utiliser @EnableMethodSecurity(prePostEnabled = true)
    public Product productById(@PathVariable String id) {
        return productRepository.findById(id).orElseThrow(()->new RuntimeException(String.format("Account %s not found",id)));
    }

    @GetMapping( "/auth")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }




}
