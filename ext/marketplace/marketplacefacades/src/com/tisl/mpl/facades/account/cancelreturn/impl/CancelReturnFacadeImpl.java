/**
 *
 */
package com.tisl.mpl.facades.account.cancelreturn.impl;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.enums.TypeofReturn;
import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.data.ReturnAddressInfo;
import com.tisl.mpl.data.ReturnLogisticsResponseData;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.ReturnItemAddressData;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.CRMTicketDetailModel;
import com.tisl.mpl.ordercancel.MplOrderCancelEntry;
import com.tisl.mpl.ordercancel.MplOrderCancelRequest;
import com.tisl.mpl.service.MplOrderCancelClientService;
import com.tisl.mpl.service.ReturnLogisticsService;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.sns.push.service.impl.MplSNSMobilePushServiceImpl;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.wsdto.PushNotificationData;
import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.wsdto.ReturnLogisticsResponseDTO;
import com.tisl.mpl.wsdto.ReturnPincodeDTO;
import com.tisl.mpl.wsdto.TicketMasterXMLData;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest;
import com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse;
import com.tisl.mpl.xml.pojo.OrderLineDataResponse;
import com.tisl.mpl.xml.pojo.ReturnLogisticsResponse;



/**
 * @author TCS
 *
 */
public class CancelReturnFacadeImpl implements CancelReturnFacade
{
	@Resource
	private MplOrderService mplOrderService;
	@Resource
	private MplSNSMobilePushServiceImpl mplSNSMobilePushService;
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;
	@Resource
	private MplOrderCancelClientService mplOrderCancelClientService;
	@Resource
	private TicketCreationCRMservice ticketCreate;
	@Resource
	private MplJusPayRefundService mplJusPayRefundService;
	@Resource
	private BaseStoreService baseStoreService;
	@Resource
	private UserService userService;
	@Resource
	private CustomerAccountService customerAccountService;
	@Resource
	private ReturnLogisticsService returnLogistics;
	@Resource
	private ModelService modelService;
	@Resource
	private OrderCancelService orderCancelService;
	@Resource
	private ReturnService returnService;
	@Resource
	private ConfigurationService configurationService;
	@Resource
	private MPLRefundService mplRefundService;
	private Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter;
	private OrderCancelRecordsHandler orderCancelRecordsHandler;

	@Resource(name = "mplProcessOrderService")
	MplProcessOrderService mplProcessOrderService;

	protected static final Logger LOG = Logger.getLogger(CancelReturnFacadeImpl.class);


