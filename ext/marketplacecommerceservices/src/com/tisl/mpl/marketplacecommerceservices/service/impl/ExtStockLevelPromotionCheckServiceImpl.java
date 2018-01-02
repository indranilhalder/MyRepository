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
	//SONAR FIX
	//private static final Logger LOG = Logger.getLogger(ExtStockLevelPromotionCheckServiceImpl.class);


	@Resource(name = "stockPromoCheckDao")
	private ExtStockLevelPromotionCheckDao stockPromoCheckDao;


	@Override
	public Map<String, Integer> getCumulativeStockMap(final String codes, final String promoCode, final boolean sellerFlag)
	{
		// YTODO Auto-generated method stub
		//final Map<String, Integer> cummulativeMap = new HashMap<String, Integer>();
		final Map<String, Integer> cumualatibveStockMap = stockPromoCheckDao.getPromoInvalidationModelMap(codes, promoCode,
				sellerFlag);
		//		final int qualifyingCount = getQualifyingCount(promoCode);
		//		if (MapUtils.isNotEmpty(cumualatibveStockMap))
		//		{
		//			for (final Map.Entry<String, Integer> data : cumualatibveStockMap.entrySet())
		//			{
		//				if (data.getValue().intValue() >= qualifyingCount && qualifyingCount > 0)
		//				{
		//					cummulativeMap.put(data.getKey(), Integer.valueOf((data.getValue().intValue() / qualifyingCount)));
		//				}
		//				else
		//				{
		//					cummulativeMap.put(data.getKey(), data.getValue());
		//				}
		//			}
		//		}
		return cumualatibveStockMap;
		//return cummulativeMap;
	}


	/**
	 * @param promoCode
	 * @return count
	 */
	//	private int getQualifyingCount(final String promoCode)
	//	{
	//		final int count = 0;
	//				try
	//				{
	//					final ProductPromotionModel oModel = getPromoDetails(promoCode);
	//					if (oModel instanceof BuyAPercentageDiscountModel)
	//					{
	//						count = ((BuyAPercentageDiscountModel) oModel).getQuantity().intValue();
	//					}
	//					else if (oModel instanceof BuyXItemsofproductAgetproductBforfreeModel)
	//					{
	//						count = ((BuyXItemsofproductAgetproductBforfreeModel) oModel).getQualifyingCount().intValue();
	//					}
	//					else if (oModel instanceof BuyABFreePrecentageDiscountModel)
	//					{
	//						count = ((BuyABFreePrecentageDiscountModel) oModel).getQuantity().intValue();
	//					}
	//				}
	//				catch (final Exception exception)
	//				{
	//					LOG.debug("Error in Fetching of Qualifying Count. Setting it as 0");
	//					count = 0;
	//				}
	//		return count;
	//	}

	/**
	 * @param guid
	 * @return List<LimitedStockPromoInvalidationModel>
	 */
	@Override
	public List<LimitedStockPromoInvalidationModel> getPromoInvalidationList(final String guid)
	{
		// YTODO Auto-generated method stub
		return stockPromoCheckDao.getPromoInvalidationList(guid);
	}

	/**
	 * @param promoCode
	 * @param maxStockCount
	 */
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



	/**
	 * @param substring
	 * @param code
	 * @param dataMap
	 * @return stockMap
	 */
	@Override
	public Map<String, Integer> getCumulativeCatLevelStockMap(final String substring, final String code,
			final Map<String, String> dataMap)
	{
		final Map<String, Integer> stockMap = new HashMap<String, Integer>();
		final Map<String, Integer> detailsMap = stockPromoCheckDao.getCumulativeCatLevelStockMap(substring, code);

		//final int qualifyingCount = getQualifyingCount(code);

		for (final Map.Entry<String, String> stockkData : dataMap.entrySet())
		{
			for (final Map.Entry<String, Integer> data : detailsMap.entrySet())
			{
				if (StringUtils.equalsIgnoreCase(data.getKey(), stockkData.getValue()))
				{
					//					if (data.getValue().intValue() >= qualifyingCount && qualifyingCount > 0)
					//					{
					//						stockMap.put(stockkData.getKey(), Integer.valueOf((data.getValue().intValue() / qualifyingCount)));
					//					}
					//					else
					//					{
					//						stockMap.put(stockkData.getKey(), data.getValue());
					//					}
					stockMap.put(stockkData.getKey(), data.getValue());
				}
			}
		}
		return stockMap;
	}


	/**
	 *
	 * @param code
	 * @return oModel
	 */
	//	private ProductPromotionModel getPromoDetails(final String code)
	//	{
	//		final ProductPromotionModel oModel = modelService.create(ProductPromotionModel.class);
	//		oModel.setCode(code);
	//		oModel.setEnabled(Boolean.TRUE);
	//
	//		return flexibleSearchService.getModelByExample(oModel);
	//	}



	/**
	 * Get Total Buy A Above Promo Offer for Customer
	 *
	 * @param promoCode
	 * @param orginalUid
	 */
	@Override
	public int getTotalOfferOrderCount(final String promoCode, final String orginalUid)
	{
		return stockPromoCheckDao.getTotalOfferOrderCount(promoCode, orginalUid);
	}

	/**
	 * TISHS-143 Get Total Buy A Above Promo Offer for Customer
	 *
	 * @param promoCode
	 * @param orginalUid
	 */
	@Override
	public int getTotalOfferOrderCount(final String promoCode, final String orginalUid, final String guid)
	{
		return stockPromoCheckDao.getTotalOfferOrderCount(promoCode, orginalUid, guid);
	}
}
