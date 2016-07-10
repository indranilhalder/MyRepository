/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.impl.DefaultPromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class ExtDefaultPromotionsService extends DefaultPromotionsService
{


	@Autowired
	private ModelService modelService;

	@Override
	public PromotionOrderResults updatePromotions(final Collection<PromotionGroupModel> promotionGroups,
			final AbstractOrderModel order)
	{
		final PromotionOrderResults result = getPromotionsManager().updatePromotions(
				getModelService().getAllSources(promotionGroups, new ArrayList()), getOrder(order));
		refreshOrder(order);
		return result;
	}

	@Override
	public PromotionOrderResults updatePromotions(final Collection<PromotionGroupModel> promotionGroups,
			final AbstractOrderModel order, final boolean evaluateRestrictions,
			final PromotionsManager.AutoApplyMode productPromotionMode, final PromotionsManager.AutoApplyMode orderPromotionMode,
			final Date date)
	{
		final PromotionOrderResults result = getPromotionsManager().updatePromotions(getSessionContext(),
				getModelService().getAllSources(promotionGroups, new ArrayList()), getOrder(order), evaluateRestrictions,
				productPromotionMode, orderPromotionMode, date);
		refreshOrder(order);
		return result;
	}

	@Override
	public void refreshOrder(final AbstractOrderModel order)
	{
		List toRefresh = new ArrayList(1);
		toRefresh.add(order);
		refreshModifiedModelsAfter(toRefresh);
		toRefresh = new ArrayList(order.getEntries());
		refreshModifiedModelsAfter(toRefresh);
		setCartSubTotal(order);
	}

	private void setCartSubTotal(final AbstractOrderModel orderModel)
	{
		double subtotal = 0.0;
		if (orderModel != null)
		{
			final List<AbstractOrderEntryModel> entries = orderModel.getEntries();
			for (final AbstractOrderEntryModel entry : entries)
			{
				if (entry.getQuantity() != null && entry.getBasePrice() != null)
				{
					final double entryTotal = entry.getQuantity().doubleValue() * entry.getBasePrice().doubleValue();
					subtotal += entryTotal;
				}
			}
			orderModel.setSubtotal(Double.valueOf(subtotal));
			modelService.save(orderModel);
		}
	}

}
