/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.sshiptxn.SShipTxnFacade;
import com.hybris.oms.domain.sshiptxninfo.dto.SShipTxnInfo;
import com.hybris.oms.domain.sshiptxnresponseinfo.dto.SShipTxnResponseInfo;
import com.hybris.oms.domain.sshiptxnresponseinfo.dto.SShipTxnResponseInfos;
import com.hybris.oms.tata.renderer.SShipTransactionInfoItemRenderer;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;


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
	@WireVariable("sshipTxnRestClient")
	private SShipTxnFacade sShipTxnFacade;
	@WireVariable("cancelReturnFacade")
	private CancelReturnFacade cancelReturnFacade;
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
			Messagebox.show("Atleast one field is mandatory");
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
		if (listBoxData.getSelectedItem() == null || listBoxData.getSelectedItem().equals(""))
		{
			Messagebox.show("Please Select at least One List Item", "OrderCancel Dialog", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		final SShipTxnResponseInfo sShipTxnResponseInfo = listBoxData.getSelectedItem().getValue();
		try
		{
			final boolean orderCancelStatus = cancelReturnFacade.orderCancellationFromBackoffice(sShipTxnResponseInfo.getOrderId(),
					sShipTxnResponseInfo.getOrderLineId());
			if (orderCancelStatus)
			{
				Messagebox.show("Order Cancel Success");
				return;
			}
			else
			{
				Messagebox.show("Order Cancel Faild");
				return;
			}
		}
		catch (final Exception e)
		{
			Messagebox.show("Order Cancel Faild");
			e.printStackTrace();
		}
	}


	private void displaySshipDeiveryBreachData(final SShipTxnInfo shipTxnInfo)
	{

		final SShipTxnResponseInfos sshipTxnResponse = sShipTxnFacade.getSShipTxns(shipTxnInfo);
		final List<SShipTxnResponseInfo> listOfSshipResponse = sshipTxnResponse.getSShipTxnResponseInfo();
		listBoxData.setModel(new ListModelList<SShipTxnResponseInfo>(listOfSshipResponse));
		listBoxData.setItemRenderer(new SShipTransactionInfoItemRenderer());

	}
}