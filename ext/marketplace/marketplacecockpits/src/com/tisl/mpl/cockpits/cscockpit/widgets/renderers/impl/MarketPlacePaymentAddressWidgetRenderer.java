package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.models.impl.CartAddressWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.PaymentAddressWidgetRenderer;
import de.hybris.platform.jalo.order.Cart;

public class MarketPlacePaymentAddressWidgetRenderer extends
		PaymentAddressWidgetRenderer {

	/* (non-Javadoc)
	 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCheckoutAddressWidgetRenderer#createAddressSelect(de.hybris.platform.cockpit.widgets.Widget, de.hybris.platform.cockpit.model.meta.TypedObject)
	 */
	@Override
	protected HtmlBasedComponent createAddressSelect(
			Widget<CartAddressWidgetModel, BasketController> widget,
			TypedObject currentAddressModel) {
	    Div container = new Div();
	    /*  69 */     container.setSclass("csAddressSelector z-textbox-disd");
	    /*  70 */     Div addressDropDownContent = new Div();
	    /*  71 */     addressDropDownContent.setParent(container);
	    /*     */ 
	    /*  73 */     Listbox addressDropdown = new Listbox();
	    /*  74 */     addressDropdown.setMold("select");
	    /*  75 */     addressDropdown.setRows(1);
	    /*  76 */     addressDropdown.setSclass("csDeliveryAddressList");
	    /*  77 */     addressDropdown.setParent(addressDropDownContent);
	    /*     */	  addressDropdown.setDisabled(true);
	    
	    /*     */ 
	    /*  80 */     if (UISessionUtils.getCurrentSession().isUsingTestIDs())
	    /*     */     {
	    /*  82 */       if ("shippingAddressWidget".equals(widget.getWidgetCode()))
	    /*     */       {
	    /*  84 */         UITools.applyTestID(addressDropdown, "Checkout_DeliveryAddress_Address_combobox");
	    /*     */       }
	    /*  86 */       else if ("paymentAddressWidget".equals(widget.getWidgetCode()))
	    /*     */       {
	    /*  88 */         UITools.applyTestID(addressDropdown, "Checkout_PaymentAddress_Address_combobox");
	    /*     */       }
	    /*     */     }
	    /*     */ 
	    /*  92 */     if (currentAddressModel == null)
	    /*     */     {
	    /*  94 */       Listitem emptyItem = new Listitem(LabelUtils.getLabel(widget, "dummyListEntry", new Object[0]), null);
	    /*  95 */       emptyItem.setParent(addressDropdown);
	    /*  96 */       emptyItem.setSelected(true);
	    /*     */     }
	    /*     */ 
	    				
	    			  boolean selectFlag = false;
	    /*  99 */     for (TypedObject address : ((CartAddressWidgetModel)widget.getWidgetModel()).getItems())
	    /*     */     {
	    /* 101 */       String text = TypeTools.getValueAsString(getLabelService(), address);
	    /* 102 */       Listitem listItem = new Listitem(text, address);
	    /* 103 */       listItem.setParent(addressDropdown);
	    				listItem.setSelected(address.equals(currentAddressModel));
	    				
	    				//CartModel cart = (CartModel)(((BasketController)widget.getWidgetController()).getCart().getObject());
	    				
	    				/*CartModel cart = (CartModel)(((BasketController)widget.getWidgetController()).getCart().getObject());
	    				if(cart.getPaymentAddress()!=null)
	    				{
	    					listItem.setSelected(address.equals(cart.getDeliveryAddress()));
	    					selectFlag = true;
	    				}
	    				else
	    				{					
	    					if(!selectFlag)
			    				{
	    					 104        listItem.setSelected(address.equals(currentAddressModel));
			    					
			    				}
	    				}*/
	    /*     */     }
	    /*     */ 		
	    /* 107 */     /*addressDropdown.addEventListener("onSelect", createSelectAddressEventListener(widget));
	          
	     109      Button addAddressButton = new Button(LabelUtils.getLabel(widget, "addAddressBtn", new Object[0]));
	     110      addAddressButton.setParent(container);*/
	    /*     */ 
	   /*  112      addAddressButton.addEventListener("onClick", new EventListener(widget, container)
	              {
	                public void onEvent(Event event)
	                  throws Exception
	                {
	     117          AbstractCheckoutAddressWidgetRenderer.this.handleAddAddressClickEvent(this.val$widget, event, this.val$container);
	         
	                }
	              });*/
	    /* 121 */     return container;
	}

}
