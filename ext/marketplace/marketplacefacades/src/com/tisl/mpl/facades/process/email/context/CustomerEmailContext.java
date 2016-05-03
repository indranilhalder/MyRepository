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
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * Velocity context for a customer email.
 */
public class CustomerEmailContext extends AbstractEmailContext<StoreFrontCustomerProcessModel>
{
	private Converter<UserModel, CustomerData> customerConverter;
	private CustomerData customerData;
	private static final String CUSTOMER_ID = "customerId";
	private static final String CUSTOMER_PASSWORD = "customerPassword";
	private static final String IS_CUSTOMER_CREATED_IN_CSCOCKPIT = "customerCreatedInCscockpit";

	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";
	private static final String WEBSITE_URL = "websiteUrl";

	@Autowired
	private ConfigurationService configurationService;

	private static final Logger LOG = Logger.getLogger(CustomerEmailContext.class);


	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		try
		{
			super.init(storeFrontCustomerProcessModel, emailPageModel);
			customerData = getCustomerConverter().convert(getCustomer(storeFrontCustomerProcessModel));
			final CustomerModel customer = getCustomer(storeFrontCustomerProcessModel);
			final String displayName = customer.getDisplayName();
			if (StringUtils.isEmpty(displayName))
			{
				put(DISPLAY_NAME, "There");
			}
			put(EMAIL, storeFrontCustomerProcessModel.getCustomer().getOriginalUid());
			//put(DISPLAY_NAME, customerModel.getDisplayName());
			if (getCustomer(storeFrontCustomerProcessModel) != null)
			{
				put(CUSTOMER_ID, customer.getOriginalUid());
			}
			if (null != getCustomer(storeFrontCustomerProcessModel)
					&& null != getCustomer(storeFrontCustomerProcessModel).getIsCustomerCreatedInCScockpit()
					&& getCustomer(storeFrontCustomerProcessModel).getIsCustomerCreatedInCScockpit().booleanValue())
			{
				put(IS_CUSTOMER_CREATED_IN_CSCOCKPIT, Boolean.TRUE);
				put(CUSTOMER_PASSWORD, storeFrontCustomerProcessModel.getPassword());
			}
			/* TISPRD-1504 Email Issue Fix */
			String websiteUrl = null;
			websiteUrl = getConfigurationService().getConfiguration().getString(
					MarketplacecommerceservicesConstants.SMS_SERVICE_WEBSITE_URL);
			if (null != websiteUrl)
			{
				put(WEBSITE_URL, websiteUrl);
			}

			final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
					"1800-208-8282");
			put(CUSTOMER_CARE_NUMBER, customerCareNumber);


			final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail",
					"hello@tatacliq.com");
			put(CUSTOMER_CARE_EMAIL, customerCareEmail);

		}
		catch (final Exception e)
		{
			LOG.info("Exception is" + e);
		}

	}

	@Override
	protected BaseSiteModel getSite(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel)
	{
		return storeFrontCustomerProcessModel.getSite();
	}

	@Override
	protected CustomerModel getCustomer(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel)
	{
		return storeFrontCustomerProcessModel.getCustomer();
	}

	protected Converter<UserModel, CustomerData> getCustomerConverter()
	{
		return customerConverter;
	}

	@Required
	public void setCustomerConverter(final Converter<UserModel, CustomerData> customerConverter)
	{
		this.customerConverter = customerConverter;
	}

	public CustomerData getCustomer()
	{
		return customerData;
	}


	@Override
	protected LanguageModel getEmailLanguage(final StoreFrontCustomerProcessModel businessProcessModel)
	{
		return businessProcessModel.getLanguage();
	}


}
