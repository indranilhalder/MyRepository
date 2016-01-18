package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;

import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;

public class MarketplaceCouponOrderWidgetRenderer extends AbstractCsWidgetRenderer<Widget<OrderItemWidgetModel, MarketPlaceOrderController>>{


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
		
		mainbox.setParent(container);
		return container;
	}

}
