/**
 *
 */
package com.tisl.mpl.promotion.dao;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;

import java.util.List;

import com.tisl.mpl.core.model.PromotionalPriceRowModel;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public interface UpdatePromotionalPriceDao
{
	List<PriceRowModel> fetchPricedData(List<String> product);

	List<PromotionalPriceRowModel> fetchPromoPriceData(String promoCode);

	/**
	 * to fetch the cron details
	 *
	 * @param code
	 * @return MplConfigurationModel
	 */
	MplConfigurationModel getCronDetails(String code);

	List<ProductPromotionModel> getSortedPromoListForProduct(final ProductModel product, final ProductPromotionModel promoCurrent);

	List<ProductModel> getProductsForCategory(final List<CategoryModel> categories, final List<ProductModel> exProductList,
			final List<CategoryModel> brands, final List<CategoryModel> rejectBrandList);

	List<SellerInformationModel> getValidSellersForPromotion(final ProductModel product, final List<String> promoSellersList,
			final List<String> promoRejectSellerList);

}