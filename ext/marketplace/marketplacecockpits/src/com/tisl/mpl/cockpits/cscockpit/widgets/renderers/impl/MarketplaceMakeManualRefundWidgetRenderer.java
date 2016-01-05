/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.tisl.mpl.cockpits.cscockpit.services.RefundInitiatedNotificationService;
import com.tisl.mpl.cockpits.cscockpit.services.impl.DefaultRefundInitiatedNotificationService;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.SendSMSRequestData;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultColumnGroupConfiguration;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderItemsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class MarketplaceMakeManualRefundWidgetRenderer extends
		OrderDetailsOrderItemsWidgetRenderer {
	
	private static final Logger LOG = Logger.getLogger(MarketplaceMakeManualRefundWidgetRenderer.class);

	private List<OrderEntryModel> manualRefundList;
	private List<ReturnEntryModel> returnList;

	@Autowired
	private ModelService modelService;

	@Autowired
	private PopupWidgetHelper popupWidgetHelper;
	
	@Autowired
	private RefundInitiatedNotificationService refundInitiatedNotificationService;

	@Override
	protected HtmlBasedComponent createContentInternal(
			final ListboxWidget<OrderItemWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		manualRefundList = Collections
				.synchronizedList(new ArrayList<OrderEntryModel>());
		returnList = Collections
				.synchronizedList(new ArrayList<ReturnEntryModel>());
		Div div = new Div();
		if (widget.getWidgetController() instanceof MarketPlaceOrderController) {
			MarketPlaceOrderController orderController = (MarketPlaceOrderController) widget
					.getWidgetController();
			final TypedObject orderTypedObject = orderController
					.getCurrentOrder();
			if (orderTypedObject.getObject() instanceof OrderModel) {
				OrderModel orderModel = (OrderModel) orderTypedObject
						.getObject();
				if (orderModel != null) {

					final MarketPlaceOrderController controller = (MarketPlaceOrderController) widget
							.getWidgetController();
					controller.getContextController().setCurrentOrder(
							getCockpitTypeService().wrapItem(
									orderController.getCurrentOrder()));

					List<ReturnRequestModel> returnRequestList = orderModel
							.getReturnRequests();
					if (CollectionUtils.isNotEmpty(returnRequestList)) {
						for (ReturnRequestModel returnRequest : returnRequestList) {
							if (CollectionUtils.isNotEmpty(returnRequest
									.getReturnEntries())) {
								for (ReturnEntryModel returnEntry : returnRequest
										.getReturnEntries()) {
									if (returnEntry instanceof RefundEntryModel) {
										if (returnEntry.getOrderEntry() != null
												&& CollectionUtils
														.isNotEmpty(returnEntry
																.getOrderEntry()
																.getConsignmentEntries())) {
											ConsignmentStatus status = returnEntry
													.getOrderEntry()
													.getConsignmentEntries()
													.iterator().next()
													.getConsignment()
													.getStatus();

											if (status
													.equals(ConsignmentStatus.QC_FAILED)
													|| status
															.equals(ConsignmentStatus.RETURN_CLOSED)) {
												Vbox orderParentVbox = new Vbox();
												// orderParentVbox.setSclass("manualRefundVbox");
												createOrderDataTable(
														orderParentVbox,
														getCockpitTypeService()
																.wrapItem(
																		orderModel));
												div.appendChild(orderParentVbox);
												Button confirmButton = new Button(
														LabelUtils
																.getLabel(
																		"manualRefund.request.confirm",
																		"refundrequested",
																		new Object[0]));
												// confirmButton.setSclass("refundConfirmBtn");
												confirmButton.addEventListener(
														Events.ON_CLICK,
														new EventListener() {
															@Override
															public void onEvent(
																	Event arg0)
																	throws Exception {
																if (CollectionUtils
																		.isNotEmpty(manualRefundList)) {
																	Boolean isorderCOD = ((MarketPlaceOrderController) widget
																			.getWidgetController())
																			.isOrderCODforManualRefund(
																					(OrderModel) orderTypedObject
																							.getObject(),
																					manualRefundList);
																	if (isorderCOD) {
																		Messagebox
																				.show(LabelUtils
																						.getLabel(
																								widget,
																								"orderIsCOD",
																								new Object[0]),
																						MarketplaceCockpitsConstants.INFO,
																						Messagebox.OK,
																						Messagebox.ERROR);
																		popupWidgetHelper
																				.dismissCurrentPopup();
																		controller
																				.getContextController()
																				.dispatchEvent(
																						null,
																						null,
																						null);
																		return;
																	}
																	for (OrderEntryModel orderEntry : manualRefundList) {
																		if (CollectionUtils
																				.isNotEmpty(orderEntry
																						.getConsignmentEntries())) {
																			ConsignmentModel consignment = orderEntry
																					.getConsignmentEntries()
																					.iterator()
																					.next()
																					.getConsignment();
																			consignment
																					.setStatus(ConsignmentStatus.REFUND_INITIATED);
																			modelService
																					.save(consignment);
																		}
																	}
																	String result = ((MarketPlaceOrderController) widget
																			.getWidgetController())
																			.doRefundPayment(manualRefundList);
																	String[] resultArray=result.split(",");
																	// String
																	// result
																	// =
																	// "FAILURE";
																	if (ArrayUtils
																			.isNotEmpty(resultArray)) {
																		if (!resultArray[0]
																				.equalsIgnoreCase("FAILURE")) {
																			for (ReturnEntryModel returnEntry : returnList) {
																				returnEntry
																						.setStatus(ReturnStatus.RETURN_COMPLETED);
																				
																				modelService
																						.save(returnEntry);
																				//TODO :Return Initiated Notification 
																				
																				try
																				{
													 								//send Notification
																				int noOfItems= returnList.size();
																					getRefundInitiatedNotificationService().sendRefundInitiatedNotification(noOfItems,returnEntry,orderModel);
																				}
																				catch (final Exception e1)
																				{
																					LOG.error("Exception during sending Notification for Refund Initiation>>> ", e1);
																				}
																			
																			}
																			Messagebox
																					.show(LabelUtils
																							.getLabel(
																									widget,
																									"refundRequestedIsSuccess",
																									new Object[] { resultArray[1],resultArray[2] }));
																			popupWidgetHelper
																					.dismissCurrentPopup();
																		} else {
																			for (ReturnEntryModel returnEntry : returnList) {
																				returnEntry
																						.setStatus(ReturnStatus.RETURN_INPROGRESS);
																				modelService
																						.save(returnEntry);
																			}
																			Messagebox
																					.show(LabelUtils
																							.getLabel(
																									widget,
																									"refundRequestedIsFailure",
																									new Object[] { result }));
																			popupWidgetHelper
																					.dismissCurrentPopup();
																		}
																	} else {
																		Messagebox
																				.show(LabelUtils
																						.getLabel(
																								widget,
																								"refundRequestedUnresponsive",
																								new Object[] { result }));
																	}
																	
																} else {
																	Messagebox
																			.show(LabelUtils
																					.getLabel(
																							widget,
																							"errorNoRecordSelected",
																							new Object[0]),
																					MarketplaceCockpitsConstants.INFO,
																					Messagebox.OK,
																					Messagebox.ERROR);
																}
															}
														});
												div.appendChild(confirmButton);
												return div;
											}
										}
									}
								}
							}
						}
					}
				}

				div.appendChild(new Label(
						"No Request Available for Manual Refund"));
			}
		}
		return div;
	}

	protected void createOrderDataTable(Vbox parentComponent,
			TypedObject orderData) {
		Vbox parentVbox = new Vbox();
		//parentVbox.setSclass("parentVboxInvoice");
		Listbox tableVbox = new Listbox();
		//tableVbox.setSclass("orderDetailTable");
		OrderModel orderModel = null;
		if (orderData.getObject() instanceof OrderModel) {
			orderModel = (OrderModel) orderData.getObject();
			Listhead tableHeader = new Listhead();
			//tableHeader.setSclass("invoiceTableHeader");
			Listheader list = new Listheader();
			list.setWidth("30px");
			//list.setClass("invoiceHeaderLabel refundRequestCheckboxHeader");
			tableHeader.appendChild(list);
			List<ColumnGroupConfiguration> columnConfigurations = (List<ColumnGroupConfiguration>) getMasterColumns();
			if (CollectionUtils.isNotEmpty(columnConfigurations)) {
				for (ColumnGroupConfiguration col : columnConfigurations) {
					Listheader headerLabel = new Listheader(
							(col instanceof DefaultColumnGroupConfiguration) ? ((DefaultColumnGroupConfiguration) col)
									.getLabelWithFallback() : col.getLabel());
					headerLabel.setTooltip(col.getName());
					//headerLabel.setWidth("230px");
					tableHeader.appendChild(headerLabel);
				}
			}
			tableVbox.appendChild(tableHeader);
			if (orderModel != null) {
				List<ReturnRequestModel> returnRequestList = orderModel
						.getReturnRequests();
				if (CollectionUtils.isNotEmpty(returnRequestList)) {
					for (ReturnRequestModel returnRequest : returnRequestList) {
						if (CollectionUtils.isNotEmpty(returnRequest
								.getReturnEntries())) {
							for (ReturnEntryModel returnEntry : returnRequest
									.getReturnEntries()) {
								if (returnEntry instanceof RefundEntryModel) {
									if (returnEntry.getOrderEntry() != null
											&& CollectionUtils
													.isNotEmpty(returnEntry
															.getOrderEntry()
															.getConsignmentEntries())) {
										ConsignmentStatus status = returnEntry
												.getOrderEntry()
												.getConsignmentEntries()
												.iterator().next()
												.getConsignment().getStatus();

										if (status
												.equals(ConsignmentStatus.QC_FAILED)
												|| status
														.equals(ConsignmentStatus.RETURN_CLOSED)) {
											Listitem tableRow = new Listitem();
											// tableRow.setSclass("manualPaymentTableRow");
											createDataRow(
													getCockpitTypeService()
															.wrapItem(
																	returnEntry),
													tableRow, orderModel);
											tableVbox.appendChild(tableRow);
										}
									}
								}
							}
						}
					}
				}
			}
			parentVbox.appendChild(tableVbox);
			parentComponent.appendChild(parentVbox);
		}
	}

	protected void createDataRow(final TypedObject returnEntry, Listitem row,
			OrderModel orderModel) {
		List<ColumnConfiguration> columnConfigurations = getColumnConfigurations();
		final Checkbox refundChkBox = new Checkbox();
		/*
		 * StringBuilder labelString = new StringBuilder(LabelUtils.getLabel(
		 * "manualRefund.payment.label", "selectRefundEntry", new Object[0]));
		 */
		final ReturnEntryModel returnEntryModel = (ReturnEntryModel) returnEntry
				.getObject();
		// refundChkBox.setLabel(labelString.toString());
		final OrderEntryModel orderEntryModel = (OrderEntryModel) returnEntryModel
				.getOrderEntry();
		// refundChkBox.setSclass("refundCheckBox");
		refundChkBox.addEventListener(Events.ON_CHECK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				if (refundChkBox.isChecked()) {
					manualRefundList.add(orderEntryModel);
					returnList.add(returnEntryModel);
				} else {
					manualRefundList.remove(orderEntryModel);
					returnList.remove(returnEntryModel);
				}
			}
		});
		Listcell rowCell = new Listcell();
		rowCell.appendChild(refundChkBox);
		row.appendChild(rowCell);
		if (CollectionUtils.isNotEmpty(columnConfigurations)) {
			for (ColumnConfiguration col : columnConfigurations) {
				rowCell = new Listcell(ObjectGetValueUtils.getValue(
						col.getValueHandler(), returnEntry));
				// rowCell.setSclass("manualRefundRowLabel");
				row.appendChild(rowCell);
			}
		}
	}
	
	/**
	 * @return the notifyPaymentGroupMailService
	 */
	public RefundInitiatedNotificationService getRefundInitiatedNotificationService()
	{
		return refundInitiatedNotificationService;
	}

}