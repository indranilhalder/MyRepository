package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CNCServiceableSlavesData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.product.data.ServiceableSlavesData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.OrderManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.enums.ClickAndCollectEnum;
import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.enums.ExpressDeliveryEnum;
import com.tisl.mpl.core.enums.HomeDeliveryEnum;
import com.tisl.mpl.core.enums.PaymentModesEnum;
import com.tisl.mpl.core.model.BrandModel;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.PcmProductVariantModel;
import com.tisl.mpl.core.model.PincodeServiceabilityDataModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.ATSResponseData;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.MplCustomerProfileData;
import com.tisl.mpl.globalcodes.utilities.MplCodeMasterUtility;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplCommerceCartDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPincodeServiceDao;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.ExchangeGuideService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplCustomerProfileService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeRestrictionService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.MplStockService;
import com.tisl.mpl.marketplacecommerceservices.strategy.ExtDefaultCommerceUpdateCartEntryStrategy;
import com.tisl.mpl.marketplacecommerceservices.strategy.MplCommerceAddToCartStrategy;
import com.tisl.mpl.model.BuyXItemsofproductAgetproductBforfreeModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.StateModel;
import com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsItemEDDInfoData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsResponseData;
import com.tisl.mpl.service.InventoryReservationService;
import com.tisl.mpl.service.PinCodeDeliveryModeService;
import com.tisl.mpl.strategy.service.impl.MplDefaultCommerceAddToCartStrategyImpl;
import com.tisl.mpl.util.CatalogUtils;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.CNCServiceableSlavesWsDTO;
import com.tisl.mpl.wsdto.DeliveryModeResOMSWsDto;
import com.tisl.mpl.wsdto.EDDInfoWsDTO;
import com.tisl.mpl.wsdto.EDDResponseWsDTO;
import com.tisl.mpl.wsdto.GetWishListDataWsDTO;
import com.tisl.mpl.wsdto.GetWishListProductWsDTO;
import com.tisl.mpl.wsdto.GetWishListWsDTO;
import com.tisl.mpl.wsdto.InventoryReservJewelleryRequest;
import com.tisl.mpl.wsdto.InventoryReservListRequest;
import com.tisl.mpl.wsdto.InventoryReservListRequestWsDTO;
import com.tisl.mpl.wsdto.InventoryReservListResponse;
import com.tisl.mpl.wsdto.InventoryReservRequest;
import com.tisl.mpl.wsdto.InventoryReservRequestWsDTO;
import com.tisl.mpl.wsdto.InventoryReservResponse;
import com.tisl.mpl.wsdto.MobdeliveryModeWsDTO;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeListResponse;
import com.tisl.mpl.wsdto.PinCodeDeliveryModeResponse;
import com.tisl.mpl.wsdto.ReservationItemWsDTO;
import com.tisl.mpl.wsdto.ReservationListWsDTO;
import com.tisl.mpl.wsdto.ServiceableSlavesDTO;
import com.tisl.mpl.wsdto.StoreLocatorAtsResponse;
import com.tisl.mpl.wsdto.StoreLocatorAtsResponseObject;
import com.tisl.mpl.wsdto.StoreLocatorResponseItem;



