package com.example.egor.Controllers.DTO;

import com.example.egor.Entities.AbstractProduct;
import com.example.egor.Entities.Client;


public class CartItemDTO {
    private Long id;
    private Long product_id;
    private String product_name;
    private Double price;
    private int quantity;
    private Long client_id;
    private String client_name;

    public CartItemDTO(Long id, Long product_id, String product_name, Double price, int quantity, Long client_id, String client_name) {
        this.id = id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.price = price;
        this.quantity = quantity;
        this.client_id = client_id;
        this.client_name = client_name;
    }

    public CartItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public Double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getClient_id() {
        return client_id;
    }

    public String getClient_name() {
        return client_name;
    }
}
