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

import com.tisl.mpl.core.model.SubmitRequestProcessModel;


/**
 * @author 765463
 *
 */
public class SubmitRequestEmailContext extends AbstractEmailContext<SubmitRequestProcessModel>
{

	private static final String CUSTOMER_MESSAGE = "customerMessage";
	private static final String ORDER_CODE = "orderCode";
	private static final String FILE_NAME = "fileName";
	private static final String ISSUE_DETAILS = "issueDetails";
	private static final String ISSUE_TYPE = "issueType";

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public void init(final SubmitRequestProcessModel submitRequestProcessModel, final EmailPageModel emailPageModel)
	{

		final String customerCareEmail = configurationService.getConfiguration().getString("contact.customercare.email",
				"customerservices@tataunistore.com");

		final String customerMessage = submitRequestProcessModel.getMessage();
		final String orderCode = submitRequestProcessModel.getOrderCode();


		final String customerEmailId = submitRequestProcessModel.getCustomerEmailId();

		final String issueDetails = submitRequestProcessModel.getIssueDetails();

		final String issueType = submitRequestProcessModel.getIssueType();
		put(ISSUE_DETAILS, issueDetails);
		put(EMAIL, customerCareEmail);
		put(ISSUE_TYPE, issueType);
		put(CUSTOMER_MESSAGE, customerMessage);
		put(ORDER_CODE, orderCode);
		put(FROM_EMAIL, customerEmailId);
		put(FROM_DISPLAY_NAME, customerEmailId);
		put(DISPLAY_NAME, "Customer Service");

		if (submitRequestProcessModel.getAttachedFileName() != null)
		{
			put(FILE_NAME, submitRequestProcessModel.getAttachedFileName());
		}

	}

	@Override
	protected BaseSiteModel getSite(final SubmitRequestProcessModel submitRequestProcessModel)
	{
		// YTODO Auto-generated method stub
		return submitRequestProcessModel.getSite();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getCustomer(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected CustomerModel getCustomer(final SubmitRequestProcessModel submitRequestProcessModel)
	{
		// YTODO Auto-generated method stub
		if (submitRequestProcessModel.getCustomer() == null)
		{
			return userService.getAnonymousUser();
		}
		else
		{
			return submitRequestProcessModel.getCustomer();
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
	protected LanguageModel getEmailLanguage(final SubmitRequestProcessModel submitRequestProcessModel)
	{
		// YTODO Auto-generated method stub
		return submitRequestProcessModel.getLanguage();
	}



}
