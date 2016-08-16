/**
 *
 */
package com.techouts.backoffice.widget.controller;

import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;

import com.hybris.oms.api.logistics.LogisticsFacade;
import com.hybris.oms.api.orderlogistics.OrderLogisticsUpdateFacade;
import com.hybris.oms.domain.logistics.dto.Logistics;
import com.hybris.oms.domain.lpawb.dto.LPAWBSearch;
import com.hybris.oms.domain.lpawb.dto.LPOverrideAWBEdit;
import com.hybris.oms.domain.lpawb.dto.LPOverrideAWBEditResponse;
import com.hybris.oms.domain.lpawb.dto.OrderLineInfo;
import com.hybris.oms.domain.lpawb.dto.OrderLineResponse;
import com.hybris.oms.domain.lpawb.dto.TransactionInfo;
import com.techouts.backoffice.exception.InvalidLpOverrideSearchParams;


/**
 * this controller class for Lp override
 *
 * @author prabhakar
 */
public class LpoverrideWidgetController
{
	private static final Logger LOG = Logger.getLogger(LpoverrideWidgetController.class);
	private String txtOrderId;
	private String txtTransactionId;
	private String txtSlaveId;
	private String txtsellerId;
	private String selectionOrderStatus;
	private String selectionLpName;
	private Boolean isReturn = Boolean.FALSE;
	private final String transactionType = "LP";
	private List<TransactionInfo> listOfTransactions; //incoming transactions
	private Set<String> lpList; //active logistcs Partners

	private List<OrderLineInfo> listOfOrderLineInfo = new ArrayList<OrderLineInfo>(); //outgoing transactions
	private final Map<String, TransactionInfo> map = new HashMap<String, TransactionInfo>();//modifed transaction

	private List<String> ordersStatus;// orders statuses



	@WireVariable("orderLogisticsUpdateRestClient")
	private OrderLogisticsUpdateFacade orderLogisticsUpdateFacade;

	@WireVariable("logisticsRestClient")
	private LogisticsFacade logisticsFacade;

	@WireVariable
	private UserService userService;

	@Init
	@NotifyChange(
	{ "ordersStatus", "lpList" })
	public void init()
	{
		LOG.info("inside init");
		ordersStatus = getOrderStatuses();
		lpList = getLpSet();

	}

	private Set<String> getLpSet()
	{
		final List<Logistics> list = (List<Logistics>) logisticsFacade.getAll();
		if (lpList == null)
		{
			lpList = new HashSet<String>();
		}
		for (final Logistics logistics : list)
		{
			if (logistics.getActive())
			{
				LOG.info(" active lpnames " + logistics.getLogisticname());
				lpList.add(logistics.getLogisticname());
			}
		}
		return lpList;

	}

	/*
	 * this method is used for when order statuses changed to return order statuses when he checked is return checkbox
	 */

	/**
	 * @return the ordersStatus
	 */
	public List<String> getOrdersStatus()
	{
		return ordersStatus;
	}

	/**
	 * @param ordersStatus
	 *           the ordersStatus to set
	 */
	public void setOrdersStatus(final List<String> ordersStatus)
	{
		this.ordersStatus = ordersStatus;
	}

	@Command("isReturnCheck")
	@NotifyChange(
	{ "ordersStatus" })
	public void isReturnCheck()
	{
		LOG.info("is Return is clicked " + isReturn);
		ordersStatus = getOrderStatuses();
	}

	/*
	 * this method is used for active order statuses
	 *
	 * @return active order staueses
	 */
	private List<String> getOrderStatuses()
	{
		if (ordersStatus == null)
		{
			ordersStatus = new ArrayList<String>();
		}

		if (isReturn.equals(Boolean.FALSE))
		{
			ordersStatus.clear();
			ordersStatus.add("ODREALOC");
			ordersStatus.add("ORDREJEC");
			ordersStatus.add("PILIGENE");
			ordersStatus.add("PICKCONF");
			ordersStatus.add("HOTCOURI");
			ordersStatus.add("SCANNED");

		}
		else
		{
			ordersStatus.clear();
			ordersStatus.add("REVERSEAWB");
			ordersStatus.add("RETURINIT");
		}

		return ordersStatus;
	}


