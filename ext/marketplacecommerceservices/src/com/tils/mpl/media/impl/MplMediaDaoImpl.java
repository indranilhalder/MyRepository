/**
 *
 */
package com.tils.mpl.media.impl;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tils.mpl.media.MplMediaDao;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public class MplMediaDaoImpl implements MplMediaDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tils.mpl.media.MplMediaDao#findMediaSearch(java.lang.String)
	 *
	 * @Javadoc Method to Optimize Image load in PDP.Single Db Call to Populate Different Image Format
	 *
	 * @param MediaContainerModel container , String mediaFormatList
	 *
	 * @return List<MediaModel>
	 */

	private static final Logger LOG = Logger.getLogger(MplMediaDaoImpl.class);

	@Override
	public List<MediaModel> findMediaForQualifier(final MediaContainerModel container, final String mediaFormatList)
	{
		try
		{
			final StringBuffer queryString = new StringBuffer(500);

			queryString
					.append("select {media.PK} from {Media as media JOIN "
							+ "MediaFormat as mf ON {media.MEDIAFORMAT}={mf.PK} JOIN CatalogVersion as cat ON {media.CATALOGVERSION }={cat.PK}}"
							+ "where {media.MEDIACONTAINER}= ?containerPK " + "and {cat.VERSION } = ?catalogVersion "
							+ "and {mf.QUALIFIER} in (");
			queryString.append(mediaFormatList);
			queryString.append(")");

			LOG.debug("QueryStringFetchingMediaforSearch::" + queryString.toString());
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
			query.addQueryParameter("catalogVersion", "Online");
			query.addQueryParameter("containerPK", container);
			return flexibleSearchService.<MediaModel> search(query).getResult();

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
}
