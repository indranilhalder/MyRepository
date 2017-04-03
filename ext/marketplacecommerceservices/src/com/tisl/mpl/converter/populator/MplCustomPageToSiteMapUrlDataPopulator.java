/**
 *
 */
package com.tisl.mpl.converter.populator;

import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tisl.mpl.data.CustomPageData;


/**
 * @author TCS
 *
 */
public class MplCustomPageToSiteMapUrlDataPopulator implements Populator<CustomPageData, SiteMapUrlData>
{
	@Override
	public void populate(final CustomPageData source, final SiteMapUrlData siteMapUrlData) throws ConversionException
	{
		siteMapUrlData.setLoc(source.getUrl());
		siteMapUrlData.setChangeFrequency(source.getChangeFrequency());
		siteMapUrlData.setPriority(source.getPriority());
		final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		final Date date = new Date();
		siteMapUrlData.setLastMod(dateFormat.format(date));
	}
}