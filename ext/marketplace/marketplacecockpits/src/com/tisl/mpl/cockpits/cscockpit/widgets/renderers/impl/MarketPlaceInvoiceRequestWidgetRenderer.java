/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vbox;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultColumnGroupConfiguration;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderItemsWidgetRenderer;

/**
 * @author 1006687
 *
 */
public class MarketPlaceInvoiceRequestWidgetRenderer extends
		OrderDetailsOrderItemsWidgetRenderer {

	private List<TypedObject> ShipmentID;

	@Override
	protected HtmlBasedComponent createContentInternal(
			final ListboxWidget<OrderItemWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		ShipmentID = Collections.synchronizedList(new ArrayList<TypedObject>());
		Div div = new Div();
		if (widget.getWidgetController() instanceof MarketPlaceOrderController) {
			final MarketPlaceOrderController orderController = (MarketPlaceOrderController) widget
					.getWidgetController();
			TypedObject orderTypedObject = orderController.getCurrentOrder();

			if (orderTypedObject.getObject() instanceof OrderModel) {
				final OrderModel orderModel = (OrderModel) orderTypedObject
						.getObject();

				Map<String, List<TypedObject>> awbGroupedEntries = orderController
						.getOrderEntriesGroupByAWB(orderModel);
				if (MapUtils.isNotEmpty(awbGroupedEntries)) {
					for (Map.Entry<String, List<TypedObject>> awb : awbGroupedEntries
							.entrySet()) {
						if (CollectionUtils.isNotEmpty(awb.getValue())) {
							Vbox orderParentVbox = new Vbox();
							orderParentVbox
									.setSclass("invoiceCompleteOrderVbox");
							createOrderDataTable(orderParentVbox,
									awb.getValue(), awb.getKey());
							div.appendChild(orderParentVbox);

						}
					}

					Button confirmButton = new Button(LabelUtils.getLabel(
							"invoice.request.confirm", "invoicerequested",
							new Object[0]));
					confirmButton.setSclass("invoiceConfirmBtn");
					confirmButton.addEventListener(Events.ON_CLICK,
							new EventListener() {

								@Override
								public void onEvent(Event arg0)
										throws Exception {
									if (CollectionUtils.isNotEmpty(ShipmentID)) {
										boolean success = orderController
												.sendInvoice(
														ShipmentID,
														orderModel.getCode());
										if (!success) {
											Messagebox.show(
													LabelUtils.getLabel(widget,
															"invoice.failed",
															new Object[0]),
													MarketplaceCockpitsConstants.ERROR,
													Messagebox.OK,
													Messagebox.ERROR);
										} else {
											Messagebox.show(
													LabelUtils.getLabel(widget,
															"invoice.sucess",
															new Object[0]),
													MarketplaceCockpitsConstants.INFO,
													Messagebox.OK,
													Messagebox.INFORMATION);
										}
									} else {
										Messagebox.show(
												LabelUtils.getLabel(widget,
														"invoice.not.selected",
														new Object[0]),
												MarketplaceCockpitsConstants.ERROR,
												Messagebox.OK, Messagebox.ERROR);
									}

								}
							});
					div.appendChild(confirmButton);
					return div;
				}
			}
		}
		div.appendChild(new Label(LabelUtils.getLabel(widget,
				"no.invoice.found", new Object[0])));
		return div;
	}

	protected void createOrderDataTable(Vbox parentComponent,
			final List<TypedObject> orderEntries, final String awbNumber) {
		Vbox parentVbox = new Vbox();
		parentVbox.setSclass("parentVboxInvoice");

		Listbox tableVbox = new Listbox();
		tableVbox.setSclass("orderDetailTable");

		final Checkbox shipmentChkBox = new Checkbox();
		StringBuilder labelString = new StringBuilder(LabelUtils.getLabel(
				"invoice.shipment.label", "invoicerequested", new Object[0]));
		labelString.append(" ");

		labelString.append(awbNumber);
		shipmentChkBox.setLabel(labelString.toString());
		shipmentChkBox.setSclass("shipmentCheckBox");

		parentVbox.appendChild(shipmentChkBox);

		Listhead tableHeader = new Listhead();
		tableHeader.setSclass("invoiceTableHeader");

		List<ColumnGroupConfiguration> columnConfigurations = (List<ColumnGroupConfiguration>) getMasterColumns();

		if (CollectionUtils.isNotEmpty(columnConfigurations)) {
			for (ColumnGroupConfiguration col : columnConfigurations) {
				Listheader headerLabel = new Listheader(
						(col instanceof DefaultColumnGroupConfiguration) ? ((DefaultColumnGroupConfiguration) col)
								.getLabelWithFallback() : col.getLabel());
				headerLabel.setTooltip(col.getName());
				headerLabel.setSclass("invoiceHeaderLabel");
				tableHeader.appendChild(headerLabel);

			}
		}
		tableVbox.appendChild(tableHeader);

		if (CollectionUtils.isNotEmpty(orderEntries)) {
			for (TypedObject entry : orderEntries) {
				Listitem tableRow = new Listitem();
				tableRow.setSclass("invoiceTableRow");
				createDataRow(entry, tableRow);
				tableVbox.appendChild(tableRow);
			}

			shipmentChkBox.addEventListener(Events.ON_CHECK,
					new EventListener() {

						@Override
						public void onEvent(Event arg0) throws Exception {

							if (StringUtils.isNotBlank(awbNumber)) {
								if (shipmentChkBox.isChecked()) {
									ShipmentID.add(orderEntries.get(0));

								} else {
									ShipmentID.remove(orderEntries.get(0));

								}
							}
						}
					});

		}

		parentVbox.appendChild(tableVbox);

		parentComponent.appendChild(parentVbox);

	}

	protected void createDataRow(TypedObject entryData, Listitem row) {
		List<ColumnConfiguration> columnConfigurations = getColumnConfigurations();
		if (CollectionUtils.isNotEmpty(columnConfigurations)) {
			for (ColumnConfiguration col : columnConfigurations) {

				Listcell rowLabel = new Listcell(ObjectGetValueUtils.getValue(
						col.getValueHandler(), entryData));
				rowLabel.setSclass("invoiceRowLabel");
				row.appendChild(rowLabel);

			}
		}

	}
}
