/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.tata.renderer.ReturnPickupAddressItemRenderer;
import com.tisl.mpl.core.model.MplReturnPickUpAddressInfoModel;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;


/**
 * this class is used for Pick Up address change request report
 *
 * @author
 *
 */
public class PickUpAddressChangeRequestWidgetController extends DefaultWidgetController
{
	private static final Logger LOG = LoggerFactory.getLogger(PickUpAddressChangeRequestWidgetController.class);
	@Wire
	private Datebox startDateValue;
	@Wire
	private Datebox endDateValue;
	@Wire
	private Listbox listBoxData;
	@Wire
	private Textbox txtOrderId;
	@Wire
	private Textbox txtCustomerID;
	@Wire
	private Textbox intPincode;

	@WireVariable("cancelReturnFacade")
	private CancelReturnFacade cancelReturnFacade;
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final String PIN_REGEX = "^[1-9][0-9]{5}";
	private List<MplReturnPickUpAddressInfoModel> returnPickUpAddressData;

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		startDateValue.setValue(cal.getTime());
		endDateValue.setValue(new Date());
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
		getReturnPickUpAddressByDate(cal.getTime(), new Date());
	}

	/**
	 * This method is to Get the selected date
	 */
	@ViewEvent(componentID = "startDateValue", eventName = Events.ON_CHANGE)
	public void getStartdpic()
	{

		if (startDateValue.getValue().after(endDateValue.getValue())) //this kind of comparison ,it will check date along with time sec
		{
			msgBox("Start date must be less than End date");
			return;
		}
		LOG.info("Start date " + startDateValue.getValue() + "end date " + endDateValue.getValue() + "output socket sended");
		getReturnPickUpAddressByDate(startDateValue.getValue(), endDateValue.getValue());

	}

	/**
	 * This method is to Get the selected date
	 */
	@ViewEvent(componentID = "endDateValue", eventName = Events.ON_CHANGE)
	public void getEnddpic()
	{
		if (endDateValue.getValue().before(startDateValue.getValue()))
		{
			msgBox("End date must be greater than  Start date");
			return;
		}

		LOG.info("Start date " + startDateValue.getValue() + "end date " + endDateValue.getValue() + "output socket sended");
		getReturnPickUpAddressByDate(startDateValue.getValue(), endDateValue.getValue());
	}

	private void msgBox(final String mesg)
	{
		Messagebox.show(mesg, "Error", Messagebox.OK, Messagebox.ERROR);
	}

	@ViewEvent(componentID = "btnPickUpAddressSearch", eventName = Events.ON_CLICK)
	public void getPickUpAddressRequestData()
	{
		int count = 0;
		String pincode = intPincode.getValue();
		String orderId = txtOrderId.getValue();
		String customerId = txtCustomerID.getValue();
		if (orderId != null && StringUtils.isNotBlank(orderId) && StringUtils.isNotEmpty(orderId))
		{
			++count;
		}
		if (customerId != null && StringUtils.isNotBlank(customerId) && StringUtils.isNotEmpty(customerId))
		{
			++count;
		}
		if (pincode != null && StringUtils.isNotBlank(pincode) && StringUtils.isNotEmpty(pincode))
		{
			if (!pincode.matches(PIN_REGEX))
			{
				Messagebox.show("Invalid Pincode ,Please provide valid pincode");
				return;
			}
			++count;
		}
		if (count > 0)
		{
			getPickUpReturnReportByParams(orderId, customerId, pincode);
			pincode = null;
			orderId = null;
			customerId = null;
		}
		else
		{
			Messagebox.show("At least one field is manditory");
			return;
		}
	}

	private void getPickUpReturnReportByParams(final String orderId, final String custimerId, final String pincode)
	{

		returnPickUpAddressData = cancelReturnFacade.getPickUpReturnReportByParams(orderId, custimerId, pincode);
		listBoxData.setModel(new ListModelList<MplReturnPickUpAddressInfoModel>(returnPickUpAddressData));
		listBoxData.setItemRenderer(new ReturnPickupAddressItemRenderer());

	}

	private void getReturnPickUpAddressByDate(final Date fromDate, final Date toDate)
	{

		returnPickUpAddressData = cancelReturnFacade.getPickUpReturnReportByDates(fromDate, toDate);
		listBoxData.setModel(new ListModelList<MplReturnPickUpAddressInfoModel>(returnPickUpAddressData));
		listBoxData.setItemRenderer(new ReturnPickupAddressItemRenderer());

	}

	/**
	 * export csv file from listview
	 *
	 * @throws InterruptedException
	 */
	@ViewEvent(componentID = "savecsv", eventName = Events.ON_CLICK)
	public void getCsv() throws InterruptedException
	{

		exportToCsv(listBoxData, returnPickUpAddressData,
				"PickUpAddressChangeREquest_" + startDateValue.getValue() + "_" + endDateValue.getValue());

	}

	public static void exportToCsv(final Listbox listbox, final List<MplReturnPickUpAddressInfoModel> returnPickUpAddressList,
			final String fileName) throws InterruptedException
	{
		final String saperator = ",";
		final StringBuffer stringBuff = new StringBuffer("");

		if (returnPickUpAddressList == null || listbox.getItems() == null || returnPickUpAddressList.isEmpty())
		{
			LOG.info("****************************Return pickup Address is empty***************************");
			Messagebox.show("List is empty", "Empty Data", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		for (final Object head : listbox.getHeads())
		{
			for (final Object header : ((Listhead) head).getChildren())
			{
				stringBuff.append(((Listheader) header).getLabel().concat(saperator));
			}
			stringBuff.append("\n");
		}
		returnPickUpAddressList.forEach(new Consumer<MplReturnPickUpAddressInfoModel>()
		{
			@Override
			public void accept(final MplReturnPickUpAddressInfoModel returnPickUpAddressData)
			{

				stringBuff.append(returnPickUpAddressData.getOrderId().concat(saperator));
				stringBuff.append(returnPickUpAddressData.getTransactionId().concat(saperator));
				stringBuff.append(returnPickUpAddressData.getPincode().concat(saperator));
				stringBuff.append(returnPickUpAddressData.getCreationtime().toString().concat(saperator));
				stringBuff.append(returnPickUpAddressData.getStatus().concat(saperator));
				stringBuff.append("\n");
			}
		});
		Filedownload.save(stringBuff.toString().getBytes(), "text/plain", fileName.concat(".csv"));
	}
}
