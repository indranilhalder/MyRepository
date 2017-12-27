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
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
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
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.jalo.PromotionVoucher;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.tata.model.MplTimeSlotsModel;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.AddressType;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.data.AddressTypeData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facade.product.ExchangeGuideFacade;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
//import com.tisl.mpl.fulfilmentprocess.events.OrderPlacedEvent;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.marketplacecommerceservices.event.OrderPlacedEvent;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCheckoutService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsItemEDDInfoData;
import com.tisl.mpl.promotion.service.SellerBasedPromotionService;
import com.tisl.mpl.shorturl.service.ShortUrlService;
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
	private MplAccountAddressFacade accountAddressFacade;

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
	@Autowired
	private MplCommerceCartService mplCommerceCartService;

	@Resource(name = "voucherModelService")
	private VoucherModelService voucherModelService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Autowired
	private DateUtilHelper dateUtilHelper;

	@Autowired
	private MplConfigFacade mplConfigFacade;

	@Resource
	private MplCheckoutFacade mplCheckoutFacade;

	private static final Logger LOG = Logger.getLogger(MplCheckoutFacadeImpl.class);

	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;

	@Autowired
	private MplCommerceCheckoutService mplCommerceCheckoutService;

	private OrderService orderService;

	@Resource(name = "sellerBasedPromotionService")
	private SellerBasedPromotionService sellerBasedPromotionService;

	//Exchange Changes
	@Resource(name = "exchangeGuideFacade")
	private ExchangeGuideFacade exchangeGuideFacade;

	@Autowired
	private ShortUrlService googleShortUrlService;

	/*
	 * @Resource(name = "stockPromoCheckService") private ExtStockLevelPromotionCheckService stockPromoCheckService;
	 */
	//TISPT-400
	@Autowired
	private Converter<CartModel, CartData> mplExtendedPromoCartConverter;


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
		String firstPriority = MarketplacecommerceservicesConstants.HOME;

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
					//TISSIT-2038
					isDefaultAddressSet = getMplCustomAddressFacade().setDeliveryAddress(addressData);
					//return (getAcceleratorCheckoutFacade().setDeliveryAddress(addressData)) ? true : false;

					//TISSIT-2038
					//isDefaultAddressSet = (getAcceleratorCheckoutFacade().setDeliveryAddress(addressData)) ? true : false;
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
			/*
			 * final Double subTotal = getCartService().getSessionCart().getSubtotal(); double finalDeliveryCost = 0.0D;
			 * //double finalDeliveryCost = cartData.getDeliveryCost().getValue().doubleValue(); final
			 * List<AbstractOrderEntryModel> cartEntryList = cartModel.getEntries(); for (final AbstractOrderEntryModel
			 * cartEntryModel : cartEntryList) { if (null != cartEntryModel &&
			 * cartEntryModel.getFulfillmentMode().equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIPCODE)) { if
			 * (cartEntryModel.getScheduledDeliveryCharge() != null &&
			 * cartEntryModel.getScheduledDeliveryCharge().doubleValue() > 0.0) { finalDeliveryCost += 0.0D; }
			 * cartEntryModel.setCurrDelCharge(Double.valueOf(finalDeliveryCost)); } else { if
			 * (cartData.getDeliveryCost().getValue().doubleValue() == 0.0) { for (final OrderEntryData cardEntryData :
			 * cartData.getEntries()) { if
			 * (cardEntryData.getSelectedUssid().equalsIgnoreCase(cartEntryModel.getSelectedUSSID())) { if (null !=
			 * cardEntryData.getMplDeliveryMode() && null != cardEntryData.getMplDeliveryMode().getDeliveryCost()) {
			 * finalDeliveryCost = cardEntryData.getMplDeliveryMode().getDeliveryCost().getDoubleValue() .doubleValue(); }
			 * } } } else { finalDeliveryCost = cartData.getDeliveryCost().getValue().doubleValue(); }
			 *
			 * cartEntryModel.setCurrDelCharge(Double.valueOf(finalDeliveryCost)); } } modelService.saveAll(cartEntryList);
			 * final Double totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() + finalDeliveryCost);
			 * cartModel.setTotalPrice(totalPriceAfterDeliveryCost);
			 * cartModel.setDeliveryCost(Double.valueOf(finalDeliveryCost)); getModelService().save(cartModel);
			 *
			 * //return true; calculateStatus = true;
			 */


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
			final Map<String, Map<String, Double>> deliveryCostPromotionMap, final CartModel cart) throws EtailNonBusinessExceptions
	{
		ServicesUtil.validateParameterNotNull(finalDeliveryCost, "finalDeliveryCost cannot be null");
		//TISEE-581
		// INC_12242
		CartModel cartModel = null;
		if (null == cart)
		{
			cartModel = getCart();
		}
		else
		{
			cartModel = cart;
		}

		Double totalPriceAfterDeliveryCost = Double.valueOf(0.0);
		Double discountValue = Double.valueOf(0.0);
		double curDeliveryCharge = 0.0d;
		//final CartData cartData = getMplExtendedCartConverter().convert(cartModel);
		final CartData cartData = mplExtendedPromoCartConverter.convert(cartModel); //TISPT-400
		boolean deliveryCostCalcStatus = false;
		final Double subTotal = cartModel.getSubtotal();
		if (cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)
		{
			discountValue = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());
		}
		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			curDeliveryCharge += entry.getCurrDelCharge().doubleValue();
		}
		totalPriceAfterDeliveryCost = Double.valueOf(subTotal.doubleValue() + curDeliveryCharge - discountValue.doubleValue());//tship charge change
		cartModel.setDeliveryCost(Double.valueOf(curDeliveryCharge));
		cartModel.setTotalPrice(totalPriceAfterDeliveryCost);

		cartModel.setTotalPriceWithConv(totalPriceAfterDeliveryCost);

		getModelService().save(cartModel);
		deliveryCostCalcStatus = true;
		return deliveryCostCalcStatus;
	}

	/**
	 * @return the accountAddressFacade
	 */
	public MplAccountAddressFacade getAccountAddressFacade()
	{
		return accountAddressFacade;
	}

	/**
	 * @param accountAddressFacade
	 *           the accountAddressFacade to set
	 */
	public void setAccountAddressFacade(final MplAccountAddressFacade accountAddressFacade)
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
					sellerOrderData.setPickupName(orderModel.getPickupPersonName());
					sellerOrderData.setPickupPhoneNumber(orderModel.getPickupPersonMobile());
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
				responseData = mplCartFacade.getOMSPincodeResponseData(defaultPinCodeId, cartData, null);
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
	public boolean isPromotionValid(final AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions //Parameter changed to abstractOrderModel for TPR-629
	{
		boolean result = true;
		if (abstractOrderModel != null)
		{

			final Set<PromotionResultModel> promotion = abstractOrderModel.getAllPromotionResults();
			//IQA changes
			if (CollectionUtils.isNotEmpty(promotion))
			{
				for (final PromotionResultModel promo : promotion)
				{
					if (promo.getCertainty().floatValue() == 1.0F)
					{

						//Changed to handle promotion for both cart and order
						if (promo.getPromotion() != null
								&& getSellerBasedPromotionService().getPromoDetails(promo.getPromotion().getCode()))
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
					//TPR-965 starts
					//					if (result && promo.getPromotion() instanceof LimitedStockPromotionModel)
					//					{
					//						final boolean payRestrictionsPresent = checkIfPaymentRestrictions(promo.getPromotion());
					//						int numRestrictions = 0;
					//						if (payRestrictionsPresent)
					//						{
					//							numRestrictions = promo.getPromotion().getRestrictions().size();
					//						}
					//						if (payRestrictionsPresent && numRestrictions == 1)
					//						{
					//							//do nothing
					//						}
					//
					//						//	result = checkIsStockPromoValid(promo.getPromotion(), abstractOrderModel, promo.getConsumedEntries());
					//					}
					//TPR-965 ends
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


	//	/**
	//	 * @param promotion
	//	 * @return
	//	 */
	//	private boolean checkIfPaymentRestrictions(final AbstractPromotionModel promotion)
	//	{
	//		boolean isPresent = false;
	//		for (final AbstractPromotionRestrictionModel restriction : promotion.getRestrictions())
	//		{
	//			if (restriction instanceof PaymentModeSpecificPromotionRestrictionModel)
	//			{
	//				isPresent = true;
	//				break;
	//			}
	//
	//		}
	//		return isPresent;
	//	}



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
		List<PinCodeResponseData> pincoderesponseDataList = null;
		pincoderesponseDataList = getSessionService()
				.getAttribute(MarketplacecommerceservicesConstants.PINCODE_RESPONSE_DATA_TO_SESSION);

		LOG.debug("******responceData******** " + pincoderesponseDataList);

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
					if (sellerInfoModel != null && CollectionUtils.isNotEmpty(sellerInfoModel.getRichAttribute())
							&& null != ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
							&& null != ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes()
							&& null != ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes()
									.getCode())
					{

						//	if (fulfillmentType.equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE))
						//	{
						//		marketplaceDeliveryModeData.setDeliveryCost(createPrice(getCartService().getSessionCart(),
						//									Double.valueOf(0.0)));
						//	}

						// For Release 1 , TShip delivery cost will always be zero . Hence , commneting the below code which check configuration from HAC
						if (null != pincoderesponseDataList && pincoderesponseDataList.size() > 0)
						{
							for (final PinCodeResponseData responseData : pincoderesponseDataList)
							{
								if (marketplaceDeliveryModeData.getSellerArticleSKU().equals(responseData.getUssid()))
								{
									for (final DeliveryDetailsData detailsData : responseData.getValidDeliveryModes())
									{
										if (null != detailsData.getFulfilmentType()
												&& detailsData.getFulfilmentType().equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE)
												&& cartData.getTotalPrice().getValue().doubleValue() > Double
														.parseDouble(tshipThresholdValue))

										{
											marketplaceDeliveryModeData
													.setDeliveryCost(createPrice(getCartService().getSessionCart(), Double.valueOf(0.0)));
										}
									}
								}
							}
						}
						/*
						 * if (fulfillmentType.equalsIgnoreCase(MarketplaceFacadesConstants.TSHIPCODE) &&
						 * cartData.getTotalPrice().getValue().doubleValue() > Double.parseDouble(tshipThresholdValue))
						 *
						 * {
						 *
						 * //******New Code Added for TPR-579 : TSHIP Shipping Charges****************** if
						 * (validate(fulfillmentType, marketplaceDeliveryModeData.getFulfillmentType())) {
						 * marketplaceDeliveryModeData.setDeliveryCost(createPrice(getCartService().getSessionCart(),
						 * marketplaceDeliveryModeData.getDeliveryCost().getDoubleValue())); } else {
						 * marketplaceDeliveryModeData.setDeliveryCost(createPrice(getCartService().getSessionCart(),
						 * Double.valueOf(0.0))); } //******************New Code Added for TPR-579 : TSHIP Shipping Charges
						 * ends*********** } }
						 *
						 * marketplaceDeliveryModeData.setDeliveryCost(createPrice(getCartService().getSessionCart(),
						 * Double.valueOf(0.0))); }
						 */
					}

				}
			}
		}
		return deliveryModeDataMap;
	}

	/**
	 * New Code Added for TPR-579: Matches Fulfillment Modes
	 *
	 * @param fulfillmentType
	 * @param fulfillmentTypeData
	 * @return flag
	 */
	//sonar fix
	/*
	 *
	 * private boolean validate(final String fulfillmentType, final String fulfillmentTypeData) { boolean flag = false;
	 * if (fulfillmentType.equalsIgnoreCase(fulfillmentTypeData)) { flag = true; } return flag; }
	 */


	@SuppressWarnings("javadoc")
	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.checkout.MplCheckoutFacade#placeOrder(java.lang.String)
	 */
	@Override
	public String placeOrderMobile(final CartModel cartModel) throws EtailNonBusinessExceptions
	{
		//final OrderData orderData = null;
		String orderCode = null;
		try
		{
			//final CartModel cartModel = getCommerceCartService().getCartForCodeAndUser(cartID, user);
			//now guid

			//CAR-110
			//final CartModel cartModel = getCommerceCartService().getCartForGuidAndSite(cartID, baseService.getCurrentBaseSite());
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
				//CAR-110
				/*
				 * if (orderModel != null) { orderData = getOrderConverter().convert(orderModel); }
				 */
				if (null != orderModel)
				{
					orderCode = orderModel.getCode();
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
		return orderCode;
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
	 * @param orderDetails
	 * @return string
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	//public String ordinalDate(final String orderCode) //TISPT-175 : Method call changed to stop multiple converter calling
	public String ordinalDate(final OrderData orderDetails)
	{
		//date format code
		//final OrderData orderDetails = getOrderDetailsForCode(orderCode); //TISPT-175 : Commented as orderdetails passed as parameter
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
				result = day + "th";
			}
			else
			{
				switch (day.intValue() % 10)
				{
					case 1:
						result = day + "st";
						//LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>date>>>>>>>>>>>>> : " + result);
						break;
					case 2:
						result = day + "nd";
						//LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>date>>>>>>>>>>>>> : " + result);
						break;
					case 3:
						result = day + "rd";
						//LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>date>>>>>>>>>>>>> : " + result);
						break;
					default:
						result = day + "th";
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
		//		String trackingUrl = getConfigurationServiceDetails().getConfiguration().getString(
		//				MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
		//				+ orderReferenceNumber;
		String trackingUrl = getConfigurationServiceDetails().getConfiguration()
				.getString(MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT);
		final String shortTrackingUrl = googleShortUrlService
				.genearateShortURL(order.getParentReference() == null ? order.getCode() : order.getParentReference().getCode());

		//print parent order number in the url
		trackingUrl = order.getParentReference() == null
				? (getConfigurationServiceDetails().getConfiguration()
						.getString(MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT) + "/" + order.getCode())
				: getConfigurationServiceDetails().getConfiguration()
						.getString(MarketplacecommerceservicesConstants.MPL_TRACK_ORDER_LONG_URL_FORMAT) + "/"
						+ order.getParentReference().getCode();
		if (order.getStatus().equals(OrderStatus.PAYMENT_SUCCESSFUL))
		{
			final OrderProcessModel orderProcessModel = new OrderProcessModel();
			orderProcessModel.setOrder(order);
			if (null != shortTrackingUrl)
			{
				orderProcessModel.setOrderTrackUrl(shortTrackingUrl);
			}
			else
			{
				orderProcessModel.setOrderTrackUrl(trackorderurl);
			}
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
								.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO,
										null == shortTrackingUrl ? trackingUrl : shortTrackingUrl),
						mobileNumber);

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
	public void saveDeliveryMethForFreebie(final AbstractOrderModel abstractOrderModel,
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, final Map<String, Long> freebieParentQtyMap)
			throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		getMplCommerceCartService().saveDeliveryMethForFreebie(abstractOrderModel, freebieModelMap, freebieParentQtyMap);
	}

	/*
	 * @Description to check coupon expired or not for Pay now
	 *
	 * @param cartData
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean isCouponValid(final AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions //Changed to abstractOrderModel for TPR-629
	{
		boolean result = false;

		final List<DiscountModel> voucherList = abstractOrderModel.getDiscounts();
		if (CollectionUtils.isNotEmpty(voucherList))
		{
			final PromotionVoucherModel voucher = (PromotionVoucherModel) voucherList.get(0);//Only one coupon would be applied in one order
			if (getVoucherModelService().isApplicable(voucher, abstractOrderModel)
					&& ((PromotionVoucher) getModelService().getSource(voucher)).isReservable(voucher.getVoucherCode(),
							(User) getModelService().getSource(abstractOrderModel.getUser())))
			{
				result = true;
			}
		}
		else
		{
			result = true;
		}

		return result;
	}



	/**
	 * This method returns orderData based on orderModel for TISPT-175. This method is replication of
	 * getOrderDetailsForCode(orderCode) but the only difference is it takes order model as parameter to minimize another
	 * db hit
	 *
	 * @param orderModel
	 * @return OrderData
	 */
	@Override
	public OrderData getOrderDetailsForCode(final OrderModel orderModel)
	{
		try
		{
			OrderData orderData = null;

			final PriceData deliveryCost = createPrice(orderModel,
					null == orderModel.getDeliveryCost() ? Double.valueOf(0.0) : orderModel.getDeliveryCost());
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
			if (CollectionUtils.isNotEmpty(orderModel.getEntries()))
			{
				for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
				{
					if (null == orderEntry.getProduct()) // it means somehow product is deleted from the order entry.
					{
						LOG.info("************************Skipping order history for order :" + orderModel.getCode() + " and for user: "
								+ orderModel.getUser().getName() + " **************************");
						return null;
					}

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
				final PriceData childDeliveryCost = createPrice(sellerOrder,
						null != sellerOrder.getDeliveryCost() ? sellerOrder.getDeliveryCost() : Double.valueOf(0.0));
				final OrderData sellerOrderData = getOrderConverter().convert(sellerOrder);
				//orderData.setDeliveryCost(childDeliveryCost);
				sellerOrderData.setDeliveryCost(childDeliveryCost);
				sellerOrderData.setPickupName(
						StringUtils.isNotEmpty(orderModel.getPickupPersonName()) ? orderModel.getPickupPersonName() : "");
				sellerOrderData.setPickupPhoneNumber(
						StringUtils.isNotEmpty(orderModel.getPickupPersonMobile()) ? orderModel.getPickupPersonMobile() : "");
				sellerOrderList.add(sellerOrderData);
			}
			orderData.setSellerOrderList(sellerOrderList);
			return orderData;
		}
		catch (final IllegalArgumentException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		//		catch (final NullPointerException ex)
		//		{
		//			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);		//Nullpointer exception not to be handled
		//		}
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
	 * This mrtjod triggers before submit order of hooks, ie. for order splitting TPR-629
	 */
	@Override
	public void beforeSubmitOrder(final OrderModel orderModel) throws InvalidCartException, CalculationException
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setSalesApplication(SalesApplication.WEB);

		final CommerceOrderResult result = new CommerceOrderResult();
		result.setOrder(orderModel);

		mplCommerceCheckoutService.beforeSubmitOrder(parameter, result);
		try
		{

			exchangeGuideFacade.getExchangeRequestID(orderModel);
		}
		catch (final Exception e)
		{
			LOG.error("Exchange Could Not be Applied" + e);
		}
	}

	/**
	 * INC144314180 PRDI-25 This method triggers before submit order of hooks for Mobile, ie. for order splitting TPR-629
	 */
	@Override
	public void beforeSubmitOrderMobile(final OrderModel orderModel) throws InvalidCartException, CalculationException
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setSalesApplication(SalesApplication.MOBILE);

		final CommerceOrderResult result = new CommerceOrderResult();
		result.setOrder(orderModel);

		mplCommerceCheckoutService.beforeSubmitOrder(parameter, result);
		try
		{
			exchangeGuideFacade.getExchangeRequestID(orderModel);
		}
		catch (final Exception e)
		{
			LOG.error("Exchange Could Not be Applied" + e);
		}
	}


	/**
	 * This method submits the order - ie. initiates the order fulfilment process TPR-629
	 *
	 * @param orderModel
	 */
	@Override
	public void submitOrder(final OrderModel orderModel)
	{
		getOrderService().submitOrder(orderModel);
	}

	/**
	 * This method overrides the OOTB placeorder method where it allows to pass the available sales application of the
	 * cartModel
	 *
	 * @param cartModel
	 */
	@Override
	public OrderModel placeOrder(final CartModel cartModel) throws InvalidCartException
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		// For TPR-5667 : setting salesapplication
		if (cartModel.getChannel() != null)
		{
			parameter.setSalesApplication(cartModel.getChannel());
		}
		else
		{
			parameter.setSalesApplication(SalesApplication.WEB);
		}

		final CommerceOrderResult commerceOrderResult = getCommerceCheckoutService().placeOrder(parameter);
		return commerceOrderResult.getOrder();
	}

	
	
	@Override
	public OrderModel placeEGVOrder(final CartModel cartModel) throws InvalidCartException
	{
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		// For TPR-5667 : setting salesapplication
		if (cartModel.getChannel() != null)
		{
			parameter.setSalesApplication(cartModel.getChannel());
		}
		else
		{
			parameter.setSalesApplication(SalesApplication.WEB);
		}

		final CommerceOrderResult commerceOrderResult = getCommerceCheckoutService().placeOrder(parameter);
		return commerceOrderResult.getOrder();
	}


	public VoucherModelService getVoucherModelService()
	{
		return voucherModelService;
	}


	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
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



	/**
	 * @return the mplCommerceCartService
	 */
	public MplCommerceCartService getMplCommerceCartService()
	{
		return mplCommerceCartService;
	}



	/**
	 * @param mplCommerceCartService
	 *           the mplCommerceCartService to set
	 */
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}

	/**
	 * @return the modelService
	 */
	@Override
	public ModelService getModelService()
	{
		return modelService;
	}




	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}



	/**
	 * @return the mplCustomAddressFacade
	 */
	public MplCustomAddressFacade getMplCustomAddressFacade()
	{
		return mplCustomAddressFacade;
	}



	/**
	 * @param mplCustomAddressFacade
	 *           the mplCustomAddressFacade to set
	 */
	public void setMplCustomAddressFacade(final MplCustomAddressFacade mplCustomAddressFacade)
	{
		this.mplCustomAddressFacade = mplCustomAddressFacade;
	}



	/**
	 * @return the orderService
	 */
	public OrderService getOrderService()
	{
		return orderService;
	}



	/**
	 * @param orderService
	 *           the orderService to set
	 */
	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}



	/**
	 * @return the sellerBasedPromotionService
	 */
	public SellerBasedPromotionService getSellerBasedPromotionService()
	{
		return sellerBasedPromotionService;
	}



	/**
	 * @param sellerBasedPromotionService
	 *           the sellerBasedPromotionService to set
	 */
	public void setSellerBasedPromotionService(final SellerBasedPromotionService sellerBasedPromotionService)
	{
		this.sellerBasedPromotionService = sellerBasedPromotionService;
	}



	/**
	 * @description: It is used for fetching order details for code ,without user checking
	 * @param orderNumber
	 * @return OrderData
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public OrderData getOrderDetailsForAnonymousUser(final String orderNumber)
	{
		try
		{
			LOG.debug("Searching for order number: " + orderNumber);
			final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
			final OrderModel orderModel = getCustomerAccountService().getOrderForCode(orderNumber, baseStoreModel);
			if (orderModel == null)
			{
				LOG.debug("Couldn't found order id DB for :" + orderNumber);
				throw new UnknownIdentifierException("Order with orderGUID " + orderNumber + " not found in current BaseStore");
			}
			return prepareOrderAndSubOrderData(orderModel);
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error("Error while searching for order :" + orderNumber);
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final NullPointerException ex)
		{
			LOG.error("Error while searching for order :" + orderNumber);
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception ex)
		{
			LOG.error("Error while searching for order :" + orderNumber);
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @param String
	 * @param CustomerModel
	 */
	@Override
	public OrderData getOrderDetailsForCockpitUser(final String code, final CustomerModel customerModel)
	{
		try
		{
			OrderData orderData = null;
			if (code != null)
			{
				final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
				final OrderModel orderModel = getCheckoutCustomerStrategy().isAnonymousCheckout()
						? getCustomerAccountService().getOrderDetailsForGUID(code, baseStoreModel)
						: getCustomerAccountService().getOrderForCode(customerModel, code, baseStoreModel);

				LOG.info("Step--1 ----- Order Codes For User " + orderModel.getCode());

				orderData = prepareOrderAndSubOrderData(orderModel);
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
	 * @description: This method is common method to set data
	 * @param orderModel
	 * @return OrderData
	 *
	 */
	private OrderData prepareOrderAndSubOrderData(final OrderModel orderModel)
	{

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

		final OrderData orderData = getOrderConverter().convert(orderModel);
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
			sellerOrderData.setPickupName(orderModel.getPickupPersonName());
			sellerOrderData.setPickupPhoneNumber(orderModel.getPickupPersonMobile());
			sellerOrderList.add(sellerOrderData);
		}
		orderData.setSellerOrderList(sellerOrderList);

		return orderData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.checkout.MplCheckoutFacade#getDateAndTimeslotMapList(java.util.List, java.util.List,
	 * java.lang.String, java.lang.String, de.hybris.platform.commercefacades.order.data.OrderEntryData,
	 * com.tisl.mpl.core.model.MplLPHolidaysModel)
	 */
	@Override
	public Map<String, List<String>> getDateAndTimeslotMapList(final List<MplTimeSlotsModel> modelList,
			List<String> calculatedDateList, final String deteWithOutTime, final String timeWithOutDate,
			final OrderEntryData cartEntryData, final MplLPHolidaysModel mplLPHolidaysModel)
	{
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		List<String> finalTimeSlotList = null;
		final Map<String, List<String>> dateTimeslotMapList = new LinkedHashMap<String, List<String>>();

		if (null != modelList)
		{
			Date startTime = null;
			Date endTIme = null;
			Date searchTime = null;
			final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			final List<MplTimeSlotsModel> timeList = new ArrayList<MplTimeSlotsModel>();

			for (final MplTimeSlotsModel mplTimeSlotsModel : modelList)
			{
				for (final String selectedDate : calculatedDateList)
				{
					if (selectedDate.equalsIgnoreCase(deteWithOutTime))
					{
						try
						{
							startTime = sdf.parse(mplTimeSlotsModel.getToTime());
							endTIme = sdf.parse(mplTimeSlotsModel.getFromTime());
							searchTime = sdf.parse(timeWithOutDate);
						}
						catch (final ParseException e)
						{
							LOG.error("Time Formater ********:" + e.getMessage());
						}
						if (startTime.compareTo(searchTime) > 0 && endTIme.compareTo(searchTime) > 0
								&& startTime.compareTo(searchTime) != 0 && endTIme.compareTo(searchTime) != 0)
						{
							try
							{
								LOG.debug("startDate:" + DateFormatUtils.format(startTime, "HH:mm") + "endDate:"
										+ DateFormatUtils.format(sdf.parse(mplTimeSlotsModel.getFromTime()), "HH:mm"));
								timeList.add(mplTimeSlotsModel);
							}
							catch (final ParseException e)
							{
								LOG.error("Exception occured while ParseException  :" + e);
							}

						}
					}
				}
			}

			LOG.debug("timeList.size()**************" + timeList.size());
			if (timeList.size() == 0)
			{
				final String nextDate = dateUtilHelper.getNextDete(deteWithOutTime, format);
				if (cartEntryData.getMplDeliveryMode().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
				{
					if (null != mplLPHolidaysModel && null != mplLPHolidaysModel.getWorkingDays())
					{
						calculatedDateList = dateUtilHelper.calculatedLpHolidays(mplLPHolidaysModel.getWorkingDays(), nextDate, 3);
					}
					else
					{
						calculatedDateList = dateUtilHelper.getDeteList(nextDate, format, 3);
					}
				}
				else if (cartEntryData.getMplDeliveryMode().getCode()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
				{
					if (null != mplLPHolidaysModel && null != mplLPHolidaysModel.getWorkingDays())
					{
						calculatedDateList = dateUtilHelper.calculatedLpHolidays(mplLPHolidaysModel.getWorkingDays(), nextDate, 2);
					}
					else
					{
						calculatedDateList = dateUtilHelper.getDeteList(nextDate, format, 2);
					}
				}

				timeList.addAll(modelList);
			}

			for (final String selectedDate : calculatedDateList)
			{

				if (selectedDate.equalsIgnoreCase(deteWithOutTime))
				{
					finalTimeSlotList = dateUtilHelper.convertFromAndToTimeSlots(timeList);
				}
				else
				{
					finalTimeSlotList = dateUtilHelper.convertFromAndToTimeSlots(modelList);
				}
				dateTimeslotMapList.put(selectedDate, finalTimeSlotList);
			}

		}

		return dateTimeslotMapList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.facade.checkout.MplCheckoutFacade#constructDeliverySlotsForEDAndHD(com.tisl.mpl.mplcommerceservices
	 * .service.data.InvReserForDeliverySlotsItemEDDInfoData,
	 * de.hybris.platform.commercefacades.order.data.OrderEntryData, com.tisl.mpl.core.model.MplLPHolidaysModel)
	 */
	@Override
	public void constructDeliverySlotsForEDAndHD(final InvReserForDeliverySlotsItemEDDInfoData deliverySlotsResponse,
			final OrderEntryData cartEntryData, final MplLPHolidaysModel mplLPHolidaysModel)
	{
		String estDeliveryDateAndTime = null;
		if (deliverySlotsResponse.getEDD() != null && StringUtils.isNotEmpty(deliverySlotsResponse.getEDD()))
		{
			estDeliveryDateAndTime = deliverySlotsResponse.getEDD();
		}
		else if (deliverySlotsResponse.getNextEDD() != null && StringUtils.isNotEmpty(deliverySlotsResponse.getNextEDD()))
		{
			estDeliveryDateAndTime = deliverySlotsResponse.getNextEDD();
		}
		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		final String deteWithOutTime = dateUtilHelper.getDateFromat(estDeliveryDateAndTime, format);
		final String timeWithOutDate = dateUtilHelper.getTimeFromat(estDeliveryDateAndTime);
		List<String> calculatedDateList = null;
		List<MplTimeSlotsModel> modelList = null;

		if (cartEntryData.getMplDeliveryMode().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
		{
			cartEntryData.setSelectedDeliveryModeForUssId(MarketplacecommerceservicesConstants.CART_HOME_DELIVERY);
			modelList = mplConfigFacade.getDeliveryTimeSlotByKey(MarketplacecommerceservicesConstants.DELIVERY_MODE_SD);

			if (null != mplLPHolidaysModel && null != mplLPHolidaysModel.getWorkingDays())
			{
				calculatedDateList = dateUtilHelper.calculatedLpHolidays(mplLPHolidaysModel.getWorkingDays(), deteWithOutTime, 3);
			}
			else
			{
				calculatedDateList = dateUtilHelper.getDeteList(deteWithOutTime, format, 3);
			}

		}
		else if (cartEntryData.getMplDeliveryMode().getCode()
				.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
		{
			cartEntryData.setSelectedDeliveryModeForUssId(MarketplacecommerceservicesConstants.CART_EXPRESS_DELIVERY);
			modelList = mplConfigFacade.getDeliveryTimeSlotByKey(MarketplacecommerceservicesConstants.ED);
			if (null != mplLPHolidaysModel && null != mplLPHolidaysModel.getWorkingDays())
			{
				calculatedDateList = dateUtilHelper.calculatedLpHolidays(mplLPHolidaysModel.getWorkingDays(), deteWithOutTime, 2);
			}
			else
			{
				calculatedDateList = dateUtilHelper.getDeteList(deteWithOutTime, format, 2);
			}
		}
		if (null != modelList)
		{
			final Map<String, List<String>> dateTimeslotMapList = mplCheckoutFacade.getDateAndTimeslotMapList(modelList,
					calculatedDateList, deteWithOutTime, timeWithOutDate, cartEntryData, mplLPHolidaysModel);
			cartEntryData.setDeliverySlotsTime(dateTimeslotMapList);
		}
		final List<String> dateList = new ArrayList<String>();
		if (null != cartEntryData.getDeliverySlotsTime() && cartEntryData.getDeliverySlotsTime().size() > 0)
		{
			for (final Entry<String, List<String>> entry : cartEntryData.getDeliverySlotsTime().entrySet())
			{
				dateList.add(entry.getKey());
			}
			if (dateList.size() > 0)
			{
				final CartModel cartModel = getCartService().getSessionCart();
				modelService.save(cartModel);
				modelService.refresh(cartModel);
				LOG.debug("Sdd Date Saved Successfully...");
			}
		}

	}

	@Override
	public void rePopulateDeliveryPointOfService(final Map deliveryPOSMap, final CartModel cartModel)
	{
		final Iterator it = deliveryPOSMap.entrySet().iterator();
		while (it.hasNext())
		{
			final Map.Entry pair = (Map.Entry) it.next();
			//	System.out.println(pair.getKey() + " = " + pair.getValue());

			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				if (pair.getKey().equals(entry.getSelectedUSSID()) && null != entry.getMplDeliveryMode().getDeliveryMode()
						&& entry.getMplDeliveryMode().getDeliveryMode().getCode().equals(MarketplaceFacadesConstants.CLICK_AND_COLLECT))
				{
					entry.setDeliveryPointOfService((PointOfServiceModel) pair.getValue());
					getModelService().save(entry);
				}
			}
		}
	}


	/**
	 * UF-281/282:used for reseting values inserted for SD
	 *
	 * @param cartEntryList
	 */
	@Override
	public void resetSlotEntries(final List<AbstractOrderEntryModel> cartEntryList)
	{
		boolean isSaveRequired = false;
		for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
		{
			if (null != cartEntryModel)
			{
				if (null != cartEntryModel.getEdScheduledDate() && StringUtils.isNotEmpty(cartEntryModel.getEdScheduledDate()))
				{
					isSaveRequired = true;
					cartEntryModel.setEdScheduledDate("".trim());
					cartEntryModel.setTimeSlotFrom("".trim());
					cartEntryModel.setTimeSlotTo("".trim());
					if (cartEntryModel.getScheduledDeliveryCharge() != null
							&& cartEntryModel.getScheduledDeliveryCharge().doubleValue() != 0.0)
					{
						cartEntryModel.setScheduledDeliveryCharge(Double.valueOf(0));
					}
				}
			}
		}
		if (isSaveRequired)
		{
			modelService.saveAll(cartEntryList);
		}
	}


}
