/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author Techouts
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Item")
@XmlType(propOrder =
{ "ussId", "ATS" })
public class StoreLocatorResponseItem
{
	@XmlElement(name = "ussId")
	private String ussId;

	@XmlElement(name = "ATS")
	private List<StoreLocatorAtsResponse> ATS;

	/**
	 * @return the ussId
	 */
	public String getUssId()
	{
		return ussId;
	}

	/**
	 * @param ussId
	 *           the ussId to set
	 */
	public void setUssId(final String ussId)
	{
		this.ussId = ussId;
	}

	/**
	 * @return the aTS
	 */
	public List<StoreLocatorAtsResponse> getATS()
	{
		return ATS;
	}

	/**
	 * @param aTS
	 *           the aTS to set
	 */
	public void setATS(final List<StoreLocatorAtsResponse> aTS)
	{
		ATS = aTS;
	}
}
