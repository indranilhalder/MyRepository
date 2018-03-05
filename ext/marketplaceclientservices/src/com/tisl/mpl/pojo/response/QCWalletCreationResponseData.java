/**
 *
 */
package com.tisl.mpl.pojo.response;

/**
 * @author TUL
 *
 */
public class QCWalletCreationResponseData
{
	private int transactionId;

	private String notes;

	private String approvalCode;

	private String errorDescription;

	private Wallet wallet;

	private int responseCode;

	private String transactionType;

	private String errorCode;

	private ApiWebProperties apiWebProperties;

	private String responseMessage;

	/**
	 * @return the transactionId
	 */
	public int getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @param transactionId
	 *           the transactionId to set
	 */
	public void setTransactionId(final int transactionId)
	{
		this.transactionId = transactionId;
	}

	/**
	 * @return the notes
	 */
	public String getNotes()
	{
		return notes;
	}

	/**
	 * @param notes
	 *           the notes to set
	 */
	public void setNotes(final String notes)
	{
		this.notes = notes;
	}

	/**
	 * @return the approvalCode
	 */
	public String getApprovalCode()
	{
		return approvalCode;
	}

	/**
	 * @param approvalCode
	 *           the approvalCode to set
	 */
	public void setApprovalCode(final String approvalCode)
	{
		this.approvalCode = approvalCode;
	}

	/**
	 * @return the errorDescription
	 */
	public String getErrorDescription()
	{
		return errorDescription;
	}

	/**
	 * @param errorDescription
	 *           the errorDescription to set
	 */
	public void setErrorDescription(final String errorDescription)
	{
		this.errorDescription = errorDescription;
	}

	/**
	 * @return the wallet
	 */
	public Wallet getWallet()
	{
		return wallet;
	}

	/**
	 * @param wallet
	 *           the wallet to set
	 */
	public void setWallet(final Wallet wallet)
	{
		this.wallet = wallet;
	}

	/**
	 * @return the responseCode
	 */
	public int getResponseCode()
	{
		return responseCode;
	}

	/**
	 * @param responseCode
	 *           the responseCode to set
	 */
	public void setResponseCode(final int responseCode)
	{
		this.responseCode = responseCode;
	}

	/**
	 * @return the transactionType
	 */
	public String getTransactionType()
	{
		return transactionType;
	}

	/**
	 * @param transactionType
	 *           the transactionType to set
	 */
	public void setTransactionType(final String transactionType)
	{
		this.transactionType = transactionType;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode()
	{
		return errorCode;
	}

	/**
	 * @param errorCode
	 *           the errorCode to set
	 */
	public void setErrorCode(final String errorCode)
	{
		this.errorCode = errorCode;
	}

	/**
	 * @return the apiWebProperties
	 */
	public ApiWebProperties getApiWebProperties()
	{
		return apiWebProperties;
	}

	/**
	 * @param apiWebProperties
	 *           the apiWebProperties to set
	 */
	public void setApiWebProperties(final ApiWebProperties apiWebProperties)
	{
		this.apiWebProperties = apiWebProperties;
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage()
	{
		return responseMessage;
	}

	/**
	 * @param responseMessage
	 *           the responseMessage to set
	 */
	public void setResponseMessage(final String responseMessage)
	{
		this.responseMessage = responseMessage;
	}



}
