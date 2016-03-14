package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zhtml.Span;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceCheckoutController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OTPModel;
import com.tisl.mpl.data.OTPResponseData;
import com.tisl.mpl.enums.OTPTypeEnum;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.BlacklistService;
import com.tisl.mpl.marketplacecommerceservices.service.CODPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplVoucherService;
import com.tisl.mpl.marketplacecommerceservices.service.OTPGenericService;
import com.tisl.mpl.sms.facades.SendSMSFacade;
import com.tisl.mpl.util.ExceptionUtil;

import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutPaymentWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.CheckoutPaymentWidgetRenderer;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketplaceCheckoutPaymentWidgetRenderer.
 */
public class MarketplaceCheckoutPaymentWidgetRenderer extends
		CheckoutPaymentWidgetRenderer {

	@Autowired
	private ModelService modelService;
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger
			.getLogger(MarketplaceCheckoutPaymentWidgetRenderer.class);
	
	/** The Constant CUSTOMER_IS_BLACKLISTED. */
	private static final String CUSTOMER_IS_BLACKLISTED = "customerisblacklisted";
	
	/** The Constant INFO. */
	private static final String INFO = "info";
	
	/** The Constant SELECT_PAYMENT_ADDRESS. */
	private static final String SELECT_PAYMENT_ADDRESS = "selectpaymentaddress";
	
	/** The Constant SELECT_DELIVERY_MODE. */
	private static final String SELECT_DELIVERY_MODE = "selectdeliverymode";
	
	/** The Constant OTP_IS_SENT. */
	private static final String OTP_IS_SENT = "otpissent";
	
	/** The Constant INVALID_OTP. */
	private static final String INVALID_OTP = "invalidotp";
	
	/** The Constant MOBILE_NUMBER_REQUIRED. */
	private static final String MOBILE_NUMBER_REQUIRED = "mobilenumberrequired";
	
	/** The Constant ENTER_OTP. */
	private static final String ENTER_OTP = "enterotp";
	
	/** The c od payment service. */
	private CODPaymentService cODPaymentService;
	
	/** The o tp generic service. */
	private OTPGenericService oTPGenericService;
	

	/** The blacklist service. */
	@Autowired
	private BlacklistService blacklistService;
	
	/** The sms send facade. */
	@Autowired
	private SendSMSFacade sendSMSFacade;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Resource(name = "mplVoucherService")
	private MplVoucherService mplVoucherService;	
	
	/**
	 * Creates the content internal.
	 *
	 * @param widget the widget
	 * @param rootContainer the root container
	 * @return the html based component
	 */
	protected HtmlBasedComponent createContentInternal(
			final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
			HtmlBasedComponent rootContainer) {
		
		 CartModel  cart = (CartModel)widget.getWidgetController().getBasketController().getCart().getObject();
		 OTPModel opt = getoTPGenericService().getLatestOTPModel(cart.getUser().getUid(), OTPTypeEnum.COD);
		//boolean isCODEligible = ((MarketplaceCheckoutController)widget.getWidgetController()).checkDeliveryModeCODLimit(cart,widget);
		Div div = new 	Div();
		Hbox hbox = new Hbox();
		try {
				Div paymentModeDropdownContent = new Div();
				
				createPaymentModeField(widget,paymentModeDropdownContent);
				
				Button generateOTP = new Button(LabelUtils.getLabel(
						widget, "generateButton"));
				generateOTP.setDisabled(cart.getPaymentInfo()==null);
				generateOTP.addEventListener(Events.ON_CLICK, new EventListener() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						handleGenerateOTP(widget);
					}
				});
				generateOTP.setParent(paymentModeDropdownContent);
				
				hbox.appendChild(paymentModeDropdownContent);
				
				div.appendChild(hbox);
				hbox = new Hbox();
				
				boolean oTPValidated =!( opt==null ||( opt.getIsValidated() !=null && opt.getIsValidated().booleanValue()));
				
				Div otparea = new Div();
				otparea.setVisible(oTPValidated);
				Textbox OTPTextBox = new Textbox();
				OTPTextBox.setMaxlength(8);
				
				OTPTextBox.setParent(otparea);
				
				
				Button validateOTP = new Button(LabelUtils.getLabel(
						widget, "validateButton"));
				
				validateOTP.setParent(otparea);
				
				hbox.appendChild(otparea);
				
				validateOTP.addEventListener(
						Events.ON_CLICK,
						createValidateOTPEventListener(widget,
								OTPTextBox));
				div.appendChild(hbox);
				div.setParent(rootContainer);
			
		}catch(ValidationException ex) {
			try {
				Messagebox.show(ex.getMessage(), LabelUtils.getLabel(widget, "failedToCreatePayment", new Object[0]), 1, "z-msgbox z-msgbox-error");
			} catch (InterruptedException e) {
				LOG.error("EtailNonBusinessExceptions in createContentInternal:",ex);
			}
		}catch(EtailNonBusinessExceptions ex) {
			LOG.error("EtailNonBusinessExceptions in createContentInternal:",ex);
			popupMessage(widget, MarketplaceCockpitsConstants.SERVER_ERROR+":"+ex,Messagebox.ERROR);
		} catch(ClientEtailNonBusinessExceptions e) {
			LOG.error("ClientEtailNonBusinessExceptions in createContentInternal:",e);
			popupMessage(widget, MarketplaceCockpitsConstants.SERVER_ERROR+":"+e,Messagebox.ERROR);
		}
		return div;
	}


	/**
	 * Creates the payment event listener.
	 *
	 * @param widget the widget
	 * @param rootContainer the root container
	 * @param PaymentModeDropdown the payment mode dropdown
	 * @return the event listener
	 */
	private EventListener createPaymentEventListener(
			DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget) {
		return new PaymentEventListener(widget);
	}

	/**
	 * Creates the payment mode field.
	 *
	 * @param widget the widget
	 * @param parent the parent
	 * @return the listbox
	 * @throws ValidationException 
	 */
	protected Listbox createPaymentModeField(
			final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
			final Div parent) throws ValidationException {
		return createSearchDropdownField(widget, parent, "paymentModeText");

	}

	/**
	 * Creates the search dropdown field.
	 *
	 * @param <E> the element type
	 * @param widget the widget
	 * @param parent the parent
	 * @param labelKey the label key
	 * @param enumType the enum type
	 * @param currentValue the current value
	 * @param cssClass the css class
	 * @return the listbox
	 * @throws ValidationException 
	 */
	protected <E extends Enum<E>> Listbox createSearchDropdownField(
			final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
			final Div parent, final String labelKey) throws ValidationException {
		final Span span = new Span();
		//span.setSclass(cssClass);
		final Label label = new Label(LabelUtils.getLabel(widget, labelKey));
		final Listbox listbox = createListbox(widget);
		span.appendChild(label);
		parent.appendChild(span);
		parent.appendChild(listbox);
		listbox.setRows(1);
		listbox.setSclass("csDeliveryAddressList");
		listbox.addEventListener(
				Events.ON_SELECT,
				createPaymentEventListener(widget));
		return listbox;
	}

	/**
	 * Creates the listbox.
	 *
	 * @param <E> the element type
	 * @param widget the widget
	 * @param enumType the enum type
	 * @param currentValue the current value
	 * @return the listbox
	 * @throws ValidationException 
	 */
	public <E extends Enum<E>> Listbox createListbox(
			final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget) throws ValidationException {
		
		final Listbox listbox = new Listbox();
		//widget.getWidgetController().canCreatePayments();
		CartModel cart = ((CartModel) ((CheckoutController) widget
				.getWidgetController()).getBasketController().getCart()
				.getObject());
		
		if(!modelService.isUpToDate(cart)){
			modelService.refresh(cart);
		}
		
		boolean disabled = true;
		
		try {
			disabled =  !((MarketplaceCheckoutController) widget
					.getWidgetController()).isCartCODEligible();
			
			if (!disabled) {
				// disabled = (null==cart.getCartReservationDate());
				if (null != cart.getCartReservationDate()) {
					disabled = false;
				} else {
					disabled = true;
				}
			}
			
		}catch( Exception e){
			LOG.equals(e);
		}
		
		listbox.setDisabled(disabled);
		listbox.setMultiple(false);
		listbox.setMold("select");
		Listitem emptyItem = new Listitem(LabelUtils.getLabel(widget, "defaultSelectOption",
				new Object[0]), null);
		emptyItem.setParent(listbox);
		emptyItem.setSelected(true);
		
		for (int i = 0; i < 1; i++) {
			Listitem listItem = listbox.appendItem(OTPTypeEnum.COD.getCode(),
					OTPTypeEnum.COD.getCode());
			listItem.setValue(OTPTypeEnum.COD.getCode());
					if(cart.getPaymentInfo()!=null && cart.getPaymentInfo() instanceof CODPaymentInfoModel){
						listItem.setSelected(true);
				}
				

		}
		return listbox;
	}

	/**
	 * The listener interface for receiving paymentEvent events.
	 * The class that is interested in processing a paymentEvent
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addPaymentEventListener<code> method. When
	 * the paymentEvent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see PaymentEventEvent
	 */
	protected class PaymentEventListener implements EventListener {

		/** The widget. */
		private DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget;
		
		/**
		 * Instantiates a new payment event listener.
		 *
		 * @param widget the widget
		 * @param rootContainer the root container
		 * @param paymentModeDropdown the payment mode dropdown
		 */
		public PaymentEventListener(
				DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget
				) {
			this.widget = widget;
		}

		/* (non-Javadoc)
		 * @see org.zkoss.zk.ui.event.EventListener#onEvent(org.zkoss.zk.ui.event.Event)
		 */
		@Override
		public void onEvent(Event event) throws ValidationException,PaymentException {
			CartModel cart = ((CartModel) ((CheckoutController) widget
					.getWidgetController()).getBasketController().getCart()
					.getObject());
			if(event instanceof SelectEvent){
				
				Set selectedItems = ((SelectEvent)event).getSelectedItems();
				Listitem selectedItem = null;
				if ((selectedItems != null) && (!(selectedItems.isEmpty()))) {
					selectedItem = (Listitem) selectedItems.iterator().next();
				}

				if (selectedItem == null)
					return;
				String mode = (String) selectedItem.getValue();
				
				
			if ("COD".equalsIgnoreCase(mode)) {
				//
				((MarketplaceCheckoutController) widget.getWidgetController()).canCreatePayments();
				((MarketplaceCheckoutController) widget.getWidgetController()).processCODPayment();
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
				Map data = Collections.singletonMap("refresh", Boolean.TRUE);
				((CheckoutController) widget.getWidgetController()).getBasketController()
						.dispatchEvent(null, widget.getWidgetController(), data);
				} else{
					((MarketplaceCheckoutController) widget.getWidgetController()).removeCODPayment();
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
					Map data = Collections.singletonMap("refresh", Boolean.TRUE);
					((CheckoutController) widget.getWidgetController()).getBasketController()
							.dispatchEvent(null, widget.getWidgetController(), data);
					}
			   
			    }
			

				}

	}

		/**
		 * Creates the validate otp event listener.
		 *
		 * @param widget the widget
		 * @param oTPTextBox the o tp text box
		 * @param userId the user id
		 * @param cart the cart
		 * @return the event listener
		 */
		private EventListener createValidateOTPEventListener(
				DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
				Textbox oTPTextBox) {

			return new ValidateOTPEventListener(widget, oTPTextBox);
		}


	/**
	 * The listener interface for receiving validateOTPEvent events.
	 * The class that is interested in processing a validateOTPEvent
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addValidateOTPEventListener<code> method. When
	 * the validateOTPEvent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ValidateOTPEventEvent
	 */
	protected class ValidateOTPEventListener implements EventListener {
		
		/** The widget. */
		private DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget;
		
		/** The o tp text box. */
		private Textbox oTPTextBox;
		
		/**
		 * Instantiates a new validate otp event listener.
		 *
		 * @param widget the widget
		 * @param oTPTextBox the o tp text box
		 * @param userId the user id
		 * @param cart the cart
		 */
		public ValidateOTPEventListener(
				DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
				Textbox oTPTextBox) {
			// TODO Auto-generated constructor stub
			super();
			this.widget = widget;
			this.oTPTextBox = oTPTextBox;
		}

		/* (non-Javadoc)
		 * @see org.zkoss.zk.ui.event.EventListener#onEvent(org.zkoss.zk.ui.event.Event)
		 */
		@Override
		public void onEvent(Event event) throws InterruptedException {
			// TODO Auto-generated method stub
			if (!oTPTextBox.getText().isEmpty()) {
				handleValidateOTPEvent(widget, oTPTextBox, event);
			} else {
				Messagebox.show(
						LabelUtils.getLabel(widget, ENTER_OTP, new Object[0]),
						INFO, Messagebox.OK, Messagebox.ERROR);
			}
		}

		/**
		 * Handle validate otp event.
		 *
		 * @param widget the widget
		 * @param oTPTextBox the o tp text box
		 * @param userId the user id
		 * @param cart the cart
		 * @param event the event
		 * @throws InterruptedException the interrupted exception
		 */
		private void handleValidateOTPEvent(
				DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
				Textbox oTPTextBox, Event event)
				throws InterruptedException {
			CartModel cart = ((CartModel) ((CheckoutController) widget
					.getWidgetController()).getBasketController().getCart()
					.getObject());
			long time=0l;
			String userId = cart.getUser().getUid();
			try{
			time=Long.parseLong(configurationService.getConfiguration().getString("OTP_Valid_Time_milliSeconds"));
			
			}catch (NumberFormatException exp){
				LOG.error("on Time limit defined");
			}
			
			
			OTPResponseData otpResponse = oTPGenericService.validateOTP(userId,null,
					oTPTextBox.getValue(), OTPTypeEnum.COD, time);
			boolean validate = otpResponse.getOTPValid();
			
				
			if(validate){	
				if(widget.getWidgetController().needPaymentOption()){
					try {
						((MarketplaceCheckoutController)widget.getWidgetController()).processPayment(cart);
					} catch (PaymentException e) {
						Messagebox
						.show(LabelUtils.getLabel(widget, e.getLocalizedMessage(),
								new Object[0]), INFO, Messagebox.OK,
								Messagebox.ERROR);
					} catch (ValidationException e) {
						Messagebox
						.show(LabelUtils.getLabel(widget, e.getLocalizedMessage(),
								new Object[0]), INFO, Messagebox.OK,
								Messagebox.ERROR);
					}
				}
			Map data = Collections.singletonMap("refresh", Boolean.TRUE);
				((CheckoutController) widget.getWidgetController()).getBasketController()
						.dispatchEvent(null, widget.getWidgetController(), data);
			} else {
				Messagebox
						.show(LabelUtils.getLabel(widget, INVALID_OTP,
								new Object[0]), INFO, Messagebox.OK,
								Messagebox.ERROR);
			}

		}

	}

	/**
	 * Gets the c od payment service.
	 *
	 * @return the c od payment service
	 */
	public CODPaymentService getcODPaymentService() {
		return cODPaymentService;
	}

	/**
	 * Sets the c od payment service.
	 *
	 * @param cODPaymentService the new c od payment service
	 */
	@Required
	public void setcODPaymentService(CODPaymentService cODPaymentService) {
		this.cODPaymentService = cODPaymentService;
	}

	/**
	 * Gets the o tp generic service.
	 *
	 * @return the o tp generic service
	 */
	public OTPGenericService getoTPGenericService() {
		return oTPGenericService;
	}

	/**
	 * Sets the o tp generic service.
	 *
	 * @param oTPGenericService the new o tp generic service
	 */
	@Required
	public void setoTPGenericService(OTPGenericService oTPGenericService) {
		this.oTPGenericService = oTPGenericService;
	}

	/**
	 * Popup message.
	 *
	 * @param widget
	 *            the widget
	 * @param message
	 *            the message
	 */
	private void popupMessage(final 
			DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget,
			final String message,String icon) {
		try {
			Messagebox.show(
					LabelUtils.getLabel(widget, message, new Object[0]),
					"Info", Messagebox.OK, icon);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}

	private void handleGenerateOTP(DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget){
		try {
		CartModel cart = ((CartModel) ((CheckoutController) widget
				.getWidgetController()).getBasketController().getCart()
				.getObject());
		
		String userId = cart.getUser().getUid();
		
		if(cart.getDeliveryAddress()==null ||  cart.getDeliveryAddress().getPhone1()==null) {
			popupMessage(widget, MOBILE_NUMBER_REQUIRED,Messagebox.ERROR);
			return ;
		}
		
		((MarketplaceCheckoutController)widget.getWidgetController()).checkCustomerStatus();
		
	
		String oTPMobileNumber =   cart.getDeliveryAddress().getPhone1();
		
		String smsContent=oTPGenericService.generateOTP(userId, OTPTypeEnum.COD.getCode(),
				oTPMobileNumber);
		final String contactNumber = configurationService.getConfiguration().getString(
				MarketplacecommerceservicesConstants.SMS_SERVICE_CONTACTNO);
		
//		String smsContent = "Your OTP for COD is "
//				+ oTPGenericService.getLatestOTP(userId,
//						OTPTypeEnum.COD);
//		smsSendFacade.sendSms(MarketplacecommerceservicesConstants.SMS_SENDER_ID,
//				 smsContent, oTPMobileNumber);
		String mplCustomerName = (null ==  cart.getUser().getName()) ? "" : cart.getUser().getName();
		
		sendSMSFacade.sendSms(
				MarketplacecommerceservicesConstants.SMS_SENDER_ID,
				MarketplacecommerceservicesConstants.SMS_MESSAGE_COD_OTP
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
								mplCustomerName != null ? mplCustomerName : "There")
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, smsContent)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, contactNumber), oTPMobileNumber);
		
		
		Map data = Collections.singletonMap("refresh", Boolean.TRUE);
		((CheckoutController) widget.getWidgetController()).getBasketController()
				.dispatchEvent(null, widget.getWidgetController(), data);
		popupMessage(widget, OTP_IS_SENT,Messagebox.INFORMATION);
		} catch (Exception e) {
			try {
				Messagebox.show(e.getMessage() + ((e.getCause() == null) ? "" : new StringBuilder(" - ").append(e.getCause().getMessage()).toString()), 
						        LabelUtils.getLabel(widget, "failedToGenerateOTP", new Object[0]), 1, "z-msgbox z-msgbox-error");
			} catch (InterruptedException e1) {
				
			}
		}
	}


	public ConfigurationService getConfigurationService() {
		return configurationService;
	}


	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}


	public MplVoucherService getMplVoucherService() {
		return mplVoucherService;
	}


	public void setMplVoucherService(MplVoucherService mplVoucherService) {
		this.mplVoucherService = mplVoucherService;
	}
	
	
	
	
	
}
