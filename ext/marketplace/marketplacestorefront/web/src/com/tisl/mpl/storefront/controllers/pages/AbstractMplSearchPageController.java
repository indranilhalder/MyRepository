/**
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;


/**
 * @author TCS
 *
 */

public abstract class AbstractMplSearchPageController extends AbstractPageController
{
	@Autowired
	private ConfigurationService configurationService;
	public static int MAX_PAGE_LIMIT = 0;
	public static final String MAX_PAGE_LIMIT_TOTAL_ORDER_COUNT_DISPLAY = "orderHistory.max.page.limit.count.display";
	private static final String PAGINATION_NUMBER_OF_RESULTS_COUNT = "orderHistory.pagination.number.results.count";
	private static final String PAGINATION_NUMBER_OF_RESULTS_COUNT_COUPON = "coupon.pagination.number.results.count";
	private static final Logger LOG = Logger.getLogger(AbstractMplSearchPageController.class);

	public static enum ShowMode
	{
		Page, All
	}

	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		getMaxPageLimit();
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	/**
	 *
	 */
	private void getMaxPageLimit()
	{
		MAX_PAGE_LIMIT = Integer.parseInt(configurationService.getConfiguration().getString(
				MAX_PAGE_LIMIT_TOTAL_ORDER_COUNT_DISPLAY, "500"));
	}

	protected PaginationData createEmptyPagination()
	{
		final PaginationData paginationData = new PaginationData();
		paginationData.setCurrentPage(0);
		paginationData.setNumberOfPages(0);
		paginationData.setPageSize(1);
		paginationData.setTotalNumberOfResults(0);
		return paginationData;
	}


	/**
	 * Special case, when total number of results > {@link #MAX_PAGE_LIMIT}
	 */
	protected boolean isShowAllAllowed(final SearchPageData<?> searchPageData)
	{
		getMaxPageLimit();
		return searchPageData.getPagination().getNumberOfPages() > 1
				&& searchPageData.getPagination().getTotalNumberOfResults() < MAX_PAGE_LIMIT;
	}

	protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		final int totalNoOfPages = searchPageData.getPagination().getNumberOfPages();
		int numberPagesShown = getSiteConfigService().getInt(PAGINATION_NUMBER_OF_RESULTS_COUNT, 5);
		if (numberPagesShown > totalNoOfPages)
		{
			LOG.debug("*************** Order History : Set Number Of Pages Shown as 5 as its found greater than Total No Of Pages ***********");
			numberPagesShown = 5;
		}

