/**
 *
 */
package com.tisl.mpl.facade.product;

import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;

import java.util.List;


/**
 * @author i313024
 *
 */
public interface MplProductFacade
{
	public ProductFeatureModel getProductFeatureModelByProductAndQualifier(ProductData product, String qualifier);

	public List<PointOfServiceData> storeLocatorPDP(final String pincode);

	public List<PointOfServiceData> storeLocatorFilterdPDP(final String pincode, final String sellerUssId);
}
