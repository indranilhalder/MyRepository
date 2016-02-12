package com.tisl.mpl.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tisl.mpl.controllers.MarketplacecheckoutaddonControllerConstants;
import com.tisl.mpl.core.enums.AddressType;
import com.tisl.mpl.data.AddressTypeData;
import com.tisl.mpl.facade.checkout.MplCustomAddressFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.storefront.web.forms.AccountAddressForm;
import com.tisl.mpl.storefront.web.forms.validator.AccountAddressValidator;


@Controller
@RequestMapping(value = "/checkout/multi/delivery-address")
public class DeliveryAddressCheckoutStepController extends AbstractCheckoutStepController
{
	protected static final Logger LOG = Logger.getLogger(DeliveryAddressCheckoutStepController.class);
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	@Autowired
	private MplEnumerationHelper mplEnumerationHelper;
	@Resource(name = "accountAddressValidator")
	private AccountAddressValidator accountAddressValidator;

	@Resource(name = "mplCustomAddressFacade")
	private MplCustomAddressFacade mplCustomAddressFacade;

	private final static String DELIVERY_ADDRESS = "delivery-address";

	private final String checkoutPageName = "Shipping Address";

	@Override
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@RequireHardLogIn
	@PreValidateCheckoutStep(checkoutStep = DELIVERY_ADDRESS)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		LOG.info("!!!!!!!!!!!!!!!!!inside add get address method of checkout!!!!!!!!!!!!!");
		getCheckoutFacade().setDeliveryAddressIfAvailable();

		//final CartData cartData = getCheckoutFacade().getCheckoutCart();      //DSC_006: Commented for Address state field addition
		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();

		model.addAttribute("cartData", cartData);
		//model.addAttribute("deliveryAddresses", getDeliveryAddresses(cartData.getDeliveryAddress())); //DSC_006: Commented for Address state field addition
		model.addAttribute("deliveryAddresses", getMplCustomAddressFacade().getDeliveryAddresses(cartData.getDeliveryAddress()));

