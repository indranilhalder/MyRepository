/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelCancelableEntriesStrategy;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;


/**
 * @author 1006687
 *
 */
public class MarketPlaceOrderCancelCancelableEntriesStrategy implements OrderCancelCancelableEntriesStrategy
{

	private List<String> cancellableStatus;

	/**
	 * @return the nonCancellableOrderStatuses
	 */
	public List<OrderStatus> getNonCancellableOrderStatuses()
	{
		return nonCancellableOrderStatuses;
	}

	private final List<OrderStatus> nonCancellableOrderStatuses = Arrays.asList(OrderStatus.RMS_VERIFICATION_PENDING,
			OrderStatus.PAYMENT_FAILED);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.ordercancel.OrderCancelCancelableEntriesStrategy#getAllCancelableEntries(de.hybris.platform
	 * .core.model.order.OrderModel, de.hybris.platform.core.model.security.PrincipalModel)
	 */
	@Override
	public Map<AbstractOrderEntryModel, Long> getAllCancelableEntries(final OrderModel orderModel,
			final PrincipalModel paramPrincipalModel)
	{
		if (orderModel != null)
		{
			final Map<AbstractOrderEntryModel, Long> result = new HashMap<>();
			//If current selection is child order.
			if (CollectionUtils.isEmpty(orderModel.getChildOrders()))
			{
				result.putAll(getCancellableEnteries(orderModel));

			}

			return result;
		}
		return null;
	}

	protected Map<AbstractOrderEntryModel, Long> getCancellableEnteries(final OrderModel orderModel)
	{
		if (orderModel != null)
		{
			if (CollectionUtils.isNotEmpty(orderModel.getEntries()))
			{

				final Map<AbstractOrderEntryModel, Long> result = new HashMap<>();
				for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
				{
					if (orderEntry.getQuantity() > NumberUtils.LONG_ZERO)
					{
						{
							if (CollectionUtils.isEmpty(orderEntry.getConsignmentEntries()))
							{
								if (!nonCancellableOrderStatuses.contains(orderModel.getStatus()))
								{
									result.put(orderEntry, orderEntry.getQuantity());
								}
							}
							else
							{
								for (final ConsignmentEntryModel consignmentEntry : orderEntry.getConsignmentEntries())
								{
									final ConsignmentStatus consignmentStatus = consignmentEntry.getConsignment().getStatus();
									if (consignmentStatus != null && orderEntry.getQuantity() > 0
											&& cancellableStatus.contains(consignmentStatus.getCode().toUpperCase()))
									{
										result.put(orderEntry, orderEntry.getQuantity());
									}
								}
							}
						}
					}
				}
				return result;
			}
		}
		return null;
	}

	public List<String> getCancellableStatus()
	{
		return cancellableStatus;
	}

	public void setCancellableStatus(final List<String> cancellableStatus)
	{
		this.cancellableStatus = cancellableStatus;
	}


}
