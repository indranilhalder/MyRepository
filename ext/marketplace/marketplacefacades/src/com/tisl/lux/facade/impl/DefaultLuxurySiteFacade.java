/**
 *
 */
package com.tisl.lux.facade.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.site.BaseSiteService;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.lux.facade.CommonUtils;
import com.tisl.mpl.facades.constants.MarketplaceFacadesConstants;


/**
 * @author vishal.parmar
 *
 */
public class CommonUtilsImpl implements CommonUtils
{
	@Autowired
	private BaseSiteService baseSiteService;

	/*
	 * This method returns true if the current site is Luxury.
	 */
	@Override
	public boolean isLuxurySite()
	{
		final BaseSiteModel currentBaseSite = baseSiteService.getCurrentBaseSite();
		final String site = currentBaseSite.getUid();

		if (site != null && !"".equals(site) && MarketplaceFacadesConstants.LuxuryPrefix.equals(site))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}


