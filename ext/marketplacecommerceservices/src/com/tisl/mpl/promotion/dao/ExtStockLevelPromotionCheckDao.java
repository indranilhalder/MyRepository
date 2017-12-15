/**
 *
 */
package com.tisl.mpl.promotion.dao;

import de.hybris.platform.core.model.LimitedStockPromoInvalidationModel;

import java.util.List;
import java.util.Map;



/**
 * @author TCS
 *
 */
public interface ExtStockLevelPromotionCheckDao
{
	public Map<String, Integer> getPromoInvalidationModelMap(final String codes, String promoCode, final boolean sellerFlag);

	public List<LimitedStockPromoInvalidationModel> getPromoInvalidationList(final String guid);

	/**
	 * TPR-965 changes for price update
	 *
	 * @param codes
	 * @param promoCode
	 * @return List<String>
	 */
	public List<String> getStockForPromotion(String promoCode, int stockCount);

	/**
	 * @param promoCode
	 * @param orginalUid
	 * @return Map<String, Integer>
	 */
	public int getCummulativeOrderCount(String promoCode, String orginalUid);

	/**
	 * @param substring
	 * @param code
	 * @return Map<String, Integer>
	 */
	public Map<String, Integer> getCumulativeCatLevelStockMap(String substring, String code);

	/**
	 * @param promoCode
	 * @param orginalUid
	 * @return int
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
