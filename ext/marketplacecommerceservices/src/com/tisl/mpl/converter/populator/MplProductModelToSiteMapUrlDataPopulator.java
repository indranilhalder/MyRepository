/**
 *
 */
package com.tisl.mpl.converter.populator;


import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.acceleratorservices.sitemap.populators.ProductModelToSiteMapUrlDataPopulator;
import de.hybris.platform.basecommerce.strategies.ActivateBaseSiteInSessionStrategy;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class MplProductModelToSiteMapUrlDataPopulator extends ProductModelToSiteMapUrlDataPopulator
{
	/**
	 * @return the cmsSiteService
	 */
	public CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}


	/**
	 * @param cmsSiteService
	 *           the cmsSiteService to set
	 */
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}


	/**
	 * @return the activateBaseSiteInSession
	 */
	public ActivateBaseSiteInSessionStrategy<CMSSiteModel> getActivateBaseSiteInSession()
	{
		return activateBaseSiteInSession;
	}


	/**
	 * @param activateBaseSiteInSession
	 *           the activateBaseSiteInSession to set
	 */
	public void setActivateBaseSiteInSession(final ActivateBaseSiteInSessionStrategy<CMSSiteModel> activateBaseSiteInSession)
	{
		this.activateBaseSiteInSession = activateBaseSiteInSession;
	}


	private final static Logger LOG = Logger.getLogger(MplProductModelToSiteMapUrlDataPopulator.class.getName());
	private CMSSiteService cmsSiteService;
	private ActivateBaseSiteInSessionStrategy<CMSSiteModel> activateBaseSiteInSession;


	@Override
	public void populate(final ProductModel productModel, final SiteMapUrlData siteMapUrlData) throws ConversionException
	{
		LOG.info("Inside MplProductModelToSiteMapUrlDataPopulator");

		// set the catalog version for the current session



		final String relUrl = StringEscapeUtils.escapeXml(getUrlResolver().resolve(productModel));


		siteMapUrlData.setLoc(relUrl);


		if (productModel.getPicture() != null)
		{
			siteMapUrlData.setImages(Collections.singletonList(productModel.getPicture().getURL()));
		}
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		final Date date = new Date();
		siteMapUrlData.setLastMod(dateFormat.format(date));

	}
}