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
 * @author 559379
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
{ "deliveryModeObj" })
public class DeliveryModeRequestOMS
{
	@XmlElement(name = "deliveryModeObj")
	private List<String> deliveryModeObj;

	/**
	 * @return the deliveryModeObj
	 */
	public List<String> getDeliveryModeObj()
	{
		return deliveryModeObj;
	}

	/**
	 * @param deliveryModeObj
	 *           the deliveryModeObj to set
	 */
	public void setDeliveryModeObj(final List<String> deliveryModeObj)
	{
		this.deliveryModeObj = deliveryModeObj;
	}




}
