/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "CrmToEcommDuplicateResp")
@XmlType(propOrder =
{ "ticketPresent", "crmTicketId" })
public class DuplicateCheckResponse
{
	private String ticketPresent;
	private String crmTicketId;

	/**
	 * @return the ticketPresent
	 */
	@XmlElement(name = "TicketPresent")
	public String getTicketPresent()
	{
		return ticketPresent;
	}

	/**
	 * @param ticketPresent
	 *           the ticketPresent to set
	 */
	public void setTicketPresent(final String ticketPresent)
	{
		this.ticketPresent = ticketPresent;
	}

	/**
	 * @return the crmTicketId
	 */
	@XmlElement(name = "CRMTicketId")
	public String getCrmTicketId()
	{
		return crmTicketId;
	}

	/**
	 * @param crmTicketId
	 *           the crmTicketId to set
	 */
	public void setCrmTicketId(final String crmTicketId)
	{
		this.crmTicketId = crmTicketId;
	}




}
