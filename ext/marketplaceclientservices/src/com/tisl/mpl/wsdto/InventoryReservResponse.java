/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author 559379
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Item")
@XmlType(propOrder =
{ "USSID", "reservationStatus", "availableQuantity" })
public class InventoryReservResponse
{
	@XmlElement(name = "ussId")
	private String USSID;
	@XmlElement(name = "reservationStatus")
	private String reservationStatus;
	@XmlElement(name = "availableQuantity")
	private String availableQuantity;

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
	 * @return the reservationStatus
	 */
	public String getReservationStatus()
	{
		return reservationStatus;
	}

	/**
	 * @param reservationStatus
	 *           the reservationStatus to set
	 */
	public void setReservationStatus(final String reservationStatus)
	{
		this.reservationStatus = reservationStatus;
	}

	/**
	 * @return the availableQuantity
	 */
	public String getAvailableQuantity()
	{
		return availableQuantity;
	}

	/**
	 * @param availableQuantity
	 *           the availableQuantity to set
	 */
	public void setAvailableQuantity(final String availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}


}
