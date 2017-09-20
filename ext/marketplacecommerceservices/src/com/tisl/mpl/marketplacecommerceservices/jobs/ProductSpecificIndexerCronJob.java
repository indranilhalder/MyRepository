/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.jobs;

import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.IndexerService;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.model.FlashSaleProductListModel;


public class ProductSpecificIndexerCronJob extends AbstractJobPerformable
{
	private static final Logger LOG = Logger.getLogger(ProductSpecificIndexerCronJob.class.getName());

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private IndexerService indexerService;

	@Autowired
	private FacetSearchConfigService facetSearchConfigService;

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
		List<FlashSaleProductListModel> flashSaleProductListModelList = null;
		final String prodListFilePath = "";
		String listingId = "";
		final Set<String> listingIdSet = new TreeSet();

		try
		{
			try
			{
				//Fetch the Product ListIds from DB table 'FlashSaleProduct' and then pass it to get the PKs of those List Ids.
				//final String qString = "select distinct{productListingId} from {FlashSaleProductList}";
				final String qString = "select distinct{PK} from {FlashSaleProductList}";
				final FlexibleSearchQuery queryProductList = new FlexibleSearchQuery(qString);
				flashSaleProductListModelList = flexibleSearchService.<FlashSaleProductListModel> search(queryProductList)
						.getResult();

				if (CollectionUtils.isNotEmpty(flashSaleProductListModelList))
				{
					for (final FlashSaleProductListModel prodListModel : flashSaleProductListModelList)
					{
						listingId = prodListModel.getProductListingId();
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
					LOG.error("ERROR: Seems No Products found in DB table 'FlashSaleProductList' plz Check ...:");
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
