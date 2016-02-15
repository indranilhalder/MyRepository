/**
 * @author TCS
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceBasketController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceSearchCommandController;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.models.impl.ShippingCartAddressWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.ShippingAddressWidgetRenderer;
import de.hybris.platform.servicelayer.model.ModelService;

// TODO: Auto-generated Javadoc
/**
 * The Class MarketplaceShippingAddressWidgetRenderer.
 */
public class MarketplaceShippingAddressWidgetRenderer extends ShippingAddressWidgetRenderer{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(MarketplaceShippingAddressWidgetRenderer.class);
	
	@Autowired
	private ModelService modelService;

	@Autowired
	private MarketplaceSearchCommandController marketplaceSearchCommandController;
	/**
	 * TIS-273
	 * Creates the address select.
	 *
	 * @param widget the widget
	 * @param rootContainer the root container
	 * @return the html based component
	 */
	 protected HtmlBasedComponent createContentInternal(Widget<ShippingCartAddressWidgetModel, BasketController> widget, HtmlBasedComponent rootContainer)
	 {
	   TypedObject cart = ((ShippingCartAddressWidgetModel)widget.getWidgetModel()).getCart();
	   if (cart != null)
	   {
	     CartModel cartModel = (CartModel)cart.getObject();
	     cartModel.setAddressConfirmationFlag(false);
	     if (cartModel != null)
	     {
	       Div content = new Div();

	       AddressModel deliveryAddress = cartModel.getDeliveryAddress();
	       TypedObject currentAddressObject = getCockpitTypeService().wrapItem(deliveryAddress);
	       content.appendChild(createAddressSelect(widget, currentAddressObject));
	     
	       return content;
	     }
	   }
	   return null;
	 }
	
	
	/**
	 * Creates the address select.
	 *
	 * @param widget the widget
	 * @param currentAddressModel the current address model
	 * @return the html based component
	 */
	@Override
	protected HtmlBasedComponent createAddressSelect(final Widget<ShippingCartAddressWidgetModel, BasketController> widget,
			TypedObject currentAddressModel) {

		final HtmlBasedComponent container = super.createAddressSelect(widget, currentAddressModel);
		final CartModel cart = (CartModel) widget.getWidgetController().getCart().getObject();
		if(!modelService.isUpToDate(cart)){
			modelService.refresh(cart);
		}
		final Button confirmAddress = new Button(LabelUtils.getLabel(widget,
				MarketplaceCockpitsConstants.CONFIRM_ADDRESS_BUTTON, new Object[0]));
			confirmAddress.setParent(container);
			confirmAddress.setDisabled(null!=cart.getCartReservationDate());
			confirmAddress.addEventListener(Events.ON_CLICK, new EventListener(){
			
			/**
			 * On event.
			 *
			 * @param event the event
			 */
			@Override
			public void onEvent(Event event) {
				try{
							((MarketPlaceBasketController)widget.getWidgetController()).checkCustomerStatus();
							Boolean isCartReserved = ((MarketPlaceBasketController)widget.getWidgetController()).reserveCart(cart);
							LOG.info("MarketplaceShippingAddressWidgetRenderer->createAddressSelect->isCartReserved:"+isCartReserved);
								Map data = Collections.singletonMap("refresh", Boolean.TRUE);
								widget.getWidgetController().dispatchEvent(null, this, data);
								
								marketplaceSearchCommandController.dispatchEvent(null, widget, data);
								
				} catch(Exception e) {
					 try {
						Messagebox.show(e.getMessage() + ((e.getCause() == null) ? "" : new StringBuilder(" - ").append(e.getCause().getMessage()).toString()), 
								 /* 527 */         LabelUtils.getLabel(widget, "failedToReserveCart", new Object[0]), 1, "z-msgbox z-msgbox-error");
					} catch (InterruptedException ex) {
						LOG.error("Exception in createAddressSelect.onEvent",ex);
					}
				} 
			}
		});
		
		Div phoneNumberDiv = new Div();
		phoneNumberDiv.setParent(container);
		
		Label phoneNumberHeading = new Label(LabelUtils.getLabel(widget,
				MarketplaceCockpitsConstants.PHONE_NUMBER_HEADING, new Object[0]));
		phoneNumberHeading.setParent(phoneNumberDiv);
		phoneNumberHeading.setSclass("phoneNumbeCaption");
		
		Label label = new Label(LabelUtils.getLabel(widget,
				MarketplaceCockpitsConstants.COUNTRY_CODE, new Object[0]));
		label.setParent(phoneNumberDiv);
	
		Longbox phoneNumber = new Longbox();
		phoneNumber.setParent(phoneNumberDiv);
		if(null!=cart.getDeliveryAddress() && cart.getDeliveryAddress().getPhone1()!=null) {
			try{
			Long cellphone=Long.valueOf(cart.getDeliveryAddress().getPhone1());
			phoneNumber.setValue(cellphone);	
			}catch (NumberFormatException ex){
				LOG.info("Not a valid phone Number ");
			}

			phoneNumber.setConstraint("/{10,10}/");
			phoneNumber.addEventListener(Events.ON_CHANGE,createAddCellPhoneToAddress(widget,phoneNumber));
		} else {
			LOG.info("The default delivery address is not set by the customer, go to the customer tab to set the same");
			//popupMessage(widget, "deliveryAddressMissing");
		}
		
		return container;
	}


	
	
	
	
	/**
	 * Creates the add to basket event listener.
	 *
	 * @param widget            the widget
	 * @param cellPhoneBox the cell phone box
	 * @return the event listener
	 */
	protected EventListener createAddCellPhoneToAddress(Widget<ShippingCartAddressWidgetModel, BasketController> widget,
			final Longbox cellPhoneBox) {
		return new AddPhoneNumberToCartAddressListener(widget, cellPhoneBox);
	}

	
	/**
	 * The listener interface for receiving addPhoneNumberToCartAddress events.
	 * The class that is interested in processing a addPhoneNumberToCartAddress
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addAddPhoneNumberToCartAddressListener<code> method. When
	 * the addPhoneNumberToCartAddress event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see AddPhoneNumberToCartAddressEvent
	 */
	protected class AddPhoneNumberToCartAddressListener implements EventListener {

		/** The widget. */
		private final Widget<ShippingCartAddressWidgetModel, BasketController> widget;

		/** The pincode value. */
		private final Longbox cellPhoneBox;

		
		/**
		 * Instantiates a new adds the phone number to cart address listener.
		 *
		 * @param widget the widget
		 * @param cellPhoneBox the cell phone box
		 */
		public AddPhoneNumberToCartAddressListener(final Widget<ShippingCartAddressWidgetModel, BasketController> widget,
				final Longbox cellPhoneBox) {
			this.widget = widget;
			this.cellPhoneBox = cellPhoneBox;

		}

		/**
		 * On event.
		 *
		 * @param event
		 *            the event
		 */
		public void onEvent(Event event) {
			Long cellphoneNumber = cellPhoneBox.getValue();
			((MarketPlaceBasketController)widget.getWidgetController()).savePhoneNumberToCart(cellphoneNumber);
		}
	}
	

	
	@Override
 protected void setSelectedAddress(Widget<ShippingCartAddressWidgetModel, BasketController> widget, TypedObject item)
   {
    ((BasketController)widget.getWidgetController()).setDeliveryAddress(item);
	((BasketController)widget.getWidgetController()).setPaymentAddress(item);
   }
	
}