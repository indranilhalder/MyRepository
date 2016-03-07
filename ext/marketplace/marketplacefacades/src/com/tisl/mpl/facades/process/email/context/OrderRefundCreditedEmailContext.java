/**
 * 
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;



/**
 * @author Dileep
 *
 */
public class OrderRefundCreditedEmailContext extends AbstractEmailContext<OrderProcessModel>
{

	private static final String ORDERCODE = "orderCode";

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		put(ORDERCODE, orderProcessModel.getOrder().getCode());

		final CustomerModel customer = (CustomerModel) orderProcessModel.getOrder().getUser();
		put(EMAIL, customer.getOriginalUid());
	}



	@Override
	protected BaseSiteModel getSite(OrderProcessModel businessProcessModel)
	{
		return businessProcessModel.getOrder().getSite();
	}


	@Override
	protected CustomerModel getCustomer(OrderProcessModel businessProcessModel)
	{
		return (CustomerModel) businessProcessModel.getOrder().getUser();
	}


	@Override
	protected LanguageModel getEmailLanguage(OrderProcessModel businessProcessModel)
	{
		return businessProcessModel.getOrder().getLanguage();
	}



}
