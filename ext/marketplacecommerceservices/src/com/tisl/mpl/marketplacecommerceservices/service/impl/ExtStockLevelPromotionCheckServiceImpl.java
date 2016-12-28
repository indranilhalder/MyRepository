/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.LimitedStockPromoInvalidationModel;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tisl.mpl.marketplacecommerceservices.service.ExtStockLevelPromotionCheckService;
import com.tisl.mpl.promotion.dao.ExtStockLevelPromotionCheckDao;


/**
 * @author TCS
 *
 */
public class ExtStockLevelPromotionCheckServiceImpl implements ExtStockLevelPromotionCheckService
{

	/*
	 * (non-Javadoc)
	 */
	@Resource(name = "stockPromoCheckDao")
	private ExtStockLevelPromotionCheckDao stockPromoCheckDao;


	@Override
	public Map<String, Integer> getCumulativeStockMap(final String codes, final String promoCode, final boolean sellerFlag)
	{
		// YTODO Auto-generated method stub
		final Map<String, Integer> cumualatibveStockMap = stockPromoCheckDao.getPromoInvalidationModelMap(codes, promoCode,
				sellerFlag);
		return cumualatibveStockMap;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.ExtStockLevelPromotionCheckService#getPromoInvalidationList(java
	 * .lang.String)
	 */
	@Override
	public List<LimitedStockPromoInvalidationModel> getPromoInvalidationList(final String guid)
	{
		// YTODO Auto-generated method stub
		return stockPromoCheckDao.getPromoInvalidationList(guid);
	}
	@Override
	public List<String> getStockForPromotion(final String promoCode, final int maxStockCount)
	{
		// YTODO Auto-generated method stub
		final List<String> cumualativeStockMap = stockPromoCheckDao.getStockForPromotion(promoCode, maxStockCount);
		return cumualativeStockMap;
	}

}
