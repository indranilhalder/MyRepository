/**
 *
 */
package com.tisl.mpl.solrfacet.search;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;

import com.tisl.mpl.facades.product.data.CategoryData;


/**
 * @author 314180
 *
 */
public interface MplProductSearchFacade<ITEM extends ProductData> extends ProductSearchFacade
{
	/*
	 * Returns Products based on the search query.
	 *
	 * @return the search results
	 */
	/**
	 * Mpl Product search.
	 *
	 * @param searchState
	 *           the search state
	 * @param pageableData
	 *           the pageable data
	 * @return the product search page data
	 */
	ProductSearchPageData<SearchStateData, ITEM> mplProductSearch(SearchStateData searchState, PageableData pageableData);

	ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> dropDownSearch(final SearchStateData searchState,
			final String brandCode, final String type, final PageableData pageableData);


	ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> conceirgeSearch(final String age, final String categoryCode,
			final String reasonOrEvent, final SearchStateData searchState, final PageableData pageableData);

	public ProductSearchPageData<SearchStateData, ITEM> mplProductSearchForWebservice(final SearchStateData searchState,
			final PageableData pageableData, final String categoryCode);

	ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> sellerCategorySearch(String categoryCode, String sellerId,
			SearchStateData searchState, PageableData pageableData);

	SolrSearchQueryData decodeSellerState(SearchStateData searchState, String categoryCode, String sellerId);

	ProductCategorySearchPageData dropDownSearchForSeller(SearchStateData searchState, String sellerId, String type,
			PageableData pageableData);

	ProductCategorySearchPageData collectionSearch(String collectionId, SearchStateData searchState, PageableData pageableData);

	//TODO : To be removed by Tharun after making age as String for Mobile
	ProductCategorySearchPageData conceirgeSearch(int age, String categoryCode, String reasonOrEvent, SearchStateData searchState,
			PageableData pageableData);

	ProductCategorySearchPageData dropDownSearchForSellerListing(SearchStateData searchState, String sellerId,
			PageableData pageableData);

	ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> searchCategorySearch(String categoryCode,
			SearchStateData searchState, PageableData pageableData);

	ProductCategorySearchPageData dropDownSearchForOffer(SearchStateData searchState, String offerId, PageableData pageableData,
			final String categoryCode, final String channel);

	ProductCategorySearchPageData dropDownSearchForOfferListing(SearchStateData searchState, String offerId,
			PageableData pageableData, final String categoryCode);
}
