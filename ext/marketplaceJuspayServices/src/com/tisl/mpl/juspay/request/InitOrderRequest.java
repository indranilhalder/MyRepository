package com.tisl.mpl.juspay.request;

public class InitOrderRequest
{

	private Double amount;
	private String orderId;
	private String customerId;
	private String customerEmail;
	private String returnUrl;

	/**
	 * UDF fields
	 */
	private String udf1;
	private String udf2;
	private String udf3;
	private String udf4;
	private String udf5;
	private String udf6;
	private String udf7;
	private String udf8;
	private String udf9;
	private String udf10;

	private String requestPayload;

	public InitOrderRequest withAmount(final Double amount)
	{
		this.amount = amount;
		return this;
	}

	public InitOrderRequest withOrderId(final String orderId)
	{
		this.orderId = orderId;
		return this;
	}

	public InitOrderRequest withCustomerId(final String customerId)
	{
		this.customerId = customerId;
		return this;
	}

	public InitOrderRequest withEmail(final String customerEmail)
	{
		this.customerEmail = customerEmail;
		return this;
	}

	public InitOrderRequest withReturnUrl(final String returnUrl)
	{
		this.returnUrl = returnUrl;
		return this;
	}

	public InitOrderRequest withUdf1(final String udf1)
	{
		this.udf1 = udf1;
		return this;
	}

	public InitOrderRequest withUdf2(final String udf2)
	{
		this.udf2 = udf2;
		return this;
	}

	public InitOrderRequest withUdf3(final String udf3)
	{
		this.udf3 = udf3;
		return this;
	}

	public InitOrderRequest withUdf4(final String udf4)
	{
		this.udf4 = udf4;
		return this;
	}

	public InitOrderRequest withUdf5(final String udf5)
	{
		this.udf5 = udf5;
		return this;
	}

	public InitOrderRequest withUdf6(final String udf6)
	{
		this.udf6 = udf6;
		return this;
	}

	public InitOrderRequest withUdf7(final String udf7)
	{
		this.udf7 = udf7;
		return this;
	}

	public InitOrderRequest withUdf8(final String udf8)
	{
		this.udf8 = udf8;
		return this;
	}

	public InitOrderRequest withUdf9(final String udf9)
	{
		this.udf9 = udf9;
		return this;
	}

	public InitOrderRequest withUdf10(final String udf10)
	{
		this.udf10 = udf10;
		return this;
	}


	public Double getAmount()
	{
		return amount;
	}

	public void setAmount(final Double amount)
	{
		this.amount = amount;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	public String getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
	}

	public String getCustomerEmail()
	{
		return customerEmail;
	}

	public void setCustomerEmail(final String customerEmail)
	{
		this.customerEmail = customerEmail;
	}

	public String getReturnUrl()
	{
		return returnUrl;
	}

	public void setReturnUrl(final String returnUrl)
	{
		this.returnUrl = returnUrl;
	}

	public String getUdf1()
	{
		return udf1;
	}

	public void setUdf1(final String udf1)
	{
		this.udf1 = udf1;
	}

	public String getUdf2()
	{
		return udf2;
	}

	public void setUdf2(final String udf2)
	{
		this.udf2 = udf2;
	}

	public String getUdf3()
	{
		return udf3;
	}

	public void setUdf3(final String udf3)
	{
		this.udf3 = udf3;
	}

	public String getUdf4()
	{
		return udf4;
	}

	public void setUdf4(final String udf4)
	{
		this.udf4 = udf4;
	}

	public String getUdf5()
	{
		return udf5;
	}

	public void setUdf5(final String udf5)
	{
		this.udf5 = udf5;
	}

	public String getUdf6()
	{
		return udf6;
	}

	public void setUdf6(final String udf6)
	{
		this.udf6 = udf6;
	}

	public String getUdf7()
	{
		return udf7;
	}

	public void setUdf7(final String udf7)
	{
		this.udf7 = udf7;
	}

	public String getUdf8()
	{
		return udf8;
	}

	public void setUdf8(final String udf8)
	{
		this.udf8 = udf8;
	}

	public String getUdf9()
	{
		return udf9;
	}

	public void setUdf9(final String udf9)
	{
		this.udf9 = udf9;
	}

	public String getUdf10()
	{
		return udf10;
	}

	public void setUdf10(final String udf10)
	{
		this.udf10 = udf10;
	}

	/**
	 * @return the requestPayload
	 */
	public String getRequestPayload()
	{
		return requestPayload;
	}

	/**
	 * @param requestPayload
	 *           the requestPayload to set
	 */
	public void setRequestPayload(final String requestPayload)
	{
		this.requestPayload = requestPayload;
	}

	@Override
	public String toString()
	{
		return "InitOrderRequest{" + "amount=" + amount + ", orderId='" + orderId + '\'' + ", customerId='" + customerId + '\''
				+ ", customerEmail='" + customerEmail + '\'' + ", returnUrl='" + returnUrl + '\'' + ", udf1='" + udf1 + '\''
				+ ", udf2='" + udf2 + '\'' + ", udf3='" + udf3 + '\'' + ", udf4='" + udf4 + '\'' + ", udf5='" + udf5 + '\''
				+ ", udf6='" + udf6 + '\'' + ", udf7='" + udf7 + '\'' + ", udf8='" + udf8 + '\'' + ", udf9='" + udf9 + '\''
				+ ", udf10='" + udf10 + '\'' + '}';
	}
}
