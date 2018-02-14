/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.product;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.dao.CustomerReviewDao;
import de.hybris.platform.customerreview.model.CustomerReviewModel;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplCustomerReviewDao extends CustomerReviewDao
{

	/**
	 * @param product
	 * @param language
	 * @param orderBy
	 * @return SearchPageData<CustomerReviewModel>
	 */
	public SearchPageData<CustomerReviewModel> getReviewsForProductAndLanguage(ProductModel product, LanguageModel language,
			final PageableData pageableData, final String orderBy);

	/**
	 * @param product
	 * @param language
	 * @return List<List<Object>>
	 */
	public List<List<Object>> getGroupByRatingsForProd(ProductModel product, LanguageModel language);


	/**
	 * @param user
	 * @param product
	 * @return boolean
	 */
	public boolean reviewApplicableForGivenCustomer(UserModel user, ProductModel product);
}