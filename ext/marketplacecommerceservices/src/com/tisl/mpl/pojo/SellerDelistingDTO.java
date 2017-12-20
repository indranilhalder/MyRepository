/**
 *
 */
package com.tisl.mpl.pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author TCS
 *
 */
@XmlRootElement(name = "SellerDelisting")
public class SellerDelistingDTO
{
	private String sellerID;
	private String delisting;
	private String blockoms;

	/**
	 * @return the sellerID
	 */
	@XmlElement(name = "sellerid")
	public String getSellerID()
	{
		return sellerID;
	}

	/**
	 * @return the delisting
	 */
	@XmlElement(name = "Delisting")
	public String getDelisting()
	{
		return delisting;
	}

	/**
	 * @return the blockoms
	 */
	@XmlElement(name = "BlockOMS")
	public String getBlockOMS()
	{
		return blockoms;
	}

	/**
	 * @param sellerID
	 *           the sellerID to set
	 */
	public void setSellerID(final String sellerID)
	{
		this.sellerID = sellerID;
	}

	/**
	 * @param delisting
	 *           the delisting to set
	 */
	public void setDelisting(final String delisting)
	{
		this.delisting = delisting;
	}

	/**
	 * @param blockoms
	 *           the blockoms to set
	 */
	public void setBlockOMS(final String blockoms)
	{
		this.blockoms = blockoms;
	}

}
