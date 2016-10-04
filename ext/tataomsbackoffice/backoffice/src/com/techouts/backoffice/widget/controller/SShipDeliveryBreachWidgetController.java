/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

import com.hybris.cockpitng.annotations.SocketEvent;
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
import com.techouts.backoffice.exception.InvalideOrderCancelException;


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
	private static final String ORDERSTATUSONE = "ORDCANCL";
	private static final String ORDERSTATUSTWO = "CLONCANC";
	@Wire
	private Datebox startdpic;
	@Wire
	private Datebox enddpic;
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
	private String startDate;
	private String endDate;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
		final SShipTxnInfo shipTxnInfo = new SShipTxnInfo();
		shipTxnInfo.setFromDate(cal.getTime());
		shipTxnInfo.setToDate(new Date());
		listBoxData.setCheckmark(Boolean.TRUE);
		listBoxData.setMultiple(Boolean.TRUE);
		displaySshipDeiveryBreachData(shipTxnInfo);
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
			listBoxData.setMultiple(Boolean.TRUE);
			displaySshipDeiveryBreachData(shipTxnInfo);
		}
		else
		{
			Messagebox.show("Empty Search fields Please Provide at least one value");
		}

	}


	/**
	 *
	 * @param startendDates
	 *
	 */
	@SocketEvent(socketId = "startendDates")
	public void getDeliveryBreachDataByDates(final String startendDates)
	{


		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];
		final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		LOG.info(" SShip Breach " + startDate + "******* End Date " + endDate);
		final SShipTxnInfo shipTxnInfo = new SShipTxnInfo();
		try
		{

			shipTxnInfo.setFromDate(dateFormat.parse(startDate));
			shipTxnInfo.setToDate(dateFormat.parse(endDate));
		}
		catch (final ParseException e)
		{

			e.printStackTrace();
		}
		listBoxData.setMultiple(Boolean.TRUE);
		displaySshipDeiveryBreachData(shipTxnInfo);

	}

	@ViewEvent(componentID = "sshipOrderCancel", eventName = Events.ON_CLICK)
	public void sshipOrderCancel()
	{
		try
		{

			final OrderCancelRequest orderCancelRequest = new OrderCancelRequest();
			if (listBoxData.getSelectedItem() == null || listBoxData.getSelectedItem().equals(""))
			{
				Messagebox.show("Please Select One List Item", "OrderCancel Dialog", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			final Set<Listitem> setOfListBox = listBoxData.getSelectedItems();
			//check for Order Cancel ,weather is given order eligible or not
			final List<OrderCancellableCheckRequest> checkOrdersForCancel = new ArrayList<OrderCancellableCheckRequest>();

			for (final Listitem listItem : setOfListBox) // filter one ignoring for cancellable check the status orders equal to ORDCANCL ,CLONCANC
			{
				final SShipTxnResponseInfo sshipTxnOrder = listItem.getValue();
				final OrderCancellableCheckRequest orderCancelCheckRequest = new OrderCancellableCheckRequest();
				if (StringUtils.isNotEmpty(sshipTxnOrder.getOrderLineStatus())
						&& sshipTxnOrder.getOrderLineStatus().equalsIgnoreCase(ORDERSTATUSONE)
						|| sshipTxnOrder.getOrderLineStatus().equalsIgnoreCase(ORDERSTATUSTWO))
				{
					throw new InvalideOrderCancelException("This order is already cancelled ");
				}
				orderCancelCheckRequest.setOrderId(sshipTxnOrder.getOrderId());
				orderCancelCheckRequest.setTransactionId(sshipTxnOrder.getOrderLineId());
				checkOrdersForCancel.add(orderCancelCheckRequest);
			}
			final OrderCancellableCheckRequests orderCancellableCheckRequests = new OrderCancellableCheckRequests(); //dto to send oms to check the eligible orders for cancel
			orderCancellableCheckRequests.setOrderCancellableCheckRequests(checkOrdersForCancel);

			final OrderCancellableCheckResponses orderCancellableCheckResponse = cancelOrderFacade
					.checkOrderLineCancellable(orderCancellableCheckRequests);
			final List<OrderCancellableCheckResponse> orderCancelResponse = orderCancellableCheckResponse
					.getOrderCancellableCheckResponses();
			int index = 0;
			for (final OrderCancellableCheckResponse orderCancelCheckResponse : orderCancelResponse)
			{
				if (orderCancelCheckResponse.getIsCancellable() == Boolean.FALSE)
				{
					throw new InvalideOrderCancelException(
							"Not Possible to Cancel This Order" + checkOrdersForCancel.get(index).getOrderId());
				}
				++index;
			}
			Messagebox.show("Are you sure to remove?", "Order Cancel Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener()
					{
						public void onEvent(final Event evt) throws InterruptedException, InvalideOrderCancelException
						{
							if (evt.getName().equals("onOK"))
							{
								final List<CancelOrderLine> cancelOrderList = new ArrayList<CancelOrderLine>();
								for (final Listitem listItem : setOfListBox)
								{
									final SShipTxnResponseInfo sshipTxnOrder = listItem.getValue();

									if (StringUtils.isNotEmpty(sshipTxnOrder.getOrderLineStatus())
											&& sshipTxnOrder.getOrderLineStatus().equalsIgnoreCase(ORDERSTATUSONE)
											|| sshipTxnOrder.getOrderLineStatus().equalsIgnoreCase(ORDERSTATUSTWO))
									{
										throw new InvalideOrderCancelException("This order is already cancelled");
									}

									final CancelOrderLine cancelOrderLine = new CancelOrderLine();
									cancelOrderLine.setOrderId(sshipTxnOrder.getOrderId());
									cancelOrderLine.setTransactionId(sshipTxnOrder.getOrderLineId());
									cancelOrderLine.setReturnCancelFlag("C");
									cancelOrderLine.setReasonCode("05");
									cancelOrderLine.setReturnCancelRemarks("admin cancled the breached order");
									cancelOrderLine.setRequestId(sshipTxnOrder.getOrderLineId());
									cancelOrderList.add(cancelOrderLine);
								}

								final OrderCancelRequest orderCancelRequest = new OrderCancelRequest();
								orderCancelRequest.setCancelOrderLine(cancelOrderList);
								cancelOrderFacade.cancelOrderLine(orderCancelRequest);
								Messagebox.show(" Order Cancel Success", "Order Cancellation Dialog", Messagebox.OK,
										Messagebox.INFORMATION);

							}
							else
							{
								Messagebox.show("Removing Canceled", "Order Cancel Dialog", Messagebox.OK, Messagebox.INFORMATION);
							}
						}
					});
		}
		catch (final Exception e)
		{

			Messagebox.show(e.getMessage());
		}
	}

	private void displaySshipDeiveryBreachData(final SShipTxnInfo shipTxnInfo)
	{

		final SShipTxnResponseInfos sshipTxnResponse = sShipTxnFacade.getSShipTxns(shipTxnInfo);
		final List<SShipTxnResponseInfo> listOfSshipResponse = sshipTxnResponse.getSShipTxnResponseInfo();
		final ListModelList<SShipTxnResponseInfo> sshipResponce = new ListModelList<SShipTxnResponseInfo>(listOfSshipResponse);
		sshipResponce.setMultiple(Boolean.TRUE);
		listBoxData.setModel(sshipResponce);
		listBoxData.setItemRenderer(new SShipTransactionInfoItemRenderer());

	}
}