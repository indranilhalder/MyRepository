/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.promotions.model.ProductPromotionModel;

import java.util.Date;
import java.util.List;


/**
 * @author 1022570
 *
 */
public interface PromotionPriceUpdaterDao
{
	List<ProductPromotionModel> getRequiredPromotionList(Date mplConfigDate);
}