package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.cscockpit.utilities.CodeMasterUtility;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceReturnsController;
import com.tisl.mpl.core.enums.TypeofReturn;
import com.tisl.mpl.marketplacecommerceservices.service.MPLReturnService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.service.MplOrderCancelClientService;
import com.tisl.mpl.service.ReturnLogisticsService;
import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest.OrderLine;
import com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse;
import com.tisl.mpl.xml.pojo.OrderLineDataResponse;
import com.tisl.mpl.xml.pojo.ReturnLogisticsResponse;

import de.hybris.platform.basecommerce.enums.ReplacementReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.widgets.InputWidget;
import de.hybris.platform.cockpit.widgets.models.impl.DefaultListWidgetModel;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.utils.SafeUnbox;
import de.hybris.platform.cscockpit.widgets.controllers.ReturnsController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultReturnsController;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.returns.OrderReturnException;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.returns.model.ReplacementOrderModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

public class MarketPlaceDefaultReturnsController extends
		DefaultReturnsController implements MarketPlaceReturnsController {

	@Autowired
	private MPLReturnService mplreturnService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ReturnLogisticsService returnLogisticsService;

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private MplOrderCancelClientService mplOrderCancelClientService;

	private static final String OMS_BYPASS_KEY = "cscockpit.oms.serviceability.check.bypass";

	private static final Logger LOG = Logger
			.getLogger(DefaultReturnsController.class);

	public Map<Boolean, List<OrderLineDataResponse>> validateReverseLogistics(
			List<ReturnLogistics> returnLogisticsList) {

		final Map<Boolean, List<OrderLineDataResponse>> responseMap = new HashMap();
		ReturnLogisticsResponse returnLogisticsResponse = returnLogisticsService
				.returnLogisticsCheck(returnLogisticsList);

		if (null != returnLogisticsResponse
				&& CollectionUtils.isNotEmpty(returnLogisticsResponse
						.getOrderlines())) {

			List<OrderLineDataResponse> orderEntryList = returnLogisticsResponse
					.getOrderlines();
			List<OrderLineDataResponse> orderEntryListNew = new ArrayList<OrderLineDataResponse>(
					returnLogisticsResponse.getOrderlines());

			if (CollectionUtils.isNotEmpty(orderEntryList)) {
				CollectionUtils.filter(orderEntryList, new Predicate() {
					@Override
					public boolean evaluate(Object o) {
						OrderLineDataResponse entry = (OrderLineDataResponse) o;
						return entry.getIsReturnLogisticsAvailable()
								.equals("Y");
					}
				});
			}
			if (CollectionUtils.isNotEmpty(orderEntryList)) {
				responseMap.put(Boolean.TRUE, orderEntryList);
			}

			if (CollectionUtils.isNotEmpty(orderEntryListNew)) {
				CollectionUtils.filter(orderEntryListNew, new Predicate() {
					@Override
					public boolean evaluate(Object o) {
						OrderLineDataResponse entry = (OrderLineDataResponse) o;
						return entry.getIsReturnLogisticsAvailable()
								.equals("N");
					}
				});
			}
			if (CollectionUtils.isNotEmpty(orderEntryListNew)) {
				responseMap.put(Boolean.FALSE, orderEntryListNew);
			}
		}
		return responseMap;
	}

	@Override
	public TypedObject createReplacementRequest(
			List<ObjectValueContainer> replacementEntriesValueContainers) {
		List<ReturnLogistics> returnLogisticsList = new ArrayList();
		ReturnRequestModel request = null;
		ReplacementOrderModel replacementOrder = null;
		List<ReplacementEntryModel> returnEntries = new ArrayList();

		ModelService modelService = getModelService();
		Map returnableOrderEntries;
		try {
			if (!(validateCreateReplacementRequest(replacementEntriesValueContainers)))
				return null;
			request = getReturnService().createReturnRequest(getOrderModel());
			String rmaString = getReturnService().createRMA(request);
			request.setRMA(rmaString);
			returnableOrderEntries = getReturnableOrderEntries();
			for (ObjectValueContainer ovc : replacementEntriesValueContainers) {
				TypedObject orderEntry = (TypedObject) ovc.getObject();
				if (!(returnableOrderEntries.containsKey(orderEntry)))
					continue;
				Long expectedQty = (Long) getPropertyValue(ovc,
						"ReturnEntry.expectedQuantity").getCurrentValue();
				ReplacementReason reason = (ReplacementReason) getPropertyValue(
						ovc, "ReplacementEntry.reason").getCurrentValue();
				ReturnAction action = (ReturnAction) getPropertyValue(ovc,
						"ReturnEntry.action").getCurrentValue();
				String notes = (String) getPropertyValue(ovc,
						"ReturnEntry.notes").getCurrentValue();
				if ((SafeUnbox.toLong(expectedQty) <= 0L)
						|| (SafeUnbox.toLong(expectedQty) > SafeUnbox
								.toLong((Long) returnableOrderEntries
										.get(orderEntry))) || (reason == null)
						|| (action == null))
					continue;
				ReplacementEntryModel replacementEntry = mplreturnService
						.createReplacement(request,
								(OrderEntryModel) orderEntry.getObject(),
								notes, expectedQty, action, reason);
				returnEntries.add(replacementEntry);

				ReturnLogistics returnLogistics = new ReturnLogistics();
				returnLogistics.setOrderId(replacementEntry.getOrderEntry()
						.getOrder().getCode());
				returnLogistics.setTransactionId(replacementEntry
						.getOrderEntry().getTransactionID());
				returnLogisticsList.add(returnLogistics);
			}

			boolean isOMSBypass = configurationService.getConfiguration()
					.getBoolean(OMS_BYPASS_KEY);
			if (!isOMSBypass) {
				returnCalltoOMS(request, null);
			}
			modelService.saveAll(returnEntries);
			replacementOrder = getReturnService().createReplacementOrder(
					request);
			mplreturnService.addReplacementOrderEntries(replacementOrder,
					returnEntries);

			Map<Boolean, List<OrderLineDataResponse>> responseMap = validateReverseLogistics(returnLogisticsList);

			if (CollectionUtils.isEmpty(responseMap.get(Boolean.FALSE))) {
				request.setTypeofreturn(TypeofReturn.REVERSE_PICKUP);
			} else {
				request.setTypeofreturn(TypeofReturn.SELF_COURIER);
			}

			request.setReturnRaisedFrom(SalesApplication.CALLCENTER);

			modelService.save(request);

			return getCockpitTypeService().wrapItem(request);
		} catch (Exception localException) {
			for (ReturnEntryModel returnEntryModel : returnEntries) {
				modelService.remove(returnEntryModel);
			}
			if (request != null) {
				modelService.remove(request);
			}
			if (replacementOrder != null) {
				modelService.remove(replacementOrder);
			}
		}
		return null;
	}

	@Override
	public TypedObject createRefundRequest() {
		try {

			List<ReturnLogistics> returnLogisticsList = new ArrayList();
			ModelService modelService = getModelService();
			OrderModel orderModel = getOrderModel();
			ReturnRequestModel refundRequest = getReturnService()
					.createReturnRequest(orderModel);

			boolean isOMSBypass = configurationService.getConfiguration()
					.getBoolean(OMS_BYPASS_KEY);
			if (!isOMSBypass) {
				returnCalltoOMS(refundRequest, this.refundDetailsList);
			}

			String rmaString = getReturnService().createRMA(refundRequest);
			refundRequest.setRMA(rmaString);

			applyRefunds(orderModel, refundRequest, this.refundDetailsList,
					true);

			TypedObject refundRequestObject = getCockpitTypeService().wrapItem(
					refundRequest);

			for (AbstractOrderEntryModel orderEntry : orderModel.getEntries()) {
				ReturnLogistics returnLogistics = new ReturnLogistics();
				returnLogistics.setOrderId(orderEntry.getOrder().getCode());
				returnLogistics.setTransactionId(orderEntry.getTransactionID());
				returnLogisticsList.add(returnLogistics);
			}

			Map<Boolean, List<OrderLineDataResponse>> responseMap = validateReverseLogistics(returnLogisticsList);

			if (CollectionUtils.isEmpty(responseMap.get(Boolean.FALSE))) {
				refundRequest.setTypeofreturn(TypeofReturn.REVERSE_PICKUP);
			} else {
				refundRequest.setTypeofreturn(TypeofReturn.SELF_COURIER);
			}

			refundRequest.setReturnRaisedFrom(SalesApplication.CALLCENTER);

			modelService.save(refundRequest);

			this.refundDetailsList.clear();
			this.refundDetailsList = null;
			deleteRefundOrderPreview();

			return refundRequestObject;
		} catch (Exception e) {
			LOG.error("Failed to create refund request", e);
		}

		return null;
	}

	@Override
	public String getSellerName(String ussid) {
		SellerInformationModel sellerinfoModel = getModelService().create(
				SellerInformationModel.class);
		sellerinfoModel.setSellerArticleSKU(ussid);
		try {
			List<SellerInformationModel> lst = flexibleSearchService
					.getModelsByExample(sellerinfoModel);
			return lst.get(0).getSellerName();
		} catch (ModelNotFoundException e) {
			LOG.error(e.getMessage(), e);
			return StringUtils.EMPTY;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return StringUtils.EMPTY;
		}
	}

	public List<ReturnLogistics> getReturnLogisticsList(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<ObjectValueContainer> returnObjectValueContainers) {
		Map returnableOrderEntries = ((ReturnsController) widget
				.getWidgetController()).getReturnableOrderEntries();
		List<ReturnLogistics> returnLogisticsList = new ArrayList();
		for (ObjectValueContainer ovc : returnObjectValueContainers) {
			TypedObject orderEntry = (TypedObject) ovc.getObject();
			AbstractOrderEntryModel entry = (OrderEntryModel) orderEntry
					.getObject();
			ObjectValueContainer.ObjectValueHolder expectedQty = getPropertyValue(
					ovc, "ReturnEntry.expectedQuantity");
			if ((expectedQty != null)
					&& (expectedQty.getCurrentValue() instanceof Long)) {
				long expectedQtyValue = SafeUnbox.toLong((Long) expectedQty
						.getCurrentValue());
				if (expectedQtyValue != 0L) {
					if (expectedQtyValue < 0L) {
						break;
					}
					if ((expectedQtyValue > 0L)
							&& (SafeUnbox.toLong((Long) returnableOrderEntries
									.get(orderEntry)) < expectedQtyValue)) {
						break;
					}
				}
				ReturnLogistics returnLogistics = new ReturnLogistics();
				returnLogistics.setOrderId(((OrderModel) (entry.getOrder()))
						.getParentReference().getCode());
				returnLogistics.setTransactionId(entry.getTransactionID());
				returnLogisticsList.add(returnLogistics);
			}
		}
		return returnLogisticsList;
	}

	private void returnCalltoOMS(ReturnRequestModel returnRequest,
			Map<Long, RefundDetails> refundDetailsList)
			throws OrderReturnException {
		try {
			if (null != returnRequest) {
				Set<ConsignmentEntryModel> consignmentEntryModels = returnRequest
						.getOrder().getConsignments().iterator().next()
						.getConsignmentEntries();
				if (CollectionUtils.isNotEmpty(consignmentEntryModels)) {
					MplCancelOrderRequest request = new MplCancelOrderRequest();
					AbstractOrderModel orderModel = null;
					OrderLine orderLine = null;
					String reason = null;
					String notes = null;
					List<ReturnEntryModel> returnEntries = returnRequest
							.getReturnEntries();
					if (CollectionUtils.isNotEmpty(returnEntries)) {
						for (ReturnEntryModel returnEntry : returnEntries) {
							orderLine = new OrderLine();
							orderModel = ((OrderModel) (returnEntry
									.getOrderEntry().getOrder()))
									.getParentReference();
							orderLine.setOrderId(orderModel.getCode());
							orderLine.setTransactionId(returnEntry
									.getOrderEntry().getTransactionID());
							orderLine.setReturnCancelFlag("R");
							orderLine.setRequestID(returnRequest.getCode());
							reason = CodeMasterUtility
									.getglobalCode(((ReplacementEntryModel) returnEntry)
											.getReason().getCode());
							notes = ((ReplacementEntryModel) returnEntry)
									.getNotes();
							orderLine.setReasonCode(reason);
							orderLine.setReturnCancelRemarks(notes);
							request.getOrderLine().add(orderLine);

						}

					} else {
						for (Map.Entry<Long, RefundDetails> refundDetail : refundDetailsList
								.entrySet()) {
							AbstractOrderEntryModel orderEntryModel = getOrderEntryByEntryNumber(
									returnRequest.getOrder(),
									refundDetail.getKey());

							orderLine = new OrderLine();
							orderModel = ((OrderModel) (orderEntryModel
									.getOrder())).getParentReference();
							orderLine.setOrderId(orderModel.getCode());
							orderLine.setTransactionId(orderEntryModel
									.getTransactionID());
							orderLine.setReturnCancelFlag("R");
							orderLine.setRequestID(returnRequest.getCode());
							reason = CodeMasterUtility
									.getglobalCode(refundDetail.getValue()
											.getReason().getCode());
							notes = refundDetail.getValue().getNotes();
							orderLine.setReasonCode(reason);
							orderLine.setReturnCancelRemarks(notes);
							request.getOrderLine().add(orderLine);

						}
					}

					MplOrderIsCancellableResponse response = mplOrderCancelClientService
							.orderCancelDataToOMS(request);
					if (null == response) {
					getModelService().remove(returnRequest);//Remove if created any 
						throw new OrderReturnException(orderModel.getCode(),
								"Item is not returnable at OMS.");
					}
				}
			}
		} catch (Exception ex) {
			throw new OrderReturnException("Failed", "OMS is not responding");
		}
	}

	@Override
	public boolean isFreebieAvaialble(
			List<ObjectValueContainer> orderEntryReturnsRecords) {
		Set<String> freebie = new HashSet<>();
		Set<String> freebieSelected = new HashSet<>();
		for (ObjectValueContainer ovc : orderEntryReturnsRecords) {
			TypedObject typedObject = (TypedObject) ovc.getObject();
			freebie.clear();
			ObjectValueContainer.ObjectValueHolder expectedQty = getPropertyValue(
					ovc, "ReturnEntry.expectedQuantity");
			AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) typedObject
					.getObject();
			
			for(AbstractOrderEntryModel e : orderEntry.getOrder().getEntries()){
				if(e.getGiveAway() || e.getIsBOGOapplied()){
					for(String id : e.getParentTransactionID().split(",")){
						freebie.add(id);
					}
					freebie.add(e.getTransactionID());
				}
			}
			if ((expectedQty != null)
					&& (expectedQty.getCurrentValue() instanceof Long)) {
				long expectedQtyValue = SafeUnbox.toLong((Long) expectedQty
						.getCurrentValue());
				
				if (expectedQtyValue > 0L) {
					freebieSelected.add(orderEntry.getTransactionID());
					continue;
				}

			}
			
		}
		int count=0;
		for(String f : freebie){
			if( freebieSelected.contains(f)){
				 count++;
			}
		}
		return (freebie.size() != count && count >0) ;
	}
}