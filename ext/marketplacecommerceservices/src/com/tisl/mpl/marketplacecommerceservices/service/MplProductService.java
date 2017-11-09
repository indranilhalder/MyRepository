/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.catalog.model.ProductFeatureModel;

import java.util.List;


/**
 * @author i313024
 *
 */
public interface MplProductService
{
	List<ProductFeatureModel> findProductFeaturesByQualifierAndProductCode(String code, String qualifier);

	/**
	 * @param productCode
	 * @return
	 */
	String getSizeForSTWProduct(String productCode);

	/**
	 * @param productCode
	 * @return
	 */
	String getVariantsForSTWProducts(String productCode);
}
