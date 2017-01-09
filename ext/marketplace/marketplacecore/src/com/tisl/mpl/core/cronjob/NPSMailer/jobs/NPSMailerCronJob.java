/**
 *
 */
package com.tisl.mpl.core.cronjob.NPSMailer.jobs;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.NPSEmailerModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.FetchSalesOrderService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class NPSMailerCronJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(NPSMailerCronJob.class.getName());

	@Autowired
	private ModelService modelService;

	@Autowired
	private UserService userService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.hybris.platform.cronjob.model.CronJobModel
	 * )
	 */
	@Override
	public PerformResult perform(final CronJobModel oModel)
	{
		// YTODO Auto-generated method stub
		System.out.println("Entering perform :: NPSMailerCronJob");
		try
		{
			/*
			 * Query for fetching all the orderModels along with the transaction ids from commerce end whose delivery date
			 * is 24 hours ago
			 */
			final Map<OrderModel, List<String>> parentOrderModelList = getFetchSalesOrderService()
					.fetchOrderDetailsforDeliveryMail();


			System.out.println("in cron job");
			//final Date date = new Date();
			//System.out.println(">>>>>>>>>>>>>>>>>>>>>>Date>>>>>>>>>>>>>>>>>>" + date);

			/* Fetches the count of all the transaction Ids whose allEmailSent flag is false */
			final Map<String, Integer> npsEmailerList = getFetchSalesOrderService().getTransactionIdCount();
			System.out.println("npsEmailerList==" + npsEmailerList);
			/* Fetches all the transaction Ids whose allEmailSent flag is false */
			final Map<String, NPSEmailerModel> npsEmailerTransactionIdMap = getFetchSalesOrderService().getTransactionIdList();
			System.out.println("npsEmailerTransactionIdMap==" + npsEmailerTransactionIdMap);
			final Map<String, NPSEmailerModel> finalPopulateNpsTable = new HashMap<String, NPSEmailerModel>();
			final List<NPSEmailerModel> finalPopulateNpsList = new ArrayList<NPSEmailerModel>();
			final Set<String> finalOrdersSet = new HashSet<String>();



			final Map<String, String> ordersEmailSntTodayMap = getFetchSalesOrderService().fetchOrderIdsToday();

			for (final Map.Entry<OrderModel, List<String>> entryParent : parentOrderModelList.entrySet())
			{
				final String parentorderNo = entryParent.getKey().getCode();
				final List<String> transactionIds = entryParent.getValue();
				System.out.println("For order# " + parentorderNo + " -" + transactionIds);
				for (final String transactionId : transactionIds)
				{
					/* Need to check whether mail has been sent for this Transaction Id */
					if (npsEmailerTransactionIdMap.containsKey(transactionId))
					{
						System.out.println("\t\t\tTrans Id " + transactionId + " exists in NPSEmailer table");
						continue;

					}
					/* Need to check whether the Transaction Id has already been considered for sending email */
					else if ((finalPopulateNpsTable.get(parentorderNo) == null)
							&& (!ordersEmailSntTodayMap.containsKey(parentorderNo)))

					{
						System.out.println("\t\t\tTrans Id " + transactionId + " does not exist in NPSEmailer table");
						final NPSEmailerModel npsEmailerModel = modelService.create(NPSEmailerModel.class);
						npsEmailerModel.setTransactionId(transactionId);
						npsEmailerModel.setIsEmailSent(Boolean.TRUE);
						//npsEmailerModel.setTimeSent(date);
						npsEmailerModel.setParentOrderNo(entryParent.getKey());
						npsEmailerModel.setCustomer((CustomerModel) entryParent.getKey().getUser());


						/*
						 * if ((transactionIdCountMap.get(transactionIdEntry.getKey()).intValue() - 1) == npsEmailerList.get(
						 * transactionIdEntry.getKey()).intValue()) { npsEmailerModel.setAllEmailSent(Boolean.TRUE); } else {
						 * npsEmailerModel.setAllEmailSent(Boolean.FALSE); }
						 */
						finalPopulateNpsTable.put(parentorderNo, npsEmailerModel);
						finalOrdersSet.add(parentorderNo);
						//finalPopulateNpsList.add(npsEmailerModel);
						//Case1:send email notification
						System.out.println("Send notification");
						break;
					}
				}
			}

			System.out.println("finalPopulateNpsTable==" + finalPopulateNpsTable);

			final Set<String> parentOrderIdsSet = new HashSet<String>();

			for (final Map.Entry<String, Integer> npsEmailerentry : npsEmailerList.entrySet())
			{
				/* Need to check whether the order no has already been considered for sending email */
				if (finalPopulateNpsTable.get(npsEmailerentry.getKey()) != null)
				{
					continue;
				}
				else
				{
					parentOrderIdsSet.add(npsEmailerentry.getKey());
				}

			}


			finalOrdersSet.addAll(parentOrderIdsSet);
			System.out.println("parentOrderIdsSet==" + parentOrderIdsSet + "--finalOrdersSet==" + finalOrdersSet);

			Map<String, Integer> transactionIdCountMap = null;
			System.out.println("transactionIdCountMap==" + transactionIdCountMap);
			List<Map> transactionIdList = null;

			if (CollectionUtils.isNotEmpty(finalOrdersSet))
			{
				transactionIdList = getFetchSalesOrderService().getOrderModelTransactionId(finalOrdersSet); //parentOrderIdsSet);
				transactionIdCountMap = getFetchSalesOrderService().getorderModelTransactionCount(finalOrdersSet);
			}
			else
			{
				transactionIdList = new ArrayList<Map>();
				transactionIdCountMap = new HashMap<String, Integer>();
			}

			final Map<String, List<String>> transactionIdMap = (transactionIdList.size() == 2) ? (Map<String, List<String>>) transactionIdList
					.get(0) : new HashMap<String, List<String>>();
			final Map<String, OrderModel> orderModelMap = (transactionIdList.size() == 2) ? (Map<String, OrderModel>) transactionIdList
					.get(1) : new HashMap<String, OrderModel>();

			System.out.println("transactionIdMap==" + transactionIdMap + "--orderModelMap==" + orderModelMap);

			for (final Map.Entry<String, List<String>> transactionIdEntry : transactionIdMap.entrySet())
			{
				final List<String> transIdsList = transactionIdEntry.getValue();
				System.out.println("For parent order " + transactionIdEntry + "--" + transIdsList);
				for (final String transId : transIdsList)
				{
					if ((npsEmailerTransactionIdMap.get(transId) == null)
							&& (!ordersEmailSntTodayMap.containsKey(transactionIdEntry.getKey())))
					{
						System.out.println("\t\t\tTrans Id " + transId + " does not exist in NPS table");
						final NPSEmailerModel npsEmailerModel = modelService.create(NPSEmailerModel.class);
						npsEmailerModel.setTransactionId(transId);
						npsEmailerModel.setIsEmailSent(Boolean.TRUE);
						//npsEmailerModel.setTimeSent(date);
						npsEmailerModel.setCustomer((CustomerModel) orderModelMap.get(transactionIdEntry.getKey()).getUser());
						/*
						 * if ((transactionIdCountMap.get(transactionIdEntry.getKey()).intValue() - 1) == npsEmailerList.get(
						 * transactionIdEntry.getKey()).intValue()) { npsEmailerModel.setAllEmailSent(Boolean.TRUE); } else {
						 * npsEmailerModel.setAllEmailSent(Boolean.FALSE); }
						 */
						npsEmailerModel.setParentOrderNo(orderModelMap.get(transactionIdEntry.getKey()));

						finalPopulateNpsTable.put(transactionIdEntry.getKey(), npsEmailerModel);
						break;
					}
					else
					{
						System.out.println("Trans Id " + transId + " exists in NPS table " + "or " + transId
								+ "was not considered for sending email ");
					}
				}
			}

			//final Collection<NPSEmailerModel> allEmailSentFlagList = finalPopulateNpsTable.values();

			for (final Map.Entry<String, NPSEmailerModel> npsEntry : finalPopulateNpsTable.entrySet())
			{
				final NPSEmailerModel npsEmailerModel = npsEntry.getValue();
				int npsTransIdCount = 0;
				if (npsEmailerList.get(npsEntry.getKey()) != null)
				{
					npsTransIdCount = npsEmailerList.get(npsEntry.getKey()).intValue();
				}

				System.out.println("For order# " + npsEntry.getKey() + " " + transactionIdCountMap.get(npsEntry.getKey()) + "--"
						+ npsTransIdCount);

				if ((transactionIdCountMap.get(npsEntry.getKey()) != null)
						&& ((transactionIdCountMap.get(npsEntry.getKey()).intValue() - 1) == npsTransIdCount))
				{
					npsEmailerModel.setAllEmailSent(Boolean.TRUE);

					System.out.println("For order# " + npsEntry.getKey() + " " + npsEmailerModel.getTransactionId()
							+ " AllEmailSentFlag added");
					finalPopulateNpsList.add(npsEmailerModel);

					/*
					 * The if condition will be true for orders having multiple transaction ids and having atleast one entry
					 * in the NPSEmailer table
					 */
					if (npsTransIdCount >= 1 && transactionIdMap.get(npsEntry.getKey()) != null)
					{
						for (final String transId : transactionIdMap.get(npsEntry.getKey()))
						{
							if (npsEmailerTransactionIdMap.get(transId) != null)
							{
								final NPSEmailerModel npsEmailerOthersModel = npsEmailerTransactionIdMap.get(transId);
								npsEmailerOthersModel.setAllEmailSent(Boolean.TRUE);
								finalPopulateNpsList.add(npsEmailerOthersModel);

								System.out.println("\t\t\t For order# " + npsEntry.getKey() + " "
										+ npsEmailerOthersModel.getTransactionId() + " AllEmailSentFlag added for other trans ids");
							}
						}
					}
				}
				else
				{
					npsEmailerModel.setAllEmailSent(Boolean.FALSE);
					System.out.println("For order# " + npsEntry.getKey() + " " + npsEmailerModel.getTransactionId()
							+ " AllEmailSentFlag set to false");
					finalPopulateNpsList.add(npsEmailerModel);
				}
			}



			System.out.println(finalPopulateNpsList);
			modelService.saveAll(finalPopulateNpsList);

			/*
			 * for (final NPSEmailerModel nmodel : allEmailSentFlagList) { //code try {
			 * 
			 * System.out.println(nmodel.getCustomer()); System.out.println(nmodel.getIsEmailSent());
			 * System.out.println(nmodel.getParentOrderNo()); System.out.println(nmodel.getTransactionId());
			 * System.out.println(nmodel.getTimeSent());
			 * 
			 * System.out.println(nmodel.getAllEmailSent());
			 * 
			 * modelService.save(nmodel); } catch (final Exception e) { System.out.println("Exception is thrown for " +
			 * nmodel.getTransactionId() + "--" + e); } }
			 */



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

	protected FetchSalesOrderService getFetchSalesOrderService()
	{
		return Registry.getApplicationContext().getBean("fetchSalesOrderServiceImpl", FetchSalesOrderService.class);
	}

}