/**
 *
 */
package com.tisl.mpl.facade.checkout.impl;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotions.util.Tuple2;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import net.sourceforge.pmd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MplGlobalCodeConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.mplconfig.service.MplConfigService;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.StoreLocationRequestData;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.marketplacecommerceservices.order.MplCommerceCartCalculationStrategy;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDelistingService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pincode.facade.PinCodeServiceAvilabilityFacade;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.wsdto.GetWishListWsDTO;




/**
 * @author TCS
 *
 */
public class MplCartFacadeImpl extends DefaultCartFacade implements MplCartFacade
{
	private static final Logger LOG = Logger.getLogger(MplCartFacadeImpl.class);
	private static final String FINEJEWELLERY = "FineJewellery";

	private ProductService productService;
	private CartService cartService;
	private MplCommerceCartService mplCommerceCartService;
	private ModelService modelService;
	private Converter<CartModel, CartData> mplExtendedCartConverter;
	private Converter<CartModel, CartData> mplExtendedPromoCartConverter;
	@Autowired
	private CommerceCartService commerceCartService;

	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private UserService userService;

	@Autowired
	private PinCodeServiceAvilabilityFacade pinCodeFacade;

	@Autowired
	private SiteConfigService siteConfigService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private MplDelistingService mplDelistingService;

	@Autowired
	private CatalogService catalogService;


	@Resource(name = "pincodeService")
	private PincodeService pincodeService;

	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;
	@Resource(name = "notificationService")
	private NotificationService notificationService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;


	@Autowired
	private MplConfigService mplConfigService;
	@Resource
	MplCommerceCartCalculationStrategy mplDefaultCommerceCartCalculationStrategy;


	public MplCommerceCartCalculationStrategy getMplDefaultCommerceCartCalculationStrategy()
	{
		return mplDefaultCommerceCartCalculationStrategy;
	}


	public void setMplDefaultCommerceCartCalculationStrategy(
			final MplCommerceCartCalculationStrategy mplDefaultCommerceCartCalculationStrategy)
	{
		this.mplDefaultCommerceCartCalculationStrategy = mplDefaultCommerceCartCalculationStrategy;
	}


	/*
	 * @Desc fetching cartdata with selected ussid
	 *
	 *
	 *
	 *
	 * @param recentlyAddedFirst
	 *
	 *
	 *
	 *
	 * @return CartData
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public CartData getSessionCartWithEntryOrdering(final boolean recentlyAddedFirst) throws EtailNonBusinessExceptions
	{
		if (hasSessionCart())
		{
			final CartData data = getSessionCart();

			if (recentlyAddedFirst)
			{
				final List<OrderEntryData> listEntries = data.getEntries();
				final List<OrderEntryData> recentlyAddedListEntries = new ArrayList<OrderEntryData>();

				for (int index = listEntries.size(); index > 0; index--)
				{
					recentlyAddedListEntries.add(listEntries.get(index - 1));
				}
				data.setEntries(Collections.unmodifiableList(recentlyAddedListEntries));
			}
			return data;
		}
		return createEmptyCart();
	}

	/*
	 * @Desc fetching cartdata with selected ussid in Mobile
	 *
	 * @param recentlyAddedFirst
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public CartData getSessionCartWithEntryOrderingMobile(final CartModel cart, final boolean recentlyAddedFirst)
			throws EtailNonBusinessExceptions
	{
		final Map<String, String> orderEntryToUssidMap = new HashMap<String, String>();
		final List<OrderEntryData> recentlyAddedListEntries = new ArrayList<OrderEntryData>();
		CartData cartData = null;

		if (cart != null)
		{
			final List<AbstractOrderEntryModel> cartEntryModels = cart.getEntries();
			cartData = getMplExtendedPromoCartConverter().convert(cart); //TISPT-104
			//	cartData = getMplExtendedCartConverter().convert(cart);
			final List<OrderEntryData> orderEntryDatas = cartData.getEntries();

			if (recentlyAddedFirst)
			{
				for (int index = orderEntryDatas.size(); index > 0; index--)
				{
					recentlyAddedListEntries.add(orderEntryDatas.get(index - 1));
				}
				cartData.setEntries(Collections.unmodifiableList(recentlyAddedListEntries));
			}


			for (final AbstractOrderEntryModel abstractOrderEntryModel : cartEntryModels)
			{
				if (null != abstractOrderEntryModel.getSelectedUSSID() && !abstractOrderEntryModel.getSelectedUSSID().isEmpty())
				{

					orderEntryToUssidMap.put(abstractOrderEntryModel.getEntryNumber().toString(),
							abstractOrderEntryModel.getSelectedUSSID());
				}
				else
				{
					for (final OrderEntryData orderEntryData : orderEntryDatas)
					{
						if (orderEntryData.getEntryNumber() == abstractOrderEntryModel.getEntryNumber())
						{
							final List<SellerInformationModel> sellerInformationModels = (List<SellerInformationModel>) abstractOrderEntryModel
									.getProduct().getSellerInformationRelator();
							if (sellerInformationModels != null && !sellerInformationModels.isEmpty())
							{
								final SellerInformationModel sellerInformationModel = sellerInformationModels.get(0);
								final SellerInformationData sellerInformationData = new SellerInformationData();
								sellerInformationData.setSellerID(sellerInformationModel.getSellerID());
								sellerInformationData.setUssid(sellerInformationModel.getSellerArticleSKU());
								sellerInformationData.setSellername(sellerInformationModel.getSellerName());
								orderEntryData.setSelectedSellerInformation(sellerInformationData);
							}
						}
					}
				}
			}
			for (final Entry<String, String> entryNumber : orderEntryToUssidMap.entrySet())
			{
				for (final OrderEntryData orderEntryData : orderEntryDatas)
				{
					if (orderEntryData.getEntryNumber() != null
							&& orderEntryData.getEntryNumber().toString().equals(entryNumber.getKey()))
					{
						LOG.debug("Matched Entry number from Mapped entry number and ussid >>>>>>" + entryNumber.getKey());
						final ProductData productData = orderEntryData.getProduct();
						final List<SellerInformationData> sellerInformationDatas = productData.getSeller();
						for (final SellerInformationData sellerInformationData : sellerInformationDatas)
						{
							if (entryNumber.getValue().equals(sellerInformationData.getUssid()))
							{
								LOG.debug("got seller information data for cart line item number " + orderEntryData.getEntryNumber()
										+ "seller name " + sellerInformationData.getSellername());
								orderEntryData.setSelectedSellerInformation(sellerInformationData);
							}
							else if (FINEJEWELLERY.equalsIgnoreCase(productData.getRootCategory()))
							{
								final List<JewelleryInformationModel> jewelleryInfo = jewelleryService
										.getJewelleryInfoByUssid(entryNumber.getValue());
								if (CollectionUtils.isNotEmpty(jewelleryInfo))
								{
									final JewelleryInformationModel infoModel = jewelleryInfo.get(0);
									if (infoModel.getPCMUSSID().equalsIgnoreCase(sellerInformationData.getUssid()))
									{
										LOG.debug("got seller information data for cart line Jewellery item :"
												+ orderEntryData.getEntryNumber() + "seller name " + sellerInformationData.getSellername());
										orderEntryData.setSelectedSellerInformation(sellerInformationData);
									}
								}
							}
						}
					}
				}
			}

			return cartData;
		}

		return createEmptyCart();
	}

	/*
	 * @Desc fetching cartdata using session cart
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public CartData getSessionCart() throws EtailNonBusinessExceptions
	{
		final CartData cartData;
		if (hasSessionCart())
		{
			final CartModel cart = getCartService().getSessionCart();
			final List<AbstractOrderEntryModel> cartEntryModels = cart.getEntries();
			cartData = getMplExtendedCartConverter().convert(cart);
			final List<OrderEntryData> orderEntryDatas = cartData.getEntries();
			final Map<String, String> orderEntryToUssidMap = new HashMap<String, String>();

			for (final AbstractOrderEntryModel abstractOrderEntryModel : cartEntryModels)
			{
				if (null != abstractOrderEntryModel.getSelectedUSSID() && !abstractOrderEntryModel.getSelectedUSSID().isEmpty())
				{
					final String ussid = abstractOrderEntryModel.getSelectedUSSID();
					orderEntryToUssidMap.put(abstractOrderEntryModel.getEntryNumber().toString(), ussid);
				}
				else
				{
					for (final OrderEntryData orderEntryData : orderEntryDatas)
					{
						if (orderEntryData.getEntryNumber() == abstractOrderEntryModel.getEntryNumber())
						{
							final List<SellerInformationModel> sellerInformationModels = (List<SellerInformationModel>) abstractOrderEntryModel
									.getProduct().getSellerInformationRelator();
							if (sellerInformationModels != null && !sellerInformationModels.isEmpty())
							{
								final SellerInformationModel sellerInformationModel = sellerInformationModels.get(0);
								final SellerInformationData sellerInformationData = new SellerInformationData();
								sellerInformationData.setSellerID(sellerInformationModel.getSellerID());
								sellerInformationData.setUssid(sellerInformationModel.getSellerArticleSKU());
								sellerInformationData.setSellername(sellerInformationModel.getSellerName());
								orderEntryData.setSelectedSellerInformation(sellerInformationData);
							}
						}
					}
				}
			}
			for (final Entry<String, String> entryNumber : orderEntryToUssidMap.entrySet())
			{
				for (final OrderEntryData orderEntryData : orderEntryDatas)
				{
					if (orderEntryData.getEntryNumber() != null
							&& orderEntryData.getEntryNumber().toString().equals(entryNumber.getKey()))
					{
						LOG.debug("Matched Entry number from Mapped entry number and ussid >>>>>>" + entryNumber.getKey());
						final ProductData productData = orderEntryData.getProduct();
						final List<SellerInformationData> sellerInformationDatas = productData.getSeller();
						for (final SellerInformationData sellerInformationData : sellerInformationDatas)
						{
							if (entryNumber.getValue().equals(sellerInformationData.getUssid()))
							{
								LOG.debug("got seller information data for cart line item number " + orderEntryData.getEntryNumber()
										+ "seller name " + sellerInformationData.getSellername());
								orderEntryData.setSelectedSellerInformation(sellerInformationData);
							}
							else if (FINEJEWELLERY.equalsIgnoreCase(productData.getRootCategory()))
							{
								final List<JewelleryInformationModel> jewelleryInfo = jewelleryService
										.getJewelleryInfoByUssid(entryNumber.getValue());
								if (CollectionUtils.isNotEmpty(jewelleryInfo))
								{
									final JewelleryInformationModel infoModel = jewelleryInfo.get(0);
									if (infoModel.getPCMUSSID().equalsIgnoreCase(sellerInformationData.getUssid()))
									{

										LOG.debug("got seller information data for cart line Jewellery item :"
												+ orderEntryData.getEntryNumber() + "seller name " + sellerInformationData.getSellername());
										orderEntryData.setSelectedSellerInformation(sellerInformationData);
									}
								}
							}
						}
					}
				}
			}
		}
		else
		{
			cartData = createEmptyCart();
		}
		return cartData;
	}

	/*
	 * @Desc fetching cart details for current user
	 *
	 *
	 *
	 *
	 * @return List<CartData>
	 */
	@Override
	public List<CartData> getCartsForCurrentUser()
	{
		return Converters.convertAll(
				commerceCartService.getCartsForSiteAndUser(baseSiteService.getCurrentBaseSite(), userService.getCurrentUser()),
				getMplExtendedCartConverter());
	}

