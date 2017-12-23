/**
 *
 */
package com.tisl.mpl.order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.hybris.commons.dto.Dto;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "shipment")
public class ShipmentDTO implements Dto
{
	private String shipmentId;
	private DeliveryDTO delivery;
	private String location;
	private String olqsStatus;
	private String orderId;
	private String pickupInStore;
	private String isEDtoHD;
	private String shippingMethod;
	private String returnInScan;
	private String inScan;
	private String returnPickUp;
	private String awbSecondaryStatus;
	private String returnAwbSecondaryStatus;
	private String ssb;
	private String sdb;
	private String refundType;

	/**
	 * @return the shipmentId
	 */
	@XmlElement(name = "shipmentId")
	public String getShipmentId()
	{
		return shipmentId;
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
	 * @return the olqsStatus
	 */
	@XmlElement(name = "olqsStatus")
	public String getOlqsStatus()
	{
		return olqsStatus;
	}

	/**
	 * @return the orderId
	 */
	@XmlElement(name = "orderId")
	public String getOrderId()
	{
		return orderId;
	}

	/**
	 * @return the pickupInStore
	 */
	@XmlElement(name = "pickupInStore")
	public String getPickupInStore()
	{
		return pickupInStore;
	}

	/**
	 * @return the isEDtoHD
	 */
	@XmlElement(name = "isEDtoHD")
	public String getIsEDtoHD()
	{
		return isEDtoHD;
	}

	/**
	 * @return the shippingMethod
	 */
	@XmlElement(name = "shippingMethod")
	public String getShippingMethod()
	{
		return shippingMethod;
	}

	/**
	 * @return the returnInScan
	 */
	@XmlElement(name = "returnInScan")
	public String getReturnInScan()
	{
		return returnInScan;
	}

	/**
	 * @return the inScan
	 */
	@XmlElement(name = "inScan")
	public String getInScan()
	{
		return inScan;
	}

	/**
	 * @return the returnPickUp
	 */
	@XmlElement(name = "returnPickUp")
	public String getReturnPickUp()
	{
		return returnPickUp;
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
	 * @return the returnAwbSecondaryStatus
	 */
	@XmlElement(name = "returnAwbSecondaryStatus")
	public String getReturnAwbSecondaryStatus()
	{
		return returnAwbSecondaryStatus;
	}

	/**
	 * @return the ssb
	 */
	@XmlElement(name = "ssb")
	public String getSsb()
	{
		return ssb;
	}

	/**
	 * @return the sdb
	 */
	@XmlElement(name = "sdb")
	public String getSdb()
	{
		return sdb;
	}

	/**
	 * @return the delivery
	 */
	@XmlElement(name = "delivery")
	public DeliveryDTO getDelivery()
	{
		return delivery;
	}

	/**
	 * @param shipmentId
	 *           the shipmentId to set
	 */
	public void setShipmentId(final String shipmentId)
	{
		this.shipmentId = shipmentId;
	}

	/**
	 * @param delivery
	 *           the delivery to set
	 */
	public void setDelivery(final DeliveryDTO delivery)
	{
		this.delivery = delivery;
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
	 * @param olqsStatus
	 *           the olqsStatus to set
	 */
	public void setOlqsStatus(final String olqsStatus)
	{
		this.olqsStatus = olqsStatus;
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
	 * @param pickupInStore
	 *           the pickupInStore to set
	 */
	public void setPickupInStore(final String pickupInStore)
	{
		this.pickupInStore = pickupInStore;
	}

	/**
	 * @param isEDtoHD
	 *           the isEDtoHD to set
	 */
	public void setIsEDtoHD(final String isEDtoHD)
	{
		this.isEDtoHD = isEDtoHD;
	}

	/**
	 * @param shippingMethod
	 *           the shippingMethod to set
	 */
	public void setShippingMethod(final String shippingMethod)
	{
		this.shippingMethod = shippingMethod;
	}

	/**
	 * @param returnInScan
	 *           the returnInScan to set
	 */
	public void setReturnInScan(final String returnInScan)
	{
		this.returnInScan = returnInScan;
	}

	/**
	 * @param inScan
	 *           the inScan to set
	 */
	public void setInScan(final String inScan)
	{
		this.inScan = inScan;
	}

	/**
	 * @param returnPickUp
	 *           the returnPickUp to set
	 */
	public void setReturnPickUp(final String returnPickUp)
	{
		this.returnPickUp = returnPickUp;
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
	 * @param returnAwbSecondaryStatus
	 *           the returnAwbSecondaryStatus to set
	 */
	public void setReturnAwbSecondaryStatus(final String returnAwbSecondaryStatus)
	{
		this.returnAwbSecondaryStatus = returnAwbSecondaryStatus;
	}

	/**
	 * @param ssb
	 *           the ssb to set
	 */
	public void setSsb(final String ssb)
	{
		this.ssb = ssb;
	}

	/**
	 * @param sdb
	 *           the sdb to set
	 */
	public void setSdb(final String sdb)
	{
		this.sdb = sdb;
	}



	/**
	 * @return the refundType
	 */
	@XmlElement(name = "refundType")
	public String getRefundType()
	{
		return refundType;
	}

	/**
	 * @param refundType
	 *           the refundType to set
	 */
	public void setRefundType(final String refundType)
	{
		this.refundType = refundType;
	}

}
