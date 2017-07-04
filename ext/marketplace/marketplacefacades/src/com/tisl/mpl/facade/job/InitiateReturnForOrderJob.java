package com.tisl.mpl.facade.job;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.BulkReturnProcessModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
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
//import com.tisl.mpl.facade.checkout.MplCheckoutFacade;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
import com.tisl.mpl.facades.data.BulkReturnStatusData;
//import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.facades.data.ReturnItemAddressData;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class InitiateReturnForOrderJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(InitiateReturnForOrderJob.class.getName());
	@Autowired
	private CancelReturnFacade cancelReturnFacade;

	@Autowired
	private OrderModelService orderModelService;


	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Autowired
	private BaseStoreService baseStoreService;

	private Converter<OrderModel, OrderData> orderConverter;
	@Autowired
	private PriceDataFactory priceDataFactory;





	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		try
		{
			String orderCode = null; // TODO to fetch from given CSV66
			String transactionId = null; // TODO to fetch from given CSV66
			OrderData subOrderDetails = null;
			Map<String, BulkReturnStatusData> returnResonseMap = null;
			OrderEntryData subOrderEntry = null;

			final Map<OrderEntryData, OrderData> dataToCallOMS = new HashMap<OrderEntryData, OrderData>();

			//			final long startTime = System.currentTimeMillis();
			//			LOG.info(MarketplacecommerceservicesConstants.START_TIME + startTime);

			//fetch list of bulk data processed
			final List<BulkReturnProcessModel> bulkList = orderModelService.getBulkReturnData();

			if (CollectionUtils.isNotEmpty(bulkList))
			{
				for (final BulkReturnProcessModel bulkModel : bulkList)
				{
					if (null != bulkModel && null != bulkModel.getParentOrderNo() && null != bulkModel.getTransactionId())
					{
						orderCode = bulkModel.getParentOrderNo();
						transactionId = bulkModel.getTransactionId();

						LOG.info("orderCode is" + bulkModel.getParentOrderNo() + "&&" + "transactionId is"
								+ bulkModel.getTransactionId());

						final OrderModel orderModel = orderModelService.getParentOrder(orderCode);

						//Step 1. prepare AddressData
						//returnAddrData = getReturnData(addrModel);

						//Step 2: prepare map to call OMS
						for (final OrderModel subOrder : orderModel.getChildOrders())
						{
							try
							{
								subOrderDetails = convertToData(subOrder);
								final List<OrderEntryData> subOrderEntries = subOrderDetails.getEntries();

								for (final OrderEntryData entry : subOrderEntries)
								{
									if (entry.getTransactionId().equalsIgnoreCase(transactionId))
									{
										subOrderEntry = entry;
										dataToCallOMS.put(entry, subOrderDetails);
									}
								}
								if (null != subOrderEntry)
								{
									break;
								}
							}
							catch (final Exception e)
							{
								LOG.info("-----------Order Data Conversion Exception and skipping----" + orderModel.getCode());
								continue;
							}
						}
					}
				}
			}
			try
			{
				//Step 3: call OMS and CRM
				returnResonseMap = callOMSandCRM(dataToCallOMS);
			}
			catch (final Exception e)
			{
				LOG.error("", e);
			}

			//Step 4 : saving the modified bulkmodel list
			saveBulkData(returnResonseMap, bulkList);

			//			final long endTime = System.currentTimeMillis();
			//			LOG.info(MarketplacecommerceservicesConstants.END_TIME + endTime);

			//			LOG.info("Total time taken : " + (endTime - startTime) / 1000);
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
			LOG.error("", e);

		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	//prepare AddressData INC144315946
	private ReturnItemAddressData getReturnDataforOrder(final String code)
	{
		final ReturnItemAddressData returnAddrData = new ReturnItemAddressData();
		final OrderModel orderModel = orderModelService.getOrder(code);
		AddressModel addrModel = null;
		if (null != orderModel && null != orderModel.getDeliveryAddress())
		{
			addrModel = orderModel.getDeliveryAddress();
			returnAddrData.setPincode(addrModel.getPostalcode());
			returnAddrData.setFirstName(addrModel.getFirstname());
			returnAddrData.setLastName(addrModel.getLastname());
			returnAddrData.setAddressLane1(addrModel.getLine1());
			returnAddrData.setAddressLane2(addrModel.getLine2());
			returnAddrData.setLandmark(addrModel.getAddressLine3());
			returnAddrData.setCity(addrModel.getTown());
			returnAddrData.setMobileNo(addrModel.getPhone1());
			returnAddrData.setState(addrModel.getDistrict());
			if (null != addrModel.getCountry())
			{
				returnAddrData.setCountry(addrModel.getCountry().getIsocode());
			}
		}
		return returnAddrData;
	}

	//prepare AddressData INC144315946
	private ReturnItemAddressData getReturnData(final AddressData addrData)
	{
		final ReturnItemAddressData returnAddrData = new ReturnItemAddressData();
		returnAddrData.setPincode(addrData.getPostalCode());
		returnAddrData.setFirstName(addrData.getFirstName());
		returnAddrData.setLastName(addrData.getLastName());
		returnAddrData.setAddressLane1(addrData.getLine1());
		returnAddrData.setAddressLane2(addrData.getLine2());
		returnAddrData.setLandmark(addrData.getLine3());
		returnAddrData.setCity(addrData.getTown());
		returnAddrData.setMobileNo(addrData.getPhone());
		returnAddrData.setState(addrData.getState());
		if (null != addrData.getCountry())
		{
			returnAddrData.setCountry(addrData.getCountry().getIsocode());
		}
		return returnAddrData;
	}

	//call OMS
	private Map<String, BulkReturnStatusData> callOMSandCRM(final Map<OrderEntryData, OrderData> dataToCallOMS)
	{
		final String ticketTypeCode = MarketplacecommerceservicesConstants.TICKETTYPECODE;
		final String refundType = MarketplacecommerceservicesConstants.REFUNDTYPE;
		final String reasonCode = MarketplacecommerceservicesConstants.REASONCODE;
		final Map<String, BulkReturnStatusData> returnResonseMap = new HashMap<String, BulkReturnStatusData>();
		ReturnItemAddressData returnAddrData = null;

		for (final Map.Entry<OrderEntryData, OrderData> entry : dataToCallOMS.entrySet())
		{
			final OrderData subOrderDetails = entry.getValue();
			final OrderEntryData subOrderEntry = entry.getKey();
			boolean returnStatus = false;
			String orderStatus = null;
			final CustomerData customerData = subOrderDetails.getCustomerData();
			final String ussid = subOrderEntry.getSelectedUssid();
			String orderConsignmentStatus = MarketplacecommerceservicesConstants.EMPTY;

			//INC144315946
			if (null != subOrderDetails.getDeliveryAddress())
			{
				returnAddrData = getReturnData(subOrderDetails.getDeliveryAddress());
			}
			else
			{
				returnAddrData = getReturnDataforOrder(subOrderDetails.getCode());
			}

			if (subOrderEntry.getQuantity().intValue() != 0 && null != subOrderEntry.getConsignment()
					&& null != subOrderEntry.getConsignment().getStatus())
			{
				orderStatus = subOrderEntry.getConsignment().getStatus().getCode();
				orderConsignmentStatus = orderStatus;
			}

			if (ticketTypeCode.equalsIgnoreCase("R") && null != orderStatus
					&& orderStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED))
			{
				LOG.info(subOrderEntry.getTransactionId());
				returnStatus = cancelReturnFacade.implementReturnItem(subOrderDetails, subOrderEntry, reasonCode, ussid,
						ticketTypeCode, customerData, refundType, true, SalesApplication.WEB, returnAddrData, "");
			}
			else
			{
				LOG.info("-----------Unable to initiate return for TransactionId----- " + subOrderEntry.getTransactionId()
						+ "==========" + "Order Status is  " + orderStatus);
			}
			final BulkReturnStatusData bulkReturnStatusData = new BulkReturnStatusData();
			bulkReturnStatusData.setConsignmentStatus(orderConsignmentStatus);
			bulkReturnStatusData.setReturnStatus(Boolean.valueOf(returnStatus));
			returnResonseMap.put(subOrderEntry.getTransactionId(), bulkReturnStatusData);
		}

		return returnResonseMap;
	}

	private BulkReturnProcessModel getBulkModel(final List<BulkReturnProcessModel> bulkList, final String transactionId)
	{
		BulkReturnProcessModel bulkModel = null;
		for (final BulkReturnProcessModel model : bulkList)
		{
			if (transactionId.equalsIgnoreCase(model.getTransactionId()))
			{
				bulkModel = model;
				break;
			}
		}

		return bulkModel;
	}

	//save data
	private void saveBulkData(final Map<String, BulkReturnStatusData> returnResonseMap, final List<BulkReturnProcessModel> bulkList)
	{
		final List<BulkReturnProcessModel> updatedBulkReturnProcessList = new ArrayList<BulkReturnProcessModel>();
		if (null != returnResonseMap && !returnResonseMap.isEmpty())
		{
			for (final Map.Entry<String, BulkReturnStatusData> entry : returnResonseMap.entrySet())
			{
				final BulkReturnProcessModel returnBulkModel = getBulkModel(bulkList, entry.getKey());
				final BulkReturnStatusData bulkReturnStatusData = entry.getValue();
				final Boolean returnStatus = bulkReturnStatusData.getReturnStatus();
				String consignment = null;
				if (StringUtils.isNotEmpty(bulkReturnStatusData.getConsignmentStatus()))
				{
					consignment = bulkReturnStatusData.getConsignmentStatus();
				}
				else
				{
					consignment = MarketplacecommerceservicesConstants.EMPTY;
				}
				final String message = MarketplacecommerceservicesConstants.BLANK_SPACE
						+ MarketplacecommerceservicesConstants.LEFT_PARENTHESIS
						+ MarketplacecommerceservicesConstants.LAST_CONSIGNMENT_STATUS + consignment
						+ MarketplacecommerceservicesConstants.RIGHT_PARENTHESIS;
				if (null != returnBulkModel && null != returnStatus)
				{
					if (returnStatus.booleanValue())
					{
						returnBulkModel.setLoadStatus("1");
						returnBulkModel.setErrorDescription(MarketplacecommerceservicesConstants.BULK_RETURN_SUCCESS_DESC + message);
					}
					else
					{
						returnBulkModel.setLoadStatus("-1");
						returnBulkModel.setErrorDescription(MarketplacecommerceservicesConstants.BULK_RETURN_FAILURE_DESC + message);
					}
					updatedBulkReturnProcessList.add(returnBulkModel);
				}
			}
			if (CollectionUtils.isNotEmpty(updatedBulkReturnProcessList))
			{
				modelService.saveAll(updatedBulkReturnProcessList);
			}
		}
	}

	/*
	 * This method is used to convert the Order Model into Order Data
	 *
	 * @param orderModel
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
			LOG.info("-----------Order Model to Order Data" + orderModel.getCode());
			final PriceData deliveryCost = createPrice(orderModel, orderModel.getDeliveryCost());
			if (orderModel.getConvenienceCharges() != null)
			{
				convenienceCharge = createPrice(orderModel, orderModel.getConvenienceCharges());
				LOG.info("-----------Order Data ConvenienceCharges" + orderModel.getCode());
			}
			if (orderModel.getTotalPriceWithConv() != null)
			{
				totalPriceWithConvenienceCharge = createPrice(orderModel, orderModel.getTotalPriceWithConv());
				LOG.info("-----------Order Data total Price with Conv" + orderModel.getCode());
			}
			//skip the order if product is missing in the order entries
			for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
			{
				if (null == orderEntry.getProduct()) // it means somehow product is deleted from the order entry.
				{
					LOG.info("************************Skipping order history for order :" + orderModel.getCode() + " and for user: "
							+ orderModel.getUser().getName() + " **************************");
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
			LOG.info("-----------Order Model to Order Data Customer" + orderModel.getCode());
			if (orderModel.getUser() instanceof CustomerModel)
			{
				customer = (CustomerModel) orderModel.getUser();
			}
			//INC144315982
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
				//INC144315982
				if (customer.getUid() != null)
				{
					customerData.setUid(customer.getUid());
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
					//INC144315982
					if (customer.getUid() != null)
					{
						customerData.setUid(customer.getUid());
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
			LOG.info("-----------Order Model to Order Data Exception" + orderModel.getCode());
			//ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return orderData;
		}
		//TODO
		//for (final OrderEntryData entry : orderData.getEntries())
		//{
		//final String productCode = entry.getProduct().getCode();
		//final ProductData product = productFacade.getProductForCodeAndOptions(productCode,
		//	Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.CATEGORIES));
		//entry.setProduct(product);
		//}

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