/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class MplCommerceCartServiceImpl extends DefaultCommerceCartService implements MplCommerceCartService
{

	private static final Logger LOG = Logger.getLogger(MplCommerceCartServiceImpl.class);

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "mplCustomerProfileService")
	private MplCustomerProfileService mplCustomerProfileService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "mplCommerceCartDao")
	private MplCommerceCartDao mplCommerceCartDao;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;

	@Resource(name = "mplCommerceCartService")
	private MplCommerceCartService mplCommerceCartService;
	@Autowired
	private BuyBoxDao buyBoxDao;

	@Autowired
	private InventoryReservationService inventoryReservationService;

	@Autowired
	private MplPincodeRestrictionService mplPincodeRestrictionService;

	@Autowired
	private MplDeliveryCostService mplDeliveryCostService;

	@Autowired
	private CartService cartService;

	@Resource
	private ExtDefaultCommerceUpdateCartEntryStrategy commerceUpdateCartEntryStrategy;

	@Resource(name = "pinCodeDeliveryModeService")
	private PinCodeDeliveryModeService pinCodeDeliveryModeService;

	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	private KeyGenerator keyGenerator;
	private Converter<CartModel, CartData> cartConverter;
	private Converter<CommerceCartModification, CartModificationData> cartModificationConverter;

	@Autowired
	private PriceDataFactory priceDataFactory;

	@Autowired
	private SessionService sessionService;
	@Autowired
	private CatalogUtils catalogUtils;



	@Autowired
	private MplDefaultCommerceAddToCartStrategyImpl mplDefaultCommerceAddToCartStrategyImpl;

	@Autowired
	private Wishlist2Service wishlistService;

	//@Autowired
	//private Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter;

	@Autowired
	private PersistentKeyGenerator subOrderIdGenerator;

	@Autowired
	private PersistentKeyGenerator orderLineIdGenerator;

	@Autowired
	private PersistentKeyGenerator orderIdGenerator;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private MplDelistingService mplDelistingService;

	@Resource(name = "mplPincodeServiceDao")
	private MplPincodeServiceDao mplPincodeServiceDao;

	@Resource
	private MplStockService mplStockService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "exchangeGuideService")
	private ExchangeGuideService exchangeService;

	@Autowired
	private MplCommerceCartCalculationStrategy calculationStrategy;


	private Converter<OrderModel, OrderData> orderConverter;


	private static final String MAXIMUM_CONFIGURED_QUANTIY = "mpl.cart.maximumConfiguredQuantity.lineItem";

	@Autowired
	private MplCommerceAddToCartStrategy mplCommerceAddToCartStrategy;






	/**
	 * @description: It is responsible for adding product to cart at ussid level
	 * @param parameter
	 * @return CommerceCartModification
	 * @throws CommerceCartModificationException
	 * @Override public CommerceCartModification addToCartWithUSSID(final CommerceCartParameter parameter) throws
	 *           CommerceCartModificationException { return
	 *           getMplDefaultCommerceAddToCartStrategyImpl().addToCart(parameter); }
	 */

	/**
	 * @description: It is responsible for fetching seller information
	 * @param cartData
	 * @param ussid
	 *
	 * @return Map<String, String>
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, String> getSellerInfo(final CartData cartData, final String ussid) throws CMSItemNotFoundException
	{
		final Map<String, String> sellerInfoMap = new HashMap<String, String>();

		if (cartData == null || CollectionUtils.isEmpty(cartData.getEntries()) || ussid == null)
		{
			LOG.debug("cartData entries or ussid is null or empty");
		}
		else
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				final List<SellerInformationData> sellerInfoData = entry.getProduct().getSeller();
				if (sellerInfoData == null)
				{
					continue;
				}
				else
				{
					for (final SellerInformationData sellerInformationData : sellerInfoData)
					{
						if (sellerInformationData.getUssid() != null && sellerInformationData.getUssid().equalsIgnoreCase(ussid))
						{
							sellerInfoMap.put(entry.getEntryNumber().toString(), sellerInformationData.getSellername());
						}
					}
				}
			}
		}
		return sellerInfoMap;
	}

	/**
	 * @description: It is responsible for fetching address
	 * @param addressData
	 *
	 * @return Map<String, String>
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, String> getAddress(final List<AddressData> addressData) throws CMSItemNotFoundException
	{
		String addr2 = null;
		final Map<String, String> expressCheckoutAddressMap = new HashMap<String, String>();
		StringBuffer addressBuilder = null;
		if (addressData != null)
		{
			for (final AddressData addressData1 : addressData)
			{
				addressBuilder = new StringBuffer();
				if (null != addressData1.getFirstName() && !addressData1.getFirstName().isEmpty())
				{
					addr2 = addressData1.getFirstName();
					addressBuilder.append(addr2);
				}
				if (null != addressData1.getLastName() && !addressData1.getLastName().isEmpty())
				{
					addressBuilder.append(' ').append(addressData1.getLastName());
				}
				if (null != addressData1.getLine1() && !addressData1.getLine1().isEmpty())
				{
					addressBuilder.append(',').append(addressData1.getLine1());
				}
				if (null != addressData1.getLine2() && !addressData1.getLine2().isEmpty())
				{
					addressBuilder.append(',').append(addressData1.getLine2());
				}
				if (null != addressData1.getLine3() && !addressData1.getLine3().isEmpty())
				{
					addressBuilder.append(',').append(addressData1.getLine3());
				}
				if (null != addressData1.getLocality() && !addressData1.getLocality().isEmpty())
				{
					addressBuilder.append(',').append(addressData1.getLocality());
				}
				if (null != addressData1.getPostalCode() && !addressData1.getPostalCode().isEmpty())
				{
					addressBuilder.append(',').append(addressData1.getPostalCode());
				}
				if (null != addressData1.getTown() && !addressData1.getTown().isEmpty())
				{
					addressBuilder.append(',').append(addressData1.getTown());
				}
				if (null != addressData1.getState() && !addressData1.getState().isEmpty())
				{
					addressBuilder.append(',').append(addressData1.getState());
				}
				if (null != addressData1.getCountry().getName() && !addressData1.getCountry().getName().isEmpty())
				{
					addressBuilder.append(',').append(addressData1.getCountry().getName());
				}
				if (null != addressData1.getPhone() && !addressData1.getPhone().isEmpty())
				{
					addressBuilder.append(',').append(addressData1.getPhone());
				}
				expressCheckoutAddressMap.put(addressData1.getId(), addressBuilder.toString());
				LOG.debug("Address are>>>>>>>>>" + addr2);
			}

		}
		return expressCheckoutAddressMap;
	}

	/**
	 * @description: It is responsible for finding fulfillment mode for delivery
	 * @param cartData
	 *
	 * @return Map<String, String>
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, String> getFullfillmentMode(final CartData cartData) throws CMSItemNotFoundException
	{
		final Map<String, String> fullfillmentDataMap = new HashMap<String, String>();

		if (cartData == null)
		{
			LOG.debug("getFullfillmentMode : cartData is null");
		}
		else
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				if (entry != null && entry.getSelectedUssid() != null)
				{
					/*
					 * final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(entry
					 * .getSelectedUssid());
					 */
					SellerInformationModel sellerInfoModel = null;
					Collection<RichAttributeModel> richAttributeModel = null;
					if ((null != entry.getProduct())
							&& (entry.getProduct().getRootCategory()
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY)))
					{
						//Below will execute for fine jewellery
						final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(entry
								.getSelectedUssid());
						if (CollectionUtils.isNotEmpty(jewelleryInfo))
						{
							sellerInfoModel = getMplSellerInformationService().getSellerDetail(jewelleryInfo.get(0).getPCMUSSID());
						}
						else
						{
							LOG.error("No entry in JewelleryInformationModel for ussid " + entry.getSelectedUssid());
						}
					}
					else
					{
						sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUssid());
					}

					if (sellerInfoModel != null)
					{
						richAttributeModel = sellerInfoModel.getRichAttribute();
					}
					DeliveryFulfillModesEnum deliveryFulfillModesEnum = null;
					if (CollectionUtils.isNotEmpty(richAttributeModel)
							&& null != ((List<RichAttributeModel>) richAttributeModel).get(0))
					{
						deliveryFulfillModesEnum = ((List<RichAttributeModel>) richAttributeModel).get(0).getDeliveryFulfillModes();
					}
					if (null != deliveryFulfillModesEnum && null != deliveryFulfillModesEnum.getCode())
					{
						String fulfillmentType = deliveryFulfillModesEnum.getCode();
						//BUG-ID TISRLEE-1561 03-01-2017
						if (fulfillmentType.equalsIgnoreCase(MarketplacecommerceservicesConstants.FULFILMENT_TYPE_BOTH))
						{
							fulfillmentType = ((List<RichAttributeModel>) richAttributeModel).get(0).getDeliveryFulfillModeByP1()
									.getCode();
							fullfillmentDataMap.put(entry.getEntryNumber().toString(), fulfillmentType.toLowerCase());
						}
						else
						{
							fullfillmentDataMap.put(entry.getEntryNumber().toString(), fulfillmentType.toLowerCase());
						}

					}
				}
			}
		}
		return fullfillmentDataMap;
	}

	/**
	 * @description: It is responsible for finding fulfillment mode for delivery TISEE-6290
	 * @param orderData
	 *
	 * @return Map<String, String>
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, String> getOrderEntryFullfillmentMode(final OrderData orderData) throws CMSItemNotFoundException
	{
		final Map<String, String> fullfillmentDataMap = new HashMap<String, String>();

		/********************* TPR-1081 *********************/
		if (orderData == null)
		{
			LOG.debug("getFullfillmentMode : orderData is null");
		}
		else if (CollectionUtils.isEmpty(orderData.getSellerOrderList()))
		{
			for (final OrderEntryData orderEntryData : orderData.getEntries())
			{
				final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(orderEntryData
						.getSelectedUssid());
				if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null
						&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
				{
					final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
							.getDeliveryFulfillModes().getCode();
					fullfillmentDataMap.put(orderEntryData.getEntryNumber().toString(), fulfillmentType.toLowerCase());
				}
			}
		}
		else
		{
			for (final OrderData sellerOrderData : orderData.getSellerOrderList())
			{
				for (final OrderEntryData orderEntryData : sellerOrderData.getEntries())
				{
					final SellerInformationModel sellerInfoModel = mplSellerInformationService.getSellerDetail(orderEntryData
							.getSelectedUssid());
					if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null
							&& ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0).getDeliveryFulfillModes() != null)
					{
						final String fulfillmentType = ((List<RichAttributeModel>) sellerInfoModel.getRichAttribute()).get(0)
								.getDeliveryFulfillModes().getCode();
						fullfillmentDataMap.put(orderEntryData.getTransactionId(), fulfillmentType.toLowerCase());
					}
				}
			}
		}
		/********************* TPR-1081 *********************/

		return fullfillmentDataMap;
	}

	/**
	 * @description: It is responsible to find possible delivery mode
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @return Map<String, List<String>>
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData,
			final List<PinCodeResponseData> omsDeliveryResponse) throws CMSItemNotFoundException
	{

		final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		List<MarketplaceDeliveryModeData> deliveryModeDataList = null;
		boolean deliveryModeSelectedForEntry = false;
		if (cartData != null && omsDeliveryResponse != null && !omsDeliveryResponse.isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				final SellerInformationData sellerInformationData = entry.getSelectedSellerInformation();
				final String sellectedUssid = entry.getSelectedUssid();
				deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
				String defaultPriorityDeliveryMode = "";

				for (final PinCodeResponseData pincodeRes : omsDeliveryResponse)
				{
					/*
					 * if (sellerInformationData != null && sellerInformationData.getUssid() != null && pincodeRes.getUssid()
					 * != null && pincodeRes.getIsServicable() != null &&
					 * sellerInformationData.getUssid().equalsIgnoreCase(pincodeRes.getUssid()) &&
					 * pincodeRes.getIsServicable().equalsIgnoreCase("Y") && !entry.isGiveAway())
					 */
					if (sellectedUssid != null && pincodeRes.getUssid() != null && pincodeRes.getIsServicable() != null

					&& sellectedUssid.equalsIgnoreCase(pincodeRes.getUssid()) && pincodeRes.getIsServicable().equalsIgnoreCase("Y")
							&& !entry.isGiveAway())

					{
						deliveryModeSelectedForEntry = false;

						for (final DeliveryDetailsData deliveryData : pincodeRes.getValidDeliveryModes())
						{
							//TISUTO 63
							if (deliveryData.getInventory() != null)
							{
								String deliveryMode = MarketplacecommerceservicesConstants.EMPTYSPACE;
								final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();

								if (deliveryData.getType() != null
										&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.HD))
								{
									deliveryMode = MarketplacecommerceservicesConstants.HOME_DELIVERY;
								}
								else if (deliveryData.getType() != null
										&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.ED))
								{
									deliveryMode = MarketplacecommerceservicesConstants.EXPRESS_DELIVERY;
								}
								else if (deliveryData.getType() != null
										&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
								{
									deliveryMode = MarketplacecommerceservicesConstants.CLICK_COLLECT;
								}

								MplZoneDeliveryModeValueModel deliveryModel = getMplDeliveryCostService().getDeliveryCost(deliveryMode,
										MarketplacecommerceservicesConstants.INR, sellerInformationData.getUssid(),
										deliveryData.getFulfilmentType());
								DeliveryModeModel deliveryModeModel = null;
								if (null != deliveryModel)
								{
									deliveryModeModel = deliveryModel.getDeliveryMode();
								}
								if (null == deliveryModel)
								{
									deliveryModel = getMplDeliveryCostService().getDeliveryCost(deliveryMode,
											MarketplacecommerceservicesConstants.INR, "DUMMYTSHIP", "TSHIP");
									deliveryModeModel = deliveryModel.getDeliveryMode();
								}
								if (deliveryModel != null && deliveryModel.getValue() != null && deliveryModeModel != null)
								{
									PriceData priceData = null;
									if (entry.isIsBOGOapplied()
											&& isFulFillmentTypeMatch(deliveryData.getFulfilmentType(), deliveryModel
													.getDeliveryFulfillModes().getCode(), sellerInformationData))
									{
										/*
										 * priceData = formPriceData(Double.valueOf(deliveryModel.getValue().doubleValue()
										 * entry.getQualifyingCount().intValue()), cartData);
										 */
										//TISPRDT-1226
										priceData = formPriceData(
												Double.valueOf(deliveryModel.getValue().doubleValue()
														* (entry.getQuantity().intValue() - entry.getFreeCount().intValue())), cartData);
									}
									else if (isFulFillmentTypeMatch(deliveryData.getFulfilmentType(), deliveryModel
											.getDeliveryFulfillModes().getCode(), sellerInformationData))
									{
										priceData = formPriceData(
												Double.valueOf(deliveryModel.getValue().doubleValue() * entry.getQuantity().doubleValue()),
												cartData);
									}
									else
									{

										priceData = formPriceData(Double.valueOf(0.0), cartData);
									}
									deliveryModeData.setCode(checkDataValue(deliveryModeModel.getCode()));
									//deliveryModeData.setDescription(checkDataValue(deliveryModel.getDeliveryMode().getDescription()));


									final String startValue = deliveryModeModel.getStart() != null ? ""
											+ deliveryModeModel.getStart().intValue()
											: MarketplacecommerceservicesConstants.DEFAULT_START_TIME;

									final String endValue = deliveryModeModel.getEnd() != null ? ""
											+ deliveryModeModel.getEnd().intValue() : MarketplacecommerceservicesConstants.DEFAULT_END_TIME;


									deliveryModeData.setDescription(getDeliveryModeDescription(pincodeRes.getUssid(),
											deliveryModeModel.getCode(), startValue, endValue));

									deliveryModeData.setName(checkDataValue(deliveryModeModel.getName()));
									deliveryModeData.setSellerArticleSKU(checkDataValue(pincodeRes.getUssid()));
									deliveryModeData.setDeliveryCost(priceData);
									//New Code Added for TPR-579 : TSHIP Shipping Charges
									final DeliveryFulfillModesEnum deliveryFulfillModesEnum = deliveryModel.getDeliveryFulfillModes();
									if (null != deliveryFulfillModesEnum && StringUtils.isNotEmpty(deliveryFulfillModesEnum.getCode()))
									{
										deliveryModeData.setFulfillmentType(deliveryFulfillModesEnum.getCode());
									}
									//UF-314
									deliveryModeData.setPriority(deliveryModeModel.getPriority());

									if (null != entry.getMplDeliveryMode() && StringUtils.isNotEmpty(entry.getMplDeliveryMode().getCode())
											&& entry.getMplDeliveryMode().getCode().equals(deliveryMode))
									{
										deliveryModeSelectedForEntry = true;
										deliveryModeData.setIsSelected(true);
										defaultPriorityDeliveryMode = deliveryMode;
										if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)
												&& null != entry.getDeliveryPointOfService())
										{
											deliveryModeData.setStoreSelected(entry.getDeliveryPointOfService().getName());
										}

									}
									else
									{
										deliveryModeData.setIsSelected(false);
									}
								}
								deliveryModeDataList.add(deliveryModeData);
							}
						}
						//UF-314
						deliveryModeDataList.sort(Comparator.comparing(MarketplaceDeliveryModeData::getPriority).reversed());
						if (deliveryModeSelectedForEntry)
						{
							deliveryModeDataList.get(deliveryModeDataList.size() - 1).setDefaultSelectedDelMode(
									defaultPriorityDeliveryMode);//UF-281
						}
						else
						{
							defaultPriorityDeliveryMode = deliveryModeDataList.get(deliveryModeDataList.size() - 1).getCode();
							deliveryModeDataList.get(deliveryModeDataList.size() - 1).setIsSelected(true);//UF-281
							deliveryModeDataList.get(deliveryModeDataList.size() - 1).setDefaultSelectedDelMode(
									defaultPriorityDeliveryMode);//UF-281
						}
						deliveryModeDataMap.put(entry.getEntryNumber().toString(), deliveryModeDataList);
					}
				}
			}
		}
		else if (cartData != null)
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
				final SellerInformationData sellerData = entry.getSelectedSellerInformation();
				if (sellerData != null && sellerData.getDeliveryModes() != null)
				{
					for (final MarketplaceDeliveryModeData marketplaceDeliveryModeData : sellerData.getDeliveryModes())
					{
						final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();

						deliveryModeData.setCode(marketplaceDeliveryModeData.getCode());
						deliveryModeData.setDescription(marketplaceDeliveryModeData.getDescription());
						deliveryModeData.setName(marketplaceDeliveryModeData.getName());
						//deliveryModeData.setSellerArticleSKU(entry.getSelectedSellerInformation().getUssid());
						deliveryModeData.setSellerArticleSKU(entry.getSelectedUssid());
						deliveryModeData.setDeliveryCost(marketplaceDeliveryModeData.getDeliveryCost());
						//UF-314 Start
						deliveryModeData.setPriority(marketplaceDeliveryModeData.getPriority());
						//UF-314 End
						deliveryModeDataList.add(deliveryModeData);
					}
				}
				if (!deliveryModeDataList.isEmpty())
				{
					//UF-314
					deliveryModeDataList.sort(Comparator.comparing(MarketplaceDeliveryModeData::getPriority).reversed());
					deliveryModeDataList.get(0).setIsSelected(true);//UF-281
					deliveryModeDataMap.put(entry.getEntryNumber().toString(), deliveryModeDataList);
				}
				else
				{
					deliveryModeDataMap.clear();
				}
			}
		}
		return deliveryModeDataMap;
	}



	/**
	 * Returns Fulfillment Details
	 *
	 *
	 * @param fulfilmentType
	 * @param zodeFulflmntcode
	 * @param sellerInformationData
	 * @return flag
	 */
	private boolean isFulFillmentTypeMatch(final String fulfilmentType, final String zodeFulflmntcode,
			final SellerInformationData sellerInformationData)
	{
		boolean flag = false;

		final String omsPincodeServiceability = getConfigurationService().getConfiguration()
				.getString(MarketplacecclientservicesConstants.PIN_CODE_DELIVERY_MODE_OMS_MOCK).trim();

		if (StringUtils.isNotEmpty(fulfilmentType) && StringUtils.isNotEmpty(zodeFulflmntcode))
		{
			if (StringUtils.equalsIgnoreCase(omsPincodeServiceability, "Y"))
			{
				flag = (StringUtils.equalsIgnoreCase(fulfilmentType, zodeFulflmntcode)) ? true : false;
			}
			else
			{
				flag = true;

				LOG.debug("Executing code for real time set as False");

				if (null != sellerInformationData && StringUtils.equalsIgnoreCase(sellerInformationData.getFullfillment(), "TSHIP")
						&& !StringUtils.equalsIgnoreCase(fulfilmentType, zodeFulflmntcode))
				{
					LOG.debug("TSHIP but no TSHIP charges configured in Zone Delivery");
					flag = false;
				}
			}
		}

		return flag;
	}

	/**
	 * @description: It is responsible to find possible delivery mode
	 * @param omsDeliveryResponse
	 * @return Map<String, List<String>>
	 * @throws CMSItemNotFoundException
	 */
	private List<MarketplaceDeliveryModeData> getDeliveryModeTopTwoWishlistMobile(final Wishlist2EntryModel wishlist2EntryModel,
			final List<PinCodeResponseData> omsDeliveryResponse, final CartData cartData) throws CMSItemNotFoundException
	{
		//final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		List<MarketplaceDeliveryModeData> deliveryModeDataList = null;
		//final SellerInformationData sellerInformationData = wishlist2EntryModel.getSelectedSellerInformation();
		deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();

		for (final PinCodeResponseData pincodeRes : omsDeliveryResponse)
		{
			if (wishlist2EntryModel.getUssid() != null && !omsDeliveryResponse.isEmpty())
			{
				for (final DeliveryDetailsData deliveryData : pincodeRes.getValidDeliveryModes())
				{
					//TISUTO 63
					if (deliveryData.getInventory() != null)
					{
						String deliveryMode = MarketplacecommerceservicesConstants.EMPTYSPACE;
						final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();

						if (deliveryData.getType() != null
								&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.HD))
						{
							deliveryMode = MarketplacecommerceservicesConstants.HOME_DELIVERY;
						}
						else if (deliveryData.getType() != null
								&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.ED))
						{
							deliveryMode = MarketplacecommerceservicesConstants.EXPRESS_DELIVERY;
						}
						//else if (deliveryData.getType() != null
						//		&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
						//{
						//	deliveryMode = MarketplacecommerceservicesConstants.CLICK_COLLECT;
						//}

						final MplZoneDeliveryModeValueModel deliveryModel = getMplDeliveryCostService().getDeliveryCost(deliveryMode,
								MarketplacecommerceservicesConstants.INR, pincodeRes.getUssid());

						if (deliveryModel != null && deliveryModel.getValue() != null && deliveryModel.getDeliveryMode() != null)
						{
							final PriceData priceData = formPriceData(Double.valueOf(deliveryModel.getValue().doubleValue()), cartData);
							deliveryModeData.setCode(checkDataValue(deliveryModel.getDeliveryMode().getCode()));
							//deliveryModeData.setDescription(checkDataValue(deliveryModel.getDeliveryMode().getDescription()));


							final String startValue = deliveryModel.getDeliveryMode().getStart() != null ? ""
									+ deliveryModel.getDeliveryMode().getStart().intValue()
									: MarketplacecommerceservicesConstants.DEFAULT_START_TIME;

							final String endValue = deliveryModel.getDeliveryMode().getEnd() != null ? ""
									+ deliveryModel.getDeliveryMode().getEnd().intValue()
									: MarketplacecommerceservicesConstants.DEFAULT_END_TIME;


							deliveryModeData.setDescription(getDeliveryModeDescription(pincodeRes.getUssid(), deliveryModel
									.getDeliveryMode().getCode(), startValue, endValue));

							deliveryModeData.setName(checkDataValue(deliveryModel.getDeliveryMode().getName()));
							deliveryModeData.setSellerArticleSKU(checkDataValue(pincodeRes.getUssid()));
							deliveryModeData.setDeliveryCost(priceData);
						}
						deliveryModeDataList.add(deliveryModeData);
					}
				}

			}
		}
		return deliveryModeDataList;
	}

	/**
	 * @description: It is responsible to find possible delivery mode
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @return Map<String, List<String>>
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData, final OrderEntryData entry,
			final List<PinCodeResponseData> omsDeliveryResponse, final CartModel cartModel) throws CMSItemNotFoundException
	{
		final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		List<MarketplaceDeliveryModeData> deliveryModeDataList = null;
		if (cartData != null && omsDeliveryResponse != null && !omsDeliveryResponse.isEmpty() && null != entry)
		{

			final SellerInformationData sellerInformationData = entry.getSelectedSellerInformation();
			final String selectedUssid = entry.getSelectedUssid();
			deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();

			for (final PinCodeResponseData pincodeRes : omsDeliveryResponse)
			{
				//				if (sellerInformationData != null && sellerInformationData.getUssid() != null && pincodeRes.getUssid() != null
				//						&& pincodeRes.getIsServicable() != null
				//						&& sellerInformationData.getUssid().equalsIgnoreCase(pincodeRes.getUssid())
				//						&& pincodeRes.getIsServicable().equalsIgnoreCase("Y") && !entry.isGiveAway())
				if (StringUtils.isNotEmpty(selectedUssid) && pincodeRes.getUssid() != null && pincodeRes.getIsServicable() != null
						&& selectedUssid.equalsIgnoreCase(pincodeRes.getUssid()) && pincodeRes.getIsServicable().equalsIgnoreCase("Y")
						&& !entry.isGiveAway())
				{

					for (final DeliveryDetailsData deliveryData : pincodeRes.getValidDeliveryModes())
					{
						//TISUTO 63
						if (deliveryData.getInventory() != null)
						{
							String deliveryMode = MarketplacecommerceservicesConstants.EMPTYSPACE;
							final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();

							if (deliveryData.getType() != null
									&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.HD))
							{
								deliveryMode = MarketplacecommerceservicesConstants.HOME_DELIVERY;
							}
							else if (deliveryData.getType() != null
									&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.ED))
							{
								deliveryMode = MarketplacecommerceservicesConstants.EXPRESS_DELIVERY;
							}
							//else if (deliveryData.getType() != null
							//		&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
							//{
							//	deliveryMode = MarketplacecommerceservicesConstants.CLICK_COLLECT;
							//}

							final MplZoneDeliveryModeValueModel deliveryModel = getMplDeliveryCostService().getDeliveryCost(
									deliveryMode, MarketplacecommerceservicesConstants.INR, sellerInformationData.getUssid());

							if (deliveryModel != null && deliveryModel.getValue() != null && deliveryModel.getDeliveryMode() != null)
							{
								final PriceData priceData = formPriceData(
										Double.valueOf(deliveryModel.getValue().doubleValue() * entry.getQuantity().doubleValue()),
										cartData);
								deliveryModeData.setCode(checkDataValue(deliveryModel.getDeliveryMode().getCode()));
								deliveryModeData.setDescription(checkDataValue(deliveryModel.getDeliveryMode().getDescription()));
								deliveryModeData.setName(checkDataValue(deliveryModel.getDeliveryMode().getName()));
								deliveryModeData.setSellerArticleSKU(checkDataValue(pincodeRes.getUssid()));
								deliveryModeData.setDeliveryCost(priceData);
							}
							deliveryModeDataList.add(deliveryModeData);
						}
					}
					deliveryModeDataMap.put(entry.getEntryNumber().toString(), deliveryModeDataList);
				}
			}
		}
		else if (cartData != null)
		{

			deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
			final SellerInformationData sellerData = entry.getSelectedSellerInformation();
			if (sellerData != null && sellerData.getDeliveryModes() != null)
			{
				for (final MarketplaceDeliveryModeData marketplaceDeliveryModeData : sellerData.getDeliveryModes())
				{
					final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();

					deliveryModeData.setCode(marketplaceDeliveryModeData.getCode());
					deliveryModeData.setDescription(marketplaceDeliveryModeData.getDescription());
					deliveryModeData.setName(marketplaceDeliveryModeData.getName());
					deliveryModeData.setSellerArticleSKU(entry.getSelectedUssid());
					deliveryModeData.setDeliveryCost(marketplaceDeliveryModeData.getDeliveryCost());
					deliveryModeDataList.add(deliveryModeData);
				}
			}
			if (!deliveryModeDataList.isEmpty())
			{
				deliveryModeDataMap.put(entry.getEntryNumber().toString(), deliveryModeDataList);
			}
			else
			{
				deliveryModeDataMap.clear();
			}

		}
		return deliveryModeDataMap;
	}

	/*
	 * @Desc : used to fetch delivery mode description details TISEE-950
	 *
	 *
	 *
	 *
	 *
	 *
	 * @param ussId
	 *
	 *
	 *
	 *
	 *
	 *
	 * @param deliveryMode
	 *
	 *
	 *
	 *
	 *
	 *
	 * @param startTime
	 *
	 *
	 *
	 *
	 *
	 *
	 * @param endTime
	 *
	 *
	 *
	 *
	 *
	 *
	 * @return String
	 */

	@Override
	public String getDeliveryModeDescription(final String ussId, final String deliveryMode, final String startTime,
			final String endTime)
	{
		String description = "";
		try
		{

			final String deliveryModeDescPrefix = MarketplacecommerceservicesConstants.DELIVERYMODE_DESC_PREFIX;
			final String deliveryModeDescSuffix = MarketplacecommerceservicesConstants.DELIVERYMODE_DESC_SUFFIX;

			if (ussId != null && startTime != null && endTime != null)
			{
				Integer leadTime = Integer.valueOf(0);
				String startDay = "";
				String endDay = "";
				startDay = StringUtils.isNotEmpty(startTime) ? startTime : MarketplacecommerceservicesConstants.DEFAULT_START_TIME;
				endDay = StringUtils.isNotEmpty(endTime) ? endTime : MarketplacecommerceservicesConstants.DEFAULT_END_TIME;

				LOG.debug("Delivery Mode Description | Ussid :" + ussId + " | deliveryMode :" + deliveryMode + " | Start time :"
						+ startTime + " | End Time : " + endTime);

				List<RichAttributeModel> richAttributeModel = new ArrayList<RichAttributeModel>();
				final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(ussId);
				if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
				{
					richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
					if (null != richAttributeModel && !richAttributeModel.isEmpty())
					{
						leadTime = richAttributeModel.get(0).getLeadTimeForHomeDelivery() != null ? richAttributeModel.get(0)
								.getLeadTimeForHomeDelivery() : Integer.valueOf(0);
					}
					LOG.debug(" >> Lead time for Home delivery  " + leadTime);
				}

				if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
				{
					final int start = Integer.parseInt(startDay) + leadTime.intValue();
					final int end = Integer.parseInt(endDay) + leadTime.intValue();
					description = deliveryModeDescPrefix + start + "-" + end + deliveryModeDescSuffix;
				}
				else if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
				{
					description = deliveryModeDescPrefix + Integer.parseInt(startDay) + "-" + Integer.parseInt(endDay)
							+ deliveryModeDescSuffix;
				}
			}
		}
		catch (final Exception ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(ex,
					MarketplacecommerceservicesConstants.E0000));
		}
		return description;
	}

	/**
	 * @description: It is responsible to find possible delivery mode
	 * @param cartData
	 * @param omsDeliveryResponse
	 * @return void
	 * @throws CMSItemNotFoundException
	 * @throws ParseException
	 */
	@SuppressWarnings("javadoc")
	@Override
	public void setDeliveryDate(final CartData cartData, final List<PinCodeResponseData> omsDeliveryResponse)
			throws CMSItemNotFoundException, ParseException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		if (cartData != null && omsDeliveryResponse != null && !omsDeliveryResponse.isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				final SellerInformationData sellerInformationData = entry.getSelectedSellerInformation();

				for (final PinCodeResponseData pincodeRes : omsDeliveryResponse)
				{
					if (sellerInformationData != null && sellerInformationData.getUssid() != null && pincodeRes.getUssid() != null
							&& pincodeRes.getIsServicable() != null
							&& sellerInformationData.getUssid().equalsIgnoreCase(pincodeRes.getUssid())
							&& pincodeRes.getIsServicable().equalsIgnoreCase("Y"))
					{
						final String deliveryDate = pincodeRes.getDeliveryDate();
						if (deliveryDate != null)
						{
							Date expectedDeliveryDate = null;
							//final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss z");
							if (pincodeRes.getDeliveryDate() != null)
							{
								expectedDeliveryDate = javax.xml.bind.DatatypeConverter.parseDateTime(deliveryDate).getTime();
								entry.setExpectedDeliveryDate(expectedDeliveryDate);

								for (final AbstractOrderEntryModel orderEntry : cartModel.getEntries())
								{
									if (orderEntry.getEntryNumber().equals(entry.getEntryNumber()))
									{
										orderEntry.setExpectedDeliveryDate(expectedDeliveryDate);
										getModelService().save(orderEntry);

									}
								}

							}
						}

					}
				}
			}

			getModelService().save(cartModel);
		}
	}

	/**
	 * Method for fetching cart details without session
	 *
	 * @param ussid
	 * @return Collection<CartModel>
	 * @throws InvalidCartException
	 *
	 */

	@Override
	public Collection<CartModel> getCartDetails(final String ussid) throws InvalidCartException
	{
		Collection<CartModel> cartModel = null;
		if (ussid != null && !ussid.isEmpty())
		{
			final UserModel userModel = getUserService().getUserForUID(ussid);
			cartModel = userModel.getCarts();
		}
		else
		{
			throw new InvalidCartException("User uid is null or empty");
		}
		return cartModel;
	}

	/**
	 * @Desc Method for default pincode
	 * @param addressData
	 * @return Collection<CartModel>
	 * @throws CMSItemNotFoundException
	 *
	 */
	@Override
	public String getDefaultPinCode(final AddressData addressData, final String defaultPinCodeId) throws CMSItemNotFoundException
	{
		String pincode = null;
		if (null == defaultPinCodeId && null != addressData)
		{
			pincode = addressData.getPostalCode();
		}
		else
		{
			pincode = defaultPinCodeId;
		}

		return pincode;
	}


	/**
	 * @Description : GiftYourself two latest product from wishlists
	 * @param minGiftQuantity
	 * @param allWishlists
	 * @param pincode
	 * @return : List<Wishlist2EntryModel>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<Wishlist2EntryModel> getGiftYourselfDetailsMobile(final int minGiftQuantity,
			final List<Wishlist2Model> allWishlists, final String pincode, final CartData cartData)
			throws EtailNonBusinessExceptions
	{

		final List<Wishlist2EntryModel> productDataList = new ArrayList<Wishlist2EntryModel>();
		final List<Wishlist2EntryModel> productExistInCartList = new ArrayList<Wishlist2EntryModel>();
		List<Wishlist2EntryModel> finalgiftList = null;

		if (allWishlists != null && !allWishlists.isEmpty())
		{
			for (final Wishlist2Model wishlistEntry : allWishlists) //traversing all wishlists
			{

				for (final Wishlist2EntryModel entryWishlist : wishlistEntry.getEntries())//getting the latest two
				{

					/*
					 * if (null != cartModel.getEntries() && !cartModel.getEntries().isEmpty()) { for (final
					 * AbstractOrderEntryModel entry : cartModel.getEntries()) {
					 *
					 *
					 * if (null != entry.getSelectedUSSID() && !entry.getSelectedUSSID().isEmpty() && null !=
					 * entryWishlist.getUssid() && !entryWishlist.getUssid().isEmpty() &&
					 * entry.getSelectedUSSID().equalsIgnoreCase(entryWishlist.getUssid())) {
					 * productExistInCartList.add(entryWishlist); } }
					 *
					 *
					 * }
					 */

					if (entryWishlist.getIsDeleted() == null
							|| (entryWishlist.getIsDeleted() != null && !entryWishlist.getIsDeleted().booleanValue()))//TPR-5787 check added
					{
						if (null != cartData.getEntries() && !cartData.getEntries().isEmpty())
						{




							for (final OrderEntryData entry : cartData.getEntries())
							{

								if (null != entry.getSelectedUssid() && !entry.getSelectedUssid().isEmpty()
										&& null != entryWishlist.getUssid() && !entryWishlist.getUssid().isEmpty()
										&& entry.getSelectedUssid().equalsIgnoreCase(entryWishlist.getUssid()))
								{
									productExistInCartList.add(entryWishlist);
								}
							}


						}
						productDataList.add(entryWishlist);
					}

				}

			}
		}
		if (!productExistInCartList.isEmpty())
		{
			productDataList.removeAll(productExistInCartList);
		}
		if (CollectionUtils.isNotEmpty(productDataList))
		{
			LOG.info("UnSorted productDataList" + productDataList);
		}
		//sorting in reverse
		final BeanComparator fieldComparator = new BeanComparator("creationtime", Collections.reverseOrder());
		if (CollectionUtils.isNotEmpty(productDataList))
		{
			Collections.sort(productDataList, fieldComparator);
			LOG.info("Sorted productDataList" + productDataList);

			//finalgiftList = preparingFinalList(productDataList, pincode, (((List<CartModel>) cartModelList).get(0))); // TISPT-179 Point 3
			final Tuple2<?, ?> wishListPincodeObject = preparingFinalList(productDataList, pincode, cartData);
			finalgiftList = (List<Wishlist2EntryModel>) wishListPincodeObject.getFirst();

		}
		return finalgiftList;
	}

	/**
	 * @Description : GiftYourself two latest product from wishlists
	 * @param minGiftQuantity
	 * @param allWishlists
	 * @param pincode
	 * @param cartModel
	 *           //TISPT-179 // / * @return : List<Wishlist2EntryModel>
	 * @return : Tuple2<?, ?> //TISPT-179 Point 3
	 * @throws CMSItemNotFoundException
	 */
	//@Override
	//public List<Wishlist2EntryModel> getGiftYourselfDetails(final int minGiftQuantity, final List<Wishlist2Model> allWishlists,final String pincode, final CartModel cartModel) throws CMSItemNotFoundException
	@Override
	public Tuple2<?, ?> getGiftYourselfDetails(final int minGiftQuantity, final List<Wishlist2Model> allWishlists,
			final String pincode, final CartData cartData) throws CMSItemNotFoundException
	{

		final List<Wishlist2EntryModel> productDataList = new ArrayList<Wishlist2EntryModel>();
		//final List<Wishlist2EntryModel> finalgiftList = new ArrayList<Wishlist2EntryModel>(); TISPT-179 Ponit 3

		//if (allWishlists != null && !allWishlists.isEmpty())
		if (CollectionUtils.isNotEmpty(allWishlists))
		{
			for (final Wishlist2Model wishlistEntry : allWishlists) //traversing all wishlists
			{
				for (final Wishlist2EntryModel entryWishlist : wishlistEntry.getEntries())//getting the latest two
				{
					// if (!entryWishlist.getIsDeleted().booleanValue() || entryWishlist.getIsDeleted() == null)
					//   {
					//	productDataList.add(entryWishlist);
					//   }

					//TPR-5787 starts here
					if (entryWishlist.getIsDeleted() != null)
					{
						LOG.info("WishListEntry isDeleted exists");
						if (!entryWishlist.getIsDeleted().booleanValue())
						{
							LOG.info("WishListEntry isDeleted false");
							productDataList.add(entryWishlist);
						}
					}
					else
					{
						LOG.info("WishListEntry isDeleted not exists");
						productDataList.add(entryWishlist);
					}
					//TPR-5787 ends here
				}
			}
		}
		LOG.info("UnSorted productDataList" + productDataList);

		//sorting in reverse
		final BeanComparator fieldComparator = new BeanComparator("creationtime", Collections.reverseOrder());
		Collections.sort(productDataList, fieldComparator);
		LOG.info("Sorted productDataList" + productDataList);
		//final CartModel cartModel = cartService.getSessionCart(); TISPT-179 Point 2
		//	finalgiftList = preparingFinalList(productDataList, pincode, cartModel); TISPT-179 Point 3
		//return finalgiftList; ISPT-179 Point 3

		final Tuple2<?, ?> wishListPincodeObj = preparingFinalList(productDataList, pincode, cartData);
		return wishListPincodeObj;

	}

	/**
	 * @Description : GiftYourself two latest product from wishlists
	 * @param productDataList
	 * @param pincode
	 * @return : List<Wishlist2EntryModel>
	 * @throws CMSItemNotFoundException
	 */

	private Tuple2<List<?>, List<?>> preparingFinalList(final List<Wishlist2EntryModel> productDataList, final String pincode,
			final CartData cartData) throws EtailNonBusinessExceptions
	{
		final List<Wishlist2EntryModel> giftList = new ArrayList<Wishlist2EntryModel>();
		List<PinCodeResponseData> pinCodeResponseDataList = null;
		if (CollectionUtils.isNotEmpty(productDataList))
		{

			final Map<String, Wishlist2EntryModel> wishListEntryMap = new HashMap<String, Wishlist2EntryModel>();
			final int wishlistDisplayQuantity = getSiteConfigService().getInt(
					MarketplacecommerceservicesConstants.WISHLIST_DISPLAY_QUANTITY, 2);

			// For creating distinct wish list with respect to ussid
			for (final Wishlist2EntryModel wishListEntryModel : productDataList)
			{
				boolean ussidExists = false;
				boolean isWishlistEntryValid = true;
				for (final OrderEntryData entryModel : cartData.getEntries())
				{
					if (wishListEntryModel.getUssid() != null
							&& wishListEntryModel.getUssid().equalsIgnoreCase(entryModel.getSelectedUssid()))
					{
						ussidExists = true;
						break;
					}
				}
				//TISEE-5185
				isWishlistEntryValid = isWishlistEntryValid(wishListEntryModel);
				LOG.debug("isWishlistEntryValid " + isWishlistEntryValid);

				if (isWishlistEntryValid && !ussidExists && wishListEntryModel.getUssid() != null)
				{
					wishListEntryMap.put(wishListEntryModel.getUssid(), wishListEntryModel);
				}
			}

			// For sorting the created wish list with respect to Wishlist added date
			final Map<java.util.Date, Wishlist2EntryModel> sortedMap = new TreeMap<java.util.Date, Wishlist2EntryModel>(
					new Comparator<java.util.Date>()
					{
						@Override
						public int compare(final java.util.Date dateObjPresent, final java.util.Date dateObjToCheck)
						{
							return dateObjToCheck.compareTo(dateObjPresent);
						}

					});

			final Iterator iterator = wishListEntryMap.entrySet().iterator();
			while (iterator.hasNext())
			{
				final Map.Entry entry = (Map.Entry) iterator.next();
				final Wishlist2EntryModel wishlist2EntryModel = (Wishlist2EntryModel) entry.getValue();
				sortedMap.put(wishlist2EntryModel.getAddedDate(), wishlist2EntryModel);
			}

			final Iterator pincodeIterator = sortedMap.entrySet().iterator();


			if (StringUtil.isNotEmpty(pincode))
			{
				// Changes for Duplicate Cart fix
				final List<PincodeServiceData> pincodeRequestDataList = fetchWishlistPincodeRequestData(sortedMap, cartData);
				if (CollectionUtils.isNotEmpty(pincodeRequestDataList))
				{
					pinCodeResponseDataList = getServiceablePinCodeCart(pincode, pincodeRequestDataList);
					if (CollectionUtils.isNotEmpty(pinCodeResponseDataList))
					{
						final Iterator wishlistIterator = sortedMap.entrySet().iterator();
						while (wishlistIterator.hasNext() && giftList.size() < wishlistDisplayQuantity)
						{
							final Map.Entry entry = (Map.Entry) wishlistIterator.next();
							final Wishlist2EntryModel wishlist2EntryModel = (Wishlist2EntryModel) entry.getValue();

							for (final PinCodeResponseData pincodServicableData : pinCodeResponseDataList)
							{
								if (pincodServicableData.getUssid() != null && pincodServicableData.getIsServicable() != null
										&& pincodServicableData.getUssid().equalsIgnoreCase(wishlist2EntryModel.getUssid())
										&& null != pincodServicableData.getStockCount()
										&& pincodServicableData.getStockCount().intValue() > 0
										&& pincodServicableData.getIsServicable().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
								{
									giftList.add(wishlist2EntryModel);
								}

							}
						}
					}
				}
			}
			else
			{
				// Pincode not available in session sortedMap
				while (pincodeIterator.hasNext() && giftList.size() < wishlistDisplayQuantity)
				{
					final Map.Entry entry = (Map.Entry) pincodeIterator.next();
					final Wishlist2EntryModel wishlist2EntryModel = (Wishlist2EntryModel) entry.getValue();
					final String articleSkuID = wishlist2EntryModel.getUssid();

					final FlexibleSearchService flexibleSearchService = Registry.getApplicationContext().getBean(
							FlexibleSearchService.class);

					if (articleSkuID != null)
					{

						StockLevelModel stockLevelModel = new StockLevelModel();
						stockLevelModel.setSellerArticleSKU(articleSkuID);
						stockLevelModel = flexibleSearchService.getModelByExample(stockLevelModel);

						if (stockLevelModel != null && stockLevelModel.getAvailable() > 0)
						{
							giftList.add((Wishlist2EntryModel) entry.getValue());
						}
					}
				}
			}

		}

		return new Tuple2(giftList, pinCodeResponseDataList); //TISPT-179
		//return giftList; //TISPT-179
	}

	/*
	 * @Desc checking wishlist entry is valid or not , delisted , end date , online from TISEE-5185
	 *
	 *
	 * @param wishlistEntryModel
	 *
	 *
	 * @return boolean
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean isWishlistEntryValid(final Wishlist2EntryModel wishlistEntryModel) throws EtailNonBusinessExceptions
	{
		boolean valid = true;
		final Date sysDate = new Date();
		final String ussid = wishlistEntryModel.getUssid();

		/*
		 * final CatalogVersionModel onlineCatalog = catalogService.getCatalogVersion(
		 * MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
		 * MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
		 */
		final CatalogVersionModel onlineCatalog = catalogUtils.getSessionCatalogVersionForProduct();
		final List<SellerInformationModel> sellerInformationModelList = mplDelistingService.getModelforUSSID(ussid, onlineCatalog);

		if (CollectionUtils.isNotEmpty(sellerInformationModelList)
				&& sellerInformationModelList.get(0) != null
				&& (sellerInformationModelList.get(0).getSellerAssociationStatus() != null && sellerInformationModelList.get(0)
						.getSellerAssociationStatus().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.NO)))
		{
			valid = false;
			LOG.debug(" >>> WIshlist Entry delisted for ussid :" + ussid);
		}
		else if (CollectionUtils.isNotEmpty(sellerInformationModelList) && sellerInformationModelList.get(0) != null
				&& sellerInformationModelList.get(0).getEndDate() != null
				&& sysDate.after(sellerInformationModelList.get(0).getEndDate()))
		{
			valid = false;
			LOG.debug(" >>> WIshlist Entry End date is not valid :" + ussid);
		}
		else
		{
			final Date onlineDate = (wishlistEntryModel.getProduct() != null && wishlistEntryModel.getProduct().getOnlineDate() != null) ? wishlistEntryModel
					.getProduct().getOnlineDate() : sysDate;
			if (onlineDate.after(sysDate))
			{
				valid = false;
				LOG.debug(" >>> WIshlist Entry Online date is not valid :" + ussid);
			}
		}

		return valid;
	}

	/*
	 * @Desc creating pin code service data for pincode serviceability for wishlist
	 *
	 *
	 * @param sortedWishListMap
	 *
	 *
	 * @return List<PincodeServiceData>
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	private List<PincodeServiceData> fetchWishlistPincodeRequestData(
			final Map<java.util.Date, Wishlist2EntryModel> sortedWishListMap, final CartData cartData)
			throws EtailNonBusinessExceptions
	{
		final List<PincodeServiceData> pincodeRequestDataList = new ArrayList<PincodeServiceData>();
		if (sortedWishListMap != null)
		{
			final Iterator pincodeIterator = sortedWishListMap.entrySet().iterator();
			while (pincodeIterator.hasNext())
			{
				final List<MarketplaceDeliveryModeData> deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
				final PincodeServiceData pincodeServiceData = new PincodeServiceData();

				final Map.Entry entry = (Map.Entry) pincodeIterator.next();
				final Wishlist2EntryModel wishlist2EntryModel = (Wishlist2EntryModel) entry.getValue();
				final ProductModel productModel = wishlist2EntryModel.getProduct();
				if (null != productModel)
				{
					pincodeServiceData.setProductCode(productModel.getCode());
				}
				else
				{
					LOG.debug("Product Model is null for the wishlist Product");
				}

				final SellerInformationModel sellerInfo = getMplSellerInformationService().getSellerDetail(
						wishlist2EntryModel.getUssid());
				if (wishlist2EntryModel.getUssid() != null && sellerInfo != null)
				{
					String fullfillmentType = MarketplacecommerceservicesConstants.EMPTYSPACE;
					List<RichAttributeModel> richAttributeModel = new ArrayList<RichAttributeModel>();

					final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
							wishlist2EntryModel.getUssid());

					if (sellerInfoModel != null && CollectionUtils.isNotEmpty(sellerInfoModel.getRichAttribute()))
					{
						richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
						fullfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes() != null ? richAttributeModel.get(0)
								.getDeliveryFulfillModes().getCode() : "";
					}
					else
					{
						LOG.info("preparingFinalList sellerInfoModel is null ");
					}
					//TISPRD-3072

					final List<MplZoneDeliveryModeValueModel> deliveryModes = getMplDeliveryCostService().getDeliveryModesAndCost(
							MarketplacecommerceservicesConstants.INR, wishlist2EntryModel.getUssid());
					if (CollectionUtils.isNotEmpty(deliveryModes))
					{
						for (final MplZoneDeliveryModeValueModel deliveryEntry : deliveryModes)
						{

							if ((CollectionUtils.isNotEmpty(richAttributeModel) && null != richAttributeModel.get(0).getHomeDelivery()
									&& richAttributeModel.get(0).getHomeDelivery().equals(HomeDeliveryEnum.YES) && StringUtils
										.equalsIgnoreCase(deliveryEntry.getDeliveryMode().getCode(),
												MarketplacecommerceservicesConstants.HOME_DELIVERY))
									|| (CollectionUtils.isNotEmpty(richAttributeModel)
											&& null != richAttributeModel.get(0).getExpressDelivery()
											&& richAttributeModel.get(0).getExpressDelivery().equals(ExpressDeliveryEnum.YES) && StringUtils
												.equalsIgnoreCase(deliveryEntry.getDeliveryMode().getCode(),
														MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))

									|| (CollectionUtils.isNotEmpty(richAttributeModel)
											&& null != richAttributeModel.get(0).getClickAndCollect()
											&& richAttributeModel.get(0).getClickAndCollect().equals(ClickAndCollectEnum.YES) && StringUtils
												.equalsIgnoreCase(deliveryEntry.getDeliveryMode().getCode(),
														MarketplacecommerceservicesConstants.CLICK_COLLECT)))
							{
								final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
								final PriceData priceData = formPriceData(deliveryEntry.getValue(), cartData);
								deliveryModeData.setCode(deliveryEntry.getDeliveryMode().getCode());
								deliveryModeData.setDescription(deliveryEntry.getDeliveryMode().getDescription());
								deliveryModeData.setName(deliveryEntry.getDeliveryMode().getName());
								deliveryModeData.setSellerArticleSKU(wishlist2EntryModel.getUssid());
								deliveryModeData.setDeliveryCost(priceData);
								deliveryModeDataList.add(deliveryModeData);
							}

						}
					}
					//TISPRD-3072 ends

					pincodeServiceData.setFullFillmentType(fullfillmentType.toUpperCase());
					if (CollectionUtils.isNotEmpty(richAttributeModel) && richAttributeModel.get(0).getShippingModes() != null
							&& richAttributeModel.get(0).getShippingModes().getCode() != null)
					{
						pincodeServiceData.setTransportMode(MplCodeMasterUtility.getglobalCode(richAttributeModel.get(0)
								.getShippingModes().getCode().toUpperCase()));
					}
					else
					{
						LOG.info("preparingFinalList Transportmode is null ");
					}
					pincodeServiceData.setSellerId(sellerInfo.getSellerID());
					pincodeServiceData.setUssid(wishlist2EntryModel.getUssid());

					if (CollectionUtils.isNotEmpty(richAttributeModel)
							&& null != richAttributeModel.get(0).getPaymentModes()
							&& ((PaymentModesEnum.valueOf("COD")).toString().equalsIgnoreCase(
									richAttributeModel.get(0).getPaymentModes().getCode()) || (PaymentModesEnum.valueOf("BOTH"))
									.toString().equalsIgnoreCase(richAttributeModel.get(0).getPaymentModes().getCode())))
					{
						pincodeServiceData.setIsCOD(MarketplacecommerceservicesConstants.Y);
					}
					else
					{
						pincodeServiceData.setIsCOD(MarketplacecommerceservicesConstants.N);
					}
					//TISEE-6376
					if (wishlist2EntryModel.getProduct() != null && wishlist2EntryModel.getProduct().getMrp() != null)
					{
						pincodeServiceData.setPrice(wishlist2EntryModel.getProduct().getMrp());
					}
					else
					{
						LOG.debug("wishlist2EntryModel product is null ");
					}
					pincodeServiceData.setDeliveryModes(deliveryModeDataList);

					pincodeServiceData.setIsDeliveryDateRequired(MarketplacecommerceservicesConstants.N);
					pincodeRequestDataList.add(pincodeServiceData);
				}
			}
		}
		return pincodeRequestDataList;
	}

	/*
	 * @Desc creating pin code service data for pincode serviceability for wishlist
	 *
	 *
	 * @param sortedWishListMap
	 *
	 *
	 * @return List<PincodeServiceData>
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	private List<PincodeServiceData> fetchWishlistPincodeRequestDataMobile(final Wishlist2EntryModel wishlist2EntryModel,
			final CartData cartData) throws EtailNonBusinessExceptions
	{
		final List<PincodeServiceData> pincodeRequestDataList = new ArrayList<PincodeServiceData>();
		final List<MarketplaceDeliveryModeData> deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();
		final PincodeServiceData pincodeServiceData = new PincodeServiceData();

		//final Map.Entry entry = (Map.Entry) pincodeIterator.next();
		//final Wishlist2EntryModel wishlist2EntryModel = (Wishlist2EntryModel) entry.getValue();
		final ProductModel productModel = wishlist2EntryModel.getProduct();
		if (null != productModel)
		{
			pincodeServiceData.setProductCode(productModel.getCode());
		}
		else
		{
			LOG.debug("Product Model is null for the wishlist Product");
		}

		final SellerInformationModel sellerInfo = getMplSellerInformationService().getSellerDetail(wishlist2EntryModel.getUssid());
		if (wishlist2EntryModel.getUssid() != null && sellerInfo != null)
		{
			String fullfillmentType = MarketplacecommerceservicesConstants.EMPTYSPACE;
			List<RichAttributeModel> richAttributeModel = new ArrayList<RichAttributeModel>();

			final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
					wishlist2EntryModel.getUssid());

			if (sellerInfoModel != null && CollectionUtils.isNotEmpty(sellerInfoModel.getRichAttribute()))
			{
				richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
				fullfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes() != null ? richAttributeModel.get(0)
						.getDeliveryFulfillModes().getCode() : "";
			}
			else
			{
				LOG.info("preparingFinalList sellerInfoModel is null ");
			}

			final List<MplZoneDeliveryModeValueModel> deliveryModes = getMplDeliveryCostService().getDeliveryModesAndCost(
					MarketplacecommerceservicesConstants.INR, wishlist2EntryModel.getUssid());
			if (CollectionUtils.isNotEmpty(deliveryModes))
			{
				for (final MplZoneDeliveryModeValueModel deliveryEntry : deliveryModes)
				{
					final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
					// Changes for Duplicate Cart fix
					final PriceData priceData = formPriceData(deliveryEntry.getValue(), cartData);
					deliveryModeData.setCode(deliveryEntry.getDeliveryMode().getCode());
					deliveryModeData.setDescription(deliveryEntry.getDeliveryMode().getDescription());
					deliveryModeData.setName(deliveryEntry.getDeliveryMode().getName());
					deliveryModeData.setSellerArticleSKU(wishlist2EntryModel.getUssid());
					deliveryModeData.setDeliveryCost(priceData);
					deliveryModeDataList.add(deliveryModeData);
				}
			}
			pincodeServiceData.setFullFillmentType(fullfillmentType.toUpperCase());
			if (CollectionUtils.isNotEmpty(richAttributeModel) && richAttributeModel.get(0).getShippingModes() != null
					&& richAttributeModel.get(0).getShippingModes().getCode() != null)
			{
				pincodeServiceData.setTransportMode(MplCodeMasterUtility.getglobalCode(richAttributeModel.get(0).getShippingModes()
						.getCode().toUpperCase()));
			}
			else
			{
				LOG.info("preparingFinalList Transportmode is null ");
			}
			pincodeServiceData.setSellerId(sellerInfo.getSellerID());
			pincodeServiceData.setUssid(wishlist2EntryModel.getUssid());

			if (CollectionUtils.isNotEmpty(richAttributeModel)
					&& null != richAttributeModel.get(0).getPaymentModes()
					&& ((PaymentModesEnum.valueOf("COD")).toString().equalsIgnoreCase(
							richAttributeModel.get(0).getPaymentModes().getCode()) || (PaymentModesEnum.valueOf("BOTH")).toString()
							.equalsIgnoreCase(richAttributeModel.get(0).getPaymentModes().getCode())))
			{
				pincodeServiceData.setIsCOD(MarketplacecommerceservicesConstants.Y);
			}
			else
			{
				pincodeServiceData.setIsCOD(MarketplacecommerceservicesConstants.N);
			}
			pincodeServiceData.setPrice(wishlist2EntryModel.getProduct().getMrp());
			pincodeServiceData.setDeliveryModes(deliveryModeDataList);
			pincodeServiceData.setIsDeliveryDateRequired(MarketplacecommerceservicesConstants.N);
			pincodeRequestDataList.add(pincodeServiceData);
		}
		return pincodeRequestDataList;
	}


	/**
	 * @Desc Method for creating empty cart if no cart is associated with user id
	 * @param emailId
	 * @return CartModel
	 * @throws CommerceCartModificationException
	 * @throws InvalidCartException
	 */
	@Override
	public CartModel createCart(final String emailId, final String baseSiteId) throws CommerceCartModificationException,
			InvalidCartException
	{

		CartModel cart = null;
		UserModel userModel = null;
		boolean createCart = false;

		MplCustomerProfileData mplCustData = new MplCustomerProfileData();
		mplCustData.setDisplayUid(emailId);

		if (emailId == null || emailId.isEmpty())
		{
			userModel = getUserService().getAnonymousUser();
			LOG.info("Customer UID is Anonymous ");
			createCart = true;
		}
		else
		{
			mplCustData = getMplCustomerProfileService().getCustomerProfileDetail(mplCustData.getDisplayUid());
			LOG.info("Customer UID is : " + mplCustData.getUid());

			final Collection<CartModel> cartModelList = getCartDetails(mplCustData.getUid());
			userModel = getUserService().getUserForUID(mplCustData.getUid());
			// If no cart is associated with emailId then a empty cart will be created
			createCart = (cartModelList.isEmpty()) ? true : false;

		}

		if (createCart)
		{
			LOG.debug(" >> Creating new cart ");
			final CurrencyModel currency = getCommonI18NService().getCurrentCurrency();
			final String cartModelTypeCode = Config.getString(JaloSession.CART_TYPE, "Cart");
			cart = getModelService().create(cartModelTypeCode);
			cart.setCode(String.valueOf(keyGenerator.generate()));
			cart.setUser(userModel);
			cart.setCurrency(currency);
			cart.setDate(new Date());
			final User userItem = getModelService().getSource(userModel);
			final boolean result = OrderManager.getInstance().getPriceFactory().isNetUser(userItem);
			cart.setNet(Boolean.valueOf(result));
			cart.setSite(getBaseSiteForUID(baseSiteId));
			getModelService().save(cart);
		}

		return cart;
	}

	/**
	 * @Desc Method for adding item to cart for Mobile service
	 *
	 * @param cartId
	 *
	 * @param productCode
	 *
	 * @param quantity
	 *
	 * @param ussid
	 *
	 * @return boolean , for success return true else false
	 *
	 * @throws CommerceCartModificationException
	 *            if cartid , product code , quantity id null or empty if cart id , product id is invalid if quantity is
	 *            less than or equals to 0
	 */

	@Override
	public boolean addItemToCart(final String cartId, final CartModel cartModel, final ProductModel productModel,
			final long quantity, final String ussid) throws InvalidCartException, CommerceCartModificationException
	{
		boolean success = false;
		//commented for car project implementation CAR:62
		//CartData cartData = null;
		final CommerceCartParameter parameter = new CommerceCartParameter();
		CartModificationData cartModificationData = null;
		String sellerUssId = null;

		if (cartModel != null)
		{
			// Check if product code is valid or not
			if (productModel == null)
			{
				throw new CommerceCartModificationException("Invalid ussid " + ussid);
			}

			//check if quantity is valid or not
			if (quantity <= 0)
			{
				throw new CommerceCartModificationException("Invalid quantity " + quantity);
			}
			//commented for car project implementation CAR:62
			//cartData = getCartConverter().convert(cartModel);

			// Check if ussid is null then fetch ussid
			if (ussid == null || ussid.isEmpty())
			{
				sellerUssId = getSellerUssid(cartModel);
			}
			else
			{
				sellerUssId = ussid;
			}
			//			if (isMaxQuantityAlreadyAdded(cartData, productCode))
			//commented for car project implementation CAR:62: added cartModel instead of carData as param in isMaxQuantityAlreadyAdded
			//if (isMaxQuantityAlreadyAdded(cartData, sellerUssId, quantity, productModel.getCode()))
			if (isMaxQuantityAlreadyAdded(cartModel, sellerUssId, quantity, productModel.getCode()))
			{
				parameter.setEnableHooks(true);
				parameter.setCart(cartModel);
				parameter.setProduct(productModel);
				parameter.setQuantity(quantity);
				parameter.setUnit(productModel.getUnit());
				parameter.setCreateNewEntry(false);
				parameter.setUssid(sellerUssId);

				//final CommerceCartModification modification = addToCartWithUSSID(parameter);
				final CommerceCartModification modification = getMplDefaultCommerceAddToCartStrategyImpl().addToCart(parameter);
				cartModificationData = getCartModificationConverter().convert(modification);

				success = (cartModificationData != null) ? true : false;
			}
			else
			{
				throw new CommerceCartModificationException("Maximum quantity reached ");
			}
		}
		else
		{
			throw new CommerceCartModificationException(" Required Paramteres are null or empty");
		}

		return success;
	}

	/**
	 * @Desc Method for fetching selected ussid
	 * @param cartModel
	 * @return String
	 *
	 * @throws EtailNonBusinessExceptions
	 *
	 */

	public String getSellerUssid(final CartModel cartModel) throws EtailNonBusinessExceptions
	{
		String ussid = "";
		final List<AbstractOrderEntryModel> cartEntryModels = cartModel.getEntries();
		for (final AbstractOrderEntryModel abstractOrderEntryModel : cartEntryModels)
		{
			if (null != abstractOrderEntryModel.getSelectedUSSID() && !abstractOrderEntryModel.getSelectedUSSID().isEmpty())
			{
				ussid = abstractOrderEntryModel.getSelectedUSSID();
				LOG.info("entry number >>>" + abstractOrderEntryModel.getEntryNumber() + " has ussid>>>>>>>"
						+ abstractOrderEntryModel.getSelectedUSSID());
				break;
			}
		}
		return ussid;
	}

	/**
	 * @Desc Method for checking if maximum quantity already added to cart
	 * @param cartData
	 * @param code
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@SuppressWarnings("javadoc")
	//commented for car project implementation CAR:62: added cartModel instead of carData as param
	//private boolean isMaxQuantityAlreadyAdded(final CartData cartData, final String ussid, final long quantity,
	private boolean isMaxQuantityAlreadyAdded(final CartModel cartModel, final String ussid, final long quantity,
			final String productCode)
	{
		boolean addToCartFlag = true;
		final int maximum_configured_quantiy = getSiteConfigService().getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);
		//commented for car project implementation CAR:62
		//if (cartData.getEntries() != null && ussid != null && !cartData.getEntries().isEmpty())
		if (cartModel.getEntries() != null && ussid != null && !cartModel.getEntries().isEmpty())
		{
			//commented for car project implementation CAR:62
			//for (final OrderEntryData entry : cartData.getEntries())
			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				//commented for car project implementation CAR:62
				//final ProductData productData = entry.getProduct();
				final ProductModel productModel = entry.getProduct();

				//commented for car project implementation CAR:62
				//if (productCode.equals(productData.getCode()) && entry.getSelectedUssid().equalsIgnoreCase(ussid))
				if (productCode.equals(productModel.getCode()) && entry.getSelectedUSSID().equalsIgnoreCase(ussid))
				{

					LOG.debug(" Product already present in the cart so now we will check the qunatity present in the cart already");
					if (entry.getQuantity().longValue() >= maximum_configured_quantiy)
					{
						addToCartFlag = false;
						LOG.debug("You are about to exceede the max quantity");
						break;
					}
					if (quantity + entry.getQuantity().longValue() > maximum_configured_quantiy)
					{
						addToCartFlag = false;
						LOG.debug("You are about to exceede the max quantity");
						break;
					}
					break;

				}
			}
		}
		else
		{
			if (quantity > maximum_configured_quantiy)
			{
				addToCartFlag = false;
				LOG.debug("You are about to exceede the max quantity");
			}

		}
		return addToCartFlag;

	}


	/**
	 * @Desc Method for checking if maximum quantity already added to cart
	 * @param cartData
	 * @param code
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	@SuppressWarnings("javadoc")
	//commented for car project implementation CAR:62: added cartModel instead of carData as param
	//private boolean isMaxQuantityAlreadyAdded(final CartData cartData, final String ussid, final long quantity,
	private boolean isMaxQuantityAlreadyAddedExchange(final CartModel cartModel, final String ussid, final long quantity,
			final String productCode)
	{
		boolean addToCartFlag = true;
		final int maximum_configured_exc_quantity = 1;
		final int maximum_configured_quantiy = getSiteConfigService().getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);
		//commented for car project implementation CAR:62
		//if (cartData.getEntries() != null && ussid != null && !cartData.getEntries().isEmpty())
		if (cartModel.getEntries() != null && ussid != null && !cartModel.getEntries().isEmpty())
		{
			//commented for car project implementation CAR:62
			//for (final OrderEntryData entry : cartData.getEntries())
			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				//commented for car project implementation CAR:62
				//final ProductData productData = entry.getProduct();
				final ProductModel productModel = entry.getProduct();

				//commented for car project implementation CAR:62
				//if (productCode.equals(productData.getCode()) && entry.getSelectedUssid().equalsIgnoreCase(ussid))
				if (productCode.equals(productModel.getCode()) && entry.getSelectedUSSID().equalsIgnoreCase(ussid))
				{

					LOG.debug(" Product already present in the cart so now we will check the qunatity present in the cart already");
					if (entry.getQuantity().longValue() >= maximum_configured_quantiy + maximum_configured_exc_quantity)
					{
						addToCartFlag = false;
						LOG.debug("You are about to exceede the max quantity");
						break;
					}
					if (quantity + entry.getQuantity().longValue() > maximum_configured_quantiy + maximum_configured_exc_quantity)
					{
						addToCartFlag = false;
						LOG.debug("You are about to exceede the max quantity");
						break;
					}
					break;

				}
			}
		}
		else
		{
			if (quantity > maximum_configured_quantiy)
			{
				addToCartFlag = false;
				LOG.debug("You are about to exceede the max quantity");
			}

		}
		return addToCartFlag;

	}

	//private boolean isMaxQuantityAlreadyAdded(final CartData cartData, final String code)
	//	@SuppressWarnings("javadoc")
	//	private boolean isMaxQuantityAlreadyAdded(final CartData cartData, final String ussid)
	//	{
	//
	//		final int maximum_configured_quantiy = getSiteConfigService().getInt(MAXIMUM_CONFIGURED_QUANTIY, 0);
	//		if (cartData.getEntries() != null && ussid != null && !cartData.getEntries().isEmpty())
	//		{
	//			for (final OrderEntryData entry : cartData.getEntries())
	//			{
	//				//final ProductData productData = entry.getProduct();
	//
	//				if (entry.getSelectedUssid().equalsIgnoreCase(ussid))
	//
	//				//if (productData.getCode().equals(ussid))
	//				{
	//					LOG.debug(" Product already present in the cart so now we will check the qunatity present in the cart already");
	//					if (entry.getQuantity() == Long.valueOf(maximum_configured_quantiy))
	//					{
	//						LOG.debug("Maximum Quantity is already added, no more quantity can be added");
	//						return false;
	//					}
	//					else
	//					{
	//						return true;
	//					}
	//				}
	//			}
	//		}
	//		return true;
	//
	//	}

	/**
	 * @Desc Method for base site model for a base site id
	 * @param baseSiteId
	 * @return BaseSiteModel
	 * @throws EtailNonBusinessExceptions
	 *
	 */
	private BaseSiteModel getBaseSiteForUID(final String baseSiteId) throws CommerceCartModificationException
	{
		BaseSiteModel baseSiteModel = null;
		if (baseSiteId == null || baseSiteId.isEmpty())
		{
			throw new CommerceCartModificationException("BaseSiteId is null or empty");
		}
		else
		{
			baseSiteModel = getBaseSiteService().getBaseSiteForUID(baseSiteId);
		}
		return baseSiteModel;
	}


	/**
	 * @Desc Converting datatype of price
	 * @param price
	 * @param cartModel
	 * @return PriceData
	 * @throws EtailNonBusinessExceptions
	 */

	private PriceData formPriceData(final Double price, final CartData cartData) throws EtailNonBusinessExceptions
	{
		//final AbstractOrderModel cart = null;
		//final PriceData priceData = new PriceData();
		PriceData formattedPriceData = new PriceData();
		//		if (null == cartModel)
		//		{
		//			cart = getCartService().getSessionCart();
		//		}
		//		else
		//		{
		//			cart = cartModel;
		//		}
		if (price != null)
		{
			//priceData.setPriceType(PriceDataType.BUY);
			//priceData.setValue(new BigDecimal(price.doubleValue()));
			//priceData.setCurrencyIso(MarketplacecommerceservicesConstants.INR);
			final CurrencyModel currency = new CurrencyModel();
			currency.setIsocode(MarketplacecommerceservicesConstants.INR);
			currency.setSymbol(cartData.getCurrencySymbol());
			formattedPriceData = getPriceDataFactory().create(PriceDataType.BUY, new BigDecimal(price.doubleValue()), currency);

		}
		return formattedPriceData;
	}

	/*
	 * private PriceData formPriceData(final Double price, final CartModel cartModel) throws EtailNonBusinessExceptions {
	 * //final AbstractOrderModel cart = null; //final PriceData priceData = new PriceData(); PriceData
	 * formattedPriceData = new PriceData(); // if (null == cartModel) // { // cart = getCartService().getSessionCart();
	 * // } // else // { // cart = cartModel; // } if (price != null) { //priceData.setPriceType(PriceDataType.BUY);
	 * //priceData.setValue(new BigDecimal(price.doubleValue()));
	 * //priceData.setCurrencyIso(MarketplacecommerceservicesConstants.INR); final CurrencyModel currency = new
	 * CurrencyModel(); currency.setIsocode(MarketplacecommerceservicesConstants.INR);
	 * currency.setSymbol(cartModel.getCurrency().getSymbol()); formattedPriceData =
	 * getPriceDataFactory().create(PriceDataType.BUY, new BigDecimal(price.doubleValue()), currency);
	 *
	 *
	 * } return formattedPriceData; }
	 */

	/**
	 * @Desc fetching oms pincode response data
	 * @param pincode
	 * @param cartData
	 * @return List<PinCodeResponseData>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<PinCodeResponseData> getOMSPincodeResponseData(final String pincode, final CartData cartData)
			throws EtailNonBusinessExceptions
	{
		List<PinCodeResponseData> pinCodeResponseData = null;

		if (pincode != null && cartData != null)
		{
			final List<PincodeServiceData> reqData = new ArrayList<PincodeServiceData>();
			for (final OrderEntryData entryData : cartData.getEntries())
			{
				final PincodeServiceData data = new PincodeServiceData();
				data.setProductCode(entryData.getProduct().getCode());
				final SellerInformationData sellerData = entryData.getSelectedSellerInformation();
				if (sellerData != null)
				{
					data.setFullFillmentType(sellerData.getFullfillment());
					data.setTransportMode(sellerData.getShippingMode());
					data.setSellerId(sellerData.getSellerID());
					data.setUssid(sellerData.getUssid());
					data.setIsCOD(sellerData.getIsCod());
					data.setPrice(Double.valueOf(entryData.getBasePrice().getValue().doubleValue()));
					data.setDeliveryModes(sellerData.getDeliveryModes());
				}
				data.setIsDeliveryDateRequired(MarketplacecommerceservicesConstants.N);
				reqData.add(data);
			}
			pinCodeResponseData = getServiceablePinCodeCart(pincode, reqData);
		}
		return pinCodeResponseData;
	}

	/**
	 * @description this method checks the restriction list and calls pincode service accordingly
	 *
	 * @param pincode
	 * @param requestData
	 * @return List<PinCodeResponseData>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<PinCodeResponseData> getServiceablePinCodeCart(final String pincode, final List<PincodeServiceData> requestData)
			throws EtailNonBusinessExceptions
	{
		List<PinCodeResponseData> response = new ArrayList<PinCodeResponseData>();
		CopyOnWriteArrayList<PinCodeResponseData> resDataList = null;
		if (pincode != null && requestData != null)
		{

			final List<PincodeServiceData> validReqData = getMplPincodeRestrictionService().getRestrictedPincodeCart(pincode,
					requestData);
			if (null != validReqData)
			{
				response = getAllResponsesForPinCode(pincode, validReqData);
			}
			resDataList = new CopyOnWriteArrayList<PinCodeResponseData>(response);
			for (final PincodeServiceData req : requestData)
			{
				boolean flag = false;
				PinCodeResponseData dummyResponse = null;
				for (final PinCodeResponseData res : response)
				{
					if (req.getUssid().equalsIgnoreCase(res.getUssid()))
					{
						flag = true;
						break;
					}

				}
				if (!flag)
				{
					dummyResponse = new PinCodeResponseData();
					dummyResponse.setUssid(req.getUssid());
					dummyResponse.setIsServicable("N");
					resDataList.add(dummyResponse);
				}
			}

		}
		return resDataList;
	}

	/**
	 * @description this method gets all the responses about servicable pincodes from OMS
	 *
	 * @param pin
	 * @param reqData
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public List<PinCodeResponseData> getAllResponsesForPinCode(final String pin, final List<PincodeServiceData> reqData)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions
	{

		final List<PinCodeResponseData> responseList = new ArrayList<PinCodeResponseData>();
		//final List<AbstractOrderEntryModel> cartEntryList = null;
		try
		{
			//fetching response   from oms  against the pincode
			PinCodeDeliveryModeListResponse response = null;

			//			for (final PincodeServiceData dataObj : reqData)
			//			{

			//				final CartModel cartModel = getCartService().getSessionCart();
			//				cartEntryList = cartModel.getEntries();
			//				for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
			//				{

			//					if (null != cartEntryModel)
			//					{

			//						if (cartEntryModel.getSelectedUSSID().equalsIgnoreCase(dataObj.getUssid()))
			//						{

			//							cartEntryModel.setIsPrecious(dataObj.getIsPrecious());
			//							cartEntryModel.setIsFragile(dataObj.getIsFragile());
			//						}
			//					}
			//				}
			//			}




			//			LOG.debug("::::::Try to save cart Entry to :::::::::");
			//			if (null != cartEntryList && cartEntryList.size() > 0)
			//			{

			//				LOG.debug("::::::In side If Statement :::::::::");
			//				getModelService().saveAll(cartEntryList);
			//			}

			//			LOG.debug("::::::SuccessFully Saved to All Cart Entries:::::::::");
			try
			{
				response = getPinCodeDeliveryModeService().prepPinCodeDeliveryModetoOMS(pin, reqData);
			}
			catch (final ClientEtailNonBusinessExceptions e)
			{
				LOG.error("::::::Exception in calling OMS Pincode service:::::::::" + e.getErrorCode());
				if (null != e.getErrorCode()
						&& (MarketplacecclientservicesConstants.O0001_EXCEP.equalsIgnoreCase(e.getErrorCode())
								|| MarketplacecclientservicesConstants.O0002_EXCEP.equalsIgnoreCase(e.getErrorCode()) || MarketplacecclientservicesConstants.O0007_EXCEP
									.equalsIgnoreCase(e.getErrorCode())))
				{
					response = callPincodeServiceabilityCommerce(pin, reqData);
				}
			}



			boolean isCod = false;
			if (null != response.getItem())
			{
				for (final PinCodeDeliveryModeResponse deliveryModeResponse : response.getItem())
				{
					final List<Integer> stockCount = new ArrayList<Integer>();
					List<DeliveryDetailsData> deliveryDataList = null;
					PinCodeResponseData responseData = null;
					boolean servicable = false;

					if (null != deliveryModeResponse.getDeliveryMode())
					{
						deliveryDataList = new ArrayList<DeliveryDetailsData>();
						for (final DeliveryModeResOMSWsDto deliveryMode : deliveryModeResponse.getDeliveryMode())
						{

							if (null != deliveryMode.getIsPincodeServiceable()
									&& deliveryMode.getIsPincodeServiceable().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
							{

								servicable = true;
								responseData = new PinCodeResponseData();
								responseData.setIsPrepaidEligible(deliveryMode.getIsPrepaidEligible());
								responseData.setIsCODLimitFailed(deliveryMode.getIsCODLimitFailed());

								if ((deliveryMode.getIsCOD() != null)
										&& (deliveryMode.getIsCOD().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y)))
								{
									isCod = true;
								}

								/*
								 * if (!(deliveryMode.getInventory().equals(MarketplacecommerceservicesConstants.ZERO))) {
								 */
								final DeliveryDetailsData data = new DeliveryDetailsData();
								stockCount.add(Integer.valueOf(deliveryMode.getInventory()));
								data.setType(deliveryMode.getType());
								data.setInventory(deliveryMode.getInventory());

								if (deliveryMode.getIsCOD() != null
										&& deliveryMode.getIsCOD().equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
								{
									data.setIsCOD(Boolean.TRUE);
								}
								else if (deliveryMode.getIsCOD() != null
										&& deliveryMode.getIsCOD().equalsIgnoreCase(MarketplacecommerceservicesConstants.N))
								{
									data.setIsCOD(Boolean.FALSE);
								}

								if (deliveryMode.getFulfillmentType() != null)
								{
									data.setFulfilmentType(deliveryMode.getFulfillmentType());
								}

								if (null != deliveryMode.getServiceableSlaves() && deliveryMode.getServiceableSlaves().size() > 0)
								{
									data.setServiceableSlaves(populatePincodeServiceableData(deliveryMode.getServiceableSlaves()));
								}

								if (null != deliveryMode.getCNCServiceableSlaves() && deliveryMode.getCNCServiceableSlaves().size() > 0)
								{
									final List<CNCServiceableSlavesData> cncServiceableSlavesDataList = new ArrayList<CNCServiceableSlavesData>();
									CNCServiceableSlavesData cncServiceableSlavesData = null;
									for (final CNCServiceableSlavesWsDTO dto : deliveryMode.getCNCServiceableSlaves())
									{
										cncServiceableSlavesData = new CNCServiceableSlavesData();
										cncServiceableSlavesData.setStoreId(dto.getStoreId());
										cncServiceableSlavesData.setQty(dto.getQty());
										cncServiceableSlavesData.setFulfillmentType(dto.getFulfillmentType());
										cncServiceableSlavesData.setServiceableSlaves(populatePincodeServiceableData(dto
												.getServiceableSlaves()));
										cncServiceableSlavesDataList.add(cncServiceableSlavesData);
									}
									data.setCNCServiceableSlavesData(cncServiceableSlavesDataList);
								}

								deliveryDataList.add(data);
								//	}
								responseData.setValidDeliveryModes(deliveryDataList);
								if (!(stockCount.isEmpty()))
								{
									responseData.setStockCount(Collections.max(stockCount));
								}
								/*
								 * else { responseData.setStockCount(Integer.valueOf(0)); }
								 */
								responseData.setIsServicable(MarketplacecommerceservicesConstants.Y);
								responseData.setUssid(deliveryModeResponse.getUSSID());
								LOG.debug(new StringBuffer().append("********************servicable ussids for pincode*******")
										.append(pin).append("are:").append(deliveryModeResponse.getUSSID()).toString());
								if (deliveryMode.getDeliveryDate() != null && !deliveryMode.getDeliveryDate().isEmpty())
								{
									responseData.setDeliveryDate(deliveryMode.getDeliveryDate());
								}
								if (isCod)
								{
									responseData.setCod(MarketplacecommerceservicesConstants.Y);
								}
								else
								{
									responseData.setCod(MarketplacecommerceservicesConstants.N);
								}
							}

						}
					}
					if (!servicable)
					{
						responseData = new PinCodeResponseData();
						responseData.setIsServicable(MarketplacecommerceservicesConstants.N);
						responseData.setUssid(deliveryModeResponse.getUSSID());
					}
					if (responseData != null)
					{
						responseList.add(responseData);
					}
				}
			}
		}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error("********* Pincode serviceability exception :");
			final PinCodeResponseData responseData = new PinCodeResponseData();
			responseData.setIsServicable(MarketplacecommerceservicesConstants.NOT_APPLICABLE);
			responseList.add(responseData);
		}
		return responseList;
	}


	private List<ServiceableSlavesData> populatePincodeServiceableData(final List<ServiceableSlavesDTO> serviceableSlavesDTOList)
	{

		final List<ServiceableSlavesData> serviceableSlavesDataList = new ArrayList<ServiceableSlavesData>();
		ServiceableSlavesData serviceableSlavesData = null;
		for (final ServiceableSlavesDTO dto : serviceableSlavesDTOList)
		{
			serviceableSlavesData = new ServiceableSlavesData();
			serviceableSlavesData.setSlaveId(dto.getSlaveId());
			serviceableSlavesData.setLogisticsID(dto.getLogisticsID());
			serviceableSlavesData.setPriority(dto.getPriority());
			serviceableSlavesData.setCodEligible(dto.getCODEligible());
			serviceableSlavesData.setTransactionType(dto.getTransactionType());
			serviceableSlavesDataList.add(serviceableSlavesData);
		}
		return serviceableSlavesDataList;
	}

	/**
	 * @desc Method used to invoke pincode serviceability check as part of OMS fallback PLAN C
	 * @param pincode
	 * @param pincodeServiceDataList
	 * @return PinCodeDeliveryModeListResponse
	 *
	 */
	@Override
	public PinCodeDeliveryModeListResponse callPincodeServiceabilityCommerce(final String pincode,
			final List<PincodeServiceData> pincodeServiceDataList)
	{
		LOG.info("*********************** Commerce Pincode Serviceability *************");
		final List<PinCodeDeliveryModeResponse> pinCodeDeliveryModeResponseList = new ArrayList<PinCodeDeliveryModeResponse>();
		final PinCodeDeliveryModeListResponse responsefromOMS = new PinCodeDeliveryModeListResponse();
		final List<PincodeServiceabilityDataModel> fallbackPincodeList = mplPincodeServiceDao.getPincodeServicableDataAtCommerce(
				pincode, pincodeServiceDataList);
		LOG.info("*********************** Data Present in  fallback table  :" + CollectionUtils.isNotEmpty(fallbackPincodeList));

		responsefromOMS.setPincode(pincode);

		// Stock level data preparation for inventory

		final List<String> ussidList = new ArrayList<String>();
		for (final PincodeServiceData pincodeServiceData : pincodeServiceDataList)
		{
			ussidList.add(pincodeServiceData.getUssid());
		}
		String sellerArticleSKUs = GenericUtilityMethods.getcommaSepUSSIDs(ussidList);
		if (null != sellerArticleSKUs && sellerArticleSKUs.length() > 0)
		{
			sellerArticleSKUs = sellerArticleSKUs.substring(0, sellerArticleSKUs.length() - 1);
		}

		final Map<String, Integer> stockLevelMap = mplStockService.getAllStockLevelDetail(sellerArticleSKUs);

		// Fetching config value from base store
		final Long codUpperLimit = getBaseStoreService().getCurrentBaseStore().getCodUpperLimit();
		final Long codLowerLimit = getBaseStoreService().getCurrentBaseStore().getCodLowerLimit();
		final Long prepaidUpperLimit = getBaseStoreService().getCurrentBaseStore().getPrepaidUpperLimit();
		final Long prepaidLowerLimit = getBaseStoreService().getCurrentBaseStore().getPrepaidLowerLimit();

		for (final PincodeServiceData pincodeServiceData : pincodeServiceDataList)
		{
			final List<DeliveryModeResOMSWsDto> deliveryModeResOMSWsDtoList = new ArrayList<DeliveryModeResOMSWsDto>();
			final PinCodeDeliveryModeResponse pinCodeDeliveryModeResponse = new PinCodeDeliveryModeResponse();

			for (final MarketplaceDeliveryModeData deliveryModeData : pincodeServiceData.getDeliveryModes())
			{
				// Validation if data is present in OMS fallback table
				final String isDelieveryModePresent = isDelieveryModePresent(pincodeServiceData, fallbackPincodeList,
						deliveryModeData.getCode());

				boolean isDataPresentInFallback = false;
				String isPincodeServiceable = "N";
				if (isDelieveryModePresent.indexOf('|') > 0)
				{
					isDataPresentInFallback = true;
					isPincodeServiceable = isDelieveryModePresent.split("\\|")[1];
				}

				// Generating request xml

				final Integer stockCount = (stockLevelMap.get(pincodeServiceData.getUssid()) != null) ? Integer.valueOf(stockLevelMap
						.get(pincodeServiceData.getUssid()).intValue()) : Integer.valueOf(0);
				final boolean isCodLimitFailed = (pincodeServiceData.getPrice().longValue() >= codLowerLimit.longValue() && pincodeServiceData
						.getPrice().longValue() <= codUpperLimit.longValue()) ? false : true;
				final boolean isCodEligible = (isCodLimitFailed || pincodeServiceData.getIsCOD().equalsIgnoreCase(
						MarketplacecclientservicesConstants.N)) ? false : true;
				final boolean isPrepaidEligible = (pincodeServiceData.getPrice().longValue() >= prepaidLowerLimit.longValue() && pincodeServiceData
						.getPrice().longValue() <= prepaidUpperLimit.longValue()) ? true : false;

				isPincodeServiceable = (!isDataPresentInFallback || (!isPrepaidEligible && !isCodEligible)) ? MarketplacecclientservicesConstants.N
						: isPincodeServiceable;
				//Added the code for CNC Delivery Mode for OMS Fall Back
				DeliveryModeResOMSWsDto deliveryModeDto = null;
				if (deliveryModeData.getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
				{

					deliveryModeDto = new DeliveryModeResOMSWsDto();
					deliveryModeDto.setType(getDeliveryGlobalCode(deliveryModeData.getCode()));
					//if (pincodeServiceData.getStore().size() > 0)
					if (CollectionUtils.isNotEmpty(pincodeServiceData.getStore()))
					{
						deliveryModeDto.setInventory(MplConstants.DEFAULT_CNC_INVENTORY_COUNT);
						deliveryModeDto.setIsPincodeServiceable(MarketplacecclientservicesConstants.Y);
					}
					else
					{
						deliveryModeDto.setInventory(MplConstants.DEFAULT_CNC_NO_INVENTORY);
						deliveryModeDto.setIsPincodeServiceable(MarketplacecclientservicesConstants.N);
					}
					deliveryModeDto.setIsCOD(MarketplacecclientservicesConstants.N);
					deliveryModeDto.setIsCODLimitFailed(MarketplacecclientservicesConstants.N);
					deliveryModeDto.setIsPrepaidEligible(MarketplacecclientservicesConstants.Y);
					deliveryModeDto.setDeliveryDate(new SimpleDateFormat(MarketplacecclientservicesConstants.DELIVERY_DATE_FORMATTER)
							.format(new Date()));
				}
				else
				{
					deliveryModeDto = new DeliveryModeResOMSWsDto();
					deliveryModeDto.setType(getDeliveryGlobalCode(deliveryModeData.getCode()));
					deliveryModeDto.setInventory(String.valueOf(stockCount));
					deliveryModeDto.setIsPincodeServiceable(isPincodeServiceable);
					deliveryModeDto.setIsCOD((isCodEligible) ? MarketplacecclientservicesConstants.Y
							: MarketplacecclientservicesConstants.N);
					deliveryModeDto.setIsCODLimitFailed((isCodLimitFailed) ? MarketplacecclientservicesConstants.Y
							: MarketplacecclientservicesConstants.N);
					deliveryModeDto.setIsPrepaidEligible((isPrepaidEligible) ? MarketplacecclientservicesConstants.Y
							: MarketplacecclientservicesConstants.N);
					deliveryModeDto.setDeliveryDate(new SimpleDateFormat(MarketplacecclientservicesConstants.DELIVERY_DATE_FORMATTER)
							.format(new Date()));
				}

				deliveryModeResOMSWsDtoList.add(deliveryModeDto);
			}

			pinCodeDeliveryModeResponse.setUSSID(pincodeServiceData.getUssid());
			pinCodeDeliveryModeResponse.setFulfilmentType(pincodeServiceData.getFullFillmentType());
			pinCodeDeliveryModeResponse.setDeliveryMode(deliveryModeResOMSWsDtoList);
			pinCodeDeliveryModeResponseList.add(pinCodeDeliveryModeResponse);

		}

		responsefromOMS.setItem(pinCodeDeliveryModeResponseList);

		try
		{
			final StringWriter stringWriter = new StringWriter();
			final JAXBContext jaxbContext = JAXBContext.newInstance(PinCodeDeliveryModeListResponse.class);
			final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(responsefromOMS, stringWriter);
			LOG.info("************Commerce response xml*************************" + stringWriter.toString());
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return responsefromOMS;
	}

	/**
	 * @desc used to validate if data present in oms fallback table for seller id,delivery mode and fulfilment type
	 * @param pincodeServiceData
	 * @param fallbackPincodeList
	 * @param deliveryMode
	 * @return String
	 */
	private String isDelieveryModePresent(final PincodeServiceData pincodeServiceData,
			final List<PincodeServiceabilityDataModel> fallbackPincodeList, final String deliveryMode)
	{
		boolean infoPresent = false;
		String isPincodeServiceable = MarketplacecommerceservicesConstants.N;
		final StringBuilder response = new StringBuilder(100);

		if (CollectionUtils.isNotEmpty(fallbackPincodeList))
		{
			for (final PincodeServiceabilityDataModel fallbackPincodeData : fallbackPincodeList)
			{
				if (pincodeServiceData.getSellerId().equalsIgnoreCase(fallbackPincodeData.getSellerId())
						&& pincodeServiceData.getFullFillmentType().equalsIgnoreCase(fallbackPincodeData.getFulfillmentType())
						&& pincodeServiceData.getTransportMode().equalsIgnoreCase(fallbackPincodeData.getTransportMode())
						&& compareDeliveryModes(deliveryMode, fallbackPincodeData.getDeliveryMode()))
				{
					infoPresent = true;
					isPincodeServiceable = fallbackPincodeData.getServiceableFlag();
					break;
				}
			}
		}
		if (infoPresent)
		{
			response.append(MarketplacecommerceservicesConstants.Y).append('|').append(isPincodeServiceable);
		}
		else
		{
			response.append(MarketplacecommerceservicesConstants.N);
		}
		return response.toString();
	}

	private String getDeliveryGlobalCode(final String deliveryCode)
	{
		String deliveryModeGlobalCode = MarketplacecommerceservicesConstants.EMPTY;
		if (deliveryCode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
		{
			deliveryModeGlobalCode = MarketplacecommerceservicesConstants.HD;
		}
		else if (deliveryCode.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
		{
			deliveryModeGlobalCode = MarketplacecommerceservicesConstants.ED;
		}
		else if (deliveryCode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
		{
			deliveryModeGlobalCode = MarketplacecommerceservicesConstants.CnC;
		}

		return deliveryModeGlobalCode;
	}


	/**
	 *
	 * @param deliveryMode1
	 * @param deliveryMode2
	 * @return boolean
	 */
	private boolean compareDeliveryModes(final String deliveryMode1, final String deliveryMode2)
	{
		boolean isValid = false;
		if (StringUtils.isNotEmpty(deliveryMode1)
				&& StringUtils.isNotEmpty(deliveryMode2)
				&& (deliveryMode1.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) || deliveryMode1
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.HD))
				&& (deliveryMode2.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) || deliveryMode2
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.HD)))
		{
			isValid = true;
		}
		else if (StringUtils.isNotEmpty(deliveryMode1)
				&& StringUtils.isNotEmpty(deliveryMode2)
				&& (deliveryMode1.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) || deliveryMode1
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.ED))
				&& (deliveryMode2.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) || deliveryMode2
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.ED)))
		{
			isValid = true;
		}
		else if (StringUtils.isNotEmpty(deliveryMode1)
				&& StringUtils.isNotEmpty(deliveryMode2)
				&& (deliveryMode1.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT) || deliveryMode1
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
				&& (deliveryMode2.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT) || deliveryMode2
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC)))
		{
			isValid = true;
		}

		return isValid;
	}

	/**
	 *
	 *
	 * @Desc fetching reservation details
	 *
	 *
	 *
	 *
	 *
	 *
	 *
	 *
	 *
	 * @param pincode
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	//commented for CAR:127
	/*
	 * @Override public ReservationListWsDTO getReservation(final AbstractOrderModel abstractOrderModel, final String
	 * pincode, final String requestType) throws EtailNonBusinessExceptions
	 */
	@Override
	public ReservationListWsDTO getReservation(final AbstractOrderData abstractOrderData, final String pincode,
			final String requestType, final AbstractOrderModel abstractOrderModel,
			final InventoryReservListRequestWsDTO inventoryRequest, final SalesApplication salesApplication)
			throws EtailNonBusinessExceptions
	{
		final ReservationListWsDTO wsDto = new ReservationListWsDTO();
		InventoryReservListResponse inventoryReservListResponse = null;
		List<ReservationItemWsDTO> reservationDataList = new ArrayList<ReservationItemWsDTO>();
		final String cartGuid = "";
		final List<String> failedUSSIDs = new ArrayList<>();
		final List<CartSoftReservationData> cartSoftForCncReservationDatalist = new ArrayList<CartSoftReservationData>();
		try
		{
			//final List<CartSoftReservationData> cartSoftReservationDatalist = populateDataForSoftReservation(abstractOrderModel);
			//			final List<CartSoftReservationData> cartSoftReservationDatalist = populateDataForSoftReservation(abstractOrderData,
			//					abstractOrderModel);
			/* Added for TISRLUAT-1161 START */
			try
			{
				if (null != abstractOrderModel && CollectionUtils.isNotEmpty(abstractOrderModel.getEntries())
						&& null != inventoryRequest && CollectionUtils.isNotEmpty(inventoryRequest.getItem()))
				{
					for (final InventoryReservRequestWsDTO item : inventoryRequest.getItem())
					{
						for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
						{
							if (item.getUssId().equalsIgnoreCase(entry.getSelectedUSSID()))
							{
								entry.setFulfillmentMode(item.getFulfillmentType());
								entry.setFulfillmentType(item.getFulfillmentType());
								try
								{
									//  INC144316545 START
									if (null != salesApplication && salesApplication.equals(SalesApplication.MOBILE))
									{
										if (null != item.getFulfillmentType())
										{
											if (MarketplacecommerceservicesConstants.TSHIP.equalsIgnoreCase(item.getFulfillmentType()))
											{
												entry.setCurrDelCharge(Double.valueOf(0.0));
											}
										}
									}
									//  INC144316545 END

									SellerInformationModel sellerInfoModel = null;
									if (null != entry.getProduct()
											&& entry.getProduct().getProductCategoryType()
													.equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))
									{
										final List<JewelleryInformationModel> jewelleryInfo = jewelleryService
												.getJewelleryInfoByUssid(entry.getSelectedUSSID());
										if (CollectionUtils.isNotEmpty(jewelleryInfo))
										{
											sellerInfoModel = getMplSellerInformationService().getSellerDetail(
													jewelleryInfo.get(0).getPCMUSSID());
										}
										else
										{
											LOG.error("No entry in JewelleryInformationModel for ussid " + entry.getSelectedUSSID());
										}
									}
									else
									{
										sellerInfoModel = getMplSellerInformationService().getSellerDetail(entry.getSelectedUSSID());
									}

									//final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
									//entry.getSelectedUSSID());
									List<RichAttributeModel> richAttributeModel = null;
									if (sellerInfoModel != null)
									{
										richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
									}
									if (richAttributeModel.get(0).getDeliveryFulfillModeByP1() != null
											&& richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode() != null)

									{
										final String fulfilmentType = richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode()
												.toUpperCase();
										entry.setFulfillmentTypeP1(fulfilmentType);
									}
								}
								catch (final ClientEtailNonBusinessExceptions e)
								{
									LOG.error("Exception occurred while setting fullFillMent Type P1" + e.getErrorCode());
								}
								getModelService().save(entry);
								getModelService().save(entry.getOrder());
							}
						}
					}
				}
				//Duplicate cart Fix
				//				else
				//				{


				//					final CartModel cartModel = getCartService().getSessionCart();
				//

				//					if (null != cartModel && null != cartModel.getEntries() && !cartModel.getEntries().isEmpty()
				//							&& null != inventoryRequest && null != inventoryRequest.getItem())
				//					{

				//						for (final InventoryReservRequestWsDTO item : inventoryRequest.getItem())
				//						{

				//							for (final AbstractOrderEntryModel entry : cartModel.getEntries())
				//							{

				//								if (item.getUssId().equalsIgnoreCase(entry.getSelectedUSSID()))
				//								{

				//									entry.setFulfillmentMode(item.getFulfillmentType());
				//									entry.setFulfillmentType(item.getFulfillmentType());
				//									try
				//									{


				//										final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
				//												entry.getSelectedUSSID());
				//										List<RichAttributeModel> richAttributeModel = null;
				//										if (sellerInfoModel != null)
				//										{

				//											richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
				//										}

				//										if (richAttributeModel.get(0).getDeliveryFulfillModeByP1() != null
				//												&& richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode() != null)
				//
				//										{


				//											final String fulfilmentType = richAttributeModel.get(0).getDeliveryFulfillModeByP1().getCode()
				//													.toUpperCase();
				//											entry.setFulfillmentTypeP1(fulfilmentType);
				//										}
				//									}


				//									catch (final ClientEtailNonBusinessExceptions e)
				//									{

				//										LOG.error("Exception occurred while setting fullFillMent Type P1" + e.getErrorCode());
				//									}

				//									getModelService().save(entry);
				//									getModelService().save(entry.getOrder());
				//								}
				//							}
				//						}
				//
				//					}
				//				}






			}
			catch (final ClientEtailNonBusinessExceptions e)
			{
				LOG.error("Exception occurred while setting fullFillMent Type" + e.getErrorCode());
			}

			/* Added for TISRLUAT-1161 end */
			final List<CartSoftReservationData> cartSoftReservationDatalist = populateDataForSoftReservation(abstractOrderData,
					abstractOrderModel, inventoryRequest, salesApplication);
			if (requestType != null && CollectionUtils.isNotEmpty(cartSoftReservationDatalist) && pincode != null)
			{
				try
				{

					//added for jewellery
					final InventoryReservListRequest reqestData = getInventoryReservationService().convertDatatoWsdto(
							cartSoftReservationDatalist, abstractOrderData.getGuid(), pincode, requestType);
					inventoryReservListResponse = getInventoryReservationService().reserveInventoryAtCheckout(reqestData);
					if (CollectionUtils.isNotEmpty(reqestData.getJewelleryItem()))
					{
						updateCart(reqestData, inventoryReservListResponse, abstractOrderModel);
					}
					//end
					//commented for CAR:127
					/*
					 * inventoryReservListResponse =
					 * getInventoryReservationService().convertDatatoWsdto(cartSoftReservationDatalist,
					 * abstractOrderModel.getGuid(), pincode, requestType);
					 */
					//inventoryReservListResponse = getInventoryReservationService().convertDatatoWsdto(cartSoftReservationDatalist,abstractOrderData.getGuid(), pincode, requestType);
				}
				catch (final ClientEtailNonBusinessExceptions e)
				{
					LOG.error("::::::Mobility  ClientEtailNonBusinessExceptions in calling OMS Inventory reservation:::::::::"
							+ e.getErrorCode());
					if (null != e.getErrorCode()
							&& (MarketplacecclientservicesConstants.O0003_EXCEP.equalsIgnoreCase(e.getErrorCode())
									|| MarketplacecclientservicesConstants.O0004_EXCEP.equalsIgnoreCase(e.getErrorCode()) || MarketplacecclientservicesConstants.O0007_EXCEP
										.equalsIgnoreCase(e.getErrorCode())))
					{
						//prepare cartSoftReservationData object only for HD and Ed
						//skip reservation call for cnc
						int cncModeCount = 0;
						for (final CartSoftReservationData cartSoftReservationData : cartSoftReservationDatalist)
						{
							if (null != cartSoftReservationData
									&& !cartSoftReservationData.getDeliveryMode().equalsIgnoreCase(
											MarketplacecommerceservicesConstants.CnC))
							{
								cartSoftForCncReservationDatalist.add(cartSoftReservationData);
							}
							else
							{
								cncModeCount++;
							}
						}
						LOG.debug("inventoryReservListResponse cncModeCount: " + cncModeCount);
						/*
						 * if (CollectionUtils.isNotEmpty(cartSoftReservationDatalist) && (cartSoftReservationDatalist.size()
						 * == cncModeCount)) { inventoryReservationStatus = true; }
						 */
						if (cartSoftForCncReservationDatalist.size() > 0)
						{
							inventoryReservListResponse = callInventoryReservationCommerce(cartSoftForCncReservationDatalist);
						}
					}
				}

				LOG.debug("inventoryReservListResponse " + inventoryReservListResponse);
				if (inventoryReservListResponse != null)
				{
					reservationDataList = converter(inventoryReservListResponse);
					if (CollectionUtils.isNotEmpty(reservationDataList))
					{
						boolean flag = true;
						for (final ReservationItemWsDTO reservationData : reservationDataList)

						{
							//INC144317815
							if (null != reservationData
									&& null != reservationData.getReservationStatus()
									&& !reservationData.getReservationStatus().equalsIgnoreCase(
											MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SUCCESS))
							{
								failedUSSIDs.add(reservationData.getUSSID());
								flag = false;

							}
						}
						/*
						 * else { LOG.debug("Inventory reservationData for Mobile from OMS is not success ###### =" +
						 * cartGuid); throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9047); }
						 */


						//inserting
						wsDto.setReservationItem(reservationDataList);
						if (flag)
						{
							wsDto.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
						}
						else

						{
							LOG.debug("Inventory reservationData for Mobile from OMS is not success ###### =" + cartGuid);
							wsDto.setFailedUSSIDs(failedUSSIDs);
							wsDto.setError(Localization.getLocalizedString(MarketplacecommerceservicesConstants.B9047));
							wsDto.setErrorCode(MarketplacecommerceservicesConstants.B9047);
							wsDto.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
							//throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9047);
						}
					}
					else
					{
						LOG.debug("Inventory reservationData for Mobile from OMS is empty###### =" + cartGuid);
						throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9047);
					}
				}
				else
				{
					LOG.debug("InventoryReservListResponse for mobile is null ##### =" + cartGuid);
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9201);
				}
			}
			else
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9161);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return wsDto;
	}

	/*
	 * @Desc converting response to dto
	 *
	 *
	 *
	 *
	 * @param inventoryReservListResponse
	 *
	 *
	 *
	 *
	 * @return List<ReservationItemWsDTO>
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	private List<ReservationItemWsDTO> converter(final InventoryReservListResponse inventoryReservListResponse)
			throws EtailNonBusinessExceptions
	{
		final List<ReservationItemWsDTO> datalist = new ArrayList<>();

		if (inventoryReservListResponse != null)
		{
			final List<InventoryReservResponse> mylist = inventoryReservListResponse.getItem();
			if (null != mylist && !mylist.isEmpty())
			{
				for (final InventoryReservResponse data : mylist)
				{
					final ReservationItemWsDTO item = new ReservationItemWsDTO();
					if (null != data.getUSSID())
					{
						item.setUSSID(data.getUSSID());
					}
					if (null != data.getReservationStatus())
					{
						item.setReservationStatus(data.getReservationStatus());
					}
					if (null != data.getAvailableQuantity())
					{
						item.setAvailableQuantity(data.getAvailableQuantity());
					}
					datalist.add(item);
				}
			}
		}
		return datalist;
	}

	/*
	 * @Desc populating data for soft reservation
	 *
	 *
	 *
	 *
	 * @param cartData
	 *
	 *
	 *
	 *
	 * @return List<CartSoftReservationData>
	 *
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	public List<CartSoftReservationData> populateDataForSoftReservation(final CartData cartData) throws EtailNonBusinessExceptions
	{
		CartSoftReservationData cartSoftReservationData = null;
		final List<CartSoftReservationData> cartSoftReservationDataList = new ArrayList<CartSoftReservationData>();

		if (cartData != null)
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				if (null != entry)
				{
					cartSoftReservationData = new CartSoftReservationData();
					if (null != entry.getMplDeliveryMode() && StringUtils.isNotEmpty(entry.getMplDeliveryMode().getSellerArticleSKU()))
					{
						cartSoftReservationData.setUSSID(entry.getMplDeliveryMode().getSellerArticleSKU());
					}
					if (null != entry.getQuantity() && entry.getQuantity().intValue() > 0)
					{
						cartSoftReservationData.setQuantity(Integer.valueOf(entry.getQuantity().toString()));
					}
					if (null != entry.getMplDeliveryMode() && StringUtils.isNotEmpty(entry.getMplDeliveryMode().getCode()))
					{
						String deliveryModeGlobalCode = null;
						if (entry.getMplDeliveryMode().getCode().equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
						{
							deliveryModeGlobalCode = MarketplacecommerceservicesConstants.HD;
						}
						else if (entry.getMplDeliveryMode().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
						{
							deliveryModeGlobalCode = MarketplacecommerceservicesConstants.ED;
						}
						else if (entry.getMplDeliveryMode().getCode()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
						{
							deliveryModeGlobalCode = MarketplacecommerceservicesConstants.CC;
						}

						if (StringUtils.isNotEmpty(deliveryModeGlobalCode))
						{
							cartSoftReservationData.setDeliveryMode(deliveryModeGlobalCode);
						}
					}
					/*** FullFillMent Type Starts Here ***/
					final ProductModel productModel = getProductService().getProductForCode(entry.getProduct().getCode());

					final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) productModel.getRichAttribute();
					if (richAttributeModel != null && richAttributeModel.get(0).getDeliveryFulfillModes() != null)
					{
						final String fullfillmentData = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
						if (fullfillmentData != null && !fullfillmentData.isEmpty())
						{
							cartSoftReservationData.setFulfillmentType(fullfillmentData.toUpperCase());
						}
					}
					/*** FullFillMent Type Ends Here ***/
					if (null != cartData.getStore())
					{
						cartSoftReservationData.setStoreId(cartData.getStore());
					}
				}
				cartSoftReservationDataList.add(cartSoftReservationData);
			} //End of for loop
		}
		return cartSoftReservationDataList;
	}


	/*
	 * @DESC MobileWS105 : get top two wish list for mobile web service
	 *
	 *
	 *
	 *
	 * @param userModel
	 *
	 *
	 *
	 *
	 * @param pincode
	 *
	 *
	 *
	 *
	 *
	 * @return GetWishListWsDTO
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public GetWishListWsDTO getTopTwoWishlistForUser(final UserModel userModel, final String pincode, final CartData cartData)
			throws EtailNonBusinessExceptions
	{
		GetWishListWsDTO getWishListWsDTO = new GetWishListWsDTO();
		List<Wishlist2EntryModel> sortedWishList = new ArrayList<Wishlist2EntryModel>();
		boolean wishlistNonExist = false;

		final List<Wishlist2Model> allWishlists = getWishlistService().getWishlists(userModel);
		if (null == allWishlists || allWishlists.isEmpty())
		{
			LOG.debug("getTopTwoWishlistForUser : allWishlists for user is null or empty ");
			return null;
		}

		final int minGiftQuantity = siteConfigService.getInt("mpl.cart.giftQuantity.lineItem", 0);


		sortedWishList = getGiftYourselfDetailsMobile(minGiftQuantity, allWishlists, pincode, cartData);

		if (null != sortedWishList && !sortedWishList.isEmpty())
		{
			wishlistNonExist = true;
		}
		if (!wishlistNonExist)
		{
			getWishListWsDTO.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			getWishListWsDTO.setError(MarketplacecommerceservicesConstants.NO_GIFT_AVAILABLE);
			return getWishListWsDTO;
		}
		final Map<String, List<Wishlist2EntryModel>> sortedWishListMap = new HashMap<String, List<Wishlist2EntryModel>>();

		if (CollectionUtils.isNotEmpty(sortedWishList))
		{
			for (final Wishlist2EntryModel wishlist2EntryModel : sortedWishList)
			{
				String wishListName = null;
				if (null != wishlist2EntryModel.getWishlist() && null != wishlist2EntryModel.getWishlist().getName())
				{
					wishListName = wishlist2EntryModel.getWishlist().getName();
				}
				final List<Wishlist2EntryModel> wishlist2EntryModelList = sortedWishListMap.get(checkDataValue(wishListName));

				if (CollectionUtils.isNotEmpty(wishlist2EntryModelList))
				{
					wishlist2EntryModelList.add(wishlist2EntryModel);
					sortedWishListMap.put(checkDataValue(wishListName), wishlist2EntryModelList);
				}
				else
				{
					final List<Wishlist2EntryModel> wishList = new ArrayList<Wishlist2EntryModel>();
					wishList.add(wishlist2EntryModel);
					sortedWishListMap.put(checkDataValue(wishListName), wishList);
				}
			}
		}
		else
		{
			LOG.debug("Sorted wishlist is empty or null ");

		}
		getWishListWsDTO = getWishListWebserviceDetails(sortedWishListMap, pincode, cartData);

		return getWishListWsDTO;

	}

	/*
	 * @param sortedWishListMap
	 *
	 *
	 * @return GetWishListWsDTO
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	private GetWishListWsDTO getWishListWebserviceDetails(final Map<String, List<Wishlist2EntryModel>> sortedWishListMap,
			final String pincode, final CartData cartData) throws EtailNonBusinessExceptions
	{
		final GetWishListWsDTO getWishListWsDTO = new GetWishListWsDTO();


		if (!sortedWishListMap.isEmpty())
		{
			final Iterator wishListIterator = sortedWishListMap.entrySet().iterator();
			final List<GetWishListDataWsDTO> getWishListDataWsDTOList = new ArrayList<GetWishListDataWsDTO>();

			while (wishListIterator.hasNext())
			{
				final Map.Entry wishListEntry = (Map.Entry) wishListIterator.next();
				final String wishListName = (String) wishListEntry.getKey();
				final List<Wishlist2EntryModel> wishList = (List<Wishlist2EntryModel>) wishListEntry.getValue();
				final GetWishListDataWsDTO getWishListDataWsDTOObj = new GetWishListDataWsDTO();
				final List<GetWishListProductWsDTO> getWishListProductWsList = new ArrayList<GetWishListProductWsDTO>();

				for (final Wishlist2EntryModel wishlist2EntryModel : wishList)
				{
					GetWishListProductWsDTO getWishListProductWsObj = new GetWishListProductWsDTO();
					final ProductModel productModel = wishlist2EntryModel.getProduct();

					String color = null;
					String size = null;
					String imageUrl = null;
					String capacity = null;

					getWishListProductWsObj = setWSWishlistEligibleDeliveryMode(wishlist2EntryModel, getWishListProductWsObj, pincode,
							cartData);

					List<RichAttributeModel> richAttributeModel = null;
					if (null != productModel.getRichAttribute() && !productModel.getRichAttribute().isEmpty())
					{
						richAttributeModel = (List<RichAttributeModel>) productModel.getRichAttribute();
					}
					if (richAttributeModel != null && null != richAttributeModel.get(0)
							&& richAttributeModel.get(0).getDeliveryFulfillModes() != null
							&& null != richAttributeModel.get(0).getDeliveryFulfillModes().getCode())
					{
						final String fullfillmentData = richAttributeModel.get(0).getDeliveryFulfillModes().getCode().toUpperCase();
						if (fullfillmentData != null && !fullfillmentData.isEmpty())
						{
							getWishListProductWsObj.setFullfillmentType(fullfillmentData);
						}
					}

					if (productModel instanceof PcmProductVariantModel)
					{
						final PcmProductVariantModel selectedVariantModel = (PcmProductVariantModel) productModel;
						ProductData productData = null;
						productData = getProductFacade().getProductForOptions(
								productModel,
								Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
										ProductOption.GALLERY, ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS,
										ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL, ProductOption.VOLUME_PRICES,
										ProductOption.DELIVERY_MODE_AVAILABILITY, ProductOption.SELLER));

						if (null != productData && StringUtils.isNotEmpty(productData.getRootCategory()))
						{
							getWishListProductWsObj.setRootCategory(productData.getRootCategory());
						}
						if (null != productData && null != getCategoryCodeOfProduct(productData)
								&& !getCategoryCodeOfProduct(productData).isEmpty())
						{
							getWishListProductWsObj.setProductCategoryId(getCategoryCodeOfProduct(productData));
						}
						if (StringUtils.isNotEmpty(selectedVariantModel.getColour()))
						{
							color = selectedVariantModel.getColour();
						}
						if (StringUtils.isNotEmpty(selectedVariantModel.getSize()))
						{
							size = selectedVariantModel.getSize();
						}
						if (StringUtils.isNotEmpty(selectedVariantModel.getCapacity()))
						{
							capacity = selectedVariantModel.getCapacity();
						}
						if (null != productData && null != productData.getImages())
						{
							//Set product image(thumbnail) url
							for (final ImageData img : productData.getImages())
							{
								if (null != img && StringUtils.isNotEmpty(img.getFormat())
										&& img.getFormat().equalsIgnoreCase(MarketplacecommerceservicesConstants.THUMBNAIL)
										&& null != img.getUrl())
								{
									imageUrl = img.getUrl().toString();
								}
							}
						}
					}
					getWishListProductWsObj = populateWSWishlistDetails(getWishListProductWsObj, wishlist2EntryModel, color, size,
							imageUrl, capacity);
					getWishListProductWsList.add(getWishListProductWsObj);
				}
				getWishListDataWsDTOObj.setName(checkDataValue(wishListName));
				getWishListDataWsDTOObj.setProducts(getWishListProductWsList);
				getWishListDataWsDTOList.add(getWishListDataWsDTOObj);
			}

			getWishListWsDTO.setWishList(getWishListDataWsDTOList);
		}
		else
		{
			LOG.debug("getWishListWebserviceDetails : Webservice wishlist map is null or empty ");
		}
		return getWishListWsDTO;
	}



	/**
	 * sales category of product
	 *
	 * @param productData
	 * @return String
	 */
	private String getCategoryCodeOfProduct(final ProductData productData)
	{
		String productCategory = null;
		try
		{
			if (null != productData.getCategories() && !productData.getCategories().isEmpty())
			{
				for (final CategoryData category : productData.getCategories())
				{
					if (null != category
							&& null != category.getCode()
							&& null != getConfigurationService().getConfiguration().getString(
									MarketplacecommerceservicesConstants.SALESCATEGORYTYPE)
							&& category.getCode().startsWith(
									getConfigurationService().getConfiguration().getString(
											MarketplacecommerceservicesConstants.SALESCATEGORYTYPE)) && null != category.getName())
					{
						productCategory = category.getCode();
						return productCategory;
					}
				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		return productCategory;
	}



	/**
	 *
	 * @param getWishListProductWsObj
	 * @param wishlist2EntryModel
	 * @param color
	 * @param size
	 * @param imageUrl
	 * @param capacity
	 * @return GetWishListProductWsDTO
	 * @throws EtailNonBusinessExceptions
	 */
	@SuppressWarnings("javadoc")
	private final GetWishListProductWsDTO populateWSWishlistDetails(GetWishListProductWsDTO wishlistProductDtoObj,
			final Wishlist2EntryModel wishlist2EntryModel, final String color, final String size, final String imageUrl,
			final String capacity) throws EtailNonBusinessExceptions
	{
		final ProductModel productModel = wishlist2EntryModel.getProduct();
		List<BuyBoxModel> buyBoxModelList = null;
		String selectedSize = null;

		//Code Change for TISPT-167
		final String usssid = wishlist2EntryModel.getUssid();

		if (StringUtil.isNotEmpty(usssid))
		{
			//final Code Change for TISPT-167
			buyBoxModelList = getBuyBoxDao().getBuyBoxPriceForUssId(usssid);
			wishlistProductDtoObj.setUSSID(checkDataValue(usssid));
		}
		List<BrandModel> brandModelList = null;
		if (null != productModel.getBrands() && !productModel.getBrands().isEmpty())
		{
			brandModelList = (List<BrandModel>) productModel.getBrands();
		}

		if (null != color)
		{
			wishlistProductDtoObj.setColor(checkDataValue(color));
		}
		wishlistProductDtoObj.setDate(wishlist2EntryModel.getAddedDate());
		wishlistProductDtoObj.setDescription(checkDataValue(productModel.getDescription()));
		if (null != imageUrl)
		{
			wishlistProductDtoObj.setImageURL(checkDataValue(imageUrl));
		}

		wishlistProductDtoObj = setWSWishlistPrice(buyBoxModelList, wishlistProductDtoObj);

		if (null != brandModelList && !brandModelList.isEmpty() && null != brandModelList.get(0)
				&& null != brandModelList.get(0).getName())
		{
			wishlistProductDtoObj.setProductBrand(checkDataValue(brandModelList.get(0).getName()));
		}
		wishlistProductDtoObj.setProductName(checkDataValue(productModel.getName()));
		wishlistProductDtoObj.setProductcode(checkDataValue(productModel.getCode()));
		String sellerId = null;
		String sellerName = null;
		if (CollectionUtils.isNotEmpty(buyBoxModelList))
		{
			sellerId = buyBoxModelList.get(0).getSellerId() != null ? buyBoxModelList.get(0).getSellerId() : "";
			sellerName = buyBoxModelList.get(0).getSellerName() != null ? buyBoxModelList.get(0).getSellerName() : "";
		}

		wishlistProductDtoObj.setSellerId(sellerId);
		wishlistProductDtoObj.setSellerName(sellerName);
		selectedSize = (null != wishlist2EntryModel.getSizeSelected() && wishlist2EntryModel.getSizeSelected().booleanValue()) ? MarketplacecommerceservicesConstants.Y
				: MarketplacecommerceservicesConstants.N;

		wishlistProductDtoObj.setSizeSelected(selectedSize);

		if (null != size)
		{
			wishlistProductDtoObj.setSize(size);
		}
		if (null != capacity)
		{
			wishlistProductDtoObj.setCapacity(capacity);
		}
		return wishlistProductDtoObj;
	}


	/*
	 * @Desc For webservice
	 *
	 *
	 *
	 *
	 * @param buyBoxModelList
	 *
	 *
	 *
	 *
	 *
	 *
	 * @param getWishListProductWsObj
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	private GetWishListProductWsDTO setWSWishlistPrice(final List<BuyBoxModel> buyBoxModelList,
			final GetWishListProductWsDTO getWishListProductWsObj) throws EtailNonBusinessExceptions
	{
		Double finalPrice = Double.valueOf(0.0);
		//Code Change for TISPT-167
		if (CollectionUtils.isNotEmpty(buyBoxModelList) && null != buyBoxModelList.get(0))
		{
			final BuyBoxModel oModel = buyBoxModelList.get(0);
			final Double mopPrice = oModel.getPrice();
			final Double mrpPrice = oModel.getMrp();
			if (mopPrice != null && mopPrice.doubleValue() > 0.0)
			{
				finalPrice = mopPrice;
			}
			else if (mrpPrice != null && mrpPrice.doubleValue() > 0.0)
			{
				finalPrice = mrpPrice;
			}
		}
		getWishListProductWsObj.setPrice(finalPrice);
		return getWishListProductWsObj;
	}

	/*
	 * @Desc For webservice
	 *
	 *
	 *
	 *
	 * @param wishlist2EntryModel
	 *
	 *
	 *
	 *
	 * @param getWishListProductWsObj
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	private GetWishListProductWsDTO setWSWishlistEligibleDeliveryMode(final Wishlist2EntryModel wishlist2EntryModel,
			final GetWishListProductWsDTO getWishListProductWsObj, final String pincode, final CartData cartData)
			throws EtailNonBusinessExceptions
	{
		List<MplZoneDeliveryModeValueModel> deliveryModes = null;
		if (wishlist2EntryModel.getUssid() != null)
		{
			String fullfillmentType = null;
			List<RichAttributeModel> richAttributeModel = null;
			if (null != wishlist2EntryModel.getProduct() && null != wishlist2EntryModel.getProduct().getRichAttribute()
					&& !wishlist2EntryModel.getProduct().getRichAttribute().isEmpty())
			{
				richAttributeModel = (List<RichAttributeModel>) wishlist2EntryModel.getProduct().getRichAttribute();
			}
			if (richAttributeModel != null && null != richAttributeModel.get(0)
					&& richAttributeModel.get(0).getDeliveryFulfillModes() != null
					&& null != richAttributeModel.get(0).getDeliveryFulfillModes().getCode())
			{
				fullfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode().toUpperCase();
			}
			if (StringUtil.isNotEmpty(pincode))
			{
				final List<MobdeliveryModeWsDTO> deliveryList = new ArrayList<MobdeliveryModeWsDTO>();

				List<PinCodeResponseData> pinCodeResponseDataList = null;
				List<MarketplaceDeliveryModeData> deliveryModesData = null;
				// Changes for Duplicate Cart fix
				final List<PincodeServiceData> pincodeRequestDataList = fetchWishlistPincodeRequestDataMobile(wishlist2EntryModel,
						cartData);
				if (CollectionUtils.isNotEmpty(pincodeRequestDataList))
				{
					pinCodeResponseDataList = getServiceablePinCodeCart(pincode, pincodeRequestDataList);
					if (CollectionUtils.isNotEmpty(pinCodeResponseDataList))
					{
						try
						{
							deliveryModesData = getDeliveryModeTopTwoWishlistMobile(wishlist2EntryModel, pinCodeResponseDataList,
									cartData);
						}
						catch (final Exception e)
						{
							LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_ERROR, e);
							//throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9041);
						}

						{
							for (final MarketplaceDeliveryModeData deliveryMode : deliveryModesData)
							{
								final MobdeliveryModeWsDTO delivery = new MobdeliveryModeWsDTO();
								delivery.setCode(Optional.ofNullable(deliveryMode.getCode()).orElse(""));
								delivery.setName(Optional.ofNullable(deliveryMode.getName()).orElse(""));
								if (StringUtils.isNotEmpty(fullfillmentType) && fullfillmentType.equalsIgnoreCase("tship"))
								{
									delivery.setDeliveryCost("0.0");
								}
								else
								{
									delivery.setDeliveryCost(Optional.ofNullable(deliveryMode.getDeliveryCost().getFormattedValue())
											.orElse(""));
								}
								delivery.setDesc(Optional.ofNullable(deliveryMode.getDescription()).orElse(""));
								deliveryList.add(delivery);
							}
						}
					}
					getWishListProductWsObj.setElligibleDeliveryMode(deliveryList);
				}
			}
			else
			{

				if (null != wishlist2EntryModel.getUssid()
						&& null != getMplDeliveryCostService().getDeliveryModesAndCost(MarketplacecommerceservicesConstants.INR,
								wishlist2EntryModel.getUssid()))
				{
					deliveryModes = getMplDeliveryCostService().getDeliveryModesAndCost(MarketplacecommerceservicesConstants.INR,
							wishlist2EntryModel.getUssid());
				}

				if (null != deliveryModes && !deliveryModes.isEmpty())
				{
					final List<MobdeliveryModeWsDTO> deliveryList = new ArrayList<MobdeliveryModeWsDTO>();
					for (final MplZoneDeliveryModeValueModel deliveryEntry : deliveryModes)
					{
						final MobdeliveryModeWsDTO delivery = new MobdeliveryModeWsDTO();
						// Changes for Duplicate Cart fix
						final PriceData priceData = formPriceData(deliveryEntry.getValue(), cartData);
						if (null != deliveryEntry.getDeliveryMode() && null != deliveryEntry.getDeliveryMode().getCode())
						{
							delivery.setCode(deliveryEntry.getDeliveryMode().getCode());
						}
						if (null != deliveryEntry.getDeliveryMode() && null != deliveryEntry.getDeliveryMode().getDescription())
						{
							delivery.setDesc(deliveryEntry.getDeliveryMode().getDescription());
						}
						if (null != deliveryEntry.getDeliveryMode() && null != deliveryEntry.getDeliveryMode().getName())
						{
							delivery.setName(deliveryEntry.getDeliveryMode().getName());
						}

						if (StringUtils.isNotEmpty(fullfillmentType) && fullfillmentType.equalsIgnoreCase("tship"))
						{
							delivery.setDeliveryCost("0.0");
						}
						else
						{
							if (null != priceData)
							{
								delivery.setDeliveryCost(priceData.getValue().toString());
							}
						}
						deliveryList.add(delivery);
					}
					getWishListProductWsObj.setElligibleDeliveryMode(deliveryList);
				}
			}

		}

		return getWishListProductWsObj;
	}

	private String checkDataValue(final String value)
	{
		return StringUtil.isNotEmpty(value) ? value : "";
	}

	/*
	 * @DESC TISST-6994,TISST-6990 adding to cart COD eligible or not with Pincode serviceabilty and sship product
	 *
	 *
	 * @param deliveryModeMap
	 *
	 *
	 *
	 *
	 * @param pincodeResponseData
	 *
	 *
	 *
	 *
	 * @return boolean
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean addCartCodEligible(final Map<String, List<MarketplaceDeliveryModeData>> deliveryModeMap,
			final List<PinCodeResponseData> pincodeResponseData, CartModel cartModel, final CartData cartData)
			throws EtailNonBusinessExceptions
	{

		boolean codEligible = true;
		ServicesUtil.validateParameterNotNull(deliveryModeMap, "deliveryModeMap cannot be null");
		ServicesUtil.validateParameterNotNull(pincodeResponseData, "pincodeResponseData cannot be null");
		if (cartModel == null)
		{
			cartModel = getCartService().getSessionCart();
		}

		// Check pincode response , if any of the item is not cod eligible , cart will not be cod eligible

		List<OrderEntryData> cartDataEntryList = null;

		if (CollectionUtils.isNotEmpty(cartData.getEntries()))
		{

			cartDataEntryList = cartData.getEntries();
		}

		//CAR-129 using cart data instead of cart model
		if (cartModel != null && CollectionUtils.isNotEmpty(cartDataEntryList) && cartDataEntryList.size() > 0)
		{
			for (final PinCodeResponseData pinCodeEntry : pincodeResponseData)
			{
				for (final OrderEntryData cartEntry : cartDataEntryList)
				{
					if (cartEntry != null && cartEntry.getSelectedUssid() != null && cartEntry.getMplDeliveryMode() != null
							&& cartEntry.getMplDeliveryMode().getCode() != null && pinCodeEntry != null
							&& pinCodeEntry.getValidDeliveryModes() != null && codEligible
							&& cartEntry.getSelectedUssid().equalsIgnoreCase(pinCodeEntry.getUssid()))
					{
						final String selectedDeliveryMode = cartEntry.getMplDeliveryMode().getCode();

						for (final DeliveryDetailsData deliveryDetailsData : pinCodeEntry.getValidDeliveryModes())
						{
							// Checking only  for selected delivery mode in cart page with pincode serviceablity response
							if (((selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) && deliveryDetailsData
									.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.HD)) || (selectedDeliveryMode
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) && deliveryDetailsData
									.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.ED)))
									&& deliveryDetailsData.getIsCOD() != null && !deliveryDetailsData.getIsCOD().booleanValue())
							{
								codEligible = false;
								break;
							}
						}
					}
				}
			}
		}

		// check with buy box , for any ship product cart will not be COD eligible

		if (codEligible)
		{
			final Iterator deliveryMapIterator = deliveryModeMap.entrySet().iterator();

			while (deliveryMapIterator.hasNext() && codEligible)
			{
				final Map.Entry entry = (Map.Entry) deliveryMapIterator.next();

				final List<MarketplaceDeliveryModeData> deliveryModeData = (List<MarketplaceDeliveryModeData>) entry.getValue();

				if (deliveryModeData != null)
				{
					for (final MarketplaceDeliveryModeData marketplaceDeliveryModeData : deliveryModeData)
					{
						if (marketplaceDeliveryModeData.getCode() != null && marketplaceDeliveryModeData.getSellerArticleSKU() != null)
						{
							final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
									marketplaceDeliveryModeData.getSellerArticleSKU());

							List<RichAttributeModel> richAttributeModel = null;
							if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
							{
								richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
							}

							if (richAttributeModel != null && richAttributeModel.get(0).getDeliveryFulfillModes() != null)
							{
								final String fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
								if (StringUtils.isNotEmpty(fulfillmentType)
										&& fulfillmentType.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIP))
								{
									//Changes to TRUE & FALSE
									final String isSshipCodEligble = (richAttributeModel.get(0).getIsSshipCodEligible() != null ? richAttributeModel
											.get(0).getIsSshipCodEligible().getCode()
											: MarketplacecommerceservicesConstants.FALSE);
									if (StringUtils.isNotEmpty(isSshipCodEligble)
											&& isSshipCodEligble.equalsIgnoreCase(MarketplacecommerceservicesConstants.FALSE))
									{
										codEligible = false;
										break;
									}
								}
							}
						}
					}
				}
			}
		}

		//Saving cod status in cart
		if (cartModel != null)
		{

			cartModel.setIsCODEligible(Boolean.valueOf(codEligible));
			getModelService().save(cartModel);
		}

		return codEligible;
	}


	/**
	 *
	 * @return the keyGenerator
	 */
	public KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}


	/**
	 * @param keyGenerator
	 *           the keyGenerator to set
	 */
	@Required
	public void setKeyGenerator(final KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	/**
	 * @return the mplCommerceCartDao
	 */
	public MplCommerceCartDao getMplCommerceCartDao()
	{
		return mplCommerceCartDao;
	}

	/**
	 * @param mplCommerceCartDao
	 *           the mplCommerceCartDao to set
	 */
	@Required
	public void setMplCommerceCartDao(final MplCommerceCartDao mplCommerceCartDao)
	{
		this.mplCommerceCartDao = mplCommerceCartDao;
	}

	/**
	 * @return the cartConverter
	 */
	public Converter<CartModel, CartData> getCartConverter()
	{
		return cartConverter;
	}


	/**
	 * @param cartConverter
	 *           the cartConverter to set
	 */
	public void setCartConverter(final Converter<CartModel, CartData> cartConverter)
	{
		this.cartConverter = cartConverter;
	}


	/**
	 * @return the siteConfigService
	 */
	public SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	/**
	 * @param siteConfigService
	 *           the siteConfigService to set
	 */
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	/**
	 * @return the cartModificationConverter
	 */
	public Converter<CommerceCartModification, CartModificationData> getCartModificationConverter()
	{
		return cartModificationConverter;
	}


	/**
	 * @param cartModificationConverter
	 *           the cartModificationConverter to set
	 */
	public void setCartModificationConverter(
			final Converter<CommerceCartModification, CartModificationData> cartModificationConverter)
	{
		this.cartModificationConverter = cartModificationConverter;
	}

	/**
	 * @return the baseSiteService
	 */
	@Override
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	@Override
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	@Override
	public PriceData addConvCharge(final CartModel source, final CartData prototype)
	{
		prototype.setConvenienceChargeForCOD(createPrice(source, source.getConvenienceCharges()));
		return prototype.getConvenienceChargeForCOD();
	}

	@Override
	public PriceData setTotalWithConvCharge(final CartModel source, final CartData prototype)
	{
		prototype.setTotalPriceWithConvCharge(createPrice(source, source.getTotalPriceWithConv()));
		return prototype.getTotalPriceWithConvCharge();
	}


	@Override
	public PriceData setTotalWithConvCharge(final OrderModel source, final OrderData prototype)
	{
		prototype.setTotalPriceWithConvCharge(createPrice(source, source.getTotalPriceWithConv()));
		return prototype.getTotalPriceWithConvCharge();
	}

	/**
	 * @return CommerceCartModification
	 * @param parameters
	 *           Update cart quantity
	 * @throws CommerceCartModificationException
	 */
	@Override
	public CommerceCartModification updateQuantityForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		return getCommerceUpdateCartEntryStrategy().updateQuantityForCartEntry(parameters);
	}

	/**
	 * @return CommerceCartModification
	 * @param parameters
	 *           Update cart quantity
	 * @throws CommerceCartModificationException
	 */

	@Override
	public CommerceCartModification updateToShippingModeForCartEntry(final CommerceCartParameter parameters)
			throws CommerceCartModificationException
	{
		return getCommerceUpdateCartEntryStrategy().updateToShippingModeForCartEntry(parameters);
	}

	/**
	 * @description: It is creating price data for a price value
	 * @param source
	 * @param val
	 *
	 * @return PriceData
	 */
	protected PriceData createPrice(final AbstractOrderModel source, final Double val)
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
	 * @Desc fetching state details for a state name
	 *
	 *
	 *
	 *
	 * @param stateName
	 *
	 *
	 *
	 *
	 * @return StateModel
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public StateModel fetchStateDetails(final String stateName) throws EtailNonBusinessExceptions
	{
		if (StringUtil.isNotEmpty(stateName))
		{
			final List<StateModel> stateModelList = getMplCommerceCartDao().fetchStateDetails(stateName);

			if (stateModelList != null && stateModelList.size() == 1)
			{
				return stateModelList.get(0);
			}
			else
			{
				LOG.debug("MplCommerceCartServiceImpl Cannot fetch stateModelList for state name " + stateName);
			}
		}
		return null;
	}

	/*
	 * @Desc to generate Sub order id
	 *
	 *
	 *
	 *
	 * @return String
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public String generateSubOrderId() throws EtailNonBusinessExceptions
	{
		return getSubOrderIdGenerator().generate().toString();
	}

	/*
	 * @Desc to generate Order Line id and transaction id
	 *
	 *
	 *
	 *
	 * @return String
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public String generateOrderLineId() throws EtailNonBusinessExceptions
	{
		return getOrderLineIdGenerator().generate().toString();
	}


	/*
	 * @Desc to generate Order Id
	 *
	 *
	 *
	 *
	 * @return String
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public String generateOrderId() throws EtailNonBusinessExceptions
	{
		return getOrderIdGenerator().generate().toString();
	}


	/*
	 * @Desc used for inventory soft reservation from Commerce Checkout and Payment
	 *
	 *
	 *
	 *
	 * @param requestType
	 *
	 *
	 *
	 *
	 * @param abstractOrderModel
	 *
	 *
	 *
	 *
	 * @return boolean
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	//commented for CAR:127
	/*
	 * @Override public boolean isInventoryReserved(final AbstractOrderModel abstractOrderModel, final String
	 * requestType, final String defaultPinCodeId) throws EtailNonBusinessExceptions
	 *///Parameter AbstractOrderModel added extra for TPR-629
	@Override
	public boolean isInventoryReserved(AbstractOrderData abstractOrderData, final String requestType,
			final String defaultPinCodeId, final AbstractOrderModel abstractOrderModel,
			final InventoryReservListRequestWsDTO inventoryRequest, final SalesApplication salesApplication)
			throws EtailNonBusinessExceptions
	{
		//commented for CAR:127
		//final List<CartSoftReservationData> cartSoftReservationDatalist = populateDataForSoftReservation(abstractOrderModel);
		if (null == abstractOrderData)
		{
			abstractOrderData = getOrderConverter().convert((OrderModel) abstractOrderModel);
		}
		final List<CartSoftReservationData> cartSoftReservationDatalist = populateDataForSoftReservation(abstractOrderData,
				abstractOrderModel, inventoryRequest, salesApplication);
		final List<CartSoftReservationData> cartSoftForCncReservationDatalist = new ArrayList<CartSoftReservationData>();


		boolean inventoryReservationStatus = true;
		InventoryReservListResponse inventoryReservListResponse = null;
		List<AbstractOrderEntryModel> cartEntryList = null;
		if (requestType != null && !cartSoftReservationDatalist.isEmpty() && defaultPinCodeId != null)
		{

			for (final CartSoftReservationData dataObj : cartSoftReservationDatalist)
			{
				//duplicate Cart Fix
				//final CartModel cartModel = getCartService().getSessionCart();
				cartEntryList = abstractOrderModel.getEntries();
				for (final AbstractOrderEntryModel cartEntryModel : cartEntryList)
				{
					if (null != cartEntryModel)
					{
						if (cartEntryModel.getSelectedUSSID().equalsIgnoreCase(dataObj.getUSSID()))
						{
							cartEntryModel.setFulfillmentMode(dataObj.getFulfillmentType());
							cartEntryModel.setFulfillmentType(dataObj.getFulfillmentType());
							cartEntryModel.setFulfillmentTypeP1(dataObj.getFulfillmentType());
							//	cartEntryModel.setFulfillmentTypeP2(dataObj.getFulfillmentType());
						}
					}
				}
			}
			LOG.debug("::::::Try to save cart Entry to :::::::::");
			if (null != cartEntryList && cartEntryList.size() > 0)
			{
				LOG.debug("::::::In side If Statement :::::::::");
				getModelService().saveAll(cartEntryList);
			}
			LOG.debug("::::::SuccessFully Saved to All Cart Entries:::::::::");

			try
			{

				final InventoryReservListRequest reqestData = getInventoryReservationService().convertDatatoWsdto(
						cartSoftReservationDatalist, abstractOrderData.getGuid(), defaultPinCodeId, requestType);
				inventoryReservListResponse = getInventoryReservationService().reserveInventoryAtCheckout(reqestData);
				if (CollectionUtils.isNotEmpty(reqestData.getJewelleryItem()))
				{
					updateCart(reqestData, inventoryReservListResponse, abstractOrderModel);
				}



				//commented for CAR:127
				/*
				 * inventoryReservListResponse =
				 * getInventoryReservationService().convertDatatoWsdto(cartSoftReservationDatalist,
				 * abstractOrderModel.getGuid(), defaultPinCodeId, requestType);
				 */
				//inventoryReservListResponse = getInventoryReservationService().convertDatatoWsdto(cartSoftReservationDatalist,abstractOrderData.getGuid(), defaultPinCodeId, requestType);
			}
			catch (final ClientEtailNonBusinessExceptions e)
			{
				LOG.error("::::::Exception in calling OMS Inventory reservation:::::::::" + e.getErrorCode());
				if (null != e.getErrorCode()
						&& (MarketplacecclientservicesConstants.O0003_EXCEP.equalsIgnoreCase(e.getErrorCode())
								|| MarketplacecclientservicesConstants.O0004_EXCEP.equalsIgnoreCase(e.getErrorCode()) || MarketplacecclientservicesConstants.O0007_EXCEP
									.equalsIgnoreCase(e.getErrorCode())))
				{
					//prepare cartSoftReservationData object only for HD and Ed
					//skip reservation call for cnc
					int cncModeCount = 0;
					for (final CartSoftReservationData cartSoftReservationData : cartSoftReservationDatalist)
					{
						if (null != cartSoftReservationData
								&& !cartSoftReservationData.getDeliveryMode().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
						{
							cartSoftForCncReservationDatalist.add(cartSoftReservationData);
						}
						else
						{
							cncModeCount++;
						}
					}
					if (CollectionUtils.isNotEmpty(cartSoftReservationDatalist)
							&& (cartSoftReservationDatalist.size() == cncModeCount))
					{
						return true;
					}
					if (cartSoftForCncReservationDatalist.size() > 0)
					{
						inventoryReservListResponse = callInventoryReservationCommerce(cartSoftForCncReservationDatalist);
					}
				}
			}
			catch (final JAXBException e)
			{
				// YTODO Auto-generated catch block
				e.printStackTrace();
			}

			LOG.debug("inventoryReservListResponse " + inventoryReservListResponse);

			if (inventoryReservListResponse != null && CollectionUtils.isNotEmpty(inventoryReservListResponse.getItem()))
			{
				final List<InventoryReservResponse> inventoryReservResponseList = inventoryReservListResponse.getItem();
				if (!inventoryReservResponseList.isEmpty())
				{
					for (final InventoryReservResponse inventoryReservResponse : inventoryReservResponseList)
					{
						if (inventoryReservResponse.getReservationStatus() != null
								&& !inventoryReservResponse.getReservationStatus().equalsIgnoreCase(
										MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SUCCESS))
						{
							inventoryReservationStatus = false;
							break;
						}
					}
				}
				else
				{
					inventoryReservationStatus = false;
				}
			}
			else
			{
				inventoryReservationStatus = false;
			}
		}
		else
		{
			inventoryReservationStatus = false;
		}
		return inventoryReservationStatus;
	}

	/**
	 * @Desc Used as part of oms fallback for inventory soft reservation
	 * @param cartDataList
	 * @return InventoryReservListResponse
	 */
	@Override
	public InventoryReservListResponse callInventoryReservationCommerce(final List<CartSoftReservationData> cartDataList)
	{

		LOG.info("*********************** Commerce Inventory Reservation *************");

		final Map<String, Integer> reqUssidQuantityMap = new HashMap<String, Integer>();
		Map<String, Integer> availableStockMap = null;
		final List<String> ussidList = new ArrayList<String>();

		for (final CartSoftReservationData reserveObj : cartDataList)
		{
			//TISTI-128
			if (!(reserveObj.getIsAFreebie() != null && reserveObj.getIsAFreebie().equals(MarketplacecommerceservicesConstants.Y)))
			{
				ussidList.add(reserveObj.getUSSID());
				reqUssidQuantityMap.put(reserveObj.getUSSID(), reserveObj.getQuantity());
			}
		}

		String sellerArticleSKUs = GenericUtilityMethods.getcommaSepUSSIDs(ussidList);
		if (null != sellerArticleSKUs && sellerArticleSKUs.length() > 0)
		{
			sellerArticleSKUs = sellerArticleSKUs.substring(0, sellerArticleSKUs.length() - 1);
			availableStockMap = mplStockService.getAllStockLevelDetail(sellerArticleSKUs);
		}

		//Preparing response
		final InventoryReservListResponse inventoryReservListResponse = new InventoryReservListResponse();
		final List<InventoryReservResponse> inventoryReservResponseList = new ArrayList<InventoryReservResponse>();

		for (final Map.Entry<String, Integer> entry : reqUssidQuantityMap.entrySet())
		{
			final InventoryReservResponse inventoryReservResponse = new InventoryReservResponse();
			inventoryReservResponse.setUSSID(entry.getKey());

			final Integer inventoryInComm = (availableStockMap == null || availableStockMap.get(entry.getKey()) == null) ? Integer
					.valueOf(0) : availableStockMap.get(entry.getKey());

			final String inventoryReservationStatus = (null != inventoryInComm && entry.getValue().intValue() <= inventoryInComm
					.intValue()) ? MarketplacecommerceservicesConstants.SUCCESS : MarketplacecommerceservicesConstants.FAILURE;

			inventoryReservResponse.setReservationStatus(inventoryReservationStatus);
			inventoryReservResponseList.add(inventoryReservResponse);
		}
		inventoryReservListResponse.setItem(inventoryReservResponseList);

		try
		{
			final StringWriter stringWriter = new StringWriter();
			final JAXBContext jaxbContext = JAXBContext.newInstance(InventoryReservListResponse.class);
			final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(inventoryReservListResponse, stringWriter);
			LOG.debug("************Commerce inventory response xml in case of OMS Fallback*************************"
					+ stringWriter.toString());
		}
		catch (final Exception e)
		{
			LOG.error(e.getMessage());
		}
		return inventoryReservListResponse;
	}

	/*
	 * @description:Populate data to CartSoftReservationData
	 *
	 *
	 *
	 *
	 * @return:List<CartSoftReservationData>
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	//commented for CAR:127
	/*
	 * private List<CartSoftReservationData> populateDataForSoftReservation(final AbstractOrderModel abstractOrderModel)
	 * throws EtailNonBusinessExceptions
	 */
	private List<CartSoftReservationData> populateDataForSoftReservation(final AbstractOrderData abstractOrderData,
			final AbstractOrderModel abstractOrderModel, final InventoryReservListRequestWsDTO inventoryRequest,
			final SalesApplication salesApplication) throws EtailNonBusinessExceptions
	{
		List<PinCodeResponseData> pincoderesponseDataList = null;
		if (null != salesApplication && salesApplication.equals(SalesApplication.WEB))
		{
			pincoderesponseDataList = getSessionService().getAttribute(
					MarketplacecommerceservicesConstants.PINCODE_RESPONSE_DATA_TO_SESSION);
		}

		LOG.debug("******responceData******** " + pincoderesponseDataList);
		CartSoftReservationData cartSoftReservationData = null;
		final List<CartSoftReservationData> cartSoftReservationDataList = new ArrayList<CartSoftReservationData>();
		try
		{
			//commented for CAR:127
			//if (abstractOrderModel != null)
			if (abstractOrderData != null)
			{
				//commented for CAR:127
				//for (final AbstractOrderEntryModel entryModel : abstractOrderModel.getEntries())
				for (final OrderEntryData entryModel : abstractOrderData.getEntries())
				{
					//Start Code added for TISPRD-2758
					//commented for CAR:127
					//final Tuple2<?, ?> tupleData = getFreebieInventoryList(abstractOrderModel, entryModel);
					final Tuple2<?, ?> tupleData = getFreebieInventoryList(abstractOrderData, entryModel, abstractOrderModel);
					if (tupleData != null && tupleData.getFirst() != null && tupleData.getFirst().equals(Boolean.TRUE))
					{
						final List<CartSoftReservationData> dataList = (List<CartSoftReservationData>) tupleData.getSecond();
						if (CollectionUtils.isNotEmpty(dataList))
						{
							cartSoftReservationDataList.addAll(dataList);
						}
						else
						{
							LOG.error("TISPRD-2758 Freebie Data Population  is empty  ");
						}
					} //End Code added for TISPRD-2758
					else
					{
						String deliveryModeGlobalCode = null;
						String deliveryMode = null;
						cartSoftReservationData = new CartSoftReservationData();
						String fulfillmentType = null;

						//commented for CAR:127
						/*
						 * if (entryModel.getSelectedUSSID() != null) {
						 * cartSoftReservationData.setUSSID(entryModel.getSelectedUSSID()); }
						 */
						if (entryModel.getSelectedUssid() != null)
						{
							cartSoftReservationData.setUSSID(entryModel.getSelectedUssid());
						}
						else
						{
							LOG.debug("populateDataForSoftReservation : entry.getMplDeliveryMode() is null or empty");
						}
						if (entryModel.getQuantity() != null && entryModel.getQuantity().intValue() > 0)
						{
							cartSoftReservationData.setQuantity(Integer.valueOf(entryModel.getQuantity().toString()));
						}
						else
						{
							LOG.debug("populateDataForSoftReservation :  entryModel.getQuantity() is null or empty");
						}

						if (entryModel.getDeliveryPointOfService() != null)
						{
							cartSoftReservationData.setStoreId(entryModel.getDeliveryPointOfService().getSlaveId());
						}
						//commented for CAR:127
						/*
						 * if (entryModel.getAssociatedItems() != null && entryModel.getAssociatedItems().size() > 0 &&
						 * entryModel.getGiveAway().booleanValue())
						 */
						if (entryModel.getAssociatedItems() != null && entryModel.getAssociatedItems().size() > 0
								&& entryModel.isGiveAway())
						{
							if (entryModel.getAssociatedItems().size() >= 2)
							{
								//commented for CAR:127
								//deliveryMode = setParentforABgetC(cartSoftReservationData, entryModel, abstractOrderModel);
								deliveryMode = setParentforABgetC(cartSoftReservationData, entryModel, abstractOrderData);
								cartSoftReservationData.setIsAFreebie(MarketplacecommerceservicesConstants.Y);
								if (null != deliveryMode)
								{
									if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
									{
										deliveryModeGlobalCode = MarketplacecommerceservicesConstants.HD;
									}
									else if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
									{
										deliveryModeGlobalCode = MarketplacecommerceservicesConstants.ED;
									}
									else if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
									{
										deliveryModeGlobalCode = MarketplacecommerceservicesConstants.CnC;
									}
									if (StringUtil.isNotEmpty(deliveryModeGlobalCode))
									{
										cartSoftReservationData.setDeliveryMode(deliveryModeGlobalCode);
									}
									//set DeliveryPointOfService as parent for freebie
									final String parentUssid = cartSoftReservationData.getParentUSSID();
									if (null != parentUssid
											&& deliveryModeGlobalCode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
									{
										//commented for CAR:127
										//for (final AbstractOrderEntryModel cEntry : abstractOrderModel.getEntries())
										for (final OrderEntryData cEntry : abstractOrderData.getEntries())
										{
											//commented for CAR:127
											//if (cEntry.getSelectedUSSID().equalsIgnoreCase(parentUssid))
											if (cEntry.getSelectedUssid().equalsIgnoreCase(parentUssid))
											{
												//commented for CAR:127
												//final PointOfServiceModel parentPosModel = cEntry.getDeliveryPointOfService();
												final PointOfServiceData parentPosData = cEntry.getDeliveryPointOfService();
												//commented for CAR:127
												/*
												 * if (null != parentPosModel) {
												 * cartSoftReservationData.setStoreId(parentPosModel.getSlaveId()); }
												 */
												if (null != parentPosData)
												{
													cartSoftReservationData.setStoreId(parentPosData.getSlaveId());
												}
											}
										}
									}
								}
							}
							else
							{
								final String ussId = entryModel.getAssociatedItems().get(0);
								cartSoftReservationData.setParentUSSID(ussId);
								//commented for CAR:127
								//final String deliveryModeForFreebie = getDeliverModeForABgetC(ussId, abstractOrderModel);
								final String deliveryModeForFreebie = getDeliverModeForABgetC(ussId, abstractOrderData);
								if (null != deliveryModeForFreebie)
								{
									if (deliveryModeForFreebie.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
									{
										deliveryModeGlobalCode = MarketplacecommerceservicesConstants.HD;
									}
									else if (deliveryModeForFreebie
											.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
									{
										deliveryModeGlobalCode = MarketplacecommerceservicesConstants.ED;
									}
									else if (deliveryModeForFreebie.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
									{
										deliveryModeGlobalCode = MarketplacecommerceservicesConstants.CnC;
									}
									if (StringUtil.isNotEmpty(deliveryModeGlobalCode))
									{
										cartSoftReservationData.setDeliveryMode(deliveryModeGlobalCode);
									}
									//set DeliveryPointOfService as parent for freebie
									if (null != ussId && deliveryModeGlobalCode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
									{
										//commented for CAR:127
										/*
										 * for (final AbstractOrderEntryModel cEntry : abstractOrderModel.getEntries()) { if
										 * (cEntry.getSelectedUSSID().equalsIgnoreCase(ussId)) { final PointOfServiceModel
										 * parentPosModel = cEntry.getDeliveryPointOfService(); if (null != parentPosModel) {
										 * cartSoftReservationData.setStoreId(parentPosModel.getSlaveId()); } } }
										 */
										for (final OrderEntryData cEntry : abstractOrderData.getEntries())
										{
											if (cEntry.getSelectedUssid().equalsIgnoreCase(ussId))
											{
												final PointOfServiceData parentPosData = cEntry.getDeliveryPointOfService();

												if (null != parentPosData)
												{
													cartSoftReservationData.setStoreId(parentPosData.getSlaveId());
												}
											}
										}
									}
								}
								cartSoftReservationData.setIsAFreebie(MarketplacecommerceservicesConstants.Y);
							}
						}
						//commented for CAR:127
						/*
						 * if (entryModel.getMplDeliveryMode() != null && entryModel.getMplDeliveryMode().getDeliveryMode() !=
						 * null && entryModel.getMplDeliveryMode().getDeliveryMode().getCode() != null &&
						 * StringUtil.isNotEmpty(entryModel.getMplDeliveryMode().getDeliveryMode().getCode()))
						 */
						if (entryModel.getMplDeliveryMode() != null && entryModel.getMplDeliveryMode().getCode() != null
								&& StringUtil.isNotEmpty(entryModel.getMplDeliveryMode().getCode()))
						{
							if (deliveryMode == null)
							{
								//commented for CAR:127
								//deliveryMode = entryModel.getMplDeliveryMode().getDeliveryMode().getCode();
								deliveryMode = entryModel.getMplDeliveryMode().getCode();
							}

							if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
							{
								deliveryModeGlobalCode = MarketplacecommerceservicesConstants.HD;
							}
							else if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
							{
								deliveryModeGlobalCode = MarketplacecommerceservicesConstants.ED;
							}
							else if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
							{
								deliveryModeGlobalCode = MarketplacecommerceservicesConstants.CnC;
							}

							if (StringUtil.isNotEmpty(deliveryModeGlobalCode))
							{
								cartSoftReservationData.setDeliveryMode(deliveryModeGlobalCode);
							}
						}
						else
						{
							LOG.debug("populateDataForSoftReservation :  entryModel.getMplDeliveryMode().getCode() is null or empty");
						}
						//commented for CAR:127
						//if (entryModel.getSelectedUSSID() != null)
						if (entryModel.getSelectedUssid() != null)
						{
							//commented for CAR:127
							/*
							 * final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
							 * entryModel.getSelectedUSSID());
							 */
							SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
									entryModel.getSelectedUssid(),
									abstractOrderModel.getStore().getCatalogs().get(0).getActiveCatalogVersion());

							if (null == sellerInfoModel)
							{
								final List<JewelleryInformationModel> jeweleryInfo = jewelleryService.getJewelleryInfoByUssid(entryModel
										.getSelectedUssid());
								if (CollectionUtils.isNotEmpty(jeweleryInfo))
								{
									sellerInfoModel = getMplSellerInformationService().getSellerDetail(jeweleryInfo.get(0).getPCMUSSID());
								}

							}
							List<RichAttributeModel> richAttributeModel = null;
							if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
							{
								richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
								if (richAttributeModel != null && richAttributeModel.get(0) != null
										&& richAttributeModel.get(0).getDeliveryFulfillModes() != null
										&& richAttributeModel.get(0).getDeliveryFulfillModes().getCode() != null)
								{
									fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
									cartSoftReservationData.setFulfillmentType(fulfillmentType.toUpperCase());
									//commented for CAR:127
									//if (entryModel.getGiveAway().booleanValue())
									if (entryModel.isGiveAway())
									{
										//commented for CAR:127
										//setFullFillmentTypeForFreebie(cartSoftReservationData, abstractOrderModel);
										setFullFillmentTypeForFreebie(cartSoftReservationData, abstractOrderData);
									}
								}
								else
								{
									LOG.debug("populateDataForSoftReservation :  Fulfillment type not received for the "
											+ entryModel.getSelectedUssid());
								}
								if (richAttributeModel != null && richAttributeModel.get(0) != null
										&& richAttributeModel.get(0).getShippingModes() != null
										&& richAttributeModel.get(0).getShippingModes().getCode() != null)
								{
									cartSoftReservationData.setTransportMode(MplGlobalCodeConstants.GLOBALCONSTANTSMAP
											.get(richAttributeModel.get(0).getShippingModes().getCode().toUpperCase()));
								}
							}
						}

						//if (null != entryModel.getProduct()
						//		&& MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(entryModel.getProduct()
						//				.getProductCategoryType()))

						if (null != entryModel.getProduct()
								&& MarketplacecommerceservicesConstants.FINEJEWELLERY.equalsIgnoreCase(entryModel.getProduct()
										.getRootCategory())) // added for fine jewellery merge.
						{

							cartSoftReservationData.setJewellery(true);

						}

						if (null != entryModel.getProduct())
						{
							cartSoftReservationData.setListingId(entryModel.getProduct().getCode());
						}

						if (null != inventoryRequest && null != salesApplication && salesApplication.equals(SalesApplication.MOBILE))
						{
							for (final InventoryReservRequestWsDTO item : inventoryRequest.getItem())
							{
								if (item.getUssId().equalsIgnoreCase(entryModel.getSelectedUssid()))
								{

									cartSoftReservationData.setDeliveryMode(item.getDeliveryMode());
									cartSoftReservationData.setFulfillmentType(item.getFulfillmentType());
									if (null != item.getServiceableSlaves())
									{
										cartSoftReservationData.setServiceableSlaves(item.getServiceableSlaves());
									}
									else if (item.getDeliveryMode().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
									{
										cartSoftReservationData.setStoreId(item.getStoreId());
									}
								}
							}

						}
						else
						{
							if (null != pincoderesponseDataList && pincoderesponseDataList.size() > 0)
							{
								for (final PinCodeResponseData responseData : pincoderesponseDataList)
								{
									if (entryModel.getSelectedUssid().equals(responseData.getUssid()))
									{
										for (final DeliveryDetailsData detailsData : responseData.getValidDeliveryModes())
										{
											if (deliveryModeGlobalCode.equalsIgnoreCase(detailsData.getType()))
											{
												if (null != detailsData.getServiceableSlaves()
														&& detailsData.getServiceableSlaves().size() > 0)
												{
													cartSoftReservationData.setServiceableSlaves(detailsData.getServiceableSlaves());
												}
												if (null != detailsData.getCNCServiceableSlavesData()
														&& detailsData.getCNCServiceableSlavesData().size() > 0)
												{
													cartSoftReservationData.setCncServiceableSlaves(detailsData.getCNCServiceableSlavesData());
													cartSoftReservationData.setFulfillmentType(detailsData.getCNCServiceableSlavesData()
															.get(0).getFulfillmentType());

												}
												if (null != detailsData.getFulfilmentType())
												{
													cartSoftReservationData.setFulfillmentType(detailsData.getFulfilmentType());
												}
											}
										}
									}
								}

							}
						}
						cartSoftReservationDataList.add(cartSoftReservationData);
						if (entryModel.getProduct().getRootCategory()
								.equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))
						{
							populataJewelleryWeight(deliveryModeGlobalCode, cartSoftReservationDataList, fulfillmentType,
									entryModel.getSelectedUssid(), cartSoftReservationData.getListingId());
						}
					}
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception  : " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9047);
		}

		getSessionService().setAttribute(MarketplacecommerceservicesConstants.RESERVATION_DATA_TO_SESSION,
				cartSoftReservationDataList);

		return cartSoftReservationDataList;
	}


	/**
	 * @param deliveryModeGlobalCode
	 * @param cartSoftReservationDataList
	 * @param fulfillmentType
	 * @param ussid
	 * @param ListingId
	 *           This method will set all variants with same serviceable slaves since on failure of IR will redirect to
	 *           cart user will have to choose a new product to continue a checkout journey
	 */
	public void populataJewelleryWeight(final String deliveryModeGlobalCode,
			final List<CartSoftReservationData> cartSoftReservationDataList, final String fulfillmentType, final String ussid,
			final String ListingId)
	{
		// YTODO Auto-generated method stub
		final List<BuyBoxModel> jewelleryUssid = jewelleryService.getAllWeightVariant(ussid);
		if (CollectionUtils.isNotEmpty(jewelleryUssid))
		{
			final List<BuyBoxModel> jewelleryUssidModifieableList = new ArrayList(jewelleryUssid);
			jewelleryUssidModifieableList.sort(Comparator.comparing(BuyBoxModel::getPrice).reversed());
			for (final BuyBoxModel jewelInfoUssid : jewelleryUssidModifieableList)
			{
				if (null != ussid && !jewelInfoUssid.getSellerArticleSKU().equals(ussid))
				{
					final CartSoftReservationData jewellery = new CartSoftReservationData();
					jewellery.setDeliveryMode(deliveryModeGlobalCode);
					jewellery.setFulfillmentType(fulfillmentType.toUpperCase());
					jewellery.setJewellery(true);
					jewellery.setListingId(ListingId);
					jewellery.setUSSID(jewelInfoUssid.getSellerArticleSKU());
					jewellery.setQuantity(Integer.valueOf(1));
					// Added to populate servicable slaves for other variants
					for (final CartSoftReservationData jwlDataObj : cartSoftReservationDataList)
					{
						if (jwlDataObj.getUSSID().equalsIgnoreCase(ussid))
						{
							jewellery.setTransportMode(jwlDataObj.getTransportMode());
							jewellery.setServiceableSlaves(jwlDataObj.getServiceableSlaves());
							jewellery.setCncServiceableSlaves(jwlDataObj.getCncServiceableSlaves());
							jewellery.setStoreId(jwlDataObj.getStoreId());
							break;
						}
					}

					cartSoftReservationDataList.add(jewellery);
				}
			}
		}

	}

	/**
	 * This method is used to set fullFillMent type for a product
	 *
	 * @author TECHOUTS
	 * @param cartSoftReservationData
	 * @param abstractOrderData
	 *
	 *
	 * @return void
	 */
	//commented for CAR:127
	/*
	 * private void setFullFillmentTypeForFreebie(final CartSoftReservationData cartSoftReservationData, final
	 * AbstractOrderModel abstractOrderModel) throws EtailNonBusinessExceptions
	 */
	private void setFullFillmentTypeForFreebie(final CartSoftReservationData cartSoftReservationData,
			final AbstractOrderData abstractOrderData) throws EtailNonBusinessExceptions
	{
		try
		{
			final String parentUSSID = cartSoftReservationData.getParentUSSID();
			String parentFullfillmenttype = StringUtils.EMPTY;
			final String freebieFullfillmenttype = cartSoftReservationData.getFulfillmentType();
			//commented for CAR:127
			//for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
			for (final OrderEntryData entry : abstractOrderData.getEntries())
			{
				//commented for CAR:127
				//if (entry.getSelectedUSSID().equalsIgnoreCase(parentUSSID))
				if (entry.getSelectedUssid().equalsIgnoreCase(parentUSSID))
				{
					//commented for CAR:127
					//parentFullfillmenttype = getFullfillmentTypeByUSSID(entry.getSelectedUSSID());
					parentFullfillmenttype = getFullfillmentTypeByUSSID(entry.getSelectedUssid());
				}
				if (parentFullfillmenttype.equalsIgnoreCase(MarketplacecommerceservicesConstants.SSHIPCODE)
						&& freebieFullfillmenttype.equalsIgnoreCase(MarketplacecommerceservicesConstants.TSHIPCODE))
				{
					cartSoftReservationData.setFulfillmentType(parentFullfillmenttype);
				}

			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception while setting fullFillment type for freebie" + e.getCause());
		}
	}


	/**
	 * this Method is used to get the fullFillMent type for a product using USSID
	 *
	 * @author TECHOUTS
	 * @param selectedUSSID
	 * @return String
	 *
	 */
	private String getFullfillmentTypeByUSSID(final String selectedUSSID)
	{
		String fulfillmentType = StringUtils.EMPTY;
		final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(selectedUSSID);
		List<RichAttributeModel> richAttributeModel = null;
		if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
		{
			richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
			if (richAttributeModel != null && richAttributeModel.get(0) != null
					&& richAttributeModel.get(0).getDeliveryFulfillModes() != null
					&& richAttributeModel.get(0).getDeliveryFulfillModes().getCode() != null)
			{
				fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
				return fulfillmentType.toUpperCase();
			}
		}
		return fulfillmentType.toUpperCase();
	}




	/**
	 * @param cartSoftReservationData
	 * @param entryModel
	 * @param abstractOrderModel
	 */
	//CAR:127
	/*
	 * private String setParentforABgetC(final CartSoftReservationData cartSoftReservationData, final
	 * AbstractOrderEntryModel entryModel, final AbstractOrderModel abstractOrderModel)
	 */
	private String setParentforABgetC(final CartSoftReservationData cartSoftReservationData, final OrderEntryData entryModel,
			final AbstractOrderData abstractOrderData)
	{
		String deliveryMode = null;
		//commented for CAR:127
		//final Long ussIdA = getQuantity(entryModel.getAssociatedItems().get(0), abstractOrderModel);
		//final Long ussIdB = getQuantity(entryModel.getAssociatedItems().get(1), abstractOrderModel);
		//final String ussIdADelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(0), abstractOrderModel);
		//final String ussIdBDelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(1), abstractOrderModel);
		final Long ussIdA = getQuantity(entryModel.getAssociatedItems().get(0), abstractOrderData);
		final Long ussIdB = getQuantity(entryModel.getAssociatedItems().get(1), abstractOrderData);
		final String ussIdADelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(0), abstractOrderData);
		final String ussIdBDelMod = getDeliverModeForABgetC(entryModel.getAssociatedItems().get(1), abstractOrderData);
		// YTODO Auto-generated method stub
		if (ussIdA.doubleValue() < ussIdB.doubleValue())
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(0));
			deliveryMode = ussIdADelMod;
		}
		else
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(1));
			deliveryMode = ussIdBDelMod;
		}
		if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT) && ussIdBDelMod
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(0));
			deliveryMode = ussIdADelMod;
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) && ussIdBDelMod
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)))
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(0));
			deliveryMode = ussIdADelMod;
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) && ussIdBDelMod
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)))
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(0));
			deliveryMode = ussIdADelMod;
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) && ussIdBDelMod
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(0));
			deliveryMode = ussIdADelMod;
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& (ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) && ussIdADelMod
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(1));
			deliveryMode = ussIdBDelMod;
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) && ussIdBDelMod
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(0));
			deliveryMode = ussIdADelMod;
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& (ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) && ussIdADelMod
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(1));
			deliveryMode = ussIdBDelMod;
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) || ussIdBDelMod
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)))
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(0));
			deliveryMode = ussIdADelMod;
		}
		else if (ussIdA.doubleValue() == ussIdB.doubleValue()
				&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) || ussIdBDelMod
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)))
		{
			cartSoftReservationData.setParentUSSID(entryModel.getAssociatedItems().get(1));
			deliveryMode = ussIdBDelMod;
		}
		return deliveryMode;
	}

	/**
	 * @param string
	 * @param abstractOrderModel
	 * @return
	 */
	//commented for CAR:127
	//private String getDeliverModeForABgetC(final String ussid, final AbstractOrderModel abstractOrderModel)
	private String getDeliverModeForABgetC(final String ussid, final AbstractOrderData abstractOrderData)
	{
		// YTODO Auto-generated method stub
		String deliveryMode = null;
		//commented for CAR:127
		//for (final AbstractOrderEntryModel cartEntry : abstractOrderModel.getEntries())
		for (final OrderEntryData cartEntry : abstractOrderData.getEntries())
		{
			//commented for CAR:127
			//if (cartEntry.getSelectedUSSID().equalsIgnoreCase(ussid) && !cartEntry.getGiveAway().booleanValue())
			if (cartEntry.getSelectedUssid().equalsIgnoreCase(ussid) && !cartEntry.isGiveAway())
			{
				//commented for CAR:127
				//deliveryMode = cartEntry.getMplDeliveryMode().getDeliveryMode().getCode();
				deliveryMode = cartEntry.getMplDeliveryMode().getCode();
			}

		}

		return deliveryMode;
	}


	/**
	 * @param ussid
	 * @param abstractOrderModel
	 * @return
	 */
	//CAR:127
	//private Long getQuantity(final String ussid, final AbstractOrderModel abstractOrderModel)
	private Long getQuantity(final String ussid, final AbstractOrderData abstractOrderData)
	{
		// YTODO Auto-generated method stub
		Long qty = null;
		//for (final AbstractOrderEntryModel cartEntry : abstractOrderModel.getEntries())
		for (final OrderEntryData cartEntry : abstractOrderData.getEntries())
		{
			//if (cartEntry.getSelectedUSSID().equalsIgnoreCase(ussid) && !cartEntry.getGiveAway().booleanValue())
			if (cartEntry.getSelectedUssid().equalsIgnoreCase(ussid) && !cartEntry.isGiveAway())
			{
				qty = cartEntry.getQuantity();
				cartEntry.getMplDeliveryMode().getCode();
			}

		}

		return qty;
	}

	/*
	 * @DESC : Inventory list to be generated for TISPRD-2758
	 *
	 *
	 *
	 *
	 * @param abstractOrderModel
	 *
	 *
	 *
	 *
	 * @param entryModel
	 *
	 *
	 *
	 *
	 * @return Tuple2<?, ?>
	 */
	//commented for CAR:127
	/*
	 * private Tuple2<?, ?> getFreebieInventoryList(final AbstractOrderModel abstractOrderModel, final
	 * AbstractOrderEntryModel entryModel)
	 */
	private Tuple2<?, ?> getFreebieInventoryList(final AbstractOrderData abstractOrderData, final OrderEntryData entryModel,
			final AbstractOrderModel abstractOrderModel)
	{
		List<CartSoftReservationData> reservationList = new ArrayList<CartSoftReservationData>();
		boolean isFreebiePromotionApplied = false;
		try
		{
			//commented for 7
			/*
			 * if (entryModel.getGiveAway().booleanValue() && CollectionUtils.isNotEmpty(entryModel.getAssociatedItems())
			 * && entryModel.getAssociatedItems().size() > 1 &&
			 * CollectionUtils.isNotEmpty(abstractOrderModel.getAllPromotionResults()))
			 */
			final Set<PromotionResultModel> promotionResults = abstractOrderModel.getAllPromotionResults();
			final List<String> associatedItems = entryModel.getAssociatedItems();
			if (entryModel.isGiveAway() && CollectionUtils.isNotEmpty(associatedItems) && associatedItems.size() > 1
					&& CollectionUtils.isNotEmpty(promotionResults))
			{

				final Set<PromotionResultModel> eligiblePromoList = promotionResults;

				String productPromoCode = MarketplacecommerceservicesConstants.EMPTY;
				for (final PromotionResultModel promotionResultModel : eligiblePromoList)
				{
					final AbstractPromotionModel promotion = promotionResultModel.getPromotion();
					if (promotionResultModel.getCertainty().floatValue() == 1.0F
							&& promotion instanceof BuyXItemsofproductAgetproductBforfreeModel
							&& StringUtils.equalsIgnoreCase(promotion.getCode(), entryModel.getProductPromoCode()))
					{
						isFreebiePromotionApplied = true;
						productPromoCode = promotion.getCode();
						break;
					}
				}
				if (isFreebiePromotionApplied)
				{
					//commented for CAR:127
					//reservationList = populateFreebieInventoryData(abstractOrderModel, entryModel, productPromoCode);
					reservationList = populateFreebieInventoryData(abstractOrderData, entryModel, productPromoCode);
				}

			}
		}
		catch (final Exception ex)
		{
			LOG.error("Exception occured in getFreebieInventoryList", ex);
		}
		return new Tuple2(Boolean.valueOf(isFreebiePromotionApplied), reservationList);
	}

	/*
	 * @DESC : Inventory list to be generated for TISPRD-2758
	 *
	 *
	 *
	 *
	 * @param abstractOrderModel
	 *
	 *
	 *
	 *
	 * @param entryModel
	 *
	 *
	 *
	 *
	 * @param productPromoCode
	 *
	 *
	 *
	 *
	 * @return List<CartSoftReservationData>
	 */
	//commented for CAR:127
	/*
	 * private List<CartSoftReservationData> populateFreebieInventoryData(final AbstractOrderModel abstractOrderModel,
	 * final AbstractOrderEntryModel entryModel, final String productPromoCode)
	 */
	private List<CartSoftReservationData> populateFreebieInventoryData(final AbstractOrderData abstractOrderModel,
			final OrderEntryData entryModel, final String productPromoCode)
	{
		final List<CartSoftReservationData> reservationList = new ArrayList<CartSoftReservationData>();
		try
		{
			CartSoftReservationData cartSoftReservationData = null;
			//commented for CAR:127
			//for (final AbstractOrderEntryModel cartEntryModel : abstractOrderModel.getEntries())
			for (final OrderEntryData cartEntryModel : abstractOrderModel.getEntries())
			{
				if (CollectionUtils.isNotEmpty(cartEntryModel.getAssociatedItems())
				/* CAR:127 cartEntryModel.getAssociatedItems().contains(entryModel.getSelectedUSSID()) */
				&& cartEntryModel.getAssociatedItems().contains(entryModel.getSelectedUssid())
						&& StringUtils.equalsIgnoreCase(productPromoCode, cartEntryModel.getProductPromoCode())
						/* CAR:127 && !cartEntryModel.getGiveAway().booleanValue() */&& !cartEntryModel.isGiveAway())
				{
					cartSoftReservationData = new CartSoftReservationData();
					//commented for CAR:127
					/*
					 * if (entryModel.getSelectedUSSID() != null) {
					 * cartSoftReservationData.setUSSID(entryModel.getSelectedUSSID()); // Setting freebie ussid }
					 */

					if (entryModel.getSelectedUssid() != null)
					{
						cartSoftReservationData.setUSSID(entryModel.getSelectedUssid()); // Setting freebie ussid
					}

					if (cartEntryModel.getQualifyingCount() != null && cartEntryModel.getQualifyingCount().intValue() > 0)
					{
						cartSoftReservationData.setQuantity(Integer.valueOf(cartEntryModel.getQualifyingCount().toString())); // Setting Qualifying count as selected quantity
					}

					if (cartEntryModel.getDeliveryPointOfService() != null
							&& cartEntryModel.getDeliveryPointOfService().getSlaveId() != null)
					{
						cartSoftReservationData.setStoreId(cartEntryModel.getDeliveryPointOfService().getSlaveId());
					}
					//commented for CAR:127
					//cartSoftReservationData.setParentUSSID(cartEntryModel.getSelectedUSSID());
					cartSoftReservationData.setParentUSSID(cartEntryModel.getSelectedUssid());
					cartSoftReservationData.setIsAFreebie(MarketplacecommerceservicesConstants.Y);

					String deliveryMode = MarketplacecommerceservicesConstants.EMPTY;

					//commented for CAR:127
					/*
					 * if (cartEntryModel.getMplDeliveryMode() != null &&
					 * cartEntryModel.getMplDeliveryMode().getDeliveryMode() != null &&
					 * cartEntryModel.getMplDeliveryMode().getDeliveryMode().getCode() != null) { deliveryMode =
					 * cartEntryModel.getMplDeliveryMode().getDeliveryMode().getCode(); }
					 */
					if (cartEntryModel.getMplDeliveryMode() != null && cartEntryModel.getMplDeliveryMode().getCode() != null)
					{
						deliveryMode = cartEntryModel.getMplDeliveryMode().getCode();
					}

					if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY))
					{
						deliveryMode = MarketplacecommerceservicesConstants.HD;
					}
					else if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY))
					{
						deliveryMode = MarketplacecommerceservicesConstants.ED;
					}
					else if (deliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT))
					{
						deliveryMode = MarketplacecommerceservicesConstants.CnC;
					}

					if (StringUtils.isNotEmpty(deliveryMode))
					{
						cartSoftReservationData.setDeliveryMode(deliveryMode);
					}
					if (StringUtils.equalsIgnoreCase(deliveryMode, MarketplacecommerceservicesConstants.CnC))
					{
						//commented for CAR:127
						//final PointOfServiceModel parentPosModel = cartEntryModel.getDeliveryPointOfService();
						final PointOfServiceData parentPosModel = cartEntryModel.getDeliveryPointOfService();
						if (null != parentPosModel && parentPosModel.getSlaveId() != null)
						{
							cartSoftReservationData.setStoreId(parentPosModel.getSlaveId());
						}
					}
					//commented for CAR:127
					/*
					 * final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
					 * entryModel.getSelectedUSSID());
					 */
					final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
							entryModel.getSelectedUssid());
					List<RichAttributeModel> richAttributeModel = null;
					if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
					{
						richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
						if (richAttributeModel != null && richAttributeModel.get(0) != null
								&& richAttributeModel.get(0).getDeliveryFulfillModes() != null
								&& richAttributeModel.get(0).getDeliveryFulfillModes().getCode() != null)
						{
							final String fulfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes().getCode();
							cartSoftReservationData.setFulfillmentType(fulfillmentType.toUpperCase());
						}
					}

					reservationList.add(cartSoftReservationData);
				}
			}
		}
		catch (final Exception ex)
		{
			LOG.error("Exception occured in populateFreebieInventoryData", ex);
		}
		return reservationList;
	}

	/**
	 * @return the commerceUpdateCartEntryStrategy
	 */
	@Override
	public ExtDefaultCommerceUpdateCartEntryStrategy getCommerceUpdateCartEntryStrategy()
	{
		return commerceUpdateCartEntryStrategy;
	}


	/**
	 * @param commerceUpdateCartEntryStrategy
	 *           the commerceUpdateCartEntryStrategy to set
	 */
	public void setCommerceUpdateCartEntryStrategy(final ExtDefaultCommerceUpdateCartEntryStrategy commerceUpdateCartEntryStrategy)
	{
		this.commerceUpdateCartEntryStrategy = commerceUpdateCartEntryStrategy;
	}


	/**
	 * @return the productService
	 */
	public ProductService getProductService()
	{
		return productService;
	}


	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}


	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}


	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	/**
	 * @return the userFacade
	 */
	public UserFacade getUserFacade()
	{
		return userFacade;
	}


	/**
	 * @param userFacade
	 *           the userFacade to set
	 */
	public void setUserFacade(final UserFacade userFacade)
	{
		this.userFacade = userFacade;
	}


	/**
	 * @return the mplCustomerProfileService
	 */
	public MplCustomerProfileService getMplCustomerProfileService()
	{
		return mplCustomerProfileService;
	}


	/**
	 * @param mplCustomerProfileService
	 *           the mplCustomerProfileService to set
	 */
	public void setMplCustomerProfileService(final MplCustomerProfileService mplCustomerProfileService)
	{
		this.mplCustomerProfileService = mplCustomerProfileService;
	}


	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}


	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}


	/**
	 * @return the buyBoxDao
	 */
	public BuyBoxDao getBuyBoxDao()
	{
		return buyBoxDao;
	}


	/**
	 * @param buyBoxDao
	 *           the buyBoxDao to set
	 */
	public void setBuyBoxDao(final BuyBoxDao buyBoxDao)
	{
		this.buyBoxDao = buyBoxDao;
	}


	/**
	 * @return the inventoryReservationService
	 */
	public InventoryReservationService getInventoryReservationService()
	{
		return inventoryReservationService;
	}


	/**
	 * @param inventoryReservationService
	 *           the inventoryReservationService to set
	 */
	public void setInventoryReservationService(final InventoryReservationService inventoryReservationService)
	{
		this.inventoryReservationService = inventoryReservationService;
	}


	/**
	 * @return the mplPincodeRestrictionService
	 */
	public MplPincodeRestrictionService getMplPincodeRestrictionService()
	{
		return mplPincodeRestrictionService;
	}


	/**
	 * @param mplPincodeRestrictionService
	 *           the mplPincodeRestrictionService to set
	 */
	public void setMplPincodeRestrictionService(final MplPincodeRestrictionService mplPincodeRestrictionService)
	{
		this.mplPincodeRestrictionService = mplPincodeRestrictionService;
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
	 * @return the pinCodeDeliveryModeService
	 */
	public PinCodeDeliveryModeService getPinCodeDeliveryModeService()
	{
		return pinCodeDeliveryModeService;
	}


	/**
	 * @param pinCodeDeliveryModeService
	 *           the pinCodeDeliveryModeService to set
	 */
	public void setPinCodeDeliveryModeService(final PinCodeDeliveryModeService pinCodeDeliveryModeService)
	{
		this.pinCodeDeliveryModeService = pinCodeDeliveryModeService;
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
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}


	/**
	 * @param priceDataFactory
	 *           the priceDataFactory to set
	 */
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}


	/**
	 * @return the mplDefaultCommerceAddToCartStrategyImpl
	 */
	public MplDefaultCommerceAddToCartStrategyImpl getMplDefaultCommerceAddToCartStrategyImpl()
	{
		return mplDefaultCommerceAddToCartStrategyImpl;
	}


	/**
	 * @param mplDefaultCommerceAddToCartStrategyImpl
	 *           the mplDefaultCommerceAddToCartStrategyImpl to set
	 */
	public void setMplDefaultCommerceAddToCartStrategyImpl(
			final MplDefaultCommerceAddToCartStrategyImpl mplDefaultCommerceAddToCartStrategyImpl)
	{
		this.mplDefaultCommerceAddToCartStrategyImpl = mplDefaultCommerceAddToCartStrategyImpl;
	}


	/**
	 * @return the wishlistService
	 */
	public Wishlist2Service getWishlistService()
	{
		return wishlistService;
	}


	/**
	 * @param wishlistService
	 *           the wishlistService to set
	 */
	public void setWishlistService(final Wishlist2Service wishlistService)
	{
		this.wishlistService = wishlistService;
	}


	/**
	 * @return the subOrderIdGenerator
	 */
	public PersistentKeyGenerator getSubOrderIdGenerator()
	{
		return subOrderIdGenerator;
	}


	/**
	 * @param subOrderIdGenerator
	 *           the subOrderIdGenerator to set
	 */
	public void setSubOrderIdGenerator(final PersistentKeyGenerator subOrderIdGenerator)
	{
		this.subOrderIdGenerator = subOrderIdGenerator;
	}


	/**
	 * @return the orderLineIdGenerator
	 */
	public PersistentKeyGenerator getOrderLineIdGenerator()
	{
		return orderLineIdGenerator;
	}


	/**
	 * @param orderLineIdGenerator
	 *           the orderLineIdGenerator to set
	 */
	public void setOrderLineIdGenerator(final PersistentKeyGenerator orderLineIdGenerator)
	{
		this.orderLineIdGenerator = orderLineIdGenerator;
	}


	/**
	 * @return the orderIdGenerator
	 */
	public PersistentKeyGenerator getOrderIdGenerator()
	{
		return orderIdGenerator;
	}


	/**
	 * @param orderIdGenerator
	 *           the orderIdGenerator to set
	 */
	public void setOrderIdGenerator(final PersistentKeyGenerator orderIdGenerator)
	{
		this.orderIdGenerator = orderIdGenerator;
	}


	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}


	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	/**
	 * @return the log
	 */
	public static Logger getLog()
	{
		return LOG;
	}


	/**
	 * @return the maximumConfiguredQuantiy
	 */
	public static String getMaximumConfiguredQuantiy()
	{
		return MAXIMUM_CONFIGURED_QUANTIY;
	}


	/**
	 * The Method used to validate serviceability check for Gift Yourself Section in Cart
	 *
	 * @param defaultPinCodeId
	 * @param entryModels
	 * @return boolean
	 */
	@Override
	public Map<String, List<String>> checkPincodeGiftCartData(final String defaultPinCodeId,
			final List<Wishlist2EntryModel> entryModels, final Tuple2<?, ?> wishListPincodeObject) //TISPT-179 Point 3
	{
		//final List<PincodeServiceData> pincodeRequestDataList = new ArrayList<>(); TISPT-179 Point 3

		Map<String, List<String>> giftYourselfDeliveryModeDataMap = null;

		//TISPT-179 Point 3
		//		for (final Wishlist2EntryModel wishModel : entryModels)
		//		{
		//			final PincodeServiceData pincodeServiceData = new PincodeServiceData();
		//			final List<MarketplaceDeliveryModeData> deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>(); //TISTI-104
		//			final ProductModel productModel = wishModel.getProduct();
		//			if (null != productModel)
		//			{
		//				pincodeServiceData.setProductCode(productModel.getCode());
		//			}
		//			else
		//			{
		//				LOG.debug("Product Model is null for the wishlist Product");
		//			}
		//
		//			final SellerInformationModel sellerInfo = getMplSellerInformationService().getSellerDetail(wishModel.getUssid());
		//			if (wishModel.getUssid() != null && sellerInfo != null)
		//			{
		//				String fullfillmentType = MarketplacecommerceservicesConstants.EMPTYSPACE;
		//				List<RichAttributeModel> richAttributeModel = new ArrayList<RichAttributeModel>();
		//
		//				final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(wishModel.getUssid());
		//
		//				if (sellerInfoModel != null && sellerInfoModel.getRichAttribute() != null)
		//				{
		//					richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
		//					fullfillmentType = richAttributeModel.get(0).getDeliveryFulfillModes() != null ? richAttributeModel.get(0)
		//							.getDeliveryFulfillModes().getCode() : "";
		//				}
		//				else
		//				{
		//					LOG.info("preparingFinalList sellerInfoModel is null ");
		//				}
		//
		//				final List<MplZoneDeliveryModeValueModel> deliveryModes = getMplDeliveryCostService().getDeliveryModesAndCost(
		//						MarketplacecommerceservicesConstants.INR, wishModel.getUssid());
		//				if (null != deliveryModes && !deliveryModes.isEmpty())
		//				{
		//					for (final MplZoneDeliveryModeValueModel deliveryEntry : deliveryModes)
		//					{
		//						final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();
		//						final PriceData priceData = formPriceData(deliveryEntry.getValue());
		//						deliveryModeData.setCode(deliveryEntry.getDeliveryMode().getCode());
		//						deliveryModeData.setDescription(deliveryEntry.getDeliveryMode().getDescription());
		//						deliveryModeData.setName(deliveryEntry.getDeliveryMode().getName());
		//						deliveryModeData.setSellerArticleSKU(wishModel.getUssid());
		//						deliveryModeData.setDeliveryCost(priceData);
		//						deliveryModeDataList.add(deliveryModeData);
		//					}
		//				}
		//				pincodeServiceData.setFullFillmentType(fullfillmentType.toUpperCase());
		//				if (richAttributeModel.get(0).getShippingModes() != null
		//						&& richAttributeModel.get(0).getShippingModes().getCode() != null)
		//				{
		//					pincodeServiceData.setTransportMode(MplCodeMasterUtility.getglobalCode(richAttributeModel.get(0)
		//							.getShippingModes().getCode().toUpperCase()));
		//				}
		//				else
		//				{
		//					LOG.info("preparingFinalList Transportmode is null ");
		//				}
		//				pincodeServiceData.setSellerId(sellerInfo.getSellerID());
		//				pincodeServiceData.setUssid(wishModel.getUssid());
		//
		//				if (null != richAttributeModel.get(0).getPaymentModes()
		//						&& ((PaymentModesEnum.valueOf("COD")).toString().equalsIgnoreCase(
		//								richAttributeModel.get(0).getPaymentModes().getCode()) || (PaymentModesEnum.valueOf("BOTH")).toString()
		//								.equalsIgnoreCase(richAttributeModel.get(0).getPaymentModes().getCode())))
		//				{
		//					pincodeServiceData.setIsCOD(MarketplacecommerceservicesConstants.Y);
		//				}
		//				else
		//				{
		//					pincodeServiceData.setIsCOD(MarketplacecommerceservicesConstants.N);
		//				}
		//				if (wishModel.getProduct() != null && wishModel.getProduct().getMrp() != null)
		//				{
		//					pincodeServiceData.setPrice(wishModel.getProduct().getMrp());
		//				}
		//				else
		//				{
		//					LOG.debug("wishlist2EntryModel product is null ");
		//				}
		//				pincodeServiceData.setDeliveryModes(deliveryModeDataList);
		//				pincodeServiceData.setIsDeliveryDateRequired(MarketplacecommerceservicesConstants.N);
		//			}
		//
		//			pincodeRequestDataList.add(pincodeServiceData);
		//		}

		//giftYourselfDeliveryModeDataMap = giftYourselfData(defaultPinCodeId, entryModels, pincodeRequestDataList); TISPT-179 Point 3
		giftYourselfDeliveryModeDataMap = giftYourselfData(defaultPinCodeId, entryModels, wishListPincodeObject);


		return giftYourselfDeliveryModeDataMap;
	}


	/**
	 * Returns Serviceable Cart Data
	 *
	 * @param defaultPinCodeId
	 * @param entryModels
	 * @param pincodeRequestDataList
	 * @param wishListPincodeObject
	 * @return giftYourselfDeliveryModeDataMap
	 */
	//TISPT-179 Point 3
	//private Map<String, List<String>> giftYourselfData(final String defaultPinCodeId, final List<Wishlist2EntryModel> entryModels,final List<PincodeServiceData> pincodeRequestDataList)
	private Map<String, List<String>> giftYourselfData(final String defaultPinCodeId, final List<Wishlist2EntryModel> entryModels,
			final Tuple2<?, ?> wishListPincodeObject)

	{
		List<PinCodeResponseData> pinCodeResponseDataList = null;
		List<String> deliveryDetailsList = null;
		final Map<String, List<String>> giftYourselfDeliveryModeDataMap = new HashMap<String, List<String>>();
		final Map<String, String> productUSSIDDetails = getUSSIDDetails(entryModels);

		if (MapUtils.isNotEmpty(productUSSIDDetails))
		{
			deliveryDetailsList = new ArrayList<String>();
			pinCodeResponseDataList = (List<PinCodeResponseData>) wishListPincodeObject.getSecond();
			if (CollectionUtils.isNotEmpty(pinCodeResponseDataList))
			{
				for (final PinCodeResponseData pinCodeResponseData : pinCodeResponseDataList)
				{
					final List<DeliveryDetailsData> deliveryData = pinCodeResponseData.getValidDeliveryModes();
					deliveryDetailsList = getDeliveryData(deliveryData);

					if (productUSSIDDetails.containsKey(pinCodeResponseData.getUssid()))
					{
						giftYourselfDeliveryModeDataMap.put(productUSSIDDetails.get(pinCodeResponseData.getUssid()),
								deliveryDetailsList);
					}
				}
			}
		}
		//TISPT-179 Point 3
		//		if (CollectionUtils.isNotEmpty(pincodeRequestDataList) && MapUtils.isNotEmpty(productUSSIDDetails))
		//		{
		//			deliveryDetailsList = new ArrayList<String>();
		//			pinCodeResponseDataList = getServiceablePinCodeCart(defaultPinCodeId, pincodeRequestDataList);
		//			if (CollectionUtils.isNotEmpty(pinCodeResponseDataList))
		//			{
		//				for (final PinCodeResponseData pinCodeResponseData : pinCodeResponseDataList)
		//				{
		//					final List<DeliveryDetailsData> deliveryData = pinCodeResponseData.getValidDeliveryModes();
		//					deliveryDetailsList = getDeliveryData(deliveryData);
		//
		//					if (productUSSIDDetails.containsKey(pinCodeResponseData.getUssid()))
		//					{
		//						giftYourselfDeliveryModeDataMap.put(productUSSIDDetails.get(pinCodeResponseData.getUssid()),
		//								deliveryDetailsList);
		//					}
		//				}
		//			}
		//		}
		return giftYourselfDeliveryModeDataMap;
	}


	/**
	 * Delivery Data
	 *
	 * @param deliveryData
	 * @return deliveryDataList
	 */
	private List<String> getDeliveryData(final List<DeliveryDetailsData> deliveryData)
	{
		final List<String> deliveryDataList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(deliveryData))
		{
			for (final DeliveryDetailsData data : deliveryData)
			{
				if (null != data && StringUtils.isNotEmpty(data.getType())
						&& data.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.HD))
				{
					deliveryDataList.add(MarketplacecommerceservicesConstants.CART_HOME_DELIVERY);
				}
				else if (null != data && StringUtils.isNotEmpty(data.getType())
						&& data.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.ED))
				{
					deliveryDataList.add(MarketplacecommerceservicesConstants.CART_EXPRESS_DELIVERY);
				}
			}
		}
		return deliveryDataList;
	}


	/**
	 * Returns Product USSID Details
	 *
	 * @param entryModels
	 * @return productUSSIDDetails
	 */
	private Map<String, String> getUSSIDDetails(final List<Wishlist2EntryModel> entryModels)
	{
		final Map<String, String> productUSSIDDetails = new HashMap<>();
		for (final Wishlist2EntryModel wishModel : entryModels)
		{
			if (wishModel.getProduct() != null
					&& wishModel.getProduct().getCode() != null
					&& (wishModel.getIsDeleted() == null || (wishModel.getIsDeleted() != null && !wishModel.getIsDeleted()
							.booleanValue())))//TPR-5787 check added
			{
				productUSSIDDetails.put(wishModel.getUssid(), wishModel.getProduct().getCode());
			}
		}
		return productUSSIDDetails;
	}


	/**
	 * Returns Seller Data
	 *
	 * @param ussid
	 * @return SellerInformationModel
	 */
	@Override
	public SellerInformationModel getSellerDetailsData(final String ussid)
	{
		final SellerInformationModel sellerInformation = mplSellerInformationService.getSellerDetail(ussid);
		return sellerInformation;
	}



	/*
	 * @desc use to save freebie delivery mode
	 *
	 *
	 *
	 *
	 * @param cartModel
	 *
	 *
	 *
	 *
	 * @param freebieModelMap
	 *
	 *
	 *
	 *
	 * @param freebieParentQtyMap
	 *
	 *
	 *
	 *
	 * @return void
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void saveDeliveryMethForFreebie(final AbstractOrderModel abstractOrderModel,
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, final Map<String, Long> freebieParentQtyMap)
			throws EtailNonBusinessExceptions
	{
		//added for car-124 implementation
		final List<AbstractOrderEntryModel> abstractOrderEntryModelList = new ArrayList<AbstractOrderEntryModel>();
		if (abstractOrderModel != null && abstractOrderModel.getEntries() != null && freebieModelMap != null
				&& !freebieModelMap.isEmpty())
		{
			for (final AbstractOrderEntryModel cartEntryModel : abstractOrderModel.getEntries())
			{
				if (cartEntryModel != null && cartEntryModel.getGiveAway().booleanValue()
						&& cartEntryModel.getAssociatedItems() != null && cartEntryModel.getAssociatedItems().size() > 0)
				{
					//commented for car-124 implementation
					//saveDeliveryMethForFreebie(cartEntryModel,freebieModelMap, freebieParentQtyMap);
					final AbstractOrderEntryModel abstractOrderEntryModel = saveDeliveryMethForFreebie(cartEntryModel,
							freebieModelMap, freebieParentQtyMap);
					//car-124 changes starts
					if (null != abstractOrderEntryModel)
					{
						abstractOrderEntryModelList.add(abstractOrderEntryModel);
					}
				}
			}
			getModelService().saveAll(abstractOrderEntryModelList);
			//car-124 changes ends
		}
	}

	/**
	 * This method saves delivery modes for freebie order entries
	 *
	 * @param abstractOrderEntryModel
	 * @param freebieModelMap
	 * @param freebieParentQtyMap
	 */
	//commented for car-124 implementation
	//private void saveDeliveryMethForFreebie(final AbstractOrderEntryModel cartEntryModel,
	//final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, final Map<String, Long> freebieParentQtyMap)
	private AbstractOrderEntryModel saveDeliveryMethForFreebie(final AbstractOrderEntryModel cartEntryModel,
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap, final Map<String, Long> freebieParentQtyMap)
	{
		MplZoneDeliveryModeValueModel mplDeliveryMode = null;
		final List<String> associatedItemsList = cartEntryModel.getAssociatedItems();
		if (associatedItemsList.size() == 1)
		{
			mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(0));
		}
		else if (associatedItemsList.size() == 2 && freebieModelMap.get(associatedItemsList.get(0)).getDeliveryMode() != null
				&& freebieModelMap.get(associatedItemsList.get(1)).getDeliveryMode() != null
				&& freebieModelMap.get(associatedItemsList.get(0)).getDeliveryMode().getCode() != null)
		{
			final Double ussIdA = Double.valueOf(freebieParentQtyMap.get(associatedItemsList.get(0)).doubleValue());
			final Double ussIdB = Double.valueOf(freebieParentQtyMap.get(associatedItemsList.get(1)).doubleValue());
			final String ussIdADelMod = freebieModelMap.get(associatedItemsList.get(0)).getDeliveryMode().getCode();
			final String ussIdBDelMod = freebieModelMap.get(associatedItemsList.get(1)).getDeliveryMode().getCode();
			if (ussIdA.doubleValue() < ussIdB.doubleValue())
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(0));
			}
			else
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(1));
			}
			if (ussIdA.doubleValue() == ussIdB.doubleValue()
					&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT) && ussIdBDelMod
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(0));
			}
			else if (ussIdA.doubleValue() == ussIdB.doubleValue()
					&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) && ussIdBDelMod
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)))
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(0));
			}
			else if (ussIdA.doubleValue() == ussIdB.doubleValue()
					&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) && ussIdBDelMod
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)))
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(0));
			}
			else if (ussIdA.doubleValue() == ussIdB.doubleValue()
					&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) && ussIdBDelMod
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(0));
			}
			else if (ussIdA.doubleValue() == ussIdB.doubleValue()
					&& (ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) && ussIdADelMod
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(1));
			}
			else if (ussIdA.doubleValue() == ussIdB.doubleValue()
					&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) && ussIdBDelMod
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(0));
			}
			else if (ussIdA.doubleValue() == ussIdB.doubleValue()
					&& (ussIdBDelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) && ussIdADelMod
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT)))
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(1));
			}
			else if (ussIdA.doubleValue() == ussIdB.doubleValue()
					&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) || ussIdBDelMod
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY)))
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(0));
			}
			else if (ussIdA.doubleValue() == ussIdB.doubleValue()
					&& (ussIdADelMod.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) || ussIdBDelMod
							.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY)))
			{
				mplDeliveryMode = freebieModelMap.get(associatedItemsList.get(1));
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
			//commented for car-124 implementation
			//getModelService().save(cartEntryModel);
			return cartEntryModel;
		}
		return null;
	}

	/**
	 * This method prepares object with ats and ussid.
	 *
	 * @param storeLocationRequestDataList
	 *           return stores with inventories.
	 *
	 */
	@Override
	public List<StoreLocationResponseData> getStoreLocationsforCnC(
			final List<StoreLocationRequestData> storeLocationRequestDataList, final CartModel cartModel)
	{
		LOG.debug("from getStoreLocationsforCnC method in serice");
		final List<StoreLocationResponseData> responseList = new ArrayList<StoreLocationResponseData>();
		try
		{
			// Commented to stop Store ATS Call

			//calls service with stores

			//			final StoreLocatorAtsResponseObject responseObject = pinCodeDeliveryModeService
			//					.prepStoreLocationsToOMS(storeLocationRequestDataList);

			final List<PinCodeResponseData> pincoderesponseDataList = getSessionService().getAttribute(
					MarketplacecommerceservicesConstants.PINCODE_RESPONSE_DATA_TO_SESSION);
			LOG.debug("******responceData******** " + pincoderesponseDataList);
			if (null != pincoderesponseDataList)
			{
				for (final StoreLocationRequestData storeLocationResponseData : storeLocationRequestDataList)
				{

					for (final PinCodeResponseData pinCodeResponseData : pincoderesponseDataList)
					{
						if (pinCodeResponseData.getUssid().equalsIgnoreCase(storeLocationResponseData.getUssId()))
						{
							final StoreLocationResponseData responseData = new StoreLocationResponseData();
							List<ATSResponseData> atsResponseDataList = null;
							for (final DeliveryDetailsData deliveryDetailsData : pinCodeResponseData.getValidDeliveryModes())
							{
								if (deliveryDetailsData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
								{
									atsResponseDataList = new ArrayList<ATSResponseData>();
									if (null != deliveryDetailsData.getCNCServiceableSlavesData()
											&& !deliveryDetailsData.getCNCServiceableSlavesData().isEmpty())
									{
										for (final CNCServiceableSlavesData cncServiceableSlavesData : deliveryDetailsData
												.getCNCServiceableSlavesData())
										{
											final ATSResponseData data = new ATSResponseData();

											data.setStoreId(cncServiceableSlavesData.getStoreId());
											data.setQuantity(cncServiceableSlavesData.getQty().intValue());

											atsResponseDataList.add(data);
										}
									}
									else
									{

										final StoreLocatorAtsResponseObject responseObject = pinCodeDeliveryModeService
												.prepStoreLocationsToOMS(storeLocationRequestDataList, cartModel);
										if (null != responseObject && null != responseObject.getItem()
												&& !responseObject.getItem().isEmpty())
										{
											for (final StoreLocationRequestData storeLocationResponse : storeLocationRequestDataList)
											{
												for (final StoreLocatorResponseItem item : responseObject.getItem())
												{
													if (item.getUssId().equalsIgnoreCase(storeLocationResponse.getUssId()))
													{
														for (final StoreLocatorAtsResponse res : item.getATS())
														{
															final ATSResponseData data = new ATSResponseData();

															data.setStoreId(res.getStoreId());
															if (null != res.getQuantity())
															{
																data.setQuantity(res.getQuantity().intValue());
															}
															atsResponseDataList.add(data);
														}
													}
												}
												responseData.setUssId(pinCodeResponseData.getUssid());
												responseData.setAts(atsResponseDataList);
												responseList.add(responseData);

											}


										}
										return responseList;
									}
								}
							}
							responseData.setUssId(pinCodeResponseData.getUssid());
							responseData.setAts(atsResponseDataList);
							responseList.add(responseData);
						}
					}
				}
			}

			return responseList;
		}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error("********* Pincode serviceability exception :");
			final StoreLocationResponseData responseData = new StoreLocationResponseData();
			// responseData.setIsServicable(MarketplacecommerceservicesConstants.NOT_APPLICABLE);
			responseList.add(responseData);
			if (null != ex.getErrorCode() && ex.getErrorCode().equalsIgnoreCase("O0001"))
			{
				throw new ClientEtailNonBusinessExceptions("O0001", ex);
			}
			else if (null != ex.getErrorCode() && ex.getErrorCode().equalsIgnoreCase("O0002"))
			{
				throw new ClientEtailNonBusinessExceptions("O0002", ex);
			}
			else
			{
				throw new ClientEtailNonBusinessExceptions(ex);
			}


		}
		//return responseList;
	}

	/**
	 * This method prepares object with ats and ussid for PDP
	 *
	 * @param storeLocationRequestDataList
	 *           return stores with inventories.
	 *
	 */
	@Override
	public List<StoreLocationResponseData> getStoreLocationsforCnC(
			final List<StoreLocationRequestData> storeLocationRequestDataList, final String sellerUssId)
	{
		LOG.debug("from getStoreLocationsforCnC method in serice PDP");
		final List<StoreLocationResponseData> responseList = new ArrayList<StoreLocationResponseData>();
		try
		{
			//fetch oms response from session added when pincode servicebility happens
			final List<PinCodeResponseData> pincoderesponseDataList = getSessionService().getAttribute(
					MarketplacecommerceservicesConstants.PINCODE_RESPONSE_DATA_PDP);
			LOG.debug("******responceData******** " + pincoderesponseDataList);
			if (null != pincoderesponseDataList)
			{
				for (final PinCodeResponseData pinCodeResponseData : pincoderesponseDataList)
				{
					if (pinCodeResponseData.getUssid().equalsIgnoreCase(sellerUssId))
					{
						final StoreLocationResponseData responseData = new StoreLocationResponseData();
						List<ATSResponseData> atsResponseDataList = null;
						for (final DeliveryDetailsData deliveryDetailsData : pinCodeResponseData.getValidDeliveryModes())
						{
							if (deliveryDetailsData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
							{
								atsResponseDataList = new ArrayList<ATSResponseData>();
								if (CollectionUtils.isNotEmpty(deliveryDetailsData.getCNCServiceableSlavesData()))
								{
									for (final CNCServiceableSlavesData cncServiceableSlavesData : deliveryDetailsData
											.getCNCServiceableSlavesData())
									{
										final ATSResponseData data = new ATSResponseData();

										data.setStoreId(cncServiceableSlavesData.getStoreId());
										data.setQuantity(cncServiceableSlavesData.getQty().intValue());

										atsResponseDataList.add(data);
									}
								}
								else
								{
									final StoreLocatorAtsResponseObject responseObject = pinCodeDeliveryModeService
											.prepStoreLocationsToOMS(storeLocationRequestDataList, null);
									if (null != responseObject && null != responseObject.getItem() && !responseObject.getItem().isEmpty())
									{

										for (final StoreLocatorResponseItem item : responseObject.getItem())
										{
											if (item.getUssId().equalsIgnoreCase(sellerUssId))
											{
												for (final StoreLocatorAtsResponse res : item.getATS())
												{
													final ATSResponseData data = new ATSResponseData();

													data.setStoreId(res.getStoreId());
													if (null != res.getQuantity())
													{
														data.setQuantity(res.getQuantity().intValue());
													}
													atsResponseDataList.add(data);
												}
											}

											responseData.setUssId(pinCodeResponseData.getUssid());
											responseData.setAts(atsResponseDataList);
											responseList.add(responseData);

										}
									}
									return responseList;
								}
							}
						}
						responseData.setUssId(pinCodeResponseData.getUssid());
						responseData.setAts(atsResponseDataList);
						responseList.add(responseData);
					}
				}
			}
			return responseList;
		}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error("********* Pincode serviceability exception :" + ex);
			if (null != ex.getErrorCode() && ex.getErrorCode().equalsIgnoreCase("O0001"))
			{
				throw new ClientEtailNonBusinessExceptions("O0001", ex);
			}
			else if (null != ex.getErrorCode() && ex.getErrorCode().equalsIgnoreCase("O0002"))
			{
				throw new ClientEtailNonBusinessExceptions("O0002", ex);
			}
			else
			{
				throw new ClientEtailNonBusinessExceptions(ex);
			}


		}
	}

	@Override
	public List<StoreLocationResponseData> getStoreLocationsforCnCMobile(
			final List<StoreLocationRequestData> storeLocationRequestDataList, final String sellerUssId, final CartModel cartModel)
	{
		LOG.debug("from getStoreLocationsforCnC method in serice");
		final List<StoreLocationResponseData> responseList = new ArrayList<StoreLocationResponseData>();
		try
		{
			// Commented to stop Store ATS Call
			final StoreLocatorAtsResponseObject responseObject = pinCodeDeliveryModeService.prepStoreLocationsToOMS(
					storeLocationRequestDataList, cartModel);
			if (null != responseObject && null != responseObject.getItem() && !responseObject.getItem().isEmpty())
			{
				List<ATSResponseData> atsResponseDataList = null;
				for (final StoreLocationRequestData storeLocationResponse : storeLocationRequestDataList)
				{
					final StoreLocationResponseData responseData = new StoreLocationResponseData();

					for (final StoreLocatorResponseItem item : responseObject.getItem())
					{
						if (item.getUssId().equalsIgnoreCase(storeLocationResponse.getUssId()))
						{
							atsResponseDataList = new ArrayList<ATSResponseData>();
							for (final StoreLocatorAtsResponse res : item.getATS())
							{
								final ATSResponseData data = new ATSResponseData();

								data.setStoreId(res.getStoreId());
								if (null != res.getQuantity())
								{
									data.setQuantity(res.getQuantity().intValue());
								}
								atsResponseDataList.add(data);
							}
						}
					}
					responseData.setUssId(storeLocationResponse.getUssId());
					responseData.setAts(atsResponseDataList);
					responseList.add(responseData);

				}
			}
			return responseList;
		}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error("********* Pincode serviceability exception :");
			final StoreLocationResponseData responseData = new StoreLocationResponseData();
			// responseData.setIsServicable(MarketplacecommerceservicesConstants.NOT_APPLICABLE);
			responseList.add(responseData);
			if (null != ex.getErrorCode() && ex.getErrorCode().equalsIgnoreCase("O0001"))
			{
				throw new ClientEtailNonBusinessExceptions("O0001", ex);
			}
			else if (null != ex.getErrorCode() && ex.getErrorCode().equalsIgnoreCase("O0002"))
			{
				throw new ClientEtailNonBusinessExceptions("O0002", ex);
			}
			else
			{
				throw new ClientEtailNonBusinessExceptions(ex);
			}


		}
		//return responseList;
	}


	/**
	 * This Method is used to get Valid Delivery Modes by Inventory
	 *
	 * @param pinCodeResponseData
	 * @throws EtailNonBusinessExceptions
	 * @return PinCodeResponseData
	 */
	@Override
	public PinCodeResponseData getVlaidDeliveryModesByInventory(final PinCodeResponseData pinCodeResponseData,
			final CartData cartData) throws EtailNonBusinessExceptions
	{
		LOG.info("Inside getVlaidDeliveryModesByInventory Method");
		try
		{
			final List<DeliveryDetailsData> validDeliveryDetailsData = new ArrayList<DeliveryDetailsData>();
			//final CartModel cartModel = cartService.getSessionCart();
			// Start Modified as part of performance fix
			if (null != pinCodeResponseData && CollectionUtils.isNotEmpty(pinCodeResponseData.getValidDeliveryModes())
					&& cartData != null && CollectionUtils.isNotEmpty(cartData.getEntries()))
			{
				for (final DeliveryDetailsData deliveryDetailsData : pinCodeResponseData.getValidDeliveryModes())
				{
					long inventory = 0;
					long selectedQuantity = 0;
					for (final OrderEntryData orderEntryData : cartData.getEntries())
					{
						if (deliveryDetailsData != null && orderEntryData != null
								&& orderEntryData.getSelectedUssid().equalsIgnoreCase(pinCodeResponseData.getUssid())
								&& !orderEntryData.isGiveAway()) //TISPRDT-219
						{
							inventory = (deliveryDetailsData.getInventory() != null) ? Long
									.parseLong(deliveryDetailsData.getInventory()) : inventory;
							selectedQuantity = (orderEntryData.getQuantity() != null) ? orderEntryData.getQuantity().longValue()
									: selectedQuantity;
							if (inventory >= selectedQuantity)
							{
								validDeliveryDetailsData.add(deliveryDetailsData);
							}
						}
						if (StringUtils.isNotEmpty(orderEntryData.getExchangeApplied())
								&& StringUtils.isNotEmpty(pinCodeResponseData.getExchangePincode()))
						{

							pinCodeResponseData.setExchangeServiceable(exchangeService.isBackwardServiceble(pinCodeResponseData
									.getExchangePincode()));

						}
					}
					pinCodeResponseData.setValidDeliveryModes(validDeliveryDetailsData);
				}
			}



			/*
			 * @Override public PinCodeResponseData getVlaidDeliveryModesByInventory(final PinCodeResponseData
			 * pinCodeResponseData) throws EtailNonBusinessExceptions { LOG.info(
			 * "Inside getVlaidDeliveryModesByInventory Method"); try { final List<DeliveryDetailsData>
			 * validDeliveryDetailsData = new ArrayList<DeliveryDetailsData>(); final CartModel cartModel =
			 * cartService.getSessionCart(); // Start Modified as part of performance fix if (null != pinCodeResponseData
			 * && CollectionUtils.isNotEmpty(pinCodeResponseData.getValidDeliveryModes()) && cartModel != null &&
			 * CollectionUtils.isNotEmpty(cartModel.getEntries())) { for (final DeliveryDetailsData deliveryDetailsData :
			 * pinCodeResponseData.getValidDeliveryModes()) { final long inventory = 0; long selectedQuantity = 0; for
			 * (final AbstractOrderEntryModel abstractOrderEntryModel : cartModel.getEntries()) { if (deliveryDetailsData
			 * != null && abstractOrderEntryModel != null &&
			 * abstractOrderEntryModel.getSelectedUSSID().equalsIgnoreCase(pinCodeResponseData.getUssid()) &&
			 * !abstractOrderEntryModel.getGiveAway().booleanValue()) //TISPRDT-219 { inventory =
			 * (deliveryDetailsData.getInventory() != null) ? Long .parseLong(deliveryDetailsData.getInventory()) :
			 * inventory; selectedQuantity = (abstractOrderEntryModel.getQuantity() != null) ?
			 * abstractOrderEntryModel.getQuantity() .longValue() : selectedQuantity; if (inventory >= selectedQuantity) {
			 * validDeliveryDetailsData.add(deliveryDetailsData); } } }
			 * pinCodeResponseData.setValidDeliveryModes(validDeliveryDetailsData); } }
			 */
			// End Modified as part of performance fix TISPT-104
			/*
			 * final List<DeliveryDetailsData> validDeliveryDetailsData = new ArrayList<DeliveryDetailsData>(); if (null !=
			 * pinCodeResponseData) { for (final DeliveryDetailsData deliveryDetailsData :
			 * pinCodeResponseData.getValidDeliveryModes()) { Long inventory = null; Long selectedQuantity = null; final
			 * CartModel cartmodel = cartService.getSessionCart(); // Iterating all cart entries and checking inventory is
			 * available or not // If inventory is available then only displaying that delivery mode in Delivery selection
			 * Page if (null != cartmodel) { for (final AbstractOrderEntryModel abstractOrderEntryModel :
			 * cartmodel.getEntries()) { if
			 * (abstractOrderEntryModel.getSelectedUSSID().equalsIgnoreCase(pinCodeResponseData.getUssid())) { inventory =
			 * Long.valueOf(deliveryDetailsData.getInventory()); selectedQuantity = abstractOrderEntryModel.getQuantity();
			 * if (inventory.longValue() >= selectedQuantity.longValue()) {
			 * validDeliveryDetailsData.add(deliveryDetailsData); } } } }
			 * pinCodeResponseData.setValidDeliveryModes(validDeliveryDetailsData); } }
			 */
			return pinCodeResponseData;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred in getVlaidDeliveryModesByInventory while checking inventory ", e);
		}
		return pinCodeResponseData;

	}

	/**
	 * @param cartdata
	 * @return
	 */

	@Override
	public InvReserForDeliverySlotsResponseData convertDeliverySlotsDatatoWsdto(final InvReserForDeliverySlotsRequestData cartdata)
	{
		LOG.debug("from convertDeliverySlotsDatatoWsdto method in serice");
		final InvReserForDeliverySlotsResponseData response = new InvReserForDeliverySlotsResponseData();
		try
		{
			final EDDResponseWsDTO responseObject = getInventoryReservationService().convertDeliverySlotsDatatoWsdto(cartdata);
			if (null != responseObject.getCartId())
			{
				response.setCartId(responseObject.getCartId());
			}
			if (null != responseObject.getItemEDDInfo())
			{
				final List<InvReserForDeliverySlotsItemEDDInfoData> deliverySlotsItemEddInfoDTOList = new ArrayList<InvReserForDeliverySlotsItemEDDInfoData>();
				InvReserForDeliverySlotsItemEDDInfoData invReserForDeliverySlotsItemEDDInfoData = null;
				for (final EDDInfoWsDTO eddInfoResponse : responseObject.getItemEDDInfo())
				{
					invReserForDeliverySlotsItemEDDInfoData = new InvReserForDeliverySlotsItemEDDInfoData();
					invReserForDeliverySlotsItemEDDInfoData.setUssId(eddInfoResponse.getUssId());
					invReserForDeliverySlotsItemEDDInfoData.setEDD(eddInfoResponse.getEDD());
					invReserForDeliverySlotsItemEDDInfoData.setNextEDD(eddInfoResponse.getNextEDD());
					invReserForDeliverySlotsItemEDDInfoData.setIsScheduled(eddInfoResponse.getIsScheduled());
					invReserForDeliverySlotsItemEDDInfoData.setCodEligible(eddInfoResponse.getCODEligible());
					deliverySlotsItemEddInfoDTOList.add(invReserForDeliverySlotsItemEDDInfoData);
				}
				response.setInvReserForDeliverySlotsItemEDDInfoData(deliverySlotsItemEddInfoDTOList);
			}
			return response;
		}
		catch (final ClientEtailNonBusinessExceptions ex)
		{
			LOG.error("********* convertDeliverySlotsDatatoWsdto :");
			if (null != ex.getErrorCode() && ex.getErrorCode().equalsIgnoreCase("O0001"))
			{
				throw new ClientEtailNonBusinessExceptions("O0001", ex);
			}
			else if (null != ex.getErrorCode() && ex.getErrorCode().equalsIgnoreCase("O0002"))
			{
				throw new ClientEtailNonBusinessExceptions("O0002", ex);
			}
			else
			{
				throw new ClientEtailNonBusinessExceptions(ex);
			}


		}
	}

	//added for jewellery
	/**
	 * @param reqdata
	 * @param response
	 * @param cart
	 */
	private void updateCart(final InventoryReservListRequest request, final InventoryReservListResponse response,
			final AbstractOrderModel cart)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Inside updateCart...");
		}
		sessionService.removeAttribute("newUssidsAfterReplace");
		// YTODO Auto-generated method stub
		for (final InventoryReservJewelleryRequest item : request.getJewelleryItem())
		{
			for (final AbstractOrderEntryModel cartEntry : cart.getEntries())
			{
				final ProductModel productModel = cartEntry.getProduct();
				if (null != productModel && productModel.getCode().equalsIgnoreCase(item.getListingID()))
				{
					for (final InventoryReservResponse responseItem : response.getItem())
					{
						if (!cartEntry.getSelectedUSSID().equalsIgnoreCase(responseItem.getUSSID()))
						{
							for (final InventoryReservRequest requestItem : item.getItem())
							{
								if (LOG.isDebugEnabled())
								{
									LOG.debug("requestItem.getUSSID()=" + requestItem.getUSSID());
									LOG.debug("responseItem.getUSSID()=" + responseItem.getUSSID());
								}
								if (requestItem.getUSSID().equalsIgnoreCase(responseItem.getUSSID()))
								{

									replaceItemForJewellery(cart, responseItem.getUSSID(), cartEntry, productModel);
									sessionService.setAttribute("replacedUssid", Boolean.TRUE);
									String newUssidsAfterReplace = sessionService.getAttribute("newUssidsAfterReplace");
									if (StringUtils.isNotEmpty(newUssidsAfterReplace))
									{
										newUssidsAfterReplace += responseItem.getUSSID() + "#";
										sessionService.setAttribute("newUssidsAfterReplace", newUssidsAfterReplace);
									}
									else
									{
										sessionService.setAttribute("newUssidsAfterReplace", responseItem.getUSSID());
									}
								}


							}



						}



					}


				}

			}
		}

	}

	//added for jewellery

	/**
	 * @param cart
	 * @param ussid
	 * @param cartEntry
	 */
	private void replaceItemForJewellery(final AbstractOrderModel cart, final String ussid,
			final AbstractOrderEntryModel cartEntry, final ProductModel productModel)
	{
		// YTODO Auto-generated method stub
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Inside replaceItemForJewellery...");
		}

		try
		{
			final CommerceCartParameter removeParameter = new CommerceCartParameter();
			removeParameter.setEnableHooks(true);
			removeParameter.setCart((CartModel) cart);
			removeParameter.setEntryNumber(cartEntry.getEntryNumber().longValue());
			removeParameter.setQuantity(0);
			CommerceCartModification modification = mplCommerceCartService.updateQuantityForCartEntry(removeParameter);
			if (MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SUCCESS.equalsIgnoreCase(modification.getStatusCode()))
			{
				LOG.debug("Inside replaceItemForJewellery remove item status success...");
				final CommerceCartParameter addParameter = new CommerceCartParameter();
				addParameter.setCreateNewEntry(true);
				addParameter.setEnableHooks(true);
				addParameter.setCart((CartModel) cart);
				addParameter.setProduct(productModel);
				addParameter.setQuantity(1);
				addParameter.setUnit(productModel.getUnit());
				addParameter.setUssid(ussid);

				//mplCommerceCartService.addToCartWithUSSID(addParameter);
				modification = getMplDefaultCommerceAddToCartStrategyImpl().addToCartJwlReplacedItm(addParameter); //
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Add to cart modification status code=" + modification.getStatusCode());
				}
				//	setDeliveryModeForJewellery(ussid, cartEntry);
			}
		}
		catch (final CommerceCartModificationException e)
		{
			LOG.error("CommerceCartModificationException" + " " + e);
		}


	}


	/**
	 * @param cart
	 * @param ussid
	 */
	private void setDeliveryModeForJewellery(final String ussid, final AbstractOrderEntryModel cartEntryOld)
	{
		// YTODO Auto-generated method stub
		final CartModel cart = cartService.getSessionCart();
		if (cart.getEntries() != null)
		{
			for (final AbstractOrderEntryModel cartEntry : cart.getEntries())
			{
				if (cartEntry.getSelectedUSSID().equalsIgnoreCase(ussid))
				{
					if (null == cartEntry.getMplDeliveryMode())
					{
						cartEntry.setMplDeliveryMode(cartEntryOld.getMplDeliveryMode());
						getModelService().save(cartEntry);
						break;
					}

				}
			}
		}
	}

	/**
	 * @description: It is responsible to find selected delivery mode map
	 * @param cartData
	 * @return Map<String, List<String>>
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, MarketplaceDeliveryModeData> getDeliveryModeMapForReviewOrder(final CartData cartData,
			final List<PinCodeResponseData> omsDeliveryResponse) throws CMSItemNotFoundException
	{

		final Map<String, MarketplaceDeliveryModeData> deliveryModeDataMap = new HashMap<String, MarketplaceDeliveryModeData>();
		//List<MarketplaceDeliveryModeData> deliveryModeDataList = null;
		if (cartData != null)
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{

				final SellerInformationData sellerInformationData = entry.getSelectedSellerInformation();
				final String selectedUssid = entry.getSelectedUssid();
				//deliveryModeDataList = new ArrayList<MarketplaceDeliveryModeData>();

				for (final PinCodeResponseData pincodeRes : omsDeliveryResponse)
				{
					//					if (sellerInformationData != null && sellerInformationData.getUssid() != null && pincodeRes.getUssid() != null
					//							&& pincodeRes.getIsServicable() != null
					//							&& sellerInformationData.getUssid().equalsIgnoreCase(pincodeRes.getUssid())
					//							&& pincodeRes.getIsServicable().equalsIgnoreCase("Y") && !entry.isGiveAway())
					if (StringUtils.isNotEmpty(selectedUssid) && pincodeRes.getUssid() != null && pincodeRes.getIsServicable() != null
							&& selectedUssid.equalsIgnoreCase(pincodeRes.getUssid())
							&& pincodeRes.getIsServicable().equalsIgnoreCase("Y") && !entry.isGiveAway())

					{

						for (final DeliveryDetailsData deliveryData : pincodeRes.getValidDeliveryModes())
						{
							//TISUTO 63
							if (deliveryData.getInventory() != null)
							{
								String deliveryMode = MarketplacecommerceservicesConstants.EMPTYSPACE;
								String deliveryModeFromResponseData = MarketplacecommerceservicesConstants.EMPTYSPACE;
								final MarketplaceDeliveryModeData deliveryModeData = new MarketplaceDeliveryModeData();

								if (deliveryData.getType() != null
										&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.HD))
								{
									deliveryModeFromResponseData = MarketplacecommerceservicesConstants.HOME_DELIVERY;
								}
								else if (deliveryData.getType() != null
										&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.ED))
								{
									deliveryModeFromResponseData = MarketplacecommerceservicesConstants.EXPRESS_DELIVERY;
								}
								else if (deliveryData.getType() != null
										&& deliveryData.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))
								{
									deliveryModeFromResponseData = MarketplacecommerceservicesConstants.CLICK_COLLECT;
								}
								deliveryMode = entry.getMplDeliveryMode().getCode();
								if (deliveryData.getType() != null && deliveryModeFromResponseData.equalsIgnoreCase(deliveryMode))
								{

									MplZoneDeliveryModeValueModel deliveryModel = getMplDeliveryCostService().getDeliveryCost(
											deliveryMode, MarketplacecommerceservicesConstants.INR, sellerInformationData.getUssid(),
											deliveryData.getFulfilmentType());
									if (null == deliveryModel)
									{
										deliveryModel = getMplDeliveryCostService().getDeliveryCost(deliveryMode,
												MarketplacecommerceservicesConstants.INR, "DUMMYTSHIP", "TSHIP");
									}

									if (deliveryModel != null && deliveryModel.getValue() != null
											&& deliveryModel.getDeliveryMode() != null)
									{
										PriceData priceData = null;
										//										if (entry.isIsBOGOapplied()
										//												&& isFulFillmentTypeMatch(deliveryData.getFulfilmentType(), deliveryModel
										//														.getDeliveryFulfillModes().getCode(), sellerInformationData))
										//										{
										//											priceData = formPriceData(
										//													Double.valueOf(deliveryModel.getValue().doubleValue()
										//															* entry.getQualifyingCount().intValue()), cartData);
										//										}
										//										else
										if (isFulFillmentTypeMatch(deliveryData.getFulfilmentType(), deliveryModel
												.getDeliveryFulfillModes().getCode(), sellerInformationData))
										{
											//											priceData = formPriceData(
											//													Double.valueOf(deliveryModel.getValue().doubleValue() * entry.getQuantity().doubleValue()),
											//													cartData);
											priceData = entry.getCurrDelCharge();
										}
										else
										{

											priceData = formPriceData(Double.valueOf(0.0), cartData);
										}
										deliveryModeData.setCode(checkDataValue(deliveryModel.getDeliveryMode().getCode()));
										//deliveryModeData.setDescription(checkDataValue(deliveryModel.getDeliveryMode().getDescription()));


										final String startValue = deliveryModel.getDeliveryMode().getStart() != null ? ""
												+ deliveryModel.getDeliveryMode().getStart().intValue()
												: MarketplacecommerceservicesConstants.DEFAULT_START_TIME;

										final String endValue = deliveryModel.getDeliveryMode().getEnd() != null ? ""
												+ deliveryModel.getDeliveryMode().getEnd().intValue()
												: MarketplacecommerceservicesConstants.DEFAULT_END_TIME;


										deliveryModeData.setDescription(getDeliveryModeDescription(pincodeRes.getUssid(), deliveryModel
												.getDeliveryMode().getCode(), startValue, endValue));

										deliveryModeData.setName(checkDataValue(deliveryModel.getDeliveryMode().getName()));
										deliveryModeData.setSellerArticleSKU(checkDataValue(pincodeRes.getUssid()));
										deliveryModeData.setDeliveryCost(priceData);
										//New Code Added for TPR-579 : TSHIP Shipping Charges
										if (null != deliveryModel.getDeliveryFulfillModes()
												&& StringUtils.isNotEmpty(deliveryModel.getDeliveryFulfillModes().getCode()))
										{
											deliveryModeData.setFulfillmentType(deliveryModel.getDeliveryFulfillModes().getCode());
										}
									}
									//deliveryModeDataList.add(deliveryModeData);
									deliveryModeDataMap.put(entry.getEntryNumber().toString(), deliveryModeData);
								} //End of delivery data If
							}
						}
					}

				}
			}
		}

		return deliveryModeDataMap;
	}

	/**
	 * This method recalculates order
	 *
	 * @param orderModel
	 */
	@Override
	public void recalculateOrder(final OrderModel orderModel)
	{
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setOrder(orderModel);
		calculationStrategy.recalculateCart(parameter);
	}



	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}


	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * This service was developed for CAR-256
	 *
	 * @param site
	 * @param user
	 * @return CartModel
	 * @throws InvalidCartException
	 */
	@Override
	public CartModel fetchLatestCart(final BaseSiteModel site, final UserModel user) throws InvalidCartException
	{
		return mplCommerceCartDao.fetchLatestCart(site, user);
	}


	protected Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	//TPR-5346 STARTS
	/*
	 * This method will check the max quantity configured for the product at cart level
	 */
	@Override
	public long checkMaxLimit(final String code, CartModel cartModel)
	{
		// YTODO Auto-generated method stub
		long checkMaxLimList = 0L;
		if (cartModel == null)
		{
			cartModel = getCartService().getSessionCart();
		}
		if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				if (StringUtils.equalsIgnoreCase(code, entry.getProduct().getCode()))
				{
					//checkMaxLimList.add(entry.getProduct().getCode());
					checkMaxLimList += entry.getQuantity().longValue();
				}
			}
		}
		return checkMaxLimList;
	}

	/*
	 * This method will check the max quantity configured for the product and update the cart accordingly
	 */
	@Override
	public boolean checkMaxLimitUpdate(final long entryNumber, final long quantityToBeUpdated)
	{
		// YTODO Auto-generated method stub
		final int maximum_configured_quantiy = siteConfigService.getInt(
				MarketplacecommerceservicesConstants.MAXIMUM_CONFIGURED_QUANTIY, 0);
		final CartModel cartModel = getCartService().getSessionCart();
		//final ArrayList<String> checkMaxLimList = new ArrayList<String>();
		long checkMaxLimList = 0L;
		long cartmaxcount = 0L;
		final AbstractOrderEntryModel entryToUpdate = getEntryForNumberUpdate(cartModel, (int) entryNumber);
		if (entryToUpdate != null)
		{
			checkMaxLimList = checkMaxLimit(entryToUpdate.getProduct().getCode(), null);
		}
		if (entryToUpdate.getProduct().getMaxOrderQuantity() != null
				&& entryToUpdate.getProduct().getMaxOrderQuantity().intValue() > 0
				&& entryToUpdate.getProduct().getMaxOrderQuantity().intValue() < maximum_configured_quantiy)
		{
			//			if (quantityToBeUpdated < checkMaxLimList)
			cartmaxcount = quantityToBeUpdated + checkMaxLimList;

			if (checkMaxLimList <= entryToUpdate.getProduct().getMaxOrderQuantity().intValue()
					&& quantityToBeUpdated <= entryToUpdate.getProduct().getMaxOrderQuantity().intValue()
					&& cartmaxcount <= entryToUpdate.getProduct().getMaxOrderQuantity().intValue())
			{
				//return true;
				entryToUpdate.setMaxCountReached(true);
				getModelService().save(entryToUpdate);
				//	getModelService().save(cartModel);
				return true;
			}
			if (checkMaxLimList <= entryToUpdate.getProduct().getMaxOrderQuantity().intValue()
					&& quantityToBeUpdated <= entryToUpdate.getProduct().getMaxOrderQuantity().intValue()
					&& entryToUpdate.getQuantity().longValue() >= quantityToBeUpdated)
			{
				entryToUpdate.setMaxCountReached(true);
				getModelService().save(entryToUpdate);
				//getModelService().save(cartModel);
				return true;
			}
			if (checkMaxLimList == 0)
			{
				//return true;
				entryToUpdate.setMaxCountReached(true);
				getModelService().save(entryToUpdate);
				//getModelService().save(cartModel);
				return true;
			}
			if (checkMaxLimList >= entryToUpdate.getProduct().getMaxOrderQuantity().intValue())
			{
				//return false;
				entryToUpdate.setMaxCountReached(false);
				getModelService().save(entryToUpdate);
				//	getModelService().save(cartModel);
				return false;
			}
		}
		//	getModelService().save(entryToUpdate);
		return true;
	}

	/**
	 * Will get the entry number which will be updated
	 *
	 * @param cartModel
	 * @param entryNumber
	 * @return AbstractOrderEntryModel
	 */
	protected AbstractOrderEntryModel getEntryForNumberUpdate(final CartModel cartModel, final int entryNumber)
	{
		if (cartModel.getEntries() != null && CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			final Integer requestedEntryNumber = Integer.valueOf(entryNumber);
			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{
				if ((entry != null) && (requestedEntryNumber.equals(entry.getEntryNumber())))
				{
					return entry;
				}
			}
		}
		return null;
	}

	/*
	 * This method will check the max quantity configured for the product at cart level
	 */
	@Override
	public boolean checkMaxLimitCheckout(final CartModel serviceCart)
	{
		// YTODO Auto-generated method stub
		final int maximum_configured_quantiy = siteConfigService.getInt(
				MarketplacecommerceservicesConstants.MAXIMUM_CONFIGURED_QUANTIY, 0);
		long quantity = 0L;
		int maxCount = 0;
		int count = 0;

		final Map<AbstractOrderEntryModel, String> productList = new HashMap<>();
		final Set<String> uniqueProductList = new HashSet<>();

		for (final AbstractOrderEntryModel entry : serviceCart.getEntries())
		{
			if (entry.getProduct().getMaxOrderQuantity() != null)
			{
				if (entry.getProduct().getMaxOrderQuantity().intValue() <= maximum_configured_quantiy
						&& entry.getProduct().getMaxOrderQuantity().intValue() > 0)
				{
					if (entry.getProduct().getMaxOrderQuantity().intValue() < entry.getQuantity().intValue())
					{
						return false;
					}
				}
			}
			productList.put(entry, entry.getProduct().getCode());
			uniqueProductList.add(entry.getProduct().getCode());
		}

		if (productList.size() != uniqueProductList.size())
		{
			final Multimap<String, AbstractOrderEntryModel> multiMap = HashMultimap.create();
			for (final Entry<AbstractOrderEntryModel, String> entry : productList.entrySet())
			{
				multiMap.put(entry.getValue(), entry.getKey());
			}

			for (final Entry<String, Collection<AbstractOrderEntryModel>> entry : multiMap.asMap().entrySet())
			{
				for (final AbstractOrderEntryModel entries : entry.getValue())
				{
					quantity += entries.getQuantity().longValue();
					count++;
					if (count == entry.getValue().size())
					{

						maxCount = (entries.getProduct().getMaxOrderQuantity() != null ? entries.getProduct().getMaxOrderQuantity()
								.intValue() : 0);
						break;
					}
				}
				if (maxCount > 0 && maxCount < maximum_configured_quantiy)
				{
					if (quantity == 0)
					{
						return true;
					}
					if (quantity >= maxCount)
					{
						return false;
					}
				}

			}
		}
		return true;
	}

	//TPR-5346 ENDS

	//TPR-5666 samsung cart changes
	/*
	 * (non-Javadoc)
	 *
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService#fetchCartUsingGuid(java.lang.String)
	 */
	@Override
	public CartModel fetchCartUsingGuid(final String cartGuid) throws InvalidCartException
	{
		return mplCommerceCartDao.getCartByGuid(cartGuid);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService#addItemToCartWithExchange(java.lang.String
	 * , de.hybris.platform.core.model.order.CartModel, de.hybris.platform.core.model.product.ProductModel, long,
	 * java.lang.String, java.lang.String)
	 */

	@Override
	public boolean addItemToCartWithExchange(final String cartId, final CartModel cartModel, final ProductModel productModel,
			final long quantity, final String ussid, final String exchangeParam) throws InvalidCartException,
			CommerceCartModificationException
	{

		boolean success = false;
		//commented for car project implementation CAR:62
		//CartData cartData = null;
		final CommerceCartParameter parameter = new CommerceCartParameter();
		CartModificationData cartModificationData = null;
		String sellerUssId = null;

		if (cartModel != null)
		{
			// Check if product code is valid or not
			if (productModel == null)
			{
				throw new CommerceCartModificationException("Invalid ussid " + ussid);
			}

			//check if quantity is valid or not
			if (quantity <= 0)
			{
				throw new CommerceCartModificationException("Invalid quantity " + quantity);
			}
			//commented for car project implementation CAR:62
			//cartData = getCartConverter().convert(cartModel);

			// Check if ussid is null then fetch ussid
			if (ussid == null || ussid.isEmpty())
			{
				sellerUssId = getSellerUssid(cartModel);
			}
			else
			{
				sellerUssId = ussid;
			}
			//			if (isMaxQuantityAlreadyAdded(cartData, productCode))
			//commented for car project implementation CAR:62: added cartModel instead of carData as param in isMaxQuantityAlreadyAdded
			//if (isMaxQuantityAlreadyAdded(cartData, sellerUssId, quantity, productModel.getCode()))
			//TISJEW-4496
			if (isMaxQuantityAlreadyAddedExchange(cartModel, sellerUssId, quantity, productModel.getCode()))
			{
				parameter.setEnableHooks(true);
				parameter.setCart(cartModel);
				parameter.setProduct(productModel);
				parameter.setQuantity(quantity);
				parameter.setUnit(productModel.getUnit());
				parameter.setCreateNewEntry(false);
				parameter.setUssid(sellerUssId);

				//final CommerceCartModification modification = addToCartWithUSSID(parameter);
				final String cartGuid = cartModel.getGuid();
				final String paramList[] = exchangeParam.split(Pattern.quote("|"));
				String tempId = "";
				if (paramList.length == 5 && StringUtils.isNotEmpty(cartGuid))
				{
					tempId = exchangeService.getExchangeID(paramList[0], paramList[1], paramList[2], paramList[3], paramList[4],
							cartGuid, productModel.getCode(), ussid);
				}

				parameter.setExchangeParam(tempId);
				final CommerceCartModification modification = getMplDefaultCommerceAddToCartStrategyImpl().addToCart(parameter);
				cartModificationData = getCartModificationConverter().convert(modification);

				success = (cartModificationData != null) ? true : false;
			}
			else
			{
				throw new CommerceCartModificationException("Maximum quantity reached ");
			}
		}
		else
		{
			throw new CommerceCartModificationException(" Required Paramteres are null or empty");
		}

		return success;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService#populatePriceDisclaimerCart(de.hybris.
	 * platform.core.model.order.CartModel)
	 */
	@Override
	public String populatePriceDisclaimerCart(final CartModel cartModel)
	{
		// YTODO Auto-generated method stub
		final List<String> discSellerNameList = new ArrayList<String>();
		String displayMsgFinal = "";
		for (final AbstractOrderEntryModel orderEntry : cartModel.getEntries())
		{
			if (MarketplacecommerceservicesConstants.FINEJEWELLERY
					.equalsIgnoreCase(orderEntry.getProduct().getProductCategoryType()))
			{
				final String sellerId = orderEntry.getSellerInfo();
				final String sellers = getConfigurationService().getConfiguration().getString("cart.jewellery.disclaimer.sellerName");
				if (StringUtils.isNotEmpty(sellers))
				{
					final String sellersArray[] = sellers.split(",");
					if (StringUtils.isNotEmpty(sellerId))
					{
						for (final String s : sellersArray)
						{
							if (StringUtils.equalsIgnoreCase(sellerId, s))
							{
								discSellerNameList.add(s + ",");
							}
						}
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(discSellerNameList))
		{
			String displayMsg = "";
			for (final String sellerName : discSellerNameList)
			{
				if (StringUtils.isEmpty(displayMsg))
				{
					displayMsg = sellerName;
				}
				else
				{
					displayMsg = displayMsg + sellerName;
				}
			}
			displayMsgFinal = getConfigurationService().getConfiguration().getString("cart.price.disclaimer.first")
					+ MarketplacecommerceservicesConstants.SPACE + displayMsg.substring(0, displayMsg.length() - 1)
					+ MarketplacecommerceservicesConstants.SPACE
					+ getConfigurationService().getConfiguration().getString("cart.price.disclaimer.second");
		}
		return displayMsgFinal;
	}

}