package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;

import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;
import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.model.RichAttributeModel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.models.impl.BasketCartWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.BasketTotalsWidgetRenderer;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.util.DiscountValue;


public class MarketPlaceBasketTotalsWidgetRenderer extends
		BasketTotalsWidgetRenderer {

	/* (non-Javadoc)
	 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.BasketTotalsWidgetRenderer#createContentInternal(de.hybris.platform.cockpit.widgets.InputWidget, org.zkoss.zk.ui.api.HtmlBasedComponent)
	 */
	
	@Autowired
	private MplFindDeliveryFulfillModeStrategy mplFindDeliveryFulfillModeStrategy;
	
	@Override
	protected HtmlBasedComponent createContentInternal(
			InputWidget<BasketCartWidgetModel, BasketController> widget,
			HtmlBasedComponent rootContainer) {
				Div content = new Div();
				renderOrderDetail(widget, ((BasketCartWidgetModel)widget.getWidgetModel()).getCart(), content);
				return content;
	}

	/* (non-Javadoc)
	 * @see de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractOrderTotalsWidgetRenderer#renderOrderDetail(de.hybris.platform.cockpit.widgets.Widget, de.hybris.platform.cockpit.model.meta.TypedObject, org.zkoss.zk.ui.api.HtmlBasedComponent)
	 */
	@Override
	protected void renderOrderDetail(
			InputWidget<BasketCartWidgetModel, BasketController> widget,
			TypedObject order, HtmlBasedComponent parent) {
		  Div container = new Div();
		      container.setSclass("csOrderTotals");
		  
		      if ((order != null) && (order.getObject() instanceof AbstractOrderModel))
		  
		      {
		        AbstractOrderModel abstractOrderModel = (AbstractOrderModel)order.getObject();
		  
		        final CurrencyModel cartCurrencyModel = abstractOrderModel.getCurrency();
		        NumberFormat currencyInstance = (NumberFormat)getSessionService().executeInLocalView(new SessionExecutionBody() 
		
		  		{
		          public Object execute()
		          {
		            getCommonI18NService().setCurrentCurrency(cartCurrencyModel);
		            return getFormatFactory().createCurrencyFormat();
		  
		          }
		        });
		        Double subtotal = abstractOrderModel.getSubtotal();
		        renderRow(subtotal, LabelUtils.getLabel(widget, "subtotal", new Object[0]), currencyInstance, container);
		  
		        Double promotion = Double.valueOf(0);
		        
		    	for(AbstractOrderEntryModel entry : abstractOrderModel.getEntries()){
		    		if(entry.getNetAmountAfterAllDisc()!=null)
					promotion+= (entry.getTotalProductLevelDisc()) ;
				}
		    	
		        renderRow(promotion, LabelUtils.getLabel(widget, "promotion", new Object[0]), currencyInstance, container);
		        
		        Double deliveryCosts = abstractOrderModel.getDeliveryCost();
		       /* 
				Double deliveryCosts = 0D;
				
				for (AbstractOrderEntryModel orderEntry : abstractOrderModel
						.getEntries()) {
					if (null != orderEntry.getMplDeliveryMode()) {
						deliveryCosts = deliveryCosts
								+ ( orderEntry.getCurrDelCharge());
					}
				}*/
				
		        renderRow(deliveryCosts, LabelUtils.getLabel(widget, "deliveryCosts", new Object[0]), currencyInstance, container);

				Double totalDeliveryCostDisc = 0D;
				
				for (AbstractOrderEntryModel orderEntry : abstractOrderModel
						.getEntries()) {
					if(! mplFindDeliveryFulfillModeStrategy.isTShip(orderEntry.getSelectedUSSID())){
						totalDeliveryCostDisc =+ Double.valueOf(orderEntry.getPrevDelCharge().doubleValue() - orderEntry.getCurrDelCharge().doubleValue());
					}
				}
				
				renderRow(totalDeliveryCostDisc > 0 ? totalDeliveryCostDisc  : 0d , LabelUtils.getLabel(widget, "deliveryDiscount", new Object[0]), currencyInstance, container);
				
				
		        //Double taxes = abstractOrderModel.getTotalTax();
		        //renderRow(taxes, LabelUtils.getLabel(widget, "taxes", new Object[0]), currencyInstance, container);
		  
		      /*  Double paymentCosts = abstractOrderModel.getPaymentCost();
		        renderRow(paymentCosts, LabelUtils.getLabel(widget, "paymentCosts", new Object[0]), currencyInstance, container);*/
		  
				final List<DiscountValue>discountList=abstractOrderModel.getGlobalDiscountValues();
			    final List<DiscountModel> voucherList=abstractOrderModel.getDiscounts();
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
				
				
		       // Double discounts = abstractOrderModel.getTotalDiscounts();
		        renderRow(orderDiscount, LabelUtils.getLabel(widget, "discounts", new Object[0]), currencyInstance, container);
		        
		        renderRow(couponDiscount, LabelUtils.getLabel(widget, "couponDiscounts", new Object[0]), currencyInstance, container);
		        
		        Double convenienceCharges = abstractOrderModel.getConvenienceCharges();
		        renderRow(convenienceCharges, LabelUtils.getLabel(widget, "convenienceCharges", new Object[0]), currencyInstance, container);
		  
		      //  Double totalPrice = abstractOrderModel.getTotalPriceWithConv();
		        renderRow(abstractOrderModel.getTotalPriceWithConv(), LabelUtils.getLabel(widget, "totalPrice", new Object[0]), currencyInstance, container);
		      }
		  
		      container.setParent(parent);
	}

}
