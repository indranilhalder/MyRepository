/**
 * 
 */
package com.hybris.oms.tata.orders;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.buc.order.OrderBUCFacade;
import com.hybris.oms.domain.orderbuc.dto.OrderBUC;



/**
 * @author Saood
 * 
 */
public class OrdersListController extends DefaultWidgetController
{

	@WireVariable("orderBUCRestClient")
	private OrderBUCFacade orderBUCFacade;
	private Listbox listView;



	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		final List<OrderBUC> list = (List<OrderBUC>) orderBUCFacade.getAll();
		listView.setModel(new ListModelList(list));
		listView.setItemRenderer(new OrderListRenderer());
	}

	@ViewEvent(componentID = "listView", eventName = Events.ON_SELECT)
	public void itemSelect()
	{

		final OrderBUC buc = (OrderBUC) listView.getSelectedItem().getValue();
		sendOutput("itemSelected", buc.getOrderId());

	}

}