		model.addAttribute("numberPagesShown", Integer.valueOf(numberPagesShown));
		model.addAttribute("searchPageData", searchPageData);
		model.addAttribute("isShowAllAllowed", calculateShowAll(searchPageData, showMode));
		model.addAttribute("isShowPageAllowed", calculateShowPaged(searchPageData, showMode));
	}


	/**
	 * @description This is populating the data of coupon to create pagination
	 * @param model
	 * @param searchPageData
	 * @param showMode
	 */

	protected void populateModelForCoupon(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		final int totalNoOfPages = searchPageData.getPagination().getNumberOfPages();
		int numberPagesShown = getSiteConfigService().getInt(PAGINATION_NUMBER_OF_RESULTS_COUNT_COUPON, 2);
		if (numberPagesShown > totalNoOfPages)
		{
			LOG.debug("*************** coupons : Set Number Of Pages Shown as 2 as its found greater than Total No Of Pages ***********");
			numberPagesShown = 5;
		}

		model.addAttribute("numberPagesShown", Integer.valueOf(numberPagesShown));
		model.addAttribute("searchPageData", searchPageData);
		model.addAttribute("isShowAllAllowed", calculateShowAll(searchPageData, showMode));
		model.addAttribute("isShowPageAllowed", calculateShowPaged(searchPageData, showMode));
	}

	protected Boolean calculateShowAll(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean.valueOf((showMode != ShowMode.All && //
				searchPageData.getPagination().getTotalNumberOfResults() > searchPageData.getPagination().getPageSize())
				&& isShowAllAllowed(searchPageData));
	}

	protected Boolean calculateShowPaged(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean
				.valueOf(showMode == ShowMode.All
						&& (searchPageData.getPagination().getNumberOfPages() > 1 || searchPageData.getPagination().getPageSize() == getMaxSearchPageSize()));
	}

	protected Map<String, FacetData<SearchStateData>> convertBreadcrumbsToFacets(
			final List<BreadcrumbData<SearchStateData>> breadcrumbs)
	{
		final Map<String, FacetData<SearchStateData>> facets = new HashMap<>();
		if (breadcrumbs == null)
		{
			return facets;
		}

		for (final BreadcrumbData<SearchStateData> breadcrumb : breadcrumbs)
		{
			FacetData<SearchStateData> facet = facets.get(breadcrumb.getFacetName());
			if (facet == null)
			{
				facet = new FacetData<>();
				facet.setName(breadcrumb.getFacetName());
				facet.setCode(breadcrumb.getFacetCode());
				facets.put(breadcrumb.getFacetName(), facet);
			}

			final List<FacetValueData<SearchStateData>> facetValues = facet.getValues() != null ? new ArrayList<>(facet.getValues())
					: new ArrayList<FacetValueData<SearchStateData>>();
			final FacetValueData<SearchStateData> facetValueData = new FacetValueData<>();
			facetValueData.setSelected(true);
			facetValueData.setName(breadcrumb.getFacetValueName());
			facetValueData.setCode(breadcrumb.getFacetValueCode());
			facetValueData.setCount(0L);
			facetValueData.setQuery(breadcrumb.getRemoveQuery());
			facetValues.add(facetValueData);
			facet.setValues(facetValues);
		}
		return facets;
	}

	protected List<FacetData<SearchStateData>> refineFacets(final List<FacetData<SearchStateData>> facets,
			final Map<String, FacetData<SearchStateData>> selectedFacets)
	{
		final List<FacetData<SearchStateData>> refinedFacets = new ArrayList<>();
		for (final FacetData<SearchStateData> facet : facets)
		{
			facet.setTopValues(Collections.<FacetValueData<SearchStateData>> emptyList());
			final List<FacetValueData<SearchStateData>> facetValues = new ArrayList<>(facet.getValues());

			for (final FacetValueData<SearchStateData> facetValueData : facetValues)
			{
				if (selectedFacets.containsKey(facet.getName()))
				{
					final boolean foundFacetWithName = existsFacetValueWithName(selectedFacets.get(facet.getName()).getValues(),
							facetValueData.getName());
					facetValueData.setSelected(foundFacetWithName);
				}
			}

			if (selectedFacets.containsKey(facet.getName()))
			{
				facetValues.addAll(selectedFacets.get(facet.getName()).getValues());
				selectedFacets.remove(facet.getName());
			}

			refinedFacets.add(facet);
		}

		if (!selectedFacets.isEmpty())
		{
			refinedFacets.addAll(selectedFacets.values());
		}

		return refinedFacets;
	}

	protected boolean existsFacetValueWithName(final List<FacetValueData<SearchStateData>> values, final String name)
	{
		if (name != null && !name.isEmpty() && values != null && !values.isEmpty())
		{
			for (final FacetValueData<SearchStateData> value : values)
			{
				if (name.equals(value.getName()))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get the default search page size.
	 *
	 * @return the number of results per page, <tt>0</tt> (zero) indicated 'default' size should be used
	 */
	protected int getSearchPageSize()
	{
		return getSiteConfigService().getInt("storefront.search.pageSize", 0);
	}

	protected int getMaxSearchPageSize()
	{
		getMaxPageLimit();
		return MAX_PAGE_LIMIT;
	}


	public static class SearchResultsData<RESULT>
	{
		private List<RESULT> results;
		private PaginationData pagination;

		public List<RESULT> getResults()
		{
			return results;
		}

		public void setResults(final List<RESULT> results)
		{
			this.results = results;
		}

		public PaginationData getPagination()
		{
			return pagination;
		}

		public void setPagination(final PaginationData pagination)
		{
			this.pagination = pagination;
		}
	}

}
