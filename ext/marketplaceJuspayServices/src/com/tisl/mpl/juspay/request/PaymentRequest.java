package com.tisl.mpl.juspay.request;

public class PaymentRequest {

    private String merchantId;
    private String orderId;
    private String cardNumber;
    private String cardExpYear;
    private String cardExpMonth;
    private String cardSecurityCode;

    public String getMerchantId() {
        return merchantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpYear() {
        return cardExpYear;
    }

    public String getCardExpMonth() {
        return cardExpMonth;
    }

    public String getCardSecurityCode() {
        return cardSecurityCode;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardExpYear(String cardExpYear) {
        this.cardExpYear = cardExpYear;
    }

    public void setCardExpMonth(String cardExpMonth) {
        this.cardExpMonth = cardExpMonth;
    }

    public void setCardSecurityCode(String cardSecurityCode) {
        this.cardSecurityCode = cardSecurityCode;
    }


    //to chain the calls
    public PaymentRequest withMerchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public PaymentRequest withOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public PaymentRequest withCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public PaymentRequest withCardExpYear(String cardExpYear) {
        this.cardExpYear = cardExpYear;
        return this;
    }

    public PaymentRequest withCardExpMonth(String cardExpMonth) {
        this.cardExpMonth = cardExpMonth;
        return this;
    }

    public PaymentRequest withCardSecurityCode(String cardSecurityCode) {
        this.cardSecurityCode = cardSecurityCode;
        return this;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "merchantId='" + merchantId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardExpYear='" + cardExpYear + '\'' +
                ", cardExpMonth='" + cardExpMonth + '\'' +
                ", cardSecurityCode='" + cardSecurityCode + '\'' +
                '}';
    }
}
