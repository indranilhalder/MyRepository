/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.LimitedStockPromoInvalidationModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

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


	/**
	 * @param orginalUid
	 * @param promoCode
	 */
	@Override
	public int getCummulativeOrderCount(final String promoCode, final String orginalUid)
	{
		// YTODO Auto-generated method stub
		return stockPromoCheckDao.getCummulativeOrderCount(promoCode, orginalUid);
	}



	@Override
	public Map<String, Integer> getCumulativeCatLevelStockMap(final String substring, final String code,
			final Map<String, String> dataMap)
	{
		final Map<String, Integer> stockMap = new HashMap<String, Integer>();
		final Map<String, Integer> detailsMap = stockPromoCheckDao.getCumulativeCatLevelStockMap(substring, code);
		for (final Map.Entry<String, String> stockkData : dataMap.entrySet())
		{
			for (final Map.Entry<String, Integer> data : detailsMap.entrySet())
			{
				if (StringUtils.equalsIgnoreCase(data.getKey(), stockkData.getValue()))
				{
					stockMap.put(stockkData.getKey(), data.getValue());
				}
			}
		}
		return stockMap;
	}
}
