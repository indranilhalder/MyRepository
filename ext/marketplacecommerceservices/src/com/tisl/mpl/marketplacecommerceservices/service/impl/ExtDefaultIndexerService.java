/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.exceptions.IndexerException;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.search.provider.helper.MplCache;


/**
 * @author TCS
 *
 */
public class ExtDefaultIndexerService extends DefaultIndexerService
{


	@Autowired
	private MplCache<String> mplColourCache;

	/**
	 * @return void
	 * @param paramFacetSearchConfig
	 * @description: Perform full Index
	 *
	 */
	@Override
	public void performFullIndex(final FacetSearchConfig paramFacetSearchConfig) throws IndexerException
	{
		//Call the super class method
		super.performFullIndex(paramFacetSearchConfig);
		clearCaches();
	}

	/**
	 * @return void
	 * @param paramFacetSearchConfig
	 * @description: Perform update index
	 *
	 */
	@Override
	public void updateIndex(final FacetSearchConfig paramFacetSearchConfig) throws IndexerException
	{
		//Call the super class method
		super.updateIndex(paramFacetSearchConfig);
		clearCaches();
	}

	/**
	 * @return void
	 * @param paramFacetSearchConfig
	 *           ,paramIndexedType
	 * @description: Perform update type index
	 *
	 */
	@Override
	public void updateTypeIndex(final FacetSearchConfig paramFacetSearchConfig, final IndexedType paramIndexedType)
			throws IndexerException
	{
		//Call the super class method
		super.updateTypeIndex(paramFacetSearchConfig, paramIndexedType);
		clearCaches();

	}

	/**
	 * @return void
	 * @param paramFacetSearchConfig
	 *           ,paramIndexedType,paramList
	 * @description: Perform update type index
	 *
	 */
	@Override
	public void updateTypeIndex(final FacetSearchConfig paramFacetSearchConfig, final IndexedType paramIndexedType,
			final List<PK> paramList) throws IndexerException
	{
		//Call the super class method
		super.updateTypeIndex(paramFacetSearchConfig, paramIndexedType, paramList);
		clearCaches();
	}

	/**
	 * @return void
	 * @param paramFacetSearchConfig
	 *           ,paramIndexedType,paramCollection,paramList
	 * @description: Perform update partial type index
	 *
	 */
	@Override
	public void updatePartialTypeIndex(final FacetSearchConfig paramFacetSearchConfig, final IndexedType paramIndexedType,
			final Collection<IndexedProperty> paramCollection, final List<PK> paramList) throws IndexerException
	{
		//Call the super class method
		super.updatePartialTypeIndex(paramFacetSearchConfig, paramIndexedType, paramCollection, paramList);
		clearCaches();
	}

	/**
	 * @return void
	 * @param paramFacetSearchConfig
	 * @description: Perform delete from index
	 *
	 */
	@Override
	public void deleteFromIndex(final FacetSearchConfig paramFacetSearchConfig) throws IndexerException
	{
		//Call the super class method
		super.deleteFromIndex(paramFacetSearchConfig);
		clearCaches();
	}

	/**
	 * @return void
	 * @param paramFacetSearchConfig
	 *           ,paramIndexedType
	 * @description: Perform delete type index
	 *
	 */
	@Override
	public void deleteTypeIndex(final FacetSearchConfig paramFacetSearchConfig, final IndexedType paramIndexedType)
			throws IndexerException
	{
		//Call the super class method
		super.deleteTypeIndex(paramFacetSearchConfig, paramIndexedType);
		clearCaches();
	}

	/**
	 * @return void
	 * @param paramFacetSearchConfig
	 *           ,paramIndexedType,paramList
	 * @description: Perform delete type index
	 *
	 */
	@Override
	public void deleteTypeIndex(final FacetSearchConfig paramFacetSearchConfig, final IndexedType paramIndexedType,
			final List<PK> paramList) throws IndexerException
	{
		//Call the super class method
		super.deleteTypeIndex(paramFacetSearchConfig, paramIndexedType, paramList);
		clearCaches();
	}

	/**
	 * @return void
	 * @param null
	 * @description: erforms the clear operation of all the indexer caches in the system.
	 *
	 */
	private void clearCaches()
	{
		mplColourCache.clear();
	}
}
