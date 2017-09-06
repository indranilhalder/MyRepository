/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invReserve", propOrder =
{ "USSID", "quantity", "parentUSSID", "isAFreebie", "storeId", "fulfillmentType", "deliveryMode", "transportMode",
		"ServiceableSlaves" })
public class InventoryReservRequest
{
	@XmlElement(name = "ussId")
	private String USSID;
	@XmlElement(name = "parentUssId")
	private String parentUSSID;
	@XmlElement(name = "isAFreebie")
	private String isAFreebie;
	@XmlElement(name = "storeId")
	private String storeId;
	@XmlElement(name = "fulfillmentType")
	private String fulfillmentType;
	@XmlElement(name = "deliveryMode")
	private String deliveryMode;
	@XmlElement(name = "quantity")
	private String quantity;
	@XmlElement(name = "ServiceableSlaves")
	private List<ServiceableSlavesDTO> ServiceableSlaves;
	@XmlElement(name = "transportMode")
	private String transportMode;

	/**
	 * @return the uSSID
	 */

	public String getUSSID()
	{
		return USSID;
	}

	/**
	 * @param uSSID
	 *           the uSSID to set
	 */
	public void setUSSID(final String uSSID)
	{
		USSID = uSSID;
	}

	/**
	 * @return the parentUSSID
	 */

	public String getParentUSSID()
	{
		return parentUSSID;
	}

	/**
	 * @param parentUSSID
	 *           the parentUSSID to set
	 */
	public void setParentUSSID(final String parentUSSID)
	{
		this.parentUSSID = parentUSSID;
	}

	/**
	 * @return the isAFreebie
	 */

	public String getIsAFreebie()
	{
		return isAFreebie;
	}

	/**
	 * @param isAFreebie
	 *           the isAFreebie to set
	 */
	public void setIsAFreebie(final String isAFreebie)
	{
		this.isAFreebie = isAFreebie;
	}

	/**
	 * @return the storeId
	 */

	public String getStoreId()
	{
		return storeId;
	}

	/**
	 * @param storeId
	 *           the storeId to set
	 */
	public void setStoreId(final String storeId)
	{
		this.storeId = storeId;
	}

	/**
	 * @return the fulfillmentType
	 */

	public String getFulfillmentType()
	{
		return fulfillmentType;
	}

	/**
	 * @param fulfillmentType
	 *           the fulfillmentType to set
	 */
	public void setFulfillmentType(final String fulfillmentType)
	{
		this.fulfillmentType = fulfillmentType;
	}

	/**
	 * @return the deliveryMode
	 */

	public String getDeliveryMode()
	{
		return deliveryMode;
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
	 * @return the quantity
	 */

	public String getQuantity()
	{
		return quantity;
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
	 * @return the serviceableSlaves
	 */
	public List<ServiceableSlavesDTO> getServiceableSlaves()
	{
		return ServiceableSlaves;
	}

	/**
	 * @param serviceableSlaves
	 *           the serviceableSlaves to set
	 */
	public void setServiceableSlaves(final List<ServiceableSlavesDTO> serviceableSlaves)
	{
		ServiceableSlaves = serviceableSlaves;
	}

	/**
	 * @return the transportMode
	 */
	public String getTransportMode()
	{
		return transportMode;
	}

	/**
	 * @param transportMode
	 *           the transportMode to set
	 */
	public void setTransportMode(final String transportMode)
	{
		this.transportMode = transportMode;
	}
}
