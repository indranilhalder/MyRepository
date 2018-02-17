/**
 *
 */
package com.tisl.mpl.pojo.response;

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
{ "Type", "Amount", "NumOfCards" })
public class Bucket
{

	@JsonProperty("Type")
	private String type;
	@JsonProperty("Amount")
	private Double amount;
	@JsonProperty("NumOfCards")
	private Integer numOfCards;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Type")
	public String getType()
	{
		return type;
	}

	@JsonProperty("Type")
	public void setType(final String type)
	{
		this.type = type;
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

	@JsonProperty("NumOfCards")
	public Integer getNumOfCards()
	{
		return numOfCards;
	}

	@JsonProperty("NumOfCards")
	public void setNumOfCards(final Integer numOfCards)
	{
		this.numOfCards = numOfCards;
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