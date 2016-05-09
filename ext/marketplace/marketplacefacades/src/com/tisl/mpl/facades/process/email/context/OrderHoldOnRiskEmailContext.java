/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class OrderHoldOnRiskEmailContext extends AbstractEmailContext<OrderProcessModel>
{

	private static final String CUSTOMER_NAME = "customerName";
	private static final String CUSTOMER = "Customer";
	private static final String ORDER_REFERENCE_NUMBER = "orderReferenceNumber";
	private static final String ORDER_DATE = "orderDate";
	private static final String CUSTOMER_FIRST_PAGE = "customerFirstPage";
	private static final String CONTACT_US_LINK = "contactUsLink";

	private static final String CUSTOMER_CARE_NUMBER = "customerCareNumber";
	private static final String CUSTOMER_CARE_EMAIL = "customerCareEmail";


	@Autowired
	private ConfigurationService configurationService;

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		final AddressModel deliveryAddress = orderProcessModel.getOrder().getDeliveryAddress();
		final String customerFirstPage = configurationService.getConfiguration().getString("website.hybris.http");
		//final String customerFirstPage = Localization.getLocalizedString("website.hybris.http");
		put(CUSTOMER_FIRST_PAGE, customerFirstPage);
		//final String contactUsLink = Localization.getLocalizedString("marketplace.contactus.link");
		final String contactUsLink = configurationService.getConfiguration().getString("marketplace.contactus.link");
		if (deliveryAddress != null)
		{
			put(DISPLAY_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));
		}
		else
		{
			put(DISPLAY_NAME, CUSTOMER);
		}
		put(CONTACT_US_LINK, contactUsLink);
		if (null != orderProcessModel.getOrder())
		{
			final OrderModel order = orderProcessModel.getOrder();
			put(ORDER_REFERENCE_NUMBER, order.getCode());
			put(ORDER_DATE, order.getDate());
			final CustomerModel customer = (CustomerModel) order.getUser();
			put(EMAIL, customer.getOriginalUid());
			if (deliveryAddress != null)
			{
				put(CUSTOMER_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));
			}
			else
			{
				put(CUSTOMER_NAME, CUSTOMER);
			}
			/*
			 * if (null != customer.getFirstName()) { put(CUSTOMER_NAME, customer.getFirstName()); } else {
			 * put(CUSTOMER_NAME, "Customer"); }
			 */

		}

		final String customerCareNumber = configurationService.getConfiguration().getString("marketplace.sms.service.contactno",
				"1800-208-8282");
		put(CUSTOMER_CARE_NUMBER, customerCareNumber);


		final String customerCareEmail = configurationService.getConfiguration().getString("cliq.care.mail", "hello@tatacliq.com");
		put(CUSTOMER_CARE_EMAIL, customerCareEmail);

	}

	@Override
	protected BaseSiteModel getSite(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getSite();
	}


	@Override
	protected CustomerModel getCustomer(final OrderProcessModel orderProcessModel)
	{
		return (CustomerModel) orderProcessModel.getOrder().getUser();
	}


	@Override
	protected LanguageModel getEmailLanguage(final OrderProcessModel orderProcessModel)
	{
		return orderProcessModel.getOrder().getLanguage();
	}




}

