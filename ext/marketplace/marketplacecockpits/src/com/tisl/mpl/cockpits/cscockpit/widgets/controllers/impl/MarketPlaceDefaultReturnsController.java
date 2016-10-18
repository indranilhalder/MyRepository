package com.tisl.mpl.cockpits.cscockpit.widgets.controllers.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Checkbox;

import com.tisl.mpl.cockpits.cscockpit.utilities.CodeMasterUtility;
import com.tisl.mpl.cockpits.cscockpit.widgets.controllers.MarketPlaceReturnsController;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.TypeofReturn;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.data.CODSelfShipResponseData;
import com.tisl.mpl.data.RTSAndRSSReturnInfoRequestData;
import com.tisl.mpl.data.ReturnInfoData;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.data.ReturnItemAddressData;
import com.tisl.mpl.marketplacecommerceservices.service.MPLReturnService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
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
import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
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
	@Autowired
	private MplConfigFacade mplConfigFacade;
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private CancelReturnFacade cancelReturnFacade;
	@Autowired
	private PincodeServiceFacade pincodeServiceFacade;
	@Autowired
	private MplOrderFacade mplOrderFacade;
//	@Autowired
//	private Populator<AddressData, ReturnItemAddressData>  ReturnItemAddressDataPopulator;
	@Autowired
	private AbstractOrderPopulator<OrderModel, OrderData> orderPopulator;
	@Autowired
	private Populator<AbstractOrderEntryModel, OrderEntryData> orderEntryPopulator;
	@Autowired
	private Populator<CustomerModel, CustomerData> customerPopulator;
