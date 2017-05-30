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
@XmlRootElement(name = "RequestMaster")
@XmlAccessorType(XmlAccessType.FIELD)
public class OneTouchCancelReturnCrmRequestList
{
	//@XmlElementWrapper(name = "complexType")
	@XmlElement(name = "RequestTickets")
	private List<OneTouchCancelReturnCrmRequestDTO> oneTouchCancelReturnRequestDTOlist;

	/**
	 * @return the oneTouchCancelReturnRequestDTOlist
	 */
	public List<OneTouchCancelReturnCrmRequestDTO> getOneTouchCancelReturnRequestDTOlist()
	{
		return oneTouchCancelReturnRequestDTOlist;
	}

	/**
	 * @param oneTouchCancelReturnRequestDTOlist
	 *           the oneTouchCancelReturnRequestDTOlist to set
	 */
	public void setOneTouchCancelReturnRequestDTOlist(
			final List<OneTouchCancelReturnCrmRequestDTO> oneTouchCancelReturnRequestDTOlist)
	{
		this.oneTouchCancelReturnRequestDTOlist = oneTouchCancelReturnRequestDTOlist;
	}
}