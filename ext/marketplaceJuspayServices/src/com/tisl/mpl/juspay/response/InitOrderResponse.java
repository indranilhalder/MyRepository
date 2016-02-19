package com.tisl.mpl.juspay.response;

public class InitOrderResponse
{

	private String status;
	private long statusId;
	private String orderId;
	private String merchantId;



	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @return the statusId
	 */
	public long getStatusId()
	{
		return statusId;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId()
	{
		return orderId;
	}

	/**
	 * @return the merchantId
	 */
	public String getMerchantId()
	{
		return merchantId;
	}

	//Setters
	public void setStatus(final String status)
	{
		this.status = status;
	}

	public void setStatusId(final long statusId)
	{
		this.statusId = statusId;
	}

	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	public void setMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
	}


	//Setters for method chaining
	public InitOrderResponse withStatus(final String status)
	{
		this.status = status;
		return this;
	}

	public InitOrderResponse withStatusId(final int statusId)
	{
		this.statusId = statusId;
		return this;
	}

	public InitOrderResponse withOrderId(final String orderId)
	{
		this.orderId = orderId;
		return this;
	}

	public InitOrderResponse withMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
		return this;
	}

	@Override
	public String toString()
	{
		final StringBuilder buffer = new StringBuilder(100);
		buffer.append("{ ");
		buffer.append("status: ");
		buffer.append(this.status);
		buffer.append(" , ");
		buffer.append("status_id: ");
		buffer.append(this.statusId);
		buffer.append(" , ");
		buffer.append("order_id: ");
		buffer.append(this.orderId);
		buffer.append(" , ");
		buffer.append("merchant_id: ");
		buffer.append(this.merchantId);
		buffer.append(" }");

		return buffer.toString();
	}

}
