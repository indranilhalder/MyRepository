/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.NotifyPaymentGroupProcessModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedEmployeeService;
import com.tisl.mpl.marketplacecommerceservices.service.NotifyPaymentGroupMailService;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class DefaultNotifyPaymentGroupMailService implements NotifyPaymentGroupMailService
{
	@Resource
	private ExtendedEmployeeService extendedEmployeeService;
	@Autowired
	private BusinessProcessService businessProcessService;
	@Autowired
	private ModelService modelService;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(DefaultNotifyPaymentGroupMailService.class.getName());

	/**
	 * @Description: For sending mail to Payment user group when an order is put on HOLD
	 * @param juspayOrderId
	 */
	@Override
	public void sendMail(final String juspayOrderId)
	{
		final List<String> recipientEmailList = formRecipientList();

		for (final String recipientEmail : recipientEmailList)
		{
			final NotifyPaymentGroupProcessModel notifyPaymentGroupProcessModel = (NotifyPaymentGroupProcessModel) getBusinessProcessService()
					.createProcess("notifyPaymentGroupProcess_" + System.currentTimeMillis(), "notifyPaymentGroupProcess");

			try
			{
				if (null != juspayOrderId)
				{
					notifyPaymentGroupProcessModel.setJuspayOrderId(juspayOrderId);
				}

				notifyPaymentGroupProcessModel.setRecipientEmail(recipientEmail);
				if (getBaseSiteService().getBaseSiteForUID(MarketplacecommerceservicesConstants.MPL) != null)
				{
					notifyPaymentGroupProcessModel.setSite(getBaseSiteService().getBaseSiteForUID(
							MarketplacecommerceservicesConstants.MPL));
				}
				getModelService().save(notifyPaymentGroupProcessModel);
				getBusinessProcessService().startProcess(notifyPaymentGroupProcessModel);
				LOG.debug("******Sending Mail For User::::::" + recipientEmail + "****************");
			}
			catch (final EtailBusinessExceptions e)
			{
				ExceptionUtil.etailBusinessExceptionHandler(e, null);
			}
			catch (final EtailNonBusinessExceptions e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(e);
			}
			catch (final Exception e)
			{
				ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
			}
		}
	}

	/**
	 *
	 * @return List<String>
	 */
	private List<String> formRecipientList()
	{
		List<PrincipalGroupModel> groupList = null;
		final List<String> recipientEmailList = new ArrayList<String>();
		final List<EmployeeModel> employeeList = getExtendedEmployeeService().getAllUsers();

		for (final EmployeeModel employee : employeeList)
		{
			groupList = new ArrayList<PrincipalGroupModel>(employee.getAllGroups());

			for (final PrincipalGroupModel group : groupList)
			{
				if (group instanceof UserGroupModel
						&& group.getUid().equalsIgnoreCase(MarketplacecommerceservicesConstants.PAYMENTUSERGROUP))
				{
					LOG.debug("Adding employee  " + employee.getUid() + "  to the recepient list");
					if (employee.getEmail() != null)
					{
						recipientEmailList.add(employee.getEmail());
					}
				}
			}
		}
		return recipientEmailList;
	}

	/**
	 *
	 * @param juspayOrderId
	 * @param tat
	 */
	@Override
	public boolean sendMailToTakeAction(final String juspayOrderId, final Double tat)
	{
		final List<String> recipientEmailList = formRecipientList();
		boolean isSuccessful = false;

		if (CollectionUtils.isNotEmpty(recipientEmailList))
		{
			for (final String recipientEmail : recipientEmailList)
			{
				final NotifyPaymentGroupProcessModel notifyPaymentGroupProcessModel = (NotifyPaymentGroupProcessModel) getBusinessProcessService()
						.createProcess("takeActionOnHeldOrderProcess_" + System.currentTimeMillis(), "takeActionOnHeldOrderProcess");

				try
				{
					notifyPaymentGroupProcessModel.setRecipientEmail(recipientEmail);

					if (null != juspayOrderId)
					{
						notifyPaymentGroupProcessModel.setJuspayOrderId(juspayOrderId);
					}
					if (null != tat)
					{
						notifyPaymentGroupProcessModel.setTatRemaining(tat);
					}

					if (getBaseSiteService().getBaseSiteForUID(MarketplacecommerceservicesConstants.MPL) != null)
					{
						notifyPaymentGroupProcessModel.setSite(getBaseSiteService().getBaseSiteForUID(
								MarketplacecommerceservicesConstants.MPL));
					}

					getModelService().save(notifyPaymentGroupProcessModel);
					getBusinessProcessService().startProcess(notifyPaymentGroupProcessModel);
					LOG.debug("******Sending Mail For User::::::" + recipientEmail + "****************");

					isSuccessful = true;
				}
				catch (final EtailBusinessExceptions e)
				{
					ExceptionUtil.etailBusinessExceptionHandler(e, null);
				}
				catch (final EtailNonBusinessExceptions e)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(e);
				}
				catch (final Exception e)
				{
					ExceptionUtil.etailNonBusinessExceptionHandler(new EtailNonBusinessExceptions(e));
				}
			}
		}

		return isSuccessful;
	}

	/**
	 *
	 * @param orderId
	 */
	@Override
	public List<NotifyPaymentGroupProcessModel> checkOrderIdForSentMail(final String orderId)
	{
		final NotifyPaymentGroupProcessModel processModel = getModelService().create(NotifyPaymentGroupProcessModel.class);
		processModel.setJuspayOrderId(orderId);
		processModel.setProcessDefinitionName("takeActionOnHeldOrderProcess");
		final List<NotifyPaymentGroupProcessModel> processList = flexibleSearchService.getModelsByExample(processModel);

		return processList;
	}

	/**
	 * @return the extendedEmployeeService
	 */
	public ExtendedEmployeeService getExtendedEmployeeService()
	{
		return extendedEmployeeService;
	}

	/**
	 * @param extendedEmployeeService
	 *           the extendedEmployeeService to set
	 */
	public void setExtendedEmployeeService(final ExtendedEmployeeService extendedEmployeeService)
	{
		this.extendedEmployeeService = extendedEmployeeService;
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
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

}
