/**
 *
 */
package com.tisl.mpl.coupon.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.coupon.dao.MplCouponDao;
import com.tisl.mpl.coupon.pojo.CampaignVoucherData;
import com.tisl.mpl.coupon.service.CampaignVoucherDataService;


/**
 * @author TCS
 *
 */
public class DefaultCampaignVoucherDataService implements CampaignVoucherDataService
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultCampaignVoucherDataService.class.getName());

	private ModelService modelService;

	private MplCouponDao mplCouponDao;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private CampaignVoucherData campaignVoucherData;















	//For Sonars
	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the mplCouponDao
	 */
	public MplCouponDao getMplCouponDao()
	{
		return mplCouponDao;
	}

	/**
	 * @param mplCouponDao
	 *           the mplCouponDao to set
	 */
	public void setMplCouponDao(final MplCouponDao mplCouponDao)
	{
		this.mplCouponDao = mplCouponDao;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the campaignVoucherData
	 */
	public CampaignVoucherData getCampaignVoucherData()
	{
		return campaignVoucherData;
	}

	/**
	 * @param campaignVoucherData
	 *           the campaignVoucherData to set
	 */
	public void setCampaignVoucherData(final CampaignVoucherData campaignVoucherData)
	{
		this.campaignVoucherData = campaignVoucherData;
	}
}