	/*
	 * @Desc add selected ussid to cart
	 *
	 *
	 *
	 *
	 * @param code
	 *
	 *
	 *
	 *
	 * @param quantity
	 *
	 *
	 *
	 *
	 * @param ussid
	 *
	 *
	 *
	 *
	 * @return CartModificationData
	 *
	 *
	 *
	 *
	 * @throws CommerceCartModificationException
	 */

	//mrpEntryPrice added to pass the MRP - TPR-774
	@Override
	public CartModificationData addToCart(final String code, final long quantity, final String ussid)
			throws CommerceCartModificationException//final Double mrpEntryPrice
	{
		final ProductModel product = getProductService().getProductForCode(code);
		final CartModel cartModel = getCartService().getSessionCart();
		final CommerceCartParameter parameter = new CommerceCartParameter();
		for (final AbstractOrderEntryModel orderEntry : cartModel.getEntries())
		{
			if (orderEntry != null && orderEntry.getSelectedUSSID() != null && orderEntry.getSelectedUSSID().equalsIgnoreCase(ussid))
			{
				parameter.setCreateNewEntry(false);
				break;
			}
			else
			{
				parameter.setCreateNewEntry(true);
			}
		}

		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setProduct(product);
		parameter.setQuantity(quantity);
		parameter.setUnit(product.getUnit());
		parameter.setUssid(ussid);
		// passing the MRP to cart- TPR-774
		//		if (null != mrpEntryPrice)
		//		{
		//			parameter.setEntryMrp(mrpEntryPrice);
		//		}

		final CommerceCartModification modification = getMplCommerceCartService().addToCartWithUSSID(parameter);

		return getCartModificationConverter().convert(modification);
	}

	/*
	 * @Desc fetching seller info
	 *
	 *
	 *
	 *
	 * @param cartData
	 *
	 *
	 *
	 *
	 * @param ussid
	 *
	 *
	 *
	 *
	 * @return Map<String, String>
	 *
	 *
	 *
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, String> getSellerInfo(final CartData cartData, final String ussid) throws CMSItemNotFoundException
	{
		return mplCommerceCartService.getSellerInfo(cartData, ussid);
	}

	/*
	 * @Desc fetching address
	 *
	 *
	 *
	 *
	 * @param addressData
	 *
	 *
	 *
	 *
	 * @return Map<String, String>
	 *
	 *
	 *
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, String> getAddress(final List<AddressData> addressData) throws CMSItemNotFoundException
	{
		return mplCommerceCartService.getAddress(addressData);
	}

	/*
	 * @Desc fetching fulfilmentmode
	 *
	 *
	 *
	 *
	 * @param cartData
	 *
	 *
	 *
	 *
	 * @return Map<String, String>
	 *
	 *
	 *
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, String> getFullfillmentMode(final CartData cartData) throws CMSItemNotFoundException
	{
		return mplCommerceCartService.getFullfillmentMode(cartData);
	}

	/*
	 * @Desc fetching fulfilmentmode TISEE-6290
	 *
	 *
	 *
	 *
	 * @param cartData
	 *
	 *
	 *
	 *
	 * @return Map<String, String>
	 *
	 *
	 *
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, String> getOrderEntryFullfillmentMode(final OrderData orderData) throws CMSItemNotFoundException
	{
		return mplCommerceCartService.getOrderEntryFullfillmentMode(orderData);
	}

	/*
	 * @Desc fetching delivery mode
	 *
	 *
	 *
	 *
	 * @param cartData
	 *
	 *
	 *
	 *
	 * @param omsDeliveryResponse
	 *
	 *
	 *
	 *
	 * @return Map<String, List<MarketplaceDeliveryModeData>>
	 *
	 *
	 *
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData,
			final List<PinCodeResponseData> omsDeliveryResponse, final CartModel cartModel) throws CMSItemNotFoundException
	{
		// Changes for Duplicate Cart fix
		return mplCommerceCartService.getDeliveryMode(cartData, omsDeliveryResponse, cartModel);
	}

	/*
	 * @Desc fetching default pin code
	 *
	 *
	 *
	 *
	 * @param addressData
	 *
	 *
	 *
	 *
	 * @return String
	 *
	 *
	 *
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public String getDefaultPinCode(final AddressData addressData, final String defaultPinCodeId) throws CMSItemNotFoundException
	{
		return mplCommerceCartService.getDefaultPinCode(addressData, defaultPinCodeId);

	}

	/*
	 * @Desc fetching cart details for ussid
	 *
	 *
	 *
	 *
	 * @param ussid
	 *
	 *
	 *
	 *
	 * @return Collection<CartModel>
	 *
	 *
	 *
	 *
	 * @throws InvalidCartException
	 */
	@Override
	public Collection<CartModel> getCartDetails(final String ussid) throws InvalidCartException
	{
		return mplCommerceCartService.getCartDetails(ussid);
	}

	/**
	 * @Desc fetching gift yourself details
	 * @param minGiftQuantity
	 * @param allWishlists
	 * @param pincode
	 * @param cartModel
	 * @return List<Wishlist2EntryModel>
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Tuple2<?, ?> getGiftYourselfDetails(final int minGiftQuantity, final List<Wishlist2Model> allWishlists,
			final String pincode, final CartModel cartModel) throws CMSItemNotFoundException
	{
		return mplCommerceCartService.getGiftYourselfDetails(minGiftQuantity, allWishlists, pincode, cartModel); //TISPT-179 Point 2
	}

	//	@Override TISPT-179 Point 3
	//	public List<Wishlist2EntryModel> getGiftYourselfDetails(final int minGiftQuantity, final List<Wishlist2Model> allWishlists,
	//			final String pincode, final CartModel cartModel) throws CMSItemNotFoundException
	//	{
	//		return mplCommerceCartService.getGiftYourselfDetails(minGiftQuantity, allWishlists, pincode, cartModel); //TISPT-179 Point 2
	//	}

	/**
	 * Method for creating empty cart if no cart is associated with user id for Mobile Service
	 *
	 * User emailId , for anonymous user emailId : null
	 **
	 * @param emailId
	 * @param baseSiteId
	 *
	 * @return CartModel
	 * @throws CommerceCartModificationException
	 */
	@Override
	public CartModel createCart(final String emailId, final String baseSiteId) throws InvalidCartException,
			CommerceCartModificationException
	{
		return mplCommerceCartService.createCart(emailId, baseSiteId);
	}

