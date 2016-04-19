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
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.returns.model.RefundEntryModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;



/**
 * @author Techouts
 *
 */
public class OrderRefundCreditedEmailContext extends AbstractEmailContext<OrderProcessModel>
{

	private static final String ORDERCODE = "orderCode";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String CUSTOMER = "Customer";
	private static final String TOTAL = "refundAomunt";
	private static final String REFUND_ENTRY = "refundEntryModel";
	private static final Logger LOG = Logger.getLogger(OrderRefundCreditedEmailContext.class);

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{

		LOG.debug("Refund Email  Init method called");

		super.init(orderProcessModel, emailPageModel);
		final List<RefundEntryModel> refundEntryModel = new ArrayList<RefundEntryModel>();
		final OrderModel orderModel = orderProcessModel.getOrder();
		final CustomerModel customer = (CustomerModel) orderProcessModel.getOrder().getUser();
		final AddressModel address = orderModel.getDeliveryAddress();
		double totalAmountValue = 0.0;
		
			LOG.debug("Refund Order Id  " + orderProcessModel.getOrder().getCode());
			for (final AbstractOrderEntryModel entryModel : orderProcessModel.getOrder().getEntries())
			{
				LOG.debug("Refund Entries transactionId :" + entryModel.getOrderLineId());
				final RefundEntryModel refundEntry = new RefundEntryModel();
				refundEntry.setOrderEntry(entryModel);
					try
					{
						totalAmountValue += entryModel.getNetAmountAfterAllDisc().doubleValue();
					}
					catch (final Exception e)
					{
						LOG.error("Exception occurred during refund email notification: " + e);
					}
			}
			put(TOTAL, String.valueOf(totalAmountValue));
   try {
		LOG.debug("Amount refunded ======" + totalAmountValue);
		put(REFUND_ENTRY, refundEntryModel);
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
		put(ORDERCODE, orderProcessModel.getOrder().getCode());
		put(EMAIL, customer.getOriginalUid());
   }catch (Exception e) {
   	LOG.debug("Exception occurred while setting details in context");
   }
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
