/**
 *
 */
package com.tisl.mpl.solr.search;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchResponseSubCategoriesPopulator;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MplConstants;
import com.tisl.mpl.data.MplDepartmentHierarchyData;


/**
 * @author 360641
 *
 */

public class MplSearchResponseSubCategoriesPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
		extends
		SearchResponseSubCategoriesPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, ITEM>
{
	private CommerceCategoryService commerceCategoryService;

	@Override
	protected CommerceCategoryService getCommerceCategoryService()
	{
		return this.commerceCategoryService;
	}

	@Override
	@Required
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}

	@Override
	public void populate(
			final SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SearchResult> source,
			final ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> target)
	{
		super.populate(source, target);
		target.setDepartmentHierarchyData(
				buildDepartmentHierarchy(source.getSearchResult(), source.getRequest().getSearchQueryData()));
		target.setDepartments(buildDepartments(source.getSearchResult()));
	}

	protected MplDepartmentHierarchyData buildDepartmentHierarchy(final SearchResult solrSearchResult,
			final SolrSearchQueryData solrSearchQueryData)
	{
		final MplDepartmentHierarchyData mplDeptHierarchyData = new MplDepartmentHierarchyData();
		final List<MplDepartmentRankingComparator> departmentsHierarchyList = new ArrayList<MplDepartmentRankingComparator>();

		final Facet departmentHierarchyFacet = solrSearchResult.getFacet("departmentHierarchy");
		if ((departmentHierarchyFacet != null))
		{
			final List<FacetValue> salesHierarchyFacets = departmentHierarchyFacet.getFacetValues();

			final String categorySelected = getCategoryFilter(solrSearchQueryData);

			boolean sortByRanking = false;
			for (final FacetValue facetValue : salesHierarchyFacets)
			{
				final String currentFacetName = facetValue.getName();

				boolean constructDeptHierarchy = true;

				if (categorySelected != null && !categorySelected.isEmpty())
				{
					if (!currentFacetName.contains(categorySelected))
					{
						constructDeptHierarchy = false;
					}
				}

				if (currentFacetName.contains(MplConstants.LEVEL_THREE) && currentFacetName.contains(MplConstants.PIPE)
						&& constructDeptHierarchy)
				{

					final StringBuffer departmentHierarchyWithCount = new StringBuffer(MplConstants.EMPTY_STRING);
					int departmentRankingValue = 0;
					for (final String facet : currentFacetName.split("\\|"))
					{

						if (facet.length() > 0 && !facet.contains(MplConstants.LEVEL_ZERO))
						{
							departmentHierarchyWithCount.append(MplConstants.PIPE).append(facet);
							if (facet.contains(MplConstants.LEVEL_ONE))
							{
								final String[] facetItems = facet.split(MplConstants.COLON);
								if (facetItems.length > 3 && facetItems[4] != null && facetItems[4].length() > 0)
								{
									departmentRankingValue = Integer.parseInt(facetItems[4]);
									if (departmentRankingValue > 0)
									{
										sortByRanking = true;
									}
								}
							}
						}
					}
					final MplDepartmentRankingComparator departmentRanking = new MplDepartmentRankingComparator();
					departmentRanking.setCount(0);
					departmentRanking.setRankingValue(departmentRankingValue);
					departmentRanking.setDepartmentHierarchy(departmentHierarchyWithCount.toString());
					departmentsHierarchyList.add(departmentRanking);

				}
			}

			//Sort department hierarchy based on Ranking or Count
			if (sortByRanking)
			{
				Collections.sort(departmentsHierarchyList, MplDepartmentRankingComparator.departmentRankingByRank);
			}

			//Convert sorted response into JSON object
			final JSONArray hierarchyListJson = new JSONArray();
			for (final MplDepartmentRankingComparator deptHierarchy : departmentsHierarchyList)
			{
				hierarchyListJson.add(deptHierarchy.getDepartmentHierarchy());
			}
			// Assign Tree List and Map to mplDeptHierarchyData
			mplDeptHierarchyData.setHierarchyList(hierarchyListJson);

		}
		return mplDeptHierarchyData;
	}

	protected List<String> buildDepartments(final SearchResult solrSearchResult)
	{
		final List<String> departments = new ArrayList<String>();
		final Facet departmentsFacet = solrSearchResult.getFacet("departments");
		if ((departmentsFacet != null))
		{
			final List<FacetValue> departmentFacetList = departmentsFacet.getFacetValues();

			for (final FacetValue facetValue : departmentFacetList)
			{
				departments.add(facetValue.getName());
			}
		}

		return departments;

	}

	// Get Category if anything selected as filter
	protected String getCategoryFilter(final SolrSearchQueryData solrSearchQueryData)
	{

		String categoryCode = "";
		final List<SolrSearchQueryTermData> solrSearchQueryTermList = solrSearchQueryData.getFilterTerms();
		if (null != solrSearchQueryTermList)
		{
			for (final SolrSearchQueryTermData solrSearchQueryTerm : solrSearchQueryTermList)
			{
				if (solrSearchQueryTerm.getKey().equalsIgnoreCase("category"))
				{
					categoryCode = solrSearchQueryTerm.getValue();
					break;
				}
			}
		}
		return categoryCode;
	}

}