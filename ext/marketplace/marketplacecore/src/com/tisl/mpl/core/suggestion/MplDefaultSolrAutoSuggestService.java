package com.tisl.mpl.core.suggestion;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.common.AbstractYSolrService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.solr.impl.SolrServer;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.springframework.beans.factory.annotation.Required;


public class MplDefaultSolrAutoSuggestService extends AbstractYSolrService implements SolrAutoSuggestService
{
	private static final Logger LOG = Logger.getLogger(MplDefaultSolrAutoSuggestService.class);
	
	private SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver;

	public SolrSuggestion getAutoSuggestionsForQuery(final LanguageModel language, final SolrIndexedTypeModel solrIndexedType,
			final String queryInput) throws SolrAutoSuggestException
	{
		try
		{
			final String configName = solrIndexedType.getSolrFacetSearchConfig().getName();
			final FacetSearchConfig facetSearchConfig = this.facetSearchConfigService.getConfiguration(configName);
			final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes()
					.get(this.solrIndexedTypeCodeResolver.resolveIndexedTypeCode(solrIndexedType));

			final SolrServer server = this.solrService.getSolrServer(facetSearchConfig.getSolrConfig(), indexedType);
			final SolrQuery query = new SolrQuery();
			query.setQuery(queryInput);
			query.setQueryType("/suggest");
			query.set("spellcheck.dictionary", new String[]
			{ language.getIsocode() });
			query.set("spellcheck.q", new String[]
			{ queryInput });
			LOG.debug("Suggest Query :" + query.getQuery());
			final QueryResponse response = server.query(query);

			final Map resultSuggestionMap = new HashMap();
			final Collection resultCollations = new ArrayList();

			final SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
			if (spellCheckResponse != null)
			{
				final List<SpellCheckResponse.Suggestion> suggestions = spellCheckResponse.getSuggestions();
				List alternatives;
				for (final SpellCheckResponse.Suggestion suggestion : suggestions)
				{
					alternatives = suggestion.getAlternatives();
					if (suggestion.getToken().equalsIgnoreCase(queryInput))
					{

						resultSuggestionMap.put(suggestion.getToken(), alternatives);
					}
					//resultSuggestion.add(suggestion.getAlternatives());

				}


				/*final List<SpellCheckResponse.Collation> collatedResults = spellCheckResponse.getCollatedResults();
				if (collatedResults != null)
				{
					for (final SpellCheckResponse.Collation collation : collatedResults)
					{
						resultCollations.add(collation.getCollationQueryString());
					}
				}*/
			}
			return new SolrSuggestion(resultSuggestionMap, resultCollations);
		}
		catch (final SolrServiceException e)
		{
			throw new SolrAutoSuggestException(e);
		}
		catch (final FacetConfigServiceException e)
		{
			throw new SolrAutoSuggestException(e);
		}
		catch (final SolrServerException e)
		{
			throw new SolrAutoSuggestException("Error issuing suggestion query", e);
		}
	}

	@Override
	protected boolean checkIfIndexPropertyQualifies(final SolrIndexedPropertyModel indexedProperty)
	{
		return ((Boolean.TRUE.equals(indexedProperty.getUseForAutocomplete())) && (!(indexedProperty.isCurrency())));
	}

	@Required
	public void setSolrIndexedTypeCodeResolver(final SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver)
	{
		this.solrIndexedTypeCodeResolver = solrIndexedTypeCodeResolver;
	}
}