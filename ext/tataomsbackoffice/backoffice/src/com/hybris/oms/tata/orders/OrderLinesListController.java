/**
 *
 */
package com.hybris.oms.tata.orders;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import com.hybris.oms.api.logistics.LogisticsFacade;
import com.hybris.oms.api.stockroomlocations.StockRoomLocationsFacade;
import com.hybris.oms.buc.order.OrderBUCFacade;
import com.hybris.oms.buc.order.OrderDetailsBUCFacade;
import com.hybris.oms.domain.logistics.dto.Logistics;
import com.hybris.oms.domain.orderbuc.dto.BUCStatusRecords;
import com.hybris.oms.domain.orderbuc.dto.OrderBUC;
import com.hybris.oms.domain.orderbuc.dto.OrderLineBUC;
import com.hybris.oms.domain.orderbuc.dto.RefundedInfoBUC;
import com.hybris.oms.domain.sp.dto.SlaveInfo;



/**
 * @author nagarjuna
 *
 */
public class OrderLinesListController
{
	private static final Logger LOG = LoggerFactory.getLogger(OrderLinesListController.class);

	@WireVariable("orderDetailsBUCRestClient")
	private OrderDetailsBUCFacade orderDetailsBUCFacade;

	@WireVariable("orderBUCRestClient")
	private OrderBUCFacade orderBUCFacade;

	@WireVariable("logisticsRestClient")
	private LogisticsFacade logisticsFacade;

	@WireVariable("stockroomLocationsRestClient")
	private StockRoomLocationsFacade stockRoomLocationsFacade;




	private String searchText;
	private List<OrderBUC> busList;
	private List<OrderLineBUC> orderlinebusList;

	private List<RefundedInfoBUC> refundedInfoBUCList;

	private ListModelList<Logistics> p1LogisticsIDList;
	private Logistics selectedP1LogisticsID;
	private ListModelList<Logistics> p2LogisticsIDList;
	private Logistics selectedP2LogisticsID;

	private ListModelList<SlaveInfo> p1SlaveIDList;
	private SlaveInfo selectedP1SlaveID;


	private ListModelList<SlaveInfo> p2SlaveIDList;


	private OrderBUC selectedOrderBUC;

	private OrderBUC orderBUC;
	private boolean editorVisible;
	private boolean slaveinfoBtn;

	private OrderLineBUC selectedOLBuc;
	private OrderLineBUC transTrackOLBus;
	private List<BUCStatusRecords> shipStatusRepBusSR;

	private String p1SlaveName;
	private String p2SlaveName;
	private String p1LogisticsName;
	private String p2LogisticsName;

	private boolean sShipStatus = false;

	@Wire("#orderListbox")
	private Listbox orderListbox;
	@Wire("#orderLinesListbox")
	private Listbox orderLinesListbox;
	@Wire("#p1SlaveIDListbox")
	private Listbox p1SlaveIDListbox;

	@Wire("#p2SlaveIDListbox")
	private Listbox p2SlaveIDListbox;

	@Wire("#p1LogisticsIDListbox")
	private Listbox p1LogisticsIDListbox;

	@Wire("#p2LogisticsIDListbox")
	private Listbox p2LogisticsIDListbox;

	private boolean p1slavesDropDownsStatus;

	private boolean logisticsDropDownsStatus;

	private boolean p1slavesVisibleStatus;

