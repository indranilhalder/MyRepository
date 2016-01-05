/**
 *
 */
package com.tisl.mpl.wsdto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productDetails", propOrder =
{ "cancellationAllowed", "returnsAllowed", "replacementAllowed", "exchangeAllowed", "productName" })
public class ProductDetailsWsDTO
{
	@XmlElement(name = "cancellationAllowed")
	private int cancellationAllowed;
	@XmlElement(name = "returnsAllowed")
	private int returnsAllowed;
	@XmlElement(name = "replacementAllowed")
	private int replacementAllowed;
	@XmlElement(name = "exchangeAllowed")
	private int exchangeAllowed;
	@XmlElement(name = "productName")
	private String productName;

	//	public ProductDetailsWsDTO()
	//	{
	//
	//	}

	/**
	 * @return the cancellationAllowed
	 */
	public int getCancellationAllowed()
	{
		return cancellationAllowed;
	}

	/**
	 * @param cancellationAllowed
	 *           the cancellationAllowed to set
	 */
	public void setCancellationAllowed(final int cancellationAllowed)
	{
		this.cancellationAllowed = cancellationAllowed;
	}

	/**
	 * @return the returnsAllowed
	 */
	public int getReturnsAllowed()
	{
		return returnsAllowed;
	}

	/**
	 * @param returnsAllowed
	 *           the returnsAllowed to set
	 */
	public void setReturnsAllowed(final int returnsAllowed)
	{
		this.returnsAllowed = returnsAllowed;
	}

	/**
	 * @return the replacementAllowed
	 */
	public int getReplacementAllowed()
	{
		return replacementAllowed;
	}

	/**
	 * @param replacementAllowed
	 *           the replacementAllowed to set
	 */
	public void setReplacementAllowed(final int replacementAllowed)
	{
		this.replacementAllowed = replacementAllowed;
	}

	/**
	 * @return the exchangeAllowed
	 */
	public int getExchangeAllowed()
	{
		return exchangeAllowed;
	}

	/**
	 * @param exchangeAllowed
	 *           the exchangeAllowed to set
	 */
	public void setExchangeAllowed(final int exchangeAllowed)
	{
		this.exchangeAllowed = exchangeAllowed;
	}

	/**
	 * @return the productName
	 */
	public String getProductName()
	{
		return productName;
	}

	/**
	 * @param productName
	 *           the productName to set
	 */
	public void setProductName(final String productName)
	{
		this.productName = productName;
	}




}
