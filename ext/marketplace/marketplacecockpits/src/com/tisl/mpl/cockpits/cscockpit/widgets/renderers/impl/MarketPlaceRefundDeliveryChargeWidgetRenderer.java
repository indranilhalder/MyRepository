package com.tisl.mpl.cockpits.cscockpit.widgets.renderers.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.data.RefundDeliveryData;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants.Enumerations.OrderModificationEntryStatus;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultColumnGroupConfiguration;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.widgets.ListboxWidget;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.LabelUtils;
import de.hybris.platform.cscockpit.utils.ObjectGetValueUtils;
import de.hybris.platform.cscockpit.widgets.controllers.OrderController;
import de.hybris.platform.cscockpit.widgets.models.impl.OrderItemWidgetModel;
import de.hybris.platform.cscockpit.widgets.renderers.impl.OrderDetailsOrderItemsWidgetRenderer;
import de.hybris.platform.cscockpit.widgets.renderers.utils.PopupWidgetHelper;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;

public class MarketPlaceRefundDeliveryChargeWidgetRenderer extends
		OrderDetailsOrderItemsWidgetRenderer {

	private Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap;

	@Autowired
	private ModelService modelService;

	@Autowired
	private PopupWidgetHelper popupWidgetHelper;

	@Autowired
	private EnumerationService enumerationService;

	@Override
	protected HtmlBasedComponent createContentInternal(
			final ListboxWidget<OrderItemWidgetModel, OrderController> widget,
			HtmlBasedComponent rootContainer) {
		refundMap = new HashMap<>();
		Div div = new Div();
		boolean buttonFlag = false;
		if (widget.getWidgetController() instanceof MarketPlaceOrderController) {
			final MarketPlaceOrderController orderController = (MarketPlaceOrderController) widget
					.getWidgetController();
			final TypedObject orderTypedObject = orderController
					.getCurrentOrder();
			if (orderTypedObject.getObject() instanceof OrderModel) {
				Vbox orderParentVbox = new Vbox();
				OrderModel order = (OrderModel) orderTypedObject.getObject();
				final CurrencyModel cartCurrencyModel = order.getCurrency();
				final NumberFormat currencyInstance = (NumberFormat) getSessionService()
						.executeInLocalView(new SessionExecutionBody() {
							public Object execute() {
								MarketPlaceRefundDeliveryChargeWidgetRenderer.this
										.getCommonI18NService()
										.setCurrentCurrency(cartCurrencyModel);
								return MarketPlaceRefundDeliveryChargeWidgetRenderer.this
										.getFormatFactory()
										.createCurrencyFormat();
							}
						});
				buttonFlag = createOrderDataTable(widget, orderParentVbox,
						orderTypedObject, buttonFlag, currencyInstance);
				div.appendChild(orderParentVbox);

				final MarketPlaceOrderController controller = (MarketPlaceOrderController) widget
						.getWidgetController();
				controller.getContextController().setCurrentOrder(
						getCockpitTypeService().wrapItem(
								orderController.getCurrentOrder()));

				if (buttonFlag) {
					Button confirmButton = new Button(LabelUtils.getLabel(
							"refund.request.confirm", "refundrequested",
							new Object[0]));
					confirmButton.addEventListener(Events.ON_CLICK,
							new EventListener() {
								@Override
								public void onEvent(Event arg0)
										throws Exception {
									Double totalRefundDeliveryCharges = Double
											.valueOf(0);
									boolean flag = true;

									if (MapUtils.isNotEmpty(refundMap)) {

										CollectionUtils.filter(
												refundMap.entrySet(),
												new Predicate() {
													@Override
													public boolean evaluate(
															Object o) {
														Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> entry = (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData>) o;
														return null != entry
																.getValue() ? entry
																.getValue()
																.isChecked()
																: false;
													}
												});

										if (CollectionUtils.isEmpty(refundMap
												.entrySet())) {
											flag = false;
											return;
										}

										for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
												.entrySet()) {
											totalRefundDeliveryCharges = totalRefundDeliveryCharges
													+ refundEntry.getKey()
															.getCurrDelCharge();
											if (StringUtils.isEmpty(refundEntry
													.getValue().getReason())) {
												flag = false;
												break;
											}
										}

										Boolean isorderCOD = ((MarketPlaceOrderController) widget
												.getWidgetController())
												.isOrderCODforDeliveryCharges(
														(OrderModel) orderTypedObject
																.getObject(),
														refundMap);

										if (isorderCOD) {
											Messagebox.show(
													LabelUtils.getLabel(widget,
															"orderIsCOD",
															new Object[0]),
													MarketplaceCockpitsConstants.INFO,
													Messagebox.OK,
													Messagebox.ERROR);
											popupWidgetHelper
													.dismissCurrentPopup();
											controller.getContextController()
													.dispatchEvent(null, null,
															null);
											return;
										}

										if (!flag) {
											Messagebox.show(
													LabelUtils
															.getLabel(
																	widget,
																	"errorNoReasonSelected",
																	new Object[0]),
													MarketplaceCockpitsConstants.INFO,
													Messagebox.OK,
													Messagebox.ERROR);
										} else {
											TypedObject refundDeliveryChargesRequest = ((MarketPlaceOrderController) widget
													.getWidgetController())
													.createRefundDeliveryChargesRequest(
															(OrderModel) orderTypedObject
																	.getObject(),
															refundMap);
											if (refundDeliveryChargesRequest != null) {
												popupWidgetHelper
														.dismissCurrentPopup();
												PaymentTransactionModel paymentTransactionModel = (PaymentTransactionModel) refundDeliveryChargesRequest
														.getObject();

												if (paymentTransactionModel
														.getStatus()
														.equals(OrderModificationEntryStatus.SUCCESSFULL)) {
													TypedObject orderHistoryRequest = ((MarketPlaceOrderController) widget
															.getWidgetController())
															.createOrderHistoryRequest(
																	(OrderModel) orderTypedObject
																			.getObject(),
																	refundMap,
																	totalRefundDeliveryCharges);
													Messagebox.show(
															LabelUtils
																	.getLabel(
																			widget,
																			"refundNumber",
																			new Object[] {
																					paymentTransactionModel
																							.getCode(),
																					currencyInstance
																							.format(totalRefundDeliveryCharges) }),
															MarketplaceCockpitsConstants.INFO,
															Messagebox.OK,
															Messagebox.INFORMATION);
												} else {
													Messagebox.show(
															LabelUtils
																	.getLabel(
																			widget,
																			"refund.failed",
																			new Object[] { paymentTransactionModel
																					.getCode() }),
															MarketplaceCockpitsConstants.INFO,
															Messagebox.OK,
															Messagebox.ERROR);
												}
											}
										}
									} else {
										Messagebox.show(
												LabelUtils
														.getLabel(
																widget,
																"errorNoRecordSelected",
																new Object[0]),
												MarketplaceCockpitsConstants.INFO,
												Messagebox.OK, Messagebox.ERROR);
									}

									controller.getContextController()
											.dispatchEvent(null, null, null);
								}
							});
					div.appendChild(confirmButton);
				}
			}
		}
		return div;
	}

	private boolean createOrderDataTable(
			final ListboxWidget<OrderItemWidgetModel, OrderController> widget,
			Vbox parentComponent, TypedObject orderData, boolean buttonFlag,
			NumberFormat currencyInstance) {
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
		List<AbstractOrderEntryModel> orderEntryList = new ArrayList<AbstractOrderEntryModel>(
				orderModel.getEntries());
		if (CollectionUtils.isNotEmpty(orderEntryList)) {
			CollectionUtils.filter(orderEntryList, new Predicate() {
				@Override
				public boolean evaluate(Object o) {
					AbstractOrderEntryModel entry = (AbstractOrderEntryModel) o;
					return entry.getCurrDelCharge() != 0D;
				}
			});
			for (AbstractOrderEntryModel orderEntry : orderEntryList) {
				if (orderEntry instanceof AbstractOrderEntryModel) {
					List<ColumnGroupConfiguration> columnConfigurations = (List<ColumnGroupConfiguration>) getMasterColumns();
					if (CollectionUtils.isNotEmpty(columnConfigurations)) {
						for (ColumnGroupConfiguration col : columnConfigurations) {
							Listheader headerLabel = new Listheader(
									(col instanceof DefaultColumnGroupConfiguration) ? ((DefaultColumnGroupConfiguration) col)
											.getLabelWithFallback() : col
											.getLabel());
							headerLabel.setTooltip(col.getName());
							headerLabel.setSclass("invoiceHeaderLabel");
							tableHeader.appendChild(headerLabel);
						}
					}
					Listheader headerLabel3 = new Listheader("Delivery Charges");
					headerLabel3.setTooltip("Delivery Charges");
					headerLabel3.setSclass("invoiceHeaderLabel");
					tableHeader.appendChild(headerLabel3);
					Listheader headerLabel1 = new Listheader("Reason");
					headerLabel1.setTooltip("Reason");
					headerLabel1.setSclass("invoiceHeaderLabel");
					tableHeader.appendChild(headerLabel1);
					Listheader headerLabel2 = new Listheader("Notes");
					headerLabel2.setTooltip("Notes");
					headerLabel2.setSclass("invoiceHeaderLabel");
					tableHeader.appendChild(headerLabel2);
					tableVbox.appendChild(tableHeader);
					buttonFlag = true;
					break;
				}
			}
			for (final AbstractOrderEntryModel orderEntry : orderEntryList) {
				if (orderEntry instanceof AbstractOrderEntryModel) {
					resultFlag = true;
					Listitem tableRow = new Listitem();
					Listcell rowLabel = new Listcell();
					final Checkbox refundEntryChkBox = new Checkbox();
					refundEntryChkBox.setId(orderEntry.getTransactionID());
					refundEntryChkBox.addEventListener(Events.ON_CHECK,
							new EventListener() {
								@Override
								public void onEvent(Event arg0)
										throws Exception {
									if (orderEntry != null) {
										if (refundEntryChkBox.isChecked()) {
											RefundDeliveryData data = refundMap
													.get(orderEntry);
											if (data == null) {
												data = new RefundDeliveryData();
											}
											data.setAbstractOrderEntryModel(orderEntry);
											data.setChecked(Boolean.TRUE);
											refundMap.put(orderEntry, data);
										} else {
											refundMap.remove(orderEntry);
										}
									}
								}
							});
					rowLabel.appendChild(refundEntryChkBox);
					tableRow.appendChild(rowLabel);
					createDataRow(getCockpitTypeService().wrapItem(orderEntry),
							tableRow);
					Listcell rowLabel3 = new Listcell();
					String deliveryChargesString = (null != orderEntry
							.getCurrDelCharge()) ? currencyInstance
							.format(orderEntry.getCurrDelCharge()) : "";
					final Label deliveryChargesStringLabel = new Label(
							deliveryChargesString);
					rowLabel3.appendChild(deliveryChargesStringLabel);
					tableRow.appendChild(rowLabel3);
					Listcell rowLabel1 = new Listcell();
					final Listbox reasonBox = new Listbox();
					reasonBox.setMultiple(false);
					reasonBox.setMold("select");
					reasonBox.setAttribute("transaction", orderEntry);
					reasonBox.addEventListener(Events.ON_SELECT,
							new EventListener() {
								@Override
								public void onEvent(Event paramEvent)
										throws Exception {
									AbstractOrderEntryModel data = (AbstractOrderEntryModel) reasonBox
											.getAttribute("transaction");
									;
									RefundDeliveryData refundDeliveryData = refundMap
											.get(data);
									if (refundDeliveryData == null) {
										refundDeliveryData = new RefundDeliveryData();
									}
									if (null != reasonBox.getSelectedItem()
											.getValue()) {
										refundDeliveryData.setReason(reasonBox
												.getSelectedItem().getValue()
												.toString());
									} else {
										refundDeliveryData.setReason("");
									}
									refundMap.put(orderEntry,
											refundDeliveryData);
								}
							});
					populateReasonDropDown(reasonBox);
					rowLabel1.appendChild(reasonBox);
					tableRow.appendChild(rowLabel1);
					Listcell rowLabel2 = new Listcell();
					final Textbox notesTextBox = new Textbox();
					notesTextBox.setAttribute("transaction", orderEntry);
					notesTextBox.addEventListener(Events.ON_BLUR,
							new EventListener() {
								@Override
								public void onEvent(Event paramEvent)
										throws Exception {
									AbstractOrderEntryModel data = (AbstractOrderEntryModel) notesTextBox
											.getAttribute("transaction");
									;
									RefundDeliveryData refundDeliveryData = refundMap
											.get(data);
									if (refundDeliveryData == null) {
										refundDeliveryData = new RefundDeliveryData();
									}
									refundDeliveryData.setNotes(notesTextBox
											.getValue().toString());
									refundMap.put(orderEntry,
											refundDeliveryData);
								}
							});
					rowLabel2.appendChild(notesTextBox);
					tableRow.appendChild(rowLabel2);
					tableVbox.appendChild(tableRow);
				}
			}
		}
		if (!resultFlag) {
			parentVbox.appendChild(new Label(
					"No Request Available for Refund Delivery Charges"));
		}
		parentVbox.appendChild(tableVbox);
		parentComponent.appendChild(parentVbox);
		return buttonFlag;
	}

	private void populateReasonDropDown(final Listbox reasonBox) {
		List<RefundReason> refundReasonList = enumerationService
				.getEnumerationValues(RefundReason.class);
		Listitem emptyItem = new Listitem("", null);
		emptyItem.setParent(reasonBox);
		emptyItem.setSelected(true);
		for (RefundReason reason : refundReasonList) {
			String objectTextLabel = TypeTools.getEnumName(reason);
			Listitem listItem = reasonBox.appendItem(objectTextLabel,
					objectTextLabel);
			listItem.setValue(reason);
		}
	}

	private void createDataRow(TypedObject entryData, Listitem row) {
		List<ColumnConfiguration> columnConfigurations = getColumnConfigurations();
		if (CollectionUtils.isNotEmpty(columnConfigurations)) {
			for (ColumnConfiguration col : columnConfigurations) {
				Listcell rowLabel = new Listcell(ObjectGetValueUtils.getValue(
						col.getValueHandler(), entryData));
				row.appendChild(rowLabel);
			}
		}
	}
}