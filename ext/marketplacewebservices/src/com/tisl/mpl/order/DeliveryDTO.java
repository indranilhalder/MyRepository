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
@XmlRootElement(name = "delivery")
public class DeliveryDTO
{
	private String deliveryMode;
	private String deliveryId;
	private String actualDeliveryDate;
	private String trackingID;
	private String trackingUrl;
	private DeliveryAddressDTO deliveryAddress;

	/**
	 * @return the deliveryMode
	 */
	@XmlElement(name = "deliveryMode")
	public String getDeliveryMode()
	{
		return deliveryMode;
	}

	/**
	 * @return the deliveryId
	 */
	@XmlElement(name = "deliveryId")
	public String getDeliveryId()
	{
		return deliveryId;
	}

	/**
	 * @return the actualDeliveryDate
	 */
	@XmlElement(name = "actualDeliveryDate")
	public String getActualDeliveryDate()
	{
		return actualDeliveryDate;
	}

	/**
	 * @return the trackingID
	 */
	@XmlElement(name = "trackingID")
	public String getTrackingID()
	{
		return trackingID;
	}

	/**
	 * @return the trackingUrl
	 */
	@XmlElement(name = "trackingUrl")
	public String getTrackingUrl()
	{
		return trackingUrl;
	}

	/**
	 * @return the deliveryAddress
	 */
	@XmlElement(name = "deliveryAddress")
	public DeliveryAddressDTO getDeliveryAddress()
	{
		return deliveryAddress;
	}

	/**
	 * @param deliveryMode
	 *           the deliveryMode to set
	 */
	public void setDeliveryMode(final String deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}

	/**
	 * @param deliveryId
	 *           the deliveryId to set
	 */
	public void setDeliveryId(final String deliveryId)
	{
		this.deliveryId = deliveryId;
	}

	/**
	 * @param actualDeliveryDate
	 *           the actualDeliveryDate to set
	 */
	public void setActualDeliveryDate(final String actualDeliveryDate)
	{
		this.actualDeliveryDate = actualDeliveryDate;
	}

	/**
	 * @param trackingID
	 *           the trackingID to set
	 */
	public void setTrackingID(final String trackingID)
	{
		this.trackingID = trackingID;
	}

	/**
	 * @param trackingUrl
	 *           the trackingUrl to set
	 */
	public void setTrackingUrl(final String trackingUrl)
	{
		this.trackingUrl = trackingUrl;
	}

	/**
	 * @param deliveryAddress
	 *           the deliveryAddress to set
	 */
	public void setDeliveryAddress(final DeliveryAddressDTO deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
	}



}
