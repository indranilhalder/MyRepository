/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedEmployeeService;
import com.tisl.mpl.marketplacecommerceservices.service.PromotionSendMailService;
import com.tisl.mpl.model.PromotionCreationMailProcessModel;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class DefaultPromotionSendMailService implements PromotionSendMailService
{
	@Autowired
	private ModelService modelService;
	@Autowired
	private BusinessProcessService businessProcessService;
	@Resource
	private UserService userService;
	@Resource
	private ExtendedEmployeeService extendedEmployeeService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	private static final Logger LOG = Logger.getLogger(DefaultPromotionSendMailService.class.getName());

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
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
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

	/**
	 * @Description: For sending mail after promotion creation
	 * @param item
	 */
	@Override
	public void sendMail(final Item item)
	{
		String promoCode = null;
		String promotionType = null;
		Date startDate = null;
		Date endDate = null;
		List<PrincipalGroupModel> groupList = null;
		final List<String> recipientEmailList = new ArrayList<String>();
		final List<EmployeeModel> employeeList = extendedEmployeeService.getAllUsers();

		for (final EmployeeModel employee : employeeList)
		{
			groupList = new ArrayList<PrincipalGroupModel>(employee.getAllGroups());

			for (final PrincipalGroupModel group : groupList)
			{
				if (group instanceof UserGroupModel
						&& group.getUid().equalsIgnoreCase(MarketplacecommerceservicesConstants.PROMOALERTSENTGROUP))
				{
					LOG.info("Adding employee " + employee.getUid() + " to the recepient list");
					if (employee.getEmail() != null)
					{
						recipientEmailList.add(employee.getEmail());
					}
				}
			}
		}

		for (final String recipientEmail : recipientEmailList)
		{
			final PromotionCreationMailProcessModel promotionCreationMailProcessModel = (PromotionCreationMailProcessModel) businessProcessService
					.createProcess("promotionCreationMailProcess_" + System.currentTimeMillis(), "promotionCreationMailProcess");


			final UserModel userModel = userService.getCurrentUser();
			promotionCreationMailProcessModel.setUser(userModel.getName());

			try
			{
				if (null != item.getAttribute(MarketplacecommerceservicesConstants.CODE))
				{
					promoCode = (String) item.getAttribute(MarketplacecommerceservicesConstants.CODE);
					promotionCreationMailProcessModel.setPromoCode(promoCode);
				}

				if (null != item.getAttribute(MarketplacecommerceservicesConstants.PROMOTIONTYPE))
				{
					promotionType = (String) item.getAttribute(MarketplacecommerceservicesConstants.PROMOTIONTYPE);
					promotionCreationMailProcessModel.setPromotionType(promotionType);
				}
				if (null != item.getAttribute(MarketplacecommerceservicesConstants.START_DATE)
						&& null != item.getAttribute(MarketplacecommerceservicesConstants.END_DATE))
				{
					startDate = (Date) item.getAttribute(MarketplacecommerceservicesConstants.START_DATE);
					endDate = (Date) item.getAttribute(MarketplacecommerceservicesConstants.END_DATE);
					promotionCreationMailProcessModel.setStartDate(startDate);
					promotionCreationMailProcessModel.setEndDate(endDate);
				}

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
			promotionCreationMailProcessModel.setRecipientEmail(recipientEmail);
			if (baseSiteService.getBaseSiteForUID(MarketplacecommerceservicesConstants.MPL) != null)
			{
				promotionCreationMailProcessModel
						.setSite(baseSiteService.getBaseSiteForUID(MarketplacecommerceservicesConstants.MPL));
			}
			modelService.save(promotionCreationMailProcessModel);
			businessProcessService.startProcess(promotionCreationMailProcessModel);
			LOG.debug("******Sending Mail For User" + recipientEmail + "****************");
		}


	}
}
