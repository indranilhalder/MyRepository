/**
 *
 */
package com.tisl.mpl.pojo;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.jobs.PromotionCreationJob;


/**
 * @author TCS
 *
 */
public class CampaignData
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PromotionCreationJob.class.getName());


	private String offerid;
	private String offername;
	private String offeractive;
	private String offertype;
	private String offerchannel;
	private String offerstartdate;
	private String offerenddate;
	private String url;
	private String creationdate;
	private String modifieddate;

	/**
	 * @return the offerid
	 */
	public String getOfferid()
	{
		return offerid;
	}

	/**
	 * @param offerid
	 *           the offerid to set
	 */
	public void setOfferid(final String offerid)
	{
		this.offerid = offerid;
	}

	/**
	 * @return the offername
	 */
	public String getOffername()
	{
		return offername;
	}

	/**
	 * @param offername
	 *           the offername to set
	 */
	public void setOffername(final String offername)
	{
		this.offername = offername;
	}

	/**
	 * @return the offeractive
	 */
	public String getOfferactive()
	{
		return offeractive;
	}

	/**
	 * @param offeractive
	 *           the offeractive to set
	 */
	public void setOfferactive(final String offeractive)
	{
		this.offeractive = offeractive;
	}

	/**
	 * @return the offertype
	 */
	public String getOffertype()
	{
		return offertype;
	}

	/**
	 * @param offertype
	 *           the offertype to set
	 */
	public void setOffertype(final String offertype)
	{
		this.offertype = offertype;
	}

	/**
	 * @return the offerchannel
	 */
	public String getOfferchannel()
	{
		return offerchannel;
	}

	/**
	 * @param offerchannel
	 *           the offerchannel to set
	 */
	public void setOfferchannel(final String offerchannel)
	{
		this.offerchannel = offerchannel;
	}

	/**
	 * @return the offerstartdate
	 */
	public String getOfferstartdate()
	{
		return offerstartdate;
	}

	/**
	 * @param offerstartdate
	 *           the offerstartdate to set
	 */
	public void setOfferstartdate(final String offerstartdate)
	{
		this.offerstartdate = offerstartdate;
	}

	/**
	 * @return the offerenddate
	 */
	public String getOfferenddate()
	{
		return offerenddate;
	}

	/**
	 * @param offerenddate
	 *           the offerenddate to set
	 */
	public void setOfferenddate(final String offerenddate)
	{
		this.offerenddate = offerenddate;
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 *           the url to set
	 */
	public void setUrl(final String url)
	{
		this.url = url;
	}

	/**
	 * @return the creationdate
	 */
	public String getCreationdate()
	{
		return creationdate;
	}

	/**
	 * @param creationdate
	 *           the creationdate to set
	 */
	public void setCreationdate(final String creationdate)
	{
		this.creationdate = creationdate;
	}

	/**
	 * @return the modifieddate
	 */
	public String getModifieddate()
	{
		return modifieddate;
	}

	/**
	 * @param modifieddate
	 *           the modifieddate to set
	 */
	public void setModifieddate(final String modifieddate)
	{
		this.modifieddate = modifieddate;
	}



}