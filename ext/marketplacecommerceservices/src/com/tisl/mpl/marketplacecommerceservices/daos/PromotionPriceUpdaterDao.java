/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.model.BuyAPercentageDiscountModel;


/**
 * @author 1022570
 *
 */
public interface PromotionPriceUpdaterDao
{
	List<BuyAPercentageDiscountModel> getRequiredPromotionList(Date mplConfigDate);
}