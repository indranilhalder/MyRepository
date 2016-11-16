/**
 *
 */
package com.tisl.mpl.promotion.helper;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.jalo.DefaultPromotionManager;


/**
 * @author TCS
 *
 */
@SuppressWarnings("deprecation")
public class MplBundlePromotionHelper
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplBundlePromotionHelper.class.getName());

	/**
	 * Calculates the discount Percentage
	 *
	 * @param validProductList
	 * @param validProductUssidMap
	 * @param totalAdjustment
	 * @return percentageDiscount
	 */
	public double getDiscountPercentage(final Map<String, Integer> validProductList,
			final Map<String, AbstractOrderEntry> validProductUssidMap, final double totalAdjustment)
	{
		double percentageDiscount = 0.0D;
		final double totalPrice = getTotalEligiblePrice(validProductUssidMap, validProductList);

		if (totalPrice > 0.0D)
		{
			percentageDiscount = getPercentageDiscount(totalAdjustment, totalPrice);
		}
		return percentageDiscount;
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	/**
	 * Get Percentage Discount
	 *
	 * @param totalAdjustment
	 * @param totalPrice
	 * @return double
	 */
	private double getPercentageDiscount(final double totalAdjustment, final double totalPrice)
	{
		return ((totalAdjustment * 100) / totalPrice);
	}


	/**
	 * Get Total Eligible Price
	 *
	 *
	 * @param validProductUssidMap
	 * @param validProductList
	 * @return double
	 */
	public double getTotalEligiblePrice(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final Map<String, Integer> validProductList)
	{
		return getDefaultPromotionsManager().getTotalValidProdPrice(validProductUssidMap, validProductList);
	}


	/**
	 * Get Sorted Valid USSID Data List
	 *
	 * @param validProductUssidMap
	 * @param stepQuantityList
	 * @param paramSessionContext
	 * @param totalCount
	 * @param restrictionList
	 */
	public Map<String, Integer> getSortedValidProdUssidMap(final Map<String, AbstractOrderEntry> validProductUssidMap,
			final List<Long> stepQuantityList, final SessionContext paramSessionContext, final int totalCount,
			final List<AbstractPromotionRestriction> restrictionList)
	{
		final Map<String, Integer> validUssidList = new HashMap<String, Integer>();
		final long totalEligibleCount = getEligibleQualifyingCount(totalCount, stepQuantityList);

		validProductUssidMap.keySet().retainAll(
				getDefaultPromotionsManager().populateSortedValidProdUssidMap(validProductUssidMap, (int) totalEligibleCount,
						paramSessionContext, restrictionList, validUssidList));
		return validUssidList;
	}

	/**
	 * Populate the Qualifying Count
	 *
	 * @param totalCount
	 * @param stepQuantityList
	 * @return totalEligibleCount
	 */
	private long getEligibleQualifyingCount(final int totalCount, final List<Long> stepQuantityList)
	{
		long totalEligibleCount = 0;
		long factorCount = 0;
		long consumedCount = totalCount;

		if (CollectionUtils.isNotEmpty(stepQuantityList) && totalCount > 0)
		{
			for (final Long quantity : stepQuantityList)
			{
				if (consumedCount >= quantity.longValue())
				{
					factorCount = consumedCount / quantity.longValue();
					totalEligibleCount += (factorCount * quantity.longValue());
					consumedCount -= factorCount * quantity.longValue();
				}
			}
		}
		return totalEligibleCount;
	}
}
