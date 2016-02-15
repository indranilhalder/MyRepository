package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.MplBuyBoxDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplBuyBoxService;


/**
 * @author TCS
 */


public class MplBuyBoxServiceImpl implements MplBuyBoxService
{
	@Autowired
	private MplBuyBoxDao mplBuyBoxDao;

	/**
	 * @return the mplBuyBoxDao
	 */
	public MplBuyBoxDao getMplBuyBoxDao()
	{
		return mplBuyBoxDao;
	}



	/**
	 * @param mplBuyBoxDao
	 *           the mplBuyBoxDao to set
	 */
	public void setMplBuyBoxDao(final MplBuyBoxDao mplBuyBoxDao)
	{
		this.mplBuyBoxDao = mplBuyBoxDao;
	}

	@Autowired
	private ModelService modelService;


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

	/*
	 * @Javadoc Method to Retrieve BuyBoxWieghtage based on articleSKUID
	 * 
	 * @param articleSKUIDs
	 * 
	 * @return availableBuyBoxMap
	 */

	@Override
	@Deprecated
	public String getAllBuyBoxDetail(final String aticleSKUIDs)
	{
		return aticleSKUIDs;
		/*
		 * String buyBoxWinnerUSSID = null; final BuyBoxWieghtageModel buyBoxWieghtageModel =
		 * mplBuyBoxDao.getAllBuyBoxDetail(aticleSKUIDs);
		 * 
		 * if (null != buyBoxWieghtageModel) { buyBoxWinnerUSSID = buyBoxWieghtageModel.getSellerArticleSKU(); } return
		 * buyBoxWinnerUSSID;
		 */

	}
}