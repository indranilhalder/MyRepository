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
{ "Externalwalletid", "Customer", "Notes" })
public class QCCustomerRegisterRequest
{

	@JsonProperty("Externalwalletid")
	private String externalwalletid;
	@JsonProperty("Customer")
	private Customer customer;
	@JsonProperty("Notes")
	private String notes;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Externalwalletid")
	public String getExternalwalletid()
	{
		return externalwalletid;
	}

	@JsonProperty("Externalwalletid")
	public void setExternalwalletid(final String externalwalletid)
	{
		this.externalwalletid = externalwalletid;
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