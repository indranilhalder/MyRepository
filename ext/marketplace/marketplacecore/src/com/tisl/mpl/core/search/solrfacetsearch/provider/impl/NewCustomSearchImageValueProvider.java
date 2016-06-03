/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class NewCustomSearchImageValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{


	private static final Logger LOG = Logger.getLogger(NewCustomSearchImageValueProvider.class);
	private FlexibleSearchService flexibleSearchService;


	private String mediaFormat;
	private MediaService mediaService;
	private MediaContainerService mediaContainerService;
	private FieldNameProvider fieldNameProvider;

	protected String getMediaFormat()
	{
		return this.mediaFormat;
	}

	@Required
	public void setMediaFormat(final String mediaFormat)
	{
		this.mediaFormat = mediaFormat;
	}

	protected MediaService getMediaService()
	{
		return this.mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	protected MediaContainerService getMediaContainerService()
	{
		return this.mediaContainerService;
	}

	@Required
	public void setMediaContainerService(final MediaContainerService mediaContainerService)
	{
		this.mediaContainerService = mediaContainerService;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return this.fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		if ((model instanceof ProductModel))
		{
			final MediaFormatModel mediaFormatModel = getMediaService().getFormat(getMediaFormat());
			if (mediaFormatModel != null)
			{
				final MediaModel media = findMedia((ProductModel) model, mediaFormatModel);
				if (media != null)
				{
					return createFieldValues(indexedProperty, media);
				}

				//if (LOG.isDebugEnabled()) Deeply nested if..then statements are hard to read
				//{
				LOG.debug("No [" + mediaFormatModel.getQualifier() + "] image found for product [" + ((ProductModel) model).getCode()
						+ "]");
				//}
			}
		}
		return Collections.emptyList();
	}

	protected MediaModel findMedia(final ProductModel product, final MediaFormatModel mediaFormat)
	{
		if ((product != null) && (mediaFormat != null))
		{
			final List<MediaContainerModel> galleryImages = product.getGalleryImages();
			if ((galleryImages != null) && (!(galleryImages.isEmpty())))
			{
				//final MediaContainerModel firstMediaContainerModel = galleryImages.get(0);

				try
				{

					final String queryString = "select {media." + MediaModel.PK + "} from {" + MediaModel._TYPECODE
							+ " as media JOIN " + MediaContainerModel._TYPECODE + " as container " + " ON {container."
							+ MediaContainerModel.PK + "}={media." + MediaModel.MEDIACONTAINER + "} JOIN "
							+ CatalogVersionModel._TYPECODE + " as cat ON {media." + MediaModel.CATALOGVERSION + "}={cat."
							+ CatalogVersionModel.PK + "} JOIN " + MediaFormatModel._TYPECODE + " as mf ON {media."
							+ MediaModel.MEDIAFORMAT + "}={mf." + MediaFormatModel.PK + "}} " + " where {media."
							+ MediaModel.MEDIAPRIORITY + "}=?priority and {cat." + CatalogVersionModel.VERSION
							+ "} = ?catalogVersion and {mf." + MediaFormatModel.QUALIFIER + "}= ?searchMediaFormat and {container."
							+ MediaContainerModel.PK + "} in (" + galleryImages + ")";

					final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

					query.addQueryParameter("priority", "1");
					query.addQueryParameter("catalogVersion", "Online");
					query.addQueryParameter("searchMediaFormat", "252Wx374H");

					LOG.info("\n\nFinal media query is ===> " + query);

					MediaModel media = null;
					//					  for (final MediaContainerModel container : galleryImages) {  try { media =
					//					  getMediaContainerService().getMediaForFormat(container, mediaFormat); } catch (final
					//					  ModelNotFoundException e) { LOG.error("Error finding Media for the Product" + e); }

					final SearchResult<MediaModel> result = flexibleSearchService.search(query);

					media = result.getResult().get(0);


					if (media != null)
					{
						return media;
					}
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

				/*
				 * final MediaModel firstMedia = getMediaContainerService().getMediaForFormat(firstMediaContainerModel,
				 * mediaFormat); if (firstMedia != null) {
				 *
				 * return firstMedia; }
				 */
			}

			/*
			 * if (product instanceof PcmProductVariantModel) { return findMedia(((PcmProductVariantModel)
			 * product).getBaseProduct(), mediaFormat); }
			 */
		}
		return null;
	}

	/*
	 * protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, final List<MediaModel>
	 * media) { return createFieldValues(indexedProperty, media.getURL()); }
	 */
	protected Collection<FieldValue> createFieldValues(final IndexedProperty indexedProperty, final MediaModel value)
	{
		final List<FieldValue> fieldValues = new ArrayList();

		final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value.getURL()));
		}
		return fieldValues;
	}
}
