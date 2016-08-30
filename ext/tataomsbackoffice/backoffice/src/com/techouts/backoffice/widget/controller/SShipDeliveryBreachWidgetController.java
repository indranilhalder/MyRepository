/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.ordercancel.CancelOrderFacade;
import com.hybris.oms.api.sshiptxn.SShipTxnFacade;
import com.hybris.oms.domain.order.cancel.CancelOrderLine;
import com.hybris.oms.domain.order.cancel.OrderCancelRequest;
import com.hybris.oms.domain.order.cancel.OrderCancellableCheckRequest;
import com.hybris.oms.domain.order.cancel.OrderCancellableCheckRequests;
import com.hybris.oms.domain.order.cancel.OrderCancellableCheckResponse;
import com.hybris.oms.domain.order.cancel.OrderCancellableCheckResponses;
import com.hybris.oms.domain.sshiptxninfo.dto.SShipTxnInfo;
import com.hybris.oms.domain.sshiptxnresponseinfo.dto.SShipTxnResponseInfo;
import com.hybris.oms.domain.sshiptxnresponseinfo.dto.SShipTxnResponseInfos;
import com.hybris.oms.tata.renderer.SShipTransactionInfoItemRenderer;


/**
 * this controller class is used for SShip Delivery Breach
 *
 * @author prabhakar
 *
 */
