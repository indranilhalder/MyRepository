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

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.NotifyPaymentGroupProcessModel;


/**
 * @author TCS
 *
 */
public class HighRiskNotificationEmailContext extends AbstractEmailContext<NotifyPaymentGroupProcessModel>
{
	private static final String JUSPAYORDERID = "juspayOrderId";

	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public void init(final NotifyPaymentGroupProcessModel notifyPaymentGroupProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(notifyPaymentGroupProcessModel, emailPageModel);

		if (null != notifyPaymentGroupProcessModel.getJuspayOrderId())
		{
			put(JUSPAYORDERID, notifyPaymentGroupProcessModel.getJuspayOrderId());
		}
		else
		{
			put(JUSPAYORDERID, MarketplacecommerceservicesConstants.EMPTYSTRING);
		}

		put(EMAIL, notifyPaymentGroupProcessModel.getRecipientEmail());
		put(DISPLAY_NAME, notifyPaymentGroupProcessModel.getRecipientEmail());

		final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
				"1800-208-8282");
		put(CUSTOMER_CARE_NUMBER, customerCareNumber);


		final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail", "hello@tatacliq.com");
		put(CUSTOMER_CARE_EMAIL, customerCareEmail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getSite(de.hybris.platform.
	 * processengine.model.BusinessProcessModel)
	 */
	@Override
	protected BaseSiteModel getSite(final NotifyPaymentGroupProcessModel businessProcessModel)
	{
		return businessProcessModel.getSite();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getCustomer(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected CustomerModel getCustomer(final NotifyPaymentGroupProcessModel businessProcessModel)
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getEmailLanguage(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected LanguageModel getEmailLanguage(final NotifyPaymentGroupProcessModel businessProcessModel)
	{
		return businessProcessModel.getSite().getDefaultLanguage();
	}

}
