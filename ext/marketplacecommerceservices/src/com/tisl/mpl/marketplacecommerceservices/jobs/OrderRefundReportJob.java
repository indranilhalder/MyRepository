/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.CODPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.DebitCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.EMIPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.NetbankingPaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReplacementEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.OrderRefundData;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.MPLRefundService;
import com.tisl.mpl.model.OrderRefundReportJobModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class OrderRefundReportJob extends AbstractJobPerformable<OrderRefundReportJobModel>
{
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private CronJobService cronJobService;
	@Resource
	private MPLRefundService mplRefundService;

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(OrderRefundReportJob.class.getName());

	/**
	 * @Descriptiion: CronJob to generate Refund/Cancellation Report
	 * @param: reportModel
	 * @return : PerformResult
	 */
	@Override
	public PerformResult perform(final OrderRefundReportJobModel reportModel)
	{
		List<RefundEntryModel> refundModels = null;
		List<ReplacementEntryModel> replaceModels = null;
		List<OrderCancelRecordEntryModel> cancelModels = null;
		Date lastRunTime = null;
		try
		{
			//getting all the Refunds details based on type
			if (getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.SALES_REPORT_INCREMENTAL))
			{
				//fetch cronjob last run time from where it will fetch the next order list
				lastRunTime = cronJobService.getCronJob(reportModel.getCode()).getModifiedtime();
				refundModels = mplRefundService.getAllRefund(lastRunTime);
				replaceModels = mplRefundService.getAllReplacement(lastRunTime);
				cancelModels = mplRefundService.getAllCancelled(lastRunTime);
			}
			else
			{
				refundModels = mplRefundService.getAllRefund(reportModel.getStartDate(), reportModel.getEndDate());
				replaceModels = mplRefundService.getAllReplacement(reportModel.getStartDate(), reportModel.getEndDate());
				cancelModels = mplRefundService.getAllCancelled(reportModel.getStartDate(), reportModel.getEndDate());
			}

			if (refundModels.size() > 0)
			{
				//Convert refund data and write into CSV
				LOG.debug("-----------------REfund Write started--" + refundModels.size());
				writeItemsToCSV(convertForOrderRefund(refundModels));
				LOG.debug("-----------------REfund Write ended");
			}
			if (replaceModels.size() > 0)
			{
				LOG.debug("-----------------Replacement Write started--" + replaceModels.size());
				writeItemsToCSV(convertForOrderReplacement(replaceModels));
				LOG.debug("-----------------Replacement Write ended--" + replaceModels.size());
			}
			if (cancelModels.size() > 0)
			{
				LOG.debug("-----------------Cancel Write started--" + cancelModels.size());
				writeItemsToCSV(convertForOrderCancelled(cancelModels));
				LOG.debug("-----------------Cancel Write ended--" + cancelModels.size());
			}
			else
			{
				LOG.debug(MarketplacecommerceservicesConstants.EXCEPTION_IS + "No Refund report generated");
			}

		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
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
	 * This method is used to get CSV headers from local.properties
	 */
	protected String getCSVHeaderLine()
	{
		return configurationService.getConfiguration().getString("cronjob.orderrefundreport.header", "");
	}

	/*
	 * This method is used to get report type from local.properties
	 */
	protected String getType()
	{
		return configurationService.getConfiguration().getString("cronjob.orderrefundreport.type", "");
	}

	/*
	 * This method is used to get output file from local.properties
	 */
	protected String getOutputFilePath()
	{

		final DateFormat df = new SimpleDateFormat(MarketplacecommerceservicesConstants.DATE_FORMAT_REPORT);
		final String timestamp = df.format(new Date());
		final StringBuilder output_file_path = new StringBuilder();

		if (getType().equalsIgnoreCase(MarketplacecommerceservicesConstants.SALES_REPORT_INCREMENTAL))
		{
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.orderrefundreport.report.path", ""));
			output_file_path.append(File.separator);
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.orderrefundreport.prefix", ""));
			output_file_path.append(MarketplacecommerceservicesConstants.FILE_PATH);
			output_file_path.append(timestamp);
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.orderrefundreport.extension", ""));
		}
		else
		{
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.orderrefundreport.report.path", ""));
			output_file_path.append(File.separator);
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.orderrefundreport.prefix", ""));
			output_file_path.append(configurationService.getConfiguration().getString("cronjob.orderrefundreport.extension", ""));
		}
		return output_file_path.toString();
	}

	/**
	 * This method takes the list of RefundModel and sets in the POJO class which is in turn set in the CSV file to be
	 * generated in a specified location
	 *
	 * @param List
	 *           <RefundEntryModel>
	 * @return List<OrderRefundData>
	 */
	protected List<OrderRefundData> convertForOrderRefund(final List<RefundEntryModel> refunds) throws EtailBusinessExceptions
	{
		//Orderno,OrderDate,SellerSKUId,SellerName,CustomerName,CustomerRegisterDate,Status,PaymentMethod,RefundRef,ReasonCode
		final List<OrderRefundData> dataList = new ArrayList<OrderRefundData>();
		CustomerModel customer = null;
		String sellerId = MarketplacecommerceservicesConstants.EMPTY;
		String sellerName = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			LOG.debug("-----------------Refund convert started--");
			for (final RefundEntryModel refund : refunds)
			{
				final OrderRefundData data = new OrderRefundData();
				if (refund.getOrderEntry().getOrder() != null)
				{
					data.setOrderNo(refund.getOrderEntry().getOrder().getCode());
					data.setOrderDate(refund.getOrderEntry().getOrder().getCreationtime().toString());
					data.setPaymentMethod(getPaymentInfo(refund.getOrderEntry().getOrder()));
					String selectedUssid = "";
					LOG.debug("-----------------Refund convert selected ussid started--" + refund.getPk());
					if (null != refund.getOrderEntry().getSelectedUSSID() && !refund.getOrderEntry().getSelectedUSSID().isEmpty())
					{
						selectedUssid = refund.getOrderEntry().getSelectedUSSID();
					}
					LOG.debug("-----------------Refund convert seller started--" + refund.getPk());
					if (refund.getOrderEntry().getProduct() != null)
					{
						final Collection<SellerInformationModel> sellerInformationData = refund.getOrderEntry().getProduct()
								.getSellerInformationRelator();
						//final MarketplaceDeliveryModeData mplDeliveryMode = entry.getMplDeliveryMode();
						for (final SellerInformationModel sellerEntry : sellerInformationData)
						{
							if (sellerEntry != null && null != selectedUssid && sellerEntry.getUSSID() != null
									&& sellerEntry.getUSSID().equalsIgnoreCase(selectedUssid))
							{
								sellerId = sellerEntry.getSellerID();
								sellerName = sellerEntry.getSellerName();
								break;
							}
						}
					}
					else
					{
						//If product is not there skip teh entry
						LOG.debug("-----------------Refund Skipped--" + refund.getPk());
						continue;
					}
					data.setSellerSKUId(sellerId);
					data.setSellerName(sellerName);
					LOG.debug("-----------------Refund convert customer started--" + refund.getPk());
					if (null != refund.getOrderEntry().getOrder().getUser())
					{
						if (refund.getOrderEntry().getOrder().getUser() instanceof CustomerModel)
						{
							customer = (CustomerModel) refund.getOrderEntry().getOrder().getUser();
						}
						String name = MarketplacecommerceservicesConstants.EMPTY;
						if (customer != null && customer.getDefaultShipmentAddress() != null)
						{
							if (customer.getDefaultShipmentAddress().getFirstname() != null)
							{
								name += customer.getDefaultShipmentAddress().getFirstname() + MarketplacecommerceservicesConstants.SPACE;
							}
							name += MarketplacecommerceservicesConstants.SPACE;
							if (customer.getDefaultShipmentAddress().getLastname() != null)
							{
								name += customer.getDefaultShipmentAddress().getLastname();
							}
							data.setCustomerName(name);
							data.setCustomerRegisterDate(customer.getCreationtime().toString());
						}
					}
				}
				else
				{
					//If product is not there skip the entry
					LOG.debug("-----------------Refund Skipped--" + refund.getPk());
					continue;
				}
				if (null != refund.getReason())
				{
					data.setReasonCode(refund.getReason().getCode());
					LOG.debug("-----------------Refund convert reason end--" + refund.getPk());
				}
				else
				{
					data.setReasonCode(MarketplacecommerceservicesConstants.EMPTY);
				}
				if (null != refund.getStatus())
				{
					data.setStatus(refund.getStatus().getCode());
					LOG.debug("-----------------Refund convert status end--" + refund.getPk());
				}
				else
				{
					data.setStatus(MarketplacecommerceservicesConstants.EMPTY);
				}
				if (null != refund.getNotes())
				{
					//data.set refund.getRefundedDate().toString()
					data.setRefundRef(refund.getNotes());
					LOG.debug("-----------------Refund convert notes end--" + refund.getPk());
				}
				else
				{
					data.setRefundRef(MarketplacecommerceservicesConstants.EMPTY);
				}

				dataList.add(data);
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.REFUND_CONVERSION_ERROR);
		}
		return dataList;

	}

	/**
	 * This method takes the list of ReplacementEntryModel and sets in the POJO class which is in turn set in the CSV
	 * file to be generated in a specified location
	 *
	 * @param List
	 *           <ReplacementEntryModel>
	 * @return List<OrderRefundData>
	 */
	protected List<OrderRefundData> convertForOrderReplacement(final List<ReplacementEntryModel> replacements)
			throws EtailBusinessExceptions
	{
		//Orderno,OrderDate,SellerSKUId,SellerName,CustomerName,CustomerRegisterDate,Status,PaymentMethod,RefundRef,ReasonCode
		final List<OrderRefundData> dataList = new ArrayList<OrderRefundData>();
		CustomerModel customer = null;
		try
		{
			LOG.debug("-----------------Replacement convert started--");
			for (final ReplacementEntryModel replacement : replacements)
			{
				final OrderRefundData data = new OrderRefundData();
				if (replacement.getOrderEntry().getOrder() != null)
				{
					data.setOrderNo(replacement.getOrderEntry().getOrder().getCode());
					data.setOrderDate(replacement.getOrderEntry().getOrder().getCreationtime().toString());
					data.setPaymentMethod(getPaymentInfo(replacement.getOrderEntry().getOrder()));
					String selectedUssid = "";
					LOG.debug("-----------------Replacement convert selected ussid started--" + replacement.getPk());
					if (null != replacement.getOrderEntry().getSelectedUSSID()
							&& !replacement.getOrderEntry().getSelectedUSSID().isEmpty())
					{
						selectedUssid = replacement.getOrderEntry().getSelectedUSSID();
					}
					LOG.debug("-----------------Replacement convert seller started--" + replacement.getPk());
					if (replacement.getOrderEntry().getProduct() != null)
					{
						final Collection<SellerInformationModel> sellerInformationData = replacement.getOrderEntry().getProduct()
								.getSellerInformationRelator();
						//final MarketplaceDeliveryModeData mplDeliveryMode = entry.getMplDeliveryMode();
						for (final SellerInformationModel sellerEntry : sellerInformationData)
						{
							if (sellerEntry != null && null != selectedUssid && sellerEntry.getUSSID() != null
									&& sellerEntry.getUSSID().equalsIgnoreCase(selectedUssid))
							{
								data.setSellerSKUId(sellerEntry.getSellerID());
								data.setSellerName(sellerEntry.getSellerName());
								break;
							}
						}
					}

					LOG.debug("-----------------Replacement convert customer started--" + replacement.getPk());

					if (null != replacement.getOrderEntry().getOrder().getUser())
					{
						if (replacement.getOrderEntry().getOrder().getUser() instanceof CustomerModel)
						{
							customer = (CustomerModel) replacement.getOrderEntry().getOrder().getUser();
						}
						String name = MarketplacecommerceservicesConstants.EMPTY;
						if (customer != null)
						{
							if (customer.getFirstName() != null)
							{
								name += customer.getFirstName();
							}
							name += MarketplacecommerceservicesConstants.SPACE;
							if (customer.getLastName() != null)
							{
								name += customer.getLastName();
							}
							data.setCustomerName(name);
							data.setCustomerRegisterDate(customer.getCreationtime().toString());
						}

						data.setCustomerName(customer.getName());
						data.setCustomerRegisterDate(customer.getCreationtime().toString());
					}

				}
				if (null != replacement.getReason())
				{
					data.setReasonCode(replacement.getReason().getCode());
					LOG.debug("-----------------Refund convert reason end--" + replacement.getPk());
				}
				if (null != replacement.getStatus())
				{
					data.setStatus(replacement.getStatus().getCode());
					LOG.debug("-----------------Refund convert status end--" + replacement.getPk());
				}

				if (null != replacement.getNotes())
				{
					//data.set refund.getRefundedDate().toString()
					data.setRefundRef(replacement.getNotes());
					LOG.debug("-----------------Refund convert notes end--" + replacement.getPk());
				}
				dataList.add(data);
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.REFUND_CONVERSION_ERROR);
		}
		return dataList;

	}

	/**
	 * This method takes the list of OrderCancelRecordEntryModel and sets in the POJO class which is in turn set in the
	 * CSV file to be generated in a specoied location
	 *
	 * @param List
	 *           <OrderCancelRecordEntryModel>
	 * @return List<OrderRefundData>
	 */
	protected List<OrderRefundData> convertForOrderCancelled(final List<OrderCancelRecordEntryModel> cancelleds)
			throws EtailBusinessExceptions
	{
		//Orderno,OrderDate,SellerSKUId,SellerName,CustomerName,CustomerRegisterDate,Status,PaymentMethod,RefundRef,ReasonCode
		final List<OrderRefundData> dataList = new ArrayList<OrderRefundData>();
		CustomerModel customer = null;
		String sellerId = MarketplacecommerceservicesConstants.EMPTY;
		String sellerName = MarketplacecommerceservicesConstants.EMPTY;
		try
		{
			LOG.debug("-----------------Cancelled convert started--");
			for (final OrderCancelRecordEntryModel cancelled : cancelleds)
			{
				if (cancelled.getOrderEntriesModificationEntries().size() > 0)
				{
					for (final OrderEntryModificationRecordEntryModel orderRecord : cancelled.getOrderEntriesModificationEntries())
					{
						final OrderRefundData data = new OrderRefundData();
						LOG.debug("-----------------Cancelled convert recordentries started--" + orderRecord.getPk());
						if (orderRecord.getOrderEntry() != null)
						{
							data.setOrderNo(orderRecord.getOrderEntry().getOrder().getCode());
							data.setOrderDate(orderRecord.getOrderEntry().getOrder().getCreationtime().toString());
							data.setPaymentMethod(getPaymentInfo(orderRecord.getOrderEntry().getOrder()));
							String selectedUssid = MarketplacecommerceservicesConstants.EMPTY;
							LOG.debug("-----------------Cancelled convert ussid started--" + orderRecord.getPk());
							if (null != orderRecord.getOrderEntry().getSelectedUSSID()
									&& !orderRecord.getOrderEntry().getSelectedUSSID().isEmpty())
							{
								selectedUssid = orderRecord.getOrderEntry().getSelectedUSSID();
							}
							LOG.debug("-----------------Cancelled convert seller started--" + orderRecord.getPk());
							if (orderRecord.getOrderEntry().getProduct() != null)
							{
								final Collection<SellerInformationModel> sellerInformationData = orderRecord.getOrderEntry().getProduct()
										.getSellerInformationRelator();
								//final MarketplaceDeliveryModeData mplDeliveryMode = entry.getMplDeliveryMode();
								for (final SellerInformationModel sellerEntry : sellerInformationData)
								{
									if (sellerEntry != null && null != selectedUssid && sellerEntry.getUSSID() != null
											&& sellerEntry.getUSSID().equalsIgnoreCase(selectedUssid))
									{
										sellerId = sellerEntry.getSellerID();
										sellerName = sellerEntry.getSellerName();
										break;
									}
								}
							}
							else
							{
								//If product is not there skip teh entry
								LOG.debug("-----------------Cancelled Skipped--" + orderRecord.getPk());
								continue;
							}
							data.setSellerSKUId(sellerId);
							data.setSellerName(sellerName);
							LOG.debug("-----------------Cancelled convert customer started--" + orderRecord.getPk());
							if (null != orderRecord.getOrderEntry().getOrder().getUser())
							{
								if (orderRecord.getOrderEntry().getOrder().getUser() instanceof CustomerModel)
								{
									customer = (CustomerModel) orderRecord.getOrderEntry().getOrder().getUser();
								}
								String name = MarketplacecommerceservicesConstants.EMPTY;
								if (customer != null)
								{
									if (customer.getFirstName() != null)
									{
										name += customer.getFirstName();
									}
									name += MarketplacecommerceservicesConstants.SPACE;
									if (customer.getLastName() != null)
									{
										name += customer.getLastName();
									}
									data.setCustomerName(name);
									data.setCustomerRegisterDate(customer.getCreationtime().toString());
								}
							}

						}
						else
						{
							//If product is not there skip teh entry
							LOG.debug("-----------------Cancelled Skipped--" + orderRecord.getPk());
							continue;
						}
						if (null != cancelled.getCancelReason())
						{
							data.setReasonCode(cancelled.getCancelReason().getCode());
							LOG.debug("-----------------Cancelled convert reason end--" + orderRecord.getPk());
						}
						else
						{
							data.setReasonCode(MarketplacecommerceservicesConstants.EMPTY);
						}
						if (null != cancelled.getStatus())
						{
							data.setStatus(cancelled.getStatus().getCode());
							LOG.debug("-----------------Cancelled convert status end--" + orderRecord.getPk());
						}
						else
						{
							data.setStatus(MarketplacecommerceservicesConstants.EMPTY);
						}

						if (null != cancelled.getNotes())
						{
							//data.set refund.getRefundedDate().toString()
							data.setRefundRef(cancelled.getNotes());
							LOG.debug("-----------------Cancelled convert notes end--" + orderRecord.getPk());
						}
						else
						{
							data.setRefundRef(MarketplacecommerceservicesConstants.EMPTY);
						}
						dataList.add(data);
					}
				}
			}
			LOG.debug("-----------------Cancelled convert end--");
		}
		catch (

		final Exception e)

		{
			LOG.error(MarketplacecommerceservicesConstants.REFUND_CONVERSION_ERROR);
		}
		return dataList;

	}

	/**
	 * This method takes the list of SalesReportData and set in the CSV file to be generated in a specified location
	 */
	void writeItemsToCSV(final List<OrderRefundData> dataList)
	{
		FileWriter fileWriter = null;
		try
		{
			final File rootFolder = new File(getOutputFilePath());
			rootFolder.getParentFile().mkdirs();
			fileWriter = new FileWriter(rootFolder);
			fileWriter.append(NEW_LINE_SEPARATOR);
			//Write the CSV file header
			fileWriter.append(getCSVHeaderLine());
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			//Write a object list to the CSV file
			//"Orderno,OrderDate,SellerSKUId,SellerName,CustomerName,CustomerRegisterDate,PaymentMethod,ReasonCode,RefundRef,Status
			/*
			 * final String headers[] = CSVHeader.split(","); for (final String header : headers) {
			 * fileWriter.append(header); fileWriter.append(COMMA_DELIMITER); if (headers.length == i) {
			 * fileWriter.append(NEW_LINE_SEPARATOR); } i++; }
			 */
			for (final OrderRefundData report : dataList)
			{
				fileWriter.append(report.getOrderNo());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getOrderDate());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getSellerSKUId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getSellerName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getCustomerName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getCustomerRegisterDate());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getPaymentMethod());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getReasonCode());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getRefundRef());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(report.getStatus());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		}
		catch (final Exception e)
		{
			LOG.error(MarketplacecommerceservicesConstants.CSV_ERROR);
			//e.printStackTrace();
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
				LOG.error(MarketplacecommerceservicesConstants.FILE_WRITER_ERROR);
				//e.printStackTrace();
			}
		}


	}

	private String getPaymentInfo(final AbstractOrderModel source)
	{
		String paymentOption = "";
		if (source.getPaymentInfo() instanceof EMIPaymentInfoModel)
		{
			paymentOption = "EMI";
		}
		if (source.getPaymentInfo() instanceof CreditCardPaymentInfoModel)
		{
			paymentOption = "Credit Card";
		}
		if (source.getPaymentInfo() instanceof DebitCardPaymentInfoModel)
		{
			paymentOption = "Debit Card";
		}
		if (source.getPaymentInfo() instanceof NetbankingPaymentInfoModel)
		{
			paymentOption = "Netbanking";
		}
		if (source.getPaymentInfo() instanceof CODPaymentInfoModel)
		{
			paymentOption = "COD";
		}
		return paymentOption;
	}
}
