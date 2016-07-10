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
@XmlRootElement(name = "lineItems")
@XmlType(propOrder =
{ "lineItemId", "returnReasonCode", "cancelReasonCode" })
public class TicketlineItemsXMLData
{
	private String lineItemId;
	private String returnReasonCode;
	private String cancelReasonCode;

	/**
	 * @return the lineItemId
	 */
	@XmlElement(name = "lineItemId")
	public String getLineItemId()
	{
		return lineItemId;
	}

	/**
	 * @param lineItemId
	 *           the lineItemId to set
	 */
	public void setLineItemId(final String lineItemId)
	{
		this.lineItemId = lineItemId;
	}

	/**
	 * @return the returnReasonCode
	 */
	@XmlElement(name = "ReturnReasonCode")
	public String getReturnReasonCode()
	{
		return returnReasonCode;
	}

	/**
	 * @param returnReasonCode
	 *           the returnReasonCode to set
	 */
	public void setReturnReasonCode(final String returnReasonCode)
	{
		this.returnReasonCode = returnReasonCode;
	}

	/**
	 * @return the cancelReasonCode
	 */
	@XmlElement(name = "CancelReasonCode")
	public String getCancelReasonCode()
	{
		return cancelReasonCode;
	}

	/**
	 * @param cancelReasonCode
	 *           the cancelReasonCode to set
	 */
	public void setCancelReasonCode(final String cancelReasonCode)
	{
		this.cancelReasonCode = cancelReasonCode;
	}


}
