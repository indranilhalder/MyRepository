/**
 *
 */
package com.tisl.mpl.pojo;

/**
 * @author TCS
 *
 */
public class MplLimitedOfferData
{

	private boolean isExhausted;
	private int actualCustomerCount;

	/**
	 * @return the isExhausted
	 */
	public boolean isExhausted()
	{
		return isExhausted;
	}

	/**
	 * @param isExhausted
	 *           the isExhausted to set
	 */
	public void setExhausted(final boolean isExhausted)
	{
		this.isExhausted = isExhausted;
	}

	/**
	 * @return the actualCustomerCount
	 */
	public int getActualCustomerCount()
	{
		return actualCustomerCount;
	}

	/**
	 * @param actualCustomerCount
	 *           the actualCustomerCount to set
	 */
	public void setActualCustomerCount(final int actualCustomerCount)
	{
		this.actualCustomerCount = actualCustomerCount;
	}

}
