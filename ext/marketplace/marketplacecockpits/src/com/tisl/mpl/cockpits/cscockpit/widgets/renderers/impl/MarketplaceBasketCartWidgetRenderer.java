package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Span;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceBasketController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketplaceSearchCommandController;
import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.util.DiscountUtility;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.SafeUnbox;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.models.impl.BasketCartWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.BasketCartWidgetRenderer;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.DiscountValue;

/**
 * The Class MarketplaceBasketCartWidgetRenderer.
 */
public class MarketplaceBasketCartWidgetRenderer extends
		BasketCartWidgetRenderer {
	
	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(MarketplaceBasketCartWidgetRenderer.class);
	
	/** The Constant INVENTORY_INSUFFICIENT. */
	private static final String INVENTORY_INSUFFICIENT = "inventoryInsufficient";
	
	/** The session service. */
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private MarketplaceSearchCommandController marketplaceSearchCommandController;
	
	@Autowired
	private DiscountUtility discountUtility;
	
	/* (non-Javadoc)
	 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.BasketCartWidgetRenderer#createPickupCartButton(de.hybris.platform.cockpit.widgets.ListboxWidget)
	 */
	@Override
	protected Component createPickupCartButton(ListboxWidget<BasketCartWidgetModel, BasketController> widget)
	{
	  Button pickupButton = new Button(LabelUtils.getLabel(widget, "pickupCartBtn", new Object[0]));
	  pickupButton.setSclass("csPickupCartButton");
	  pickupButton.setVisible(false); //Removing the pick up cart button as per customer recommendation
	  if (UISessionUtils.getCurrentSession().isUsingTestIDs())
	  {
	    UITools.applyTestID(pickupButton, "Cart_Cart_Pickup_Cart_button");
	  }

	  pickupButton.addEventListener("onClick", createOpenCartPickupFormButtonEventListener(widget));
	  return pickupButton;
	}
	
	/* (non-Javadoc)
	 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.BasketCartWidgetRenderer#handleCartEntryQtyChangeEvent(de.hybris.platform.cockpit.widgets.ListboxWidget, org.zkoss.zk.ui.event.Event, de.hybris.platform.cockpit.model.meta.TypedObject, org.zkoss.zul.Longbox, java.lang.Long)
	 */
	@Override
	protected void handleCartEntryQtyChangeEvent(ListboxWidget<BasketCartWidgetModel, BasketController> widget, org.zkoss.zk.ui.event.Event event, TypedObject item, Longbox qtyInput, Long oldQuantity)
	{
	  Long qty = qtyInput.getValue();
	  if ((qty == null) || 

	    (UITools.isFromOtherDesktop(event.getTarget())) || (item.getObject() == null) || 

	    (!(item.getObject() instanceof AbstractOrderEntryModel)))
	    return;
	  AbstractOrderEntryModel entry = (AbstractOrderEntryModel)item.getObject();
	  long totalEntryQuantity;
	  if (oldQuantity == null) {
	    totalEntryQuantity = SafeUnbox.toLong(qty);
	  }
	  else
	  {
	    totalEntryQuantity = SafeUnbox.toLong(entry.getQuantity()) + 
	      SafeUnbox.toLong(qty) - SafeUnbox.toLong(oldQuantity);
	  }

	  String pincode=sessionService.getAttribute("pincode");
	  try {
		  boolean byPassOmsChecks=configurationService.getConfiguration().getBoolean(MarketplaceCockpitsConstants.COCKPIT_SERVICEABILITY_CHECK_BYPASS);
			LOG.info("Bypass the pincode serviceability:"
					+ byPassOmsChecks);
			if (byPassOmsChecks) {
				((MarketPlaceBasketController)widget.getWidgetController()).setQuantity(item, totalEntryQuantity);
			} else {
				if(StringUtils.isNotEmpty(pincode)) {
					  final ProductModel product = entry.getProduct();
						final String isDeliveryDateRequired = LabelUtils.getLabel(widget,
								"isDeliveryDateRequired", new Object[0]);
						List<PinCodeResponseData> pinCodeResponses = marketplaceSearchCommandController.getResponseForPinCode(product,
								String.valueOf(pincode), isDeliveryDateRequired,entry.getSelectedUSSID());
						LOG.info("pinCodeResponse:" + pinCodeResponses.size());

						for(PinCodeResponseData response:pinCodeResponses) {
							if(totalEntryQuantity<=response.getStockCount()) {
								((BasketController)widget.getWidgetController()).setQuantity(item, totalEntryQuantity);
							} else {
								LOG.info("The item could not be updated due to insufficient quantity. Code:"+product.getCode());
								popupMessage(widget, INVENTORY_INSUFFICIENT);
							}
							break;
						}
				  } else{
					  LOG.info("ClientEtailNonBusinessExceptions in handleCartEntryQtyChangeEvent");
					  popupMessage(widget, "nopincode");
				  }
			}
			((BasketController)widget.getWidgetController()).dispatchEvent(null, widget, null);
	  } catch(ClientEtailNonBusinessExceptions ex) {
		  LOG.error("ClientEtailNonBusinessExceptions in handleCartEntryQtyChangeEvent",ex);
		  popupMessage(widget, MarketplaceCockpitsConstants.NO_RESPONSE_FROM_SERVER);
	  }
	}
	
	/**
	 * Popup message.
	 *
	 * @param widget
	 *            the widget
	 * @param message
	 *            the message
	 */
	private void popupMessage(ListboxWidget<BasketCartWidgetModel, BasketController> widget,
			final String message) {
		try {
			Messagebox.show(
					LabelUtils.getLabel(widget, message, new Object[0]),
					MarketplaceCockpitsConstants.INFO, Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
	}
	  protected Component createSummaryTotals(ListboxWidget<BasketCartWidgetModel, BasketController> widget, BasketController.CartSummary cartSummary)
	  {
	    Div summary = new Div();
	    summary.setSclass("csCartSummary");
	    Span netTotalSpan = new Span();
	    netTotalSpan.setSclass("csCartPriceRow");
	    netTotalSpan.setParent(summary);
	    Label netTotalLabel = new Label(LabelUtils.getLabel(widget, "summary.subTotal", new Object[0]));
	    netTotalLabel.setParent(netTotalSpan);
	    Label netTotal = new Label(cartSummary.getSubtotal());
	    netTotal.setSclass("csCartPriceRowValue");
	    netTotal.setParent(netTotalSpan);


	    Component orderLevelPromotions = createAppliedOrderPromotions(widget);
	    if (orderLevelPromotions != null)
	    {
	      summary.appendChild(orderLevelPromotions);
	    }

	    /** no need for taxes
	    Span taxSpan = new Span();
	    taxSpan.setSclass("csCartPriceRow");
	    taxSpan.setParent(summary);
	    Label taxLabel = new Label(LabelUtils.getLabel(widget, "summary.totalTax", new Object[0]));
	    taxLabel.setParent(taxSpan);
	    Label tax = new Label(cartSummary.getTotalTax());
	    tax.setSclass("csCartPriceRowValue");
	    tax.setParent(taxSpan);
	     **/
	    TypedObject cartObject=widget.getWidgetController().getCart();
	    CartModel cartModel=(CartModel) cartObject.getObject();
	    
	    final List<DiscountValue>discountList=cartModel.getGlobalDiscountValues();
	    final List<DiscountModel> voucherList=cartModel.getDiscounts();
	    double orderDiscount=0;
	    double couponDiscount=0;
	    
	    if(CollectionUtils.isNotEmpty(discountList))
	    {
	    	for(DiscountValue disVal:discountList)
	    	{
	    		if(CollectionUtils.isNotEmpty(voucherList) && disVal.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
	    		{
	    			couponDiscount+=disVal.getAppliedValue();
	    		}
	    		else
	    		{
	    			orderDiscount+=disVal.getAppliedValue();
	    		}
	    	}
	    }
	    
	    Span discountsTotalSpan = new Span();
	    discountsTotalSpan.setSclass("csCartPriceRow");
	    discountsTotalSpan.setParent(summary);
	    Label discountsTotalLabel = new Label(LabelUtils.getLabel(widget, "summary.discountTotal", new Object[0]));
	    discountsTotalLabel.setParent(discountsTotalSpan);
		//Label discountsTotal = new Label((Double.valueOf(orderDiscount)).toString());
	    Label discountsTotal = new Label((discountUtility.createPrice(cartModel, Double.valueOf(orderDiscount))).getFormattedValue());
	    discountsTotal.setSclass("csCartPriceRowValue");
	    discountsTotal.setParent(discountsTotalSpan);
	    
	    Span couponDiscountTotalSpan = new Span();
	    couponDiscountTotalSpan.setSclass("csCartPriceRow");
	    couponDiscountTotalSpan.setParent(summary);
	    Label couponDiscountTotalLabel = new Label(LabelUtils.getLabel(widget, "summary.couponDiscountTotal", new Object[0]));
	    couponDiscountTotalLabel.setParent(couponDiscountTotalSpan);
	   // Label couponDiscountTotal = new Label((Double.valueOf(couponDiscount)).toString());
	    Label couponDiscountTotal = new Label((discountUtility.createPrice(cartModel, Double.valueOf(couponDiscount))).getFormattedValue());
	    couponDiscountTotal.setSclass("csCartPriceRowValue");
	    couponDiscountTotal.setParent(couponDiscountTotalSpan);

	    Span grossTotalSpan = new Span();
	    grossTotalSpan.setSclass("csCartPriceRow");
	    grossTotalSpan.setParent(summary);
	    Label grossTotalLabel = new Label(LabelUtils.getLabel(widget, "summary.totalPrice", new Object[0]));
	    grossTotalLabel.setParent(grossTotalSpan);
	    Label grossTotal = new Label(cartSummary.getTotalPrice());
	    grossTotal.setSclass("csCartPriceRowValue");
	    grossTotal.setParent(grossTotalSpan);

	    return summary;
	  }
	  
	protected void handleClearCartClickEvent(
			ListboxWidget<BasketCartWidgetModel, BasketController> widget,
			Event event) {
		if (!("onOK".equals(event.getName())))
			return;
		((BasketController) widget.getWidgetController()).clearCart();

		((MarketPlaceBasketController)widget.getWidgetController()).releaseVoucher();
		
		((BasketController) widget.getWidgetController()).dispatchEvent(null,
				widget, null);
		
		sessionService.setAttribute("isclear", true);
		
		Map data = Collections.singletonMap("refresh", Boolean.TRUE);
		marketplaceSearchCommandController.dispatchEvent(null, null, data);
	}
	
	
	/* (non-Javadoc)
 	 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.BasketCartWidgetRenderer#createContentInternal(de.hybris.platform.cockpit.widgets.ListboxWidget, org.zkoss.zk.ui.api.HtmlBasedComponent)
 	 */
  	@Override
  	protected HtmlBasedComponent createContentInternal(
  			ListboxWidget<BasketCartWidgetModel, BasketController> widget,
  			HtmlBasedComponent rootContainer) {
  		
  		//Fix TISEE-6282
  		widget.getWidgetModel().setItems(((BasketController)widget.getWidgetController()).getPromotionalCartLineItems());
  		
  		return super.createContentInternal(widget, rootContainer);
  	}	
}