	/*
	 * this method is used for search the list of orders based on the parameters
	 */
	@Command("lpAwbSearch")
	@NotifyChange(
	{ "listOfTransactions" })
	public void lpAwbSearch()
	{
		LOG.info("inside lpawb search");

		final LPAWBSearch lpAwbSearch = new LPAWBSearch();

		try
		{
			if (StringUtils.isNotEmpty(selectionOrderStatus))
			{
				LOG.info("in side selection order status" + selectionOrderStatus);
				if (StringUtils.isEmpty(txtSlaveId))
				{
					throw new InvalidLpOverrideSearchParams("slave id is required ");
				}
				lpAwbSearch.setOrderStatus(selectionOrderStatus);
			}
			if (StringUtils.isNotEmpty(selectionLpName))
			{
				LOG.info("second time lp checking Checking and slave id");

				if (StringUtils.isEmpty(txtSlaveId))
				{
					throw new InvalidLpOverrideSearchParams("slave id is required ");
				}
				lpAwbSearch.setLogisticName(selectionLpName);
			}
			if (StringUtils.isNotEmpty(txtOrderId))//orderid
			{
				LOG.info("order id : =" + txtOrderId);
				lpAwbSearch.setOrderId(txtOrderId);
			}
			if (StringUtils.isNotEmpty(txtTransactionId)) //transacion id
			{
				LOG.info("transaction id : =" + txtTransactionId);
				lpAwbSearch.setTransactionId(txtTransactionId);

			}
			if (StringUtils.isNotEmpty(txtsellerId)) //seller id
			{
				LOG.info("seller id : =" + txtsellerId);
				lpAwbSearch.setSellerId(txtsellerId);
			}
			if (StringUtils.isNotEmpty(txtSlaveId)) //slave id
			{
				LOG.info("slave id : =" + txtSlaveId);
				lpAwbSearch.setSlaveId(txtSlaveId);
			}

			lpAwbSearch.setTransactionType(transactionType);
			lpAwbSearch.setIsReturn(isReturn);

			//before doing next line get the response and check if response equals 50 then say the message user user search params records exceeds to more
			listOfTransactions = orderLogisticsUpdateFacade.getOrderLogisticsInfo(lpAwbSearch).getTransactionInfo(); //if response

		}
		catch (final InvalidLpOverrideSearchParams exception)
		{
			Messagebox.show(exception.getMessage());
		}
	}

	/*
	 * this method is used for capture the changed transaction
	 */
	@Command("onChangeTransactionInfo")
	public void onChangeTransactionInfo(@BindingParam("transaction") final TransactionInfo selectedTransaction)
	{
		LOG.info("inside onchange");
		map.put(selectedTransaction.getTransactionId(), selectedTransaction); //if required lp flag then send as bind prarm attribute
		LOG.info("the final map " + map.toString());
	}

	/*
	 * this method is used to persist the modified transactions
	 */
	@Command("saveAllTransactions")
	public void saveAllTransactions()
	{
		LOG.info("inside save all transaction");
		listOfTransactions = new ArrayList<TransactionInfo>(map.values());

		if (listOfOrderLineInfo == null)
		{
			listOfOrderLineInfo = new ArrayList<OrderLineInfo>();
		}

		for (final TransactionInfo transaction : listOfTransactions)
		{

			final OrderLineInfo orderLineInfo = new OrderLineInfo();
			orderLineInfo.setOrderId(transaction.getOrderId());
			orderLineInfo.setTransactionId(transaction.getTransactionId());
			orderLineInfo.setLogisticName(transaction.getLogisticName());
			orderLineInfo.setAwbNumber(transaction.getAwbNumber());
			orderLineInfo.setLpOverride(Boolean.FALSE); //this will get from check box button
			orderLineInfo.setNextLP(Boolean.FALSE);//this will get from checkbox button
			listOfOrderLineInfo.add(orderLineInfo);
		}

		LOG.info("list of orders" + listOfOrderLineInfo.toString());
		final LPOverrideAWBEdit lpOverrideEdit = new LPOverrideAWBEdit();
		lpOverrideEdit.setOrderLineInfo(listOfOrderLineInfo);
		lpOverrideEdit.setIsReturn(isReturn);
		lpOverrideEdit.setUserId(userService.getCurrentUser().getUid());
		lpOverrideEdit.setRoleId(userService.getCurrentUser().getUid()); //logic has to be get used role hear
		lpOverrideEdit.setTransactionType(transactionType);

		final LPOverrideAWBEditResponse lpOverrideAwbEditResponse = orderLogisticsUpdateFacade
				.updateOrderLogisticOrAwbNumber(lpOverrideEdit);
		final List<OrderLineResponse> orderLineResponse = lpOverrideAwbEditResponse.getOrderLineResponse();

		final StringBuilder message = new StringBuilder("*** transactions statuses***" + "/n");

		for (final OrderLineResponse orderResponse : orderLineResponse)
		{

			message.append(orderResponse.getTransactionId() + "\t" + orderResponse.getStatus() + "\n");
		}
		listOfTransactions.clear();
		Messagebox.show("" + message.toString());

	}

