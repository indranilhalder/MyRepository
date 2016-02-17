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
package com.tisl.mpl.v2.helper;

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductCategorySearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductSearchPageWsDTO;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.solrfacet.search.MplProductSearchFacade;
import com.tisl.mpl.util.ws.SearchQueryCodec;
import com.tisl.mpl.v2.controller.BaseController.ShowMode;
import com.tisl.mpl.wsdto.ProductSearchPageWsDto;


@Component
public class ProductsHelper extends AbstractHelper
{
	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;
	@Resource(name = "cwsSearchQueryCodec")
	private SearchQueryCodec<SolrSearchQueryData> searchQueryCodec;
	@Resource(name = "solrSearchStateConverter")
	private Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter;


	@Cacheable(value = MarketplacewebservicesConstants.PRODSEARCHCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'DTO',#query,#currentPage,#pageSize,#sort,#fields)")
	public ProductSearchPageWsDTO searchProducts(final String query, final int currentPage, final int pageSize, final String sort,
			final String fields)
	{
		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = searchProducts(query, currentPage, pageSize, sort);
		if (sourceResult instanceof ProductCategorySearchPageData)
		{
			return dataMapper.map(sourceResult, ProductCategorySearchPageWsDTO.class, fields);
		}

		return dataMapper.map(sourceResult, ProductSearchPageWsDTO.class, fields);
	}

	@Cacheable(value = MarketplacewebservicesConstants.PRODSEARCHCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'Data',#query,#currentPage,#pageSize,#sort)")
	public ProductSearchPageData<SearchStateData, ProductData> searchProducts(final String query, final int currentPage,
			final int pageSize, final String sort)
	{
		final SolrSearchQueryData searchQueryData = searchQueryCodec.decodeQuery(query);
		final PageableData pageable = createPageableData(currentPage, pageSize, sort);

		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = productSearchFacade
				.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
		return sourceResult;
	}

	/**
	 * @description method is called to search the available products
	 * @param query
	 * @param currentPage
	 * @param pageSize
	 * @param showMode
	 * @param sort
	 * @param fields
	 * @return ProductSearchPageWsDTO
	 */
	@Cacheable(value = MarketplacewebservicesConstants.PRODSEARCHCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'DTO',#query,#currentPage,#pageSize,#sort,#fields)")
	public ProductSearchPageWsDTO searchProductsfacatedto(final String query, final int currentPage, final int pageSize,
			final ShowMode showMode, final String sort, final String fields)
	{
		final ProductSearchPageData<SearchStateData, ProductData> sourceResult2 = searchProductslist(query, currentPage, pageSize,
				showMode, sort);
		if (sourceResult2 instanceof ProductCategorySearchPageData)
		{
			return dataMapper.map(sourceResult2, ProductSearchPageWsDTO.class, fields);

		}

		return dataMapper.map(sourceResult2, ProductSearchPageWsDTO.class, fields);
	}

	/**
	 * @description method is called to search the available products
	 * @param searchQuery
	 * @param page
	 * @param pageSize
	 * @param showMode
	 * @param sortCode
	 * @return ProductSearchPageData
	 */
	@Cacheable(value = MarketplacewebservicesConstants.PRODSEARCHCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'Data',#query,#currentPage,#pageSize,#sort)")
	public ProductSearchPageData<SearchStateData, ProductData> searchProductslist(final String searchQuery, final int page,
			final int pageSize, final ShowMode showMode, final String sortCode)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		final ProductSearchPageData<SearchStateData, ProductData> sourceResultproduct = productSearchFacade.textSearch(searchState,
				pageableData);
		return sourceResultproduct;
	}

	/**
	 * @description method is called to create Pageable data
	 * @param pageNumber
	 * @param pageSize
	 * @param sortCode
	 * @param showMode
	 * @return PageableData
	 */
	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MarketplacecommerceservicesConstants.MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	@Cacheable(value = MarketplacewebservicesConstants.PRODSEARCHCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'DTO',#query,#currentPage,#pageSize,#sort,#fields)")
	public ProductSearchPageWsDto searchProductsForCategory(final String categoryId, final int currentPage, final int pageSize,
			final String sort, final String fields)
	{
		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = searchProductsForCategory(categoryId, currentPage,
				pageSize, sort);
		if (sourceResult instanceof ProductCategorySearchPageData)
		{
			return dataMapper.map(sourceResult, ProductSearchPageWsDto.class, fields);
		}

		return dataMapper.map(sourceResult, ProductSearchPageWsDto.class, fields);
	}

	//@Cacheable(value = "productSearchCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'Data',#query,#currentPage,#pageSize,#sort)")
	public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchProductsForCategory(
			final String categoryId, final int currentPage, final int pageSize, final String sort)
	{
		final SolrSearchQueryData searchQueryData = new SolrSearchQueryData();
		searchQueryData.setCategoryCode(categoryId);
		final PageableData pageable = createPageableData(currentPage, pageSize, sort);

		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = ((MplProductSearchFacade) productSearchFacade)
				.mplProductSearchForWebservice(solrSearchStateConverter.convert(searchQueryData), pageable, categoryId);
		return (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) sourceResult;
	}

	//@Cacheable(value = "productSearchCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'Data',#query,#currentPage,#pageSize,#sort)")
	public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchProductsForSeller(final String sellerId,
			final int currentPage, final int pageSize, final String sort)
	{
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchState.setQuery(searchQueryData);
		final PageableData pageable = createPageableData(currentPage, pageSize, sort);

		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = ((MplProductSearchFacade) productSearchFacade)
				.dropDownSearchForSeller(searchState, sellerId, MarketplaceCoreConstants.SELLER_ID, pageable);
		return (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) sourceResult;
	}

	//@Cacheable(value = "productSearchCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,true,'Data',#query,#currentPage,#pageSize,#sort)")
	public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchProductsForOffer(final String offerId,
			final int currentPage, final int pageSize, final String sort, final String categoryId, final String channel)
	{
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchState.setQuery(searchQueryData);
		final PageableData pageable = createPageableData(currentPage, pageSize, sort);

		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = ((MplProductSearchFacade) productSearchFacade)
				.dropDownSearchForOffer(searchState, offerId, pageable, categoryId, channel);
		return (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) sourceResult;
	}
}
