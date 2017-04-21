/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.text.NumberFormat;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;

import com.tisl.mpl.cockpits.cscockpit.strategies.MplFindDeliveryFulfillModeStrategy;
import com.tisl.mpl.constants.GeneratedMarketplacecommerceservicesConstants.Attributes.AbstractOrderEntry;
import com.tisl.mpl.core.enums.DeliveryFulfillModesEnum;
import com.tisl.mpl.core.model.RichAttributeModel;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderTotalsWidgetRenderer;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.util.DiscountValue;

/**
 * @author 890223
 *
 */
public class MplOrderDetailsOrderTotalsWidgetRenderer extends
		OrderDetailsOrderTotalsWidgetRenderer {

	@Autowired
	private MplFindDeliveryFulfillModeStrategy mplFindDeliveryFulfillModeStrategy;
	
	@Override
	protected void renderOrderDetail(
			Widget<OrderItemWidgetModel, OrderController> widget,
			TypedObject order, HtmlBasedComponent parent) {
		Div container = new Div();
		container.setSclass("csOrderTotals");

		if ((order != null)
				&& (order.getObject() instanceof AbstractOrderModel))

		{
			AbstractOrderModel abstractOrderModel = (AbstractOrderModel) order
					.getObject();

			final CurrencyModel cartCurrencyModel = abstractOrderModel
					.getCurrency();
			NumberFormat currencyInstance = (NumberFormat) getSessionService()
					.executeInLocalView(new SessionExecutionBody()

					{
						public Object execute() {
							getCommonI18NService().setCurrentCurrency(
									cartCurrencyModel);
							return getFormatFactory().createCurrencyFormat();

						}
					});
			Double subtotal = abstractOrderModel.getSubtotal();
			renderRow(subtotal,
					LabelUtils.getLabel(widget, "subtotal", new Object[0]),
					currencyInstance, container);

			double promotion = Double.valueOf(0);
			double cartPromo = Double.valueOf(0);
			double couponPromo = Double.valueOf(0);
			
			for(AbstractOrderEntryModel entry : abstractOrderModel.getEntries()){
				if(entry.getNetAmountAfterAllDisc()!=null)
				promotion+= (entry.getTotalProductLevelDisc()) ;
				if(null!=entry.getCartLevelDisc())
				{
					cartPromo+=entry.getCartLevelDisc();
				}
				if(null!=entry.getCouponValue())
				{
					couponPromo+=entry.getCouponValue();
				}
			}
			renderRow(promotion,
					LabelUtils.getLabel(widget, "promotion", new Object[0]),
					currencyInstance, container);

			// Double taxes = abstractOrderModel.getTotalTax();
			// renderRow(taxes, LabelUtils.getLabel(widget, "taxes", new
			// Object[0]), currencyInstance, container);

			// Double paymentCosts = abstractOrderModel.getPaymentCost();
			// renderRow(paymentCosts, LabelUtils.getLabel(widget,
			// "paymentCosts", new Object[0]), currencyInstance, container);

			Double totalDeliveryCostDisc = 0D;
			
			for (AbstractOrderEntryModel orderEntry : abstractOrderModel
					.getEntries()) {
				/********************************* TPR-4401 Delivery Charge Addition Starts Here ****************************************************/
				/*if(! mplFindDeliveryFulfillModeStrategy.isTShip(orderEntry.getSelectedUSSID())){
					totalDeliveryCostDisc += Double.valueOf(orderEntry.getPrevDelCharge().doubleValue() - orderEntry.getCurrDelCharge().doubleValue());
				}*/
				totalDeliveryCostDisc += Double.valueOf(orderEntry.getPrevDelCharge().doubleValue() - orderEntry.getCurrDelCharge().doubleValue());
				/********************************* TPR-4401 Delivery Charge Addition Ends Here ****************************************************/
			}

			renderRow(totalDeliveryCostDisc >0 ? totalDeliveryCostDisc : 0d , LabelUtils.getLabel(widget,
					"deliveryDiscount", new Object[0]), currencyInstance,
					container);

			Double deliveryCosts = abstractOrderModel.getDeliveryCost();
			if(deliveryCosts==0.0){
			for (AbstractOrderEntryModel orderEntry : abstractOrderModel
					.getEntries()) {
				if (null != orderEntry.getMplDeliveryMode()) {
					deliveryCosts =  (orderEntry.getMplDeliveryMode().getValue()-orderEntry.getCurrDelCharge().doubleValue())*orderEntry.getQuantity();
				}
			}
			}
			
			
			renderRow(
					deliveryCosts,
					LabelUtils.getLabel(widget, "deliveryCosts", new Object[0]),
					currencyInstance, container);
			
//			final List<DiscountValue>discountList=abstractOrderModel.getGlobalDiscountValues();
//		    final List<DiscountModel> voucherList=abstractOrderModel.getDiscounts();
//		    double orderDiscount=0;
//		    double couponDiscount=0;
//		    
//		    if(CollectionUtils.isNotEmpty(discountList))
//		    {
//		    	for(DiscountValue disVal:discountList)
//		    	{
//		    		if(CollectionUtils.isNotEmpty(voucherList) && disVal.getCode().equalsIgnoreCase(voucherList.get(0).getCode()))
//		    		{
//		    			couponDiscount+=disVal.getAppliedValue();
//		    		}
//		    		else
//		    		{
//		    			orderDiscount+=disVal.getAppliedValue();
//		    		}
//		    	}
//		    }

			//Double discounts = abstractOrderModel.getTotalDiscounts();
			renderRow(Double.valueOf(cartPromo),
					LabelUtils.getLabel(widget, "discounts", new Object[0]),
					currencyInstance, container);
			
			renderRow(Double.valueOf(couponPromo),
					LabelUtils.getLabel(widget, "couponDiscounts", new Object[0]),
					currencyInstance, container);

			Double convenienceCharges = abstractOrderModel
					.getConvenienceCharges();
			renderRow(convenienceCharges, LabelUtils.getLabel(widget,
					"convenienceCharges", new Object[0]), currencyInstance,
					container);
			//TISSQAUAT-1539 starts
			//Double totalPrice = subtotal+deliveryCosts+convenienceCharges-cartPromo-couponPromo-promotion;
			Double totalPrice = subtotal+deliveryCosts+convenienceCharges-cartPromo-couponPromo-promotion-totalDeliveryCostDisc;
			//TISSQAUAT-1539 ends
			renderRow(totalPrice,
					LabelUtils.getLabel(widget, "totalPrice", new Object[0]),
					currencyInstance, container);
		}

		container.setParent(parent);
	}
}
