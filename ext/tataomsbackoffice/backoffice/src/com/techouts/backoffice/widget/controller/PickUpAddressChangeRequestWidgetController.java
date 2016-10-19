/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;


/**
 * this class is used for Pick Up address change request report
 *
 * @author prabhakar
 *
 */
public class PickUpAddressChangeRequestWidgetController extends DefaultWidgetController
{
	private static final Logger LOG = LoggerFactory.getLogger(PickUpAddressChangeRequestWidgetController.class);
	final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static final String PIN_REGEX = "^[1-9][0-9]{5}";

	private String startDate;
	private String endDate;
	@Wire
	private Textbox txtOrderId;
	@Wire
	private Textbox txtCustomerID;
	@Wire
	private Intbox intPincode;
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);

	}

	@SocketEvent(socketId = "startendDates")
	public void getPickUpAddressRequestByDate(final String startendDates)
	{
		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];
		LOG.info(" in side Get pick Up adderss Data By date " + startDate + "******* End Date " + endDate);
	}

	@ViewEvent(componentID = "btnPickUpAddressSearch", eventName = Events.ON_CLICK)
	public void getPickUpAddressRequestData()
	{
		//	final PickUpAddressReportsSearchData pickUpAddressSearchDate = new PickUpAddressReportsSearchData();

		if (txtOrderId != null && StringUtils.isNotEmpty(txtOrderId.getValue()) && StringUtils.isNotBlank(txtOrderId.getValue()))
		{
			//		pickUpAddressSearchDate.setOrderId(txtOrderId.getValue());
		}
		if (txtCustomerID != null && StringUtils.isNotEmpty(txtCustomerID.getValue())
				&& StringUtils.isNotBlank(txtCustomerID.getValue()))
		{
			//		pickUpAddressSearchDate.setCustomerId(txtCustomerID.getValue());
		}
		if (intPincode != null)
		{
			final String pincode = String.valueOf(intPincode.getValue());
			if (pincode.matches(PIN_REGEX))
			{
				//			pickUpAddressSearchDate.setPinCode(pincode);
			}
			else
			{
				Messagebox.show("please provide valid picode");
				return;
			}

		}

	}

}
