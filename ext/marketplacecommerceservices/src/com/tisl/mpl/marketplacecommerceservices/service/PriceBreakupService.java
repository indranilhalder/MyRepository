/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.LinkedHashMap;


/**
 * @author 970506
 *
 */
public interface PriceBreakupService
{
	//
	public LinkedHashMap<String, PriceData> getPricebreakup(final String productCode, final String ussid);
}
