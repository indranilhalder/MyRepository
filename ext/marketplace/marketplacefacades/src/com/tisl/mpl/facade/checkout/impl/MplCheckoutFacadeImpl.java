/**
 *
 */
package com.tisl.mpl.facade.checkout.impl;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;
import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade.ExpressCheckoutResult;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.AddressType;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.data.AddressTypeData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
//import com.tisl.mpl.fulfilmentprocess.events.OrderPlacedEvent;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.marketplacecommerceservices.event.OrderPlacedEvent;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.sms.facades.SendSMSFacade;
import com.tisl.mpl.sns.push.service.impl.MplSNSMobilePushServiceImpl;
import com.tisl.mpl.wsdto.PushNotificationData;

import net.sourceforge.pmd.util.StringUtil;


/**
 * @author TCS
 *
 */
public class MplCheckoutFacadeImpl extends DefaultCheckoutFacade implements MplCheckoutFacade
{

	@Autowired
	private MplDeliveryCostService deliveryCostService;

	@Autowired
	private AccountAddressFacade accountAddressFacade;

	@Autowired
	private AcceleratorCheckoutFacade acceleratorCheckoutFacade;

	@Resource(name = "mplEnumerationHelper")
	private MplEnumerationHelper mplEnumerationHelper;

	//	@Autowired
	//	private ConfigurationService configurationService;

	@Autowired
	private MplCartFacade mplCartFacade;

	@Autowired
	private CartService cartService;


	@Autowired
	private CommerceCartService commerceCartService;

	@Autowired
	private ExtendedUserService extendedUserService;

	@Autowired
	private Converter<CartModel, CartData> mplExtendedCartConverter;

	@Autowired
	private SendSMSFacade sendSMSFacade;

	@Autowired
	private MplSNSMobilePushServiceImpl mplSNSMobilePushService;

	@Autowired
	private EventService eventService;

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Autowired
	private SessionService sessionService;

	private static final Logger LOG = Logger.getLogger(MplCheckoutFacadeImpl.class);


