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
	public Map<String, Integer> getCumulativeStockMap(final String codes, String promoCode, boolean sellerFlag);

	public List<LimitedStockPromoInvalidationModel> getPromoInvalidationList(final String guid);
}