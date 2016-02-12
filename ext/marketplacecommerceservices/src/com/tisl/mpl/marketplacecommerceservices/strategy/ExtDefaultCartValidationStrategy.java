/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.strategies.impl.DefaultCartValidationStrategy;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.service.ExtCommerceStockService;


/**
 * @author TCS
 *
 */
public class ExtDefaultCartValidationStrategy extends DefaultCartValidationStrategy
{
	private ModelService modelService;
	@Resource
	private CartService cartService;
	@Resource
	private ProductService productService;
	@Resource
	private ExtCommerceStockService commerceStockService;
	@Resource
	private BaseStoreService baseStoreService;
	@Resource
	private UserService userService;

	@Override
	public CommerceCartModification validateCartEntry(final CartModel cartModel, final CartEntryModel cartEntryModel)
	{
		try
		{
			getProductService().getProductForCode(cartEntryModel.getProduct().getCode());
		}
		catch (final UnknownIdentifierException localUnknownIdentifierException)
		{
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode("unavailable");
			modification.setQuantityAdded(0L);
			modification.setQuantity(0L);

			final CartEntryModel entry = new CartEntryModel();
			entry.setProduct(cartEntryModel.getProduct());

			modification.setEntry(entry);

			getModelService().remove(cartEntryModel);
			getModelService().refresh(cartModel);

			return modification;
		}

		final Long stockLevel = getStockLevel(cartEntryModel);

		final long cartLevel = getCartLevel(cartEntryModel, cartModel);

		final long cartEntryLevel = cartEntryModel.getQuantity().longValue();

		Long stockLevelForProductInBaseStore = null;
		long newOrderEntryLevel = 0;
		if (stockLevel != null)
		{
			if (isProductNotAvailableInPOS(cartEntryModel, stockLevel))
			{
				stockLevelForProductInBaseStore = getCommerceStockService().getStockLevelForProductAndBaseStore(
						cartEntryModel.getSelectedUSSID(), getBaseStoreService().getCurrentBaseStore());
				if (stockLevelForProductInBaseStore != null)
				{
					newOrderEntryLevel = Math.min(cartEntryLevel, stockLevelForProductInBaseStore.longValue());
				}
				else
				{
					newOrderEntryLevel = Math.min(cartEntryLevel, cartLevel);
				}

			}
			else
			{
				newOrderEntryLevel = Math.min(cartEntryLevel, stockLevel.longValue());
			}

		}
		else
		{
			newOrderEntryLevel = Math.min(cartEntryLevel, cartLevel);
		}

		if ((stockLevelForProductInBaseStore != null) && (stockLevelForProductInBaseStore.longValue() != 0L))
		{
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode("movedFromPOSToStore");
			final CartEntryModel existingEntryForProduct = getExistingShipCartEntryForProduct(cartModel, cartEntryModel.getProduct());
			if (existingEntryForProduct != null)
			{
				getModelService().remove(cartEntryModel);
				final long quantityAdded = (stockLevelForProductInBaseStore.longValue() >= cartLevel) ? newOrderEntryLevel
						: cartLevel - stockLevelForProductInBaseStore.longValue();
				modification.setQuantityAdded(quantityAdded);
				final long updatedQuantity = (stockLevelForProductInBaseStore.longValue() <= cartLevel) ? stockLevelForProductInBaseStore
						.longValue() : cartLevel;
				modification.setQuantity(updatedQuantity);
				existingEntryForProduct.setQuantity(Long.valueOf(updatedQuantity));
				getModelService().save(existingEntryForProduct);
				modification.setEntry(existingEntryForProduct);
			}
			else
			{
				modification.setQuantityAdded(newOrderEntryLevel);
				modification.setQuantity(cartEntryLevel);
				cartEntryModel.setDeliveryPointOfService(null);
				modification.setEntry(cartEntryModel);
				getModelService().save(cartEntryModel);
			}

			getModelService().refresh(cartModel);

			return modification;
		}
		if (((stockLevel != null) && (stockLevel.longValue() <= 0L)) || (newOrderEntryLevel < 0L))
		{
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode("noStock");
			modification.setQuantityAdded(0L);
			modification.setQuantity(cartEntryLevel);
			final CartEntryModel entry = new CartEntryModel();
			entry.setProduct(cartEntryModel.getProduct());
			modification.setEntry(entry);
			getModelService().remove(cartEntryModel);
			getModelService().refresh(cartModel);

			return modification;
		}
		if (cartEntryLevel != newOrderEntryLevel)
		{
			final CommerceCartModification modification = new CommerceCartModification();
			modification.setStatusCode("lowStock");
			modification.setQuantityAdded(newOrderEntryLevel);
			modification.setQuantity(cartEntryLevel);
			modification.setEntry(cartEntryModel);
			cartEntryModel.setQuantity(Long.valueOf(newOrderEntryLevel));
			getModelService().save(cartEntryModel);
			getModelService().refresh(cartModel);

			return modification;
		}

		final CommerceCartModification modification = new CommerceCartModification();
		modification.setStatusCode("success");
		modification.setQuantityAdded(cartEntryLevel);
		modification.setQuantity(cartEntryLevel);
		modification.setEntry(cartEntryModel);

		return modification;
	}

	@Override
	public Long getStockLevel(final CartEntryModel cartEntryModel)
	{
		final PointOfServiceModel pointOfService = cartEntryModel.getDeliveryPointOfService();

		if (hasPointOfService(cartEntryModel))
		{
			return getCommerceStockService().getStockLevelForProductAndPointOfService(cartEntryModel.getSelectedUSSID(),
					pointOfService);
		}

		return getCommerceStockService().getStockLevelForProductAndBaseStore(cartEntryModel.getSelectedUSSID(),
				getBaseStoreService().getCurrentBaseStore());
	}

	@Override
	public ModelService getModelService()
	{
		return this.modelService;
	}

	@Override
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Override
	public CartService getCartService()
	{
		return this.cartService;
	}

	@Override
	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Override
	public ProductService getProductService()
	{
		return this.productService;
	}

	@Override
	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	@Override
	public ExtCommerceStockService getCommerceStockService()
	{
		return this.commerceStockService;
	}

	@Required
	public void setCommerceStockService(final ExtCommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	@Override
	public BaseStoreService getBaseStoreService()
	{
		return this.baseStoreService;
	}

	@Override
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Override
	protected UserService getUserService()
	{
		return this.userService;
	}

	@Override
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

}
