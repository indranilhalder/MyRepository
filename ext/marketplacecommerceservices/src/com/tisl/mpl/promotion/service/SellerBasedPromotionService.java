/**
 *
 */
package com.tisl.mpl.promotion.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface SellerBasedPromotionService
{
	List<SellerInformationModel> fetchSellerInformation(String code, CatalogVersionModel oModel);

	List<PriceRowModel> fetchPriceInformation(String code, CatalogVersionModel oModel);

	List<AbstractPromotionModel> fetchPromotionDetails(String code);

	void modifyFiredMessage(String string);

	List<AbstractPromotionModel> getPromoDetails();

	/**
	 * @param promoCode
	 * @return boolean
	 */
	boolean getPromoDetails(String promoCode);
}
