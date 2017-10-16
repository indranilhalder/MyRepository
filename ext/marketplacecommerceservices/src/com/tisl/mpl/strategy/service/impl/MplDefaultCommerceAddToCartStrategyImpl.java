/**
 *
 */
package com.tisl.mpl.strategy.service.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplStockDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplCommerceAddToCartStrategy;
import com.tisl.mpl.model.SellerInformationModel;




/**
 * @author TCS
 *
 */
public class MplDefaultCommerceAddToCartStrategyImpl extends DefaultCommerceAddToCartStrategy implements
		MplCommerceAddToCartStrategy
{

	private final String maxOrderQuantityConstant = "mpl.cart.maximumConfiguredQuantity.lineItem";
	protected static final Logger LOG = Logger.getLogger(MplDefaultCommerceAddToCartStrategyImpl.class);
	@Resource(name = "mplDeliveryCostService")
	private MplDeliveryCostService mplDeliveryCostService;
	@Resource(name = "exchangeGuideService")
	private ExchangeGuideService exchangeService;
	//@Resource(name = "mplCheckCartLevelStrategy")
	//private MplCheckCartLevelStrategy mplCheckCartLevelStrategy;
	@Autowired
	private MplStockDao mplStockDao;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;

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
			Boolean exchangeApplied = Boolean.FALSE;
			final long quantityToAdd = parameter.getQuantity();
			//final UnitModel unit = parameter.getUnit();
			final String ussid = parameter.getUssid();
			cartModel.setCheckUssid(ussid);
			final boolean forceNewEntry = parameter.isCreateNewEntry();
			final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();
			final CommerceCartModification localCommerceCartModification1;
			UnitModel orderableUnit = parameter.getUnit();
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

			//TISJEW-4496
			long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
					deliveryPointOfService, ussid);
			final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();


			final long cartLevel = checkCartLevel(productModel, cartModel, deliveryPointOfService);
			final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

			final long maxqtyExc = 1L;
			String existingExcId = null;
			if (StringUtils.isNotEmpty(parameter.getExchangeParam()) && cartLevelAfterQuantityChange > maxqtyExc)
			{

				final Long actualAllowedQuantityChangeExc = Long.valueOf(maxqtyExc - cartLevelAfterQuantityChange);
				final List<CartEntryModel> modelList = getCartService().getEntriesForProduct(cartModel, productModel);
				final Map<Integer, Long> updateMap = new HashMap<>();
				for (final CartEntryModel model : modelList)
				{
					if (model.getSelectedUSSID().equals(parameter.getUssid()))
					{
						updateMap.put(model.getEntryNumber(), actualAllowedQuantityChangeExc);
						if (StringUtils.isNotEmpty(model.getExchangeId()))
						{
							existingExcId = model.getExchangeId();
						}
					}
				}
				getCartService().updateQuantities(cartModel, updateMap);
				//TISJEW-4496
				//check again when the product qauntity is updated in cart while Exchange is applied
				actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
						deliveryPointOfService, ussid);

			}
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
					//setSellerInformationinCartEntry(cartEntryModel, productModel);
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


				//getModelService().save(cartEntryModel);
				//setSellerInformationinCartEntry(cartEntryModel, productModel);
				setSellerInformationinCartEntry(cartEntryModel, productModel.getSellerInformationRelator(),
						productModel.getProductCategoryType());

				//TPR-1083
				//Set Temporary Exchange ID

				if (StringUtils.isNotEmpty(parameter.getExchangeParam()))
				{
					if (StringUtil.isEmpty(existingExcId))
					{
						cartEntryModel.setExchangeId(parameter.getExchangeParam());
					}
					else
					{
						//set the anonymous cart exchange id
						cartEntryModel.setExchangeId(existingExcId);
						existingExcId = parameter.getExchangeParam();

					}
					exchangeApplied = Boolean.TRUE;

				}

				if (StringUtil.isNotEmpty(existingExcId))
				{
					exchangeService.removeFromTransactionTable(existingExcId, "Removed Due to Merge Cart", null);
				}

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
					if (exchangeApplied.booleanValue())
					{
						final CartModel updateCart = parameter.getCart();
						updateCart.setExchangeAppliedCart(exchangeApplied);
						getModelService().save(updateCart);
					}
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
	 * @Desc Adding replaced jwellery product to cart
	 *
	 * @param parameter
	 *
	 * @return CommerceCartModification
	 *
	 * @throws CommerceCartModificationException
	 */
	@Override
	public CommerceCartModification addToCartJwlReplacedItm(final CommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		CommerceCartModification modification = null;
		try
		{
			LOG.debug("addToCartJwlReplacedItm called");
			beforeAddToCart(parameter);
			validateAddToCart(parameter);

			final CartModel cartModel = parameter.getCart();
			final ProductModel productModel = parameter.getProduct();
			//Boolean exchangeApplied = Boolean.FALSE;
			final long quantityToAdd = parameter.getQuantity();
			//final UnitModel unit = parameter.getUnit();
			final String ussid = parameter.getUssid();
			cartModel.setCheckUssid(ussid);
			final boolean forceNewEntry = parameter.isCreateNewEntry();
			//final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();
			final CommerceCartModification localCommerceCartModification1;
			UnitModel orderableUnit = parameter.getUnit();
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

			//TISJEW-4496
			//long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
			//		deliveryPointOfService, ussid);
			//final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();


			//final long cartLevel = checkCartLevel(productModel, cartModel, deliveryPointOfService);
			//final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

			//final long maxqtyExc = 1L;
			//String existingExcId = null;
			//			if (StringUtils.isNotEmpty(parameter.getExchangeParam()) && cartLevelAfterQuantityChange > maxqtyExc)
			//			{
			//
			//				final Long actualAllowedQuantityChangeExc = Long.valueOf(maxqtyExc - cartLevelAfterQuantityChange);
			//				final List<CartEntryModel> modelList = getCartService().getEntriesForProduct(cartModel, productModel);
			//				final Map<Integer, Long> updateMap = new HashMap<>();
			//				for (final CartEntryModel model : modelList)
			//				{
			//					if (model.getSelectedUSSID().equals(parameter.getUssid()))
			//					{
			//						updateMap.put(model.getEntryNumber(), actualAllowedQuantityChangeExc);
			//						if (StringUtils.isNotEmpty(model.getExchangeId()))
			//						{
			//							existingExcId = model.getExchangeId();
			//						}
			//					}
			//				}
			//				getCartService().updateQuantities(cartModel, updateMap);
			//				//TISJEW-4496
			//				//check again when the product qauntity is updated in cart while Exchange is applied
			//				actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
			//						deliveryPointOfService, ussid);
			//
			//			}
			//if (actualAllowedQuantityChange > 0L)
			//{
			CartEntryModel cartEntryModel = null;
			//if (deliveryPointOfService == null)
			//{
			cartEntryModel = getCartService().addNewEntry(cartModel, productModel, quantityToAdd, orderableUnit, -1,
					!(forceNewEntry));
			if (ussid != null && !ussid.isEmpty())
			{
				cartEntryModel.setSelectedUSSID(ussid);
				//final List<MplZoneDeliveryModeValueModel> MplZoneDeliveryModeValueModel = getMplDeliveryCostService()
				//		.getDeliveryModesAndCost(MarketplacecommerceservicesConstants.INR, ussid);
				//cartEntryModel.setMplZoneDeliveryModeValue(MplZoneDeliveryModeValueModel);
			}
			//setSellerInformationinCartEntry(cartEntryModel, productModel);
			//}
			//				else
			//				{
			//					final Integer entryNumber = getEntryForProductAndPointOfService(cartModel, productModel, deliveryPointOfService);
			//
			//					cartEntryModel = getCartService().addNewEntry(cartModel, productModel, actualAllowedQuantityChange, orderableUnit,
			//							entryNumber.intValue(), entryNumber.intValue() >= 0);
			//
			//
			//					if (cartEntryModel != null)
			//					{
			//						cartEntryModel.setDeliveryPointOfService(deliveryPointOfService);
			//					}
			//
			//					if (ussid != null && !ussid.isEmpty())
			//					{
			//						cartEntryModel.setSelectedUSSID(ussid);
			//						final List<MplZoneDeliveryModeValueModel> MplZoneDeliveryModeValueModel = getMplDeliveryCostService()
			//								.getDeliveryModesAndCost(MarketplacecommerceservicesConstants.INR, ussid);
			//						cartEntryModel.setMplZoneDeliveryModeValue(MplZoneDeliveryModeValueModel);
			//					}
			//				}


			//getModelService().save(cartEntryModel);
			//setSellerInformationinCartEntry(cartEntryModel, productModel);
			setSellerInformationinCartEntry(cartEntryModel, productModel.getSellerInformationRelator(),
					productModel.getProductCategoryType());

			//TPR-1083
			//Set Temporary Exchange ID

			//				if (StringUtils.isNotEmpty(parameter.getExchangeParam()))
			//				{
			//					if (StringUtil.isEmpty(existingExcId))
			//					{
			//						cartEntryModel.setExchangeId(parameter.getExchangeParam());
			//					}
			//					else
			//					{
			//						//set the anonymous cart exchange id
			//						cartEntryModel.setExchangeId(existingExcId);
			//						existingExcId = parameter.getExchangeParam();
			//
			//					}
			//					exchangeApplied = Boolean.TRUE;
			//
			//				}

			//				if (StringUtil.isNotEmpty(existingExcId))
			//				{
			//					exchangeService.removeFromTransactionTable(existingExcId, "Removed Due to Merge Cart", null);
			//				}

			getCommerceCartCalculationStrategy().calculateCart(cartModel);
			getModelService().save(cartEntryModel);

			modification = new CommerceCartModification();
			modification.setQuantityAdded(quantityToAdd);
			modification.setQuantity(quantityToAdd);

			if (cartEntryModel != null)
			{
				modification.setEntry(cartEntryModel);
			}
			modification.setStatusCode(MarketplacecommerceservicesConstants.SUCCESS);

			//				if ((isMaxOrderQuantitySet(maxOrderQuantity)) && (actualAllowedQuantityChange < quantityToAdd)
			//						&& (cartLevelAfterQuantityChange == maxOrderQuantity.longValue()))
			//				{
			//					modification.setStatusCode(MarketplacecommerceservicesConstants.MAX_ORDER_QUANTITY_EXCEEDED);
			//				}
			//				else if (actualAllowedQuantityChange == quantityToAdd)
			//				{
			//modification.setStatusCode(MarketplacecommerceservicesConstants.SUCCESS);
			//					if (exchangeApplied.booleanValue())
			//					{
			//						final CartModel updateCart = parameter.getCart();
			//						updateCart.setExchangeAppliedCart(exchangeApplied);
			//						getModelService().save(updateCart);
			//					}
			//}
			//				else
			//				{
			//					modification.setStatusCode(MarketplacecommerceservicesConstants.LOWSTOCK);
			//				}

			localCommerceCartModification1 = modification;
			return localCommerceCartModification1;
			//}

			//			modification = new CommerceCartModification();
			//
			//			if ((isMaxOrderQuantitySet(maxOrderQuantity)) && (cartLevelAfterQuantityChange == maxOrderQuantity.longValue()))
			//			{
			//				modification.setStatusCode(MarketplacecommerceservicesConstants.MAX_ORDER_QUANTITY_EXCEEDED);
			//			}
			//			else
			//			{
			//				modification.setStatusCode(MarketplacecommerceservicesConstants.NOSTOCK);
			//			}
			//			modification.setQuantityAdded(0L);
			//			modification.setQuantity(quantityToAdd);
			//			final CartEntryModel entry = new CartEntryModel();
			//			entry.setProduct(productModel);
			//			entry.setDeliveryPointOfService(deliveryPointOfService);
			//			modification.setEntry(entry);
			//			return modification;
		}
		finally
		{
			afterAddToCart(parameter, modification);
		}
	}

	/**
	 * Adding Seller ID Details to Cart Entry
	 *
	 * @param cartEntryModel
	 * @param sellerCollection
	 * @return cartEntryModel
	 */
	private AbstractOrderEntryModel setSellerInformationinCartEntry(final CartEntryModel cartEntryModel,
			final Collection<SellerInformationModel> sellerCollection, final String categoryType)
	{
		//if (CollectionUtils.isNotEmpty(collection.getSellerInformationRelator()))

		final String cartEnrtyUssid = cartEntryModel.getSelectedUSSID();
		List<JewelleryInformationModel> jewelleryInfo = null;

		if ("FineJewellery".equalsIgnoreCase(categoryType))
		{
			jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(cartEnrtyUssid);
		}

		if (CollectionUtils.isNotEmpty(sellerCollection))
		{
			for (final SellerInformationModel sellerModel : sellerCollection)
			{
				final String sellerInfoUssid = sellerModel.getSellerArticleSKU();
				final String sellerName = sellerModel.getSellerName();

				if (CollectionUtils.isNotEmpty(jewelleryInfo))
				{
					if (StringUtils.isNotEmpty(jewelleryInfo.get(0).getPCMUSSID()) && StringUtils.isNotEmpty(sellerInfoUssid)
							&& StringUtils.equalsIgnoreCase(jewelleryInfo.get(0).getPCMUSSID(), sellerInfoUssid)
							&& StringUtils.isNotEmpty(sellerName))
					{
						cartEntryModel.setSellerInfo(sellerName);
						getModelService().save(cartEntryModel);
						break;
					}
				}

				if (StringUtils.isNotEmpty(cartEnrtyUssid) && StringUtils.isNotEmpty(sellerInfoUssid)
						&& StringUtils.equalsIgnoreCase(cartEnrtyUssid, sellerInfoUssid) && StringUtils.isNotEmpty(sellerName))
				{
					cartEntryModel.setSellerInfo(sellerName);
					getModelService().save(cartEntryModel);
					break;
				}
			}
		}
		return cartEntryModel;
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
		final long cartLevel = checkCartLevel(productModel, cartModel, pointOfServiceModel);
		final long stockLevel = getAvailableStockLevel(ussid);

		final long newTotalQuantity = cartLevel + quantityToAdd;

		final long newTotalQuantityAfterStockLimit = Math.min(newTotalQuantity, stockLevel);

		Integer maxOrderQuantity = productModel.getMaxOrderQuantity();

		if (!isMaxOrderQuantitySet(maxOrderQuantity))
		{
			// maxOrderQuantity = new Integer(maxOrderQuantityConstant); Critical Sonar fixes
			//maxOrderQuantity = Integer.valueOf(maxOrderQuantityConstant);

			maxOrderQuantity = Integer.valueOf(getConfigurationService().getConfiguration().getString(maxOrderQuantityConstant));
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
			final StockLevelModel stockLevelList = mplStockDao.getStockDetail(ussid);
			//if (stockLevelList != null && stockLevelList.size() > 0)
			if (null != stockLevelList)
			{
				availableStockLevel = Long.valueOf(stockLevelList.getAvailable());
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
	public String getMaxOrderQuantityConstant()
	{
		return maxOrderQuantityConstant;
	}

	/**
	 * @param productModel
	 * @param cartModel
	 * @param pointOfServiceModel
	 */
	@Override
	protected long checkCartLevel(final ProductModel productModel, final CartModel cartModel,
			final PointOfServiceModel pointOfServiceModel)
	{
		long cartLevel = 0L;
		final String ussid = cartModel.getCheckUssid();
		if (pointOfServiceModel == null)
		{
			for (final CartEntryModel entryModel : getCartService().getEntriesForProduct(cartModel, productModel))
			{
				if (entryModel.getDeliveryPointOfService() != null)
				{
					continue;
				}
				if (StringUtils.equalsIgnoreCase(entryModel.getSelectedUSSID(), ussid))
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
				if (StringUtils.equalsIgnoreCase(entryModel.getSelectedUSSID(), ussid))
				{
					cartLevel = ((entryModel.getQuantity() != null) ? entryModel.getQuantity().longValue() : 0L);
					break;
				}
			}
		}
		return cartLevel;
	}

}
