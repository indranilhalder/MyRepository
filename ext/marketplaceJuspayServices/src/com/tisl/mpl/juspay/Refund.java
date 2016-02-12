package com.tisl.mpl.juspay;

import java.util.Date;


public class Refund
{

	private String id;
	private String reference;
	private Double amount;
	private Date created;
	private String uniqueRequestId;
	private String status;




	public String getStatus()
	{
		return status;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the uniqueRequestId
	 */
	public String getUniqueRequestId()
	{
		return uniqueRequestId;
	}

	/**
	 * @param uniqueRequestId
	 *           the uniqueRequestId to set
	 */
	public void setUniqueRequestId(final String uniqueRequestId)
	{
		this.uniqueRequestId = uniqueRequestId;
	}

	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(final String reference)
	{
		this.reference = reference;
	}

	public Double getAmount()
	{
		return amount;
	}

	public void setAmount(final Double amount)
	{
		this.amount = amount;
	}

	public Date getCreated()
	{
		return created;
	}

	public void setCreated(final Date created)
	{
		this.created = created;
	}

	@Override
	public String toString()
	{
		return "Refund{" + "id='" + id + '\'' + ", reference='" + reference + '\'' + ", amount=" + amount + ", created=" + created
				+ '}';
	}
}
