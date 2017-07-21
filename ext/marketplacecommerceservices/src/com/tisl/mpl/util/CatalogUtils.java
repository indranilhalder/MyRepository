/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


/**
 * @author Madhavan
 *
 */
public class CatalogUtils
{

	@Autowired
	private BaseSiteService baseSiteService;

	@Autowired
	private CMSSiteService cmsSiteService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	public CatalogVersionModel getSessionCatalogVersionForProduct()
	{
		final Collection<CatalogVersionModel> currentCatalogVersion = catalogVersionService.getSessionCatalogVersions();
		if (CollectionUtils.isNotEmpty(currentCatalogVersion))
		{
			return currentCatalogVersion.iterator().next();
		}
		return catalogVersionService
				.getSessionCatalogVersionForCatalog(MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID);
	}

	public CatalogVersionModel getSessionCatalogVersionForContent()
	{
		CatalogVersionModel catalogVersionModel = null;
		final List<ContentCatalogModel> contentCatalogs = cmsSiteService.getCurrentSite().getContentCatalogs();
		if (CollectionUtils.isNotEmpty(contentCatalogs))
		{
			catalogVersionModel = catalogVersionService.getSessionCatalogVersionForCatalog(contentCatalogs.get(0).getId());
		}
		else
		{
			catalogVersionModel = catalogVersionService
					.getSessionCatalogVersionForCatalog(MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CONTENT_CATALOG_ID);
		}
		return catalogVersionModel;
	}
}
