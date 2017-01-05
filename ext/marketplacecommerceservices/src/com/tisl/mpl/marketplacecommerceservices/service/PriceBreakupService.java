/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.LinkedHashMap;


/**
 * @author Tcs
 *
 */
public interface PriceBreakupService
{
	//
	public LinkedHashMap<String, PriceData> getPricebreakup(final String ussid);
}
