package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.MarketplaceCsCheckoutService;
import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceBasketController;
import com.tisl.mpl.cockpits.cscockpit.widgets.helpers.MarketplaceServiceabilityCheckHelper;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.data.VoucherDiscountData;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.BuyBoxData;
import com.tisl.mpl.marketplacecommerceservices.service.MplCommerceCartService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData;
import com.tisl.mpl.seller.product.facades.BuyBoxFacade;
import com.tisl.mpl.service.InventoryReservationService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.InventoryReservListResponse;
import com.tisl.mpl.wsdto.InventoryReservResponse;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.browsers.WidgetBrowserModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.TypeUtils;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultBasketController;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.WeakArrayList;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.DateRestrictionModel;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.UserRestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

/**
 * The Class MarketPlaceBasketControllerImpl.
 */
public class MarketPlaceBasketControllerImpl extends DefaultBasketController
		implements MarketPlaceBasketController {

	/** The Constant _15. */
	//private static final int _15 = 15;

	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MarketPlaceBasketControllerImpl.class);
	
	private static final String MOBILENUMBER_REGEX = "^[0-9]{10}";
	
	protected static final String CATALOG_ID = "mplProductCatalog";
	protected static final String VERSION_ONLINE = "Online";

	/** The marketplace serviceability check helper. */
	@Autowired
	private MarketplaceServiceabilityCheckHelper marketplaceServiceabilityCheckHelper;

	/** The inventory reservation service. */
	@Autowired
	InventoryReservationService inventoryReservationService;

	/** The mpl extended cart converter. */
	@Autowired
	private Converter<CartModel, CartData> mplExtendedCartConverter;

	/** The mpl commerce cart service. */
	@Autowired
	private MplCommerceCartService mplCommerceCartService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private ConfigurationService configurationService;


	@Autowired
	private BuyBoxFacade buyBoxFacade;
	
	@Autowired
	private MplFindDeliveryFulfillModeStrategy mplFindDeliveryFulfillModeStrategy;
	
	@Resource(name = "voucherService")
	private VoucherService voucherService;
	
	@Resource(name = "voucherModelService")
	private VoucherModelService voucherModelService;
		
	@Resource(name = "mplVoucherService")
	private MplVoucherService mplVoucherService;	

	/**
	 * Adds the to market place cart.
	 *
	 * @param product
	 *            the product
	 * @param quantity
	 *            the quantity
	 * @return true, if successful
	 * @throws CommerceCartModificationException
	 *             the commerce cart modification exception
	 */
	@Override
	public boolean addToMarketPlaceCart(TypedObject product,
			final long quantity, final String ussid)
			throws CommerceCartModificationException {
		boolean isItemAddedToCart = false;
		final ProductModel productModel = (ProductModel) TypeUtils.unwrapItem(
				product, ProductModel.class);
		final CartModel cart = getCartModel();
		ImpersonationContext context = createImpersonationContext(cart);
		getImpersonationService()
				.executeInContext(
						context,
						new ImpersonationService.Executor<CartModel, ImpersonationService.Nothing>() {
							@Override
							public CartModel execute() {
								try {
									CommerceCartParameter cartParameter = new CommerceCartParameter();
									cartParameter.setCart(cart);
									cartParameter.setUssid(ussid);
									cartParameter.setQuantity(quantity);
									cartParameter.setProduct(productModel);
									
									((MplCommerceCartService) getCommerceCartService())
											.addToCartWithUSSID(cartParameter);
									cart.setCartReservationDate(null);
								} catch (CommerceCartModificationException e) {
									LOG.error("Exception calculating cart ["
											+ cart + "]", e);
								}
								return null;
							}
						});
		
		modelService.save(cart);
		
			try {
				getMplVoucherService().checkCartWithVoucher(cart);
			} catch (EtailNonBusinessExceptions e) {
				LOG.error("Exception calculating cart ["
						+ cart + "]", e);
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
			}
			catch(VoucherOperationException e)
			{
				LOG.error("Exception calculating cart ["
						+ cart + "]", e);
			}
			catch(Exception e)
			{
				LOG.error("Exception calculating cart ["
						+ cart + "]", e);
				ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
			}

		return isItemAddedToCart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultBasketController
	 * #addToCart(de.hybris.platform.cockpit.model.meta.TypedObject, long)
	 */
	@Override
	public void addToCart(TypedObject product, final long quantity) {
		final CartModel cart = getCartModel();
		try {
			final ProductModel productModel = (ProductModel) TypeUtils
					.unwrapItem(product, ProductModel.class);
			BuyBoxData buyBoxData =  getBuyBoxFacade().buyboxPrice(productModel.getCode());
			cart.setCartReservationDate(null);
			addToMarketPlaceCart(product, quantity, buyBoxData.getSellerArticleSKU());
		} catch (CommerceCartModificationException e) {
			LOG.error("Exception while adding to cart [" + cart + "]", e);

		}
	}

	/**
	 * Gets the product value.
	 *
	 * @param productModel
	 *            the product model
	 * @return the product value
	 */
	@Override
	public List<String> getSellerDetails(ProductModel productModel) {
		List<String> sellerNames = marketplaceServiceabilityCheckHelper
				.getSellerDetails(productModel);

		return sellerNames;

	}

	/**
	 * Gets the response for pin code.
	 *
	 * @param product
	 *            the product
	 * @param pin
	 *            the pin
	 * @param isDeliveryDateRequired
	 *            the is delivery date required
	 * @return listingId
	 * @throws EtailNonBusinessExceptions
	 *             the etail non business exceptions
	 * @throws ClientEtailNonBusinessExceptions
	 *             the client etail non business exceptions
	 * @description this method checks the restriction list and calls pincode
	 *              service accordingly
	 */
	@Override
	public List<PinCodeResponseData> getResponseForPinCode(
			final ProductModel product, final String pin,
			final String isDeliveryDateRequired, final String ussid)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions {

		List<PinCodeResponseData> responseData = marketplaceServiceabilityCheckHelper
				.getResponseForPinCode(product, pin, isDeliveryDateRequired, ussid);
		LOG.info("responseData size:" + responseData);

		return responseData;
	}

	/**
	 * Reserve cart.
	 *
	 * @param cart
	 *            the cart
	 * @param duration
	 *            the duration
	 * @return the boolean
	 */
	@Override
	public Boolean reserveCart(final CartModel cart)  throws ValidationException{
		Boolean isCartReserved = Boolean.TRUE;
		List<ResourceMessage> errorMessages = new ArrayList<ResourceMessage>();
		
		  modelService.refresh(cart);
		
	      if ((cart.getDeliveryAddress() == null))
	      {
	        errorMessages.add(new ResourceMessage("placeOrder.validation.noShippingAddress"));
	      }
	     
	       if(!(cart.getDeliveryAddress() != null && cart.getDeliveryAddress().getPhone1()!=null  && 
	    		  cart.getDeliveryAddress().getPhone1().matches(MOBILENUMBER_REGEX) ) ) { 
	    	  errorMessages.add(new ResourceMessage("placeOrder.validation.invalidPhone"));
	      }

	      if (cart.getPaymentAddress() == null)
	      {
	        errorMessages.add(new ResourceMessage("placeOrder.validation.noPaymentAddress"));
	      }
	      
			for(AbstractOrderEntryModel entry : cart.getEntries()){
				if(!mplFindDeliveryFulfillModeStrategy.isTShip(entry.getSelectedUSSID())){						
					errorMessages.add(new ResourceMessage("placeOrder.validation.sship",Arrays.asList(entry.getInfo())));
					break;
				} 
			}
	      
			for(AbstractOrderEntryModel entry : cart.getEntries()){
					if(entry.getMplDeliveryMode()==null){						
						errorMessages.add(new ResourceMessage("placeOrder.validation.noDeliveryMode",Arrays.asList(entry.getInfo())));
					} else{
						validateWithOMS(getCockpitTypeService().wrapItem(entry), getCockpitTypeService().wrapItem(entry.getMplDeliveryMode()));
					}
			}

			if(errorMessages.isEmpty())	{
				if (configurationService
						.getConfiguration()
						.getBoolean(
								MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS,
								false)) {
					LOG.info("Bypass the pincode serviceability:"
							+ configurationService
									.getConfiguration()
									.getBoolean(
											MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS));
	
				// cart.setAddressConfirmationFlag(true);
				cart.setCartReservationDate(new Date());
				modelService.save(cart);
				modelService.refresh(cart);
				LOG.info("Bypassing the cart inventoryReservationService check call to OMS");
			} else {
				final List<com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData> cartdatalist = new WeakArrayList<CartSoftReservationData>();
	
				final String pincode = cart.getDeliveryAddress().getPostalcode();
				if (StringUtils.isEmpty(pincode)) {
					LOG.info("Customer has not selected the delivery address. Please select a delivery address and then continue");
					return false;
				}
				List<AbstractOrderEntryModel> cartEntries = cart.getEntries();
				//Map<String, String> cartMap = getCartData(cart);
				try {
					for (AbstractOrderEntryModel cartEntry : cartEntries) {
						com.tisl.mpl.mplcommerceservices.service.data.CartSoftReservationData cartSoftReservationRequestData = new CartSoftReservationData();
	
						if(BooleanUtils.isTrue(cartEntry.getGiveAway())){
						cartSoftReservationRequestData.setIsAFreebie("Y");
						cartSoftReservationRequestData.setParentUSSID(cartEntry.getParentTransactionID());
						}
						Integer cartEntryQty = (int) (long) cartEntry.getQuantity();
						cartSoftReservationRequestData.setQuantity(cartEntryQty);
	
//						cartSoftReservationRequestData.setStoreId(cart.getStore().getUid());
						
						cartSoftReservationRequestData.setUSSID(cartEntry
								.getSelectedUSSID());
						
						cartSoftReservationRequestData
								.setDeliveryMode( MarketplaceCockpitsConstants.delOmsCodeMap.get(cartEntry.getMplDeliveryMode().getDeliveryMode().getCode()));
	
						// refer TIS-276 for details
						
						cartSoftReservationRequestData.setFulfillmentType(mplFindDeliveryFulfillModeStrategy.findDeliveryFulfillMode(cartEntry.getSelectedUSSID()));
						cartdatalist.add(cartSoftReservationRequestData);
					}
					// Added inventory request type to set the duration type for cart only
					InventoryReservListResponse response = inventoryReservationService
							.convertDatatoWsdto(cartdatalist, cart.getGuid(),
									pincode,MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART);
					
					for(InventoryReservResponse entry: response.getItem()){
						
						if(!"success".equalsIgnoreCase(entry.getReservationStatus()) ){
							errorMessages.add(new ResourceMessage("placeOrder.validation.cartItemNotReserved",
									Arrays.asList(entry.getUSSID(), entry.getAvailableQuantity()==null?entry.getReservationStatus():entry.getAvailableQuantity())));
						} else{
							// cart.setAddressConfirmationFlag(true);
							cart.setCartReservationDate(new Date());
							modelService.save(cart);
							modelService.refresh(cart);
							
						}
					}
	
				} catch (Exception ex) {
					isCartReserved = Boolean.FALSE;
					cart.setCartReservationDate(null);
					modelService.save(cart);
					modelService.refresh(cart);
					errorMessages.add(new ResourceMessage("placeOrder.validation.cartNotReserved"));
					LOG.error(
							"Exception caught in MarketPlaceBasketControllerImpl.reserveCart",
							ex);
					errorMessages.add(new ResourceMessage("placeOrder.validation.cartNotReservedException",Arrays.asList(ex)));
					throw new ValidationException(errorMessages);
					}
				}
			}
		if (!errorMessages.isEmpty()){
			throw new ValidationException(errorMessages);
		}
		return isCartReserved;

	}
	
/**
	 * Gets the cart data.
	 *
	 * @param cart
	 *            the cart
	 * @return the cart data
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getCartData(final CartModel cart) {
		CartData cartData = mplExtendedCartConverter.convert(cart);
		// Key=Cart Entry,Value=Fulfillment Type
		Map<String, String> fulfillmentData = new WeakHashMap<String, String>();
		//final List<String> categoryList = new ArrayList<String>();
		final JaloSession session = JaloSession.getCurrentSession();
		session.createLocalSessionContext();
		try
		{
			Collection<CatalogVersion> vers = null;

			final Collection<CatalogVersion> cvs = (Collection<CatalogVersion>) session
					.getAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS);

			for (final CatalogVersion ver : cvs)
			{
				if (VERSION_ONLINE.equals(ver.getVersion()) && CATALOG_ID.equals(ver.getCatalog().getId()))
				{
					vers = Collections.singleton(ver);
					break;
				}
			}
			session.setAttribute(CatalogConstants.SESSION_CATALOG_VERSIONS, vers);
			fulfillmentData = mplCommerceCartService
					.getFullfillmentMode(cartData);
		} catch (CMSItemNotFoundException e) {
			LOG.error(
					"Exception in MarketPlaceBasketControllerImpl.getCartData",
					e);
		}finally
		{
			session.removeLocalSessionContext();
		}
		return fulfillmentData;
	}

	/**
	 * Save phone number to cart.
	 *
	 * @param cellPhoneNumber
	 *            the cell phone number
	 */
	@Override
	public void savePhoneNumberToCart(final Long cellPhoneNumber) {
		final CartModel cart = (CartModel) getCart().getObject();
		try {
			AddressModel address = cart.getDeliveryAddress();
			 address.setPhone1(String.valueOf(cellPhoneNumber));
			 modelService.save(address);
			modelService.save(cart);
			LOG.info("Cell phone saved in cart delivery address:" + cellPhoneNumber);	
		} catch(ModelSavingException ex) {
			LOG.error("The phone number could not be saved in the database");
		}
		
	}

	/**
	 * Check cart reservation status.
	 *
	 * @param cart
	 *            the cart
	 * @param confirmAddress
	 *            the confirm address
	 * @return true, if successful
	 */
	@Override
	public boolean checkCartReservationStatus(final CartModel cart) {
		return ((MarketplaceCsCheckoutService)getCsCheckoutService()).checkCartReservationStatus(cart);
	}

	/**
	 * Sets the quantity.
	 *
	 * @param cartEntry
	 *            the cart entry
	 * @param quantity
	 *            the quantity
	 */
	@Override
	public void setQuantity(final TypedObject cartEntry, final long quantity) {
		final CartModel cart = getCartModel();

		final AbstractOrderEntryModel entry = (AbstractOrderEntryModel) TypeUtils
				.unwrapItem(cartEntry, AbstractOrderEntryModel.class);
		ImpersonationContext context = createImpersonationContext(cart);
		getImpersonationService()
				.executeInContext(
						context,
						new ImpersonationService.Executor<CartModel, ImpersonationService.Nothing>() {
							@Override
							public CartModel execute() {
								try {
									CommerceCartParameter cartParameter = new CommerceCartParameter();
									cartParameter.setCart(cart);
									cartParameter.setUssid(entry.getSelectedUSSID());
									cartParameter.setEntryNumber(entry
											.getEntryNumber());
									cartParameter.setQuantity(quantity);
									getCommerceCartService()
											.updateQuantityForCartEntry(
													cartParameter);
									cart.setCartReservationDate(null);
									 ((MarketplaceCheckoutControllerImpl) getCheckoutController()).removeCODPayment();
									
									 
								} catch (CommerceCartModificationException | PaymentException | 
										ValidationException | JaloInvalidParameterException | NumberFormatException e) {
									LOG.error("Exception calculating cart ["
											+ cart + "]", e);
									throw new ClientEtailNonBusinessExceptions(e);
								}
								return null;
							}
						});
		
		modelService.save(cart);
				try {
					getMplVoucherService().checkCartWithVoucher(cart);
				} catch (EtailNonBusinessExceptions e) {
					LOG.error("Exception calculating cart ["
							+ cart + "]", e);
					ExceptionUtil.etailNonBusinessExceptionHandler(e);
				}catch(VoucherOperationException e)
				{
					LOG.error("Exception calculating cart ["
							+ cart + "]", e);
				}
				catch(Exception e)
				{
					LOG.error("Exception calculating cart ["
							+ cart + "]", e);
					ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
				}

	}
	
	
	@Override
	public void setDeliveryAddress(TypedObject address) {
		CartModel cartModel = getCartModel();
		cartModel.setCartReservationDate(null);
		getModelService().save(cartModel);
		CommerceCheckoutParameter cartParameter = new CommerceCheckoutParameter();
		cartParameter.setCart(cartModel);
		cartParameter.setAddress(TypeUtils.unwrapItem(address, AddressModel.class));
		cartParameter.setIsDeliveryAddress(true);
		getCommerceCheckoutService().setDeliveryAddress(cartParameter);
	}

	@Override
	  public boolean setCartEntryDeliveryMode(TypedObject cartEntry, TypedObject deliveryMode)
	  {
	    boolean changed = false;
	    if ((cartEntry != null) && (cartEntry.getObject() instanceof AbstractOrderEntryModel))
	    {
	      AbstractOrderEntryModel entry = (AbstractOrderEntryModel)cartEntry.getObject();
	      final CartModel cart = (CartModel)entry.getOrder();

	      MplZoneDeliveryModeValueModel deliveryModeModel = null;
	      if ((deliveryMode != null) && (deliveryMode.getObject() instanceof MplZoneDeliveryModeValueModel))
	      {
	        deliveryModeModel = (MplZoneDeliveryModeValueModel)deliveryMode.getObject();
	      }
	      try {
	      ((CartModel)entry.getOrder()).setCartReservationDate(null);  
	      ((MarketplaceCheckoutControllerImpl) getCheckoutController()).removeCODPayment();
	    	entry.setMplDeliveryMode(deliveryModeModel);
	        getModelService().save(entry);
	        getModelService().save(entry.getOrder());
	        getModelService().refresh(entry);
	        entry.getOrder().setCalculated(false);
	        getModelService().save(entry.getOrder());
	        getModelService().refresh(entry.getOrder());	        
	        changed = true;
	        CommerceCartParameter cartParameter = new CommerceCartParameter();
			cartParameter.setCart((CartModel)entry.getOrder());			
			getCommerceCartService().recalculateCart(cartParameter);
			
			getMplVoucherService().checkCartWithVoucher(cart);
	      }catch (CalculationException | PaymentException | ValidationException e) {
				LOG.error(e);
				ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
			}	
	      catch(final VoucherOperationException e)
			{
	    	  LOG.error(e);
			}
	      catch(final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
			}
			catch(final Exception e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
			}
			
	    }
	    return changed;
	  }
	
	@Override
/*     */   public boolean  checkCustomerStatus()
/*     */     throws  ValidationException
/*     */   {
/* 382 */      ((MarketplaceCsCheckoutService)getCsCheckoutService()).checkCustomerStatus( getCartModel());
				return true;
					
/*     */   }
	
	
	
	
	
	
	
	
	@Override
	public boolean validateWithOMS(TypedObject cartEntry,
			TypedObject deliveryMode) throws ValidationException {

		//
		List<ResourceMessage> errorMessages = new ArrayList<ResourceMessage>();
		AbstractOrderEntryModel entry = null;
		if (configurationService
				.getConfiguration()
				.getBoolean(
						MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS,
						false)) {
			LOG.info("Bypassing all of the serviceability checks");
			return false;
		}
		boolean changed = false;
		if ((cartEntry != null)
				&& (cartEntry.getObject() instanceof AbstractOrderEntryModel)) {
			entry = (AbstractOrderEntryModel) cartEntry.getObject();
			if (entry.getOrder().getDeliveryAddress() == null)
		      {
		        errorMessages.add(new ResourceMessage("placeOrder.validation.noShippingAddress"));
		        throw new ValidationException(errorMessages);
		      }
			List<PinCodeResponseData> pinCodeResponses = marketplaceServiceabilityCheckHelper
					.getResponseForPinCode(
							entry.getProduct(),
							String.valueOf(entry.getOrder()
									.getDeliveryAddress().getPostalcode()),
							"Y", entry.getSelectedUSSID());

			MplZoneDeliveryModeValueModel deliveryModeModel = null;
			if ((deliveryMode != null)
					&& (deliveryMode.getObject() instanceof MplZoneDeliveryModeValueModel)) {
				deliveryModeModel = (MplZoneDeliveryModeValueModel) deliveryMode
						.getObject();
			}

			/*
			 * DeliveryModeModel deliveryModeModel = null; if ((deliveryMode !=
			 * null) && (deliveryMode.getObject() instanceof DeliveryModeModel))
			 * { deliveryModeModel =
			 * (DeliveryModeModel)deliveryMode.getObject(); }
			 */

			if (pinCodeResponses == null || pinCodeResponses.isEmpty()) {
				errorMessages.add(new ResourceMessage(
						"placeOrder.validation.noomsreponse"));
				throw new ValidationException(errorMessages);
			}
			for (PinCodeResponseData pinData : pinCodeResponses) {
				if (pinData.getUssid().equalsIgnoreCase(
						entry.getSelectedUSSID())) {

					/*
					 * if(!"Y".equalsIgnoreCase(pinData.getCod())){
					 * errorMessages.add(new
					 * ResourceMessage("placeOrder.validation.nocod"
					 * ,Arrays.asList(pinData.getUssid()))); }
					 * 
					 * if(!"Y".equalsIgnoreCase(pinData.getIsServicable())){
					 * errorMessages.add(new
					 * ResourceMessage("placeOrder.validation.notservicable"
					 * ,Arrays.asList(pinData.getUssid()))); }
					 * 
					 * if(!"N".equalsIgnoreCase(pinData.getIsCODLimitFailed())){
					 * errorMessages.add(new
					 * ResourceMessage("placeOrder.validation.codlimitFailed"
					 * ,Arrays.asList(pinData.getUssid()))); }
					 */

					if (pinData.getValidDeliveryModes() == null
							|| pinData.getValidDeliveryModes().isEmpty()) {
						errorMessages.add(new ResourceMessage(
								"placeOrder.validation.noomsreponse", Arrays
										.asList(pinData.getUssid())));
						throw new ValidationException(errorMessages);
					}
					for (DeliveryDetailsData delData : pinData
							.getValidDeliveryModes()) {
						if (deliveryModeModel
								.getDeliveryMode()
								.getCode()
								.equalsIgnoreCase(
										MarketplaceCockpitsConstants.delCodeMap
												.get(delData.getType()))) {
							changed = true;
							if (!delData.getIsPincodeServiceable()) {
								errorMessages.add(new ResourceMessage(
										"placeOrder.validation.notservicable",
										Arrays.asList(pinData.getUssid())));
								break;
							}
							if (!delData.getIsCOD()) {
								errorMessages.add(new ResourceMessage(
										"placeOrder.validation.nocod", Arrays
												.asList(pinData.getUssid())));
								break;
							}

							//TISBOX-1746  commenting see TISBOX-1746
							if (delData.getIsCODLimitFailed()) {
								errorMessages.add(new ResourceMessage(
										"placeOrder.validation.codlimitFailed",
										Arrays.asList(pinData.getUssid())));
								break;
							}
						}
					}

				}

			}
			if (!changed) {
				errorMessages.add(new ResourceMessage(
						"placeOrder.validation.noomsdeliverymode",Arrays.asList(deliveryModeModel.getDeliveryMode().getName(),entry.getInfo())));
			}
		}
		if (!errorMessages.isEmpty()) {
			throw new ValidationException(errorMessages);
		}
		return changed;

	}

	@Override
	public String applyVoucher(final String voucherCode)
	{
		final CartModel cartModel =getCartModel();
		
		if(cartModel.getEntries() == null || cartModel.getEntries().size()<=0)
		{
			LOG.error("No Product available in Cart to apply Voucher : "+voucherCode);
			return "no_product_selected";
		}

		if (org.apache.commons.lang.StringUtils.isBlank(voucherCode))
		{
			LOG.error("Parameter voucherCode must not be empty");
			return "no_voucher_code";

		}

		
		if (!isVoucherCodeValid(voucherCode))
		{
			LOG.error("Invalid Voucher : " + voucherCode);
			return "invalid_voucher_code";
			
		}

		
		final VoucherModel voucher = getVoucherModel(voucherCode);
		if (voucher == null)
		{
			LOG.error("Voucher not found: " + voucherCode);
			return "voucher_notfound";


		}
		
		if (voucher.getValue().doubleValue() <= 0)
		{
			LOG.error("Invalid Voucher : " + voucherCode);
			return "invalid_voucher_code";
		}
		
		if (!checkVoucherIsApplicable(voucher, voucherCode, cartModel))
		{
			LOG.error("Voucher is not applicable");
			final String error = checkViolatedRestrictions(voucher, cartModel);
			if(null!=cartModel.getUser() && cartModel.getUser().getUid().equalsIgnoreCase("anonymous"))
			{
				return "no_user";
			}
			if (error.equalsIgnoreCase("Date"))
			{
				return "date_invalid";
			}
			else if (error.equalsIgnoreCase("User"))
			{
				return "user_not_valid";
			}
			else
			{
				return "voucher_inapplicable";
			}
		}

		else if (!checkVoucherIsReservable(voucher, voucherCode, cartModel))
		{
			LOG.error("Voucher is not reservable");
			return "voucher_not_reservable";
		}
		else
		{
			try
			{
				if (!getVoucherService().redeemVoucher(voucherCode, cartModel))
				{
					LOG.error("Error while applying voucher: " + voucherCode);
					return "error_voucher";
					
				}
				//Important! Checking cart, if total amount <0, release this voucher
				//((EziBuyCommerceCartService) getCommerceCartService()).setAppliedVoucherCode(cartModel, voucherCode);
				//getCommerceCartService().recalculateCart(cartModel);
				
				getMplVoucherService().recalculateCartForCoupon(cartModel);
				
				final List<AbstractOrderEntryModel> applicableOrderEntryList = getMplVoucherService().getOrderEntryModelFromVouEntries(voucher,
						cartModel);
				
				final VoucherDiscountData data=getMplVoucherService().checkCartAfterApply(voucher, cartModel, applicableOrderEntryList);
				if (null != data && StringUtils.isNotEmpty(data.getRedeemErrorMsg()))
				{
					if (data.getRedeemErrorMsg().equalsIgnoreCase("freebie"))
					{
						return "freebie";
					}
					else if (data.getRedeemErrorMsg().equalsIgnoreCase("Price_exceeded"))
					{
						return "total_price_exceeded";
					}
					else if (data.getRedeemErrorMsg().equalsIgnoreCase("not_applicable"))
					{
						return "voucher_inapplicable";
					}
				}
				
				getMplVoucherService().setApportionedValueForVoucher(voucher, cartModel, voucherCode, applicableOrderEntryList);
				
				//For TISSTRT-302
				return "coupon_redeem";
			}
			catch (Exception e)
			{
				LOG.error("Error while applying voucher: " + voucherCode);
				ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
				return "error_voucher";
			}
		}
	}
	protected void validateVoucherCodeParameter(final String voucherCode)
	{
		if (StringUtils.isBlank(voucherCode))
		{
			throw new IllegalArgumentException("Parameter voucherCode must not be empty");
		}
	}

	protected boolean isVoucherCodeValid(final String voucherCode)
	{
		final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
		if (voucher == null)
		{
			return false;
		}
		return true;
	}

	protected boolean checkVoucherCanBeRedeemed(final VoucherModel voucher, final String voucherCode)
	{
		return getVoucherModelService().isApplicable(voucher, getCartModel())
				&& getVoucherModelService().isReservable(voucher, voucherCode, getCartModel());
	}
	protected VoucherModel getVoucherModel(final String voucherCode)
	{
		final VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
		if (voucher == null)
		{
			throw new IllegalArgumentException("Voucher not found: " + voucherCode);
		}
		return voucher;
	}
	
	@Override
	public String releaseVoucher()
	{
		//Modified for TISSTRT-303
		String message = StringUtils.EMPTY;
		final CartModel cartModel = getCartModel();
		Collection<String> voucherList = getVoucherService().getAppliedVoucherCodes(cartModel);
		if(CollectionUtils.isNotEmpty(voucherList))
		{
			for (String voucherCode : voucherList) {
				try {
					final VoucherModel voucher = getVoucherModel(voucherCode);
					getVoucherService().releaseVoucher(voucherCode, cartModel);
					
					for (final AbstractOrderEntryModel entry : getMplVoucherService().getOrderEntryModelFromVouEntries(voucher, cartModel))
						
					{
						entry.setCouponCode("");
						entry.setCouponValue(Double.valueOf(0.00D));
						getModelService().save(entry);
					}
					
					//((EziBuyCommerceCartService) getCommerceCartService()).setAppliedVoucherCode(cartModel, null);
				} catch (JaloPriceFactoryException e) {
					LOG.error("Couldn't release voucher: ",e);
				} catch (ModelSavingException e){
					LOG.error("Couldn't release voucher: ",e);
				}
				catch(Exception e)
				{
					LOG.error("Couldn't release voucher: " + voucherCode);
					ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
				}
			}
			try {
				//getCommerceCartService().recalculateCart(cartModel);
				getMplVoucherService().recalculateCartForCoupon(cartModel);
				message="release_voucher";
			} catch (EtailNonBusinessExceptions e) {
				LOG.error("Recalculation of Cart Failed ");
			}catch(Exception e)
			{
				LOG.error("Recalculation of Cart Failed ");
				ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
			}
		}
		
		return message;
	}
	@Override
	public Collection<String> getAppliedVoucherCodesList()
	{
		final CartModel cartModel = getCartModel();
		Collection<String> voucherList = getVoucherService().getAppliedVoucherCodes(cartModel);
		return voucherList;
	}
	
	
	protected boolean checkVoucherIsApplicable(final VoucherModel voucher, final String voucherCode, final CartModel cartModel)
	{
		return getVoucherModelService().isApplicable(voucher, cartModel);
	}
	
	
	protected String checkViolatedRestrictions(final VoucherModel voucher, final CartModel cartModel)
	{
		final List<RestrictionModel> getViolatedRestrictions = getVoucherModelService().getViolatedRestrictions(voucher, cartModel);
		String error = "";
		for (final RestrictionModel restriction : getViolatedRestrictions)
		{
			if (restriction instanceof DateRestrictionModel)
			{
				LOG.error("Date restriction is violated");
				error = "Date";
				break;
			}
			else if (restriction instanceof UserRestrictionModel)
			{
				LOG.error("user restriction is violated");
				error = "User";
				break;
			}
		}
		return error;
	}
	
	
	protected boolean checkVoucherIsReservable(final VoucherModel voucher, final String voucherCode, final CartModel cartModel)
	{
		return getVoucherModelService().isReservable(voucher, voucherCode, cartModel);
	}	
	
	
	
	/**
	 * This method is called when "Checkout" button is clicked in CS Cockpit
	 */
	@Override
	public void triggerCheckout() throws ValidationException
	{
		try
		{
			CartModel cartModel = getCartModel();
				
			calculateCartInContext(cartModel);
			validateBasketReadyForCheckout(cartModel);
			setDeliveryAddressIfAvailable(cartModel);
			setDeliveryModeIfAvailable(cartModel);
			setPaymentAddressIfAvailable(cartModel);
			
			//Custom to handle voucher custom code
			getMplVoucherService().checkCartWithVoucher(cartModel);
				     
			WidgetBrowserModel checkoutBrowser = getCheckoutBrowser();
				     
			getWidgetHelper().openAndFocusBrowser(checkoutBrowser);
			getWidgetHelper().focusWidget(checkoutBrowser.getBrowserCode(), getCheckoutBrowserDefaultWidgetCode());
		}catch (final VoucherOperationException e)
		{
			LOG.error(e);
		}
		catch(final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch(final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler((EtailNonBusinessExceptions) e);
		}
	}
	
	
	
	
	/**
	 * @return the mplVoucherService
	 */
	public MplVoucherService getMplVoucherService()
	{
		return mplVoucherService;
	}

	/**
	 * @param mplVoucherService
	 *           the mplVoucherService to set
	 */
	public void setMplVoucherService(final MplVoucherService mplVoucherService)
	{
		this.mplVoucherService = mplVoucherService;
	}
	
	public BuyBoxFacade getBuyBoxFacade() {
		return buyBoxFacade;
	}

	@Required
	public void setBuyBoxFacade(BuyBoxFacade buyBoxFacade) {
		this.buyBoxFacade = buyBoxFacade;
	}
	
	public VoucherModelService getVoucherModelService()
	{
		return voucherModelService;
	}


	public void setVoucherModelService(final VoucherModelService voucherModelService)
	{
		this.voucherModelService = voucherModelService;
	}


	public VoucherService getVoucherService()
	{
		return voucherService;
	}


	public void setVoucherService(final VoucherService voucherService)
	{
		this.voucherService = voucherService;
	}
		
	
}
