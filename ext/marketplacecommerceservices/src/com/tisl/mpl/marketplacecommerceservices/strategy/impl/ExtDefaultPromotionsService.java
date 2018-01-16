/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.impl.DefaultPromotionsService;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author TCS
 *
 */
public class ExtDefaultPromotionsService extends DefaultPromotionsService
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(ExtDefaultPromotionsService.class);

	@Autowired
	private ModelService modelService;

	//CAR-324 SONR Fix unused variable
	//	@Autowired
	//	private ConfigurationService configurationService;

	@Override
	public PromotionOrderResults updatePromotions(final Collection<PromotionGroupModel> promotionGroups,
			final AbstractOrderModel order)
	{
		final PromotionOrderResults result = getPromotionsManager().updatePromotions(
				getModelService().getAllSources(promotionGroups, new ArrayList()), getOrder(order));

		refreshOrder(order);
		//CAR-324
		//		if ("Y".equalsIgnoreCase(configurationService.getConfiguration().getString("new.refreshOrder.flag")))
		//		{
		//			refreshOrderNew(order, result);
		//		}
		//		else
		//		{
		//			refreshOrder(order);
		//		}
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

		//		if (result == null)
		//		{
		//			LOG.error("Failed to update promotion for orderId::" + order.getCode());
		//		}
		//		if ("Y".equalsIgnoreCase(configurationService.getConfiguration().getString("new.refreshOrder.flag")))
		//		{
		//			refreshOrderNew(order, result);
		//		}
		//		else
		//		{
		//			refreshOrder(order);
		//		}
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
				final Long quantity = entry.getQuantity();
				final Double basePrice = entry.getBasePrice();

				if (quantity != null && basePrice != null)
				{
					final double entryTotal = basePrice.doubleValue() * quantity.doubleValue();
					subtotal += entryTotal;
				}
			}
			orderModel.setSubtotal(Double.valueOf(subtotal));
			modelService.save(orderModel);
		}
	}

	//CAR-324
	private List<String> isOrderAndEntryLevelPromotion(final PromotionOrderResults result)
	{
		PromotionResultModel promotionResModel = null;
		AbstractPromotionModel abstPromotionModel = null;
		List<PromotionResult> promotionResultList = null;
		final List<String> isOrderAndEntryPromotionList = new ArrayList<String>(Arrays.asList("N", "N"));

		try
		{
			if (result != null && result.getAllResults() != null)
			{
				promotionResultList = result.getAllResults();
				if (promotionResultList != null)
				{
					for (final PromotionResult promotionResult : promotionResultList)
					{
						promotionResModel = getModelService().get(promotionResult);
						if (promotionResModel != null && null != promotionResModel.getCertainty()
								&& (promotionResModel.getCertainty().floatValue() == 1F))
						{
							abstPromotionModel = promotionResModel.getPromotion();

							if (abstPromotionModel != null)
							{
								if (abstPromotionModel instanceof ProductPromotionModel)
								{
									isOrderAndEntryPromotionList.add(0, "Y");
								}
								else if (abstPromotionModel instanceof OrderPromotionModel)
								{
									isOrderAndEntryPromotionList.add(1, "Y");
								}
							}
						}
					}
				}
				else
				{
					LOG.error("promotionResultList is null");
				}
			}
			else
			{
				LOG.error("result is null or result.getAllResults() is null");
			}
		}
		catch (final Exception e)
		{
			LOG.error(e);
			//e.printStackTrace();
		}

		return isOrderAndEntryPromotionList;
	}
	//SONR FIX Unused Methods
	//CAR-324
	/*
	 * private void refreshOrderNew(final AbstractOrderModel order, final PromotionOrderResults result) { final
	 * List<String> isOrdAndEntryLevelPromotionList = isOrderAndEntryLevelPromotion(result); List toRefresh = new
	 * ArrayList();
	 * 
	 * if ("Y".equalsIgnoreCase(isOrdAndEntryLevelPromotionList.get(1))) { toRefresh.add(order);
	 * refreshModifiedModelsAfter(toRefresh); }
	 * 
	 * if ("Y".equalsIgnoreCase(isOrdAndEntryLevelPromotionList.get(0))) { toRefresh = new ArrayList(order.getEntries());
	 * refreshModifiedModelsAfter(toRefresh); }
	 * 
	 * setCartSubTotal(order); }
	 */

}
