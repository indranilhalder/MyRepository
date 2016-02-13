package com.tisl.mpl.juspay.request;

public class GetCardRequest
{

	private String cardToken;

	public String getCardToken()
	{
		return cardToken;
	}

	public void setCardToken(final String cardToken)
	{
		this.cardToken = cardToken;
	}

	public GetCardRequest withCardToken(final String cardToken)
	{
		this.cardToken = cardToken;
		return this;
	}
}
