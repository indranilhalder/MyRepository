/**
 *
 */
package com.tisl.mpl.wsdto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "ticketMaster")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketMaster
{
	@XmlElement(name = "ticket")
	public List<OneTouchCancelReturnDTO> oneTouchList;

	/**
	 * @return the oneTouchList
	 */

	public List<OneTouchCancelReturnDTO> getOneTouchList()
	{
		return oneTouchList;
	}

	/**
	 * @param oneTouchList
	 *           the oneTouchList to set
	 */
	public void setOneTouchList(final List<OneTouchCancelReturnDTO> oneTouchList)
	{
		this.oneTouchList = oneTouchList;
	}

}
