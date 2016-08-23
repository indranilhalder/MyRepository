/**
 * 
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author TECHOUTS
 *
 */
@XmlRootElement(name="TicketUpdateResponse")
@XmlType(propOrder =
{ "ecomRequestId","ticketId","transactionId","rssLPName","rssOtherLPName","rssAWBNumber","rssCharge","rssDispathProofURL" })
public class TicketUpdateRequestXML
{
	
	private String ecomRequestId;
	private String ticketId ;
	private String transactionId;
	
	private String rssLPName;
	
	private String rssOtherLPName;
	private String rssAWBNumber;
	
	private String rssCharge ;
	
	private String rssDispathProofURL;

	/**
	 * @return the ecomRequestId
	 */
	public String getEcomRequestId()
	{
		return ecomRequestId;
	}

	/**
	 * @param ecomRequestId the ecomRequestId to set
	 */
	public void setEcomRequestId(String ecomRequestId)
	{
		this.ecomRequestId = ecomRequestId;
	}

	/**
	 * @return the ticketId
	 */
	public String getTicketId()
	{
		return ticketId;
	}

	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(String ticketId)
	{
		this.ticketId = ticketId;
	}

	/**
	 * @return the transactionId
	 */
	public String getTransactionId()
	{
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId)
	{
		this.transactionId = transactionId;
	}

	/**
	 * @return the rssLPName
	 */
	public String getRssLPName()
	{
		return rssLPName;
	}

	/**
	 * @param rssLPName the rssLPName to set
	 */
	public void setRssLPName(String rssLPName)
	{
		this.rssLPName = rssLPName;
	}

	/**
	 * @return the rssOtherLPName
	 */
	public String getRssOtherLPName()
	{
		return rssOtherLPName;
	}

	/**
	 * @param rssOtherLPName the rssOtherLPName to set
	 */
	public void setRssOtherLPName(String rssOtherLPName)
	{
		this.rssOtherLPName = rssOtherLPName;
	}

	/**
	 * @return the rssAWBNumber
	 */
	public String getRssAWBNumber()
	{
		return rssAWBNumber;
	}

	/**
	 * @param rssAWBNumber the rssAWBNumber to set
	 */
	public void setRssAWBNumber(String rssAWBNumber)
	{
		this.rssAWBNumber = rssAWBNumber;
	}

	/**
	 * @return the rssCharge
	 */
	public String getRssCharge()
	{
		return rssCharge;
	}

	/**
	 * @param rssCharge the rssCharge to set
	 */
	public void setRssCharge(String rssCharge)
	{
		this.rssCharge = rssCharge;
	}

	/**
	 * @return the rssDispathProofURL
	 */
	public String getRssDispathProofURL()
	{
		return rssDispathProofURL;
	}

	/**
	 * @param rssDispathProofURL the rssDispathProofURL to set
	 */
	public void setRssDispathProofURL(String rssDispathProofURL)
	{
		this.rssDispathProofURL = rssDispathProofURL;
	}

	
}
