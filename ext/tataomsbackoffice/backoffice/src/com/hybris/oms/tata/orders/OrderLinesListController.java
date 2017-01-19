/**
 *
 */
package com.hybris.oms.tata.orders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.logistics.LogisticsFacade;
import com.hybris.oms.api.stockroomlocations.StockRoomLocationsFacade;
import com.hybris.oms.buc.order.OrderBUCFacade;
import com.hybris.oms.buc.order.OrderDetailsBUCFacade;
import com.hybris.oms.domain.logistics.dto.Logistics;
import com.hybris.oms.domain.orderbuc.dto.BUCStatusRecords;
import com.hybris.oms.domain.orderbuc.dto.OrderBUC;
import com.hybris.oms.domain.orderbuc.dto.OrderLineBUC;
import com.hybris.oms.domain.orderbuc.dto.RefundedInfoBUC;
import com.hybris.oms.domain.sp.dto.SellerSlave;
import com.hybris.oms.domain.sp.dto.SlaveInfo;



/**
 * @author nagarjuna
 *
 */
public class OrderLinesListController extends DefaultWidgetController
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

	private List<RefundedInfoBUC> refundedInfoBUCList;


	private OrderLineBUC transTrackOLBus;
	private OrderBUC orderBUC = new OrderBUC();

	@Wire("#orderLinePanel")
	private Panel orderLinePanel;

	@Wire("#orderListbox")
	private Listbox orderListbox;
	@Wire("#shippingAddGrid")
	private Grid shippingAddGrid;
	@Wire("#customerIDLable")
	private Label customerIDLable;
	@Wire("#emailIDLable")
	private Label emailIDLable;

	@Wire("#orderLinesListbox")
	private Listbox orderLinesListbox;
	@Wire("#transactionInfoGrid")
	private Grid transactionInfoGrid;



	@Wire("#orderSearchTbox")
	private Textbox orderSearchTbox;


	@Wire("#logisticsVlayout")
	private Vlayout logisticsVlayout;
	@Wire("#p1LogisticsNameLabel")
	private Label p1LogisticsNameLabel;
	@Wire("#p1LogisticsIDListbox")
	private Listbox p1LogisticsIDListbox;



	@Wire("#slavesVlayout")
	private Vlayout slavesVlayout;
	@Wire("#p1slavesHlayout")
	private Hlayout p1slavesHlayout;
	@Wire("#p2slavesNameHlayout")
	private Hlayout p2slavesNameHlayout;
	@Wire("#p1SlaveIDListbox")
	private Listbox p1SlaveIDListbox;
	@Wire("#p2SlaveIDListbox")
	private Listbox p2SlaveIDListbox;
	@Wire("#p1SlaveNameLable")
	private Label p1SlaveNameLable;
	@Wire("#p2SlaveNameLable")
	private Label p2SlaveNameLable;
	@Wire("#p2slavesHlayout")
	private Hlayout p2slavesHlayout;
	@Wire("#orderSubmitBtn")
	private Button orderSubmitBtn;


	@Wire("#shipmentStatusListbox")
	private Listbox shipmentStatusListbox;
	@Wire("#refundedInfoListbox")
	private Listbox refundedInfoListbox;


	@Override
	public void initialize(final Component comp)
	{
		super.initialize(comp);
		LOG.info("******************************OrderLinesZul Intializing............................");
		orderLinePanel.setVisible(false);
		if (orderBUCFacade != null)
		{
			final List<OrderBUC> busList = (List<OrderBUC>) orderBUCFacade.getAll();
			busList.sort(bUCOrderListComparator);
			orderListbox.setModel(new ListModelList(busList));
		}
	}


	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) final Component view)
	{
		Selectors.wireComponents(view, this, false);
	}

	public void searchEnter()
	{
		commonLogicForSearch();
	}

	public void searchClick()
	{
		commonLogicForSearch();
	}


	public void commonLogicForSearch()
	{
		LOG.debug("******************************OrderLinesZul Search Button Clicked............................");

		if (null == orderSearchTbox || "".equals(orderSearchTbox.getValue()))
		{
			Messagebox.show("Please Enter OrderId");
			return;
		}

		if (orderDetailsBUCFacade != null)
		{

			final OrderBUC orderBUC = orderDetailsBUCFacade.getOrderBUCByOrderId(orderSearchTbox.getValue().trim());

			if (orderBUC != null)
			{
				orderListbox.clearSelection();
				final List<OrderBUC> busList = new ArrayList<>();
				busList.add(orderBUC);
				orderListbox.setModel(new ListModelList(busList));
				orderLinePanel.setVisible(false);
			}
			else
			{
				Messagebox.show("This OrderId Not Present");
			}

		}
		else
		{
			LOG.error("orderDetailsBUCFacade is null");
		}
		orderSearchTbox.setValue("");
	}

	/**
	 * This is method execute at time of any item select from listview and change the objects.
	 *
	 * @throws InterruptedException
	 */
	public void onListItemSelectOfOrder()
	{
		LOG.info("****OrderLinesList Item Selected....");
		orderLinePanel.setVisible(true);
		if (orderDetailsBUCFacade != null)
		{
			orderBUC = (OrderBUC) orderListbox.getSelectedItem().getValue();
			papulateShippingAddress(orderBUC);
			final List<OrderLineBUC> orderlinebusList = (List<OrderLineBUC>) orderBUCFacade.getOrderLinesBUCByOrderId(orderBUC
					.getOrderId());
			orderLinesListbox.setModel(new ListModelList<OrderLineBUC>(orderlinebusList));
			LOG.info("**************************OrderLineBus list size" + orderlinebusList.size());
			p1SlaveIDListbox.setModel(new ListModelList<SlaveInfo>());
			p2SlaveIDListbox.setModel(new ListModelList<SlaveInfo>());

			p1SlaveNameLable.setValue("");
			p2SlaveNameLable.setValue("");

			logisticsVlayout.setVisible(false);

			slavesVlayout.setVisible(false);
			transTrackOLBus = new OrderLineBUC();

			orderSubmitBtn.setVisible(false);

			shipmentStatusListbox.setModel(new ListModelList<>());

			refundedInfoListbox.setModel(new ListModelList<>());
		}

		orderListbox.clearSelection();

	}

	private void papulateShippingAddress(final OrderBUC selectedOrder)
	{
		final ListModelList<String> shippingAddressList = new ListModelList<>();
		shippingAddressList.add("First Name : " + selectedOrder.getShipment_firstName());
		shippingAddressList.add("Last Name : " + selectedOrder.getShipment_lastName());
		shippingAddressList.add("Phone No : " + selectedOrder.getShipment_phoneNo());
		shippingAddressList.add("AddressLine 1 : " + selectedOrder.getShipment_addressLine1());
		shippingAddressList.add("AddressLine 2 : " + selectedOrder.getShipment_addressLine2());
		shippingAddressList.add("AddressLine 3 : " + selectedOrder.getShipment_addressLine3());
		shippingAddressList.add("Landmark : " + selectedOrder.getShipmentLandMark());
		shippingAddressList.add("City : " + selectedOrder.getShipment_city());
		shippingAddressList.add("State : " + selectedOrder.getShipment_state());
		shippingAddressList.add("Country : " + selectedOrder.getShipment_country());
		shippingAddressList.add("Pincode : " + selectedOrder.getShipment_pincode());

		shippingAddressList.add("Payment Cost : " + selectedOrder.getPaymentCost());
		shippingAddressList.add("Payment Mode : " + selectedOrder.getPaymentMode());
		shippingAddressList.add("Payment Info : " + selectedOrder.getPaymentInfo());
		shippingAddressList.add("Payment Date : " + selectedOrder.getPaymentDate());

		shippingAddGrid.setModel(shippingAddressList);
	}


	private void papulateTransactionInfo(final OrderLineBUC selectedOrder)
	{
		final ListModelList<String> shippingAddressList = new ListModelList<>();
		shippingAddressList.add("SellerOrderNo : " + selectedOrder.getSellerOrderNo());
		shippingAddressList.add("TransactionId : " + selectedOrder.getTransactionId());
		shippingAddressList.add("Original Transaction ID : " + selectedOrder.getOriginalTransactionId());
		shippingAddressList.add("AWB Number : " + selectedOrder.getAwbNum());
		shippingAddressList.add("AWB Sencondary Status : " + selectedOrder.getAwbSecondaryStatus());
		shippingAddressList.add("Return AWB Number : " + selectedOrder.getReturnAWBNum());
		shippingAddressList.add("Seller ID : " + selectedOrder.getSellerID());
		shippingAddressList.add("Serial Num : " + selectedOrder.getSerialNum());
		shippingAddressList.add("IMEINo1 : " + selectedOrder.getImeino1());
		shippingAddressList.add("IMEINo2 : " + selectedOrder.getImeino2());
		shippingAddressList.add("ISBNNo : " + selectedOrder.getIsbno1());

		shippingAddressList.add("Return Request ID : " + selectedOrder.getReturnRequestID());
		shippingAddressList.add("Return Reason Code : " + selectedOrder.getReturnReasonCode());
		shippingAddressList.add("Cancel request ID : " + selectedOrder.getCancelRequestID());
		shippingAddressList.add("Return/Cancel Remarks : " + selectedOrder.getReturnCancelRemarks());
		shippingAddressList.add("Return Logistic Provider Name : " + selectedOrder.getReturnLogisticProviderName());
		transactionInfoGrid.setModel(shippingAddressList);
	}

	/**
	 * updating order_line_bus
	 */
	public void onOrderLineItemSelectOrderLines()
	{
		//transTrackOLBus = selectedOLBuc;
		transTrackOLBus = orderLinesListbox.getSelectedItem().getValue();
		if (null != transTrackOLBus)
		{
			customerIDLable.setValue(transTrackOLBus.getCustomerID());
			emailIDLable.setValue(transTrackOLBus.getEmailId());
			papulateTransactionInfo(transTrackOLBus);
			Object[] slaveInfoArray = new Object[2];

			logisticsVlayout.setVisible(false);

			orderSubmitBtn.setVisible(false);

			p1SlaveIDListbox.setDisabled(true);
			p1SlaveIDListbox.setRows(1);


			p2slavesNameHlayout.setVisible(false);
			p2SlaveIDListbox.setDisabled(true);
			p2slavesHlayout.setVisible(false);
			p2SlaveIDListbox.setRows(1);

			p1LogisticsIDListbox.setDisabled(true);
			p1LogisticsIDListbox.setRows(1);

			slavesVlayout.setVisible(true);
			List<BUCStatusRecords> shipStatusRepBusSR = null;

			if (CollectionUtils.isNotEmpty(transTrackOLBus.getBUCStatusRecordsList()))
			{
				shipStatusRepBusSR = transTrackOLBus.getBUCStatusRecordsList();
				shipStatusRepBusSR.sort(bUCStatusRecordsListComparator);
				shipmentStatusListbox.setModel(new ListModelList<>(shipStatusRepBusSR));
			}

			refundedInfoBUCList = transTrackOLBus.getRefundedInfoRecordsList();
			refundedInfoListbox.setModel(new ListModelList<>(refundedInfoBUCList));

			//for TSHIP
			if (logisticsFacade != null && transTrackOLBus.getFulfillmentType().equals("TSHIP"))
			{
				logisticsVlayout.setVisible(true);
				//TSHIP order not allocated
				if ("ORDPNASG".equals(transTrackOLBus.getTransactionLineStatus())
						|| "ORDREJEC".equals(transTrackOLBus.getTransactionLineStatus())
						|| "PYMTSCSS".equals(transTrackOLBus.getTransactionLineStatus()))
				{
					LOG.info("Order Status" + transTrackOLBus.getTransactionLineStatus());
					p1LogisticsIDListbox.setModel(new ListModelList<Logistics>(logisticsFacade.getAll()));
					p1LogisticsNameLabel.setValue("");

					p1SlaveIDListbox.setDisabled(false);

					p1LogisticsIDListbox.setDisabled(false);
					p1LogisticsIDListbox.setRows(3);
					orderSubmitBtn.setVisible(true);
					//Only CNC orders
					if (StringUtils.isNotEmpty(transTrackOLBus.getStoreID()) && "CNC".equals(transTrackOLBus.getDeliveryType()))
					{
						p2slavesHlayout.setVisible(true);
						//p2SlaveIDListbox.setVisible(true);
						p2SlaveIDListbox.setRows(3);
						p1SlaveIDListbox.setRows(1);
						p2SlaveIDListbox.setDisabled(false);
						final SlaveInfo allocatedSecSlaveInfo = stockRoomLocationsFacade
								.getStockRoomLocationByLocationId(transTrackOLBus.getStoreID());
						p1SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(Arrays.asList(allocatedSecSlaveInfo)));
						p1SlaveNameLable.setValue(allocatedSecSlaveInfo.getName());

						final SellerSlave sellerSlaveInfo = stockRoomLocationsFacade.getAllStockRoomLocationsBySeller(transTrackOLBus
								.getSellerID());
						p2SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(sellerSlaveInfo.getSlaveInfo()));
						p2SlaveNameLable.setValue("");
						p2slavesNameHlayout.setVisible(true);
					}
					else
					{
						p1SlaveIDListbox.setRows(3);
						final SellerSlave sellerSlaveInfo = stockRoomLocationsFacade.getAllStockRoomLocationsBySeller(transTrackOLBus
								.getSellerID());
						p1SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(sellerSlaveInfo.getSlaveInfo()));
						p1SlaveNameLable.setValue("");
					}

				}
				else
				{
					//TSHIP order allocated
					slaveInfoArray = createLogisticsListForDropdown((List<Logistics>) logisticsFacade.getAll(),
							transTrackOLBus.getPrimaryLogisticID());
					p1LogisticsIDListbox.setModel((ListModelList<Logistics>) slaveInfoArray[0]);
					p1LogisticsNameLabel.setValue((String) slaveInfoArray[1]);

					final SlaveInfo allocatedp1SlaveInfo = stockRoomLocationsFacade.getStockRoomLocationByLocationId(transTrackOLBus
							.getPrimarySlaveID());
					p1SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(Arrays.asList(allocatedp1SlaveInfo)));
					p1SlaveNameLable.setValue(allocatedp1SlaveInfo.getName());

					if (StringUtils.isNotEmpty(transTrackOLBus.getStoreID()) && "CNC".equals(transTrackOLBus.getDeliveryType())
							&& StringUtils.isNotEmpty(transTrackOLBus.getSecondarySlaveID()))
					{
						p2slavesHlayout.setVisible(true);
						final SlaveInfo allocatedp2SlaveInfo = stockRoomLocationsFacade
								.getStockRoomLocationByLocationId(transTrackOLBus.getSecondarySlaveID());
						p2SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(Arrays.asList(allocatedp2SlaveInfo)));
						p2SlaveNameLable.setValue(allocatedp2SlaveInfo.getName());
						p2slavesNameHlayout.setVisible(false);
					}
				}

			}
			else if (transTrackOLBus.getFulfillmentType().equals("SSHIP"))
			{
				if ("ORDPNASG".equals(transTrackOLBus.getTransactionLineStatus())
						|| "ORDREJEC".equals(transTrackOLBus.getTransactionLineStatus())
						|| "PYMTSCSS".equals(transTrackOLBus.getTransactionLineStatus()))
				{
					LOG.info("Order Status" + transTrackOLBus.getTransactionLineStatus());

					p1SlaveIDListbox.setDisabled(false);

					p1LogisticsIDListbox.setDisabled(false);
					p1LogisticsIDListbox.setRows(3);
					orderSubmitBtn.setVisible(true);

					if (StringUtils.isNotEmpty(transTrackOLBus.getStoreID()) && "CNC".equals(transTrackOLBus.getDeliveryType()))
					{
						p2slavesHlayout.setVisible(true);
						p2SlaveIDListbox.setRows(3);
						p1SlaveIDListbox.setRows(1);
						p2SlaveIDListbox.setDisabled(false);
						final SlaveInfo allocatedSecSlaveInfo = stockRoomLocationsFacade
								.getStockRoomLocationByLocationId(transTrackOLBus.getStoreID());
						p1SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(Arrays.asList(allocatedSecSlaveInfo)));
						p1SlaveNameLable.setValue(allocatedSecSlaveInfo.getName());

						final SellerSlave sellerSlaveInfo = stockRoomLocationsFacade.getAllStockRoomLocationsBySeller(transTrackOLBus
								.getSellerID());
						p2SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(sellerSlaveInfo.getSlaveInfo()));
						p2SlaveNameLable.setValue("");
						p2slavesNameHlayout.setVisible(true);
					}
					else
					{
						p1SlaveIDListbox.setRows(3);
						final SellerSlave sellerSlaveInfo = stockRoomLocationsFacade.getAllStockRoomLocationsBySeller(transTrackOLBus
								.getSellerID());
						p1SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(sellerSlaveInfo.getSlaveInfo()));
						p1SlaveNameLable.setValue("");
					}
				}
				else
				{
					final SlaveInfo allocatedp1SlaveInfo = stockRoomLocationsFacade.getStockRoomLocationByLocationId(transTrackOLBus
							.getPrimarySlaveID());
					p1SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(Arrays.asList(allocatedp1SlaveInfo)));
					p1SlaveNameLable.setValue(allocatedp1SlaveInfo.getName());

					if (StringUtils.isNotEmpty(transTrackOLBus.getStoreID()) && "CNC".equals(transTrackOLBus.getDeliveryType())
							&& StringUtils.isNotEmpty(transTrackOLBus.getSecondarySlaveID()))
					{
						p2slavesHlayout.setVisible(true);
						//p2SlaveIDListbox.setVisible(true);
						final SlaveInfo allocatedp2SlaveInfo = stockRoomLocationsFacade
								.getStockRoomLocationByLocationId(transTrackOLBus.getSecondarySlaveID());
						p2SlaveIDListbox.setModel(new ListModelList<SlaveInfo>(Arrays.asList(allocatedp2SlaveInfo)));
						p2SlaveNameLable.setValue(allocatedp2SlaveInfo.getName());
						p2slavesNameHlayout.setVisible(true);
					}
				}
			}
			orderLinesListbox.clearSelection();
		}
		else
		{

			LOG.error("transTrackOLBus(orderLine) is cont null");
		}

	}

	public void onSelectp1SlaveIDList()
	{
		final SlaveInfo info = (SlaveInfo) p1SlaveIDListbox.getSelectedItem().getValue();
		p1SlaveNameLable.setValue(info.getName());

	}

	public void onSelectp2SlaveIDList()
	{
		final SlaveInfo info = (SlaveInfo) p2SlaveIDListbox.getSelectedItem().getValue();
		p2SlaveNameLable.setValue(info.getName());
	}


	public void onSelectp1LogisticsIDList()
	{
		final Logistics logistics = (Logistics) p1LogisticsIDListbox.getSelectedItem().getValue();
		p1LogisticsNameLabel.setValue(logistics.getLogisticname());
	}




	public void slaveinfoBtnAction()
	{
		OrderLineBUC orderLineBuc = new OrderLineBUC();
		Logistics logistic = null;
		if (orderBUCFacade == null)
		{
			LOG.info("Not enjected object for orderBUCFacade");
			return;
		}

		if (transTrackOLBus.getFulfillmentType().equals("TSHIP"))
		{
			if (p1SlaveIDListbox.getSelectedItem() == null)
			{
				Messagebox.show("Please choose any one option from p1slaveid box", "Alert", Messagebox.OK, Messagebox.ERROR);
				return;
			}

			if ("CNC".equals(transTrackOLBus.getDeliveryType()))
			{
				if (p2SlaveIDListbox.getSelectedItem() != null)
				{
					final SlaveInfo p2slave = (SlaveInfo) p2SlaveIDListbox.getSelectedItem().getValue();
					orderLineBuc.setSecondarySlaveID(p2slave.getSlaveid());
					LOG.info("Selected p2slave id {}", p2slave.getSlaveid());
				}
			}
			else
			{
				if (p1LogisticsIDListbox.getSelectedItem() == null)
				{
					Messagebox.show("Please choose any one option from p1logistics box", "Alert", Messagebox.OK, Messagebox.ERROR);
					return;
				}
			}

			final SlaveInfo p1slave = (SlaveInfo) p1SlaveIDListbox.getSelectedItem().getValue();

			if (p1LogisticsIDListbox.getSelectedItem() != null)
			{
				logistic = (Logistics) p1LogisticsIDListbox.getSelectedItem().getValue();
				orderLineBuc.setPrimaryLogisticID(logistic.getLogisticsid());
			}
			orderLineBuc.setPrimarySlaveID(p1slave.getSlaveid());
			orderLineBuc.setTransactionId(transTrackOLBus.getTransactionId());
			orderLineBuc.setTransactionLineStatus(transTrackOLBus.getTransactionLineStatus());
			LOG.info("************************save buttom sent OLBUC is {}", orderLineBuc);
			orderLineAllocValidation(orderLineBuc, orderBUCFacade.allocateOrderLine(orderLineBuc));
		}
		else if (transTrackOLBus.getFulfillmentType().equals("SSHIP"))
		{
			if (p1SlaveIDListbox.getSelectedItem() == null)
			{
				Messagebox.show("Please choose any one option from P1SlaveID", "Alert", Messagebox.OK, Messagebox.ERROR);
				return;
			}

			orderLineBuc = new OrderLineBUC();
			if (p2SlaveIDListbox.getSelectedItem() != null)
			{
				final SlaveInfo p2slave = (SlaveInfo) p2SlaveIDListbox.getSelectedItem().getValue();
				orderLineBuc.setSecondarySlaveID(p2slave.getSlaveid());
			}
			final SlaveInfo p1slave = (SlaveInfo) p1SlaveIDListbox.getSelectedItem().getValue();
			LOG.info("Selected p1slave id {}", p1slave.getSlaveid());
			orderLineBuc.setPrimarySlaveID(p1slave.getSlaveid());

			orderLineBuc.setTransactionId(transTrackOLBus.getTransactionId());
			orderLineAllocValidation(orderLineBuc, orderBUCFacade.allocateOrderLine(orderLineBuc));
		}
	}

	private void orderLineAllocValidation(final OrderLineBUC source, final OrderLineBUC orderLBucRtrn)
	{
		if (orderLBucRtrn == null)
		{
			LOG.error("Update failed orderlineid {} :Reason update call return null", source.getTransactionId());
			Messagebox.show("Updating failed", "Alert", Messagebox.OK, Messagebox.ERROR);
		}
		else if (!source.getPrimarySlaveID().equals(orderLBucRtrn.getPrimarySlaveID())
				|| !source.getTransactionId().equals(orderLBucRtrn.getTransactionId()))
		{

			LOG.error("Orderline update failed :Reason ,not equal source p1slaveid {} with result p1slaveid",
					source.getPrimarySlaveID(), orderLBucRtrn.getPrimarySlaveID());
			Messagebox.show("Updating failed", "Alert", Messagebox.OK, Messagebox.ERROR);

		}
		else if (StringUtils.isNotEmpty(source.getSecondarySlaveID())
				&& !source.getSecondarySlaveID().equals(orderLBucRtrn.getSecondarySlaveID()))
		{
			LOG.error("Orderline update failed :Reason ,not equal source p2slaveid {} with result p2slaveid",
					source.getSecondarySlaveID(), orderLBucRtrn.getSecondarySlaveID());
			Messagebox.show("Updating failed", "Alert", Messagebox.OK, Messagebox.ERROR);
		}
		else
		{
			Messagebox.show("Successfully updated orderLine");
		}
	}

	private Object[] createLogisticsListForDropdown(final List<Logistics> source, final String orderLBLogisticsId)
	{
		final Object[] slaveInfoArray = new Object[2];
		LOG.debug("In createLogisticsListForDropdown() ,logisticsID :{}", orderLBLogisticsId);
		final ListModelList<Logistics> listModelList = new ListModelList<Logistics>();
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
				LOG.error("NO slaves avaliable for logistcs,list size " + source.size());
			}
		}
		else
		{
			LOG.error("***********************Logistics list is null");
		}


		return slaveInfoArray;

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
