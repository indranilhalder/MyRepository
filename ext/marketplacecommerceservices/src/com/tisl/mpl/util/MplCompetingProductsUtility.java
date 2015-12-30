/**
 *
 */
package com.tisl.mpl.util;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.impl.DefaultProductService;

import java.util.Collection;

import javax.annotation.Resource;

import com.tisl.mpl.constants.MplConstants;


/**
 * @author TCS
 *
 */
public class MplCompetingProductsUtility
{

	public static final int MAX_PAGE_LIMIT = 100; // should be configured

	@Resource(name = "defaultProductService")
	private DefaultProductService defaultProductService;

	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;

	public static enum ShowMode
	{
		Page, All
	}

	public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> getCompetingProducts(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		//Competing products implementation starts
		if (findAllOutOfStock(searchPageData))
		{
			ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> competingProductsSearchPageData = null;
			final PageableData competingProductsPageableData = createPageableData(0, 20, null, ShowMode.Page);
			final CategoryModel competingProductsCategory = getCompetingProductsCategory(searchPageData);
			if (competingProductsCategory != null)
			{
				competingProductsSearchPageData = productSearchFacade.categorySearch(competingProductsCategory.getCode(),
						getCompetingProductsSearchState(competingProductsCategory.getName() + MplConstants.COLON
								+ MplConstants.RELEVANCE + MplConstants.COLON + MplConstants.INSTOCKFLAG_QUERY_PATTERN),
						competingProductsPageableData);
				//model.addAttribute("competingProductsSearchPageData", competingProductsSearchPageData);
				return competingProductsSearchPageData;
			}
		}
		return null;
		//Competing products implementation end
	}

	private boolean findAllOutOfStock(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		if (searchPageData.getFacets() != null && searchPageData.getFacets().size() > 0)
		{
			final Collection<FacetData<SearchStateData>> facets = searchPageData.getFacets();

			for (final FacetData<SearchStateData> facet : facets)
			{
				if (facet.getCode().equalsIgnoreCase("inStockFlag"))
				{
					final int facetSize = facet.getValues().size();
					final String facetValue = facet.getValues().get(0).getCode();
					if (facetSize == 1 && facetValue.equalsIgnoreCase("false"))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private CategoryModel getCompetingProductsCategory(
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData)
	{
		if (searchPageData.getResults() != null && searchPageData.getResults().size() > 0)
		{
			final ProductData productData = searchPageData.getResults().get(0);
			final ProductModel productModel = defaultProductService.getProductForCode(productData.getCode());
			final Collection<CategoryModel> superCategories = productModel.getSupercategories();

			for (final CategoryModel categoryModel : superCategories)
			{
				if (categoryModel instanceof ClassificationClassModel)
				{
					continue;
				}
				else if (categoryModel.getCode().startsWith("MSH"))
				{
					return categoryModel;
				}

			}
		}
		return null;
	}

	private SearchStateData getCompetingProductsSearchState(final String searchText)
	{
		final SearchStateData competingProductsSearchState = new SearchStateData();
		final SearchQueryData competingProductsSearchQueryData = new SearchQueryData();

		competingProductsSearchQueryData.setValue(filter(searchText));
		competingProductsSearchState.setQuery(competingProductsSearchQueryData);
		competingProductsSearchQueryData.setValue(searchText);
		competingProductsSearchState.setQuery(competingProductsSearchQueryData);

		return competingProductsSearchState;
	}


	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
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

	public static String filter(final String value)
	{
		if (value == null)
		{
			return null;
		}
		String sanitized = value;
		sanitized = sanitized.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		sanitized = sanitized.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		sanitized = sanitized.replaceAll("'", "&#39;");
		sanitized = sanitized.replaceAll("eval\\((.*)\\)", "");
		sanitized = sanitized.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		return sanitized;
	}


	/**
	 * @return the defaultProductService
	 */
	public DefaultProductService getDefaultProductService()
	{
		return defaultProductService;
	}

	/**
	 * @param defaultProductService
	 *           the defaultProductService to set
	 */
	public void setDefaultProductService(final DefaultProductService defaultProductService)
	{
		this.defaultProductService = defaultProductService;
	}

	/**
	 * @return the productSearchFacade
	 */
	public ProductSearchFacade<ProductData> getProductSearchFacade()
	{
		return productSearchFacade;
	}

	/**
	 * @param productSearchFacade
	 *           the productSearchFacade to set
	 */
	public void setProductSearchFacade(final ProductSearchFacade<ProductData> productSearchFacade)
	{
		this.productSearchFacade = productSearchFacade;
	}

}


