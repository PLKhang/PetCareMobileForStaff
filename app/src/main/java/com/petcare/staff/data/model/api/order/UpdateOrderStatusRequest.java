package com.petcare.staff.data.model.api.order;

public class UpdateOrderStatusRequest {
    private String order_id;
    private OrderStatus status;

    public UpdateOrderStatusRequest(String order_id, OrderStatus status) {
        this.order_id = order_id;
        this.status = status;
    }
// getters & setters

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}