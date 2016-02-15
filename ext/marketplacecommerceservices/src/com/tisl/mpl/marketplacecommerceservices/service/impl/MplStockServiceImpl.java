package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.MplStockDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplStockService;


/**
 * @author TCS
 */


public class MplStockServiceImpl implements MplStockService
{
	@Autowired
	private MplStockDao mplStockDao;
	@Autowired
	private ModelService modelService;



	/**
	 * @return the mplStockDao
	 */
	public MplStockDao getMplStockDao()
	{
		return mplStockDao;
	}



	/**
	 * @param mplStockDao
	 *           the mplStockDao to set
	 */
	public void setMplStockDao(final MplStockDao mplStockDao)
	{
		this.mplStockDao = mplStockDao;
	}



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
	 *
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplStockService#getPriceRowDetail(de.hybris.platform.
	 * ordersplitting .model.StockLevelModel)
	 *
	 * @Javadoc Method to Retrieve Stock based on articleSKUID
	 *
	 * @param articleSKUID
	 *
	 * @return stockDetails
	 */
	@Override
	public List<StockLevelModel> getStockLevelDetail(final String articleSKUID)
	{
		final List<StockLevelModel> stockDetails = mplStockDao.getStockDetail(articleSKUID);
		return stockDetails;
	}

	/*
	 * @Javadoc Method to Retrieve Stock based on articleSKUID
	 *
	 * @param articleSKUIDs
	 *
	 * @return availableStockMap
	 */

	@Override
	public Map<String, Integer> getAllStockLevelDetail(final String aticleSKUIDs)
	{
		final List<StockLevelModel> stockDetails = mplStockDao.getAllStockDetail(aticleSKUIDs);
		final Map<String, Integer> availableStockMap = new HashMap<String, Integer>();
		for (final StockLevelModel stockLevelModel : stockDetails)
		{
			availableStockMap.put(stockLevelModel.getSellerArticleSKU(), Integer.valueOf(stockLevelModel.getAvailable()));
		}
		return availableStockMap;
	}

}