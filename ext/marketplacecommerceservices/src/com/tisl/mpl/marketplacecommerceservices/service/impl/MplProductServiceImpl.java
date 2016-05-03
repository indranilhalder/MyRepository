/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplProductService;


/**
 * @author i313024
 *
 */
public class MplProductServiceImpl implements MplProductService
{

	private MplProductDao productDao;


	/**
	 * @return the productDao
	 */
	public MplProductDao getProductDao()
	{
		return productDao;
	}


	/**
	 * @param productDao
	 *           the productDao to set
	 */
	public void setProductDao(final MplProductDao productDao)
	{
		this.productDao = productDao;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplProductService#findProductFeaturesByQualifierAndProductCode
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public List<ProductFeatureModel> findProductFeaturesByQualifierAndProductCode(final String code, final String qualifier)
	{
		// YTODO Auto-generated method stub
		final List<ProductModel> productList = productDao.findProductsByCode(code);
		if (productList != null && productList.size() > 0)
		{
			return productDao.findProductFeaturesByCodeAndQualifier(productList.get(0).getPk().toString(), qualifier);
		}
		return null;
	}

}
