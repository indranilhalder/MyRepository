/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.oms.api.logistics.LogisticsFacade;
import com.hybris.oms.api.orderlogistics.OrderLogisticsFacade;
import com.hybris.oms.domain.logistics.dto.Logistics;
import com.hybris.oms.domain.lpawb.dto.LPAWBSearch;
import com.hybris.oms.domain.lpawb.dto.LPOverrideAWBEdit;
import com.hybris.oms.domain.lpawb.dto.LPOverrideAWBEditResponse;
import com.hybris.oms.domain.lpawb.dto.OrderLineInfo;
import com.hybris.oms.domain.lpawb.dto.OrderLineResponse;
import com.hybris.oms.domain.lpawb.dto.TransactionInfo;
import com.techouts.backoffice.exception.InvalidLpOverrideSearchParams;


/**
 * this controller class for awb edit
 *
 * @author prabhakar
 */
public class AwbEditWidgetController
{
	private static final Logger LOG = Logger.getLogger(AwbEditWidgetController.class);
	private String txtOrderId;
	private String txtTransactionId;
	private String txtSlaveId;
	private String txtsellerId;
	private String selectionOrderStatus;
	private String selectionLpName;
	private Boolean isReturn = Boolean.FALSE;
	private final String transactionType = "AWB";
	private List<TransactionInfo> listOfTransactions; //incoming transactions
	private Set<String> lpList; //active logistcs Partners
	private List<OrderLineInfo> listOfOrderLineInfo;
	private Map<String, TransactionInfo> map;
	private List<String> ordersStatus;// orders statuses
	@WireVariable("orderLogisticsRestClient")
	private OrderLogisticsFacade orderLogisticsUpdateFacade;
	@WireVariable("logisticsRestClient")
	private LogisticsFacade logisticsFacade;

	@WireVariable
	private transient CockpitUserService cockpitUserService;
	@WireVariable
	private transient AuthorityGroupService authorityGroupService;
	private transient AuthorityGroup activeUserRole;

	@Init
	@NotifyChange(
	{ "ordersStatus", "lpList" })
	public void init()
	{
		LOG.info("inside init");
		ordersStatus = getOrderStatuses(isReturn);
		lpList = getLpSet();
		if (map == null)
		{
			map = new HashMap<String, TransactionInfo>();
		}
		if (listOfTransactions == null)
		{
			listOfTransactions = new ArrayList<TransactionInfo>();
		}
	}


	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) final Component view)
	{
		Selectors.wireComponents(view, this, false);
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
	public void isReturnCheck(@BindingParam("checkedValue") final Boolean isReturnCheckdValue)
	{
		LOG.info("is Return Checked Value" + isReturnCheckdValue);
		this.isReturn = isReturnCheckdValue;
		this.ordersStatus = getOrderStatuses(this.isReturn);
	}

	/*
	 * this method is used for active order statuses
	 *
	 * @return active order staueses
	 */
	private List<String> getOrderStatuses(final Boolean isReturn)
	{
		if (ordersStatus == null)
		{
			ordersStatus = new ArrayList<String>();
		}
		if (isReturn)
		{
			ordersStatus.clear();
			ordersStatus.add("REVERSEAWB");
			ordersStatus.add("RETURINIT");
		}
		else
		{
			ordersStatus.clear();
			ordersStatus.add("ODREALOC");
			ordersStatus.add("ORDREJEC");
			ordersStatus.add("PILIGENE");
			ordersStatus.add("PICKCONF");
			ordersStatus.add("HOTCOURI");
			ordersStatus.add("SCANNED");
		}
		return ordersStatus;
	}


	/*
	 * this method is used for search the list of orders based on the parameters
	 */
	@Command
	@NotifyChange(
	{ "listOfTransactions" })
	public void awbSearch()
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
			final List<TransactionInfo> transactionsList = orderLogisticsUpdateFacade.getOrderLogisticsInfo(lpAwbSearch)
					.getTransactionInfo(); //if response
			for (final TransactionInfo transaction : transactionsList)
			{
				final String orderStatus = transaction.getOrderStatus();
				if (orderStatus.equalsIgnoreCase("SCANNED") || orderStatus.equalsIgnoreCase("HOTCOURI")
						|| orderStatus.equalsIgnoreCase("REVERSEAWB"))
				{
					transaction.setAwbReadOnly(Boolean.FALSE);
				}
				else
				{
					transaction.setAwbReadOnly(Boolean.TRUE);
					//this step is removed once awbEditable default value== false at dto level
				}
			}
			listOfTransactions = transactionsList;

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
		map.put(selectedTransaction.getTransactionId(), selectedTransaction); //if required lp flag then send as bind prarm attribute
		LOG.info("the final map " + map.toString());
	}

	/*
	 * this method is used to persist the modified transactions
	 */
	@Command("saveAllTransactions")
	@NotifyChange(
	{ "listOfTransactions" })
	public void saveAllTransactions()
	{
		LOG.info("inside save all transaction");
		if (listOfOrderLineInfo == null)
		{
			listOfOrderLineInfo = new ArrayList<OrderLineInfo>();
		}
		for (final TransactionInfo transaction : map.values())
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
		final LPOverrideAWBEdit lpOverrideEdit = new LPOverrideAWBEdit();
		if (CollectionUtils.isEmpty(listOfOrderLineInfo))
		{
			Messagebox.show("No Changes Found ");
			return;
		}
		lpOverrideEdit.setOrderLineInfo(listOfOrderLineInfo);
		lpOverrideEdit.setIsReturn(isReturn);
		final String userId = cockpitUserService.getCurrentUser();
		lpOverrideEdit.setUserId(userId);
		activeUserRole = authorityGroupService.getActiveAuthorityGroupForUser(userId);
		if (activeUserRole != null)
		{
			lpOverrideEdit.setRoleId(activeUserRole.getCode());
		}
		else
		{
			lpOverrideEdit.setRoleId("none");
		}

		lpOverrideEdit.setTransactionType(transactionType);
		final LPOverrideAWBEditResponse lpOverrideAwbEditResponse = orderLogisticsUpdateFacade
				.updateOrderLogisticOrAwbNumber(lpOverrideEdit);
		final List<OrderLineResponse> orderLineResponse = lpOverrideAwbEditResponse.getOrderLineResponse();
		final StringBuilder message = new StringBuilder();
		for (final OrderLineResponse orderResponse : orderLineResponse)
		{
			message.append(orderResponse.getTransactionId() + "\t " + orderResponse.getStatus());
		}
		Messagebox.show(message.toString());
		map.clear();
		listOfOrderLineInfo.clear();
		message.setLength(0);
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


	public ListitemRenderer<String> getListItemRenderer()
	{
		ListitemRenderer<String> _rowRenderer = null;
		if (_rowRenderer == null)
		{
			_rowRenderer = new ListitemRenderer<String>()
			{
				public void render(final Listitem item, final String value,

						final int index) throws Exception
				{
					item.setLabel(value);
				}
			};
		}
		return _rowRenderer;
	}


}