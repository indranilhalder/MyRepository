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

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.SpellingSuggestionData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.lux.facade.CommonUtils;


/**
 */
public class MplProductCategorySearchPagePopulator<QUERY, STATE, RESULT, ITEM extends ProductData, SCAT, CATEGORY> implements
		Populator<ProductCategorySearchPageData<QUERY, RESULT, SCAT>, ProductCategorySearchPageData<STATE, ITEM, CATEGORY>>
{

	private Converter<QUERY, STATE> searchStateConverter;
	private Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> breadcrumbConverter;
	private Converter<FacetData<QUERY>, FacetData<STATE>> facetConverter;
	private Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> spellingSuggestionConverter;
	private Converter<RESULT, ITEM> searchResultProductConverter;
	private Converter<SCAT, CATEGORY> categoryConverter;
	private static final String DEFAULTCOLLOCTIONID = "collectionIds";

	protected Converter<QUERY, STATE> getSearchStateConverter()
	{
		return searchStateConverter;
	}

	@Required
	public void setSearchStateConverter(final Converter<QUERY, STATE> searchStateConverter)
	{
		this.searchStateConverter = searchStateConverter;
	}

	protected Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> getBreadcrumbConverter()
	{
		return breadcrumbConverter;
	}

	@Required
	public void setBreadcrumbConverter(final Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> breadcrumbConverter)
	{
		this.breadcrumbConverter = breadcrumbConverter;
	}

	protected Converter<FacetData<QUERY>, FacetData<STATE>> getFacetConverter()
	{
		return facetConverter;
	}

	@Required
	public void setFacetConverter(final Converter<FacetData<QUERY>, FacetData<STATE>> facetConverter)
	{
		this.facetConverter = facetConverter;
	}

	protected Converter<RESULT, ITEM> getSearchResultProductConverter()
	{
		return searchResultProductConverter;
	}

	@Required
	public void setSearchResultProductConverter(final Converter<RESULT, ITEM> searchResultProductConverter)
	{
		this.searchResultProductConverter = searchResultProductConverter;
	}

	protected Converter<SCAT, CATEGORY> getCategoryConverter()
	{
		return categoryConverter;
	}

	@Required
	public void setCategoryConverter(final Converter<SCAT, CATEGORY> categoryConverter)
	{
		this.categoryConverter = categoryConverter;
	}

	protected Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> getSpellingSuggestionConverter()
	{
		return spellingSuggestionConverter;
	}

	@Required
	public void setSpellingSuggestionConverter(
			final Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> spellingSuggestionConverter)
	{
		this.spellingSuggestionConverter = spellingSuggestionConverter;
	}

	@Autowired
	private CommonUtils commonUtils;

	@Override
	public void populate(final ProductCategorySearchPageData<QUERY, RESULT, SCAT> source,
			final ProductCategorySearchPageData<STATE, ITEM, CATEGORY> target)
	{
		target.setFreeTextSearch(source.getFreeTextSearch());
		target.setCategoryCode(source.getCategoryCode());
		//target.setAllBrand(source.getAllBrand());
		if (commonUtils.isLuxurySite())
		{

			source.getBreadcrumbs().removeIf(
					(final BreadcrumbData breadcrumbData) -> breadcrumbData.getFacetName().equalsIgnoreCase(DEFAULTCOLLOCTIONID)
							|| (breadcrumbData.getFacetName().equalsIgnoreCase("category") && Config.getString("luxury.salescategories",
									"LSH1,ISH1").contains(breadcrumbData.getFacetValueCode())));

		}


		if (source.getSellerID() != null)
		{
			target.setSellerID(source.getSellerID());
		}

		if (source.getOfferID() != null)
		{
			target.setOfferID(source.getOfferID());
		}

		if (source.getOfferCategoryID() != null)
		{
			target.setOfferCategoryID(source.getOfferCategoryID());
		}

		target.setAllSeller(source.getAllSeller());
		target.setDepartmentHierarchyData(source.getDepartmentHierarchyData());
		if (source.getSubCategories() != null)
		{
			target.setSubCategories(Converters.convertAll(source.getSubCategories(), getCategoryConverter()));
		}

		if (source.getBreadcrumbs() != null)
		{
			target.setBreadcrumbs(Converters.convertAll(source.getBreadcrumbs(), getBreadcrumbConverter()));
		}

		target.setCurrentQuery(getSearchStateConverter().convert(source.getCurrentQuery()));

		if (source.getFacets() != null)
		{
			target.setFacets(Converters.convertAll(source.getFacets(), getFacetConverter()));
		}

		target.setPagination(source.getPagination());

		if (source.getResults() != null)
		{
			target.setResults(Converters.convertAll(source.getResults(), getSearchResultProductConverter()));
		}

		target.setSorts(source.getSorts());

		if (source.getSpellingSuggestion() != null)
		{
			target.setSpellingSuggestion(getSpellingSuggestionConverter().convert(source.getSpellingSuggestion()));
		}

		target.setKeywordRedirectUrl(source.getKeywordRedirectUrl());
		/*
		 * if (source.getSnsCategories() != null) {
		 * target.setSnsCategories(Converters.convertAll(source.getSnsCategories(), getCategoryConverter())); }
		 */


		if (source.getSnsCategories() != null)
		{
			target.setSnsCategories(source.getSnsCategories());
		}

		if (source.getAllBrand() != null)
		{
			target.setAllBrand(source.getAllBrand());
		}

		if (source.getDeptType() != null)
		{
			target.setDeptType(source.getDeptType());
		}

		if (source.getDepartments() != null)
		{
			target.setDepartments(source.getDepartments());
		}

		if (source.getMicrositeSnsCategories() != null)
		{
			target.setMicrositeSnsCategories(Converters.convertAll(source.getMicrositeSnsCategories(), getCategoryConverter()));
		}
	}
}
