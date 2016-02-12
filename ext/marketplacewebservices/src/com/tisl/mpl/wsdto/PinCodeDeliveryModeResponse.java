/**
 *
 */
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
@XmlType(name = "pincodeprod", propOrder =
{ "ussid", "isPincodeServiceable", "isCOD", "isPrepaidEligible", "deliverymode" })
public class PinCodeDeliveryModeResponse
{
	@XmlElement(name = "ussid")
	private String ussid;
	@XmlElement(name = "isPincodeServiceable")
	private String isPincodeServiceable;
	@XmlElement(name = "isCOD")
	private String isCOD;
	@XmlElement(name = "isPrepaidEligible")
	private String isPrepaidEligible;
	@XmlElement(name = "deliverymode")
	private DeliveryModeResOMSWsDto deliverymode;

	/**
	 * @return the ussid
	 */
	public String getUssid()
	{
		return ussid;
	}

	/**
	 * @param ussid
	 *           the ussid to set
	 */
	public void setUssid(final String ussid)
	{
		this.ussid = ussid;
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
	 * @return the deliverymode
	 */
	public DeliveryModeResOMSWsDto getDeliverymode()
	{
		return deliverymode;
	}

	/**
	 * @param deliverymode
	 *           the deliverymode to set
	 */
	public void setDeliverymode(final DeliveryModeResOMSWsDto deliverymode)
	{
		this.deliverymode = deliverymode;
	}
}
