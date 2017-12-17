package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;
import com.tisl.mpl.model.MplCartOfferVoucherModel;
import com.tisl.mpl.util.DiscountUtility;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.voucher.model.PromotionVoucherModel;


public class MarketplaceCouponOrderWidgetRenderer extends AbstractCsWidgetRenderer<Widget<OrderItemWidgetModel, MarketPlaceOrderController>> {

	@Autowired
	private DiscountUtility discountUtility;

	@Override
	protected HtmlBasedComponent createContentInternal(
			Widget<OrderItemWidgetModel, MarketPlaceOrderController> widget,
			HtmlBasedComponent component) {
		Div content = new Div();
		TypedObject order=widget.getWidgetController().getCurrentOrder();
		OrderModel orderModel=(OrderModel) order.getObject();
		List<DiscountModel> discounts=orderModel.getDiscounts();
		
		final List<AbstractOrderEntryModel> orderEntryList=orderModel.getEntries();
		boolean couponFlag=false; 
		boolean cartCouponFlag=false; 
		
		for(AbstractOrderEntryModel orderEntry:orderEntryList)
		{
			if(StringUtils.isNotEmpty(orderEntry.getCouponCode()))
			{
				couponFlag=true;
				break;
			}
		}
		
		for(AbstractOrderEntryModel orderEntry:orderEntryList)
		{
			if(StringUtils.isNotEmpty(orderEntry.getCartCouponCode()))
			{
				cartCouponFlag=true;
				break;
			}
		}
		
		if(couponFlag)
		{
			Div container = new Div();
			container.setSclass("voucherAlignment");
			container.setParent(content);
			
			Hbox mainbox = new Hbox();

			//Coupon Details
			Div couponDiv=new Div();
			couponDiv.setParent(mainbox);
			
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

			for(DiscountModel discount:discounts){
				if(discount instanceof PromotionVoucherModel && !(discount instanceof MplCartOfferVoucherModel))
				{
					final Listitem row = new Listitem();
					
					row.setSclass("listbox-row-item");
					row.setParent(listBox);			
					
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
						final PriceData couponVal=discountUtility.createPrice(orderModel, ((PromotionVoucherModel)discount).getValue());
						Label couponValLabel = new Label(couponVal.getFormattedValue());
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
					if(!discount.getAbsolute() && null!=((PromotionVoucherModel)discount).getMaxDiscountValue())
					{
						final PriceData couponMaxVal=discountUtility.createPrice(orderModel, ((PromotionVoucherModel)discount).getMaxDiscountValue());
						Label couponMaxLabel = new Label(couponMaxVal.getFormattedValue());
						couponMaxLabel.setParent(couponMaxDiv);
					}
					else
					{
						Label couponMaxLabel=new Label();
						couponMaxLabel.setParent(couponMaxDiv);
					}
						
				}
			}
			
			mainbox.setParent(container);
			
			//if(couponFlag)
			//{
				//Product Details
			List<AbstractOrderEntryModel> parentOrderEntryList=new ArrayList<AbstractOrderEntryModel>();
			if(orderModel.getType().equalsIgnoreCase("SubOrder"))
			{
				final OrderModel parentOrder=orderModel.getParentReference();
				parentOrderEntryList = parentOrder.getEntries();
				
			}
			else if(orderModel.getType().equalsIgnoreCase("Parent"))
			{
				parentOrderEntryList = orderEntryList;
			}
				Hbox prodBox = new Hbox();		
				Div couponEntryDiv=new Div();
				couponEntryDiv.setParent(prodBox);
				Listbox listBoxProd=new Listbox();
				listBoxProd.setParent(couponEntryDiv);
				
				Listhead headRowProd = new Listhead();
				headRowProd.setParent(listBoxProd);

				Listheader colProdIdHeader = new Listheader(LabelUtils.getLabel(
						widget, "productId", new Object[0]));
				colProdIdHeader.setWidth("25px");
				colProdIdHeader.setParent(headRowProd);
				
				Listheader colProdNameHeader = new Listheader(LabelUtils.getLabel(
						widget, "productName", new Object[0]));
				colProdNameHeader.setWidth("50px");
				colProdNameHeader.setParent(headRowProd);
				
				Listheader colProdSaveHeader = new Listheader(LabelUtils.getLabel(
						widget, "productSave", new Object[0]));
				colProdSaveHeader.setWidth("50px");
				colProdSaveHeader.setParent(headRowProd);

//				for(AbstractOrderEntryModel entry:orderEntryList){
//					final OrderModel orderForEntry=(OrderModel) entry.getOrder();
//					if(orderForEntry.getType().equalsIgnoreCase("Child") && CollectionUtils.isNotEmpty(parentOrderEntryList)){
//						populateSubOrder(parentOrderEntryList, entry, orderForEntry, listBoxProd);
//					}
//					else{
						//populateParentOrder(entry, orderModel, listBoxProd);
					//}					
				//}
				
				for(AbstractOrderEntryModel entry : parentOrderEntryList)
				{
					populateParentOrder(entry, orderModel, listBoxProd);
				}
				
				
				prodBox.setParent(container);
			//}
		}
		if(cartCouponFlag)
		{
			Div cartcontainer = new Div();
			cartcontainer.setSclass("voucherAlignment");
			cartcontainer.setParent(content);
			
			Hbox cartmainbox = new Hbox();

			//Coupon Details
			Div cartcouponDiv=new Div();
			cartcouponDiv.setParent(cartmainbox);
			
			Listbox listBox=new Listbox();
			listBox.setParent(cartcouponDiv);
			
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

			for(DiscountModel discount:discounts){
				if(discount instanceof MplCartOfferVoucherModel)
				{
					MplCartOfferVoucherModel coupon = (MplCartOfferVoucherModel) discount;
					final Listitem row = new Listitem();
					
					row.setSclass("listbox-row-item");
					row.setParent(listBox);			
					
					Listcell couponCodeCell = new Listcell();
					couponCodeCell.setParent(row);
					Div couponCodeDiv = new Div();
					couponCodeDiv.setParent(couponCodeCell);
					couponCodeDiv.setSclass("editorWidgetEditor");
					Label couponCodeLabel = new Label(coupon.getVoucherCode());
					couponCodeLabel.setParent(couponCodeDiv);
					
					Listcell couponNameCell = new Listcell();
					couponNameCell.setParent(row);
					Div couponNameDiv = new Div();
					couponNameDiv.setParent(couponNameCell);
					couponNameDiv.setSclass("editorWidgetEditor");
					Label couponNameLabel = new Label(coupon.getName());
					couponNameLabel.setParent(couponNameDiv);

					Listcell couponValCell = new Listcell();
					couponValCell.setParent(row);
					Div couponValDiv = new Div();
					couponValDiv.setParent(couponValCell);
					couponValDiv.setSclass("editorWidgetEditor");	
					if(discount.getAbsolute())
					{
						final PriceData couponVal=discountUtility.createPrice(orderModel, coupon.getValue());
						Label couponValLabel = new Label(couponVal.getFormattedValue());
						couponValLabel.setParent(couponValDiv);
					}
					else
					{
						Label couponValLabel = new Label(coupon.getDiscountString());
						couponValLabel.setParent(couponValDiv);
					}
					
					Listcell couponMaxCell = new Listcell();
					couponMaxCell.setParent(row);
					Div couponMaxDiv = new Div();
					couponMaxDiv.setParent(couponMaxCell);
					couponMaxDiv.setSclass("editorWidgetEditor");
					if(!discount.getAbsolute() && null!=(coupon.getMaxDiscountValue()))
					{
						final PriceData couponMaxVal=discountUtility.createPrice(orderModel, (coupon.getMaxDiscountValue()));
						Label couponMaxLabel = new Label(couponMaxVal.getFormattedValue());
						couponMaxLabel.setParent(couponMaxDiv);
					}
					else
					{
						Label couponMaxLabel=new Label();
						couponMaxLabel.setParent(couponMaxDiv);
					}
				}
			}
			
			cartmainbox.setParent(cartcontainer);
			
			//if(couponFlag)
			//{
				//Product Details
			List<AbstractOrderEntryModel> parentOrderEntryList=new ArrayList<AbstractOrderEntryModel>();
			if(orderModel.getType().equalsIgnoreCase("SubOrder"))
			{
				final OrderModel parentOrder=orderModel.getParentReference();
				parentOrderEntryList = parentOrder.getEntries();
				
			}
			else if(orderModel.getType().equalsIgnoreCase("Parent"))
			{
				parentOrderEntryList = orderEntryList;
			}
				Hbox prodBox = new Hbox();		
				Div couponEntryDiv=new Div();
				couponEntryDiv.setParent(prodBox);
				Listbox listBoxProd=new Listbox();
				listBoxProd.setParent(couponEntryDiv);
				
				Listhead headRowProd = new Listhead();
				headRowProd.setParent(listBoxProd);

				Listheader colProdIdHeader = new Listheader(LabelUtils.getLabel(
						widget, "productId", new Object[0]));
				colProdIdHeader.setWidth("25px");
				colProdIdHeader.setParent(headRowProd);
				
				Listheader colProdNameHeader = new Listheader(LabelUtils.getLabel(
						widget, "productName", new Object[0]));
				colProdNameHeader.setWidth("50px");
				colProdNameHeader.setParent(headRowProd);
				
				Listheader colProdSaveHeader = new Listheader(LabelUtils.getLabel(
						widget, "productSave", new Object[0]));
				colProdSaveHeader.setWidth("50px");
				colProdSaveHeader.setParent(headRowProd);

//				for(AbstractOrderEntryModel entry:orderEntryList){
//					final OrderModel orderForEntry=(OrderModel) entry.getOrder();
//					if(orderForEntry.getType().equalsIgnoreCase("Child") && CollectionUtils.isNotEmpty(parentOrderEntryList)){
//						populateSubOrder(parentOrderEntryList, entry, orderForEntry, listBoxProd);
//					}
//					else{
						//populateParentOrder(entry, orderModel, listBoxProd);
					//}					
				//}
				
				for(AbstractOrderEntryModel entry : parentOrderEntryList)
				{
					populateCartCoupon(entry, orderModel, listBoxProd);
				}
				
				
				prodBox.setParent(cartcontainer);
			//}
		}
		else
		{
			Label dummyLabel = new Label(LabelUtils.getLabel(widget,
					"noCoupon", new Object[0]));
			dummyLabel.setSclass("csNoCoupon");
			dummyLabel.setParent(content);
		}
	
		return content;
	}
	
	
	
