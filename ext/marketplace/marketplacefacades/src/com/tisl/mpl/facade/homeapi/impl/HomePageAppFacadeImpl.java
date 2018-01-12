/**
 *
 */
package com.tisl.mpl.facade.homeapi.impl;

import javax.annotation.Resource;

import com.tisl.mpl.facade.homeapi.HomePageAppFacade;
import com.tisl.mpl.service.HomePageAppService;
import com.tisl.mpl.wsdto.ComponentRequestDTO;


/**
 * @author TCS
 *
 */
public class HomePageAppFacadeImpl implements HomePageAppFacade
{
	@Resource(name = "homePageAppService")
	private HomePageAppService homePageAppService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.homeapi.HomePageAppFacade#getAdobeTargetData()
	 */
	@Override
	public void getAdobeTargetDataOfferWidget(final ComponentRequestDTO ComponentRequestDTO)
	{
		// YTODO Auto-generated method stub
		final String st = homePageAppService.getAdobeTargetDataOfferWidget(ComponentRequestDTO);
	}

	/**
	 * @return the homePageAppService
	 */
	public HomePageAppService getHomePageAppService()
	{
		return homePageAppService;
	}

	/**
	 * @param homePageAppService
	 *           the homePageAppService to set
	 */
	public void setHomePageAppService(final HomePageAppService homePageAppService)
	{
		this.homePageAppService = homePageAppService;
	}

}
