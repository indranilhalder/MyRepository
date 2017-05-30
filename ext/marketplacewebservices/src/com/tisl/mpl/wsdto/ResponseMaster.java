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



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "responseMaster")
public class ResponseMaster
{
	@XmlElement(name = "ResponseTickets")
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
