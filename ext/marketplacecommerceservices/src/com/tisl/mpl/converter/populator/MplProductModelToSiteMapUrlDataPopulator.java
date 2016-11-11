/**
 *
 */
package com.tisl.mpl.converter.populator;

import de.hybris.platform.acceleratorservices.sitemap.data.SiteMapUrlData;
import de.hybris.platform.acceleratorservices.sitemap.populators.ProductModelToSiteMapUrlDataPopulator;
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
	private final static Logger LOG = Logger.getLogger(MplProductModelToSiteMapUrlDataPopulator.class.getName());

	@Override
	public void populate(final ProductModel productModel, final SiteMapUrlData siteMapUrlData) throws ConversionException
	{
		LOG.info("Inside MplProductModelToSiteMapUrlDataPopulator");
		final String relUrl = StringEscapeUtils.escapeXml(getUrlResolver().resolve(productModel));

		siteMapUrlData.setLoc(relUrl);
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