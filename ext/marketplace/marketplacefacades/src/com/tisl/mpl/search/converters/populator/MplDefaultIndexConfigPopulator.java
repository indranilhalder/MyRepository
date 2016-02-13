/**
 *
 */

package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.solrfacetsearch.config.CommitMode;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.OptimizeMode;
import de.hybris.platform.solrfacetsearch.config.SolrConfig;
import de.hybris.platform.solrfacetsearch.config.SolrServerMode;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.converters.populator.DefaultIndexConfigPopulator;
import de.hybris.platform.solrfacetsearch.enums.IndexerOperationValues;
import de.hybris.platform.solrfacetsearch.enums.SolrCommitMode;
import de.hybris.platform.solrfacetsearch.enums.SolrOptimizeMode;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexConfigModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexerQueryModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrServerConfigModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;


/**
 * @author 360641
 *
 */
public class MplDefaultIndexConfigPopulator extends DefaultIndexConfigPopulator

{
	private Converter<SolrServerConfigModel, SolrConfig> solrServerConfigConverter;
	private Converter<SolrIndexedTypeModel, IndexedType> indexedTypeConverter;

	@Override
	public void populate(final SolrFacetSearchConfigModel source, final IndexConfig target) throws ConversionException
	{
		Collection<IndexedType> indexTypesFromItems;
		try
		{
			indexTypesFromItems = getIndexTypesFromItems(source.getSolrIndexedTypes(),
					getSolrConfigFromItems(source.getSolrServerConfig()), source.getQueryType());
		}
		catch (final FacetConfigServiceException e)
		{
			throw new ConversionException("Cannot get index types from items", e);
		}
		target.setIndexedTypes(new HashMap());
		for (final IndexedType indexedType : indexTypesFromItems)
		{
			target.getIndexedTypes().put(indexedType.getUniqueIndexedTypeCode(), indexedType);
		}

		target.setCatalogVersions(Collections.unmodifiableCollection(source.getCatalogVersions()));
		target.setLanguages(Collections.unmodifiableCollection(source.getLanguages()));
		target.setCurrencies(Collections.unmodifiableCollection(source.getCurrencies()));
		target.setEnabledLanguageFallbackMechanism(source.isEnabledLanguageFallbackMechanism());
		target.setListeners(Collections.unmodifiableCollection(source.getListeners()));

		final SolrIndexConfigModel solrIndexConfig = source.getSolrIndexConfig();
		target.setExportPath(solrIndexConfig.getExportPath());
		target.setBatchSize(solrIndexConfig.getBatchSize());
		target.setNumberOfThreads(solrIndexConfig.getNumberOfThreads());
		target.setIndexMode(solrIndexConfig.getIndexMode());
		target.setIgnoreErrors(solrIndexConfig.isIgnoreErrors());

		final SolrCommitMode solrCommitMode = solrIndexConfig.getCommitMode();
		target.setCommitMode((solrCommitMode == null) ? null : CommitMode.valueOf(solrCommitMode.toString()));

		final SolrOptimizeMode solrOptimizeMode = solrIndexConfig.getOptimizeMode();
		target.setOptimizeMode((solrOptimizeMode == null) ? null : OptimizeMode.valueOf(solrOptimizeMode.toString()));
	}

	private Collection<IndexedType> getIndexTypesFromItems(final Collection<SolrIndexedTypeModel> itemTypes,
			final SolrConfig solrConfig, final String queryType) throws FacetConfigServiceException
	{
		final Collection result = new ArrayList();

		for (final SolrIndexedTypeModel itemType : itemTypes)
		{
			SolrIndexerQueryModel full = null;
			SolrIndexerQueryModel update = null;
			SolrIndexerQueryModel delete = null;

			for (final SolrIndexerQueryModel query : itemType.getSolrIndexerQueries())
			{
				if (query.getType().equals(IndexerOperationValues.FULL))
				{
					full = query;
				}
				else if (query.getType().equals(IndexerOperationValues.UPDATE))
				{
					update = query;
				}
				else
				{
					if (!(query.getType().equals(IndexerOperationValues.DELETE)))
					{
						continue;
					}
					delete = query;
				}
			}

			if (SolrServerMode.XML_EXPORT.equals(solrConfig.getMode()))
			{
				checkSingleQuery(full);
				checkSingleQuery(update);
				checkSingleQuery(delete);
			}
			itemType.setQueryType(queryType);
			final IndexedType indexedType = this.indexedTypeConverter.convert(itemType);

			result.add(indexedType);
		}
		return result;
	}

	private void checkSingleQuery(final SolrIndexerQueryModel query) throws FacetConfigServiceException
	{
		if (!(query.isInjectLastIndexTime()))
		{
			return;
		}
		throw new FacetConfigServiceException("Cannot use 'lastIndexTime' in solr indexer queries in XML_EXPORT mode");
	}

	private SolrConfig getSolrConfigFromItems(final SolrServerConfigModel itemConfig)
	{
		return (this.solrServerConfigConverter.convert(itemConfig));
	}

	@Override
	public void setSolrServerConfigConverter(final Converter<SolrServerConfigModel, SolrConfig> solrServerConfigConverter)
	{
		this.solrServerConfigConverter = solrServerConfigConverter;
	}

	@Override
	public void setIndexedTypeConverter(final Converter<SolrIndexedTypeModel, IndexedType> indexedTypeConverter)
	{
		this.indexedTypeConverter = indexedTypeConverter;
	}
}
