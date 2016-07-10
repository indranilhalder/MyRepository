package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;

import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderItemsWidgetRenderer;

public class MarketPlaceAssociatedOrderDetailsRenderer extends
		OrderDetailsOrderItemsWidgetRenderer {

	protected HtmlBasedComponent createContentInternal(
			final ListboxWidget<OrderItemWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		if ((widget.getWidgetModel() != null)
				&& CollectionUtils.isNotEmpty((((OrderItemWidgetModel) widget
						.getWidgetModel()).getItems()))) {
			Div div = new Div();
			if (widget.getWidgetModel().getOrder() != null
					&& widget.getWidgetModel().getOrder().getObject() instanceof OrderModel) {

				if (widget.getWidgetController() instanceof MarketPlaceOrderController) {
					OrderModel orderModel = (OrderModel) widget
							.getWidgetModel().getOrder().getObject();
					List<OrderModel> childOrders = orderModel.getChildOrders();
					if (CollectionUtils.isNotEmpty(childOrders)) {
						Label orderHeaderLabel = new Label(LabelUtils.getLabel(
								"order.label.heading", "associatedorders",
								new Object[0])
								+ orderModel.getCode());
						orderHeaderLabel.setSclass("orderHeader");
						div.appendChild(orderHeaderLabel);
						Vbox table = new Vbox();
						table.setSclass("associatedTable");
						Hbox headerRow = new Hbox();
						headerRow.setSclass("asstableHeaderRow");
						Label headerLabel = new Label(LabelUtils.getLabel(
								"associatedheader.label", "associatedorders",
								new Object[0]));
						headerLabel.setSclass("headerRow");
						headerRow.appendChild(headerLabel);
						table.appendChild(headerRow);
						for (final OrderModel childOrder : childOrders) {
							Hbox dataRow = new Hbox();
							dataRow.setSclass("dataRow");
							final Label label1 = new Label(childOrder.getCode());
							label1.setSclass("asslabel");
							dataRow.appendChild(label1);
							Button selectButton = new Button("Select");
							selectButton.addEventListener(Events.ON_CLICK,
									createOrderListener(widget, childOrder));
							selectButton.setSclass("selectButton");
							dataRow.appendChild(selectButton);
							table.appendChild(dataRow);
						}
						div.appendChild(table);
					}
				}
			}
			return div;
		}
		return null;
	}

	private EventListener createOrderListener(
			ListboxWidget<OrderItemWidgetModel, OrderController> widget,
			OrderModel childOrder) {
		return new orderEventListener(widget, childOrder);
	}

	protected class orderEventListener implements EventListener {
		private final ListboxWidget<OrderItemWidgetModel, OrderController> widget;
		private final OrderModel childOrder;
		
		public orderEventListener(
				ListboxWidget<OrderItemWidgetModel, OrderController> widget,
				final OrderModel childOrder) {
			this.widget = widget;
			this.childOrder = childOrder;
		}

		public void onEvent(Event event) {
			final MarketPlaceOrderController controller = (MarketPlaceOrderController) widget
					.getWidgetController();
			controller.getContextController().setCurrentOrder(
					getCockpitTypeService().wrapItem(childOrder));
			controller.getContextController().dispatchEvent(null, null, null);
		}
	}
}