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



	private boolean exhausted;//SONAR FIX
	private int actualCustomerCount;


	/**
	 * @return the exhausted
	 */
	public boolean isExhausted()
	{
		return exhausted;
	}

	/**
	 * @param exhausted
	 *           the exhausted to set
	 */
	public void setExhausted(final boolean exhausted)
	{
		this.exhausted = exhausted;
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
