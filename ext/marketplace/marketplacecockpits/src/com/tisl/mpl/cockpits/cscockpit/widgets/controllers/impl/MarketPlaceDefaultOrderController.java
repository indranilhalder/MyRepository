/**
 * 
 */
package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.data.RefundDeliveryData;
import com.tisl.mpl.cockpits.cscockpit.services.MarketPlaceOrderSearchServices;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceOrderController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.model.InvoiceDetailModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.register.RegisterCustomerFacade;
import com.tisl.mpl.facades.product.data.SendInvoiceData;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cscockpit.widgets.controllers.CallContextController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultOrderController;
import de.hybris.platform.orderhistory.OrderHistoryService;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.FormatFactory;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;

/**
 * @author 1006687
 *
 */
public class MarketPlaceDefaultOrderController extends DefaultOrderController
		implements MarketPlaceOrderController {

	@Autowired
	private MarketPlaceOrderSearchServices marketPlaceOrderSearchServices;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplJusPayRefundService mplJusPayRefundService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private OrderHistoryService orderHistoryService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private FormatFactory formatFactory;

	@Autowired
	private RegisterCustomerFacade registerCustomerFacade;

	private static final Logger LOG = Logger
			.getLogger(MarketPlaceDefaultOrderController.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.cockpits.cscockpit.widgets.controllers.
	 * MarketPlaceOrderController#getAssociatedOrders()
	 */
	@Override
	public List<TypedObject> getAssociatedOrders() {
		TypedObject typedObject = getCallContextController().getCurrentOrder();
		if (typedObject != null && typedObject.getObject() != null
				&& typedObject.getObject() instanceof OrderModel) {
			OrderModel currentOrder = (OrderModel) typedObject.getObject();
			Collection<OrderModel> orders = marketPlaceOrderSearchServices
					.getAssociatedOrders(currentOrder.getCode());
			return getCockpitTypeService().wrapItems(orders);
		}

		return Collections.emptyList();
	}

	@Override
	public CallContextController getContextController() {
		return getCallContextController();
	}

	@Override
	public List<TypedObject> getPreviousOrders() {
		TypedObject typedObject = getCallContextController()
				.getCurrentCustomer();
		if (typedObject != null && typedObject.getObject() != null
				&& typedObject.getObject() instanceof CustomerModel) {
			CustomerModel currentCustomer = (CustomerModel) typedObject
					.getObject();

			Collection<OrderModel> oldOrders = new ArrayList<OrderModel>(
					currentCustomer.getOrders());

			if (CollectionUtils.isNotEmpty(oldOrders)) {
				final Calendar cal = Calendar.getInstance();
				int monthThreshold = configurationService.getConfiguration()
						.getInt("cscockpit.order.invoice.threshold.months")
						* -1;
				cal.add(Calendar.MONTH, monthThreshold);

				CollectionUtils.filter(oldOrders, new Predicate() {

					@Override
					public boolean evaluate(Object obj) {
						OrderModel temp = (OrderModel) obj;
						return cal.before(temp.getCreationtime())
								|| cal.equals(temp.getCreationtime());

					}
				});
				return getCockpitTypeService().wrapItems(oldOrders);
			}
		}
		return Collections.emptyList();
	}

	@Override
	public String doRefundPayment(List<OrderEntryModel> orderEntryModel) {
		Double totalRefundAmount = 0d;
		PaymentTransactionModel paymentTransactionModel = null;
		for (OrderEntryModel orderEntry : orderEntryModel) {
			totalRefundAmount += orderEntry.getNetAmountAfterAllDisc();
		}
		final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
		try {
			paymentTransactionModel = mplJusPayRefundService.doRefund(
					orderEntryModel.get(0).getOrder(), totalRefundAmount,
					PaymentTransactionType.RETURN,uniqueRequestId);
			if (null != paymentTransactionModel) {
				mplJusPayRefundService.attachPaymentTransactionModel(
						orderEntryModel.get(0).getOrder(),
						paymentTransactionModel);
				for (OrderEntryModel orderEntry : orderEntryModel) {

					// If CosignmentEnteries are present then update OMS with
					// the state.
					ConsignmentStatus newStatus = null;
					if (orderEntry != null
							&& CollectionUtils.isNotEmpty(orderEntry
									.getConsignmentEntries())) {
						// ConsignmentModel consignmentModel = orderEntry
						// .getConsignmentEntries().iterator().next()
						// .getConsignment();
						if (StringUtils.equalsIgnoreCase(
								paymentTransactionModel.getStatus(), "SUCCESS")) {
							newStatus = ConsignmentStatus.RETURN_COMPLETED;
						} else if (StringUtils.equalsIgnoreCase(
								paymentTransactionModel.getStatus(), "PENDING")) {
							newStatus = ConsignmentStatus.REFUND_INITIATED;
							RefundTransactionMappingModel refundTransactionMappingModel = getModelService()
									.create(RefundTransactionMappingModel.class);
							refundTransactionMappingModel
									.setRefundedOrderEntry(orderEntry);
							refundTransactionMappingModel
									.setJuspayRefundId(paymentTransactionModel
											.getCode());
							refundTransactionMappingModel
									.setCreationtime(new Date());
							refundTransactionMappingModel
									.setRefundType(JuspayRefundType.RETURN);
							getModelService().save(
									refundTransactionMappingModel);
						} else {
							newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
						}
						// getModelService().save(consignmentModel);
						mplJusPayRefundService.makeRefundOMSCall(orderEntry,
								paymentTransactionModel,
								orderEntry.getNetAmountAfterAllDisc(),
								newStatus);
					}
				}
			} else {
				
				//TISSIT-1801
				LOG.error("Manual Refund Failed");
				for (OrderEntryModel orderEntry : orderEntryModel) {
					mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_IN_PROGRESS);
				}
				
				paymentTransactionModel = mplJusPayRefundService
						.createPaymentTransactionModel(orderEntryModel.get(0).getOrder(), "FAILURE", totalRefundAmount,
								PaymentTransactionType.RETURN, "NO Response FROM PG", uniqueRequestId);
				mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);
				//TISSIT-1801
				
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			
			// TISSIT-1784 Code addition started
			for (OrderEntryModel orderEntry : orderEntryModel) {
				mplJusPayRefundService.makeRefundOMSCall(orderEntry,paymentTransactionModel,orderEntry.getNetAmountAfterAllDisc(),ConsignmentStatus.REFUND_INITIATED);

				// Making RTM entry to be picked up by webhook job	
				RefundTransactionMappingModel refundTransactionMappingModel = getModelService().create(RefundTransactionMappingModel.class);
				refundTransactionMappingModel.setRefundedOrderEntry(orderEntry);
				refundTransactionMappingModel.setJuspayRefundId(uniqueRequestId);
				refundTransactionMappingModel.setCreationtime(new Date());
				refundTransactionMappingModel.setRefundType(JuspayRefundType.RETURN);
				getModelService().save(refundTransactionMappingModel);
			}
			// TISSIT-1784 Code addition ended
			
			paymentTransactionModel = mplJusPayRefundService
					.createPaymentTransactionModel(orderEntryModel.get(0).getOrder(), "FAILURE", totalRefundAmount,
							PaymentTransactionType.RETURN, "FAILURE", uniqueRequestId);
			mplJusPayRefundService.attachPaymentTransactionModel(orderEntryModel.get(0).getOrder(), paymentTransactionModel);
			
		}
		String result = paymentTransactionModel.getStatus() + ","
				+ paymentTransactionModel.getCode() + "," + totalRefundAmount;
		return result;
	}

	@Override
	public TypedObject createRefundDeliveryChargesRequest(
			OrderModel orderModel,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap) {
		PaymentTransactionModel paymentTransactionModel = null;
		Double totalRefundDeliveryCharges = Double.valueOf(0);
		if (MapUtils.isNotEmpty(refundMap)) {
			final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
			
			try {
				for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
						.entrySet()) {
					totalRefundDeliveryCharges = totalRefundDeliveryCharges
							+ refundEntry.getKey().getCurrDelCharge();

					refundEntry.getKey().setRefundedDeliveryChargeAmt(
							refundEntry.getKey().getCurrDelCharge());
					refundEntry.getKey().setCurrDelCharge(Double.valueOf(0));
					modelService.save(refundEntry.getKey());

				}
				paymentTransactionModel = mplJusPayRefundService.doRefund(
						orderModel, totalRefundDeliveryCharges,
						PaymentTransactionType.REFUND_DELIVERY_CHARGES,uniqueRequestId);
				if (null != paymentTransactionModel) {
					mplJusPayRefundService.attachPaymentTransactionModel(
							orderModel, paymentTransactionModel);
					if ("PENDING".equalsIgnoreCase(paymentTransactionModel
							.getStatus())) {
						RefundTransactionMappingModel refundTransactionMappingModel = getModelService()
								.create(RefundTransactionMappingModel.class);
						refundTransactionMappingModel
								.setRefundedOrderEntry(orderModel.getEntries()
										.iterator().next());
						refundTransactionMappingModel
								.setJuspayRefundId(paymentTransactionModel
										.getCode());
						refundTransactionMappingModel
								.setCreationtime(new Date());
						refundTransactionMappingModel
								.setRefundType(JuspayRefundType.REFUND_DELIVERY_CHARGE);
						getModelService().save(refundTransactionMappingModel);
					}
					for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
							.entrySet()) {
						mplJusPayRefundService.makeRefundOMSCall(refundEntry
								.getKey(), paymentTransactionModel, refundEntry
								.getKey().getRefundedDeliveryChargeAmt(), null);// Sending
																				// null
																				// as
																				// for
																				// refund
																				// delivery
																				// charge
																				// no
																				// status
																				// update
																				// is
																				// required.
					}
				} else {

					LOG.error("Refund Delivery Charges Failed");
				}

			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				paymentTransactionModel = mplJusPayRefundService
						.createPaymentTransactionModel(orderModel, "FAILURE",
								totalRefundDeliveryCharges,
								PaymentTransactionType.REFUND_DELIVERY_CHARGES,
								"FAILURE", uniqueRequestId);
				mplJusPayRefundService.attachPaymentTransactionModel(
						orderModel, paymentTransactionModel);
			}
		}
		return getCockpitTypeService().wrapItem(paymentTransactionModel);
	}

	@Override
	public TypedObject createOrderHistoryRequest(OrderModel orderModel,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap,
			Double totalRefundDeliveryCharges) {
		OrderModel snapshot = orderHistoryService
				.createHistorySnapshot(orderModel);
		final CurrencyModel cartCurrencyModel = orderModel.getCurrency();
		final NumberFormat currencyInstance = (NumberFormat) sessionService
				.executeInLocalView(new SessionExecutionBody() {
					public Object execute() {
						commonI18NService.setCurrentCurrency(cartCurrencyModel);
						return formatFactory.createCurrencyFormat();
					}
				});
		for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
				.entrySet()) {
			AbstractOrderEntryModel refundEntryModel = refundEntry.getKey();
			// refundEntryModel.setCurrDelCharge(Double.valueOf(0));
			OrderHistoryEntryModel entry = modelService
					.create(OrderHistoryEntryModel.class);
			entry.setTimestamp(new Date());
			entry.setOrder(orderModel);
			String desc = "Refunded Delivery Charges "
					+ currencyInstance.format(totalRefundDeliveryCharges)
					+ " for Transaction Id: "
					+ refundEntry.getKey().getTransactionID() + "- Reason: "
					+ refundEntry.getValue().getReason();
			if (StringUtils.isNotEmpty(refundEntry.getValue().getNotes())) {
				desc = desc + ", Notes: " + refundEntry.getValue().getNotes();
			}
			entry.setDescription(desc);
			entry.setPreviousOrderVersion(snapshot);
			modelService.saveAll(orderModel, refundEntryModel);
			modelService.saveAll(orderModel, entry);
		}
		return getCockpitTypeService().wrapItem(orderModel);
	}

	@Override
	public boolean isOrderCODforManualRefund(OrderModel order,
			List<OrderEntryModel> orderEntryModel) {
		List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(
				order.getPaymentTransactions());
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(tranactions)) {
			for (PaymentTransactionModel transaction : tranactions) {
				if (CollectionUtils.isNotEmpty(transaction.getEntries())) {
					for (PaymentTransactionEntryModel entry : transaction
							.getEntries()) {
						if (entry.getPaymentMode() != null
								&& entry.getPaymentMode().getMode() != null
								&& entry.getPaymentMode().getMode()
										.equalsIgnoreCase("COD")) {
							flag = true;
							break;
						}
					}
				}
				if (flag) {
					break;
				}
			}
		}
		if (flag) {
			Double totalRefundAmount = 0d;

			for (OrderEntryModel orderEntry : orderEntryModel) {
				ConsignmentModel consignment = orderEntry
						 .getConsignmentEntries().iterator().next()
						 .getConsignment();
				if (consignment.getDeliveryDate() != null) {
					totalRefundAmount += orderEntry.getNetAmountAfterAllDisc();
				}
			}

			PaymentTransactionModel paymentTransactionModel = null;

			if (totalRefundAmount > 0D) {
				paymentTransactionModel = mplJusPayRefundService
						.createPaymentTransactionModel(order, "FAILURE",
								totalRefundAmount,
								PaymentTransactionType.RETURN, "FAILURE", UUID
										.randomUUID().toString());
				mplJusPayRefundService.attachPaymentTransactionModel(order,
						paymentTransactionModel);
			}
			for (OrderEntryModel orderEntry : orderEntryModel) {
				ConsignmentStatus newStatus = null;
				
				 ConsignmentModel consignment = orderEntry
				 .getConsignmentEntries().iterator().next()
				 .getConsignment();
				 
				if (consignment.getDeliveryDate() != null) {
					newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
				} else {
					newStatus = ConsignmentStatus.COD_CLOSED_WITHOUT_REFUND;
				}
				// Do not save status updates in commerce, rather update it on
				// OMS and let it come in sync.
				// modelService.save(consignment);

				if (paymentTransactionModel != null) {
					mplJusPayRefundService.makeRefundOMSCall(orderEntry,
							paymentTransactionModel,
							orderEntry.getNetAmountAfterAllDisc(), newStatus);
				}
				mplJusPayRefundService.makeOMSStatusUpdate(orderEntry,
						newStatus);

			}

			return true;
		}
		return false;
	}

	@Override
	public Map<String, List<TypedObject>> getOrderEntriesGroupByAWB(
			OrderModel orderModel) {
		if (orderModel != null) {
			List<AbstractOrderEntryModel> orderEntries = orderModel
					.getEntries();
			if (CollectionUtils.isNotEmpty(orderEntries)) {
				Map<String, List<TypedObject>> results = new HashMap<>();
				for (AbstractOrderEntryModel entry : orderEntries) {
					ConsignmentModel consignmentModel = getConsignment(entry);
					if (MarketplaceCockpitsConstants.validInvoiceStatus.contains(consignmentModel.getStatus())) {
						String awbNumber = consignmentModel != null ? consignmentModel
								.getTrackingID() : null;
						if (StringUtils.isNotBlank(awbNumber)) {
							List<TypedObject> entries = results.get(awbNumber);
							if (entries == null) {
								entries = new ArrayList<TypedObject>();
								results.put(awbNumber, entries);
							}
							entries.add(getCockpitTypeService().wrapItem(entry));

						}
					}
				}
				return results;
			}

		}
		return null;
	}

	protected ConsignmentModel getConsignment(
			AbstractOrderEntryModel orderEntryModel) {
		if (CollectionUtils.isNotEmpty(orderEntryModel.getConsignmentEntries())) {
			ConsignmentEntryModel consignmentEntry = orderEntryModel
					.getConsignmentEntries().iterator().next();
			return consignmentEntry.getConsignment();
		}
		return null;
	}

	@Override
	public boolean sendInvoice(List<TypedObject> orderLineId, String orderId) {
		String customerEmail = null;

		CustomerModel customerModel = (CustomerModel) getCallContextController()
				.getCurrentCustomer().getObject();
		customerEmail = customerModel.getOriginalUid();

		if (CollectionUtils.isNotEmpty(orderLineId)) {
			for (TypedObject id : orderLineId) {
				if (id != null && id.getObject() != null
						&& id.getObject() instanceof AbstractOrderEntryModel) {
					AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) id
							.getObject();
					ConsignmentModel consignmet = getConsignment(orderEntry);
					if (consignmet != null) {
						SendInvoiceData invoiceData = new SendInvoiceData();
						invoiceData.setCustomerEmail(customerEmail);
						InvoiceDetailModel invoiceDetails = consignmet
								.getInvoice();
						if (invoiceDetails != null) {
							invoiceData.setInvoiceUrl(invoiceDetails
									.getInvoiceUrl());
						}
						invoiceData
								.setLineItemId(orderEntry.getTransactionID());
						invoiceData
						.setTransactionId(orderEntry.getTransactionID());
						invoiceData.setOrdercode(orderId);
						try {
							registerCustomerFacade.sendInvoice(invoiceData,
									customerModel);
							LOG.info("Invoice Sending Succesfull");
							return true;
						} catch (EtailNonBusinessExceptions e) {
							LOG.error(e.getMessage(), e);
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isOrderCODforDeliveryCharges(OrderModel order,
			Map<AbstractOrderEntryModel, RefundDeliveryData> refundMap) {
		List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(
				order.getPaymentTransactions());
		boolean flag = false;
		if (CollectionUtils.isNotEmpty(tranactions)) {
			for (PaymentTransactionModel transaction : tranactions) {
				if (CollectionUtils.isNotEmpty(transaction.getEntries())) {
					for (PaymentTransactionEntryModel entry : transaction
							.getEntries()) {
						if (entry.getPaymentMode() != null
								&& entry.getPaymentMode().getMode() != null
								&& entry.getPaymentMode().getMode()
										.equalsIgnoreCase("COD")) {
							flag = true;
							break;
						}
					}
				}
				if (flag) {
					break;
				}
			}
		}
		if (flag) {
			Double totalRefundDeliveryCharges = Double.valueOf(0);
			if (MapUtils.isNotEmpty(refundMap)) {
				for (Map.Entry<AbstractOrderEntryModel, RefundDeliveryData> refundEntry : refundMap
						.entrySet()) {
					totalRefundDeliveryCharges = totalRefundDeliveryCharges
							+ refundEntry.getKey().getCurrDelCharge();

					refundEntry.getKey().setRefundedDeliveryChargeAmt(
							refundEntry.getKey().getCurrDelCharge());
					refundEntry.getKey().setCurrDelCharge(Double.valueOf(0));
					modelService.save(refundEntry.getKey());
				}
			}
			PaymentTransactionModel paymentTransactionModel = mplJusPayRefundService
					.createPaymentTransactionModel(order, "FAILURE",
							totalRefundDeliveryCharges,
							PaymentTransactionType.REFUND_DELIVERY_CHARGES,
							"FAILURE", UUID.randomUUID().toString());
			mplJusPayRefundService.attachPaymentTransactionModel(order,
					paymentTransactionModel);
			return true;
		}
		return false;
	}
}
