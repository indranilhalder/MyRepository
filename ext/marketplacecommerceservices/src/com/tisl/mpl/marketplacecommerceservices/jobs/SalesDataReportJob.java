/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.data.MplPaymentInfoData;
import com.tisl.mpl.data.SalesReportData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.MarketplaceDeliveryModeData;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.service.MplOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.MplPaymentService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.SalesReportCreationJobModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class SalesDataReportJob extends AbstractJobPerformable<SalesReportCreationJobModel>
{

	@Autowired
	private OrderModelService orderModelService;
	@Autowired
	private ConfigurationService configurationService;
	//@Autowired
	//private ProductService productService;
	@Resource
	private MplPaymentService mplPaymentService;
	@Autowired
	private MplOrderService mplOrderService;
	@Autowired
	private CronJobService cronJobService;
	@Autowired
	private DefaultPromotionManager defaultPromotionManager;
	/*
	 * @Autowired private MplCheckoutFacade mplCheckoutFacade;
	 */
	@Autowired
	private MplSellerInformationService mplSellerInformationService;

	private Converter<OrderModel, OrderData> orderConverter;
	private PriceDataFactory priceDataFactory;

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SalesDataReportJob.class.getName());

	/**
	 * @description: CronJob to generate SalesData Report
	 * @param: reportModel
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final SalesReportCreationJobModel reportModel)
	{
		//LOG.info("Starting sales report generation");
		List<OrderModel> orderModels = null;
		Date lastRunTime = null;
		try
		{
			//getting all the Orders details based on type
			if (getSalesType().equalsIgnoreCase(MarketplacecommerceservicesConstants.SALES_REPORT_INCREMENTAL))
			{
				//fetch cronjob last run time from where it will fetch the next order list
				lastRunTime = cronJobService.getCronJob(reportModel.getCode()).getModifiedtime();
				orderModels = orderModelService.getOrderList(lastRunTime);
			}
			else
			{
				orderModels = orderModelService.getOrderList(reportModel.getStartDate(), reportModel.getEndDate());
			}

			if (orderModels.size() <= 0)
			{
				throw new UnknownIdentifierException(MarketplacecommerceservicesConstants.ORDER_ERROR);
			}
			else
			{
				///Convert order data and write into CSV
				writeItemsToCSV(convertForSales(orderModels));
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
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
		String name = MarketplacecommerceservicesConstants.EMPTY;
		PriceData convenienceCharge = null;
		PriceData totalPriceWithConvenienceCharge = null;
		try
		{
			LOG.debug("-----------Order Model to Order Data" + orderModel.getCode());
			final PriceData deliveryCost = createPrice(orderModel, orderModel.getDeliveryCost());
			if (orderModel.getConvenienceCharges() != null)
			{
				convenienceCharge = createPrice(orderModel, orderModel.getConvenienceCharges());
				LOG.debug("-----------Order Data Conv" + orderModel.getCode());
			}
			if (orderModel.getTotalPriceWithConv() != null)
			{
				totalPriceWithConvenienceCharge = createPrice(orderModel, orderModel.getTotalPriceWithConv());
				LOG.debug("-----------Order Data total Price with Conv" + orderModel.getCode());
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
			LOG.debug("-----------Order Model to Order Data Customer" + orderModel.getCode());
			if (orderModel.getUser() instanceof CustomerModel)
			{
				customer = (CustomerModel) orderModel.getUser();
			}
			name = MarketplacecommerceservicesConstants.EMPTY;
			if (customer != null && null != customer.getDefaultShipmentAddress())
			{
				customerData = new CustomerData();
				if (customer.getDefaultShipmentAddress().getFirstname() != null)
				{
					name += customer.getDefaultShipmentAddress().getFirstname() + MarketplacecommerceservicesConstants.SPACE;
					customerData.setFirstName(customer.getDefaultShipmentAddress().getFirstname());
				}
				if (customer.getDefaultShipmentAddress().getLastname() != null)
				{
					name += customer.getDefaultShipmentAddress().getLastname();
					customerData.setLastName(customer.getDefaultShipmentAddress().getLastname());
				}
				customerData.setName(name);
				if (customer.getDefaultShipmentAddress().getCellphone() != null)
				{
					customerData.setContactNumber(customer.getDefaultShipmentAddress().getCellphone());
				}
				else
				{
					customerData.setContactNumber(MarketplacecommerceservicesConstants.NA);
				}
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
			final List<OrderData> sellerOrderList = new ArrayList<OrderData>();
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
				name = MarketplacecommerceservicesConstants.EMPTY;
				if (customer != null)
				{
					customerData = new CustomerData();
					if (null != customer.getDefaultShipmentAddress() && customer.getDefaultShipmentAddress().getFirstname() != null)
					{
						name += customer.getDefaultShipmentAddress().getFirstname() + MarketplacecommerceservicesConstants.SPACE;
						customerData.setFirstName(customer.getDefaultShipmentAddress().getFirstname());
					}
					else
					{
						customerData.setFirstName(MarketplacecommerceservicesConstants.NA);
					}
					if (null != customer.getDefaultShipmentAddress() && customer.getDefaultShipmentAddress().getLastname() != null)
					{
						name += customer.getDefaultShipmentAddress().getLastname();
						customerData.setLastName(customer.getDefaultShipmentAddress().getLastname());
					}
					else
					{
						customerData.setLastName(MarketplacecommerceservicesConstants.NA);
					}
					customerData.setName(name);

					if (null != customer.getDefaultShipmentAddress() && customer.getDefaultShipmentAddress().getCellphone() != null)
					{
						customerData.setContactNumber(customer.getDefaultShipmentAddress().getCellphone());
					}
					else
					{
						customerData.setContactNumber(MarketplacecommerceservicesConstants.NA);
					}
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
		catch (

		final Exception e)

		{
			LOG.debug("-----------Order Model to Order Data" + orderModel.getCode());
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
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

	/**
	 * @description: It is creating price data for a price value
	 * @param source
	 * @param val
	 *
	 * @return PriceData
	 */
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

	/*
	 * This method is used to get CSV headers from local.properties
	 */
	protected String getCSVHeaderLine()
	{
		return configurationService.getConfiguration().getString("cronjob.salesreport.header", "");
	}

	/*
	 * This method is used to get sales report type from local.properties
	 */
	protected String getSalesType()
	{
		return configurationService.getConfiguration().getString("cronjob.salesreport.type", "");
	}

	/*
	 * This method is used to get output file from local.properties
	 */
	protected String getOutputFilePath()
	{

		final DateFormat df = new SimpleDateFormat(MarketplacecommerceservicesConstants.DATE_FORMAT_REPORT);
		final String timestamp = df.format(new Date());
		final StringBuilder output_file_path = new StringBuilder();

		if (getSalesType().equalsIgnoreCase(MarketplacecommerceservicesConstants.SALES_REPORT_INCREMENTAL))
		{
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.salesreport.report.path", ""));
			output_file_path.append(File.separator);
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.salesreport.prefix", ""));
			output_file_path.append(MarketplacecommerceservicesConstants.FILE_PATH);
			output_file_path.append(timestamp);
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.salesreport.extension", ""));
		}
		else
		{
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.salesreport.report.path", ""));
			output_file_path.append(File.separator);
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.salesreport.prefix", ""));
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.salesreport.extension", ""));
		}
		return output_file_path.toString();
	}

	/**
	 * This method takes the list of Orders and sets in the POJO class which is in turn set in the CSV file to be
	 * generated in a specified location
	 *
	 * @param orderModels
	 */
	protected List<SalesReportData> convertForSales(final List<OrderModel> orderModels)
	{
		ProductData product = null;
		SalesReportData data = null;
		final List<SalesReportData> dataList = new ArrayList<SalesReportData>();
		MarketplaceDeliveryModeData mplDeliveryMode = null;
		String consignmentStatus = MarketplacecommerceservicesConstants.NA;
		List<String> categoryList = new ArrayList<String>();
		OrderData orderDetail = null;
		//final Date today = new Date();
		//"Orderno,OrderDate,Orderstatus,ListingId,USSID,SellerSKUId,CustomerName,TransactionRef,TransactinRefId,SellerName,Brand,productPrice,Quantity,Itemcategory,
		//Itemsubcategory,PaymentMethod,BankName,Tenure(only if eMI),Shippingcity,zipcode,ShippingState,address,phnumber,producttype,CustomerRegisterDate,TransactionRefNumberasgateway,
		//Ipadddress,Totalprice,Email,Riskscore,MrpPrice,Mopprice,deliverytype";
		try
		{
			//iterating through the list and adding the rows in the list of the pojo class
			for (final OrderModel orderModel : orderModels)
			{
				try
				{
					//orderDetail = mplCheckoutFacade.getOrderDetailsForCode(orderModel.getCode());
					orderDetail = convertToData(orderModel);
				}
				catch (final Exception e)
				{
					LOG.debug("-----------Order Conversion Exception----");
					ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
					continue;
				}
				if (orderDetail != null)
				{
					final List<OrderData> subOrder = orderDetail.getSellerOrderList();
					for (final OrderData subOrderDetail : subOrder)
					{

						for (final OrderEntryData entry : subOrderDetail.getEntries())
						{
							data = new SalesReportData();
							data.setOrderNo(orderDetail.getCode());
							data.setOrderDate(orderDetail.getCreated().toString());
							data.setTransactionRefId(entry.getTransactionId());
							LOG.debug("-----------Fetching consignment status----" + entry.getTransactionId());
							if (null != entry.getConsignment() && null != entry.getConsignment().getStatus())
							{

								consignmentStatus = entry.getConsignment().getStatus().getCode();
							}
							else if (subOrderDetail.getStatus().getCode() != null)
							{
								consignmentStatus = subOrderDetail.getStatus().getCode();
							}
							data.setOrderStatus(consignmentStatus);

							LOG.debug("-----------Fetching Customer----" + subOrderDetail.getCode());
							if (subOrderDetail.getCustomerData() != null)
							{
								data.setCustomerName(subOrderDetail.getCustomerData().getName());
								data.setCustomerRegisterDate(subOrderDetail.getCustomerData().getRegistrationDate().toString());
								data.setEmail(subOrderDetail.getCustomerData().getEmail());
								data.setPhNumber(subOrderDetail.getCustomerData().getContactNumber());

							}
							//TODO IPaddress track by Order / Customer
							data.setIpAdddress(configurationService.getConfiguration().getString(
									MarketplacecommerceservicesConstants.SALES_DATA_REPORT_JOB_IP, ""));

							//check delivery address and set
							LOG.debug("-----------Order Delivery Address----" + entry.getProduct().getCode());
							setDeliveryAddress(subOrderDetail, data);

							LOG.debug("-----------Fetching Order product----" + entry.getProduct().getCode());
							product = entry.getProduct();
							if (product != null)
							{
								if (product.getRootCategory() != null)
								{
									data.setItemCategory(product.getRootCategory());
								}
								if (product.getBrand() != null)
								{
									data.setBrand(product.getBrand().getBrandname());
								}
								if (product.getListingId() != null)
								{
									data.setListingId(product.getListingId());
								}
							}

							final ProductModel productModel = mplOrderService.findProductsByCode(entry.getProduct().getCode());
							String fulfillmentType = MarketplacecommerceservicesConstants.NA;
							String primaryCategory = MarketplacecommerceservicesConstants.NA;
							String secondaryCategory = MarketplacecommerceservicesConstants.NA;
							if (productModel != null)
							{
								LOG.debug("---------Product Type---------");
								if (null != productModel.getSellerInformationRelator())
								{
									final Collection<SellerInformationModel> sellerinfolist = productModel.getSellerInformationRelator();
									if (sellerinfolist.size() > 0)
									{
										for (final SellerInformationModel sellerEntry : sellerinfolist)
										{
											if (sellerEntry.getRichAttribute().size() > 0)
											{
												for (final RichAttributeModel richEntry : sellerEntry.getRichAttribute())
												{
													LOG.debug("-----Inside seller rich attribute model-----"
															+ richEntry.getDeliveryFulfillModes());
													fulfillmentType = richEntry.getDeliveryFulfillModes().toString().toUpperCase();
													LOG.debug("-----Fulfilment mode set------");
												}
											}
										}
									}
								}
								data.setProductType(fulfillmentType);
								LOG.debug("----Product Category---");
								final List<CategoryModel> productCategoryList = defaultPromotionManager
										.getPrimarycategoryData(productModel);
								if (null != productCategoryList && productCategoryList.size() > 0)
								{
									categoryList = new ArrayList<String>();
									for (final CategoryModel category : productCategoryList)
									{
										if (category != null && !(category instanceof ClassificationClassModel)
												&& null != category.getName())
										{
											categoryList.add(category.getName());
										}
									}
									if (categoryList.size() > 2)
									{
										primaryCategory = categoryList.get(categoryList.size() - 2);
										secondaryCategory = categoryList.get(categoryList.size() - 1);
										LOG.debug("---Product Category list:" + categoryList.size());
									}

								}
								data.setItemCategory(primaryCategory);
								data.setItemSubCategory(secondaryCategory);
								LOG.debug("--Product Category set----");
							}

							data.setQuantity(entry.getQuantity().toString());
							//Checking payment type and then setting payment info
							//TISUAT-4850 moved prices in this method
							setPaymentInfo(subOrderDetail, orderModel, data, entry);
							if (entry.getMplDeliveryMode() != null)
							{
								mplDeliveryMode = entry.getMplDeliveryMode();
								data.setDeliveryType(mplDeliveryMode.getName());
							}
							else
							{
								data.setDeliveryType(MarketplacecommerceservicesConstants.NA);
							}

							LOG.debug("-----------Order Entry seller" + entry.getTransactionId());
							setSellerInfo(entry, data);

							dataList.add(data);
						}
					}
					LOG.debug("-----------Order Data conversion finished----" + orderDetail.getCode());
				}
			}
		}
		catch (final Exception e)
		{
			LOG.debug("-----------Order Conversion Exception----" + orderModels.size());
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			return dataList;
		}
		return dataList;
	}

	/* Setting Seller Information */
	protected void setSellerInfo(final OrderEntryData product, final SalesReportData reportDTO)
	{
		//Freebie and non-freebie seller detail population
		SellerInformationModel sellerInfoModel = null;
		if (StringUtils.isNotEmpty(product.getSelectedUssid()))
		{
			sellerInfoModel = mplSellerInformationService.getSellerDetail(product.getSelectedUssid());

			if (null != sellerInfoModel && StringUtils.isNotEmpty(sellerInfoModel.getSellerID()))
			{
				reportDTO.setSellerSKUId(sellerInfoModel.getSellerID());
			}
			if (null != sellerInfoModel && StringUtils.isNotEmpty(sellerInfoModel.getUSSID()))
			{
				reportDTO.setUSSID(sellerInfoModel.getUSSID());
			}
			if (null != sellerInfoModel && StringUtils.isNotEmpty(sellerInfoModel.getSellerName()))
			{
				reportDTO.setSellerName(sellerInfoModel.getSellerName());
			}
		}
	}

	/* Setting DeliveryAddress */
	protected void setDeliveryAddress(final OrderData orderDetail, final SalesReportData reportDTO)
	{
		String address = MarketplacecommerceservicesConstants.QUOTE;
		if (null != orderDetail.getDeliveryAddress().getId() && StringUtils.isNotEmpty(orderDetail.getDeliveryAddress().getId()))
		{
			//address += orderDetail.getDeliveryAddress().getFirstName() + " ";
			//address += orderDetail.getDeliveryAddress().getLastName() + " ";
			reportDTO.setShippingzipcode(orderDetail.getDeliveryAddress().getPostalCode());
			reportDTO.setShippingState(orderDetail.getDeliveryAddress().getState());
			reportDTO.setShippingCity(orderDetail.getDeliveryAddress().getTown());
			address += orderDetail.getDeliveryAddress().getFormattedAddress() + MarketplacecommerceservicesConstants.QUOTE;
			reportDTO.setShippingAddress(address);
		}
	}

	/* Checking payment type and then setting payment info */
	protected void setPaymentInfo(final OrderData orderDetail, final OrderModel orderModel, final SalesReportData reportDTO,
			final OrderEntryData entry)
	{
		MplPaymentInfoData paymentInfo = null;
		String auditId = MarketplacecommerceservicesConstants.NA;
		String riskScore = MarketplacecommerceservicesConstants.NA;
		String transactionRef = MarketplacecommerceservicesConstants.NA;
		String transactionRefGateway = MarketplacecommerceservicesConstants.NA;
		double totalPrice = 0d;
		String mrp = MarketplacecommerceservicesConstants.NA;
		String mop = MarketplacecommerceservicesConstants.NA;
		String productPrice = MarketplacecommerceservicesConstants.NA;
		try
		{
			if (orderDetail.getMplPaymentInfo() != null)
			{
				paymentInfo = orderDetail.getMplPaymentInfo();
				reportDTO.setPaymentMethod(paymentInfo.getPaymentOption());

				if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.CREDIT))
				{
					reportDTO.setBankName(paymentInfo.getCardCardType());
					reportDTO.setTenure(MarketplacecommerceservicesConstants.NA);
				}
				else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.EMI))
				{
					reportDTO.setBankName(paymentInfo.getCardCardType());
					if (paymentInfo.getEmiInfo() != null)
					{
						reportDTO.setTenure(paymentInfo.getEmiInfo().getTerm());
					}
				}
				else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.NETBANKING))
				{
					reportDTO.setBankName(paymentInfo.getBank());
					reportDTO.setTenure(MarketplacecommerceservicesConstants.NA);
				}
				else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.DEBIT))
				{
					reportDTO.setBankName(paymentInfo.getCardCardType());
					reportDTO.setTenure(MarketplacecommerceservicesConstants.NA);
				}
				else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.COD))
				{
					reportDTO.setBankName(MarketplacecommerceservicesConstants.NA);
					reportDTO.setTenure(MarketplacecommerceservicesConstants.NA);
				}

				else if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.WALLET))
				{
					reportDTO.setBankName(MarketplacecommerceservicesConstants.NA);
					reportDTO.setTenure(MarketplacecommerceservicesConstants.NA);
				}
				//TODO
				//Find the correct juspay orderid(auditid) model for sucess //AT
				for (final PaymentTransactionModel paymentTransaction : orderModel.getPaymentTransactions())
				{
					if (!auditId.equalsIgnoreCase(MarketplacecommerceservicesConstants.NA))
					{
						break;
					}
					for (final PaymentTransactionEntryModel paymentTransactionEntry : paymentTransaction.getEntries())
					{
						if (PaymentTransactionType.CAPTURE.equals(paymentTransactionEntry.getType())
								&& "success".equalsIgnoreCase(paymentTransactionEntry.getTransactionStatus()))
						{
							auditId = paymentTransactionEntry.getRequestToken();
							transactionRefGateway = auditId;
							if (null != paymentTransactionEntry.getPaymentTransaction())
							{
								transactionRef = paymentTransactionEntry.getPaymentTransaction().getCode();
							}
							break;
						}
					}
				}

				if (!auditId.equalsIgnoreCase(MarketplacecommerceservicesConstants.NA))
				{
					final JuspayEBSResponseModel jusPay = mplPaymentService.getEntryInAuditByOrder(auditId);
					if (null != jusPay && null != jusPay.getEbsRiskLevel())
					{
						riskScore = jusPay.getEbsRiskLevel().getCode();
						LOG.debug("-----------Payment Jusp Pay Risk");
					}
				}

				reportDTO.setTransactionRefNo(transactionRef);
				reportDTO.setRiskScore(riskScore);
				reportDTO.setTransactionRefNumber(transactionRefGateway);
				//TISUAT-4850
				LOG.debug("-----------Order Prices--" + orderModel.getCode());
				if (paymentInfo.getPaymentOption().equalsIgnoreCase(MarketplacecommerceservicesConstants.COD)
						&& null != orderModel.getTotalPriceWithConv())
				{
					totalPrice = orderModel.getTotalPriceWithConv() == null ? 0.0d : orderModel.getTotalPriceWithConv().doubleValue();
				}
				else
				{
					totalPrice = orderModel.getTotalPrice() == null ? 0.0d : orderModel.getTotalPrice().doubleValue();
					// Add the taxes to the total price if the cart is net; if the total was null taxes should be null as well
					if (Boolean.TRUE.equals(orderModel.getNet()) && totalPrice != 0.0d)
					{
						totalPrice += orderModel.getTotalTax() == null ? 0.0d : orderModel.getTotalTax().doubleValue();
					}
				}
				reportDTO.setTotalPrice(String.valueOf(totalPrice));

				if (entry.getBasePrice() != null)
				{
					mrp = entry.getBasePrice().getValue().toPlainString();
				}
				reportDTO.setMrpPrice(mrp);

				if (entry.getTotalSalePrice() != null)
				{
					mop = entry.getTotalSalePrice().getValue().toPlainString();
				}
				reportDTO.setMopPrice(mop);
				if (entry.getAmountAfterAllDisc() != null)
				{
					productPrice = entry.getAmountAfterAllDisc().getValue().toPlainString();
				}
				reportDTO.setProductPrice(productPrice);
			}

		}
		catch (final Exception e)
		{
			LOG.debug("-----------Order Data Payment Exception" + orderDetail.getCode());
		}

		//TODO Need to remove Hard code
		/*
		 * else { data.setPaymentMethod(orderDetail.getPaymentType().getDisplayName()); data.setRiskScore("NA");
		 * data.setTenure("NA"); data.setBankName("NA"); }
		 */
	}





	/**
	 * This method takes the list of SalesReportData and set in the CSV file to be generated in a specified location
	 */
	void writeItemsToCSV(final List<SalesReportData> dataList)
	{
		FileWriter fileWriter = null;
		String CSVHeader = "";
		try
		{
			final File rootFolder = new File(getOutputFilePath());
			rootFolder.getParentFile().mkdirs();
			fileWriter = new FileWriter(rootFolder);
			CSVHeader = getCSVHeaderLine();
			//Write the CSV file header
			fileWriter.append(CSVHeader);
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			//Write a new student object list to the CSV file
			//"OrderNo,OrderDate,TotalPrice,OrderStatus,TransactinRefId,ListingId,USSID,SellerSKUId,SellerName,
			//CustomerName,Email,Phone,CustomerRegisterDate,ShippingCity,ZipCode,ShippingState,Address,IP,Brand,ProductPrice,
			//Quantity,ItemCategory,ItemSubcategory,ProductType,MRP,MOP,DeliveryType,PaymentMethod,BankName,
			//Tenure(only if eMI),RiskScore,TransactionRef,TransactionRefNoAsGateway";

			for (final SalesReportData report : dataList)
			{
				//for(String header:headers){
				fileWriter.append(report.getOrderNo());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getOrderDate());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getTotalPrice());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getOrderStatus());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getTransactionRefId());
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(report.getListingId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getUSSID());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getSellerSKUId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getSellerName());
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(report.getCustomerName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getEmail());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getPhNumber());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getCustomerRegisterDate());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getShippingCity());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getShippingzipcode());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getShippingState());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getShippingAddress());
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(report.getIpAdddress());
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(report.getBrand());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getProductPrice());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getQuantity());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getItemCategory());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getItemSubCategory());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getProductType());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getMrpPrice());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getMopPrice());
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(report.getDeliveryType());
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(report.getPaymentMethod());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getBankName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getTenure());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getRiskScore());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getTransactionRefNo());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getTransactionRefNumber());
				//fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(NEW_LINE_SEPARATOR);

			}
		}
		catch (final Exception e)
		{
			LOG.debug(MarketplacecommerceservicesConstants.CSV_ERROR);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fileWriter.flush();
				fileWriter.close();
			}
			catch (final IOException e)
			{
				LOG.debug(MarketplacecommerceservicesConstants.FILE_WRITER_ERROR);
				e.printStackTrace();
			}
		}


	}


	/**
	 * @return the orderModelService
	 */
	public OrderModelService getOrderModelService()
	{
		return orderModelService;
	}


	/**
	 * @param orderModelService
	 *           the orderModelService to set
	 */
	public void setOrderModelService(final OrderModelService orderModelService)
	{
		this.orderModelService = orderModelService;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the cronJobService
	 */
	public CronJobService getCronJobService()
	{
		return cronJobService;
	}

	/**
	 * @param cronJobService
	 *           the cronJobService to set
	 */
	public void setCronJobService(final CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
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
