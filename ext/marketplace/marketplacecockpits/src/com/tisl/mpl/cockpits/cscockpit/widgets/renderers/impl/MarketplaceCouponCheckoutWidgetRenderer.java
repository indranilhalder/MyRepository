package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.List;

import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceBasketController;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.models.impl.BasketCartWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

public class MarketplaceCouponCheckoutWidgetRenderer extends AbstractCsWidgetRenderer<Widget<BasketCartWidgetModel, MarketPlaceBasketController>>{


	@Override
	protected HtmlBasedComponent createContentInternal(
			Widget<BasketCartWidgetModel, MarketPlaceBasketController> widget,
			HtmlBasedComponent component) {
Div container = new Div();
		
		Hbox mainbox = new Hbox();
		
		container.setSclass("voucherAlignment");
		
		Div couponDiv=new Div();
		couponDiv.setParent(mainbox);
		TypedObject cart=widget.getWidgetController().getCart();
		CartModel cartModel=(CartModel) cart.getObject();
		List<DiscountModel> discounts=cartModel.getDiscounts();
		final CurrencyModel currency=cartModel.getCurrency();
		Listbox listBox=new Listbox();
		listBox.setParent(couponDiv);
		
		Listhead headRow = new Listhead();
		headRow.setParent(listBox);

		Listheader colcouponCodeHeader = new Listheader(LabelUtils.getLabel(
				widget, "couponCode", new Object[0]));
		colcouponCodeHeader.setWidth("50px");
		colcouponCodeHeader.setParent(headRow);
		
		Listheader colcouponNameHeader = new Listheader(LabelUtils.getLabel(
				widget, "couponName", new Object[0]));
		colcouponNameHeader.setWidth("50px");
		colcouponNameHeader.setParent(headRow);

		Listheader colCouponValHeader = new Listheader(LabelUtils.getLabel(
				widget, "couponValue", new Object[0]));
		colCouponValHeader.setWidth("50px");
		colCouponValHeader.setParent(headRow);
		
		Listheader maxDiscHeader = new Listheader(LabelUtils.getLabel(
				widget, "maxDiscount", new Object[0]));
		maxDiscHeader.setWidth("50px");
		maxDiscHeader.setParent(headRow);
				
		final Listitem row = new Listitem();
		
		row.setSclass("listbox-row-item");
		row.setParent(listBox);

		for(DiscountModel discount:discounts){			
			Listcell couponCodeCell = new Listcell();
			couponCodeCell.setParent(row);
			Div couponCodeDiv = new Div();
			couponCodeDiv.setParent(couponCodeCell);
			couponCodeDiv.setSclass("editorWidgetEditor");
			Label couponCodeLabel = new Label(((PromotionVoucherModel)discount).getVoucherCode());
			couponCodeLabel.setParent(couponCodeDiv);
			
			Listcell couponNameCell = new Listcell();
			couponNameCell.setParent(row);
			Div couponNameDiv = new Div();
			couponNameDiv.setParent(couponNameCell);
			couponNameDiv.setSclass("editorWidgetEditor");
			Label couponNameLabel = new Label(((PromotionVoucherModel)discount).getName());
			couponNameLabel.setParent(couponNameDiv);

			Listcell couponValCell = new Listcell();
			couponValCell.setParent(row);
			Div couponValDiv = new Div();
			couponValDiv.setParent(couponValCell);
			couponValDiv.setSclass("editorWidgetEditor");	
			if(discount.getAbsolute())
			{
				Label couponValLabel = new Label((((PromotionVoucherModel)discount).getCurrency().getSymbol())+(String.valueOf(((PromotionVoucherModel)discount).getValue())));
				couponValLabel.setParent(couponValDiv);
			}
			else
			{
				Label couponValLabel = new Label(((PromotionVoucherModel)discount).getDiscountString());
				couponValLabel.setParent(couponValDiv);
			}
			
			Listcell couponMaxCell = new Listcell();
			couponMaxCell.setParent(row);
			Div couponMaxDiv = new Div();
			couponMaxDiv.setParent(couponMaxCell);
			couponMaxDiv.setSclass("editorWidgetEditor");
			if(!discount.getAbsolute())
			{
				Label couponMaxLabel = new Label(currency.getSymbol()+(String.valueOf(((PromotionVoucherModel)discount).getMaxDiscountValue())));
				couponMaxLabel.setParent(couponMaxDiv);
			}
			else
			{
				Label couponMaxLabel=new Label();
				couponMaxLabel.setParent(couponMaxDiv);
			}
				
		}
		
		mainbox.setParent(container);
		return container;
	}

}
