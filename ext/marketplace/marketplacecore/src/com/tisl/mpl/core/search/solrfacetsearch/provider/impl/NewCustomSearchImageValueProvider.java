/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.core.util.MediaHelper;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class NewCustomSearchImageValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
{

	private static final Logger LOG = Logger.getLogger(NewCustomSearchImageValueProvider.class);


	private String mediaFormat;
	private MediaService mediaService;
	private MediaContainerService mediaContainerService;
	private FieldNameProvider fieldNameProvider;

	@Resource
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private MediaHelper mediaHelper;

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

	@SuppressWarnings("deprecation")
	@Override
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
					final MediaModel media = mediaHelper.findMedia((ProductModel) model, mediaFormatModel);
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
			throw new FieldValueProviderException("Cannot evaluate " + indexedProperty.getName() + " using "
					+ super.getClass().getName() + "exception" + e, e);
		}
		return Collections.emptyList();

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
