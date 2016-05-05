/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vbox;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultColumnGroupConfiguration;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderItemsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class MarketPlaceReturnRequestWidgetRenderer extends
		OrderDetailsOrderItemsWidgetRenderer {

	private List<ReturnEntryModel> refundEntries;
	// private boolean flag=false;

	@Autowired
	private ModelService modelService;

	@Autowired
	private PopupWidgetHelper popupWidgetHelper;

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;
	
	@Override
	protected HtmlBasedComponent createContentInternal(
			ListboxWidget<OrderItemWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		refundEntries = Collections
				.synchronizedList(new ArrayList<ReturnEntryModel>());
		Div div = new Div();
		boolean buttonFlag = false;
		if (widget.getWidgetController() instanceof MarketPlaceOrderController) {
			MarketPlaceOrderController orderController = (MarketPlaceOrderController) widget
					.getWidgetController();
			TypedObject orderTypedObject = orderController.getCurrentOrder();

			if (orderTypedObject.getObject() instanceof OrderModel) {
				Vbox orderParentVbox = new Vbox();
				orderParentVbox.setSclass("invoiceCompleteOrderVbox");
				buttonFlag = createOrderDataTable(orderParentVbox,
						orderTypedObject, buttonFlag);
				div.appendChild(orderParentVbox);

				// Submit button will convert the refund status of the request
				// to DELIVERED
				if (buttonFlag) {
					Button confirmButton = new Button(LabelUtils.getLabel(
							"refund.request.confirm", "refundrequested",
							new Object[0]));
					confirmButton.setSclass("invoiceConfirmBtn");
					confirmButton.addEventListener(Events.ON_CLICK,
							new EventListener() {

								@Override
								public void onEvent(Event arg0)
										throws Exception {
									// the Status of the Refund entry needs to
									// changed to delivered...
									if (CollectionUtils.isEmpty(refundEntries)) {
										Messagebox.show(
												LabelUtils
														.getLabel(
																"refund.request.confirm",
																"errorNoRecordSelected",
																new Object[0]),
												MarketplaceCockpitsConstants.INFO,
												Messagebox.OK, Messagebox.ERROR);
										return;
									} else {
										for (ReturnEntryModel refundEntry : refundEntries) {
											refundEntry
													.setStatus(ReturnStatus.DELIVERED);
											if(refundEntry.getOrderEntry()!=null && CollectionUtils.isNotEmpty(refundEntry.getOrderEntry().getConsignmentEntries())){
												//Do not save status updates in commerce, rather update it on OMS and let it come in sync.
//												ConsignmentModel consignmentModel=refundEntry.getOrderEntry().getConsignmentEntries().iterator().next().getConsignment();
//												consignmentModel.setStatus(ConsignmentStatus.RETURN_REJECTED);
//												modelService.save(consignmentModel);
												mplJusPayRefundService.makeOMSStatusUpdate(refundEntry.getOrderEntry(),ConsignmentStatus.RETURN_REJECTED);
											}
											modelService.save(refundEntry);											
										}
										popupWidgetHelper.dismissCurrentPopup();
									}
								}
							});
					div.appendChild(confirmButton);
				}
			}
		}
		return div;
	}

	/**
	 * The method will populate the pop up and header information of the table
	 * 
	 * @param parentComponent
	 * @param orderData
	 */
	private boolean createOrderDataTable(Vbox parentComponent,
			TypedObject orderData, boolean buttonFlag) {
		// Populate the refund data information in the table
		boolean resultFlag = false;
		OrderModel orderModel = null;
		Vbox parentVbox = new Vbox();
		parentVbox.setSclass("parentVboxInvoice");

		Listbox tableVbox = new Listbox();
		tableVbox.setSclass("orderDetailTable");

		Listhead tableHeader = new Listhead();
		tableHeader.setSclass("invoiceTableHeader");
		Listheader list = new Listheader();
		list.setClass("invoiceHeaderLabel refundRequestCheckboxHeader");
		tableHeader.appendChild(list);

		if (orderData.getObject() instanceof OrderModel) {
			orderModel = (OrderModel) orderData.getObject();
		}

		// Populate the header of the table from the xml
		List<ReturnEntryModel> allreturnRequestList = new ArrayList<ReturnEntryModel>();
		List<ReturnRequestModel> returnRequestList = orderModel
				.getReturnRequests();
		if (CollectionUtils.isNotEmpty(returnRequestList)) {
			for (ReturnRequestModel returnRequest : returnRequestList) {
				allreturnRequestList.addAll(returnRequest.getReturnEntries());
			}

			for (ReturnEntryModel returnEntry : allreturnRequestList) {
				if (returnEntry instanceof RefundEntryModel) {
					if (returnEntry.getOrderEntry() != null
							&& CollectionUtils.isNotEmpty(returnEntry
									.getOrderEntry().getConsignmentEntries())) {
						ConsignmentStatus status = returnEntry.getOrderEntry()
								.getConsignmentEntries().iterator().next()
								.getConsignment().getStatus();
						//TISOMSII-174 added additional check for RETURN_CANCELLED status R2.1  QC_FAILED status skipped for SP automation
						if (status.equals(ConsignmentStatus.QC_FAILED) || status.equals(ConsignmentStatus.RETURN_CANCELLED)) {

							List<ColumnGroupConfiguration> columnConfigurations = (List<ColumnGroupConfiguration>) getMasterColumns();
							if (CollectionUtils
									.isNotEmpty(columnConfigurations)) {
								for (ColumnGroupConfiguration col : columnConfigurations) {
									Listheader headerLabel = new Listheader(
											(col instanceof DefaultColumnGroupConfiguration) ? ((DefaultColumnGroupConfiguration) col)
													.getLabelWithFallback()
													: col.getLabel());
									headerLabel.setTooltip(col.getName());
									headerLabel.setSclass("invoiceHeaderLabel");
									tableHeader.appendChild(headerLabel);

								}
							}
							tableVbox.appendChild(tableHeader);
							buttonFlag = true;
							break;
						}
					}
				}
			}

			for (final ReturnEntryModel returnEntry : allreturnRequestList) {
				if (returnEntry instanceof RefundEntryModel) {
					if (returnEntry.getOrderEntry() != null
							&& CollectionUtils.isNotEmpty(returnEntry
									.getOrderEntry().getConsignmentEntries())) {
						ConsignmentStatus status = returnEntry.getOrderEntry()
								.getConsignmentEntries().iterator().next()
								.getConsignment().getStatus();
						//TISOMSII-174 added additional check for RETURN_CANCELLED status R2.1  QC_FAILED status skipped for SP automation 
						if (status.equals(ConsignmentStatus.QC_FAILED) || status.equals(ConsignmentStatus.RETURN_CANCELLED)) {
							resultFlag = true;
							Listitem tableRow = new Listitem();
							tableRow.setSclass("invoiceTableRow");
							Listcell rowLabel = new Listcell();
							final Checkbox refundEntryChkBox = new Checkbox();

							refundEntryChkBox.setSclass("shipmentCheckBox");
							refundEntryChkBox.addEventListener(Events.ON_CHECK,
									new EventListener() {

										@Override
										public void onEvent(Event arg0)
												throws Exception {

											// add the product entry and refund
											// entry
											if (returnEntry != null) {
												if (refundEntryChkBox
														.isChecked()) {
													refundEntries
															.add(returnEntry);
												} else {
													refundEntries
															.remove(returnEntry);
												}
											}
										}
									});

							rowLabel.appendChild(refundEntryChkBox);
							rowLabel.setSclass("invoiceRowLabel");
							tableRow.appendChild(rowLabel);
							createDataRow(
									getCockpitTypeService().wrapItem(
											returnEntry), tableRow);
							tableVbox.appendChild(tableRow);
						}
					}
				}
			}

		}

		if (!resultFlag) {
			parentVbox
					.appendChild(new Label("No Request Available for Reject"));
		}
		parentVbox.appendChild(tableVbox);
		parentComponent.appendChild(parentVbox);
		return buttonFlag;
	}

	/**
	 * The method will populate the data in the table.
	 * 
	 * @param entryData
	 * @param row
	 */
	private void createDataRow(TypedObject entryData, Listitem row) {
		List<ColumnConfiguration> columnConfigurations = getColumnConfigurations();
		if (CollectionUtils.isNotEmpty(columnConfigurations)) {
			for (ColumnConfiguration col : columnConfigurations) {

				Listcell rowLabel = new Listcell(ObjectGetValueUtils.getValue(
						col.getValueHandler(), entryData));
				rowLabel.setSclass("invoiceRowLabel");
				row.appendChild(rowLabel);
			}
		}
	}
}
