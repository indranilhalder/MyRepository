/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import javax.annotation.Resource;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.service.ExtCommerceStockService;
import com.tisl.mpl.strategy.service.MplCheckCartLevelStrategy;


/**
 * @author TCS
 * 
 */
public class ExtDefaultCommerceUpdateCartEntryStrategy extends DefaultCommerceUpdateCartEntryStrategy
{
	@Resource
	private ExtCommerceStockService commerceStockService;

	public static final String SUCCESS = "success";
	public static final String LOW_STOCK = "lowStock";
	private final int maxOrderQuantityConstant = 10;//
	@Resource(name = "mplCheckCartLevelStrategy")
	private MplCheckCartLevelStrategy mplCheckCartLevelStrategy;

	/**
	 * @return the commerceStockService
	 */
	@Override
	public ExtCommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	/**
	 * @param commerceStockService
	 *           the commerceStockService to set
	 */
	public void setCommerceStockService(final ExtCommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	/*
	 * @DESC Update Quantity For Cart
	 * 
	 * @param parameters
	 * 
	 * @return CommerceCartModification
	 * 
	 * @throws CommerceCartModificationException
	 */

	@Override
	public CommerceCartModification updateQuantityForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		beforeUpdateCartEntry(parameters);
		final CartModel cartModel = parameters.getCart();
		final long newQuantity = parameters.getQuantity();
		final long entryNumber = parameters.getEntryNumber();

		ServicesUtil.validateParameterNotNull(cartModel, MarketplacecommerceservicesConstants.CART_NULL);

		final AbstractOrderEntryModel entryToUpdate = getEntryForNumber(cartModel, (int) entryNumber);
		validateEntryBeforeModification(newQuantity, entryToUpdate);

		Integer maxOrderQuantity = null;

		if (entryToUpdate.getProduct() != null)
		{
			maxOrderQuantity = entryToUpdate.getProduct().getMaxOrderQuantity();
		}
		final long quantityToAdd = newQuantity - entryToUpdate.getQuantity().longValue();

		//commented by Techouts
		//As we do not maintain stock at commerce side

		/*
		 * if (entryToUpdate.getDeliveryPointOfService() != null) { final long actualAllowedQuantityChange =
		 * getAllowedCartAdjustmentForProduct(cartModel, entryToUpdate.getProduct(), quantityToAdd,
		 * entryToUpdate.getDeliveryPointOfService());
		 * 
		 * final CommerceCartModification modification = modifyEntry(cartModel, entryToUpdate,
		 * actualAllowedQuantityChange, newQuantity, maxOrderQuantity); return modification; }
		 */
		//Find stock level From USSID, Sent USSID as a parameter
		long actualAllowedQuantityChange = 0;
		CommerceCartModification modification = null;
		if (entryToUpdate.getProduct() != null)
		{
			actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, entryToUpdate.getProduct(), quantityToAdd,
					null, entryToUpdate.getSelectedUSSID());
			modification = modifyEntry(cartModel, entryToUpdate, actualAllowedQuantityChange, newQuantity, maxOrderQuantity);
		}
		else
		{
			modification = modifyEntry(cartModel, entryToUpdate, quantityToAdd, 0, maxOrderQuantity);
		}

