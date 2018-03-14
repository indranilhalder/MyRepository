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
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.model.process.ForgottenPasswordProcessModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Velocity context for a forgotten password email.
 */
public class ForgottenPasswordEmailContext extends CustomerEmailContext
{
	private static final Logger LOG = Logger.getLogger(ForgottenPasswordEmailContext.class);//sonar fix
	private int expiresInMinutes = 30;
	private String token;
	public static final String SECURE_RESET_PASSWORD_URL = "secureResetPasswordUrl";

	public static final String IS_OTP = "isOtp";

	public static final String FORGOT_PWD_OTP = "forgotPasswordOtp";

	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";
	//TISTE-197 starts
	private CustomerData customerData;
	//TISTE-197 ends

	@Autowired
	private ConfigurationService configurationService;

	public int getExpiresInMinutes()
	{
		return expiresInMinutes;
	}

	public void setExpiresInMinutes(final int expiresInMinutes)
	{
		this.expiresInMinutes = expiresInMinutes;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(final String token)
	{
		this.token = token;
	}

	public String getURLEncodedToken() throws UnsupportedEncodingException
	{
		return URLEncoder.encode(token, "UTF-8");
	}



	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		//TISTE-197 starts
		customerData = getCustomerConverter().convert(getCustomer(storeFrontCustomerProcessModel));
		LOG.debug("customer data ::: " + customerData);
		final CustomerModel customer = getCustomer(storeFrontCustomerProcessModel);
		final String displayName = customer.getFirstName();
		if (displayName != null && StringUtils.isNotEmpty(displayName))
		{
			put(DISPLAY_NAME, displayName);
		}
		//TISTE-197 ends
		if (storeFrontCustomerProcessModel instanceof ForgottenPasswordProcessModel)
		{
			setToken(((ForgottenPasswordProcessModel) storeFrontCustomerProcessModel).getToken());
			//final String uniqueID = storeFrontCustomerProcessModel.getCustomer().getUid();
			//final String token = ((ForgottenPasswordProcessModel) storeFrontCustomerProcessModel).getToken();
			final String secureResetPasswordUrl = ((ForgottenPasswordProcessModel) storeFrontCustomerProcessModel)
					.getForgetPasswordUrl();
			put(SECURE_RESET_PASSWORD_URL, secureResetPasswordUrl);

			final String isOtp = ((ForgottenPasswordProcessModel) storeFrontCustomerProcessModel).getIsOtp();

			put(IS_OTP, isOtp);

			final String forgotPasswordOtp = ((ForgottenPasswordProcessModel) storeFrontCustomerProcessModel).getForgetPasswordOtp();

			put(FORGOT_PWD_OTP, forgotPasswordOtp);

		}

		final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
				"1800-208-8282");
		put(CUSTOMER_CARE_NUMBER, customerCareNumber);


		final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail", "hello@tatacliq.com");
		put(CUSTOMER_CARE_EMAIL, customerCareEmail);
	}
}
