/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.model.JuspayEBSResponseModel;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.juspay.PaymentService;
import com.tisl.mpl.juspay.request.GetOrderStatusRequest;
import com.tisl.mpl.juspay.response.GetOrderStatusResponse;
import com.tisl.mpl.marketplacecommerceservices.daos.JuspayWebHookDao;
import com.tisl.mpl.marketplacecommerceservices.service.JuspayEBSService;
import com.tisl.mpl.marketplacecommerceservices.service.RMSVerificationNotificationService;
import com.tisl.mpl.util.OrderStatusSpecifier;


/**
 * @author TCS
 *
 */
public class DefaultJuspayEBSServiceImpl implements JuspayEBSService
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultJuspayEBSServiceImpl.class.getName());

	private JuspayWebHookDao juspayWebHookDao;

	private ModelService modelService;

	private ConfigurationService configurationService;

	private BusinessProcessService businessProcessService;

	@Autowired
	private OrderStatusSpecifier orderStatusSpecifier;

	@Autowired
	private RMSVerificationNotificationService rMSVerificationNotificationService;

	/**
	 * @Description: Fetch Audit Based on EBS Status
	 */
	@Override
	public List<MplPaymentAuditModel> fetchAUDITonEBS()
	{
		return juspayWebHookDao.fetchAUDITonEBS(MarketplacecommerceservicesConstants.EBS_STATUS_REVIEW);
	}

	/**
	 * @Description: Fetch Audit which have empty risk Based on EBS Risk
	 */
	@Override
	public List<MplPaymentAuditModel> fetchAuditForEmptyRisk()
	{
		return juspayWebHookDao.fetchAuditForEmptyRisk(MarketplacecommerceservicesConstants.DEFAULT_EBS_RISK_PERC);
	}

	/**
	 * @return the juspayWebHookDao
	 */
	public JuspayWebHookDao getJuspayWebHookDao()
	{
		return juspayWebHookDao;
	}

	/**
	 * @param juspayWebHookDao
	 *           the juspayWebHookDao to set
	 */
	public void setJuspayWebHookDao(final JuspayWebHookDao juspayWebHookDao)
	{
		this.juspayWebHookDao = juspayWebHookDao;
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
	 * @Description : Get Juspay Response corresponding to a Juspay ID
	 * @param auditID
	 * @return : getOrderStatusResponse
	 */
	@Override
	public GetOrderStatusResponse getOrderStatusFromJuspay(final String auditID)
	{
		GetOrderStatusResponse orderStatusResponse = new GetOrderStatusResponse();

		if (null != auditID && StringUtils.trim(auditID).length() > 0)
		{
			final PaymentService juspayService = new PaymentService();

			juspayService.setBaseUrl(
					getConfigurationService().getConfiguration().getString(MarketplacecommerceservicesConstants.JUSPAYBASEURL));
			juspayService
					.withKey(getConfigurationService().getConfiguration()
							.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTTESTKEY))
					.withMerchantId(getConfigurationService().getConfiguration()
							.getString(MarketplacecommerceservicesConstants.JUSPAYMERCHANTID));

			//creating OrderStatusRequest
			final GetOrderStatusRequest orderStatusRequest = new GetOrderStatusRequest();
			LOG.debug("Audit ID: for fetching Response" + auditID);
			orderStatusRequest.withOrderId(auditID);
			try
			{
				orderStatusResponse = juspayService.getOrderStatus(orderStatusRequest);
			}
			catch (final Exception exception)
			{
				LOG.error("No Response Received");
				LOG.error(exception.getMessage());
			}
		}

		return orderStatusResponse;
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
	 * @Description : Perform Action on Response
	 * @param: getOrderStatusResponse
	 * @param: audit
	 */
	@Override
	public void actionOnResponse(final GetOrderStatusResponse orderStatusResponse, final MplPaymentAuditModel audit)
	{

		if (null != orderStatusResponse && null != audit && null != audit.getRisk() && !audit.getRisk().isEmpty())
		{
			for (final JuspayEBSResponseModel ebsModel : audit.getRisk())
			{
				if (null != ebsModel.getEbsRiskStatus() && null != orderStatusResponse.getRiskResponse()
						&& null != orderStatusResponse.getRiskResponse().getEbsPaymentStatus())
				{
					//setting order status based on Risk status from EBS
					setOrderStatusAfterEbsResp(ebsModel, orderStatusResponse, audit);
				}
			}
		}
	}

	/**
	 * This sets the order status based on response from EBS
	 *
	 * @param ebsModel
	 * @param orderStatusResponse
	 * @param audit
	 */
	private void setOrderStatusAfterEbsResp(final JuspayEBSResponseModel ebsModel,
			final GetOrderStatusResponse orderStatusResponse, final MplPaymentAuditModel audit)
	{
		boolean errorFlag = false;
		if (!ebsModel.getEbsRiskStatus().toString().equalsIgnoreCase(orderStatusResponse.getRiskResponse().getStatus().toString())
				&& orderStatusResponse.getRiskResponse().getEbsPaymentStatus().toString()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.EBS_STATUS_APPROVED))
		{
			final OrderModel oModel = fetchOrderDetails(audit);
			if (null != oModel)
			{
				getOrderStatusSpecifier().setOrderStatus(oModel, OrderStatus.PAYMENT_SUCCESSFUL);

				try
				{
					//send Notification
					getRMSVerificationNotificationService().sendRMSNotification(oModel);
				}
				catch (final Exception e1)
				{
					LOG.error("Exception during sending Notification for RMS_VERIFICATION_APPROVED>>> ", e1);
				}

				errorFlag = initiateProcess(oModel);
				if (!errorFlag)
				{
					audit.setIsExpired(Boolean.TRUE);
					LOG.debug("Saving Audit Data with Completed Status");
					createAuditEntry(audit, true);
				}
			}

		}
		else if (!ebsModel.getEbsRiskStatus().toString()
				.equalsIgnoreCase(orderStatusResponse.getRiskResponse().getStatus().toString())
				&& orderStatusResponse.getRiskResponse().getEbsPaymentStatus().toString()
						.equalsIgnoreCase(MarketplacecommerceservicesConstants.EBS_STATUS_REJECTED))
		{
			final OrderModel oModel = fetchOrderDetails(audit);
			if (null != oModel)
			{
				getOrderStatusSpecifier().setOrderStatus(oModel, OrderStatus.RMS_VERIFICATION_FAILED);
				try
				{
					//send Notification
					getRMSVerificationNotificationService().sendRMSNotification(oModel);
				}
				catch (final Exception e1)
				{
					LOG.error("Exception during sending Notification for RMS_VERIFICATION_FAILED>>> ", e1);
				}


				errorFlag = initiateProcess(oModel);
				updateAudit(errorFlag, audit);
				//				if (!errorFlag)
				//				{
				//					audit.setIsExpired(Boolean.TRUE);
				//					LOG.debug("Saving Audit Data with EBS Declined Status");
				//					createAuditEntry(audit, false);
				//				}

			}
		}
	}


	/**
	 * Update auditModel based on errorFlag
	 *
	 * @param errorFlag
	 * @param audit
	 */
	private void updateAudit(final boolean errorFlag, final MplPaymentAuditModel audit)
	{
		if (!errorFlag)
		{
			audit.setIsExpired(Boolean.TRUE);
			LOG.debug("Saving Audit Data with EBS Declined Status");
			createAuditEntry(audit, false);
		}
	}

	/**
	 * @return the rMSVerificationNotificationService
	 */
	public RMSVerificationNotificationService getRMSVerificationNotificationService()
	{
		return rMSVerificationNotificationService;
	}

	/**
	 * @Description : Fetch Order Details based on GUID
	 * @param: guid
	 */
	@Override
	public OrderModel fetchOrderOnGUID(final String guid)
	{
		return juspayWebHookDao.fetchOrderOnGUID(guid);
	}


	/**
	 * @Description : Initiate Action based on status
	 * @param: oModel
	 */
	@Override
	public boolean initiateProcess(final OrderModel oModel)
	{
		boolean errorFlag = true;
		try
		{
			final Collection<OrderProcessModel> ops = oModel.getOrderProcess();
			for (final OrderProcessModel op : ops)
			{
				LOG.debug(" order state ====> " + op.getState());
				if (StringUtils.isNotEmpty(op.getProcessDefinitionName())
						&& op.getProcessDefinitionName().equalsIgnoreCase("mpl-oms-submitorder-process")
						&& ProcessState.WAITING.equals(op.getState()) && null != op.getOrder()
						&& StringUtils.isNotEmpty(op.getOrder().getCode()))
				{
					LOG.debug(" inside if .. triffering review decision event " + op.getOrder().getCode());
					businessProcessService.triggerEvent(op.getOrder().getCode() + "_ReviewDecision");
					errorFlag = false;
					break;
				}
			}
		}
		catch (final Exception exception)
		{
			errorFlag = true;
			LOG.error(exception.getMessage());
		}
		return errorFlag;
	}

	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
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
	 * @param audit
	 * @Description : Fetch Order Details Based on GUID
	 * @param:
	 */
	private OrderModel fetchOrderDetails(final MplPaymentAuditModel audit)
	{
		OrderModel oModel = null;
		try
		{
			if (null != audit.getCartGUID())
			{
				oModel = fetchOrderOnGUID(audit.getCartGUID());
			}
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.debug(exception.getMessage());
		}

		return oModel;
	}

	/**
	 * @Description :Create Audit Entry
	 * @param: audit
	 */
	@Override
	public void createAuditEntry(final MplPaymentAuditModel audit, final boolean isCompleted)
	{
		List<MplPaymentAuditEntryModel> entryList = null;
		final MplPaymentAuditEntryModel oModel = modelService.create(MplPaymentAuditEntryModel.class);

		try
		{
			if (null != audit && null != audit.getAuditEntries() && !audit.getAuditEntries().isEmpty())
			{
				entryList = new ArrayList<MplPaymentAuditEntryModel>(audit.getAuditEntries());
			}
			else
			{
				entryList = new ArrayList<MplPaymentAuditEntryModel>();
			}

			if (null != audit.getAuditId())
			{
				oModel.setAuditId(audit.getAuditId());
			}

			oModel.setResponseDate(new Date());
			if (isCompleted)
			{
				oModel.setStatus(MplPaymentAuditStatusEnum.COMPLETED);
			}
			else
			{
				oModel.setStatus(MplPaymentAuditStatusEnum.EBS_DECLINED);
			}
			entryList.add(oModel);
			audit.setAuditEntries(entryList);
			modelService.save(oModel);
			modelService.save(audit);
		}
		catch (final ModelSavingException exception)
		{
			LOG.error(exception.getMessage());
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getMessage());
		}
	}

}
