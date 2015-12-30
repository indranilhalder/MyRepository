/**
 *
 */
package com.tisl.mpl.solrfacet.search.impl;

import de.hybris.platform.solrfacetsearch.indexer.IndexerContext;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.strategies.impl.AbstractIndexOperationStrategy;


public class MplDeleteIndexOperationStrategy extends AbstractIndexOperationStrategy
{
	@Override
	public void beforeIndex(final IndexerContext context) throws IndexerException
	{
		createIndexOperationRecord(context);
	}

	@Override
	public void afterIndex(final IndexerContext context) throws IndexerException
	{
		updateIndexOperationRecord(context);
	}

	@Override
	public void afterIndexError(final IndexerContext context) throws IndexerException
	{
		updateIndexOperationRecord(context);
	}
}