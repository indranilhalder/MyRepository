/**
 *
 */
package com.tisl.mpl.pojo;

import java.util.Date;


/**
 * @author TCS
 *
 */
public class MplBinErrorData
{
	private String bin;
	private Date time;
	private String customerId;
	private String paymentMode;
	private String typeOfError;

	/**
	 * @return the bin
	 */
	public String getBin()
	{
		return bin;
	}

	/**
	 * @param bin
	 *           the bin to set
	 */
	public void setBin(final String bin)
	{
		this.bin = bin;
	}



	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @param customerId
	 *           the customerId to set
	 */
	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
	}

	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode()
	{
		return paymentMode;
	}

	/**
	 * @param paymentMode
	 *           the paymentMode to set
	 */
	public void setPaymentMode(final String paymentMode)
	{
		this.paymentMode = paymentMode;
	}

	/**
	 * @return the time
	 */
	public Date getTime()
	{
		return time;
	}

	/**
	 * @param time
	 *           the time to set
	 */
	public void setTime(final Date time)
	{
		this.time = time;
	}

	/**
	 * @return the typeOfError
	 */
	public String getTypeOfError()
	{
		return typeOfError;
	}

	/**
	 * @param typeOfError
	 *           the typeOfError to set
	 */
	public void setTypeOfError(final String typeOfError)
	{
		this.typeOfError = typeOfError;
	}


}
