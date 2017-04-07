package com.tisl.mpl.marketplacecommerceservices.services.product;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
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

}