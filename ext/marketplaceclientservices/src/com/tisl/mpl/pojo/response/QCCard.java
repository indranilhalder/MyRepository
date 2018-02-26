/**
 *
 */
package com.tisl.mpl.pojo.response;

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
{ "CardNumber", "Amount", "CardBalance", "CardProgramName", "CardStatus", "CardType", "Expiry", "BucketType", "Notes" })
public class QCCard
{

	@JsonProperty("CardNumber")
	private String cardNumber;
	@JsonProperty("Amount")
	private Double amount;
	@JsonProperty("CardBalance")
	private Double cardBalance;
	@JsonProperty("CardProgramName")
	private String cardProgramName;
	@JsonProperty("CardStatus")
	private String cardStatus;
	@JsonProperty("CardType")
	private String cardType;
	@JsonProperty("Expiry")
	private String expiry;
	@JsonProperty("BucketType")
	private String bucketType;
	@JsonProperty("Notes")
	private Object notes;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("CardNumber")
	public String getCardNumber()
	{
		return cardNumber;
	}

	@JsonProperty("CardNumber")
	public void setCardNumber(final String cardNumber)
	{
		this.cardNumber = cardNumber;
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

	@JsonProperty("CardBalance")
	public Double getCardBalance()
	{
		return cardBalance;
	}

	@JsonProperty("CardBalance")
	public void setCardBalance(final Double cardBalance)
	{
		this.cardBalance = cardBalance;
	}

	@JsonProperty("CardProgramName")
	public String getCardProgramName()
	{
		return cardProgramName;
	}

	@JsonProperty("CardProgramName")
	public void setCardProgramName(final String cardProgramName)
	{
		this.cardProgramName = cardProgramName;
	}

	@JsonProperty("CardStatus")
	public String getCardStatus()
	{
		return cardStatus;
	}

	@JsonProperty("CardStatus")
	public void setCardStatus(final String cardStatus)
	{
		this.cardStatus = cardStatus;
	}

	@JsonProperty("CardType")
	public String getCardType()
	{
		return cardType;
	}

	@JsonProperty("CardType")
	public void setCardType(final String cardType)
	{
		this.cardType = cardType;
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

	@JsonProperty("BucketType")
	public String getBucketType()
	{
		return bucketType;
	}

	@JsonProperty("BucketType")
	public void setBucketType(final String bucketType)
	{
		this.bucketType = bucketType;
	}

	@JsonProperty("Notes")
	public Object getNotes()
	{
		return notes;
	}

	@JsonProperty("Notes")
	public void setNotes(final Object notes)
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