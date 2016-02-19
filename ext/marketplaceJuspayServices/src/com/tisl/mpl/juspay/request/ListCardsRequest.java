package com.tisl.mpl.juspay.request;


public class ListCardsRequest
{

	private String customerId;



	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
	}

	public ListCardsRequest withCustomerId(final String customerId)
	{
		this.customerId = customerId;
		return this;
	}
}