	/* all setter and getter methods */
	/**
	 * @return the listOfOrderLineInfo
	 */
	public List<OrderLineInfo> getListOfOrderLineInfo()
	{
		return listOfOrderLineInfo;
	}

	/**
	 * @param listOfOrderLineInfo
	 *           the listOfOrderLineInfo to set
	 */
	public void setListOfOrderLineInfo(final List<OrderLineInfo> listOfOrderLineInfo)
	{
		this.listOfOrderLineInfo = listOfOrderLineInfo;
	}




	/**
	 * @return the lpList
	 */
	public Set<String> getLpList()
	{
		return lpList;
	}



	/**
	 * @param lpList
	 *           the lpList to set
	 */
	public void setLpList(final Set<String> lpList)
	{
		this.lpList = lpList;
	}



	/**
	 * @return the listOfTransactions
	 */
	public List<TransactionInfo> getListOfTransactions()
	{
		return listOfTransactions;
	}

	/**
	 * @return the isReturn
	 */
	public Boolean getIsReturn()
	{
		return isReturn;
	}

	/**
	 * @param isReturn
	 *           the isReturn to set
	 */
	public void setIsReturn(final Boolean isReturn)
	{
		this.isReturn = isReturn;
	}

	/**
	 * @param listOfTransactions
	 *           the listOfTransactions to set
	 */
	public void setListOfTransactions(final List<TransactionInfo> listOfTransactions)
	{
		this.listOfTransactions = listOfTransactions;
	}

	/**
	 * @return the txtOrderId
	 */
	public String getTxtOrderId()
	{
		return txtOrderId;
	}

	/**
	 * @param txtOrderId
	 *           the txtOrderId to set
	 */
	@NotifyChange
	public void setTxtOrderId(final String txtOrderId)
	{
		this.txtOrderId = txtOrderId;
	}

	/**
	 * @return the txtTransactionId
	 */
	public String getTxtTransactionId()
	{
		return txtTransactionId;
	}

	/**
	 * @param txtTransactionId
	 *           the txtTransactionId to set
	 */
	@NotifyChange
	public void setTxtTransactionId(final String txtTransactionId)
	{
		this.txtTransactionId = txtTransactionId;
	}

	/**
	 * @return the txtSlaveId
	 */
	public String getTxtSlaveId()
	{
		return txtSlaveId;
	}

	/**
	 * @param txtSlaveId
	 *           the txtSlaveId to set
	 */
	@NotifyChange
	public void setTxtSlaveId(final String txtSlaveId)
	{
		this.txtSlaveId = txtSlaveId;
	}

	/**
	 * @return the txtsellerId
	 */
	@NotifyChange
	public String getTxtsellerId()
	{
		return txtsellerId;
	}

	/**
	 * @param txtsellerId
	 *           the txtsellerId to set
	 */
	@NotifyChange
	public void setTxtsellerId(final String txtsellerId)
	{
		this.txtsellerId = txtsellerId;
	}

	/**
	 * @return the selectionOrderStatus
	 */
	public String getSelectionOrderStatus()
	{
		return selectionOrderStatus;
	}

	/**
	 * @param selectionOrderStatus
	 *           the selectionOrderStatus to set
	 */
	@NotifyChange
	public void setSelectionOrderStatus(final String selectionOrderStatus)
	{
		this.selectionOrderStatus = selectionOrderStatus;
	}

	/**
	 * @return the selectionLpName
	 */
	public String getSelectionLpName()
	{
		return selectionLpName;
	}

	/**
	 * @param selectionLpName
	 *           the selectionLpName to set
	 */
	@NotifyChange
	public void setSelectionLpName(final String selectionLpName)
	{
		this.selectionLpName = selectionLpName;
	}



	/**
	 * @return the transactionType
	 */
	public String getTransactionType()
	{
		return transactionType;
	}

}