//	private CallContextController callContextController;
//
//	protected CallContextController getCallContextController() {
//		return callContextController;
//	}
//
//	@Required
//	public void setCallContextController(
//			CallContextController callContextController) {
//		this.callContextController = callContextController;
//	}

	private static final String OMS_BYPASS_KEY = "cscockpit.oms.serviceability.check.bypass";

	private static final Logger LOG = Logger
			.getLogger(DefaultReturnsController.class);

	public Map<Boolean, List<OrderLineDataResponse>> validateReverseLogistics(
			List<ReturnLogistics> returnLogisticsList) {
		String returnFulfillmentType = null;
		String returnFulfillmentByP1 = null;
		List<ReturnLogistics> returnItems = new ArrayList<ReturnLogistics>();
		final OrderModel orderModel = getOrderModel();
		for (AbstractOrderEntryModel entry : orderModel.getEntries()) {
			for (ReturnLogistics seletedItems : returnLogisticsList) {
				ReturnLogistics returnLogistics = new ReturnLogistics();
				if (seletedItems.getTransactionId().equals(
						entry.getTransactionID())) {
					returnLogistics.setOrderId(seletedItems.getOrderId());
					returnLogistics.setTransactionId(seletedItems.getTransactionId());
					returnLogistics.setPinCode(seletedItems.getPinCode());
					final ProductModel productModel = mplOrderFacade
							.getProductForCode(entry.getProduct().getCode());
					for (final SellerInformationModel sellerInfo : productModel
							.getSellerInformationRelator()) {
						if (entry.getSelectedUSSID().equalsIgnoreCase(
								sellerInfo.getUSSID())) {
							if (CollectionUtils.isNotEmpty(sellerInfo
									.getRichAttribute())) {
								for (RichAttributeModel richAttribute : sellerInfo
										.getRichAttribute()) {
									if (null != richAttribute
											.getReturnFulfillMode()) {
										LOG.info(richAttribute
												.getReturnFulfillMode());
										returnFulfillmentType = richAttribute
												.getReturnFulfillMode()
												.getCode();
									}

									if (null != richAttribute
											.getReturnFulfillModeByP1()) {
										LOG.info(richAttribute
												.getReturnFulfillModeByP1());
										returnFulfillmentByP1 = richAttribute
												.getReturnFulfillModeByP1()
												.getCode();
									}
								}
							}
							if(StringUtils.isNotEmpty(returnFulfillmentType)) {
								returnLogistics.setReturnFulfillmentType(returnFulfillmentType);
							}
							if(StringUtils.isNotEmpty(returnFulfillmentByP1)) {
								returnLogistics.setReturnFulfillmentByP1(returnFulfillmentByP1);
							}
							returnItems.add(returnLogistics);
						}
					}
				}
			}
		}

		final Map<Boolean, List<OrderLineDataResponse>> responseMap = new HashMap<Boolean, List<OrderLineDataResponse>>();
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
		List<ReturnLogistics> returnLogisticsList = new ArrayList<ReturnLogistics>();
		ReturnRequestModel request = null;
		ReplacementOrderModel replacementOrder = null;
		List<ReplacementEntryModel> returnEntries = new ArrayList<ReplacementEntryModel>();

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

			List<ReturnLogistics> returnLogisticsList = new ArrayList<ReturnLogistics>();
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

			// Added By Techouts for return pincode
			Session session = Executions.getCurrent().getDesktop().getSession();
			String pinCode = (String) session.getAttribute("pinCode");

			for (AbstractOrderEntryModel orderEntry : orderModel.getEntries()) {
				ReturnLogistics returnLogistics = new ReturnLogistics();
				returnLogistics.setOrderId(orderEntry.getOrder().getCode());
				returnLogistics.setTransactionId(orderEntry.getTransactionID());
				returnLogistics.setPinCode(pinCode);
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
			final List<ObjectValueContainer> returnObjectValueContainers,
			String pinCode) {
		Map returnableOrderEntries = widget.getWidgetController()
				.getReturnableOrderEntries();
		List<ReturnLogistics> returnLogisticsList = new ArrayList<ReturnLogistics>();
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
				returnLogistics.setPinCode(pinCode);
				returnLogisticsList.add(returnLogistics);

			}
		}
		return returnLogisticsList;
	}

	public List<ReturnLogistics> getReturnLogisticsList(
			final InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			final List<ObjectValueContainer> returnObjectValueContainers) {
		Map returnableOrderEntries = widget.getWidgetController()
				.getReturnableOrderEntries();
		List<ReturnLogistics> returnLogisticsList = new ArrayList<ReturnLogistics>();
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

					// Added By Techouts

					Session session = Executions.getCurrent().getDesktop()
							.getSession();
					String pinCode = (String) session.getAttribute("pinCode");

					List<ReturnEntryModel> returnEntries = returnRequest
							.getReturnEntries();
					if (CollectionUtils.isNotEmpty(returnEntries)) {
						for (ReturnEntryModel returnEntry : returnEntries) {
							orderLine = new OrderLine();
							String returnFulfillmentType = null;
							orderModel = ((OrderModel) (returnEntry
									.getOrderEntry().getOrder()))
									.getParentReference();
							orderLine.setOrderId(orderModel.getCode());
							orderLine.setTransactionId(returnEntry
									.getOrderEntry().getTransactionID());
							orderLine.setReturnCancelFlag("R");
							orderLine.setRequestID(returnRequest.getCode());
							// For FullFillment Type
							for (AbstractOrderEntryModel entry : orderModel.getEntries()) {
								final ProductModel productModel = mplOrderFacade
										.getProductForCode(entry.getProduct().getCode());
								for (final SellerInformationModel sellerInfo : productModel
										.getSellerInformationRelator()) {
									if (returnEntry.getOrderEntry().getSelectedUSSID().equalsIgnoreCase(sellerInfo.getUSSID())) {
										if (CollectionUtils.isNotEmpty(sellerInfo
												.getRichAttribute())) {
											for (RichAttributeModel richAttribute : sellerInfo
													.getRichAttribute()) {
												if (null != richAttribute
														.getReturnFulfillMode()) {
													LOG.info(richAttribute
															.getReturnFulfillMode());
													returnFulfillmentType = richAttribute
															.getReturnFulfillMode()
															.getCode();
												}
											}
										}
									}
								}
							}
							if(returnFulfillmentType != null) {
								orderLine.setReturnFulfillmentMode(returnFulfillmentType);
							}
							reason = CodeMasterUtility
									.getglobalCode(((ReplacementEntryModel) returnEntry)
											.getReason().getCode());
							notes = ((ReplacementEntryModel) returnEntry)
									.getNotes();
							orderLine.setReasonCode(reason);
							orderLine.setReturnCancelRemarks(notes);
							request.getOrderLine().add(orderLine);
							if (null != pinCode || !pinCode.isEmpty()) {
								orderLine.setPinCode(pinCode);
							}
							
							ReturnItem(returnEntry.getOrderEntry(),orderLine,returnRequest);
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
							if (null != pinCode || !pinCode.isEmpty()) {
								orderLine.setPinCode(pinCode);
							}
							
							for (AbstractOrderEntryModel entry : orderModel.getEntries()) {
								String returnFulfillmentType = null;
								final ProductModel productModel = mplOrderFacade
										.getProductForCode(entry.getProduct().getCode());
								for (final SellerInformationModel sellerInfo : productModel
										.getSellerInformationRelator()) {
									if (orderEntryModel.getSelectedUSSID().equalsIgnoreCase(sellerInfo.getUSSID())) {
										if (CollectionUtils.isNotEmpty(sellerInfo
												.getRichAttribute())) {
											for (RichAttributeModel richAttribute : sellerInfo
													.getRichAttribute()) {
												if (null != richAttribute
														.getReturnFulfillMode()) {
													LOG.info(richAttribute
															.getReturnFulfillMode());
													returnFulfillmentType = richAttribute
															.getReturnFulfillMode()
															.getCode();
												}
											}
										}
										if(returnFulfillmentType != null) {
											orderLine.setReturnFulfillmentMode(returnFulfillmentType);
										}
									}
								}
							}
							
							
							// OMS Status and Craing crm Ticket for return
							ReturnItem(orderEntryModel,orderLine,returnRequest);
							
						}
						
				
					}

				  	MplOrderIsCancellableResponse response = mplOrderCancelClientService.orderCancelDataToOMS(request);
					
//					for ( OrderLine CancelOrderRequest : request.getOrderLine() ) {
//						OrderData subOrderDetails = new OrderData();
//						
//							Boolean omsResponce= cancelReturnFacade.implementReturnItem(subOrderDetails, subOrderEntry, returnData, customerData, salesApplication, returnAddress);
//					}
//					
//					Boolean omsResponce= cancelReturnFacade.implementReturnItem(subOrderDetails, subOrderEntry, returnData, customerData, salesApplication, returnAddress);
//					
					
//					
//					if (null == omsResponce) {
//					getModelService().remove(returnRequest);//Remove if created any 
//						throw new OrderReturnException(orderModel.getCode(),
//								"Item is not returnable at OMS.");
//					}
				}
			}
		} catch (Exception ex) {
			throw new OrderReturnException("Failed", "OMS is not responding");
		}
	}

	private void ReturnItem(AbstractOrderEntryModel orderEntryModel, OrderLine orderLine, ReturnRequestModel returnRequest) throws OrderReturnException {
		OrderModel  subOrderModel = (OrderModel) orderEntryModel.getOrder();
		OrderData subOrderDetails = new OrderData();
		orderPopulator.populate(subOrderModel, subOrderDetails);
		OrderEntryData orderEntryData = new OrderEntryData();
		
		orderEntryPopulator.populate(orderEntryModel, orderEntryData);
	//	TypedObject customer = getCallContextController().getCurrentCustomer();
		//CustomerModel customermodel = (CustomerModel) customer
		//		.getObject();
		CustomerData customerData = new CustomerData();
		//customerPopulator.populate(customermodel, customerData);
		ReturnInfoData returnInfoData = new ReturnInfoData();
		returnInfoData=PopulateReturnInfoData(orderLine);
		Session session = Executions.getCurrent().getDesktop().getSession();
		final AddressData AddressData = (AddressData) session.getAttribute("returnAddress");
		ReturnItemAddressData returnItemAddressData = new ReturnItemAddressData();
		//ReturnItemAddressDataPopulator.populate(AddressData, returnItemAddressData);
		Boolean omsResponce= cancelReturnFacade.implementReturnItem(subOrderDetails, orderEntryData, returnInfoData, customerData, SalesApplication.CALLCENTER, returnItemAddressData);

		if (null == omsResponce) {
		getModelService().remove(returnRequest);//Remove if created any 
			throw new OrderReturnException(orderEntryModel.getOrder().getCode(),
					"Item is not returnable at OMS.");
		}
		
	}

	private ReturnInfoData PopulateReturnInfoData(OrderLine orderLine) {

		ReturnInfoData returnInfoData = new ReturnInfoData();
		returnInfoData.setIsReturn("Y");
		returnInfoData.setReasonCode(orderLine.getReasonCode());
		returnInfoData.setTicketTypeCode("R");
		returnInfoData.setUssid(orderLine.getTransactionId());
		returnInfoData.setReturnFulfillmentMode(orderLine.getReturnFulfillmentMode());
		returnInfoData.setReturnMethod("SchedulePichup");
		returnInfoData.setReturnPickupDate("11-12-2016");
		returnInfoData.setTimeSlotFrom("11 AM");
		returnInfoData.setTimeSlotTo("1 PM");
	
		return returnInfoData;
		
		
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

	@Override
	public List<String> getReturnTimeSlotsByKey(String configKey) {
		List<String> timeSlots = mplConfigFacade
				.getDeliveryTimeSlots(configKey);
		if (timeSlots.size() > 0) {
			return timeSlots;
		}
		return null;
	}

	@Override
	public List<PointOfServiceData> getAllReturnableStores(String pincode,
			String sellerId) {
		List<PointOfServiceData> returnStores = null;
		if (null != pincode && null != sellerId) {
			returnStores = pincodeServiceFacade.getAllReturnableStores(pincode,
					sellerId);
		}
		if (null != returnStores && returnStores.size() > 0) {
			return returnStores;
		}
		return null;
	}

	@Override
	public List<String> getReturnScheduleDates(AbstractOrderEntryModel entry) {
		List<String> returnableDates = new ArrayList<String>();
		final String orderCode = entry.getOrder().getCode();
		OrderData orderData = mplCheckoutFacade.getOrderDetailsForCockpitUser(orderCode, (CustomerModel) entry.getOrder().getUser());
		final List<OrderEntryData> subOrderEntries = orderData.getEntries();
		for (OrderEntryData entrys : subOrderEntries) {
			if (entry.getTransactionID().equals(entrys.getTransactionId())) {
				returnableDates = cancelReturnFacade.getReturnableDates(entrys);
			}
		}
		return returnableDates;
	}

	@Override
	public CODSelfShipResponseData getCodPaymentInfoToFICO(
			CODSelfShipData codSelfShipData,
			List<AbstractOrderEntryModel> returnEntry) {

		OrderModel subOrder = (OrderModel) returnEntry.get(0).getOrder();
		String orderCode = subOrder.getParentReference().getCode();
		for (AbstractOrderEntryModel entry : returnEntry) {
			codSelfShipData.setOrderNo(orderCode);
			codSelfShipData.setTransactionID(entry.getTransactionID());
			cancelReturnFacade.codPaymentInfoToFICO(codSelfShipData);
		}

		return null;
	}

	@Override
	public void retrunInfoCallToOMSFromCsCockpit(
			InputWidget<DefaultListWidgetModel<TypedObject>, ReturnsController> widget,
			AbstractOrderEntryModel entry, List<Checkbox> storeChecks) {
		OrderModel orderModel = (OrderModel) entry.getOrder();
		RTSAndRSSReturnInfoRequestData infoRequestData = new RTSAndRSSReturnInfoRequestData();
		List<String> stores = new ArrayList<String>();
		Map<String, PointOfServiceData> stor = new HashMap<String, PointOfServiceData>();
		for (Checkbox selctedStores : storeChecks) {
			selctedStores.getAttributes();
			if (selctedStores.isChecked()) {
				stor = selctedStores.getAttributes();
				for (Entry<String, PointOfServiceData> entrySet : stor
						.entrySet()) {
					PointOfServiceData store = entrySet.getValue();
					stores.add(store.getSlaveId());
				}
			}
		}
		infoRequestData.setOrderId(orderModel.getParentReference().getCode());
		infoRequestData.setRTSStore(stores);
		infoRequestData.setTransactionId(entry.getTransactionID());
		infoRequestData
				.setReturnType(MarketplacecommerceservicesConstants.RETURN_TYPE_RTS);
		cancelReturnFacade.retrunInfoCallToOMS(infoRequestData);

	} 
/**
 * This method is used for ticket creation for crm 
 */
	@Override
	public boolean createTicketInCRM(OrderData subOrderDetails,
			OrderEntryData subOrderEntry, String ticketTypeCode,
			String reasonCode, String refundType, String ussid,
			CustomerData customerData, OrderModel subOrderModel,
			ReturnItemAddressData returnAddress, ReturnInfoData returnInfoData) {
		try {
			cancelReturnFacade.createTicketInCRM(subOrderDetails, subOrderEntry, ticketTypeCode, reasonCode, refundType, ussid, customerData, subOrderModel, returnAddress, returnInfoData);
		}catch(Exception e) {
			LOG.error("Exception while creating crm ticket for return "+e.getMessage());
		}
		return false;
	}

}