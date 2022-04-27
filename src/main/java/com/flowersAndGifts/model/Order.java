package com.flowersAndGifts.model;

import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private Long user_id;
    private List<ProductOrder> productOrder;
    private Address address;
    private boolean status = false;

    public Order() {
    }

    public Order(Long user_id, List<ProductOrder> productOrder, Address address, boolean status) {
        this.user_id = user_id;
        this.productOrder = productOrder;
        this.address = address;
        this.status = status;
    }

    public Order(Long id, Long user_id, List<ProductOrder> productOrder, Address address, boolean status) {
        this.id = id;
        this.user_id = user_id;
        this.productOrder = productOrder;
        this.address = address;
        this.status = status;
    }

    public Order(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public List<ProductOrder> getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(List<ProductOrder> productOrder) {
        this.productOrder = productOrder;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return status == order.status && Objects.equals(id, order.id) && Objects.equals(user_id, order.user_id) && Objects.equals(productOrder, order.productOrder) && Objects.equals(address, order.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, productOrder, address, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", productOrder=" + productOrder +
                ", address=" + address +
                ", isSent=" + status +
                '}';
    }
}