/**
 *
 */
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.order.converters.populator.PromotionOrderEntryConsumedPopulator;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;

import org.springframework.util.Assert;


/**
 * @author TCS
 *
 */
public class MplPromotionOrderEntryConsumedPopulator extends PromotionOrderEntryConsumedPopulator
{

	@Override
	public void populate(final PromotionOrderEntryConsumedModel source, final PromotionOrderEntryConsumedData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getCode());
		target.setAdjustedUnitPrice(source.getAdjustedUnitPrice());
		if (source.getOrderEntry() != null)
		{
			target.setOrderEntryNumber(source.getOrderEntry().getEntryNumber());
			target.setUssid(source.getOrderEntry().getSelectedUSSID());
		}
		target.setQuantity(source.getQuantity());
	}
}
