/**
 *
 */
package com.tisl.mpl.wsdto;

/**
 * @author TCS
 *
 */
public class DeliveryOptWsDTO
{
	private boolean homedelivery;
	private boolean expressdelivery;

	//	public DeliveryOptWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the homedelivery
	 */
	public boolean isHomedelivery()
	{
		return homedelivery;
	}

	/**
	 * @param homedelivery
	 *           the homedelivery to set
	 */
	public void setHomedelivery(final boolean homedelivery)
	{
		this.homedelivery = homedelivery;
	}

	/**
	 * @return the expressdelivery
	 */
	public boolean isExpressdelivery()
	{
		return expressdelivery;
	}

	/**
	 * @param expressdelivery
	 *           the expressdelivery to set
	 */
	public void setExpressdelivery(final boolean expressdelivery)
	{
		this.expressdelivery = expressdelivery;
	}



}
