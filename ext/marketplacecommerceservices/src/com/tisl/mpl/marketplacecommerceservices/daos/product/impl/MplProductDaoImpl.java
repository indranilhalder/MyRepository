/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.product.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.impl.DefaultProductDao;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */
public class MplProductDaoImpl extends DefaultProductDao implements MplProductDao
{

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private SearchRestrictionService searchRestrictionService;

	protected static final Logger LOG = Logger.getLogger(MplProductDaoImpl.class);

	private static final String SELECT_STRING = "SELECT  distinct {p:";
	private static final String FROM_STRING = "FROM {";
	private static final String CODE_STRING = "} = (?code) AND {p:";
	private static final String CODE = "code";
	private static final String PRODUCT_PARAM = "{c.code}=?productParam";


	public MplProductDaoImpl(final String typecode)
	{
		super(typecode);
	}


	@Override
	public List<ProductModel> findProductsByCode(final String code)
	{
		LOG.debug("findProductsByCode: code********** " + code);
		//final CatalogVersionModel catalogVersion = getCatalogVersion();
		final StringBuilder stringBuilder = new StringBuilder(70);

		stringBuilder.append(SELECT_STRING).append(ProductModel.PK).append("} ").append(FROM_STRING).append(ProductModel._TYPECODE)
				.append(" AS p ").append("JOIN ").append(SellerInformationModel._TYPECODE).append(" AS s ").append("ON {s:")
				.append(SellerInformationModel.PRODUCTSOURCE).append("}={p:").append(ProductModel.PK).append("} ").append("JOIN ")
				.append(CatalogVersionModel._TYPECODE).append(" AS cat ").append("ON {p:").append(ProductModel.CATALOGVERSION)
				.append("}={cat:").append(CatalogVersionModel.PK).append("} }");

		final String inPart = "{p:" + ProductModel.CODE + "} = (?code) AND {cat:" + CatalogVersionModel.VERSION
				+ "} = 'Online' and  sysdate between {s.startdate} and {s.enddate} ";
		stringBuilder.append("WHERE ").append(inPart);

		LOG.debug("findProductsByCode: stringBuilder******* " + stringBuilder);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());

		query.addQueryParameter(CODE, code);

