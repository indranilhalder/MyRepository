/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class MplColorFacetDisplayNameProvider extends AbstractFacetValueDisplayNameProvider
{

	private static final Logger LOG = Logger.getLogger(MplColorFacetDisplayNameProvider.class);

	private EnumerationService enumerationService;
	private I18NService i18nService;
	private CommonI18NService commonI18NService;

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue)
	{
		if (facetValue == null)
		{
			return "";
		}

		try
		{
			String[] colourWithCode = new String[2];
			if (facetValue.contains("_"))
			{
				colourWithCode = facetValue.split("_");
				if (colourWithCode[0] != null && colourWithCode[0].equalsIgnoreCase("multi"))
				{
					return "Multicolor";
				}

				return colourWithCode[0];
			}
			return facetValue;

		}

		catch (final UnknownIdentifierException exception)
		{
			LOG.error("Unable to resolve Enum for the specified colour" + exception);
		}
		return "";
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}
}
