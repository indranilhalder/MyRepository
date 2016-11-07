/**
 *
 */
package com.techouts.backoffice.widget.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.oms.api.logistics.LogisticsFacade;
import com.hybris.oms.api.orderlogistics.OrderLogisticsFacade;
import com.hybris.oms.domain.logistics.dto.Logistics;
import com.hybris.oms.domain.lpawb.dto.LPAWBSearch;
import com.hybris.oms.domain.lpawb.dto.LPOverrideAWBEdit;
import com.hybris.oms.domain.lpawb.dto.LPOverrideAWBEditResponse;
import com.hybris.oms.domain.lpawb.dto.OrderLineInfo;
import com.hybris.oms.domain.lpawb.dto.OrderLineResponse;
import com.hybris.oms.domain.lpawb.dto.TransactionInfo;
import com.hybris.oms.tata.constants.TataomsbackofficeConstants;


/**
 * this controller class for awb edit
 *
 * @author prabhakar
 */
public class AwbEditWidgetController extends DefaultWidgetController
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
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
	private List<String> activelpList;//active logistcs Partners
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
	{ "ordersStatus" })
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
		lpList.add(TataomsbackofficeConstants.LPNAME_NONE);
		return lpList;
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) final Component view)
	{
		Selectors.wireComponents(view, this, false);
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

	@Command("isReturnCheck")
	@NotifyChange(
	{ "ordersStatus", "selectionOrderStatus", "listOfTransactions", "txtOrderId", "txtsellerId", "txtSlaveId", "selectionLpName",
			"txtTransactionId" })
	public void isReturnCheck(@BindingParam("checkedValue") final Boolean isReturnCheckdValue)
	{
		LOG.info("is Return Checked Value" + isReturnCheckdValue);
		selectionOrderStatus = null;
		txtOrderId = null;
		txtsellerId = null;
		txtSlaveId = null;
		selectionLpName = null;
		txtTransactionId = null;
		this.isReturn = isReturnCheckdValue;
		this.ordersStatus = getOrderStatuses(this.isReturn);
		if (this.listOfTransactions != null && CollectionUtils.isNotEmpty(this.listOfTransactions))
		{
			this.listOfTransactions.clear();
		}
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
			ordersStatus.add(TataomsbackofficeConstants.REVERSE_ORDERSTATUS_REVERSEAWB);
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_NONE);
		}
		else
		{
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_SCANNED);
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_HOTCOURI);
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_NONE);
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
		LOG.info("inside awb search");
		final LPAWBSearch lpAwbSearch = new LPAWBSearch();
		int count = 0;

		if (txtOrderId != null && StringUtils.isNotEmpty(txtOrderId))//orderid
		{
			++count;
			lpAwbSearch.setOrderId(txtOrderId);
		}
		if (txtTransactionId != null && StringUtils.isNotEmpty(txtTransactionId)) //transacion id
		{
			++count;
			lpAwbSearch.setTransactionId(txtTransactionId);
		}
		if (txtsellerId != null && StringUtils.isNotEmpty(txtsellerId)) //seller id
		{
			++count;
			lpAwbSearch.setSellerId(txtsellerId);
		}
		if (txtSlaveId != null && StringUtils.isNotEmpty(txtSlaveId)) //slave id
		{
			++count;
			lpAwbSearch.setSlaveId(txtSlaveId);
		}

		if (selectionOrderStatus != null && StringUtils.isNotEmpty(selectionOrderStatus))
		{
			if (!selectionOrderStatus.equalsIgnoreCase(TataomsbackofficeConstants.ORDERSTATUS_NONE))
			{
				++count;
				lpAwbSearch.setOrderStatus(selectionOrderStatus);
			}
		}
		if (selectionLpName != null && StringUtils.isNotEmpty(selectionLpName))
		{
			if (!selectionLpName.equalsIgnoreCase(TataomsbackofficeConstants.LPNAME_NONE))
			{
				++count;
				lpAwbSearch.setLogisticName(selectionLpName);
			}
		}

		if (count > 0)
		{
			lpAwbSearch.setTransactionType(transactionType);
			lpAwbSearch.setIsReturn(isReturn);
			final List<TransactionInfo> transactionsList = orderLogisticsUpdateFacade.getOrderLogisticsInfo(lpAwbSearch)
					.getTransactionInfo();

			this.listOfTransactions = transactionsList;
		}
		else
		{
			Messagebox.show("Atleast one field is mandatory");
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
	 * @return the activelpList
	 */
	public List<String> getActivelpList()
	{
		return activelpList;
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