/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.SendUpdateProcessModel;


/**
 * @author 314180
 *
 */
public class SendUpdateEmailContext extends CustomerEmailContext
{

	private static final String CUSTOMER_EMAIL = "customerEmail";
	private static final String ENCODED_PASSWORD = "encodedPassword";




	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		{
			super.init(storeFrontCustomerProcessModel, emailPageModel);

			if (storeFrontCustomerProcessModel instanceof SendUpdateProcessModel)
			{
				put(EMAIL, ((SendUpdateProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				put(DISPLAY_NAME, ((SendUpdateProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				put(CUSTOMER_EMAIL, ((SendUpdateProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				final String password = ((SendUpdateProcessModel) storeFrontCustomerProcessModel).getEncodedPassword();
				if (!StringUtils.isBlank(password))
				{
					put(ENCODED_PASSWORD, password);
				}
			}
		}
	}





}
