package com.tisl.mpl.juspay.response;



import java.util.List;

import com.tisl.mpl.juspay.Refund;
import com.tisl.mpl.juspay.model.Promotion;


public class GetOrderStatusResponse
{

	private String merchantId;
	private String orderId;
	private Double amount;
	private String status;
	private Long statusId;
	private String txnId;
	private Long gatewayId;
	private String bankErrorCode;
	private String bankErrorMessage;

	/* Refund information */
	private List<Refund> refunds;
	private Double amountRefunded;
	private Boolean refunded;

	/* Promotion information */
	private Promotion promotion;

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

	private PaymentGatewayResponse paymentGatewayResponse;

	private CardResponse cardResponse;

	private RiskResponse riskResponse;

	//Extra fields to identify Payment method
	private String paymentMethodType;
	private String paymentMethod;


	public String getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(final String merchantId)
	{
		this.merchantId = merchantId;
	}

	public String getOrderId()
	{
		return orderId;
	}

	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}

	public Double getAmount()
	{
		return amount;
	}

	public void setAmount(final Double amount)
	{
		this.amount = amount;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public Long getStatusId()
	{
		return statusId;
	}

	public void setStatusId(final Long statusId)
	{
		this.statusId = statusId;
	}

	public String getTxnId()
	{
		return txnId;
	}

	public void setTxnId(final String txnId)
	{
		this.txnId = txnId;
	}

	public Long getGatewayId()
	{
		return gatewayId;
	}

	public void setGatewayId(final Long gatewayId)
	{
		this.gatewayId = gatewayId;
	}

	public String getBankErrorCode()
	{
		return bankErrorCode;
	}

	public void setBankErrorCode(final String bankErrorCode)
	{
		this.bankErrorCode = bankErrorCode;
	}

	public String getBankErrorMessage()
	{
		return bankErrorMessage;
	}

	public void setBankErrorMessage(final String bankErrorMessage)
	{
		this.bankErrorMessage = bankErrorMessage;
	}

	public PaymentGatewayResponse getPaymentGatewayResponse()
	{
		return paymentGatewayResponse;
	}

	public void setPaymentGatewayResponse(final PaymentGatewayResponse paymentGatewayResponse)
	{
		this.paymentGatewayResponse = paymentGatewayResponse;
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

	public Double getAmountRefunded()
	{
		return amountRefunded;
	}

	public void setAmountRefunded(final Double amountRefunded)
	{
		this.amountRefunded = amountRefunded;
	}

	public Boolean getRefunded()
	{
		return refunded;
	}

	public void setRefunded(final Boolean refunded)
	{
		this.refunded = refunded;
	}

	public List<Refund> getRefunds()
	{
		return refunds;
	}

	public void setRefunds(final List<Refund> refunds)
	{
		this.refunds = refunds;
	}

	public Promotion getPromotion()
	{
		return promotion;
	}

	public void setPromotion(final Promotion promotion)
	{
		this.promotion = promotion;
	}



	/**
	 * @return the cardResponse
	 */
	public CardResponse getCardResponse()
	{
		return cardResponse;
	}

	/**
	 * @param cardResponse
	 *           the cardResponse to set
	 */
	public void setCardResponse(final CardResponse cardResponse)
	{
		this.cardResponse = cardResponse;
	}



	/**
	 * @return the riskResponse
	 */
	public RiskResponse getRiskResponse()
	{
		return riskResponse;
	}

	/**
	 * @param riskResponse
	 *           the riskResponse to set
	 */
	public void setRiskResponse(final RiskResponse riskResponse)
	{
		this.riskResponse = riskResponse;
	}

	@Override
	public String toString()
	{
		return "GetOrderStatusResponse{" + "merchantId='" + merchantId + '\'' + ", orderId='" + orderId + '\'' + ", amount="
				+ amount + ", status='" + status + '\'' + ", statusId=" + statusId + ", txnId='" + txnId + '\'' + ", gatewayId="
				+ gatewayId + ", bankErrorCode='" + bankErrorCode + '\'' + ", bankErrorMessage='" + bankErrorMessage + '\''
				+ ", refunds=" + refunds + ", amountRefunded=" + amountRefunded + ", refunded=" + refunded + ", promotion="
				+ promotion + ", udf1='" + udf1 + '\'' + ", udf2='" + udf2 + '\'' + ", udf3='" + udf3 + '\'' + ", udf4='" + udf4
				+ '\'' + ", udf5='" + udf5 + '\'' + ", udf6='" + udf6 + '\'' + ", udf7='" + udf7 + '\'' + ", udf8='" + udf8 + '\''
				+ ", udf9='" + udf9 + '\'' + ", udf10='" + udf10 + '\'' + ", paymentGatewayResponse=" + paymentGatewayResponse + '\''
				+ ", cardResponse=" + cardResponse + '\'' + ", riskResponse=" + riskResponse + '}';
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
}
