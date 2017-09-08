/**
 *
 */
package com.tisl.mpl.pojo.request;

/**
 * @author TUL
 *
 */

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "Amount", "InvoiceNumber", "BillAmount", "Notes" })
public class QCRedeemRequest
{

	@JsonProperty("Amount")
	private String amount;
	@JsonProperty("InvoiceNumber")
	private String invoiceNumber;
	@JsonProperty("BillAmount")
	private String billAmount;
	@JsonProperty("Notes")
	private String notes;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	@JsonProperty("BillAmount")
	public String getBillAmount()
	{
		return billAmount;
	}

	@JsonProperty("BillAmount")
	public void setBillAmount(final String billAmount)
	{
		this.billAmount = billAmount;
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties()
	{
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(final String name, final Object value)
	{
		this.additionalProperties.put(name, value);
	}

}