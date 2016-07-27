package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

/**
 * @author Techouts
 */
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
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
import org.zkoss.zul.Window;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceChangeDeliveryAddressController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.facade.data.LandMarksData;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.data.PincodeData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.sms.facades.SendSMSFacade;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.WidgetContainer;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetContainer;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetFactory;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

public class MarketPlaceChangeDeliveryAddressWidgetRenderer
		extends
		AbstractCsWidgetRenderer<Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController>> {

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceChangeDeliveryAddressWidgetRenderer.class);
	private static final String INFO = "info";
	private static final String PIN_REGEX = "^[1-9][0-9]{5}";
	private static final String NAME_REGEX = "[a-zA-Z ]+";
	private static final String ADDRESS_LINE1_REGEX = "[a-zA-Z0-9 ]+";
	private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
	private static final String MOBILENUMBER_REGEX = "^[0-9]{10}";
	private static final int MAX_LENGTH = 20;

	@Autowired
	private FlexibleSearchService flexibleSearhService;
	@Autowired
	private OTPGenericService oTPGenericService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private SendSMSFacade sendSMSFacade;
	@Autowired
	private AccountAddressFacade accountAddressFacade;
	@Autowired
	private PopupWidgetHelper popupWidgetHelper;
	@Autowired
	private DefaultCommonI18NService commonI18NService;
	@Autowired
	private MarketPlaceChangeDeliveryAddressController mplChangeDeliveryAddressController;
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

	protected HtmlBasedComponent createContentInternal(
			final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			HtmlBasedComponent rootContainer) {

		final Div content = new Div();
		content.setClass("changeDeliveryAddress");
		try {
			LOG.info("Inside change delivery Address  createContentInternal method ");
			TypedObject order = getOrder();
			OrderModel ordermodel = modelService.create(OrderModel.class);
			if (null != order && null != order.getObject()) {
				ordermodel = (OrderModel) order.getObject();
			}
			String paymentMode = StringUtils.EMPTY;

			if (mplChangeDeliveryAddressController
					.isDeliveryAddressChangable(order)) {
				if (null != ordermodel.getPaymentTransactions()) {
					PaymentTransactionModel payment = ordermodel
							.getPaymentTransactions().get(0);
					paymentMode = payment.getEntries().get(0).getPaymentMode()
							.getMode();
					LOG.debug("Payment mode = " + paymentMode);
				}

				if (!paymentMode
						.equalsIgnoreCase(MarketplaceCockpitsConstants.PAYMENT_MODE_COD)) {
					Label label = new Label(
							"For prepaid orders please use commerce to change delivery Address");
					content.appendChild(label);
					return content;
				}
				final AddressModel deliveryAddress = ordermodel
						.getParentReference().getDeliveryAddress();
				content.setClass("changeDeliveryAddress");
				// First Name Hbox
				final Hbox firstNameHbox = createHbox(widget, "firstName",
						false, true);
				firstNameHbox.setClass("hbox");
				final Textbox firstNameFieldTextBox = createTextbox(firstNameHbox);
				firstNameFieldTextBox.setMaxlength(MAX_LENGTH);
				firstNameFieldTextBox.setValue(deliveryAddress.getFirstname()
						.toString());
				content.appendChild(firstNameHbox);

				// Last name hbox
				final Hbox lastNameHbox = createHbox(widget, "lastName", false,
						true);
				final Textbox lastNameFieldTextBox = createTextbox(lastNameHbox);
				lastNameFieldTextBox.setMaxlength(MAX_LENGTH);
				if (null != deliveryAddress.getLastname()) {
					lastNameFieldTextBox.setValue(deliveryAddress.getLastname()
							.toString());
				}
				lastNameHbox.setClass("hbox");
				content.appendChild(lastNameHbox);

				// line1 hbox
				final Hbox line1Hbox = createHbox(widget, "line1", false, true);
				final Textbox line1FieldTextBox = createTextbox(line1Hbox);
				line1FieldTextBox.setMaxlength(MAX_LENGTH);
				if (null != deliveryAddress.getLine1()) {
					line1FieldTextBox.setValue(deliveryAddress.getLine1()
							.toString());
				}
				line1Hbox.setClass("hbox");
				content.appendChild(line1Hbox);

				// line2 hbox
				final Hbox line2Hbox = createHbox(widget, "line2", false, true);
				final Textbox line2FieldTextBox = createTextbox(line2Hbox);
				line2FieldTextBox.setMaxlength(MAX_LENGTH);
				if (null != deliveryAddress.getLine2()) {
					line2FieldTextBox.setValue(deliveryAddress.getLine2()
							.toString());
				}
				line2Hbox.setClass("hbox");
				content.appendChild(line2Hbox);

				// line 3
				final Hbox line3Hbox = createHbox(widget, "line3", false, true);
				final Textbox line3FieldTextBox = createTextbox(line3Hbox);
				line3FieldTextBox.setMaxlength(MAX_LENGTH);
				if (null != deliveryAddress.getAddressLine3()) {
					line3FieldTextBox.setValue(deliveryAddress
							.getAddressLine3().toString());
				}
				line3Hbox.setClass("hbox");
				content.appendChild(line3Hbox);

				final Hbox emailHbox = createHbox(widget, "Email", false, true);
				final Textbox emailFieldTextBox = createTextbox(emailHbox);
				if (null != deliveryAddress.getEmail()) {
					line2FieldTextBox.setValue(deliveryAddress.getEmail()
							.toString());
				}
				emailHbox.setClass("hbox");
				content.appendChild(emailHbox);

				// pincode
				final Hbox pincodeHbox = createHbox(widget, "pincode", false,
						true);
				final Textbox pincodeFieldTextBox = createTextbox(pincodeHbox);
				if (null != deliveryAddress.getPostalcode()) {
					pincodeFieldTextBox.setValue(deliveryAddress
							.getPostalcode().toString());
				}
				pincodeHbox.setClass("hbox");
				content.appendChild(pincodeHbox);
				pincodeFieldTextBox.setMaxlength(Integer.valueOf(LabelUtils
						.getLabel(widget, "maxLength", new Object[0])));
				PincodeData pincodeData = new PincodeData();
				pincodeData = mplChangeDeliveryAddressController
						.getPincodeData(pincodeFieldTextBox.getValue());

				// Country
				Hbox CountryHbox = createHbox(widget, "country", false, true);
				final Textbox countryFieldTextBox = createTextbox(CountryHbox);
				countryFieldTextBox.setMaxlength(MAX_LENGTH);
				if (null != deliveryAddress.getCountry().getName()) {
					countryFieldTextBox.setValue(deliveryAddress.getCountry()
							.getName().toString());
				}
				CountryHbox.setClass("hbox");
				content.appendChild(CountryHbox);

				// State
				final Hbox stateHbox = createHbox(widget, "state", false, true);
				final Textbox stateFieldTextBox = createTextbox(stateHbox);
				stateFieldTextBox.setMaxlength(MAX_LENGTH);
				if (null != deliveryAddress.getDistrict()) {
					stateFieldTextBox.setValue(deliveryAddress.getDistrict()
							.toString());
				}else if (null != deliveryAddress.getState()) {
					stateFieldTextBox.setValue(deliveryAddress.getState()
							.toString());
				}
				stateHbox.setClass("hbox");
				content.appendChild(stateHbox);

				// City
				Hbox cityHbox = createHbox(widget, "city", false, true);
				final Textbox cityFieldTextBox = createTextbox(cityHbox);
				cityFieldTextBox.setMaxlength(MAX_LENGTH);
				if (null != deliveryAddress.getTown()) {
					cityFieldTextBox.setValue(deliveryAddress.getTown()
							.toString());
				} else if (null != deliveryAddress.getCity()) {
					cityFieldTextBox.setValue(deliveryAddress.getCity()
							.toString());
				}
				cityHbox.setClass("hbox");
				content.appendChild(cityHbox);
				// landMark
				Hbox landMarkHbox = createHbox(widget, "landMark", false, true);
				final Textbox landMarkTextBox = new Textbox();
				landMarkTextBox.setMaxlength(MAX_LENGTH);
				if (null != deliveryAddress.getLandmark()) {
					landMarkTextBox.setValue(deliveryAddress.getLandmark());
				}
				landMarkHbox.appendChild(landMarkTextBox);
				content.appendChild(landMarkHbox);
				landMarkHbox.setClass("hbox");
				content.appendChild(landMarkHbox);
				final Listbox landMarkListbox = new Listbox();
				Collection<LandMarksData> landMarks = pincodeData
						.getLandMarks();
				createLandMarkListBox(widget, landMarkHbox, landMarkListbox,
						landMarks);
				landMarkHbox.setClass("hbox");
				landMarkListbox.addEventListener(
						Events.ON_SELECT,
						createLandMarkChangeEventListener(widget,
								landMarkListbox, landMarkTextBox));
				final Hbox mobileNumberHbox = createHbox(widget,
						"mobileNumber", false, true);
				final Textbox mobileNumberFieldTextBox = createTextbox(mobileNumberHbox);
				mobileNumberFieldTextBox.setMaxlength(10);
				if (null != deliveryAddress.getPhone1()) {
					mobileNumberFieldTextBox.setValue(deliveryAddress
							.getPhone1());
				}
				mobileNumberHbox.setClass("hbox");
				content.appendChild(mobileNumberHbox);
				Div division = new Div();
				content.appendChild(division);
				pincodeFieldTextBox.addEventListener(
						Events.ON_BLUR,
						createAddPinCodeListener(widget, pincodeFieldTextBox,
								cityFieldTextBox, stateFieldTextBox,
								landMarkListbox, landMarkTextBox, cityHbox,
								stateHbox, CountryHbox, landMarkHbox));
				Div div = new Div();
				final Button update = new Button(LabelUtils.getLabel(widget,
						"Update"));
				div.setClass("updateDeliveryAddress");
				div.appendChild(update);
				content.appendChild(div);
				update.addEventListener(Events.ON_CLICK, new EventListener() {
					@Override
					public void onEvent(final Event event)
							throws InterruptedException, ParseException,
							InvalidKeyException, NoSuchAlgorithmException {

						handleUpdateDetails(widget, deliveryAddress,
								firstNameFieldTextBox, lastNameFieldTextBox,
								emailFieldTextBox, line1FieldTextBox,
								line2FieldTextBox, line3FieldTextBox,
								pincodeFieldTextBox, countryFieldTextBox,
								stateFieldTextBox, cityFieldTextBox,
								landMarkListbox, landMarkTextBox,
								mobileNumberFieldTextBox, content);
					}
				});
				return content;

			} else {
				Label label = new Label("Delivery Address Not Changable");
				content.appendChild(label);
				content.setHeight("200px");
				return content;
			}

		} catch (UiException e) {
			LOG.error(" UiException occurred " + e.getMessage());
		} catch (NullPointerException e) {
			LOG.error(" NullPointerException Exception " + e.getMessage());
		} catch (FlexibleSearchException e) {
			LOG.error(" FlexibleSearchException Exception : no result for the given pincode "
					+ e.getMessage());
		} catch (Exception e) {
			LOG.error("Exception occurred " + e.getMessage());
		}
		return rootContainer;
	}

	// Landmark Event Listener
	private EventListener createLandMarkChangeEventListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Listbox landMarkListbox, Textbox landMarkTextbox) {

		return new AddLandMarkChangeEventListener(widget, landMarkListbox,
				landMarkTextbox);
	}

	protected class AddLandMarkChangeEventListener implements EventListener {

		private final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget;

		private Listbox landMarkListbox;
		private Textbox landMarkTextbox;

		public AddLandMarkChangeEventListener(
				Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
				Listbox landMarkListbox, Textbox landMarkTextbox) {
			this.widget = widget;
			this.landMarkListbox = landMarkListbox;
			this.landMarkTextbox = landMarkTextbox;
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
				List<Listitem> items = landMarkListbox.getItems();
				String selectedLandmark = StringUtils.EMPTY;
				for (Listitem item : items) {
					if (item.isSelected()) {
						selectedLandmark = (String) item.getValue();
					}
				}
				if (null != selectedLandmark
						&& selectedLandmark
								.equalsIgnoreCase(MarketplaceCockpitsConstants.NONE_OF_ABOVE)) {
					landMarkTextbox.setDisabled(false);
				} else {
					landMarkTextbox.setDisabled(true);
				}
			} catch (NullPointerException e) {
				LOG.error("NullPointerException Occurred " + e.getMessage());
			} catch (Exception e) {
				LOG.error("Exception Occurred " + e.getMessage());
			}
		}
	}

	private Listbox createLandMarkListBox(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Hbox landMarkHbox, Listbox landMarkListbox,
			Collection<LandMarksData> landMarks) {
		if (null != landMarkListbox && null != landMarkListbox.getItems()) {
			landMarkListbox.getItems().clear();
		}
		landMarkListbox.setMultiple(false);
		landMarkListbox.setMold("select");
		for (final LandMarksData landMark : landMarks) {
			final Listitem listItem = new Listitem(landMark.getLandmark());
			listItem.setValue(landMark.getLandmark());
			listItem.setParent(landMarkListbox);
			landMarkListbox.addItemToSelection(listItem);
		}
		Listitem listItem = new Listitem(
				MarketplaceCockpitsConstants.NONE_OF_ABOVE);
		listItem.setValue(MarketplaceCockpitsConstants.NONE_OF_ABOVE);
		listItem.setParent(landMarkListbox);
		landMarkListbox.addItemToSelection(listItem);
		landMarkListbox.setSelectedIndex(landMarkListbox.getItemCount() - 1);
		landMarkHbox.appendChild(landMarkListbox);

		return landMarkListbox;
	}

	private EventListener createAddPinCodeListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Textbox pincodeFieldTextBox, Textbox cityFieldTextBox,
			Textbox stateFieldTextBox, Listbox landMarksListBox,
			Textbox landMarkTextBox, Hbox cityHbox, Hbox stateHbox,
			Hbox countryHbox, Hbox landMarkHbox) {

		return new AddPinCodeEventListener(widget, pincodeFieldTextBox,
				cityFieldTextBox, stateFieldTextBox, landMarksListBox,
				landMarkTextBox, cityHbox, stateHbox, countryHbox, landMarkHbox);
	}

	protected class AddPinCodeEventListener implements EventListener {

		private final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget;

		private final Textbox pincodeValue;
		private Textbox cityFieldTextBox;
		private Textbox stateFieldTextBox;
		private Listbox landMarkListBox;
		private Textbox landMarkTextBox;
		private Hbox cityHbox;
		private Hbox stateHbox;
		private Hbox countryHbox;
		private Hbox landMarkHbox;

		public AddPinCodeEventListener(
				Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
				Textbox pincodeValue, Textbox cityFieldTextBox,
				Textbox stateFieldTextBox, Listbox landMarkListBox,
				Textbox landMarkTextBox, Hbox cityHbox, Hbox stateHbox,
				Hbox countryHbox, Hbox landMarkHbox) {
			this.widget = widget;
			this.pincodeValue = pincodeValue;
			this.cityFieldTextBox = cityFieldTextBox;
			this.stateFieldTextBox = stateFieldTextBox;
			this.landMarkListBox = landMarkListBox;
			this.landMarkTextBox = landMarkTextBox;
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
			try {
				String pincode = pincodeValue.getValue();
				if (null != pincode) {
					if (String.valueOf(pincode).matches(PIN_REGEX)) {
						LOG.info("Pin code entered:" + pincode);
						PincodeData pincodeData = new PincodeData();
						try {
							pincodeData.setPincode(pincode);
							pincodeData = mplChangeDeliveryAddressController
									.getPincodeData(pincode);
						} catch (Exception e) {
							LOG.error("FlexibleSearchException No result for the given pincode "
									+ pincodeValue.getValue());
						}
						if (null != pincodeData) {
							if (null != pincodeData.getCityName()) {
								cityFieldTextBox.setValue(pincodeData
										.getCityName());
							} else {
								cityFieldTextBox
										.setValue(MarketplacecommerceservicesConstants.EMPTY);
							}
							StateData stateData = pincodeData.getState();
							if (null != stateData
									&& null != stateData.getName()) {
								stateFieldTextBox.setValue(stateData.getName());
							} else {
								stateFieldTextBox
										.setValue(MarketplacecommerceservicesConstants.EMPTY);
							}
							if (null != pincodeData.getLandMarks()) {
								landMarkListBox.setDisabled(false);
								landMarkTextBox.setDisabled(true);
								createLandMarkListBox(widget, landMarkHbox,
										landMarkListBox,
										pincodeData.getLandMarks());
							} else {
								landMarkListBox.getItems().clear();
								landMarkListBox.setDisabled(true);
								landMarkTextBox.setDisabled(false);
							}
						}
					} else {
						landMarkTextBox.setDisabled(false);
						cityFieldTextBox.setValue(StringUtils.EMPTY);
						stateFieldTextBox.setValue(StringUtils.EMPTY);
						if (null != landMarkListBox.getItems()) {
							landMarkListBox.getItems().clear();
						}
						LOG.info("Pincode is Not Servisable ");
					}
				} else {
					pincodeValue.setValue(StringUtils.EMPTY);
					pincodeValue.setFocus(true);
					popupMessage(widget,
							MarketplaceCockpitsConstants.PIN_CODE_INVALID);
				}
			} catch (NullPointerException e) {
				LOG.error("NullPointerException Occurred " + e.getMessage());
			} catch (Exception e) {
				LOG.error("Exception Occurred " + e.getMessage());
			}
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

	public void handleUpdateDetails(
			final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			final AddressModel deliveryAddress,
			final Textbox firstNameFieldTextBox,
			final Textbox lastNameFieldTextBox,
			final Textbox emailFieldTextBox, final Textbox line1FieldTextBox,
			final Textbox line2FieldTextBox, final Textbox line3FieldTextBox,
			final Textbox pincodeFieldTextBox,
			final Textbox countryFieldTextBox, final Textbox stateFieldTextBox,
			final Textbox cityFieldTextBox, final Listbox landMarkListItem,
			final Textbox landMarkFieldTextBox,
			final Textbox mobileNumberFieldTextBox, Div content)
			throws InterruptedException, ParseException, InvalidKeyException,
			NoSuchAlgorithmException {
		LOG.info("Update button clicked ");
		final String changedFirstName = firstNameFieldTextBox.getValue();
		final String changedLastName = lastNameFieldTextBox.getValue();
		final String changedEmail = emailFieldTextBox.getValue();
		final String changedLine1 = line1FieldTextBox.getValue();
		final String changedLine2 = line2FieldTextBox.getValue();
		final String changedLine3 = line3FieldTextBox.getValue();
		final String changedPincode = pincodeFieldTextBox.getValue();
		final String changedCountry = countryFieldTextBox.getValue().toString();
		final String changedState = stateFieldTextBox.getValue().toString();
		final String changedCity = cityFieldTextBox.getValue().toString();
		String changedLandMark = StringUtils.EMPTY;
		PincodeModel pincodeModel = modelService.create(PincodeModel.class);
		pincodeModel.setPincode(changedPincode);

		if (!landMarkFieldTextBox.isDisabled()) {
			if (null != landMarkFieldTextBox.getValue()) {
				changedLandMark = landMarkFieldTextBox.getValue();
			}
		} else {
			List<Listitem> items = landMarkListItem.getItems();
			for (Listitem item : items) {

				if (item.isSelected()) {
					changedLandMark = (String) item.getValue();
					if (changedLandMark
							.equalsIgnoreCase(MarketplaceCockpitsConstants.NONE_OF_ABOVE)) {
						changedLandMark = null;
					}
				}
			}
		}
		final String changedMobileNumber = mobileNumberFieldTextBox.getValue();
		TypedObject order = getOrder();
		final OrderModel ordermodel = (OrderModel) order.getObject();
		AbstractOrderModel parentOrder = ordermodel.getParentReference();
		final TemproryAddressModel tempororyAddress = modelService
				.create(TemproryAddressModel.class);
		tempororyAddress.setOrderId(parentOrder.getCode());
		CustomerModel customer = (CustomerModel) callContextController
				.getCurrentCustomer().getObject();
		tempororyAddress.setOwner(customer);
		LOG.info("Change Delivery Address Details");
		if (changedFirstName != null && !changedFirstName.isEmpty()) {
			tempororyAddress.setFirstname(changedFirstName);
			LOG.debug(" First Name =" + changedFirstName);
		} else {
			tempororyAddress
					.setFirstname(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedLastName != null && !changedLastName.isEmpty()) {
			tempororyAddress.setLastname(changedLastName);
			LOG.debug(" last Name =" + changedLastName);
		} else {
			tempororyAddress
					.setLastname(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedEmail != null && !changedEmail.isEmpty()) {
			tempororyAddress.setEmail(changedEmail);
			LOG.debug(" Email =" + changedEmail);
		} else {
			tempororyAddress
					.setEmail(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedLine1 != null && !changedLine1.isEmpty()) {
			tempororyAddress.setLine1(changedLine1.toString());

		} else {
			tempororyAddress
					.setLine1(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedLine2 != null && !changedLine2.isEmpty()) {
			LOG.debug("Line2 =" + changedLine2);
			tempororyAddress.setLine2(changedLine2);
		} else {
			tempororyAddress
					.setLine2(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedLine3 != null && !changedLine3.isEmpty()) {
			tempororyAddress.setAddressLine3(changedLine3);
		} else {
			tempororyAddress
					.setAddressLine3(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedPincode != null && !changedPincode.isEmpty()) {
			tempororyAddress.setPostalcode(changedPincode);
		} else {
			tempororyAddress
					.setPostalcode(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedState != null && !changedState.isEmpty()) {
			tempororyAddress.setState(changedState);
			LOG.debug(" State Name =" + changedState);
		} else {
			tempororyAddress
					.setState(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedCity != null && !changedCity.isEmpty()) {
			LOG.debug(" City Name =" + changedCity);
			tempororyAddress.setCity(changedCity);
		} else {
			tempororyAddress
					.setCity(MarketplacecommerceservicesConstants.EMPTY);
		}

		if (changedCountry != null && !changedCountry.isEmpty()) {
			CountryModel countrymodel = commonI18NService.getCountry("IN");
			Locale loc = new Locale("en");
			countrymodel.setName(changedCountry, loc);

			tempororyAddress.setCountry(countrymodel);
		}
		if (changedCity != null && !changedCity.isEmpty()) {
			tempororyAddress.setCity(changedCity);
		} else {
			tempororyAddress
					.setCity(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedLandMark != null && !changedLandMark.isEmpty()) {
			tempororyAddress.setLandmark(changedLandMark);
			LOG.debug(" LandMark :" + changedLandMark);
		} else {
			tempororyAddress
					.setLandmark(MarketplacecommerceservicesConstants.EMPTY);
		}
		if (changedMobileNumber != null) {
			tempororyAddress.setPhone1(changedMobileNumber);
			LOG.debug(" Mobile Number :" + changedMobileNumber);
		} else {
			tempororyAddress
					.setPhone1(MarketplacecommerceservicesConstants.EMPTY);
		}
		String validatemessage = validatenewDeliveryAddress(tempororyAddress);
		if (!validatemessage
				.equalsIgnoreCase(MarketplaceCockpitsConstants.SUCCESS)) {
			popupMessage(widget, validatemessage);
		} else {
			try {
				proceedToChangeAddress(widget, tempororyAddress);
			} catch (Exception e) {
				LOG.error("Exception occurred " + e.getMessage());
			}
		}
	}

	private void proceedToChangeAddress(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			TemproryAddressModel tempororyAddress) {
		modelService.save(tempororyAddress);
		createOTPPopupWindow(widget, popupWidgetHelper.getCurrentPopup()
				.getParent(), tempororyAddress);
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
			final Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> parentWidget,
			final Component parentWindow,
			final TemproryAddressModel tempororyAddress) {
		final WidgetContainer<Widget> widgetContainer = new DefaultWidgetContainer(
				new DefaultWidgetFactory());
		Widget popupWidget = createPopupWidget(widgetContainer,
				"csChangeDeliveryAddressOtpWidgetConfig",
				"csChangeDeliveryAddressOtpWidgetConfig-Popup");
		final Window popup = new Window();
		popup.appendChild(popupWidget);
		popup.addEventListener("onClose", new EventListener() {
			public void onEvent(Event event) {
				handleOTPPopupCloseEvent(tempororyAddress, parentWidget,
						parentWindow, widgetContainer, popup);
			}

			private void handleOTPPopupCloseEvent(
					TemproryAddressModel tempororyAddress,
					Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> parentWidget,
					Component parentWindow,
					WidgetContainer<Widget> widgetContainer, Window popup) {
				widgetContainer.cleanup();
				parentWindow.removeChild(popup);
				modelService.remove(tempororyAddress);
			}
		});
		popup.setTitle(LabelUtils.getLabel(popupWidget,
				"popup.changeDeliveryConfirmation", new Object[0]));
		popup.setParent(parentWindow);
		popup.doHighlighted();
		popup.setClosable(true);
		popup.setWidth("900px");
		return popup;
	}

	// }catch (Exception e) {
	// LOG.error("Exception occurred " + e.getMessage());
	// }
	//

	private String validatenewDeliveryAddress(
			TemproryAddressModel newDeliveryAddress) {
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getFirstname())
				|| !newDeliveryAddress.getFirstname().matches(NAME_REGEX)) {
			return MarketplaceCockpitsConstants.FIRST_NAME_NOT_VALID;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getLastname())
				|| !newDeliveryAddress.getLastname().matches(NAME_REGEX)) {
			return MarketplaceCockpitsConstants.LAST_NAME_NOT_VALID;
		}
		if (StringUtils.isNotEmpty(newDeliveryAddress.getEmail())
				&& !newDeliveryAddress.getEmail().matches(EMAIL_REGEX)) {
			return MarketplaceCockpitsConstants.EMAIL_NOT_VALID;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getLine1())
				|| !newDeliveryAddress.getLine1().matches(ADDRESS_LINE1_REGEX)) {
			return MarketplaceCockpitsConstants.LINE1_NOT_VALID;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getAddressLine3())
				|| !newDeliveryAddress.getAddressLine3().matches(
						ADDRESS_LINE1_REGEX)) {
			return MarketplaceCockpitsConstants.LINE3_NOT_VALID;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getLine2())
				|| !newDeliveryAddress.getLine2().matches(ADDRESS_LINE1_REGEX)) {
			return MarketplaceCockpitsConstants.LINE2_NOT_VALID;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getCity())
				|| !newDeliveryAddress.getCity().matches(NAME_REGEX)) {
			return MarketplaceCockpitsConstants.CITY_NOT_VALID;
		}
		if (null == newDeliveryAddress.getCountry()
				|| StringUtils.isNotEmpty(newDeliveryAddress.getCountry()
						.getName())
				&& !newDeliveryAddress.getCountry().getName()
						.matches(NAME_REGEX)) {
			return MarketplaceCockpitsConstants.COUNTRY_NOT_VALID;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getState())
				|| !newDeliveryAddress.getState().matches(NAME_REGEX)) {
			return MarketplaceCockpitsConstants.STATE_NOT_VALID;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getAddressLine3())
				|| !newDeliveryAddress.getAddressLine3().matches(
						ADDRESS_LINE1_REGEX)) {
			return MarketplaceCockpitsConstants.LINE3_NOT_VALID;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getPostalcode())) {
			return MarketplaceCockpitsConstants.PIN_CODE_EMPTY;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getPhone1())
				|| !newDeliveryAddress.getPhone1().matches(MOBILENUMBER_REGEX)) {
			return MarketplaceCockpitsConstants.MOBILENUMBER_NOT_VALID;
		}
		if (!StringUtils.isNotEmpty(newDeliveryAddress.getLandmark())
				|| !newDeliveryAddress.getLandmark().matches(
						ADDRESS_LINE1_REGEX)) {
			return MarketplaceCockpitsConstants.LAND_MARK_NOT_VALID;
		} else {
			return MarketplaceCockpitsConstants.SUCCESS;
		}
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

	private Textbox createTextbox(final Hbox parent) {
		final Textbox textBox = new Textbox();
		textBox.setFocus(true);
		textBox.setWidth("170px");
		textBox.setParent(parent);
		return textBox;
	}

	private Hbox createHbox(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			String attributeLabel, boolean hidden, final boolean overwriteWidth) {
		final Hbox hbox = new Hbox();
		if (overwriteWidth) {
			hbox.setWidths("11em, none");
		}
		hbox.setAlign("center");
		if (hidden) {
			hbox.setVisible(false);
		}
		final Label label = new Label(LabelUtils.getLabel(widget,
				attributeLabel));
		label.setFocus(true);
		label.setParent(hbox);
		return hbox;
	}
}