package com.tisl.mpl.marketplacecommerceservices.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerEmailResolutionService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.util.mail.MailUtils;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;


/**
 * The Class ExtendedCustomerEmailResolutionService - Service interface used to lookup/validate the contact email
 * address for a customer.
 * 
 * @author 314180
 */
public class ExtendedCustomerEmailResolutionService extends DefaultCustomerEmailResolutionService
{

	private static final Logger LOG = Logger.getLogger(DefaultCustomerEmailResolutionService.class);

	/** The Constant DEFAULT_CUSTOMER_KEY. */
	public static final String DEFAULT_CUSTOMER_KEY = "customer.email.default";

	/** The Constant DEFAULT_CUSTOMER_EMAIL. */
	public static final String DEFAULT_CUSTOMER_EMAIL = "demo@example.com";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.customer.impl.DefaultCustomerEmailResolutionService#getEmailForCustomer(de
	 * .hybris.platform.core.model.user.CustomerModel)
	 */
	@Override
	public String getEmailForCustomer(final CustomerModel customerModel)
	{
		if (isEmailValidForCustomer(customerModel))
		{
			return customerModel.getOriginalUid();
		}

		return getConfigurationService().getConfiguration().getString(DEFAULT_CUSTOMER_KEY, DEFAULT_CUSTOMER_EMAIL);
	}

	private boolean isEmailValidForCustomer(final CustomerModel customerModel)
	{
		validateParameterNotNullStandardMessage("customerModel", customerModel);

		final String email = customerModel.getOriginalUid();
		try
		{
			MailUtils.validateEmailAddress(email, "customer email");
			return true;
		}
		catch (final EmailException e)
		{
			LOG.info("Given uid is not appropriate email " + email);
		}
		return false;
	}
}
