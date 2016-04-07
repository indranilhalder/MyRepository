package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.MarketplaceCsCheckoutService;
import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceBasketController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCheckoutController;
import com.tisl.mpl.cockpits.cscockpit.widgets.helpers.MarketplaceServiceabilityCheckHelper;
import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.CODPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplDeliveryCostService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeRestrictionService;
import com.tisl.mpl.service.PinCodeDeliveryModeService;

import de.hybris.platform.catalog.impl.DefaultCatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.commercefacades.product.data.DeliveryDetailsData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commerceservices.impersonation.ImpersonationContext;
import de.hybris.platform.commerceservices.impersonation.ImpersonationService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ResourceMessage;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultCheckoutController;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutCartWidgetModel;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutPaymentWidgetModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.WeakArrayList;

public class MarketplaceCheckoutControllerImpl extends
		DefaultCheckoutController implements MarketplaceCheckoutController {

	/** The Constant OMS_MODES_NOT_CONFIGURED. */
	private static final String OMS_MODES_NOT_CONFIGURED = "omsModesNotConfigured";

	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MarketplaceCheckoutControllerImpl.class);

	/** The Constant NO_RESPONSE_FOR_PRODUCT. */
	private static final String NO_RESPONSE_FOR_PRODUCT = "noResponseForProduct";

	/** The Constant _15. */
	private static final int _15 = 15;

	private static final String NO_DELIVERY_MODE_FOR_USSID = "noDeliveryForUssid";

	/** The mpl pincode restriction service. */
	@Resource(name = "mplPincodeRestrictionService")
	private MplPincodeRestrictionService mplPincodeRestrictionService;

	/** The pin code delivery mode service. */
	@Resource(name = "pinCodeDeliveryModeService")
	private PinCodeDeliveryModeService pinCodeDeliveryModeService;

	@Autowired
	private MarketplaceServiceabilityCheckHelper marketplaceServiceabilityCheckHelper;

	/** The catalog version service. */
	@Autowired
	private DefaultCatalogVersionService catalogVersionService;

	/** The configuration service. */
	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private ModelService modelService;

	@Autowired
	/** The c od payment service. */
	private CODPaymentService cODPaymentService;

	@Autowired
	private MplDeliveryCostService deliveryCostService;

	
	@Autowired
	private MplPaymentService mplPaymentService;
	
	@Autowired
	private MplFindDeliveryFulfillModeStrategy mplFindDeliveryFulfillModeStrategy;
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
	 * @param productCode
	 *            the product code
	 * @param pin
	 *            the pin
	 * @param requestData
	 *            the request data
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
				.getResponseForPinCode(product, pin, isDeliveryDateRequired,
						ussid);

		return responseData;
	}

	@Override
	public void setCurrentSite() {
		catalogVersionService.getSessionCatalogVersions();
		final CatalogVersionModel catalogVersion = catalogVersionService
				.getCatalogVersion("mplProductCatalog", "Online");
		catalogVersionService.setSessionCatalogVersions(Collections
				.singleton(catalogVersion));
	}

	/**
	 * Process pin serviceability.
	 *
	 * @param widget
	 *            the widget
	 * @param deliveryModeDropdown
	 *            the delivery mode dropdown
	 * @param pinCode
	 *            the pin code
	 * @param product
	 *            the product
	 * @param isDeliveryDateRequired
	 *            the is delivery date required
	 */
	@Override
	public void processPinServiceability(
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			Listbox deliveryModeDropdown, String pinCode,
			CartEntryModel cartEntry, final String isDeliveryDateRequired)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions {
		try {
			List<PinCodeResponseData> pinCodeResponses = ((MarketplaceCheckoutController) widget
					.getWidgetController()).getResponseForPinCode(
					cartEntry.getProduct(), String.valueOf(pinCode),
					isDeliveryDateRequired, cartEntry.getSelectedUSSID());

			if (CollectionUtils.isNotEmpty(pinCodeResponses)) {
				LOG.info("pinCodeResponse:" + pinCodeResponses.size());
				for (final PinCodeResponseData pincodeResponse : pinCodeResponses) {
					if (StringUtils.equals(cartEntry.getSelectedUSSID(),
							pincodeResponse.getUssid())
							&& CollectionUtils.isNotEmpty(pincodeResponse
									.getValidDeliveryModes())) {
						List<DeliveryModeModel> validDeliveryModes = getValidDeliveryModesForCheckout(
								getDeliveryModesfromTypedObject(getBasketController()
										.getAvailableDeliveryModes()),
								pincodeResponse.getValidDeliveryModes());

						if (CollectionUtils.isNotEmpty(validDeliveryModes)) {
							for (final DeliveryModeModel deliveryMode : validDeliveryModes) {
								Listitem deliveryModeItem = new Listitem(
										deliveryMode.getCode());
								deliveryModeItem
										.setParent(deliveryModeDropdown);
								deliveryModeItem.setSelected(true);
							}
						} else {
							LOG.warn("The delivery modes mentioned in the OMS are not configured in hybris");
							popupMessage(widget, OMS_MODES_NOT_CONFIGURED);
						}
					} else {
						LOG.info("There are no delivery modes specified in OMS for the ussid:"
								+ pincodeResponse.getUssid());
						popupMessage(widget, NO_DELIVERY_MODE_FOR_USSID);
					}
				}
			} else {
				popupMessage(widget, NO_RESPONSE_FOR_PRODUCT);
			}
		} catch (Exception ex) {
			LOG.error("Exception while fetching pinCodeResponses:", ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}


	/**
	 * Gets the valid delivery modes for checkout.
	 *
	 * @param deliveryModesfromTypedObject
	 *            the delivery modesfrom typed object
	 * @param validDeliveryModes
	 *            the valid delivery modes
	 * @return the valid delivery modes for checkout
	 */
	private List<DeliveryModeModel> getValidDeliveryModesForCheckout(
			List<DeliveryModeModel> configuredDeliveryModes,
			List<DeliveryDetailsData> deliveryDetailFromPincodeResponse) {

		String defaultDeliveryMode = MarketplaceCockpitsConstants.EXPRESS_DELIVERY;
		if (CollectionUtils.isNotEmpty(configuredDeliveryModes)) {
			for (final DeliveryDetailsData deliveryDetail : deliveryDetailFromPincodeResponse) {
				LOG.info("deliveryDetail.getType() in getValidDeliveryModesForCheckout:"
						+ deliveryDetail.getType());
				if (StringUtils.equals(deliveryDetail.getType(), "HD")) {
					defaultDeliveryMode = MarketplaceCockpitsConstants.HOME_DELIVERY;
				}

				final Iterator<DeliveryModeModel> configuredModeIterator = configuredDeliveryModes
						.iterator();

				while (configuredModeIterator.hasNext()) {
					final DeliveryModeModel deliverymode = configuredModeIterator
							.next();
					if (!StringUtils.equals(deliverymode.getCode(),
							defaultDeliveryMode)) {
						LOG.info(deliverymode.getCode()
								+ ":deliverymode is not exisiting in the service response, hence removing from the final list");
						configuredDeliveryModes.remove(deliverymode);
						Log.info("Configured delivery mode list size:"
								+ configuredDeliveryModes.size());
					}
				}
			}

			Log.info("final valid delivery mode list size:"
					+ configuredDeliveryModes.size());
			Log.info("Response delivery mode list size:"
					+ deliveryDetailFromPincodeResponse.size());

			return configuredDeliveryModes;
		}

		return null;

	}

	/**
	 * Gets the delivery modesfrom typed object.
	 *
	 * @param availableDeliveryModes
	 *            the available delivery modes
	 * @return the delivery modesfrom typed object
	 */
	private List<DeliveryModeModel> getDeliveryModesfromTypedObject(
			List<TypedObject> availableDeliveryModes) {
		List<DeliveryModeModel> configuredDeliveryModes = new WeakArrayList<DeliveryModeModel>();
		if (CollectionUtils.isNotEmpty(availableDeliveryModes)) {
			for (TypedObject availableDeliveryMode : availableDeliveryModes) {
				configuredDeliveryModes
						.add((DeliveryModeModel) availableDeliveryMode
								.getObject());
			}
		} else {
			LOG.info("There are no delivery modes configured in hybris");
		}

		return configuredDeliveryModes;
	}

	/**
	 * Popup message.
	 *
	 * @param widget
	 *            the widget
	 * @param message
	 *            the message
	 */
	@Override
	public void popupMessage(
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			final String message) {
		try {
			Messagebox.show(
					LabelUtils.getLabel(widget, message, new Object[0]),
					MarketplaceCockpitsConstants.INFO, Messagebox.OK,
					Messagebox.ERROR);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	/**
	 * Checks if is cart cod eligible.
	 *
	 * @return the boolean
	 * @throws ValidationException
	 */
	@Override
	public Boolean isCartCODEligible() throws ValidationException {

		Boolean isEntryCODEligible = true;
		if (configurationService
				.getConfiguration()
				.getBoolean(
						MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS,
						false)) {
			LOG.info("Bypassing all of the serviceability checks");
			return isEntryCODEligible;
		}
		//
		List<ResourceMessage> errorMessages = new ArrayList<ResourceMessage>();
		try {
			final CartModel cart = (CartModel) getBasketController().getCart()
					.getObject();
			final String pincode = cart.getDeliveryAddress().getPostalcode();

			for (final AbstractOrderEntryModel cartEntry : cart.getEntries()) {
				List<PinCodeResponseData> responseData = marketplaceServiceabilityCheckHelper
						.getResponseForPinCode(cartEntry.getProduct(), pincode,
								MarketplaceCockpitsConstants.NO,
								cartEntry.getSelectedUSSID());
				if (responseData == null) {
					return false;
				}
				LOG.info("Response size for pin code serviceability call:"
						+ responseData.size());
				for (PinCodeResponseData response : responseData) {
					LOG.info("COD eligibility status for "
							+ cartEntry.getProduct().getCode() + " is "
							+ response.getCod());

					if (StringUtils.equalsIgnoreCase(response.getCod(),
							MarketplaceCockpitsConstants.NO)) {
						isEntryCODEligible = false;
						errorMessages.add(new ResourceMessage(
								"placeOrder.validation.nocod", Arrays
										.asList(response.getUssid())));
						break;
					}
				}
				break;
			}
		} catch (Exception ex) {
			isEntryCODEligible = false;
			LOG.error(
					"Exception in MarketplaceCheckoutControllerImpl.isCartCODEligible()",
					ex);
			throw new ValidationException(errorMessages);
		}

		LOG.info("isEntryCODEligible:" + isEntryCODEligible);
		return isEntryCODEligible;
	}

	/**
	 * Gets the cart ful fill ment type.
	 *
	 * @return the cart ful fill ment type
	 */
	private Boolean getCartFulFillMentType() {
		boolean isCartTShipFulfillied = true;
		try {
			final CartModel cart = (CartModel) getBasketController().getCart()
					.getObject();
			/*final Map<String, String> cartDataMap = ((MarketPlaceBasketController) getBasketController())
					.getCartData(cart);

			final Set<Entry<String, String>> entrySet = cartDataMap.entrySet();

			for (Map.Entry<String, String> entry : entrySet) {
				LOG.info("Cart Entry:" + entry.getKey() + " Fulfillment Type:"
						+ entry.getValue());
				if (!StringUtils.equalsIgnoreCase(entry.getValue(), "TSHIP")) {
					isCartTShipFulfillied = false;
					LOG.info("Fulfillment status of Cart Entry:"
							+ entry.getKey() + " is " + entry.getValue()
							+ ". Hence breaking the loop");
					break;
				}
			}*/
			for(AbstractOrderEntryModel cartEntry:cart.getEntries()){
				if(!mplFindDeliveryFulfillModeStrategy.isTShip(cartEntry.getSelectedUSSID())){
					isCartTShipFulfillied =  false;
					break;
				}
				return isCartTShipFulfillied;
			}
			
		} catch (Exception ex) {
			LOG.error("Exception in getCartFulFillMentType", ex);
			isCartTShipFulfillied = false;
		}
		LOG.info("Final Fulfillment Status:" + isCartTShipFulfillied);
		return isCartTShipFulfillied;

	}
	
	
	/**
	 * TIS-275 & TIS-276 Check if cart expired.
	 *
	 * @param widget
	 *            the widget
	 * @return true, if successful
	 */
	@Override
	public boolean checkIfCartExpired(
			final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget) {
		try {
			CartModel cart = (CartModel) widget.getWidgetController()
					.getBasketController().getCart().getObject();
			boolean isCartExpired = ((MarketplaceCsCheckoutService) getCsCheckoutService())
					.checkCartReservationStatus(cart);
			LOG.info("Bypass the pincode serviceability:"
					+ configurationService
							.getConfiguration()
							.getBoolean(
									MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS));

			if (configurationService
					.getConfiguration()
					.getBoolean(
							MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS,
							false)) {
				LOG.info("Bypassing all of the serviceability checks");
				return isCartExpired;
			} else {
				return isCartExpired && isCartCODEligible()
						&& getCartFulFillMentType();
			}
		} catch (Exception ex) {
			LOG.error("EtailNonBusinessExceptions in checkIfCartExpired()", ex);
			throw new EtailNonBusinessExceptions(ex);
		}

	}

	/**
	 * Check delivery mode cod limit.
	 *
	 * @param cart
	 *            the cart
	 * @param widget
	 *            the widget
	 * @return true if the limits are not exceeded TIS-276: Losgistic partner
	 *         COD limit check
	 */
	@Override
	public boolean checkDeliveryModeCODLimit(
			final CartModel cart,
			final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget) {

		boolean isCODLimitAvailable = true;
		try {
			if (configurationService
					.getConfiguration()
					.getBoolean(
							MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS,
							false)) {
				LOG.info("Bypassing all of the serviceability checks");
				return isCODLimitAvailable;
			} else {
				for (final AbstractOrderEntryModel cartEntry : cart
						.getEntries()) {
					final List<PinCodeResponseData> serviceabilityResponses = ((MarketplaceCheckoutController) widget
							.getWidgetController()).getResponseForPinCode(
							cartEntry.getProduct(), cart.getDeliveryAddress()
									.getPostalcode(),
							MarketplaceCockpitsConstants.NO, cartEntry
									.getSelectedUSSID());
					if (CollectionUtils.isNotEmpty(serviceabilityResponses)) {
						if (!checkCODEligibilityInServiceResponse(
								isCODLimitAvailable, cartEntry,
								serviceabilityResponses)) {
							break;
						}
					} else {
						LOG.info("There is no pincode response for the product="
								+ cartEntry.getProduct().getCode()
								+ ":"
								+ cartEntry.getProduct().getName());
					}
				}
			}
		} catch (Exception ex) {
			isCODLimitAvailable = false;
			LOG.error("Exception in fetching the response from OMS", ex);
			throw new EtailNonBusinessExceptions(ex);
		}

		return isCODLimitAvailable;
	}

	private boolean checkCODEligibilityInServiceResponse(
			boolean isCODLimitAvailable,
			final AbstractOrderEntryModel cartEntry,
			final List<PinCodeResponseData> serviceabilityResponses) {
		for (final PinCodeResponseData serviceabilityResponse : serviceabilityResponses) {
			if (StringUtils.equals(serviceabilityResponse.getUssid(),
					cartEntry.getSelectedUSSID())) {
				LOG.info("serviceabilityResponse.getUssid():"
						+ serviceabilityResponse.getUssid()
						+ " matches the cartEntry.getSelectedUSSID():"
						+ cartEntry.getSelectedUSSID());
				if (CollectionUtils.isNotEmpty(serviceabilityResponse
						.getValidDeliveryModes())) {
					isCODLimitAvailable = checkDeliveryDetails(
							isCODLimitAvailable, cartEntry,
							serviceabilityResponse);
					if (!isCODLimitAvailable) {
						break;
					}
				} else {
					LOG.info("There is no delivery detail in the pincode response for the product="
							+ cartEntry.getProduct().getCode()
							+ ":"
							+ cartEntry.getProduct().getName());
				}
			} else {
				LOG.info("serviceabilityResponse.getUssid():"
						+ serviceabilityResponse.getUssid());
				LOG.info("cartEntry.getSelectedUSSID():"
						+ cartEntry.getSelectedUSSID());
			}
		}
		return isCODLimitAvailable;
	}

	private boolean checkDeliveryDetails(boolean isCODLimitAvailable,
			final AbstractOrderEntryModel cartEntry,
			final PinCodeResponseData serviceabilityResponse) {
		for (final DeliveryDetailsData deliveryDetails : serviceabilityResponse
				.getValidDeliveryModes()) {
			/*
			 * if(deliveryDetails.getIsCODLimitFailed()) {
			 * isCODLimitAvailable=false;
			 * LOG.info("COD Limit Failed for product="
			 * +cartEntry.getProduct().getCode()
			 * +":"+cartEntry.getProduct().getName()); break; } else {
			 * LOG.info("COD limit is Yes for product="
			 * +cartEntry.getProduct().getCode()
			 * +":"+cartEntry.getProduct().getName()); }
			 */
		}
		return isCODLimitAvailable;
	}

	@Override
	public boolean processPayment(CartModel cart) throws PaymentException,
			ValidationException ,Exception{

		double unTotal = getCsCheckoutService().getUnauthorizedTotal(cart);

		unTotal = cart.getConvenienceCharges() == null ? unTotal : unTotal+cart
				.getConvenienceCharges();

		String cusName= null;
		cODPaymentService.getTransactionModel(cart, unTotal);
		if (StringUtils.isNotEmpty(cart.getUser().getName()) && !cart.getUser().getName().equalsIgnoreCase(" "))
		{
			cusName = cart.getUser().getName();

		}
		else
		{
			cusName = ((CustomerModel)cart.getUser()).getOriginalUid();
		}
		mplPaymentService.saveCODPaymentInfo(cusName, cart.getSubtotal(), cart.getConvenienceCharges(), cart.getEntries(),cart);
		getModelService().refresh(cart);
		return (true);
	}

	@Override
	/*     */public boolean processCODPayment()
	/*     */throws PaymentException, ValidationException
	/*     */{

		/* 379 */cODPaymentService.createCODPaymentInfo(getCartModel());
				 CommerceCartParameter cartParameter = new CommerceCartParameter();
					cartParameter.setCart(getCartModel());			
					ImpersonationContext context = createImpersonationContext(getCartModel());
					getImpersonationService()
							.executeInContext(
									context,
									new ImpersonationService.Executor<CartModel, ImpersonationService.Nothing>() {
										@Override
										public CartModel execute() {
											try {
												CommerceCartParameter cartParameter = new CommerceCartParameter();
												cartParameter.setCart(getCartModel());
												getCommerceCartService()
														.recalculateCart(
																cartParameter);
											} catch (CalculationException e) {
												LOG.error("Exception calculating cart ["
														+ getCartModel() + "]", e);
												throw new ClientEtailNonBusinessExceptions(e);
											}
											return null;
										}
									});
		/* 382 */return (true);
		/*     */}

	@Override
	/*     */public boolean removeCODPayment()
	/*     */throws PaymentException, ValidationException
	/*     */{
		/* 379 */cODPaymentService.removeCODPaymentInfo(getCartModel());
		 CommerceCartParameter cartParameter = new CommerceCartParameter();
			cartParameter.setCart(getCartModel());			
			ImpersonationContext context = createImpersonationContext(getCartModel());
			getImpersonationService()
					.executeInContext(
							context,
							new ImpersonationService.Executor<CartModel, ImpersonationService.Nothing>() {
								@Override
								public CartModel execute() {
									try {
										CommerceCartParameter cartParameter = new CommerceCartParameter();
										cartParameter.setCart(getCartModel());
										getCommerceCartService()
												.recalculateCart(
														cartParameter);
									} catch (CalculationException e) {
										LOG.error("Exception calculating cart ["
												+ getCartModel() + "]", e);
										throw new ClientEtailNonBusinessExceptions(e);
									}
									return null;
								}
							});
		/* 382 */return (true);
		/*     */}

	@Override
	/*     */public void checkCustomerStatus()
	/*     */throws ValidationException
	/*     */{
		/* 382 */((MarketplaceCsCheckoutService) getCsCheckoutService())
				.checkCustomerStatus(getCartModel());

		/*     */}

	@Override
	public List<MplZoneDeliveryModeValueModel> getDeliveryModesAndCost(
			String currency, String selectedUSSID) {
		List<MplZoneDeliveryModeValueModel> deliveryModes = deliveryCostService
				.getDeliveryModesAndCost(currency, selectedUSSID);
		return deliveryModes;
	}
	
	@Override
	public void setFreeBiDeliveryModesAndCost(
			AbstractOrderEntryModel entry,MplZoneDeliveryModeValueModel deliveryMode) {
		entry.setMplDeliveryMode(deliveryMode);
		getModelService().save(entry);
	}

	@Override
	public boolean validateWithOMS(TypedObject cartEntry,
			TypedObject deliveryMode) throws ValidationException {
		return ((MarketPlaceBasketController) getBasketController()).validateWithOMS(cartEntry, deliveryMode);
	}
	

}
