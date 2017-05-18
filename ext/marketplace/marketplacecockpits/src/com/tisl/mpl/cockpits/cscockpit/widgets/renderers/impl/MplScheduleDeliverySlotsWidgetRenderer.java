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
			/*PRDI-115 START */
			content.setSclass("csOrderHistory");
			Listbox listBox = new Listbox();
			listBox.setParent(content);
			listBox.setSclass("csWidgetListbox");
			boolean orderHasDeliverySlots = false;
			if(null != order && null != order.getType() && order.getType().equalsIgnoreCase("PARENT")) {
				Listitem row = new Listitem();
				row.setParent(listBox);
				Listcell cell = new Listcell(LabelUtils.getLabel(widget,
						"noEntries", new Object[0]));
				cell.setParent(row);
				return content;
			} else {
				if (null != order.getEntries()) {
					for (AbstractOrderEntryModel entry : order.getEntries()) {
						if (null != entry.getEdScheduledDate() || null != entry.getSddDateBetween()) {
							orderHasDeliverySlots = true;
							break;
						}
					}
				}
			}
			LOG.debug("Order " + order.getCode()
					+ " has schedule Delviery slots " + orderHasDeliverySlots);
			/*PRDI-115 END */
			
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

		Listhead header = new Listhead();
		header.setParent(listBox);
		populateHeaderRow(widget, header);
		if(null !=orderModel && null !=orderModel.getEntries()) {
				for(AbstractOrderEntryModel suborderEntry: orderModel.getEntries() ) {
						if(null != suborderEntry.getEdScheduledDate() || null != suborderEntry.getSddDateBetween()) {
							Listitem row = new Listitem();
							row.setParent(listBox);
							populateDataRow(widget, suborderEntry, row);
						}
				}
		}
	}

	protected void populateDataRow(Widget<OrderItemWidgetModel, OrderController> widget, AbstractOrderEntryModel entry,
			Listitem row) {
		/*PRDI-115 START */
		// Entry Number
		String entryNumber = String.valueOf(entry.getEntryNumber());
		Listcell entryNumberCell = new Listcell(entryNumber);
		entryNumberCell.setParent(row);

		// Transaction ID
		String transactionId = entry.getTransactionID();
		Listcell transactionIdCell = new Listcell(transactionId);
		transactionIdCell.setParent(row);
		/*PRDI-115 END */
		
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

		/*PRDI-115 START */
		Listheader entryNumberHeader= new Listheader("");
		entryNumberHeader.setWidth("2px");
		listHeader.appendChild(entryNumberHeader);
		
		Listheader listHeaderTransactionId= new Listheader("Transaction Id");
		listHeaderTransactionId.setWidth("130px");
		listHeader.appendChild(listHeaderTransactionId);
		/*PRDI-115 END */
		
		Listheader listHeaderUssid = new Listheader("USSID");
		listHeaderUssid.setWidth("120px");
		listHeader.appendChild(listHeaderUssid);

		Listheader listHeaderDate = new Listheader("Schedule/Expected Delivery Date");
		listHeaderDate.setWidth("150px");
		listHeader.appendChild(listHeaderDate);

		Listheader listHeaderTime = new Listheader("Schedule/Expected Delivery Time");
		listHeaderTime.setWidth("150px");
		listHeader.appendChild(listHeaderTime);
	}
}
