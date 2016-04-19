/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.SendInvoiceProcessModel;


/**
 * @author TCS
 *
 */
public class SendInvoiceEmailContext extends CustomerEmailContext
{

	private static final String CUSTOMER_EMAIL = "customerEmail";
	private static final String ORDER_CODE = "orderCode";
	private static final String TRANSACTION_ID = "transactionID";
	private static final String INVOICE_URL = "invoiceUrl";
	private static final String WEBSITE_URL = "websiteUrl";

	//	private CustomerData customerData;

	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";

	@Autowired
	private ConfigurationService configurationService;


	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		{
			super.init(storeFrontCustomerProcessModel, emailPageModel);

			//			final CustomerData customerData = getCustomerConverter().convert(getCustomer(storeFrontCustomerProcessModel));
			final CustomerModel customer = getCustomer(storeFrontCustomerProcessModel);
			final String displayName = customer.getDisplayName();
			if (StringUtils.isEmpty(displayName))
			{
				put(DISPLAY_NAME, displayName);
			}

			if (storeFrontCustomerProcessModel instanceof SendInvoiceProcessModel)
			{
				put(EMAIL, ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				//put(DISPLAY_NAME, ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				put(CUSTOMER_EMAIL, ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getCustomerEmail());
				String ordercode = null;
				ordercode = ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getOrderCode();
				if (null != ordercode)
				{
					put(ORDER_CODE, ordercode);

				}

				String transactionID = null;
				transactionID = ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getTransactionId();
				if (null != transactionID)
				{
					put(TRANSACTION_ID, transactionID);

				}

				String invoiceUrl = null;
				invoiceUrl = ((SendInvoiceProcessModel) storeFrontCustomerProcessModel).getInvoiceUrl();
				if (null != invoiceUrl)
				{
					put(INVOICE_URL, invoiceUrl);

				}
				String websiteUrl = null;
				websiteUrl = getConfigurationService().getConfiguration().getString(
						MarketplacecommerceservicesConstants.SMS_SERVICE_WEBSITE_URL);
				if (null != websiteUrl)
				{
					put(WEBSITE_URL, websiteUrl);
				}

			}

			final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
					"1800-208-8282");
			put(CUSTOMER_CARE_NUMBER, customerCareNumber);


			final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail",
					"hello@tatacliq.com");
			put(CUSTOMER_CARE_EMAIL, customerCareEmail);

		}
	}



}
