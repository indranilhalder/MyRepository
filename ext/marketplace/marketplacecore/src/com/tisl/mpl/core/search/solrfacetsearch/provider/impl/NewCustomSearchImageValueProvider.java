/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tils.mpl.media.MplMediaService;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class NewCustomSearchImageValueProvider extends AbstractPropertyFieldValueProvider
		implements FieldValueProvider, Serializable
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	private static final Logger LOG = Logger.getLogger(NewCustomSearchImageValueProvider.class);


	private String mediaFormat;
	private MediaService mediaService;
	private MediaContainerService mediaContainerService;
	private FieldNameProvider fieldNameProvider;

	@Autowired
	private MplMediaService mplMediService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

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
		try
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
					LOG.debug("No [" + mediaFormatModel.getQualifier() + "] image found for product ["
							+ ((ProductModel) model).getCode() + "]");
					//}
				}
			}
		}
		catch (final Exception e) /* added part of value provider go through */
		{
			throw new FieldValueProviderException(
					"Cannot evaluate " + indexedProperty.getName() + " using " + super.getClass().getName() + "exception" + e, e);
		}
		return Collections.emptyList();
	}

	@SuppressWarnings("deprecation")
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
					MediaModel media = null;
					media = mplMediService.getMediaForIndexing(product, mediaFormat, galleryImages);
					if (media.getUrl() != null && media.getUrl().length() > 0)
					{
						LOG.debug("Domain sharding started for product code--> " + product.getCode().toString());
						final String prodCode = product.getCode().toString();
						final char lastChar = prodCode.charAt(prodCode.length() - 1);
						final int lastProductNum = Integer.parseInt(String.valueOf(lastChar));
						int numberOfHosts;
						if (configurationService.getConfiguration().getString("search.media.numberofhosts") != null)
						{
							numberOfHosts = Integer.parseInt(
									String.valueOf(configurationService.getConfiguration().getString("search.media.numberofhosts")));
						}
						else
						{
							numberOfHosts = 1;
						}
						if (((lastProductNum % numberOfHosts) != 0) && (numberOfHosts != 1))
						{
							URL netUrl = null;
							if (!media.getUrl().contains("https://") && !media.getUrl().contains("http://"))
							{
								netUrl = new URL("http:" + media.getUrl());
							}
							else
							{
								netUrl = new URL(media.getUrl());
							}
							String host = null;
							host = netUrl.getHost();
							final String path = netUrl.getPath();
							if (!host.contains("localhost"))
							{
								final String splitHostName[] = host.split("\\.");
								if (splitHostName != null)
								{
									String newHost = "";
									newHost = splitHostName[0] + (lastProductNum % numberOfHosts) + "." + splitHostName[1] + "."
											+ splitHostName[2];
									final String newMediaUrl = "//" + newHost + path;
									LOG.debug("New newMediaUrl for media during indexing --> " + newMediaUrl);
									media.setUrl(newMediaUrl);
								}
							}
						}
					}
					return media;
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