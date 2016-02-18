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

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;


public class MplFulfillmentModeDisplayNameProvider extends AbstractFacetValueDisplayNameProvider
{

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue)
	{
		String displayName = MarketplacecommerceservicesConstants.EMPTYSPACE;

		if (facetValue != null && facetValue.equalsIgnoreCase("TShip"))
		{
			//return "Tata Marketplace";
			displayName = "Tata Marketplace";
		}
		if (facetValue != null && facetValue.equalsIgnoreCase("SShip"))
		{
			//return "Seller";
			displayName = "Seller";
		}
		//return "";
		return displayName;
	}

}
