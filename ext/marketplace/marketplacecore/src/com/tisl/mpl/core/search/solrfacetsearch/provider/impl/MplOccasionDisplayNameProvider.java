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

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;


public class MplOccasionDisplayNameProvider extends AbstractFacetValueDisplayNameProvider
{

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue)
	{

		if (facetValue != null && !facetValue.isEmpty())
		{

			if (facetValue.equalsIgnoreCase("eveningwear"))
			{
				return "Evening Wear";
			}
			else if (facetValue.equalsIgnoreCase("partyclubwear"))
			{
				return "Party & Club Wear";
			}
			else if (facetValue.equalsIgnoreCase("ethnicwear"))
			{
				return "Ethnic Wear";
			}
			else if (facetValue.equalsIgnoreCase("workwear"))
			{
				return "Work Wear";
			}
			else if (facetValue.equalsIgnoreCase("casualwear"))
			{
				return "Casual Wear";
			}
			else if (facetValue.equalsIgnoreCase("collegelook"))
			{
				return "College Look";
			}
			else if (facetValue.equalsIgnoreCase("weddingwear"))
			{
				return "Wedding Wear";
			}

			//added for kidswear
			if (facetValue.equalsIgnoreCase("occasionkidswearcasual"))
			{
				return "Casual";
			}
			else if (facetValue.equalsIgnoreCase("occasionkidswearparty"))
			{
				return "Party";
			}
			else if (facetValue.equalsIgnoreCase("occasionkidswearethnic"))
			{
				return "Ethnic";
			}
			else if (facetValue.equalsIgnoreCase("occasionkidswearwestern"))
			{
				return "Western";
			}
			else if (facetValue.equalsIgnoreCase("occasionkidswearsleepwear"))
			{
				return "Sleepwear";
			}
			else if (facetValue.equalsIgnoreCase("occasionkidswearsports"))
			{
				return "Sports";
			}
			else if (facetValue.equalsIgnoreCase("occasionkidswearactive"))
			{
				return "Active";

			}
			else if (facetValue.equalsIgnoreCase("occasionkidsweareverydaywear"))
			{
				return "Everyday Wear";
			}
			return facetValue;
		}

		return "";
	}

}
