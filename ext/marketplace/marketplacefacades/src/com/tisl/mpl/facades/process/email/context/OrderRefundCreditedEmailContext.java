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



/**
 * @author Dileep
 *
 */
public class OrderRefundCreditedEmailContext extends AbstractEmailContext<OrderProcessModel>
{

	private static final String ORDERCODE = "orderCode";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String CUSTOMER = "Customer";

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		put(ORDERCODE, orderProcessModel.getOrder().getCode());
		final OrderModel orderModel = orderProcessModel.getOrder();
		final AddressModel address = orderModel.getDeliveryAddress();
		if (address != null)
		{
			if (address.getFirstname() != null)
			{
				put(CUSTOMER_NAME, address.getFirstname());
			}
		}
		else
		{
			put(CUSTOMER_NAME, CUSTOMER);
		}
		final CustomerModel customer = (CustomerModel) orderProcessModel.getOrder().getUser();
		put(EMAIL, customer.getOriginalUid());
	}



	@Override
	protected BaseSiteModel getSite(final OrderProcessModel businessProcessModel)
	{
		return businessProcessModel.getOrder().getSite();
	}


	@Override
	protected CustomerModel getCustomer(final OrderProcessModel businessProcessModel)
	{
		return (CustomerModel) businessProcessModel.getOrder().getUser();
	}


	@Override
	protected LanguageModel getEmailLanguage(final OrderProcessModel businessProcessModel)
	{
		return businessProcessModel.getOrder().getLanguage();
	}



}
