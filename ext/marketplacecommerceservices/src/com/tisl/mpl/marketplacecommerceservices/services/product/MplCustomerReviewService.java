/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.services.product;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.CustomerReviewService;
import de.hybris.platform.customerreview.model.CustomerReviewModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplCustomerReviewService extends CustomerReviewService
{
	public abstract SearchPageData<CustomerReviewModel> getReviewsForProductAndLanguage(ProductModel paramProductModel,
			LanguageModel paramLanguageModel, PageableData pageableData, final String orderBy);

	/**
	 * @param product
	 * @return List<List<Object>>
	 */
	public List<List<Object>> getGroupByRatingsForProd(ProductModel product);

	/**
	 * @param product
	 * @param user
	 * @return boolean
	 */
	public boolean reviewApplicableForGivenCustomer(final UserModel user, final ProductModel product);

}