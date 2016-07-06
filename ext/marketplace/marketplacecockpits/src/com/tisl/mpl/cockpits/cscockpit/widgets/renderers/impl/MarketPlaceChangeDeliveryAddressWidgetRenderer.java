package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.ItemModificationHistoryService;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceChangeDeliveryAddressController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCallContextController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.LandMarksModel;
import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.model.StateModel;
import com.tisl.mpl.sms.facades.SendSMSFacade;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.WidgetHelper;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.popup.PopupWindowCreator;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

public class MarketPlaceChangeDeliveryAddressWidgetRenderer
		extends
		AbstractCsWidgetRenderer<Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController>> {

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceChangeDeliveryAddressWidgetRenderer.class);
	private static final String CUSTOMER_DETAILS_UPDATED = "customerdetailsupdated";
	private static final String INFO = "info";
	private static final String PIN_REGEX = "^[1-9][0-9]{5}";

	@Autowired
	private FlexibleSearchService flexibleSearhService;
	@Autowired
	private MplOrderFacade mplOrderFacade;
	@Autowired
	private OTPGenericService oTPGenericService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private SendSMSFacade sendSMSFacade;
	@Autowired
	private MarketplaceCallContextController marketplaceCallContextController;
	@Autowired
	private ItemModificationHistoryService itemModificationHistoryService;
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private PopupWindowCreator popupWindowCreator;
	@Autowired
	private PopupWidgetHelper popupWidgetHelper;
	@Autowired
	private WidgetHelper WidgetHelper;
	@Autowired
	private DefaultCommonI18NService commonI18NService;
	@Autowired
	private MarketPlaceChangeDeliveryAddressController marketPlaceChangeDeliveryAddressController;

	private CallContextController callContextController;

	protected CallContextController getCallContextController() {
		return callContextController;
	}
	@Required
	public void setCallContextController(
			CallContextController callContextController) {
		this.callContextController = callContextController;
	}
	public TypedObject getOrder() {
		return getCallContextController().getCurrentOrder();
	}

	int otpCount = 0;

	protected HtmlBasedComponent createContentInternal(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			HtmlBasedComponent rootContainer) {

		TypedObject order = getOrder();
		OrderModel ordermodel = modelService.create(OrderModel.class);
		if (null != order && null != order.getObject()) {
			ordermodel = (OrderModel) order.getObject();
		}
		String paymentMode = StringUtils.EMPTY;
		final Div content = new Div();
		if (marketPlaceChangeDeliveryAddressController
				.isDeliveryAddressChangable(order)) {
			if (null != ordermodel.getPaymentTransactions() ) {
				PaymentTransactionModel payment  =ordermodel.getPaymentTransactions().get(0);
				payment.getEntries().get(0).getPaymentMode().getMode();
			}
		
			if (!paymentMode
					.equalsIgnoreCase(MarketplaceCockpitsConstants.PAYMENT_MODE_COD)) {
				Label label = new Label(
						"For prepaid orders please use commerce to change delivery Address");
				content.appendChild(label);
				content.setHeight("500px");
				return content;
			}
			AddressModel deliveryAddress = ordermodel.getParentReference()
					.getDeliveryAddress();
			content.setClass("changeDeliveryAddress");
			// First Name Hbox
			final Hbox firstNameHbox = createHbox(widget, "firstName", false,
					true);
			final Textbox firstNameFieldTextBox = createTextbox(firstNameHbox);
			firstNameFieldTextBox.setValue(deliveryAddress.getFirstname()
					.toString());
			firstNameHbox.setClass("hbox");
			content.appendChild(firstNameHbox);

			// Last name hbox
			final Hbox lastNameHbox = createHbox(widget, "lastName", false,
					true);
			final Textbox lastNameFieldTextBox = createTextbox(lastNameHbox);
			if (null != deliveryAddress.getLastname()) {
				lastNameFieldTextBox.setValue(deliveryAddress.getLastname()
						.toString());
			}
			lastNameHbox.setClass("hbox");
			content.appendChild(lastNameHbox);

			// line1 hbox
			final Hbox line1Hbox = createHbox(widget, "line1", false, true);
			final Textbox line1FieldTextBox = createTextbox(line1Hbox);
			if (null != deliveryAddress.getLine1()) {
				line1FieldTextBox.setValue(deliveryAddress.getLine1()
						.toString());
			}
			line1Hbox.setClass("hbox");
			content.appendChild(line1Hbox);

			// line2 hbox
			final Hbox line2Hbox = createHbox(widget, "line2", false, true);
			final Textbox line2FieldTextBox = createTextbox(line2Hbox);
			if (null != deliveryAddress.getLine2()) {
				line2FieldTextBox.setValue(deliveryAddress.getLine2()
						.toString());
			}
			line2Hbox.setClass("hbox");
			content.appendChild(line2Hbox);

			// pincode
			final Hbox pincodeHbox = createHbox(widget, "pincode", false, true);
			final Textbox pincodeFieldTextBox = createTextbox(pincodeHbox);
			if (null != deliveryAddress.getPostalcode()) {
				pincodeFieldTextBox.setValue(deliveryAddress.getPostalcode()
						.toString());
			}
			// pincode = pincodeFieldTextBox.getValue();
			pincodeHbox.setClass("hbox");
			content.appendChild(pincodeHbox);

			pincodeFieldTextBox.setMaxlength(Integer.valueOf(LabelUtils
					.getLabel(widget, "maxLength", new Object[0])));

			PincodeModel pincodeModel = new PincodeModel();

			pincodeModel = new PincodeModel();
			pincodeModel.setPincode(pincodeFieldTextBox.getValue());
			pincodeModel = flexibleSearhService.getModelByExample(pincodeModel);
			//CityModel cityModel = pincodeModel.getCityCode();
			String cityName = pincodeModel.getCityName();
			StateModel stateModel = pincodeModel.getState();
			//String cityName = cityModel.getName();
			String stateName = stateModel.getDescription();
			String countryName = commonI18NService.getCountry("IN").getName();
			final List<CountryModel> countries = commonI18NService
					.getAllCountries();
			final Div countryDiv = new Div();
			content.appendChild(countryDiv);
			Listbox countryListbox = new Listbox();
			Hbox CountryHbox = createHbox(widget, "country", false, true);
			countryListbox = createCountryListBox(widget, CountryHbox,
					countries, countryName);
			CountryHbox.setClass("hbox");
			content.appendChild(CountryHbox);
			countryListbox.getSelectedItem();

			final List<StateData> States = accountAddressFacade.getStates();
			Listbox stateListbox = new Listbox();
			final Hbox stateHbox = createHbox(widget, "state", false, true);
			createStateListBox(widget, stateHbox, stateListbox, States,
					stateName);
			stateHbox.setClass("hbox");
			content.appendChild(stateHbox);
			stateListbox.getSelectedItem();

			Listbox cityListbox = new Listbox();
			Hbox cityHbox = createHbox(widget, "city", false, true);
			Collection<PincodeModel> cities = stateModel.getCity();
			createCityListBox(widget, cityHbox, cityListbox, cities, cityName);
			cityHbox.setClass("hbox");
			content.appendChild(cityHbox);
			cityListbox.getSelectedItem();

			// landMark
			Listbox landMarkListbox = new Listbox();
			Hbox landMarkHbox = createHbox(widget, "landMark", false, true);
			// landMarkListbox.add
			Collection<LandMarksModel> landMarks = pincodeModel.getLandmarks();
			landMarkListbox = createLandMarkListBox(widget, landMarkHbox,
					landMarkListbox, landMarks);
			landMarkHbox.setClass("hbox");
			content.appendChild(landMarkHbox);

			final Hbox mobileNumberHbox = createHbox(widget, "mobileNumber",
					false, true);
			final Textbox mobileNumberFieldTextBox = createTextbox(mobileNumberHbox);
			if (null != deliveryAddress.getPhone1()) {
				mobileNumberFieldTextBox.setValue(deliveryAddress.getPhone1());
			}
			mobileNumberHbox.setClass("hbox");
			content.appendChild(mobileNumberHbox);
			Div div = new Div();
			content.appendChild(div);
			pincodeFieldTextBox.addEventListener(
					Events.ON_BLUR,
					createAddPinCodeListener(widget, pincodeFieldTextBox,
							cityListbox, stateListbox, landMarkListbox,
							cityHbox, stateHbox, CountryHbox, landMarkHbox));
			final Button update = new Button(LabelUtils.getLabel(widget,
					"Update"));
			update.setClass("updateDeliveryAddress");
			update.setParent(content);
			otpCount = 0;
			update.addEventListener(
					Events.ON_CLICK,
					createUpdateDetailsEventListener(widget, deliveryAddress,
							firstNameFieldTextBox, lastNameFieldTextBox,
							line1FieldTextBox, line1FieldTextBox,
							pincodeFieldTextBox,
							countryListbox.getSelectedItem(),
							stateListbox.getSelectedItem(),
							cityListbox.getSelectedItem(),
							landMarkListbox.getSelectedItem(),
							mobileNumberFieldTextBox, content));
			return content;

		} else {
			Label label = new Label("Delivery Address Not Changable");
			content.appendChild(label);
			content.setHeight("200px");
			return content;
		}

	}// createContentInternal

	private Listbox createCityListBox(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Hbox cityHbox, Listbox cityListbox, Collection<PincodeModel> cities,
			String cityName) {
		if (null != cityListbox && null != cityListbox.getItems()) {
			cityListbox.getItems().clear();
		}
		cityListbox.setMultiple(false);
		cityListbox.setMold("select");
		for (final PincodeModel pincode : cities) {
			final Listitem listItem = new Listitem(pincode.getCityName());
			listItem.setParent(cityListbox);
			if (pincode.getCityName().equalsIgnoreCase(cityName)) {
				listItem.setValue(pincode.getCityName());
				cityListbox.setSelectedItem(listItem);
			}
		}
		cityHbox.appendChild(cityListbox);
		return cityListbox;
	}
	


	private Listbox createLandMarkListBox(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Hbox landMarkHbox, Listbox landMarkListbox,
			Collection<LandMarksModel> landMarks) {
		if (null != landMarkListbox && null != landMarkListbox.getItems()) {
			landMarkListbox.getItems().clear();
		}
		landMarkListbox.setMultiple(false);
		landMarkListbox.setMold("select");
		for (final LandMarksModel landMark : landMarks) {
			final Listitem listItem = new Listitem(landMark.getLandmark());
			listItem.setValue(landMark.getLandmark());
			listItem.setParent(landMarkListbox);
		}
		landMarkListbox.setSelectedIndex(0);
		landMarkHbox.appendChild(landMarkListbox);
		return landMarkListbox;
	}

	private Listbox createCountryListBox(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Hbox countryHbox, List<CountryModel> countries, String countryName) {
		Listbox countryListbox = new Listbox();
		countryListbox.setMultiple(false);
		countryListbox.setMold("select");
		for (final CountryModel country : countries) {
			final Listitem listItem = new Listitem(country.getName());
			listItem.setParent(countryListbox);
			if (country.getName().equalsIgnoreCase(countryName)) {
				listItem.setValue(country.getName());
				countryListbox.setSelectedItem(listItem);
			}
		}
		countryHbox.appendChild(countryListbox);
		return countryListbox;
	}

	private Listbox createStateListBox(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Hbox stateHbox, Listbox stateListbox, List<StateData> states,
			String stateName) {
		if (null != stateListbox && null != stateListbox.getItems()) {
			stateListbox.getItems().clear();
		}
		stateListbox.setMultiple(false);
		stateListbox.setMold("select");
		for (final StateData state : states) {
			final Listitem listItem = new Listitem(state.getName());
			listItem.setParent(stateListbox);
			if (state.getName().trim().equalsIgnoreCase(stateName.trim())) {
				listItem.setValue(state.getName());
				stateListbox.setSelectedItem(listItem);
			}
		}
		stateHbox.appendChild(stateListbox);
		return stateListbox;
	}

	private EventListener createAddPinCodeListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Textbox pincodeFieldTextBox, Listbox cityListbox,
			Listbox stateListbox, Listbox landMarksListBox, Hbox cityHbox,
			Hbox stateHbox, Hbox countryHbox, Hbox landMarkHbox) {

		return new AddPinCodeEventListener(widget, pincodeFieldTextBox,
				cityListbox, stateListbox, landMarksListBox, cityHbox,
				stateHbox, countryHbox, landMarkHbox);
	}

	protected class AddPinCodeEventListener implements EventListener {

		private final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget;

		private final Textbox pincodeValue;
		private Listbox cityListbox;
		private Listbox stateListbox;
		private Listbox landMarkListBox;
		private Hbox cityHbox;
		private Hbox stateHbox;
		private Hbox countryHbox;
		private Hbox landMarkHbox;

		public AddPinCodeEventListener(
				Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
				Textbox pincodeValue, Listbox cityListbox,
				Listbox stateListbox, Listbox landMarkListBox, Hbox cityHbox,
				Hbox stateHbox, Hbox countryHbox, Hbox landMarkHbox) {
			this.widget = widget;
			this.pincodeValue = pincodeValue;
			this.cityListbox = cityListbox;
			this.stateListbox = stateListbox;
			this.landMarkListBox = landMarkListBox;
			this.stateHbox = stateHbox;
			this.cityHbox = cityHbox;
			this.countryHbox = countryHbox;
			this.landMarkHbox = landMarkHbox;
		}

		/**//**
		 * On event.
		 *
		 * @param event
		 *            the event
		 * @throws InterruptedException
		 */
		public void onEvent(Event event) throws InterruptedException {
			String pincode = pincodeValue.getValue();
			if (null != pincode) {
				if (String.valueOf(pincode).matches(PIN_REGEX)) {
					LOG.info("Pin code entered:" + pincode);
					LandMarksModel model = new LandMarksModel();

					PincodeModel pincodeModel = new PincodeModel();
					pincodeModel.setPincode(pincode);
					pincodeModel = flexibleSearhService
							.getModelByExample(pincodeModel);
					//CityModel citymodel = pincodeModel.getCityName();
					String cityName = pincodeModel.getCityName();
					StateModel statemodel = pincodeModel.getState();
					createCityListBox(widget, cityHbox, cityListbox,
							statemodel.getCity(), pincodeModel.getCityName());
					List<StateData> states = accountAddressFacade.getStates();
					createStateListBox(widget, stateHbox, stateListbox, states,
							statemodel.getDescription());
					Collection<LandMarksModel> landMarks = pincodeModel.getLandmarks();
					landMarkListBox = createLandMarkListBox(widget,
							landMarkHbox, landMarkListBox, landMarks);
				} else {

					pincodeValue.setValue(null);
					pincodeValue.setFocus(true);
					popupMessage(widget,
							MarketplaceCockpitsConstants.PIN_CODE_INVALID);
				}
			}

			LOG.info("Pin code entered:" + pincode);
		}
	}

	private void popupMessage(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			final String message) {
		try {
			Messagebox.show(
					LabelUtils.getLabel(widget, message, new Object[0]), INFO,
					Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	private EventListener createUpdateDetailsEventListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			AddressModel deliveryAddress, Textbox firstNameFieldTextBox,
			Textbox lastNameFieldTextBox, Textbox line1FieldTextBox,
			Textbox line2FieldTextBox, Textbox pincodeFieldTextBox,
			Listitem countryListItem, Listitem stateListItem,
			Listitem citylistItem, Listitem landMarkListItem,
			Textbox mobileNumberFieldTextBox, Div content) {

		return new UpdateDetailsEventListener(widget, deliveryAddress,
				firstNameFieldTextBox, lastNameFieldTextBox, line1FieldTextBox,
				line2FieldTextBox, pincodeFieldTextBox, countryListItem,
				stateListItem, citylistItem, landMarkListItem,
				mobileNumberFieldTextBox, content);
	}

	protected class UpdateDetailsEventListener implements EventListener {

		private final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget;
		private static final String FAILED_TO_UPDATE_CUSTOMER_FORM = "failedToUpdateCustomerForm";
		private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		private static final String MOBILENUMBER_REGEX = "^[0-9]{10}";
		private final AddressModel deliveryAddress;
		private final Textbox firstNameFieldTextBox;
		private final Textbox lastNameFieldTextBox;
		private final Textbox line1FieldTextBox;
		private final Textbox line2FieldTextBox;
		private final Textbox pincodeFieldTextBox;
		private final Listitem countryListbox;
		private final Listitem stateFieldTextBox;
		private final Listitem cityFieldTextBox;
		private final Listitem landMarkFieldTextBox;
		private final Textbox mobileNumberFieldTextBox;
		private Div content;

		public UpdateDetailsEventListener(
				final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget2,
				final AddressModel deliveryAddress,
				final Textbox firstNameFieldTextBox,
				final Textbox lastNameFieldTextBox,
				final Textbox line1FieldTextBox,
				final Textbox line2FieldTextBox,
				final Textbox pincodeFieldTextBox,
				final Listitem countryListItem, final Listitem stateListItem,
				final Listitem citylistItem, final Listitem landMarkListItem,
				final Textbox mobileNumberFieldTextBox, Div content) {
			this.widget = widget2;
			this.deliveryAddress = deliveryAddress;

			this.firstNameFieldTextBox = firstNameFieldTextBox;
			this.lastNameFieldTextBox = lastNameFieldTextBox;
			this.line1FieldTextBox = line1FieldTextBox;
			this.line2FieldTextBox = line2FieldTextBox;
			this.pincodeFieldTextBox = pincodeFieldTextBox;
			this.countryListbox = countryListItem;
			this.stateFieldTextBox = stateListItem;
			this.cityFieldTextBox = citylistItem;
			this.landMarkFieldTextBox = landMarkListItem;
			this.mobileNumberFieldTextBox = mobileNumberFieldTextBox;
			this.content = content;

		}

		@Override
		public void onEvent(final Event event) throws InterruptedException,
				ParseException, InvalidKeyException, NoSuchAlgorithmException {

			{
				handleUpdateDetails(event, deliveryAddress,
						firstNameFieldTextBox, lastNameFieldTextBox,
						line1FieldTextBox, line2FieldTextBox,
						pincodeFieldTextBox, countryListbox, stateFieldTextBox,
						cityFieldTextBox, landMarkFieldTextBox,
						mobileNumberFieldTextBox, content);
			}
		}
		// @SuppressWarnings({ "deprecation", "null" }) private void
		void handleUpdateDetails(final Event event,
				final AddressModel deliveryAddress,
				final Textbox firstNameFieldTextBox,
				final Textbox lastNameFieldTextBox,
				final Textbox line1FieldTextBox,
				final Textbox line2FieldTextBox,
				final Textbox pincodeFieldTextBox,
				final Listitem countryListbox,
				final Listitem stateFieldTextBox,
				final Listitem cityFieldTextBox,
				final Listitem landMarkFieldTextBox,
				final Textbox mobileNumberFieldTextBox, Div content)
				throws InterruptedException, ParseException,
				InvalidKeyException, NoSuchAlgorithmException {

			final String changedFirstName = firstNameFieldTextBox.getValue();
			final String changedLastName = lastNameFieldTextBox.getValue();
			final String changedLine1 = line1FieldTextBox.getValue();
			final String changedLine2 = line2FieldTextBox.getValue();
			final String changedPincode = pincodeFieldTextBox.getValue();
			final String changedCountry = countryListbox.getValue().toString();
			final String changedState = stateFieldTextBox.getValue().toString();
			final String changedCity = cityFieldTextBox.getValue().toString();
			final String changedlandMark = landMarkFieldTextBox.getValue()
					.toString();
			final String changedMobileNumber = mobileNumberFieldTextBox
					.getValue();
			final AddressModel newDeliveryAddress = modelService
					.create(AddressModel.class);
			CustomerModel customer = (CustomerModel) callContextController
					.getCurrentCustomer().getObject();

			if (changedFirstName != null && !changedFirstName.isEmpty()) {
				newDeliveryAddress.setFirstname(changedFirstName);
			} else {
				newDeliveryAddress
						.setFirstname(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedLastName != null && !changedLastName.isEmpty()) {
				newDeliveryAddress.setLastname(changedLastName);
			} else {
				newDeliveryAddress
						.setLastname(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedLine1 != null && !changedLine1.isEmpty()) {
				newDeliveryAddress.setLine1(changedLine1.toString());

			} else {
				newDeliveryAddress
						.setLine1(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedLine2 != null && !changedLine2.isEmpty()) {
				newDeliveryAddress.setLine2(changedLine2);
			} else {
				newDeliveryAddress
						.setLine2(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedPincode != null && !changedPincode.isEmpty()) {
				newDeliveryAddress.setPostalcode(changedPincode);
			} else {
				newDeliveryAddress
						.setPostalcode(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedState != null && !changedState.isEmpty()) {
				newDeliveryAddress.setState(changedState);
			} else {
				newDeliveryAddress
						.setState(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedCity != null && !changedCity.isEmpty()) {
				newDeliveryAddress.setCity(changedCity);
			} else {
				newDeliveryAddress
						.setCity(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedState != null && !changedState.isEmpty()) {
				newDeliveryAddress.setState(changedState);
			} else {
				newDeliveryAddress
						.setState(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedCountry != null && !changedCountry.isEmpty()) {
				CountryModel countrymodel = commonI18NService.getCountry("IN");
				Locale loc = new Locale("en");
				countrymodel.setName(changedCountry, loc);
				
				newDeliveryAddress.setCountry(countrymodel);
			}
			if (changedCity != null && !changedCity.isEmpty()) {
				newDeliveryAddress.setCity(changedCity);
			} else {
				newDeliveryAddress
						.setCity(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedlandMark != null && !changedlandMark.isEmpty()) {
				newDeliveryAddress.setLandmark(changedlandMark);
			} else {
				newDeliveryAddress
						.setLandmark(MarketplacecommerceservicesConstants.EMPTY);
			}
			if (changedMobileNumber != null) {
				newDeliveryAddress.setPhone1(changedMobileNumber);
			} else {
				newDeliveryAddress
						.setPhone1(MarketplacecommerceservicesConstants.EMPTY);
			}
			TypedObject order = getOrder();
			final OrderModel ordermodel = (OrderModel) order.getObject();
			AbstractOrderModel parentOrder = ordermodel.getParentReference();
			AddressModel address = parentOrder.getDeliveryAddress();
			List<AddressModel> deliveryAddressList = new ArrayList<AddressModel>();
			if (null == deliveryAddressList || deliveryAddressList.isEmpty()) {
				deliveryAddressList.add(0, parentOrder.getDeliveryAddress()
						.getOriginal());
			}
			newDeliveryAddress.setOwner(customer);
			OTPModel OTP = new OTPModel();
			OTP = sendSmsToCustomer(ordermodel, otpCount);
			content.getAttribute("otparea");
			if (otpCount == 1) {
				Div otparea = new Div();
				otparea.setClass("changeDeliveryotpArea");
				Hbox otpHbox = new Hbox();
				otpHbox.setClass("otpHbox");
				otpHbox.setParent(content);
				otparea.setVisible(true);
				Textbox OTPTextBox = new Textbox();
				OTPTextBox.setMaxlength(8);
				otpHbox.appendChild(otparea);
				OTPTextBox.setParent(otparea);
				Toolbarbutton resendOtp = new Toolbarbutton("Resend OTP");
				resendOtp.setClass("changeDeliveryResentOtp");
				resendOtp.setParent(otparea);
				Div validateOtpButtonDiv = new Div();
				Button validateOTP = new Button(LabelUtils.getLabel(widget,
						"validateButton"));
				validateOTP.setClass("validateOTP");
				validateOtpButtonDiv.appendChild(validateOTP);
				validateOtpButtonDiv.setParent(content);
				resendOtp.addEventListener(Events.ON_CLICK,
						new EventListener() {
							@Override
							public void onEvent(Event arg0) throws Exception {
								// createValidateOTPEventListener(widget);
								sendSmsToCustomer(ordermodel, otpCount);
							}
						});
				
				validateOTP.addEventListener(
						Events.ON_CLICK,
						createValidateOTPEventListener(widget, OTPTextBox,
								ordermodel, newDeliveryAddress));
			}
		}

	}

	// To send SMS
	private OTPModel sendSmsToCustomer(OrderModel ordermodel, int otpcount)
			throws InvalidKeyException, NoSuchAlgorithmException {
		String oTPMobileNumber = ordermodel.getDeliveryAddress().getPhone1();
		OTPModel OTP = oTPGenericService.getLatestOTPModel(ordermodel.getUser()
				.getUid(), OTPTypeEnum.COD);
		String userId = ordermodel.getUser().getUid();
		final String contactNumber = configurationService
				.getConfiguration()
				.getString(
						MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);
		String mplCustomerName = (null == ordermodel.getUser().getName()) ? ""
				: ordermodel.getUser().getName();
		String smsContent = oTPGenericService.generateOTP(userId,
				OTPTypeEnum.COD.getCode(), oTPMobileNumber);
		
		sendSMSFacade
				.sendSms(
						MarketplacecommerceservicesConstants.SMS_SENDER_ID,
						MarketplacecommerceservicesConstants.SMS_MESSAGE_CD_OTP
								.replace(
										MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
										mplCustomerName != null ? mplCustomerName
												: "There")
								.replace(
										MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE,
										smsContent)
								.replace(
										MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO,
										contactNumber), oTPMobileNumber);
		
		otpCount++;
		return OTP;
	}

	private void popupMessage(
			final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			final String message, String icon) {
		try {
			Messagebox.show(
					LabelUtils.getLabel(widget, message, new Object[0]),
					"Info", Messagebox.OK, icon);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	public EventListener createValidateOTPEventListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Textbox oTPTextBox, OrderModel ordermodel,
			AddressModel newDeliveryAddress) {
		return new ValidateOTPEventListener(widget, oTPTextBox, ordermodel,
				newDeliveryAddress);
	}

	// Validate OTP

	protected class ValidateOTPEventListener implements EventListener {
		/** The widget. */
		private Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget;

		/** The o tp text box. */
		private Textbox oTPTextBox;
		private OrderModel orderModel;
		private AddressModel newDeliveryAddress;

		public ValidateOTPEventListener(
				Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
				Textbox oTPTextBox, OrderModel orderModel,
				AddressModel newDeliveryAddress) {
			super();
			this.widget = widget;
			this.oTPTextBox = oTPTextBox;
			this.orderModel = orderModel;
			this.newDeliveryAddress = newDeliveryAddress;
		}


		@Override
		public void onEvent(Event event) throws InterruptedException {
			if (!oTPTextBox.getText().isEmpty()) {
				handleValidateOTPEvent(widget, oTPTextBox, event, orderModel,
						newDeliveryAddress);
			} else {
				Messagebox
						.show(LabelUtils.getLabel(widget, "ENTER_OTP",
								new Object[0]), INFO, Messagebox.OK,
								Messagebox.ERROR);
			}
		}

		private void handleValidateOTPEvent(
				Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
				Textbox oTPTextBox, Event event, OrderModel orderModel,
				AddressModel newDeliveryAddress) throws InterruptedException {
			long time = 0l;
			try {
				time = Long.parseLong(configurationService.getConfiguration()
						.getString("OTP_Valid_Time_milliSeconds"));

			} catch (NumberFormatException exp) {
				LOG.error("on Time limit defined");
			}
			OTPResponseData otpResponse = oTPGenericService.validateOTP(
					orderModel.getUser().getUid(), orderModel
							.getDeliveryAddress().getPhone1(), oTPTextBox
							.getValue(), OTPTypeEnum.COD, time);
			boolean validate = otpResponse.getOTPValid();

			if (validate) {
				boolean omsStatus = marketPlaceChangeDeliveryAddressController .changeDeliveryAddressCallToOMS(orderModel.getParentReference().getCode(), newDeliveryAddress);
										 
				
				if (omsStatus) {
					orderModel.setDeliveryAddress(newDeliveryAddress);
					orderModel.getParentReference().setDeliveryAddress(
							newDeliveryAddress);
					TypedObject customer = marketplaceCallContextController
							.getCurrentCustomer();
					CustomerModel customermodel = (CustomerModel) customer
							.getObject();
					String customerId = customermodel.getUid();
					marketPlaceChangeDeliveryAddressController
							.ticketCreateToCrm(orderModel.getParentReference(),
									customerId,
									MarketplaceCockpitsConstants.SOURCE);
					Messagebox.show(LabelUtils.getLabel(widget,
							CUSTOMER_DETAILS_UPDATED, new Object[0]), INFO,
							Messagebox.OK, Messagebox.INFORMATION);
					popupWidgetHelper.dismissCurrentPopup();
				} else {
					Messagebox.show(LabelUtils.getLabel(widget, "FailedAtOMS ",
							new Object[0]), INFO, Messagebox.OK,
							Messagebox.ERROR);
					popupWidgetHelper.dismissCurrentPopup();
				}
			} else {
				Messagebox.show(LabelUtils.getLabel(widget, "INVALID_OTP",
						new Object[0]), INFO, Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	private Textbox createTextbox(final Hbox parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("150px");
		textBox.setParent(parent);
		return textBox;
	}

	private Hbox createHbox(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			String attributeLabel, boolean hidden, final boolean overwriteWidth) {
		final Hbox hbox = new Hbox();
		if (overwriteWidth) {
			hbox.setWidths("9em, none");
		}
		hbox.setAlign("center");
		if (hidden) {
			hbox.setVisible(false);
		}
		final Label label = new Label(LabelUtils.getLabel(widget,
				attributeLabel));
		label.setParent(hbox);
		return hbox;
	}
}