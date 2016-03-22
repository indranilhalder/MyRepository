/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.checkout.form.DeliveryMethodEntry;
import com.tisl.mpl.checkout.form.DeliveryMethodForm;
import com.tisl.mpl.constants.MarketplacecheckoutaddonConstants;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.coupon.facade.MplCouponFacade;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCartFacade;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;
import com.tisl.mpl.storefront.web.forms.validator.MplAddressValidator;
import com.tisl.mpl.util.ExceptionUtil;


@Controller
@RequestMapping(value = "/checkout/multi/delivery-method")
public class DeliveryMethodCheckoutStepController extends AbstractCheckoutStepController
{
	@Resource
	private MplCheckoutFacade mplCheckoutFacade;

	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;

	@Resource(name = "mplCartFacade")
	private MplCartFacade mplCartFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = ModelAttributetConstants.ACCELERATOR_CHECKOUT_FACADE)
	private CheckoutFacade checkoutFacade;

	@Autowired
	private AccountAddressFacade accountAddressFacade;

	@Autowired
	private MplAddressValidator mplAddressValidator;

	@Autowired
	private CartService cartService;

	@Autowired
	private CommerceCartService commerceCartService;

	@Autowired
	private Converter<CartModel, CartData> mplExtendedCartConverter;

	@Autowired
	private MplCouponFacade mplCouponFacade;

	private final String checkoutPageName = "Choose Your Delivery Options";

	private final String checkoutPageName1 = "New Address";
	private final String selectAddress = "Select Address";

	private static final Logger LOG = Logger.getLogger(DeliveryMethodCheckoutStepController.class);

	@RequestMapping(value = MarketplacecheckoutaddonConstants.CHOOSEVALUE, method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	@PreValidateCheckoutStep(checkoutStep = MarketplacecheckoutaddonConstants.DELIVERY_METHOD)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes)
	{
		Map<String, String> fullfillmentDataMap = new HashMap<String, String>();
		Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
		List<PinCodeResponseData> responseData = null;
		String returnPage = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				return getCheckoutStep().previousStep();
			}

			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			//TISBOX-1618
			applyPromotions();
			getMplCartFacade().removeDeliveryMode(getCartService().getSessionCart());
			getMplCouponFacade().releaseVoucherInCheckout(getCartService().getSessionCart());
			final CartData cartData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			final String defaultPinCodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);

			timeOutSet(model);
			if (StringUtils.isNotEmpty(defaultPinCodeId) && (cartData.getEntries() != null && !cartData.getEntries().isEmpty()))
			{
				responseData = getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartData);
				deliveryModeDataMap = getMplCartFacade().getDeliveryMode(cartData, responseData);
				fullfillmentDataMap = getMplCartFacade().getFullfillmentMode(cartData);

				//TIS-397
				deliveryModeDataMap = mplCheckoutFacade.repopulateTshipDeliveryCost(deliveryModeDataMap, cartData);

				model.addAttribute("deliveryModeData", deliveryModeDataMap);
				model.addAttribute("deliveryMethodForm", new DeliveryMethodForm());
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.FULFILLMENTDATA, fullfillmentDataMap);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.TRUE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
				model.addAttribute("defaultPincode", defaultPinCodeId);

				this.prepareDataForPage(model);
				storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				model.addAttribute(
						WebConstants.BREADCRUMBS_KEY,
						getResourceBreadcrumbBuilder().getBreadcrumbs(
								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				model.addAttribute("metaRobots", "noindex,nofollow");
				setCheckoutStepLinksForModel(model, getCheckoutStep());
			}

			returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}

		catch (final CMSItemNotFoundException e)
		{
			LOG.error(" CMSItemNotFoundException while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			LOG.error("EtailNonBusinessExceptions  while  delivery mode show enter step ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		model.addAttribute("checkoutPageName", checkoutPageName);
		return returnPage;
	}


	/**
	 * This method gets called when the "Use Selected Delivery Method" button is clicked. It sets the selected delivery
	 * mode on the checkout facade and reloads the page highlighting the selected delivery Mode.
	 *
	 * @param model
	 *           - the id of the delivery mode
	 * @param deliveryMethodForm
	 * @param bindingResult
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTURL)
	@RequireHardLogIn
	public String doSelectDeliveryMode(final DeliveryMethodForm deliveryMethodForm, final BindingResult bindingResult,
			final Model model) throws CMSItemNotFoundException
	{
		Double finalDeliveryCost = Double.valueOf(0.0);
		String returnPage = MarketplacecommerceservicesConstants.EMPTY;

		try
		{
			if (getUserFacade().isAnonymousUser())
			{
				return getCheckoutStep().previousStep();
			}

			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			if (deliveryMethodForm.getDeliveryMethodEntry() != null && !deliveryMethodForm.getDeliveryMethodEntry().isEmpty())
			{
				for (final DeliveryMethodEntry deliveryEntry : deliveryMethodForm.getDeliveryMethodEntry())
				{
					final String deliveryCode = deliveryEntry.getDeliveryCode();
					if (StringUtils.isNotEmpty(deliveryCode))
					{
						final Double deliveryCost = getMplCustomAddressFacade().populateDeliveryMethodData(deliveryCode,
								deliveryEntry.getSellerArticleSKU());

						finalDeliveryCost = Double.valueOf(finalDeliveryCost.doubleValue() + deliveryCost.doubleValue());
					}
				}
			}

			LOG.debug(">>>>>>>>>>  Step 1 :Delivery cost before applying any promotion :  " + finalDeliveryCost);


			final CartModel cartModel = getCartService().getSessionCart();
			final Map<String, MplZoneDeliveryModeValueModel> freebieModelMap = new HashMap<String, MplZoneDeliveryModeValueModel>();
			final Map<String, Long> freebieParentQtyMap = new HashMap<String, Long>();

			if (cartModel != null && cartModel.getEntries() != null)
			{
				for (final AbstractOrderEntryModel cartEntryModel : cartModel.getEntries())
				{
					if (cartEntryModel != null && !cartEntryModel.getGiveAway().booleanValue()
							&& cartEntryModel.getSelectedUSSID() != null)
					{
						freebieModelMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getMplDeliveryMode());
						freebieParentQtyMap.put(cartEntryModel.getSelectedUSSID(), cartEntryModel.getQuantity());
					}
				}
			}

			applyPromotions();

			getMplCheckoutFacade().saveDeliveryMethForFreebie(cartModel, freebieModelMap, freebieParentQtyMap);

			LOG.debug(">>>>>>>>>>  Step 2  :Freebie data preparation ");

			timeOutSet(model);

			final Map<String, Map<String, Double>> deliveryChargePromotionMap = null;
			final boolean calculationStatus = getMplCheckoutFacade().populateDeliveryCost(finalDeliveryCost,
					deliveryChargePromotionMap); //TIS 400

			LOG.debug(">>>>>>>>>>  Step 3:   Delivery cost final calculation status  " + calculationStatus);

			/*** Inventory Soft Reservation Start ***/

			final boolean inventoryReservationStatus = getMplCartFacade().isInventoryReserved(
					MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_TYPE_CART);
			if (!inventoryReservationStatus)
			{
				getSessionService().setAttribute(MarketplacecclientservicesConstants.OMS_INVENTORY_RESV_SESSION_ID, "TRUE");
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}
			/*** Inventory Soft Reservation Start ***/

			LOG.debug(">>>>>>>>>>  Step 4:  Inventory soft reservation status  " + inventoryReservationStatus);


			Map<String, List<MarketplaceDeliveryModeData>> deliveryModeDataMap = new HashMap<String, List<MarketplaceDeliveryModeData>>();
			List<PinCodeResponseData> responseData = null;
			final CartData cartUssidData = getMplCartFacade().getSessionCartWithEntryOrdering(true);
			final String defaultPinCodeId = getSessionService().getAttribute(MarketplacecommerceservicesConstants.SESSION_PINCODE);


			if (StringUtils.isNotEmpty(defaultPinCodeId)
					&& (cartUssidData.getEntries() != null && !cartUssidData.getEntries().isEmpty()))
			{
				responseData = getMplCartFacade().getOMSPincodeResponseData(defaultPinCodeId, cartUssidData);
				deliveryModeDataMap = getMplCartFacade().getDeliveryMode(cartUssidData, responseData);

				getMplCartFacade().setDeliveryDate(cartUssidData, responseData);
				//TISUTO-72 TISST-6994,TISST-6990 set cart COD eligible
				final boolean isCOdEligible = getMplCartFacade().addCartCodEligible(deliveryModeDataMap, responseData);
				LOG.info("isCOdEligible " + isCOdEligible);
			}

			LOG.debug(">>>>>>>>>>  Step 5:  Cod status setting done  ");

			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			//TIS 391:  In case of normal checkout , delivery address will be null and for express checkout delivery address will be ore selected from the cart page
			// In case of express checkout it will redirect to the payment page from delivery mode selection
			if (cartData.getDeliveryAddress() != null)
			{
				LOG.debug("Express checkout selected for address id : " + cartData.getDeliveryAddress().getId());
				return getCheckoutStep().nextStep();
			}


			List<AddressData> deliveryAddress = (List<AddressData>) getMplCustomAddressFacade().getDeliveryAddresses(
					cartData.getDeliveryAddress());
			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;
			deliveryAddress = getMplCheckoutFacade().rePopulateDeliveryAddress(deliveryAddress);

			//TISST-7473
			if (deliveryAddress == null || deliveryAddress.isEmpty())
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.NEWADDRESSLINK;
			}
			LOG.debug("Before final model attribute level set ");

			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSES, deliveryAddress);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
					Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWSAVEDTOADDRESSBOOK, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error("CMSItemNotFoundException  while  selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e,
					MarketplacecommerceservicesConstants.E0000));
			LOG.error("Exception  while selecting delivery mode ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}

		return returnPage;
	}

	/**
	 *
	 * @param model
	 *           - the id of the delivery mode
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLSELECTSAVEDADDRESS, method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectSavedAddress(final Model model)
	{

		String returnPage = MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			List<AddressData> deliveryAddress = (List<AddressData>) getMplCustomAddressFacade().getDeliveryAddresses(
					cartData.getDeliveryAddress());
			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;
			deliveryAddress = getMplCheckoutFacade().rePopulateDeliveryAddress(deliveryAddress);

			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);

			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSES, deliveryAddress);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
					Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWSAVEDTOADDRESSBOOK, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			timeOutSet(model);
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());
		}
		catch (final CMSItemNotFoundException cmsEx)
		{
			LOG.error("CMSItemNotFoundException  while  selecting saved address ", cmsEx);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  selecting saved address ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting saved address ", e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while showing saved address :" + e);
			getSessionService().setAttribute(MarketplacecclientservicesConstants.DELIVERY_MODE_ENTER_STEP_ERROR_ID, "TRUE");
			returnPage = MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
		}
		model.addAttribute("checkoutPageName", selectAddress);
		return returnPage;
	}

	/**
	 * @description method is called while editing an address
	 * @param model
	 * @param addressCode
	 * @throws CMSItemNotFoundException
	 * @return String
	 */

	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_ADDRESS
			+ MarketplacecheckoutaddonConstants.ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.GET)
	@RequireHardLogIn
	public String editAddress(@PathVariable(ModelAttributetConstants.ADDRESS_CODE) final String addressCode, final Model model)
			throws CMSItemNotFoundException
	{

		List<StateData> stateDataList = new ArrayList<StateData>();
		final AccountAddressForm addressForm = new AccountAddressForm();
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(ModelAttributetConstants.COUNTRY_DATA, checkoutFacade.getDeliveryCountries());
			model.addAttribute(ModelAttributetConstants.TITLE_DATA, userFacade.getTitles());
			model.addAttribute(ModelAttributetConstants.ADDRESS_FORM, addressForm);
			model.addAttribute(ModelAttributetConstants.ADDRESS_BOOK_EMPTY, Boolean.valueOf(userFacade.isAddressBookEmpty()));
			timeOutSet(model);
			stateDataList = accountAddressFacade.getStates();
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);

			for (final AddressData addressData : accountAddressFacade.getAddressBook())
			{
				if (null != addressData && null != addressData.getId() && addressData.getId().equals(addressCode))
				{
					model.addAttribute(ModelAttributetConstants.REGIONS,
							getI18NFacade().getRegionsForCountryIso(addressData.getCountry().getIsocode()));
					model.addAttribute(ModelAttributetConstants.COUNTRY, addressData.getCountry().getIsocode());
					model.addAttribute(ModelAttributetConstants.ADDRESS_DATA, addressData);
					addressForm.setAddressId(addressData.getId());
					addressForm.setFirstName(addressData.getFirstName());
					addressForm.setLastName(addressData.getLastName());
					addressForm.setLine1(addressData.getLine1());
					addressForm.setLine2(addressData.getLine2());
					addressForm.setTownCity(addressData.getTown());
					addressForm.setPostcode(addressData.getPostalCode());
					addressForm.setCountryIso(addressData.getCountry().getIsocode());
					addressForm.setAddressType(addressData.getAddressType());
					addressForm.setMobileNo(addressData.getPhone());
					addressForm.setState(addressData.getState());
					addressForm.setLine3(addressData.getLine3());
					addressForm.setLocality(addressData.getLocality());

					if (addressData.getRegion() != null && !StringUtils.isEmpty(addressData.getRegion().getIsocode()))
					{
						addressForm.setRegionIso(addressData.getRegion().getIsocode());
					}

					if (isDefaultAddress(addressData.getId()))
					{
						addressForm.setDefaultAddress(Boolean.TRUE);
						model.addAttribute(ModelAttributetConstants.IS_DEFAULT_ADDRESS, Boolean.TRUE);
					}
					else
					{
						addressForm.setDefaultAddress(Boolean.FALSE);
						model.addAttribute(ModelAttributetConstants.IS_DEFAULT_ADDRESS, Boolean.FALSE);
					}
					break;
				}
			}

			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			model.addAttribute(ModelAttributetConstants.EDIT, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS, Boolean.TRUE);

			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());

		}
		catch (final Exception e)
		{
			this.prepareDataForPage(model);
			model.addAttribute("addressForm", addressForm);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
					Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			GlobalMessages.addErrorMessage(model, "address.error.formentry.invalid");
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);
			return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
		}
		model.addAttribute("checkoutPageName", checkoutPageName1);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
	}

	/**
	 * @description method is called after selecting delivery mode and forward to address
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYNEWADDRESSURL, method = RequestMethod.GET)
	@RequireHardLogIn
	public String addNewAddress(final Model model) throws CMSItemNotFoundException
	{
		String addressFlag = "F";
		try
		{
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			final AccountAddressForm addressForm = new AccountAddressForm();
			addressForm.setCountryIso(MarketplacecheckoutaddonConstants.COUNTRYISO);
			final List<StateData> stateDataList = accountAddressFacade.getStates();
			List<AddressData> deliveryAddress = (List<AddressData>) getMplCustomAddressFacade().getDeliveryAddresses(
					cartData.getDeliveryAddress());
			deliveryAddress = (deliveryAddress == null || deliveryAddress.isEmpty()) ? accountAddressFacade.getAddressBook()
					: deliveryAddress;

			final CartModel cartModel = getCartService().getSessionCart();

			for (final AddressModel userAddress : cartModel.getUser().getAddresses())
			{
				if (userAddress != null && userAddress.getVisibleInAddressBook() != null
						&& userAddress.getVisibleInAddressBook().booleanValue() == true)
				{
					addressFlag = "T";
				}
			}

			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFLAG, addressFlag);

			model.addAttribute(MarketplacecheckoutaddonConstants.DELIVERYADDRESSES, deliveryAddress);
			model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWSAVEDTOADDRESSBOOK, Boolean.TRUE);
			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.TRUE);
			model.addAttribute(ModelAttributetConstants.EDIT, Boolean.FALSE);
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS, Boolean.TRUE);
			timeOutSet(model);
			this.prepareDataForPage(model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			model.addAttribute(
					WebConstants.BREADCRUMBS_KEY,
					getResourceBreadcrumbBuilder().getBreadcrumbs(
							MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
			model.addAttribute("metaRobots", "noindex,nofollow");
			setCheckoutStepLinksForModel(model, getCheckoutStep());

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  adding new address ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while adding new address ", e);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception occured while adding new address:", ex);
		}
		model.addAttribute("checkoutPageName", checkoutPageName1);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
	}

	/**
	 * @description method is called to select address from list and forward to payment step
	 * @param selectedAddressCode
	 * @throws CMSItemNotFoundException
	 * @return String
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYSELECTADDRESSURL, method = RequestMethod.GET)
	@RequireHardLogIn
	public String selectAddress(@RequestParam("selectedAddressCode") final String selectedAddressCode)
			throws CMSItemNotFoundException
	{
		try
		{
			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
			AddressData finaladdressData = new AddressData();
			for (final AddressData addressData : accountAddressFacade.getAddressBook())
			{
				if (null != addressData && null != addressData.getId() && addressData.getId().equals(selectedAddressCode))
				{
					finaladdressData = addressData;
					break;
				}
			}

			final String selectedPincode = finaladdressData.getPostalCode();

			//TISSEC-11
			final String regex = "\\d{6}";

			if (selectedPincode.matches(regex))
			{
				//TISUTO-12 , TISUTO-11
				final String redirectURL = getMplCartFacade().checkPincodeAndInventory(selectedPincode);
				if (redirectURL.trim().length() > 0)
				{
					return redirectURL;
				}
				getMplCustomAddressFacade().setDeliveryAddress(finaladdressData);

				// Recalculating Cart Model
				LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
				getMplCheckoutFacade().reCalculateCart(cartData);
			}
			else
			{
				return MarketplacecommerceservicesConstants.REDIRECT + MarketplacecommerceservicesConstants.CART;
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while selecting address ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while selecting address ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while selecting  address:" + e);
		}
		return getCheckoutStep().nextStep();
	}

	/**
	 * @description method is called to add new address while checkout and forward to payment step
	 * @param addressForm
	 * @param bindingResult
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @return String
	 */

	@RequestMapping(value = MarketplacecheckoutaddonConstants.MPLDELIVERYNEWADDRESSURL, method = RequestMethod.POST)
	@RequireHardLogIn
	public String add(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			final String errorMsg = mplAddressValidator.validate(addressForm);
			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			if ((!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
					|| bindingResult.hasErrors())
			{
				this.prepareDataForPage(model);
				model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
				model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
						Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
				model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
				GlobalMessages.addErrorMessage(model, errorMsg);
				storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				model.addAttribute(
						WebConstants.BREADCRUMBS_KEY,
						getResourceBreadcrumbBuilder().getBreadcrumbs(
								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				setCheckoutStepLinksForModel(model, getCheckoutStep());
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.TRUE);
				timeOutSet(model);
				return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
			}

			if (StringUtils.isNotBlank(addressForm.getCountryIso()))
			{
				model.addAttribute(ModelAttributetConstants.REGIONS,
						getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
				model.addAttribute(ModelAttributetConstants.COUNTRY, addressForm.getCountryIso());
			}

			final AddressData newAddress = new AddressData();
			newAddress.setTitleCode(addressForm.getTitleCode());
			newAddress.setFirstName(addressForm.getFirstName());
			newAddress.setLastName(addressForm.getLastName());
			newAddress.setLine1(addressForm.getLine1());
			newAddress.setLine2(addressForm.getLine2());
			newAddress.setTown(addressForm.getTownCity());
			newAddress.setPostalCode(addressForm.getPostcode());
			newAddress.setBillingAddress(false);
			newAddress.setShippingAddress(true);
			newAddress.setAddressType(addressForm.getAddressType());
			newAddress.setState(addressForm.getState());
			newAddress.setPhone(addressForm.getMobileNo());
			newAddress.setLine3(addressForm.getLine3());
			newAddress.setLocality(addressForm.getLocality());
			if (StringUtils.isNotEmpty(addressForm.getCountryIso()))
			{
				final CountryData countryData = getI18NFacade().getCountryForIsocode(addressForm.getCountryIso());
				newAddress.setCountry(countryData);
			}
			if (StringUtils.isNotEmpty(addressForm.getRegionIso()))
			{
				final RegionData regionData = getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso());
				newAddress.setRegion(regionData);
			}
			if (addressForm.getSaveInAddressBook() != null)
			{
				newAddress.setVisibleInAddressBook(addressForm.getSaveInAddressBook().booleanValue());
				if (addressForm.getSaveInAddressBook().booleanValue() && getUserFacade().isAddressBookEmpty())
				{
					newAddress.setDefaultAddress(true);
				}
			}
			else if (getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				newAddress.setDefaultAddress(true);
				newAddress.setVisibleInAddressBook(true);
			}
			accountAddressFacade.addaddress(newAddress);
			getMplCustomAddressFacade().setDeliveryAddress(newAddress);
			//TISUTO-12 , TISUTO-11
			final String redirectURL = getMplCartFacade().checkPincodeAndInventory(addressForm.getPostcode());
			if (redirectURL.trim().length() > 0)
			{
				return redirectURL;
			}

			//Recalculating Cart Model
			LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
			getMplCheckoutFacade().reCalculateCart(cartData);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while  saving new address ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while saving new address ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while saving new address :", e);
		}
		return getCheckoutStep().nextStep();
	}

	/**
	 * @description method is called to edit address and forward to payment step
	 * @param addressForm
	 * @param bindingResult
	 * @param model
	 * @throws CMSItemNotFoundException
	 * @return String
	 */

	@RequestMapping(value = RequestMappingUrlConstants.LINK_EDIT_ADDRESS
			+ MarketplacecheckoutaddonConstants.ADDRESS_CODE_PATH_VARIABLE_PATTERN, method = RequestMethod.POST)
	@RequireHardLogIn
	public String edit(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model)
			throws CMSItemNotFoundException
	{
		try
		{
			//TISST-13012
			final boolean cartItemDelistedStatus = getMplCartFacade().isCartEntryDelisted(getCartService().getSessionCart());
			if (cartItemDelistedStatus)
			{
				return MarketplacecheckoutaddonConstants.REDIRECT + MarketplacecheckoutaddonConstants.CART;
			}

			final List<StateData> stateDataList = accountAddressFacade.getStates();
			final String errorMsg = mplAddressValidator.validate(addressForm);
			final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

			if ((!StringUtils.isEmpty(errorMsg) && !errorMsg.equalsIgnoreCase(ModelAttributetConstants.SUCCESS))
					|| bindingResult.hasErrors())
			{
				this.prepareDataForPage(model);
				model.addAttribute(MarketplacecheckoutaddonConstants.CARTDATA, cartData);
				model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSFORM, addressForm);
				model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);
				model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
				model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
						Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
				GlobalMessages.addErrorMessage(model, errorMsg);
				storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
				model.addAttribute(
						WebConstants.BREADCRUMBS_KEY,
						getResourceBreadcrumbBuilder().getBreadcrumbs(
								MarketplacecheckoutaddonConstants.CHECKOUT_MULTI_DELIVERYMETHOD_BREADCRUMB));
				setCheckoutStepLinksForModel(model, getCheckoutStep());
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWDELIVERYMETHOD, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDRESS, Boolean.FALSE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWEDITADDRESS, Boolean.TRUE);
				model.addAttribute(MarketplacecheckoutaddonConstants.SHOWADDADDRESS, Boolean.FALSE);

				return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
			}

			model.addAttribute(MarketplacecheckoutaddonConstants.ADDRESSTYPE, getMplCheckoutFacade().getAddressType());
			model.addAttribute(ModelAttributetConstants.STATE_DATA_LIST, stateDataList);

			if (StringUtils.isNotBlank(addressForm.getCountryIso()))
			{
				model.addAttribute(ModelAttributetConstants.REGIONS,
						getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
				model.addAttribute(ModelAttributetConstants.COUNTRY, addressForm.getCountryIso());
			}
			model.addAttribute(MarketplacecheckoutaddonConstants.NOADDRESS,
					Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));

			final AddressData newAddress = new AddressData();
			newAddress.setId(addressForm.getAddressId());
			newAddress.setTitleCode(addressForm.getTitleCode());
			newAddress.setFirstName(addressForm.getFirstName());
			newAddress.setLastName(addressForm.getLastName());
			newAddress.setLine1(addressForm.getLine1());
			newAddress.setLine2(addressForm.getLine2());
			newAddress.setTown(addressForm.getTownCity());
			newAddress.setPostalCode(addressForm.getPostcode());
			newAddress.setPhone(addressForm.getMobileNo());
			newAddress.setBillingAddress(false);
			newAddress.setShippingAddress(true);
			newAddress.setAddressType(addressForm.getAddressType());
			newAddress.setLine3(addressForm.getLine3());
			newAddress.setLocality(addressForm.getLocality());
			newAddress.setState(addressForm.getState());

			if (StringUtils.isNotEmpty(addressForm.getCountryIso()))
			{
				final CountryData countryData = getI18NFacade().getCountryForIsocode(addressForm.getCountryIso());
				newAddress.setCountry(countryData);
			}
			if (StringUtils.isNotEmpty(addressForm.getRegionIso()))
			{
				final RegionData regionData = getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso());
				newAddress.setRegion(regionData);
			}

			if (addressForm.getSaveInAddressBook() == null)
			{
				newAddress.setVisibleInAddressBook(true);
			}
			else
			{
				newAddress.setVisibleInAddressBook(Boolean.TRUE.equals(addressForm.getSaveInAddressBook()));
			}

			//TISUTO-12 , TISUTO-11 ,TISUTO 106
			final String redirectURL = getMplCartFacade().checkPincodeAndInventory(addressForm.getPostcode());
			if (redirectURL.trim().length() > 0)
			{
				return redirectURL;
			}


			newAddress.setDefaultAddress(getUserFacade().isAddressBookEmpty() || getUserFacade().getAddressBook().size() == 1
					|| Boolean.TRUE.equals(addressForm.getDefaultAddress()));
			accountAddressFacade.editAddress(newAddress);

			//getCheckoutFacade().setDeliveryAddress(newAddress);
			getMplCustomAddressFacade().setDeliveryAddress(newAddress);

			//Recalculating Cart Model
			LOG.debug(">> Delivery cost " + cartData.getDeliveryCost().getValue());
			getMplCheckoutFacade().reCalculateCart(cartData);

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions  while editing address ", e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
			LOG.error("EtailNonBusinessExceptions  while editing address ", e);
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured while editing address :", e);
		}
		return getCheckoutStep().nextStep();

	}

	/**
	 * This is a GET method which helps in resetting the convenience charges whenever it is not required
	 *
	 * @return String
	 * @throws NoSuchAlgorithmException
	 * @throws CalculationException
	 */
	@RequestMapping(value = MarketplacecheckoutaddonConstants.CALCULATEDELIVERYCOST, method = RequestMethod.POST)
	@RequireHardLogIn
	public @ResponseBody String calculateDeliveryCost(
			@PathVariable(MarketplacecheckoutaddonConstants.DELIVERYCOST) final String deliveryCost)
			throws NoSuchAlgorithmException, CalculationException
	{

		LOG.info("deliveryCost " + deliveryCost);
		//TISST-13010
		String totalPriceFormatted = MarketplacecommerceservicesConstants.EMPTY;
		String formatDeliveryCost = MarketplacecommerceservicesConstants.EMPTY;

		try
		{
			Double discountValue = Double.valueOf(0.0);
			getMplCartFacade().removeDeliveryMode(getCartService().getSessionCart());

			final Double subTotal = getCartService().getSessionCart().getSubtotal();

			final CartModel cartModel = getCartService().getSessionCart();
			final CartData cartData = mplExtendedCartConverter.convert(cartModel);

			if (cartData.getTotalDiscounts() != null && cartData.getTotalDiscounts().getValue() != null)
			{
				discountValue = Double.valueOf(cartData.getTotalDiscounts().getValue().doubleValue());
			}
			LOG.info("discountValue  " + discountValue);
			final Double totalPriceAfterDeliveryCharge = Double.valueOf(subTotal.doubleValue() + Double.parseDouble(deliveryCost)
					- discountValue.doubleValue());
			final DecimalFormat df = new DecimalFormat("#.00");
			totalPriceFormatted = df.format(totalPriceAfterDeliveryCharge);
			formatDeliveryCost = df.format(Double.valueOf(deliveryCost));
		}
		catch (final EtailNonBusinessExceptions ex)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(ex);
			LOG.error("EtailNonBusinessExceptions in calculateDeliveryCost ", ex);
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			LOG.error("EtailBusinessExceptions in calculateDeliveryCost ", e);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in calculateDeliveryCost ", ex);
		}

		return getCartService().getSessionCart().getCurrency().getSymbol() + MarketplacecheckoutaddonConstants.STRINGSEPARATOR
				+ formatDeliveryCost + MarketplacecheckoutaddonConstants.STRINGSEPARATOR + totalPriceFormatted;
	}

	/**
	 * @Method checks if address is set as default
	 *
	 * @param addressId
	 *           - identifier for address to check
	 * @return true if address is default, false if address is not default
	 */
	protected boolean isDefaultAddress(final String addressId)
	{
		final AddressData defaultAddress = accountAddressFacade.getDefaultAddress();
		return (defaultAddress != null && defaultAddress.getId() != null && defaultAddress.getId().equals(addressId));
	}

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(MarketplacecheckoutaddonConstants.DELIVERY_METHOD);
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
	 * @return the mplCheckoutFacade
	 */
	public MplCheckoutFacade getMplCheckoutFacade()
	{
		return mplCheckoutFacade;
	}

	/**
	 * @param mplCheckoutFacade
	 *           the mplCheckoutFacade to set
	 */
	public void setMplCheckoutFacade(final MplCheckoutFacade mplCheckoutFacade)
	{
		this.mplCheckoutFacade = mplCheckoutFacade;
	}

	private void applyPromotions()
	{
		final CartModel cart = getCartService().getSessionCart();
		//recalculating cart
		try
		{
			commerceCartService.recalculateCart(cart);
		}
		catch (final CalculationException e)
		{
			LOG.error(" Exception in applyPromotions due to ", e);
		}
		catch (final Exception ex)
		{
			LOG.error("Exception in  applyPromotions", ex);
		}
	}

	/**
	 * @param model
	 */
	private void timeOutSet(final Model model)
	{
		// YTODO Auto-generated method stub
		final double deliveryMode = Double.parseDouble(configurationService.getConfiguration().getString(
				"checkout.deliverymode.timeout"));
		final String timeOut = Double.toString(deliveryMode * 60 * 1000);
		model.addAttribute(MarketplacecheckoutaddonConstants.TIMEOUT, timeOut);
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


	/**
	 * @return the mplCouponFacade
	 */
	public MplCouponFacade getMplCouponFacade()
	{
		return mplCouponFacade;
	}


	/**
	 * @param mplCouponFacade
	 *           the mplCouponFacade to set
	 */
	public void setMplCouponFacade(final MplCouponFacade mplCouponFacade)
	{
		this.mplCouponFacade = mplCouponFacade;
	}



}
