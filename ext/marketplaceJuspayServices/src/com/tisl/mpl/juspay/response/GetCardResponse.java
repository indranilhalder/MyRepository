package com.tisl.mpl.juspay.response;

public class GetCardResponse {

    private String cardToken;
    private String merchantId;
    private StoredCard card;

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public StoredCard getCard() {
        return card;
    }

    public void setCard(StoredCard card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "GetCardResponse{" +
                "cardToken='" + cardToken + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", card=" + card +
                '}';
    }
}
