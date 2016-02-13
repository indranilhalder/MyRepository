/**
 *
 */
package com.tisl.mpl.strategy.service.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.AbstractCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplStockDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplCommerceAddToCartStrategy;
import com.tisl.mpl.strategy.service.MplCheckCartLevelStrategy;




/**
 * @author TCS
 *
 */
public class MplDefaultCommerceAddToCartStrategyImpl extends AbstractCommerceAddToCartStrategy implements
		MplCommerceAddToCartStrategy
{

	private final int maxOrderQuantityConstant = 10;//
	protected static final Logger LOG = Logger.getLogger(MplDefaultCommerceAddToCartStrategyImpl.class);
	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;
	@Resource(name = "mplCheckCartLevelStrategy")
	private MplCheckCartLevelStrategy mplCheckCartLevelStrategy;
	@Autowired
	private MplStockDao mplStockDao;



	/*
	 * @Desc Adding product to cart
	 * 
	 * @param parameter
	 * 
	 * @return CommerceCartModification
	 * 
	 * @throws CommerceCartModificationException
	 */
	@Override
	public CommerceCartModification addToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException
	{
		CommerceCartModification modification = null;
		try
		{
			beforeAddToCart(parameter);
			validateAddToCart(parameter);

			final CartModel cartModel = parameter.getCart();
			final ProductModel productModel = parameter.getProduct();
			final long quantityToAdd = parameter.getQuantity();
			final UnitModel unit = parameter.getUnit();
			final String ussid = parameter.getUssid();
			cartModel.setCheckUssid(ussid);
			final boolean forceNewEntry = parameter.isCreateNewEntry();
			final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();
			CommerceCartModification localCommerceCartModification1;
			UnitModel orderableUnit = unit;
			if (orderableUnit == null)
			{
				try
				{
					orderableUnit = getProductService().getOrderableUnit(productModel);
				}
				catch (final ModelNotFoundException e)
				{
					throw new CommerceCartModificationException(e.getMessage(), e);
				}
			}

			final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
					deliveryPointOfService, ussid);
			final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();
			final long cartLevel = mplCheckCartLevelStrategy.checkCartLevelValue(ussid, productModel, cartModel,
					deliveryPointOfService);
			final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

			if (actualAllowedQuantityChange > 0L)
			{
				CartEntryModel cartEntryModel = null;
				if (deliveryPointOfService == null)
				{
					cartEntryModel = getCartService().addNewEntry(cartModel, productModel, actualAllowedQuantityChange, orderableUnit,
							-1, !(forceNewEntry));
					if (ussid != null && !ussid.isEmpty())
					{
						cartEntryModel.setSelectedUSSID(ussid);
						final List<MplZoneDeliveryModeValueModel> MplZoneDeliveryModeValueModel = getMplDeliveryCostService()
								.getDeliveryModesAndCost(MarketplacecommerceservicesConstants.INR, ussid);
						cartEntryModel.setMplZoneDeliveryModeValue(MplZoneDeliveryModeValueModel);
					}
				}
				else
				{
					final Integer entryNumber = getEntryForProductAndPointOfService(cartModel, productModel, deliveryPointOfService);

					cartEntryModel = getCartService().addNewEntry(cartModel, productModel, actualAllowedQuantityChange, orderableUnit,
							entryNumber.intValue(), entryNumber.intValue() >= 0);

					if (cartEntryModel != null)
					{
						cartEntryModel.setDeliveryPointOfService(deliveryPointOfService);
					}

					if (ussid != null && !ussid.isEmpty())
					{
						cartEntryModel.setSelectedUSSID(ussid);
						final List<MplZoneDeliveryModeValueModel> MplZoneDeliveryModeValueModel = getMplDeliveryCostService()
								.getDeliveryModesAndCost(MarketplacecommerceservicesConstants.INR, ussid);
						cartEntryModel.setMplZoneDeliveryModeValue(MplZoneDeliveryModeValueModel);
					}
				}

				getModelService().save(cartEntryModel);
				getCommerceCartCalculationStrategy().calculateCart(cartModel);
				getModelService().save(cartEntryModel);

				modification = new CommerceCartModification();
				modification.setQuantityAdded(actualAllowedQuantityChange);
				modification.setQuantity(quantityToAdd);

				if (cartEntryModel != null)
				{
					modification.setEntry(cartEntryModel);
				}

				if ((isMaxOrderQuantitySet(maxOrderQuantity)) && (actualAllowedQuantityChange < quantityToAdd)
						&& (cartLevelAfterQuantityChange == maxOrderQuantity.longValue()))
				{
					modification.setStatusCode(MarketplacecommerceservicesConstants.MAX_ORDER_QUANTITY_EXCEEDED);
				}
				else if (actualAllowedQuantityChange == quantityToAdd)
				{
					modification.setStatusCode(MarketplacecommerceservicesConstants.SUCCESS);
				}
				else
				{
					modification.setStatusCode(MarketplacecommerceservicesConstants.LOWSTOCK);
				}

				localCommerceCartModification1 = modification;
				return localCommerceCartModification1;
			}

			modification = new CommerceCartModification();

			if ((isMaxOrderQuantitySet(maxOrderQuantity)) && (cartLevelAfterQuantityChange == maxOrderQuantity.longValue()))
			{
				modification.setStatusCode(MarketplacecommerceservicesConstants.MAX_ORDER_QUANTITY_EXCEEDED);
			}
			else
			{
				modification.setStatusCode(MarketplacecommerceservicesConstants.NOSTOCK);
			}
			modification.setQuantityAdded(0L);
			modification.setQuantity(quantityToAdd);
			final CartEntryModel entry = new CartEntryModel();
			entry.setProduct(productModel);
			entry.setDeliveryPointOfService(deliveryPointOfService);
			modification.setEntry(entry);
			return modification;
		}
		finally
		{
			afterAddToCart(parameter, modification);
		}
	}

	/*
	 * @Desc Fetching eligible quantity for a ussid which can be added in cart
	 * 
	 * @param cartModel
	 * 
	 * @param productModel
	 * 
	 * @param quantityToAdd
	 * 
	 * @param pointOfServiceModel
	 * 
	 * @param ussid
	 * 
	 * @return long
	 * 
	 * @throws CommerceCartModificationException
	 */
	private long getAllowedCartAdjustmentForProduct(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final PointOfServiceModel pointOfServiceModel, final String ussid)
			throws CommerceCartModificationException
	{
		final long cartLevel = mplCheckCartLevelStrategy.checkCartLevelValue(ussid, productModel, cartModel, pointOfServiceModel);
		final long stockLevel = getAvailableStockLevel(ussid);

		final long newTotalQuantity = cartLevel + quantityToAdd;

		final long newTotalQuantityAfterStockLimit = Math.min(newTotalQuantity, stockLevel);

		Integer maxOrderQuantity = productModel.getMaxOrderQuantity();

		if (!isMaxOrderQuantitySet(maxOrderQuantity))
		{
			// maxOrderQuantity = new Integer(maxOrderQuantityConstant); Critical Sonar fixes
			maxOrderQuantity = Integer.valueOf(maxOrderQuantityConstant);
		}
		final long newTotalQuantityAfterProductMaxOrder = Math.min(newTotalQuantityAfterStockLimit, maxOrderQuantity.longValue());

		return (newTotalQuantityAfterProductMaxOrder - cartLevel);

	}

	/*
	 * @Desc Fetching available stock information for a ussid from Stock Level
	 * 
	 * @param ussid
	 * 
	 * @return long
	 * 
	 * @throws CommerceCartModificationException
	 */
	private long getAvailableStockLevel(final String ussid) throws CommerceCartModificationException
	{
		Long availableStockLevel = Long.valueOf(0);
		if (ussid != null)
		{
			final List<StockLevelModel> stockLevelList = mplStockDao.getStockDetail(ussid);
			//if (stockLevelList != null && stockLevelList.size() > 0)
			if (CollectionUtils.isNotEmpty(stockLevelList))
			{
				availableStockLevel = Long.valueOf(stockLevelList.get(0).getAvailable());
			}
		}
		else
		{
			LOG.debug("USSID is null in getAvailableStockLevel");
		}
		return availableStockLevel.longValue();
	}

	/**
	 * @return the mplDeliveryCostService
	 */
	public MplDeliveryCostService getMplDeliveryCostService()
	{
		return mplDeliveryCostService;
	}

	/**
	 * @param mplDeliveryCostService
	 *           the mplDeliveryCostService to set
	 */
	public void setMplDeliveryCostService(final MplDeliveryCostService mplDeliveryCostService)
	{
		this.mplDeliveryCostService = mplDeliveryCostService;
	}

	/**
	 * @return the mplStockDao
	 */
	public MplStockDao getMplStockDao()
	{
		return mplStockDao;
	}

	/**
	 * @param mplStockDao
	 *           the mplStockDao to set
	 */
	public void setMplStockDao(final MplStockDao mplStockDao)
	{
		this.mplStockDao = mplStockDao;
	}

	/**
	 * @return the maxOrderQuantityConstant
	 */
	public int getMaxOrderQuantityConstant()
	{
		return maxOrderQuantityConstant;
	}
}
