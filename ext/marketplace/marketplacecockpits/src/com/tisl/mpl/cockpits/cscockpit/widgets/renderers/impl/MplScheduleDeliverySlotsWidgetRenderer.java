package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.impl.DefaultListboxWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.renderers.impl.AbstractCsWidgetRenderer;
import de.hybris.platform.servicelayer.model.ModelService;

public class MplScheduleDeliverySlotsWidgetRenderer
 extends AbstractCsWidgetRenderer<DefaultListboxWidget<DefaultListWidgetModel, OrderController>> {
	
	
	private CallContextController callContextController;

	protected CallContextController getCallContextController() {
		return callContextController;
	}

	@Required
	public void setCallContextController(
			CallContextController callContextController) {
		this.callContextController = callContextController;
	}

	public TypedObject getOrder() {
		return getCallContextController().getCurrentOrder();
	}

	@Autowired
	private ModelService modelService;

	protected static final String CSS_ORDER_HISTORY = "csOrderHistory";
	private final static Logger LOG = Logger.getLogger(MplScheduleDeliverySlotsWidgetRenderer.class);
	protected HtmlBasedComponent createContentInternal(DefaultListboxWidget<DefaultListWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		Div content = new Div();
		LOG.info("Inside MplScheduleDeliverySlotsWidgetRenderer  createContentInternal Method");
		TypedObject typedObject = getOrder();
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
					if (null != entry.getEdScheduledDate()) {
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
			DefaultListboxWidget<DefaultListWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		TypedObject order = getOrder();
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
						if(null != entry.getEdScheduledDate()) {
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

	protected void populateDataRow(DefaultListboxWidget<DefaultListWidgetModel, OrderController> widget, AbstractOrderEntryModel entry,
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
		String edScheduledDate = entry.getEdScheduledDate();
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

	protected void populateHeaderRow(DefaultListboxWidget<DefaultListWidgetModel, OrderController> widget, Listhead listHeader) {

		
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
