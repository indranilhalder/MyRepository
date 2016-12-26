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
}