public class SShipDeliveryBreachWidgetController extends DefaultWidgetController
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SShipDeliveryBreachWidgetController.class);
	private static final String ORDERSTATUS = "ORDCANCL";
	@Wire
	private Datebox startdpic;
	@Wire
	private Datebox enddpic;
	@Wire
	private Listbox listBoxData;
	@Wire
	private Textbox txtOrderId;
	@Wire
	private Textbox txtOrderlineId;
	@Wire
	private Textbox txtsellerId;
	@Wire
	private Textbox txtSlaveId;
	@Wire
	private Label msg;
	@WireVariable("sshipTxnRestClient")
	private SShipTxnFacade sShipTxnFacade;
	@WireVariable("orderCancellationRestClient")
	private CancelOrderFacade cancelOrderFacade;

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
		final SShipTxnInfo shipTxnInfo = new SShipTxnInfo();
		shipTxnInfo.setFromDate(cal.getTime());
		shipTxnInfo.setToDate(new Date());
		displaySshipDeiveryBreachData(shipTxnInfo);
		listBoxData.setMultiple(true);
	}

	@ViewEvent(componentID = "sshipOrderSearch", eventName = Events.ON_CLICK)
	public void sshipOrderSearch()
	{
		LOG.info("inside sship order search");
		final SShipTxnInfo shipTxnInfo = new SShipTxnInfo();
		int count = 0;

		if (txtOrderId != null && StringUtils.isNotEmpty(txtOrderId.getValue()) && StringUtils.isNotBlank(txtOrderId.getValue()))
		{
			LOG.info("txt orderID" + txtOrderId.getValue());
			++count;
			shipTxnInfo.setOrderId(txtOrderId.getValue().trim());
		}
		if (txtOrderlineId != null && StringUtils.isNotEmpty(txtOrderlineId.getValue())
				&& StringUtils.isNotBlank(txtOrderlineId.getValue()))
		{
			LOG.info("txtOrderLine Id " + txtOrderlineId.getValue());
			++count;
			shipTxnInfo.setOrderLineId(txtOrderlineId.getValue().trim());
		}
		if (txtsellerId != null && StringUtils.isNotEmpty(txtsellerId.getValue()) && StringUtils.isNotBlank(txtsellerId.getValue()))
		{
			++count;
			LOG.info("txt seller id" + txtsellerId.getValue());
			shipTxnInfo.setSellerId(txtsellerId.getValue().trim());
		}
		if (txtSlaveId != null && StringUtils.isNotEmpty(txtSlaveId.getValue()) && StringUtils.isNotBlank(txtSlaveId.getValue()))
		{
			LOG.info("txt slave id" + txtSlaveId.getValue());
			++count;
			shipTxnInfo.setSlaveId(txtSlaveId.getValue().trim());
		}
		if (count > 0)
		{
			displaySshipDeiveryBreachData(shipTxnInfo);
		}
		else
		{
			if (startdpic.getValue() == null)
			{
				msgBox("Please choose the Start Date");
				return;
			}
			if (enddpic.getValue() == null)
			{
				msgBox("Please choose the End Date");
				return;
			}
			if (startdpic.getValue().compareTo(enddpic.getValue()) > 0)
			{
				msgBox("Start date must be less than or equal to End date");
				return;
			}
			LOG.info("statdate" + startdpic.getValue() + "end date" + enddpic.getValue());
			shipTxnInfo.setFromDate(startdpic.getValue());
			shipTxnInfo.setToDate(enddpic.getValue());
			displaySshipDeiveryBreachData(shipTxnInfo);
		}
	}

	@ViewEvent(componentID = "sshipOrderCancel", eventName = Events.ON_CLICK)
	public void sshipOrderCancel()
	{
		final OrderCancelRequest orderCancelRequest = new OrderCancelRequest();

		if (listBoxData.getSelectedItem() == null || listBoxData.getSelectedItem().equals(""))
		{
			Messagebox.show("Please Select One List Item", "OrderCancel Dialog", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		final Set<Listitem> setOfListBox = listBoxData.getSelectedItems();
		//check for Order Cancel ,weather is given order eligible or not
		final List<OrderCancellableCheckRequest> checkCancellableOrders = new ArrayList<OrderCancellableCheckRequest>();
		for (final Listitem listItem : setOfListBox)
		{
			final SShipTxnResponseInfo sshipTxnOrder = listItem.getValue();
			final OrderCancellableCheckRequest orderCancelCheckRequest = new OrderCancellableCheckRequest();
			if (StringUtils.isNotEmpty(sshipTxnOrder.getOrderLineStatus())
					&& sshipTxnOrder.getOrderLineStatus().equalsIgnoreCase(ORDERSTATUS))
			{
				continue;
			}
			orderCancelCheckRequest.setOrderId(sshipTxnOrder.getOrderId());
			orderCancelCheckRequest.setTransactionId(sshipTxnOrder.getOrderLineId());
			checkCancellableOrders.add(orderCancelCheckRequest);
			LOG.info("call for cancel Order Id" + sshipTxnOrder + "transaction id");
		}
		final OrderCancellableCheckRequests orderCancellableCheckRequests = new OrderCancellableCheckRequests();
		orderCancellableCheckRequests.setOrderCancellableCheckRequests(checkCancellableOrders);
		//calling the facade to
		final OrderCancellableCheckResponses orderCancellableCheckResponse = cancelOrderFacade
				.checkOrderLineCancellable(orderCancellableCheckRequests);
		final List<OrderCancellableCheckResponse> orderCancelResponse = orderCancellableCheckResponse
				.getOrderCancellableCheckResponses();
		if (orderCancelResponse.contains(Boolean.FALSE))
		{
			final StringBuilder cancelOrderMessage = new StringBuilder("the follwoing  order not possible to cancel");
			int index = 0;
			for (final OrderCancellableCheckResponse orderCancelCheckResponse : orderCancelResponse)
			{
				if (orderCancelCheckResponse.getIsCancellable() == Boolean.FALSE)
				{
					cancelOrderMessage.append(checkCancellableOrders.get(index).getOrderId());
				}
				++index;
			}
			msg.setValue(cancelOrderMessage.toString());
		}
		else
		{
			Messagebox.show("Are you sure to remove?", "Order Cancel Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException
						{
							if (evt.getName().equals("onOK"))
							{
								final List<CancelOrderLine> cancelOrderList = new ArrayList<CancelOrderLine>();
								for (final Listitem listItem : setOfListBox)
								{
									final SShipTxnResponseInfo sshipTxnOrder = listItem.getValue();
									final CancelOrderLine cancelOrderLine = new CancelOrderLine();
									cancelOrderLine.setOrderId(sshipTxnOrder.getOrderId());
									cancelOrderLine.setTransactionId(sshipTxnOrder.getOrderLineId());
									cancelOrderLine.setReturnCancelFlag("C");
									cancelOrderLine.setReasonCode("05");
									cancelOrderLine.setReturnCancelRemarks("The product is missing a part");
									cancelOrderLine.setRequestId(new Random(123456789) + "CANCEL");
									cancelOrderList.add(cancelOrderLine);
								}

								final OrderCancelRequest orderCancelRequest = new OrderCancelRequest();
								orderCancelRequest.setCancelOrderLine(cancelOrderList);
								cancelOrderFacade.cancelOrderLine(orderCancelRequest);
								Messagebox.show(" Order Cancel Success", "Order Cancellation Dialog", Messagebox.OK,
										Messagebox.INFORMATION);
								listBoxData.clearSelection();

							}
							else
							{
								Messagebox.show("Removing Canceled", "Order Cancel Dialog", Messagebox.OK, Messagebox.INFORMATION);
							}
						}
					});
		}
	}

	private void msgBox(final String mesg)
	{
		Messagebox.show(mesg, "Error", Messagebox.OK, Messagebox.ERROR);
	}

	private void displaySshipDeiveryBreachData(final SShipTxnInfo shipTxnInfo)
	{
		final SShipTxnResponseInfos sshipTxnResponse = sShipTxnFacade.getSShipTxns(shipTxnInfo);
		final List<SShipTxnResponseInfo> listOfSshipResponse = sshipTxnResponse.getSShipTxnResponseInfo();
		listBoxData.setModel(new ListModelList<SShipTxnResponseInfo>(listOfSshipResponse));
		listBoxData.setItemRenderer(new SShipTransactionInfoItemRenderer());
	}
}