	/**
	 * Method for adding item to cart for Mobile service
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
		return mplCommerceCartService.addItemToCart(cartId, cartModel, productModel, quantity, ussid);
	}

	/*
	 * @Desc setting cart sub total
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public void setCartSubTotal() throws EtailNonBusinessExceptions
	{
		double subtotal = 0.0;
		double totalPrice = 0.0;
		Double discountValue = Double.valueOf(0.0);
		final CartModel cartModel = getCartService().getSessionCart();
		boolean cartSaveRequired = false; //TISPT 80
		if (cartModel != null)
		{
			final List<AbstractOrderEntryModel> entries = cartModel.getEntries();
			for (final AbstractOrderEntryModel entry : entries)
			{
				final double entryTotal = entry.getQuantity().doubleValue() * entry.getBasePrice().doubleValue();
				subtotal += entryTotal;
				//TISEE-581
				if (entry.getPrevDelCharge().doubleValue() > 0 || entry.getCurrDelCharge().doubleValue() > 0)
				{
					cartSaveRequired = true;
					entry.setPrevDelCharge(Double.valueOf(0));
					entry.setCurrDelCharge(Double.valueOf(0));
				}
				//getModelService().save(entry); //TISPT 80
			}
			if (cartSaveRequired)
			{
				getModelService().saveAll(entries);
			}

			//final CartData cartData = mplExtendedCartConverter.convert(cartModel);

			final CartData cartData = getMplExtendedPromoCartConverter().convert(cartModel); //TISPT-104


			////TISST-13010
			if (cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)
			{
				discountValue = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());
			}

			totalPrice = subtotal - discountValue.doubleValue();

			cartModel.setSubtotal(Double.valueOf(subtotal));
			cartModel.setTotalPrice(Double.valueOf(totalPrice));
			cartModel.setTotalPriceWithConv(Double.valueOf(totalPrice));
			getModelService().save(cartModel);
		}
	}



	@Override
	public boolean setCartSubTotal2(final CartModel cartModel) throws EtailNonBusinessExceptions
	{
		double subtotal = 0.0;
		double totalPrice = 0.0;
		Double discountValue = Double.valueOf(0.0);
		boolean subtotalChanged = false;
		//	final CartModel cartModel = getCartService().getSessionCart();
		if (cartModel != null)
		{
			if (null != cartModel.getDeliveryCost() && 0.0 != cartModel.getDeliveryCost().doubleValue())
			{
				final List<AbstractOrderEntryModel> entries = cartModel.getEntries();
				for (final AbstractOrderEntryModel entry : entries)
				{
					final double entryTotal = entry.getQuantity().doubleValue() * entry.getBasePrice().doubleValue();
					subtotal += entryTotal;
					//TISEE-581
					entry.setPrevDelCharge(Double.valueOf(0));
					entry.setCurrDelCharge(Double.valueOf(0));
					getModelService().save(entry);
					subtotalChanged = true;
				}

				//final CartData cartData = mplExtendedCartConverter.convert(cartModel);
				////TISST-13010

				//				if (cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)
				//				{
				//					discountValue = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());
				//				}

				if (cartModel.getTotalDiscounts() != null && cartModel.getTotalDiscounts() != null)
				{
					discountValue = Double.valueOf(cartModel.getTotalDiscounts().doubleValue());
				}

				totalPrice = subtotal - discountValue.doubleValue();
				cartModel.setSubtotal(Double.valueOf(subtotal));
				cartModel.setTotalPrice(Double.valueOf(totalPrice));
				cartModel.setTotalPriceWithConv(Double.valueOf(totalPrice));
				getModelService().save(cartModel);
			}
		}

		return subtotalChanged;

	}


	/*
	 * @Desc fetching pincode response
	 *
	 *
	 *
	 *
	 * @param pincode
	 *
	 *
	 *
	 *
	 * @param cartData
	 *
	 *
	 *
	 *
	 * @return List<PinCodeResponseData>
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<PinCodeResponseData> getOMSPincodeResponseData(final String pincode, final CartData cartData)
			throws EtailNonBusinessExceptions
	{
		List<PinCodeResponseData> pinCodeResponseData = null;

		final List<PincodeServiceData> pincodeServiceReqDataList = new ArrayList<PincodeServiceData>();



		final PincodeModel pinCodeModelObj = pincodeService.getLatAndLongForPincode(pincode);
		final LocationDTO dto = new LocationDTO();
		Location myLocation = null;
		double configurableRadius = 0;
		if (null != pinCodeModelObj)
		{
			try
			{
				final String configRadius = mplConfigService.getConfigValueById(MarketplaceFacadesConstants.CONFIGURABLE_RADIUS);
				configurableRadius = Double.parseDouble(configRadius);
				LOG.debug("**********configrableRadius:" + configurableRadius);
				dto.setLongitude(pinCodeModelObj.getLongitude().toString());
				dto.setLatitude(pinCodeModelObj.getLatitude().toString());
				myLocation = new LocationDtoWrapper(dto);
				LOG.debug("Selected Location for Latitude:" + myLocation.getGPS().getDecimalLatitude());
				LOG.debug("Selected Location for Longitude:" + myLocation.getGPS().getDecimalLongitude());
			}
			catch (final Exception e)
			{
				LOG.error("configurableRadius values is empty please add radius property in properties file ");
			}
		}
		else
		{
			return pinCodeResponseData;
		}


		for (final OrderEntryData entryData : cartData.getEntries())
		{



			if (!entryData.isGiveAway())
			{
				final PincodeServiceData pincodeServiceData = new PincodeServiceData();
				pincodeServiceData.setProductCode(entryData.getProduct().getCode());
				final SellerInformationData sellerData = entryData.getSelectedSellerInformation();
				if (sellerData != null)
				{
					if (StringUtils.isNotEmpty(sellerData.getFullfillment()))
					{
						final String globalCodeFulfilmentType = MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(sellerData

						.getFullfillment().toUpperCase());
						if (StringUtils.isNotEmpty(globalCodeFulfilmentType))
						{
							pincodeServiceData.setFullFillmentType(globalCodeFulfilmentType.toUpperCase());
						}
						else
						{
							LOG.debug("getOMSPincodeResponseData : GLOBALCONSTANTSMAP fulfilement type not found");
						}
					}
					else
					{
						LOG.debug("getOMSPincodeResponseData : Fullfillment is null or empty for product selected");
					}

					if (StringUtils.isNotEmpty(sellerData.getShippingMode()))
					{
						final String globalCodeShippingMode = MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(sellerData

						.getShippingMode().toUpperCase());
						if (StringUtils.isNotEmpty(globalCodeShippingMode))
						{
							pincodeServiceData.setTransportMode(globalCodeShippingMode.toUpperCase());
						}
						else
						{
							LOG.debug("getOMSPincodeResponseData : GLOBALCONSTANTSMAP Transport mode not found");
						}
					}
					else
					{
						LOG.debug("getOMSPincodeResponseData : ShippingMode is null or empty for product selected");
					}

					if (StringUtils.isNotEmpty(sellerData.getSellerID()))
					{
						pincodeServiceData.setSellerId(sellerData.getSellerID());
					}
					else
					{
						LOG.debug("getOMSPincodeResponseData : Seller id is null or empty for product selected");
					}

					if (StringUtils.isNotEmpty(entryData.getSelectedUssid()))
					{
						pincodeServiceData.setUssid(entryData.getSelectedUssid());
					}
					else
					{
						LOG.debug("getOMSPincodeResponseData : Ussid is null or empty for product selected");
					}

					if (StringUtils.isNotEmpty(sellerData.getIsCod()))
					{
						pincodeServiceData.setIsCOD(sellerData.getIsCod().toUpperCase());
					}
					else
					{
						LOG.debug("getOMSPincodeResponseData : Seller COD is null or empty for product selected");
					}


					if (entryData.getAmountAfterAllDisc() != null && entryData.getAmountAfterAllDisc().getValue() != null
							&& entryData.getAmountAfterAllDisc().getValue().doubleValue() > 0.0 && entryData.getQuantity() != null
							&& entryData.getQuantity().doubleValue() > 0)
					{
						//pincodeServiceData.setPrice(Double.valueOf(entryData.getAmountAfterAllDisc().getValue().doubleValue()));
						//TISEE-5293
						Double apportionPrice = Double.valueOf(entryData.getAmountAfterAllDisc().getValue().doubleValue());
						final Long entryQuantity = entryData.getQuantity();
						//TISPRDT-155
						if (!entryData.isIsBOGOapplied())
						{
							apportionPrice = Double.valueOf(apportionPrice.doubleValue() / entryQuantity.doubleValue());

						}
						else
						{
							final int freeItem = entryData.getFreeCount().intValue();
							final int entryQuantitywithoutFree = (int) (entryQuantity.longValue() - freeItem);
							final Double priceforFreeItems = Double.valueOf(freeItem * 0.01);
							apportionPrice = Double.valueOf(apportionPrice.doubleValue() - priceforFreeItems.doubleValue());

							if (entryQuantitywithoutFree > 1)
							{
								apportionPrice = Double.valueOf(apportionPrice.doubleValue() / entryQuantitywithoutFree);
							}

							apportionPrice = Double.valueOf(Math.round(apportionPrice.doubleValue()));

						}
						pincodeServiceData.setPrice(apportionPrice);
					}
					else if (sellerData.getSpPrice() != null && StringUtils.isNotEmpty(sellerData.getSpPrice().getValue().toString()))
					{
						pincodeServiceData.setPrice(new Double(sellerData.getSpPrice().getValue().doubleValue()));
					}

					else if (sellerData.getMopPrice() != null
							&& StringUtils.isNotEmpty(sellerData.getMopPrice().getValue().toString()))
					{
						pincodeServiceData.setPrice(new Double(sellerData.getMopPrice().getValue().doubleValue()));
					}
					//TPR-774
					else if (sellerData.getMrpPrice() != null
							&& StringUtils.isNotEmpty(sellerData.getMrpPrice().getValue().toString()))
					{
						pincodeServiceData.setPrice(new Double(sellerData.getMrpPrice().getValue().doubleValue()));
					}
					else
					{
						pincodeServiceData.setPrice(Double.valueOf(entryData.getBasePrice().getValue().doubleValue()));
					}
					pincodeServiceData.setDeliveryModes(sellerData.getDeliveryModes());

					final List<Location> storeList = pincodeService.getSortedLocationsNearby(myLocation.getGPS(), configurableRadius,
							sellerData.getSellerID());

					LOG.debug("StoreList size is :" + storeList.size());

					if (storeList.size() > 0)
					{
						final List<String> locationList = new ArrayList<String>();
						for (final Location location : storeList)
						{
							locationList.add(location.getName());
						}
						pincodeServiceData.setStore(locationList);
					}
				}
				else
				{
					LOG.debug("getOMSPincodeResponseData : Seller information is null or empty for product selected");
				}

				if (StringUtils.isNotEmpty(cartData.getGuid()))
				{
					pincodeServiceData.setCartId(cartData.getGuid());
				}
				else
				{
					LOG.debug("getOMSPincodeResponseData : Cart GUID is null or empty for product selected");
				}

				pincodeServiceData.setIsDeliveryDateRequired(MarketplacecommerceservicesConstants.N);
				pincodeServiceReqDataList.add(pincodeServiceData);
			}
		}
		pinCodeResponseData = pinCodeFacade.getServiceablePinCodeCart(pincode, pincodeServiceReqDataList);

		return pinCodeResponseData;

	}

	@Override
	public List<PinCodeResponseData> getOMSPincodeResponseData(final String pincode, final CartData cartData,
			final OrderEntryData entryData) throws EtailNonBusinessExceptions
	{
		List<PinCodeResponseData> pinCodeResponseData = null;
		final List<PincodeServiceData> pincodeServiceReqDataList = new ArrayList<PincodeServiceData>();
		if (!entryData.isGiveAway())
		{
			final PincodeServiceData pincodeServiceData = new PincodeServiceData();
			pincodeServiceData.setProductCode(entryData.getProduct().getCode());
			final SellerInformationData sellerData = entryData.getSelectedSellerInformation();
			if (sellerData != null)
			{
				if (StringUtils.isNotEmpty(sellerData.getFullfillment()))
				{
					final String globalCodeFulfilmentType = MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(sellerData.getFullfillment()

					.toUpperCase());
					if (StringUtils.isNotEmpty(globalCodeFulfilmentType))
					{
						pincodeServiceData.setFullFillmentType(globalCodeFulfilmentType.toUpperCase());
					}
					else
					{
						LOG.debug("getOMSPincodeResponseData : GLOBALCONSTANTSMAP fulfilement type not found");
					}
				}
				else
				{
					LOG.debug("getOMSPincodeResponseData : Fullfillment is null or empty for product selected");
				}

				if (StringUtils.isNotEmpty(sellerData.getShippingMode()))
				{
					final String globalCodeShippingMode = MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(sellerData.getShippingMode()
							.toUpperCase());
					if (StringUtils.isNotEmpty(globalCodeShippingMode))
					{
						pincodeServiceData.setTransportMode(globalCodeShippingMode.toUpperCase());
					}
					else
					{
						LOG.debug("getOMSPincodeResponseData : GLOBALCONSTANTSMAP Transport mode not found");
					}
				}
				else
				{
					LOG.debug("getOMSPincodeResponseData : ShippingMode is null or empty for product selected");
				}

				if (StringUtils.isNotEmpty(sellerData.getSellerID()))
				{
					pincodeServiceData.setSellerId(sellerData.getSellerID());
				}
				else
				{
					LOG.debug("getOMSPincodeResponseData : Seller id is null or empty for product selected");
				}

				if (StringUtils.isNotEmpty(sellerData.getUssid()))
				{
					pincodeServiceData.setUssid(sellerData.getUssid());
				}
				else
				{
					LOG.debug("getOMSPincodeResponseData : Ussid is null or empty for product selected");
				}

				if (StringUtils.isNotEmpty(sellerData.getIsCod()))
				{
					pincodeServiceData.setIsCOD(sellerData.getIsCod().toUpperCase());
				}
				else
				{
					LOG.debug("getOMSPincodeResponseData : Seller COD is null or empty for product selected");
				}


				if (entryData.getAmountAfterAllDisc() != null && entryData.getAmountAfterAllDisc().getValue() != null
						&& entryData.getAmountAfterAllDisc().getValue().doubleValue() > 0.0)
				{
					pincodeServiceData.setPrice(Double.valueOf(entryData.getAmountAfterAllDisc().getValue().doubleValue()));
				}
				else if (sellerData.getSpPrice() != null && StringUtils.isNotEmpty(sellerData.getSpPrice().getValue().toString()))
				{
					pincodeServiceData.setPrice(new Double(sellerData.getSpPrice().getValue().doubleValue()));
				}
				else if (sellerData.getMopPrice() != null && StringUtils.isNotEmpty(sellerData.getMopPrice().getValue().toString()))
				{
					pincodeServiceData.setPrice(new Double(sellerData.getMopPrice().getValue().doubleValue()));
				}
				else if (sellerData.getMrpPrice() != null && StringUtils.isNotEmpty(sellerData.getMrpPrice().getValue().toString()))
				{
					pincodeServiceData.setPrice(new Double(sellerData.getMrpPrice().getValue().doubleValue()));
				}
				else
				{
					pincodeServiceData.setPrice(Double.valueOf(entryData.getBasePrice().getValue().doubleValue()));
				}
				pincodeServiceData.setDeliveryModes(sellerData.getDeliveryModes());
			}
			else
			{
				LOG.debug("getOMSPincodeResponseData : Seller information is null or empty for product selected");
			}

			if (StringUtils.isNotEmpty(cartData.getGuid()))
			{
				pincodeServiceData.setCartId(cartData.getGuid());
			}
			else
			{
				LOG.debug("getOMSPincodeResponseData : Cart GUID is null or empty for product selected");
			}

			pincodeServiceData.setIsDeliveryDateRequired(MarketplacecommerceservicesConstants.N);
			pincodeServiceReqDataList.add(pincodeServiceData);
		}
		pinCodeResponseData = pinCodeFacade.getServiceablePinCodeCart(pincode, pincodeServiceReqDataList);

		return pinCodeResponseData;

	}


	/**
	 * @DESC Discount specific to the promotions TIS-386
	 * @return Map<String, String>
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public Map<String, String> prepareForItemLevelDiscount() throws EtailNonBusinessExceptions
	{
		final Map<String, String> itemLevelDiscount = new HashMap<String, String>();
		final DecimalFormat format_2Places = new DecimalFormat("0.00");
		final CartModel cart = getCartService().getSessionCart();
		final List<AbstractOrderEntryModel> entries = cart.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				final double entryDiscount = (entry.getQuantity().doubleValue() * entry.getBasePrice().doubleValue())
						- entry.getTotalPrice().doubleValue();
				if (entryDiscount > 0)
				{
					itemLevelDiscount.put(entry.getEntryNumber().toString(),
							cart.getCurrency().getSymbol() + Double.valueOf(format_2Places.format(entryDiscount)) + " Off");
				}
			}
		}
		return itemLevelDiscount;
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
	 * @return GetWishListWsDTO
	 *
	 *
	 *
	 *
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public GetWishListWsDTO getTopTwoWishlistForUser(final UserModel userModel, final String pincode, final CartModel cartModel)
			throws EtailNonBusinessExceptions
	{
		if (userModel != null)
		{
			return mplCommerceCartService.getTopTwoWishlistForUser(userModel, pincode, cartModel);
		}
		else
		{
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B0006);
		}
	}

	/*
	 * @DESC TISST-6994,TISST-6990 adding to cart COD eligible or not with Pincode serviceabilty and sship product
	 *
	 *
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
			final List<PinCodeResponseData> pincodeResponseData, final CartModel cartModel) throws EtailNonBusinessExceptions
	{

		ServicesUtil.validateParameterNotNull(deliveryModeMap, "deliveryModeMap cannot be null");
		ServicesUtil.validateParameterNotNull(pincodeResponseData, "pincodeResponseData cannot be null");
		return mplCommerceCartService.addCartCodEligible(deliveryModeMap, pincodeResponseData, cartModel);
	}


	/*
	 * @Desc fetching Delivery Date
	 *
	 *
	 *
	 *
	 * @param cartData
	 *
	 *
	 *
	 *
	 * @param omsDeliveryResponse
	 *
	 *
	 *
	 *
	 * @return void
	 *
	 *
	 *
	 *
	 * @throws CMSItemNotFoundException
	 *
	 *
	 *
	 *
	 * @throws ParseException
	 */
	@Override
	public void setDeliveryDate(final CartData cartData, final List<PinCodeResponseData> omsDeliveryResponse)
			throws CMSItemNotFoundException, ParseException
	{
		mplCommerceCartService.setDeliveryDate(cartData, omsDeliveryResponse);
	}

