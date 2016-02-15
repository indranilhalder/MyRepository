/**
 *
 */
package com.tisl.mpl.juspay.request;


public class NetbankingRequest
{
	private String orderId;
	private String merchantId;
	private String paymentMethodType;
	private String paymentMethod;
	private String redirectAfterPayment;
	private String format;

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
	 * @return the redirectAfterPayment
	 */
	public String getRedirectAfterPayment()
	{
		return redirectAfterPayment;
	}

	/**
	 * @param redirectAfterPayment
	 *           the redirectAfterPayment to set
	 */
	public void setRedirectAfterPayment(final String redirectAfterPayment)
	{
		this.redirectAfterPayment = redirectAfterPayment;
	}

	/**
	 * @return the format
	 */
	public String getFormat()
	{
		return format;
	}

	/**
	 * @param format
	 *           the format to set
	 */
	public void setFormat(final String format)
	{
		this.format = format;
	}


	@Override
	public String toString()
	{
		return "NetbankingRequest{" + "orderId=" + orderId + ", merchantId='" + merchantId + '\'' + ", paymentMethodType='"
				+ paymentMethodType + '\'' + ", paymentMethod='" + paymentMethod + '\'' + ", redirectAfterPayment='"
				+ redirectAfterPayment + '\'' + ", format='" + format + '\'' + '}';
	}

}
