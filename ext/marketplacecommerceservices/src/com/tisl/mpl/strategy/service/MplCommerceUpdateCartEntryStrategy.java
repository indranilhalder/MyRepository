/**
 * 
 */
package com.tisl.mpl.strategy.service;

import de.hybris.platform.commerceservices.order.CommerceUpdateCartEntryStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;


/**
 * @author 890223
 * 
 */
public interface MplCommerceUpdateCartEntryStrategy extends CommerceUpdateCartEntryStrategy
{

	/**
	 * @param productModel
	 * @param pointOfServiceModel
	 * @param ussid
	 * @return
	 */
	long getAvailableStockLevel(ProductModel productModel, PointOfServiceModel pointOfServiceModel, String ussid);

	/**
	 * @param cartModel
	 * @param entryToUpdate
	 * @param quantityToAdd
	 * @param pointOfServiceModel
	 * @return
	 */
	long getAllowedCartAdjustmentForProduct(CartModel cartModel, AbstractOrderEntryModel entryToUpdate, long quantityToAdd,
			PointOfServiceModel pointOfServiceModel);



}
