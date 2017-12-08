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
@XmlRootElement(name = "ListDelist")
public class RecordsetDTO

{

	private String sellerID;
	private String delisting;
	private DelistDTO delist;

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
	 * @return the delist
	 */

	@XmlElement(name = "Delist")
	public DelistDTO getDelist()
	{
		return delist;
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
	 * @param delist
	 *           the delist to set
	 */

	public void setDelist(final DelistDTO delist)

	{
		this.delist = delist;
	}

}
