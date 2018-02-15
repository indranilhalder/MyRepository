package com.tisl.mpl.marketplacecommerceservices.services.product;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;

import java.util.List;


/**
 * Service to read and update {@link ProductModel ProductModel}s.
 *
 * @spring.bean productService
 */
public interface MplProductService extends ProductService
{
	//TISPRD-1631 Changes
	@Override
	ProductModel getProductForCode(String code);

	List<ProductModel> getProductListForCodeList(CatalogVersionModel catalogVersion, List<String> productCodeList);

	/**
	 * @param userModel
	 * @param productModel
	 * @param reviewId
	 * @param isEdit
	 * @param review
	 */
	ReviewData editDeleteReviewEntry(UserModel userModel, ProductModel productModel, String reviewId, boolean isEdit,
			ReviewData review);

	/*
	 * @param reviewId
	 */
	ReviewData getReviewDataforPK(String reviewId);
}