package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl.MarketPlaceDefaultOrderController;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsWidgetRenderer;
import de.hybris.platform.servicelayer.config.ConfigurationService;

public class MarketPlaceOrderDetailsWidgetRenderer extends
		OrderDetailsWidgetRenderer {
	
	@Autowired
	private ConfigurationService configurationService;

	protected HtmlBasedComponent createContentInternal(
			Widget<OrderItemWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		Div content = new Div();
		content.setSclass("csOrderDetailsWidget");
		TypedObject order = ((OrderItemWidgetModel) widget.getWidgetModel())
				.getOrder();
		if ((order != null) && (order.getObject() instanceof OrderModel)) {
			HtmlBasedComponent detailContent = getDetailRenderer()
					.createContent(null, order, widget);
			if (detailContent != null) {
				detailContent.setParent(content);
			}
			createRefreshButton(widget, content, "refresh");
			if (isUserInRole(configurationService
					.getConfiguration()
					.getString(	MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_SYNCCSAGENTGROUP))) {
				createSyncButton(widget, content, "sync");
			}

		} else {
			Label dummyLabel = new Label(LabelUtils.getLabel(widget,
					"noOrderSelected", new Object[0]));
			dummyLabel.setParent(content);
		}
		return content;
	}

	protected void createSyncButton(
			final Widget<OrderItemWidgetModel, OrderController> widget,
			Div container, String buttonLabelName) {
		Button button = new Button(LabelUtils.getLabel(widget, buttonLabelName,
				new Object[0]));
		button.setParent(container);

		button.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) throws Exception {
				handleSyncOrderListEvent(widget, event);
				handleRefreshTicketListEvent(widget, event);
			}
		});
	}

	protected void handleSyncOrderListEvent(
			Widget<OrderItemWidgetModel, OrderController> widget, Event event) {
		OrderModel currentOrder = (OrderModel) widget.getWidgetController()
				.getCurrentOrder().getObject();
		((MarketPlaceOrderController) widget.getWidgetController())
				.syncOrder(currentOrder);

	}

	private boolean isUserInRole(String groupName) {
		Set<PrincipalGroupModel> userGroups = UISessionUtils
				.getCurrentSession().getUser().getAllGroups();

		for (PrincipalGroupModel ug : userGroups) {
			if (ug.getUid().equalsIgnoreCase(groupName)) {
				return true;
			}
		}
		return false;
	}
}
