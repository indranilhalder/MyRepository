/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.List;

import com.tisl.mpl.data.PriceBreakupData;


/**
 * @author Tcs
 *
 */
public interface PriceBreakupService
{
	//
	public List<PriceBreakupData> getPricebreakup(final String ussid, final String sellerId);

	//Added for 3782
	/**
	 * @param entry
	 * @param childOrderEntry
	 * @return
	 */
	public void createPricebreakupOrder(AbstractOrderEntryModel entry, AbstractOrderEntryModel childOrderEntry);
}
