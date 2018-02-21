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
public class QCCreditRequest
{
	@JsonProperty("Amount")
	private String amount;
	
	@JsonProperty("InvoiceNumber")
	private String invoiceNumber;
	
	@JsonProperty("Notes")
	private String notes;

	@JsonProperty("Amount")
	public String getAmount()
	{
		return amount;
	}

	@JsonProperty("Amount")
	public void setAmount(final String amount)
	{
		this.amount = amount;
	}

	@JsonProperty("InvoiceNumber")
	public String getInvoiceNumber()
	{
		return invoiceNumber;
	}

	@JsonProperty("InvoiceNumber")
	public void setInvoiceNumber(final String invoiceNumber)
	{
		this.invoiceNumber = invoiceNumber;
	}

	@JsonProperty("Notes")
	public String getNotes()
	{
		return notes;
	}

	@JsonProperty("Notes")
	public void setNotes(final String notes)
	{
		this.notes = notes;
	}
}
