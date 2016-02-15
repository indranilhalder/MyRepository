package com.tisl.mpl.helper;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;


public class MplCouponHelper
{


	@Resource(name = "modelService")
	private ModelService modelService;

	/**
	 * @Description: validate delivery mode coupon
	 *
	 * @param restrDelModeList
	 * @param order
	 * @param isPositive
	 *
	 * @return List<AbstractOrderEntry>
	 */
	@SuppressWarnings("deprecation")
	public List<AbstractOrderEntry> validateDelliveryMode(final List<DeliveryMode> restrDelModeList, final AbstractOrder order,
			final boolean isPositive)
	{
		final List<AbstractOrderEntry> matchingEntryList = new ArrayList<AbstractOrderEntry>();

		final List<AbstractOrderEntry> entryList = new ArrayList<AbstractOrderEntry>(order.getAllEntries());

		for (final AbstractOrderEntry orderEntry : entryList)
		{
			final AbstractOrderEntryModel entry = ((AbstractOrderEntryModel) modelService.get(orderEntry));

			if (entry.getMplDeliveryMode() != null)
			{
				final DeliveryMode selectedDelMode = modelService.getSource(entry.getMplDeliveryMode().getDeliveryMode());
				if (restrDelModeList.contains(selectedDelMode))
				{
					matchingEntryList.add(orderEntry);
				}
			}
		}

		if (!matchingEntryList.isEmpty())
		{
			if (isPositive)
			{
				entryList.retainAll(matchingEntryList);
			}
			else
			{
				entryList.removeAll(matchingEntryList);
			}

			return entryList;
		}
		else
		{
			return matchingEntryList;
		}

	}

}