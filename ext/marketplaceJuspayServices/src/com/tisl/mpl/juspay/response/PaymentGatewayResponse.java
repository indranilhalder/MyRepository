package com.tisl.mpl.juspay.response;

public class PaymentGatewayResponse
{

	private String rootReferenceNumber;
	private String responseCode;
	private String responseMessage;
	private String txnId;
	private String externalGatewayTxnId;
	private String authIdCode;
	private String created;

	public String getRootReferenceNumber()
	{
		return rootReferenceNumber;
	}

	public void setRootReferenceNumber(final String rootReferenceNumber)
	{
		this.rootReferenceNumber = rootReferenceNumber;
	}

	public String getResponseCode()
	{
		return responseCode;
	}

	public void setResponseCode(final String responseCode)
	{
		this.responseCode = responseCode;
	}

	public String getResponseMessage()
	{
		return responseMessage;
	}

	public void setResponseMessage(final String responseMessage)
	{
		this.responseMessage = responseMessage;
	}

	public String getTxnId()
	{
		return txnId;
	}

	public void setTxnId(final String txnId)
	{
		this.txnId = txnId;
	}

	public String getExternalGatewayTxnId()
	{
		return externalGatewayTxnId;
	}

	public void setExternalGatewayTxnId(final String externalGatewayTxnId)
	{
		this.externalGatewayTxnId = externalGatewayTxnId;
	}

	public String getAuthIdCode()
	{
		return authIdCode;
	}

	public void setAuthIdCode(final String authIdCode)
	{
		this.authIdCode = authIdCode;
	}



	/**
	 * @return the created
	 */
	public String getCreated()
	{
		return created;
	}

	/**
	 * @param created
	 *           the created to set
	 */
	public void setCreated(final String created)
	{
		this.created = created;
	}

	@Override
	public String toString()
	{
		return "PaymentGatewayResponse{" + "cretaed='" + created + '\'' + ", rootReferenceNumber='" + rootReferenceNumber + '\''
				+ ", responseCode='" + responseCode + '\'' + ", responseMessage='" + responseMessage + '\'' + ", txnId='" + txnId
				+ '\'' + ", externalGatewayTxnId='" + externalGatewayTxnId + '\'' + ", authIdCode='" + authIdCode + '\'' + '}';
	}
}