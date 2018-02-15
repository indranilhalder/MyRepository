package com.tisl.mpl.marketplacecommerceservices.services.product.impl;


import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.enums.CustomerReviewApprovalType;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao;
import com.tisl.mpl.marketplacecommerceservices.services.product.MplProductService;


/**
 * Default implementation of the {@link ProductService}.
 */

public class MplProductServiceImpl extends DefaultProductService implements MplProductService
{


	@Autowired
	private MplProductDao productDao;

	private static final Logger LOG = Logger.getLogger(MplProductServiceImpl.class);

	private Converter<CustomerReviewModel, ReviewData> customerReviewConverter;

	@Override
	public ProductModel getProductForCode(final String code)
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		final List<ProductModel> products = productDao.findProductsByCodeHero(code);

		validateIfSingleResult(products, format("Product with code '%s' not found!", code),
				format("Product code '%s' is not unique, %d products found!", code, Integer.valueOf(products.size())));

		return products.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.services.product.MplProductService#getProductListForCodeList(de.hybris
	 * .platform.catalog.model.CatalogVersionModel, java.util.List)
	 */
	@Override
	public List<ProductModel> getProductListForCodeList(final CatalogVersionModel catalogVersion,
			final List<String> productCodeList)
	{
		// YTODO Auto-generated method stub
		final List<ProductModel> products = productDao.findProductListByCodeList(catalogVersion, productCodeList);
		return products;
	}

	/*
	 *
	 *
	 */
	@Override
	public ReviewData editDeleteReviewEntry(final UserModel userModel, final ProductModel productModel, final String reviewId,
			final boolean isEdit, final ReviewData review)
	{
		CustomerReviewModel customerReview = null;
		try
		{
			final PK pk = PK.fromLong(Long.parseLong(reviewId));

			customerReview = getModelService().get(pk);
			if (customerReview != null && productModel != null && customerReview.getProduct().equals(productModel)
					&& customerReview.getUser().equals(userModel))
			{
				if (isEdit && review != null)
				{
					//Edit
					customerReview.setHeadline(review.getHeadline());
					customerReview.setComment(review.getComment());
					customerReview.setRating(review.getRating());
					customerReview.setAlias(review.getAlias());
					customerReview.setApprovalStatus(CustomerReviewApprovalType.PENDING);
					getModelService().save(customerReview);
				}
				else
				{
					//Delete

					customerReview.setBlocked(Boolean.TRUE);
					getModelService().save(customerReview);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error in editDeleteReviewEntry=>", e);
			throw new EtailNonBusinessExceptions(e);
		}
		return getCustomerReviewConverter().convert(customerReview);
	}

	@Override
	public ReviewData getReviewDataforPK(final String reviewId)
	{
		final PK pk = PK.fromLong(Long.parseLong(reviewId));

		return getCustomerReviewConverter().convert(getModelService().get(pk));
	}

	/**
	 * @return the customerReviewConverter
	 */
	public Converter<CustomerReviewModel, ReviewData> getCustomerReviewConverter()
	{
		return customerReviewConverter;
	}

	/**
	 * @param customerReviewConverter
	 *           the customerReviewConverter to set
	 */
	public void setCustomerReviewConverter(final Converter<CustomerReviewModel, ReviewData> customerReviewConverter)
	{
		this.customerReviewConverter = customerReviewConverter;
	}
}