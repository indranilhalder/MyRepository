/**
 * 
 */
package com.tisl.mpl.pojo.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Tech
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "Amount", "InvoiceNumber", "Notes" })
public class QCCustomerPromotionRequest
{
	@JsonProperty("Amount")
	private Double amount;
	
	@JsonProperty("InvoiceNumber")
	private String invoiceNumber;
	
	@JsonProperty("Notes")
	private String notes;

	@JsonProperty("Amount")
	public Double getAmount()
	{
		return amount;
	}

	@JsonProperty("Amount")
	public void setAmount(Double amount)
	{
		this.amount = amount;
	}

	@JsonProperty("InvoiceNumber")
	public String getInvoiceNumber()
	{
		return invoiceNumber;
	}

	@JsonProperty("InvoiceNumber")
	public void setInvoiceNumber(String invoiceNumber)
	{
		this.invoiceNumber = invoiceNumber;
	}

	@JsonProperty("Notes")
	public String getNotes()
	{
		return notes;
	}

	@JsonProperty("Notes")
	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "QCCustomerPromotionRequest [amount=" + amount + ", invoiceNumber=" + invoiceNumber + ", notes=" + notes + "]";
	}
}