		afterUpdateCartEntry(parameters, modification);
		return modification;
	}

	/*
	 * @DESC Get allowed cart adjustment for Product
	 * 
	 * @param cartModel
	 * 
	 * @param productModel
	 * 
	 * @param quantityToAdd
	 * 
	 * @param pointOfServiceModel
	 * 
	 * @param pointOussidfServiceModel
	 * 
	 * @return long
	 */
	public long getAllowedCartAdjustmentForProduct(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final PointOfServiceModel pointOfServiceModel, final String ussid)
	{
		//	final long cartLevel = checkCartLevel(productModel, cartModel, pointOfServiceModel);
		final long cartLevel = mplCheckCartLevelStrategy.checkCartLevelValue(ussid, productModel, cartModel, pointOfServiceModel);
		//Find stock level From USSID
		final long stockLevel = getAvailableStockLevel(ussid, pointOfServiceModel);

		final long newTotalQuantity = cartLevel + quantityToAdd;

		final long newTotalQuantityAfterStockLimit = Math.min(newTotalQuantity, stockLevel);


		Integer maxOrderQuantity = productModel.getMaxOrderQuantity();

		if (!isMaxOrderQuantitySet(maxOrderQuantity))
		{
			//maxOrderQuantity = new Integer(maxOrderQuantityConstant); Critical Sonar fixes
			maxOrderQuantity = Integer.valueOf(maxOrderQuantityConstant);
		}
		final long newTotalQuantityAfterProductMaxOrder = Math.min(newTotalQuantityAfterStockLimit, maxOrderQuantity.longValue());
		return (newTotalQuantityAfterProductMaxOrder - cartLevel);
	}

	/*
	 * @DESC Update For Cart-Update Quantity
	 * 
	 * @param parameters
	 * 
	 * @return CommerceCartModification
	 * 
	 * @throws CommerceCartModificationException
	 */

	@Override
	public CommerceCartModification updateToShippingModeForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = parameters.getCart();

		final AbstractOrderEntryModel entryToUpdate = getEntryForNumber(cartModel, (int) parameters.getEntryNumber());
		final CommerceCartModification modification = new CommerceCartModification();
		AbstractOrderEntryModel matchingShippingModeEntry = null;

		validateEntryBeforeSetShippingMode(entryToUpdate);

		for (final AbstractOrderEntryModel entryModel : cartModel.getEntries())
		{
			if ((entryModel.getDeliveryPointOfService() != null) || (!(entryToUpdate.getProduct().equals(entryModel.getProduct()))))
			{
				continue;
			}
			matchingShippingModeEntry = entryModel;
			break;
		}

		if (matchingShippingModeEntry != null)
		{
			final long quantityForMerge = matchingShippingModeEntry.getQuantity().intValue()
					+ entryToUpdate.getQuantity().intValue();
			getModelService().remove(entryToUpdate);

			final CommerceCartParameter updateQuantityParams = new CommerceCartParameter();
			updateQuantityParams.setEnableHooks(true);
			updateQuantityParams.setCart(cartModel);
			updateQuantityParams.setEntryNumber(matchingShippingModeEntry.getEntryNumber().longValue());
			updateQuantityParams.setQuantity(quantityForMerge);

			return updateQuantityForCartEntry(updateQuantityParams);
		}

		final long quantityBeforeChange = entryToUpdate.getQuantity().longValue();
		//Find stock level From USSID
		final long stockLevel = getAvailableStockLevel(entryToUpdate.getSelectedUSSID(), null);

		final long newTotalQuantityAfterStockLimit = Math.min(quantityBeforeChange, stockLevel);

		entryToUpdate.setDeliveryPointOfService(null);
		entryToUpdate.setQuantity(Long.valueOf(newTotalQuantityAfterStockLimit));
		getModelService().save(entryToUpdate);
		getModelService().refresh(cartModel);
		getCommerceCartCalculationStrategy().calculateCart(cartModel);

		if (quantityBeforeChange == newTotalQuantityAfterStockLimit)
		{
			modification.setStatusCode(SUCCESS);
		}
		else
		{
			modification.setStatusCode(LOW_STOCK);
		}
		modification.setQuantity(entryToUpdate.getQuantity().longValue());
		modification.setEntry(entryToUpdate);

		return modification;
	}

	/**
	 * @param selectedUSSID
	 * @param pointOfServiceModel
	 * @return
	 */
	private long getAvailableStockLevel(final String selectedUSSID, final PointOfServiceModel pointOfServiceModel)
	{
		final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		if (!(getCommerceStockService().isStockSystemEnabled(baseStore)))
		{
			return getForceInStockMaxQuantity();
		}
		Long availableStockLevel;
		if (pointOfServiceModel == null)
		{
			//Find stock level From USSID
			availableStockLevel = getCommerceStockService().getStockLevelForProductAndBaseStore(selectedUSSID, baseStore);
		}
		else
		{
			//Find stock level From USSID
			availableStockLevel = getCommerceStockService().getStockLevelForProductAndPointOfService(selectedUSSID,
					pointOfServiceModel);
		}

		if (availableStockLevel == null)
		{
			return getForceInStockMaxQuantity();
		}

		return availableStockLevel.longValue();
	}

	/*
	 * @DESC Update Point Of Service For Cart-Update Quantity
	 * 
	 * @param parameters
	 * 
	 * @return CommerceCartModification
	 * 
	 * @throws CommerceCartModificationException
	 */
	@Override
	public CommerceCartModification updatePointOfServiceForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = parameters.getCart();
		final PointOfServiceModel pointOfServiceModel = parameters.getPointOfService();
		ServicesUtil.validateParameterNotNull(cartModel, MarketplacecommerceservicesConstants.CART_NULL);
		ServicesUtil.validateParameterNotNull(pointOfServiceModel, MarketplacecommerceservicesConstants.POS_NULL);

		final CartEntryModel entryToUpdate = (CartEntryModel) getEntryForNumber(cartModel, (int) parameters.getEntryNumber());

		if (entryToUpdate == null)
		{
			throw new CommerceCartModificationException(MarketplacecommerceservicesConstants.UNKNOWN_ENTRY);
		}

		if (!(isOrderEntryUpdatable(entryToUpdate)))
		{
			throw new CommerceCartModificationException(MarketplacecommerceservicesConstants.ENTRY_NOT_UPDATEABLE);
		}

		final Integer entryNumberWithSamePosAndSKU = getEntryForProductAndPointOfService(cartModel, entryToUpdate.getProduct(),
				pointOfServiceModel);

		if ((entryNumberWithSamePosAndSKU.intValue() > -1)
				&& (entryNumberWithSamePosAndSKU.intValue() != entryToUpdate.getEntryNumber().intValue()))
		{
			final CartEntryModel entryWithSamePosAndSKU = (CartEntryModel) getEntryForNumber(cartModel,
					entryNumberWithSamePosAndSKU.intValue());
			final long quantityForMerge = entryWithSamePosAndSKU.getQuantity().intValue() + entryToUpdate.getQuantity().intValue();
			getModelService().remove(entryToUpdate);

			final CommerceCartParameter updateQuantityParams = new CommerceCartParameter();
			updateQuantityParams.setEnableHooks(true);
			updateQuantityParams.setCart(cartModel);
			updateQuantityParams.setEntryNumber(entryWithSamePosAndSKU.getEntryNumber().longValue());
			updateQuantityParams.setQuantity(quantityForMerge);

			return updateQuantityForCartEntry(updateQuantityParams);
		}

		final CommerceCartModification modification = new CommerceCartModification();
		entryToUpdate.setDeliveryPointOfService(pointOfServiceModel);
		getModelService().save(entryToUpdate);
		getModelService().refresh(cartModel);
		getCommerceCartCalculationStrategy().calculateCart(cartModel);
		getModelService().refresh(entryToUpdate);
		modification.setEntry(entryToUpdate);
		modification.setStatusCode(SUCCESS);
		return modification;
	}



}
