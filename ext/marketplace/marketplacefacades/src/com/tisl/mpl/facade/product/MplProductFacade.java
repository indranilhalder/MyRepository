/**
 *
 */
package com.tisl.mpl.facade.product;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.commercefacades.product.data.ProductData;


/**
 * @author i313024
 *
 */
public interface MplProductFacade
{
	public ProductFeatureModel getProductFeatureModelByProductAndQualifier(ProductData product, String qualifier);
}
