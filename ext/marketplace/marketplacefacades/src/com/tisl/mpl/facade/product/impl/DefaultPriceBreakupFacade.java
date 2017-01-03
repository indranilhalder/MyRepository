/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.LinkedHashMap;

import javax.annotation.Resource;

import com.tisl.mpl.facade.product.PriceBreakupFacade;
import com.tisl.mpl.marketplacecommerceservices.service.PriceBreakupService;



/**
 * @author 970506
 *
 */


public class DefaultPriceBreakupFacade implements PriceBreakupFacade
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.product.PriceBreakupFacade#getPricebreakup(java.lang.String, java.lang.String)
	 */

	@Resource(name = "priceBreakupService")
	private PriceBreakupService priceBreakupService;

	@Override
	public LinkedHashMap<String, PriceData> getPricebreakup(final String productCode, final String ussid)
	{
		//List<JewelleryPriceRowModel> jewelleryPriceRowList = new ArrayList<JewelleryPriceRowModel>();

		final LinkedHashMap<String, PriceData> PriceMap = priceBreakupService.getPricebreakup(productCode, ussid);

		return PriceMap;
	}

	//

	/**
	 * @return the priceBreakupService
	 */
	public PriceBreakupService getPriceBreakupService()
	{
		return priceBreakupService;
	}

	/**
	 * @param priceBreakupService
	 *           the priceBreakupService to set
	 */
	public void setPriceBreakupService(final PriceBreakupService priceBreakupService)
	{
		this.priceBreakupService = priceBreakupService;
	}


}
