package com.tisl.mpl.juspay.response;

public class IciciAuthenticationResponse {

    private String cavv;
    private String xid;
    private Integer purchaseAmount;
    private String eci;
    private String mpiErrorCode;
    private String shoppingContext;
    private String status;
    private String currency;

    public String getCavv() {
        return cavv;
    }

    public void setCavv(String cavv) {
        this.cavv = cavv;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public Integer getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Integer purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getEci() {
        return eci;
    }

    public void setEci(String eci) {
        this.eci = eci;
    }

    public String getMpiErrorCode() {
        return mpiErrorCode;
    }

    public void setMpiErrorCode(String mpiErrorCode) {
        this.mpiErrorCode = mpiErrorCode;
    }

    public String getShoppingContext() {
        return shoppingContext;
    }

    public void setShoppingContext(String shoppingContext) {
        this.shoppingContext = shoppingContext;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
