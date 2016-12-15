/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
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
	private static final String PIN_REGEX = "^[1-9][0-9]{5}";
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
	private Intbox intPincode;

	@WireVariable("cancelReturnFacade")
	private CancelReturnFacade cancelReturnFacade;
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		startDateValue.setValue(cal.getTime());
		endDateValue.setValue(new Date());
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());
		getReturnPickUpAddress(cal.getTime(), new Date(), null, null, null);
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
		getReturnPickUpAddress(startDateValue.getValue(), endDateValue.getValue(), null, null, null);

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
		getReturnPickUpAddress(startDateValue.getValue(), endDateValue.getValue(), null, null, null);
	}

	private void msgBox(final String mesg)
	{
		Messagebox.show(mesg, "Error", Messagebox.OK, Messagebox.ERROR);
	}

	@ViewEvent(componentID = "btnPickUpAddressSearch", eventName = Events.ON_CLICK)
	public void getPickUpAddressRequestData()
	{
		int count = 0;
		String pincode = null;
		if (txtOrderId != null && StringUtils.isNotEmpty(txtOrderId.getValue()) && StringUtils.isNotBlank(txtOrderId.getValue()))
		{
			++count;
		}
		if (txtCustomerID != null && StringUtils.isNotEmpty(txtCustomerID.getValue())
				&& StringUtils.isNotBlank(txtCustomerID.getValue()))
		{
			++count;
		}
		if (intPincode != null)
		{
			pincode = String.valueOf(intPincode.getValue());
			++count;
		}
		if (count > 0)
		{
			getReturnPickUpAddress(startDateValue.getValue(), endDateValue.getValue(), txtOrderId.getValue(),
					txtCustomerID.getValue(), pincode);
		}
		else
		{
			Messagebox.show("At least one field is manditory");
			return;
		}

	}

	private void getReturnPickUpAddress(final Date fromDate, final Date toDate, final String orderId, final String custimerId,
			final String pincode)
	{
		final List<MplReturnPickUpAddressInfoModel> returnPickUpAddressData = cancelReturnFacade.getPickUpReturnReport(fromDate,
				toDate, orderId, custimerId, pincode);
		if (CollectionUtils.isNotEmpty(returnPickUpAddressData))
		{
			final ListModelList<MplReturnPickUpAddressInfoModel> listModelList = new ListModelList<MplReturnPickUpAddressInfoModel>();
			listBoxData.setModel(listModelList);
			listBoxData.setItemRenderer(new ReturnPickupAddressItemRenderer());
		}
		else
		{
			Messagebox.show("No Information Found for Return");
		}

	}
}