	@Init
	public void initialize()
	{

		LOG.info("******************************OrderLinesZul Intializing............................");
		editorVisible = false;
		slaveinfoBtn = false;
		p1slavesVisibleStatus = false;
		sShipStatus = false;
		if (orderBUCFacade != null)
		{
			busList = (List<OrderBUC>) orderBUCFacade.getAll();
			busList.sort(bUCOrderListComparator);
		}


	}





	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) final Component view)
	{
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange(
	{ "editorVisible" })
	public void searchEnter()
	{
		commonLogicForSearch();
	}

	@Command
	@NotifyChange(
	{ "editorVisible" })
	public void searchClick()
	{
		commonLogicForSearch();
	}


	public void commonLogicForSearch()
	{
		LOG.debug("******************************OrderLinesZul Search Button Clicked............................");

		if (null == searchText || "".equals(searchText))
		{
			Messagebox.show("Please Enter OrderId");
			return;
		}

		if (orderDetailsBUCFacade != null)
		{

			final OrderBUC orderBUC = orderDetailsBUCFacade.getOrderBUCByOrderId(searchText.trim());

			if (orderBUC != null)
			{
				orderListbox.clearSelection();
				busList = new ArrayList<>();
				busList.add(orderBUC);
				orderListbox.setModel(new ListModelList(busList));
				orderListbox.setItemRenderer(new OrderListRenderer());
				editorVisible = false;
			}
			else
			{
				Messagebox.show("This OrderId Not Present");
				LOG.error("This OrderId Not Present");
			}

		}
		else
		{
			LOG.error("orderDetailsBUCFacade is null");
		}

	}

	/**
	 * This is method execute at time of any item select from listview and change the objects.
	 *
	 * @throws InterruptedException
	 */


	@Command
	@NotifyChange(
	{ "orderBUC", "orderlinebusList", "editorVisible", "p1SlaveName", "transTrackOLBus", "sShipStatus", "slaveinfoBtn",
			"p1SlaveIDList" })
	public void onListItemSelectOfOrder() throws InterruptedException
	{
		LOG.info("******************************OrderLinesList Item Selected............................");
		editorVisible = true;

		if (orderDetailsBUCFacade != null)
		{
			final OrderBUC selectedOrderbuc = (OrderBUC) orderListbox.getSelectedItem().getValue();
			orderBUC = orderDetailsBUCFacade.getOrderBUCByOrderId(selectedOrderbuc.getOrderId());
			orderlinebusList = (List<OrderLineBUC>) orderBUCFacade.getOrderLinesBUCByOrderId(selectedOrderbuc.getOrderId());
			LOG.info("**************************OrderLineBus list size" + orderlinebusList.size());
			p1SlaveIDList = new ListModelList<SlaveInfo>();
			p1SlaveName = "";
			sShipStatus = false;
			transTrackOLBus = new OrderLineBUC();
			slaveinfoBtn = false;
		}

		orderListbox.clearSelection();

	}

	/**
	 * updating order_line_bus
	 */

	@Command
	@NotifyChange(
	{ "transTrackOLBus", "shipStatusRepBusSR", "refundedInfoBUCList", "p1LogisticsIDList", "p2LogisticsIDList", "p1SlaveIDList",
			"p2SlaveIDList", "p1LogisticsName", "p2LogisticsName", "slaveinfoBtn", "p1SlaveName", "p2SlaveName", "p1LogisticsList",
			"sShipStatus", "p1slavesDropDownsStatus", "logisticsDropDownsStatus", "p1slavesVisibleStatus" })
	public void onOrderLineItemSelectOrderLines()
	{
		Object[] slaveInfoArray = new Object[2];
		sShipStatus = false;
		slaveinfoBtn = false;
		p1slavesDropDownsStatus = true;
		logisticsDropDownsStatus = true;
		p1slavesVisibleStatus = false;
		transTrackOLBus = selectedOLBuc;
		if (null != transTrackOLBus)
		{
			if (null != transTrackOLBus.getBUCStatusRecordsList() || !transTrackOLBus.getBUCStatusRecordsList().isEmpty())
			{
				shipStatusRepBusSR = transTrackOLBus.getBUCStatusRecordsList();
				shipStatusRepBusSR.sort(bUCStatusRecordsListComparator);
			}
		}
		else
		{

			LOG.error(" transTrackOLBus is null ");
		}
		refundedInfoBUCList = transTrackOLBus.getRefundedInfoRecordsList();

		if (logisticsFacade != null && !selectedOLBuc.getFulfillmentType().equals("SSHIP"))
		{
			sShipStatus = true;
			p1slavesVisibleStatus = true;

			LOG.info("selected OLBUC's primary logistics id *************:::" + selectedOLBuc.getPrimaryLogisticID());
			slaveInfoArray = craeteLogisticsListForDropdown((List<Logistics>) logisticsFacade.getAll(),
					selectedOLBuc.getPrimaryLogisticID());

			p1LogisticsIDList = (ListModelList<Logistics>) slaveInfoArray[0];

			p1LogisticsName = (String) slaveInfoArray[1];

			LOG.info("selected OLBUC's secondry logistics id *************:::" + selectedOLBuc.getSecondaryLogisticID());

			slaveInfoArray = craeteLogisticsListForDropdown((List<Logistics>) logisticsFacade.getAll(),
					selectedOLBuc.getSecondaryLogisticID());

			p2LogisticsIDList = (ListModelList<Logistics>) slaveInfoArray[0];

			p2LogisticsName = (String) slaveInfoArray[1];

			if (transTrackOLBus.getTransactionLineStatus().equals("ORDPNASG")
					|| transTrackOLBus.getTransactionLineStatus().equals("ORDREJEC")
					|| transTrackOLBus.getTransactionLineStatus().equals("PYMTSCSS"))
			{
				LOG.info("Order Status" + transTrackOLBus.getTransactionLineStatus());
				p1slavesDropDownsStatus = false;
				logisticsDropDownsStatus = false;
				slaveinfoBtn = true;
			}

		}
		if (selectedOLBuc.getFulfillmentType().equals("SSHIP"))
		{
			p1slavesVisibleStatus = true;
			if (transTrackOLBus.getTransactionLineStatus().equals("ORDPNASG")
					|| transTrackOLBus.getTransactionLineStatus().equals("ORDREJEC")
					|| transTrackOLBus.getTransactionLineStatus().equals("PYMTSCSS"))
			{
				slaveinfoBtn = true;
				p1slavesDropDownsStatus = false;
			}
		}

		if (stockRoomLocationsFacade != null)
		{
			LOG.info("selected OLBUC's primary slave id *************:::" + selectedOLBuc.getPrimarySlaveID());
			//Added to get all stockroom locations by seller
			slaveInfoArray = craeteSlaveInfoListForDropdown(
					stockRoomLocationsFacade.getAllStockRoomLocationsBySeller(selectedOLBuc.getSellerID()).getSlaveInfo(),
					selectedOLBuc.getPrimarySlaveID());

			p1SlaveIDList = (ListModelList<SlaveInfo>) slaveInfoArray[0];

			p1SlaveName = (String) slaveInfoArray[1];


			/**
			 * The underline code is only for click and collect. which not implemented in release 1. when ever required
			 * uncomment the code and get the value
			 */

			//			slaveInfoArray = craeteSlaveInfoListForDropdown(stockRoomLocationsFacade.getAllStockRoomLocations().getSlaveInfo(),
			//					selectedOLBuc.getSecondarySlaveID());
			//
			//			p2SlaveIDList = (ListModelList<SlaveInfo>) slaveInfoArray[0];
			//
			//			p2SlaveName = (String) slaveInfoArray[1];

		}
		orderLinesListbox.clearSelection();

	}


	/**
	 * @param slavesInfoDropDownsStatus
	 *           the slavesInfoDropDownsStatus to set
	 */
	public void setP1slavesDropDownsStatus(final boolean p1slavesDropDownsStatus)
	{
		this.p1slavesDropDownsStatus = p1slavesDropDownsStatus;
	}

	@Command
	@NotifyChange(
	{ "p1SlaveName" })
	public void onSelectp1SlaveIDList()
	{
		final SlaveInfo info = (SlaveInfo) p1SlaveIDListbox.getSelectedItem().getValue();
		p1SlaveName = info.getName();

	}

	@Command
	@NotifyChange(
	{ "p2SlaveName" })
	public void onSelectp2SlaveIDList()
	{
		final SlaveInfo info = (SlaveInfo) p2SlaveIDListbox.getSelectedItem().getValue();
		p2SlaveName = info.getName();
	}

	@Command
	@NotifyChange(
	{ "p1LogisticsName" })
	public void onSelectp1LogisticsIDList()
	{
		final Logistics logistics = (Logistics) p1LogisticsIDListbox.getSelectedItem().getValue();
		p1LogisticsName = logistics.getLogisticname();
	}

	@Command
	@NotifyChange(
	{ "p2LogisticsName" })
	public void onSelectp2LogisticsIDList()
	{
		final Logistics logistics = (Logistics) p2LogisticsIDListbox.getSelectedItem().getValue();
		p2LogisticsName = logistics.getLogisticname();
	}



	@Command
	@NotifyChange(
	{ "slaveinfoBtn", "logisticsDropDownsStatus", "p1slavesDropDownsStatus" })
	public void slaveinfoBtnAction()
	{
		OrderLineBUC orderLineBuc;
		if (orderBUCFacade == null)
		{
			LOG.info("Not enjected object for orderBUCFacade");
			return;
		}

		if (!p1slavesDropDownsStatus && logisticsDropDownsStatus)
		{
			if (p1SlaveIDListbox.getSelectedItem() == null)
			{
				Messagebox.show("Please choose any one option from P1SlaveID", "Alert", Messagebox.OK, Messagebox.ERROR);
				return;
			}
			final SlaveInfo p1slave = (SlaveInfo) p1SlaveIDListbox.getSelectedItem().getValue();
			orderLineBuc = new OrderLineBUC();
			orderLineBuc.setPrimarySlaveID(p1slave.getSlaveid());
			orderLineBuc.setTransactionId(transTrackOLBus.getTransactionId());
			final OrderLineBUC orderLineBucRtrn = orderBUCFacade.allocateOrderLine(orderLineBuc);
			if (!orderLineBucRtrn.getPrimarySlaveID().equals(orderLineBuc.getPrimarySlaveID())
					|| !orderLineBucRtrn.getTransactionId().equals(orderLineBuc.getTransactionId()))
			{

				LOG.info("while updating p1slave in orderline, return data::: " + orderLineBucRtrn);
				LOG.info("sent ordrrLineBUC  *****" + orderLineBuc);
				Messagebox.show("Updating failed");

			}
			else
			{

				Messagebox.show("Successfully updated PrimarySlaveID of orderLine");
			}
			p1slavesDropDownsStatus = true;
			slaveinfoBtn = false;
			return;
		}

		if (!logisticsDropDownsStatus)
		{
			if (p1LogisticsIDListbox.getSelectedItem() == null || p1SlaveIDListbox.getSelectedItem() == null)
			{
				LOG.error("null p1LogisticsIDListbox or  p1SlaveIDListbox");
				Messagebox.show("Please choose any one option from every P1SlaveID and P1LogisticsID", "Alert", Messagebox.OK,
						Messagebox.ERROR);
				return;
			}
			final SlaveInfo p1slave = (SlaveInfo) p1SlaveIDListbox.getSelectedItem().getValue();
			final Logistics p1logistics = (Logistics) p1LogisticsIDListbox.getSelectedItem().getValue();
			orderLineBuc = new OrderLineBUC();
			orderLineBuc.setPrimaryLogisticID(p1logistics.getLogisticsid());

			if (p2LogisticsIDListbox.getSelectedItem() != null)
			{
				final Logistics p2logistics = (Logistics) p2LogisticsIDListbox.getSelectedItem().getValue();
				orderLineBuc.setSecondaryLogisticID(p2logistics.getLogisticsid());
			}
			else
			{
				orderLineBuc.setSecondaryLogisticID(null);
				LOG.info("*********************selected p2LogisticsId {}", "null");
			}
			orderLineBuc.setPrimarySlaveID(p1slave.getSlaveid());
			orderLineBuc.setTransactionId(transTrackOLBus.getTransactionId());
			orderLineBuc.setTransactionLineStatus(transTrackOLBus.getTransactionLineStatus());
			LOG.info("************************save buttom sent OLBUC is {}", orderLineBuc);
			final OrderLineBUC orderLineBucRtrn = orderBUCFacade.allocateOrderLine(orderLineBuc);
			if (!orderLineBucRtrn.getPrimaryLogisticID().equals(orderLineBuc.getPrimaryLogisticID())
					|| !orderLineBucRtrn.getSecondaryLogisticID().equals(orderLineBuc.getSecondaryLogisticID())
					|| !orderLineBucRtrn.getPrimarySlaveID().equals(orderLineBuc.getPrimarySlaveID())
					|| !orderLineBucRtrn.getTransactionLineStatus().equals("ORDALLOC")
					|| !orderLineBucRtrn.getTransactionId().equals(orderLineBuc.getTransactionId()))
			{
				LOG.error("while updating p1slave,setPrimarySlaveID,and SecondaryLogisticID in orderline, return data :::"
						+ orderLineBucRtrn);
				Messagebox.show("Updating failed");
			}
			else
			{

				Messagebox.show("Successfully updated");
			}
			logisticsDropDownsStatus = true;
			p1slavesDropDownsStatus = true;
			slaveinfoBtn = false;
		}
		else
		{

			LOG.error("***************logisticsDropDownsStatus is disable*****************");
		}
	}



	private Object[] craeteLogisticsListForDropdown(final List<Logistics> source, final String orderLBLogisticsId)
	{
		final Object[] slaveInfoArray = new Object[2];

		ListModelList<Logistics> listModelList = new ListModelList<Logistics>();
		if (source != null)
		{
			if (!source.isEmpty())
			{
				LOG.debug("logitics dropdown list size" + source.size());
				final Iterator<Logistics> slaveIdItr = source.iterator();
				int index = 0;
				int temp = -1;
				String str = "";
				while (slaveIdItr.hasNext())
				{
					final Logistics info = slaveIdItr.next();
					if (info.getActive())
					{
						if (info.getLogisticname().equals(orderLBLogisticsId) || info.getLogisticsid().equals(orderLBLogisticsId))
						{
							temp = index;
							str = info.getLogisticname();
							LOG.info("intially equal logisticsids######################" + info.getLogisticname() + "=="
									+ orderLBLogisticsId);
						}
						listModelList.add(info);
						index++;
					}
					else
					{
						LOG.info("Not active records for logistics dropdown ::::" + info);
					}

				}
				if (temp >= 0)
				{
					listModelList.addToSelection(listModelList.get(temp));
				}
				{
					LOG.info("Not equal OLBUC's logisticsID with any logisticsID of Logistics");
				}
				slaveInfoArray[0] = listModelList;
				slaveInfoArray[1] = str;
			}
			else
			{
				LOG.equals("NO slaves avaliable for logistcs,list size " + source.size());
			}
		}
		else
		{
			LOG.error("***********************Logistics list is null");
		}


		return slaveInfoArray;

	}

	private Object[] craeteSlaveInfoListForDropdown(final List<SlaveInfo> source, final String orderLBSlaveId)
	{
		final Object[] slaveInfoArray = new Object[2];

		ListModelList<SlaveInfo> listModelList = new ListModelList<SlaveInfo>();
		if (source != null)
		{
			if (!source.isEmpty())
			{
				final Iterator<SlaveInfo> slaveIdItr = source.iterator();
				LOG.info("p1Slave dropdown list size" + source.size());
				int index = 0;
				int temp = -1;
				String str = "";
				while (slaveIdItr.hasNext())
				{
					final SlaveInfo info = slaveIdItr.next();

					if (info.getSlaveid().equals(orderLBSlaveId))
					{
						temp = index;
						str = info.getName();
						LOG.info("intially equal slaveids######################" + info.getSlaveid() + "==" + orderLBSlaveId);
					}
					listModelList.add(info);
					index++;

				}
				if (temp >= 0)
				{
					listModelList.addToSelection(listModelList.get(temp));
				}
				else
				{

					LOG.info("Not equal OLBUC's primary slaveid with any slaveid of stockroomlocation");
				}
				slaveInfoArray[0] = listModelList;
				slaveInfoArray[1] = str;
			}
			else
			{
				LOG.equals("NO slaves avaliable for P1Slaves ,list size " + source.size());
			}
		}
		else
		{
			LOG.error("***********************SlaveInfo list is null");

		}

		return slaveInfoArray;

	}

	/**
	 * @return the searchText
	 */
	public String getSearchText()
	{
		return searchText;
	}

	/**
	 * @param searchText
	 *           the searchText to set
	 */
	public void setSearchText(final String searchText)
	{
		this.searchText = searchText;
	}


	/**
	 * @return the orderlinebusList
	 */
	public List<OrderLineBUC> getOrderlinebusList()
	{
		return orderlinebusList;
	}

	/**
	 * @param orderlinebusList
	 *           the orderlinebusList to set
	 */
	public void setOrderlinebusList(final List<OrderLineBUC> orderlinebusList)
	{
		this.orderlinebusList = orderlinebusList;
	}

	/**
	 * @return the selectedOrderBUC
	 */
	public OrderBUC getselectedOrderBUC()
	{
		return selectedOrderBUC;
	}

	/**
	 * @param selectedOrderBUC
	 *           the selectedOrderBUC to set
	 */
	public void setselectedOrderBUC(final OrderBUC selectedOrderBUC)
	{
		this.selectedOrderBUC = selectedOrderBUC;
	}


	/**
	 * @return the p1LogisticsIDList
	 */
	public ListModelList<Logistics> getP1LogisticsIDList()
	{
		return p1LogisticsIDList;
	}


	/**
	 * @param p1LogisticsIDList
	 *           the p1LogisticsIDList to set
	 */
	public void setP1LogisticsIDList(ListModelList<Logistics> p1LogisticsIDList)
	{
		this.p1LogisticsIDList = p1LogisticsIDList;
	}


	/**
	 * @return the p2LogisticsIDList
	 */
	public ListModelList<Logistics> getP2LogisticsIDList()
	{
		return p2LogisticsIDList;
	}


	/**
	 * @param p2LogisticsIDList
	 *           the p2LogisticsIDList to set
	 */
	public void setP2LogisticsIDList(ListModelList<Logistics> p2LogisticsIDList)
	{
		this.p2LogisticsIDList = p2LogisticsIDList;
	}


	/**
	 * @return the p1SlaveIDList
	 */
	public ListModelList<SlaveInfo> getP1SlaveIDList()
	{
		return p1SlaveIDList;
	}

	/**
	 * @param p1SlaveIDList
	 *           the p1SlaveIDList to set
	 */
	public void setP1SlaveIDList(ListModelList<SlaveInfo> p1SlaveIDList)
	{
		this.p1SlaveIDList = p1SlaveIDList;
	}

	/**
	 * @return the p2SlaveIDList
	 */
	public ListModelList<SlaveInfo> getP2SlaveIDList()
	{
		return p2SlaveIDList;
	}

	/**
	 * @param p2SlaveIDList
	 *           the p2SlaveIDList to set
	 */
	public void setP2SlaveIDList(ListModelList<SlaveInfo> p2SlaveIDList)
	{
		this.p2SlaveIDList = p2SlaveIDList;
	}

	/**
	 * @return the shipStatusRepBusSR
	 */
	public List<BUCStatusRecords> getshipStatusRepBusSR()
	{
		return shipStatusRepBusSR;
	}

	/**
	 * @param shipStatusRepBusSR
	 *           the shipStatusRepBusSR to set
	 */
	public void setshipStatusRepBusSR(final List<BUCStatusRecords> shipStatusRepBusSR)
	{
		this.shipStatusRepBusSR = shipStatusRepBusSR;
	}

	/**
	 * @return the editorVisible
	 */
	public boolean iseditorVisible()
	{
		return editorVisible;
	}


	/**
	 * @param editorVisible
	 *           the editorVisible to set
	 */
	public void seteditorVisible(final boolean editorVisible)
	{
		this.editorVisible = editorVisible;
	}




	/**
	 * @return the orderBUC
	 */
	public OrderBUC getOrderBUC()
	{
		return orderBUC;
	}


	/**
	 * @param orderBUC
	 *           the orderBUC to set
	 */
	public void setOrderBUC(final OrderBUC orderBUC)
	{
		this.orderBUC = orderBUC;
	}

	/**
	 * @return the busList
	 */
	public List<OrderBUC> getbusList()
	{
		return busList;
	}


	/**
	 * @param busList
	 *           the busList to set
	 */
	public void setbusList(final List<OrderBUC> busList)
	{
		this.busList = busList;
	}

	/**
	 * @return the slaveinfoBtn
	 */
	public boolean isslaveinfoBtn()
	{
		return slaveinfoBtn;
	}


	/**
	 * @param slaveinfoBtn
	 *           the slaveinfoBtn to set
	 */
	public void setslaveinfoBtn(final boolean slaveinfoBtn)
	{
		this.slaveinfoBtn = slaveinfoBtn;
	}

	/**
	 * @return the p1SlaveName
	 */
	public String getP1SlaveName()
	{
		return p1SlaveName;
	}


	/**
	 * @param p1SlaveName
	 *           the p1SlaveName to set
	 */
	public void setP1SlaveName(final String p1SlaveName)
	{
		this.p1SlaveName = p1SlaveName;
	}


	/**
	 * @return the p2SlaveName
	 */
	public String getP2SlaveName()
	{
		return p2SlaveName;
	}


	/**
	 * @param p2SlaveName
	 *           the p2SlaveName to set
	 */
	public void setP2SlaveName(final String p2SlaveName)
	{
		this.p2SlaveName = p2SlaveName;
	}


	/**
	 * @return the p1LogisticsName
	 */
	public String getP1LogisticsName()
	{
		return p1LogisticsName;
	}


	/**
	 * @param p1LogisticsName
	 *           the p1LogisticsName to set
	 */
	public void setP1LogisticsName(final String p1LogisticsName)
	{
		this.p1LogisticsName = p1LogisticsName;
	}


	/**
	 * @return the p2LogisticsName
	 */
	public String getP2LogisticsName()
	{
		return p2LogisticsName;
	}


	/**
	 * @param p2LogisticsName
	 *           the p2LogisticsName to set
	 */
	public void setP2LogisticsName(final String p2LogisticsName)
	{
		this.p2LogisticsName = p2LogisticsName;
	}

	/**
	 * @return the transTrackOLBus
	 */
	public OrderLineBUC gettransTrackOLBus()
	{
		return transTrackOLBus;
	}

	/**
	 * @param transTrackOLBus
	 *           the transTrackOLBus to set
	 */
	public void settransTrackOLBus(final OrderLineBUC transTrackOLBus)
	{
		this.transTrackOLBus = transTrackOLBus;
	}

	/**
	 * @return the selectedOLBuc
	 */
	public OrderLineBUC getselectedOLBuc()
	{
		return selectedOLBuc;
	}

	/**
	 * @param selectedOLBuc
	 *           the selectedOLBuc to set
	 */
	public void setselectedOLBuc(final OrderLineBUC selectedOLBuc)
	{
		this.selectedOLBuc = selectedOLBuc;
	}

	/**
	 * @return the refundedInfoBUCList
	 */
	public List<RefundedInfoBUC> getRefundedInfoBUCList()
	{
		return refundedInfoBUCList;
	}

	/**
	 * @param refundedInfoBUCList
	 *           the refundedInfoBUCList to set
	 */
	public void setRefundedInfoBUCList(final List<RefundedInfoBUC> refundedInfoBUCList)
	{
		this.refundedInfoBUCList = refundedInfoBUCList;
	}


	/**
	 * @return the sShipStatus
	 */
	public boolean issShipStatus()
	{
		return sShipStatus;
	}

	/**
	 * @param sShipStatus
	 *           the sShipStatus to set
	 */
	public void setsShipStatus(final boolean sShipStatus)
	{
		this.sShipStatus = sShipStatus;
	}

	/**
	 * @return the slavesInfoDropDownsStatus
	 */
	public boolean isP1slavesDropDownsStatus()
	{
		return p1slavesDropDownsStatus;
	}

	/**
	 * @return the selectedP1LogisticsID
	 */
	public Logistics getSelectedP1LogisticsID()
	{
		return selectedP1LogisticsID;
	}



	/**
	 * @param selectedP1LogisticsID
	 *           the selectedP1LogisticsID to set
	 */
	public void setSelectedP1LogisticsID(final Logistics selectedP1LogisticsID)
	{
		this.selectedP1LogisticsID = selectedP1LogisticsID;
	}



	/**
	 * @return the selectedP2LogisticsID
	 */
	public Logistics getSelectedP2LogisticsID()
	{
		return selectedP2LogisticsID;
	}



	/**
	 * @param selectedP2LogisticsID
	 *           the selectedP2LogisticsID to set
	 */
	public void setSelectedP2LogisticsID(final Logistics selectedP2LogisticsID)
	{
		this.selectedP2LogisticsID = selectedP2LogisticsID;
	}



	/**
	 * @return the selectedP1SlaveID
	 */
	public SlaveInfo getSelectedP1SlaveID()
	{
		return selectedP1SlaveID;
	}


	/**
	 * @param selectedP1SlaveID
	 *           the selectedP1SlaveID to set
	 */
	public void setSelectedP1SlaveID(final SlaveInfo selectedP1SlaveID)
	{
		this.selectedP1SlaveID = selectedP1SlaveID;
	}

	/**
	 * @return the logisticsDropDownsStatus
	 */
	public boolean isLogisticsDropDownsStatus()
	{
		return logisticsDropDownsStatus;
	}



	/**
	 * @param logisticsDropDownsStatus
	 *           the logisticsDropDownsStatus to set
	 */
	public void setLogisticsDropDownsStatus(final boolean logisticsDropDownsStatus)
	{
		this.logisticsDropDownsStatus = logisticsDropDownsStatus;
	}



	/**
	 * @return the p1slavesVisibleStatus
	 */
	public boolean isP1slavesVisibleStatus()
	{
		return p1slavesVisibleStatus;
	}



	/**
	 * @param p1slavesVisibleStatus
	 *           the p1slavesVisibleStatus to set
	 */
	public void setP1slavesVisibleStatus(final boolean p1slavesVisibleStatus)
	{
		this.p1slavesVisibleStatus = p1slavesVisibleStatus;
	}

	public static Comparator<BUCStatusRecords> bUCStatusRecordsListComparator = new Comparator<BUCStatusRecords>()
	{
		public int compare(final BUCStatusRecords arg0, final BUCStatusRecords arg1)
		{

			if (arg1 == null || arg0 == null || arg1.getDate() == null || arg0.getDate() == null)
			{
				return -1;
			}
			return arg0.getDate().compareTo(arg1.getDate());


		}
	};

	public static Comparator<OrderBUC> bUCOrderListComparator = new Comparator<OrderBUC>()
	{
		public int compare(final OrderBUC arg0, final OrderBUC arg1)
		{

			if (arg1 == null || arg0 == null || arg1.getCreationTime() == null || arg0.getCreationTime() == null)
			{
				return -1;
			}
			return arg1.getCreationTime().compareTo(arg0.getCreationTime());


		}
	};


}
