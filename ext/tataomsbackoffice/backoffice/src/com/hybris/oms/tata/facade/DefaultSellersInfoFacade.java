package com.hybris.oms.tata.facade;


import java.util.List;

import javax.annotation.Resource;

import com.hybris.oms.tata.services.SellerInfoService;


/**
 * @author techouts
 *
 */
public class DefaultSellersInfoFacade implements SellersInfoFacade
{

	@Resource(name = "sellerInfoService")
	private SellerInfoService sellerInfoService;

	/**
	 * @return sellers
	 */
	@Override
	public List<String> getAllSellers()
	{
		return sellerInfoService.getAllSellers();
	}

	/**
	 * @param sellerInfoService
	 *           the sellerInfoService to set
	 */
	public void setSellerInfoService(final SellerInfoService sellerInfoService)
	{
		this.sellerInfoService = sellerInfoService;
	}

}
