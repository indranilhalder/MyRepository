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
{ "WalletNumber", "ExternalWalletId", "WalletPin", "Status", "TrackData", "BarCode", "WalletProgramGroupName", "WalletHolderName",
		"Balance", "Notes", "Card", "Customer" })
public class Wallet
{

	@JsonProperty("WalletNumber")
	private String walletNumber;
	@JsonProperty("ExternalWalletId")
	private String externalWalletId;
	@JsonProperty("WalletPin")
	private String walletPin;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("TrackData")
	private Object trackData;
	@JsonProperty("BarCode")
	private Object barCode;
	@JsonProperty("WalletProgramGroupName")
	private String walletProgramGroupName;
	@JsonProperty("WalletHolderName")
	private String walletHolderName;
	@JsonProperty("Balance")
	private Integer balance;
	@JsonProperty("Notes")
	private String notes;
	@JsonProperty("Card")
	private Object card;
	@JsonProperty("Customer")
	private Object customer;
	@JsonIgnore
	private final Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("WalletNumber")
	public String getWalletNumber()
	{
		return walletNumber;
	}

	@JsonProperty("WalletNumber")
	public void setWalletNumber(final String walletNumber)
	{
		this.walletNumber = walletNumber;
	}

	@JsonProperty("ExternalWalletId")
	public String getExternalWalletId()
	{
		return externalWalletId;
	}

	@JsonProperty("ExternalWalletId")
	public void setExternalWalletId(final String externalWalletId)
	{
		this.externalWalletId = externalWalletId;
	}

	@JsonProperty("WalletPin")
	public String getWalletPin()
	{
		return walletPin;
	}

	@JsonProperty("WalletPin")
	public void setWalletPin(final String walletPin)
	{
		this.walletPin = walletPin;
	}

	@JsonProperty("Status")
	public String getStatus()
	{
		return status;
	}

	@JsonProperty("Status")
	public void setStatus(final String status)
	{
		this.status = status;
	}

	@JsonProperty("TrackData")
	public Object getTrackData()
	{
		return trackData;
	}

	@JsonProperty("TrackData")
	public void setTrackData(final Object trackData)
	{
		this.trackData = trackData;
	}

	@JsonProperty("BarCode")
	public Object getBarCode()
	{
		return barCode;
	}

	@JsonProperty("BarCode")
	public void setBarCode(final Object barCode)
	{
		this.barCode = barCode;
	}

	@JsonProperty("WalletProgramGroupName")
	public String getWalletProgramGroupName()
	{
		return walletProgramGroupName;
	}

	@JsonProperty("WalletProgramGroupName")
	public void setWalletProgramGroupName(final String walletProgramGroupName)
	{
		this.walletProgramGroupName = walletProgramGroupName;
	}

	@JsonProperty("WalletHolderName")
	public String getWalletHolderName()
	{
		return walletHolderName;
	}

	@JsonProperty("WalletHolderName")
	public void setWalletHolderName(final String walletHolderName)
	{
		this.walletHolderName = walletHolderName;
	}

	@JsonProperty("Balance")
	public Integer getBalance()
	{
		return balance;
	}

	@JsonProperty("Balance")
	public void setBalance(final Integer balance)
	{
		this.balance = balance;
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

	@JsonProperty("Card")
	public Object getCard()
	{
		return card;
	}

	@JsonProperty("Card")
	public void setCard(final Object card)
	{
		this.card = card;
	}

	@JsonProperty("Customer")
	public Object getCustomer()
	{
		return customer;
	}

	@JsonProperty("Customer")
	public void setCustomer(final Object customer)
	{
		this.customer = customer;
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