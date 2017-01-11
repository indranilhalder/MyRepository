/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.util.LinkedHashMap;


/**
 * @author Tcs
 *
 */
public interface PriceBreakupService
{
	//
	public LinkedHashMap<String, PriceData> getPricebreakup(final String ussid);

	//Added for 3782
	/**
	 * @param entry
	 * @return
	 */
	public boolean createPricebreakupOrder(AbstractOrderEntryModel entry);
}
