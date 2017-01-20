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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.user.impl.AuthorityGroup;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
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
import com.hybris.oms.tata.services.LpAwbDataUploadService;


/**
 * this controller class for Lp override
 *
 * @author prabhakar
 */
public class LpoverrideWidgetController
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(LpoverrideWidgetController.class);

	private String txtOrderId;
	private String txtTransactionId;
	private String txtSlaveId;
	private String txtsellerId;
	private String selectionOrderStatus;
	private String selectionLpName;
	private Set<TransactionInfo> selectedEntities;
	private Boolean isReturn = Boolean.FALSE;
	private final String transactionType = "LP";
	private List<TransactionInfo> listOfTransactions; //incoming transactions
	private List<OrderLineResponse> orderlineRespone;
	private List<String> activelpList; //active logistcs Partners
	private List<String> dropDownSearchLpList; //active logistcs Partners drop down
	private final Map<String, TransactionInfo> modifiedTransactinTrack = new HashMap<>();//modifed transaction
	private List<String> ordersStatus;// orders statuses
	private Map<String, TransactionInfo> previousLpAndAwbNumberForTrack; //track  awb numbers for scanned/HOTC
	@WireVariable("orderLogisticsRestClient")
	private OrderLogisticsFacade orderLogisticsUpdateFacade;
	@WireVariable("logisticsRestClient")
	private LogisticsFacade logisticsFacade;
	@WireVariable
	private transient CockpitUserService cockpitUserService;
	@WireVariable
	private transient AuthorityGroupService authorityGroupService;
	@WireVariable
	private transient AuthorityGroup activeUserRole;
	private Boolean displayPopup = Boolean.FALSE;

	@WireVariable("lpAwbDataUploadService")
	private LpAwbDataUploadService lpAwbDataUploadService;
	private String errorMessageValue = "";
	@WireVariable
	private WidgetInstanceManager widgetInstanceManager;

	@Command
	@NotifyChange(
	{ "listOfTransactions", "displayPopup", "errorMessage" })
	public void lpSerachButton()
	{
		displayPopup = Boolean.FALSE;
		LOG.info("inside lp search");
		final LPAWBSearch lpAwbSearch = new LPAWBSearch();
		int count = 0;
		previousLpAndAwbNumberForTrack = new HashMap<String, TransactionInfo>();
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
					.getTransactionInfo(); //if response

			for (final TransactionInfo transaction : transactionsList)
			{
				final String orderStatus = transaction.getOrderStatus();
				if (orderStatus.equalsIgnoreCase(TataomsbackofficeConstants.ORDERSTATUS_SCANNED)
						|| orderStatus.equalsIgnoreCase(TataomsbackofficeConstants.ORDERSTATUS_HOTCOURI)
						|| orderStatus.equalsIgnoreCase(TataomsbackofficeConstants.REVERSE_ORDERSTATUS_REVERSEAWB))
				{
					transaction.setAwbReadOnly(Boolean.FALSE);
					//create new Transaction Object  for track
					final TransactionInfo newTransaction = new TransactionInfo();
					newTransaction.setAwbNumber(transaction.getAwbNumber());
					newTransaction.setOrderStatus(transaction.getOrderStatus());
					newTransaction.setLogisticName(transaction.getLogisticName());
					previousLpAndAwbNumberForTrack.put(transaction.getTransactionId(), newTransaction);
				}
				else
				{
					transaction.setAwbReadOnly(Boolean.TRUE); //this step is removed once awbEditable default value==true
					previousLpAndAwbNumberForTrack.put(transaction.getTransactionId(), transaction);
				}
			}
			this.listOfTransactions = transactionsList;
			this.errorMessageValue = "";
		}
		else
		{
			this.errorMessageValue = "At least one field is mandatory";
			if (this.listOfTransactions != null && CollectionUtils.isNotEmpty(this.listOfTransactions))
			{
				this.listOfTransactions.clear();
			}
		}
	}


	public List<String> getActivelpList()
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
			ordersStatus.add(TataomsbackofficeConstants.REVERSE_ORDERSTATUS_RETURINIT);
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_NONE);
		}
		else
		{
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_ORDALLOC);
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_PILIGENE);
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_PICKCONF);
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_SCANNED);
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_HOTCOURI);
			ordersStatus.add(TataomsbackofficeConstants.ORDERSTATUS_NONE);
		}
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
	 * this method is used for capture the changed transaction
	 */
	@Command("onChangeTransactionInfo")
	@NotifyChange(
	{ "lpOverrideEnabled" })
	public void onChangeTransactionInfo(@BindingParam("transaction") final TransactionInfo selectedTransaction)
	{

		modifiedTransactinTrack.put(selectedTransaction.getTransactionId(), selectedTransaction); //if required lp flag then send as bind prarm attribute

	}

	/*
	 * this method is used to persist the modified Lp override and force fit Transactions
	 */
	@Command("lpOverrideSave")
	@NotifyChange(
	{ "orderlineRespone", "displayPopup" })

	public void saveAllTransactions(@BindingParam("lpOverride") final Boolean lpOverride)
	{
		LOG.info("in side lp override and servicable" + lpOverride);

		final List<OrderLineInfo> listOfOrderLineInfo = new ArrayList<OrderLineInfo>();
		if (modifiedTransactinTrack != null && modifiedTransactinTrack.isEmpty())
		{
			Messagebox.show("No Changes Found ");
			displayPopup = Boolean.FALSE;
			return;
		}

		for (final TransactionInfo currentTransaction : modifiedTransactinTrack.values())
		{
			if (currentTransaction.getOrderStatus().equalsIgnoreCase(TataomsbackofficeConstants.ORDERSTATUS_HOTCOURI)
					|| currentTransaction.getOrderStatus().equalsIgnoreCase(TataomsbackofficeConstants.ORDERSTATUS_SCANNED)
					|| currentTransaction.getOrderStatus().equalsIgnoreCase(TataomsbackofficeConstants.REVERSE_ORDERSTATUS_REVERSEAWB))
			{
				//if changed transaction order status HOTC/SCANNED  then awb manditory to change
				final TransactionInfo previousTransaction = previousLpAndAwbNumberForTrack.get(currentTransaction.getTransactionId());
				/*
				 * if (previousTransaction == null) { Messagebox.show(
				 * "Please Search Again for Second Time LpOverride/ForceFit LP Operation when order status is SCANNED/HOTC/REVRSAWB"
				 * ); displayPopup = Boolean.FALSE; return; }
				 */

				if (!previousTransaction.getLogisticName().equals(currentTransaction.getLogisticName())
						&& previousTransaction.getAwbNumber().equals(currentTransaction.getAwbNumber()))
				{
					Messagebox.show(
							"Changes could not processed. AWB number should be changed along with LP name when order status is SCANNED/HOTC/REVRSAWB");
					return;
				}
			}
			final OrderLineInfo orderLineInfo = new OrderLineInfo();
			orderLineInfo.setOrderId(currentTransaction.getOrderId());
			orderLineInfo.setTransactionId(currentTransaction.getTransactionId());
			orderLineInfo.setLogisticName(currentTransaction.getLogisticName());
			orderLineInfo.setAwbNumber(currentTransaction.getAwbNumber());
			orderLineInfo.setLpOverride(lpOverride); //this will get from check box button
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
		if (CollectionUtils.isNotEmpty(orderlineRespone))
		{
			displayPopup = Boolean.TRUE;
		}
		for (final OrderLineResponse currentResponse : orderlineRespone) //this is added for LP override /force fit Lp operation at present oms not sending updated awb NUMBER
		{
			final TransactionInfo modifiedTransaction = modifiedTransactinTrack.get(currentResponse.getTransactionId());
			final TransactionInfo previousTransaction = previousLpAndAwbNumberForTrack.get(currentResponse.getTransactionId());
			if (currentResponse.getLogisticId() != null)
			{
				previousTransaction.setLogisticName(currentResponse.getLogisticId());
				previousTransaction.setAwbNumber(modifiedTransaction.getAwbNumber());
			}
		}
		modifiedTransactinTrack.clear();
	}

	@Command("nextLpSave")
	@NotifyChange(
	{ "orderlineRespone", "displayPopup", "selectedEntities", "listOfTransactions" })
	public void nextLpSaveTransaction()
	{
		LOG.info("inside nextLp");
		final List<OrderLineInfo> listOfOrderLineInfo = new ArrayList<OrderLineInfo>();
		if (CollectionUtils.isEmpty(selectedEntities))
		{
			displayPopup = Boolean.FALSE;
			Messagebox.show("Please Select  List Item");
			return;
		}

		for (final TransactionInfo transaction : selectedEntities)
		{
			if (transaction.getOrderStatus().equals(TataomsbackofficeConstants.ORDERSTATUS_SCANNED)
					|| transaction.getOrderStatus().equals(TataomsbackofficeConstants.ORDERSTATUS_HOTCOURI)
					|| transaction.getOrderStatus().equals(TataomsbackofficeConstants.REVERSE_ORDERSTATUS_REVERSEAWB))
			{
				Messagebox.show("Next Lp could not processed. Some of the order status is SCANNED/HOTC/REVRSAWB");
				selectedEntities.clear();
				return;
			}
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
		//refresh
		final ArrayList<TransactionInfo> refreshListTransactions = new ArrayList<TransactionInfo>();
		for (final TransactionInfo transaction : selectedEntities)
		{
			for (final OrderLineResponse ordreLineResponseRecord : orderlineRespone)
			{
				if (transaction.getTransactionId().equals(ordreLineResponseRecord.getTransactionId()))
				{
					if (ordreLineResponseRecord.getLogisticId() != null)
					{
						transaction.setLogisticName(ordreLineResponseRecord.getLogisticId());
						refreshListTransactions.add(transaction);
					}
				}
			}
		}
		this.listOfTransactions = refreshListTransactions;
		selectedEntities.clear();
		if (CollectionUtils.isNotEmpty(orderlineRespone))
		{
			displayPopup = Boolean.TRUE;
		}
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
		return getOrderStatuses(this.isReturn);
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
	 * @param txtOrderId
	 *           the txtOrderId to set
	 */
	public void setTxtOrderId(final String txtOrderId)
	{
		this.txtOrderId = txtOrderId;
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
	 * @return the dropDownSearchLpList
	 */
	public List<String> getDropDownSearchLpList()
	{
		return dropDownSearchLpList;
	}

	public String getErrorMessage()
	{
		return this.errorMessageValue;
	}
}