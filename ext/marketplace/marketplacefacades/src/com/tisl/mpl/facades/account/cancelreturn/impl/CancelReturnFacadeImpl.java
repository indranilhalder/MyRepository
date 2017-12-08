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
import de.hybris.platform.basecommerce.jalo.BasecommerceManager;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.JewelleryInformationModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.orderhistory.model.OrderHistoryEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.returns.ReturnService;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.clientservice.MarketplacecclientservicesConstants;
import com.tisl.mpl.core.enums.JuspayRefundType;
import com.tisl.mpl.core.enums.RefundFomType;
import com.tisl.mpl.core.enums.TypeofReturn;
import com.tisl.mpl.core.event.OrderReturnToStoreEvent;
import com.tisl.mpl.core.keygenerator.MplPrefixablePersistentKeyGenerator;
import com.tisl.mpl.core.model.BankDetailsInfoToFICOHistoryModel;
import com.tisl.mpl.core.model.CancellationReasonModel;
import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;
import com.tisl.mpl.core.model.MplLPHolidaysModel;
import com.tisl.mpl.core.model.MplReturnPickUpAddressInfoModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.core.model.ReturnQuickDropProcessModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.core.util.DateUtilHelper;
import com.tisl.mpl.data.CODSelfShipData;
import com.tisl.mpl.data.CODSelfShipResponseData;
import com.tisl.mpl.data.CRMTicketUpdateData;
import com.tisl.mpl.data.CRMTicketUpdateResponseData;
import com.tisl.mpl.data.OrderLineData;
import com.tisl.mpl.data.RTSAndRSSReturnInfoRequestData;
import com.tisl.mpl.data.RTSAndRSSReturnInfoResponseData;
import com.tisl.mpl.data.ReturnAddressInfo;
import com.tisl.mpl.data.ReturnInfoData;
import com.tisl.mpl.data.ReturnLogisticsResponseData;
import com.tisl.mpl.data.SendTicketLineItemData;
import com.tisl.mpl.data.SendTicketRequestData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facade.config.MplConfigFacade;
import com.tisl.mpl.facades.account.address.AccountAddressFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;
import com.tisl.mpl.facades.data.ReturnItemAddressData;
import com.tisl.mpl.facades.product.data.ReturnReasonData;
import com.tisl.mpl.facades.product.data.StateData;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;
import com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MPLReturnService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJewelleryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplJusPayRefundService;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplProcessOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.CRMTicketDetailModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.ordercancel.MplOrderCancelEntry;
import com.tisl.mpl.ordercancel.MplOrderCancelRequest;
import com.tisl.mpl.service.MplOrderCancelClientService;
import com.tisl.mpl.service.ReturnLogisticsService;
import com.tisl.mpl.service.TicketCreationCRMservice;
import com.tisl.mpl.sms.facades.SendSMSFacade;
import com.tisl.mpl.sns.push.service.impl.MplSNSMobilePushServiceImpl;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.GenericUtilityMethods;
import com.tisl.mpl.wsdto.PushNotificationData;
import com.tisl.mpl.wsdto.ReturnLogistics;
import com.tisl.mpl.wsdto.ReturnLogisticsResponseDTO;
import com.tisl.mpl.wsdto.ReturnPincodeDTO;
import com.tisl.mpl.wsdto.ReturnRequestDTO;
import com.tisl.mpl.wsdto.TicketMasterXMLData;
import com.tisl.mpl.wsdto.TicketUpdateRequestXML;
import com.tisl.mpl.wsdto.TicketUpdateResponseXML;
import com.tisl.mpl.xml.pojo.CODSelfShipmentRequest;
import com.tisl.mpl.xml.pojo.CODSelfShipmentResponse;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest;
import com.tisl.mpl.xml.pojo.MplCancelOrderRequest.OrderLine;
import com.tisl.mpl.xml.pojo.MplOrderIsCancellableResponse;
import com.tisl.mpl.xml.pojo.OrderLineDataResponse;
import com.tisl.mpl.xml.pojo.RTSAndRSSReturnInfoRequest;
import com.tisl.mpl.xml.pojo.RTSAndRSSReturnInfoResponse;
import com.tisl.mpl.xml.pojo.ReturnLogisticsResponse;




/**
 * @author TCS
 *
 */
public class CancelReturnFacadeImpl implements CancelReturnFacade
{
	/**
	 *
	 */
	private static final String LOG_MSG_REVERSE_LOGISTICS_AVAILABILTY_RESPONSE_ORDERLINE_IS_NULL = "*****Reverse logistics availabilty  Response orderline is null*********";
	/**
	 *
	 */
	private static final String DD_MM_YYYY = "dd-MM-yyyy";
	/**
	 *
	 */
	private static final String RETURN_CATEGORY_RSP = "RSP";
	/**
	 *
	 */
	private static final String RETURN_CATEGORY_RSS = "RSS";
	/**
	 *
	 */
	private static final String LOG_MSG_RMA_NUMBER = "*************** RMA number:";
	/**
	 *
	 */
	private static final String LOG_MSG_CREATE_REFUND_SETTING_TYPE_OF_RETURN = ">> createRefund >>  Setting Type of Return ";
	/**
	 *
	 */
	private static final String COD = "COD";


	@Resource
	private MplOrderService mplOrderService;
	@Resource
	private MplSNSMobilePushServiceImpl mplSNSMobilePushService;
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;
	@Autowired
	private MplOrderFacade mplOrderFacade;
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
	private SessionService sessionService;
	@Resource
	private MplCheckoutFacade mplCheckoutFacade;
	@Resource
	private MPLRefundService mplRefundService;
	private Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter;

	@Resource(name = "codReturnPaymentInfoReverseConverter")
	private Converter<CODSelfShipData, BankDetailsInfoToFICOHistoryModel> codReturnPaymentInfoReverseConverter;

	@Resource(name = "mplCustomerBankDetailsReverseConverter")
	private Converter<CODSelfShipData, MplCustomerBankAccountDetailsModel> mplCustomerBankDetailsReverseConverter;

	@Resource(name = "mplCustomerBankDetailsConverter")
	private Converter<MplCustomerBankAccountDetailsModel, CODSelfShipData> mplCustomerBankDetailsConverter;

	@Autowired
	private MPLReturnService mplReturnService;

	private OrderCancelRecordsHandler orderCancelRecordsHandler;
	//SONAR FIX JEWELLERY
	//	@Autowired
	//	private DefaultMplMWalletRefundService walletRefundService;



	@Resource(name = "mplProcessOrderService")
	MplProcessOrderService mplProcessOrderService;
	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	@Autowired
	private MplPrefixablePersistentKeyGenerator prefixableKeyGenerator;

	@Autowired
	private MplConfigFacade mplConfigFacade;

	@Autowired
	private DateUtilHelper dateUtilHelper;
	@Autowired
	private EventService eventService;
	@Autowired
	private OrderModelDao orderModelDao;
	@Autowired
	private SendSMSFacade sendSMSFacade;
	@Autowired
	private Populator customerPopulator;
	protected static final Logger LOG = Logger.getLogger(CancelReturnFacadeImpl.class);

	private static final String SPACE = " ";

	private static final String SSB = "SSB";

	@Autowired
	private AccountAddressFacade accountAddressFacade;

	@Resource(name = "mplJewelleryService")
	private MplJewelleryService jewelleryService;


	@Override
	public boolean implementCancelOrReturn(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String reasonCode, final String ussid, final String ticketTypeCode, final CustomerData customerData,
			final String refundType, final boolean isReturn, final SalesApplication salesApplication)
	{

		//TISRLEE-1703 start
		return implementCancelOrReturn(subOrderDetails, subOrderEntry, reasonCode, ussid, ticketTypeCode, customerData, refundType,
				isReturn, salesApplication, null);
	}

