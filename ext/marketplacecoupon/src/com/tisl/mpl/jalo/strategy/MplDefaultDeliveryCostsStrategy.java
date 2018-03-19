/**
 *
 */
package com.tisl.mpl.jalo.strategy;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryCostsStrategy;
import de.hybris.platform.util.PriceValue;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author TCS
 *
 */
public class MplDefaultDeliveryCostsStrategy implements DeliveryCostsStrategy
{
	private static final Logger LOG = Logger.getLogger(MplDefaultDeliveryCostsStrategy.class);

	@Override
	public PriceValue findDeliveryCosts(final SessionContext ctx, final AbstractOrder order)
	{
		try
		{
			final Double deliveryCost = order.getDeliveryCost();

			if (deliveryCost != null && deliveryCost.doubleValue() != 0.0D)
			{
				return new PriceValue(order.getCurrency().getIsoCode(), deliveryCost.doubleValue(), order.isNet().booleanValue());

			}
			else
			{
				double totalDeliveryCharge = 0.0D;
				for (final AbstractOrderEntry entry : order.getEntries())
				{
					final Double currDelCharge = entry.getProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE) != null ? (Double) entry
							.getProperty(ctx, MarketplacecommerceservicesConstants.CURRENTDELIVERYCHARGE) : null;
					final Double scheduledDeliveryCharge = entry.getProperty(ctx, "scheduledDeliveryCharge") != null ? (Double) entry
							.getProperty(ctx, "scheduledDeliveryCharge") : null;

					totalDeliveryCharge += (((currDelCharge != null && currDelCharge.doubleValue() != 0.0D) ? currDelCharge
							.doubleValue() : 0.0D) + ((scheduledDeliveryCharge != null && scheduledDeliveryCharge.doubleValue() != 0.0D) ? scheduledDeliveryCharge
							.doubleValue() : 0.0D));
				}


				return (totalDeliveryCharge != 0.0D) ? new PriceValue(order.getCurrency().getIsoCode(), totalDeliveryCharge, order
						.isNet().booleanValue()) : null;

			}
		}
		catch (final Exception e)
		{
			LOG.error("Delivery Cost error for Order >>>", e);
		}

		return null;
	}
}