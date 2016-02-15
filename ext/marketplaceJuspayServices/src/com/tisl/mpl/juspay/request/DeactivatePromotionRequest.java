package com.tisl.mpl.juspay.request;

public class DeactivatePromotionRequest {
    private String promotionId;

    public DeactivatePromotionRequest withPromotionId(String promotionId) {
        this.promotionId = promotionId;
        return this;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
}
