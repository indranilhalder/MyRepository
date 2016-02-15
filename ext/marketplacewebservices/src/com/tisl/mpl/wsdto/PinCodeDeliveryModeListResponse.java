/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pinCodeDeliveryModeListResponse")
@XmlType(propOrder =
{ "pincode", "pincodeproperties" })
public class PinCodeDeliveryModeListResponse
{
	@XmlElement(name = "pincode")
	private String pincode;
	@XmlElementWrapper(name = "pincodeproperties")
	@XmlElement(name = "pincodeItems")
	List<PinCodeDeliveryModeRes> pincodeproperties;

	/**
	 * @return the pincodeproperties
	 */
	public List<PinCodeDeliveryModeRes> getPincodeproperties()
	{
		return pincodeproperties;
	}

	/**
	 * @param pincodeproperties
	 *           the pincodeproperties to set
	 */
	public void setPincodeproperties(final List<PinCodeDeliveryModeRes> pincodeproperties)
	{
		this.pincodeproperties = pincodeproperties;
	}

	/**
	 * @return the pincode
	 */
	public String getPincode()
	{
		return pincode;
	}

	/**
	 * @param pincode
	 *           the pincode to set
	 */
	public void setPincode(final String pincode)
	{
		this.pincode = pincode;
	}

	/**
	 * @return the pincodeproperties
	 */

}
