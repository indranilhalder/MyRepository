/**
 *
 */
package com.tisl.mpl.wallet.refund;


/**
 * @author TCS
 *
 */
public class MRupeeRefundResponse
{
	private boolean success;

	private String merchantId;
	private String orderId;
	private Double amount;
	private String status;
	private Long statusId;
	private String txnId;
	private String mwrefNo;
	private String narration;
	private Double amountRefunded;
	private Boolean refunded;
	/* Refund information */

	/* Promotion information */
	//private Promotion promotion;

	//Extra fields to identify Payment method
	private String paymentMethodType;
	private String paymentMethod;
	private String request_ID;
	private String refNo;
	private String reason;

	/**
	 * @return the mwrefNo
	 */
	public String getMwrefNo()
	{
		return mwrefNo;
	}

	/**
	 * @param mwrefNo
	 *           the mwrefNo to set
	 */
	public void setMwrefNo(final String mwrefNo)
	{
		this.mwrefNo = mwrefNo;
	}

	/**
	 * @return the narration
	 */
	public String getNarration()
	{
		return narration;
	}

	/**
	 * @param narration
	 *           the narration to set
	 */
	public void setNarration(final String narration)
	{
		this.narration = narration;
	}

	/**
	 * @return the request_ID
	 */
	public String getRequest_ID()
	{
		return request_ID;
	}

	/**
	 * @param request_ID
	 *           the request_ID to set
	 */
	public void setRequest_ID(final String request_ID)
	{
		this.request_ID = request_ID;
	}

	/**
	 * @return the refNo
	 */
	public String getRefNo()
	{
		return refNo;
	}

	/**
	 * @param refNo
	 *           the refNo to set
	 */
	public void setRefNo(final String refNo)
	{
		this.refNo = refNo;
	}

	/**
	 * @return the reason
	 */
	public String getReason()
	{
		return reason;
	}

	/**
	 * @param reason
	 *           the reason to set
	 */
	public void setReason(final String reason)
	{
		this.reason = reason;
	}






	/**
	 * @return the merchantId
	 */
	public String getMerchantId()
	{
		return merchantId;
	}

	/**
	 * @param merchantId
	 *           the merchantId to set
	 */
	public void setMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId()
	{
		return orderId;
	}

	/**
	 * @param orderId
	 *           the orderId to set
	 */
	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount()
	{
		return amount;
	}

	/**
	 * @param amount
	 *           the amount to set
	 */
	public void setAmount(final Double amount)
	{
		this.amount = amount;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the statusId
	 */
	public Long getStatusId()
	{
		return statusId;
	}

	/**
	 * @param statusId
	 *           the statusId to set
	 */
	public void setStatusId(final Long statusId)
	{
		this.statusId = statusId;
	}

	/**
	 * @return the txnId
	 */
	public String getTxnId()
	{
		return txnId;
	}

	/**
	 * @param txnId
	 *           the txnId to set
	 */
	public void setTxnId(final String txnId)
	{
		this.txnId = txnId;
	}

	/**
	 * @return the amountRefunded
	 */
	public Double getAmountRefunded()
	{
		return amountRefunded;
	}

	/**
	 * @param amountRefunded
	 *           the amountRefunded to set
	 */
	public void setAmountRefunded(final Double amountRefunded)
	{
		this.amountRefunded = amountRefunded;
	}

	/**
	 * @return the refunded
	 */
	public Boolean getRefunded()
	{
		return refunded;
	}

	/**
	 * @param refunded
	 *           the refunded to set
	 */
	public void setRefunded(final Boolean refunded)
	{
		this.refunded = refunded;
	}

	/**
	 * @return the paymentMethodType
	 */
	public String getPaymentMethodType()
	{
		return paymentMethodType;
	}

	/**
	 * @param paymentMethodType
	 *           the paymentMethodType to set
	 */
	public void setPaymentMethodType(final String paymentMethodType)
	{
		this.paymentMethodType = paymentMethodType;
	}

	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod()
	{
		return paymentMethod;
	}

	/**
	 * @param paymentMethod
	 *           the paymentMethod to set
	 */
	public void setPaymentMethod(final String paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}



	/**
	 * @return the success
	 */
	public boolean isSuccess()
	{
		return success;
	}

	/**
	 * @param success
	 *           the success to set
	 */
	public void setSuccess(final boolean success)
	{
		this.success = success;
	}

}
