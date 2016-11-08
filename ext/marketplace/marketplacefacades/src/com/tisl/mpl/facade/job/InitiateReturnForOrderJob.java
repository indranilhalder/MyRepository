package com.tisl.mpl.facade.job;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
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
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.cancelreturn.CancelReturnFacade;
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

		//Reading CSV file

		//String fileName = System.getProperty("user.home") + "/csv_files/return.csv";

		//final String fileName = "D:/Nazia/csv_files/return.csv";
		//BufferedReader fileReader = null;


		try
		{


			String orderCode = null; // TODO to fetch from given CSV66
			String transactionId = null; // TODO to fetch from given CSV66
			String ussid = null;
			final String ticketTypeCode = MarketplacecommerceservicesConstants.TICKETTYPECODE;
			final String refundType = MarketplacecommerceservicesConstants.REFUNDTYPE;
			final String reasonCode = MarketplacecommerceservicesConstants.REASONCODE;
			String orderStatus = null;
			CustomerData customerData = null;
			OrderData subOrderDetails = null;

			boolean returnStatus = false;
			/*
			 * String line = ""; fileReader = new BufferedReader(new FileReader(fileName));
			 */

			final List<BulkReturnProcessModel> bulkList = orderModelService.getBulkReturnData();

			for (final BulkReturnProcessModel bulkModel : bulkList)
			{
				if (null != bulkModel && null != bulkModel.getParentOrderNo() && null != bulkModel.getTransactionId())
				{
					orderCode = bulkModel.getParentOrderNo();
					transactionId = bulkModel.getTransactionId();


					LOG.info("orderCode is" + bulkModel.getParentOrderNo() + "&&" + "transactionId is" + bulkModel.getTransactionId());


					/*
					 * while ((line = fileReader.readLine()) != null) {
					 * 
					 * final String[] tokens = line.split(MarketplacecommerceservicesConstants.COMMA_DELIMITER);
					 * 
					 * 
					 * orderCode = tokens[0]; transactionId = tokens[1];
					 * 
					 * System.out.println("RETURN [orderCode= " + tokens[0] + " , transactionId=" + tokens[1] + "]");
					 */



					final ReturnItemAddressData returnAddrData = new ReturnItemAddressData();
					OrderEntryData subOrderEntry = new OrderEntryData();
					final OrderModel orderModel = orderModelService.getParentOrder(orderCode);
					final AddressModel addrModel = orderModel.getDeliveryAddress();

					returnAddrData.setPincode(addrModel.getPostalcode());
					returnAddrData.setFirstName(addrModel.getFirstname());
					returnAddrData.setLastName(addrModel.getLastname());
					returnAddrData.setCity(addrModel.getCity());
					returnAddrData.setMobileNo(addrModel.getCellphone());
					returnAddrData.setState(addrModel.getState());

					for (final OrderModel subOrder : orderModel.getChildOrders())
					{
						try
						{
							subOrderDetails = convertToData(subOrder);
							customerData = subOrderDetails.getCustomerData();
						}
						catch (final Exception e)
						{
							LOG.info("-----------Order Data Conversion Exception and skipping----" + orderModel.getCode());
							continue;
						}
					}
					final List<OrderEntryData> subOrderEntries = subOrderDetails.getEntries();

					for (final OrderEntryData entry : subOrderEntries)
					{
						if (entry.getTransactionId().equalsIgnoreCase(transactionId))
						{
							subOrderEntry = entry;
							ussid = entry.getSelectedUssid();
							orderStatus = entry.getConsignment().getStatus().getCode();

							break;
						}
						// TODO To read csv for getting order ids,ussid and transcation id
					}


					if (ticketTypeCode.equalsIgnoreCase("R") && orderStatus != null
							&& orderStatus.equalsIgnoreCase(MarketplacecommerceservicesConstants.DELIVERED))
					{
						returnStatus = cancelReturnFacade.implementReturnItem(subOrderDetails, subOrderEntry, reasonCode, ussid,
								ticketTypeCode, customerData, refundType, true, SalesApplication.WEB, returnAddrData);
					}
					else
					{
						LOG.info("-----------Unable to initiate return----" + "Order Id " + orderModel.getCode() + "Order Status "
								+ orderStatus);
					}

					if (returnStatus)
					{
						bulkModel.setLoadStatus("1");
						bulkModel.setErrorDescription(MarketplacecommerceservicesConstants.SUCCESS);
					}
					else
					{
						bulkModel.setLoadStatus("-1");
						bulkModel.setErrorDescription(MarketplacecommerceservicesConstants.FAILURE);
					}
					modelService.save(bulkModel);
					LOG.info("Update Load Status with success");
				}
				else
				{
					bulkModel.setLoadStatus("-1");
					bulkModel.setErrorDescription(MarketplacecommerceservicesConstants.FAILURE);
					modelService.save(bulkModel);
					LOG.info("Update Load Status with failure");
				}
			}
			/*
			 * else { cancelReturnFacade.implementCancelOrReturn(subOrderDetails, subOrderEntry, reasonCode, ussid,
			 * ticketTypeCode, customerData, refundType, true, SalesApplication.WEB); }
			 */
			//}

			/*
			 * try { fileReader.close(); } catch (final IOException e) {
			 * System.out.println("Error while closing fileReader !!!"); e.printStackTrace(); }
			 */



			/*
			 * //Start Archiving
			 * 
			 * 
			 * InputStream inStream = null; OutputStream outStream = null;
			 * 
			 * 
			 * String dateTime = null; final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); final Calendar
			 * cal = Calendar.getInstance(); dateTime = dateFormat.format(cal.getTime());
			 * System.out.println(dateFormat.format(cal.getTime()));
			 * 
			 * 
			 * final File afile = new File("D:\\Nazia\\csv_files\\return.csv"); final File bfile = new
			 * File("D:\\Nazia\\Archive\\return_" + dateTime + ".csv");
			 * 
			 * 
			 * boolean blnCreated = false;
			 * 
			 * blnCreated = bfile.createNewFile();
			 * 
			 * System.out.println("Was file " + bfile.getPath() + " created ? : " + blnCreated);
			 * 
			 * inStream = new FileInputStream(afile); outStream = new FileOutputStream(bfile);
			 * 
			 * 
			 * 
			 * final byte[] buffer = new byte[1024];
			 * 
			 * 
			 * int length; //copy the file content in bytes while ((length = inStream.read(buffer)) > 0) {
			 * 
			 * outStream.write(buffer, 0, length);
			 * 
			 * //afile.renameTo((new File(afile.getName() + dateTime))); }
			 * 
			 * inStream.close(); outStream.close();
			 * 
			 * //delete the original file afile.delete();
			 * 
			 * System.out.println("File is copied successful!");
			 */




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
			System.out.println("Error in Processing !!!");
			e.printStackTrace();
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
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
