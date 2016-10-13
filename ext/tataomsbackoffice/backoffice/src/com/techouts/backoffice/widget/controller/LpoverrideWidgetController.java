/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listbox;
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
	private Set<TransactionInfo> selectedEntities;
	private Boolean isReturn = Boolean.FALSE;
	private Boolean lpOverrideEnabled = Boolean.FALSE;
	private final String transactionType = "LP";
	private List<TransactionInfo> listOfTransactions; //incoming transactions
	private List<OrderLineResponse> orderlineRespone;
	private List<String> activelpList; //active logistcs Partners
	private Map<String, TransactionInfo> map;//modifed transaction
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
	private Boolean displayPopup = Boolean.FALSE;
	@Wire("#listBoxData")
	private Listbox listBoxData;

	@Init
	@NotifyChange(
	{ "ordersStatus", "lpList" })
	public void init()
	{
		LOG.info("inside init");
		ordersStatus = getOrderStatuses(isReturn);
		activelpList = getLpSet();
		if (map == null)
		{
			map = new HashMap<String, TransactionInfo>();
		}
	}

	private List<String> getLpSet()
	{
		final List<Logistics> list = (List<Logistics>) logisticsFacade.getAll();

		final List<String> lpList = new ArrayList<>();
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
	 * this method is used for active order statuses
	 *
	 * @return active order staueses
	 */
	private List<String> getOrderStatuses(final Boolean isReturn)
	{
		final List<String> ordersStatus = new ArrayList<String>();

		if (isReturn)
		{
			ordersStatus.add("REVERSEAWB");
			ordersStatus.add("RETURINIT");
		}
		else
		{
			ordersStatus.add("ODREALOC");
			ordersStatus.add("ORDREJEC");
			ordersStatus.add("PILIGENE");
			ordersStatus.add("PICKCONF");
			ordersStatus.add("HOTCOURI");
			ordersStatus.add("SCANNED");
		}
		return ordersStatus;
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
	 * this method is used for search the list of orders based on the parameters
	 */
	@Command
	@NotifyChange(
	{ "listOfTransactions" })
	public void lpSearch()
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
					transaction.setAwbEditable(Boolean.FALSE);
				}
				else
				{
					transaction.setAwbEditable(Boolean.TRUE); //this step is removed once awbEditable default value== false at dto level
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
	@NotifyChange(
	{ "lpOverrideEnabled" })
	public void onChangeTransactionInfo(@BindingParam("transaction") final TransactionInfo selectedTransaction)
	{
		LOG.info("inside onchange");

		lpOverrideEnabled = Boolean.TRUE;
		map.put(selectedTransaction.getTransactionId(), selectedTransaction); //if required lp flag then send as bind prarm attribute
		LOG.info("the final map " + map.toString());
	}

	/**
	 * @return the lpOverrideEnabled
	 */
	public Boolean getLpOverrideEnabled()
	{
		return lpOverrideEnabled;
	}

	/*
	 * this method is used to persist the modified transactions
	 */
	@Command("lpOverrideSave")
	@NotifyChange(
	{ "orderlineRespone", "displayPopup" })

	public void saveAllTransactions()
	{
		LOG.info("in side lp override");

		final List<OrderLineInfo> listOfOrderLineInfo = new ArrayList<OrderLineInfo>();
		if (CollectionUtils.sizeIsEmpty(map))
		{
			Messagebox.show("No Changes Found ");
			displayPopup = Boolean.FALSE;
			return;
		}

		for (final TransactionInfo transaction : map.values())
		{
			final OrderLineInfo orderLineInfo = new OrderLineInfo();
			orderLineInfo.setOrderId(transaction.getOrderId());
			orderLineInfo.setTransactionId(transaction.getTransactionId());
			orderLineInfo.setLogisticName(transaction.getLogisticName());
			orderLineInfo.setAwbNumber(transaction.getAwbNumber());
			orderLineInfo.setLpOverride(Boolean.TRUE); //this will get from check box button
			orderLineInfo.setNextLP(Boolean.FALSE);//this will get from checkbox button
			listOfOrderLineInfo.add(orderLineInfo);
		}
		LOG.info("No Of Transactions For LpOverride Lp " + listOfOrderLineInfo.size());
		final LPOverrideAWBEdit lpOverrideEdit = new LPOverrideAWBEdit();
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
		orderlineRespone = lpOverrideAwbEditResponse.getOrderLineResponse();
		listBoxData.clearSelection();
		selectedEntities.clear();
		if (CollectionUtils.isNotEmpty(orderlineRespone))
		{
			displayPopup = Boolean.TRUE;
		}
		map.clear();

	}

	@Command("nextLpSave")
	@NotifyChange(
	{ "orderlineRespone", "displayPopup", "selectedEntities" })
	public void nextLpSaveTransaction()
	{
		LOG.info("inside nextLp");
		final List<OrderLineInfo> listOfOrderLineInfo = new ArrayList<OrderLineInfo>();
		if (CollectionUtils.isEmpty(selectedEntities))
		{
			displayPopup = Boolean.FALSE;
			Messagebox.show("No Changes Found ");
			return;
		}

		if (this.selectedEntities != null)
		{
			for (final TransactionInfo transaction : selectedEntities)
			{
				final OrderLineInfo orderLineInfo = new OrderLineInfo();
				orderLineInfo.setOrderId(transaction.getOrderId());
				orderLineInfo.setTransactionId(transaction.getTransactionId());
				orderLineInfo.setLogisticName(transaction.getLogisticName());
				orderLineInfo.setAwbNumber(transaction.getAwbNumber());
				orderLineInfo.setLpOverride(Boolean.FALSE); //this will get from check box button
				orderLineInfo.setNextLP(Boolean.TRUE);//this will get from checkbox button
				listOfOrderLineInfo.add(orderLineInfo);
			}
			LOG.info("No Of Transactions For Next Lp " + listOfOrderLineInfo.size());
			final LPOverrideAWBEdit lpOverrideEdit = new LPOverrideAWBEdit();
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
			orderlineRespone = lpOverrideAwbEditResponse.getOrderLineResponse();
			listBoxData.clearSelection();
			selectedEntities.clear();
			if (CollectionUtils.isNotEmpty(orderlineRespone))
			{
				displayPopup = Boolean.TRUE;
			}
		}

	}


	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) final Component view)
	{
		Selectors.wireComponents(view, this, false);
	}

	/**
	 * List box item renderer
	 *
	 * @return ListitemRenderer
	 */
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

	/**
	 * @return the activelpList
	 */
	public List<String> getActivelpList()
	{
		return activelpList;
	}


	/**
	 * @return the displayPopup
	 */
	public Boolean getDisplayPopup()
	{
		return displayPopup;
	}


	/**
	 * @return the orderlineRespone
	 */
	public List<OrderLineResponse> getOrderlineRespone()
	{
		return orderlineRespone;
	}

	/**
	 * @return the ordersStatus
	 */
	public List<String> getOrdersStatus()
	{
		return ordersStatus;
	}




	@NotifyChange
	public void setSelectedEntities(final Set<TransactionInfo> entities)
	{
		if (CollectionUtils.isNotEmpty(this.selectedEntities))
		{
			this.selectedEntities.clear();
		}
		this.selectedEntities = entities;
	}

	public Set<TransactionInfo> getSelectedEntities()
	{
		return selectedEntities;
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



}