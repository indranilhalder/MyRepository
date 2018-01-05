/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.LimitedStockPromoInvalidationModel;

import java.util.List;
import java.util.Map;




/**
 * @author TCS
 *
 */
public interface ExtStockLevelPromotionCheckService
{
	/**
	 *
	 * @param codes
	 * @param promoCode
	 * @param sellerFlag
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getCumulativeStockMap(final String codes, String promoCode, boolean sellerFlag);

	/**
	 *
	 * @param guid
	 * @return List<LimitedStockPromoInvalidationModel>
	 */
	public List<LimitedStockPromoInvalidationModel> getPromoInvalidationList(final String guid);

	/**
	 * @param promoCode
	 * @param maxStockCount
	 * @return List<String> getStockForPromotion
	 */
	public List<String> getStockForPromotion(final String promoCode, final int maxStockCount);

	/**
	 *
	 * @param promoCode
	 * @param orginalUid
	 */
	public int getCummulativeOrderCount(String promoCode, String orginalUid);

	/**
	 * @param substring
	 * @param code
	 * @param dataMap
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getCumulativeCatLevelStockMap(String substring, String code, Map<String, String> dataMap);

	/**
	 *
	 * @param promoCode
	 * @param orginalUid
	 */
	public int getTotalOfferOrderCount(String promoCode, String orginalUid);

	/**
	 * TISHS-143
	 * 
	 * @param promoCode
	 * @param orginalUid
	 * @param guid
	 * @return int
	 */
	public int getTotalOfferOrderCount(String promoCode, String orginalUid, String guid);




}
