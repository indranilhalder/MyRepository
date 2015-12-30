/**
 *
 */
package com.tisl.mpl.solrfacet.search.impl;



import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commercefacades.search.solrfacetsearch.impl.DefaultSolrProductSearchFacade;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commerceservices.threadcontext.ThreadContextService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.constants.MarketplaceCoreConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.data.CategoryData;
import com.tisl.mpl.solrfacet.search.MplProductSearchFacade;
import com.tisl.mpl.solrfacet.search.service.MplProductSearchService;


/**
 * @author 314180
 *
 */
public class DefaultMplProductSearchFacade<ITEM extends ProductData> extends DefaultSolrProductSearchFacade
		implements MplProductSearchFacade
{
	private MplProductSearchService<SolrSearchQueryData, SearchResultValueData, ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>> mplProductSearchService;

	/**
	 * @return the mplProductSearchService
	 */
	public MplProductSearchService<SolrSearchQueryData, SearchResultValueData, ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>> getMplProductSearchService()
	{
		return mplProductSearchService;
	}

	/**
	 * @param mplProductSearchService
	 *           the mplProductSearchService to set
	 */
	public void setMplProductSearchService(
			final MplProductSearchService<SolrSearchQueryData, SearchResultValueData, ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>> mplProductSearchService)
	{
		this.mplProductSearchService = mplProductSearchService;
	}

	/**
	 *
	 * @param searchState
	 * @param pageableData
	 * @return ProductSearchPageData
	 */
	@Override
	public ProductSearchPageData mplProductSearch(final SearchStateData searchState, final PageableData pageableData)
	{
		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductSearchPageData<SearchStateData, ITEM> execute()
					{
						return (ProductSearchPageData<SearchStateData, ITEM>) getProductCategorySearchPageConverter()
								.convert(getMplProductSearchService().mplProductSearch(decodeState(searchState), pageableData));
					}
				});
	}

	/**
	 * @param searchState
	 * @return SolrSearchQueryData
	 */
	protected SolrSearchQueryData decodeState(final SearchStateData searchState)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		return searchQueryData;
	}

	/**
	 * @param searchState
	 * @param brandCode
	 * @param type
	 * @param pageableData
	 * @return ProductCategorySearchPageData
	 * @description The method is used for searching data depending on the search dropdown ,it takes
	 *              searchdata,brandcode,type and pageabledata as parameters
	 */
	@Override
	public ProductCategorySearchPageData dropDownSearch(final SearchStateData searchState, final String brandCode,
			final String type, final PageableData pageableData)
	{
		// YTODO Auto-generated method stub

		try
		{

			return getThreadContextService().executeInContext(
					new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
					{
						@Override
						public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
						{
							return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
									.convert(getProductSearchService().searchAgain(decodeStateDropDown(searchState, brandCode, type),
											pageableData));
						}
					});


		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 *
	 * @param searchState
	 * @param code
	 * @param type
	 * @return SolrSearchQueryData
	 */
	protected final SolrSearchQueryData decodeStateDropDown(final SearchStateData searchState, final String code,
			final String type)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		final List<SolrSearchQueryTermData> filterTerms = searchQueryData.getFilterTerms();
		final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
		if (code != null && type != null)
		{
			if (type.equalsIgnoreCase(MarketplaceCoreConstants.BRAND))
			{
				solrSearchQueryTermData.setKey(MarketplaceCoreConstants.BRAND);
			}
			else if (type.equalsIgnoreCase(MarketplaceCoreConstants.SNS_SELLER_ID))
			{
				solrSearchQueryTermData.setKey(MarketplaceCoreConstants.SNS_SELLER_ID);
			}
			else if (type.equalsIgnoreCase(MarketplaceCoreConstants.NAME))
			{
				solrSearchQueryTermData.setKey(MarketplaceCoreConstants.NAME);
			}
			solrSearchQueryTermData.setValue(code);
			filterTerms.add(solrSearchQueryTermData);
			searchQueryData.setFilterTerms(filterTerms);
			searchQueryData.setSns(searchState.isSns());
		}
		return searchQueryData;
	}

	/**
	 * @param age
	 * @param categoryCode
	 * @param reasonOrEvent
	 * @param searchState
	 * @param pageableData
	 * @return ProductCategorySearchPageData
	 * @description The method is used for searching for a reason/event against a particular categoryCode. Method takes
	 *              age,categoryCode,event,searchstate,pageabledata as parameters
	 *
	 */
	@Override
	public ProductCategorySearchPageData conceirgeSearch(final String age, final String categoryCode, final String reasonOrEvent,
			final SearchStateData searchState, final PageableData pageableData)
	{

		Assert.notNull(searchState, MarketplacecommerceservicesConstants.SEARCH_STATE_DATA_MSG);
		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
					{
						return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
								.convert(getProductSearchService()
										.searchAgain(decodeStateConceirge(searchState, categoryCode, age, reasonOrEvent), pageableData));
					}
				});




	}

	/**
	 *
	 * @param searchState
	 * @param categoryCode
	 * @param age
	 * @param reasonOrEvent
	 * @return SolrSearchQueryData
	 */
	protected final SolrSearchQueryData decodeStateConceirge(final SearchStateData searchState, final String categoryCode,
			final String age, final String reasonOrEvent)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		if (categoryCode != null)
		{

			final List<SolrSearchQueryTermData> terms = new ArrayList<SolrSearchQueryTermData>();

			searchQueryData.setCategoryCode(categoryCode);
			if (reasonOrEvent != null)
			{
				final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
				solrSearchQueryTermData.setKey(MarketplaceCoreConstants.OCCASSION);
				solrSearchQueryTermData.setValue(reasonOrEvent);
				terms.add(solrSearchQueryTermData);
			}


			if (age != null)
			{
				if (age.contains(","))
				{
					final String[] ageSplit = age.split(",");

					for (int i = 0; i < ageSplit.length; i++)
					{
						final String age_i = ageSplit[i];
						final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
						solrSearchQueryTermData.setKey("age");
						solrSearchQueryTermData.setValue(age_i);
						terms.add(solrSearchQueryTermData);
					}

				}
				else
				{
					final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
					solrSearchQueryTermData.setKey("age");
					solrSearchQueryTermData.setValue(age);
					terms.add(solrSearchQueryTermData);
				}

			}


			searchQueryData.setFilterTerms(terms);
		}
		return searchQueryData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.solrfacet.search.MplProductSearchFacade#mplProductSearch(de.hybris.platform.commercefacades.search.
	 * data.SearchStateData, de.hybris.platform.commerceservices.search.pagedata.PageableData, java.lang.String)
	 */
	@Override
	public ProductSearchPageData<SearchStateData, ITEM> mplProductSearchForWebservice(final SearchStateData searchState,
			final PageableData pageableData, final String categoryCode)
	{
		Assert.notNull(searchState, MarketplacecommerceservicesConstants.SEARCH_STATE_DATA_MSG);

		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductSearchPageData<SearchStateData, ITEM> execute()
					{
						return (ProductSearchPageData<SearchStateData, ITEM>) getProductCategorySearchPageConverter()
								.convert(getProductSearchService().searchAgain(decodeState(searchState, categoryCode), pageableData));
					}
				});
	}



	/**
	 * @param searchState
	 * @param code
	 * @param type
	 * @param pageableData
	 * @return ProductCategorySearchPageData
	 * @description The method is used for searching data depending on the search dropdown ,it takes
	 *              searchdata,sellerId,type and pageabledata as parameters
	 */
	@Override
	public ProductCategorySearchPageData dropDownSearchForSeller(final SearchStateData searchState, final String sellerId,
			final String type, final PageableData pageableData)
	{
		// YTODO Auto-generated method stub
		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
					{
						return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
								.convert(getProductSearchService().searchAgain(decodeSellerStateDropDown(searchState, sellerId),
										pageableData));
					}
				});
	}


	/**
	 *
	 * @param searchState
	 * @param code
	 * @param type
	 * @return SolrSearchQueryData
	 */
	protected final SolrSearchQueryData decodeSellerStateDropDown(final SearchStateData searchState, final String sellerId)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());

		if (sellerId != null)
		{


			final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
			solrSearchQueryTermData.setKey("sellerId");
			solrSearchQueryTermData.setValue(sellerId);
			searchQueryData.setFilterTerms(Collections.singletonList(solrSearchQueryTermData));

			searchQueryData.setSellerID(sellerId);
			//	searchQueryData.setSns(searchState.isSns());
		}


		return searchQueryData;
	}

	/**
	 * @param searchState
	 * @param code
	 * @param type
	 * @param pageableData
	 * @return ProductCategorySearchPageData
	 * @description The method is used for searching products for the given offer ,it takes searchdata,offerId,type and
	 *              pageabledata as parameters
	 */
	@Override
	public ProductCategorySearchPageData dropDownSearchForOffer(final SearchStateData searchState, final String offerId,
			final PageableData pageableData, final String categoryCode, final String channel)
	{
		// YTODO Auto-generated method stub
		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
					{
						return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
								.convert(getProductSearchService()
										.searchAgain(decodeOfferStateDropDown(searchState, offerId, categoryCode, channel), pageableData));
					}
				});
	}


	/**
	 *
	 * @param searchState
	 * @param code
	 * @param type
	 * @return SolrSearchQueryData
	 */
	protected final SolrSearchQueryData decodeOfferStateDropDown(final SearchStateData searchState, final String offerId,
			final String categoryCode, final String channel)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		final List<SolrSearchQueryTermData> terms = new ArrayList<SolrSearchQueryTermData>();
		if (offerId != null)
		{
			if (channel.equalsIgnoreCase("web"))
			{
				final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
				solrSearchQueryTermData.setKey("allPromotions");
				solrSearchQueryTermData.setValue(offerId);
				//searchQueryData.setFilterTerms(Collections.singletonList(solrSearchQueryTermData));
				terms.add(solrSearchQueryTermData);
			}
			else if (channel.equalsIgnoreCase("mobile"))
			{
				final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
				solrSearchQueryTermData.setKey("allMobilePromotions");
				solrSearchQueryTermData.setValue(offerId);
				//searchQueryData.setFilterTerms(Collections.singletonList(solrSearchQueryTermData));
				terms.add(solrSearchQueryTermData);
			}
			searchQueryData.setOfferID(offerId);
		}

		if (!categoryCode.equalsIgnoreCase("all"))
		{
			final SolrSearchQueryTermData solrSearchQueryTermData1 = new SolrSearchQueryTermData();

			if (categoryCode.startsWith("MSH"))
			{
				solrSearchQueryTermData1.setKey(MarketplaceCoreConstants.CATEGORY);
			}
			else if (categoryCode.startsWith("MBH"))
			{
				solrSearchQueryTermData1.setKey(MarketplaceCoreConstants.BRAND);
			}
			else
			{
				solrSearchQueryTermData1.setKey(MarketplaceCoreConstants.SELLER_ID);
			}
			solrSearchQueryTermData1.setValue(categoryCode);
			//searchQueryData.setFilterTerms(Collections.singletonList(solrSearchQueryTermData1));
			terms.add(solrSearchQueryTermData1);
			searchQueryData.setOfferCategoryID(categoryCode);

		}
		searchQueryData.setFilterTerms(terms);

		return searchQueryData;
	}


	@Override
	public SolrSearchQueryData decodeSellerState(final SearchStateData searchState, final String categoryCode,
			final String sellerId)
	{
		final List<SolrSearchQueryTermData> terms = new ArrayList<SolrSearchQueryTermData>();
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());

		if (categoryCode != null && sellerId != null)
		{

			final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
			solrSearchQueryTermData.setKey(MarketplaceCoreConstants.SELLER_ID);
			solrSearchQueryTermData.setValue(sellerId);
			terms.add(solrSearchQueryTermData);


			final SolrSearchQueryTermData solrSearchQueryTermDataCategory = new SolrSearchQueryTermData();

			if (categoryCode.startsWith("MBH"))
			{

				solrSearchQueryTermDataCategory.setKey(MarketplaceCoreConstants.BRAND);

			}
			else
			{
				solrSearchQueryTermDataCategory.setKey("allCategories");
			}

			solrSearchQueryTermDataCategory.setValue(categoryCode);
			terms.add(solrSearchQueryTermDataCategory);
		}

		searchQueryData.setFilterTerms(terms);
		searchQueryData.setSns(searchState.isSns());
		return searchQueryData;
	}



	@Override
	public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> sellerCategorySearch(final String categoryCode,
			final String sellerId, final SearchStateData searchState, final PageableData pageableData)
	{
		Assert.notNull(searchState, MarketplacecommerceservicesConstants.SEARCH_STATE_DATA_MSG);

		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
					{
						return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
								.convert(getProductSearchService().searchAgain(decodeSellerState(searchState, categoryCode, sellerId),
										pageableData));
					}
				});
	}


	protected final SolrSearchQueryData decodeStateCollection(final SearchStateData searchState, final String collectionId)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		if (collectionId != null)
		{
			final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
			solrSearchQueryTermData.setKey("collectionIds");
			solrSearchQueryTermData.setValue(collectionId);
			searchQueryData.setFilterTerms(Collections.singletonList(solrSearchQueryTermData));
		}
		return searchQueryData;
	}

	@Override
	public ProductCategorySearchPageData collectionSearch(final String collectionId, final SearchStateData searchState,
			final PageableData pageableData)
	{

		Assert.notNull(searchState, MarketplacecommerceservicesConstants.SEARCH_STATE_DATA_MSG);
		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
					{
						return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
								.convert(getProductSearchService().searchAgain(decodeStateCollection(searchState, collectionId),
										pageableData));
					}
				});

	}


	//TODO : To be removed by Tharun after making age as String for Mobile
	@Override
	public ProductCategorySearchPageData conceirgeSearch(final int age, final String categoryCode, final String reasonOrEvent,
			final SearchStateData searchState, final PageableData pageableData)
	{

		Assert.notNull(searchState, MarketplacecommerceservicesConstants.SEARCH_STATE_DATA_MSG);
		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
					{
						return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
								.convert(getProductSearchService()
										.searchAgain(decodeStateConceirge(searchState, categoryCode, age, reasonOrEvent), pageableData));
					}
				});

	}

	//TODO : To be removed by Tharun after making age as String for Mobile

	protected final SolrSearchQueryData decodeStateConceirge(final SearchStateData searchState, final String categoryCode,
			final int age, final String reasonOrEvent)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		if (categoryCode != null)
		{
			searchQueryData.setCategoryCode(categoryCode);
			final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
			solrSearchQueryTermData.setKey(MarketplaceCoreConstants.OCCASSION);
			solrSearchQueryTermData.setValue(reasonOrEvent);
			searchQueryData.setFilterTerms(Collections.singletonList(solrSearchQueryTermData));
		}
		return searchQueryData;
	}

	/**
	 * @param searchState
	 * @param pageableData
	 * @return ProductCategorySearchPageData
	 * @description The method is used for searching data depending on the search dropdown
	 */
	@Override
	public ProductCategorySearchPageData dropDownSearchForSellerListing(final SearchStateData searchState, final String sellerId,
			final PageableData pageableData)
	{
		// YTODO Auto-generated method stub
		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
					{
						return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
								.convert(getProductSearchService().searchAgain(decodeSellerListingStateDropDown(searchState, sellerId),
										pageableData));
					}
				});
	}


	/**
	 *
	 * @param searchState
	 * @param code
	 * @param type
	 * @return SolrSearchQueryData
	 */
	protected final SolrSearchQueryData decodeSellerListingStateDropDown(final SearchStateData searchState, final String sellerId)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		if (sellerId != null)
		{

			searchQueryData.setSellerID(sellerId);
		}

		return searchQueryData;
	}

	/**
	 * @param searchState
	 * @param pageableData
	 * @return ProductCategorySearchPageData
	 * @description The method is used for searching products for given offer
	 */
	@Override
	public ProductCategorySearchPageData dropDownSearchForOfferListing(final SearchStateData searchState, final String offerId,
			final PageableData pageableData, final String categoryCode)
	{
		// YTODO Auto-generated method stub
		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
					{
						return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
								.convert(getProductSearchService()
										.searchAgain(decodeOfferListingStateDropDown(searchState, offerId, categoryCode), pageableData));
					}
				});
	}

	/**
	 *
	 * @param searchState
	 * @param code
	 * @param type
	 * @return SolrSearchQueryData
	 */
	protected final SolrSearchQueryData decodeOfferListingStateDropDown(final SearchStateData searchState, final String offerId,
			final String categoryCode)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		if (offerId != null)
		{

			searchQueryData.setOfferID(offerId);
		}
		if (categoryCode != null)
		{

			searchQueryData.setOfferCategoryID(categoryCode);
		}
		return searchQueryData;
	}


	@Override
	public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> searchCategorySearch(final String categoryCode,
			final SearchStateData searchState, final PageableData pageableData)
	{
		Assert.notNull(searchState, MarketplacecommerceservicesConstants.SEARCH_STATE_DATA_MSG);

		try
		{

			return getThreadContextService().executeInContext(
					new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
					{
						@Override
						public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
						{
							return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
									.convert(getProductSearchService().searchAgain(decodeSearchCategoryState(searchState, categoryCode),
											pageableData));
						}
					});


		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	protected SolrSearchQueryData decodeSearchCategoryState(final SearchStateData searchState, final String categoryCode)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		final List<SolrSearchQueryTermData> filterTerms = searchQueryData.getFilterTerms();
		final SolrSearchQueryTermData solrSearchQueryTermData = new SolrSearchQueryTermData();
		if (categoryCode != null)
		{

			if (categoryCode.startsWith("MSH"))
			{
				solrSearchQueryTermData.setKey(MarketplaceCoreConstants.CATEGORY);
			}
			else if (categoryCode.startsWith("MBH"))
			{
				solrSearchQueryTermData.setKey(MarketplaceCoreConstants.BRAND);
			}

			solrSearchQueryTermData.setValue(categoryCode);
			filterTerms.add(solrSearchQueryTermData);
			searchQueryData.setFilterTerms(filterTerms);

		}
		return searchQueryData;
	}

	@Override
	public ProductSearchPageData<SearchStateData, ITEM> textSearch(final SearchStateData searchState,
			final PageableData pageableData)
	{
		Assert.notNull(searchState, "SearchStateData must not be null.");
		try
		{

			return getThreadContextService().executeInContext(
					new ThreadContextService.Executor<ProductSearchPageData<SearchStateData, ITEM>, ThreadContextService.Nothing>()
					{
						@Override
						public ProductSearchPageData<SearchStateData, ITEM> execute()
						{
							return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
									.convert(getProductSearchService().searchAgain(decodeState(searchState, null), pageableData));
						}
					});



		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	protected SolrSearchQueryData decodeState(final SearchStateData searchState, final String categoryCode)
	{
		final SolrSearchQueryData searchQueryData = (SolrSearchQueryData) getSearchQueryDecoder().convert(searchState.getQuery());
		if (categoryCode != null)
		{
			searchQueryData.setCategoryCode(categoryCode);
		}
		searchQueryData.setSns(searchState.isSns());
		return searchQueryData;
	}

	@Override
	public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> categorySearch(final String categoryCode,
			final SearchStateData searchState, final PageableData pageableData)
	{
		Assert.notNull(searchState, "SearchStateData must not be null.");
		try
		{
			return getThreadContextService().executeInContext(
					new ThreadContextService.Executor<ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>, ThreadContextService.Nothing>()
					{
						@Override
						public ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData> execute()
						{
							return (ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>) getProductCategorySearchPageConverter()
									.convert(getProductSearchService().searchAgain(decodeState(searchState, categoryCode), pageableData));
						}
					});

		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

}

