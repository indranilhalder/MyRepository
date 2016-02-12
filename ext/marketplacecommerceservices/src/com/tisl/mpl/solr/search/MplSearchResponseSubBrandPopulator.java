package com.tisl.mpl.solr.search;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;



public class MplSearchResponseSubBrandPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
		implements
		Populator<SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel>>
{

	private CommerceCategoryService commerceCategoryService;

	/**
	 * @return the commerceCategoryService
	 */
	public CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	/**
	 * @param commerceCategoryService
	 *           the commerceCategoryService to set
	 */
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SearchResult> source,
			final ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> target) throws ConversionException
	{
		// YTODO Auto-generated method stub
		target.setAllBrand(buildSubBrands(source.getSearchResult()));
		target.setAllSeller(buildSubSeller(source.getSearchResult()));
		target.setSnsCategories(buildCategories(source.getSearchResult()));
		target.setDeptType(builddeptType(source.getSearchResult()));
		target.setMicrositeSnsCategories(buildMicrositeSNSCategories(source.getSearchResult()));

	}

	/**
	 * @param categoryCode
	 * @param searchResult
	 * @return
	 */
	private List<String> buildSubSeller(final SearchResult solrSearchResult)
	{
		// YTODO Auto-generated method stub

		final Facet sellerPathFacet = solrSearchResult.getFacet(MarketplacecommerceservicesConstants.SELLER);
		final List<String> allSeller = new ArrayList<String>();
		if ((sellerPathFacet != null) && !(sellerPathFacet.getFacetValues().isEmpty()))
		{

			for (final FacetValue facetValue : sellerPathFacet.getFacetValues())
			{
				allSeller.add(facetValue.getName());
			}
		}

		return allSeller;
	}

	/**
	 * @param categoryCode
	 * @param searchResult
	 * @return
	 */
	/*
	 * private List<String> buildSubBrands(final SearchResult solrSearchResult) { // YTODO Auto-generated method stub
	 * final Facet brandPathFacet = solrSearchResult.getFacet(MarketplacecommerceservicesConstants.BRAND); final
	 * List<String> allBrands = new ArrayList<String>(); if ((brandPathFacet != null) &&
	 * !(brandPathFacet.getFacetValues().isEmpty())) {
	 *
	 * for (final FacetValue facetValue : brandPathFacet.getFacetValues()) { allBrands.add(facetValue.getName()); }
	 *
	 * }
	 *
	 *
	 * return allBrands; }
	 */

	private List<CategoryModel> buildSubBrands(final SearchResult solrSearchResult)
	{
		// YTODO Auto-generated method stub
		final Facet brandFacet = solrSearchResult.getFacet(MarketplacecommerceservicesConstants.BRAND);
		final List<CategoryModel> allBrands = new ArrayList<CategoryModel>();
		if ((brandFacet != null) && !(brandFacet.getFacetValues().isEmpty()))
		{

			for (final FacetValue facetValue : brandFacet.getFacetValues())
			{
				if (facetValue != null && facetValue.getName() != null)
				{
					final CategoryModel categoryModel = getCommerceCategoryService().getCategoryForCode(facetValue.getName());

					//adding category to brands
					//allBrands = addCategoryToBrand(categoryModel);
					addCategoryToBrand(categoryModel, allBrands);

				}
			}
		}

		return allBrands;
	}

	/**
	 *
	 * @param categoryModel
	 * @return List<CategoryModel>
	 */
	private List<CategoryModel> addCategoryToBrand(final CategoryModel categoryModel, final List<CategoryModel> allBrands)
	{
		if (categoryModel != null)
		{
			allBrands.add(categoryModel);
		}
		return allBrands;
	}


	/**
	 * @param categoryCode
	 * @param searchResult
	 * @return
	 */
	private List<CategoryModel> buildCategories(final SearchResult solrSearchResult)
	{
		// YTODO Auto-generated method stub
		final Facet snsCategoryPathFacet = solrSearchResult.getFacet(MarketplacecommerceservicesConstants.SNS_CATEGORY);
		final List<CategoryModel> allCategories = new ArrayList<CategoryModel>();
		if ((snsCategoryPathFacet != null) && !(snsCategoryPathFacet.getFacetValues().isEmpty()))
		{

			for (final FacetValue facetValue : snsCategoryPathFacet.getFacetValues())
			{
				allCategories.add(getCommerceCategoryService().getCategoryForCode(facetValue.getName()));
			}

		}


		return allCategories;
	}

	/**
	 * @param categoryCode
	 * @param searchResult
	 * @return
	 */
	private List<CategoryModel> buildMicrositeSNSCategories(final SearchResult solrSearchResult)
	{
		// YTODO Auto-generated method stub
		final Facet snsCategoryPathFacet = solrSearchResult.getFacet(MarketplacecommerceservicesConstants.MICROSITE_SNS_CATEGORY);
		final List<CategoryModel> allCategories = new ArrayList<CategoryModel>();
		if ((snsCategoryPathFacet != null) && !(snsCategoryPathFacet.getFacetValues().isEmpty()))
		{

			for (final FacetValue facetValue : snsCategoryPathFacet.getFacetValues())
			{
				allCategories.add(getCommerceCategoryService().getCategoryForCode(facetValue.getName()));
			}

		}


		return allCategories;
	}

	private String builddeptType(final SearchResult solrSearchResult)
	{
		// YTODO Auto-generated method stub

		String deptType = "";
		final Facet deptFacet = solrSearchResult.getFacet(MarketplacecommerceservicesConstants.DEPT_TYPE);
		if ((deptFacet != null) && !(deptFacet.getFacetValues().isEmpty()))
		{

			if (deptFacet.getFacetValues().size() == 1)
			{
				if (deptFacet.getFacetValues().get(0).getName().equalsIgnoreCase("Apparel"))
				{
					deptType = "Apparel";

				}
				else if (deptFacet.getFacetValues().get(0).getName().equalsIgnoreCase("Electronics"))
				{
					deptType = "Electronics";


				}
			}
			else if (deptFacet.getFacetValues().size() > 1)
			{

				deptType = "Generic";
			}

		}


		return deptType;
	}

}