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
{ "CardProgramGroupName", "Amount", "BillAmount", "InvoiceNumber", "ExternalCardNumber", "Customer", "Expiry", "Notes",
		"IdempotencyKey" })
public class PurchaseEGVRequest
{

	@JsonProperty("CardProgramGroupName")
	private String cardProgramGroupName;
	@JsonProperty("Amount")
	private Double amount;
	@JsonProperty("BillAmount")
	private Double billAmount;
	@JsonProperty("InvoiceNumber")
	private String invoiceNumber;
	@JsonProperty("ExternalCardNumber")
	private Object externalCardNumber;
	@JsonProperty("Customer")
	private Customer customer;
	@JsonProperty("Expiry")
	private String expiry;
	@JsonProperty("Notes")
	private String notes;
	@JsonProperty("IdempotencyKey")
	private String idempotencyKey;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("CardProgramGroupName")
	public String getCardProgramGroupName()
	{
		return cardProgramGroupName;
	}

	@JsonProperty("CardProgramGroupName")
	public void setCardProgramGroupName(final String cardProgramGroupName)
	{
		this.cardProgramGroupName = cardProgramGroupName;
	}

	@JsonProperty("Amount")
	public Double getAmount()
	{
		return amount;
	}

	@JsonProperty("Amount")
	public void setAmount(final Double amount)
	{
		this.amount = amount;
	}

	@JsonProperty("BillAmount")
	public Double getBillAmount()
	{
		return billAmount;
	}

	@JsonProperty("BillAmount")
	public void setBillAmount(final Double billAmount)
	{
		this.billAmount = billAmount;
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

	@JsonProperty("ExternalCardNumber")
	public Object getExternalCardNumber()
	{
		return externalCardNumber;
	}

	@JsonProperty("ExternalCardNumber")
	public void setExternalCardNumber(final Object externalCardNumber)
	{
		this.externalCardNumber = externalCardNumber;
	}

	@JsonProperty("Customer")
	public Customer getCustomer()
	{
		return customer;
	}

	@JsonProperty("Customer")
	public void setCustomer(final Customer customer)
	{
		this.customer = customer;
	}

	@JsonProperty("Expiry")
	public String getExpiry()
	{
		return expiry;
	}

	@JsonProperty("Expiry")
	public void setExpiry(final String expiry)
	{
		this.expiry = expiry;
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

	@JsonProperty("IdempotencyKey")
	public String getIdempotencyKey()
	{
		return idempotencyKey;
	}

	@JsonProperty("IdempotencyKey")
	public void setIdempotencyKey(final String idempotencyKey)
	{
		this.idempotencyKey = idempotencyKey;
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