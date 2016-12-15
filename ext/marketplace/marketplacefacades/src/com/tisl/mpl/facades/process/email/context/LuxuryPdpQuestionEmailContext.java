/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.LuxuryPdpQuestionProcessModel;


/**
 * @author 1240990
 *
 */
public class LuxuryPdpQuestionEmailContext extends AbstractEmailContext<LuxuryPdpQuestionProcessModel>
{
	private static final String CUSTOMER_MESSAGE = "customerMessage";
	private static final String PRODUCT_CODE = "productCode";
	private static final String FILE_NAME = "fileName";
	private static final String ISSUE_DETAILS = "issueDetails";
	private static final String ISSUE_TYPE = "issueType";
	private static final String EMAIL_TO = "emailTo"; 

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public void init(final LuxuryPdpQuestionProcessModel luxuryPdpQuestionProcessModel, final EmailPageModel emailPageModel)
	{

		final String customerCareEmail = configurationService.getConfiguration().getString("contact.customercare.email",
				"customerservices@tataunistore.com");
		final String customerMessage = luxuryPdpQuestionProcessModel.getMessage();
		final String productCode = luxuryPdpQuestionProcessModel.getProductCode();

		final String customerEmailId = luxuryPdpQuestionProcessModel.getCustomerEmailId();

		final String issueDetails = luxuryPdpQuestionProcessModel.getIssueDetails();

		final String issueType = luxuryPdpQuestionProcessModel.getIssueType();
		
		final String emailTo = luxuryPdpQuestionProcessModel.getEmailTo();
		put(ISSUE_DETAILS, issueDetails);
		put(EMAIL, customerCareEmail);
		put(ISSUE_TYPE, issueType);
		put(CUSTOMER_MESSAGE, customerMessage);
		put(PRODUCT_CODE, productCode);
		put(FROM_EMAIL, customerEmailId);
		put(FROM_DISPLAY_NAME, customerEmailId);
		put(DISPLAY_NAME, "Customer Service");
		put(EMAIL_TO, emailTo);

		if (luxuryPdpQuestionProcessModel.getAttachedFileName() != null)
		{
			put(FILE_NAME, luxuryPdpQuestionProcessModel.getAttachedFileName());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getSite(de.hybris.platform.
	 * processengine.model.BusinessProcessModel)
	 */
	@Override
	protected BaseSiteModel getSite(final LuxuryPdpQuestionProcessModel luxuryPdpQuestionProcessModel)
	{
		return luxuryPdpQuestionProcessModel.getSite();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getCustomer(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected CustomerModel getCustomer(final LuxuryPdpQuestionProcessModel luxuryPdpQuestionProcessModel)
	{
		if (luxuryPdpQuestionProcessModel.getCustomer() == null)
		{
			return userService.getAnonymousUser();
		}
		else
		{
			return luxuryPdpQuestionProcessModel.getCustomer();
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getEmailLanguage(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected LanguageModel getEmailLanguage(final LuxuryPdpQuestionProcessModel luxuryPdpQuestionProcessModel)
	{
		// YTODO Auto-generated method stub
		return luxuryPdpQuestionProcessModel.getLanguage();
	}

}