	private boolean implementCancelOrReturn(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String reasonCode, final String ussid, final String ticketTypeCode, final CustomerData customerData,
			String refundType, final boolean isReturn, final SalesApplication salesApplication, final String ticketSubType)
	{
		//TISRLEE-1703 end
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

			LOG.debug("Step 1: BOGO or Free Bie available for order" + subOrderModel.getCode() + bogoOrFreeBie);
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
			LOG.debug("Step 2:cancelOrRetrnanable : " + cancelOrRetrnanable);
			if (cancelOrRetrnanable)
			{
				final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
						subOrderEntry.getTransactionId());
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
				{
					if (ticketTypeCode.equalsIgnoreCase("C"))
					{
						LOG.debug("Step 3:History creation start for cancellation");
						createHistoryEntry(abstractOrderEntryModel, subOrderModel, ConsignmentStatus.CANCELLATION_INITIATED);
					}
					else if (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) ////TISEE-933
					{
						LOG.debug("Step 3:History creation start for retrun");
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
				LOG.debug("Step 4:Ticket is to be created for sub order:" + subOrderDetails.getCode());

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
				//TISRLEE-1703 starts
				boolean ticketCreationStatus = false;
				if (ticketSubType != null)
				{
					ticketCreationStatus = createTicketInCRM(subOrderDetails, subOrderEntry, ticketTypeCode, reasonCode, refundType,
							ussid, customerData, subOrderModel, returnLogisticsCheck, SSB);
				}
				else
				{
					//TISPRD-1641
					ticketCreationStatus = createTicketInCRM(subOrderDetails, subOrderEntry, ticketTypeCode, reasonCode, refundType,
							ussid, customerData, subOrderModel, returnLogisticsCheck);
				}
				//TISRLEE-1703 end

				LOG.debug("Step 4.1:Ticket creation status for sub order:" + ticketCreationStatus);
				LOG.debug("Step 5 :Refund and OMS call started");
				cancelOrRetrnanable = initiateCancellation(ticketTypeCode, subOrderDetails, subOrderEntry, subOrderModel, reasonCode);
				LOG.debug("Step 5.1 : Refund and OMS call status:" + cancelOrRetrnanable);

				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) //TISEE-5524
				{

					//Mrupee checking

					//				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie
					//						&& !subOrderModel.getIsWallet().equals(WalletEnum.MRUPEE))
					//				{
					LOG.debug("Step 6:Create return request for Return:" + subOrderDetails.getCode());


					final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
							subOrderEntry.getTransactionId());

					for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
					{
						final boolean returnReqSuccess = createRefund(subOrderModel, abstractOrderEntryModel, reasonCode,
								salesApplication, returnLogisticsCheck);

						LOG.debug("Return request successful :" + returnReqSuccess);
					}
				}
				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("C"))
				{
					LOG.debug("Step 7:Create Cancel request" + subOrderDetails.getCode());
					frameCancelPushNotification(subOrderModel, subOrderModelVersioned, subOrderEntry, reasonCode, customerData);
					LOG.debug("*Step 7:Cancel request successful and push notification sent:");
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
			LOG.debug("Step 8: Updating commerce consignment status" + omsCancellationStatus);

			if (omsCancellationStatus)
			{
				//TPR-965 : Count not to be reverted
				//mplProcessOrderService.removePromotionInvalidation(subOrderModel.getParentReference());

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
			LOG.error("Updating commerce consignment status ", e);
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
			final ReturnInfoData returninfoData, final CustomerData customerData, final SalesApplication salesApplication,
			final ReturnItemAddressData returnAddress)
	{

		final boolean isReturn = true;
		final String ticketTypeCode = returninfoData.getTicketTypeCode();
		final String reasonCode = returninfoData.getReasonCode();
		boolean cancelOrRetrnanable = true;
		boolean omsCancellationStatus = false;
		String pincode = null;

		//R2.3 techout changes
		//	final String transactionId = subOrderEntry.getTransactionId();
		//	final OrderModel subOrderModel = orderModelService.getOrder(subOrderDetails.getCode());
		final OrderModel subOrderModel = customerAccountService.getOrderForCode((CustomerModel) userService.getCurrentUser(),
				subOrderDetails.getCode(), baseStoreService.getCurrentBaseStore());

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

			LOG.debug("==BOGO or Free Bie available for order" + subOrderModel.getCode() + bogoOrFreeBie);
			LOG.debug("==Step 2: Ticket Type code : " + ticketTypeCode);
			if ((ticketTypeCode.equalsIgnoreCase("C") || (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie))) //TISEE-933
			{

				orderLineRequest = populateOrderLineData(subOrderEntry, ticketTypeCode, subOrderModel,
						returninfoData.getReasonCode(), returninfoData.getUssid(), pincode, returninfoData.getReturnFulfillmentMode());


				if (CollectionUtils.isNotEmpty(orderLineRequest.getOrderLine()))
				{
					cancelOrRetrnanable = cancelOrderInOMS(orderLineRequest, cancelOrRetrnanable, isReturn);
				}


			}
			if (ticketTypeCode.equalsIgnoreCase("R") && bogoOrFreeBie) //TISEE-933
			{
				cancelOrRetrnanable = true;
			}
			LOG.debug("==Step 2:cancelOrRetrnanable : " + cancelOrRetrnanable);
			if (cancelOrRetrnanable)
			{
				final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
						subOrderEntry.getTransactionId());
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
				{

					if (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) ////TISEE-933
					{
						LOG.debug("==Step 3:History creation start for retrun");
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
				LOG.debug("==Step 4:Ticket is to be created for sub order:" + subOrderDetails.getCode());


				final boolean ticketCreationStatus = createTicketInCRM(subOrderDetails, subOrderEntry, ticketTypeCode, reasonCode,
						returninfoData.getRefundType(), returninfoData.getUssid(), customerData, subOrderModel, returnAddress,
						returninfoData);

				LOG.debug("==Step 4.1:Ticket creation status for sub order:" + ticketCreationStatus);
				LOG.debug("==Step 5 : Refund and OMS call started");
				cancelOrRetrnanable = initiateCancellation(ticketTypeCode, subOrderDetails, subOrderEntry, subOrderModel, reasonCode);
				LOG.debug("==Step 5.1 :*********************************** Refund and OMS call status:" + cancelOrRetrnanable);

				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) //TISEE-5524
				{
					//Mrupee checking

					//				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie
					//						&& !subOrderModel.getIsWallet().equals(WalletEnum.MRUPEE))
					//				{
					LOG.debug("==Step 6:***********************************Create return request for Return:"
							+ subOrderDetails.getCode());


					final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
							subOrderEntry.getTransactionId());

					for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
					{
						final boolean returnReqSuccess = createRefund(subOrderModel, abstractOrderEntryModel, reasonCode,
								salesApplication, returnAddress.getPincode(), subOrderDetails);

						LOG.debug("==**********************************Return request successful :" + returnReqSuccess);
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(">>>== Cancel Refund exception occured in implementReturnItem etail non business exception : ", e);

			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(">>>== Cancel Refund exception occured in implementReturnItem : ", e);
		}


		try
		{
			LOG.debug("Step 8:== *********************************** Updating commerce consignment status" + omsCancellationStatus);

			if (omsCancellationStatus)
			{
				//TPR-965 Count not to be reverted
				//mplProcessOrderService.removePromotionInvalidation(subOrderModel.getParentReference());

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
			LOG.error("==*******************Updating commerce consignment status ", e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception ex)
		{
			LOG.error(">>>== Exception occured while updating consignment : ", ex);
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
				LOG.debug("Step8:****== Updating consignment status and order histoery insertion Consinment is not present ************** ");
			}

			//createHistoryEntry(orderEntryModel, orderModel, consignmentStatus);
		}
		catch (final Exception ex)
		{
			LOG.error("**** ==Error occurs while updateConsignmentStatus ", ex);
		}
		LOG.debug("Step8:****== Updating consignment status and order history completed" + updateStatus);
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
						&& COD.equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
				{
					amount = 0d;
				}
				else if (suborderEntry.getAmountAfterAllDisc().getDoubleValue() != null)
				{
					amount += suborderEntry.getAmountAfterAllDisc().getDoubleValue().doubleValue();
					if (suborderEntry.getScheduledDeliveryCharge() != null)
					{
						amount += suborderEntry.getScheduledDeliveryCharge().doubleValue();
					}
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
			final String reasonCode, final SalesApplication salesApplication, boolean returnLogisticsCheck)
	{

		boolean returnReqCreated = false;
		final List<RefundEntryModel> refundList = new ArrayList<>();
		try
		{
			final ReturnRequestModel returnRequestModel = returnService.createReturnRequest(subOrderModel);
			returnRequestModel.setRMA(returnService.createRMA(returnRequestModel));
			//TISEE-5471

			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(subOrderModel.getCode());
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
			LOG.info(LOG_MSG_CREATE_REFUND_SETTING_TYPE_OF_RETURN + returnLogisticsCheck);
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
							&& COD.equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
					{
						refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
					}
					else
					{
						final double amount = (abstractOrderEntryModel.getNetAmountAfterAllDisc() != null ? abstractOrderEntryModel
								.getNetAmountAfterAllDisc().doubleValue() : 0D)
								+ (abstractOrderEntryModel.getCurrDelCharge() != null ? abstractOrderEntryModel.getCurrDelCharge()
										.doubleValue() : 0D)
								+ (abstractOrderEntryModel.getScheduledDeliveryCharge() != null ? abstractOrderEntryModel
										.getScheduledDeliveryCharge().doubleValue() : 0D);

						refundEntryModel.setAmount(NumberUtils.createBigDecimal(Double.toString(amount)));
					}
				}
				refundList.add(refundEntryModel);
				//modelService.save(refundEntryModel);
			}
			//TISPT-386
			modelService.saveAll(refundList);
			modelService.save(returnRequestModel);

			LOG.debug(LOG_MSG_RMA_NUMBER + returnRequestModel.getRMA());
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
			final String reasonCode, final SalesApplication salesApplication, final String pinCode)
	{

		boolean returnReqCreated = false;
		boolean returnLogisticsCheck = true;
		try
		{

			final ReturnRequestModel returnRequestModel = returnService.createReturnRequest(subOrderModel);
			returnRequestModel.setRMA(returnService.createRMA(returnRequestModel));
			//TISEE-5471
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(subOrderModel.getCode());
			final List<ReturnLogisticsResponseData> returnLogisticsRespList = checkReturnLogistics(subOrderDetails, pinCode);
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
			LOG.info(LOG_MSG_CREATE_REFUND_SETTING_TYPE_OF_RETURN + returnLogisticsCheck);
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
							&& COD.equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
					{
						refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
					}
					else
					{
						final double amount = (abstractOrderEntryModel.getNetAmountAfterAllDisc() != null ? abstractOrderEntryModel
								.getNetAmountAfterAllDisc().doubleValue() : 0D)
								+ (abstractOrderEntryModel.getCurrDelCharge() != null ? abstractOrderEntryModel.getCurrDelCharge()
										.doubleValue() : 0D)
								+ (abstractOrderEntryModel.getScheduledDeliveryCharge() != null ? abstractOrderEntryModel
										.getScheduledDeliveryCharge().doubleValue() : 0D);
						refundEntryModel.setAmount(NumberUtils.createBigDecimal(Double.toString(amount)));
					}
				}
				modelService.save(refundEntryModel);
			}

			modelService.save(returnRequestModel);

			LOG.debug(LOG_MSG_RMA_NUMBER + returnRequestModel.getRMA());
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

		LOG.info("****==Actual return reason desc from Global code master : " + reasonDescription);
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
		//TISRLEE-1703 start
		return createTicketInCRM(subOrderDetails, subOrderEntry, ticketTypeCode, reasonCode, refundType, ussid, customerData,
				subOrderModel, returnLogisticsCheck, null);
	}

	private boolean createTicketInCRM(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String ticketTypeCode, final String reasonCode, final String refundType, final String ussid,
			final CustomerData customerData, final OrderModel subOrderModel, final boolean returnLogisticsCheck,
			final String ticketSubtype)
	{
		//TISRLEE-1703 end
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
				else
				{
					sendTicketLineItemData.setReturnReasonCode(reasonCode);
					sendTicketRequestData.setRefundType(refundType);

					if (returnLogisticsCheck)
					{
						//LOG.info("Setting Type of Return::::::" + returnLogisticsCheck);
						sendTicketRequestData.setReturnCategory(RETURN_CATEGORY_RSP);
					}
					else
					{
						//LOG.info("Setting Type of Return::::::" + returnLogisticsCheck);
						sendTicketRequestData.setReturnCategory(RETURN_CATEGORY_RSS);
					}

					//lineItemDataList.add(sendTicketLineItemData);
					//End
				}

				lineItemDataList.add(sendTicketLineItemData);
			}
			sendTicketRequestData.setCustomerID(customerData.getUid());
			sendTicketRequestData.setLineItemDataList(lineItemDataList);
			sendTicketRequestData.setOrderId(subOrderModel.getParentReference().getCode());
			sendTicketRequestData.setSubOrderId(subOrderDetails.getCode());
			sendTicketRequestData.setTicketType(ticketTypeCode);
			//TISRLEE-1703 start
			if (StringUtils.isNotBlank(ticketSubtype))
			{
				sendTicketRequestData.setTicketSubType(ticketSubtype);
			}
			//TISRLEE-1703 end

			final String asyncEnabled = configurationService.getConfiguration()
					.getString(MarketplacecommerceservicesConstants.ASYNC_ENABLE).trim();
			//create ticket only if async is not working
			if (asyncEnabled.equalsIgnoreCase("N"))
			{
				ticketCreate.ticketCreationModeltoWsDTO(sendTicketRequestData, Boolean.TRUE);
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
			final CustomerData customerData, final OrderModel subOrderModel, final ReturnItemAddressData returnAddress,
			final ReturnInfoData returnInfoData)
	{
		boolean ticketCreationStatus = false;

		try
		{
			final List<SendTicketLineItemData> lineItemDataList = new ArrayList<SendTicketLineItemData>();
			final SendTicketRequestData sendTicketRequestData = new SendTicketRequestData();
			final ReturnAddressInfo addressInfo = new ReturnAddressInfo();
			String pinCode = null;
			if (null != returnAddress)
			{
				pinCode = returnAddress.getPincode();
			}
			final List<AbstractOrderEntryModel> orderEntries = associatedEntries(subOrderModel, subOrderEntry.getTransactionId());
			for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntries)
			{
				final SendTicketLineItemData sendTicketLineItemData = new SendTicketLineItemData();
				sendTicketLineItemData.setLineItemId(abstractOrderEntryModel.getOrderLineId());
				if (ticketTypeCode.equalsIgnoreCase("R"))
				{
					sendTicketLineItemData.setReturnReasonCode(reasonCode);
					sendTicketRequestData.setRefundType(refundType);
					//TPR-4134
					if (null != returnInfoData.getReverseSealLostflag())
					{
						sendTicketLineItemData.setReverseSealLostflag(returnInfoData.getReverseSealLostflag());
					}


					if (!(returnInfoData.getReturnMethod().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_SELF)))
					{
						boolean returnLogisticsCheck = true; //Start

						final List<ReturnLogisticsResponseData> returnLogisticsRespList = checkReturnLogistics(subOrderDetails,
								pinCode, subOrderEntry.getTransactionId());
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
						if (returnInfoData.getReturnMethod().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_SCHEDULE))
						{
							if (returnLogisticsCheck)
							{
								sendTicketRequestData.setTicketSubType(MarketplacecommerceservicesConstants.RETURN_TYPE_RSP);
							}
						}
					}

					if (returnInfoData.getReturnMethod() != null)
					{
						if (returnInfoData.getReturnMethod().equalsIgnoreCase(MarketplacecommerceservicesConstants.RETURN_SELF))
						{
							sendTicketRequestData.setTicketSubType(MarketplacecommerceservicesConstants.RETURN_TYPE_RSS);
						}
						else if (returnInfoData.getReturnMethod().equalsIgnoreCase(
								MarketplacecommerceservicesConstants.RETURN_METHOD_QUICKDROP))
						{
							sendTicketRequestData.setTicketSubType(MarketplacecommerceservicesConstants.RETURN_TYPE_RTS);
						}
					}
				}

				lineItemDataList.add(sendTicketLineItemData);
			}
			if (StringUtils.isNotEmpty(sendTicketRequestData.getTicketSubType()))
			{
				if (!((sendTicketRequestData.getTicketSubType()).equals(RETURN_CATEGORY_RSS)))
				{
					addressInfo.setShippingFirstName(returnAddress.getFirstName());
					addressInfo.setShippingLastName(returnAddress.getLastName());
					addressInfo.setPhoneNo(returnAddress.getMobileNo());
					addressInfo.setAddress1(returnAddress.getAddressLane1());
					addressInfo.setAddress2(returnAddress.getAddressLane2());
					addressInfo.setAddress3(returnAddress.getAddressLine3());
					addressInfo.setCountry(returnAddress.getCountry());
					addressInfo.setCity(returnAddress.getCity());
					addressInfo.setState(returnAddress.getState());
					addressInfo.setPincode(returnAddress.getPincode());
					addressInfo.setLandmark(returnAddress.getLandmark());
				}
			}

			//set ECOM request prefix as E to for COMM triggered Ticket
			prefixableKeyGenerator.setPrefix(MarketplacecommerceservicesConstants.TICKETID_PREFIX_E);
			sendTicketRequestData.setEcomRequestId(prefixableKeyGenerator.generate().toString());

			/* TISRLEE-3290 start */
			if (null != returnInfoData.getReturnPickupDate())
			{
				try
				{
					String returnPickUpdate = returnInfoData.getReturnPickupDate();
					returnPickUpdate = returnPickUpdate.concat("00:00:00");
					final SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddhh:mm:ss");
					final SimpleDateFormat format2 = new SimpleDateFormat(DD_MM_YYYY);
					final Date da = format1.parse(returnPickUpdate);
					final String date = format2.format(da);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("ReturnPickupDate" + date);
					}
					sendTicketRequestData.setReturnPickupDate(date);
				}
				catch (final Exception e)
				{
					LOG.error("Exception occurred while setting ReturnPickupDate");
				}
			}