	/**
	 * @description: It is used to set delivery mode to cart
	 * @param deliveryModeCode
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean setMplDeliveryMode(final String deliveryModeCode) throws EtailNonBusinessExceptions
	{

		boolean saveDeliveryModeStatus = false;

		if (deliveryModeCode != null)
		{
			final CartModel cartModel = getCart();
			final DeliveryModeModel deliveryModeModel = getDeliveryService().getDeliveryModeForCode(deliveryModeCode);
			if (deliveryModeModel != null)
			{
				final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
				parameter.setEnableHooks(true);
				parameter.setCart(cartModel);
				parameter.setDeliveryMode(deliveryModeModel);
				saveDeliveryModeStatus = getCommerceCheckoutService().setDeliveryMode(parameter);
			}
		}
		return saveDeliveryModeStatus;
	}



	protected ConfigurationService getConfigurationServiceDetails()
	{
		return Registry.getApplicationContext().getBean(MarketplacecommerceservicesConstants.CONFIGURATION_SER,
				ConfigurationService.class);
	}

	/**
	 * @description: It is used to populate delivery address
	 * @param addressDataList
	 * @return List<AddressData>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<AddressData> rePopulateDeliveryAddress(final List<AddressData> addressDataList) throws EtailNonBusinessExceptions
	{
		ServicesUtil.validateParameterNotNull(addressDataList, "Address data list cannot be empty");
		final List<AddressData> addressDataNewList = new ArrayList<AddressData>();
		String defaultAddressId = "";
		String firstPriority = "Home";
		int count = 0;

		final Map<String, AddressData> sortedMap = new TreeMap<String, AddressData>(new Comparator<String>()
		{
			@Override
			public int compare(final String compareFrom, final String compareTwo)
			{
				return compareFrom.compareTo(compareTwo);
			}

		});

		for (final AddressData addressData : addressDataList)
		{
			if (addressData.isDefaultAddress())
			{
				firstPriority = addressData.getAddressType();
				defaultAddressId = addressData.getId();
				break;
			}
		}
		for (final AddressData addressData : addressDataList)
		{
			if (addressData.getAddressType().equalsIgnoreCase(firstPriority)
					&& addressData.getId().equalsIgnoreCase(defaultAddressId))
			{
				sortedMap.put("A", addressData);
			}
			else if (addressData.getAddressType().equalsIgnoreCase(firstPriority)
					&& !addressData.getId().equalsIgnoreCase(defaultAddressId))
			{
				sortedMap.put("B_" + count, addressData);
			}
			else
			{
				sortedMap.put("C_" + count, addressData);
			}
			count++;
		}

		final Iterator iterator = sortedMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			final Map.Entry pair = (Map.Entry) iterator.next();
			addressDataNewList.add((AddressData) pair.getValue());
		}

		return addressDataNewList;
	}

	/**
	 * @description: It is used for populating delivery mode and cost for sellerartickeSKU
	 * @param currency
	 * @param sellerartickeSKU
	 *
	 * @return List<MplZoneDeliveryModeValueModel>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<MplZoneDeliveryModeValueModel> populateDeliveryModesCostForUSSID(final String currency,
			final String sellerartickeSKU) throws EtailNonBusinessExceptions
	{
		ServicesUtil.validateParameterNotNull(currency, "currency cannot be null");
		ServicesUtil.validateParameterNotNull(sellerartickeSKU, "sellerartickeSKU cannot be null");
		return getDeliveryCostService().getDeliveryModesAndCost(currency, sellerartickeSKU);
	}

	/*
	 * @description: It is used for populating delivery code and cost for sellerartickeSKU
	 *
	 * @param deliveryCode
	 *
	 * @param currencyIsoCode
	 *
	 * @param sellerArticleSKU
	 *
	 * @return MplZoneDeliveryModeValueModel
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public MplZoneDeliveryModeValueModel populateDeliveryCostForUSSIDAndDeliveryMode(final String deliveryCode,
			final String currencyIsoCode, final String sellerArticleSKU) throws EtailNonBusinessExceptions
	{
		ServicesUtil.validateParameterNotNull(deliveryCode, "deliveryCode cannot be null");
		ServicesUtil.validateParameterNotNull(currencyIsoCode, "currencyIsoCode cannot be null");
		ServicesUtil.validateParameterNotNull(sellerArticleSKU, "sellerArticleSKU cannot be null");
		return getDeliveryCostService().getDeliveryCost(deliveryCode, currencyIsoCode, sellerArticleSKU);
	}

	/**
	 * @description method is called to get the type of Address
	 * @return Collection<AddressTypeData>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public Collection<AddressTypeData> getAddressType() throws EtailNonBusinessExceptions
	{
		final List<EnumerationValueModel> enumList = getMplEnumerationHelper().getEnumerationValuesForCode(AddressType._TYPECODE);

		final Collection<AddressTypeData> addressTypes = new ArrayList<>();
		for (final EnumerationValueModel enumerationValueModel : enumList)
		{
			final AddressTypeData addressTypeData = new AddressTypeData();
			addressTypeData.setCode(enumerationValueModel.getCode());
			addressTypeData.setName(enumerationValueModel.getCode());
			addressTypes.add(addressTypeData);
		}
		return addressTypes;
	}

	/*
	 * @description modified from DefaultAcceleratorCheckoutFacade performExpressCheckout : TIS 391
	 *
	 * @ Selected Address set for express checkout
	 *
	 * @param addressId
	 *
	 * @return ExpressCheckoutResult
	 *
	 * @throws EtailNonBusinessExceptions,Exception
	 */
	@Override
	public ExpressCheckoutResult performExpressCheckout(final String addressId) throws EtailNonBusinessExceptions
	{
		ServicesUtil.validateParameterNotNull(addressId, "addressId cannot be null");

		ExpressCheckoutResult expressCheckoutResult = null;

		if (isExpressCheckoutEnabledForStore())
		{
			if (hasShippingItems() && !setDefaultDeliveryAddressForCheckout(addressId))
			{
				expressCheckoutResult = ExpressCheckoutResult.ERROR_DELIVERY_ADDRESS;
			}
			else
			{
				expressCheckoutResult = ExpressCheckoutResult.SUCCESS;
			}
		}
		else
		{
			expressCheckoutResult = ExpressCheckoutResult.ERROR_NOT_AVAILABLE;
		}
		return expressCheckoutResult;
	}

	/*
	 * @Desc if express checkout is enabled for the store
	 *
	 * @return boolean
	 */

	private boolean isExpressCheckoutEnabledForStore()
	{
		boolean isExpressCheckoutEnabled = false;

		if (getBaseStoreService().getCurrentBaseStore() != null)
		{
			isExpressCheckoutEnabled = BooleanUtils.isTrue(getBaseStoreService().getCurrentBaseStore().getExpressCheckoutEnabled());
		}
		//return false;
		return isExpressCheckoutEnabled;
	}

	/*
	 * @description setting address for express checkout : TIS 391
	 *
	 * @param addressId
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions,Exception
	 */
	private boolean setDefaultDeliveryAddressForCheckout(final String addressId) throws EtailNonBusinessExceptions
	{
		ServicesUtil.validateParameterNotNull(addressId, "addressId cannot be null");

		boolean isDefaultAddressSet = false;

		if (checkIfCurrentUserIsTheCartUser())
		{
			for (final AddressData addressData : getAccountAddressFacade().getAddressBook())
			{
				if (null != addressData.getId() && addressData.getId().equals(addressId))
				{
					//return (getAcceleratorCheckoutFacade().setDeliveryAddress(addressData)) ? true : false;
					isDefaultAddressSet = (getAcceleratorCheckoutFacade().setDeliveryAddress(addressData)) ? true : false;
					break;
				}
			}
		}
		//return false;
		return isDefaultAddressSet;
	}

