/**
 *
 */
package com.tisl.mpl.facade.product.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.data.PriceBreakupData;
import com.tisl.mpl.facade.product.PriceBreakupFacade;
import com.tisl.mpl.marketplacecommerceservices.service.PriceBreakupService;


/**
 * @author TCS
 *
 */
public class MplPriceBreakupFacadeImpl implements PriceBreakupFacade
{


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.product.PriceBreakupFacade#getPricebreakup(java.lang.String, java.lang.String)
	 */

	@Resource(name = "priceBreakupService")
	private PriceBreakupService priceBreakupService;

	@Override
	public List<PriceBreakupData> getPricebreakup(final String ussid, final String sellerId)
	{
		//List<JewelleryPriceRowModel> jewelleryPriceRowList = new ArrayList<JewelleryPriceRowModel>();

		// LinkedHashMap<String, PriceData> PriceMap = priceBreakupService.getPricebreakup(ussid);

		return priceBreakupService.getPricebreakup(ussid, sellerId);
	}

	//ADDED FOR 3782
	//	@Override
	//	public boolean createPricebreakupOrder(final AbstractOrderEntryModel entry)
	//	{
	//
	//
	//		return priceBreakupService.createPricebreakupOrder(entry);
	//	}


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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.product.PriceBreakupFacade#createPricebreakupOrder(java.lang.String, java.lang.String)
	 */




}