			if (null != returnInfoData.getReturnPickupDate() && null != returnInfoData.getTimeSlotFrom())
			{
				try
				{
					final String timeslot = returnInfoData.getReturnPickupDate();
					final String strDate = timeslot.concat("00:00:00");
					final SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddhh:mm:ss");
					final SimpleDateFormat format2 = new SimpleDateFormat(DD_MM_YYYY);
					final Date da = format1.parse(strDate);
					final String date = format2.format(da);
					System.out.println("date" + date);
					final String timeSlotFrom = date.concat(" " + returnInfoData.getTimeSlotFrom());
					final SimpleDateFormat format3 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
					final SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					format4.setTimeZone(TimeZone.getTimeZone("GMT"));
					if (LOG.isDebugEnabled())
					{
						LOG.debug("ReturnPickupDate Time Slot From");
					}
					sendTicketRequestData.setTimeSlotFrom(String.valueOf(format4.format(format3.parse(timeSlotFrom))));
				}
				catch (final Exception e)
				{
					LOG.error("Exception ReturnPickupDate Time Slot From" + e.getMessage());
				}
			}

			if (null != returnInfoData.getReturnPickupDate() && null != returnInfoData.getTimeSlotTo())
			{
				try
				{
					final String timeslot = returnInfoData.getReturnPickupDate();
					final String strDate = timeslot.concat("00:00:00");
					final SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddhh:mm:ss");
					final SimpleDateFormat format2 = new SimpleDateFormat(DD_MM_YYYY);
					final Date da = format1.parse(strDate);
					final String date = format2.format(da);
					System.out.println("date" + date);
					final String timeSlotTo = date.concat(" " + returnInfoData.getTimeSlotTo());
					final SimpleDateFormat format3 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
					final SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					format4.setTimeZone(TimeZone.getTimeZone("GMT"));
					if (LOG.isDebugEnabled())
					{
						LOG.debug("ReturnPickupDate Time Slot To");
					}
					sendTicketRequestData.setTimeSlotTo(String.valueOf(format4.format(format3.parse(timeSlotTo))));
				}
				catch (final Exception e)
				{
					LOG.error("Exception ReturnPickupDate" + e.getMessage());
				}
			}
			/* TISRLEE-3290 end */
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
				ticketCreate.ticketCreationModeltoWsDTO(sendTicketRequestData, Boolean.TRUE);
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
	 * Return Pincode Serviceabilty And CRM Ticket Creation
	 *
	 * @return boolean
	 */
	@Override
	public boolean createTicketInCRM(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String ticketTypeCode, final String reasonCode, final String refundType, final String ussid,
			final CustomerData customerData, final OrderModel subOrderModel, final ReturnItemAddressData returnAddress,
			final String revSealForJwlry)
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
					//TPR-4134
					if (StringUtils.isNotEmpty(revSealForJwlry))
					{
						sendTicketLineItemData.setReverseSealLostflag(revSealForJwlry);

					}
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
						sendTicketRequestData.setTicketSubType(RETURN_CATEGORY_RSP);
					}
					else
					{
						//LOG.info("Setting Type of Return::::::" +returnLogisticsCheck);
						sendTicketRequestData.setTicketSubType(RETURN_CATEGORY_RSS);
					}

					//lineItemDataList.add(sendTicketLineItemData);
					//End
				}

				lineItemDataList.add(sendTicketLineItemData);
			}
			if (!((sendTicketRequestData.getTicketSubType()).equals(RETURN_CATEGORY_RSS)))
			{
				addressInfo.setShippingFirstName(returnAddress.getFirstName());
				addressInfo.setShippingLastName(returnAddress.getLastName());
				addressInfo.setPhoneNo(returnAddress.getMobileNo());
				addressInfo.setAddress1(returnAddress.getAddressLane1());
				addressInfo.setAddress2(returnAddress.getAddressLane2());
				addressInfo.setAddress3(returnAddress.getAddressLine3());
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
				ticketCreate.ticketCreationModeltoWsDTO(sendTicketRequestData, Boolean.TRUE);
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

		if (null != sendTicketRequestData.getReturnPickupDate())
		{
			ticket.setReturnPickupDate(sendTicketRequestData.getReturnPickupDate());
		}
		if (null != sendTicketRequestData.getTimeSlotFrom())
		{
			ticket.setTimeSlotFrom(sendTicketRequestData.getTimeSlotFrom());
		}
		if (null != sendTicketRequestData.getTimeSlotTo())
		{
			ticket.setTimeSlotTo(sendTicketRequestData.getTimeSlotTo());
		}
		if (null != sendTicketRequestData.getEcomRequestId())
		{
			ticket.setEcomRequestId(sendTicketRequestData.getEcomRequestId());
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
			final OrderModel subOrderModel, final String reasonCode, final String ussid, final String pincode,
			final String returnFulfillmentMode) throws Exception
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
				orderLineData.setReturnFulfillmentMode(returnFulfillmentMode);
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
		String key = null;
		//Get the reason from Global Code master
		String reasonDescription = null;
		if (Integer.parseInt(reasonCode) > 5)
		{
			key = reasonCode + "_onetouch";
			reasonDescription = configurationService.getConfiguration().getString(key);
			reasonDescription = reasonDescription.trim();
		}
		else
		{
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
				if (null != orderEntry.getIsEDtoHD() && orderEntry.getIsEDtoHD().booleanValue()
						&& null != orderEntry.getRefundedEdChargeAmt() && orderEntry.getRefundedEdChargeAmt().doubleValue() != 0D)
				{
					if (null != orderEntry.getHdDeliveryCharge() && orderEntry.getHdDeliveryCharge().doubleValue() > 0.0D)
					{
						deliveryCost = orderEntry.getHdDeliveryCharge().doubleValue();
					}
				}
				else
				{
					deliveryCost = orderEntry.getCurrDelCharge().doubleValue();
				}

			}

			double scheduleDeliveryCost = 0D;
			if (orderEntry.getScheduledDeliveryCharge() != null)
			{
				scheduleDeliveryCost = orderEntry.getScheduledDeliveryCharge().doubleValue();
			}


			refundAmount = refundAmount + orderEntry.getNetAmountAfterAllDisc().doubleValue() + deliveryCost + scheduleDeliveryCost;
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

		//CODE COMMENTED AFTER R2.3 FOR ONE TOUCH CANCELLATION/RETURN FEATURE----START


		//OrderCancelRecordEntryModel orderRequestRecord = null;

		//cancel Order

		//final OrderCancelRecordEntryModel orderRequestRecord = orderCancelService.requestOrderCancel(orderCancelRequest,
		//		userService.getCurrentUser());
		//CODE COMMENTED AFTER R2.3 FOR ONE TOUCH CANCELLATION/RETURN FEATURE----END



		//TRP-1345: START

		//if ((userService.getCurrentUser()) instanceof EmployeeModel)
		//{
		//	orderRequestRecord = orderCancelService.requestOrderCancel(orderCancelRequest, subOrderModel.getUser());
		//}
		//else
		//{
		//	orderRequestRecord = orderCancelService.requestOrderCancel(orderCancelRequest, userService.getCurrentUser());
		//	}

		final OrderCancelRecordEntryModel orderRequestRecord = orderCancelService.requestOrderCancel(orderCancelRequest,
				subOrderModel.getUser());
		//TRP-1345: END

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
			// Done for INC144317893
			initiateRefund(subOrderModel, orderRequestRecord);

			//			if (null != subOrderModel && subOrderModel.getIsWallet().equals(WalletEnum.MRUPEE))
			//			{
			//				//Mrupee implementation
			//
			//				initiateRefundMrupee(subOrderModel, orderRequestRecord, "C");
			//
			//			}
			//			else
			//			{
			//				initiateRefund(subOrderModel, orderRequestRecord);
			//			}
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

							//h2refund Added to know the refund type
							try
							{

								if (CollectionUtils.isNotEmpty(orderEntry.getConsignmentEntries()))
								{

									final ConsignmentModel consignmentModel = orderEntry.getConsignmentEntries().iterator().next()
											.getConsignment();
									consignmentModel.setRefundDetails(RefundFomType.AUTOMATIC);
									getModelService().save(consignmentModel);

								}

							}
							catch (final Exception e)
							{
								LOG.error("Refund updation data failed");
							}


							ConsignmentStatus newStatus = null;
							if (orderEntry != null)
							{
								//TISPRO-216 Starts
								double refundAmount = 0D;
								final Double currDeliveryCharges = orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge()
										: NumberUtils.DOUBLE_ZERO;
								Double deliveryCost = Double.valueOf(0.0D);
								if (null != orderEntry.getIsEDtoHD() && orderEntry.getIsEDtoHD().booleanValue()
										&& null != orderEntry.getRefundedEdChargeAmt()
										&& orderEntry.getRefundedEdChargeAmt().doubleValue() != 0D)
								{
									deliveryCost = orderEntry.getHdDeliveryCharge() != null ? orderEntry.getHdDeliveryCharge()
											: NumberUtils.DOUBLE_ZERO;
								}
								else
								{
									deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge()
											: NumberUtils.DOUBLE_ZERO;
								}
								// Added in r2.3 START
								final Double scheduleDeliveryCost = orderEntry.getScheduledDeliveryCharge() != null ? orderEntry
										.getScheduledDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
								// Added in r2.3 END
								refundAmount = orderEntry.getNetAmountAfterAllDisc().doubleValue() + deliveryCost.doubleValue()
										+ scheduleDeliveryCost.doubleValue();
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


								orderEntry.setRefundedDeliveryChargeAmt(currDeliveryCharges);
								orderEntry.setCurrDelCharge(new Double(0D));
								if (null != orderEntry.getIsEDtoHD() && orderEntry.getIsEDtoHD().booleanValue()
										&& null != orderEntry.getRefundedEdChargeAmt()
										&& orderEntry.getRefundedEdChargeAmt().doubleValue() == 0D)
								{
									double hdDeliveryCharges = 0.0D;
									if (null != orderEntry.getHdDeliveryCharge())
									{
										hdDeliveryCharges = orderEntry.getHdDeliveryCharge().doubleValue();
									}
									orderEntry
											.setRefundedEdChargeAmt(Double.valueOf(currDeliveryCharges.doubleValue() - hdDeliveryCharges));
								}
								// Added in R2.3 START
								orderEntry.setRefundedScheduleDeliveryChargeAmt(scheduleDeliveryCost);
								orderEntry.setScheduledDeliveryCharge(new Double(0D));
								// Added in R2.3 END
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
										Double.valueOf(refundAmount), newStatus, null);

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
					LOG.debug(LOG_MSG_REVERSE_LOGISTICS_AVAILABILTY_RESPONSE_ORDERLINE_IS_NULL);
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
	public List<ReturnLogisticsResponseData> checkReturnLogistics(final OrderData orderDetails, final String pincode)

	{
		try
		{
			final List<OrderEntryData> entries = orderDetails.getEntries();
			final OrderModel orderModel = orderModelService.getOrder(orderDetails.getCode());
			final List<ReturnLogistics> returnLogisticsList = new ArrayList<ReturnLogistics>();
			String returningTransactionId;
			returningTransactionId = sessionService.getAttribute("transactionId");
			//	returningTransactionId = transId;
			if (StringUtils.isEmpty(returningTransactionId))
			{
				return null;
			}
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

					String returnFulfillmentType = null;
					String returnFulfillmentByP1 = null;
					//getting the product code
					final ProductModel productModel = mplOrderFacade.getProductForCode(eachEntry.getProduct().getCode());
					String ussid = "";
					for (final SellerInformationModel sellerInfo : productModel.getSellerInformationRelator())
					{
						//Added for jewellery
						if (productModel.getProductCategoryType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))

						{
							final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(eachEntry
									.getSelectedUssid());
							ussid = (CollectionUtils.isNotEmpty(jewelleryInfo)) ? jewelleryInfo.get(0).getUSSID() : "";

							LOG.debug("PCMUSSID FOR JEWELLERY :::::::::: " + "for " + eachEntry.getSelectedUssid() + " is "
									+ jewelleryInfo.get(0).getPCMUSSID());
						}
						else
						{
							ussid = sellerInfo.getUSSID();
						}

						//if (eachEntry.getSelectedUssid().equalsIgnoreCase(sellerInfo.getUSSID()))
						if (eachEntry.getSelectedUssid().equalsIgnoreCase(ussid))
						{
							if (CollectionUtils.isNotEmpty(sellerInfo.getRichAttribute()))
							{
								for (final RichAttributeModel richAttribute : sellerInfo.getRichAttribute())
								{
									if (null != richAttribute.getReturnFulfillMode())
									{
										LOG.info(richAttribute.getReturnFulfillMode());
										returnFulfillmentType = richAttribute.getReturnFulfillMode().getCode();
									}

									if (null != richAttribute.getReturnFulfillModeByP1())
									{
										LOG.info(richAttribute.getReturnFulfillModeByP1());
										returnFulfillmentByP1 = richAttribute.getReturnFulfillModeByP1().getCode();
									}
								}
							}
						}
					}

					if (StringUtils.isNotEmpty(returnFulfillmentType.toUpperCase()))
					{
						returnLogistics.setReturnFulfillmentType(returnFulfillmentType.toUpperCase());
					}
					if (StringUtils.isNotEmpty(returnFulfillmentByP1))
					{
						returnLogistics.setReturnFulfillmentByP1(returnFulfillmentByP1);
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
						if (null != orderLine.getReturnFulfillmentType())
						{
							returnLogRespData.setReturnFulfillmentType(orderLine.getReturnFulfillmentType());
						}
						if (null != orderLine.getIsReturnLogisticsAvailable())
						{

							if (StringUtils.isNotBlank(returningTransactionId)
									&& orderLine.getTransactionId().equalsIgnoreCase(returningTransactionId))
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
					LOG.debug(LOG_MSG_REVERSE_LOGISTICS_AVAILABILTY_RESPONSE_ORDERLINE_IS_NULL);
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
	@Override
	public List<AbstractOrderEntryModel> associatedEntries(final OrderModel subOrderDetails, final String transactionId)
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

	@Override
	public RTSAndRSSReturnInfoResponseData retrunInfoCallToOMS(final RTSAndRSSReturnInfoRequestData returnInfoRequestData)
	{

		final RTSAndRSSReturnInfoRequest returnInfoRequest = new RTSAndRSSReturnInfoRequest();
		final RTSAndRSSReturnInfoResponseData returnInforesponse = new RTSAndRSSReturnInfoResponseData();
		returnInfoRequest.setOrderId(returnInfoRequestData.getOrderId());
		returnInfoRequest.setAwbNum(returnInfoRequestData.getAWBNum());
		returnInfoRequest.setLogisticsID(returnInfoRequestData.getLogisticsID());
		returnInfoRequest.setLpNameOther(returnInfoRequestData.getLPNameOther());
		if (null != returnInfoRequestData.getRTSStore() && returnInfoRequestData.getRTSStore().size() > 0)
		{
			returnInfoRequest.setRtsStore(returnInfoRequestData.getRTSStore());
		}
		returnInfoRequest.setShipmentCharge(returnInfoRequestData.getShipmentCharge());
		returnInfoRequest.setReturnType(returnInfoRequestData.getReturnType());
		returnInfoRequest.setTransactionId(returnInfoRequestData.getTransactionId());
		returnInfoRequest.setShipmentProofURL(returnInfoRequestData.getShipmentProofURL());

		if (MarketplacecommerceservicesConstants.RETURN_TYPE_RSS.equalsIgnoreCase(returnInfoRequest.getReturnType()))
		{
			LOG.info("CancelReturnFacadeImp:::CRM Ticket RSS Update");
			final CRMTicketDetailModel ticketDetailModel = mplReturnService.getCRMTicketDetail(returnInfoRequestData
					.getTransactionId());
			if (ticketDetailModel != null)
			{
				final CRMTicketUpdateData ticketUpdateData = new CRMTicketUpdateData();
				ticketUpdateData.setEcomRequestId(ticketDetailModel.getEcomRequestId());
				ticketUpdateData.setTicketId(ticketDetailModel.getTicketId());
				ticketUpdateData.setTransactionId(returnInfoRequestData.getTransactionId());
				ticketUpdateData.setRssOtherLPName(returnInfoRequestData.getLPNameOther());
				ticketUpdateData.setRssAWBNumber(returnInfoRequestData.getAWBNum());
				ticketUpdateData.setRssCharge(returnInfoRequestData.getShipmentCharge());
				ticketUpdateData.setRssDispathProofURL(returnInfoRequestData.getShipmentProofURL());
				updateCRMTicket(ticketUpdateData);
			}
		}


		try
		{

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Sending Returninfo to OMS  ");
			}
			LOG.info("Return Info Call to OMS data:" + returnInfoRequest);

			final RTSAndRSSReturnInfoResponse response = mplOrderCancelClientService.orderReturnInfoOMS(returnInfoRequest);

			try
			{
				if (MarketplacecommerceservicesConstants.RETURN_TYPE_RTS.equalsIgnoreCase(returnInfoRequest.getReturnType()))
				{
					final OrderModel orderModel = orderModelDao.getOrderModel(returnInfoRequestData.getOrderId());
					final CustomerModel customerModel = (CustomerModel) orderModel.getUser();
					String mobilenumber = null;
					if (orderModel.getDeliveryAddress() != null)
					{
						if (orderModel.getDeliveryAddress().getCellphone() != null)
						{
							mobilenumber = orderModel.getDeliveryAddress().getCellphone();
						}
						else if (orderModel.getDeliveryAddress().getPhone2() != null)
						{
							mobilenumber = orderModel.getDeliveryAddress().getPhone2();
						}
						else
						{
							mobilenumber = orderModel.getPickupPersonMobile();
						}

					}
					else
					{
						//for CNC ore
						mobilenumber = orderModel.getPickupPersonMobile();
					}

					//Send notification sms
					final AbstractOrderEntryModel entrymodel = getOrderEntryModel(orderModel, returnInfoRequestData.getTransactionId());
					final String date = getDateReturnToStore(entrymodel);
					sendPushNotificationForReturnToStore(customerModel, returnInfoRequestData.getRTSStore(), mobilenumber,
							returnInfoRequestData.getOrderId(), date);
					final ReturnQuickDropProcessModel qickdropProcess = new ReturnQuickDropProcessModel();
					qickdropProcess.setOrder(orderModel);
					qickdropProcess.setTransactionId(returnInfoRequestData.getTransactionId());
					qickdropProcess.setDateReturnToStore(date);
					qickdropProcess.setStoreIds(returnInfoRequestData.getRTSStore());
					qickdropProcess.setStoreNames(getStoreAddressList(returnInfoRequestData.getRTSStore()));
					final OrderReturnToStoreEvent event = new OrderReturnToStoreEvent(qickdropProcess);
					eventService.publishEvent(event);
				}
			}
			catch (final Exception e)
			{
				LOG.info(" Return QuickDrop Mail Sending Mail ::::::  " + e.getMessage());
			}

			// below portion of code valid for cancel only
			if (null != response)
			{
				returnInforesponse.setSuccess(response.getSuccess());
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return returnInforesponse;
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


	@Override
	public CODSelfShipResponseData codPaymentInfoToFICO(final CODSelfShipData codSelfShipData)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Sending Returninfo to FICO  ");
		}

		final CODSelfShipmentRequest requestData = new CODSelfShipmentRequest();

		requestData.setAmount(codSelfShipData.getAmount());
		requestData.setBankAccount(codSelfShipData.getBankAccount());
		requestData.setBankKey(codSelfShipData.getBankKey());
		requestData.setTransactionID(codSelfShipData.getTransactionID());
		requestData.setOrderDate(codSelfShipData.getOrderDate());
		requestData.setTransactionDate(codSelfShipData.getTransactionDate());
		if (null != codSelfShipData.getTitle())
		{
			requestData.setTitle(codSelfShipData.getTitle().toUpperCase());
		}
		requestData.setPaymentMode(codSelfShipData.getPaymentMode());
		requestData.setBankName(codSelfShipData.getBankName());
		requestData.setName(codSelfShipData.getName());
		requestData.setOrderTag(codSelfShipData.getOrderTag());
		requestData.setOrderNo(codSelfShipData.getOrderNo());
		requestData.setCustomerNumber(codSelfShipData.getCustomerNumber());
		requestData.setOrderRefNo(codSelfShipData.getOrderRefNo());
		requestData.setTransactionType(codSelfShipData.getTransactionType());


		final CODSelfShipResponseData codSelfShipResponseData = new CODSelfShipResponseData();
		try
		{
			final CODSelfShipmentResponse response = mplOrderCancelClientService.codPaymentInfoToFICO(requestData);

			// below portion of code valid for cancel only
			if (null != response)
			{
				codSelfShipResponseData.setSuccess(response.getSuccess());
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}


		return codSelfShipResponseData;

	}

	/**
	 * @param updateTicketData
	 * @return CRMTicketUpdateResponseData
	 *
	 */
	@Override
	public CRMTicketUpdateResponseData updateCRMTicket(final CRMTicketUpdateData updateTicketData)
	{

		final TicketUpdateRequestXML requestXml = new TicketUpdateRequestXML();
		final CRMTicketUpdateResponseData responseData = new CRMTicketUpdateResponseData();
		requestXml.setEcomRequestId(updateTicketData.getEcomRequestId());
		requestXml.setRssAWBNumber(updateTicketData.getRssAWBNumber());
		requestXml.setRssCharge(updateTicketData.getRssCharge());
		requestXml.setRssDispathProofURL(updateTicketData.getRssDispathProofURL());
		requestXml.setRssLPName(updateTicketData.getRssLPName());
		requestXml.setRssOtherLPName(updateTicketData.getRssOtherLPName());
		requestXml.setTicketId(updateTicketData.getTicketId());
		requestXml.setTransactionId(updateTicketData.getTransactionId());
		final TicketUpdateResponseXML response = mplOrderCancelClientService.updateCRMTicket(requestXml);

		// below portion of code valid for cancel only
		if (null != response)
		{
			responseData.setSuccess(response.getSuccess());
		}
		return responseData;
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

	//Mrupee implementation
	/**
	 * @Description This method will initiate refund for cancel/return orders of type isWallet
	 * @param subOrderModel
	 * @param orderRequestRecord
	 */
	//	private void initiateRefundMrupee(final OrderModel subOrderModel, final OrderCancelRecordEntryModel orderRequestRecord,
	//			final String ticketTypeCode)
	//	{
	//		PaymentTransactionModel paymentTransactionModel = null;
	//		if (orderRequestRecord.getRefundableAmount() != null
	//				&& orderRequestRecord.getRefundableAmount().doubleValue() > NumberUtils.DOUBLE_ZERO.doubleValue())
	//		{
	//			//TISSIT-1801
	//			final String uniqueRequestId = walletRefundService.getRefundUniqueRequestId();
	//			try
	//			{
	//				LOG.debug("****** initiateRefund Step 1 >> Begin >> Calling for prepaid for " + orderRequestRecord.getCode());
	//
	//				if ("C".equalsIgnoreCase(ticketTypeCode))
	//				{
	//					LOG.debug(" ############### MRupee doRefund  Method for Cancel order *********************************** "
	//							+ ticketTypeCode);
	//					paymentTransactionModel = walletRefundService.doRefund(subOrderModel, orderRequestRecord.getRefundableAmount()
	//							.doubleValue(), PaymentTransactionType.CANCEL, uniqueRequestId);
	//				}
	//				if (null != paymentTransactionModel)
	//				{
	//					LOG.debug(" ############### MRupee doRefund  paymentTransactionModel not null  *************");
	//					mplJusPayRefundService.attachPaymentTransactionModel(subOrderModel, paymentTransactionModel);
	//
	//					if (CollectionUtils.isNotEmpty(orderRequestRecord.getOrderEntriesModificationEntries()))
	//					{
	//						for (final OrderEntryModificationRecordEntryModel modificationEntry : orderRequestRecord
	//								.getOrderEntriesModificationEntries())
	//						{
	//							final OrderEntryModel orderEntry = modificationEntry.getOrderEntry();
	//							ConsignmentStatus newStatus = null;
	//							if (orderEntry != null)
	//							{
	//								double refundAmount = 0D;
	//								final Double deliveryCost = orderEntry.getCurrDelCharge() != null ? orderEntry.getCurrDelCharge()
	//										: NumberUtils.DOUBLE_ZERO;
	//								// Added in r2.3 START
	//								final Double scheduleDeliveryCost = orderEntry.getScheduledDeliveryCharge() != null ? orderEntry
	//										.getScheduledDeliveryCharge() : NumberUtils.DOUBLE_ZERO;
	//								// Added in r2.3 END
	//
	//								//refundAmount = orderEntry.getNetAmountAfterAllDisc().doubleValue() + deliveryCost.doubleValue();
	//								refundAmount = orderEntry.getNetAmountAfterAllDisc().doubleValue() + deliveryCost.doubleValue()
	//										+ scheduleDeliveryCost.doubleValue();
	//
	//								refundAmount = mplJusPayRefundService.validateRefundAmount(refundAmount, subOrderModel);
	//								List<PaymentTransactionEntryModel> entryList = new ArrayList<PaymentTransactionEntryModel>();
	//								PaymentTransactionEntryModel entryValue = null;
	//								entryList = paymentTransactionModel.getEntries();
	//								if (CollectionUtils.isNotEmpty(entryList))
	//								{
	//									entryValue = entryList.get(entryList.size() - 1);
	//
	//									if (null != entryValue)
	//									{
	//										if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
	//												MarketplacecommerceservicesConstants.SUCCESS))
	//										{
	//											LOG.debug(" ########## ConsignmentStatus for MRupee cancel order ********************************"
	//													+ paymentTransactionModel.getStatus());
	//
	//											newStatus = ConsignmentStatus.ORDER_CANCELLED;
	//										}
	//									}
	//								}
	//								else if (StringUtils.equalsIgnoreCase(paymentTransactionModel.getStatus(),
	//										MarketplacecommerceservicesConstants.FAILURE))
	//								{
	//									LOG.debug(" ########## ConsignmentStatus for MRupee refund Inprogress ******************************"
	//											+ paymentTransactionModel.getStatus());
	//
	//									newStatus = ConsignmentStatus.REFUND_IN_PROGRESS;
	//								}
	//								else
	//								{
	//									LOG.debug(" ########## ConsignmentStatus for MRupee refund initiated ******************************"
	//											+ paymentTransactionModel.getStatus());
	//
	//									newStatus = ConsignmentStatus.REFUND_INITIATED;
	//								}
	//								orderEntry.setRefundedDeliveryChargeAmt(deliveryCost);
	//								orderEntry.setCurrDelCharge(new Double(0D));
	//
	//								// Added in R2.3 START
	//								orderEntry.setRefundedScheduleDeliveryChargeAmt(scheduleDeliveryCost);
	//								orderEntry.setScheduledDeliveryCharge(new Double(0D));
	//								// Added in R2.3 END
	//
	//								modelService.save(orderEntry);
	//								LOG.debug("****** initiateRefund : Step 3  >>Payment transaction mode is not null >> Calling OMS with status as received from Mrupee "
	//										+ newStatus.getCode());
	//
	//								LOG.debug(" ########## Calling Oms refund method at the time of Mrupee order*************" + newStatus);
	//								//								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
	//								//										Double.valueOf(refundAmount), newStatus);
	//
	//								//R2.3 changes
	//
	//								mplJusPayRefundService.makeRefundOMSCall(orderEntry, paymentTransactionModel,
	//										Double.valueOf(refundAmount), newStatus, null);
	//							}
	//						}
	//					}
	//				}
	//				else
	//				{
	//					LOG.debug("****** initiateRefund >>Payment transaction mode is null");
	//					if (PaymentTransactionType.CANCEL.toString().equalsIgnoreCase("CANCEL"))
	//					{
	//						walletRefundService.createCancelRefundPgErrorEntry(orderRequestRecord, PaymentTransactionType.CANCEL,
	//								uniqueRequestId);
	//					}
	//				}
	//			}
	//			catch (final EtailNonBusinessExceptions e)
	//			{
	//				LOG.error(">>>> *****************initiateRefund*********** Exception occured " + e.getMessage(), e);
	//				walletRefundService.createCancelRefundExceptionEntry(orderRequestRecord, PaymentTransactionType.CANCEL,
	//						uniqueRequestId);
	//			}
	//			catch (final Exception e)
	//			{
	//				LOG.error(">>>> *****************initiateRefund*********** Exception occured " + e.getMessage(), e);
	//				walletRefundService.createCancelRefundExceptionEntry(orderRequestRecord, PaymentTransactionType.CANCEL,
	//						uniqueRequestId);
	//			}
	//		}
	//		else
	//
	//		{// Case of COD.
	//
	//			LOG.debug("****** initiateRefund >> Begin >> OMS will not be called for COD  ");
	//			final double refundedAmount = 0D;
	//			paymentTransactionModel = mplJusPayRefundService.createPaymentTransactionModel(orderRequestRecord.getOriginalVersion()
	//					.getOrder(), MarketplacecommerceservicesConstants.FAILURE_FLAG, new Double(refundedAmount),
	//					PaymentTransactionType.CANCEL, MarketplacecommerceservicesConstants.FAILURE_FLAG, UUID.randomUUID().toString());
	//			mplJusPayRefundService.attachPaymentTransactionModel(orderRequestRecord.getOriginalVersion().getOrder(),
	//					paymentTransactionModel);
	//		}
	//		orderRequestRecord.setStatus(OrderModificationEntryStatus.SUCCESSFULL);
	//		orderRequestRecord.setTransactionCode(paymentTransactionModel != null ? paymentTransactionModel.getCode()
	//				: MarketplacecommerceservicesConstants.EMPTY);
	//		modelService.save(orderRequestRecord);
	//	}

	/**
	 * @author TECHOUTS
	 *
	 * @param orderEntryData
	 * @return List<String>
	 *
	 */


	@Override
	public List<String> getReturnableDates(final OrderEntryData orderEntryData)
	{
		List<RichAttributeModel> richAttributeModel = new ArrayList<RichAttributeModel>();
		String ussid = "";
		if (orderEntryData.getProduct().getRootCategory().equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))

		{ //SellerInformationModel sellerInfoModel = null;
			final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(orderEntryData
					.getSelectedUssid());
			ussid = (CollectionUtils.isNotEmpty(jewelleryInfo)) ? jewelleryInfo.get(0).getPCMUSSID() : "";

			LOG.debug("PCMUSSID FOR JEWELLERY :::::::::: " + "for " + orderEntryData.getSelectedUssid() + " is "
					+ jewelleryInfo.get(0).getPCMUSSID());
		}
		else
		{
			ussid = orderEntryData.getSelectedUssid();
		}

		/*
		 * final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(
		 * orderEntryData.getSelectedUssid());
		 */

		final SellerInformationModel sellerInfoModel = getMplSellerInformationService().getSellerDetail(ussid);
		if (sellerInfoModel != null && CollectionUtils.isNotEmpty(sellerInfoModel.getRichAttribute()))
		{



			richAttributeModel = (List<RichAttributeModel>) sellerInfoModel.getRichAttribute();
		}

		final Date currentDate = new Date();
		final ConsignmentModel consignmentModel = mplOrderService.fetchConsignment(orderEntryData.getConsignment().getCode());

		List<String> returnableDates = new ArrayList<String>();
		if (null != consignmentModel)
		{
			final int returnWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(consignmentModel.getDeliveryDate(),
					currentDate);
			final int actualReturnWindow = Integer.parseInt(richAttributeModel.get(0).getReturnWindow());
			final DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd-MM-yyyy");
			final MplLPHolidaysModel mplLPHolidaysModel = mplConfigFacade
					.getMplLPHolidays(MarketplacecommerceservicesConstants.CAMPAIGN_URL_ALL);
			if (actualReturnWindow >= returnWindow)
			{
				DateTime today = new DateTime().withTimeAtStartOfDay();
				today = today.plusDays(1);
				if ((actualReturnWindow - returnWindow) >= 3)
				{
					if (null != mplLPHolidaysModel && null != mplLPHolidaysModel.getWorkingDays())
					{
						returnableDates = dateUtilHelper.calculatedLpHolidays(mplLPHolidaysModel.getWorkingDays(), dtfOut.print(today),
								3);
					}
					else
					{
						for (int i = 0; i < 3; i++)
						{
							returnableDates.add(dtfOut.print(today.plusDays(i).withTimeAtStartOfDay()));
						}
					}
				}
				else if ((actualReturnWindow - returnWindow) == 2)
				{
					if (null != mplLPHolidaysModel && null != mplLPHolidaysModel.getWorkingDays())
					{
						returnableDates = dateUtilHelper.calculatedLpHolidays(mplLPHolidaysModel.getWorkingDays(), dtfOut.print(today),
								2);
					}
					else
					{
						for (int i = 0; i < 2; i++)
						{
							returnableDates.add(dtfOut.print(today.plusDays(i).withTimeAtStartOfDay()));
						}
					}
				}
				else if ((actualReturnWindow - returnWindow) == 1)
				{
					if (null != mplLPHolidaysModel && null != mplLPHolidaysModel.getWorkingDays())
					{
						returnableDates = dateUtilHelper.calculatedLpHolidays(mplLPHolidaysModel.getWorkingDays(), dtfOut.print(today),
								1);
					}
					else
					{
						for (int i = 0; i < 1; i++)
						{


							returnableDates.add(dtfOut.print(today.plusDays(i).withTimeAtStartOfDay()));
						}
					}

				}
				else if ((actualReturnWindow - returnWindow) == 0)
				{
					if (null != mplLPHolidaysModel && null != mplLPHolidaysModel.getWorkingDays())
					{
						returnableDates = dateUtilHelper.calculatedLpHolidays(mplLPHolidaysModel.getWorkingDays(), dtfOut.print(today),
								0);
					}
					else
					{
						returnableDates.add(dtfOut.print(today.plusDays(0).withTimeAtStartOfDay()));
					}
				}
			}
		}
		return returnableDates;
	}

	/**
	 * @author TECHOUTS save bank details for COD order in case of COMM-FICO call failure
	 * @param codSelfShipData
	 */
	@Override
	public void saveCODReturnsBankDetails(final CODSelfShipData codSelfShipData)
	{
		try
		{
			BankDetailsInfoToFICOHistoryModel codReturnPaymentModel = modelService.create(BankDetailsInfoToFICOHistoryModel.class);
			codReturnPaymentModel = codReturnPaymentInfoReverseConverter.convert(codSelfShipData);
			if (codReturnPaymentModel != null)
			{
				modelService.save(codReturnPaymentModel);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, "Exception Occured during saving COD bank details ");
		}
	}

	/**
	 * @author TECHOUTS
	 * @param codSelfShipData
	 */
	@Override
	public void insertUpdateCustomerBankDetails(final CODSelfShipData codSelfShipData)
	{
		try
		{
			MplCustomerBankAccountDetailsModel customerBankDetailsModel = null;

			customerBankDetailsModel = mplReturnService.getCustomerBakDetailsById(codSelfShipData.getCustomerNumber());

			if (customerBankDetailsModel != null)
			{
				//update existing customer account details
				mplCustomerBankDetailsReverseConverter.convert(codSelfShipData, customerBankDetailsModel);
			}
			else
			{
				// insert new account details
				customerBankDetailsModel = modelService.create(MplCustomerBankAccountDetailsModel.class);
				customerBankDetailsModel = mplCustomerBankDetailsReverseConverter.convert(codSelfShipData);
			}

			if (customerBankDetailsModel != null)
			{
				modelService.save(customerBankDetailsModel);
			}
		}
		catch (final Exception e)
		{

			LOG.error("Exception ocurred while saving customerBankDetails",e);
		}

	}

	@Override
	public CODSelfShipData getCustomerBankDetailsByCustomerId(final String customerId)
	{
		CODSelfShipData codSelfShipData = null;
		try
		{
			final MplCustomerBankAccountDetailsModel customerBankDetailsModel = mplReturnService
					.getCustomerBakDetailsById(customerId);
			//TISRLUAT-50
			if (customerBankDetailsModel != null)
			{
				codSelfShipData = mplCustomerBankDetailsConverter.convert(customerBankDetailsModel);
			}
		}
		catch (final Exception e)
		{
			throw new EtailBusinessExceptions("Exception occured while retriving  customer bank details with customer Id "
					+ customerId);
		}

		return codSelfShipData;

	}

	@Override
	public List<ReturnRequestModel> getListOfReturnRequest(final String orlderId)
	{
		return mplReturnService.getListOfReturnRequest(orlderId);
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

	/**
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}

	/**
	 * @param mplSellerInformationService
	 *           the mplSellerInformationService to set
	 */
	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}

	/**
	 * @author Techouts
	 * @return boolean Return Item Pincode Serviceability
	 */
	@Override
	public boolean implementReturnItem(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String reasonCode, final String ussid, final String ticketTypeCode, final CustomerData customerData,
			final String refundType, final boolean isReturn, final SalesApplication salesApplication,
			final ReturnItemAddressData returnAddress, final String revSealJwlry)
	{


		boolean cancelOrRetrnanable = true;
		boolean omsCancellationStatus = false;
		String pincode = null;


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

			LOG.debug("======***********BOGO or Free Bie available for order" + subOrderModel.getCode() + bogoOrFreeBie);
			LOG.debug("Step 2: =====***********************************Ticket Type code : " + ticketTypeCode);
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
			LOG.debug("Step 2: =====**************************cancelOrRetrnanable : " + cancelOrRetrnanable);
			if (cancelOrRetrnanable)
			{
				final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
						subOrderEntry.getTransactionId());
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
				{

					if (ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) ////TISEE-933
					{
						LOG.debug("Step 3:======************************History creation start for retrun");
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
				LOG.debug("Step 4:==========*************************Ticket is to be created for sub order:"
						+ subOrderDetails.getCode());

				final boolean ticketCreationStatus = createTicketInCRM(subOrderDetails, subOrderEntry, ticketTypeCode, reasonCode,
						refundType, ussid, customerData, subOrderModel, returnAddress, revSealJwlry);

				LOG.debug("Step 4.1:=========***********************Ticket creation status for sub order:" + ticketCreationStatus);
				LOG.debug("Step 5 :============********************** Refund and OMS call started");
				cancelOrRetrnanable = initiateCancellation(ticketTypeCode, subOrderDetails, subOrderEntry, subOrderModel, reasonCode);
				LOG.debug("Step 5.1 :================************************* Refund and OMS call status:" + cancelOrRetrnanable);

				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie) //TISEE-5524
				{
					//Mrupee checking

					//				if (cancelOrRetrnanable && ticketTypeCode.equalsIgnoreCase("R") && !bogoOrFreeBie
					//						&& !subOrderModel.getIsWallet().equals(WalletEnum.MRUPEE))
					//				{
					LOG.debug("Step 6:=================***************************Create return request for Return:"
							+ subOrderDetails.getCode());

					final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
							subOrderEntry.getTransactionId());

					for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntriesModel)
					{
						final boolean returnReqSuccess = createRefund(subOrderModel, abstractOrderEntryModel, reasonCode,
								salesApplication, returnAddress.getPincode(), subOrderDetails);

						LOG.debug("=======================*****************************Return request successful :" + returnReqSuccess);
					}
				}
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(">>>===== Cancel Refund exception occured in implementReturnItem etail non business exception : ", e);

			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(">>>=========== Cancel Refund exception occured in implementReturnItem : ", e);
		}


		try
		{
			LOG.debug("Step 8: ==================************************* Updating commerce consignment status"
					+ omsCancellationStatus);

			if (omsCancellationStatus)
			{
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
			LOG.error("=========================***************Updating commerce consignment status ", e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception ex)
		{
			LOG.error(">>> ====================Exception occured while updating consignment : ", ex);
		}

		return omsCancellationStatus;
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
			final String reasonCode, final SalesApplication salesApplication, final String pinCode, final OrderData subOrderDetails)
	{

		boolean returnReqCreated = false;
		boolean returnLogisticsCheck = true;
		try
		{

			final ReturnRequestModel returnRequestModel = returnService.createReturnRequest(subOrderModel);
			returnRequestModel.setRMA(returnService.createRMA(returnRequestModel));
			//TISEE-5471
			//final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(subOrderModel.getCode()); //Changes for Bulk Return Initiation
			//TISRLUAT-1090 Return Initiate API issue
			final List<ReturnLogisticsResponseData> returnLogisticsRespList = checkReturnLogistics(subOrderDetails, pinCode,
					abstractOrderEntryModel.getTransactionID());
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
			LOG.info(LOG_MSG_CREATE_REFUND_SETTING_TYPE_OF_RETURN + returnLogisticsCheck);
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
			//TISRLUAT-1090 Return Initiate API issue
			/*
			 * if (null != abstractOrderEntryModel) {
			 */
			final RefundEntryModel refundEntryModel = modelService.create(RefundEntryModel.class);
			refundEntryModel.setOrderEntry(abstractOrderEntryModel);
			refundEntryModel.setReturnRequest(returnRequestModel);
			if (null != reasonCode)
			{
				refundEntryModel.setReason(RefundReason.valueOf(getReasonDesc(reasonCode)));
			}
			refundEntryModel.setStatus(ReturnStatus.RETURN_INITIATED);
			refundEntryModel.setAction(ReturnAction.IMMEDIATE);
			refundEntryModel.setNotes(getReasonDesc(reasonCode));
			refundEntryModel.setExpectedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
			refundEntryModel.setReceivedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
			refundEntryModel.setRefundedDate(new Date());
			final List<PaymentTransactionModel> tranactions = subOrderModel.getPaymentTransactions();
			if (CollectionUtils.isNotEmpty(tranactions))
			{
				final PaymentTransactionEntryModel paymentTransEntry = tranactions.iterator().next().getEntries().iterator().next();
				if (paymentTransEntry.getPaymentMode() != null && paymentTransEntry.getPaymentMode().getMode() != null
						&& COD.equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
				{
					refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
				}
				else
				{
					final double amount = (abstractOrderEntryModel.getNetAmountAfterAllDisc() != null ? abstractOrderEntryModel
							.getNetAmountAfterAllDisc().doubleValue() : 0D)
							+ (abstractOrderEntryModel.getCurrDelCharge() != null ? abstractOrderEntryModel.getCurrDelCharge()
									.doubleValue() : 0D)
							+ (abstractOrderEntryModel.getScheduledDeliveryCharge() != null ? abstractOrderEntryModel
									.getScheduledDeliveryCharge().doubleValue() : 0D);

					refundEntryModel.setAmount(NumberUtils.createBigDecimal(Double.toString(amount)));
				}
			}
			modelService.save(refundEntryModel);
			/* } */

			modelService.save(returnRequestModel);

			LOG.debug(LOG_MSG_RMA_NUMBER + returnRequestModel.getRMA());
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
			String ussidJwlry = "";
			for (final OrderEntryData orderEntry : entries)
			{
				final ReturnLogistics returnLogistics = new ReturnLogistics();
				//TISEE-5557
				if (!(orderEntry.isGiveAway() || orderEntry.isIsBOGOapplied()))
				//	|| (null != eachEntry.getAssociatedItems() && !eachEntry.getAssociatedItems().isEmpty())))
				{
					returnLogistics.setOrderId(orderModel.getParentReference().getCode());
					if (null != pincode)
					{
						returnLogistics.setPinCode(pincode);
					}
					if (StringUtils.isNotEmpty(orderEntry.getOrderLineId()))
					{
						transactionId = orderEntry.getOrderLineId();
						returnLogistics.setTransactionId(orderEntry.getOrderLineId());
					}
					else if (StringUtils.isNotEmpty(orderEntry.getTransactionId()))
					{
						transactionId = orderEntry.getTransactionId();
						returnLogistics.setTransactionId(orderEntry.getTransactionId());
					}
				}

				String returnFulfillmentType = null;
				String returnFulfillmentByP1 = null;
				//getting the product code
				final ProductModel productModel = mplOrderFacade.getProductForCode(orderEntry.getProduct().getCode());

				for (final SellerInformationModel sellerInfo : productModel.getSellerInformationRelator())
				{
					if (productModel.getProductCategoryType().equalsIgnoreCase(MarketplacecommerceservicesConstants.FINEJEWELLERY))

					{
						final List<JewelleryInformationModel> jewelleryInfo = jewelleryService.getJewelleryInfoByUssid(orderEntry
								.getSelectedUssid());
						ussidJwlry = (CollectionUtils.isNotEmpty(jewelleryInfo)) ? jewelleryInfo.get(0).getUSSID() : "";

						LOG.debug("PCMUSSID FOR JEWELLERY :::::::::: " + "for " + orderEntry.getSelectedUssid() + " is "
								+ jewelleryInfo.get(0).getPCMUSSID());
					}
					else
					{
						ussidJwlry = sellerInfo.getUSSID();
					}

					/* if (orderEntry.getSelectedUssid().equalsIgnoreCase(sellerInfo.getUSSID())) */
					if (orderEntry.getSelectedUssid().equalsIgnoreCase(ussidJwlry))
					{
						if (CollectionUtils.isNotEmpty(sellerInfo.getRichAttribute()))
						{
							for (final RichAttributeModel richAttribute : sellerInfo.getRichAttribute())
							{
								if (null != richAttribute.getReturnFulfillMode())
								{
									LOG.info(richAttribute.getReturnFulfillMode());
									returnFulfillmentType = richAttribute.getReturnFulfillMode().getCode();
								}

								if (null != richAttribute.getReturnFulfillModeByP1())
								{
									LOG.info(richAttribute.getReturnFulfillModeByP1());
									returnFulfillmentByP1 = richAttribute.getReturnFulfillModeByP1().getCode();
								}
							}
						}
					}
				}

				if (StringUtils.isNotEmpty(returnFulfillmentType))
				{
					returnLogistics.setReturnFulfillmentType(returnFulfillmentType.toUpperCase());
				}
				if (StringUtils.isNotEmpty(returnFulfillmentByP1))
				{
					returnLogistics.setReturnFulfillmentByP1(returnFulfillmentByP1);
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
								/* R2.3 START */
								if (StringUtils.isNotBlank(orderLine.getReturnFulfillmentType()))
								{
									returnLogRespData.setReturnFulfillmentType(orderLine.getReturnFulfillmentType());
								}
								/* R2.3 END */
								returnLogRespDataList.add(returnLogRespData);
								responseList.add(orderLine);

							}

						}
					}
				}
				else
				{
					//TISEE-5357
					LOG.debug(LOG_MSG_REVERSE_LOGISTICS_AVAILABILTY_RESPONSE_ORDERLINE_IS_NULL);
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
	 * Added Code for Return Initiation From SellerPortel
	 */
	@Override
	public List<OrderLineData> returnInitiationForRTS(final List<OrderLineData> orerLines)
	{
		final List<OrderLineData> orderList = new ArrayList<OrderLineData>();
		final MplCancelOrderRequest orderLineRequest = new MplCancelOrderRequest();
		final List<OrderLine> orderLineList = new ArrayList<MplCancelOrderRequest.OrderLine>();
		try
		{
			final OrderModel subOrderModel = orderModelService.getOrder(orerLines.get(0).getOrderId());
			for (final OrderLineData data : orerLines)
			{
				for (final AbstractOrderEntryModel entry : subOrderModel.getEntries())
				{
					if (entry.getTransactionID().equalsIgnoreCase(data.getTransactionId()))
					{
						final ProductModel product = entry.getProduct();
						LOG.info("Product Deatails : " + product.getCode() + "Product Name : " + product.getName());
						final List<SellerInformationModel> sellersList = (List<SellerInformationModel>) product
								.getSellerInformationRelator();
						for (final SellerInformationModel seller : sellersList)
						{
							if (seller.getSellerArticleSKU().equals(entry.getSelectedUSSID()))
							{
								final OrderLineData orderData = getReturnEligibility(seller, entry, subOrderModel.getCode(),
										subOrderModel, data);
								orderList.add(orderData);
								if (orderData != null && orderData.getIsReturnInitiated().equalsIgnoreCase("Y"))
								{
									final OrderLine orderLines = populateOrderLineForRTS(data, entry, subOrderModel);
									orderLineList.add(orderLines);
								}
							}
						}
					}
				}
			}
			if (CollectionUtils.isNotEmpty(orderLineList))
			{
				orderLineRequest.setOrderLine(orderLineList);
				mplOrderCancelClientService.orderCancelDataToOMS(orderLineRequest);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("CancelReturnFacadeImpl::::returnInitiationForRTS", e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final EtailBusinessExceptions e)
		{
			LOG.error("CancelReturnFacadeImpl:::returnInitiationForRTS");
		}

		return orderList;
	}

	private OrderLineData getReturnEligibility(final SellerInformationModel seller, final AbstractOrderEntryModel entry,
			final String orderId, final OrderModel subOrder, final OrderLineData data)
	{
		final List<RichAttributeModel> richAttributeModelForSeller = (List<RichAttributeModel>) seller.getRichAttribute();
		if (Integer.parseInt(richAttributeModelForSeller.get(0).getReturnWindow()) == 0)
		{
			return getRTSResponseData(orderId, entry.getTransactionID(), false);
		}
		else
		{
			if (CollectionUtils.isNotEmpty(entry.getConsignmentEntries()))
			{
				for (final ConsignmentEntryModel ceModel : entry.getConsignmentEntries())
				{
					String consignmentStatus = null;
					final ConsignmentModel cModel = ceModel.getConsignment();
					if (StringUtils.isNotEmpty(cModel.getStatus().getCode()))
					{
						consignmentStatus = cModel.getStatus().getCode();
					}
					if (null != consignmentStatus
							&& consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED)
							|| consignmentStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.ORDER_COLLECTED))
					{
						final Date sDate = new Date();
						final int returnWindow = GenericUtilityMethods.noOfDaysCalculatorBetweenDates(cModel.getDeliveryDate(), sDate);
						final int actualReturnWindow = Integer.parseInt(richAttributeModelForSeller.get(0).getReturnWindow());
						if (returnWindow <= actualReturnWindow)
						{
							updateConsignmentStatus(entry, ConsignmentStatus.RETURN_INITIATED);
							createHistoryEntry(entry, subOrder, ConsignmentStatus.RETURN_INITIATED);
							createRefundForRTS(subOrder, entry, data, SalesApplication.WEB);
							return getRTSResponseData(orderId, entry.getTransactionID(), true);
						}
						else
						{
							return getRTSResponseData(orderId, entry.getTransactionID(), false);
						}

					}
					else
					{
						return getRTSResponseData(orderId, entry.getTransactionID(), false);
					}

				}
			}
		}
		return new OrderLineData();
	}

	private OrderLineData getRTSResponseData(final String orderId, final String transactionID, final boolean flag)
	{
		if (StringUtils.isNotEmpty(orderId) && StringUtils.isNotEmpty(transactionID))
		{
			final OrderLineData responseData = new OrderLineData();
			responseData.setOrderId(orderId);
			responseData.setTransactionId(transactionID);
			if (!flag)
			{
				responseData.setIsReturnEligible("N");
				responseData.setIsReturnInitiated("N");
			}
			else
			{
				responseData.setIsReturnEligible("Y");
				responseData.setIsReturnInitiated("Y");
			}
			return responseData;
		}
		return new OrderLineData();
	}

	private OrderLine populateOrderLineForRTS(final OrderLineData data, final AbstractOrderEntryModel abstractOrderentry,
			final OrderModel subOrder)
	{
		final MplCancelOrderRequest.OrderLine orderLineData = new MplCancelOrderRequest.OrderLine();
		orderLineData.setOrderId(subOrder.getParentReference().getCode());
		orderLineData.setReasonCode(data.getReasonCode());
		orderLineData.setRequestID(abstractOrderentry.getSelectedUSSID() + MarketplacecommerceservicesConstants.EMPTY
				+ System.currentTimeMillis());
		orderLineData.setReturnCancelFlag("R");
		orderLineData.setReturnCancelRemarks(getReasonDesc(data.getReasonCode()));
		if (StringUtils.isNotEmpty(abstractOrderentry.getOrderLineId()))
		{
			orderLineData.setTransactionId(abstractOrderentry.getOrderLineId());
		}
		else if (StringUtils.isNotEmpty(abstractOrderentry.getTransactionID()))
		{
			orderLineData.setTransactionId(abstractOrderentry.getTransactionID());
		}
		return orderLineData;
	}

	private boolean createRefundForRTS(final OrderModel subOrderModel, final AbstractOrderEntryModel abstractOrderEntryModel,
			final OrderLineData orderLineData, final SalesApplication salesApplication)
	{
		boolean returnReqCreated = false;
		final boolean returnLogisticsCheck = true;
		try
		{
			final ReturnRequestModel returnRequestModel = returnService.createReturnRequest(subOrderModel);
			returnRequestModel.setRMA(returnService.createRMA(returnRequestModel));
			LOG.info(LOG_MSG_CREATE_REFUND_SETTING_TYPE_OF_RETURN + returnLogisticsCheck);
			returnRequestModel.setTypeofreturn(TypeofReturn.REVERSE_PICKUP);
			if (salesApplication != null)
			{
				returnRequestModel.setReturnRaisedFrom(salesApplication);
			}
			if (null != abstractOrderEntryModel)
			{
				final RefundEntryModel refundEntryModel = modelService.create(RefundEntryModel.class);
				refundEntryModel.setOrderEntry(abstractOrderEntryModel);
				refundEntryModel.setReturnRequest(returnRequestModel);
				refundEntryModel.setReason(RefundReason.valueOf(getReasonDesc(orderLineData.getReasonCode())));
				refundEntryModel.setStatus(ReturnStatus.RETURN_INITIATED);
				refundEntryModel.setAction(ReturnAction.IMMEDIATE);
				refundEntryModel.setNotes(getReasonDesc(orderLineData.getReasonCode()));
				refundEntryModel.setExpectedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
				refundEntryModel.setReceivedQuantity(abstractOrderEntryModel.getQuantity());//Single line quantity
				refundEntryModel.setRefundedDate(new Date());
				refundEntryModel.setRefundMode(orderLineData.getRefundMode());
				final List<PaymentTransactionModel> tranactions = subOrderModel.getPaymentTransactions();
				if (CollectionUtils.isNotEmpty(tranactions))
				{
					final PaymentTransactionEntryModel paymentTransEntry = tranactions.iterator().next().getEntries().iterator()
							.next();

					if (paymentTransEntry.getPaymentMode() != null && paymentTransEntry.getPaymentMode().getMode() != null
							&& COD.equalsIgnoreCase(paymentTransEntry.getPaymentMode().getMode()))
					{
						refundEntryModel.setAmount(NumberUtils.createBigDecimal("0"));
					}
					else
					{
						final double amount = (abstractOrderEntryModel.getNetAmountAfterAllDisc() != null ? abstractOrderEntryModel
								.getNetAmountAfterAllDisc().doubleValue() : 0D)
								+ (abstractOrderEntryModel.getCurrDelCharge() != null ? abstractOrderEntryModel.getCurrDelCharge()
										.doubleValue() : 0D)
								+ (abstractOrderEntryModel.getScheduledDeliveryCharge() != null ? abstractOrderEntryModel
										.getScheduledDeliveryCharge().doubleValue() : 0D);

						refundEntryModel.setAmount(NumberUtils.createBigDecimal(Double.toString(amount)));
					}
				}
				modelService.save(refundEntryModel);
			}
			modelService.save(returnRequestModel);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(LOG_MSG_RMA_NUMBER + returnRequestModel.getRMA());
			}
			returnReqCreated = true;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return returnReqCreated;
	}


	/**
	 * This method will save Proof of dispatcher information saved flag
	 */
	@Override
	public void saveRTSAndRSSFInfoflag(final String transactionId)
	{
		try
		{
			final AbstractOrderEntryModel abstractOrderEntryModel = mplOrderService.getEntryModel(transactionId);
			abstractOrderEntryModel.setIsRefundable(true);
			LOG.info("Cancel Return facade transactionId::::::::::::" + abstractOrderEntryModel);
			modelService.save(abstractOrderEntryModel);
			LOG.info("AWB Details Saved For Self Shipment as true");
			if (LOG.isDebugEnabled())
			{
				LOG.debug("***************  number:" + transactionId);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByDates(final Date fromDate, final Date toDate)
	{
		return mplReturnService.getPickUpReturnReportByDates(fromDate, toDate);
	}

	@Override
	public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByParams(final String orderID, final String customerId,
			final String pincode)
	{
		return mplReturnService.getPickUpReturnReportByParams(orderID, customerId, pincode);
	}

	/***
	 * Send Notification For Return TO Store
	 *
	 * @param customerId
	 * @param OTPNumber
	 */

	private void sendPushNotificationForReturnToStore(final CustomerModel customerModel, final List<String> storeNameList,
			final String mobileNumber, final String ordernumber, final String date)
	{
		final String mplCustomerName = customerModel.getFirstName();
		StringBuilder storeAddress = null;
		StringBuilder storeName = null;
		if (null != storeNameList)
		{
			if (CollectionUtils.isNotEmpty(storeNameList))
			{
				for (final String store : storeNameList)
				{
					final PointOfServiceModel pointOfSerivce = orderModelService.getPointOfService(store);
					final String geoCodeUrl = ", http://maps.google.com/?q=" + pointOfSerivce.getLatitude() + ","
							+ pointOfSerivce.getLongitude();
					if (storeAddress == null)
					{
						storeName = new StringBuilder(store);
						storeAddress = storeAddress(pointOfSerivce.getAddress(), pointOfSerivce.getDisplayName(), null);
						storeAddress.append(geoCodeUrl);
					}
					else
					{
						storeName.append(MarketplacecommerceservicesConstants.COMMA);
						storeName.append(store);
						storeAddress = storeAddress(pointOfSerivce.getAddress(), pointOfSerivce.getDisplayName(), storeAddress);
						storeAddress.append(geoCodeUrl);
					}
				}
			}
		}
		sendSMSFacade.sendSms(
				MarketplacecommerceservicesConstants.SMS_SENDER_ID,
				MarketplacecommerceservicesConstants.SMS_MESSAGE_RETURN_TO_STORE
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ZERO,
								mplCustomerName != null ? mplCustomerName : "There")
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_ONE, ordernumber)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_TWO, storeName)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_THREE, date)
						.replace(MarketplacecommerceservicesConstants.SMS_VARIABLE_FOUR, storeAddress.toString()), mobileNumber);

	}

	private StringBuilder storeAddress(final AddressModel address, final String diaplayName, StringBuilder store)
	{
		if (address != null)
		{
			if (StringUtils.isNotEmpty(diaplayName))
			{
				if (store == null)
				{
					store = new StringBuilder(diaplayName);
				}
				else
				{
					store.append(MarketplacecommerceservicesConstants.COMMA + SPACE + diaplayName);
				}
			}
			if (StringUtils.isNotEmpty(address.getLine1()))
			{
				store.append(SPACE + address.getLine1());
			}
			if (StringUtils.isNotEmpty(address.getLine2()))
			{
				store.append(SPACE + address.getLine2());
			}
			if (StringUtils.isNotEmpty(address.getAddressLine3()))
			{
				store.append(SPACE + address.getAddressLine3());
			}
			if (StringUtils.isNotEmpty(address.getAppartment()))
			{
				store.append(SPACE + address.getAppartment());
			}
			if (StringUtils.isNotEmpty(address.getLandmark()))
			{
				store.append(SPACE + address.getLandmark());
			}
			if (StringUtils.isNotEmpty(address.getCity()))
			{
				store.append(SPACE + address.getCity());
			}
			if (StringUtils.isNotEmpty(address.getDistrict()))
			{
				store.append(SPACE + address.getDistrict());
			}
			if (StringUtils.isNotEmpty(address.getState()))
			{
				String stateName = getStateCode(address.getState());
				if (stateName == null)
				{
					stateName = address.getState();
				}
				store.append(SPACE + stateName);
			}
			if (StringUtils.isNotEmpty(address.getCountry().getName()))
			{
				store.append(SPACE + address.getCountry().getName());
			}
		}
		if (StringUtils.isNotEmpty(address.getPostalcode()))
		{
			store.append(SPACE + address.getPostalcode());
		}
		if (StringUtils.isNotEmpty(address.getPhone1()))
		{
			store.append(SPACE + address.getPostalcode());
		}
		return store;
	}

	//get Entry Model
	private AbstractOrderEntryModel getOrderEntryModel(final OrderModel ordermodel, final String transactionId)
	{
		for (final OrderModel subOrder : ordermodel.getChildOrders())
		{

			for (final AbstractOrderEntryModel entry : subOrder.getEntries())
			{
				if (entry.getTransactionID().equalsIgnoreCase(transactionId))
				{
					return entry;
				}
			}
		}
		return null;
	}

	//get all store Address
	private List<String> getStoreAddressList(final List<String> storeIdList)
	{
		final List<String> storeAddresList = new ArrayList<String>();
		StringBuilder storeAddress = null;
		PointOfServiceModel pointOfSerivce = null;
		String geoCodeUrl = null;
		for (final String storeId : storeIdList)
		{
			pointOfSerivce = orderModelService.getPointOfService(storeId);

			geoCodeUrl = " , http://maps.google.com/?q=" + pointOfSerivce.getLatitude() + "," + pointOfSerivce.getLongitude();
			storeAddress = new StringBuilder();
			storeAddress = storeAddress(pointOfSerivce.getAddress(), pointOfSerivce.getDisplayName(), null);
			storeAddress.append(geoCodeUrl);
			storeAddresList.add(storeAddress.toString());
		}
		return storeAddresList;
	}

	//getReturnToStoreDate
	private String getDateReturnToStore(final AbstractOrderEntryModel entryModel)
	{
		int refundWindow = 0;
		int daysRemaining = 0;
		DateTime deliveryTime = null;
		int totalDaysPassed = 0;
		final SellerInformationModel sellerInfo = mplSellerInformationService.getSellerDetail(entryModel.getSelectedUSSID());

		for (final RichAttributeModel richAttribute : sellerInfo.getRichAttribute())
		{
			if (richAttribute.getReturnWindow() != null && Integer.parseInt(richAttribute.getReturnWindow()) > 0)
			{
				refundWindow = Integer.parseInt(richAttribute.getReturnWindow());
				break;
			}
		}
		final DateTime currentTime = new DateTime(new Date().getTime());
		final Set consignments = BasecommerceManager.getInstance().getConsignments(
				(Order) modelService.toPersistenceLayer(entryModel.getOrder()));
		if (!consignments.isEmpty())
		{
			for (final Iterator iterator = consignments.iterator(); iterator.hasNext();)
			{
				final Consignment consignment = (Consignment) iterator.next();
				final ConsignmentModel consignmentModel = modelService.get(consignment);
				if (consignment.getStatus().getCode().equals(ConsignmentStatus.DELIVERED.getCode())
						|| consignment.getStatus().getCode().equals(ConsignmentStatus.ORDER_COLLECTED.getCode()))
				{

					if (null != consignmentModel.getDeliveryDate())
					{
						deliveryTime = new DateTime(consignmentModel.getDeliveryDate().getTime());
					}
					totalDaysPassed = Days.daysBetween(deliveryTime, currentTime).getDays();
				}
			}
		}
		daysRemaining = refundWindow - totalDaysPassed;
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, daysRemaining);
		final DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * S-SHIP breach Order Cancellation from BackOffice
	 *
	 * @param orderCode
	 * @param transactionId
	 * @return boolen
	 */
	@Override
	public boolean orderCancellationFromBackoffice(final String orderCode, final String transactionId) throws Exception
	{
		String ussid = StringUtils.EMPTY;
		OrderEntryData subOrderEntry = null;
		boolean cancellationStatus = false;
		try
		{
			final String refundType = "S";
			LOG.info("Cancelled Order Id " + orderCode + " Transacsaction Id " + transactionId);
			final CustomerData customerData = new CustomerData();
			final OrderModel orderModel = orderModelService.getParentOrder(orderCode);
			final CustomerModel customerModel = (CustomerModel) orderModel.getUser();
			customerPopulator.populate(customerModel, customerData);
			final List<OrderModel> subOrderList = orderModel.getChildOrders();
			OrderModel subOrderModel = new OrderModel();
			for (final OrderModel subOrder : subOrderList)
			{
				for (final AbstractOrderEntryModel subOrderEntry1 : subOrder.getEntries())
				{
					if (subOrderEntry1.getTransactionID().equalsIgnoreCase(transactionId))
					{
						subOrderModel = subOrder;
						break;
					}
				}
			}

			//TISRLEE-1703
			final OrderData subOrderDetails = mplCheckoutFacade.getOrderDetailsForCode(subOrderModel);

			for (final OrderEntryData orderEntry : subOrderDetails.getEntries())
			{
				if (transactionId.equalsIgnoreCase(orderEntry.getTransactionId()))
				{
					subOrderEntry = orderEntry;
					ussid = subOrderEntry.getProduct().getUssID();
					break;
				}
			}
			cancellationStatus = implementCancelOrReturn(subOrderDetails, subOrderEntry, "05", ussid, "C", customerData, refundType,
					false, SalesApplication.WEB, SSB);
		}
		catch (final Exception e)
		{
			LOG.info(" Exception while canceling the order from backOffice  " + e.getMessage());
			e.printStackTrace();
		}
		return cancellationStatus;

	}

	@Override
	public void returnRssCRMRequest(final ReturnRequestDTO returnRequestDTO)
	{
		mplReturnService.returnRssCRMRequest(returnRequestDTO);
	}

	//Get State name
	private String getStateCode(final String statcode)
	{
		for (final StateData state : accountAddressFacade.getStates())
		{
			if (state.getCode().equalsIgnoreCase(statcode))
			{
				return state.getName();
			}
		}
		return null;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade#checkReturnLogisticsForApp(de.hybris.platform.
	 * commercefacades.order.data.OrderData, java.lang.String, java.lang.String)
	 */
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

				// Added in R2.3 START
				String returnFulfillmentType = null;
				String returnFulfillmentByP1 = null;
				//getting the product code
				final ProductModel productModel = mplOrderFacade.getProductForCode(eachEntry.getProduct().getCode());

				for (final SellerInformationModel sellerInfo : productModel.getSellerInformationRelator())
				{
					if (eachEntry.getSelectedUssid().equalsIgnoreCase(sellerInfo.getUSSID()))
					{
						if (CollectionUtils.isNotEmpty(sellerInfo.getRichAttribute()))
						{
							for (final RichAttributeModel richAttribute : sellerInfo.getRichAttribute())
							{
								if (null != richAttribute.getReturnFulfillMode())
								{
									LOG.info(richAttribute.getReturnFulfillMode());
									returnFulfillmentType = richAttribute.getReturnFulfillMode().getCode();
								}

								if (null != richAttribute.getReturnFulfillModeByP1())
								{
									LOG.info(richAttribute.getReturnFulfillModeByP1());
									returnFulfillmentByP1 = richAttribute.getReturnFulfillModeByP1().getCode();
								}
							}
						}
					}
				}

				if (StringUtils.isNotEmpty(returnFulfillmentType))
				{
					returnLogistics.setReturnFulfillmentType(returnFulfillmentType.toUpperCase());
				}
				if (StringUtils.isNotEmpty(returnFulfillmentByP1))
				{
					returnLogistics.setReturnFulfillmentByP1(returnFulfillmentByP1);
				}

				// Added in R2.3 END
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
						if (null != orderLine.getReturnFulfillmentType())
						{
							returnLogisticsResponseDTO.setReturnFullfillmentType(orderLine.getReturnFulfillmentType());
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
					LOG.debug(LOG_MSG_REVERSE_LOGISTICS_AVAILABILTY_RESPONSE_ORDERLINE_IS_NULL);
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
	 * Method: for Return part of one touch CRM--TPR-1345
	 *
	 * @param subOrderDetails
	 * @param subOrderEntry
	 * @param ticketTypeCode
	 * @param salesApplication
	 * @param returnPincode
	 * @return OMS cancellation status
	 */
	@Override
	public boolean oneTouchReturn(final OrderData subOrderDetails, final OrderEntryData subOrderEntry,
			final String reasonCodeSource, final String ticketTypeCode, final SalesApplication salesApplication,
			final String returnPincode, final List<AbstractOrderEntryModel> orderEntries, final OrderModel subOrderModel,
			final CODSelfShipData codSelfShipData, final String ussid, final String txnId)
	{
		final boolean isReturn = true;
		boolean cancelOrRetrnanable = true;
		boolean omsCancellationStatus = false;
		String returnFulfillmentType = null;

		String reasonCode = null;
		if (reasonCodeSource.length() < 2)
		{
			reasonCode = 0 + reasonCodeSource;
			LOG.debug("==========================" + reasonCode);
		}
		else
		{
			reasonCode = reasonCodeSource;
			LOG.debug("else reason==============" + reasonCode);
		}

		try
		{
			//New addition for fectching return fullfillment type/mode
			final List<ReturnLogisticsResponseData> returnLogisticsRespList = checkReturnLogistics(subOrderDetails, returnPincode,
					txnId);
			if (CollectionUtils.isNotEmpty(returnLogisticsRespList))
			{
				for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
				{
					if (StringUtils.isNotEmpty(response.getReturnFulfillmentType()))
					{
						returnFulfillmentType = response.getReturnFulfillmentType();
						break;
					}

				}
			}


			MplCancelOrderRequest orderLineRequest = new MplCancelOrderRequest();
			LOG.debug("Step 2: ------------------****************Ticket Type code : " + ticketTypeCode);
			//orderLineRequest = populateOrderLineData(subOrderEntry, ticketTypeCode, subOrderModel, reasonCode, returnPincode);
			orderLineRequest = populateOrderLineData(subOrderEntry, ticketTypeCode, subOrderModel, reasonCode, ussid, returnPincode,
					returnFulfillmentType);
			if (CollectionUtils.isNotEmpty(orderLineRequest.getOrderLine()))
			{
				cancelOrRetrnanable = cancelOrderInOMS(orderLineRequest, cancelOrRetrnanable, isReturn);
			}
			LOG.debug("Step 2: ------------------****************cancelOrRetrnanable : " + cancelOrRetrnanable);
			if (cancelOrRetrnanable)
			{
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntries)
				{
					LOG.debug("Step 3:------------------****************History creation start for retrun");
					createHistoryEntry(abstractOrderEntryModel, subOrderModel, ConsignmentStatus.RETURN_INITIATED);
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
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntries)
				{
					final boolean returnReqSuccess = createRefund(subOrderModel, abstractOrderEntryModel, reasonCode,
							salesApplication, returnPincode, subOrderDetails);
					LOG.debug("------------------***************Return request successful :" + returnReqSuccess);
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
			LOG.debug("Step 8: ------------------**************** Updating commerce consignment status" + omsCancellationStatus);

			if (omsCancellationStatus)
			{
				for (final AbstractOrderEntryModel abstractOrderEntryModel : orderEntries)
				{
					updateConsignmentStatus(abstractOrderEntryModel, ConsignmentStatus.RETURN_INITIATED);
				}
			}

		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("------------------Updating commerce consignment status ", e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception ex)
		{
			LOG.error(">>> Exception occured while updating consignment : ", ex);
		}
		return omsCancellationStatus;
	}

	/**
	 * Method: for cancellation part of one touch CRM--TPR-1345
	 *
	 * @param subOrderDetails
	 * @param subOrderEntry
	 * @param reasonCode
	 * @param ussid
	 * @param ticketTypeCode
	 * @param refundType
	 * @param isReturn
	 * @param salesApplication
	 * @return OMS cancellation status
	 */
	@Override
	public boolean oneTouchCancel(final OrderModel subOrderModel, final OrderData subOrderDetails,
			final OrderEntryData subOrderEntry, final String reasonCodeSource, final String ussid, final String ticketTypeCode,
			String refundType, final boolean isReturn, final SalesApplication salesApplication,
			final List<AbstractOrderEntryModel> abstractOrderEntry)
	{
		boolean cancelOrRetrnanable = true;
		boolean omsCancellationStatus = false;
		boolean bogoOrFreeBie = false;
		String reasonCode = null;
		if (reasonCodeSource.length() < 2)
		{
			reasonCode = 0 + reasonCodeSource;
			LOG.debug("==========================" + reasonCode);
		}
		else
		{
			reasonCode = reasonCodeSource;
			LOG.debug("else reason==============" + reasonCode);
		}
		try
		{
			MplCancelOrderRequest orderLineRequest = new MplCancelOrderRequest();
			if (CollectionUtils.isNotEmpty(subOrderEntry.getAssociatedItems()))
			{
				bogoOrFreeBie = true;
			}
			LOG.debug("----------------****BOGO or Free Bie available for order" + subOrderModel.getCode() + bogoOrFreeBie);
			LOG.debug("Step 2: ------------------****************Ticket Type code : " + ticketTypeCode);
			if ((ticketTypeCode.equalsIgnoreCase("C")))
			{
				orderLineRequest = populateOrderLineData(subOrderEntry, ticketTypeCode, subOrderModel, reasonCode);

				if (CollectionUtils.isNotEmpty(orderLineRequest.getOrderLine()))
				{
					cancelOrRetrnanable = cancelOrderInOMS(orderLineRequest, cancelOrRetrnanable, isReturn);
				}
			}
			LOG.debug("Step 2: ------------------****************cancelOrRetrnanable : " + cancelOrRetrnanable);
			if (cancelOrRetrnanable)
			{
				//				final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
				//						subOrderEntry.getTransactionId());
				for (final AbstractOrderEntryModel abstractOrderEntryModel : abstractOrderEntry)
				{
					if (ticketTypeCode.equalsIgnoreCase("C"))
					{
						LOG.debug("Step 3:------------------****************History creation start for cancellation");
						createHistoryEntry(abstractOrderEntryModel, subOrderModel, ConsignmentStatus.CANCELLATION_INITIATED);
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
				LOG.debug("Step 4:------------------****************Ticket is to be created for sub order:"
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
				LOG.debug("Step 5 :------------------**************** Refund and OMS call started");
				cancelOrRetrnanable = initiateCancellation(ticketTypeCode, subOrderDetails, subOrderEntry, subOrderModel, reasonCode);
				LOG.debug("Step 5.1 :------------------**************** Refund and OMS call status:" + cancelOrRetrnanable);
			}
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error(">>>------------ Cancel Refund exception occured in implementCancelOrReturn Etail Non BusinessException: ", e);
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.error(">>>-------------- Cancel Refund exception occured in implementCancelOrReturn in implementCancelOrReturn ", e);
		}


		try
		{
			LOG.debug("Step 8: ------------------**************** Updating commerce consignment status" + omsCancellationStatus);

			if (omsCancellationStatus)
			{
				//				final List<AbstractOrderEntryModel> orderEntriesModel = associatedEntries(subOrderModel,
				//						subOrderEntry.getTransactionId());
				for (final AbstractOrderEntryModel abstractOrderEntryModel : abstractOrderEntry)
				{
					if (ticketTypeCode.equalsIgnoreCase("C"))
					{
						updateConsignmentStatus(abstractOrderEntryModel, ConsignmentStatus.CANCELLATION_INITIATED);

					}
				}
			}
		}
		/*
		 * catch (final EtailNonBusinessExceptions e) {
		 * LOG.error("------------------Updating commerce consignment status ", e);
		 * ExceptionUtil.etailNonBusinessExceptionHandler(e); }
		 */
		catch (final Exception ex)
		{
			LOG.error(">>>------------------ Exception occured while updating consignment : ", ex);
		}

		return omsCancellationStatus;
	}

	/**
	 * Method: for pincode serviceability part of one touch CRM--TPR-1345
	 *
	 * @param subOrderDetails
	 * @param returnPincode
	 * @param txnId
	 * @return true, if serviceable
	 * @throws Exception
	 */
	@Override
	public boolean oneTouchPincodeCheck(final OrderData subOrderDetails, final String returnPincode, final String txnId)
			throws Exception
	{
		boolean pincodeCheck = true;
		try
		{
			final List<ReturnLogisticsResponseData> returnLogisticsRespList = checkReturnLogistics(subOrderDetails, returnPincode,
					txnId);
			LOG.debug("======Checking pincode response=======");
			if (CollectionUtils.isNotEmpty(returnLogisticsRespList))
			{
				for (final ReturnLogisticsResponseData response : returnLogisticsRespList)
				{
					if (StringUtils.isNotEmpty(response.getIsReturnLogisticsAvailable())
							&& response.getIsReturnLogisticsAvailable().equalsIgnoreCase("N"))
					{
						LOG.debug("======Checking pincode value======" + response.getIsReturnLogisticsAvailable());
						pincodeCheck = false;
						break;
					}

				}
			}
		}
		catch (final Exception ex)
		{
			LOG.error(ex.getMessage());
		}
		return pincodeCheck;
	}

	@Override
	public boolean appliedPromotionCheckOnetouch(final OrderModel subOrderModel)
	{
		LOG.info("Starting executing appliedPromotion method of cancelReturnFacadeImpl....");
		final List<PromotionResultModel> promotionlist = new ArrayList<PromotionResultModel>(subOrderModel.getAllPromotionResults());
		boolean isBuyAandBgetC = false;
		final Iterator<PromotionResultModel> iter2 = promotionlist.iterator();
		while (iter2.hasNext())
		{
			final PromotionResultModel model2 = iter2.next();
			if (StringUtils.isNotEmpty(model2.getPromotion().getPromotionType())
					&& model2.getPromotion().getPromotionType().equalsIgnoreCase("Tata Etail - Buy A and B get C Free"))

			{
				isBuyAandBgetC = true;
				LOG.debug("===Promotion Type of BuyAandBgetC-->" + isBuyAandBgetC);
			}
		}
		LOG.info("Finished executing appliedPromotion method of cancelReturnFacadeImpl....");
		return isBuyAandBgetC;
	}




}
