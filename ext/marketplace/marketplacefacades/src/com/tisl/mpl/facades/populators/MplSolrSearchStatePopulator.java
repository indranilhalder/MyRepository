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
package com.tisl.mpl.facades.populators;

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 */
public class MplSolrSearchStatePopulator implements Populator<SolrSearchQueryData, SearchStateData>
{
	private String searchPath;
	private UrlResolver<CategoryData> categoryDataUrlResolver;
	private Converter<SolrSearchQueryData, SearchQueryData> searchQueryConverter;
	private static final Logger LOG = Logger.getLogger(MplSolrSearchStatePopulator.class);
	private static final String IS_OFFER_EXISTING = "isOffersExisting";

	protected String getSearchPath()
	{
		return searchPath;
	}

	@Required
	public void setSearchPath(final String searchPath)
	{
		this.searchPath = searchPath;
	}

	protected UrlResolver<CategoryData> getCategoryDataUrlResolver()
	{
		return categoryDataUrlResolver;
	}

	@Required
	public void setCategoryDataUrlResolver(final UrlResolver<CategoryData> categoryDataUrlResolver)
	{
		this.categoryDataUrlResolver = categoryDataUrlResolver;
	}

	protected Converter<SolrSearchQueryData, SearchQueryData> getSearchQueryConverter()
	{
		return searchQueryConverter;
	}

	@Required
	public void setSearchQueryConverter(final Converter<SolrSearchQueryData, SearchQueryData> searchQueryConverter)
	{
		this.searchQueryConverter = searchQueryConverter;
	}

	@Override
	public void populate(final SolrSearchQueryData source, final SearchStateData target)
	{
		target.setQuery(getSearchQueryConverter().convert(source));

		if (source.getCategoryCode() != null)
		{
			populateCategorySearchUrl(source, target);
		}
		else if (source.getSellerID() != null)
		{

			populateSellerListingUrl(source, target);
		}


		else if (checkIfOfferListingPage(source.getFilterTerms()) && source.getOfferID() == null || (source.getOfferID() != null))
		{
			populateOfferListingUrl(source, target);
		}
		//		else if (source.getOfferID() != null)
		//		{
		//
		//			populateOfferListingUrl(source, target);
		//		}
		else
		{
			populateFreeTextSearchUrl(source, target);
		}


	}

	/**
	 * @param filterTerms
	 */
	private boolean checkIfOfferListingPage(final List<SolrSearchQueryTermData> filterTerms)
	{
		// YTODO Auto-generated method stub
		boolean isOffer = false;
		for (final SolrSearchQueryTermData term : filterTerms)
		{
			if (term.getKey().equalsIgnoreCase(IS_OFFER_EXISTING))
			{
				isOffer = true;
				break;
			}
		}
		return isOffer;

	}

	protected void populateCategorySearchUrl(final SolrSearchQueryData source, final SearchStateData target)
	{
		target.setUrl(getCategoryUrl(source) + buildUrlQueryString(source, target));
	}

	protected void populateFreeTextSearchUrl(final SolrSearchQueryData source, final SearchStateData target)
	{
		target.setUrl(getSearchPath() + buildUrlQueryString(source, target));
	}


	protected void populateSellerListingUrl(final SolrSearchQueryData source, final SearchStateData target)
	{
		if (source.getSellerID() != null)
		{
			target.setUrl("/s" + "/" + source.getSellerID() + buildUrlQueryString(source, target));
		}

	}

	protected String getCategoryUrl(final SolrSearchQueryData source)
	{
		final CategoryData categoryData = new CategoryData();
		categoryData.setCode(source.getCategoryCode());
		return getCategoryDataUrlResolver().resolve(categoryData);
	}

	protected String buildUrlQueryString(final SolrSearchQueryData source, final SearchStateData target)
	{
		final String searchQueryParam = target.getQuery().getValue();
		if (StringUtils.isNotBlank(searchQueryParam))
		{
			try
			{
				return "/page-{pageNo}?q=" + URLEncoder.encode(searchQueryParam, "UTF-8");
			}
			catch (final UnsupportedEncodingException e)
			{
				return "/page-{pageNo}?q=" + StringEscapeUtils.escapeHtml(searchQueryParam);
			}
		}
		return "";
	}

	protected void populateOfferListingUrl(final SolrSearchQueryData source, final SearchStateData target)
	{
		String offerCategoryID;
		if (source.getOfferCategoryID() == null)
		{
			offerCategoryID = "all";

		}
		else
		{
			offerCategoryID = source.getOfferCategoryID();
		}
		String encodedOfferId = "";
		if (source.getOfferID() != null)

		{

			try
			{
				encodedOfferId = URLEncoder.encode(source.getOfferID(), "UTF-8");
				target.setUrl("/o/" + offerCategoryID + "?offer=" + encodedOfferId
						+ buildUrlQueryString(source, target).replace("?", "&"));
			}
			catch (final UnsupportedEncodingException e)
			{
				//e.printStackTrace();
				LOG.error("UnsupportedEncodingException ", e);
			}
		}
		else if (source.getOfferID() == null)
		{
			target.setUrl("/o/viewAllOffers?offer=" + encodedOfferId + "?searchCategory=" + offerCategoryID
					+ buildUrlQueryString(source, target).replace("?", "&"));
		}

	}
}