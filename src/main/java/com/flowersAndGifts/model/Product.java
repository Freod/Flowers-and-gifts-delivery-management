package com.flowersAndGifts.model;

import java.util.Objects;

public class Product {
    private Long id = null;
    private String name = null;
    private Double price = null;
    private String image = null;
    private boolean active;

    public Product() {
    }

    public Product(Long id, String name, Double price, String image, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.active = active;
    }

    public Product(String name, Double price, String image, boolean active) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return active == product.active && Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(image, product.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, image, active);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", active=" + active +
                '}';
    }
}
