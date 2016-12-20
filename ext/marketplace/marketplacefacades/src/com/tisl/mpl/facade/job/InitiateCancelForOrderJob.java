package com.tisl.mpl.facade.job;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.BulkCancellationProcessModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.services.BaseStoreService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.data.BulkCancelStoreData;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class InitiateCancelForOrderJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(InitiateCancelForOrderJob.class.getName());
	@Autowired
	private CancelReturnFacade cancelReturnFacade;
	@Autowired
	private MplCheckoutFacade mplCheckoutFacade;
	@Autowired
	private OrderModelService orderModelService;
	@Autowired
	private MplOrderFacade mplOrderFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Autowired
	private BaseStoreService baseStoreService;
	private Converter<OrderModel, OrderData> orderConverter;
	@Autowired
	private PriceDataFactory priceDataFactory;


	@Autowired
	private ConfigurationService configurationService;


	@SuppressWarnings(MarketplacecommerceservicesConstants.BOXING)
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		try
		{

			String orderCode = null;
			String transactionId = null;
			OrderData subOrderDetailsForMap = null;
			OrderEntryData subOrderEntry = null;
			//int counter = 0;
			final Map<OrderEntryData, BulkCancelStoreData> cancellationDataMap = new HashMap<OrderEntryData, BulkCancelStoreData>();
			List<BulkCancellationProcessModel> finalModelToSaveList = new ArrayList<BulkCancellationProcessModel>();
			//			final long startTime = System.currentTimeMillis();
			//			LOG.info(MarketplacecommerceservicesConstants.START_TIME_C + startTime);
			final List<BulkCancellationProcessModel> bulkList = orderModelService.getBulkCancelData();

			if (CollectionUtils.isNotEmpty(bulkList))
			{

				for (final BulkCancellationProcessModel bulkModel : bulkList)
				{
					if (null != bulkModel && null != bulkModel.getParentOrderNo() && null != bulkModel.getTransactionId())
					{
						orderCode = bulkModel.getParentOrderNo();
						transactionId = bulkModel.getTransactionId();

						LOG.info(MarketplacecommerceservicesConstants.BULK_CANCEL_LOG_STEP_2_1 + bulkModel.getParentOrderNo()
								+ MarketplacecommerceservicesConstants.BULK_CANCEL_LOG_STEP_2_2 + bulkModel.getTransactionId());

						final OrderModel orderModel = orderModelService.getParentOrder(orderCode);
						for (final OrderModel subOrder : orderModel.getChildOrders())
						{
							try
							{
								subOrderDetailsForMap = convertToData(subOrder);

								// Fetch USSID and orderEntry from Sub Order
								final List<OrderEntryData> subOrderEntries = subOrderDetailsForMap.getEntries();
								for (final OrderEntryData orderEntry : subOrderEntries)
								{
									if (orderEntry.getTransactionId().equalsIgnoreCase(transactionId))
									{
										final BulkCancelStoreData bulkCancelStoreData = new BulkCancelStoreData();
										// SubOrderDetails are added into Map having the key as subOrderEntry
										subOrderEntry = orderEntry;
										bulkCancelStoreData.setSubOrderDetails(subOrderDetailsForMap);
										bulkCancelStoreData.setBulkCancelModelData(bulkModel);
										cancellationDataMap.put(orderEntry, bulkCancelStoreData);
										break;
									}
								}
								if (null != subOrderEntry)
								{
									break;
								}
							}
							catch (final Exception e)
							{
								LOG.error(MarketplacecommerceservicesConstants.BULK_CANCEL_LOG_STEP_11);
								continue;
							}
						}
					}
					else
					{

						bulkModel.setLoadStatus(MarketplacecommerceservicesConstants.FAILURE_LOAD_STATUS);
						bulkModel.setStatusDescription(MarketplacecommerceservicesConstants.FAILURE);
						finalModelToSaveList.add(bulkModel);
					}
					//counter++;
				}

				// Iterate the HashMap to trigger the bulk cancellation process
				if (!cancellationDataMap.isEmpty() && cancellationDataMap.size() > 0)
				{

					finalModelToSaveList = callOMStoCancelOrder(cancellationDataMap);

				}

				//save bulk cancel data
				saveBulkCancelData(finalModelToSaveList);
			}
			else
			{
				LOG.error(MarketplacecommerceservicesConstants.BULK_CANCEL_LOG_STEP_14);
			}
			//			final long endTime = System.currentTimeMillis();
			//			LOG.info(MarketplacecommerceservicesConstants.END_TIME_C + endTime);
		}
		catch (final EtailBusinessExceptions exception)
		{
			LOG.error("", exception);
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions exception)
		{

			LOG.error(exception.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception e)
		{
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @param finalModelToSaveList
	 *
	 */
	private void saveBulkCancelData(final List<BulkCancellationProcessModel> finalModelToSaveList)
	{


		final int maxAllowedSize = configurationService.getConfiguration().getInt(
				MarketplacecommerceservicesConstants.initiate_cancel_job_cancellation_count, 1);

		if (finalModelToSaveList.size() > maxAllowedSize)//save data in sub batches
		{
			int cnt = 0;
			while (cnt <= finalModelToSaveList.size())
			{

				List<BulkCancellationProcessModel> subList = null;
				if (maxAllowedSize < (finalModelToSaveList.size() - cnt))
				{
					subList = finalModelToSaveList.subList(cnt, (cnt + maxAllowedSize));
				}
				else
				{
					subList = finalModelToSaveList.subList(cnt, (finalModelToSaveList.size()));//get rest of the data
				}

				modelService.saveAll(subList);
				cnt = cnt + maxAllowedSize;
			}
		}
		else
		{
			modelService.saveAll(finalModelToSaveList); //save all data
		}

	}


	/**
	 * @param cancellationDataMap
	 * @return List
	 *
	 */
	private List<BulkCancellationProcessModel> callOMStoCancelOrder(
			final Map<OrderEntryData, BulkCancelStoreData> cancellationDataMap)
	{
		String ussid = null;
		final String ticketTypeCode = MarketplacecommerceservicesConstants.TICKETTYPECODE_CANCEL;
		final String refundType = MarketplacecommerceservicesConstants.REFUNDTYPE;
		final String reasonCode = MarketplacecommerceservicesConstants.REASONCODE;
		String orderConsignmentStatus = null;
		CustomerData customerData = null;
		boolean cancellationStatus = false;
		boolean isCancellable = false;
		OrderEntryData subOrderEntry = null;
		OrderData subOrderDetails = null;
		final List<BulkCancellationProcessModel> finalModelToSaveList = new ArrayList<BulkCancellationProcessModel>();

		for (final OrderEntryData entryHashMap : cancellationDataMap.keySet())
		{
			//BulkCancelStoreData mplCancelStoreData = new BulkCancelStoreData();
			//BulkCancellationProcessModel bulkCancelModel = new BulkCancellationProcessModel();
			subOrderEntry = entryHashMap;
			final BulkCancelStoreData mplCancelStoreData = cancellationDataMap.get(entryHashMap);
			subOrderDetails = mplCancelStoreData.getSubOrderDetails();
			customerData = subOrderDetails.getCustomerData();
			final BulkCancellationProcessModel bulkCancelModel = mplCancelStoreData.getBulkCancelModelData();
			ussid = subOrderEntry.getSelectedUssid();


			// Check consignment status (if present) and cancellable flag

			if (null == subOrderEntry.getConsignment() && subOrderEntry.getQuantity().intValue() != 0
					&& null != subOrderDetails.getStatus())
			{

				orderConsignmentStatus = subOrderDetails.getStatus().getCode();
				isCancellable = mplOrderFacade.checkCancelStatus(orderConsignmentStatus,
						MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS);
			}
			else if (null != subOrderEntry.getConsignment() && null != subOrderEntry.getConsignment().getStatus())
			{

				orderConsignmentStatus = subOrderEntry.getConsignment().getStatus().getCode();
				isCancellable = mplOrderFacade.checkCancelStatus(orderConsignmentStatus,
						MarketplacecommerceservicesConstants.CANCEL_STATUS);
			}

			String message = MarketplacecommerceservicesConstants.EMPTY;
			message = MarketplacecommerceservicesConstants.BLANK_SPACE + MarketplacecommerceservicesConstants.LEFT_PARENTHESIS
					+ MarketplacecommerceservicesConstants.LAST_CONSIGNMENT_STATUS + orderConsignmentStatus
					+ MarketplacecommerceservicesConstants.RIGHT_PARENTHESIS;

			// Call cancelReturnFacade to implement cancellation process and to create CRM ticket
			if (ticketTypeCode.equalsIgnoreCase(MarketplacecommerceservicesConstants.TICKETTYPECODE_CANCEL)
					&& null != orderConsignmentStatus && isCancellable)
			{
				if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.initiate_cancel_job_cancellation_flag))
						&& (configurationService.getConfiguration().getString(
								MarketplacecommerceservicesConstants.initiate_cancel_job_cancellation_flag).equals("true")))
				{
					cancellationStatus = Boolean.parseBoolean(configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.initiate_cancel_job_cancellation_flag));
				}
				else
				{
					cancellationStatus = cancelReturnFacade.implementCancelOrReturn(subOrderDetails, subOrderEntry, reasonCode, ussid,
							ticketTypeCode, customerData, refundType, false, SalesApplication.WEB);
				}

				if (cancellationStatus)
				{
					bulkCancelModel.setLoadStatus(MarketplacecommerceservicesConstants.SUCCESS_LOAD_STATUS);
					bulkCancelModel.setStatusDescription(MarketplacecommerceservicesConstants.BULK_CANCEL_SUCCESS_DESC + message);

				}
				else
				{
					bulkCancelModel.setLoadStatus(MarketplacecommerceservicesConstants.FAILURE_LOAD_STATUS);
					bulkCancelModel.setStatusDescription(MarketplacecommerceservicesConstants.BULK_CANCEL_FAILURE_DESC + message);
				}
				finalModelToSaveList.add(bulkCancelModel);
			}
			else
			{
				bulkCancelModel.setLoadStatus(MarketplacecommerceservicesConstants.FAILURE_LOAD_STATUS);
				bulkCancelModel.setStatusDescription(MarketplacecommerceservicesConstants.BULK_CANCEL_FAILURE_DESC + message);
				finalModelToSaveList.add(bulkCancelModel);
			}
		}
		return finalModelToSaveList;
	}

	/**
	 * @param orderModel
	 * @return OrderData
	 */
	protected OrderData convertToData(final OrderModel orderModel)
	{
		OrderData orderData = null;
		CustomerData customerData = new CustomerData();
		CustomerModel customer = null;
		PriceData convenienceCharge = null;
		PriceData totalPriceWithConvenienceCharge = null;
		final List<OrderData> sellerOrderList = new ArrayList<OrderData>();
		try
		{
			final PriceData deliveryCost = createPrice(orderModel, orderModel.getDeliveryCost());
			if (orderModel.getConvenienceCharges() != null)
			{
				convenienceCharge = createPrice(orderModel, orderModel.getConvenienceCharges());
			}
			if (orderModel.getTotalPriceWithConv() != null)
			{
				totalPriceWithConvenienceCharge = createPrice(orderModel, orderModel.getTotalPriceWithConv());
			}
			//skip the order if product is missing in the order entries
			for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
			{
				if (null == orderEntry.getProduct()) // it means somehow product is deleted from the order entry.
				{
					return null;
				}
			}
			orderData = getOrderConverter().convert(orderModel);
			orderData.setDeliveryCost(deliveryCost);
			if (convenienceCharge != null)
			{
				orderData.setConvenienceChargeForCOD(convenienceCharge);
			}
			if (totalPriceWithConvenienceCharge != null)
			{
				orderData.setTotalPriceWithConvCharge(totalPriceWithConvenienceCharge);
			}
			if (orderModel.getUser() instanceof CustomerModel)
			{
				customer = (CustomerModel) orderModel.getUser();
			}
			if (customer != null && null != customer.getDefaultShipmentAddress())
			{
				customerData = new CustomerData();
				//TISUAT-4850
				if (customer.getOriginalUid() != null)
				{
					customerData.setEmail(customer.getOriginalUid());
				}
				else
				{
					customerData.setEmail(MarketplacecommerceservicesConstants.NA);
				}
				customerData.setRegistrationDate(customer.getCreationtime());
				orderData.setCustomerData(customerData);
			}

			for (final OrderModel sellerOrder : orderModel.getChildOrders())
			{
				final PriceData childDeliveryCost = createPrice(sellerOrder, sellerOrder.getDeliveryCost());
				final OrderData sellerOrderData = getOrderConverter().convert(sellerOrder);
				orderData.setDeliveryCost(childDeliveryCost);
				if (convenienceCharge != null)
				{
					sellerOrderData.setConvenienceChargeForCOD(convenienceCharge);
				}
				if (totalPriceWithConvenienceCharge != null)
				{
					sellerOrderData.setTotalPriceWithConvCharge(totalPriceWithConvenienceCharge);
				}
				if (orderModel.getUser() instanceof CustomerModel)
				{
					customer = (CustomerModel) orderModel.getUser();
				}
				if (customer != null)
				{
					customerData = new CustomerData();
					//TISUAT-4850
					if (customer.getOriginalUid() != null)
					{
						customerData.setEmail(customer.getOriginalUid());
					}
					else
					{
						customerData.setEmail(MarketplacecommerceservicesConstants.NA);
					}
					customerData.setRegistrationDate(customer.getCreationtime());
					sellerOrderData.setCustomerData(customerData);
				}
				sellerOrderList.add(sellerOrderData);
			}
			orderData.setSellerOrderList(sellerOrderList);

		}
		catch (final Exception e)
		{
			return orderData;
		}

		return orderData;

	}

	private PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException(MarketplacecommerceservicesConstants.ORDER_ERROR);
		}

		final CurrencyModel currency = source.getCurrency();

		if (currency == null)
		{
			throw new IllegalArgumentException(MarketplacecommerceservicesConstants.ORDER_CURRENCY_ERROR);
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	@Override
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
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
	 * @return the orderConverter
	 */
	public Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}


	/**
	 * @param orderConverter
	 *           the orderConverter to set
	 */
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}


	/**
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}


	/**
	 * @param priceDataFactory
	 *           the priceDataFactory to set
	 */
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}
}