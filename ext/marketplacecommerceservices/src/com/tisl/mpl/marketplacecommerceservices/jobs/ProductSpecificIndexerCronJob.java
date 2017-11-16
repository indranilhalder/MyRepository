/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;


public class ProductSpecificIndexerCronJob extends AbstractJobPerformable //,AbstractItemDao
{
	private static final Logger LOG = Logger.getLogger(ProductSpecificIndexerCronJob.class.getName());


	@Autowired
	private IndexerService indexerService;

	@Autowired
	private FacetSearchConfigService facetSearchConfigService;

	@Autowired
	BaseStoreService baseStoreService; // For Testing

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		LOG.error("DEBUG: Started ProductSpecificIndexerCronJob.......");
		List productList = new ArrayList();
		SolrFacetSearchConfigModel facetSearchConfigModel = null;
		FacetSearchConfig facetSearchConfig = null;

		try
		{
			productList = getProductData(); //read the products from External File from whose path in local.properties.

			if (productList == null || productList.size() == 0)
			{
				LOG.error("ERROR: No product found in the DB to Index ....:");
				LOG.error("ERROR: Going to ABORT ProductSpecificIndexerCronJob .................");

				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}

			//fetch the SolrFacetSearchConfigModel
			facetSearchConfigModel = getSolrFacetSearchConfigModel();

			if (null != facetSearchConfigModel)
			{
				facetSearchConfig = facetSearchConfigService.getConfiguration(facetSearchConfigModel.getName());
			}
			else
			{
				LOG.error("ERROR: facetSearchConfigModel found null");
				LOG.error("ERROR: Going to ABORT ProductSpecificIndexerCronJob .................");
				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}

			if (facetSearchConfig == null)
			{
				LOG.error("ERROR: ..facetSearchConfig found null");
				LOG.error("ERROR: Going to ABORT ProductSpecificIndexerCronJob .................");

				return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
			}

			if (null != facetSearchConfig.getIndexConfig()
					&& MapUtils.isNotEmpty(facetSearchConfig.getIndexConfig().getIndexedTypes()))
			{
				final Map<String, IndexedType> indexedTypes = facetSearchConfig.getIndexConfig().getIndexedTypes();
				final List<IndexedType> indexTypeList = getDesiredIndexTypeList(indexedTypes);

				if (CollectionUtils.isNotEmpty(indexTypeList))
				{
					LOG.error("DEBUG: Going to start the Update Index for mentioned products........");
					for (final IndexedType index : indexTypeList)
					{
						indexerService.updateTypeIndex(facetSearchConfig, index, productList);
					}
				}
				else
				{
					LOG.error("ERROR: !! No Update to SOLR document done ..indexTypeList is empty..:");
					LOG.error("ERROR: Going to ABORT ProductSpecificIndexerCronJob .................");

					return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("ERROR: ... Exiting" + e);
			LOG.error("ERROR: Going to ABORT ProductSpecificIndexerCronJob .................");

			e.printStackTrace();
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		LOG.error("DEBUG: Going to END ProductSpecificIndexerCronJob .................");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	//This method reads the External files for ProductsListingIds and return the PKs for them
	public List<PK> getProductData()
	{
		final String propLine = "";
		StringBuffer productString = new StringBuffer();
		List<PK> prodList = null;
		final List<String> flashSaleProductListModelList = new ArrayList();
		final List<BuyBoxModel> flashSaleProductListModelListBB = null;
		final String prodListFilePath = "";
		final Set<String> listingIdSet = new TreeSet();
		String flashSaleUSSIDListString = "";
		String formattedFlashSaleUSSIDListString = "";
		List<String> flashSaleUSSIDList = new ArrayList();
		StringBuffer sb = null;

		try
		{
			try
			{
				/************* Pick Up the FlashSaleUSSIDList from BaseStore and then the ProductListingIds from BuyBox ******************/

				final List<BaseStoreModel> allBaseStoresList = baseStoreService.getAllBaseStores();

				for (final BaseStoreModel baseStoreModel : allBaseStoresList)
				{
					if ("mpl".equalsIgnoreCase(baseStoreModel.getUid()))
					{
						flashSaleUSSIDListString = baseStoreModel.getFlashSaleUSSIDList();
						if (flashSaleUSSIDListString != null && flashSaleUSSIDListString.trim().length() > 0)
						{
							LOG.error("DEBUG: flashSaleUSSIDList from {BaseStore}:: " + flashSaleUSSIDList);
							flashSaleUSSIDList = Arrays.asList(flashSaleUSSIDListString.trim().split(
									MarketplacecommerceservicesConstants.COMMA));

							if (flashSaleUSSIDList != null && flashSaleUSSIDList.size() > 0)
							{
								for (final String ussId : flashSaleUSSIDList)
								{
									if (!formattedFlashSaleUSSIDListString.contains(ussId.trim()))
									{
										formattedFlashSaleUSSIDListString = formattedFlashSaleUSSIDListString + "\'" + ussId.trim() + "\',";
									}
								}
							}
							else
							{
								LOG.error("ERROR: flashSaleUSSIDList is null or empty");
							}
							if (formattedFlashSaleUSSIDListString != null && formattedFlashSaleUSSIDListString.length() > 0)
							{
								sb = new StringBuffer(formattedFlashSaleUSSIDListString);
								if (sb != null && sb.length() > 0)
								{
									sb.deleteCharAt(sb.toString().lastIndexOf(",")); // remove the last `,` from the string.

									final String qStringBuyBox = "select distinct{product} from {BuyBox} where {sellerarticlesku} in ("
											+ sb + ")";
									final FlexibleSearchQuery queryProductListBB = new FlexibleSearchQuery(qStringBuyBox);
									queryProductListBB.setResultClassList(Arrays.asList(String.class));
									final SearchResult<String> rows = flexibleSearchService.search(queryProductListBB);

									if (rows != null)
									{
										for (final String row : rows.getResult())
										{
											flashSaleProductListModelList.add(row);
										}
									}
								}
							}
							else
							{
								LOG.error("ERROR: formattedFlashSaleUSSIDListString is null or empty");
							}
						}
						else
						{
							LOG.error("flashSaleUSSIDList is empty::");
						}
					}
				}

				/******************************************************************************/

				if (CollectionUtils.isNotEmpty(flashSaleProductListModelList))
				{
					for (final String listingId : flashSaleProductListModelList)
					{
						//listingId = prodListModel.getProductListingId();
						if (listingId != null && listingId.trim().length() > 0 && !listingIdSet.contains(listingId))
						{
							productString.append("'" + listingId.trim() + "',");
							listingIdSet.add(listingId);
						}
					}
					if (productString != null && productString.length() > 0)
					{
						productString.deleteCharAt(productString.toString().lastIndexOf(",")); // remove the last `,` from the string.
						LOG.error("DEBUG: The File List to be indexed ::" + productString);
					}
					else
					{
						LOG.error("ERROR: Seems No Products List found plz Check ...:");
						return null;
					}
				}
				else
				{
					LOG.error("ERROR: Seems No Products found in flashSaleProductListModelList  plz Check ...:");
					return null;
				}
			}
			catch (final Exception e)
			{
				LOG.error("ERROR: getProductData() ..No Products found in the FIle:" + e);
				e.printStackTrace();
			}
			if (productString != null && productString.length() > 0)
			{
				final String queryString = "select distinct{PK} from {product}  where {CODE} in (" + productString.toString() + ")";
				final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
				prodList = flexibleSearchService.<PK> search(query).getResult();
			}
		}
		catch (final Exception e)
		{
			LOG.error("ERROR: in  getProductData() ::" + e);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				productString = null;
			}
			catch (final Exception e)
			{
				LOG.error("ERROR: in Finally block of getProductData()" + e);
				e.printStackTrace();
			}
		}

		return prodList;
	}

	//This method returns the SolrFacetSearchConfigModel from DB
	private SolrFacetSearchConfigModel getSolrFacetSearchConfigModel()
	{
		try
		{
			final String queryString = "select {pk} from {SolrFacetSearchConfig} where {name} = ?name";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("name", "mplIndex");

			final List<SolrFacetSearchConfigModel> dataList = flexibleSearchService.<SolrFacetSearchConfigModel> search(query)
					.getResult();

			if (CollectionUtils.isNotEmpty(dataList))
			{
				return dataList.get(0);
			}
			else
			{
				LOG.error("ERROR: in getSolrFacetSearchConfigModel() dataList is empty or null");
			}

			return null;
		}
		catch (final Exception ex)
		{
			LOG.error("ERROR: in getSolrFacetSearchConfigModel()" + ex);
			ex.printStackTrace();
		}
		return null;
	}

	private List<IndexedType> getDesiredIndexTypeList(final Map<String, IndexedType> indexedTypes)
	{
		return new ArrayList<>(indexedTypes.values());
	}

}