		//query.addQueryParameter("catalogVersion", catalogVersion);
		query.setResultClassList(Collections.singletonList(ProductModel.class));
		final SearchResult<ProductModel> searchResult = getFlexibleSearchService().search(query);
		LOG.debug("findProductsByCode: searchResult********** " + searchResult);
		return searchResult.getResult();
	}

	@Override
	public List<ProductModel> findProductsByCodeNew(final String code)
	{
		List<ProductModel> productList = null;
		final CatalogVersionModel catalogVersion = getCatalogVersion();
		final StringBuilder stringBuilder = new StringBuilder(70);

		stringBuilder.append(SELECT_STRING).append(ProductModel.PK).append("} ");
		stringBuilder.append(FROM_STRING).append(ProductModel._TYPECODE).append(" AS p ");
		stringBuilder.append("JOIN ").append(SellerInformationModel._TYPECODE).append(" AS s ");
		stringBuilder.append("ON {s:").append(SellerInformationModel.PRODUCTSOURCE).append("}={p:").append(ProductModel.PK)
				.append("} } ");
		final String inPart = "{p:" + ProductModel.CODE + CODE_STRING + ProductModel.CATALOGVERSION + "} = ?catalogVersion";
		stringBuilder.append("WHERE ").append(inPart);
		LOG.debug(stringBuilder);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
		query.addQueryParameter(CODE, code);
		query.addQueryParameter("catalogVersion", catalogVersion);
		query.setResultClassList(Collections.singletonList(ProductModel.class));

		//disabling search restriction and then enabling once again
		SearchResult<ProductModel> searchResult = null;
		try
		{
			searchResult = getFlexibleSearchService().search(query);
			if (null != searchResult && null != searchResult.getResult() && searchResult.getResult().isEmpty())
			{
				searchRestrictionService.disableSearchRestrictions();
				searchResult = getFlexibleSearchService().search(query);
			}
		}
		finally
		{
			searchRestrictionService.enableSearchRestrictions();
		}

		productList = searchResult.getResult();
		//return searchResult.getResult();
		return productList;
	}

	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
		return catalogVersionModel;
	}


	//TISPRD-1631 Changes Start
	//Get Session Catalog Version
	private CatalogVersionModel getCatalogVersionSession()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService
				.getSessionCatalogVersionForCatalog(MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID);
		return catalogVersionModel;
	}


	@Override
	public List<ProductModel> findProductsByCodeHero(final String code)
	{
		LOG.debug("findProductsByCode: code********** " + code);
		final CatalogVersionModel catalogVersion = getCatalogVersionSession();
		final StringBuilder stringBuilder = new StringBuilder(70);
		stringBuilder.append(SELECT_STRING).append(ProductModel.PK).append("} ");
		stringBuilder.append(FROM_STRING).append(ProductModel._TYPECODE).append(" AS p ");
		stringBuilder.append("JOIN ").append(SellerInformationModel._TYPECODE).append(" AS s ");
		stringBuilder.append("ON {s:").append(SellerInformationModel.PRODUCTSOURCE).append("}={p:").append(ProductModel.PK)
				.append("} } ");
		final String inPart = "{p:" + ProductModel.CODE + CODE_STRING + ProductModel.CATALOGVERSION
				+ "} = ?catalogVersion and  sysdate between {s.startdate} and {s.enddate} ";
		stringBuilder.append("WHERE ").append(inPart);
		LOG.debug("findProductsByCode: stringBuilder******* " + stringBuilder);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
		query.addQueryParameter(CODE, code);
		query.addQueryParameter("catalogVersion", catalogVersion);
		query.setResultClassList(Collections.singletonList(ProductModel.class));
		final SearchResult<ProductModel> searchResult = getFlexibleSearchService().search(query);
		LOG.debug("findProductsByCode: searchResult********** " + searchResult);
		return searchResult.getResult();
	}

	//TISPRD-1631 Changes End


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao#findProductFeaturesByCodeAndQualifier(java
	 * .lang.String, java.lang.String)
	 */

	/*
	 * Added by SAP for get prodcutFeture by given product & qualifier start
	 */
	@Override
	public List<ProductFeatureModel> findProductFeaturesByCodeAndQualifier(final String code, final String qualifier)
	{
		final StringBuilder stringBuilder = new StringBuilder(70);

		//Product
		stringBuilder.append(SELECT_STRING).append(ProductFeatureModel.PK).append("} ");
		stringBuilder.append(FROM_STRING).append(ProductFeatureModel._TYPECODE).append(" AS p }");
		final String conditionPart = ProductFeatureModel.PRODUCT + CODE_STRING + ProductFeatureModel.QUALIFIER + "} = (?qualifier)";
		stringBuilder.append("WHERE {p:").append(conditionPart);
		LOG.debug(stringBuilder);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
		query.addQueryParameter(CODE, code);
		query.addQueryParameter("qualifier", qualifier);
		query.setResultClassList(Collections.singletonList(ProductFeatureModel.class));
		final SearchResult<ProductFeatureModel> searchResult = getFlexibleSearchService().search(query);
		return searchResult.getResult();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao#findProductListByCodeList(de.hybris.platform
	 * .catalog.model.CatalogVersionModel, java.util.List)
	 */
	@Override
	public List<ProductModel> findProductListByCodeList(final CatalogVersionModel catalogVersion,
			final List<String> productCodeList)
	{
		// YTODO Auto-generated method stub
		List<ProductModel> productModelList = null;
		final StringBuilder productCodes = new StringBuilder(100);
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		try
		{

			if (productCodeList != null)
			{
				int cnt = 0;
				productCodes.append("( ");
				for (final String id : productCodeList)
				{
					cnt = cnt + 1;
					if (cnt == 1)
					{
						productCodes.append(PRODUCT_PARAM + (cnt));
					}
					else
					{
						productCodes.append(" OR " + PRODUCT_PARAM + (cnt));
					}
					queryParamMap.put("productParam" + (cnt), id);
				}
				productCodes.append(" )");
			}


			final String queryString = "SELECT {c.pk} FROM {Product AS c}  Where " + productCodes.toString()
					+ " AND {c.catalogVersion}=?catalogVersion";



			//LOG.debug("QueryStringFetchingPrice" + queryStringForPrice);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter("catalogVersion", catalogVersion);

			for (final Map.Entry<String, String> entry : queryParamMap.entrySet())
			{
				query.addQueryParameter(entry.getKey(), entry.getValue());
			}

			productModelList = getFlexibleSearchService().<ProductModel> search(query).getResult();


		}

		catch (final FlexibleSearchException e)
		{
			e.printStackTrace();
		}

		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return productModelList;
	}


}
