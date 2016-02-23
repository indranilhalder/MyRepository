package com.tisl.mpl.helper;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;


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
	public List<AbstractOrderEntry> validateDelliveryMode(final List<DeliveryMode> restrDelModeList, final AbstractOrder order,
			final boolean isPositive)
	{
		final List<AbstractOrderEntry> matchingEntryList = new ArrayList<AbstractOrderEntry>();
		final List<AbstractOrderEntry> entryList = new ArrayList<AbstractOrderEntry>(order.getAllEntries());
		final List<AbstractOrderEntry> finalEntryList = new ArrayList<AbstractOrderEntry>();
		for (final AbstractOrderEntry orderEntry : entryList)
		{
			final AbstractOrderEntryModel entry = ((AbstractOrderEntryModel) getModelService().get(orderEntry));
			if (entry.getMplDeliveryMode() != null)
			{
				final DeliveryMode selectedDelMode = getModelService().getSource(entry.getMplDeliveryMode().getDeliveryMode());
				//Adding entry to list if it contains selected delivery mode
				if (restrDelModeList.contains(selectedDelMode))
				{
					matchingEntryList.add(orderEntry);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(matchingEntryList))
		{
			if (isPositive)
			{
				entryList.retainAll(matchingEntryList);
			}
			else
			{
				entryList.removeAll(matchingEntryList);
			}
			//Adding to final entry list
			finalEntryList.addAll(entryList);
		}
		else
		{
			finalEntryList.addAll(matchingEntryList); //Adding to final entry list
		}
		return finalEntryList;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}




}