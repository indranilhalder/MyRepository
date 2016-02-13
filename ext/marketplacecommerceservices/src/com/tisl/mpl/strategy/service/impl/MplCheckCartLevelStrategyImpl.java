/**
 *
 */
package com.tisl.mpl.strategy.service.impl;

import de.hybris.platform.commerceservices.order.dao.CartEntryDao;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import com.tisl.mpl.strategy.service.MplCheckCartLevelStrategy;


/**
 * @author TCS
 *
 */

public class MplCheckCartLevelStrategyImpl implements MplCheckCartLevelStrategy
{
	private CartEntryDao cartEntryDao;
	private CartService cartService;

	@Override
	public long checkCartLevelValue(final String ussid, final ProductModel productModel, final CartModel cartModel,
			final PointOfServiceModel pointOfServiceModel)
	{
		long cartLevel = 0L;
		if (pointOfServiceModel == null)
		{
			for (final CartEntryModel entryModel : getCartService().getEntriesForProduct(cartModel, productModel))
			{
				if (entryModel.getDeliveryPointOfService() != null)
				{
					continue;
				}
				if (entryModel.getSelectedUSSID().equals(ussid))
				{
					cartLevel = ((entryModel.getQuantity() != null) ? entryModel.getQuantity().longValue() : 0L);
					break;
				}
			}

		}
		else
		{
			for (final CartEntryModel entryModel : getCartEntryDao().findEntriesByProductAndPointOfService(cartModel, productModel,
					pointOfServiceModel))
			{
				if (entryModel.getSelectedUSSID().equals(ussid))
				{
					cartLevel = ((entryModel.getQuantity() != null) ? entryModel.getQuantity().longValue() : 0L);
					break;
				}
				//cartLevel += ((entryModel.getQuantity() != null) ? entryModel.getQuantity().longValue() : 0L);
			}
		}
		return cartLevel;
	}

	/**
	 * @return the cartEntryDao
	 */
	public CartEntryDao getCartEntryDao()
	{
		return cartEntryDao;
	}

	/**
	 * @param cartEntryDao
	 *           the cartEntryDao to set
	 */
	public void setCartEntryDao(final CartEntryDao cartEntryDao)
	{
		this.cartEntryDao = cartEntryDao;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

}