	private void populateCartCoupon(AbstractOrderEntryModel entry, OrderModel orderModel, Listbox listBoxProd) {
		if(StringUtils.isNotEmpty(entry.getCartCouponCode()) && null!=entry.getCartCouponValue() && entry.getCartCouponValue()>0 )
		{
			populate(orderModel, listBoxProd, entry);
		}
	}



	/**
	 * 
	 * @param parentOrderEntryList
	 * @param entry
	 * @param orderModel
	 * @param listBoxProd
	 */
	private void populateSubOrder(final List<AbstractOrderEntryModel> parentOrderEntryList, final AbstractOrderEntryModel entry, final OrderModel orderModel, Listbox listBoxProd)
	{
		for(AbstractOrderEntryModel parOrderEntry: parentOrderEntryList)
		{
			if(StringUtils.isNotEmpty(entry.getCouponCode()) && null!=entry.getCouponValue() && entry.getCouponValue()>0 
					&& entry.getSelectedUSSID().equalsIgnoreCase(parOrderEntry.getSelectedUSSID()))
			{
				populateList(orderModel, listBoxProd, entry);
			}
		}
	}
	
	
	
	
	/**
	 * 
	 * @param parentOrderEntryList
	 * @param entry
	 * @param orderModel
	 * @param listBoxProd
	 */
	private void populateParentOrder(final AbstractOrderEntryModel entry, final OrderModel orderModel, Listbox listBoxProd)
	{
		if(StringUtils.isNotEmpty(entry.getCouponCode()) && null!=entry.getCouponValue() && entry.getCouponValue()>0 )
		{
			populateList(orderModel, listBoxProd, entry);
		}
	}
	
	
	
	
	/**
	 * 
	 * @param orderModel
	 * @param listBoxProd
	 * @param entry
	 */
	private void populateList(final OrderModel orderModel, Listbox listBoxProd, final AbstractOrderEntryModel entry)
	{
		if(StringUtils.isNotEmpty(entry.getCouponCode()) && null!=entry.getCouponValue() && entry.getCouponValue()>0)
		{
		final Listitem rowProd = new Listitem();
		
		rowProd.setSclass("listbox-row-item");
		rowProd.setParent(listBoxProd);
		
		
		Listcell productIdCell = new Listcell();
		productIdCell.setParent(rowProd);
		Div productIdDiv = new Div();
		productIdDiv.setParent(productIdCell);
		productIdDiv.setSclass("editorWidgetEditor");
		Label productIdLabel = new Label(entry.getProduct().getCode());
		productIdLabel.setParent(productIdDiv);
		
		Listcell productNameCell = new Listcell();
		productNameCell.setParent(rowProd);
		Div productNameDiv = new Div();
		productNameDiv.setParent(productNameCell);
		productNameDiv.setSclass("editorWidgetEditor");
		Label productNameLabel = new Label(entry.getProduct().getName());
		productNameLabel.setParent(productNameDiv);

		Listcell productSaveCell = new Listcell();
		productSaveCell.setParent(rowProd);
		Div productSaveDiv = new Div();
		productSaveDiv.setParent(productSaveCell);
		productSaveDiv.setSclass("editorWidgetEditor");
		final PriceData couponDiscVal=discountUtility.createPrice(orderModel, entry.getCouponValue());
		Label productSaveLabel = new Label(couponDiscVal.getFormattedValue());
		productSaveLabel.setParent(productSaveDiv);
		}
	}
	
