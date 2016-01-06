package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceBasketController;

import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.models.impl.BasketCartWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;

public class MarketplaceCouponWidgetRenderer extends AbstractCsWidgetRenderer<Widget<BasketCartWidgetModel, BasketController>>{


	@Override
	protected HtmlBasedComponent createContentInternal(
			Widget<BasketCartWidgetModel, BasketController> widget,
			HtmlBasedComponent component) {
		Div container = new Div();
		
		Hbox mainbox = new Hbox();
		
		container.setSclass("voucherAlignment");
		Label lbl = new Label(LabelUtils.getLabel(widget, "lblCode",new Object[0]));
		lbl.setParent(mainbox);
		//lbl.setSclass("editorWidgetEditorLabel z-label");
		Textbox txtbox = new Textbox();
		txtbox.setParent(mainbox);
		
		createButtons(mainbox, widget, txtbox);
		mainbox.setParent(container);
		return container;
	}
	protected void createButtons(final Hbox mainbox,final Widget<BasketCartWidgetModel, BasketController> widget,final Textbox txtbox)
	{
		final Button applyButton = new Button(LabelUtils.getLabel(widget, "btnApply",new Object[0]));	
		final Button releaseVoucherButton = new Button(LabelUtils.getLabel(widget, "btnReleaseVoucher",new Object[0]));
		applyButton.setParent(mainbox);
		
		releaseVoucherButton.setParent(mainbox);
		
		applyButton.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				handleApplyBtnEvent(widget,txtbox);
			}
		});
		releaseVoucherButton.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				handleReleaseVoucherBtnEvent(widget);
			}
		});
		Collection<String> appliedVoucherCodeList =((MarketPlaceBasketController)widget.getWidgetController()).getAppliedVoucherCodesList(); 
		boolean relaseFlag = CollectionUtils.isEmpty(appliedVoucherCodeList);
		if(relaseFlag)
		{
			releaseVoucherButton.setDisabled(true);
		}
		else
		{
			applyButton.setDisabled(true);
		}
		
	}
	private void handleReleaseVoucherBtnEvent(
			Widget<BasketCartWidgetModel, BasketController> widget) throws Exception {
		((MarketPlaceBasketController)widget.getWidgetController()).releaseVoucher();
		Map data = Collections.singletonMap("refresh", Boolean.TRUE);
		widget.getWidgetController().dispatchEvent(null,null, data);
	}
	private void handleApplyBtnEvent(
			Widget<BasketCartWidgetModel, BasketController> widget,
			Textbox txtbox) throws Exception {
		String successMessage = ((MarketPlaceBasketController)widget.getWidgetController()).applyVoucher(txtbox.getValue());
		if(StringUtils.isNotEmpty(successMessage))
		{
			try {
				Messagebox.show(LabelUtils.getLabel(widget, successMessage, txtbox.getValue()),LabelUtils.getLabel(widget, "voucher_error_title", new Object[0]), 1, "z-msgbox z-msgbox-error");
			} catch (InterruptedException e) {
				throw new Exception(e);
			}	
		}
		Map data = Collections.singletonMap("refresh", Boolean.TRUE);
		widget.getWidgetController().dispatchEvent(null,null, data);
	}
}