	@Override
	public boolean implementCancelOrReturn(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String reasonCode, final String ussid, final String ticketTypeCode, final CustomerData customerData,
			String refundType, final boolean isReturn, final SalesApplication salesApplication)
	{

		boolean cancelOrRetrnanable = true;
		boolean omsCancellationStatus = false;
		//fix for TISPRD-5958
		boolean returnLogisticsCheck = true;
		List<OrderModel> subOrderModels = null;
		OrderModel subOrderModel = null;
		OrderModel subOrderModelVersioned = null;
		/*
		 * customerAccountService.getOrderForCode((CustomerModel) userService.getCurrentUser(), subOrderDetails.getCode(),
		 * baseStoreService.getCurrentBaseStore());
		 */
		boolean bogoOrFreeBie = false;
		try
		{
			//After cancellation tehre are 2 models one with versionId and with not
			subOrderModels = orderModelService.getOrders(subOrderDetails.getCode());
			for (final OrderModel subOrder : subOrderModels)
			{
				if (subOrder.getVersionID() != null)
				{
					subOrderModelVersioned = subOrder;
				}
				if (subOrder.getVersionID() == null)
				{
					subOrderModel = subOrder;
				}
			}
			MplCancelOrderRequest orderLineRequest = new MplCancelOrderRequest();

			//			for (final AbstractOrderEntryModel entry : subOrderModel.getEntries())
			//			{
			//				if (CollectionUtils.isNotEmpty(entry.getAssociatedItems())
			//						&& subOrderEntry.getTransactionId().equalsIgnoreCase(entry.getTransactionID()))
			//				{
			//					bogoOrFreeBie = true;
			//					break;
			//				}
			//			}

			//TISSIT-1779
			if (CollectionUtils.isNotEmpty(subOrderEntry.getAssociatedItems()))
			{
				bogoOrFreeBie = true;
			}

			LOG.debug("************BOGO or Free Bie available for order" + subOrderModel.getCode() + " is " + bogoOrFreeBie);
			LOG.debug("Step 2: ***********************************Ticket Type code : " + ticketTypeCode);
			if ((ticketTypeCode.equalsIgnoreCase("C") || (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie))) //TISEE-933
			{
				orderLineRequest = populateOrderLineData(subOrderEntry, ticketTypeCode, subOrderModel, reasonCode);

				if (CollectionUtils.isNotEmpty(orderLineRequest.getOrderLine()))
				{
					cancelOrRetrnanable = cancelOrderInOMS(orderLineRequest, cancelOrRetrnanable, isReturn);
				}
			}
			if (ticketTypeCode.equalsIgnoreCase("R") && bogoOrFreeBie) //TISEE-933
			{
				cancelOrRetrnanable = true;
			}
			LOG.debug("Step 2: ***********************************cancelOrRetrnanable : " + cancelOrRetrnanable);
			if (cancelOrRetrnanable)
			{
				final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
						subOrderEntry.getTransactionId());
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
				{
					if (ticketTypeCode.equalsIgnoreCase("C"))
					{
						LOG.debug("Step 3:***********************************History creation start for cancellation");
						createHistoryEntry(abstractOrderEntryModel, subOrderModel, ConsignmentStatus.CANCELLATION_INITIATED);
					}
					else if (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) ////TISEE-933
					{
						LOG.debug("Step 3:***********************************History creation start for retrun");
						createHistoryEntry(abstractOrderEntryModel, subOrderModel, ConsignmentStatus.RETURN_INITIATED);
					}
				}
			}


			omsCancellationStatus = cancelOrRetrnanable;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		try
		{

			if (omsCancellationStatus)
			{
				LOG.debug("Step 4:***********************************Ticket is to be created for sub order:"
						+ subOrderDetails.getCode());
				//TISPRD-1641
				final List<PaymentTransactionModel> tranactions = subOrderModel.getPaymentTransactions();
				if (CollectionUtils.isNotEmpty(tranactions))
				{
					final PaymentTransactionEntryModel paymentTransEntry = tranactions.iterator().next().getEntries().iterator()
							.next();
					if (paymentTransEntry.getPaymentMode() != null
							&& paymentTransEntry.getPaymentMode().getMode() != null
							&& MarketplacecommerceservicesConstants.CASH_ON_DELIVERY.equalsIgnoreCase(paymentTransEntry.getPaymentMode()
									.getMode()))
					{
						refundType = "N";
					}
				}

				//TISPT-386
				try
				{
					final List<ReturnLogisticsResponseData> returnLogisticsRespList = checkReturnLogistics(subOrderDetails);
					if (CollectionUtils.isNotEmpty(returnLogisticsRespList))
					{
						for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
						{
							if (StringUtils.isNotEmpty(response.getIsReturnLogisticsAvailable())
									&& response.getIsReturnLogisticsAvailable().equalsIgnoreCase("N"))
							{
								returnLogisticsCheck = false;
								break;
							}
						}
					}
					else
					{
						returnLogisticsCheck = false;
					}
				}
				catch (final Exception e)
				{
					LOG.info(">> returnLogisticsCheck Fails>>  Setting Type of Return " + returnLogisticsCheck);
				}
				//TISPRD-1641
				final boolean ticketCreationStatus = createTicketInCRM(subOrderDetails, subOrderEntry, ticketTypeCode, reasonCode,
						refundType, ussid, customerData, subOrderModel, returnLogisticsCheck);
				LOG.debug("Step 4.1:***********************************Ticket creation status for sub order:" + ticketCreationStatus);
				LOG.debug("Step 5 :*********************************** Refund and OMS call started");
				cancelOrRetrnanable = initiateCancellation(ticketTypeCode, subOrderDetails, subOrderEntry, subOrderModel, reasonCode);
				LOG.debug("Step 5.1 :*********************************** Refund and OMS call status:" + cancelOrRetrnanable);

				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) //TISEE-5524
				{
					LOG.debug("Step 6:***********************************Create return request for Return:"
							+ subOrderDetails.getCode());

					final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
							subOrderEntry.getTransactionId());

					for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
					{
						final boolean returnReqSuccess = createRefund(subOrderModel, abstractOrderEntryModel, reasonCode,
								salesApplication, returnLogisticsCheck);

						LOG.debug("**********************************Return request successful :" + returnReqSuccess);
					}
				}
				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("C"))
				{
					LOG.debug("Step 7:***********************************Create Cancel request" + subOrderDetails.getCode());
					frameCancelPushNotification(subOrderModel, subOrderModelVersioned, subOrderEntry, reasonCode, customerData);
					LOG.debug("*Step 7:*********************************Cancel request successful and push notification sent:");
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(">>> Cancel Refund exception occured in implementCancelOrReturn Etail Non BusinessException: ", e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(">>> Cancel Refund exception occured in implementCancelOrReturn in implementCancelOrReturn ", e);
		}


		try
		{
			LOG.debug("Step 8: *********************************** Updating commerce consignment status" + omsCancellationStatus);

			if (omsCancellationStatus)
			{
				//TPR-965
				mplProcessOrderService.removePromotionInvalidation(subOrderModel.getParentReference());

				final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
						subOrderEntry.getTransactionId());
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
				{
					if (ticketTypeCode.equalsIgnoreCase("C"))
					{
						updateConsignmentStatus(abstractOrderEntryModel, ConsignmentStatus.CANCELLATION_INITIATED);
					}
					else if (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) ////TISEE-933
					{
						updateConsignmentStatus(abstractOrderEntryModel, ConsignmentStatus.RETURN_INITIATED);
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("*******************Updating commerce consignment status ", e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception ex)
		{
			LOG.error(">>> Exception occured while updating consignment : ", ex);
		}

		return omsCancellationStatus;
	}

	/**
	 * @author Techouts
	 * @return boolean Retun Item Pincode Serviceability
	 */
	@Override
	public boolean implementReturnItem(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String reasonCode, final String ussid, final String ticketTypeCode, final CustomerData customerData,
			final String refundType, final boolean isReturn, final SalesApplication salesApplication,
			final ReturnItemAddressData returnAddress)
	{

		LOG.debug("Step 1 :*********************************** isReturn:" + isReturn);

		boolean cancelOrRetrnanable = true;
		boolean omsCancellationStatus = false;
		String pincode = null;

		final String transactionId = subOrderEntry.getTransactionId();

		final OrderModel subOrderModel = orderModelService.getOrder(subOrderDetails.getCode());
		boolean bogoOrFreeBie = false;
		try
		{
			MplCancelOrderRequest orderLineRequest = new MplCancelOrderRequest();

			if (null != returnAddress.getPincode())
			{
				pincode = returnAddress.getPincode();
			}
			if (CollectionUtils.isNotEmpty(subOrderEntry.getAssociatedItems()))
			{
				bogoOrFreeBie = true;
			}

			LOG.debug("************BOGO or Free Bie available for order" + subOrderModel.getCode() + " is " + bogoOrFreeBie);
			LOG.debug("Step 2: ***********************************Ticket Type code : " + ticketTypeCode);
			if ((ticketTypeCode.equalsIgnoreCase("C") || (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie))) //TISEE-933
			{

				orderLineRequest = populateOrderLineData(subOrderEntry, ticketTypeCode, subOrderModel, reasonCode, pincode);




				if (CollectionUtils.isNotEmpty(orderLineRequest.getOrderLine()))
				{
					cancelOrRetrnanable = cancelOrderInOMS(orderLineRequest, cancelOrRetrnanable, isReturn);
				}


			}
			if (ticketTypeCode.equalsIgnoreCase("R") && bogoOrFreeBie) //TISEE-933
			{
				cancelOrRetrnanable = true;
			}
			LOG.debug("Step 2: ***********************************cancelOrRetrnanable : " + cancelOrRetrnanable);
			if (cancelOrRetrnanable)
			{
				final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
						subOrderEntry.getTransactionId());
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
				{

					if (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) ////TISEE-933
					{
						LOG.debug("Step 3:***********************************History creation start for retrun");
						createHistoryEntry(abstractOrderEntryModel, subOrderModel, ConsignmentStatus.RETURN_INITIATED);
					}
				}
			}


			omsCancellationStatus = cancelOrRetrnanable;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		try
		{

			if (omsCancellationStatus)
			{
				LOG.debug("Step 4:***********************************Ticket is to be created for sub order:"
						+ subOrderDetails.getCode());

				final boolean ticketCreationStatus = createTicketInCRM(subOrderDetails, subOrderEntry, ticketTypeCode, reasonCode,
						refundType, ussid, customerData, subOrderModel, returnAddress);

				LOG.debug("Step 4.1:***********************************Ticket creation status for sub order:" + ticketCreationStatus);
				LOG.debug("Step 5 :*********************************** Refund and OMS call started");
				cancelOrRetrnanable = initiateCancellation(ticketTypeCode, subOrderDetails, subOrderEntry, subOrderModel, reasonCode);
				LOG.debug("Step 5.1 :*********************************** Refund and OMS call status:" + cancelOrRetrnanable);

				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) //TISEE-5524
				{
					LOG.debug("Step 6:***********************************Create return request for Return:"
							+ subOrderDetails.getCode());

					final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
							subOrderEntry.getTransactionId());

					for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
					{
						final boolean returnReqSuccess = createRefund(subOrderModel, abstractOrderEntryModel, reasonCode,
								salesApplication, returnAddress.getPincode(), subOrderDetails, transactionId);

						LOG.debug("**********************************Return request successful :" + returnReqSuccess);
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(">>> Cancel Refund exception occured in implementReturnItem etail non business exception : ", e);

			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(">>> Cancel Refund exception occured in implementReturnItem : ", e);
		}


		try
		{
			LOG.debug("Step 8: *********************************** Updating commerce consignment status" + omsCancellationStatus);

			if (omsCancellationStatus)
			{
				//TPR-965
				mplProcessOrderService.removePromotionInvalidation(subOrderModel.getParentReference());

				final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
						subOrderEntry.getTransactionId());
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
				{
					if (ticketTypeCode.equalsIgnoreCase("C"))
					{
						updateConsignmentStatus(abstractOrderEntryModel, ConsignmentStatus.CANCELLATION_INITIATED);
					}
					else if (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) ////TISEE-933
					{
						updateConsignmentStatus(abstractOrderEntryModel, ConsignmentStatus.RETURN_INITIATED);
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("*******************Updating commerce consignment status ", e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception ex)
		{
			LOG.error(">>> Exception occured while updating consignment : ", ex);
		}

		return omsCancellationStatus;
	}

	private void updateConsignmentStatus(final AbstractOrderEntryModel orderEntryModel, final ConsignmentStatus consignmentStatus)
	{
		boolean updateStatus = false;
		try
		{
			if (CollectionUtils.isNotEmpty(orderEntryModel.getConsignmentEntries()))
			{
				for (final ConsignmentEntryModel consignmentEntryModel : orderEntryModel.getConsignmentEntries())
				{
					final ConsignmentModel cosignmentModel = consignmentEntryModel.getConsignment();
					if (cosignmentModel != null)
					{
						cosignmentModel.setStatus(consignmentStatus);
						getModelService().save(cosignmentModel);
						LOG.debug(" Consignment entry updated for Transaction id  " + orderEntryModel.getTransactionID() + " Status :"
								+ consignmentStatus.getCode());
						LOG.debug(" Order history created for Transaction id  " + orderEntryModel.getTransactionID() + " Status :"
								+ consignmentStatus.getCode());
						updateStatus = true;
					}
				}
			}
			else
			{
				LOG.debug("Step8:**** Updating consignment status and order histoery insertion Consinment is not present ************** ");
			}

			//createHistoryEntry(orderEntryModel, orderModel, consignmentStatus);
		}
		catch (final Exception ex)
		{
			LOG.error("**** Error occurs while updateConsignmentStatus ", ex);
		}
		LOG.debug("Step8:**** Updating consignment status and order history completed" + updateStatus);
	}


	private PushNotificationData frameCancelPushNotification(final OrderModel subOrderModel,
			final OrderModel subOrderModelVersioned, final OrderEntryData suborderEntry, final String reasonCode,
			final CustomerData customerData)
	{
		PushNotificationData pushData = null;
		try
		{
			String refundableAmount = null;
			String cancelReason = null;
			String cancelledItems = null;
			double amount = 0d;
			//final AbstractOrderEntryModel entry = null;
			/*
			 * for (final AbstractOrderEntryModel orderEntry : subOrderModel.getEntries()) { if (null !=
			 * orderEntry.getEntryNumber() && orderEntry.getEntryNumber().intValue() == suborderEntryNumber.intValue()) {
			 * entry = orderEntry; break; } }
			 */

			final List<PaymentTransactionModel> tranactions = subOrderModel.getPaymentTransactions();
			if (CollectionUtils.isNotEmpty(tranactions))
			{
				final PaymentTransactionEntryModel paymentTransEntry = tranactions.iterator().next().getEntries().iterator().next();

				if (paymentTransEntry.getPaymentMode() != null && paymentTransEntry.getPaymentMode().getMode() != null
						&& "COD".equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
				{
					amount = 0d;
				}
				else if (suborderEntry.getAmountAfterAllDisc().getDoubleValue() != null)
				{
					amount += suborderEntry.getAmountAfterAllDisc().getDoubleValue().doubleValue();
				}
				else if (suborderEntry.getCurrDelCharge().getDoubleValue() != null)
				{
					amount += suborderEntry.getCurrDelCharge().getDoubleValue().doubleValue();
				}
				refundableAmount = Double.toString(amount);
			}
			//TISPT-386
			//OrderModel orderMod = null;
			//if (null != subOrderModel.getCode() && !subOrderModel.getCode().isEmpty())
			//{
			//	orderMod = orderModelService.getOrderPushNotification(subOrderModel.getCode());
			//}
			AbstractOrderEntryModel cancelledEntry = null;
			if (null != subOrderModelVersioned)
			{
				for (final AbstractOrderEntryModel orderEntry : subOrderModelVersioned.getEntries())
				{
					if (null != orderEntry.getEntryNumber() && orderEntry.getEntryNumber() == suborderEntry.getEntryNumber())
					{
						cancelledEntry = orderEntry;
						break;
					}
				}
			}
			if (null != cancelledEntry && null != cancelledEntry.getQuantity())
			{
				cancelledItems = cancelledEntry.getQuantity().toString();
			}
			cancelReason = getReasonForCancellation(reasonCode);

			CustomerModel customer = getModelService().create(CustomerModel.class);
			if (null != customerData.getUid() && !customerData.getUid().isEmpty())
			{
				customer = getMplSNSMobilePushService().getCustForUId(customerData.getUid());
				if (null != customer && null != customer.getDeviceKey() && !customer.getDeviceKey().isEmpty()
						&& null != customer.getOriginalUid() && !customer.getOriginalUid().isEmpty() && null != customer.getIsActive()
						&& !customer.getIsActive().isEmpty() && customer.getIsActive().equalsIgnoreCase("Y"))
				{
					pushData = new PushNotificationData();
					if (null != refundableAmount && !refundableAmount.isEmpty() && null != cancelReason && !cancelReason.isEmpty())
					{
						pushData.setMessage(MarketplacecommerceservicesConstants.PUSH_MESSAGE_ORDER_CANCELLED
								.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO, refundableAmount)
								.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, cancelledItems)
								.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, cancelReason));
					}
					if (null != subOrderModel.getParentReference() && null != subOrderModel.getParentReference().getCode()
							&& !subOrderModel.getParentReference().getCode().isEmpty())
					{
						pushData.setOrderId(subOrderModel.getParentReference().getCode());
					}
					getMplSNSMobilePushService().setUpNotification(customer.getOriginalUid(), pushData);

				}
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			//return returnReqCreated;
		}
		return pushData;
	}

	private boolean createRefund(final OrderModel subOrderModel, final AbstractOrderEntryModel abstractOrderEntryModel,
			final String reasonCode, final SalesApplication salesApplication, final boolean returnLogisticsCheck)
	{

		boolean returnReqCreated = false;
		final List<RefundEntryModel> refundList = new ArrayList<>();
		try
		{
			final ReturnRequestModel returnRequestModel = returnService.createReturnRequest(subOrderModel);
			returnRequestModel.setRMA(returnService.createRMA(returnRequestModel));
			//TISEE-5471

			if (returnLogisticsCheck)
			{
				//LOG.info(">> createRefund >> if >> Setting Type of Return " + returnLogisticsCheck);
				returnRequestModel.setTypeofreturn(TypeofReturn.REVERSE_PICKUP);
			}
			else
			{
				//LOG.info("Setting Type of Return::::::" + returnLogisticsCheck);
				returnRequestModel.setTypeofreturn(TypeofReturn.SELF_COURIER);
			}

			if (salesApplication != null)
			{
				returnRequestModel.setReturnRaisedFrom(salesApplication);
			}
			//End

			if (null != abstractOrderEntryModel)
			{
				final RefundEntryModel refundEntryModel = modelService.create(RefundEntryModel.class);
				refundEntryModel.setOrderEntry(abstractOrderEntryModel);
				refundEntryModel.setReturnRequest(returnRequestModel);
				refundEntryModel.setReason(RefundReason.valueOf(getReasonDesc(reasonCode)));
				refundEntryModel.setStatus(ReturnStatus.RETURN_INITIATED);
				refundEntryModel.setAction(ReturnAction.IMMEDIATE);
				refundEntryModel.setNotes(getReasonDesc(reasonCode));
				refundEntryModel.setExpectedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
				refundEntryModel.setReceivedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
				refundEntryModel.setRefundedDate(new Date());
				final List<PaymentTransactionModel> tranactions = subOrderModel.getPaymentTransactions();
				if (CollectionUtils.isNotEmpty(tranactions))
				{
					final PaymentTransactionEntryModel paymentTransEntry = tranactions.iterator().next().getEntries().iterator()
							.next();

					if (paymentTransEntry.getPaymentMode() != null && paymentTransEntry.getPaymentMode().getMode() != null
							&& "COD".equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
					{
						refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
					}
					else
					{
						final double amount = (abstractOrderEntryModel.getNetAmountAfterAllDisc() != null ? abstractOrderEntryModel
								.getNetAmountAfterAllDisc().doubleValue() : 0D)
								+ (abstractOrderEntryModel.getCurrDelCharge() != null ? abstractOrderEntryModel.getCurrDelCharge()
										.doubleValue() : 0D);

						refundEntryModel.setAmount(NumberUtils.createBigDecimal(Double.toString(amount)));
					}
				}
				refundList.add(refundEntryModel);
				//modelService.save(refundEntryModel);
			}
			//TISPT-386
			modelService.saveAll(refundList);
			modelService.save(returnRequestModel);

			LOG.debug("*************** RMA number:" + returnRequestModel.getRMA());
			returnReqCreated = true;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			//return returnReqCreated;
		}

		return returnReqCreated;
	}

	/**
	 * @author Techouts
	 * @param subOrderModel
	 * @param abstractOrderEntryModel
	 * @param reasonCode
	 * @param salesApplication
	 * @param pinCode
	 * @param subOrderDetails2
	 * @return boolean
	 */
	private boolean createRefund(final OrderModel subOrderModel, final AbstractOrderEntryModel abstractOrderEntryModel,
			final String reasonCode, final SalesApplication salesApplication, final String pinCode, final OrderData subOrderDetails,
			final String transactionId)
	{

		boolean returnReqCreated = false;
		boolean returnLogisticsCheck = true;
		try
		{

			final ReturnRequestModel returnRequestModel = returnService.createReturnRequest(subOrderModel);
			returnRequestModel.setRMA(returnService.createRMA(returnRequestModel));
			//TISEE-5471
			//final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(subOrderModel.getCode()); //Changes for Bulk Return Initiation
			final List<ReturnLogisticsResponseData> returnLogisticsRespList = checkReturnLogistics(subOrderDetails, pinCode,
					transactionId);
			if (CollectionUtils.isNotEmpty(returnLogisticsRespList))
			{
				for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
				{
					if (StringUtils.isNotEmpty(response.getIsReturnLogisticsAvailable())
							&& response.getIsReturnLogisticsAvailable().equalsIgnoreCase("N"))
					{
						returnLogisticsCheck = false;
						break;
					}
				}
			}
			else
			{
				returnLogisticsCheck = false;
			}
			LOG.info(">> createRefund >>  Setting Type of Return " + returnLogisticsCheck);
			if (returnLogisticsCheck)
			{
				//LOG.info(">> createRefund >> if >> Setting Type of Return " + returnLogisticsCheck);
				returnRequestModel.setTypeofreturn(TypeofReturn.REVERSE_PICKUP);
			}
			else
			{
				//LOG.info("Setting Type of Return::::::" + returnLogisticsCheck);
				returnRequestModel.setTypeofreturn(TypeofReturn.SELF_COURIER);
			}

			if (salesApplication != null)
			{
				returnRequestModel.setReturnRaisedFrom(salesApplication);
			}
			//End

			if (null != abstractOrderEntryModel)
			{
				final RefundEntryModel refundEntryModel = modelService.create(RefundEntryModel.class);
				refundEntryModel.setOrderEntry(abstractOrderEntryModel);
				refundEntryModel.setReturnRequest(returnRequestModel);
				refundEntryModel.setReason(RefundReason.valueOf(getReasonDesc(reasonCode)));
				refundEntryModel.setStatus(ReturnStatus.RETURN_INITIATED);
				refundEntryModel.setAction(ReturnAction.IMMEDIATE);
				refundEntryModel.setNotes(getReasonDesc(reasonCode));
				refundEntryModel.setExpectedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
				refundEntryModel.setReceivedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
				refundEntryModel.setRefundedDate(new Date());
				final List<PaymentTransactionModel> tranactions = subOrderModel.getPaymentTransactions();
				if (CollectionUtils.isNotEmpty(tranactions))
				{
					final PaymentTransactionEntryModel paymentTransEntry = tranactions.iterator().next().getEntries().iterator()
							.next();

					if (paymentTransEntry.getPaymentMode() != null && paymentTransEntry.getPaymentMode().getMode() != null
							&& "COD".equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
					{
						refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
					}
					else
					{
						final double amount = (abstractOrderEntryModel.getNetAmountAfterAllDisc() != null ? abstractOrderEntryModel
								.getNetAmountAfterAllDisc().doubleValue() : 0D)
								+ (abstractOrderEntryModel.getCurrDelCharge() != null ? abstractOrderEntryModel.getCurrDelCharge()
										.doubleValue() : 0D);

						refundEntryModel.setAmount(NumberUtils.createBigDecimal(Double.toString(amount)));
					}
				}
				modelService.save(refundEntryModel);
			}

			modelService.save(returnRequestModel);

			LOG.debug("*************** RMA number:" + returnRequestModel.getRMA());
			returnReqCreated = true;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
			//return returnReqCreated;
		}

		return returnReqCreated;
	}

	private String getReasonDesc(final String reasonCode)
	{

		//Get the reason from Global Code master
		String reasonDescription = null;
		final List<ReturnReasonData> returnReasonList = mplOrderService.getReturnReasonForOrderItem();
		for (final ReturnReasonData returnReasonData : returnReasonList)
		{
			if (returnReasonData.getCode().equalsIgnoreCase(reasonCode))
			{
				if (StringUtils.isNotEmpty(returnReasonData.getReasonDescription()))
				{
					reasonDescription = returnReasonData.getReasonDescription();
				}
				break;
			}
		}
		//End

		LOG.info("****Actual return reason desc from Global code master : " + reasonDescription);
		return reasonDescription;
	}

	/**
	 * reasons for cancellation based on a reason code
	 *
	 * @param reasonCode
	 * @return String
	 */
	private String getReasonForCancellation(final String reasonCode)
	{
		//Get the reason from Global Code master
		String reasonDescription = null;
		final List<CancellationReasonModel> cancellationReason = mplOrderService.getCancellationReason();
		for (final CancellationReasonModel cancelModel : cancellationReason)
		{
			if (cancelModel.getReasonCode().equalsIgnoreCase(reasonCode)
					&& StringUtils.isNotEmpty(cancelModel.getReasonDescription()))
			{
				reasonDescription = cancelModel.getReasonDescription();
				break;
			}
		}
		return reasonDescription;
	}

	/**
	 * @param subOrderEntry
	 * @param subOrderDetails
	 * @param ticketTypeCode
	 * @param reasonCode
	 * @param refundType
	 * @param ussid
	 * @param subOrderModel
	 * @param customerData
	 * @return boolean
	 *
	 */
	@Override
	public boolean createTicketInCRM(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String ticketTypeCode, final String reasonCode, final String refundType, final String ussid,
			final CustomerData customerData, final OrderModel subOrderModel, final boolean returnLogisticsCheck)
	{
		boolean ticketCreationStatus = false;
		try
		{
			final List<SendTicketLineItemData> lineItemDataList = new ArrayList<SendTicketLineItemData>();
			final SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();

			final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, subOrderEntry.getTransactionId());
			for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntries)
			{
				final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();
				sendTicketLineItemData.setLineItemId(abstractOrderEntryModel.getOrderLineId());
				if (ticketTypeCode.equalsIgnoreCase("C"))
				{
					sendTicketLineItemData.setCancelReasonCode(reasonCode);
					sendTicketRequestData.setRefundType(refundType);
				}
				//				else
				//				{
				//					sendTicketLineItemData.setReturnReasonCode(reasonCode);
				//					sendTicketRequestData.setRefundType(refundType);
				//					sendTicketRequestData.setReturnCategory("RSP");
				//				}
				//				lineItemDataList.add(sendTicketLineItemData);
				else
				{
					sendTicketLineItemData.setReturnReasonCode(reasonCode);
					sendTicketRequestData.setRefundType(refundType);

					if (returnLogisticsCheck)
					{
						//LOG.info("Setting Type of Return::::::" + returnLogisticsCheck);
						sendTicketRequestData.setReturnCategory("RSP");
					}
					else
					{
						//LOG.info("Setting Type of Return::::::" + returnLogisticsCheck);
						sendTicketRequestData.setReturnCategory("RSS");
					}

					//lineItemDataList.add(sendTicketLineItemData);
					//End
				}

				lineItemDataList.add(sendTicketLineItemData);
			}
			//			final List<OrderEntryData> allEntries = subOrderDetails.getEntries();
			//			final List<OrderEntryData> assosiatedEntryList = new ArrayList<OrderEntryData>();
			//			for (final OrderEntryData eachEntry : allEntries)
			//			{
			//				if (!eachEntry.getTransactionId().equalsIgnoreCase(subOrderEntry.getTransactionId()))
			//				{
			//					assosiatedEntryList.add(eachEntry);
			//				}
			//			}
			//			if (subOrderEntry.getMplDeliveryMode().getSellerArticleSKU().equalsIgnoreCase(ussid))
			//			{
			//				final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();
			//				if (StringUtils.isNotEmpty(subOrderEntry.getOrderLineId()))
			//				{
			//					sendTicketLineItemData.setLineItemId(subOrderEntry.getOrderLineId());
			//				}
			//				else
			//				{
			//					sendTicketLineItemData.setLineItemId(subOrderEntry.getTransactionId());
			//				}
			//				if (ticketTypeCode.equalsIgnoreCase("C"))
			//				{
			//					sendTicketLineItemData.setCancelReasonCode(reasonCode);
			//					sendTicketRequestData.setRefundType(refundType);
			//				}
			//				else
			//				{
			//					sendTicketLineItemData.setReturnReasonCode(reasonCode);
			//					sendTicketRequestData.setRefundType(refundType);
			//					sendTicketRequestData.setReturnCategory("RSP");
			//				}
			//				lineItemDataList.add(sendTicketLineItemData);
			//			}
			//
			//			if (null != subOrderEntry.getAssociatedItems() && !subOrderEntry.getAssociatedItems().isEmpty())
			//			{
			//				for (final OrderEntryData eachAssociatedEntry : assosiatedEntryList)
			//				{
			//					final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();
			//					if (StringUtils.isNotEmpty(eachAssociatedEntry.getOrderLineId()))
			//					{
			//						sendTicketLineItemData.setLineItemId(eachAssociatedEntry.getOrderLineId());
			//					}
			//					else
			//					{
			//						sendTicketLineItemData.setLineItemId(eachAssociatedEntry.getTransactionId());
			//					}
			//					if (ticketTypeCode.equalsIgnoreCase("C"))
			//					{
			//						sendTicketLineItemData.setCancelReasonCode(reasonCode);
			//					}
			//					else
			//					{
			//						sendTicketLineItemData.setReturnReasonCode(reasonCode);
			//						sendTicketRequestData.setRefundType(refundType);
			//						sendTicketRequestData.setReturnCategory("RSP");
			//					}
			//					lineItemDataList.add(sendTicketLineItemData);
			//				}
			//			}

			sendTicketRequestData.setCustomerID(customerData.getUid());
			sendTicketRequestData.setLineItemDataList(lineItemDataList);
			sendTicketRequestData.setOrderId(subOrderModel.getParentReference().getCode());
			sendTicketRequestData.setSubOrderId(subOrderDetails.getCode());
			sendTicketRequestData.setTicketType(ticketTypeCode);

			final String asyncEnabled = configurationService.getConfiguration()
					.getString(MarketplacecommerceservicesConstants.ASYNC_ENABLE).trim();
			//create ticket only if async is not working
			if (asyncEnabled.equalsIgnoreCase("N"))
			{
				ticketCreate.ticketCreationModeltoWsDTO(sendTicketRequestData);
			}
			else
			{
				// CRM ticket Cron JOB data preparation
				saveTicketDetailsInCommerce(sendTicketRequestData);
			}

			ticketCreationStatus = true;

		}
		catch (final JAXBException ex)
		{
			LOG.error(" >> Exception occured while CRM ticket creation JAXBException", ex);
		}
		catch (final Exception ex)
		{
			LOG.error(" >> Exception occured while CRM ticket creation", ex);
		}

		return ticketCreationStatus;
	}


	/**
	 * Return Pincode Serviceabilty And CRM Ticket Creation
	 *
	 * @return boolean
	 */
	@Override
	public boolean createTicketInCRM(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String ticketTypeCode, final String reasonCode, final String refundType, final String ussid,
			final CustomerData customerData, final OrderModel subOrderModel, final ReturnItemAddressData returnAddress)
	{
		boolean ticketCreationStatus = false;

		try
		{
			final String transactionId = subOrderEntry.getTransactionId();
			final List<SendTicketLineItemData> lineItemDataList = new ArrayList<SendTicketLineItemData>();
			final SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();
			final ReturnAddressInfo addressInfo = new ReturnAddressInfo();
			final String pinCode = returnAddress.getPincode();
			final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, subOrderEntry.getTransactionId());
			for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntries)
			{
				final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();
				sendTicketLineItemData.setLineItemId(abstractOrderEntryModel.getOrderLineId());
				if (ticketTypeCode.equalsIgnoreCase("R"))
				{
					sendTicketLineItemData.setReturnReasonCode(reasonCode);
					sendTicketRequestData.setRefundType(refundType);



					boolean returnLogisticsCheck = true; //Start

					final List<ReturnLogisticsResponseData> returnLogisticsRespList = checkReturnLogistics(subOrderDetails, pinCode,
							transactionId);
					if (CollectionUtils.isNotEmpty(returnLogisticsRespList))
					{
						for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
						{
							if (StringUtils.isNotEmpty(response.getIsReturnLogisticsAvailable())
									&& response.getIsReturnLogisticsAvailable().equalsIgnoreCase("N"))
							{
								returnLogisticsCheck = false;
								break;
							}
						}
					}
					else
					{
						returnLogisticsCheck = false;
					}
					LOG.info(">>createTicketInCRM >> Setting Type of Return :" + returnLogisticsCheck);
					if (returnLogisticsCheck)
					{
						//LOG.info("Setting Type of Return::::::" + returnLogisticsCheck);
						sendTicketRequestData.setTicketSubType("RSP");
					}
					else
					{
						//LOG.info("Setting Type of Return::::::" +returnLogisticsCheck);
						sendTicketRequestData.setTicketSubType("RSS");
					}

					//lineItemDataList.add(sendTicketLineItemData);
					//End
				}

				lineItemDataList.add(sendTicketLineItemData);
			}
			if (!((sendTicketRequestData.getTicketSubType()).equals("RSS")))
			{
				addressInfo.setShippingFirstName(returnAddress.getFirstName());
				addressInfo.setShippingLastName(returnAddress.getLastName());
				addressInfo.setPhoneNo(returnAddress.getMobileNo());
				addressInfo.setAddress1(returnAddress.getAddressLane1());
				addressInfo.setAddress2(returnAddress.getAddressLane2());
				addressInfo.setAddress3(returnAddress.getCity());
				addressInfo.setCountry(returnAddress.getCountry());
				addressInfo.setCity(returnAddress.getCity());
				addressInfo.setState(returnAddress.getState());
				addressInfo.setPincode(returnAddress.getPincode());
				addressInfo.setLandmark(returnAddress.getLandmark());
			}

			sendTicketRequestData.setCustomerID(customerData.getUid());
			sendTicketRequestData.setLineItemDataList(lineItemDataList);
			sendTicketRequestData.setOrderId(subOrderModel.getParentReference().getCode());
			sendTicketRequestData.setSubOrderId(subOrderDetails.getCode());
			sendTicketRequestData.setTicketType(ticketTypeCode);
			sendTicketRequestData.setAddressInfo(addressInfo);

			final String asyncEnabled = configurationService.getConfiguration()
					.getString(MarketplacecommerceservicesConstants.ASYNC_ENABLE).trim();
			//create ticket only if async is not working
			if (asyncEnabled.equalsIgnoreCase("N"))
			{
				ticketCreate.ticketCreationModeltoWsDTO(sendTicketRequestData);
			}
			else
			{
				// CRM ticket Cron JOB data preparation
				saveTicketDetailsInCommerce(sendTicketRequestData);
			}

			ticketCreationStatus = true;

		}
		catch (final JAXBException ex)
		{
			LOG.error(" >> Exception occured while CRM ticket creation in createTicketInCRM JaxbException", ex);
		}
		catch (final Exception ex)
		{
			LOG.error(" >> Exception occured while CRM ticket creation in createTicketInCRM", ex);
		}

		return ticketCreationStatus;
	}




	/**
	 * @param sendTicketRequestData
	 */
	private void saveTicketDetailsInCommerce(final SendTicketRequestData sendTicketRequestData)
	{
		String crmRequest = null;

		final CRMTicketDetailModel ticket = modelService.create(CRMTicketDetailModel.class);
		if (null != sendTicketRequestData.getCustomerID())
		{
			ticket.setCustomerID(sendTicketRequestData.getCustomerID());
			LOG.debug("ticket create: customer Id>>>>> " + sendTicketRequestData.getCustomerID());
		}
		if (null != sendTicketRequestData.getOrderId())
		{
			ticket.setOrderId(sendTicketRequestData.getOrderId());
			LOG.debug("ticket create:order Id>>>>> " + sendTicketRequestData.getOrderId());
		}
		if (null != sendTicketRequestData.getSubOrderId())
		{
			ticket.setSubOrderId(sendTicketRequestData.getSubOrderId());
			LOG.debug("ticket create:suborder Id>>>>> " + sendTicketRequestData.getSubOrderId());
		}

		if (null != sendTicketRequestData.getTicketType())
		{
			ticket.setTicketType(sendTicketRequestData.getTicketType());
		}
		if (null != sendTicketRequestData.getRefundType())
		{
			ticket.setRefundType(sendTicketRequestData.getRefundType());
		}
		if (null != sendTicketRequestData.getReturnCategory())
		{
			ticket.setReturnCategory(sendTicketRequestData.getReturnCategory());
		}
		if (null != sendTicketRequestData.getAddressInfo().getAddress1())
		{
			ticket.setAddress1(sendTicketRequestData.getAddressInfo().getAddress1());
		}
		if (null != sendTicketRequestData.getAddressInfo().getAddress2())
		{
			ticket.setAddress2(sendTicketRequestData.getAddressInfo().getAddress2());
		}
		if (null != sendTicketRequestData.getAddressInfo().getAddress3())
		{
			ticket.setAddress3(sendTicketRequestData.getAddressInfo().getAddress3());
		}
		if (null != sendTicketRequestData.getAddressInfo().getCity())
		{
			ticket.setCity(sendTicketRequestData.getAddressInfo().getCity());
		}
		if (null != sendTicketRequestData.getAddressInfo().getCountry())
		{
			ticket.setCountry(sendTicketRequestData.getAddressInfo().getCountry());
		}
		if (null != sendTicketRequestData.getAddressInfo().getLandmark())
		{
			ticket.setLandmark(sendTicketRequestData.getAddressInfo().getLandmark());
		}
		if (null != sendTicketRequestData.getAddressInfo().getPhoneNo())
		{
			ticket.setPhoneNo(sendTicketRequestData.getAddressInfo().getPhoneNo());
		}
		if (null != sendTicketRequestData.getAddressInfo().getPincode())
		{
			ticket.setPincode(sendTicketRequestData.getAddressInfo().getPincode());
		}
		if (null != sendTicketRequestData.getAddressInfo().getState())
		{
			ticket.setState(sendTicketRequestData.getAddressInfo().getState());
		}
		if (null != sendTicketRequestData.getAddressInfo().getShippingFirstName())
		{
			ticket.setShippingFirstName(sendTicketRequestData.getAddressInfo().getShippingFirstName());
		}
		if (null != sendTicketRequestData.getAddressInfo().getShippingLastName())
		{
			ticket.setShippingLastName(sendTicketRequestData.getAddressInfo().getShippingLastName());
		}

		final TicketMasterXMLData ticketXmlData = ticketCreate.ticketCreationModeltoXMLData(sendTicketRequestData);
		if (ticketXmlData != null)
		{
			try
			{
				crmRequest = ticketCreate.createCRMRequestXml(ticketXmlData);
			}
			catch (final JAXBException ex)
			{
				LOG.info(MarketplacecclientservicesConstants.EXCEPTION_IS);

			}
			ticket.setCRMRequest(crmRequest);
		}
		modelService.save(ticket);
	}

	/**
	 * @param subOrderDetails
	 * @param reasonCode
	 * @param subOrderModel
	 * @param subOrderEntry
	 *
	 */
	private boolean initiateCancellation(final String ticketTypeCode, final OrderData subOrderDetails,
			final OrderEntryData subOrderEntry, final OrderModel subOrderModel, final String reasonCode)
	{
		boolean cancellationInitiated = false;

		try
		{
			if ("C".equalsIgnoreCase(ticketTypeCode))
			{
				//final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, subOrderEntry.getTransactionId());
				//for (final AbstractOrderEntryModel orderEntryData : orderEntries)
				//{
				//final MplOrderCancelRequest orderCancelRequest = buildCancelRequest(orderEntryData, reasonCode, subOrderDetails,subOrderModel);
				//requestOrderCancel(subOrderDetails, subOrderModel, orderCancelRequest);
				//}
				//TISEE-5446
				final MplOrderCancelRequest orderCancelRequest = buildCancelRequest(reasonCode, subOrderModel,
						subOrderEntry.getTransactionId());
				requestOrderCancel(subOrderDetails, subOrderModel, orderCancelRequest);
			}
			cancellationInitiated = true;
		}
		catch (final ModelSavingException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return cancellationInitiated;
	}

	/**
	 *
	 *
	 * @param orderLineRequest
	 *
	 */
	private boolean cancelOrderInOMS(final MplCancelOrderRequest orderLineRequest, boolean cancelOrRetrnanable,
			final boolean isReturn)
	{
		List<MplOrderIsCancellableResponse.OrderLine> responseList = new ArrayList<MplOrderIsCancellableResponse.OrderLine>();

		try
		{

			LOG.debug("Step 3:*********************************** Calling OMS for return?" + isReturn);

			final MplOrderIsCancellableResponse response = mplOrderCancelClientService.orderCancelDataToOMS(orderLineRequest);
			//			if (isReturn) //for return do not need to execute the below code
			//			{
			//				return cancelOrRetrnanable;
			//			}

			// below portion of code valid for cancel only
			if (null != response)
			{
				responseList = response.getOrderLine();
			}
			else
			{
				cancelOrRetrnanable = false;
			}

			for (final MplOrderIsCancellableResponse.OrderLine orderLine : responseList)
			{
				if (orderLine.isIsCancellable() != null && orderLine.isIsCancellable().equalsIgnoreCase("N"))
				{
					cancelOrRetrnanable = false;
					break;
				}
			}
		}
		catch (final Exception e)
		{
			cancelOrRetrnanable = false;
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return cancelOrRetrnanable;
	}

	/**
	 * @param ticketTypeCode
	 * @param subOrderEntry
	 * @param subOrderModel
	 * @param reasonCode
	 *
	 *
	 */
	private MplCancelOrderRequest populateOrderLineData(final OrderEntryData subOrderEntry, final String ticketTypeCode,
			final OrderModel subOrderModel, final String reasonCode) throws Exception
	{

		final MplCancelOrderRequest orderLineRequest = new MplCancelOrderRequest();
		final List<MplCancelOrderRequest.OrderLine> orderLineList = new ArrayList<MplCancelOrderRequest.OrderLine>();

		final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, subOrderEntry.getTransactionId());

		for (final AbstractOrderEntryModel subEntry : orderEntries)
		{
			final MplCancelOrderRequest.OrderLine orderLineData = new MplCancelOrderRequest.OrderLine();
			orderLineData.setOrderId(subOrderModel.getParentReference().getCode());
			orderLineData.setReasonCode(reasonCode);
			orderLineData.setRequestID(subEntry.getSelectedUSSID() + MarketplacecommerceservicesConstants.EMPTY
					+ System.currentTimeMillis());//TODO: Change with a valid request ID
			orderLineData.setReturnCancelFlag(ticketTypeCode);
			if (ticketTypeCode.equalsIgnoreCase("C"))
			{
				orderLineData.setReturnCancelRemarks(getReasonForCancellation(reasonCode));
			}
			else if (ticketTypeCode.equalsIgnoreCase("R"))
			{
				orderLineData.setReturnCancelRemarks(getReasonDesc(reasonCode));
			}
			if (StringUtils.isNotEmpty(subEntry.getOrderLineId()))
			{
				orderLineData.setTransactionId(subEntry.getOrderLineId());
			}
			else if (StringUtils.isNotEmpty(subEntry.getTransactionID()))
			{
				orderLineData.setTransactionId(subEntry.getTransactionID());
			}

			orderLineList.add(orderLineData);
		}
		orderLineRequest.setOrderLine(orderLineList);
		return orderLineRequest;
	}

	// Return Item Pincode Property
	private MplCancelOrderRequest populateOrderLineData(final OrderEntryData subOrderEntry, final String ticketTypeCode,
			final OrderModel subOrderModel, final String reasonCode, final String pincode) throws Exception
	{

		final MplCancelOrderRequest orderLineRequest = new MplCancelOrderRequest();
		final List<MplCancelOrderRequest.OrderLine> orderLineList = new ArrayList<MplCancelOrderRequest.OrderLine>();

		final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, subOrderEntry.getTransactionId());

		for (final AbstractOrderEntryModel subEntry : orderEntries)
		{
			final MplCancelOrderRequest.OrderLine orderLineData = new MplCancelOrderRequest.OrderLine();
			orderLineData.setOrderId(subOrderModel.getParentReference().getCode());
			orderLineData.setReasonCode(reasonCode);
			orderLineData.setRequestID(subEntry.getSelectedUSSID() + MarketplacecommerceservicesConstants.EMPTY
					+ System.currentTimeMillis());//TODO: Change with a valid request ID
			orderLineData.setReturnCancelFlag(ticketTypeCode);
			if (ticketTypeCode.equalsIgnoreCase("C"))
			{
				orderLineData.setReturnCancelRemarks(getReasonForCancellation(reasonCode));
			}
			else if (ticketTypeCode.equalsIgnoreCase("R"))
			{
				orderLineData.setReturnCancelRemarks(getReasonDesc(reasonCode));
				orderLineData.setPinCode(pincode);
			}
			if (StringUtils.isNotEmpty(subEntry.getOrderLineId()))
			{
				orderLineData.setTransactionId(subEntry.getOrderLineId());
			}
			else if (StringUtils.isNotEmpty(subEntry.getTransactionID()))
			{
				orderLineData.setTransactionId(subEntry.getTransactionID());
			}

			orderLineList.add(orderLineData);
		}
		orderLineRequest.setOrderLine(orderLineList);
		return orderLineRequest;
	}


	/**
	 * @param reasonCode
	 * @param subOrderModel
	 * @throws OrderCancelException
	 */
	private MplOrderCancelRequest buildCancelRequest(final String reasonCode, final OrderModel subOrderModel,
			final String transactionId) throws OrderCancelException, Exception
	{

		final List orderCancelEntries = new ArrayList();

		//Get the reason from Global Code master
		String reasonDescription = null;
		final List<CancellationReasonModel> cancellationReasonList = mplOrderService.getCancellationReason();
		for (final CancellationReasonModel cancellationReason : cancellationReasonList)
		{
			if (cancellationReason.getReasonCode().equalsIgnoreCase(reasonCode))
			{
				if (StringUtils.isNotEmpty(cancellationReason.getReasonDescription()))
				{
					reasonDescription = cancellationReason.getReasonDescription();
				}
				break;
			}
		}


		final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, transactionId);
		for (final AbstractOrderEntryModel orderEntryData : orderEntries)
		{
			final MplOrderCancelEntry orderCancelEntryData = new MplOrderCancelEntry(orderEntryData, orderEntryData.getQuantity()
					.longValue(), reasonDescription, reasonDescription);
			orderCancelEntries.add(orderCancelEntryData);
		}

		LOG.debug(">>> buildCancelRequest : orderCancelEntries size " + orderCancelEntries.size());

		//create order cancel request
		final CancelReason reason = CancelReason.valueOf(reasonDescription);
		final MplOrderCancelRequest orderCancelRequest = new MplOrderCancelRequest(subOrderModel, orderCancelEntries);
		orderCancelRequest.setCancelReason(reason);
		orderCancelRequest.setNotes(reasonDescription);
		double refundAmount = 0D;
		for (final OrderCancelEntry orderCancelEntry : orderCancelRequest.getEntriesToCancel())
		{
			final AbstractOrderEntryModel orderEntry = orderCancelEntry.getOrderEntry();
			final List<PaymentTransactionModel> tranactions = new ArrayList<PaymentTransactionModel>(
					subOrderModel.getPaymentTransactions());

			if (CollectionUtils.isNotEmpty(tranactions))
			{
				for (final PaymentTransactionModel transaction : tranactions)
				{
					if (CollectionUtils.isNotEmpty(transaction.getEntries()))
					{
						for (final PaymentTransactionEntryModel entry : transaction.getEntries())
						{
							if (entry.getPaymentMode() != null && entry.getPaymentMode().getMode() != null
									&& entry.getPaymentMode().getMode().equalsIgnoreCase(MarketplaceFacadesConstants.PAYMENT_METHOS_COD))
							{
								orderCancelRequest.setAmountToRefund(NumberUtils.DOUBLE_ZERO);
								return orderCancelRequest;
							}
						}
					}
				}
			}

			double deliveryCost = 0D;
			if (orderEntry.getCurrDelCharge() != null)
			{
				deliveryCost = orderEntry.getCurrDelCharge().doubleValue();
			}


			refundAmount = refundAmount + orderEntry.getNetAmountAfterAllDisc().doubleValue() + deliveryCost;
			refundAmount = mplJusPayRefundService.validateRefundAmount(refundAmount, subOrderModel);

		}
		//Setting Refund Amount
		orderCancelRequest.setAmountToRefund(new Double(refundAmount));
		return orderCancelRequest;
	}


	/*
	 * private MplOrderCancelRequest buildCancelRequest(final AbstractOrderEntryModel orderEntryData, final String
	 * reasonCode, final OrderData subOrderDetails, final OrderModel subOrderModel) throws OrderCancelException {
	 *
	 * final List orderCancelEntries = new ArrayList();
	 *
	 * //Get the reason from Global Code master String reasonDescription = null; final List<CancellationReasonModel>
	 * cancellationReasonList = mplOrderService.getCancellationReason(); for (final CancellationReasonModel
	 * cancellationReason : cancellationReasonList) { if
	 * (cancellationReason.getReasonCode().equalsIgnoreCase(reasonCode)) { if
	 * (StringUtils.isNotEmpty(cancellationReason.getReasonDescription())) { reasonDescription =
	 * cancellationReason.getReasonDescription(); } break; } } //End //create order cancel entry final
	 * MplOrderCancelEntry orderCancelEntryData = new MplOrderCancelEntry(orderEntryData,
	 * orderEntryData.getQuantity().longValue(), reasonDescription, reasonDescription);
	 * orderCancelEntries.add(orderCancelEntryData); //create order cancel request final CancelReason reason =
	 * CancelReason.valueOf(reasonDescription); final MplOrderCancelRequest orderCancelRequest = new
	 * MplOrderCancelRequest(subOrderModel, orderCancelEntries); orderCancelRequest.setCancelReason(reason);
	 * orderCancelRequest.setNotes(reasonDescription); double refundAmount = 0D; for (final OrderCancelEntry
	 * orderCancelEntry : orderCancelRequest.getEntriesToCancel()) { final AbstractOrderEntryModel orderEntry =
	 * orderCancelEntry.getOrderEntry(); final List<PaymentTransactionModel> tranactions = new
	 * ArrayList<PaymentTransactionModel>( subOrderModel.getPaymentTransactions());
	 *
	 * if (CollectionUtils.isNotEmpty(tranactions)) { for (final PaymentTransactionModel transaction : tranactions) { if
	 * (CollectionUtils.isNotEmpty(transaction.getEntries())) { for (final PaymentTransactionEntryModel entry :
	 * transaction.getEntries()) { if (entry.getPaymentMode() != null && entry.getPaymentMode().getMode() != null &&
	 * entry.getPaymentMode().getMode().equalsIgnoreCase(MarketplaceFacadesConstants.PAYMENT_METHOS_COD)) {
	 * orderCancelRequest.setAmountToRefund(NumberUtils.DOUBLE_ZERO); return orderCancelRequest; } } } } }
	 *
	 * double deliveryCost = 0D; if (orderEntry.getCurrDelCharge() != null) { deliveryCost =
	 * orderEntry.getCurrDelCharge().doubleValue(); }
	 *
	 * refundAmount = orderEntryData.getNetAmountAfterAllDisc().doubleValue() + deliveryCost; } //Setting Refund Amount
	 * orderCancelRequest.setAmountToRefund(new Double(refundAmount)); return orderCancelRequest; }
	 */



	/*	*//**
	 * @param subOrderModel
	 * @param subOrderDetails
	 * @param orderCancelRequest
	 * @param sendTicketRequestData
	 * @throws OrderCancelRecordsHandlerException
	 *
	 */
	/*
	 * private void createCancelEntriesAndRefund(final MplOrderCancelRequest orderCancelRequest, final OrderData
	 * subOrderDetails, final OrderModel subOrderModel, final SendTicketRequestData sendTicketRequestData) throws
	 * OrderCancelRecordsHandlerException { final OrderCancelRecordEntryModel result =
	 * this.getOrderCancelRecordsHandler().createRecordEntry(orderCancelRequest, userService.getCurrentUser());
	 * //Initiate Refund initiateRefund(subOrderDetails, subOrderModel, result);
	 *
	 * }
	 */


	/**
	 * @param subOrderModel
	 * @param subOrderDetails
	 * @param orderCancelRequest
	 * @throws OrderCancelException
	 *
	 */
	private void requestOrderCancel(final OrderData subOrderDetails, final OrderModel subOrderModel,
			final MplOrderCancelRequest orderCancelRequest) throws OrderCancelException
	{
		//cancel Order
		final OrderCancelRecordEntryModel orderRequestRecord = orderCancelService.requestOrderCancel(orderCancelRequest,
				userService.getCurrentUser());

		if (OrderCancelEntryStatus.DENIED.equals(orderRequestRecord.getCancelResult()))
		{
			final String orderCode = subOrderDetails.getCode();

			String message = MarketplacecommerceservicesConstants.EMPTY;
			if (orderRequestRecord.getRefusedMessage() != null)
			{
				message = message + orderRequestRecord.getRefusedMessage();
			}
			if (orderRequestRecord.getFailedMessage() != null)
			{
				message = message + orderRequestRecord.getFailedMessage();
			}

			throw new OrderCancelException(orderCode, message);
		}
		else
		{
			initiateRefund(subOrderModel, orderRequestRecord);
		}
	}

	/**
	 * @param subOrderModel
	 * @param orderRequestRecord
	 */
	private void initiateRefund(final OrderModel subOrderModel, final OrderCancelRecordEntryModel orderRequestRecord)
	{

		PaymentTransactionModel paymentTransactionModel = null;
		if (orderRequestRecord.getRefundableAmount() != null
				&& orderRequestRecord.getRefundableAmount().doubleValue() > NumberUtils.DOUBLE_ZERO.doubleValue())
		{
			//TISSIT-1801
			final String uniqueRequestId = mplJusPayRefundService.getRefundUniqueRequestId();
			try
			{
				LOG.debug("****** initiateRefund Step 1 >> Begin >> Calling for prepaid for " + orderRequestRecord.getCode());
				paymentTransactionModel = mplJusPayRefundService.doRefund(subOrderModel, orderRequestRecord.getRefundableAmount()
						.doubleValue(), PaymentTransactionType.CANCEL, uniqueRequestId);

				if (null != paymentTransactionModel)
				{
					mplJusPayRefundService.attachPaymentTransactionModel(subOrderModel, paymentTransactionModel);

					if (CollectionUtils.isNotEmpty(orderRequestRecord.getOrderEntriesModificationEntries()))
					{
						for (final OrderEntryModificationRecordEntryModel modificationEntry : orderRequestRecord
								.getOrderEntriesModificationEntries())
						{
							final OrderEntryModel orderEntry = modificationEntry.getOrderEntry();
							ConsignmentStatus newStatus = null;
							if (orderEntry != null)
							{
								//TISPRO-216 Starts
								double refundAmount = 0D;
								final Double deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge()
										: NumberUtils.DOUBLE_ZERO;

								refundAmount = orderEntry.getNetAmountAfterAllDisc().doubleValue() + deliveryCost.doubleValue();
								refundAmount = mplJusPayRefundService.validateRefundAmount(refundAmount, subOrderModel);
								//TISPRO-216 Ends

								if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
										MarketplacecommerceservicesConstants.SUCCESS))
								{
									newStatus = ConsignmentStatus.ORDER_CANCELLED;
								}
								else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(), "PENDING"))
								{
									newStatus = ConsignmentStatus.REFUND_INITIATED;
									final RefundTransactionMappingModel refundTransactionMappingModel = modelService
											.create(RefundTransactionMappingModel.class);
									refundTransactionMappingModel.setRefundedOrderEntry(orderEntry);
									refundTransactionMappingModel.setJuspayRefundId(paymentTransactionModel.getCode());
									refundTransactionMappingModel.setCreationtime(new Date());
									refundTransactionMappingModel.setRefundType(JuspayRefundType.CANCELLED);
									refundTransactionMappingModel.setRefundAmount(new Double(refundAmount));//TISPRO-216 : Refund amount Set in RTM
									modelService.save(refundTransactionMappingModel);
								}
								else
								{
									newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
								}


								orderEntry.setRefundedDeliveryChargeAmt(deliveryCost);
								orderEntry.setCurrDelCharge(new Double(0D));

								//Start TISPRD-871
								if (newStatus.equals(ConsignmentStatus.ORDER_CANCELLED))
								{
									orderEntry.setJuspayRequestId(uniqueRequestId);
								}
								//End TISPRD-871

								modelService.save(orderEntry);
								LOG.debug("****** initiateRefund : Step 3  >>Payment transaction mode is not null >> Calling OMS with status as received from JUSPAY "
										+ newStatus.getCode());

								//mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,orderRequestRecord.getRefundableAmount(), newStatus);

								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
										Double.valueOf(refundAmount), newStatus);

							}
						}
					}
				}
				else
				{
					LOG.debug("****** initiateRefund >>Payment transaction mode is null");
					////TISSIT-1801
					mplJusPayRefundService.createCancelRefundPgErrorEntry(orderRequestRecord, PaymentTransactionType.CANCEL,
							JuspayRefundType.CANCELLED, uniqueRequestId);
				}
			}
			catch (final Exception e)
			{
				LOG.error(">>>> *****************initiateRefund*********** Exception occured " + e.getMessage(), e);
				////TISSIT-1801
				mplJusPayRefundService.createCancelRefundExceptionEntry(orderRequestRecord, PaymentTransactionType.CANCEL,
						JuspayRefundType.CANCELLED, uniqueRequestId);
			}
		}
		else
		{// Case of COD.

			LOG.debug("****** initiateRefund >> Begin >> OMS will not be called for COD  ");
			final double refundedAmount = 0D;
			paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(orderRequestRecord.getOriginalVersion()
					.getOrder(), MarketplacecommerceservicesConstants.FAILURE_FLAG, new Double(refundedAmount),
					PaymentTransactionType.CANCEL, MarketplacecommerceservicesConstants.FAILURE_FLAG, UUID.randomUUID().toString());
			mplJusPayRefundService.attachPaymentTransactionModel(orderRequestRecord.getOriginalVersion().getOrder(),
					paymentTransactionModel);
		}
		orderRequestRecord.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
		orderRequestRecord.setTransactionCode(paymentTransactionModel != null ? paymentTransactionModel.getCode()
				: MarketplacecommerceservicesConstants.EMPTY);
		modelService.save(orderRequestRecord);
	}

	@Override
	public List<ReturnLogisticsResponseData> checkReturnLogistics(final OrderData orderDetails)
	{
		try
		{
			final List<OrderEntryData> entries = orderDetails.getEntries();
			final List<ReturnLogistics> returnLogisticsList = new ArrayList<ReturnLogistics>();
			String transactionId = "";
			for (final OrderEntryData eachEntry : entries)
			{
				final ReturnLogistics returnLogistics = new ReturnLogistics();
				//TISEE-5557
				if (!(eachEntry.isGiveAway() || eachEntry.isIsBOGOapplied()))
				//	|| (null != eachEntry.getAssociatedItems() && !eachEntry.getAssociatedItems().isEmpty())))
				{
					returnLogistics.setOrderId(orderDetails.getCode());
					if (StringUtils.isNotEmpty(eachEntry.getOrderLineId()))
					{
						transactionId = eachEntry.getOrderLineId();
						returnLogistics.setTransactionId(eachEntry.getOrderLineId());
					}
					else if (StringUtils.isNotEmpty(eachEntry.getTransactionId()))
					{
						transactionId = eachEntry.getTransactionId();
						returnLogistics.setTransactionId(eachEntry.getTransactionId());
					}
				}
				returnLogisticsList.add(returnLogistics);
			}
			final List<OrderLineDataResponse> responseList = new ArrayList<OrderLineDataResponse>();
			final List<ReturnLogisticsResponseData> returnLogRespDataList = new ArrayList<ReturnLogisticsResponseData>();
			if (!returnLogisticsList.isEmpty())
			{
				final ReturnLogisticsResponse response = returnLogistics.returnLogisticsCheck(returnLogisticsList);
				if (null != response.getOrderlines())
				{
					for (final OrderLineDataResponse orderLine : response.getOrderlines())
					{
						final ReturnLogisticsResponseData returnLogRespData = new ReturnLogisticsResponseData();
						if (null != orderLine.getOrderId())
						{
							returnLogRespData.setOrderId(orderLine.getOrderId());
						}
						if (null != orderLine.getTransactionId())
						{
							returnLogRespData.setTransactionId(orderLine.getTransactionId());
						}
						if (null != orderLine.getIsReturnLogisticsAvailable())
						{
							returnLogRespData.setIsReturnLogisticsAvailable(orderLine.getIsReturnLogisticsAvailable());
							if (orderLine.getIsReturnLogisticsAvailable().equalsIgnoreCase("Y"))
							{
								returnLogRespData
										.setResponseMessage(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_MESSAGE);
								returnLogRespData
										.setResponseDescription(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_DESC);
							}
							else
							{
								returnLogRespData
										.setResponseMessage(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_MESSAGE);
								returnLogRespData
										.setResponseDescription(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_DESC);
							}

						}
						returnLogRespDataList.add(returnLogRespData);
						responseList.add(orderLine);
					}
				}
				else
				{
					//TISEE-5357
					LOG.debug("*****Reverse logistics availabilty  Response orderline is null*********");
					final ReturnLogisticsResponseData returnLogRespData = new ReturnLogisticsResponseData();
					returnLogRespData.setIsReturnLogisticsAvailable("N");
					if (null != orderDetails.getCode())
					{
						returnLogRespData.setOrderId(orderDetails.getCode());
						returnLogRespData
								.setResponseMessage(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_MESSAGE);
						returnLogRespData
								.setResponseDescription(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_DESC);
					}
					returnLogRespData.setTransactionId(transactionId);
					returnLogRespDataList.add(returnLogRespData);
				}
			}
			return returnLogRespDataList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * Return Logistics Serviceability checking
	 *
	 * @return List
	 */
	@Override
	public List<ReturnLogisticsResponseData> checkReturnLogistics(final OrderData orderDetails, final String pincode,
			final String transId)
	{
		try
		{
			final List<OrderEntryData> entries = orderDetails.getEntries();
			final OrderModel orderModel = orderModelService.getOrder(orderDetails.getCode());
			final List<ReturnLogistics> returnLogisticsList = new ArrayList<ReturnLogistics>();
			String returningTransactionId;
			//returningTransactionId = sessionService.getAttribute("transactionId"); // Commented for Bulk Return Initiation
			returningTransactionId = transId;
			String transactionId = "";
			for (final OrderEntryData eachEntry : entries)
			{
				final ReturnLogistics returnLogistics = new ReturnLogistics();
				//TISEE-5557
				if (!(eachEntry.isGiveAway() || eachEntry.isIsBOGOapplied()))
				//	|| (null != eachEntry.getAssociatedItems() && !eachEntry.getAssociatedItems().isEmpty())))
				{
					returnLogistics.setOrderId(orderModel.getParentReference().getCode());
					if (null != pincode)
					{
						returnLogistics.setPinCode(pincode);
					}
					if (StringUtils.isNotEmpty(eachEntry.getOrderLineId()))
					{
						transactionId = eachEntry.getOrderLineId();
						returnLogistics.setTransactionId(eachEntry.getOrderLineId());
					}
					else if (StringUtils.isNotEmpty(eachEntry.getTransactionId()))
					{
						transactionId = eachEntry.getTransactionId();
						returnLogistics.setTransactionId(eachEntry.getTransactionId());
					}
				}
				returnLogisticsList.add(returnLogistics);
			}
			final List<OrderLineDataResponse> responseList = new ArrayList<OrderLineDataResponse>();
			final List<ReturnLogisticsResponseData> returnLogRespDataList = new ArrayList<ReturnLogisticsResponseData>();
			if (!returnLogisticsList.isEmpty())
			{
				final ReturnLogisticsResponse response = returnLogistics.returnLogisticsCheck(returnLogisticsList);
				if (null != response.getOrderlines())
				{
					for (final OrderLineDataResponse orderLine : response.getOrderlines())
					{
						final ReturnLogisticsResponseData returnLogRespData = new ReturnLogisticsResponseData();
						if (null != orderLine.getOrderId())
						{
							returnLogRespData.setOrderId(orderLine.getOrderId());
						}
						if (null != orderLine.getTransactionId())
						{
							returnLogRespData.setTransactionId(orderLine.getTransactionId());
						}
						if (null != orderLine.getIsReturnLogisticsAvailable())
						{

							if (orderLine.getTransactionId().trim().equalsIgnoreCase(returningTransactionId.trim()))
							{

								returnLogRespData.setIsReturnLogisticsAvailable(orderLine.getIsReturnLogisticsAvailable());

								if (orderLine.getIsReturnLogisticsAvailable().equalsIgnoreCase("Y"))
								{
									returnLogRespData
											.setResponseMessage(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_MESSAGE);
									returnLogRespData
											.setResponseDescription(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_DESC);
								}
								else
								{
									returnLogRespData
											.setResponseMessage(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_MESSAGE);
									returnLogRespData
											.setResponseDescription(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_DESC);
								}

								returnLogRespDataList.add(returnLogRespData);
								responseList.add(orderLine);

							}

						}
					}
				}
				else
				{
					//TISEE-5357
					LOG.debug("*****Reverse logistics availabilty  Response orderline is null*********");
					final ReturnLogisticsResponseData returnLogRespData = new ReturnLogisticsResponseData();
					returnLogRespData.setIsReturnLogisticsAvailable("N");
					if (null != orderDetails.getCode())
					{
						returnLogRespData.setOrderId(orderDetails.getCode());
						returnLogRespData
								.setResponseMessage(MarketplacecommerceservicesConstants.REVERCE_LOGISTIC_PINCODE_SERVICEABLE_NOTAVAIL_MESSAGE);
					}
					returnLogRespData.setTransactionId(transactionId);
					returnLogRespDataList.add(returnLogRespData);
				}
			}
			return returnLogRespDataList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}







	/*
	 * @desc Saving order history for cancellation as OMS is not sending
	 *
	 * @param subOrderData
	 *
	 * @param subOrderModel
	 */
	private void createHistoryEntry(final AbstractOrderEntryModel orderEntryModel, final OrderModel orderModel,
			final ConsignmentStatus consignmentStatus)
	{
		final OrderHistoryEntryModel historyEntryModel = modelService.create(OrderHistoryEntryModel.class);
		historyEntryModel.setDescription(consignmentStatus.getCode());
		historyEntryModel.setCreationtime(new Date());
		historyEntryModel.setLineId(orderEntryModel.getTransactionID());
		historyEntryModel.setOrder(orderModel);
		historyEntryModel.setTimestamp(new Date());
		modelService.save(historyEntryModel);
	}

	/**
	 * @param subOrderDetails
	 * @param transactionId
	 * @return List<OrderEntryData>
	 */
	private List<AbstractOrderEntryModel> associatedEntries(final OrderModel subOrderDetails, final String transactionId)
			throws Exception
	{
		final List<AbstractOrderEntryModel> orderEntries = new ArrayList<>();

		//final String associatedItemUssid = "";
		//final String productPromoCode = "";
		//TISSIT-1720
		final List<String> parentTransactionIdList = new ArrayList<String>();
		for (final AbstractOrderEntryModel subEntry : subOrderDetails.getEntries())
		{
			//Start TISPRO-249
			final String parentTransactionId = ((subEntry.getIsBOGOapplied().booleanValue() || subEntry.getGiveAway().booleanValue()) && mplOrderService
					.checkIfBuyABGetCApplied(subEntry)) ? subEntry.getBuyABGetcParentTransactionId() : subEntry
					.getParentTransactionID();
			//End TISPRO-249


			if (StringUtils.isNotEmpty(parentTransactionId)
					&& (subEntry.getIsBOGOapplied().booleanValue() || subEntry.getGiveAway().booleanValue())
					&& parentTransactionId.split(",").length > 1 && parentTransactionId.contains(transactionId))
			{
				for (final String parentTransId : parentTransactionId.split(","))
				{
					parentTransactionIdList.add(parentTransId);
				}
				parentTransactionIdList.add(subEntry.getTransactionID());
			}
			else if (StringUtils.isNotEmpty(parentTransactionId)
					&& (subEntry.getIsBOGOapplied().booleanValue() || subEntry.getGiveAway().booleanValue())
					&& parentTransactionId.split(",").length == 1 && parentTransactionId.equalsIgnoreCase(transactionId))
			{
				parentTransactionIdList.add(parentTransactionId);
				parentTransactionIdList.add(subEntry.getTransactionID());
			}
		}


		LOG.debug("*** parentTransactionIdList " + parentTransactionIdList + " parentTransactionIdList :"
				+ parentTransactionIdList.size());

		for (final AbstractOrderEntryModel subEntry : subOrderDetails.getEntries())
		{
			if (transactionId.equalsIgnoreCase(subEntry.getTransactionID())
					|| (CollectionUtils.isNotEmpty(parentTransactionIdList) && parentTransactionIdList.contains(subEntry
							.getTransactionID())))
			{
				orderEntries.add(subEntry);
			}
		}

		return orderEntries;
	}

	@Override
	public List<OrderEntryData> associatedEntriesData(final OrderModel subOrderDetails, final String transactionId)
			throws Exception
	{
		final List<OrderEntryData> entryData = new ArrayList<OrderEntryData>();
		for (final AbstractOrderEntryModel orderEntry : associatedEntries(subOrderDetails, transactionId))
		{
			entryData.add(getOrderEntryConverter().convert(orderEntry));
		}
		return entryData;
	}

	/**
	 * @description: To find the Cancellation is enabled/disabled
	 * @param: currentStatus
	 * @return: currentStatus
	 */
	protected List<OrderEntryData> associatedEntries(final OrderEntryData orderEntry, final OrderData subOrder)
	{
		final List<OrderEntryData> cancelProduct = new ArrayList<>();
		if (orderEntry.getAssociatedItems() != null)
		{
			cancelProduct.add(orderEntry);
			for (final OrderEntryData entry : subOrder.getEntries())
			{
				for (final String ussid : orderEntry.getAssociatedItems())
				{
					if (ussid.equalsIgnoreCase(entry.getSelectedUssid()))
					{
						cancelProduct.add(entry);
						break;
					}
				}
			}

		}
		else
		{
			cancelProduct.add(orderEntry);
		}
		return cancelProduct;
	}



	/**
	 * TISCR-410 : this method picks up the stage in which the order status is currently
	 *
	 * @param orderEntryStatus
	 * @return String
	 *
	 */
	@Override
	public String getOrderStatusStage(final String orderEntryStatus)
	{
		String stage = null;
		if (StringUtils.isNotEmpty(orderEntryStatus))
		{
			stage = getMplRefundService().getOrderStatusStage(orderEntryStatus);
		}
		return stage;
	}



	@Override
	public ReturnPincodeDTO checkReturnLogisticsForApp(final OrderData orderDetails, final String pincode,
			final String returntransactionId)
	{

		try
		{
			final ReturnPincodeDTO returnPincodeDTO = new ReturnPincodeDTO();
			final List<OrderEntryData> entries = orderDetails.getEntries();
			final OrderModel orderModel = orderModelService.getOrder(orderDetails.getCode());
			final List<ReturnLogistics> returnLogisticsList = new ArrayList<ReturnLogistics>();
			final String returningTransactionId = returntransactionId;
			//	returningTransactionId = sessionService.getAttribute("transactionId");
			String transactionId = "";
			for (final OrderEntryData eachEntry : entries)
			{
				final ReturnLogistics returnLogistics = new ReturnLogistics();
				//TISEE-5557
				if (!(eachEntry.isGiveAway() || eachEntry.isIsBOGOapplied()))
				//	|| (null != eachEntry.getAssociatedItems() && !eachEntry.getAssociatedItems().isEmpty())))
				{
					returnLogistics.setOrderId(orderModel.getParentReference().getCode());
					if (null != pincode)
					{
						returnLogistics.setPinCode(pincode);
					}
					if (StringUtils.isNotEmpty(eachEntry.getOrderLineId()))
					{
						transactionId = eachEntry.getOrderLineId();
						returnLogistics.setTransactionId(eachEntry.getOrderLineId());
					}
					else if (StringUtils.isNotEmpty(eachEntry.getTransactionId()))
					{
						transactionId = eachEntry.getTransactionId();
						returnLogistics.setTransactionId(eachEntry.getTransactionId());
					}
				}
				returnLogisticsList.add(returnLogistics);
			}
			final List<OrderLineDataResponse> responseList = new ArrayList<OrderLineDataResponse>();
			//final List<ReturnLogisticsResponseData> returnLogRespDataList = new ArrayList<ReturnLogisticsResponseData>();
			final List<ReturnLogisticsResponseDTO> returnLogRespDTOList = new ArrayList<ReturnLogisticsResponseDTO>();
			if (!returnLogisticsList.isEmpty())
			{
				final ReturnLogisticsResponse response = returnLogistics.returnLogisticsCheck(returnLogisticsList);
				if (null != response.getOrderlines())
				{
					for (final OrderLineDataResponse orderLine : response.getOrderlines())
					{
						//final ReturnLogisticsResponseData returnLogRespData = new ReturnLogisticsResponseData();
						final ReturnLogisticsResponseDTO returnLogisticsResponseDTO = new ReturnLogisticsResponseDTO();
						if (null != orderLine.getOrderId())
						{
							returnLogisticsResponseDTO.setOrderId(orderLine.getOrderId());
						}
						if (null != orderLine.getTransactionId())
						{
							returnLogisticsResponseDTO.setTransactionId(orderLine.getTransactionId());
						}
						if (null != orderLine.getIsReturnLogisticsAvailable())
						{

							if (orderLine.getTransactionId().trim().equalsIgnoreCase(returningTransactionId.trim()))
							{

								returnLogisticsResponseDTO.setIsReturnLogisticsAvailable(orderLine.getIsReturnLogisticsAvailable());

								if (orderLine.getIsReturnLogisticsAvailable().equalsIgnoreCase("Y"))
								{
									returnLogisticsResponseDTO
											.setResponseMessage(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_MESSAGE);
									returnLogisticsResponseDTO
											.setResponseDescription(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_DESC);
									//returnLogRespData
									//	.setResponseMessage(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_MESSAGE);
									//returnLogRespData
									//.setResponseDescription(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_AVAILABLE_RESPONSE_DESC);
								}
								else
								{
									returnLogisticsResponseDTO
											.setResponseMessage((MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_MESSAGE));
									//returnLogRespData.setResponseMessage(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_MESSAGE);
									returnLogisticsResponseDTO
											.setResponseDescription(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_DESC);
									//returnLogRespData.setResponseDescription(MarketplacecommerceservicesConstants.REVERSE_LOGISTIC_NOT_AVAILABLE_RESPONSE_DESC);
								}

								//	returnLogRespDataList.add(returnLogRespData);
								returnLogRespDTOList.add(returnLogisticsResponseDTO);
								returnPincodeDTO.setReturnLogisticsResponseDTO(returnLogRespDTOList);
								responseList.add(orderLine);

							}

						}
					}
				}
				else
				{
					//TISEE-5357
					LOG.debug("*****Reverse logistics availabilty  Response orderline is null*********");
					//final ReturnLogisticsResponseData returnLogRespData = new ReturnLogisticsResponseData();
					final ReturnLogisticsResponseDTO returnLogRespDTO = new ReturnLogisticsResponseDTO();
					//returnLogRespData.setIsReturnLogisticsAvailable("N");
					returnLogRespDTO.setIsReturnLogisticsAvailable("N");
					if (null != orderDetails.getCode())
					{
						//returnLogRespData.setOrderId(orderDetails.getCode());
						returnLogRespDTO.setOrderId(orderDetails.getCode());
						//returnLogRespData
						//	.setResponseMessage(MarketplacecommerceservicesConstants.REVERCE_LOGISTIC_PINCODE_SERVICEABLE_NOTAVAIL_MESSAGE);
						returnLogRespDTO
								.setResponseMessage(MarketplacecommerceservicesConstants.REVERCE_LOGISTIC_PINCODE_SERVICEABLE_NOTAVAIL_MESSAGE);
					}
					returnLogRespDTO.setTransactionId(transactionId);
					//returnLogRespData.setTransactionId(transactionId);
					returnLogRespDTOList.add(returnLogRespDTO);
					returnPincodeDTO.setReturnLogisticsResponseDTO(returnLogRespDTOList);
					//	returnLogRespDataList.add(returnLogRespData);
				}
			}
			return returnPincodeDTO;
			//	return returnLogRespDataList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}





	/**
	 * @return the mplSNSMobilePushService
	 */
	public MplSNSMobilePushServiceImpl getMplSNSMobilePushService()
	{
		return mplSNSMobilePushService;
	}

	/**
	 * @param mplSNSMobilePushService
	 *           the mplSNSMobilePushService to set
	 */
	public void setMplSNSMobilePushService(final MplSNSMobilePushServiceImpl mplSNSMobilePushService)
	{
		this.mplSNSMobilePushService = mplSNSMobilePushService;
	}

	/**
	 * @return the mplOrderService
	 */
	public MplOrderService getMplOrderService()
	{
		return mplOrderService;
	}

	/**
	 * @param mplOrderService
	 *           the mplOrderService to set
	 */
	public void setMplOrderService(final MplOrderService mplOrderService)
	{
		this.mplOrderService = mplOrderService;
	}

	/**
	 * @return the mplOrderCancelClientService
	 */
	public MplOrderCancelClientService getMplOrderCancelClientService()
	{
		return mplOrderCancelClientService;
	}

	/**
	 * @param mplOrderCancelClientService
	 *           the mplOrderCancelClientService to set
	 */
	public void setMplOrderCancelClientService(final MplOrderCancelClientService mplOrderCancelClientService)
	{
		this.mplOrderCancelClientService = mplOrderCancelClientService;
	}

	/**
	 * @return the ticketCreate
	 */
	public TicketCreationCRMservice getTicketCreate()
	{
		return ticketCreate;
	}

	/**
	 * @param ticketCreate
	 *           the ticketCreate to set
	 */
	public void setTicketCreate(final TicketCreationCRMservice ticketCreate)
	{
		this.ticketCreate = ticketCreate;
	}

	/**
	 * @return the mplJusPayRefundService
	 */
	public MplJusPayRefundService getMplJusPayRefundService()
	{
		return mplJusPayRefundService;
	}

	/**
	 * @param mplJusPayRefundService
	 *           the mplJusPayRefundService to set
	 */
	public void setMplJusPayRefundService(final MplJusPayRefundService mplJusPayRefundService)
	{
		this.mplJusPayRefundService = mplJusPayRefundService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	/**
	 * @return the returnLogistics
	 */
	public ReturnLogisticsService getReturnLogistics()
	{
		return returnLogistics;
	}

	/**
	 * @param returnLogistics
	 *           the returnLogistics to set
	 */
	public void setReturnLogistics(final ReturnLogisticsService returnLogistics)
	{
		this.returnLogistics = returnLogistics;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the orderCancelService
	 */
	public OrderCancelService getOrderCancelService()
	{
		return orderCancelService;
	}

	/**
	 * @param orderCancelService
	 *           the orderCancelService to set
	 */
	public void setOrderCancelService(final OrderCancelService orderCancelService)
	{
		this.orderCancelService = orderCancelService;
	}

	/**
	 * @return the orderCancelRecordsHandler
	 */
	public OrderCancelRecordsHandler getOrderCancelRecordsHandler()
	{
		return orderCancelRecordsHandler;
	}

	/**
	 * @param orderCancelRecordsHandler
	 *           the orderCancelRecordsHandler to set
	 */
	public void setOrderCancelRecordsHandler(final OrderCancelRecordsHandler orderCancelRecordsHandler)
	{
		this.orderCancelRecordsHandler = orderCancelRecordsHandler;
	}



	/**
	 * @return the orderEntryConverter
	 */
	public Converter<AbstractOrderEntryModel, OrderEntryData> getOrderEntryConverter()
	{
		return orderEntryConverter;
	}



	/**
	 * @param orderEntryConverter
	 *           the orderEntryConverter to set
	 */
	@Required
	public void setOrderEntryConverter(final Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter)
	{
		this.orderEntryConverter = orderEntryConverter;
	}

	/**
	 * @return the mplRefundService
	 */
	public MPLRefundService getMplRefundService()
	{
		return mplRefundService;
	}

	/**
	 * @param mplRefundService
	 *           the mplRefundService to set
	 */
	public void setMplRefundService(final MPLRefundService mplRefundService)
	{
		this.mplRefundService = mplRefundService;
	}



}
