/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;



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
	@Autowired
	private FlexibleSearchService flexibleSearchService;
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
		double totalAmountValue = 0;
		for (final AbstractOrderModel subOrder : orderProcessModel.getOrder().getChildOrders())
		{
			LOG.debug("subOrder ID " + subOrder.getCode());
			for (final AbstractOrderEntryModel entryModel : subOrder.getEntries())
			{
				LOG.debug("entryModel :" + entryModel.getOrderLineId());
				final RefundEntryModel refundEntry = new RefundEntryModel();
				refundEntry.setOrderEntry(entryModel);
				if (!CollectionUtils.isEmpty(flexibleSearchService.getModelsByExample(refundEntry)))
				{
					try
					{
						final List<RefundEntryModel> refundList = flexibleSearchService.getModelsByExample(refundEntry);
						totalAmountValue += entryModel.getNetAmountAfterAllDisc().doubleValue();
						refundEntryModel.addAll(refundList);
						LOG.debug("refundEntryModel Size" + refundEntryModel.size());
					}
					catch (final Exception e)
					{
						LOG.error("Exception occurred during refund email notification: " + e);
					}
				}
			}

		}
		LOG.debug("Amount refunded ======" + totalAmountValue);
		put(REFUND_ENTRY, refundEntryModel);
		put(TOTAL, String.valueOf(totalAmountValue));
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