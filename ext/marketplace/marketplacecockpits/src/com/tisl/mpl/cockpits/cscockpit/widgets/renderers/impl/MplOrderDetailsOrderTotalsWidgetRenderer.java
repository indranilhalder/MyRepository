/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.text.NumberFormat;

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
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderTotalsWidgetRenderer;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;

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
			
			for(AbstractOrderEntryModel entry : abstractOrderModel.getEntries()){
				if(entry.getNetAmountAfterAllDisc()!=null)
				promotion+= (entry.getTotalProductLevelDisc()) ;
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
				if(! mplFindDeliveryFulfillModeStrategy.isTShip(orderEntry.getSelectedUSSID())){
					totalDeliveryCostDisc += Double.valueOf(orderEntry.getPrevDelCharge().doubleValue() - orderEntry.getCurrDelCharge().doubleValue());
				}
			}

			renderRow(totalDeliveryCostDisc >0 ? totalDeliveryCostDisc : 0d , LabelUtils.getLabel(widget,
					"deliveryDiscount", new Object[0]), currencyInstance,
					container);

			Double deliveryCosts = abstractOrderModel.getDeliveryCost();
			/*Double deliveryCosts = 0D;
			
			for (AbstractOrderEntryModel orderEntry : abstractOrderModel
					.getEntries()) {
				if (null != orderEntry.getMplDeliveryMode()) {
					deliveryCosts = deliveryCosts
							+ ( orderEntry.getCurrDelCharge());
				}
			}*/
			
			
			renderRow(
					deliveryCosts,
					LabelUtils.getLabel(widget, "deliveryCosts", new Object[0]),
					currencyInstance, container);

			Double discounts = abstractOrderModel.getTotalDiscounts();
			renderRow(discounts,
					LabelUtils.getLabel(widget, "discounts", new Object[0]),
					currencyInstance, container);

			Double convenienceCharges = abstractOrderModel
					.getConvenienceCharges();
			renderRow(convenienceCharges, LabelUtils.getLabel(widget,
					"convenienceCharges", new Object[0]), currencyInstance,
					container);

			Double totalPrice = abstractOrderModel.getTotalPriceWithConv();
			renderRow(totalPrice,
					LabelUtils.getLabel(widget, "totalPrice", new Object[0]),
					currencyInstance, container);
		}

		container.setParent(parent);
	}
}
