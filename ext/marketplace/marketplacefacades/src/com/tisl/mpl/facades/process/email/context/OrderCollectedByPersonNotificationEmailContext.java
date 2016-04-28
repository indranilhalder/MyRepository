/**
 *
 */
package com.tisl.mpl.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;


/**
 * @author Dileep
 *
 */
public class OrderCollectedByPersonNotificationEmailContext extends AbstractEmailContext<OrderProcessModel>
{

	private static final String ORDERCODE = "orderCode";
	private static final String CUSTOMER = "Customer";
	private static final String PICKUP_PERSON_NAME = "pickupPerson";
	private static final String PICKUP_PERSON_NUMBER = "pickupPersonNo";
	private static final String STORE_NAME = "storeName";
	private static final String CUSTOMER_NAME = "customerName";
	private static final String TIME = "time";
	private static final Logger LOG = Logger.getLogger(OrderCollectedByPersonNotificationEmailContext.class);

	@Override
	public void init(final OrderProcessModel orderProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(orderProcessModel, emailPageModel);
		final AddressModel deliveryAddress = orderProcessModel.getOrder().getDeliveryAddress();
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final Calendar cal = Calendar.getInstance();
		put(TIME, dateFormat.format(cal.getTime()));
		LOG.info("Order Collected By Nominal Person Email Context Class");
		put(ORDERCODE, orderProcessModel.getOrder().getCode());
		LOG.debug("Order Colletcted By Nominal Person Email Context ");
		put(PICKUP_PERSON_NAME, orderProcessModel.getOrder().getPickupPersonName());
		put(PICKUP_PERSON_NUMBER, orderProcessModel.getOrder().getPickupPersonMobile());
		for (final AbstractOrderEntryModel entry : orderProcessModel.getOrder().getEntries())
		{
			final String storeName = entry.getDeliveryPointOfService().getDisplayName();
			put(STORE_NAME, storeName);
		}
		final CustomerModel customer = (CustomerModel) orderProcessModel.getOrder().getUser();
		put(EMAIL, customer.getOriginalUid());
		if (deliveryAddress != null)
		{
			put(CUSTOMER_NAME, (null != deliveryAddress.getFirstname() ? deliveryAddress.getFirstname() : CUSTOMER));
		}
		else
		{
			put(CUSTOMER_NAME, CUSTOMER);
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