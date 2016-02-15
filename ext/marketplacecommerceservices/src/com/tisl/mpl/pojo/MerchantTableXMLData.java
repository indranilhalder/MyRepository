/**
 *
 */
package com.tisl.mpl.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "Merchant")
@XmlType(propOrder =
{ "merchantCode", "merchantDesc" })
public class MerchantTableXMLData
{
	private String merchantCode;
	private String merchantDesc;

	/**
	 * @return the merchantCode
	 */
	@XmlElement(name = "MerchantCode")
	public String getMerchantCode()
	{
		return merchantCode;
	}

	/**
	 * @param merchantCode
	 *           the merchantCode to set
	 */
	public void setMerchantCode(final String merchantCode)
	{
		this.merchantCode = merchantCode;
	}

	/**
	 * @return the merchantDesc
	 */
	@XmlElement(name = "MerchantDesc")
	public String getMerchantDesc()
	{
		return merchantDesc;
	}

	/**
	 * @param merchantDesc
	 *           the merchantDesc to set
	 */
	public void setMerchantDesc(final String merchantDesc)
	{
		this.merchantDesc = merchantDesc;
	}


}