		model.addAttribute("noAddress", Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
		final AccountAddressForm addressForm = new AccountAddressForm();
		addressForm.setCountryIso("IN");
		model.addAttribute("addressForm", addressForm);
		//model.addAttribute("addressForm", new AccountAddressForm());
		model.addAttribute("showSaveToAddressBook", Boolean.TRUE);
		prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryAddress.breadcrumb"));
		model.addAttribute("metaRobots", "noindex,nofollow");
		setCheckoutStepLinksForModel(model, getCheckoutStep());


		//pratik
		model.addAttribute("addressType", getAddressType());
		//pratik
		model.addAttribute("checkoutPageName", checkoutPageName);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.AddEditDeliveryAddressPage;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequireHardLogIn
	public String add(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model)
			throws CMSItemNotFoundException
	{

		//pratik
		model.addAttribute("addressType", getAddressType());
		//pratik

		LOG.info("!!!!!!!!!!!!!!!!!inside add post address method of checkout!!!!!!!!!!!!!");
		accountAddressValidator.validate(addressForm, bindingResult);

		//final CartData cartData = getCheckoutFacade().getCheckoutCart();  											// DSC_006:Commented for Address state field addition
		//model.addAttribute("cartData", cartData);																			// Commented for Address state field addition
		//model.addAttribute("deliveryAddresses", getDeliveryAddresses(cartData.getDeliveryAddress()));		// DSC_006:Commented for Address state field addition

		final CartData cartData = getMplCustomAddressFacade().getCheckoutCart();
		model.addAttribute("cartData", cartData);
		model.addAttribute("deliveryAddresses", getMplCustomAddressFacade().getDeliveryAddresses(cartData.getDeliveryAddress()));

		this.prepareDataForPage(model);
		if (StringUtils.isNotBlank(addressForm.getCountryIso()))
		{
			model.addAttribute("regions", getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
			model.addAttribute("country", addressForm.getCountryIso());
		}

		model.addAttribute("noAddress", Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
		model.addAttribute("showSaveToAddressBook", Boolean.TRUE);

		if (bindingResult.hasErrors())
		{
			model.addAttribute("addressForm", new AccountAddressForm());
			GlobalMessages.addErrorMessage(model, "address.error.formentry.invalid");
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setCheckoutStepLinksForModel(model, getCheckoutStep());
			return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.AddEditDeliveryAddressPage;
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
		if (addressForm.getCountryIso() != null)
		{
			final CountryData countryData = getI18NFacade().getCountryForIsocode(addressForm.getCountryIso());
			newAddress.setCountry(countryData);
		}
		if (addressForm.getRegionIso() != null && !StringUtils.isEmpty(addressForm.getRegionIso()))
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

		// Verify the address data.
		/*
		 * final AddressVerificationResult<AddressVerificationDecision> verificationResult =
		 * getAddressVerificationFacade() .verifyAddressData(newAddress); final boolean addressRequiresReview =
		 * getAddressVerificationResultHandler().handleResult(verificationResult, newAddress, model, redirectModel,
		 * bindingResult, getAddressVerificationFacade().isCustomerAllowedToIgnoreAddressSuggestions(),
		 * "checkout.multi.address.updated");
		 *
		 * if (addressRequiresReview) { storeCmsPageInModel(model,
		 * getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL)); setUpMetaDataForContentPage(model,
		 * getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL)); return
		 * ControllerConstants.Views.Pages.MultiStepCheckout.AddEditDeliveryAddressPage; }
		 */
		accountAddressFacade.addaddress(newAddress);

		final AddressData previousSelectedAddress = getCheckoutFacade().getCheckoutCart().getDeliveryAddress();
		// Set the new address as the selected checkout delivery address
		getCheckoutFacade().setDeliveryAddress(newAddress);
		if (previousSelectedAddress != null && !previousSelectedAddress.isVisibleInAddressBook())
		{ // temporary address should be removed
			getUserFacade().removeAddress(previousSelectedAddress);
		}

		// Set the new address as the selected checkout delivery address
		getCheckoutFacade().setDeliveryAddress(newAddress);

		return getCheckoutStep().nextStep();
	}

	/**
	 * @param cartData
	 * @return CartSoftReservationData
	 */
	/*
	 * private List<CartSoftReservationData> populateDataForSoftReservation(final CartData cartData) {
	 * CartSoftReservationData cartSoftReservationData;
	 *
	 * final List<CartSoftReservationData> cartSoftReservationDataList = new ArrayList<CartSoftReservationData>(); for
	 * (final OrderEntryData entry : cartData.getEntries()) { cartSoftReservationData = new CartSoftReservationData();
	 * cartSoftReservationData.setUSSID(entry.getSelectedSellerInformation().getUssid());
	 * cartSoftReservationData.setPincode(cartData.getDeliveryAddress().getPostalCode());
	 * cartSoftReservationData.setQuantity(entry.getQuantity().toString());
	 * cartSoftReservationData.setProductSize(entry.getProduct().getSize());
	 * cartSoftReservationData.setPrice(Double.parseDouble(entry.getProduct().getPrice().getValue().toString()));
	 * cartSoftReservationData.setIsCOD("true");
	 * cartSoftReservationData.setTransportMode(entry.getDeliveryMode().getName());
	 * cartSoftReservationData.setFulfillmentType("");
	 *
	 * cartSoftReservationDataList.add(cartSoftReservationData); } return cartSoftReservationDataList; }
	 */


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	@RequireHardLogIn
	public String editAddressForm(@RequestParam("editAddressCode") final String editAddressCode, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{

		//pratik
		model.addAttribute("addressType", getAddressType());
		//pratik
		final ValidationResults validationResults = getCheckoutStep().validate(redirectAttributes);
		if (getCheckoutStep().checkIfValidationErrors(validationResults))
		{
			return getCheckoutStep().onValidation(validationResults);
		}

		AddressData addressData = null;
		if (StringUtils.isNotEmpty(editAddressCode))
		{
			addressData = getCheckoutFacade().getDeliveryAddressForCode(editAddressCode);
		}

		final AccountAddressForm addressForm = new AccountAddressForm();
		final boolean hasAddressData = addressData != null;
		if (hasAddressData)
		{
			addressForm.setAddressId(addressData.getId());
			addressForm.setTitleCode(addressData.getTitleCode());
			addressForm.setFirstName(addressData.getFirstName());
			addressForm.setLastName(addressData.getLastName());
			addressForm.setLine1(addressData.getLine1());
			addressForm.setLine2(addressData.getLine2());
			addressForm.setTownCity(addressData.getTown());
			addressForm.setPostcode(addressData.getPostalCode());
			addressForm.setCountryIso(addressData.getCountry().getIsocode());
			addressForm.setSaveInAddressBook(Boolean.valueOf(addressData.isVisibleInAddressBook()));
			addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
			addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
			addressForm.setAddressType(addressData.getAddressType());
			if (addressData.getRegion() != null && !StringUtils.isEmpty(addressData.getRegion().getIsocode()))
			{
				addressForm.setRegionIso(addressData.getRegion().getIsocode());
			}
		}

		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		model.addAttribute("cartData", cartData);
		model.addAttribute("deliveryAddresses", getDeliveryAddresses(cartData.getDeliveryAddress()));
		if (StringUtils.isNotBlank(addressForm.getCountryIso()))
		{
			model.addAttribute("regions", getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
			model.addAttribute("country", addressForm.getCountryIso());
		}
		model.addAttribute("noAddress", Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));
		model.addAttribute("edit", Boolean.valueOf(hasAddressData));
		model.addAttribute("addressForm", addressForm);
		if (addressData != null)
		{
			model.addAttribute("showSaveToAddressBook", Boolean.valueOf(!addressData.isVisibleInAddressBook()));
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryAddress.breadcrumb"));
		model.addAttribute("metaRobots", "noindex,nofollow");
		setCheckoutStepLinksForModel(model, getCheckoutStep());

		model.addAttribute("checkoutPageName", checkoutPageName);
		return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.AddEditDeliveryAddressPage;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@RequireHardLogIn
	public String edit(final AccountAddressForm addressForm, final BindingResult bindingResult, final Model model)
			throws CMSItemNotFoundException
	{

		//pratik
		model.addAttribute("addressType", getAddressType());
		//pratik
		accountAddressValidator.validate(addressForm, bindingResult);
		if (StringUtils.isNotBlank(addressForm.getCountryIso()))
		{
			model.addAttribute("regions", getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
			model.addAttribute("country", addressForm.getCountryIso());
		}
		model.addAttribute("noAddress", Boolean.valueOf(getCheckoutFlowFacade().hasNoDeliveryAddress()));

		if (bindingResult.hasErrors())
		{
			model.addAttribute("addressForm", new AccountAddressForm());
			GlobalMessages.addErrorMessage(model, "address.error.formentry.invalid");
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			return MarketplacecheckoutaddonControllerConstants.Views.Pages.MultiStepCheckout.AddEditDeliveryAddressPage;
		}

		final AddressData newAddress = new AddressData();
		newAddress.setId(addressForm.getAddressId());
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
		if (addressForm.getCountryIso() != null)
		{
			final CountryData countryData = getI18NFacade().getCountryForIsocode(addressForm.getCountryIso());
			newAddress.setCountry(countryData);
		}
		if (addressForm.getRegionIso() != null && !StringUtils.isEmpty(addressForm.getRegionIso()))
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

		newAddress.setDefaultAddress(getUserFacade().isAddressBookEmpty() || getUserFacade().getAddressBook().size() == 1
				|| Boolean.TRUE.equals(addressForm.getDefaultAddress()));
		/*
		 * // Verify the address data. final AddressVerificationResult<AddressVerificationDecision> verificationResult =
		 * getAddressVerificationFacade() .verifyAddressData(newAddress); final boolean addressRequiresReview =
		 * getAddressVerificationResultHandler().handleResult(verificationResult, newAddress, model, redirectModel,
		 * bindingResult, getAddressVerificationFacade().isCustomerAllowedToIgnoreAddressSuggestions(),
		 * "checkout.multi.address.updated");
		 *
		 * if (addressRequiresReview) { if (StringUtils.isNotBlank(addressForm.getCountryIso())) {
		 * model.addAttribute("regions", getI18NFacade().getRegionsForCountryIso(addressForm.getCountryIso()));
		 * model.addAttribute("country", addressForm.getCountryIso()); } storeCmsPageInModel(model,
		 * getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL)); setUpMetaDataForContentPage(model,
		 * getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		 *
		 * if (StringUtils.isNotEmpty(addressForm.getAddressId())) { final AddressData addressData =
		 * getCheckoutFacade().getDeliveryAddressForCode(addressForm.getAddressId()); if (addressData != null) {
		 * model.addAttribute("showSaveToAddressBook", Boolean.valueOf(!addressData.isVisibleInAddressBook()));
		 * model.addAttribute("edit", Boolean.TRUE); } }
		 *
		 * return MarketplacecheckoutaddonControllerConstants.AddEditDeliveryAddressPage; }
		 */

		accountAddressFacade.editAddress(newAddress);
		getCheckoutFacade().setDeliveryModeIfAvailable();
		getCheckoutFacade().setDeliveryAddress(newAddress);

		return getCheckoutStep().nextStep();
	}

	@RequestMapping(value = "/remove", method =
	{ RequestMethod.GET, RequestMethod.POST })
	@RequireHardLogIn
	public String removeAddress(@RequestParam("addressCode") final String addressCode, final RedirectAttributes redirectModel,
			final Model model) throws CMSItemNotFoundException
	{

		//pratik
		model.addAttribute("addressType", getAddressType());
		//pratik
		final AddressData addressData = new AddressData();
		addressData.setId(addressCode);
		getUserFacade().removeAddress(addressData);
		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "account.confirmation.address.removed");
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute("addressForm", new AccountAddressForm());

		return getCheckoutStep().currentStep();
	}

	@RequestMapping(value = "/select", method = RequestMethod.POST)
	@RequireHardLogIn
	public String doSelectSuggestedAddress(final AccountAddressForm addressForm, final RedirectAttributes redirectModel)
	{


		final Set<String> resolveCountryRegions = org.springframework.util.StringUtils
				.commaDelimitedListToSet(Config.getParameter("resolve.country.regions"));

		final AddressData selectedAddress = new AddressData();
		selectedAddress.setId(addressForm.getAddressId());
		selectedAddress.setTitleCode(addressForm.getTitleCode());
		selectedAddress.setFirstName(addressForm.getFirstName());
		selectedAddress.setLastName(addressForm.getLastName());
		selectedAddress.setLine1(addressForm.getLine1());
		selectedAddress.setLine2(addressForm.getLine2());
		selectedAddress.setTown(addressForm.getTownCity());
		selectedAddress.setPostalCode(addressForm.getPostcode());
		selectedAddress.setBillingAddress(false);
		selectedAddress.setShippingAddress(true);
		final CountryData countryData = getI18NFacade().getCountryForIsocode(addressForm.getCountryIso());
		selectedAddress.setCountry(countryData);

		if (resolveCountryRegions.contains(countryData.getIsocode()) && addressForm.getRegionIso() != null
				&& !StringUtils.isEmpty(addressForm.getRegionIso()))
		{
			final RegionData regionData = getI18NFacade().getRegion(addressForm.getCountryIso(), addressForm.getRegionIso());
			selectedAddress.setRegion(regionData);
		}

		if (addressForm.getSaveInAddressBook() != null)
		{
			selectedAddress.setVisibleInAddressBook(addressForm.getSaveInAddressBook().booleanValue());
		}

		if (Boolean.TRUE.equals(addressForm.getEditAddress()))
		{
			getUserFacade().editAddress(selectedAddress);
		}
		else
		{
			getUserFacade().addAddress(selectedAddress);
		}

		final AddressData previousSelectedAddress = getCheckoutFacade().getCheckoutCart().getDeliveryAddress();
		// Set the new address as the selected checkout delivery address
		getCheckoutFacade().setDeliveryAddress(selectedAddress);
		if (previousSelectedAddress != null && !previousSelectedAddress.isVisibleInAddressBook())
		{ // temporary address should be removed
			getUserFacade().removeAddress(previousSelectedAddress);
		}

		GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.CONF_MESSAGES_HOLDER, "checkout.multi.address.added");

		return getCheckoutStep().nextStep();
	}


	/**
	 * This method gets called when the "Use this Address" button is clicked. It sets the selected delivery address on
	 * the checkout facade - if it has changed, and reloads the page highlighting the selected delivery address.
	 *
	 * @param selectedAddressCode
	 *           - the id of the delivery address.
	 *
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectDeliveryAddress(@RequestParam("selectedAddressCode") final String selectedAddressCode,
			final RedirectAttributes redirectAttributes, final Model model)
	{

		model.addAttribute("checkoutPageName", checkoutPageName);
		final ValidationResults validationResults = getCheckoutStep().validate(redirectAttributes);
		if (getCheckoutStep().checkIfValidationErrors(validationResults))
		{
			return getCheckoutStep().onValidation(validationResults);
		}
		if (StringUtils.isNotBlank(selectedAddressCode))
		{
			//final AddressData selectedAddressData = getCheckoutFacade().getDeliveryAddressForCode(selectedAddressCode); // DSC_006:Commented for Address state field addition
			final AddressData selectedAddressData = getMplCustomAddressFacade().getDeliveryAddressForCode(selectedAddressCode);

			final boolean hasSelectedAddressData = selectedAddressData != null;
			if (hasSelectedAddressData)
			{
				//final AddressData cartCheckoutDeliveryAddress = getCheckoutFacade().getCheckoutCart().getDeliveryAddress(); // DSC_006:Commented for Address state field addition
				final AddressData cartCheckoutDeliveryAddress = getMplCustomAddressFacade().getCheckoutCart().getDeliveryAddress();

				if (isAddressIdChanged(cartCheckoutDeliveryAddress, selectedAddressData))
				{
					getCheckoutFacade().setDeliveryAddress(selectedAddressData);
					if (cartCheckoutDeliveryAddress != null && !cartCheckoutDeliveryAddress.isVisibleInAddressBook())
					{ // temporary address should be removed
						getUserFacade().removeAddress(cartCheckoutDeliveryAddress);
					}
				}
			}
		}
		return getCheckoutStep().nextStep();
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
		return getCheckoutStep(DELIVERY_ADDRESS);
	}

	private Collection<AddressTypeData> getAddressType()
	{
		final List<EnumerationValueModel> enumList = mplEnumerationHelper.getEnumerationValuesForCode(AddressType._TYPECODE);
		final Collection<AddressTypeData> addressTypes = new ArrayList<>();
		//		final AddressTypeData addressTypeData = new AddressTypeData();

		for (final EnumerationValueModel enumerationValueModel : enumList)
		{
			final AddressTypeData addressTypeData = new AddressTypeData();
			addressTypeData.setCode(enumerationValueModel.getCode());
			addressTypeData.setName(enumerationValueModel.getCode());
			addressTypes.add(addressTypeData);
		}
		return addressTypes;




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
}
