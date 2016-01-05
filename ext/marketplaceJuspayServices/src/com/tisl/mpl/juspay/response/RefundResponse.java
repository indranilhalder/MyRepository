package com.tisl.mpl.juspay.response;

public class RefundResponse extends GetOrderStatusResponse {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
