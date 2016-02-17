/**
 *
 */
package com.tisl.mpl.strategy.service;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


/**
 * @author TCS
 *
 */
public interface MplCheckCartLevelStrategy
{
	public long checkCartLevelValue(final String ussid, final ProductModel productModel, final CartModel cartModel,
			final PointOfServiceModel pointOfServiceModel);
}
