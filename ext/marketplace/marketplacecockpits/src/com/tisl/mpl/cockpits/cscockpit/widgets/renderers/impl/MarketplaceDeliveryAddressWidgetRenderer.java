package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MplDeliveryAddressController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.facade.data.LandMarksData;
import com.tisl.mpl.facades.account.address.MplAccountAddressFacade;
import com.tisl.mpl.facades.data.PincodeData;
import com.tisl.mpl.facades.populators.CustomAddressPopulator;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.daos.AccountAddressDao;

import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.WidgetContainer;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetContainer;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetFactory;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.CustomerController;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AddressCreateWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

public class MarketplaceDeliveryAddressWidgetRenderer extends
				AddressCreateWidgetRenderer {
	private static final Logger LOG = Logger
			.getLogger(MarketplaceDeliveryAddressWidgetRenderer.class);
	protected static final String CSS_CREATE_ADDRESS_ACTIONS = "csCreateAddressActions";
	private static final String NO_RESPONCE_FROM_SERVER = "noresponce";
	private static final String PINCODE_NOT_SERVICIBLE = "pincodeNotServicible";
	private static final String INFO = "info";
	protected static final String FAILED_ADDRESS_LOOKUP = "failedAddressLookup";
	protected static final String FAILED_VALIDATION = "failedValidation";
	private static final String USE_WEBSITE_FOR_COD = "useWebsiteForCOD";
	private static final String PIN_REGEX = "^[1-9][0-9]{5}";
	private static final String ADDRESS_NOT_CHANGABLE = "addressNotChangable";
	private boolean isChangeDeliveryAddress;
	private boolean isAddressForReturn;

	public boolean getIsChangeDeliveryAddress() {
		return isChangeDeliveryAddress;
	}

	public void setIsChangeDeliveryAddress(boolean isChangeDeliveryAddress) {
		this.isChangeDeliveryAddress = isChangeDeliveryAddress;
	}
	
	public boolean getIsAddressForReturn() {
		return isChangeDeliveryAddress;
	}

	public void setIsAddressForReturn(boolean isAddressForReturn) {
		this.isAddressForReturn = isAddressForReturn;
	}

	@Autowired
	private SessionService sessionService;
	@Autowired
	private MplDeliveryAddressController mplDeliveryAddressController;
	@Autowired
	private PopupWidgetHelper popupWidgetHelper;
	@Autowired
	private ModelService modelService;
	@Autowired
	private DefaultCommonI18NService commonI18NService;
	@Autowired
	private MplAccountAddressFacade accountAddressFacade;
	@Autowired
	private Populator<AddressModel, AddressData> addressPopulator ;
	@Autowired
	private CustomAddressPopulator customAddressPopulator;
	@Autowired
	OrderModelDao orderModelDao;

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

	@Override
	protected HtmlBasedComponent createContentInternal(
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
			final HtmlBasedComponent rootContainer) {

		final Div content = new Div();
		final Div customerAddressContent = new Div();
		content.appendChild(customerAddressContent);
		if (isChangeDeliveryAddress) {
			try {
				TypedObject order = getOrder();
				OrderModel ordermodel = null;
				if (null != order && null != order.getObject()) {
					ordermodel = (OrderModel) order.getObject();
				}
				if (mplDeliveryAddressController
						.isDeliveryAddressChangable(order)) {
					LOG.info("Inside change delivery Address  createContentInternal method ");

					String paymentMode = StringUtils.EMPTY;
					if (null != ordermodel.getPaymentTransactions()) {
						PaymentTransactionModel payment = ordermodel
								.getPaymentTransactions().get(0);
						paymentMode = payment.getEntries().get(0)
								.getPaymentMode().getMode();
						LOG.debug("Payment mode = " + paymentMode);
					}

					if (!paymentMode
							.equalsIgnoreCase(MarketplaceCockpitsConstants.PAYMENT_MODE_COD)) {
						Label label = new Label(LabelUtils.getLabel(widget,
								USE_WEBSITE_FOR_COD, new Object[0]));
						content.appendChild(label);
						return content;
					}
				} else {
					Label label = new Label("Delivery Address Not Changable");
					content.appendChild(label);
					content.setHeight("200px");
					return content;
				}
			} catch (Exception e) {
				LOG.error("Exception while calling  isDeliveryAddressChangable method"
						+ e.getMessage());
			}
		}
		// Create First Name field
		final Br br1 = new Br();
		br1.setParent(customerAddressContent);
		final Div firstNameDiv = new Div();
		firstNameDiv.setParent(customerAddressContent);
		firstNameDiv.setSclass("createNewAddress");
		final Label firstNamelabel = new Label(LabelUtils.getLabel(widget,
				"firstName"));
		firstNamelabel.setParent(firstNameDiv);
		final Textbox firstNameField = createTextbox(firstNameDiv);
		firstNameField.setSclass("addressForFN");
		firstNameField.setMaxlength(40);

		// Create Last Name field
		final Br br2 = new Br();
		br2.setParent(customerAddressContent);
		final Div lastNameDiv = new Div();
		lastNameDiv.setParent(customerAddressContent);
		lastNameDiv.setSclass("createNewAddress");
		final Label lastNamelabel = new Label(LabelUtils.getLabel(widget,
				"lastName"));
		lastNamelabel.setParent(lastNameDiv);
		final Textbox lastNameField = createTextbox(lastNameDiv);
		lastNameField.setSclass("addressForLN");
		lastNameField.setMaxlength(40);

		// Creates Postal Code field
		final Br br6 = new Br();
		br6.setParent(customerAddressContent);
		final Div postalCodeDiv = new Div();
		postalCodeDiv.setParent(customerAddressContent);
		postalCodeDiv.setSclass("createNewAddress");
		final Label postalCodelabel = new Label(LabelUtils.getLabel(widget,
				"postalCode"));
		postalCodelabel.setParent(postalCodeDiv);
		final Textbox postalCodeField = createTextbox(postalCodeDiv);
		postalCodeField.setMaxlength(6);
		postalCodeField.setSclass("addressForPostalCodeField");
		postalCodeField.setMaxlength(6);

		// Creates Address Line1 field
		final Br br3 = new Br();
		br3.setParent(customerAddressContent);
		final Div addressDiv = new Div();
		addressDiv.setParent(customerAddressContent);
		addressDiv.setSclass("createNewAddress");
		final Label addresslabel = new Label(LabelUtils.getLabel(widget,
				"address"));
		addresslabel.setParent(addressDiv);
		final Textbox address1Field = createTextbox(addressDiv);
		address1Field.setSclass("address1ForAddressField");
		address1Field.setMaxlength(40);//changing for TISPRDT-1264

		// Creates Address Line2 field
		final Br br4 = new Br();
		br4.setParent(customerAddressContent);
		final Div address2Div = new Div();
		address2Div.setParent(customerAddressContent);
		address2Div.setSclass("createNewAddress");
		final Label address2label = new Label(LabelUtils.getLabel(widget,
				"address2"));
		address2label.setParent(address2Div);
		final Textbox address2Field = createTextbox(address2Div);
		address2Field.setSclass("address2ForAddressField");
		address2Field.setMaxlength(40);//changing for TISPRDT-1264

		// Creates Address Line3 field
		final Br br5 = new Br();
		br5.setParent(customerAddressContent);
		final Div address3Div = new Div();
		address3Div.setParent(customerAddressContent);
		address3Div.setSclass("createNewAddress");
		final Label address3label = new Label(LabelUtils.getLabel(widget,
				"address3"));
		address3label.setParent(address3Div);
		final Textbox address3Field = createTextbox(address3Div);
		address3Field.setSclass("address2ForAddressField");
		address3Field.setMaxlength(40);//changing for TISPRDT-1264

		// Creates LandMark field
		final Br br12 = new Br();
		br12.setParent(customerAddressContent);
		final Div landMarkDiv = new Div();
		landMarkDiv.setParent(customerAddressContent);
		landMarkDiv.setSclass("createNewAddress");
		final Label landMarklabel = new Label(LabelUtils.getLabel(widget,
				"landMark"));
		landMarklabel.setParent(landMarkDiv);
		final Listbox landMarkListbox = new Listbox();
		try {
			landMarkListbox.setClass("landMarkDropDownField");
			landMarkListbox.setMultiple(false);
			landMarkListbox.setMold("select");
			landMarkListbox.setParent(landMarkDiv);
		} catch (Exception e) {
			//System.out.println("setParent " + e.getCause());
			LOG.debug("setParent " + e.getCause());
		}

		final Textbox landMarkField = createTextbox(landMarkDiv);
		landMarkField.setSclass("landMarkAddressField");
		landMarkField.setMaxlength(30);
		landMarkField.setVisible(false);
		landMarkListbox.addEventListener(Events.ON_SELECT, new EventListener() {
			@Override
			public void onEvent(final Event event) throws InterruptedException,
			ParseException, InvalidKeyException,
			NoSuchAlgorithmException {
				createLandMarkChangeEventListener(widget, landMarkListbox,
						landMarkField);
			}
		});
		// Creates City/District field
		final Br br7 = new Br();
		br7.setParent(customerAddressContent);
		final Div cityDiv = new Div();
		cityDiv.setParent(customerAddressContent);
		cityDiv.setSclass("createNewAddress");
		final Label citylabel = new Label(LabelUtils.getLabel(widget, "city"));
		citylabel.setParent(cityDiv);
		final Textbox cityField = createTextbox(cityDiv);
		cityField.setSclass("addressForCityField");
		cityField.setMaxlength(30);

		// Creates State List Box
		final Br br8 = new Br();
		br8.setParent(customerAddressContent);
		final Div stateDiv = new Div();
		stateDiv.setParent(customerAddressContent);
		stateDiv.setSclass("stateForAddressType");
		final Label statelabel = new Label(LabelUtils.getLabel(widget, "state"));
		statelabel.setParent(stateDiv);

		final Listbox stateFieldListBox = new Listbox();
		stateFieldListBox.setMultiple(false);
		stateFieldListBox.setMold("select");
		stateFieldListBox.setParent(stateDiv);

		final List<StateData> stateDataList = accountAddressFacade.getStates();

		stateFieldListBox.appendItem(
				LabelUtils.getLabel(widget, "defaultSelectOption"),
				StringUtils.EMPTY);
		for (final StateData value : stateDataList) {
			final Listitem stateListItem = new Listitem(value.getName());
			stateListItem.setParent(stateFieldListBox);
		}

		// Create Country List box
		final Br br9 = new Br();
		br9.setParent(customerAddressContent);
		final Div countryDiv = new Div();
		countryDiv.setParent(customerAddressContent);
		countryDiv.setSclass("addressForCountry");
		final List<CountryModel> countries = commonI18NService
				.getAllCountries();
		final Label countryLabel = new Label(LabelUtils.getLabel(widget,
				"country"));
		countryLabel.setParent(countryDiv);
		final Listbox countryListbox = new Listbox();
		countryListbox.setMultiple(false);
		countryListbox.setMold("select");
		countryListbox.setParent(countryDiv);
		for (final CountryModel country : countries) {
			final Listitem listItem = new Listitem(country.getName());
			listItem.setParent(countryListbox);
			if (country.getName().equalsIgnoreCase("INDIA")) {
				countryListbox.setSelectedItem(listItem);
			}
		}
		countryListbox.setDisabled(true);

		// Create List Box for Address type
		final Br br10 = new Br();
		br10.setParent(customerAddressContent);
		final Div addressTypeDiv = new Div();
		addressTypeDiv.setParent(customerAddressContent);
		addressTypeDiv.setSclass("addressForAddressType");
		final Label label = new Label(
				LabelUtils.getLabel(widget, "addressType"));
		label.setParent(addressTypeDiv);
		final Listbox AddressTypeListbox = new Listbox();
		AddressTypeListbox.setMultiple(false);
		AddressTypeListbox.setMold("select");
		final List<String> AddressRadioTypeList = getAddressRadioTypeList();
		// final List<AddressCSEnum> values =
		// enumService.getEnumerationValues(AddressCSEnum.class);
		AddressTypeListbox.appendItem(
				LabelUtils.getLabel(widget, "defaultSelectOption"),
				StringUtils.EMPTY);
		for (final String value : AddressRadioTypeList) {
			final Listitem listItem = new Listitem(value);
			listItem.setParent(AddressTypeListbox);
		}
		AddressTypeListbox.setSelectedIndex(1);
		AddressTypeListbox.setParent(addressTypeDiv);

		// Creates Mobile Number field
		final Br br11 = new Br();
		br11.setParent(customerAddressContent);
		final Div mobileNumberDiv = new Div();
		mobileNumberDiv.setParent(customerAddressContent);
		mobileNumberDiv.setSclass("createNewAddress");
		final Label mobileNumberlabel = new Label(LabelUtils.getLabel(widget,
				"mobileNumber"));
		mobileNumberlabel.setParent(mobileNumberDiv);
		final Textbox mobileNumberPrefixField = createPrefixTextbox(mobileNumberDiv);
		final Textbox mobileNumberField = createMobileNumberTextbox(mobileNumberDiv);
		mobileNumberField.setMaxlength(10);
		mobileNumberField.setSclass("addressForMobileNumberField");
		mobileNumberPrefixField.setSclass("addressForMobileNumberPrefixField");

		if (isChangeDeliveryAddress) {
			try {
				TypedObject orderModel = getOrder();
				OrderModel order = (OrderModel) orderModel.getObject();
				AddressModel deliveryAddress = order.getDeliveryAddress();

				if (null != deliveryAddress) {
					String state = StringUtils.EMPTY;
					if(null != deliveryAddress.getState()) {
						state = deliveryAddress.getState();
					}else if(null != deliveryAddress.getDistrict()){
						state = deliveryAddress.getDistrict();
					}
					PincodeData pincodeData = mplDeliveryAddressController
							.getPincodeData(deliveryAddress.getPostalcode());
					firstNameField.setValue(deliveryAddress.getFirstname());
					lastNameField.setValue(deliveryAddress.getLastname());
					postalCodeField.setValue(deliveryAddress.getPostalcode());
					address1Field.setValue(deliveryAddress.getLine1());
					address2Field.setValue(deliveryAddress.getLine2());
					address3Field.setValue(deliveryAddress.getAddressLine3());
					String addresslandMark = deliveryAddress.getLandmark();
					if (pincodeData != null
							&& null != pincodeData.getLandMarks()) {
						
						Listitem listItem = new Listitem();
						listItem = new Listitem(
								MarketplaceCockpitsConstants.SELECT_LANDMARK);
						listItem.setValue(MarketplaceCockpitsConstants.SELECT_LANDMARK);
						listItem.setParent(landMarkListbox);
						for (final LandMarksData landMark : pincodeData.getLandMarks()) {
							Listitem	item = new Listitem(landMark.getLandmark());
							item.setValue(landMark.getLandmark());
							item.setParent(landMarkListbox);
							if(null != addresslandMark && addresslandMark.equalsIgnoreCase(landMark.getLandmark())) {
								item.setSelected(true);
								landMarkListbox.setSelectedItem(item);
							}
						}
						Listitem listItemOthers = new Listitem();
						listItemOthers = new Listitem(MarketplaceCockpitsConstants.OTHERS);
						listItemOthers.setValue(MarketplaceCockpitsConstants.OTHERS);
						listItemOthers.setParent(landMarkListbox);
						
						if(null != addresslandMark && null == landMarkListbox.getSelectedItem()) {
							listItemOthers.setSelected(true);
							landMarkListbox.setSelectedItem(listItemOthers);
							landMarkField.setValue(deliveryAddress.getLandmark());
							landMarkField.setVisible(true);
						}
					} else if((null == pincodeData || null == pincodeData.getLandMarks())&& null != deliveryAddress.getLandmark()) {
						Listitem listItemNoLandMark = new Listitem(MarketplaceCockpitsConstants.NO_LANDMARKS_FOUND);
						listItemNoLandMark.setValue(MarketplaceCockpitsConstants.NO_LANDMARKS_FOUND);
						listItemNoLandMark.setParent(landMarkListbox);
						Listitem listItem = new Listitem(
								MarketplaceCockpitsConstants.OTHERS);
						listItem.setValue(MarketplaceCockpitsConstants.OTHERS);
						listItem.setParent(landMarkListbox);
						landMarkListbox.setSelectedItem(listItem);
						landMarkField.setValue(deliveryAddress.getLandmark());
						landMarkField.setVisible(true);
					}else if (null == addresslandMark &&( null == pincodeData || null == pincodeData.getLandMarks())) {
						Listitem listItem = new Listitem();
						listItem = new Listitem(MarketplaceCockpitsConstants.NO_LANDMARKS_FOUND);
						listItem.setValue(MarketplaceCockpitsConstants.NO_LANDMARKS_FOUND);
						listItem.setParent(landMarkListbox);
						listItem = new Listitem(MarketplaceCockpitsConstants.OTHERS);
						listItem.setValue(MarketplaceCockpitsConstants.OTHERS);
						listItem.setParent(landMarkListbox);
						landMarkListbox.setSelectedIndex(0);
					}
					if (null != deliveryAddress.getCity()) {
						cityField.setValue(deliveryAddress.getCity());
						cityField.setDisabled(true);
					} else if(null != deliveryAddress.getTown()){
						cityField.setValue(deliveryAddress.getTown());
						cityField.setDisabled(true);
					}else{
						cityField.setDisabled(false);
					}
					mobileNumberField.setValue(deliveryAddress.getPhone1());
					List<Listitem> items = stateFieldListBox.getItems();
					for (Listitem item : items) {
						String stateName = (String) item.getLabel();
						if (stateName.equalsIgnoreCase(state)) {
							stateFieldListBox.setSelectedItem(item);
							stateFieldListBox.setDisabled(true);
						}
					}
				}
			}catch (Exception e) {
				LOG.error("Exception while getting the pincode data "
						+ e.getMessage());
			}
		}
		postalCodeField.addEventListener(
				Events.ON_CHANGE,
				createAddPinCodeListener(widget, postalCodeField, cityField,
						stateFieldListBox, landMarkListbox, landMarkField));
		final Button createButton = new Button(LabelUtils.getLabel(widget,
				"createButton", new Object[0]));
		createButton.setParent(customerAddressContent);
		createButton.setSclass("updateDeliveryAddress");

		createButton.addEventListener(
				"onClick",
				createCreateClickEventListener(widget, firstNameField,
						lastNameField, address1Field, address2Field,
						address3Field, landMarkField, landMarkListbox,
						cityField, postalCodeField, stateFieldListBox,
						mobileNumberPrefixField, mobileNumberField,
						AddressTypeListbox, countryListbox));
		return content;
	}

	private void createLandMarkChangeEventListener(
			InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
			Listbox landMarkListbox, Textbox landMarkField) {
		try {
			List<Listitem> items = landMarkListbox.getItems();
			String selectedLandmark = StringUtils.EMPTY;
			for (Listitem item : items) {
				if (item.isSelected()) {
					selectedLandmark = (String) item.getValue();
				}
			}
			if (null != selectedLandmark
					&& selectedLandmark
					.equalsIgnoreCase(MarketplaceCockpitsConstants.OTHERS)) {
				landMarkField.setValue(null);
				landMarkField.setVisible(true);
			} else if (StringUtils.isNotEmpty(selectedLandmark) && landMarkField.getValue().isEmpty()) {
				landMarkField.setValue(null);
				landMarkField.setVisible(false);
			} else {
				landMarkField.setValue(null);
				landMarkField.setVisible(false);
			}
		} catch (Exception e) {
			LOG.error("Exception Occurred while getting landmark value"
					+ e.getMessage());
		}
	}

	private void createlandMarkDropDown(
			List<LandMarksData> landMarks, Listbox landMarkListbox) {
		if (null != landMarkListbox && null != landMarkListbox.getItems()) {
			landMarkListbox.getItems().clear();
		}
		Listitem listItem;
		if(null != landMarks) {
			listItem = new Listitem(
					MarketplaceCockpitsConstants.SELECT_LANDMARK);
			listItem.setValue(MarketplaceCockpitsConstants.SELECT_LANDMARK);
			listItem.setParent(landMarkListbox);
			for (final LandMarksData landMark : landMarks) {
				listItem = new Listitem(landMark.getLandmark());
				listItem.setValue(landMark.getLandmark());
				listItem.setParent(landMarkListbox);
			}
			listItem = new Listitem(MarketplaceCockpitsConstants.OTHERS);
			listItem.setValue(MarketplaceCockpitsConstants.OTHERS);
			listItem.setParent(landMarkListbox);
			landMarkListbox.addItemToSelection(listItem);
		} else if(null == landMarks) {
			listItem = new Listitem(
					MarketplaceCockpitsConstants.NO_LANDMARKS_FOUND);
			listItem.setValue(MarketplaceCockpitsConstants.NO_LANDMARKS_FOUND);
			listItem.setParent(landMarkListbox);
			landMarkListbox.addItemToSelection(listItem);
			listItem = new Listitem(
					MarketplaceCockpitsConstants.OTHERS);
			listItem.setValue(MarketplaceCockpitsConstants.OTHERS);
			listItem.setParent(landMarkListbox);
		}
		landMarkListbox.setSelectedIndex(0);
	}

	private EventListener createAddPinCodeListener(
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
			Textbox postalCodeField, Textbox cityField,
			Listbox stateFieldListBox, Listbox landMarkListbox,
			Textbox landMarkField) {

		return new AddPinCodeEventListener(widget, postalCodeField, cityField,
				stateFieldListBox, landMarkListbox, landMarkField);
	}

	protected class AddPinCodeEventListener implements EventListener {

		private final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget;
		private final Textbox postalCodeField;
		private Textbox cityField;
		private Listbox stateFieldListBox;
		private Listbox landMarkListbox;
		private Textbox landMarkField;

		public AddPinCodeEventListener(
				final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
				Textbox postalCodeField, Textbox cityField,
				Listbox stateFieldListBox, Listbox landMarkListbox,
				Textbox landMarkField) {
			this.widget = widget;
			this.postalCodeField = postalCodeField;
			this.cityField = cityField;
			this.stateFieldListBox = stateFieldListBox;
			this.landMarkListbox = landMarkListbox;
			this.landMarkField = landMarkField;
		}

		
		/**//**
		 * On event.
		 *
		 * @param event
		 *            the event
		 * @throws InterruptedException
		 */
		public void onEvent(Event event) throws InterruptedException {
			try {
				String pincode = postalCodeField.getValue();
				if (null != pincode && pincode.matches(PIN_REGEX)) {
					LOG.info("Pin code entered:" + pincode);
					PincodeData pincodeData = new PincodeData();
					try {
						pincodeData.setPincode(pincode);
						pincodeData = mplDeliveryAddressController
								.getPincodeData(pincode);
					} catch (Exception e) {
						LOG.error("FlexibleSearchException No result for the given pincode "
								+ pincode);
					}
					if (null != pincodeData) {
						if (null != pincodeData.getCityName()) {
							cityField.setValue(pincodeData.getCityName());
							cityField.setDisabled(true);
						} else {
							cityField
							.setValue(MarketplacecommerceservicesConstants.EMPTY);
							cityField.setDisabled(false);
						}
						StateData stateData = pincodeData.getState();
						if (null != stateData && null != stateData.getName()) {
							List<Listbox> stateList = stateFieldListBox
									.getItems();
							try {
								List<Listitem> items = stateFieldListBox
										.getItems();
								for (Listitem item : items) {
									String stateName = (String) item.getLabel();
									if (stateName.equalsIgnoreCase(pincodeData
											.getState().getName())) {
										stateFieldListBox.setSelectedItem(item);
										stateFieldListBox.setDisabled(true);
									}
								}
							} catch (Exception e) {
								LOG.error("Exception  occurred while getting the state data "
										+ e.getMessage());
							}
						}
						if (null != pincodeData.getLandMarks()) {
							landMarkField.setVisible(false);
							landMarkField.setValue(null);
							createlandMarkDropDown(
									pincodeData.getLandMarks(), landMarkListbox);
						} else {
							landMarkListbox.getItems().clear();
							landMarkField.setVisible(false);
							landMarkField.setValue(null);
							createlandMarkDropDown(
									null, landMarkListbox);
						}
					} else {
						cityField
						.setValue(MarketplacecommerceservicesConstants.EMPTY);
						cityField.setDisabled(false);
						landMarkListbox.getItems().clear();
						Listitem listItem;
						listItem = new Listitem(
								MarketplaceCockpitsConstants.NO_LANDMARKS_FOUND);
						listItem.setValue(MarketplaceCockpitsConstants.NO_LANDMARKS_FOUND);
						listItem.setParent(landMarkListbox);
						listItem = new Listitem(
								MarketplaceCockpitsConstants.OTHERS);
						listItem.setValue(MarketplaceCockpitsConstants.OTHERS);
						listItem.setParent(landMarkListbox);
						stateFieldListBox.setSelectedIndex(0);
						stateFieldListBox.setDisabled(false);
						landMarkField.setVisible(false);
						landMarkField.setValue(null);
					}
				} else {
					postalCodeField.setFocus(true);
					stateFieldListBox.setDisabled(false);
					stateFieldListBox.setSelectedIndex(0);
					popupMessage(widget,
							MarketplaceCockpitsConstants.PIN_CODE_INVALID);
				}
			} catch (Exception e) {
				LOG.error("Exception Occurred  while populating pincode details"
						+ e.getMessage());
			}
		}
	}

	private void popupMessage(
			InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
			final String message) {
		try {
			Messagebox.show(
					LabelUtils.getLabel(widget, message, new Object[0]), INFO,
					Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	/**
	 * @return List<String>
	 */
	private List<String> getAddressRadioTypeList() {
		final List<String> list = new ArrayList<String>();
		list.add(MarketplacecommerceservicesConstants.LIST_VAL_RESIDENTIAL);
		list.add(MarketplacecommerceservicesConstants.LIST_VAL_COMMERCIAL);
		return list;
	}

	/**
	 * It creates a textbox for mobile number
	 * 
	 * @param parent
	 * @return
	 */
	private Textbox createMobileNumberTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("26%");
		textBox.setParent(parent);
		return textBox;
	}

	/**
	 * It creates a textbox for mobile number where +91 will be displayed
	 * 
	 * @param parent
	 * @return
	 */
	private Textbox createPrefixTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("3%");
		textBox.setParent(parent);
		textBox.setValue("+91");
		textBox.setDisabled(true);
		return textBox;
	}

	/**
	 * method to create a Textbox and assign it to the hbox parent
	 * 
	 * @param parent
	 * @return
	 */
	private Textbox createTextbox(final Div parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("30%");
		textBox.setParent(parent);
		return textBox;
	}

	/**
	 * To create the address
	 * 
	 * @param widget
	 * @param firstNameField
	 * @param middleNameField
	 * @param lastNameField
	 * @param addressField
	 * @param address2Field
	 * @param address3Field
	 * @param cityField
	 * @param postalCodeField
	 * @param stateFieldListBox
	 * @param mobileNumberPrefixField
	 * @param mobileNumberField
	 * @param listbox
	 * @param countryListbox
	 * @return
	 */
	private EventListener createCreateClickEventListener(
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
			final Textbox firstNameField, final Textbox lastNameField,
			final Textbox address1Field, final Textbox address2Field,
			final Textbox address3Field, final Textbox landMarkField,
			final Listbox landmarkListbox, final Textbox cityField,
			final Textbox postalCodeField, final Listbox stateFieldListBox,
			final Textbox mobileNumberPrefixField,
			final Textbox mobileNumberField, final Listbox addressTypeListbox,
			final Listbox countryListbox) {
		return new MarketplaceCreateClickEventListener(widget, firstNameField,
				lastNameField, address1Field, address2Field, address3Field,
				landMarkField, landmarkListbox, cityField, postalCodeField,
				stateFieldListBox, mobileNumberPrefixField, mobileNumberField,
				addressTypeListbox, countryListbox);
	}

	protected class MarketplaceCreateClickEventListener implements
	EventListener {

		private final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget;
		private static final String PIN_REGEX = "^([0-9]{6})$";
		private static final String MOBILENUMBER_REGEX = "^[0-9]{10}";
		private final Listbox addressTypeListbox;
		private final Listbox stateFieldListBox;
		private final Listbox countryListbox;
		private final Textbox firstNameField;
		private final Textbox lastNameField;
		private final Textbox address1Field;
		private final Textbox address2Field;
		private final Textbox address3Field;
		private final Textbox landmarkfield;
		private final Listbox landMarkListbox;
		private final Textbox cityField;
		private final Textbox postalCodeField;
		private final Textbox mobileNumberField;
		private Boolean valid = Boolean.TRUE;
		private final Textbox mobileNumberPrefixField;

		public MarketplaceCreateClickEventListener(
				final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
				final Textbox firstNameField, final Textbox lastNameField,
				final Textbox address1Field, final Textbox address2Field,
				final Textbox address3Field, final Textbox landmarkfield,
				final Listbox landMarkListbox, final Textbox cityField,
				final Textbox postalCodeField, final Listbox stateFieldListBox,
				final Textbox mobileNumberPrefixField,
				final Textbox mobileNumberField,
				final Listbox addressTypeListbox, final Listbox countryListbox) {
			this.widget = widget;
			this.addressTypeListbox = addressTypeListbox;
			this.stateFieldListBox = stateFieldListBox;
			this.countryListbox = countryListbox;
			this.firstNameField = firstNameField;
			this.lastNameField = lastNameField;
			this.address1Field = address1Field;
			this.address2Field = address2Field;
			this.address3Field = address3Field;
			this.landmarkfield = landmarkfield;
			this.landMarkListbox = landMarkListbox;
			this.cityField = cityField;
			this.postalCodeField = postalCodeField;
			this.mobileNumberField = mobileNumberField;
			this.mobileNumberPrefixField = mobileNumberPrefixField;
		}

		@Override
		public void onEvent(final Event event) throws InterruptedException {
			handleCreateClickEvent(widget, firstNameField, lastNameField,
					address1Field, address2Field, address3Field, landmarkfield,
					landMarkListbox, cityField, postalCodeField,
					stateFieldListBox, mobileNumberPrefixField,
					mobileNumberField, addressTypeListbox, countryListbox);
		}

		private void handleCreateClickEvent(
				final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
				final Textbox firstNameField, final Textbox lastNameField,
				final Textbox address1Field, final Textbox address2Field,
				final Textbox address3Field, final Textbox landmarkfield,
				final Listbox landMarkListbox, final Textbox cityField,
				final Textbox postalCodeField, final Listbox stateFieldListBox,
				final Textbox mobileNumberPrefixField,
				final Textbox mobileNumberField,
				final Listbox addressTypeListbox, final Listbox countryListbox)
						throws InterruptedException {

			//List<Listitem> items = landMarkListbox.getItems();
			String landMark = StringUtils.EMPTY;
			String selectedLandmark = StringUtils.EMPTY;
			if(null != landMarkListbox && null != landMarkListbox.getSelectedItem() && null != landMarkListbox.getSelectedItem().getValue()) {
				selectedLandmark = (String)landMarkListbox.getSelectedItem().getValue();
			}
			if(null != selectedLandmark && selectedLandmark.equalsIgnoreCase(MarketplaceCockpitsConstants.OTHERS)) {
				if(null != landmarkfield && null != landmarkfield.getValue()) {
					landMark=landmarkfield.getValue();
				}
			}else if(selectedLandmark.equalsIgnoreCase(MarketplaceCockpitsConstants.NO_LANDMARKS_FOUND) || selectedLandmark.equalsIgnoreCase(MarketplaceCockpitsConstants.SELECT_LANDMARK)){
				landMark=null;
			}else {
				landMark =selectedLandmark;
			}
			LOG.debug("landMark = "+landMark);
			if (StringUtils.isBlank(firstNameField.getValue()) || StringUtils.isBlank(firstNameField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "firstNameValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (firstNameField.getValue().length() > 255)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidFirstNameLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				return;
			}
			else if (!MarketplaceCockpitCommonAsciiValidator.validateAlphaWithoutSpaceNoSpCh(firstNameField.getValue()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidFirstNameChar"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				return;
			}
			
			else if (StringUtils.isBlank(lastNameField.getValue()) || StringUtils.isBlank(lastNameField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "lastNameValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (lastNameField.getValue().length() > 255)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidLastNameLength"), LabelUtils.getLabel(widget, FAILED_VALIDATION), 
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			else if (!MarketplaceCockpitCommonAsciiValidator.validateAlphaWithoutSpaceNoSpCh(lastNameField.getValue()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidLastNameChar"), LabelUtils.getLabel(widget, FAILED_VALIDATION), 
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			
			else if (StringUtils.isBlank(address1Field.getValue()) || StringUtils.isBlank(address1Field.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "addressLine1ValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (address1Field.getValue().length() > 255)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidAddress1Length"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
            // commented this code for TISRLEE-1655 start 
			/*else if (StringUtils.isBlank(address2Field.getValue()) || StringUtils.isBlank(address2Field.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "addressLine2ValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}*/
			 // commented this code for TISRLEE-1655 end 
			else if (null != address2Field && null != address2Field.getValue() && address2Field.getValue().length() > 255)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidAddress2Length"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				return;
			}
			// commented this code for TISRLEE-1655 start
			/*else if (StringUtils.isBlank(address3Field.getValue()) || StringUtils.isBlank(address3Field.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "landmarkValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}*/
			// commented this code for TISRLEE-1655 end
			else if (null != address3Field && null != address3Field.getValue() && address3Field.getValue().length() > 255)
			{
					Messagebox.show(LabelUtils.getLabel(widget, "invalidLandMarkLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION), 
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			else if (StringUtils.isBlank(cityField.getValue()) || StringUtils.isBlank(cityField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "cityValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (cityField.getValue().length() > 255)
			{
					Messagebox.show(LabelUtils.getLabel(widget, "invalidCityLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			//INC144316265
			/*else if (!MarketplaceCockpitCommonAsciiValidator.validateAlphaWithoutSpaceNoSpCh(cityField.getValue()))
			{
					Messagebox.show(LabelUtils.getLabel(widget, "invalidCityChar"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
					return;
			}*/
			
			else if (stateFieldListBox.getSelectedItem() == null || stateFieldListBox.getSelectedItem().getLabel().equalsIgnoreCase("Select"))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "stateTypeValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (StringUtils.isBlank(postalCodeField.getValue()) || StringUtils.isBlank(postalCodeField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "postalCodeValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (postalCodeField.getValue().length() > 6)
			{
					Messagebox.show(LabelUtils.getLabel(widget, "invalidPinCodeLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION), 
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			else if (!(postalCodeField.getValue().matches(PIN_REGEX)))
			{
				//valid = true;
				Messagebox.show(LabelUtils.getLabel(widget, "postalCodeValueIncorrect"),LabelUtils.getLabel(widget, FAILED_VALIDATION), 
					   Messagebox.OK, Messagebox.ERROR);
				return;
			}
			else if (StringUtils.isBlank(mobileNumberField.getValue()) || StringUtils.isBlank(mobileNumberField.getValue().trim()))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "mobileNoValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (mobileNumberField.getValue().length() > 10)
			{
				Messagebox.show(LabelUtils.getLabel(widget, "invalidMobileLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				return;
			}
			else if (!(mobileNumberField.getValue().matches(MOBILENUMBER_REGEX)))
			{
				//valid = true;
				Messagebox.show(LabelUtils.getLabel(widget, "mobileNumberValueIncorrect"),
						LabelUtils.getLabel(widget, FAILED_VALIDATION), Messagebox.OK, Messagebox.ERROR);
				return;
			}
			else if (addressTypeListbox.getSelectedItem() == null || addressTypeListbox.getSelectedItem().getLabel().equalsIgnoreCase("Select"))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "addressTypeValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			else if (countryListbox.getSelectedItem() == null || countryListbox.getSelectedItem().getLabel().equalsIgnoreCase("Select"))
			{
				Messagebox.show(LabelUtils.getLabel(widget, "countryValueField"), LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
				//valid = Boolean.FALSE;
				return;
			}
			
			/*Added in R2.3  for landMark validation start */
			else if (null != landMark && landMark.length() > 255)
			{
					Messagebox.show(LabelUtils.getLabel(widget, "invalidLandMarkLength"),LabelUtils.getLabel(widget, FAILED_VALIDATION),
						Messagebox.OK, Messagebox.ERROR);
					return;
			}
			/*Added in R2.3  for landMark validation end */
			else
			{
				valid = true;
			}
			if (valid) {
				// Saving the address
				saveShippingAddress(widget, firstNameField.getValue(),
						lastNameField.getValue(), address1Field.getValue(),
						address2Field.getValue(), address3Field.getValue(),
						landMark, cityField.getValue(),
						postalCodeField.getValue(), stateFieldListBox
						.getSelectedItem().getLabel().toString(),
						mobileNumberField.getValue(), addressTypeListbox
						.getSelectedItem().getLabel().toString(),
						countryListbox.getSelectedItem().getLabel().toString());
				// kill the popup
				try {
					if(isAddressForReturn){
						popupWidgetHelper.getCurrentPopup().getNextSibling().getNextSibling().detach();
					}
					if (!isChangeDeliveryAddress) {
						popupWidgetHelper.dismissCurrentPopup();
						// fire a dispatch event to refresh the page/widget
						widget.getWidgetController().dispatchEvent(
								widget.getControllerCtx(), this, null);
					}
				}catch(Exception e){
					LOG.error("Exception while closing the popUp "+e.getMessage());
				}

			}
		}

		/**
		 * Method to save the address
		 * 
		 * @param widget2
		 * @param firstNameField
		 * @param middleNameField
		 * @param lastNameField
		 * @param addressField
		 * @param address2Field
		 * @param address3Field
		 * @param cityField
		 * @param postalCodeField
		 * @param stateField
		 * @param mobileNumberField
		 * @param addressType
		 * @param country
		 */
		public void saveShippingAddress(
				final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget2,
				final String firstName, final String lastName,
				final String addressLine1, final String addressLine2,
				final String addressLine3, final String LandMark,
				final String city, final String postalCode, final String state,
				final String mobileNumber, final String addressType,
				final String country) {
			final CustomerModel customerModel = (CustomerModel) widget
					.getWidgetController().getCurrentCustomer().getObject();
			final AddressModel deliveryAddress = modelService
					.create(AddressModel.class);
			LOG.debug(" Address : "+firstName+" "+lastName+" "+addressLine1+" "+addressLine2+" "+
					addressLine3+" "+LandMark+" "+city+" "+postalCode+" "+state+" "+mobileNumber+" "+addressType+" "+country+" ");
			
			deliveryAddress.setOwner(customerModel);
			deliveryAddress.setFirstname(firstName);
			deliveryAddress.setLastname(lastName);
			deliveryAddress.setPostalcode(postalCode);
			deliveryAddress.setLine1(addressLine1);
			deliveryAddress.setLine2(addressLine2);
			deliveryAddress.setAddressLine3(addressLine3);
			deliveryAddress.setTown(city);
			deliveryAddress.setCity(city);
			deliveryAddress.setState(state);
			deliveryAddress.setDistrict(state);
			deliveryAddress.setAddressType(addressType);
			deliveryAddress.setLandmark(LandMark);
			deliveryAddress.setBillingAddress(false);
			deliveryAddress.setShippingAddress(true);
			deliveryAddress.setPhone1(mobileNumber);
			deliveryAddress.setCellphone(mobileNumber);
			deliveryAddress.setShippingAddress(Boolean.TRUE);
			deliveryAddress.setBillingAddress(Boolean.TRUE);
			deliveryAddress.setVisibleInAddressBook(Boolean.TRUE);
			
			if (countryListbox.getSelectedItem() != null) {
				for (final CountryModel countryModel : commonI18NService
						.getAllCountries()) {
					if (countryListbox.getSelectedItem().getLabel()
							.equalsIgnoreCase(countryModel.getName())) {
						deliveryAddress.setCountry(countryModel);
					}
				}
			}
			
			try {
				if (isChangeDeliveryAddress) {
					TypedObject order = getOrder();
					OrderModel orderModel = (OrderModel) order.getObject();
					// Storing Delivery Address in a session
					AddressData changeDeliveryAddressData = new AddressData();
					customAddressPopulator.populate(deliveryAddress, changeDeliveryAddressData);
					sessionService.setAttribute(MarketplacecommerceservicesConstants.CHANGE_DELIVERY_ADDRESS, changeDeliveryAddressData);
					String omsStatus = null;
					// pincode Servicibility call To OMS  , only if pincode change
					if (!deliveryAddress
							.getPostalcode()
							.trim()
							.equalsIgnoreCase(
									orderModel.getDeliveryAddress()
									.getPostalcode())) {
						try {
							omsStatus = mplDeliveryAddressController
									.changeDeliveryAddressCallToOMS(orderModel
											.getParentReference().getCode(),
											deliveryAddress, "CDP", null);
						} catch (Exception e) {
							LOG.error("Exception in picode servicibility call for change delivery address for the order "
									+ orderModel.getCode() + "");
						}
						if (null != omsStatus) {
							if (omsStatus
									.equalsIgnoreCase(MarketplaceCockpitsConstants.SUCCESS)) {
								proceedToChangeAddress(widget,
										deliveryAddress);
							} else {
								
								// Saving no_of Total requests and rejects for changeDelivery
								mplDeliveryAddressController.saveChangeDeliveryRequests(orderModel.getParentReference());;
								popupMessage(
										widget, PINCODE_NOT_SERVICIBLE);
								return;
							}
						} else {
							// Saving no_of Total requests and rejects for changeDelivery
							mplDeliveryAddressController.saveChangeDeliveryRequests(orderModel.getParentReference());
							popupMessage(widget, NO_RESPONCE_FROM_SERVER);
						}
					} else {
						proceedToChangeAddress(widget, deliveryAddress);
					}
				} else {
					modelService.save(deliveryAddress);
				}
			} catch (ModelSavingException e) {
				LOG.error("Exception while saving the new Address "
						+ e.getMessage());
			} catch (Exception e) {
				LOG.error("Exception while creating popup window for OTP "
						+ e.getMessage());
			}
		}
	}

	private void proceedToChangeAddress(
			InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> widget,
		AddressModel deliveryAddress) {
			TypedObject orderObject = getOrder();
			if(!mplDeliveryAddressController
			.isDeliveryAddressChangable(orderObject)) {
				OrderModel order = (OrderModel) orderObject.getObject();
				mplDeliveryAddressController.saveChangeDeliveryRequests(order.getParentReference());
				try {
					Messagebox.show(LabelUtils.getLabel(widget, ADDRESS_NOT_CHANGABLE,
							new Object[0]), INFO, Messagebox.OK, Messagebox.ERROR);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				popupWidgetHelper.dismissCurrentPopup();
				// fire a dispatch event to refresh the page/widget
				widget.getWidgetController().dispatchEvent(
						widget.getControllerCtx(), this, null);
			}else {
				createOTPPopupWindow(widget, popupWidgetHelper.getCurrentPopup()
						.getParent(), deliveryAddress);
			}
		
	}

	protected Widget createPopupWidget(WidgetContainer<Widget> widgetContainer,
			String widgetConfig, String popupCode) {
		WidgetConfig popupWidgetConfig = (WidgetConfig) SpringUtil
				.getBean(widgetConfig);
		Map<String, Widget> widgetMap = widgetContainer.initialize(Collections
				.singletonMap(popupCode, popupWidgetConfig));
		return (Widget) widgetMap.get(popupCode);
	}

	private Window createOTPPopupWindow(
			final InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> parentWidget,
			final Component parentWindow,
			final AddressModel deliveryAddress) {
		try {
			LOG.info("Inside createOTPPopupWindow method");
			final WidgetContainer<Widget> widgetContainer = new DefaultWidgetContainer(
					new DefaultWidgetFactory());
			Widget popupWidget = createPopupWidget(widgetContainer,
					"csChangeDeliveryAddressOtpWidgetConfig",
					"csChangeDeliveryAddressOtpWidgetConfig-Popup");
			final Window popup = new Window();
			popup.appendChild(popupWidget);
			popup.addEventListener("onClose", new EventListener() {
				public void onEvent(Event event) {
					handleOTPPopupCloseEvent(deliveryAddress, parentWidget,
							parentWindow, widgetContainer, popup);
				}

				private void handleOTPPopupCloseEvent(
					AddressModel tempororyAddress,
						InputWidget<DefaultMasterDetailListWidgetModel<TypedObject>, CustomerController> parentWidget,
						Component parentWindow,
						WidgetContainer<Widget> widgetContainer, Window popup) {
					widgetContainer.cleanup();
					parentWindow.removeChild(popup);
				}
			});
			popup.setTitle(LabelUtils.getLabel(popupWidget,
					"popup.changeDeliveryConfirmation", new Object[0]));
			popup.setParent(parentWindow);
			popup.doHighlighted();
			popup.setClosable(true);
			popup.setWidth("1150px");
			return popup;
		} catch (Exception e) {
			LOG.error("No bean id found with name csChangeDeliveryAddressOtpWidgetConfig"
					+ e.getMessage());
		}
		return null;
	}

}
