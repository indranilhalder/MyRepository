package com.tisl.mpl.facade.job;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.pojo.OmsOrderCancelTaskContext;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class InitiateCancelForOrderJob extends AbstractJobPerformable<CronJobModel>
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(InitiateCancelForOrderJob.class.getName());
	/*
	 * @Autowired private CancelReturnFacade cancelReturnFacade;
	 */
	@Resource(name = "orderModelService")
	private OrderModelService orderModelService;

	/* sonar fix */
	/*
	 * @Resource(name = "mplOrderFacade") private MplOrderFacade mplOrderFacade;
	 */
	/*
	 * @Resource(name = "sessionService") private SessionService sessionService;
	 * 
	 * @Autowired private BaseStoreService baseStoreService;
	 * 
	 * @Autowired private PriceDataFactory priceDataFactory;
	 */
	private Converter<OrderModel, OrderData> orderConverter;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	private TaskService taskService;

	//SONAR FIX
	//@Autowired
	//private BulkCancellationService bulkCancellationService;

	@SuppressWarnings(MarketplacecommerceservicesConstants.BOXING)
	/*
	 * private static final String COMMA_DELIMITER = ","; private static final String NEW_LINE = "\r\n"; private static
	 * final String DOUBLE_QUOTE = "\""; private static final String HYPHEN = "-"; private static final String DOT = ".";
	 * private static final String FILE_EXTENSION = "csv"; private static final String REPORT_FILENAME =
	 * "bulk_cancellation_report";
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		try
		{

			/*
			 * final Map<OrderEntryData, BulkCancelStoreData> cancellationDataMap = new HashMap<OrderEntryData,
			 * BulkCancelStoreData>(); final List<BulkCancellationProcessModel> finalModelToSaveList = new
			 * ArrayList<BulkCancellationProcessModel>();
			 */

			//final List<BulkCancellationProcessModel> bulkCanProcessList = bulkCancellationService.getOrderCancelData();
			final List<OrderEntryModel> orderCanList = orderModelService.getOrderCancelData();

			for (final OrderEntryModel orderEntry : orderCanList)
			{
				final OrderData childOrderData = orderConverter.convert(orderEntry.getOrder());
				for (final OrderEntryData orderEntryData : childOrderData.getEntries())
				{
					final long sleepTime = Long.parseLong(configurationService.getConfiguration().getString(
							MarketplacecommerceservicesConstants.initiate_cancel_job_thread_sleep));

					if (orderEntryData.getOrderLineId().equalsIgnoreCase(orderEntry.getOrderLineId()))
					{
						Thread.sleep(sleepTime);
						final TaskModel task = (TaskModel) this.modelService.create(TaskModel.class);
						task.setContext(new OmsOrderCancelTaskContext(childOrderData, orderEntryData));
						task.setRunnerBean("omsOrderCancelTaskRunner");
						getTaskService().scheduleTask(task);

					}

				}

				/*
				 * final Date date = new Date(); final SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd-HHmmss-"); final
				 * String fileName = ft.format(date) + HYPHEN + REPORT_FILENAME + DOT + FILE_EXTENSION; final
				 * List<Map<Integer, String>> listOfMaps = new ArrayList<Map<Integer, String>>(); Map<Integer, String>
				 * dataMap = null; for (final BulkCancellationProcessModel bulkCanEntry : bulkCanProcessList) { String
				 * status = null; bulkCanEntry.getTransactionId(); bulkCanEntry.getParentOrderNo();
				 * bulkCanEntry.getLoadStatus(); if ("1".equals(bulkCanEntry.getLoadStatus())) { status = "Success"; } else
				 * if ("-1".equals(bulkCanEntry.getLoadStatus())) { status = "Failure"; } else { status = "Initiate"; }
				 * dataMap = new HashMap<Integer, String>(); dataMap.put(Integer.valueOf(1),
				 * bulkCanEntry.getParentOrderNo()); dataMap.put(Integer.valueOf(2), bulkCanEntry.getTransactionId());
				 * dataMap.put(Integer.valueOf(3), status); listOfMaps.add(dataMap); } writeToCSV(listOfMaps, fileName);
				 */

			}


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

	/*
	 * public void writeToCSV(final List<Map<Integer, String>> listOfMaps, final String fileName) { final String
	 * exportFilePath = configurationService.getConfiguration().getString(
	 * MarketplacecommerceservicesConstants.bulk_cancellation_report_path); final File exportDir = new
	 * File(exportFilePath); // Creating export directory if not exists. if (!exportDir.isDirectory()) {
	 * exportDir.mkdir(); } String exportFileName = null;
	 *
	 *
	 * exportFileName = exportFilePath + fileName;
	 *
	 *
	 * final File exportFile = new File(exportFileName); if (!exportFile.exists()) { try { exportFile.createNewFile(); }
	 * catch (final IOException e) { LOG.error("error occurred while creating the export file" + e.getMessage()); } }
	 *
	 * try { BufferedWriter bufferedWriter = null; final FileWriter fileWriter = new FileWriter(exportFileName, true);
	 * bufferedWriter = new BufferedWriter(fileWriter); if (listOfMaps != null && listOfMaps.size() > 0) { for (final
	 * Map<Integer, String> dataMap : listOfMaps) { for (int i = 0; i < dataMap.size(); i++) {
	 * bufferedWriter.append(DOUBLE_QUOTE); bufferedWriter.append(dataMap.get(Integer.valueOf(i + 1)));
	 * bufferedWriter.append(DOUBLE_QUOTE); if ((i + 1) != dataMap.size()) { bufferedWriter.append(COMMA_DELIMITER); }
	 *
	 * } bufferedWriter.append(NEW_LINE); } bufferedWriter.flush(); bufferedWriter.close(); fileWriter.close();
	 *
	 *
	 *
	 * } } catch (UnsupportedEncodingException | FileNotFoundException e) {
	 * LOG.error("Error occurred while creating the export file" + e.getMessage()); } catch (final IOException e) {
	 * LOG.error("Error occurred while writing into the export file" + e.getMessage()); } }
	 */

	/**
	 * @param finalModelToSaveList
	 *
	 *
	 *           private void saveBulkCancelData(final List<BulkCancellationProcessModel> finalModelToSaveList) {
	 *
	 *
	 *           final int maxAllowedSize = configurationService.getConfiguration().getInt(
	 *           MarketplacecommerceservicesConstants.initiate_cancel_job_cancellation_count, 1);
	 *
	 *           if (finalModelToSaveList.size() > maxAllowedSize)//save data in sub batches { int cnt = 0; while (cnt <=
	 *           finalModelToSaveList.size()) {
	 *
	 *           List<BulkCancellationProcessModel> subList = null; if (maxAllowedSize < (finalModelToSaveList.size() -
	 *           cnt)) { subList = finalModelToSaveList.subList(cnt, (cnt + maxAllowedSize)); } else { subList =
	 *           finalModelToSaveList.subList(cnt, (finalModelToSaveList.size()));//get rest of the data }
	 *
	 *           modelService.saveAll(subList); cnt = cnt + maxAllowedSize; } } else {
	 *           modelService.saveAll(finalModelToSaveList); //save all data }
	 *
	 *           }
	 */

	/**
	 * @param cancellationDataMap
	 * @return List
	 *
	 *
	 *         private List<BulkCancellationProcessModel> callOMStoCancelOrder( final Map<OrderEntryData,
	 *         BulkCancelStoreData> cancellationDataMap) { String ussid = null; final String ticketTypeCode =
	 *         MarketplacecommerceservicesConstants.TICKETTYPECODE_CANCEL; final String refundType =
	 *         MarketplacecommerceservicesConstants.REFUNDTYPE; final String reasonCode =
	 *         MarketplacecommerceservicesConstants.REASONCODE; String orderConsignmentStatus = null; CustomerData
	 *         customerData = null; boolean cancellationStatus = false; boolean isCancellable = false; OrderEntryData
	 *         subOrderEntry = null; OrderData subOrderDetails = null; final List<BulkCancellationProcessModel>
	 *         finalModelToSaveList = new ArrayList<BulkCancellationProcessModel>();
	 *
	 *         for (final OrderEntryData entryHashMap : cancellationDataMap.keySet()) {
	 *
	 *         subOrderEntry = entryHashMap; final BulkCancelStoreData mplCancelStoreData =
	 *         cancellationDataMap.get(entryHashMap); subOrderDetails = mplCancelStoreData.getSubOrderDetails();
	 *         customerData = subOrderDetails.getCustomerData(); final BulkCancellationProcessModel bulkCancelModel =
	 *         mplCancelStoreData.getBulkCancelModelData(); ussid = subOrderEntry.getSelectedUssid();
	 *
	 *
	 *         // Check consignment status (if present) and cancellable flag
	 *
	 *         if (null == subOrderEntry.getConsignment() && subOrderEntry.getQuantity().intValue() != 0 && null !=
	 *         subOrderDetails.getStatus().getCode()) {
	 *
	 *         orderConsignmentStatus = subOrderDetails.getStatus().getCode(); isCancellable =
	 *         mplOrderFacade.checkCancelStatus(orderConsignmentStatus,
	 *         MarketplacecommerceservicesConstants.CANCEL_ORDER_STATUS); } else if (null !=
	 *         subOrderEntry.getConsignment() && null != subOrderEntry.getConsignment().getStatus().getCode()) {
	 *
	 *         orderConsignmentStatus = subOrderEntry.getConsignment().getStatus().getCode(); isCancellable =
	 *         mplOrderFacade.checkCancelStatus(orderConsignmentStatus,
	 *         MarketplacecommerceservicesConstants.CANCEL_STATUS); }
	 *
	 *         String message = MarketplacecommerceservicesConstants.EMPTY; message =
	 *         MarketplacecommerceservicesConstants.BLANK_SPACE + MarketplacecommerceservicesConstants.LEFT_PARENTHESIS +
	 *         MarketplacecommerceservicesConstants.LAST_CONSIGNMENT_STATUS + orderConsignmentStatus +
	 *         MarketplacecommerceservicesConstants.RIGHT_PARENTHESIS;
	 *
	 *         // Call cancelReturnFacade to implement cancellation process and to create CRM ticket if
	 *         (ticketTypeCode.equalsIgnoreCase(MarketplacecommerceservicesConstants.TICKETTYPECODE_CANCEL) && null !=
	 *         orderConsignmentStatus && isCancellable) { if
	 *         (StringUtils.isNotEmpty(configurationService.getConfiguration().getString(
	 *         MarketplacecommerceservicesConstants.initiate_cancel_job_cancellation_flag)) &&
	 *         (configurationService.getConfiguration().getString(
	 *         MarketplacecommerceservicesConstants.initiate_cancel_job_cancellation_flag).equals("true"))) {
	 *         cancellationStatus = Boolean.parseBoolean(configurationService.getConfiguration().getString(
	 *         MarketplacecommerceservicesConstants.initiate_cancel_job_cancellation_flag)); } else { cancellationStatus
	 *         = cancelReturnFacade.implementCancelOrReturn(subOrderDetails, subOrderEntry, reasonCode, ussid,
	 *         ticketTypeCode, customerData, refundType, false, SalesApplication.WEB); }
	 *
	 *         if (cancellationStatus) {
	 *         bulkCancelModel.setLoadStatus(MarketplacecommerceservicesConstants.SUCCESS_LOAD_STATUS);
	 *         bulkCancelModel.setStatusDescription(MarketplacecommerceservicesConstants.BULK_CANCEL_SUCCESS_DESC +
	 *         message);
	 *
	 *         } else { bulkCancelModel.setLoadStatus(MarketplacecommerceservicesConstants.FAILURE_LOAD_STATUS);
	 *         bulkCancelModel.setStatusDescription(MarketplacecommerceservicesConstants.BULK_CANCEL_FAILURE_DESC +
	 *         message); } finalModelToSaveList.add(bulkCancelModel); } else {
	 *         bulkCancelModel.setLoadStatus(MarketplacecommerceservicesConstants.FAILURE_LOAD_STATUS);
	 *         bulkCancelModel.setStatusDescription(MarketplacecommerceservicesConstants.BULK_CANCEL_FAILURE_DESC +
	 *         message); finalModelToSaveList.add(bulkCancelModel); } } return finalModelToSaveList; }
	 */

	/**
	 * @param orderModel
	 * @return OrderData
	 *
	 *         protected OrderData convertToData(final OrderModel orderModel) { OrderData orderData = null; CustomerData
	 *         customerData = new CustomerData(); CustomerModel customer = null; PriceData convenienceCharge = null;
	 *         PriceData totalPriceWithConvenienceCharge = null; final List<OrderData> sellerOrderList = new
	 *         ArrayList<OrderData>(); try { final PriceData deliveryCost = createPrice(orderModel,
	 *         orderModel.getDeliveryCost()); if (orderModel.getConvenienceCharges() != null) { convenienceCharge =
	 *         createPrice(orderModel, orderModel.getConvenienceCharges()); } if (orderModel.getTotalPriceWithConv() !=
	 *         null) { totalPriceWithConvenienceCharge = createPrice(orderModel, orderModel.getTotalPriceWithConv()); }
	 *         //skip the order if product is missing in the order entries for (final AbstractOrderEntryModel orderEntry
	 *         : orderModel.getEntries()) { if (null == orderEntry.getProduct()) // it means somehow product is deleted
	 *         from the order entry. { return null; } } orderData = getOrderConverter().convert(orderModel);
	 *         orderData.setDeliveryCost(deliveryCost); if (convenienceCharge != null) {
	 *         orderData.setConvenienceChargeForCOD(convenienceCharge); } if (totalPriceWithConvenienceCharge != null) {
	 *         orderData.setTotalPriceWithConvCharge(totalPriceWithConvenienceCharge); } if (orderModel.getUser()
	 *         instanceof CustomerModel) { customer = (CustomerModel) orderModel.getUser(); } if (customer != null &&
	 *         null != customer.getDefaultShipmentAddress()) { customerData = new CustomerData(); //TISUAT-4850 if
	 *         (customer.getOriginalUid() != null) { customerData.setEmail(customer.getOriginalUid()); } else {
	 *         customerData.setEmail(MarketplacecommerceservicesConstants.NA); }
	 *         customerData.setRegistrationDate(customer.getCreationtime()); orderData.setCustomerData(customerData); }
	 *
	 *         for (final OrderModel sellerOrder : orderModel.getChildOrders()) { final PriceData childDeliveryCost =
	 *         createPrice(sellerOrder, sellerOrder.getDeliveryCost()); final OrderData sellerOrderData =
	 *         getOrderConverter().convert(sellerOrder); orderData.setDeliveryCost(childDeliveryCost); if
	 *         (convenienceCharge != null) { sellerOrderData.setConvenienceChargeForCOD(convenienceCharge); } if
	 *         (totalPriceWithConvenienceCharge != null) {
	 *         sellerOrderData.setTotalPriceWithConvCharge(totalPriceWithConvenienceCharge); } if (orderModel.getUser()
	 *         instanceof CustomerModel) { customer = (CustomerModel) orderModel.getUser(); } if (customer != null) {
	 *         customerData = new CustomerData(); //TISUAT-4850 if (customer.getOriginalUid() != null) {
	 *         customerData.setEmail(customer.getOriginalUid()); } else {
	 *         customerData.setEmail(MarketplacecommerceservicesConstants.NA); }
	 *         customerData.setRegistrationDate(customer.getCreationtime());
	 *         sellerOrderData.setCustomerData(customerData); } sellerOrderList.add(sellerOrderData); }
	 *         orderData.setSellerOrderList(sellerOrderList);
	 *
	 *         } catch (final Exception e) { return orderData; }
	 *
	 *         return orderData;
	 *
	 *         }
	 *
	 *         private PriceData createPrice(final AbstractOrderModel source, final Double val) { if (source == null) {
	 *         throw new IllegalArgumentException(MarketplacecommerceservicesConstants.ORDER_ERROR); }
	 *
	 *         final CurrencyModel currency = source.getCurrency();
	 *
	 *         if (currency == null) { throw new
	 *         IllegalArgumentException(MarketplacecommerceservicesConstants.ORDER_CURRENCY_ERROR); }
	 *
	 *         // Get double value, handle null as zero final double priceValue = val != null ? val.doubleValue() : 0d;
	 *
	 *         return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency); }
	 */
	/**
	 * @return the sessionService
	 *
	 *         public SessionService getSessionService() { return sessionService; }
	 */

	/**
	 * @param sessionService
	 *           the sessionService to set
	 * @Override public void setSessionService(final SessionService sessionService) { this.sessionService =
	 *           sessionService; }
	 */

	/**
	 * @return the baseStoreService
	 *
	 *         public BaseStoreService getBaseStoreService() { return baseStoreService; }
	 */

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 *
	 *           public void setBaseStoreService(final BaseStoreService baseStoreService) { this.baseStoreService =
	 *           baseStoreService; }
	 */

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
	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}


	/**
	 * @return the priceDataFactory
	 *
	 *         public PriceDataFactory getPriceDataFactory() { return priceDataFactory; }
	 */

	/**
	 * @param priceDataFactory
	 *           the priceDataFactory to set
	 *
	 *           public void setPriceDataFactory(final PriceDataFactory priceDataFactory) { this.priceDataFactory =
	 *           priceDataFactory; }
	 */
	/**
	 * @return the taskService
	 */
	public TaskService getTaskService()
	{
		return taskService;
	}

	/**
	 * @param taskService
	 *           the taskService to set
	 */
	@Required
	public void setTaskService(final TaskService taskService)
	{
		this.taskService = taskService;
	}
}