	/*
	 * @Desc checking max added quantity with store configuration
	 *
	 *
	 *
	 *
	 * @param productCode
	 *
	 *
	 *
	 *
	 * @param qty
	 *
	 *
	 *
	 *
	 * @return String
	 *
	 *
	 *
	 *
	 * @throws CommerceCartModificationException
	 */
	@Override
	public String isMaxQuantityAlreadyAdded(final String productCode, final long qty, final long stock, final String ussid)
			throws CommerceCartModificationException
	{
		String addToCartFlag = "";
		if (productCode != null && qty > 0)
		{
			final CartData cartData = getSessionCartWithEntryOrdering(true);
			final int maximum_configured_quantiy = siteConfigService.getInt(

			MarketplacecommerceservicesConstants.MAXIMUM_CONFIGURED_QUANTIY, 0);

			if (cartData != null && cartData.getEntries() != null && !cartData.getEntries().isEmpty())
			{
				//TISPRO-590
				if (qty > stock)
				{
					addToCartFlag = MarketplacecommerceservicesConstants.OUT_OF_INVENTORY;
					LOG.debug("You are about to exceede the max inventory when the cart is not empty");
				}

				for (final OrderEntryData entry : cartData.getEntries())
				{
					final ProductData productData = entry.getProduct();
					if (productCode.equals(productData.getCode()) && ussid.equals(entry.getSelectedUssid()))
					{
						LOG.debug("Product already present in the cart so now we will check the qunatity present in the cart already");
						if (entry.getQuantity().longValue() >= stock)
						{
							addToCartFlag = MarketplacecommerceservicesConstants.OUT_OF_INVENTORY;
							LOG.debug("You are about to exceede the max inventory");
							break;
						}
						if (qty + entry.getQuantity().longValue() > stock)
						{
							addToCartFlag = MarketplacecommerceservicesConstants.INVENTORY_WIIL_EXCEDE;
							LOG.debug("You are about to exceede the max inventory");
							break;
						}
						if (entry.getQuantity().longValue() >= maximum_configured_quantiy)
						{
							addToCartFlag = MarketplacecommerceservicesConstants.CROSSED_MAX_LIMIT;
							LOG.debug("You are about to exceede the max quantity");
							break;
						}
						if (qty + entry.getQuantity().longValue() > maximum_configured_quantiy)
						{
							addToCartFlag = MarketplacecommerceservicesConstants.REACHED_MAX_LIMIT;
							LOG.debug("You are about to exceede the max quantity");
							break;
						}
						break;
					}
				}
			}
			else
			{
				if (qty > stock)
				{
					addToCartFlag = MarketplacecommerceservicesConstants.OUT_OF_INVENTORY;
					LOG.debug("You are about to exceede the max inventory");
				}
				if (qty > maximum_configured_quantiy)
				{
					addToCartFlag = MarketplacecommerceservicesConstants.REACHED_MAX_LIMIT;
					LOG.debug("You are about to exceede the max quantity");
				}

			}
			return addToCartFlag;
		}
		else
		{
			addToCartFlag = MarketplacecommerceservicesConstants.ERROR_MSG_TYPE;
		}
		return addToCartFlag;
	}