	/**
	 * 
	 * @param orderModel
	 * @param listBoxProd
	 * @param entry
	 */
	private void populate(final OrderModel orderModel, Listbox listBoxProd, final AbstractOrderEntryModel entry)
	{

		final Listitem rowProd = new Listitem();
		
		rowProd.setSclass("listbox-row-item");
		rowProd.setParent(listBoxProd);
		
		
		Listcell productIdCell = new Listcell();
		productIdCell.setParent(rowProd);
		Div productIdDiv = new Div();
		productIdDiv.setParent(productIdCell);
		productIdDiv.setSclass("editorWidgetEditor");
		Label productIdLabel = new Label(entry.getProduct().getCode());
		productIdLabel.setParent(productIdDiv);
		
		Listcell productNameCell = new Listcell();
		productNameCell.setParent(rowProd);
		Div productNameDiv = new Div();
		productNameDiv.setParent(productNameCell);
		productNameDiv.setSclass("editorWidgetEditor");
		Label productNameLabel = new Label(entry.getProduct().getName());
		productNameLabel.setParent(productNameDiv);

		Listcell productSaveCell = new Listcell();
		productSaveCell.setParent(rowProd);
		Div productSaveDiv = new Div();
		productSaveDiv.setParent(productSaveCell);
		productSaveDiv.setSclass("editorWidgetEditor");
		final PriceData couponDiscVal=discountUtility.createPrice(orderModel, entry.getCartCouponValue());
		Label productSaveLabel = new Label(couponDiscVal.getFormattedValue());
		productSaveLabel.setParent(productSaveDiv);
	}

}
