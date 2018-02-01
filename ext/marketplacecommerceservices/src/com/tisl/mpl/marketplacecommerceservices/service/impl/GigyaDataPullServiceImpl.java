/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.daos.impl.GigyaDataPullDaoImpl;
import com.tisl.mpl.marketplacecommerceservices.service.GigyaDataPullService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
public class GigyaDataPullServiceImpl implements GigyaDataPullService
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(GigyaDataPullServiceImpl.class.getName());
	private ModelService modelService;

	@Resource(name = "gigyaDataPullDaoImpl")
	GigyaDataPullDaoImpl gigyaDataPullDaoImpl;

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
	 * @Description : To Save Cron details in a Table
	 * @param: endTime
	 * @param: cronCode
	 */
	@Override
	public void saveCronDetails(final Date time, final String cronCode)
	{
		final MplConfigurationModel oModel = gigyaDataPullDaoImpl.fetchConfigDetails(cronCode);
		if (null != oModel && null != oModel.getMplConfigCode())
		{
			oModel.setMplConfigDate(time);
			getModelService().save(oModel);
		}
	}

	/**
	 * @Description : To Save Cron details in a Table
	 * @param: cronCode
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String cronCode)
	{
		// YTODO Auto-generated method stub
		return gigyaDataPullDaoImpl.fetchConfigDetails(cronCode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.GigyaDataPullService#fetchProductDetails()
	 */
	@Override
	public Map<String, Date> fetchProductDetails()
	{
		// YTODO Auto-generated method stub
		return gigyaDataPullDaoImpl.fetchProductDetails();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.GigyaDataPullService#specificCustomerDetails(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public Map<String, Date> specificProductDetails(final Date lastFetchedDate)
	{
		// YTODO Auto-generated method stub
		return gigyaDataPullDaoImpl.specificProductDetails(lastFetchedDate);
	}
}
