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

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.apache.log4j.Logger;


public class MplWchFAColorFacetDisplayNameProvider extends AbstractFacetValueDisplayNameProvider
{

	private static final Logger LOG = Logger.getLogger(MplWchFAColorFacetDisplayNameProvider.class);

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue)
	{
		if (facetValue == null)
		{
			return "";
		}

		try
		{
			if (facetValue.contains("_"))
			{
				final String[] colourWithCode = facetValue.split("_");
				if (colourWithCode != null && colourWithCode.length >= 2)
				{
					if (colourWithCode[0] != null && colourWithCode[0].equalsIgnoreCase("multi"))
					{
						if (colourWithCode[1] != null)
						{
							return "Multi" + colourWithCode[1];
						}
					}

					return colourWithCode[0];
				}
			}
			return facetValue;

		}

		catch (final UnknownIdentifierException exception)
		{
			LOG.error("Unable to resolve Enum for the specified colour" + exception);
		}
		return "";
	}
}