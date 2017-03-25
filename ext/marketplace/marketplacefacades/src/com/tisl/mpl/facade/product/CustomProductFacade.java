/**
 *
 */
package com.tisl.mpl.facade.product;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;


/**
 * @author TCS
 *
 */
public interface CustomProductFacade extends ProductFacade
{
	/**
	 * Gets the product data. The current session data (catalog versions, user) are used, so the valid product for the
	 * current session parameters will be returned. Use
	 * {@link ProductFacade#getProductForCodeAndOptions(String, Collection)} if you only have the code.
	 *
	 * @param productModel
	 *           the productModel
	 * @param options
	 *           options set that determines amount of information that will be attached to the returned product. If
	 *           empty or null default BASIC option is assumed
	 * @return the {@link ProductData}
	 */
	ProductData getProductForAjaxOptions(ProductModel productModel, Collection<ProductOption> options);
}
