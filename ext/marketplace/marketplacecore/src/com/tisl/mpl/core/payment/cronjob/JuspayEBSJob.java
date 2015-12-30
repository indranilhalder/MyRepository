/**
 *
 */
package com.tisl.mpl.core.payment.cronjob;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.NotifyPaymentGroupProcessModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayEBSService;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayWebHookService;
import com.tisl.mpl.marketplacecommerceservices.service.NotifyPaymentGroupMailService;
import com.tisl.mpl.util.ExceptionUtil;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author TCS
 *
 */
public class JuspayEBSJob extends AbstractJobPerformable<CronJobModel>
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(JuspayEBSJob.class.getName());

	@Autowired
	private JuspayWebHookService juspayWebHookService;

	@Autowired
	private JuspayEBSService juspayEBSService;

	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;

	@Autowired
	private NotifyPaymentGroupMailService notifyPaymentGroupMailService;


	/**
	 * @Description : Job to validate Audit Table and WebHook Table Entries
	 * @param:cronModel
	 * @return PerformResult
	 *
	 */
	@Override
	public PerformResult perform(final CronJobModel cronModel)
	{
		try
		{
			LOG.debug("Inside JuspayEBSJob");
			processAuditDetails(cronModel);
			saveCronData(cronModel);
		}
		catch (final EtailBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailBusinessExceptionHandler(exception, null);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		catch (final EtailNonBusinessExceptions exception)
		{
			LOG.error(exception.getMessage());
			ExceptionUtil.etailNonBusinessExceptionHandler(exception);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	/**
	 * @Description : Fetch Audit Details
	 * @param:cronModel
	 *
	 */
	private void processAuditDetails(final CronJobModel cronModel)
	{
		//Logic for Emply Risk
		emptyRiskJob(cronModel);

		//Logic for Held Orders with Risk
		heldOrdersJob(cronModel);
	}



	/**
	 * This method picks up the audit ids with Empty Risk and processes them
	 *
	 * @param cronModel
	 */
	private void emptyRiskJob(final CronJobModel cronModel)
	{
		boolean isTAT1Expired = false;
		boolean errorFlag = false;
		//GetOrderStatusResponse getOrderStatusResponse = new GetOrderStatusResponse();
		List<MplPaymentAuditModel> emptyRiskAuditList = new ArrayList<MplPaymentAuditModel>();
		emptyRiskAuditList = getJuspayEBSService().fetchAuditForEmptyRisk();
		if (!CollectionUtils.isEmpty(emptyRiskAuditList))
		{
			final Double ebsjobTATOne = getJuspayWebHookService().getEmptyEBSJobTAT();

			for (final MplPaymentAuditModel audit : emptyRiskAuditList)
			{
				if (null != audit.getRequestDate() && null != ebsjobTATOne && StringUtils.isNotEmpty(ebsjobTATOne.toString())
						&& null != cronModel && null != cronModel.getStartTime())
				{
					isTAT1Expired = checkIfTATExpired(audit.getRequestDate(), ebsjobTATOne, cronModel.getStartTime());
				}
				else
				{
					isTAT1Expired = true;
				}
				if (null != audit.getCartGUID())
				{
					final OrderModel oModel = fetchOrderDetails(audit.getCartGUID());
					if (null != oModel)
					{
						if (!isTAT1Expired)
						{
							executeProcess(audit, oModel);
						}
						else
						{
							LOG.debug("OMS CALL TO APROVE ORDER");

							getOrderStatusSpecifier().setOrderStatus(oModel, OrderStatus.PAYMENT_SUCCESSFUL);
							errorFlag = getJuspayEBSService().initiateProcess(oModel);
							setAuditExpiredIfNoError(errorFlag, audit);
						}
					}
				}
			}
		}
	}

	/**
	 *
	 * @param errorFlag
	 */
	private void setAuditExpiredIfNoError(final boolean errorFlag, final MplPaymentAuditModel audit)
	{
		if (!errorFlag)
		{
			audit.setIsExpired(Boolean.TRUE);
			modelService.save(audit);
			LOG.debug("Saving Audit Data with Completed Status");
			getJuspayEBSService().createAuditEntry(audit, true);
		}
	}

	/**
	 * This method checks the status of RiskResponse and executes the process accordingly
	 *
	 * @param audit
	 * @param oModel
	 */
	private void executeProcess(final MplPaymentAuditModel audit, final OrderModel oModel)
	{
		GetOrderStatusResponse getOrderStatusResponse = new GetOrderStatusResponse();
		getOrderStatusResponse = getJuspayEBSService().getOrderStatusFromJuspay(audit.getAuditId());
		if (null != getOrderStatusResponse && null != getOrderStatusResponse.getRiskResponse())
		{
			//Logic when risk response fields come later
			if (null != getOrderStatusResponse.getRiskResponse().getEbsRiskPercentage()
					&& StringUtils.isNotEmpty(getOrderStatusResponse.getRiskResponse().getEbsRiskPercentage().toString()))
			{
				final OrderModel orderModel = getJuspayWebHookService().updateAuditEntry(getOrderStatusResponse, audit, oModel);
				if (null != orderModel.getStatus() && StringUtils.isNotEmpty(orderModel.getStatus().toString())
						&& !orderModel.getStatus().equals(OrderStatus.RMS_VERIFICATION_PENDING))
				{
					getJuspayEBSService().initiateProcess(orderModel);
				}
			}

			//Logic when only status comes as approved/rejected later
			else if (StringUtils.isNotEmpty(getOrderStatusResponse.getRiskResponse().getEbsPaymentStatus()))
			{
				final OrderModel orderModel = getJuspayWebHookService().updAuditForEmptyRisk(getOrderStatusResponse, audit, oModel);
				if (null != orderModel.getStatus() && StringUtils.isNotEmpty(orderModel.getStatus().toString())
						&& !orderModel.getStatus().equals(OrderStatus.RMS_VERIFICATION_PENDING))
				{
					getJuspayEBSService().initiateProcess(orderModel);
				}
			}

		}

	}



	/**
	 * This method picks up the audits in Pending Status and processes them
	 *
	 * @param cronModel
	 */
	private void heldOrdersJob(final CronJobModel cronModel)
	{
		boolean isExpired = false;
		boolean errorFlag = false;
		boolean isCompleted = false;
		final Date date = new Date();

		GetOrderStatusResponse getOrderStatusResponse = new GetOrderStatusResponse();
		List<MplPaymentAuditModel> auditList = new ArrayList<MplPaymentAuditModel>();

		final Double ebsjobTAT = getJuspayWebHookService().getEBSJobTAT();
		auditList = getJuspayEBSService().fetchAUDITonEBS();

		//New TAT for sending alert to user group for taking action
		final Double tatForAlert = getJuspayWebHookService().getEBSTatExpiryAlertTime();

		if (!CollectionUtils.isEmpty(auditList))
		{
			LOG.debug("Fetched Audit Entries with Status REVIEW");
			for (final MplPaymentAuditModel audit : auditList)
			{
				isCompleted = checkAuditEntryStatus(audit);
				if (!isCompleted)
				{
					getOrderStatusResponse = getJuspayEBSService().getOrderStatusFromJuspay(audit.getAuditId());
					if (null != audit.getRequestDate() && null != cronModel && null != cronModel.getStartTime())
					{
						isExpired = checkIfTATExpired(audit.getRequestDate(), ebsjobTAT, cronModel.getStartTime());
						final Boolean isMailSent = audit.getIsNotificationSent();

						//For sending alert to Payment User Group when EBS TAT is about to expire
						final boolean isSuccessful = notifyPaymentUserGroup(audit.getAuditId(), audit.getRequestDate(), ebsjobTAT,
								tatForAlert, date, isMailSent);

						if (isSuccessful)
						{
							audit.setIsNotificationSent(Boolean.TRUE);
							modelService.save(audit);
						}

						if (isExpired)
						{
							LOG.debug("OMS CALL TO APROVE ORDER");
							if (null != audit.getCartGUID())
							{
								final OrderModel oModel = fetchOrderDetails(audit.getCartGUID());
								if (null != oModel)
								{
									getOrderStatusSpecifier().setOrderStatus(oModel, OrderStatus.PAYMENT_SUCCESSFUL);
									errorFlag = getJuspayEBSService().initiateProcess(oModel);
									setAuditExpiredIfNoError(errorFlag, audit);
								}
							}

						}
						else
						{
							if (null != getOrderStatusResponse)
							{
								getJuspayEBSService().actionOnResponse(getOrderStatusResponse, audit);
							}
						}
					}
				}
			}
		}
	}



	/**
	 * Description: Check If status Completed for audit Entry
	 *
	 * @param audit
	 * @return boolean
	 */
	private boolean checkAuditEntryStatus(final MplPaymentAuditModel audit)
	{
		boolean isCompleted = false;
		if (null != audit && null != audit.getAuditEntries() && !audit.getAuditEntries().isEmpty())
		{
			for (final MplPaymentAuditEntryModel entryModel : audit.getAuditEntries())
			{
				if (null != entryModel.getStatus() && entryModel.getStatus().equals(MplPaymentAuditStatusEnum.COMPLETED))
				{
					isCompleted = true;
					break;
				}
			}
		}
		return isCompleted;
	}

	/**
	 * @param guid
	 * @Description : Fetch Order Details Based on GUID
	 * @return OrderModel
	 */
	private OrderModel fetchOrderDetails(final String guid)
	{
		OrderModel oModel = null;
		try
		{
			oModel = getJuspayEBSService().fetchOrderOnGUID(guid);
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.debug(exception.getMessage());
		}

		return oModel;
	}

	/**
	 * @Description : Check if TAT Expired for Audit Entry
	 * @param requestDate
	 * @param ebsjobTAT
	 * @param compareTime
	 * @return boolean
	 *
	 */
	private boolean checkIfTATExpired(final Date requestDate, final Double ebsjobTAT, final Date compareTime)
	{
		boolean isExpired = false;

		final Calendar cal = Calendar.getInstance();
		cal.setTime(requestDate);
		cal.add(Calendar.HOUR, +ebsjobTAT.intValue());
		final double tatMin = ebsjobTAT.doubleValue() % 1;
		final double tatMinCalc = tatMin * 60;
		cal.add(Calendar.MINUTE, +(int) tatMinCalc);
		final double tatSec = tatMinCalc % 1;
		final double tatSecCalc = tatSec * 60;
		cal.add(Calendar.SECOND, +(int) tatSecCalc);
		final Date requiredTime = cal.getTime();

		if (compareTime.after(requiredTime))
		{
			isExpired = true;
		}

		return isExpired;
	}

	/**
	 * To send notification to Payment user group to take action on HELD orders from EBS
	 *
	 * @param juspayOrderId
	 * @param requestDate
	 * @param ebsjobTAT
	 * @param ebsAlertTAT
	 * @param compareTime
	 * @return boolean
	 */
	private boolean notifyPaymentUserGroup(final String juspayOrderId, final Date requestDate, final Double ebsjobTAT,
			final Double ebsAlertTAT, final Date compareTime, final Boolean isMailSent)
	{
		boolean isSuccessful = false;
		//final boolean flag = false;
		final Calendar cal = Calendar.getInstance();
		cal.setTime(requestDate);//13:00hrs--13+6=19
		cal.add(Calendar.HOUR, +ebsjobTAT.intValue());//--13+6=19
		final double tatMin = ebsjobTAT.doubleValue() % 1;
		final double tatMinCalc = tatMin * 60;
		cal.add(Calendar.MINUTE, +(int) tatMinCalc);
		final double tatSec = tatMinCalc % 1;
		final double tatSecCalc = tatSec * 60;
		cal.add(Calendar.SECOND, +(int) tatSecCalc);
		cal.add(Calendar.MINUTE, -ebsAlertTAT.intValue());//-15mins
		final Date requiredTime = cal.getTime();//19:00hrs-15mins=18:45hrs

		if (compareTime.after(requiredTime) && null != isMailSent && isMailSent.equals(Boolean.FALSE))
		{
			try
			{
				final List<NotifyPaymentGroupProcessModel> processModels = getNotifyPaymentGroupMailService()
						.checkOrderIdForSentMail(juspayOrderId);

				if (CollectionUtils.isEmpty(processModels))
				{
					isSuccessful = getNotifyPaymentGroupMailService().sendMailToTakeAction(juspayOrderId, ebsAlertTAT);
				}

			}
			catch (final Exception e)
			{
				LOG.error("Error while sending alert to Payment user team  ", e);
			}
		}

		return isSuccessful;
	}

	/**
	 * @Description : Save Cron Job run time
	 * @param cronModel
	 */
	private void saveCronData(final CronJobModel cronModel)
	{
		if (null != cronModel && null != cronModel.getStartTime() && null != cronModel.getCode())
		{
			getJuspayWebHookService().saveCronDetails(cronModel.getStartTime(), cronModel.getCode());
		}
	}



	//Getters and Setters

	/**
	 * @return the juspayWebHookService
	 */
	public JuspayWebHookService getJuspayWebHookService()
	{
		return juspayWebHookService;
	}

	/**
	 * @param juspayWebHookService
	 *           the juspayWebHookService to set
	 */
	public void setJuspayWebHookService(final JuspayWebHookService juspayWebHookService)
	{
		this.juspayWebHookService = juspayWebHookService;
	}

	/**
	 * @return the juspayEBSService
	 */
	public JuspayEBSService getJuspayEBSService()
	{
		return juspayEBSService;
	}

	/**
	 * @param juspayEBSService
	 *           the juspayEBSService to set
	 */
	public void setJuspayEBSService(final JuspayEBSService juspayEBSService)
	{
		this.juspayEBSService = juspayEBSService;
	}


	/**
	 * @return the orderStatusSpecifier
	 */
	public OrderStatusSpecifier getOrderStatusSpecifier()
	{
		return orderStatusSpecifier;
	}

	/**
	 * @param orderStatusSpecifier
	 *           the orderStatusSpecifier to set
	 */
	public void setOrderStatusSpecifier(final OrderStatusSpecifier orderStatusSpecifier)
	{
		this.orderStatusSpecifier = orderStatusSpecifier;
	}

	/**
	 * @return the notifyPaymentGroupMailService
	 */
	public NotifyPaymentGroupMailService getNotifyPaymentGroupMailService()
	{
		return notifyPaymentGroupMailService;
	}

	/**
	 * @param notifyPaymentGroupMailService
	 *           the notifyPaymentGroupMailService to set
	 */
	public void setNotifyPaymentGroupMailService(final NotifyPaymentGroupMailService notifyPaymentGroupMailService)
	{
		this.notifyPaymentGroupMailService = notifyPaymentGroupMailService;
	}


}
