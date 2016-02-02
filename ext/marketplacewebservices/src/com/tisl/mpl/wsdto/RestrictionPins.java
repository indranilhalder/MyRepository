/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
//@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RestrictionPins")
public class RestrictionPins
{

	private List<PinData> listPin;

	/**
	 * @return the pin
	 */
	@XmlElement(name = "Pin")
	public List<PinData> getPin()
	{
		return listPin;
	}

	/**
	 * @param pin
	 *           the pin to set
	 */
	public void setPin(final List<PinData> pin)
	{
		this.listPin = pin;
	}

	/**
	 * @return the pin
	 */





}
