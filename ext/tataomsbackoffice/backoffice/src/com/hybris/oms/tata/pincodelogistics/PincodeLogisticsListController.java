/**
 *
 */
package com.hybris.oms.tata.pincodelogistics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.report.sellerperformance.SellerAndLogisticsPerfFacade;
import com.hybris.oms.domain.buc.report.SellerAndLogisticsPerfRpt.dto.SellerAndLogisticsPerfRpt;



/**
 * @author Pradeep
 *
 */
public class PincodeLogisticsListController extends DefaultWidgetController
{

	@WireVariable("omsSellerAndLogisticsPerfRestClient")
	private SellerAndLogisticsPerfFacade sellerAndLogisticsFacade;

	private List<SellerAndLogisticsPerfRpt> sellerLogisticsRptList;
	private Listbox listview;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private String startDate;
	private String endDate;

	private static final Logger LOG = LoggerFactory.getLogger(PincodeLogisticsListController.class);

	@Override
	public void initialize(final Component comp)
	{
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		super.initialize(comp);
		startDate = dateFormat.format(cal.getTime());
		endDate = dateFormat.format(new Date());
		LOG.info("Start Date " + startDate + "******* End Date " + endDate);
		sellerLogisticsRptList = (List<SellerAndLogisticsPerfRpt>) sellerAndLogisticsFacade.getOrderLinesByDate(startDate, endDate);
		sellerLogisticsRptList.sort(PincodeLogisticsListController.pincodeLogisticsListComparator);
		listview.setModel(new ListModelList(sellerLogisticsRptList));
		listview.setItemRenderer(new PincodeLogisticsListRenderer());

	}

	/**
	 *
	 * @param startendTime
	 *
	 */
	@SocketEvent(socketId = "startendDates")
	public void editListView(final String startendDates)
	{

		final String[] startEndArray = startendDates.trim().split(",");
		startDate = startEndArray[0];
		endDate = startEndArray[1];
		LOG.info("Start Date " + startDate + "******* End Date " + endDate);
		sellerLogisticsRptList = (List<SellerAndLogisticsPerfRpt>) sellerAndLogisticsFacade.getOrderLinesByDate(startDate, endDate);
		sellerLogisticsRptList.sort(PincodeLogisticsListController.pincodeLogisticsListComparator);
		listview.setModel(new ListModelList(sellerLogisticsRptList));
		listview.setItemRenderer(new PincodeLogisticsListRenderer());
	}


	/**
	 * export csv file from listview
	 *
	 * @throws InterruptedException
	 */

	@ViewEvent(componentID = "savecsv", eventName = Events.ON_CLICK)
	public void getCsv() throws InterruptedException
	{

		LOG.info("Size list " + sellerLogisticsRptList.size());
		exportToCsv(listview, sellerLogisticsRptList,
				"SnL_Perf_Report_" + startDate.replace("-", "") + "_" + endDate.replace("-", ""));

	}

	/**
	 * This function used to export listbox to csv from listbox
	 *
	 * @param listbox
	 * @throws InterruptedException
	 */

	public static void exportToCsv(final Listbox listbox, final List<SellerAndLogisticsPerfRpt> sellerLogisticsRptDList,
			final String fileName) throws InterruptedException
	{
		final String saperator = ",";
		StringBuffer stringBuff = null;

		if (sellerLogisticsRptDList == null || listbox.getItems() == null || sellerLogisticsRptDList.isEmpty())
		{
			LOG.info("**************************** Pincode Logistics List Is Empty***************************");
			Messagebox.show("List is empty", "Error", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		stringBuff = new StringBuffer("");
		for (final Object head : listbox.getHeads())
		{
			final StringBuffer headLabel = new StringBuffer();
			for (final Object header : ((Listhead) head).getChildren())
			{
				headLabel.append(((Listheader) header).getLabel().concat(saperator));
			}
			stringBuff.append(headLabel.append("\n").toString());
		}
		final Iterator<SellerAndLogisticsPerfRpt> itr = sellerLogisticsRptDList.iterator();
		StringBuffer cellLabel = null;
		while (itr.hasNext())
		{
			cellLabel = new StringBuffer("");
			final SellerAndLogisticsPerfRpt item = itr.next();
			cellLabel.append(item.getSellerId().concat(saperator));
			cellLabel.append(item.getOrderId().concat(saperator));
			cellLabel.append(item.getTransactionId().concat(saperator));

			if (item.getOrderSubDateTime() != null)
			{
				cellLabel.append(item.getOrderSubDateTime().toString().concat(saperator));
			}
			else
			{
				cellLabel.append(saperator);
			}

			cellLabel.append(item.getCustomerId().concat(saperator));
			cellLabel.append(item.getEmail().concat(saperator));
			if (StringUtils.isEmpty(item.getMobileNo()))
			{
				cellLabel.append("NA".concat(saperator));
			}
			else
			{
				cellLabel.append(item.getMobileNo().concat(saperator));
			}
			cellLabel.append(String.valueOf(item.getTatConf2Hotc()).concat(saperator));
			cellLabel.append(String.valueOf(item.getActualTatOrder2Hotc()).concat(saperator));
			cellLabel.append(String.valueOf(item.getTatConfHotc2Delvd()).concat(saperator));
			cellLabel.append(String.valueOf(item.getActualTatHotc2Delvd()).concat(saperator));
			stringBuff.append(cellLabel.toString().concat("\n"));

		}
		Filedownload.save(stringBuff.toString().getBytes(), "text/plain", fileName.concat(".csv"));
	}

	public static Comparator<SellerAndLogisticsPerfRpt> pincodeLogisticsListComparator = new Comparator<SellerAndLogisticsPerfRpt>()
	{
		public int compare(final SellerAndLogisticsPerfRpt arg0, final SellerAndLogisticsPerfRpt arg1)
		{

			if (arg1 == null || arg0 == null || arg1.getOrderSubDateTime() == null || arg0.getOrderSubDateTime() == null)
			{
				return -1;
			}
			return arg1.getOrderSubDateTime().compareTo(arg0.getOrderSubDateTime());


		}
	};

}
