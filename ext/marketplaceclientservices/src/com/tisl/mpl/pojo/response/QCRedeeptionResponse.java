/**
 *
 */
package com.tisl.mpl.pojo.response;

/**
 * @author TUL
 *
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "WalletNumber", "InvoiceNumber", "DateAtServer", "BatchNumber", "Amount", "Balance", "BillAmount", "WalletPIN", "Cards",
		"ExcludedBucketsBalance", "ApiWebProperties", "Notes", "ApprovalCode", "ResponseCode", "ResponseMessage", "TransactionId",
		"TransactionType", "ErrorCode", "ErrorDescription" })
public class QCRedeeptionResponse
{

	@JsonProperty("WalletNumber")
	private String walletNumber;
	@JsonProperty("InvoiceNumber")
	private String invoiceNumber;
	@JsonProperty("DateAtServer")
	private String dateAtServer;
	@JsonProperty("BatchNumber")
	private Integer batchNumber;
	@JsonProperty("Amount")
	private Double amount;
	@JsonProperty("Balance")
	private Double balance;
	@JsonProperty("BillAmount")
	private Double billAmount;
	@JsonProperty("WalletPIN")
	private Object walletPIN;
	@JsonProperty("Cards")
	private List<QCCard> cards = null;
	@JsonProperty("ExcludedBucketsBalance")
	private Integer excludedBucketsBalance;
	@JsonProperty("ApiWebProperties")
	private ApiWebProperties apiWebProperties;
	@JsonProperty("Notes")
	private String notes;
	@JsonProperty("ApprovalCode")
	private String approvalCode;
	@JsonProperty("ResponseCode")
	private Integer responseCode;
	@JsonProperty("ResponseMessage")
	private String responseMessage;
	@JsonProperty("TransactionId")
	private Integer transactionId;
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("ErrorCode")
	private Object errorCode;
	@JsonProperty("ErrorDescription")
	private Object errorDescription;
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

	@JsonProperty("DateAtServer")
	public String getDateAtServer()
	{
		return dateAtServer;
	}

	@JsonProperty("DateAtServer")
	public void setDateAtServer(final String dateAtServer)
	{
		this.dateAtServer = dateAtServer;
	}

	@JsonProperty("BatchNumber")
	public Integer getBatchNumber()
	{
		return batchNumber;
	}

	@JsonProperty("BatchNumber")
	public void setBatchNumber(final Integer batchNumber)
	{
		this.batchNumber = batchNumber;
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

	@JsonProperty("Balance")
	public Double getBalance()
	{
		return balance;
	}

	@JsonProperty("Balance")
	public void setBalance(final Double balance)
	{
		this.balance = balance;
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

	@JsonProperty("WalletPIN")
	public Object getWalletPIN()
	{
		return walletPIN;
	}

	@JsonProperty("WalletPIN")
	public void setWalletPIN(final Object walletPIN)
	{
		this.walletPIN = walletPIN;
	}

	@JsonProperty("Cards")
	public List<QCCard> getCards()
	{
		return cards;
	}

	@JsonProperty("Cards")
	public void setCards(final List<QCCard> cards)
	{
		this.cards = cards;
	}

	@JsonProperty("ExcludedBucketsBalance")
	public Integer getExcludedBucketsBalance()
	{
		return excludedBucketsBalance;
	}

	@JsonProperty("ExcludedBucketsBalance")
	public void setExcludedBucketsBalance(final Integer excludedBucketsBalance)
	{
		this.excludedBucketsBalance = excludedBucketsBalance;
	}

	@JsonProperty("ApiWebProperties")
	public ApiWebProperties getApiWebProperties()
	{
		return apiWebProperties;
	}

	@JsonProperty("ApiWebProperties")
	public void setApiWebProperties(final ApiWebProperties apiWebProperties)
	{
		this.apiWebProperties = apiWebProperties;
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

	@JsonProperty("ApprovalCode")
	public String getApprovalCode()
	{
		return approvalCode;
	}

	@JsonProperty("ApprovalCode")
	public void setApprovalCode(final String approvalCode)
	{
		this.approvalCode = approvalCode;
	}

	@JsonProperty("ResponseCode")
	public Integer getResponseCode()
	{
		return responseCode;
	}

	@JsonProperty("ResponseCode")
	public void setResponseCode(final Integer responseCode)
	{
		this.responseCode = responseCode;
	}

	@JsonProperty("ResponseMessage")
	public String getResponseMessage()
	{
		return responseMessage;
	}

	@JsonProperty("ResponseMessage")
	public void setResponseMessage(final String responseMessage)
	{
		this.responseMessage = responseMessage;
	}

	@JsonProperty("TransactionId")
	public Integer getTransactionId()
	{
		return transactionId;
	}

	@JsonProperty("TransactionId")
	public void setTransactionId(final Integer transactionId)
	{
		this.transactionId = transactionId;
	}

	@JsonProperty("TransactionType")
	public String getTransactionType()
	{
		return transactionType;
	}

	@JsonProperty("TransactionType")
	public void setTransactionType(final String transactionType)
	{
		this.transactionType = transactionType;
	}

	@JsonProperty("ErrorCode")
	public Object getErrorCode()
	{
		return errorCode;
	}

	@JsonProperty("ErrorCode")
	public void setErrorCode(final Object errorCode)
	{
		this.errorCode = errorCode;
	}

	@JsonProperty("ErrorDescription")
	public Object getErrorDescription()
	{
		return errorDescription;
	}

	@JsonProperty("ErrorDescription")
	public void setErrorDescription(final Object errorDescription)
	{
		this.errorDescription = errorDescription;
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