	/**
	 * Update cart quantity by quantity and entry number
	 *
	 * @return CartModificationData
	 * @param entryNumber
	 * @param quantity
	 *
	 * @throws CommerceCartModificationException
	 */
	@Override
	public CartModificationData updateCartEntry(final long entryNumber, final long quantity)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setEntryNumber(entryNumber);
		parameter.setQuantity(quantity);

		final CommerceCartModification modification = getMplCommerceCartService().updateQuantityForCartEntry(parameter);

		return getCartModificationConverter().convert(modification);
	}

	/**
	 * Update cart quantity for entry id and store
	 *
	 * @return CartModificationData
	 * @param entryNumber
	 * @param storeId
	 *
	 * @throws CommerceCartModificationException
	 */
	@Override
	public CartModificationData updateCartEntry(final long entryNumber, final String storeId)
			throws CommerceCartModificationException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		final PointOfServiceModel pointOfServiceModel = StringUtil.isEmpty(storeId) ? null : getPointOfServiceService()
				.getPointOfServiceForName(storeId);
		if (pointOfServiceModel == null)
		{
			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			parameter.setEntryNumber(entryNumber);
			return getCartModificationConverter().convert(getMplCommerceCartService().updateToShippingModeForCartEntry(parameter));
		}
		else
		{

			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			parameter.setEntryNumber(entryNumber);
			parameter.setPointOfService(pointOfServiceModel);

			return getCartModificationConverter().convert(getCommerceCartService().updatePointOfServiceForCartEntry(parameter));
		}
	}

	/**
	 * Update cart quantity for entry id and store
	 *
	 * @return CartModificationData
	 * @param entryNumber
	 * @param storeId
	 * @param cartModel
	 *
	 * @throws CommerceCartModificationException
	 */
	@Override
	public CartModificationData updateCartEntryMobile(final long entryNumber, final String storeId, final CartModel cartModel)
			throws CommerceCartModificationException
	{
		// Changes for Duplicate Cart fix
		final PointOfServiceModel pointOfServiceModel = StringUtil.isEmpty(storeId) ? null : getPointOfServiceService()
				.getPointOfServiceForName(storeId);
		if (pointOfServiceModel == null)
		{
			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			parameter.setEntryNumber(entryNumber);
			return getCartModificationConverter().convert(getMplCommerceCartService().updateToShippingModeForCartEntry(parameter));
		}
		else
		{
			final CommerceCartParameter parameter = new CommerceCartParameter();
			parameter.setEnableHooks(true);
			parameter.setCart(cartModel);
			parameter.setEntryNumber(entryNumber);
			parameter.setPointOfService(pointOfServiceModel);

			return getCartModificationConverter().convert(getCommerceCartService().updatePointOfServiceForCartEntry(parameter));
		}
	}


	/*
	 * @Desc used for inventory soft reservation from Commerce Checkout and Payment
	 *
	 * @param requestType
	 *
	 * @param abstractOrderModel
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean isInventoryReserved(final String requestType, AbstractOrderModel abstractOrderModel) //Parameter AbstractOrderModel added extra for TPR-629
			throws EtailNonBusinessExceptions
	{
		if (null == abstractOrderModel)
		{
			abstractOrderModel = cartService.getSessionCart();
		}
		//final AbstractOrderModel abstractOrderModel = cartService.getSessionCart();
		final String defaultPinCodeId = sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);
		return mplCommerceCartService.isInventoryReserved(abstractOrderModel, requestType, defaultPinCodeId);

	}

	/*
	 * @Desc used for inventory soft reservation from Mobile Checkout and Payment ---TPR-629
	 *
	 * @param requestType
	 *
	 * @param abstractOrderModel
	 *
	 * @param defaultPinCodeId
	 *
	 * @return boolean
	 *
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean isInventoryReservedMobile(final String requestType, final AbstractOrderModel abstractOrderModel,
			final String defaultPinCodeId) throws EtailNonBusinessExceptions
	{
		return mplCommerceCartService.isInventoryReserved(abstractOrderModel, requestType, defaultPinCodeId);
	}

	/**
	 * @return the productService
	 */
	@Override
	public ProductService getProductService()
	{
		return productService;
	}

	/**
	 * @param productService
	 *           the productService to set
	 */
	@Override
	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
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
	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
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
	@Required
	public void setMplCommerceCartService(final MplCommerceCartService mplCommerceCartService)
	{
		this.mplCommerceCartService = mplCommerceCartService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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

	/*
	 * (non-Javadoc)
	 *
	 *
	 *
	 *
	 * @see com.tisl.mpl.facade.checkout.MplCartFacade#removeDeliveryandPaymentMode(de.hybris.platform.core.model.order.
	 * CartModel )
	 */
	@Override
	public CartModel removeDeliveryMode(final CartModel cart)
	{
		boolean reCalculationRequired = false;
		boolean deliverypointOfService = false;
		try
		{
			final List<AbstractOrderEntryModel> entryList = cart.getEntries();
			if (CollectionUtils.isNotEmpty(entryList))
			{
				for (final AbstractOrderEntryModel entry : entryList)
				{
					if (entry.getMplDeliveryMode() != null)
					{
						entry.setMplDeliveryMode(null);
						//modelService.save(entry); TISPT-104
						reCalculationRequired = true;
					}
					if (entry.getDeliveryPointOfService() != null)
					{
						entry.setDeliveryPointOfService(null);
						//modelService.save(entry); TISPT-104
						deliverypointOfService = true;
					}
				}
			}
			if (reCalculationRequired || deliverypointOfService) //TISPT-104
			{
				modelService.saveAll(entryList);
			}

			//TISPT-104
			//call recalculate on cart, only if there's a change in deliverymode.
			//			if (hasDeliveryMode)
			//			{
			commerceCartService.recalculateCart(cart); //TISPT-104
			modelService.save(cart);
			//			}
		}
		catch (final Exception e)
		{
			LOG.info("Some issue may be happend while removing Dellivery mode from Cart to remove Delivery Specific promotion", e);
		}

		return cart;
	}

	@Override
	public boolean removeDeliveryMode2(final AbstractOrderModel cart)
	{
		boolean hasDeliveryMode = false;
		try
		{
			for (final AbstractOrderEntryModel entry : cart.getEntries())
			{
				if (entry.getMplDeliveryMode() != null)
				{
					entry.setMplDeliveryMode(null);
					modelService.save(entry);
					hasDeliveryMode = true;
				}
			}

			if (hasDeliveryMode)
			{
				modelService.save(cart);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error while removing delivery mode ", e);
		}

		return hasDeliveryMode;
	}

	/*
	 * @Desc used for In case pincode is changed in delivery address selection page then this method will be called to
	 * check pincode serviceabilty //TISUTO-106
	 *
	 *
	 *
	 *
	 * @param selectedPincode
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
	public String checkPincodeAndInventory(final String selectedPincode) throws EtailNonBusinessExceptions,
			CMSItemNotFoundException
	{
		final String sessionPincode = sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);

		// if different pin code from cart page is selected then only all checks will be invoked
		if (selectedPincode != null && sessionPincode != null && !selectedPincode.equalsIgnoreCase(sessionPincode))
		{
			String isServicable = MarketplacecclientservicesConstants.Y;
			List<PinCodeResponseData> responseDataList = null;
			final CartData cartData = getSessionCartWithEntryOrdering(true);
			final CartModel cartModel = cartService.getSessionCart();

			if ((cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
			{
				responseDataList = getOMSPincodeResponseData(selectedPincode, cartData);

				if (null != responseDataList && responseDataList.size() > 0 && cartModel != null && cartModel.getEntries() != null
						&& cartModel.getEntries().size() > 0)
				{
					for (final PinCodeResponseData pinCodeEntry : responseDataList)
					{
						for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
						{
							// Checking if for any ussid delivery modes are present or not
							if (cartEntry != null && cartEntry.getSelectedUSSID() != null && cartEntry.getMplDeliveryMode() != null
									&& cartEntry.getMplDeliveryMode().getDeliveryMode() != null
									&& cartEntry.getMplDeliveryMode().getDeliveryMode().getCode() != null && pinCodeEntry != null
									&& cartEntry.getSelectedUSSID().equalsIgnoreCase(pinCodeEntry.getUssid())
									&& pinCodeEntry.getIsServicable().equalsIgnoreCase(MarketplacecclientservicesConstants.N)
									&& isServicable.equalsIgnoreCase(MarketplacecclientservicesConstants.Y))
							{
								isServicable = MarketplacecclientservicesConstants.N;
								break;
							}

							if (cartEntry != null && cartEntry.getSelectedUSSID() != null && cartEntry.getMplDeliveryMode() != null
									&& cartEntry.getMplDeliveryMode().getDeliveryMode() != null
									&& cartEntry.getMplDeliveryMode().getDeliveryMode().getCode() != null && pinCodeEntry != null
									&& pinCodeEntry.getValidDeliveryModes() != null
									&& cartEntry.getSelectedUSSID().equalsIgnoreCase(pinCodeEntry.getUssid())
									&& isServicable.equalsIgnoreCase(MarketplacecclientservicesConstants.Y))
							{
								final String selectedDeliveryMode = cartEntry.getMplDeliveryMode().getDeliveryMode().getCode();
								boolean deliveryModeAvaiableInResponse = false;

								// Checking if selected delivery mode is present in response
								for (final DeliveryDetailsData deliveryDetailsData : pinCodeEntry.getValidDeliveryModes())
								{
									if (((selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) && deliveryDetailsData
											.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.HD)) || (selectedDeliveryMode
											.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) && deliveryDetailsData

									.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.ED)))
											|| ((selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT) && deliveryDetailsData
													.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))))
									{
										deliveryModeAvaiableInResponse = true;
									}
								}

								if (deliveryModeAvaiableInResponse)
								{
									for (final DeliveryDetailsData deliveryDetailsData : pinCodeEntry.getValidDeliveryModes())
									{
										// Checking for selected delivery mode inventory is available

										if (((selectedDeliveryMode.equalsIgnoreCase(MarketplacecommerceservicesConstants.HOME_DELIVERY) && deliveryDetailsData
												.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.HD))

												|| (selectedDeliveryMode
														.equalsIgnoreCase(MarketplacecommerceservicesConstants.EXPRESS_DELIVERY) && deliveryDetailsData

												.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.ED)) || ((selectedDeliveryMode
												.equalsIgnoreCase(MarketplacecommerceservicesConstants.CLICK_COLLECT) && deliveryDetailsData
												.getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.CnC))))
												&& StringUtils.isNotEmpty(deliveryDetailsData.getInventory())
												&& cartEntry.getQuantity().longValue() > Long.parseLong(deliveryDetailsData.getInventory()))
										{
											isServicable = MarketplacecclientservicesConstants.N;
											break;
										}
									}
								}
								else
								{
									isServicable = MarketplacecclientservicesConstants.N;
									break;
								}
							}
						}
					}

				}
				else
				{
					isServicable = MarketplacecclientservicesConstants.N;
				}
				if (null != responseDataList)
				{
					Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
					deliveryModeDataMap = getDeliveryMode(cartData, responseDataList, cartModel);
					final boolean isCOdEligible = addCartCodEligible(deliveryModeDataMap, responseDataList, cartModel);
					LOG.info("isCOdEligible " + isCOdEligible);
				}


			}
			if (sessionService.getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE) != null)
			{
				sessionService.setAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE, selectedPincode);
			}

			if (isServicable.equalsIgnoreCase(MarketplacecclientservicesConstants.Y))
			{

				final boolean inventoryReservationStatus = isInventoryReserved(
						MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART, null);
				if (!inventoryReservationStatus)
				{
					sessionService.setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID,
							MarketplacecommerceservicesConstants.TRUE_UPPER);
					return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
				}
			}
			else
			{
				sessionService.setAttribute(MarketplacecclientservicesConstants.OMS_PINCODE_SERVICEABILTY_MSG_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
				return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
			}
		}
		return "";

	}

	/**
	 * @Desc : To validate if any of the product is delisted or not TISEE-3676 Not handling freebie promotions
	 * @param cartModel
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public boolean isCartEntryDelisted(final CartModel cartModel) throws CommerceCartModificationException,
			EtailNonBusinessExceptions
	{
		boolean delistedStatus = false;
		//TISEE-5143
		//final CatalogVersionModel onlineCatalog = catalogService.getCatalogVersion(
		//	MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
		//MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);

		try
		{
			final CatalogVersionModel onlineCatalog = getSessionService().getAttribute("currentCatalogVersion");

			if (cartModel != null)
			{
				for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
				{

					if (cartEntryModel != null && cartEntryModel.getProduct() == null)
					{
						final CartModificationData cartModification = updateCartEntry(cartEntryModel.getEntryNumber().longValue(), 0);

						if (cartModification.getQuantity() == 0)
						{
							LOG.debug(">> Removed product for cart dut to product unavailablity");
						}
						delistedStatus = true;
					}
					else if (cartEntryModel != null && StringUtils.isNotEmpty(cartEntryModel.getSelectedUSSID())
							&& !cartEntryModel.getGiveAway().booleanValue())
					{
						final Date sysDate = new Date();
						final String ussid = cartEntryModel.getSelectedUSSID();
						//TISEE-5143
						final List<SellerInformationModel> sellerInformationModelList = getMplDelistingService().getModelforUSSID(
								ussid, onlineCatalog);
						if (CollectionUtils.isNotEmpty(sellerInformationModelList)
								&& sellerInformationModelList.get(0) != null
								&& ((sellerInformationModelList.get(0).getSellerAssociationStatus() != null && sellerInformationModelList
										.get(0).getSellerAssociationStatus().getCode()
										.equalsIgnoreCase(MarketplacecommerceservicesConstants.NO)) || (sellerInformationModelList.get(0)

								.getEndDate() != null && sysDate.after(sellerInformationModelList.get(0).getEndDate()))))
						{
							LOG.debug(">> Removing Cart entry for delisted ussid for " + cartEntryModel.getSelectedUSSID());
							final CartModificationData cartModification = updateCartEntry(cartEntryModel.getEntryNumber().longValue(), 0);

							if (cartModification.getQuantity() == 0)
							{
								LOG.debug(">> Delisted Item has been Removed From the cart for " + ussid);
							}
							else
							{

								LOG.debug(">> Delisted item Could not removed item from cart for " + ussid
										+ " trying for hard reset ...... ");
								getModelService().remove(cartEntryModel);
								getModelService().refresh(cartModel);
							}
							delistedStatus = true;
						}
					}
				}
			}
			if (delistedStatus)
			{
				getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
						MarketplacecommerceservicesConstants.TRUE_UPPER);
			}
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		return delistedStatus;
	}

	/**
	 * @Desc : To validate if any of the product is delisted or not TISEE-3676 Not handling freebie promotions
	 * @param cartModel
	 * @return boolean
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	@SuppressWarnings("deprecation")
	public boolean isCartEntryDelistedMobile(final CartModel cartModel) throws CommerceCartModificationException,
			EtailNonBusinessExceptions
	{
		boolean delistedStatus = false;
		final List<AbstractOrderEntryModel> cartEntryModelList = new ArrayList<AbstractOrderEntryModel>();

		//TISEE-5143
		final CatalogVersionModel onlineCatalog = catalogService.getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);


		if (cartModel != null)
		{
			for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
			{

				if (cartEntryModel != null && cartEntryModel.getProduct() == null)
				{
					final CartModificationData cartModification = updateCartEntryMobile(cartEntryModel.getEntryNumber().longValue(),
							0, cartModel);

					if (cartModification.getQuantity() == 0)
					{
						LOG.debug(">> Removed product for cart dut to product unavailablity");
					}
					delistedStatus = true;
				}
				else if (cartEntryModel != null && StringUtils.isNotEmpty(cartEntryModel.getSelectedUSSID())
						&& !cartEntryModel.getGiveAway().booleanValue())
				{
					final Date sysDate = new Date();
					final String ussid = cartEntryModel.getSelectedUSSID();
					//TISEE-5143
					final List<SellerInformationModel> sellerInformationModelList = getMplDelistingService().getModelforUSSID(ussid,
							onlineCatalog);
					if (CollectionUtils.isNotEmpty(sellerInformationModelList)
							&& sellerInformationModelList.get(0) != null
							&& ((sellerInformationModelList.get(0).getSellerAssociationStatus() != null && sellerInformationModelList
									.get(0).getSellerAssociationStatus().getCode()
									.equalsIgnoreCase(MarketplacecommerceservicesConstants.NO)) || (sellerInformationModelList.get(0)

							.getEndDate() != null && sysDate.after(sellerInformationModelList.get(0).getEndDate()))))
					{
						LOG.debug(">> Removing Cart entry for delisted ussid for " + cartEntryModel.getSelectedUSSID());
						final CartModificationData cartModification = updateCartEntryMobile(
								cartEntryModel.getEntryNumber().longValue(), 0, cartModel);



						if (cartModification.getQuantity() == 0)
						{
							LOG.debug(">> Delisted Item has been Removed From the cart for " + ussid);
						}
						else
						{


							LOG.debug(">> Delisted item Could not removed item from cart for " + ussid
									+ " trying for hard reset ...... ");

						}
						cartEntryModelList.add(cartEntryModel);
						delistedStatus = true;
						//removed saving model in loop
					}
				}
			}
			//saving model list
			if (cartEntryModelList.size() > 0)
			{
				/*
				 * //Fix for Freebie issue with duplicate Product in cart //Converting ArrayList to HashSet to remove
				 * duplicates final HashSet<AbstractOrderEntryModel> cartEntryModellistToSet = new
				 * HashSet<AbstractOrderEntryModel>( cartEntryModelList); //Creating Arraylist without duplicate values
				 * final List<AbstractOrderEntryModel> cartEntryModellistWithoutDuplicates = new
				 * ArrayList<AbstractOrderEntryModel>( cartEntryModellistToSet);
				 */
				modelService.remove(cartEntryModelList);
				modelService.refresh(cartEntryModelList);
			}
		}
		if (delistedStatus)
		{
			getSessionService().setAttribute(MarketplacecommerceservicesConstants.CART_DELISTED_SESSION_ID,
					MarketplacecommerceservicesConstants.TRUE_UPPER);
		}
		return delistedStatus;
	}

	/*
	 * @Desc fetching delivery mode
	 *
	 *
	 *
	 *
	 * @param cartData
	 *
	 *
	 *
	 *
	 * @param omsDeliveryResponse
	 *
	 *
	 *
	 *
	 * @return Map<String, List<MarketplaceDeliveryModeData>>
	 *
	 *
	 *
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Override
	public Map<String, List<MarketplaceDeliveryModeData>> getDeliveryMode(final CartData cartData,
			final OrderEntryData cartEntryData, final List<PinCodeResponseData> omsDeliveryResponse) throws CMSItemNotFoundException
	{
		final CartModel cartModel = getCartService().getSessionCart();
		return mplCommerceCartService.getDeliveryMode(cartData, cartEntryData, omsDeliveryResponse, cartModel);
	}

	/*
	 * @Desc used to display quantity in cart page
	 *
	 *
	 *
	 *
	 * @return ArrayList<Integer>
	 */
	@Override
	public ArrayList<Integer> getQuantityConfiguratioList() throws EtailNonBusinessExceptions
	{
		final int minQuantity = getSiteConfigService().getInt(MarketplacecommerceservicesConstants.MINIMUM_CONFIGURED_QUANTIY, 0);
		final int maxQuantity = getSiteConfigService().getInt(MarketplacecommerceservicesConstants.MAXIMUM_CONFIGURED_QUANTIY, 0);

		final ArrayList<Integer> quantityConfigurationList = new ArrayList<Integer>();

		for (int count = minQuantity; count <= maxQuantity; count++)
		{
			quantityConfigurationList.add(Integer.valueOf(count));
		}

		return quantityConfigurationList;
	}

	//added for jewellery

	@Override
	public ArrayList<Integer> getQuantityConfiguratioListforJewellery() throws EtailNonBusinessExceptions
	{
		final int minJwlQuantity = getSiteConfigService().getInt(
				MarketplacecommerceservicesConstants.MINIMUM_CONFIGURED_QUANTIY_JEWELLERY, 0);
		final int maxJwlQuantity = getSiteConfigService().getInt(
				MarketplacecommerceservicesConstants.MAXIMUM_CONFIGURED_QUANTIY_JEWELLERY, 0);

		final ArrayList<Integer> quantityConfigurationJwlList = new ArrayList<Integer>();

		for (int count = minJwlQuantity; count <= maxJwlQuantity; count++)
		{
			quantityConfigurationJwlList.add(Integer.valueOf(count));
		}

		return quantityConfigurationJwlList;
	}


	/**
	 * Update cart quantity by quantity and entry number
	 *
	 * @return CartModificationData
	 * @param entryNumber
	 * @param quantity
	 *
	 * @throws CommerceCartModificationException
	 */
	@Override
	public CartModificationData updateCartEntryMobile(final long entryNumber, final long quantity, final CartModel cartModel)
			throws CommerceCartModificationException
	{
		//final CartModel cartModel = getCartService().getSessionCart();
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setEntryNumber(entryNumber);
		parameter.setQuantity(quantity);
		final CommerceCartModification modification = getMplCommerceCartService().updateQuantityForCartEntry(parameter);

		return getCartModificationConverter().convert(modification);
	}

	/**
	 * @return the commerceCartService
	 */
	@Override
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	/**
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	@Override
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
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

	/**
	 * @return the userService
	 */
	@Override
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Override
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the pinCodeFacade
	 */
	public PinCodeServiceAvilabilityFacade getPinCodeFacade()
	{
		return pinCodeFacade;
	}

	/**
	 * @param pinCodeFacade
	 *           the pinCodeFacade to set
	 */
	public void setPinCodeFacade(final PinCodeServiceAvilabilityFacade pinCodeFacade)
	{
		this.pinCodeFacade = pinCodeFacade;
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
	 * @return the mplDelistingService
	 */
	public MplDelistingService getMplDelistingService()
	{
		return mplDelistingService;
	}

	/**
	 * @param mplDelistingService
	 *           the mplDelistingService to set
	 */
	public void setMplDelistingService(final MplDelistingService mplDelistingService)
	{
		this.mplDelistingService = mplDelistingService;
	}

	/*
	 * @Desc checking wishlist entry is valid or not , delisted , end date , online from TISEE-5185
	 *
	 *
	 *
	 *
	 * @param wishlistEntryModel
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
	public boolean isWishlistEntryValid(final Wishlist2EntryModel wishlistEntryModel) throws EtailNonBusinessExceptions
	{
		boolean isWishlistEntryValid = false;
		if (wishlistEntryModel != null)
		{
			isWishlistEntryValid = getMplCommerceCartService().isWishlistEntryValid(wishlistEntryModel);
		}
		return isWishlistEntryValid;
	}

	/**
	 * The Method used to validate serviceability check for Gift Yourself Section in Cart
	 *
	 * @param defaultPinCodeId
	 * @param entryModels
	 * @return Map<String, List<String>>
	 */
	@Override
	public Map<String, List<String>> checkPincodeGiftCartData(final String defaultPinCodeId,
			final List<Wishlist2EntryModel> entryModels, final Tuple2<?, ?> wishListPincodeObject) //TISPT-179 Point 3
	{
		Map<String, List<String>> giftYourselfDeliveryModeDataMap = new HashMap<String, List<String>>();
		if (StringUtil.isNotEmpty(defaultPinCodeId))
		{
			giftYourselfDeliveryModeDataMap = getMplCommerceCartService().checkPincodeGiftCartData(defaultPinCodeId, entryModels,
					wishListPincodeObject); //TISPT-179 Point 3
			if (MapUtils.isNotEmpty(giftYourselfDeliveryModeDataMap))
			{
				LOG.debug("Servicable Data Obtained");
			}
			else
			{
				giftYourselfDeliveryModeDataMap = populateGiftData(entryModels);
			}
		}

		return giftYourselfDeliveryModeDataMap;
	}

	/**
	 * Populate Gift Cart Data
	 *
	 * @param entryModels
	 * @return Map<String, List<String>>
	 */
	private Map<String, List<String>> populateGiftData(final List<Wishlist2EntryModel> entryModels)
	{
		Map<String, List<String>> giftYourselfDeliveryModeDataMap = new HashMap<String, List<String>>();
		for (final Wishlist2EntryModel wishModel : entryModels)
		{
			if (wishModel.getUssid() != null)
			{
				final SellerInformationModel sellerInfoForWishlist = getMplCommerceCartService().getSellerDetailsData(
						wishModel.getUssid());
				if (null != sellerInfoForWishlist)
				{
					giftYourselfDeliveryModeDataMap = populategiftYourselfData(wishModel, sellerInfoForWishlist);
				}
			}
		}
		return giftYourselfDeliveryModeDataMap;
	}

	/**
	 * @param wishModel
	 * @param sellerInfoForWishlist
	 * @return giftYourselfDeliveryModeDataMap
	 */
	private Map<String, List<String>> populategiftYourselfData(final Wishlist2EntryModel wishModel,
			final SellerInformationModel sellerInfoForWishlist)
	{
		final Map<String, List<String>> giftYourselfDeliveryModeDataMap = new HashMap<String, List<String>>();
		final List<RichAttributeModel> richAttributeModel = (List<RichAttributeModel>) sellerInfoForWishlist.getRichAttribute();

		final List<String> deliveryModeDataList = new ArrayList<String>();

		if (null != richAttributeModel && null != richAttributeModel.get(0) && wishModel.getProduct() != null
				&& wishModel.getProduct().getCode() != null)
		{
			if (richAttributeModel.get(0).getHomeDelivery() != null)
			{
				final String homeDelivery = richAttributeModel.get(0).getHomeDelivery().getCode();
				if (null != homeDelivery && homeDelivery.equalsIgnoreCase("YES"))
				{
					deliveryModeDataList.add(MarketplaceFacadesConstants.CART_HOME_DELIVERY);
				}
			}
			else if (richAttributeModel.get(0).getExpressDelivery() != null)
			{
				final String expressDelivery = richAttributeModel.get(0).getExpressDelivery().getCode();
				if (null != expressDelivery && expressDelivery.equalsIgnoreCase("YES"))
				{
					deliveryModeDataList.add(MarketplaceFacadesConstants.CART_EXPRESS_DELIVERY);
				}
			}
			else if (richAttributeModel.get(0).getClickAndCollect() != null)
			{
				final String clickAndCollect = richAttributeModel.get(0).getClickAndCollect().getCode();
				if (null != clickAndCollect && clickAndCollect.equals("YES"))
				{ //Click And Collect is out of scope for release-I
					deliveryModeDataList.add(MarketplaceFacadesConstants.CART_CLICK_COLLECT);
					deliveryModeDataList.remove(MarketplaceFacadesConstants.CART_CLICK_COLLECT);
				}
			}

			giftYourselfDeliveryModeDataMap.put(wishModel.getProduct().getCode(), deliveryModeDataList);
		}

		return giftYourselfDeliveryModeDataMap;
	}

	@Override
	public CartModel getCalculatedCart() throws CommerceCartModificationException, EtailNonBusinessExceptions
	{
		CartModel cart = getCartService().getSessionCart();
		//setChannelForCart(cart);
		cart = setChannelAndExpressCheckout(cart);
		isCartEntryDelisted(cart);
		removeDeliveryMode(cart); // Cart recalculation method invoked inside this method
		//final boolean hasSubtotalChanged = setCartSubTotal(cart);
		//TODO Add release voucher code
		//Do calculate the cart, if any of the following conditions met. Else return the cart as it is.
		//		if (isDelisted || isDeliveryModeRemoved)
		//		{
		//			cart.setCalculated(Boolean.FALSE);
		//			setCartEntriesCalculation(cart);
		//			final CommerceCartParameter parameter = new CommerceCartParameter();
		//			parameter.setCart(cart);
		//			getMplDefaultCommerceCartCalculationStrategy().calculateCart(parameter);
		//			getCartService().setSessionCart(cart);
		//		}
		//		return cart;

		return cart; //TISPT-104

	}

	/*
	 * private void setCartEntriesCalculation(final CartModel oldCart) { for (final AbstractOrderEntryModel entry :
	 * oldCart.getEntries()) { entry.setCalculated(Boolean.FALSE); } }
	 */



	//TISPT-104
	private CartModel setChannelAndExpressCheckout(final CartModel cartModel)
	{
		cartModel.setIsExpressCheckoutSelected(Boolean.FALSE);
		if (cartModel.getDeliveryAddress() != null)
		{
			cartModel.setDeliveryAddress(null);
		}
		if (!cartModel.getChannel().equals(SalesApplication.WEB))
		{
			cartModel.setChannel(SalesApplication.WEB);
		}

		modelService.save(cartModel);

		return cartModel;
	}

	/**
	 * TPR-774
	 *
	 * @doc To calculate discount percentage amount for display purpose
	 * @param cartModel
	 */
	@Override
	public void totalMrpCal(final CartModel cartModel) throws EtailNonBusinessExceptions
	{
		try

		{
			if (null != cartModel && CollectionUtils.isNotEmpty(cartModel.getEntries()))
			{
				for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
				{
					// For Not a freebie product Percentage calculation
					if (null != cartEntryModel && !cartEntryModel.getGiveAway().booleanValue())
					{
						double prodDisCal = 0.0;
						double prodDisCalPer = 0.0;
						//final double percentVal = 0.0;
						BigDecimal totMrp = BigDecimal.valueOf(0);

						// Total MRP
						totMrp = BigDecimal.valueOf((null != cartEntryModel.getMrp() ? Double.valueOf((cartEntryModel.getMrp()
								.doubleValue() * cartEntryModel.getQuantity().doubleValue())) : totMrp).doubleValue());

						cartEntryModel.setTotalMrp(Double.valueOf(totMrp.doubleValue()));

						if (null != cartEntryModel.getNetSellingPrice()
								&& cartEntryModel.getNetSellingPrice().doubleValue() > 0.0
								&& cartEntryModel.getNetSellingPrice().doubleValue() != totMrp.doubleValue()
								&& (cartEntryModel.getNetSellingPrice().doubleValue() < cartEntryModel.getBasePrice().doubleValue()
										* cartEntryModel.getQuantity().doubleValue()))
						{
							prodDisCal = totMrp.doubleValue() - (cartEntryModel.getNetSellingPrice().doubleValue());
							prodDisCalPer = Math.round((prodDisCal / totMrp.doubleValue()) * 100);
							//percentVal = Math.round((prodDisCalPer * 100.0));
							cartEntryModel.setProductPerDiscDisplay(Double.valueOf(prodDisCalPer));
						}
						else if (null != cartEntryModel.getMrp())
						{
							prodDisCal = cartEntryModel.getMrp().doubleValue() - cartEntryModel.getBasePrice().doubleValue();
							prodDisCalPer = Math.round((prodDisCal / cartEntryModel.getMrp().doubleValue()) * 100);
							//percentVal = Math.round((prodDisCalPer * 100.0));
							cartEntryModel.setProductPerDiscDisplay(Double.valueOf(prodDisCalPer));
						}
					}
				}
				modelService.saveAll(cartModel.getEntries());
			}
		}
		catch (final Exception e)
		{
			LOG.debug("Error while calculating percentage discount for display");
			throw e;
		}

	}

	/*
	 * private void setChannelForCart(final CartModel model) { if (!model.getChannel().equals(SalesApplication.WEB)) {
	 * model.setChannel(SalesApplication.WEB); getModelService().save(model); } }
	 */
	/**
	 * this method calls service to get inventories for stores.
	 *
	 *
	 * @param storeLocationRequestDataList
	 * @return returns Stores with inventories.
	 */
	@Override
	public List<StoreLocationResponseData> getStoreLocationsforCnC(
			final List<StoreLocationRequestData> storeLocationRequestDataList, final CartModel cartModel)
	{

		LOG.debug("from getStoreLocationforCnC");
		return mplCommerceCartService.getStoreLocationsforCnC(storeLocationRequestDataList, cartModel);
	}

	@Override
	public OrderEntryData getCartEntryByUssid(final String ussid, final CartData cart)


	{
		OrderEntryData requiredCartEntry = null;

		if (cart.getEntries() != null)
		{
			for (final OrderEntryData cartEntry : cart.getEntries())
			{
				if (cartEntry.getSelectedUssid().equalsIgnoreCase(ussid))
				{
					requiredCartEntry = cartEntry;
					break;
				}
			}
		}
		return requiredCartEntry;


	}




	/**
	 * This method recalculates order
	 *
	 * @param orderModel
	 */
	@Override
	public void recalculateOrder(final OrderModel orderModel)
	{
		mplCommerceCartService.recalculateOrder(orderModel);
	}


	/**
	 * @This Method is used to get Valid Delivery Modes by Inventory
	 * @param pinCodeResponseData
	 * @throws EtailNonBusinessExceptions
	 * @return PinCodeResponseData
	 */
	@Override
	public PinCodeResponseData getVlaidDeliveryModesByInventory(final PinCodeResponseData pinCodeResponseData)
			throws EtailNonBusinessExceptions
	{
		return mplCommerceCartService.getVlaidDeliveryModesByInventory(pinCodeResponseData);
	}


	/**
	 * @return the mplExtendedPromoCartConverter
	 */
	public Converter<CartModel, CartData> getMplExtendedPromoCartConverter()
	{
		return mplExtendedPromoCartConverter;
	}


	/**
	 * @param mplExtendedPromoCartConverter
	 *           the mplExtendedPromoCartConverter to set
	 */
	public void setMplExtendedPromoCartConverter(final Converter<CartModel, CartData> mplExtendedPromoCartConverter)
	{
		this.mplExtendedPromoCartConverter = mplExtendedPromoCartConverter;
	}

	/* TPR-970 changes ,populate city and stae details in cart */
	@Override
	public void populatePinCodeData(final CartModel cartModel, final String pincode)
	{
		final PincodeModel pinCodeModelObj = pincodeServiceFacade.getLatAndLongForPincode(pincode);
		if (null != pinCodeModelObj)
		{
			cartModel.setStateForPincode(pinCodeModelObj.getState() == null ? "" : pinCodeModelObj.getState().getCountrykey());
			cartModel.setCityForPincode(pinCodeModelObj.getCity() == null ? "" : pinCodeModelObj.getCity().getCityName());
			cartModel.setPincodeNumber(pincode);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.facade.checkout.MplCartFacade#notifyEmailAndSmsOnInventoryFail(de.hybris.platform.core.model.order
	 * .OrderModel)
	 */
	@Override
	public boolean notifyEmailAndSmsOnInventoryFail(final OrderModel orderModel) throws EtailNonBusinessExceptions
	{
		boolean flag = false;
		//Email and sms for Payment_Timeout
		final String trackOrderUrl = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_ORDER_TRACK_URL)
				+ orderModel.getCode();
		try
		{
			notificationService.triggerEmailAndSmsOnPaymentTimeout(orderModel, trackOrderUrl);
			flag = true;
		}
		catch (final JAXBException e)
		{
			LOG.error("Error while sending notifications>>>>>>", e);
		}
		catch (final Exception ex)
		{
			LOG.error("Error while sending notifications>>>>>>", ex);
		}
		return flag;
	}



}
