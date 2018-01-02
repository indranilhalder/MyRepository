/**
 *
 */
package com.tisl.mpl.order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "orderLines")
public class OrderLineDTO
{
	private String orderLineId;
	private String estimatedDelivery;
	private String awbNumber;
	private String shipmentStatus;
	private String invoiceNo;
	private String invoiceUrl;
	private String reverseLogisticProviderName;
	private String logisticProviderName;
	private String ISBNNo;
	private String IMEINo2;
	private String IMEINo1;
	private String serialNum;
	private String returnAWBNum;
	private String forwardSealNo;
	private String reverseSealNo;
	private String deliveryDate;
	private String shippingDate;
	private String qcReasonCode;
	private String awbSecondaryStatus;
	private String location;
	private String quantity;
	private String orderLineStatus;
	private String fulfillmentMode;
	private String communication;
	private ShipmentDTO shipment;

	/**
	 * @return the orderLineId
	 */
	@XmlElement(name = "orderLineId")
	public String getOrderLineId()
	{
		return orderLineId;
	}

	/**
	 * @return the estimatedDelivery
	 */
	@XmlElement(name = "estimatedDelivery")
	public String getEstimatedDelivery()
	{
		return estimatedDelivery;
	}

	/**
	 * @return the awbNumber
	 */
	@XmlElement(name = "awbNumber")
	public String getAwbNumber()
	{
		return awbNumber;
	}

	/**
	 * @return the shipmentStatus
	 */
	@XmlElement(name = "shipmentStatus")
	public String getShipmentStatus()
	{
		return shipmentStatus;
	}

	/**
	 * @return the invoiceNo
	 */
	@XmlElement(name = "invoiceNo")
	public String getInvoiceNo()
	{
		return invoiceNo;
	}


	/**
	 * @return the reverseLogisticProviderName
	 */
	@XmlElement(name = "reverseLogisticProviderName")
	public String getReverseLogisticProviderName()
	{
		return reverseLogisticProviderName;
	}

	/**
	 * @return the logisticProviderName
	 */
	@XmlElement(name = "logisticProviderName")
	public String getLogisticProviderName()
	{
		return logisticProviderName;
	}

	/**
	 * @return the iSBNNo
	 */
	@XmlElement(name = "ISBNNo")
	public String getISBNNo()
	{
		return ISBNNo;
	}

	/**
	 * @return the iMEINo2
	 */
	@XmlElement(name = "IMEINo2")
	public String getIMEINo2()
	{
		return IMEINo2;
	}

	/**
	 * @return the iMEINo1
	 */
	@XmlElement(name = "IMEINo1")
	public String getIMEINo1()
	{
		return IMEINo1;
	}

	/**
	 * @return the serialNum
	 */
	@XmlElement(name = "serialNum")
	public String getSerialNum()
	{
		return serialNum;
	}

	/**
	 * @return the returnAWBNum
	 */
	@XmlElement(name = "returnAWBNum")
	public String getReturnAWBNum()
	{
		return returnAWBNum;
	}

	/**
	 * @return the forwardSealNo
	 */
	@XmlElement(name = "forwardSealNo")
	public String getForwardSealNo()
	{
		return forwardSealNo;
	}

	/**
	 * @return the reverseSealNo
	 */
	@XmlElement(name = "reverseSealNo")
	public String getReverseSealNo()
	{
		return reverseSealNo;
	}

	/**
	 * @return the deliveryDate
	 */
	@XmlElement(name = "deliveryDate")
	public String getDeliveryDate()
	{
		return deliveryDate;
	}

	/**
	 * @return the shippingDate
	 */
	@XmlElement(name = "shippingDate")
	public String getShippingDate()
	{
		return shippingDate;
	}

	/**
	 * @return the qcReasonCode
	 */
	@XmlElement(name = "qcReasonCode")
	public String getQcReasonCode()
	{
		return qcReasonCode;
	}

	/**
	 * @return the awbSecondaryStatus
	 */
	@XmlElement(name = "awbSecondaryStatus")
	public String getAwbSecondaryStatus()
	{
		return awbSecondaryStatus;
	}

	/**
	 * @return the location
	 */
	@XmlElement(name = "location")
	public String getLocation()
	{
		return location;
	}

	/**
	 * @return the quantity
	 */
	@XmlElement(name = "quantity")
	public String getQuantity()
	{
		return quantity;
	}

	/**
	 * @return the orderLineStatus
	 */
	@XmlElement(name = "orderLineStatus")
	public String getOrderLineStatus()
	{
		return orderLineStatus;
	}

	/**
	 * @return the fulfillmentMode
	 */
	@XmlElement(name = "fulfillmentMode")
	public String getFulfillmentMode()
	{
		return fulfillmentMode;
	}

	/**
	 * @return the communication
	 */
	@XmlElement(name = "communication")
	public String getCommunication()
	{
		return communication;
	}

	/**
	 * @return the shipment
	 */
	@XmlElement(name = "shipment")
	public ShipmentDTO getShipment()
	{
		return shipment;
	}

