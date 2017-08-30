/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.product;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;

import java.util.List;


/**
 *
 * @author TCS
 *
 */
public interface MplProductDao extends ProductDao
{

	@Override
	public List<ProductModel> findProductsByCode(final String code);

	/**
	 * @param code
	 * @return List<ProductModel>
	 */
	List<ProductModel> findProductsByCodeNew(String code);

	//Changes for TISPRD-1631 Start
	/**
	 * @param code
	 * @return List<ProductModel>
	 * @description Get products based on selected catalog version in Backoffice
	 */

	List<ProductModel> findProductsByCodeHero(final String code);

	//Changes for TISPRD-1631 End

	List<ProductFeatureModel> findProductFeaturesByCodeAndQualifier(final String code, final String qualifier);

	/**
	 * @param catalogVersion
	 * @param productCodeList
	 * @return
	 */
	public List<ProductModel> findProductListByCodeList(CatalogVersionModel catalogVersion, List<String> productCodeList);

	/**
	 * @param code
	 * @return
	 */
	ProductModel findProductData(String code);

	List<ProductModel> findProductForHasVariant(String code);
}
