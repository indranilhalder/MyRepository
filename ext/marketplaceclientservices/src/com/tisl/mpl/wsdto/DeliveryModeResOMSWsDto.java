package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
/*
 * @XmlType(propOrder = { "Type", "Inventory", "deliveryDate", "isPincodeServiceable", "isCOD", "isCODLimitFailed",
 * "isPrepaidEligible" })
 */
@XmlType(propOrder =
{ "Type", "Inventory", "DeliveryDate","isPincodeServiceable", "isCOD", "isCODLimitFailed", "isPrepaidEligible" })
public class DeliveryModeResOMSWsDto
{
	@XmlElement(name = "Type")
	private String Type;
	@XmlElement(name = "Inventory")
	private String Inventory;

	@XmlElement(name = "isPincodeServiceable")
	private String isPincodeServiceable;
	@XmlElement(name = "isCOD")
	private String isCOD;
	@XmlElement(name = "isCODLimitFailed")
	private String isCODLimitFailed;
	@XmlElement(name = "isPrepaidEligible")
	private String isPrepaidEligible;

	@XmlElement(name = "DeliveryDate")
	private String DeliveryDate;

	/**
	 * @return the type
	 */
	public String getType()
	{
		return Type;
	}

	/**
	 * @param type
	 *           the type to set
	 */
	public void setType(final String type)
	{
		Type = type;
	}

	/**
	 * @return the inventory
	 */
	public String getInventory()
	{
		return Inventory;
	}

	/**
	 * @param inventory
	 *           the inventory to set
	 */
	public void setInventory(final String inventory)
	{
		Inventory = inventory;
	}

	/**
	 * @return the isPincodeServiceable
	 */
	public String getIsPincodeServiceable()
	{
		return isPincodeServiceable;
	}

	/**
	 * @param isPincodeServiceable
	 *           the isPincodeServiceable to set
	 */
	public void setIsPincodeServiceable(final String isPincodeServiceable)
	{
		this.isPincodeServiceable = isPincodeServiceable;
	}

	/**
	 * @return the isCOD
	 */
	public String getIsCOD()
	{
		return isCOD;
	}

	/**
	 * @param isCOD
	 *           the isCOD to set
	 */
	public void setIsCOD(final String isCOD)
	{
		this.isCOD = isCOD;
	}

	/**
	 * @return the isCODLimitFailed
	 */
	public String getIsCODLimitFailed()
	{
		return isCODLimitFailed;
	}

	/**
	 * @param isCODLimitFailed
	 *           the isCODLimitFailed to set
	 */
	public void setIsCODLimitFailed(final String isCODLimitFailed)
	{
		this.isCODLimitFailed = isCODLimitFailed;
	}

	/**
	 * @return the isPrepaidEligible
	 */
	public String getIsPrepaidEligible()
	{
		return isPrepaidEligible;
	}

	/**
	 * @param isPrepaidEligible
	 *           the isPrepaidEligible to set
	 */
	public void setIsPrepaidEligible(final String isPrepaidEligible)
	{
		this.isPrepaidEligible = isPrepaidEligible;
	}
	
		/**
	 * @return the deliveryDate
	 */
	public String getDeliveryDate()
	{
		return DeliveryDate;
	}

	/**
	 * @param deliveryDate
	 *           the deliveryDate to set
	 */
	public void setDeliveryDate(final String deliveryDate)
	{
		DeliveryDate = deliveryDate;
	}

}
