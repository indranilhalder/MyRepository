package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.List;

import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;

public class MarketplaceCouponOrderWidgetRenderer extends AbstractCsWidgetRenderer<Widget<OrderItemWidgetModel, MarketPlaceOrderController>> {


	@Override
	protected HtmlBasedComponent createContentInternal(
			Widget<OrderItemWidgetModel, MarketPlaceOrderController> widget,
			HtmlBasedComponent component) {
		Div container = new Div();
		
		Hbox mainbox = new Hbox();
		
		container.setSclass("voucherAlignment");
		Label lbl = new Label(LabelUtils.getLabel(widget, "lblCode",new Object[0]));
		lbl.setParent(mainbox);
		//lbl.setSclass("editorWidgetEditorLabel z-label");
		lbl = new Label("Applied Coupon");
		lbl.setParent(mainbox);
		
		Div couponDiv=new Div();
		couponDiv.setParent(mainbox);
		TypedObject order=widget.getWidgetController().getCurrentOrder();
		OrderModel orderModel=(OrderModel) order.getObject();
		List<DiscountModel> discounts=orderModel.getDiscounts();
		Listbox listbox=new Listbox();
		listbox.setParent(couponDiv);
		final Listitem row = new Listitem();
		//row.setSclass(CSS_LISTBOX_ROW_ITEM);
		row.setParent(listbox);
		for(DiscountModel discount:discounts){
			Listcell cell=new Listcell();
			cell.setValue(discount.getCode());
			cell.setParent(row);
		}
		
		mainbox.setParent(container);
		return container;
	}

}
