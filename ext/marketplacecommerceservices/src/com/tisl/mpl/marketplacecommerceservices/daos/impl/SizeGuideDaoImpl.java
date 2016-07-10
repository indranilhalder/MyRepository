/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.SizeGuideModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.SizeGuideDao;


/**
 * @author TCS
 *
 */
public class SizeGuideDaoImpl implements SizeGuideDao
{


	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private CatalogVersionService catalogVersionService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.SizeGuideDao#getsizeGuideByCode(java.lang.String)
	 */
	@Override
	public List<SizeGuideModel> getsizeGuideByCode(final String sizeGuideCode)
	{

		try
		{
			final CatalogVersionModel catalogVersion = getCatalogVersion();
			final String queryString = "SELECT {sg." + SizeGuideModel.PK + "} FROM {" + SizeGuideModel._TYPECODE + " AS sg}"

			+ " WHERE {sg:" + SizeGuideModel.SIZEGUIDEID + "}=?sizeGuideCode  AND {sg:" + SizeGuideModel.CATALOGVERSION
					+ "} = ?catalogVersion";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("sizeGuideCode", sizeGuideCode);
			query.addQueryParameter("catalogVersion", catalogVersion);
			return flexibleSearchService.<SizeGuideModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

	}

	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
		return catalogVersionModel;
	}
}
