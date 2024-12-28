package org.example.inventoryservice.entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder @ToString
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
}