package com.tisl.mpl.cockpits.cscockpit.widgets.controllers;

import java.util.List;

import org.zkoss.zul.Listbox;

import com.hybris.oms.tata.model.MplBUCConfigurationsModel;
import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsRequestData;
import com.tisl.mpl.mplcommerceservices.service.data.InvReserForDeliverySlotsResponseData;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.exceptions.PaymentException;
import de.hybris.platform.cscockpit.exceptions.ValidationException;
import de.hybris.platform.cscockpit.widgets.controllers.CheckoutController;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutCartWidgetModel;
import de.hybris.platform.cscockpit.widgets.models.impl.CheckoutPaymentWidgetModel;


// TODO: Auto-generated Javadoc
/**
 * The Interface MarketplaceDefaultCheckoutController.
 */
public interface MarketplaceCheckoutController extends
		CheckoutController {

	/**
	 * Gets the response for pin code.
	 *
	 * @param product the product
	 * @param pin the pin
	 * @param isDeliveryDateRequired the is delivery date required
	 * @return the response for pin code
	 * @throws EtailNonBusinessExceptions the etail non business exceptions
	 * @throws ClientEtailNonBusinessExceptions the client etail non business exceptions
	 */
	List<PinCodeResponseData> getResponseForPinCode(ProductModel product,
			String pin, String isDeliveryDateRequired, String ussid)
			throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions;

	/**
	 * Gets the seller details.
	 *
	 * @param productModel the product model
	 * @return the seller details
	 */
	List<String> getSellerDetails(ProductModel productModel);
	
	/**
	 * Sets the current site.
	 */
	void setCurrentSite();
	
	/**
	 * Process pin serviceability.
	 *
	 * @param widget the widget
	 * @param deliveryModeDropdown the delivery mode dropdown
	 * @param pinCode the pin code
	 * @param product the product
	 * @param isDeliveryDateRequired the is delivery date required
	 */
	void processPinServiceability(
			ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			Listbox deliveryModeDropdown, String pinCode, CartEntryModel product,
			final String isDeliveryDateRequired);


	/**
	 * Popup message.
	 *
	 * @param widget the widget
	 * @param message the message
	 */
	void popupMessage(
			final ListboxWidget<CheckoutCartWidgetModel, CheckoutController> widget,
			final String message);


	/**
	 * Check if cart expired.
	 *
	 * @param widget the widget
	 * @return true, if successful
	 */
	boolean checkIfCartExpired(
			final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget);

	/**
	 * Check delivery mode cod limit.
	 *
	 * @param cart the cart
	 * @param widget the widget
	 * @return true, if successful
	 */
	boolean checkDeliveryModeCODLimit(final CartModel cart,final DefaultListboxWidget<CheckoutPaymentWidgetModel, CheckoutController> widget);

	/**
	 * Process payment.
	 *
	 * @param cart the cart
	 * @return true, if successful
	 * @throws PaymentException the payment exception
	 * @throws ValidationException the validation exception
	 * @throws Exception 
	 */
	boolean processPayment(CartModel cart) throws PaymentException,
			ValidationException, Exception;

	boolean validateWithOMS(TypedObject cartEntry, TypedObject deliveryMode)
			throws ValidationException;

	boolean processCODPayment() throws PaymentException,
			ValidationException;

	boolean removeCODPayment() throws PaymentException,
			ValidationException;


	void checkCustomerStatus() throws ValidationException;
	
	Boolean isCartCODEligible()  throws ValidationException ;

	List<MplZoneDeliveryModeValueModel> getDeliveryModesAndCost(String string,
			String selectedUSSID);

	void setFreeBiDeliveryModesAndCost(AbstractOrderEntryModel entry,
			MplZoneDeliveryModeValueModel deliveryMode);

	void setCODPaymentMode(final CartModel cartModel);	//TPR-3471


	
	void processJuspayPayment(final CartModel cart, final CustomerModel customer);
	
	String juspayPaymentValidation(String commerceOrderId);
	
	boolean jusPayprocessPaymentTxn(CartModel cart) throws PaymentException, 
					ValidationException, Exception;
	
	boolean processJusPayPaymentOnSelect() throws PaymentException, ValidationException;
	
	void setJusPayPaymentModeOnSelect(final CartModel cartModel);

	InvReserForDeliverySlotsResponseData deliverySlotsRequestDataCallToOms(InvReserForDeliverySlotsRequestData deliverySlotsRequestData, CartModel cartModel);
	public Double getScheduleDeliveryCharges();
	public SellerInformationModel getSellerInformationByUssid(String Ussid);

}
