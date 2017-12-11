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
{ "lineItemId", "returnReasonCode", "cancelReasonCode", "timeSlotFrom", "timeSlotTo", "reverseSealLostflag",
		"subReturnReasonCode" })
public class TicketlineItemsXMLData
{
	private String lineItemId;
	private String returnReasonCode;
	private String cancelReasonCode;
	//R2.3 Changes Start
	private String timeSlotFrom;
	private String timeSlotTo;
	private String subReturnReasonCode;

	//R2.3 Changes END

	// TPR-4134
	private String reverseSealLostflag;

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

	/**
	 * @return the timeSlotFrom
	 */
	@XmlElement(name = "ReScheduleTimestampFrom")
	public String getTimeSlotFrom()
	{
		return timeSlotFrom;
	}

	/**
	 * @param timeSlotFrom
	 *           the timeSlotFrom to set
	 */
	public void setTimeSlotFrom(final String timeSlotFrom)
	{
		this.timeSlotFrom = timeSlotFrom;
	}

	/**
	 * @return the timeSlotTo
	 */
	@XmlElement(name = "ReScheduleTimestampTo")
	public String getTimeSlotTo()
	{
		return timeSlotTo;
	}

	/**
	 * @param timeSlotTo
	 *           the timeSlotTo to set
	 */
	public void setTimeSlotTo(final String timeSlotTo)
	{
		this.timeSlotTo = timeSlotTo;
	}

	/**
	 * @return the reverseSealForJwllry
	 */
	@XmlElement(name = "ReverseSealLostflag")
	public String getReverseSealLostflag()
	{
		return reverseSealLostflag;
	}

	/**
	 * @param reverseSealForJwllry
	 *           the reverseSealForJwllry to set
	 */
	public void setReverseSealLostflag(final String reverseSealLostflag)
	{
		this.reverseSealLostflag = reverseSealLostflag;
	}

	/**
	 * @return the subReturnReasonCode
	 */
	@XmlElement(name = "SubReturnReasonCode")
	public String getSubReturnReasonCode()
	{
		return subReturnReasonCode;
	}

	/**
	 * @param subReturnReasonCode
	 *           the subReturnReasonCode to set
	 */
	public void setSubReturnReasonCode(final String subReturnReasonCode)
	{
		this.subReturnReasonCode = subReturnReasonCode;
	}
}
