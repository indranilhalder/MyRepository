/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.solr.search;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;



public class ExtSearchSolrQueryPopulator<INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE> implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private Converter<SolrFacetSearchConfigModel, FacetSearchConfig> facetSearchConfigConverter;
	private CommonI18NService commonI18NService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private CatalogVersionService catalogVersionService;
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;

	protected Converter<SolrFacetSearchConfigModel, FacetSearchConfig> getFacetSearchConfigConverter()
	{
		return this.facetSearchConfigConverter;
	}

	@Required
	public void setFacetSearchConfigConverter(
			final Converter<SolrFacetSearchConfigModel, FacetSearchConfig> facetSearchConfigConverter)
	{
		this.facetSearchConfigConverter = facetSearchConfigConverter;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return this.commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return this.baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return this.catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, IndexedType, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		target.setSearchQueryData(source.getSearchQueryData());
		target.setPageableData(source.getPageableData());

		final Collection catalogVersions = getSessionProductCatalogVersions();
		if ((catalogVersions == null) || (catalogVersions.isEmpty()))
		{
			throw new ConversionException("Missing solr facet search indexed catalog versions");
		}

		target.setCatalogVersions(new ArrayList(catalogVersions));
		try
		{
			target.setFacetSearchConfig(getFacetSearchConfig());
		}
		catch (final NoValidSolrConfigException e)
		{
			throw new ConversionException("No valid solrFacetSearchConfig found for the current context", e);
		}


		target.setIndexedType(getIndexedType(target.getFacetSearchConfig()));

		if (source.getSearchQueryData().isSns())
		{
			target.setSearchQuery(createMplSearchQuery(target.getFacetSearchConfig(), target.getIndexedType()));
		}
		else
		{
			target.setSearchQuery(createSearchQuery(target.getFacetSearchConfig(), target.getIndexedType()));
		}
		target.getSearchQuery().setCatalogVersions(target.getCatalogVersions());
		target.getSearchQuery().setCurrency(getCommonI18NService().getCurrentCurrency().getIsocode());
		target.getSearchQuery().setLanguage(getCommonI18NService().getCurrentLanguage().getIsocode());

		target.getSearchQuery().setUserQuery(source.getSearchQueryData().getFreeTextSearch());

		target.getSearchQuery().setEnableSpellcheck(true);

		target.getSearchQuery().addSolrParams("group", new String[]
		{ "true" });
		target.getSearchQuery().addSolrParams("group.field", new String[]
		{ "baseProductColourGroup_string" });
		target.getSearchQuery().addSolrParams("group.truncate", new String[]
		{ "false" });
		target.getSearchQuery().addSolrParams("group.ngroups", new String[]
		{ "true" });
		target.getSearchQuery().addSolrParams("group.limit", new String[]
		{ "1" });
		/*
		 * target.getSearchQuery().addSolrParams("group.sort", new String[] { "priceValue_inr_double asc" });
		 */


	}

	@Deprecated
	protected CatalogVersionModel getSessionProductCatalogVersion()
	{
		for (final CatalogVersionModel catalogVersion : getSessionProductCatalogVersions())
		{
			final List facetSearchConfigs = catalogVersion.getFacetSearchConfigs();
			if ((facetSearchConfigs != null) && (!(facetSearchConfigs.isEmpty())))
			{
				return catalogVersion;
			}
		}
		return null;
	}

	protected Collection<CatalogVersionModel> getSessionProductCatalogVersions()
	{
		final BaseSiteModel currentSite = getBaseSiteService().getCurrentBaseSite();
		final List productCatalogs = getBaseSiteService().getProductCatalogs(currentSite);

		final Collection<CatalogVersionModel> sessionCatalogVersions = getCatalogVersionService().getSessionCatalogVersions();

		final Collection result = new ArrayList();
		for (final CatalogVersionModel sessionCatalogVersion : sessionCatalogVersions)
		{
			if (!(productCatalogs.contains(sessionCatalogVersion.getCatalog())))
			{
				continue;
			}
			result.add(sessionCatalogVersion);
		}

		return result;
	}

	protected FacetSearchConfig getFacetSearchConfig() throws NoValidSolrConfigException
	{
		return (getFacetSearchConfigConverter()
				.convert(getSolrFacetSearchConfigSelectionStrategy().getCurrentSolrFacetSearchConfig()));
	}

	protected IndexedType getIndexedType(final FacetSearchConfig config)
	{
		final IndexConfig indexConfig = config.getIndexConfig();

		final Collection indexedTypes = indexConfig.getIndexedTypes().values();
		if ((indexedTypes != null) && (!(indexedTypes.isEmpty())))
		{
			return ((IndexedType) indexedTypes.iterator().next());
		}

		return null;
	}

	protected SearchQuery createSearchQuery(final FacetSearchConfig config, final IndexedType indexedType)
	{
		return new SearchQuery(config, indexedType);
	}

	protected SearchQuery createMplSearchQuery(final FacetSearchConfig config, final IndexedType indexedType)
	{
		return new MplSearchQuery(config, indexedType, true);
	}

	protected BaseStoreService getBaseStoreService()
	{
		return this.baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected SolrFacetSearchConfigSelectionStrategy getSolrFacetSearchConfigSelectionStrategy()
	{
		return this.solrFacetSearchConfigSelectionStrategy;
	}

	@Required
	public void setSolrFacetSearchConfigSelectionStrategy(
			final SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy)
	{
		this.solrFacetSearchConfigSelectionStrategy = solrFacetSearchConfigSelectionStrategy;
	}
}