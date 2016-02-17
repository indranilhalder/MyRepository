package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Span;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.tisl.mpl.service.MplAwbStatusService;
import com.tisl.mpl.xml.pojo.AWBStatusResponse;
import com.tisl.mpl.xml.pojo.AWBStatusResponse.AWBResponseInfo;
import com.tisl.mpl.xml.pojo.AWBStatusResponse.AWBResponseInfo.StatusRecords;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.models.impl.DefaultMasterDetailListWidgetModel;
import de.hybris.platform.cscockpit.widgets.popup.PopupWindowCreator;
import de.hybris.platform.cscockpit.widgets.renderers.impl.ConfigurableCsMasterDetailListboxWidgetRenderer;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

public class MarketPlaceConfigurableCsMasterDetailListboxWidgetRenderer extends
		ConfigurableCsMasterDetailListboxWidgetRenderer {

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceConfigurableCsMasterDetailListboxWidgetRenderer.class);
	protected static final String CSS_ORDER_DETAILS_LABEL = "csOrderDetailsLabel";
	protected static final String CSS_ORDER_DETAILS_CONSIGNMENTS = "csOrderDetailsConsignments";
	private static final String DELIVERY_TRACKING_DETAILS = "csDeliveryTrackingDetails";
	private static final String CS_DELIVERY_TRACKING_DETAIL_POPUP = "csDeliveryTrackingDetails";

	@Autowired
	private PopupWindowCreator popupWindowCreator;

	@Autowired
	private MplAwbStatusService mplAwbStatusService;

	@Override
	protected Object populateHeaderRow(
			ListboxWidget<DefaultMasterDetailListWidgetModel<TypedObject>, WidgetController> widget,
			Listhead row) {
		super.populateHeaderRow(widget, row);
		Listheader headerLabel = new Listheader("Delivery Tracking");
		row.appendChild(headerLabel);
		return null;
	}

	@Override
	protected void populateMasterRow(
			ListboxWidget<DefaultMasterDetailListWidgetModel<TypedObject>, WidgetController> widget,
			Listitem row, Object context, TypedObject item) {
		super.populateDataForRow(row, getColumnConfigurations(), item);
		Div tempDiv = new Div();
		Listcell rowLabel = new Listcell();
		rowLabel.appendChild(tempDiv);
		createDeliveryTrackingLink(widget, tempDiv, item);
		row.appendChild(rowLabel);
	}

	private void createDeliveryTrackingLink(final Widget widget,
			final Div container, final TypedObject item) {
		Div div = new Div();
		div.setParent(container);
		Span span = new Span();
		span.setParent(div);
		span.setSclass("csProductLink");
		Toolbarbutton viewdeliveryTrackingDetails = new Toolbarbutton(
				"Delivery Tracking");
		viewdeliveryTrackingDetails.setParent(span);
		viewdeliveryTrackingDetails.setSclass("blueLink");
		viewdeliveryTrackingDetails.addEventListener("onClick",
				createDeliveryTrackingListener(widget, item, div));
	}

	protected EventListener createDeliveryTrackingListener(Widget widget,
			TypedObject item, Div div) {
		return new OpenDeliveryTrackingWindowEventListener(widget, item, div);
	}

	protected class OpenDeliveryTrackingWindowEventListener implements
			EventListener {
		private final Widget widget;
		private final TypedObject item;
		private final Div div;

		public OpenDeliveryTrackingWindowEventListener(Widget widget,
				TypedObject item, Div div) {
			this.widget = widget;
			this.item = item;
			this.div = div;
		}

		public void onEvent(Event event) throws Exception {
			handleDeliveryTrackingWindowEvent(this.widget, event, item, div);
		}
	}

	private void handleDeliveryTrackingWindowEvent(final Widget widget,
			final Event event, final TypedObject item, final Div div) {
		final Div productDetailDiv = new Div();
		productDetailDiv.setParent(div);
		Window popupWindow = popupWindowCreator.createModalPopupWindow(widget,
				LabelUtils.getLabel(widget, DELIVERY_TRACKING_DETAILS,
						new Object[0]), CS_DELIVERY_TRACKING_DETAIL_POPUP, 510,
				productDetailDiv);
		populateDeliveryTrackingPopup(widget, productDetailDiv, popupWindow,
				item);
	}

	protected void populateDeliveryTrackingPopup(final Widget widget,
			Div container, final Window popupWindow, final TypedObject item) {
		container.setSclass("csProductDetailsWindow");
		Listbox listbox = populateDeliveryTrackingHeaderRow(widget, container);
		populateListData(widget, container, item, listbox);
	}

	protected Listbox populateDeliveryTrackingHeaderRow(Widget widget,
			Div container) {
		Listbox listBox = new Listbox();
		container.appendChild(listBox);
		Listhead row = new Listhead();
		listBox.appendChild(row);
		Listheader listheader = new Listheader(LabelUtils.getLabel(widget,
				"date", new Object[0]));
		listheader.setWidth("110px");
		listheader.setSclass("listPriceColHeader");
		row.appendChild(listheader);
		listheader = new Listheader(LabelUtils.getLabel(widget, "time",
				new Object[0]));
		listheader.setWidth("110px");
		listheader.setSclass("listPriceColHeader");
		row.appendChild(listheader);
		listheader = new Listheader(LabelUtils.getLabel(widget, "location",
				new Object[0]));
		listheader.setWidth("130px");
		listheader.setSclass("listPriceColHeader");
		row.appendChild(listheader);
		listheader = new Listheader(LabelUtils.getLabel(widget, "description",
				new Object[0]));
		listheader.setWidth("130px");
		listheader.setSclass("listPriceColHeader");
		row.appendChild(listheader);
		return listBox;
	}

	protected AWBStatusResponse getAWBStatusResponse(final TypedObject item) {
		ConsignmentModel consignmentModel = (ConsignmentModel) item.getObject();

		return mplAwbStatusService
				.prepAwbNumbertoOMS(consignmentModel.getTrackingID(),
						consignmentModel.getCarrier());
	}

	private void populateListData(final Widget widget, final Div container,
			final TypedObject item, final Listbox listbox) {
		//ConsignmentModel consignmentModel = (ConsignmentModel) item.getObject();
		AWBStatusResponse aWBStatusResponse = getAWBStatusResponse(item);
		Listitem DeliveryTrackingItem = null;
		Label label = null;
		Listcell cell = null;
		if (null != aWBStatusResponse
				&& CollectionUtils.isNotEmpty(aWBStatusResponse
						.getAWBResponseInfo())) {
			for (AWBResponseInfo awbResponseInfo : aWBStatusResponse
					.getAWBResponseInfo()) {
				for (StatusRecords statusRecords : awbResponseInfo
						.getStatusRecords()) {
					DeliveryTrackingItem = new Listitem();
					DeliveryTrackingItem.setParent(listbox);
					cell = new Listcell();
					cell.setParent(DeliveryTrackingItem);
					label = new Label(statusRecords.getDate());
					label.setParent(cell);
					cell = new Listcell();
					cell.setParent(DeliveryTrackingItem);
					label = new Label(statusRecords.getTime());
					label.setParent(cell);
					cell = new Listcell();
					cell.setParent(DeliveryTrackingItem);
					label = new Label(statusRecords.getLocation());
					label.setParent(cell);
					cell = new Listcell();
					cell.setParent(DeliveryTrackingItem);
					label = new Label(statusRecords.getStatusDescription());
					label.setParent(cell);
				}
			}
		} else {
			DeliveryTrackingItem = new Listitem();
			DeliveryTrackingItem.setParent(listbox);
			cell = new Listcell();
			cell.setSpan(4);
			cell.setParent(DeliveryTrackingItem);
			label = new Label("No Details available");
			label.setParent(cell);
		}
	}

	public MplAwbStatusService getMplAwbStatusService() {
		return mplAwbStatusService;
	}

	public void setMplAwbStatusService(MplAwbStatusService mplAwbStatusService) {
		this.mplAwbStatusService = mplAwbStatusService;
	}

}