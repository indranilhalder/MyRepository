package com.tisl.mpl.juspay.request;

public class DeleteCardRequest
{

	private String cardToken;



	/**
	 * @return the cardToken
	 */
	public String getCardToken()
	{
		return cardToken;
	}

	public void setCardToken(final String cardToken)
	{
		this.cardToken = cardToken;
	}

	public DeleteCardRequest withCardToken(final String cardToken)
	{
		this.cardToken = cardToken;
		return this;
	}

}