	/*
	 * @description Re calculating cart delivery cost: TIS 400
	 *
	 * @param addressId
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean reCalculateCart(final CartData cartData) throws EtailNonBusinessExceptions
	{
		ServicesUtil.validateParameterNotNull(cartData, "CartData cannot be null");
		ServicesUtil.validateParameterNotNull(cartData.getDeliveryCost(), "Delivery Cost cannot be null");

		final CartModel cartModel = getCart();
		boolean calculateStatus = false;

		if (cartModel != null)
		{
			final Double subTotal = getCartService().getSessionCart().getSubtotal();
			final Double finalDeliveryCost = Double.valueOf(cartData.getDeliveryCost().getValue().doubleValue());
			final Double totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() + finalDeliveryCost.doubleValue());
			cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
			cartModel.setDeliveryCost(finalDeliveryCost);
			getModelService().save(cartModel);

			//return true;
			calculateStatus = true;
		}
		//return false;
		return calculateStatus;
	}

	//	/*
	//	 * @description Storing delivery cost while navigating from Delivery mode to address selection : TIS 400
	//	 *
	//	 * @param finalDeliveryCost
	//	 *
	//	 * @param deliveryCostPromotionMap
	//	 *
	//	 * @return boolean
	//	 *
	//	 * @throws EtailNonBusinessExceptions
	//	 */
	//	@Override
	//	public boolean populateDeliveryCost(Double finalDeliveryCost, final Map<String, Map<String, Double>> deliveryCostPromotionMap)
	//			throws EtailNonBusinessExceptions
	//	{
	//		ServicesUtil.validateParameterNotNull(finalDeliveryCost, "finalDeliveryCost cannot be null");
	//
	//		final CartModel cartModel = getCart();
	//		boolean promotedValueApplied = false;
	//		Double totalPriceAfterDeliveryCost = Double.valueOf(0.0);
	//		Double finalPromotedDeliveryCost = Double.valueOf(0.0);
	//		Double discountValue = Double.valueOf(0.0);
	//		final CartData cartData = getMplExtendedCartConverter().convert(cartModel);
	//		boolean deliveryCostCalcStatus = false;
	//
	//
	//		if (cartModel == null)
	//		{
	//			LOG.debug("populateDeliveryCost : CartModel is null");
	//		}
	//		else
	//		{
	//			//TIS-384 Calculating delivery cost related promotion
	//			if (!deliveryCostPromotionMap.isEmpty())
	//			{
	//				for (final AbstractOrderEntryModel entry : cartModel.getEntries())
	//				{
	//					if (entry.getSelectedUSSID() != null && !entry.getGiveAway().booleanValue())
	//					{
	//						final MplZoneDeliveryModeValueModel entryDeliveryModel = entry.getMplDeliveryMode();
	//						final String ussid = entry.getSelectedUSSID();
	//						final Map<String, Double> promotedDeliveryCostMap = deliveryCostPromotionMap.get(ussid);
	//						boolean promotionApplicable = false;
	//
	//						if (promotedDeliveryCostMap != null && !promotedDeliveryCostMap.isEmpty())
	//						{
	//							final Double promotedValue = promotedDeliveryCostMap
	//									.get(MarketplacecommerceservicesConstants.DELIVERYCHARGE_PROMOTION_MAP_KEY);
	//
	//							if (promotedValue != null && promotedValue.doubleValue() >= 0)
	//							{
	//								promotedValueApplied = true;
	//								promotionApplicable = true;
	//								entry.setCurrDelCharge(promotedValue);
	//								getModelService().save(entry);
	//								finalPromotedDeliveryCost = Double.valueOf(finalPromotedDeliveryCost.doubleValue()
	//										+ promotedValue.doubleValue());
	//							}
	//						}
	//						if (!promotionApplicable)
	//						{
	//							finalPromotedDeliveryCost = Double.valueOf(finalPromotedDeliveryCost.doubleValue()
	//									+ entry.getQuantity().doubleValue() * entryDeliveryModel.getValue().doubleValue());
	//						}
	//					}
	//				}
	//			}
	//
	//			final Double subTotal = cartModel.getSubtotal();
	//			if (promotedValueApplied)
	//			{
	//				finalDeliveryCost = finalPromotedDeliveryCost;
	//			}
	//
	//			if (cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)
	//			{
	//				discountValue = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());
	//			}
	//
	//			totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() + finalDeliveryCost.doubleValue()
	//					- discountValue.doubleValue());
	//
	//			cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
	//			cartModel.setDeliveryCost(finalDeliveryCost);
	//			getModelService().save(cartModel);
	//			//return true;
	//			deliveryCostCalcStatus = true;
	//		}
	//
	//
	//		return deliveryCostCalcStatus;
	//	}

