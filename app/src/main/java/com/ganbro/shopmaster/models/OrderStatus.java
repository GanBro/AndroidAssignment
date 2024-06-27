package com.ganbro.shopmaster.models;

public enum OrderStatus {
    PENDING_RECEIPT, // 待收货
    PENDING_PAYMENT, // 待付款
    SHIPPED,         // 已发货
    COMPLETED,       // 已完成
    CANCELLED        // 已取消
}
