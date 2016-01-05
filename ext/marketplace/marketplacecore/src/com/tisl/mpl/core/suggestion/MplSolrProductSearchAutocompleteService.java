/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.tisl.mpl.core.suggestion;

import de.hybris.platform.commerceservices.search.ProductSearchAutocompleteService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


public class MplSolrProductSearchAutocompleteService implements ProductSearchAutocompleteService<AutocompleteSuggestion>
{
	//	private static final Logger LOG = Logger.getLogger(MplSolrProductSearchAutocompleteService.class);
	private FacetSearchConfigService facetSearchConfigService;
	private CommonI18NService commonI18NService;
	private SolrAutoSuggestService solrAutoSuggestService;
	private SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver;
	private SolrFacetSearchConfigSelectionStrategy solrFacetSearchConfigSelectionStrategy;

	protected FacetSearchConfigService getFacetSearchConfigService()
	{
		return this.facetSearchConfigService;
	}

	@Required
	public void setFacetSearchConfigService(final FacetSearchConfigService facetSearchConfigService)
	{
		this.facetSearchConfigService = facetSearchConfigService;
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

	@Override
	public List<AutocompleteSuggestion> getAutocompleteSuggestions(final String input) throws EtailNonBusinessExceptions
	{
		try
		{


			final SolrFacetSearchConfigModel solrFacetSearchConfigModel = getSolrFacetSearchConfigSelectionStrategy()
					.getCurrentSolrFacetSearchConfig();

			final FacetSearchConfig facetSearchConfig = getFacetSearchConfigService()
					.getConfiguration(solrFacetSearchConfigModel.getName());
			final IndexedType indexedType = getIndexedType(facetSearchConfig);

			final SolrIndexedTypeModel indexedTypeModel = findIndexedTypeModel(solrFacetSearchConfigModel, indexedType);

			final SolrSuggestion suggestions = getSolrAutoSuggestService()
					.getAutoSuggestionsForQuery(getCommonI18NService().getCurrentLanguage(), indexedTypeModel, input);

			return findBestSuggestions(suggestions, input);


		}
		catch (final SolrAutoSuggestException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B7000);
		}
		catch (final FacetConfigServiceException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B7001);
		}
		catch (final IndexerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B7002);
		}
		catch (final NoValidSolrConfigException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B7003);
		}
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

	protected SolrIndexedTypeModel findIndexedTypeModel(final SolrFacetSearchConfigModel facetSearchConfigModel,
			final IndexedType indexedType) throws IndexerException
	{
		for (final SolrIndexedTypeModel type : facetSearchConfigModel.getSolrIndexedTypes())
		{
			if (this.solrIndexedTypeCodeResolver.resolveIndexedTypeCode(type).equals(indexedType.getUniqueIndexedTypeCode()))
			{
				return type;
			}
		}
		throw new IndexerException("Could not find matching model for type: " + indexedType.getCode());
	}

	protected SolrAutoSuggestService getSolrAutoSuggestService()
	{
		return this.solrAutoSuggestService;
	}

	@Required
	public void setSolrAutoSuggestService(final SolrAutoSuggestService solrAutoSuggestService)
	{
		this.solrAutoSuggestService = solrAutoSuggestService;
	}

	protected List<AutocompleteSuggestion> findBestSuggestions(final SolrSuggestion solrSuggestion, final String input)
	{



		final List result = new ArrayList();

		if (solrSuggestion.getSuggestions() != null)
		{

			final Collection<String> suggestions = solrSuggestion.getSuggestions().get(input.toLowerCase());
			if (suggestions != null)
			{
				for (final String suggestion : suggestions)
				{
					final AutocompleteSuggestion autocompleteSuggestion = createAutocompleteSuggestion();
					autocompleteSuggestion.setTerm(suggestion);
					result.add(autocompleteSuggestion);
				}
			}
		}


		return result;
	}

	protected AutocompleteSuggestion createAutocompleteSuggestion()
	{
		return new AutocompleteSuggestion();
	}

	@Required
	public void setSolrIndexedTypeCodeResolver(final SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver)
	{
		this.solrIndexedTypeCodeResolver = solrIndexedTypeCodeResolver;
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