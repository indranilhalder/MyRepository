/**
 *
 */
package com.tisl.mpl.wsdto;

/**
 * @author TCS
 *
 */
public class ProtectionPlanWsDTO implements java.io.Serializable
{
	private String description;
	private String defaultYear;
	private String totalYear;
	private double priceOneYear;

	//	public ProtectionPlanWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *           the description to set
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * @return the defaultYear
	 */
	public String getDefaultYear()
	{
		return defaultYear;
	}

	/**
	 * @param defaultYear
	 *           the defaultYear to set
	 */
	public void setDefaultYear(final String defaultYear)
	{
		this.defaultYear = defaultYear;
	}

	/**
	 * @return the totalYear
	 */
	public String getTotalYear()
	{
		return totalYear;
	}

	/**
	 * @param totalYear
	 *           the totalYear to set
	 */
	public void setTotalYear(final String totalYear)
	{
		this.totalYear = totalYear;
	}

	/**
	 * @return the priceOneYear
	 */
	public double getPriceOneYear()
	{
		return priceOneYear;
	}

	/**
	 * @param priceOneYear
	 *           the priceOneYear to set
	 */
	public void setPriceOneYear(final double priceOneYear)
	{
		this.priceOneYear = priceOneYear;
	}


}
