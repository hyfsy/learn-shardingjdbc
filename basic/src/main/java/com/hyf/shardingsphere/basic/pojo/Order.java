package com.hyf.shardingsphere.basic.pojo;

/**
 * @author baB_hyf
 * @date 2022/08/06
 */
public class Order {
    private Long id;
    private Long orderId;
    private String orderName;
    private Long orderPrice;
    private Long addressId;
    private Long userId;

    public Order(Long id, Long orderId, String orderName, Long orderPrice, Long addressId, Long userId) {
        this.id = id;
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.addressId = addressId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Long getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Long orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                ", orderPrice=" + orderPrice +
                ", addressId=" + addressId +
                ", userId=" + userId +
                '}';
    }
}
