/**
 *
 */
package com.tisl.mpl.order.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.impl.FindOrderDiscountValuesStrategy;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class MplFindOrderDiscountValuesStrategy extends FindOrderDiscountValuesStrategy
{

	private static final Logger LOG = Logger.getLogger(MplFindOrderDiscountValuesStrategy.class);

	@Override
	public List<DiscountValue> findDiscountValues(final AbstractOrderModel order) throws CalculationException
	{
		LOG.debug("inside MplFindOrderDiscountValuesStrategy");
		final List<DiscountModel> discounts = order.getDiscounts();
		final List<DiscountValue> existingDiscounts = order.getGlobalDiscountValues();
		double promoDiscount = 0.0;
		if (CollectionUtils.isNotEmpty(existingDiscounts))
		{
			for (final DiscountValue discountVal : existingDiscounts)
			{
				if (CollectionUtils.isEmpty(discounts))
				{
					promoDiscount += discountVal.getAppliedValue();
				}
				else if (CollectionUtils.isNotEmpty(discounts))
				{
					for (final DiscountModel orderDiscount : discounts)
					{
						if (!(discountVal.getCode().equalsIgnoreCase(orderDiscount.getCode())))
						{
							promoDiscount += discountVal.getAppliedValue();
						}
					}
				}
			}
		}
		LOG.debug("::::::Promo Discount is ::::::" + promoDiscount);
		if (CollectionUtils.isNotEmpty(discounts))
		{
			final List<DiscountValue> result = new ArrayList<DiscountValue>(discounts.size());
			for (final DiscountModel orderDiscount : discounts)
			{
				final DiscountValue discountValue = getDiscountValue(orderDiscount, order);
				if (discountValue != null)
				{
					if (orderDiscount.getAbsolute().booleanValue()
							&& (order.getSubtotal().doubleValue() - promoDiscount) - discountValue.getValue() <= 0)
					{
						LOG.debug("Inside newDisValue");
						final DiscountValue newDisValue = new DiscountValue(discountValue.getCode(),
								(discountValue.getValue() - promoDiscount) - 0.01, orderDiscount.getAbsolute().booleanValue(),
								discountValue.getCurrencyIsoCode());
						LOG.debug(":::::::::::::newDisValue:::::::::::::" + newDisValue.getAppliedValue());
						result.add(newDisValue);
					}
					else
					{
						LOG.debug("Inside oldDiscountValue");
						result.add(discountValue);
					}
				}
			}
			return result;
		}
		return Collections.EMPTY_LIST;
	}


}