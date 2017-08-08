/**
 *
 */
package com.tisl.mpl.facade.product;

import java.util.List;

import com.tisl.mpl.data.PriceBreakupData;


/**
 * @author Tcs
 *
 */
public interface PriceBreakupFacade
{
	//
	public List<PriceBreakupData> getPricebreakup(final String ussid, final String sellerId);

	//Added for 3782

	/**
	 * @param abstractOrderEntryModel
	 */
	//public boolean createPricebreakupOrder(AbstractOrderEntryModel abstractOrderEntryModel);
}
