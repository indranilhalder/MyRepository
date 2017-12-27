/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.enums.CustomerType;
import de.hybris.platform.commerceservices.strategies.CustomerNameStrategy;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.EMIPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.NetbankingPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.order.payment.ThirdPartyWalletInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.globalcodes.utilities.MplCodeMasterUtility;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.service.FetchSalesOrderService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class OmsSubmissionPendingReportJob extends AbstractJobPerformable<CronJobModel>
{

	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "customerNameStrategy")
	private CustomerNameStrategy customerNameStrategy;

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String DATEFORMAT = "dd/MMM/yyyy hh:mm:ss".intern();
	private static final String L1 = "l1".intern();
	private static final String L2 = "l2".intern();
	private static final String L3 = "l3".intern();
	private static final String L4 = "l4".intern();

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(OmsSubmissionPendingReportJob.class.getName());

	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		LOG.info("**********************************");
		LOG.info("OmsSubmissionPendingReportJob!!!");
		LOG.info("**********************************");

		try
		{
			final MplConfigurationModel configModel = getFetchSalesOrderService().getCronDetails(cronJobModel.getCode());
			if (null != configModel && null != configModel.getMplConfigDate())
			{
				LOG.debug("CRON START DATE" + configModel.getMplConfigDate());
				populateSpecifiedData(configModel.getMplConfigDate(), cronJobModel.getStartTime());
			}
			else
			{
				LOG.debug("1st time call");
				populateOrderData();
			}

			saveCronData(cronJobModel);
		}
		catch (final EtailBusinessExceptions exception)
		{
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final Exception exception)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(exception));
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected FetchSalesOrderService getFetchSalesOrderService()
	{
		return Registry.getApplicationContext().getBean("fetchSalesOrderServiceImpl", FetchSalesOrderService.class);
	}

	/**
	 * @param cronJobModel
	 */
	private void saveCronData(final CronJobModel cronJobModel)
	{
		final String reportDuration = configurationService.getConfiguration().getString(
				"cronjob.omsSubmissionPending.report.duration", "15");
		if (null != cronJobModel && null != cronJobModel.getStartTime() && null != cronJobModel.getCode())
		{
			final Date cronStartTime = cronJobModel.getStartTime();
			final Calendar cal = Calendar.getInstance();
			cal.setTime(cronStartTime);
			cal.add(Calendar.DATE, -Integer.parseInt(reportDuration));
			final Date finalStartDate = cal.getTime();
			getFetchSalesOrderService().saveCronDetails(finalStartDate, cronJobModel.getCode());
		}
		else
		{
			LOG.error("Either cronJobModel or cronJobModel.getStartTime() or cronJobModel.getCode() is null");
		}
	}

	/**
	 * Populate order data
	 */
	private void populateOrderData()
	{
		final SearchResult<List<Object>> result = getFetchSalesOrderService().fetchSpecifiedDataForPymntScss();
		if (null != result)
		{
			LOG.debug("all orders db fetch successful in populateOrderData");
			writeItemsToCSV(result);
		}
	}

	/**
	 * @param mplConfigDate
	 * @param startTime
	 */
	private void populateSpecifiedData(final Date mplConfigDate, final Date startTime)
	{
		SearchResult<List<Object>> result = null;
		result = getFetchSalesOrderService().fetchSpecifiedDataForPymntScss(mplConfigDate, startTime);
		if (null != result)
		{
			LOG.debug("all orders db fetch successful in populateSpecifiedData");
			///Convert order data and write into CSV
			writeItemsToCSV(result);
		}
		else
		{
			writeBlankCSV();
		}
	}

	/**
	 * Generate blank csv
	 */
	void writeBlankCSV()
	{
		FileWriter fileWriter = null;
		String CSVHeader = StringUtils.EMPTY;
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
			fileWriter.append("No Records Found");//Phone Number
			fileWriter.append(COMMA_DELIMITER);
			//fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(NEW_LINE_SEPARATOR);
		}
		catch (final Exception e)
		{
			LOG.error("Error in writeBlankCSV:", e);
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.CSV_ERROR);
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
				LOG.error("Error in writeBlankCSV:", e);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.FILE_WRITER_ERROR);
			}
		}
	}

	/**
	 * This method takes the list of SalesReportData and set in the CSV file to be generated in a specified location
	 *
	 * @param result
	 */
	void writeItemsToCSV(final SearchResult<List<Object>> result)
	{
		FileWriter fileWriter = null;
		String CSVHeader = StringUtils.EMPTY;
		try
		{
			final SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMAT);

			final File rootFolder = new File(getOutputFilePath());
			rootFolder.getParentFile().mkdirs();
			fileWriter = new FileWriter(rootFolder);
			CSVHeader = getCSVHeaderLine();
			//Write the CSV file header
			fileWriter.append(CSVHeader);
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			for (final List<Object> row : result.getResult())
			{
				if (CollectionUtils.isNotEmpty(row))
				{
					final OrderModel subOrder = (OrderModel) row.get(0);
					final AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) row.get(1);
					final String orderEntryStatus = (String) row.get(2);



					final AddressModel delAddress = subOrder.getDeliveryAddress();
					final Map<String, String> map = categoryList(orderEntry);
					fileWriter.append(csvFormat(formatter.format(subOrder.getCreationtime())));//Order date
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(subOrder.getParentReference().getCode()));//ORN
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(orderEntryStatus));//Order Status
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(subOrder.getCode()));//Seller Order Id
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(orderEntry.getOrderLineId()));//Transaction Id
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(getPaymentMode(subOrder.getPaymentInfo())));//Payment Type
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(map.get(L1)));//l1
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(map.get(L2)));//l2
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(map.get(L3)));//l3
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(map.get(L4)));//l4
					fileWriter.append(COMMA_DELIMITER);
					//final String fullName = setFirstAndLastName(subOrder);
					String fullName = "";
					if (null != delAddress)
					{
						fullName = setFirstAndLastName(delAddress);
					}
					fileWriter.append(csvFormat(fullName == null ? "" : fullName));//Customer Name
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(csvFormat(((CustomerModel) subOrder.getUser()).getOriginalUid()));//Customer EmailId
					fileWriter.append(COMMA_DELIMITER);
					if (null != delAddress)
					{
						fileWriter.append(csvFormat(delAddress.getPhone1() != null ? delAddress.getPhone1() : delAddress.getPhone2()));//Phone Number
					}
					fileWriter.append(COMMA_DELIMITER);
					//fileWriter.append(COMMA_DELIMITER);
					fileWriter.append(NEW_LINE_SEPARATOR);

				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Error in writeItemsToCSV:", e);
			throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.CSV_ERROR);
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
				LOG.error("Error in writeItemsToCSV:", e);
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.FILE_WRITER_ERROR);
			}
		}


	}

	/**
	 * @param value
	 * @return String
	 */
	private String csvFormat(final String value)
	{

		String result = value;
		if (result.contains("\""))
		{
			result = result.replace("\"", "\"\"");
		}
		result = "\"" + result + "\"";
		return result;

	}

	/**
	 * @return string
	 */
	private String getOutputFilePath()
	{
		final DateFormat df = new SimpleDateFormat(MarketplacecommerceservicesConstants.DATE_FORMAT_REPORT);
		final String timestamp = df.format(new Date());
		final StringBuilder output_file_path = new StringBuilder();

		output_file_path.append(configurationService.getConfiguration().getString("cronjob.omsSubmissionPending.folder.path", ""));
		output_file_path.append(File.separator);
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.omsSubmissionPending.fileName", ""));
		output_file_path.append(MarketplacecommerceservicesConstants.FILE_PATH);
		output_file_path.append(timestamp);
		output_file_path.append(configurationService.getConfiguration().getString("cronjob.omsSubmissionPending.extension", ""));

		return output_file_path.toString();
	}

	/**
	 * @param paymentInfoModel
	 * @return paymentMode
	 */
	private String getPaymentMode(final PaymentInfoModel paymentInfoModel)
	{

		if (paymentInfoModel != null)
		{
			if (paymentInfoModel instanceof CODPaymentInfoModel)
			{
				//return MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_COD);
				return MplCodeMasterUtility.getglobalCode(MarketplacecommerceservicesConstants.COD);
			}
			else if (paymentInfoModel instanceof CreditCardPaymentInfoModel)
			{
				/*
				 * return
				 * MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_CREDIT_CARD
				 * .toUpperCase());
				 */
				return MplCodeMasterUtility.getglobalCode(MarketplacecommerceservicesConstants.CREDIT.toUpperCase());

			}
			else if (paymentInfoModel instanceof DebitCardPaymentInfoModel)
			{
				return MplCodeMasterUtility.getglobalCode(MarketplacecommerceservicesConstants.DEBIT.toUpperCase());
				/*
				 * return
				 * MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_DEBIT_CARD
				 * .toUpperCase());
				 */
			}
			else if (paymentInfoModel instanceof NetbankingPaymentInfoModel)
			{
				/*
				 * return
				 * MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_NETBANKING
				 * .toUpperCase());
				 */
				return MplCodeMasterUtility.getglobalCode(MarketplacecommerceservicesConstants.NETBANKING.toUpperCase());
			}
			else if (paymentInfoModel instanceof EMIPaymentInfoModel)
			{
				//return MplGlobalCodeConstants.GLOBALCONSTANTSMAP.get(MarketplaceomsordersConstants.PAYMENTMETHOD_EMI.toUpperCase());
				return MplCodeMasterUtility.getglobalCode(MarketplacecommerceservicesConstants.EMI);
			}

			//Added for third party wallet
			else if (paymentInfoModel instanceof ThirdPartyWalletInfoModel)
			{
				//changes for paytm integration--Start
				if (((ThirdPartyWalletInfoModel) paymentInfoModel).getProviderName().equalsIgnoreCase("PAYTM"))
				{
					//return MplCodeMasterUtility.getglobalCode(MarketplacecommerceservicesConstants.PAYTM.toUpperCase());
				}
				else
				{
					return MplCodeMasterUtility.getglobalCode(MarketplacecommerceservicesConstants.MRUPEE);
					//changes for paytm integration--End
				}
			}

			else
			{
				return "CC";
			}
		}
		return "CC";
	}

	/**
	 * @param entry
	 * @return l1 to l4 category
	 */
	private Map<String, String> categoryList(final AbstractOrderEntryModel entry)
	{
		final Map map = new HashMap<String, String>();
		ProductModel product = null;
		if (null != entry)
		{
			product = entry.getProduct();
		}
		if (null != product)
		{
			//List<String> categoryList = new ArrayList<String>();
			final HashMap<String, String> categoryMap = new HashMap<String, String>();
			LOG.debug(">>>>>>> before prodcatlist");
			List<CategoryModel> productCategoryList = new ArrayList<>();
			productCategoryList = getDefaultPromotionsManager().getPrimarycategoryData(product);
			LOG.debug(">>>>>>> after prodcatlist");

			if (null != productCategoryList && productCategoryList.size() > 0)
			{
				LOG.debug("prodcatlist" + productCategoryList.size());
				//categoryList = new ArrayList<String>();
				for (final CategoryModel category : productCategoryList)
				{
					if (category != null && !(category instanceof ClassificationClassModel) && null != category.getCode())
					{
						LOG.debug("Category Data:>>>>>>>> Category Code>>>" + category.getCode());
						//categoryList.add(category.getName()getCode());
						if (category.getName().equalsIgnoreCase("Primary") || category.getCode().length() <= 4)
						{
							continue;
						}
						categoryMap.put(category.getCode(), category.getName());
					}
				}

				if (!(categoryMap.isEmpty()) && categoryMap.size() > 0)
				{
					final Map<String, String> treeMap = new TreeMap<String, String>(new Comparator()
					{

						@Override
						public int compare(final Object o1, final Object o2)
						{
							if (o1.toString().length() > o2.toString().length())
							{
								return 1;
							}
							else if (o1.toString().length() < o2.toString().length())
							{
								return -1;
							}
							else
							{
								return 0;
							}
						}
					});
					treeMap.putAll(categoryMap);

					//					Collections.sort(categoryList, new Comparator()
					//					{
					//
					//						@Override
					//						public int compare(final Object o1, final Object o2)
					//						{
					//							if (o1.toString().length() > o2.toString().length())
					//							{
					//								return 1;
					//							}
					//							else if (o1.toString().length() < o2.toString().length())
					//							{
					//								return -1;
					//							}
					//							else
					//							{
					//								return 0;
					//							}
					//						}
					//
					//					});


					LOG.debug("Inside Category>>>>>");
					LOG.debug("Category size is >>>>> " + treeMap.size());
					if (!(treeMap.isEmpty()) && treeMap.size() > 1)
					{

						int i = 1;
						for (final Map.Entry<String, String> entrySet : treeMap.entrySet())
						{
							if (StringUtils.isNotEmpty(entrySet.getValue()))
							{
								map.put("l" + i, entrySet.getValue());
								LOG.debug("l" + i + "=" + entrySet.getValue());
							}
							else
							{
								map.put("l" + i, entrySet.getKey());
								LOG.debug("l" + i + "=" + entrySet.getKey());
							}
							i++;
						}
						//						for (int i = 1; i < treeMap.size(); i++)
						//						{
						//							map.put("l" + i, treeMap.get(i));
						//							LOG.debug("l" + i + "=" + categoryList.get(i));
						//						}
						//						map.put("l1", categoryList.get(3));
						//						LOG.debug("l1" + categoryList.get(3));
						//						map.put("l2", categoryList.get(2));
						//						LOG.debug("l2" + categoryList.get(2));
						//						map.put("l3", categoryList.get(1));
						//						LOG.debug("l3" + categoryList.get(1));
						//						map.put("l4", categoryList.get(0));
						//						LOG.debug("l4" + categoryList.get(0));
					}

				}

			}
		}
		return map;
	}


	//	@SuppressWarnings("unused")
	//	private String setFirstAndLastName(final OrderModel source)
	//	{
	//		String name = null;
	//		if (source.getDeliveryAddress() != null)
	//		{
	//			final AddressModel deliveryAddressModel = source.getDeliveryAddress();
	//			//stubbed as there is not there in user or address table
	//			if (StringUtils.isNotBlank(deliveryAddressModel.getFirstname()))
	//			{
	//				name = deliveryAddressModel.getFirstname();
	//			}
	//			if (StringUtils.isNotBlank(deliveryAddressModel.getLastname()))
	//			{
	//				name = name + " " + deliveryAddressModel.getLastname();
	//			}
	//		}
	//		return name;
	//	}

	private String setFirstAndLastName(final AddressModel deliveryAddressModel)
	{
		String name = null;
		//stubbed as there is not there in user or address table
		if (null != deliveryAddressModel && StringUtils.isNotBlank(deliveryAddressModel.getFirstname()))
		{
			name = deliveryAddressModel.getFirstname();
		}
		if (null != deliveryAddressModel && StringUtils.isNotBlank(deliveryAddressModel.getLastname()))
		{
			name = name + " " + deliveryAddressModel.getLastname();
		}
		return name;
	}

	protected boolean isGuestCustomerOrder(final OrderModel order)
	{
		final UserModel user = order.getUser();
		return ((user instanceof CustomerModel) && (CustomerType.GUEST.equals(((CustomerModel) user).getType())));
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

	/*
	 * This method is used to get CSV headers from local.properties
	 */
	protected String getCSVHeaderLine()
	{
		return configurationService.getConfiguration().getString("cronjob.omsSubmissionPending.header");
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	protected CustomerNameStrategy getCustomerNameStrategy()
	{
		return this.customerNameStrategy;
	}

	public void setCustomerNameStrategy(final CustomerNameStrategy customerNameStrategy)
	{
		this.customerNameStrategy = customerNameStrategy;
	}

}
