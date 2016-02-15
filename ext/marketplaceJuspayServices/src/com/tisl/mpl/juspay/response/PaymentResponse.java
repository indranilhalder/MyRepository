package com.tisl.mpl.juspay.response;

public class PaymentResponse
{

	private String status;
	private String txnId;
	private String merchantId;
	private String purchaseId;
	private String vbvUrl;



	/**
	 * @param vbvUrl
	 *           the vbvUrl to set
	 */
	public void setVbvUrl(final String vbvUrl)
	{
		this.vbvUrl = vbvUrl;
	}

	public String getStatus()
	{
		return status;
	}

	public String getTxnId()
	{
		return txnId;
	}

	public String getMerchantId()
	{
		return merchantId;
	}

	public String getPurchaseId()
	{
		return purchaseId;
	}

	public String getVbvUrl()
	{
		return vbvUrl;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public void setTxnId(final String txnId)
	{
		this.txnId = txnId;
	}

	public void setMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
	}

	public void setPurchaseId(final String purchaseId)
	{
		this.purchaseId = purchaseId;
	}


	public PaymentResponse withStatus(final String status)
	{
		this.status = status;
		return this;
	}

	public PaymentResponse withTxnId(final String txnId)
	{
		this.txnId = txnId;
		return this;
	}

	public PaymentResponse withMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
		return this;
	}

	public PaymentResponse withPurchaseId(final String purchaseId)
	{
		this.purchaseId = purchaseId;
		return this;
	}

	public PaymentResponse withVbvUrl(final String vbv_url)
	{
		this.vbvUrl = vbv_url;
		return this;
	}

	@Override
	public String toString()
	{
		return "PaymentResponse{" + "status='" + status + '\'' + ", txnId='" + txnId + '\'' + ", merchantId='" + merchantId + '\''
				+ ", purchaseId='" + purchaseId + '\'' + ", vbvUrl='" + vbvUrl + '\'' + '}';
	}
}
