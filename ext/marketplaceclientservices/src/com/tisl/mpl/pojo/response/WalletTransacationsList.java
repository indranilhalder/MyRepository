/**
 * 
 */
package com.tisl.mpl.pojo.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Tech
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "TransactionsTotalPages", "WalletTransactions", "ApiWebProperties" ,"Notes", "ApprovalCode", "ResponseCode", "ResponseMessage",
	"TransactionId", "TransactionType", "ErrorCode", "ErrorDescription" })
public class WalletTransacationsList
{
	@JsonProperty("TransactionsTotalPages")
	private Integer transactionsTotalPages;
	
	@JsonProperty("WalletTransactions")
	private List<WalletTransactions> walletTransactions;
	
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

	@JsonProperty("TransactionsTotalPages")
	public Integer getTransactionsTotalPages()
	{
		return transactionsTotalPages;
	}

	@JsonProperty("TransactionsTotalPages")
	public void setTransactionsTotalPages(Integer transactionsTotalPages)
	{
		this.transactionsTotalPages = transactionsTotalPages;
	}

	@JsonProperty("WalletTransactions")
	public List<WalletTransactions> getWalletTransactions()
	{
		return walletTransactions;
	}

	@JsonProperty("WalletTransactions")
	public void setWalletTransactions(List<WalletTransactions> walletTransactions)
	{
		this.walletTransactions = walletTransactions;
	}
	
	@JsonProperty("ApiWebProperties")
	public ApiWebProperties getApiWebProperties()
	{
		return apiWebProperties;
	}

	@JsonProperty("ApiWebProperties")
	public void setApiWebProperties(ApiWebProperties apiWebProperties)
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
}
