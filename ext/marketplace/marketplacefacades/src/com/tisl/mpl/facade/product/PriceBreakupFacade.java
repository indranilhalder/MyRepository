/**
 *
 */
package com.tisl.mpl.facade.product;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.LinkedHashMap;


/**
 * @author Tcs
 *
 */
public interface PriceBreakupFacade
{
	//
	public LinkedHashMap<String, PriceData> getPricebreakup(final String ussid);

	//Added for 3782

	/**
	 * @param abstractOrderEntryModel
	 */
	public boolean createPricebreakupOrder(AbstractOrderEntryModel abstractOrderEntryModel);
}
