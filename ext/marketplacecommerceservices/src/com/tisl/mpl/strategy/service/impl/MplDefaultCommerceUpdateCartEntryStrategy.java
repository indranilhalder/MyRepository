/**
 *
 */
package com.tisl.mpl.strategy.service.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import com.tisl.mpl.marketplacecommerceservices.service.ExtCommerceStockService;
import com.tisl.mpl.strategy.service.MplCommerceUpdateCartEntryStrategy;


/**
 * @author TCS
 *
 */
public class MplDefaultCommerceUpdateCartEntryStrategy extends DefaultCommerceUpdateCartEntryStrategy implements
		MplCommerceUpdateCartEntryStrategy
{

	private final int maxOrderQuantityConstant = 10;//

	@Override
	public final CommerceCartModification updateQuantityForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		beforeUpdateCartEntry(parameters);
		final CartModel cartModel = parameters.getCart();
		final long newQuantity = parameters.getQuantity();
		final long entryNumber = parameters.getEntryNumber();

		ServicesUtil.validateParameterNotNull(cartModel, "Cart model cannot be null");


		final AbstractOrderEntryModel entryToUpdate = getEntryForNumber(cartModel, (int) entryNumber);
		validateEntryBeforeModification(newQuantity, entryToUpdate);
		final Integer maxOrderQuantity = entryToUpdate.getProduct().getMaxOrderQuantity();


		final long quantityToAdd = newQuantity - entryToUpdate.getQuantity().longValue();

		if (entryToUpdate.getDeliveryPointOfService() != null)


		{
			final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, entryToUpdate, quantityToAdd,
					entryToUpdate.getDeliveryPointOfService());

			final CommerceCartModification modification = modifyEntry(cartModel, entryToUpdate, actualAllowedQuantityChange,
					newQuantity, maxOrderQuantity);
			return modification;

		}

		final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, entryToUpdate, quantityToAdd, null);
		final CommerceCartModification modification = modifyEntry(cartModel, entryToUpdate, actualAllowedQuantityChange,
				newQuantity, maxOrderQuantity);
		afterUpdateCartEntry(parameters, modification);
		return modification;
	}



	@Override
	public long getAllowedCartAdjustmentForProduct(final CartModel cartModel, final AbstractOrderEntryModel entryToUpdate,
			final long quantityToAdd, final PointOfServiceModel pointOfServiceModel)
	{
		final ProductModel prd = entryToUpdate.getProduct();
		final long cartLevel = checkCartLevel(prd, cartModel, pointOfServiceModel);
		final long stockLevel = getAvailableStockLevel(prd, pointOfServiceModel, entryToUpdate.getSelectedUSSID());


		final long newTotalQuantity = cartLevel + quantityToAdd;


		final long newTotalQuantityAfterStockLimit = Math.min(newTotalQuantity, stockLevel);



		Integer maxOrderQuantity = prd.getMaxOrderQuantity();

		if (!isMaxOrderQuantitySet(maxOrderQuantity))
		{
			//maxOrderQuantity = new Integer(maxOrderQuantityConstant);  Avoid instantiating Integer objects. Call Integer.valueOf() instead.
			maxOrderQuantity = Integer.valueOf(maxOrderQuantityConstant);
		}
		final long newTotalQuantityAfterProductMaxOrder = Math.min(newTotalQuantityAfterStockLimit, maxOrderQuantity.longValue());
		return (newTotalQuantityAfterProductMaxOrder - cartLevel);
	}

	@Override
	public long getAvailableStockLevel(final ProductModel productModel, final PointOfServiceModel pointOfServiceModel,
			final String ussid)
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		if (!(getCommerceStockService().isStockSystemEnabled(baseStore)))
		{
			return getForceInStockMaxQuantity();


		}
		Long availableStockLevel = null;
		if (pointOfServiceModel == null)
		{
			availableStockLevel = getExtCommerceStockService().getStockLevelForProductAndBaseStore(ussid, baseStore);
		}
		else
		{
			/*
			 * availableStockLevel = getCommerceStockService().getStockLevelForProductAndPointOfService(productModel,
			 * pointOfServiceModel);
			 */

			availableStockLevel = getExtCommerceStockService().getStockLevelForProductAndPointOfService(ussid, pointOfServiceModel);
		}

		if (availableStockLevel == null)
		{
			return getForceInStockMaxQuantity();

		}

		return availableStockLevel.longValue();
	}

	protected ExtCommerceStockService getExtCommerceStockService()
	{

		return (ExtCommerceStockService) super.getCommerceStockService();
	}



}
