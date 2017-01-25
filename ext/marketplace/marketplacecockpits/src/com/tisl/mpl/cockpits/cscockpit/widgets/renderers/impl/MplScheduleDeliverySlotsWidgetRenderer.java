package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.servicelayer.model.ModelService;

public class MplScheduleDeliverySlotsWidgetRenderer
extends AbstractCsWidgetRenderer<Widget<OrderItemWidgetModel, OrderController>>  {
	
	

	@Autowired
	private ModelService modelService;

	protected static final String CSS_ORDER_HISTORY = "csOrderHistory";
	private final static Logger LOG = Logger.getLogger(MplScheduleDeliverySlotsWidgetRenderer.class);
	protected HtmlBasedComponent createContentInternal(
			Widget<OrderItemWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		Div content = new Div();
		LOG.info("Inside MplScheduleDeliverySlotsWidgetRenderer  createContentInternal Method");
		TypedObject typedObject = widget.getWidgetModel().getOrder();
		OrderModel order = (OrderModel) typedObject.getObject();
		try {
			OrderModel parentorder = modelService.create(OrderModel.class);
			if (null != order.getParentReference()) {
				parentorder = order.getParentReference();
			} else {
				parentorder = order;
			}
			boolean orderHasDeliverySlots = false;
			if (null != parentorder.getEntries()) {
				for (AbstractOrderEntryModel entry : parentorder.getEntries()) {
					if (null != entry.getEdScheduledDate() || null != entry.getSddDateBetween()) {
						orderHasDeliverySlots = true;
						break;
					}
				}
			}
			LOG.debug("Order " + order.getCode()
					+ " has schedule Delviery slots " + orderHasDeliverySlots);
			content.setSclass("csOrderHistory");
			Listbox listBox = new Listbox();
			listBox.setParent(content);
			listBox.setSclass("csWidgetListbox");
			if (orderHasDeliverySlots) {
				renderListbox(listBox, widget, rootContainer);
			} else {
				Listitem row = new Listitem();
				row.setParent(listBox);
				Listcell cell = new Listcell(LabelUtils.getLabel(widget,
						"noEntries", new Object[0]));
				cell.setParent(row);
			}
			return content;
		} catch (UiException e) {
			LOG.error("UI Exception while showing delivery slots for order ID :"
					+ order.getCode());
		} catch (Exception e) {
			LOG.error("Exception while showing delivery slots for order ID :"
					+ order.getCode());
		}
		return content;
	}

	protected void renderListbox(
			Listbox listBox,
			Widget<OrderItemWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		TypedObject order = widget.getWidgetModel().getOrder();
		OrderModel orderModel = (OrderModel) order.getObject();
		OrderModel parentOrder = modelService.create(OrderModel.class);
		if(null !=orderModel.getParentReference()) {
			parentOrder = orderModel.getParentReference();
		}else {
			parentOrder= orderModel;
		}
		Listhead header = new Listhead();
		header.setParent(listBox);
		populateHeaderRow(widget, header);
		if(null !=parentOrder && null !=parentOrder.getEntries()) {
			for (AbstractOrderEntryModel entry : parentOrder.getEntries()) {
				for(AbstractOrderEntryModel suborderEntry: orderModel.getEntries() ) {
					if(suborderEntry.getSelectedUSSID().equalsIgnoreCase(entry.getSelectedUSSID())) {
						if(null != entry.getEdScheduledDate() || null != entry.getSddDateBetween()) {
							Listitem row = new Listitem();
							row.setParent(listBox);
							populateDataRow(widget, entry, row);
						}
						break;
					}
				}
			}
		}
	}

	protected void populateDataRow(Widget<OrderItemWidgetModel, OrderController> widget, AbstractOrderEntryModel entry,
			Listitem row) {

		// Product Name 
		String productName = entry.getProduct().getArticleDescription();
		Listcell productNameCell = new Listcell(productName);
		productNameCell.setParent(row);

		// USSID
		String Ussid = entry.getSelectedUSSID();
		Listcell ussidCell = new Listcell(Ussid);
		ussidCell.setParent(row);

		// Date 
		String edScheduledDate = null;
		if(null != entry.getEdScheduledDate() ) {
			edScheduledDate = entry.getEdScheduledDate();
		}else if (null != entry.getSddDateBetween()) {
			edScheduledDate = entry.getSddDateBetween();
		}
		
		Listcell edScheduledDatecell = new Listcell(edScheduledDate);
		edScheduledDatecell.setParent(row);

		// Time
		String eddfrom = entry.getTimeSlotFrom();
		String eddTo   = entry.getTimeSlotTo();
		if(null != eddfrom && null !=eddTo){
			String eddTime = eddfrom.concat("-").concat(eddTo);
			Listcell eddTimeCell = new Listcell(eddTime);
			eddTimeCell.setParent(row);
		}
	}

	protected void populateHeaderRow(Widget<OrderItemWidgetModel, OrderController> widget, Listhead listHeader) {

		
		Listheader listHeaderProduct = new Listheader("Product");
		listHeaderProduct.setWidth("250px");
		listHeader.appendChild(listHeaderProduct);

		Listheader listHeaderUssid = new Listheader("USSID");
		listHeaderUssid.setWidth("120px");
		listHeader.appendChild(listHeaderUssid);

		Listheader listHeaderDate = new Listheader("Schedule Delivery Date");
		listHeaderDate.setWidth("100px");
		listHeader.appendChild(listHeaderDate);

		Listheader listHeaderTime = new Listheader("Schedule Delivery Time");
		listHeaderTime.setWidth("120px");
		listHeader.appendChild(listHeaderTime);

	}

	


}
