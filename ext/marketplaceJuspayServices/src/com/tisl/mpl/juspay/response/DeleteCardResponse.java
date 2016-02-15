package com.tisl.mpl.juspay.response;

public class DeleteCardResponse
{

	private String cardToken;
	private boolean deleted;
	private String cardReference;




	/**
	 * @return the deleted
	 */
	public boolean isDeleted()
	{
		return deleted;
	}

	/**
	 * @return the cardToken
	 */
	public String getCardToken()
	{
		return cardToken;
	}

	/**
	 * @return the cardReference
	 */
	public String getCardReference()
	{
		return cardReference;
	}

	/**
	 * @param cardReference
	 *           the cardReference to set
	 */
	public void setCardReference(final String cardReference)
	{
		this.cardReference = cardReference;
	}

	public void setCardToken(final String cardToken)
	{
		this.cardToken = cardToken;
	}

	public void setDeleted(final boolean deleted)
	{
		this.deleted = deleted;
	}

	public DeleteCardResponse withCardToken(final String cardToken)
	{
		this.cardToken = cardToken;
		return this;
	}

	public DeleteCardResponse withDeleted(final boolean deleted)
	{
		this.deleted = deleted;
		return this;
	}

	public DeleteCardResponse withCardReference(final String cardReference)
	{
		this.cardReference = cardReference;
		return this;
	}

	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder(30);
		buffer.append("{ ");
		buffer.append("card_token: ");
		buffer.append(this.cardToken);
		buffer.append(" , ");
		buffer.append("deleted: ");
		buffer.append(this.deleted);
		buffer.append(" }");

		return buffer.toString();
	}
}
