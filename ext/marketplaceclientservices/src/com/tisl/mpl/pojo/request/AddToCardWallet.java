/**
 * 
 */
package com.tisl.mpl.pojo.request;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({ "CardNumber", "CardPin" })
public class AddToCardWallet {
	@JsonProperty("CardNumber")
	private String cardNumber;
	@JsonProperty("CardPin")
	private String cardPin;

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardPin() {
		return this.cardPin;
	}

	public void setCardPin(String cardPin) {
		this.cardPin = cardPin;
	}
}