	/*
	 * @description Storing delivery cost while navigating from Delivery mode to address selection : TIS 400 TISEE-581
	 *
	 * @param finalDeliveryCost
	 *
	 * @param deliveryCostPromotionMap
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean populateDeliveryCost(final Double finalDeliveryCost,
			final Map<String, Map<String, Double>> deliveryCostPromotionMap) throws EtailNonBusinessExceptions
	{
		ServicesUtil.validateParameterNotNull(finalDeliveryCost, "finalDeliveryCost cannot be null");
		//TISEE-581
		final CartModel cartModel = getCart();
		Double totalPriceAfterDeliveryCost = Double.valueOf(0.0);
		Double discountValue = Double.valueOf(0.0);
		final CartData cartData = getMplExtendedCartConverter().convert(cartModel);
		boolean deliveryCostCalcStatus = false;
		final Double subTotal = cartModel.getSubtotal();
		if (cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)
		{
			discountValue = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());
		}
		totalPriceAfterDeliveryCost = Double
				.valueOf(subTotal.doubleValue() + finalDeliveryCost.doubleValue() - discountValue.doubleValue());

		cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
		cartModel.setDeliveryCost(finalDeliveryCost);
		getModelService().save(cartModel);
		deliveryCostCalcStatus = true;
		return deliveryCostCalcStatus;
	}

	/**
	 * @return the accountAddressFacade
	 */
	public AccountAddressFacade getAccountAddressFacade()
	{
		return accountAddressFacade;
	}

	/**
	 * @param accountAddressFacade
	 *           the accountAddressFacade to set
	 */
	public void setAccountAddressFacade(final AccountAddressFacade accountAddressFacade)
	{
		this.accountAddressFacade = accountAddressFacade;
	}

	/**
	 * @return the acceleratorCheckoutFacade
	 */
	public AcceleratorCheckoutFacade getAcceleratorCheckoutFacade()
	{
		return acceleratorCheckoutFacade;
	}

	/**
	 * @param acceleratorCheckoutFacade
	 *           the acceleratorCheckoutFacade to set
	 */
	public void setAcceleratorCheckoutFacade(final AcceleratorCheckoutFacade acceleratorCheckoutFacade)
	{
		this.acceleratorCheckoutFacade = acceleratorCheckoutFacade;
	}

	/**
	 * @return the mplEnumerationHelper
	 */
	public MplEnumerationHelper getMplEnumerationHelper()
	{
		return mplEnumerationHelper;
	}

	/**
	 * @param mplEnumerationHelper
	 *           the mplEnumerationHelper to set
	 */
	public void setMplEnumerationHelper(final MplEnumerationHelper mplEnumerationHelper)
	{
		this.mplEnumerationHelper = mplEnumerationHelper;
	}

	/**
	 * @description: It is used for fetching order details for code
	 * @param code
	 * @return OrderData
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public OrderData getOrderDetailsForCode(final String code)
	{
		try
		{
			OrderData orderData = null;
			if (code != null)
			{
				final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
				final OrderModel orderModel = getCheckoutCustomerStrategy().isAnonymousCheckout()
						? getCustomerAccountService().getOrderDetailsForGUID(code, baseStoreModel)
						: getCustomerAccountService().getOrderForCode((CustomerModel) getUserService().getCurrentUser(), code,
								baseStoreModel);


				LOG.info("Step--1 ----- Order Codes For User " + orderModel.getCode());

				/*
				 * if (orderModel == null) { throw new UnknownIdentifierException("Order with orderGUID " + code +
				 * " not found for current user in current BaseStore"); }
				 */


				final PriceData deliveryCost = createPrice(orderModel, orderModel.getDeliveryCost());
				//TISBOX-1417 Displaying COD value in order confirmation page
				PriceData convenienceCharge = null;
				PriceData totalPriceWithConvenienceCharge = null;
				if (orderModel.getConvenienceCharges() != null)
				{
					convenienceCharge = createPrice(orderModel, orderModel.getConvenienceCharges());
				}

				if (orderModel.getTotalPriceWithConv() != null)
				{
					totalPriceWithConvenienceCharge = createPrice(orderModel, orderModel.getTotalPriceWithConv());
				}


				//skip the order if product is missing in the order entries
				for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
				{
					if (null == orderEntry.getProduct()) // it means somehow product is deleted from the order entry.
					{
						LOG.info("************************Skipping order history for order :" + orderModel.getCode() + " and for user: "
								+ orderModel.getUser().getName() + " **************************");
						return null;
					}
				}

