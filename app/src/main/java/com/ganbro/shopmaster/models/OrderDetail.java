package com.ganbro.shopmaster.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OrderDetail implements Serializable {
    private int orderId;
    private String userEmail;
    private Date createTime;
    private OrderStatus status;
    private List<Product> products;

    public OrderDetail() {
    }

    public OrderDetail(int orderId, String userEmail, Date createTime, OrderStatus status, List<Product> products) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.createTime = createTime;
        this.status = status;
        this.products = products;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderId=" + orderId +
                ", userEmail='" + userEmail + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", products=" + products +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDetail that = (OrderDetail) o;

        if (orderId != that.orderId) return false;
        if (!userEmail.equals(that.userEmail)) return false;
        if (!createTime.equals(that.createTime)) return false;
        if (status != that.status) return false;
        return products.equals(that.products);
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + userEmail.hashCode();
        result = 31 * result + createTime.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + products.hashCode();
        return result;
    }
}
