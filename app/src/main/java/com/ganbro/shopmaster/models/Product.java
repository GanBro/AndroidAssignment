package com.ganbro.shopmaster.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {
    private int id;
    private String name;
    private double price;
    private String imageUrl;
    private int quantity;
    private String category;
    private String description;
    private boolean isRecommended;
    private boolean isSelected;

    public Product(int id, String name, double price, String imageUrl, int quantity, String category, String description, boolean isRecommended) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.category = category;
        this.description = description;
        this.isRecommended = isRecommended;
        this.isSelected = false;
    }
}
