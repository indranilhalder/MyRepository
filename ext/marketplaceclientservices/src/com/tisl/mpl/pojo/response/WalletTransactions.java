/**
 * 
 */
package com.tisl.mpl.pojo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Tech
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "WalletNumber", "InvoiceNumber", "DateAtServer", "BatchNumber", "Amount", "Balance", "BillAmount", "MerchantOutletName",
		"TransactionPostDate", "TransactionStatus", "User", "MerchantName" ,"POSName" , "CustomerName" , "WalletPIN" , "ApiWebProperties" ,
		"Notes", "ApprovalCode", "ResponseCode", "ResponseMessage", "TransactionId", "TransactionType", "ErrorCode", "ErrorDescription"})
public class WalletTransactions
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
	private Integer amount;
	
	@JsonProperty("Balance")
	private Integer balance;
	
	@JsonProperty("BillAmount")
	private Integer billAmount;
	
	@JsonProperty("MerchantOutletName")
	private String merchantOutletName;
	
	@JsonProperty("TransactionPostDate")
	private String transactionPostDate;
	
	@JsonProperty("TransactionStatus")
	private String transactionStatus;
	
	@JsonProperty("User")
	private String user;
	
	@JsonProperty("MerchantName")
	private String merchantName;
	
	@JsonProperty("POSName")
	private String pOSName;
	
	@JsonProperty("CustomerName")
	private String customerName;
	
	@JsonProperty("WalletPIN")
	private String walletPIN;
	
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

	@JsonProperty("WalletNumber")
	public String getWalletNumber()
	{
		return walletNumber;
	}

	@JsonProperty("WalletNumber")
	public void setWalletNumber(String walletNumber)
	{
		this.walletNumber = walletNumber;
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

	@JsonProperty("DateAtServer")
	public String getDateAtServer()
	{
		return dateAtServer;
	}
	@JsonProperty("DateAtServer")
	public void setDateAtServer(String dateAtServer)
	{
		this.dateAtServer = dateAtServer;
	}

	@JsonProperty("BatchNumber")
	public Integer getBatchNumber()
	{
		return batchNumber;
	}

	@JsonProperty("BatchNumber")
	public void setBatchNumber(Integer batchNumber)
	{
		this.batchNumber = batchNumber;
	}

	@JsonProperty("Amount")
	public Integer getAmount()
	{
		return amount;
	}

	@JsonProperty("Amount")
	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}

	@JsonProperty("Balance")
	public Integer getBalance()
	{
		return balance;
	}

	@JsonProperty("Balance")
	public void setBalance(Integer balance)
	{
		this.balance = balance;
	}

	@JsonProperty("BillAmount")
	public Integer getBillAmount()
	{
		return billAmount;
	}

	@JsonProperty("BillAmount")
	public void setBillAmount(Integer billAmount)
	{
		this.billAmount = billAmount;
	}

	@JsonProperty("MerchantOutletName")
	public String getMerchantOutletName()
	{
		return merchantOutletName;
	}
	@JsonProperty("MerchantOutletName")
	public void setMerchantOutletName(String merchantOutletName)
	{
		this.merchantOutletName = merchantOutletName;
	}

	@JsonProperty("TransactionPostDate")
	public String getTransactionPostDate()
	{
		return transactionPostDate;
	}

	@JsonProperty("TransactionPostDate")
	public void setTransactionPostDate(String transactionPostDate)
	{
		this.transactionPostDate = transactionPostDate;
	}

	@JsonProperty("TransactionStatus")
	public String getTransactionStatus()
	{
		return transactionStatus;
	}

	@JsonProperty("TransactionStatus")
	public void setTransactionStatus(String transactionStatus)
	{
		this.transactionStatus = transactionStatus;
	}

	@JsonProperty("User")
	public String getUser()
	{
		return user;
	}

	@JsonProperty("User")
	public void setUser(String user)
	{
		this.user = user;
	}

	@JsonProperty("MerchantName")
	public String getMerchantName()
	{
		return merchantName;
	}

	@JsonProperty("MerchantName")
	public void setMerchantName(String merchantName)
	{
		this.merchantName = merchantName;
	}

	@JsonProperty("POSName")
	public String getpOSName()
	{
		return pOSName;
	}

	@JsonProperty("POSName")
	public void setpOSName(String pOSName)
	{
		this.pOSName = pOSName;
	}

	@JsonProperty("CustomerName")
	public String getCustomerName()
	{
		return customerName;
	}

	@JsonProperty("CustomerName")
	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	@JsonProperty("WalletPIN")
	public String getWalletPIN()
	{
		return walletPIN;
	}

	@JsonProperty("WalletPIN")
	public void setWalletPIN(String walletPIN)
	{
		this.walletPIN = walletPIN;
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
