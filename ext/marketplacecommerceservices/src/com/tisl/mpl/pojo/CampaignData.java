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


	private String offer_id;
	private String offer_name;
	private String offer_active;
	private String offer_type;
	private String offer_channel;
	private String offer_startdate;
	private String offer_enddate;
	private String url;
	private String creation_date;
	private String modified_date;


	/**
	 * @return the offer_id
	 */
	public String getOffer_id()
	{
		return offer_id;
	}

	/**
	 * @param offer_id
	 *           the offer_id to set
	 */
	public void setOffer_id(final String offer_id)
	{
		this.offer_id = offer_id;
	}

	/**
	 * @return the offer_name
	 */
	public String getOffer_name()
	{
		return offer_name;
	}

	/**
	 * @param offer_name
	 *           the offer_name to set
	 */
	public void setOffer_name(final String offer_name)
	{
		this.offer_name = offer_name;
	}

	/**
	 * @return the offer_active
	 */
	public String getOffer_active()
	{
		return offer_active;
	}

	/**
	 * @param offer_active
	 *           the offer_active to set
	 */
	public void setOffer_active(final String offer_active)
	{
		this.offer_active = offer_active;
	}

	/**
	 * @return the offer_type
	 */
	public String getOffer_type()
	{
		return offer_type;
	}

	/**
	 * @param offer_type
	 *           the offer_type to set
	 */
	public void setOffer_type(final String offer_type)
	{
		this.offer_type = offer_type;
	}

	/**
	 * @return the offer_channel
	 */
	public String getOffer_channel()
	{
		return offer_channel;
	}

	/**
	 * @param offer_channel
	 *           the offer_channel to set
	 */
	public void setOffer_channel(final String offer_channel)
	{
		this.offer_channel = offer_channel;
	}

	/**
	 * @return the offer_startdate
	 */
	public String getOffer_startdate()
	{
		return offer_startdate;
	}

	/**
	 * @param offer_startdate
	 *           the offer_startdate to set
	 */
	public void setOffer_startdate(final String offer_startdate)
	{
		this.offer_startdate = offer_startdate;
	}

	/**
	 * @return the offer_enddate
	 */
	public String getOffer_enddate()
	{
		return offer_enddate;
	}

	/**
	 * @param offer_enddate
	 *           the offer_enddate to set
	 */
	public void setOffer_enddate(final String offer_enddate)
	{
		this.offer_enddate = offer_enddate;
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
	 * @return the creation_date
	 */
	public String getCreation_date()
	{
		return creation_date;
	}

	/**
	 * @param creation_date
	 *           the creation_date to set
	 */
	public void setCreation_date(final String creation_date)
	{
		this.creation_date = creation_date;
	}

	/**
	 * @return the modified_date
	 */
	public String getModified_date()
	{
		return modified_date;
	}

	/**
	 * @param modified_date
	 *           the modified_date to set
	 */
	public void setModified_date(final String modified_date)
	{
		this.modified_date = modified_date;
	}

}