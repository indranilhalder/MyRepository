package com.tisl.mpl.juspay.request;

public class GetOrderStatusRequest {

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public GetOrderStatusRequest withOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }
}
