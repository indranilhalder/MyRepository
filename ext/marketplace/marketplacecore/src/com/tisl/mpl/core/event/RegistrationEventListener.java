/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.core.event;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.event.MplRegisterEvent;
import com.tisl.mpl.service.MplCustomerWebService;


/**
 * Listener for customer registration events.
 */
public class RegistrationEventListener extends AbstractSiteEventListener<MplRegisterEvent>
{

	private ModelService modelService;
	private BusinessProcessService businessProcessService;
	private UserService userService;
	@Autowired
	private MplCustomerWebService mplCustomerWebService;

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Override
	protected void onSiteEvent(final MplRegisterEvent registerEvent)
	{
		StoreFrontCustomerProcessModel storeFrontCustomerProcessModel = null;


		if (registerEvent.getCustomer().getIsCustomerCreatedInCScockpit().booleanValue())
		{
			final Random randomGenerator = new Random();
			final String password = String.valueOf(randomGenerator.nextInt(1000000));
			storeFrontCustomerProcessModel = (StoreFrontCustomerProcessModel) businessProcessService.createProcess(
					"customerRegistrationInCsEmailProcess-" + registerEvent.getCustomer().getUid() + "-" + System.currentTimeMillis(),
					"customerRegistrationInCsEmailProcess");
			storeFrontCustomerProcessModel.setPassword(password);
			userService.setPassword(registerEvent.getCustomer(), password, registerEvent.getCustomer().getPasswordEncoding());
			getModelService().save(registerEvent.getCustomer());
		}
		else
		{
			storeFrontCustomerProcessModel = (StoreFrontCustomerProcessModel) getBusinessProcessService().createProcess(
					"customerRegistrationEmailProcess-" + registerEvent.getCustomer().getUid() + "-" + System.currentTimeMillis(),
					"customerRegistrationEmailProcess");
		}
		storeFrontCustomerProcessModel.setSite(registerEvent.getSite());
		storeFrontCustomerProcessModel.setCustomer(registerEvent.getCustomer());
		storeFrontCustomerProcessModel.setLanguage(registerEvent.getLanguage());
		storeFrontCustomerProcessModel.setCurrency(registerEvent.getCurrency());
		storeFrontCustomerProcessModel.setStore(registerEvent.getBaseStore());
		getModelService().save(storeFrontCustomerProcessModel);
		getBusinessProcessService().startProcess(storeFrontCustomerProcessModel);
		mplCustomerWebService.customerModeltoWsData(registerEvent.getCustomer(),
				MarketplacecommerceservicesConstants.NEW_CUSTOMER_CREATE_FLAG, false);
	}

	@Override
	protected boolean shouldHandleEvent(final MplRegisterEvent event)
	{
		final BaseSiteModel site = event.getSite();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
		return SiteChannel.B2C.equals(site.getChannel());
	}
}
