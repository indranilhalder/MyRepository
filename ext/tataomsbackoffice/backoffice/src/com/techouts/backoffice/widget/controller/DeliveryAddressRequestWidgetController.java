/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.domain.buc.reports.dto.ReportRequest;
import com.hybris.oms.tata.renderer.DeliveryAdressReportItemRenderer;
import com.tis.mpl.facade.changedelivery.MplDeliveryAddressFacade;
import com.tisl.mpl.facades.data.MplDeliveryAddressReportData;


/**
 * this class is used for Delivery Address request report purposes
 *
 * @author prabhakar
 *
 */
public class DeliveryAddressRequestWidgetController extends DefaultWidgetController
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DeliveryAddressRequestWidgetController.class);
	private String startDate;
	private String endDate;

	@Wire
	private Listbox listBoxData;

	@WireVariable(value = "deliveryAddressFacade")
	private MplDeliveryAddressFacade mplDeliveryAddressFacade;

	final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -6);
		super.initialize(comp);
		LOG.info("inside initialize method" + "Start Date " + cal.getTime() + "******* End Date " + new Date());

		getDeliveryAddressRequestInfo(cal.getTime(), new Date());

	}

	/**
	 *
	 * @param startendDates
	 *
	 */
	@SocketEvent(socketId = "startendDates")
	public void getDeliveryAddressRequestByDate(final String startendDates)
	{

		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];

		LOG.info(" inside sockent Start Date " + startDate + "******* End Date " + endDate);
		final ReportRequest reportRequest = new ReportRequest();
		try
		{
			getDeliveryAddressRequestInfo(dateFormat.parse(startDate), dateFormat.parse(endDate));
		}
		catch (final ParseException e)
		{

			e.printStackTrace();
		}




	}

	private void getDeliveryAddressRequestInfo(final Date dateFrom, final Date toDate)
	{
		final Collection<MplDeliveryAddressReportData> deliveryAddressData = mplDeliveryAddressFacade
				.getDeliveryAddressRepot(dateFrom, toDate);

		listBoxData.setModel(new ListModelList<MplDeliveryAddressReportData>(deliveryAddressData));
		listBoxData.setItemRenderer(new DeliveryAdressReportItemRenderer());

	}
}
