package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

/**
 * @author Techouts
 */

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceChangeDeliveryAddressController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCallContextController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.core.model.TemproryAddressModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.sms.facades.SendSMSFacade;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderManagementActionsWidgetController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

public class MarketPlaceChangeDeliveryOTPWidgetRenderer
		extends
		AbstractCsWidgetRenderer<Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController>> {

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceChangeDeliveryAddressWidgetRenderer.class);
	private static final String CUSTOMER_DETAILS_UPDATED = "customerdetailsupdated";
	private static final String FAILED_AT_OMS = "failedAtOms";
	private static final String AN_ERROR_OCCURRED = "erroroccurred";
	private static final String INFO = "info";
	@Autowired
	private SendSMSFacade sendSMSFacade;
	@Autowired
	private OTPGenericService oTPGenericService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private MarketplaceCallContextController marketplaceCallContextController;
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private PopupWidgetHelper popupWidgetHelper;
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
		final Div otpAreaDiv = new Div();
		TypedObject order = getOrder();
		final OrderModel ordermodel = (OrderModel) order.getObject();
		AbstractOrderModel parentOrder = ordermodel.getParentReference();
		try {
			sendSmsToCustomer(ordermodel);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		final Hbox otpHbox = createHbox(widget, "OTP", false, true);
		otpHbox.setClass("hbox");
		final Textbox OTPTextBox = createTextbox(otpHbox);
		otpAreaDiv.setClass("changeDeliveryotpArea");
		otpHbox.setClass("otpHbox");
		otpHbox.setParent(otpAreaDiv);
		otpAreaDiv.setVisible(true);
		OTPTextBox.setMaxlength(8);
		final Toolbarbutton resendOtp = new Toolbarbutton("Resend OTP");
		resendOtp.setParent(otpHbox);
		resendOtp.setClass("changeDeliveryResentOtp");
		Div validateOtpButtonDiv = new Div();
		validateOtpButtonDiv.setClass("validateOTP");
		Button validateOTP = new Button(LabelUtils.getLabel(widget,
				"validateButton"));
		validateOTP.setClass("validateOTP");
		validateOtpButtonDiv.appendChild(validateOTP);
		validateOtpButtonDiv.setParent(otpAreaDiv);
		TemproryAddressModel tempAddress = new TemproryAddressModel();
		tempAddress.setOrderId(parentOrder.getCode());
		tempAddress = mplChangeDeliveryAddressController
				.getTempororyAddress(parentOrder.getCode());
		resendOtp.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				handleResendOtpButtonClickEvent(ordermodel, resendOtp);
			}
		});
		validateOTP.addEventListener(
				Events.ON_CLICK,
				createValidateOTPEventListener(widget, OTPTextBox, ordermodel,
						tempAddress));
		return otpAreaDiv;
	}

	private Textbox createTextbox(final Hbox parent) {
		final Textbox textBox = new Textbox();
		textBox.setWidth("150px");
		textBox.setParent(parent);
		return textBox;
	}

	private void handleResendOtpButtonClickEvent(OrderModel ordermodel,
			Toolbarbutton resendOtp) throws InvalidKeyException,
			NoSuchAlgorithmException {
		sendSmsToCustomer(ordermodel);
	}

	// To send SMS
	private void sendSmsToCustomer(OrderModel ordermodel)
			throws InvalidKeyException, NoSuchAlgorithmException {
		String oTPMobileNumber = ordermodel.getDeliveryAddress().getPhone1();
		OTPModel OTP = oTPGenericService.getLatestOTPModel(ordermodel.getUser()
				.getUid(), OTPTypeEnum.CDA);
		String userId = ordermodel.getUser().getUid();
		final String contactNumber = configurationService
				.getConfiguration()
				.getString(
						MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);
		String mplCustomerName = (null == ordermodel.getUser().getName()) ? ""
				: ordermodel.getUser().getName();
		String smsContent = oTPGenericService.generateOTP(userId,
				OTPTypeEnum.CDA.getCode(), oTPMobileNumber);

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

	}

	public EventListener createValidateOTPEventListener(
			Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
			Textbox oTPTextBox, OrderModel ordermodel,
			TemproryAddressModel newDeliveryAddress) {
		return new ValidateOTPEventListener(widget, oTPTextBox, ordermodel,
				newDeliveryAddress);
	}

	// Validate OTP

	protected class ValidateOTPEventListener implements EventListener {
		private Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget;
		private Textbox oTPTextBox;
		private OrderModel orderModel;
		private TemproryAddressModel newDeliveryAddress;

		public ValidateOTPEventListener(
				Widget<OrderItemWidgetModel, OrderManagementActionsWidgetController> widget,
				Textbox oTPTextBox, OrderModel orderModel,
				TemproryAddressModel newDeliveryAddress) {
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
				TemproryAddressModel newDeliveryAddress)
				throws InterruptedException {
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
							.getValue(), OTPTypeEnum.CDA, time);
			boolean validate = otpResponse.getOTPValid();
			if (validate) {
				try {
					String omsStatus = null;
					TypedObject customer = marketplaceCallContextController
							.getCurrentCustomer();
					CustomerModel customermodel = (CustomerModel) customer
							.getObject();
					String customerId = customermodel.getUid();
					AddressModel address = new AddressModel();
					address = setNewDeliveryAddress(newDeliveryAddress);
					address.setOwner(customermodel);
					omsStatus = mplChangeDeliveryAddressController
							.changeDeliveryAddressCallToOMS(orderModel
									.getParentReference().getCode(), address);
					if (omsStatus
							.equalsIgnoreCase(MarketplaceCockpitsConstants.SUCCESS)) {

						try {

							mplChangeDeliveryAddressController
									.saveDeliveryAddress(orderModel, address);
							LOG.debug("Delivery Address Changed Successfully");
						} catch (ModelSavingException e) {
							LOG.debug("Model saving Exception" + e.getMessage());
						} catch (Exception e) {
							LOG.debug("Exception while saving Address"
									+ e.getMessage());
						}
						removeTempororyAddress(newDeliveryAddress);

						mplChangeDeliveryAddressController.ticketCreateToCrm(
								orderModel.getParentReference(), customerId,
								MarketplaceCockpitsConstants.SOURCE);
						LOG.debug("CRM Ticket Created for Change Delivery Request");
						Messagebox.show(LabelUtils.getLabel(widget,
								CUSTOMER_DETAILS_UPDATED, new Object[0]), INFO,
								Messagebox.OK, Messagebox.INFORMATION);
						closePopUp();

					} else if (omsStatus
							.equalsIgnoreCase(MarketplaceCockpitsConstants.FAILED)) {
						try {
							removeTempororyAddress(newDeliveryAddress);
							Messagebox.show(LabelUtils.getLabel(widget,
									FAILED_AT_OMS, new Object[0]), INFO,
									Messagebox.OK, Messagebox.ERROR);
							closePopUp();
						} catch (ModelRemovalException e) {
							LOG.error("ModelRemovalException  while removing temprory Address"
									+ e.getMessage());
						} catch (Exception e) {
							LOG.error("Exception occurred " + e.getMessage());
						}
					} else {
						removeTempororyAddress(newDeliveryAddress);
						closePopUp();
						Messagebox.show(LabelUtils.getLabel(widget,
								AN_ERROR_OCCURRED, new Object[0]), INFO,
								Messagebox.OK, Messagebox.ERROR);
					}
				} catch (ModelRemovalException e) {
					LOG.error("ModelRemovalException " + e.getMessage());
				} catch (Exception e) {
					LOG.error("Exception in changeDeliveryAddressCallToOMS"
							+ e.getMessage());
				}
			} else {
				Messagebox.show(LabelUtils.getLabel(widget, "INVALID_OTP",
						new Object[0]), INFO, Messagebox.OK, Messagebox.ERROR);
			}
		}
	}

	private void closePopUp() {
		int i = popupWidgetHelper.getCurrentPopup().getParent().getChildren()
				.size();
		while (i >= 1) {
			popupWidgetHelper
					.getCurrentPopup()
					.getParent()
					.getChildren()
					.remove(popupWidgetHelper.getCurrentPopup().getParent()
							.getChildren().size() - 1);
			i--;

		}
	}

	private void removeTempororyAddress(TemproryAddressModel newDeliveryAddrfess) {
		modelService.remove(newDeliveryAddrfess);

	}

	private AddressModel setNewDeliveryAddress(
			TemproryAddressModel newDeliveryAddress) {
		AddressModel deliveryAddress = new AddressModel();
		deliveryAddress.setCreationtime(new Date());
		if (null != newDeliveryAddress.getFirstname()) {
			deliveryAddress.setFirstname(newDeliveryAddress.getFirstname());
		}
		if (null != newDeliveryAddress.getLastname()) {
			deliveryAddress.setLastname(newDeliveryAddress.getLastname());
		}
		if (null != newDeliveryAddress.getLine1()) {
			deliveryAddress.setLine1(newDeliveryAddress.getLine1());
		}
		if (null != newDeliveryAddress.getLine2()) {
			deliveryAddress.setLine2(newDeliveryAddress.getLine2());
		}
		if (null != newDeliveryAddress.getAddressLine3()) {
			deliveryAddress.setAddressLine3(newDeliveryAddress
					.getAddressLine3());
		}
		if (null != newDeliveryAddress.getEmail()) {
			deliveryAddress.setEmail(newDeliveryAddress.getEmail());
		}
		if (null != newDeliveryAddress.getPostalcode()) {
			deliveryAddress.setPostalcode(newDeliveryAddress.getPostalcode());
		}
		if (null != newDeliveryAddress.getCountry()) {
			deliveryAddress.setCountry(newDeliveryAddress.getCountry());
		}
		if (null != newDeliveryAddress.getCity()) {
			deliveryAddress.setCity(newDeliveryAddress.getCity());
		}
		if (null != newDeliveryAddress.getState()) {
			deliveryAddress.setState(newDeliveryAddress.getState());
		}
		if (null != newDeliveryAddress.getLandmark()) {
			deliveryAddress.setLandmark(newDeliveryAddress.getLandmark());
		}
		if (null != newDeliveryAddress.getPhone1()) {
			deliveryAddress.setPhone1(newDeliveryAddress.getPhone1());
		}
		return deliveryAddress;
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