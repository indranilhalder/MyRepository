package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.MplBuyBoxDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplBuyBoxService;


/**
 * @author TCS
 */


public class DefaultMplBuyBoxService implements MplBuyBoxService
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
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplBuyBoxService#getAllBuyBoxDetail(java.lang.String)
	 */
	@Override
	public String getAllBuyBoxDetail(final String aticleSKUIDs)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * @Javadoc Method to Retrieve BuyBoxWieghtage based on articleSKUID
	 *
	 * @param articleSKUIDs
	 *
	 * @return availableBuyBoxMap
	 */

	/*
	 * @Override public String getAllBuyBoxDetail(final String aticleSKUIDs) { String buyBoxWinnerUSSID = null; final
	 * BuyBoxWieghtageModel buyBoxWieghtageModel = mplBuyBoxDao.getAllBuyBoxDetail(aticleSKUIDs);
	 *
	 * if (null != buyBoxWieghtageModel) { buyBoxWinnerUSSID = buyBoxWieghtageModel.getSellerArticleSKU(); } return
	 * buyBoxWinnerUSSID;
	 *
	 * }
	 */
}