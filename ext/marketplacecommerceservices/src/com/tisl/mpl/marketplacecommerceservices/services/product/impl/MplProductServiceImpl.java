package com.tisl.mpl.marketplacecommerceservices.services.product.impl;


import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.impl.DefaultProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao;
import com.tisl.mpl.marketplacecommerceservices.services.product.MplProductService;


/**
 * Default implementation of the {@link ProductService}.
 */

public class MplProductServiceImpl extends DefaultProductService implements MplProductService
{


	@Autowired
	private MplProductDao productDao;

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

}
