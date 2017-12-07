package com.tisl.mpl.juspay.response;

public class AddCardResponse
{

	private String cardToken;
	private String cardReference;
	private String cardFingerprint;

	/**
	 * @return the cardFingerprint
	 */
	public String getCardFingerprint()
	{
		return cardFingerprint;
	}

	/**
	 * @param cardFingerprint
	 *           the cardFingerprint to set
	 */
	public void setCardFingerprint(final String cardFingerprint)
	{
		this.cardFingerprint = cardFingerprint;
	}

	public String getCardToken()
	{
		return cardToken;
	}

	public void setCardToken(final String cardToken)
	{
		this.cardToken = cardToken;
	}

	public String getCardReference()
	{
		return cardReference;
	}

	public void setCardReference(final String cardReference)
	{
		this.cardReference = cardReference;
	}

	@Override
	public String toString()
	{
		return "AddCardResponse{" + "cardToken='" + cardToken + '\'' + ", cardReference='" + cardReference + '\''
				+ ", cardFingerprint='" + cardFingerprint + '\'' + '}';
	}
}
