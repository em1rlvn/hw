package com.example.demo7;

import java.time.LocalDate;

public class Sale {
    private int id;
    private String productName;
    private double price;
    private int quantity;
    private double total;
    private LocalDate saleDate;
    public Sale(int id, String productName, double price, int quantity, double total, LocalDate saleDate) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.saleDate = saleDate;
    }

    public double getTotal() { return total; }
    public LocalDate getSaleDate() { return saleDate; }
}