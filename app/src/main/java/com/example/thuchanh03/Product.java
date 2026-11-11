package com.example.thuchanh03;

public class Product {
    private int id;
    private String name;
    private int price;
    private int quantity;
    private String category;
    private boolean active;
    private String description;

    public Product(int id, String name, int price, int quantity, String category, boolean active, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.active = active;
        this.description = description;
    }

    public Product(String name, int price, int quantity, String category, boolean active, String description) {
        this(0, name, price, quantity, category, active, description);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public boolean isActive() { return active; }
    public String getDescription() { return description; }
    public boolean isLowStock() { return quantity < 5; }
}
