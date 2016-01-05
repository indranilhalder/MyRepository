package com.tisl.mpl.juspay.request;

public class RefundRequest
{

	private String orderId;
	private Double amount;
	private String uniqueRequestId;



	public String getUniqueRequestId()
	{
		return uniqueRequestId;
	}

	public void setUniqueRequestId(final String uniqueRequestId)
	{
		this.uniqueRequestId = uniqueRequestId;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	public Double getAmount()
	{
		return amount;
	}

	public void setAmount(final Double amount)
	{
		this.amount = amount;
	}

	public RefundRequest withOrderId(final String orderId)
	{
		this.orderId = orderId;
		return this;
	}

	public RefundRequest withAmount(final Double amount)
	{
		this.amount = amount;
		return this;
	}
}
