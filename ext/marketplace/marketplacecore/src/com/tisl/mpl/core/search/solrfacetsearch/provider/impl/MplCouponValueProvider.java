/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherRestrictionService;
import de.hybris.platform.voucher.model.VoucherModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.coupon.dao.MplCouponListingDao;


public class MplCouponValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{
	private FieldNameProvider fieldNameProvider;
	private MplCouponListingDao mplCouponListingDao;
	private VoucherModelService voucherModelService;
	private VoucherRestrictionService voucherRestrictionService;

	/**
	 * @return the voucherRestrictionService
	 */
	public VoucherRestrictionService getVoucherRestrictionService()
	{
		return voucherRestrictionService;
	}





	/**
	 * @param voucherRestrictionService
	 *           the voucherRestrictionService to set
	 */
	public void setVoucherRestrictionService(final VoucherRestrictionService voucherRestrictionService)
	{
		this.voucherRestrictionService = voucherRestrictionService;
	}





	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}





	/**
	 * @return the mplCouponListingDao
	 */
	public MplCouponListingDao getMplCouponListingDao()
	{
		return mplCouponListingDao;
	}





	/**
	 * @param mplCouponListingDao
	 *           the mplCouponListingDao to set
	 */
	public void setMplCouponListingDao(final MplCouponListingDao mplCouponListingDao)
	{
		this.mplCouponListingDao = mplCouponListingDao;
	}



	/**
	 * @return the voucherModelService
	 */
	public VoucherModelService getVoucherModelService()
	{
		return voucherModelService;
	}


	/**
	 * @param voucherModelService
	 *           the voucherModelService to set
	 */
	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
	}


	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{

		if (model instanceof ProductModel)
		{
			final ProductModel product = (ProductModel) model;

			final Collection fieldValues = new ArrayList();

			fieldValues.addAll(createFieldValue(product, indexConfig, indexedProperty));

			return fieldValues;
		}
		throw new FieldValueProviderException("Cannot get promotion codes of non-product item");
	}

	protected List<FieldValue> createFieldValue(final ProductModel product, final IndexConfig indexConfig,
			final IndexedProperty indexedProperty)
	{
		final List fieldValues = new ArrayList();

		List<VoucherModel> restrictedVouchers = new ArrayList<VoucherModel>();
		final Collection<VoucherModel> productVouchers = getMplCouponListingDao().findVoucher();

		if (productVouchers != null)
		{
			restrictedVouchers = validateVoucherRestrictions(productVouchers, product);

		}



		final Iterator localIterator = restrictedVouchers.iterator();


		if (localIterator.hasNext())
		{
			final VoucherModel voucher = (VoucherModel) localIterator.next();

			addFieldValues(fieldValues, indexedProperty, null, voucher.getCode());
		}


		return fieldValues;
	}



	protected void addFieldValues(final List<FieldValue> fieldValues, final IndexedProperty indexedProperty,
			final LanguageModel language, final Object value)
	{
		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty,
				(language == null) ? null : language.getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
	}



	public List<VoucherModel> validateVoucherRestrictions(final Collection<VoucherModel> vouchers, final ProductModel productModel)
	{
		//include product vouchers alone
		final List<VoucherModel> productVoucherList = new ArrayList<VoucherModel>();
		//final boolean isProductCategoryVoucher = false;
		if (null != vouchers)
		{
			for (final VoucherModel voucher : vouchers)
			{
				if (null != voucher && null != productModel)
				{
					if (getVoucherModelService().isApplicable(voucher, productModel))
					{
						productVoucherList.add(voucher);
					}

				}


				/*
				 * if (!voucher.getRestrictions().isEmpty()) { for (final RestrictionModel voucherRestriction :
				 * voucher.getRestrictions()) { if (voucherRestriction instanceof ProductCategoryRestrictionModel) { if
				 * (getVoucherRestrictionService().isFulfilled(voucherRestriction, productModel)) { isProductCategoryVoucher
				 * = true; } break; } } }
				 */



				/*
				 * if (isProductCategoryVoucher) {
				 *
				 * productVoucherList.add(voucher); }
				 */
			} //end promotion for loop
		}


		return productVoucherList;

	}



}