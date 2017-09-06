/**
 *
 */
package com.tisl.mpl.checkout.form;

/**
 * @author TCS
 *
 */
public class DeliverySlotEntry
{
	private String entryNumber;
	private String ussid;
	private String deliverySlotCost;
	private String deliverySlotDate;
	private String deliverySlotTime;

	/**
	 * @return the entryNumber
	 */
	public String getEntryNumber()
	{
		return entryNumber;
	}

	/**
	 * @param entryNumber
	 *           the entryNumber to set
	 */
	public void setEntryNumber(final String entryNumber)
	{
		this.entryNumber = entryNumber;
	}

	/**
	 * @return the ussid
	 */
	public String getUssid()
	{
		return ussid;
	}

	/**
	 * @param ussid
	 *           the ussid to set
	 */
	public void setUssid(final String ussid)
	{
		this.ussid = ussid;
	}

	/**
	 * @return the deliveryCost
	 */
	public String getDeliverySlotCost()
	{
		return deliverySlotCost;
	}

	/**
	 * @param deliverySlotCost
	 *           the deliverySlotCost to set
	 */
	public void setDeliverySlotCost(final String deliverySlotCost)
	{
		this.deliverySlotCost = deliverySlotCost;
	}

	/**
	 * @return the deliverySlotDate
	 */
	public String getDeliverySlotDate()
	{
		return deliverySlotDate;
	}

	/**
	 * @param deliverySlotDate
	 *           the deliverySlotDate to set
	 */
	public void setDeliverySlotDate(final String deliverySlotDate)
	{
		this.deliverySlotDate = deliverySlotDate;
	}

	/**
	 * @return the deliverySlotTime
	 */
	public String getDeliverySlotTime()
	{
		return deliverySlotTime;
	}

	/**
	 * @param deliverySlotTime
	 *           the deliverySlotTime to set
	 */
	public void setDeliverySlotTime(final String deliverySlotTime)
	{
		this.deliverySlotTime = deliverySlotTime;
	}

}