				orderData = getOrderConverter().convert(orderModel);
				orderData.setDeliveryCost(deliveryCost);

				if (convenienceCharge != null)
				{
					orderData.setConvenienceChargeForCOD(convenienceCharge);
				}

				if (totalPriceWithConvenienceCharge != null)
				{
					orderData.setTotalPriceWithConvCharge(totalPriceWithConvenienceCharge);
				}

				final List<OrderData> sellerOrderList = new ArrayList<OrderData>();
				for (final OrderModel sellerOrder : orderModel.getChildOrders())
				{
					final PriceData childDeliveryCost = createPrice(sellerOrder, sellerOrder.getDeliveryCost());
					final OrderData sellerOrderData = getOrderConverter().convert(sellerOrder);
					//orderData.setDeliveryCost(childDeliveryCost);
					sellerOrderData.setDeliveryCost(childDeliveryCost);
					sellerOrderList.add(sellerOrderData);
				}
				orderData.setSellerOrderList(sellerOrderList);
			}
			return orderData;
		}
		catch (final IllegalArgumentException ex)
		{

			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final NullPointerException ex)

		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description: It is creating price data for a price value
	 * @param source
	 * @param val
	 *
	 * @return PriceData
	 */
	@Override
	public PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}

		final CurrencyModel currency = source.getCurrency();
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}

	/*
	 * @Desc to check pincode inventory for Pay now TIS 414
	 *
	 * @param cartData
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public boolean isPincodeInventoryAvailable(final String defaultPinCodeId, final CartData cartData)
			throws EtailNonBusinessExceptions
	{
		boolean result = true;
		if (defaultPinCodeId != null && !defaultPinCodeId.isEmpty()
				&& (cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
		{
			List<PinCodeResponseData> responseData = null;

			if (!StringUtil.isEmpty(defaultPinCodeId))
			{
				responseData = mplCartFacade.getOMSPincodeResponseData(defaultPinCodeId, cartData);
			}
			if (responseData != null)
			{
				for (int i = 0; i < responseData.size(); i++)
				{
					if (responseData.get(i).getIsServicable().equalsIgnoreCase("N"))
					{
						LOG.debug("Pincode not servicable");
						//return false;
						result = false;
						break;
					}
				}
			}
			else
			{
				result = false;
				//return false;
			}

			if (result)
			{
				for (final OrderEntryData orderEntry : cartData.getEntries())
				{
					for (final PinCodeResponseData pinCodeResponseData : responseData)
					{
						if (orderEntry != null && orderEntry.getSelectedSellerInformation() != null
								&& orderEntry.getSelectedSellerInformation().getUssid() != null && pinCodeResponseData != null
								&& pinCodeResponseData.getUssid() != null && orderEntry.getQuantity() != null
								&& pinCodeResponseData.getStockCount() != null
								&& orderEntry.getSelectedSellerInformation().getUssid().equalsIgnoreCase(pinCodeResponseData.getUssid())
								&& orderEntry.getQuantity().longValue() > pinCodeResponseData.getStockCount().longValue())
						{
							LOG.debug("Inventory not available for ussid " + orderEntry.getSelectedSellerInformation().getUssid());
							result = false;
							break;
							//return false;
						}
					}
				}
			}
		}
		else
		{
			//return false;
			result = false;
		}
		return result;
	}

	/*
	 * @ Desc to check promotion expired or not for Pay now : TIS 414
	 *
	 * @param cartData
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean isPromotionValid(final CartModel cart) throws EtailNonBusinessExceptions
	{
		boolean result = true;
		if (cart != null)
		{
			final Set<PromotionResultModel> promotion = cart.getAllPromotionResults();
			if (null != promotion && !promotion.isEmpty())
			{
				for (final PromotionResultModel promo : promotion)
				{
					if (promo.getCertainty().floatValue() == 1.0F)
					{
						if (promo.getPromotion() != null && promo.getPromotion().getEnabled().booleanValue())
						{
							final java.util.Date endDate = promo.getPromotion().getEndDate();
							if (endDate.before(new Date()))
							{
								//return false;
								result = false;
								break;
							}
						}
						else
						{
							//return false;
							result = false;
							break;
						}
					}
				}
			}
		}
		else
		{
			//return false;
			result = false;
		}
		return result;
	}

	/**
	 * @ Override TSHIP : TIS 397
	 *
	 * @param deliveryModeDataMap
	 * @param cartData
	 * @return Map<String, List<MarketplaceDeliveryModeData>>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public Map<String, List<MarketplaceDeliveryModeData>> repopulateTshipDeliveryCost(
			final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap, final CartData cartData)
					throws EtailNonBusinessExceptions
	{

		if (!deliveryModeDataMap.isEmpty() && cartData != null)
		{
			String tshipThresholdValue = getConfigurationServiceDetails().getConfiguration()
					.getString(MarketplaceFacadesConstants.TSHIPTHRESHOLDVALUE);
			tshipThresholdValue = (StringUtils.isNotEmpty(tshipThresholdValue)) ? tshipThresholdValue : Integer.toString(0);

			final Iterator deliveryModeMapIterator = deliveryModeDataMap.entrySet().iterator();
			while (deliveryModeMapIterator.hasNext())
			{
				final Map.Entry deliveryEntry = (Map.Entry) deliveryModeMapIterator.next();
				final List<MarketplaceDeliveryModeData> list = (List<MarketplaceDeliveryModeData>) deliveryEntry.getValue();

				for (final MarketplaceDeliveryModeData marketplaceDeliveryModeData : list)
				{
					final SellerInformationModel sellerInfoModel = getMplSellerInformationService()
							.getSellerDetail(marketplaceDeliveryModeData.getSellerArticleSKU());
					if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null
							&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
					{
						final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
								.getDeliveryFulfillModes().getCode();

						//	if (fulfillmentType.equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE))
						//	{
						//		marketplaceDeliveryModeData.setDeliveryCost(createPrice(getCartService().getSessionCart(),
						//									Double.valueOf(0.0)));
						//	}

						// For Release 1 , TShip delivery cost will always be zero . Hence , commneting the below code which check configuration from HAC
						if (fulfillmentType.equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE)
								&& cartData.getTotalPrice().getValue().doubleValue() > Double.parseDouble(tshipThresholdValue))

						{
							marketplaceDeliveryModeData
									.setDeliveryCost(createPrice(getCartService().getSessionCart(), Double.valueOf(0.0)));
						}
					}
				}
			}
		}

		return deliveryModeDataMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.checkout.MplCheckoutFacade#placeOrder(java.lang.String)
	 */
	@Override
	public OrderData placeOrderByCartId(final String cartID, final String userId) throws EtailNonBusinessExceptions
	{
		final UserModel user = getExtendedUserService().getUserForOriginalUid(userId);

		if (null != user)
		{
			try
			{

				final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartID, user);
				/*
				 * CartModel cartModel = modelService.create(CartModel.class); cartModel.setCode(cartCode); cartModel =
				 * flexibleSearchService.getModelByExample(cartModel);
				 */


				if (null != cartModel && !getUserService().isAnonymousUser(cartModel.getUser()))
				{
					//For mobile
					cartModel.setChannel(SalesApplication.MOBILE);
					getModelService().save(cartModel);
					beforePlaceOrder(cartModel);

					final OrderModel orderModel = placeOrderWS(cartModel);

					//TISUT-1810
					afterPlaceOrderWS(cartModel, orderModel);


					// Convert the order to an order data
					if (orderModel != null)
					{
						return getOrderConverter().convert(orderModel);
					}
				}
			}
			catch (final InvalidCartException e)
			{
				throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			}
			catch (final EtailBusinessExceptions | EtailNonBusinessExceptions e)
			{
				throw e;
			}
			catch (final Exception ex)
			{
				// Error message for All Exceptions
				throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
			}

		}
		return null;

	}

	//TISUT-1810
	protected void afterPlaceOrderWS(final CartModel cartModel, final OrderModel orderModel)
	{
		if (orderModel != null && cartModel != null)
		{
			getModelService().remove(cartModel);
			getSessionService().removeAttribute("cart");
			getModelService().refresh(orderModel);
		}
	}


	//TISST-13685
	protected OrderModel placeOrderWS(final CartModel cartModel) throws InvalidCartException
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setSalesApplication(SalesApplication.MOBILE);

		final CommerceOrderResult commerceOrderResult = getCommerceCheckoutService().placeOrder(parameter);
		return commerceOrderResult.getOrder();
	}

	/**
	 * @description: It is used for converting date into ordinal date
	 * @param orderCode
	 * @return string
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public String ordinalDate(final String orderCode)
	{
		//date format code
		final OrderData orderDetails = getOrderDetailsForCode(orderCode);
		final Date sysDate = new Date();
		final Date orderDate = (orderDetails == null || orderDetails.getCreated() == null) ? sysDate : orderDetails.getCreated();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
		final String currentDate = (orderDate == null) ? null : dateFormat.format(orderDate);
		final Integer day = (currentDate == null) ? null : Integer.valueOf(currentDate);

		String result = null;

		if (day != null)
		{
			if (day.intValue() >= 11 && day.intValue() <= 13)
			{
				result = currentDate + "th";
			}
			else
			{
				switch (day.intValue() % 10)
				{
					case 1:
						result = currentDate + "st";
						//LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>date>>>>>>>>>>>>> : " + result);
						break;
					case 2:
						result = currentDate + "nd";
						//LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>date>>>>>>>>>>>>> : " + result);
						break;
					case 3:
						result = currentDate + "rd";
						//LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>date>>>>>>>>>>>>> : " + result);
						break;
					default:
						result = currentDate + "th";
						//LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>date>>>>>>>>>>>>> : " + result);
				}
			}
		}
		return result;

	}

	/**
	 * send mobile push notifications - Android and iOS
	 *
	 * @param orderDetails
	 */
	@Override
	public void sendMobileNotifications(final OrderData orderDetails)
	{
		try
		{
			String orderReferenceNumber = null;

			if (null != orderDetails.getCode() && !orderDetails.getCode().isEmpty())
			{
				orderReferenceNumber = orderDetails.getCode();
			}
			String uid = null;
			if (null != orderDetails.getUser() && null != orderDetails.getUser().getUid()
					&& !orderDetails.getUser().getUid().isEmpty())
			{
				uid = orderDetails.getUser().getUid();
			}
			PushNotificationData pushData = null;
			CustomerModel customer = getModelService().create(CustomerModel.class);
			if (null != uid && !uid.isEmpty())
			{
				customer = getMplSNSMobilePushService().getCustForUId(uid);
				if (null != customer && null != customer.getDeviceKey() && !customer.getDeviceKey().isEmpty())
				{
					pushData = new PushNotificationData();
					if (null != orderReferenceNumber)
					{
						pushData.setMessage(MarketplacecommerceservicesConstants.PUSH_MESSAGE_ORDER_PLACED
								.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, orderReferenceNumber));
						pushData.setOrderId(orderReferenceNumber);
					}
					if (null != customer.getOriginalUid() && !customer.getOriginalUid().isEmpty() && null != customer.getIsActive()
							&& !customer.getIsActive().isEmpty() && customer.getIsActive().equalsIgnoreCase("Y"))
					{
						getMplSNSMobilePushService().setUpNotification(customer.getOriginalUid(), pushData);
					}
				}
			}

		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.B9032);
		}
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.facade.checkout.MplCheckoutFacade#triggerEmailAndSmsOnOrderConfirmation(de.hybris.platform.core.model
	 * .order.OrderModel)
	 */
	@Override
	public void triggerEmailAndSmsOnOrderConfirmation(final OrderModel order, final OrderData orderDetails,
			final String trackorderurl)
	{
		final String mobileNumber = orderDetails.getDeliveryAddress().getPhone();
		final String firstName = orderDetails.getDeliveryAddress().getFirstName();
		final String orderReferenceNumber = orderDetails.getCode();
		final String trackingUrl = getConfigurationServiceDetails().getConfiguration()
				.getString(MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL) + orderReferenceNumber;

		if (order.getStatus().equals(OrderStatus.PAYMENT_SUCCESSFUL))
		{
			final OrderProcessModel orderProcessModel = new OrderProcessModel();
			orderProcessModel.setOrder(order);
			orderProcessModel.setOrderTrackUrl(trackorderurl);
			final OrderPlacedEvent orderplacedEvent = new OrderPlacedEvent(orderProcessModel);
			try
			{
				eventService.publishEvent(orderplacedEvent);
			}
			catch (final Exception e1)
			{ // YTODO
			  // Auto-generated catch block
				LOG.error("Exception during sending mail >> " + e1.getMessage());
			}

			try
			{
				getSendSMSFacade().sendSms(MarketplacecommerceservicesConstants.SMS_SENDER_ID,

				MarketplacecommerceservicesConstants.SMS_MESSAGE_ORDER_PLACED
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, firstName)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, orderReferenceNumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, trackingUrl), mobileNumber);

			}
			catch (final EtailNonBusinessExceptions ex)
			{
				LOG.error("EtailNonBusinessExceptions occured while sending sms " + ex);
			}
		}

	}

	/*
	 * @desc use to save freebie delivery mode
	 *
	 * @param cartModel
	 *
	 * @param freebieModelMap
	 *
	 * @param freebieParentQtyMap
	 *
	 * @return void
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void saveDeliveryMethForFreebie(final CartModel cartModel,
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, final Map<String, Long> freebieParentQtyMap)
					throws EtailNonBusinessExceptions
	{
		if (cartModel != null && cartModel.getEntries() != null && freebieModelMap != null && !freebieModelMap.isEmpty())
		{
			for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
			{
				if (cartEntryModel != null && cartEntryModel.getGiveAway().booleanValue()
						&& cartEntryModel.getAssociatedItems() != null && cartEntryModel.getAssociatedItems().size() > 0)
				{
					saveDeliveryMethForFreebie(cartEntryModel, freebieModelMap, freebieParentQtyMap);
				}
			}
		}
	}

	private void saveDeliveryMethForFreebie(final AbstractOrderEntryModel cartEntryModel,
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, final Map<String, Long> freebieParentQtyMap)
	{

		MplZoneDeliveryModeValueModel mplDeliveryMode = null;
		if (cartEntryModel.getAssociatedItems().size() == 1)
		{
			mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));
		}
		else if (cartEntryModel.getAssociatedItems().size() == 2
				&& freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0)).getDeliveryMode() != null
				&& freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1)).getDeliveryMode() != null
				&& freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0)).getDeliveryMode().getCode() != null)
		{
			if ((freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0)).getDeliveryMode().getCode())
					.equals((freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1)).getDeliveryMode().getCode())))
			{
				mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));
			}
			else if (freebieParentQtyMap.get(cartEntryModel.getAssociatedItems().get(0)).doubleValue() == freebieParentQtyMap
					.get(cartEntryModel.getAssociatedItems().get(1)).doubleValue())
			{
				if (freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0)).getDeliveryMode().getCode()
						.equalsIgnoreCase("home-delivery"))
				{
					mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));
				}
				else
				{
					mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1));
				}
			}
			else if (freebieParentQtyMap.get(cartEntryModel.getAssociatedItems().get(0)).doubleValue() > freebieParentQtyMap
					.get(cartEntryModel.getAssociatedItems().get(1)).doubleValue())
			{

				mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(1));

			}
			else
			{
				mplDeliveryMode = freebieModelMap.get(cartEntryModel.getAssociatedItems().get(0));
			}

		}
		else
		{
			LOG.debug("Unable to handle DeliveryMode as more than two Parent");
		}


		if (mplDeliveryMode != null)
		{
			//saving parent product delivery mode to freebie item
			cartEntryModel.setMplDeliveryMode(mplDeliveryMode);
			getModelService().save(cartEntryModel);
		}
	}

	/**
	 * @return the deliveryCostService
	 */
	public MplDeliveryCostService getDeliveryCostService()
	{
		return deliveryCostService;
	}



	/**
	 * @param deliveryCostService
	 *           the deliveryCostService to set
	 */
	public void setDeliveryCostService(final MplDeliveryCostService deliveryCostService)
	{
		this.deliveryCostService = deliveryCostService;
	}



	/**
	 * @return the mplCartFacade
	 */
	public MplCartFacade getMplCartFacade()
	{
		return mplCartFacade;
	}



	/**
	 * @param mplCartFacade
	 *           the mplCartFacade to set
	 */
	public void setMplCartFacade(final MplCartFacade mplCartFacade)
	{
		this.mplCartFacade = mplCartFacade;
	}



	/**
	 * @return the cartService
	 */
	@Override
	public CartService getCartService()
	{
		return cartService;
	}



	/**
	 * @param cartService
	 *           the cartService to set
	 */
	@Override
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the commerceCartService
	 */
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}



	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}



	/**
	 * @return the extendedUserService
	 */
	public ExtendedUserService getExtendedUserService()
	{
		return extendedUserService;
	}



	/**
	 * @param extendedUserService
	 *           the extendedUserService to set
	 */
	public void setExtendedUserService(final ExtendedUserService extendedUserService)
	{
		this.extendedUserService = extendedUserService;
	}



	/**
	 * @return the mplExtendedCartConverter
	 */
	public Converter<CartModel, CartData> getMplExtendedCartConverter()
	{
		return mplExtendedCartConverter;
	}



	/**
	 * @param mplExtendedCartConverter
	 *           the mplExtendedCartConverter to set
	 */
	public void setMplExtendedCartConverter(final Converter<CartModel, CartData> mplExtendedCartConverter)
	{
		this.mplExtendedCartConverter = mplExtendedCartConverter;
	}



	/**
	 * @return the sendSMSFacade
	 */
	public SendSMSFacade getSendSMSFacade()
	{
		return sendSMSFacade;
	}



	/**
	 * @param sendSMSFacade
	 *           the sendSMSFacade to set
	 */
	public void setSendSMSFacade(final SendSMSFacade sendSMSFacade)
	{
		this.sendSMSFacade = sendSMSFacade;
	}



	/**
	 * @return the mplSNSMobilePushService
	 */
	public MplSNSMobilePushServiceImpl getMplSNSMobilePushService()
	{
		return mplSNSMobilePushService;
	}



	/**
	 * @param mplSNSMobilePushService
	 *           the mplSNSMobilePushService to set
	 */
	public void setMplSNSMobilePushService(final MplSNSMobilePushServiceImpl mplSNSMobilePushService)
	{
		this.mplSNSMobilePushService = mplSNSMobilePushService;
	}



	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}



	/**
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}



	/**
	 * @param mplSellerInformationService
	 *           the mplSellerInformationService to set
	 */
	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}



	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}



	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}



}
