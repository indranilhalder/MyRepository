/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


/**
 * @author TCS
 *
 */
public class ChangePasswordEmailContext extends AbstractEmailContext<StoreFrontCustomerProcessModel>
{

	private static final String CUSTOMER_ID = "customerId";
	private static final String DATE_UPDATE = "dateOfUpdate";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#init(de.hybris.platform.
	 * processengine.model.BusinessProcessModel, de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel)
	 */
	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		final CustomerModel customer = getCustomer(storeFrontCustomerProcessModel);
		final String displayName = customer.getDisplayName();
		if (StringUtils.isEmpty(displayName))
		{
			put(DISPLAY_NAME, "There");
		}
		put(EMAIL, storeFrontCustomerProcessModel.getCustomer().getOriginalUid());
		if (null != getCustomer(storeFrontCustomerProcessModel))
		{
			put(CUSTOMER_ID, customer.getOriginalUid());
		}
		put(EMAIL, storeFrontCustomerProcessModel.getCustomer().getOriginalUid());
		final DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy  hh:mm a");
		final Date date = new Date();
		final String UpdateDate = dateFormat.format(date);

		put(DATE_UPDATE, UpdateDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getSite(de.hybris.platform.
	 * processengine.model.BusinessProcessModel)
	 */
	@Override
	protected BaseSiteModel getSite(final StoreFrontCustomerProcessModel businessProcessModel)
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
	protected CustomerModel getCustomer(final StoreFrontCustomerProcessModel businessProcessModel)
	{
		return businessProcessModel.getCustomer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext#getEmailLanguage(de.hybris.platform
	 * .processengine.model.BusinessProcessModel)
	 */
	@Override
	protected LanguageModel getEmailLanguage(final StoreFrontCustomerProcessModel businessProcessModel)
	{
		return businessProcessModel.getLanguage();
	}

}
