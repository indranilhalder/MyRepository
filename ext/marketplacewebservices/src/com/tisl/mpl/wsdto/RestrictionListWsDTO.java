/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RestrictionPins")
@XmlType(propOrder =
{ "restriction" })
public class RestrictionListWsDTO
{
	@XmlElementWrapper(name = "Pin")
	private List<RestrictionWsDTO> restriction;

	/**
	 * @return the restriction
	 */
	public List<RestrictionWsDTO> getRestriction()
	{
		return restriction;
	}

	/**
	 * @param restriction
	 *           the restriction to set
	 */
	public void setRestriction(final List<RestrictionWsDTO> restriction)
	{
		this.restriction = restriction;
	}



}
