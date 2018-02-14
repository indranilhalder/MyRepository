/**
 *
 */
package com.tils.mpl.media.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
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
			queryString.append(')');

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


	@Override
	public MediaModel getMediaForIndexing(final ProductModel product, final MediaFormatModel mediaFormat,
			final List<MediaContainerModel> galleryImages)
	{

		int count = 1;
		final StringBuilder galImgPK = new StringBuilder();
		MediaModel media = null; //SONR FIX-A method should have only one exit point, and that should be the last statement in the method

		for (final MediaContainerModel mcList : galleryImages)
		{

			if (galleryImages.size() != count)
			{
				galImgPK.append(mcList.getPk()); //Sonr Fix
                                galImgPK.append(',');

			}
			else
			{
				galImgPK.append(mcList.getPk());
				
			}
			count = count + 1;
		}
		try
		{

			final String queryString = "select {media." + MediaModel.PK + "} from {" + MediaModel._TYPECODE + " as media JOIN "
					+ MediaContainerModel._TYPECODE + " as container " + " ON {container." + MediaContainerModel.PK + "}={media."
					+ MediaModel.MEDIACONTAINER + "} JOIN " + CatalogVersionModel._TYPECODE + " as cat ON {media."
					+ MediaModel.CATALOGVERSION + "}={cat." + CatalogVersionModel.PK + "} JOIN " + MediaFormatModel._TYPECODE
					+ " as mf ON {media." + MediaModel.MEDIAFORMAT + "}={mf." + MediaFormatModel.PK + "}} " + " where {media."
					+ MediaModel.MEDIAPRIORITY + "}=?priority and {cat." + CatalogVersionModel.VERSION + "} =?catalogVersion and {mf."
					+ MediaFormatModel.QUALIFIER + "}= ?searchMediaFormat and {container." + MediaContainerModel.PK + "} in ("
					+ galImgPK + ")";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter("priority", "1");
			query.addQueryParameter("catalogVersion", product.getCatalogVersion().getVersion());
			query.addQueryParameter("searchMediaFormat", mediaFormat.getQualifier());

			final SearchResult<MediaModel> searchResult = flexibleSearchService.search(query);



			media = searchResult.getResult().get(0);



		}

		catch (final ModelNotFoundException localModelNotFoundException)
		{

			LOG.debug("Error finding Media for the Product" + localModelNotFoundException);
			return null;
		}
		catch (final Exception e)
		{

			LOG.debug("Exception in finding Media" + e);
			return null;
		}
		return media;
	}


	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
