/**
 *
 */
package com.tisl.mpl.facade.product;

import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.LinkedHashMap;


/**
 * @author Tcs
 *
 */
public interface PriceBreakupFacade
{
	//
	public LinkedHashMap<String, PriceData> getPricebreakup(final String ussid);
}
