/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.NotifyPaymentGroupProcessModel;


/**
 * @author TCS
 *
 */
public class HighRiskNotificationEmailContext extends AbstractEmailContext<NotifyPaymentGroupProcessModel>
{
	private static final String JUSPAYORDERID = "juspayOrderId";

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
