/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplPriceRowDao;


/**
 * @author TCS
 *
 */
public class MplPriceRowDaoImpl implements MplPriceRowDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(MplPriceRowDaoImpl.class);
	private static final String SELECT_CLASS = "SELECT {c:";
	private static final String FROM_CLASS = "FROM {";
	private static final String WHERE_CLASS = "WHERE ";
	private static final String C_CLASS = "{c:";
	private static final String AS_CLASS = " AS c} ";


	/*
	 * @Javadoc Method to Retrieve Pricerow based on articleSKUID
	 * 
	 * @param->articleSKUID,catalogVersionModel
	 * 
	 * @return->listOfPrice
	 */
	@Override
	public List<PriceRowModel> getPriceRowDetail(final CatalogVersionModel catalogVersionModel, final String articleSKUID)
			throws EtailNonBusinessExceptions
	{


		final String queryString = //
		SELECT_CLASS + PriceRowModel.PK
				+ "}" //
				+ FROM_CLASS + PriceRowModel._TYPECODE
				+ AS_CLASS//
				+ WHERE_CLASS + C_CLASS + PriceRowModel.SELLERARTICLESKU + "}=?articleSKUID AND " + C_CLASS
				+ PriceRowModel.CATALOGVERSION + "}=?catalogVersion";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(articleSKUID, articleSKUID);
		query.addQueryParameter(PriceRowModel.CATALOGVERSION, catalogVersionModel);


		final List<PriceRowModel> listOfPrice = flexibleSearchService.<PriceRowModel> search(query).getResult();
		return listOfPrice;

	}


	/*
	 * <<<<<<< Updated upstream
	 * 
	 * @Javadoc Method Method to Retrieve Pricerow based on multiple articleSKUID
	 * 
	 * @param articleSKUIDList,catalogVersionModel
	 * 
	 * @return PriceRowModel ======= (Javadoc) Method to Retrieve Pricerow based on multiple articleSKUID
	 * 
	 * @param->articleSKUIDList,catalogVersionModel
	 * 
	 * @return->listOfPrice >>>>>>> Stashed changes
	 */

	@Override
	public List<PriceRowModel> getAllPriceRowDetail(final CatalogVersionModel catalogVersionModel,
			final List<String> sellerArticleSKU)
	{
		try
		{
			final String queryString = //
			SELECT_CLASS + PriceRowModel.PK
					+ "} " //
					+ FROM_CLASS + PriceRowModel._TYPECODE
					+ AS_CLASS//
					+ WHERE_CLASS + C_CLASS + PriceRowModel.SELLERARTICLESKU + "} in (?sellerArticleSKU)  AND " + C_CLASS
					+ PriceRowModel.CATALOGVERSION + "}=?catalogVersion";

			LOG.info("Query:" + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(PriceRowModel.SELLERARTICLESKU, sellerArticleSKU);
			query.addQueryParameter(PriceRowModel.CATALOGVERSION, catalogVersionModel);


			final List<PriceRowModel> listOfPrice = flexibleSearchService.<PriceRowModel> search(query).getResult();
			return listOfPrice;
		}

		catch (final Exception ex)
		{

			throw new EtailNonBusinessExceptions(ex);
		}
	}

	@Override
	public List<PriceRowModel> getAllPriceRowDetail(final CatalogVersionModel catalogVersionModel, final String articleSKUIDList)
	{
		try
		{
			final String queryString = //
			SELECT_CLASS + PriceRowModel.PK
					+ "} " //
					+ FROM_CLASS + PriceRowModel._TYPECODE
					+ AS_CLASS//
					+ WHERE_CLASS + C_CLASS + PriceRowModel.SELLERARTICLESKU + "} in (" + articleSKUIDList + ")  AND " + C_CLASS
					+ PriceRowModel.CATALOGVERSION + "}=?catalogVersion";

			LOG.info("Query:" + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(PriceRowModel.SELLERARTICLESKU, articleSKUIDList);
			query.addQueryParameter(PriceRowModel.CATALOGVERSION, catalogVersionModel);


			final List<PriceRowModel> listOfPrice = flexibleSearchService.<PriceRowModel> search(query).getResult();
			return listOfPrice;
		}

		catch (final Exception ex)
		{

			throw new EtailNonBusinessExceptions(ex);
		}
	}

	/*
	 * <<<<<<< Updated upstream Method to retrieve PriceRow based on articleSKUID with checking stock level =======
	 * 
	 * @Javadoc Method to retrieve PriceRow based on articleSKUID with checking stock level >>>>>>> Stashed changes
	 * 
	 * @param articleSKUIDList,catalogVersionModel
	 * 
	 * @return PriceRowModel
	 */

	@Override
	public PriceRowModel getPriceRowDetailForSKUWithStockCheck(final CatalogVersionModel catalogVersionModel,
			final String articleSKUIDList)
	{
		PriceRowModel priceRowModel = null;
		try
		{
			final String queryString = "SELECT {pm:pk} FROM {Pricerow AS pm JOIN Stocklevel AS sl ON {sl.SELLERARTICLESKU}={pm.SELLERARTICLESKU}} WHERE  {pm.SELLERARTICLESKU}=?sellerArticleSKU AND {sl.available}>'0' AND {pm.CATALOGVERSION}=?CATALOGVERSION ";
			final Map<String, Object> params = new HashMap<String, Object>(2);
			params.put(PriceRowModel.SELLERARTICLESKU, articleSKUIDList);
			params.put(PriceRowModel.CATALOGVERSION, catalogVersionModel);
			final SearchResult<PriceRowModel> searchRes = flexibleSearchService.search(queryString, params);
			if (searchRes != null && searchRes.getCount() > 0)
			{
				//return searchRes.getResult().get(0);
				priceRowModel = searchRes.getResult().get(0);
			}
			//return null;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
		return priceRowModel;
	}

	/*
	 * @Javadoc Method to retrieve least Price of product
	 * 
	 * @param Productmodel, CatalogVersionModel
	 * 
	 * @return PriceRowModel
	 */

	@Override
	public PriceRowModel getMinimumPriceForProduct(final CatalogVersionModel catalogVersionModel, final ProductModel productModel)
	{
		PriceRowModel priceRowModel = null;
		try
		{
			final String queryString = //
			SELECT_CLASS + PriceRowModel.PK
					+ "}" //
					+ FROM_CLASS + PriceRowModel._TYPECODE
					+ AS_CLASS//
					+ WHERE_CLASS + C_CLASS + PriceRowModel.PRODUCT + "}=?productPk AND " + C_CLASS + PriceRowModel.CATALOGVERSION
					+ "}=?catalogVersion order by {price} asc";

			final Map<String, Object> params = new HashMap<String, Object>(2);
			params.put("productPk", productModel.getPk());
			params.put(PriceRowModel.CATALOGVERSION, catalogVersionModel);
			final SearchResult<PriceRowModel> searchRes = flexibleSearchService.search(queryString, params);
			if (searchRes != null && searchRes.getCount() > 0)
			{
				//return searchRes.getResult().get(0);
				priceRowModel = searchRes.getResult().get(0);
			}
			//return null;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex);
		}
		return priceRowModel;
	}
}
