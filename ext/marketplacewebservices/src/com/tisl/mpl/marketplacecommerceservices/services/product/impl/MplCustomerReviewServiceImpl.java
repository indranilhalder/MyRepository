/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.services.product.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.impl.DefaultCustomerReviewService;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.product.MplCustomerReviewDao;
import com.tisl.mpl.marketplacecommerceservices.services.product.MplCustomerReviewService;


/**
 * @author TCS
 *
 */
public class MplCustomerReviewServiceImpl extends DefaultCustomerReviewService implements MplCustomerReviewService
{

	@Autowired
	private MplCustomerReviewDao mplCustomerReviewDao;

	@Resource
	private CommonI18NService commonI18NService;

	private ModelService modelService;

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}



	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}



	/**
	 * @return the mplCustomerReviewDao
	 */
	public MplCustomerReviewDao getMplCustomerReviewDao()
	{
		return mplCustomerReviewDao;
	}



	/**
	 * @param mplCustomerReviewDao
	 *           the mplCustomerReviewDao to set
	 */
	public void setMplCustomerReviewDao(final MplCustomerReviewDao mplCustomerReviewDao)
	{
		this.mplCustomerReviewDao = mplCustomerReviewDao;
	}



	@Override
	public SearchPageData<CustomerReviewModel> getReviewsForProductAndLanguage(final ProductModel product,
			final LanguageModel language, final PageableData pageableData, final String orderBy)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("product", product);
		ServicesUtil.validateParameterNotNullStandardMessage("language", language);
		return getMplCustomerReviewDao().getReviewsForProductAndLanguage(product, language, pageableData, orderBy);
	}

	@Override
	public List<List<Object>> getGroupByRatingsForProd(final ProductModel product)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("product", product);
		return getMplCustomerReviewDao().getGroupByRatingsForProd(product, getCommonI18NService().getCurrentLanguage());
	}

	@Override
	public boolean reviewApplicableForGivenCustomer(final UserModel user, final ProductModel product)
	{
		return getMplCustomerReviewDao().reviewApplicableForGivenCustomer(user, product);
	}
}