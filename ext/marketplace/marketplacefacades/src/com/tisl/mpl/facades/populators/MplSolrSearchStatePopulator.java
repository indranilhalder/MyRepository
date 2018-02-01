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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import reactor.util.CollectionUtils;

import com.tisl.lux.facade.CommonUtils;


/**
 */
public class MplSolrSearchStatePopulator implements Populator<SolrSearchQueryData, SearchStateData>
{
	/**
	 *
	 */
	private static final String UTF_8 = "UTF-8";
	/**
	 *
	 */

	@Autowired
	private CommonUtils commonUtils;

	private static final String PAGE_PAGE_NO_Q = "/page-{pageNo}?q=";
	private String searchPath;
	private UrlResolver<CategoryData> categoryDataUrlResolver;
	private Converter<SolrSearchQueryData, SearchQueryData> searchQueryConverter;
	private static final Logger LOG = Logger.getLogger(MplSolrSearchStatePopulator.class);
	private static final String IS_OFFER_EXISTING = "isOffersExisting";
	private static final String PROMOTED_PRODUCT = "promotedProduct";

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
			//TISPRD-2315

			if (CollectionUtils.isEmpty(source.getFilterTerms()) && source.getSort().equals("relevance"))
			{
				if (commonUtils.isLuxurySite() && source.getFreeTextSearch() != null)
				{
					target.setUrl("/search" + buildUrlQueryString(source, target));
				}
				else
				{
					target.setUrl(getCategoryUrl(source) + "/page-{pageNo}");
				}
			}
			else
			{
				populateCategorySearchUrl(source, target);
			}
		}
		else if (source.getSellerID() != null)
		{

			populateSellerListingUrl(source, target);
		}


		else if (checkIfOfferListingPage(source.getFilterTerms()) && source.getOfferID() == null || (source.getOfferID() != null))
		{
			populateOfferListingUrl(source, target);
		}
		else if (checkIfNewProductsPage(source.getFilterTerms()))
		{
			target.setUrl("/search/viewOnlineProducts" + PAGE_PAGE_NO_Q
					+ buildUrlQueryStringForNew(source, target).replace("?", "&"));
			//target.setUrl("/search/viewOnlineProducts" + buildUrlQueryString(source, target).replace("?", "&"));
		}
		else if (checkIfShopByLookPage(source.getFilterTerms()))
		{
			String lookId = "";
			for (final SolrSearchQueryTermData data : source.getFilterTerms())
			{
				if (data.getKey().equalsIgnoreCase("collectionIds"))
				{
					lookId = data.getValue();
					break;
				}
			}
			//changed with dynamic collection id key
			//target.setUrl("/collection/chef" + PAGE_PAGE_NO_Q + buildUrlQueryStringForNew(source, target).replace("?", "&"));
			target.setUrl("/CustomSkuCollection/" + lookId + PAGE_PAGE_NO_Q
					+ buildUrlQueryStringForNew(source, target).replace("?", "&"));
			//target.setUrl("/search/viewOnlineProducts" + buildUrlQueryString(source, target).replace("?", "&"));
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

	private boolean checkIfNewProductsPage(final List<SolrSearchQueryTermData> filterTerms)
	{
		// YTODO Auto-generated method stub
		boolean isNew = false;
		for (final SolrSearchQueryTermData term : filterTerms)
		{
			if (term.getKey().equalsIgnoreCase(PROMOTED_PRODUCT))
			{
				isNew = true;
				break;
			}
		}
		return isNew;

	}


	private boolean checkIfShopByLookPage(final List<SolrSearchQueryTermData> filterTerms)
	{
		// YTODO Auto-generated method stub
		boolean exists = false;
		for (final SolrSearchQueryTermData term : filterTerms)
		{
			if (term.getKey().equalsIgnoreCase("collectionIds"))
			{
				exists = true;
				break;
			}
		}
		return exists;

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
		if (commonUtils.isLuxurySite() && source.getFreeTextSearch() != null)
		{
			target.setUrl("/search" + buildUrlQueryString(source, target));
		}
		else
		{
			target.setUrl(getCategoryUrl(source) + buildUrlQueryString(source, target));
		}
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
				return PAGE_PAGE_NO_Q + URLEncoder.encode(searchQueryParam, UTF_8);
			}
			catch (final UnsupportedEncodingException e)
			{
				return PAGE_PAGE_NO_Q + StringEscapeUtils.escapeHtml(searchQueryParam);
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
				encodedOfferId = URLEncoder.encode(source.getOfferID(), UTF_8);
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
			//target.setUrl("/o/viewAllOffers" + buildUrlQueryString(source, target).replace("?", "&"));

			//TISPRD-1867
			target.setUrl("/view-all-offers" + PAGE_PAGE_NO_Q + buildUrlQueryStringForOffer(source, target).replace("?", "&"));
			//target.setUrl("/view-all-offers" + buildUrlQueryString(source, target).replace("?", "&"));
			//			target.setUrl("/o/viewAllOffers?offer=" + encodedOfferId + "?searchCategory=" + offerCategoryID
			//					+ buildUrlQueryString(source, target).replace("?", "&"));
		}

	}

	protected String buildUrlQueryStringForNew(final SolrSearchQueryData source, final SearchStateData target)
	{
		final String searchQueryParam = target.getQuery().getValue();
		if (StringUtils.isNotBlank(searchQueryParam))
		{
			try
			{
				return URLEncoder.encode(searchQueryParam, UTF_8);
			}
			catch (final UnsupportedEncodingException e)
			{
				return StringEscapeUtils.escapeHtml(searchQueryParam);
			}
		}
		return "";
	}

	protected String buildUrlQueryStringForOffer(final SolrSearchQueryData source, final SearchStateData target)
	{
		final String searchQueryParam = target.getQuery().getValue();
		if (StringUtils.isNotBlank(searchQueryParam))
		{
			try
			{
				return URLEncoder.encode(searchQueryParam, UTF_8);
			}
			catch (final UnsupportedEncodingException e)
			{
				return StringEscapeUtils.escapeHtml(searchQueryParam);
			}
		}
		return "";
	}
}