	/**
	 * @param orderLineId
	 *           the orderLineId to set
	 */
	public void setOrderLineId(final String orderLineId)
	{
		this.orderLineId = orderLineId;
	}

	/**
	 * @param estimatedDelivery
	 *           the estimatedDelivery to set
	 */
	public void setEstimatedDelivery(final String estimatedDelivery)
	{
		this.estimatedDelivery = estimatedDelivery;
	}

	/**
	 * @param awbNumber
	 *           the awbNumber to set
	 */
	public void setAwbNumber(final String awbNumber)
	{
		this.awbNumber = awbNumber;
	}

	/**
	 * @param shipmentStatus
	 *           the shipmentStatus to set
	 */
	public void setShipmentStatus(final String shipmentStatus)
	{
		this.shipmentStatus = shipmentStatus;
	}

	/**
	 * @param invoiceNo
	 *           the invoiceNo to set
	 */
	public void setInvoiceNo(final String invoiceNo)
	{
		this.invoiceNo = invoiceNo;
	}

	/**
	 * @param reverseLogisticProviderName
	 *           the reverseLogisticProviderName to set
	 */
	public void setReverseLogisticProviderName(final String reverseLogisticProviderName)
	{
		this.reverseLogisticProviderName = reverseLogisticProviderName;
	}

	/**
	 * @param logisticProviderName
	 *           the logisticProviderName to set
	 */
	public void setLogisticProviderName(final String logisticProviderName)
	{
		this.logisticProviderName = logisticProviderName;
	}

	/**
	 * @param iSBNNo
	 *           the iSBNNo to set
	 */
	public void setISBNNo(final String iSBNNo)
	{
		ISBNNo = iSBNNo;
	}

	/**
	 * @param iMEINo2
	 *           the iMEINo2 to set
	 */
	public void setIMEINo2(final String iMEINo2)
	{
		IMEINo2 = iMEINo2;
	}

	/**
	 * @param iMEINo1
	 *           the iMEINo1 to set
	 */
	public void setIMEINo1(final String iMEINo1)
	{
		IMEINo1 = iMEINo1;
	}

	/**
	 * @param serialNum
	 *           the serialNum to set
	 */
	public void setSerialNum(final String serialNum)
	{
		this.serialNum = serialNum;
	}

	/**
	 * @param returnAWBNum
	 *           the returnAWBNum to set
	 */
	public void setReturnAWBNum(final String returnAWBNum)
	{
		this.returnAWBNum = returnAWBNum;
	}

	/**
	 * @param forwardSealNo
	 *           the forwardSealNo to set
	 */
	public void setForwardSealNo(final String forwardSealNo)
	{
		this.forwardSealNo = forwardSealNo;
	}

	/**
	 * @param reverseSealNo
	 *           the reverseSealNo to set
	 */
	public void setReverseSealNo(final String reverseSealNo)
	{
		this.reverseSealNo = reverseSealNo;
	}

	/**
	 * @param deliveryDate
	 *           the deliveryDate to set
	 */
	public void setDeliveryDate(final String deliveryDate)
	{
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @param shippingDate
	 *           the shippingDate to set
	 */
	public void setShippingDate(final String shippingDate)
	{
		this.shippingDate = shippingDate;
	}

	/**
	 * @param qcReasonCode
	 *           the qcReasonCode to set
	 */
	public void setQcReasonCode(final String qcReasonCode)
	{
		this.qcReasonCode = qcReasonCode;
	}

	/**
	 * @param awbSecondaryStatus
	 *           the awbSecondaryStatus to set
	 */
	public void setAwbSecondaryStatus(final String awbSecondaryStatus)
	{
		this.awbSecondaryStatus = awbSecondaryStatus;
	}

	/**
	 * @param location
	 *           the location to set
	 */
	public void setLocation(final String location)
	{
		this.location = location;
	}

	/**
	 * @param quantity
	 *           the quantity to set
	 */
	public void setQuantity(final String quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * @param orderLineStatus
	 *           the orderLineStatus to set
	 */
	public void setOrderLineStatus(final String orderLineStatus)
	{
		this.orderLineStatus = orderLineStatus;
	}

	/**
	 * @param fulfillmentMode
	 *           the fulfillmentMode to set
	 */
	public void setFulfillmentMode(final String fulfillmentMode)
	{
		this.fulfillmentMode = fulfillmentMode;
	}

	/**
	 * @param communication
	 *           the communication to set
	 */
	public void setCommunication(final String communication)
	{
		this.communication = communication;
	}

	/**
	 * @param shipment
	 *           the shipment to set
	 */
	public void setShipment(final ShipmentDTO shipment)
	{
		this.shipment = shipment;
	}

	/**
	 * @return the invoiceUrl
	 */
	@XmlElement(name = "invoiceUrl")
	public String getInvoiceUrl()
	{
		return invoiceUrl;
	}

	/**
	 * @param invoiceUrl
	 *           the invoiceUrl to set
	 */
	public void setInvoiceUrl(final String invoiceUrl)
	{
		this.invoiceUrl = invoiceUrl;
	}


}
