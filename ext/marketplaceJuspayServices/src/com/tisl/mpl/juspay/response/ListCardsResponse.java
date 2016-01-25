package com.tisl.mpl.juspay.response;

import java.util.List;


public class ListCardsResponse
{

	private String customerId;
	private String merchantId;
	private List<StoredCard> cards;

	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
	}

	public void setMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
	}

	public void setCards(final List<StoredCard> cards)
	{
		this.cards = cards;
	}



	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @return the merchantId
	 */
	public String getMerchantId()
	{
		return merchantId;
	}

	/**
	 * @return the cards
	 */
	public List<StoredCard> getCards()
	{
		return cards;
	}

	@Override
	public String toString()
	{
		return "ListCardsResponse{" + "customerId='" + customerId + '\'' + ", merchantId='" + merchantId + '\'' + ", cards="
				+ cards + '}';
	}
}
