/**
 * 
 */
package com.tisl.mpl.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author PankajS
 *
 */
@XmlRootElement(name = "MerchantInfo")
@XmlType(propOrder =
{ "merchantType","merchantCode" ,"bucketId","shipmentCharge","scheduleDelCharge","expressDelCharge","paymentRefID","reversePaymentRefId","cardNumber","cardExpiryDate","productAmount"})
public class MerchantInfoXMlData
{
  private String merchantType;
  private String merchantCode;
  private String bucketId;
  private double shipmentCharge;
  private double scheduleDelCharge;
  private  double expressDelCharge;
  private String paymentRefID;
  private String reversePaymentRefId;
  private String cardNumber;
  private String cardExpiryDate;
  private double productAmount;

  
  
  
  
  
  /**
 * @return the merchantType
 */
  @XmlElement(name = "MerchantType")
public String getMerchantType()
{
	return merchantType;
}
/**
 * @param merchantType the merchantType to set
 */
public void setMerchantType(String merchantType)
{
	this.merchantType = merchantType;
}
/**
 * @return the merchantCode
 */

@XmlElement(name = "MerchantCode")
public String getMerchantCode()
{
	return merchantCode;
}
/**
 * @param merchantCode the merchantCode to set
 */
public void setMerchantCode(String merchantCode)
{
	this.merchantCode = merchantCode;
}
/**
 * @return the bucketId
 */
@XmlElement(name = "BucketId")
public String getBucketId()
{
	return bucketId;
}
/**
 * @param bucketId the bucketId to set
 */
public void setBucketId(String bucketId)
{
	this.bucketId = bucketId;
}
/**
 * @return the shipmentCharge
 */
@XmlElement(name = "ShipmentCharge")
public double getShipmentCharge()
{
	return shipmentCharge;
}
/**
 * @param shipmentCharge the shipmentCharge to set
 */
public void setShipmentCharge(double shipmentCharge)
{
	this.shipmentCharge = shipmentCharge;
}
/**
 * @return the scheduleDelCharge
 */
@XmlElement(name = "ScheduleDelCharge")
public double getScheduleDelCharge()
{
	return scheduleDelCharge;
}
/**
 * @param scheduleDelCharge the scheduleDelCharge to set
 */
public void setScheduleDelCharge(double scheduleDelCharge)
{
	this.scheduleDelCharge = scheduleDelCharge;
}
/**
 * @return the expressDelCharge
 */
@XmlElement(name = "ExpressDelCharge")
public double getExpressDelCharge()
{
	return expressDelCharge;
}
/**
 * @param expressDelCharge the expressDelCharge to set
 */
public void setExpressDelCharge(double expressDelCharge)
{
	this.expressDelCharge = expressDelCharge;
}
/**
 * @return the paymentRefID
 */
@XmlElement(name = "PaymentRefID")
public String getPaymentRefID()
{
	return paymentRefID;
}
/**
 * @param paymentRefID the paymentRefID to set
 */
public void setPaymentRefID(String paymentRefID)
{
	this.paymentRefID = paymentRefID;
}
/**
 * @return the reversePaymentRefId
 */
@XmlElement(name = "ReversePaymentRefId")
public String getReversePaymentRefId()
{
	return reversePaymentRefId;
}
/**
 * @param reversePaymentRefId the reversePaymentRefId to set
 */

public void setReversePaymentRefId(String reversePaymentRefId)
{
	this.reversePaymentRefId = reversePaymentRefId;
}
/**
 * @return the cardNumber
 */
@XmlElement(name = "CardNumber")
public String getCardNumber()
{
	return cardNumber;
}
/**
 * @param cardNumber the cardNumber to set
 */
public void setCardNumber(String cardNumber)
{
	this.cardNumber = cardNumber;
}
/**
 * @return the cardExpiryDate
 */
@XmlElement(name = "CardExpiryDate")
public String getCardExpiryDate()
{
	return cardExpiryDate;
}
/**
 * @param cardExpiryDate the cardExpiryDate to set
 */
public void setCardExpiryDate(String cardExpiryDate)
{
	this.cardExpiryDate = cardExpiryDate;
}
/**
 * @return the productAmount
 */

@XmlElement(name = "ProductAmount")
public double getProductAmount()
{
	return productAmount;
}
/**
 * @param productAmount the productAmount to set
 */
public void setProductAmount(double productAmount)
{
	this.productAmount = productAmount;
}


	
}
