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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.util.CatalogUtils;


/**
 * @author TCS
 *
 */
public class MplProductDaoImpl extends DefaultProductDao implements MplProductDao
{

	/*
	 * @Autowired private CatalogVersionService catalogVersionService;
	 */

	@Autowired
	private SearchRestrictionService searchRestrictionService;

	@Autowired
	private CatalogUtils catalogUtils;

	@Autowired
	private CatalogVersionService catalogVersionService;

	protected static final Logger LOG = Logger.getLogger(MplProductDaoImpl.class);

	private static final String SELECT_STRING = "SELECT  distinct {p:";
	private static final String FROM_STRING = "FROM {";
	private static final String CODE_STRING = "} = (?code) AND {p:";
	private static final String CODE = "code";
	private static final String PRODUCT_PARAM = "{c.code}=?productParam";
	private static final String AS_P = " AS p ";
	private static final String AS_S = " AS s ";
	private static final String ON_S = "ON {s:";
	private static final String BRACET_P = "}={p:";
	private static final String P = "{p:";


	//Sonar Fix
	private static final String CATALOG_VERSION_KEY = "catalogVersion";


	public MplProductDaoImpl(final String typecode)
	{
		super(typecode);
	}


	@Override
	public List<ProductModel> findProductsByCode(final String code)
	{
		LOG.debug("findProductsByCode: code********** " + code);
		final CatalogVersionModel catalogVersion = catalogUtils.getSessionCatalogVersionForProduct();
		final StringBuilder stringBuilder = new StringBuilder(70);

		stringBuilder.append(SELECT_STRING).append(ProductModel.PK).append("} ").append(FROM_STRING).append(ProductModel._TYPECODE)
				.append(AS_P).append(MarketplacecommerceservicesConstants.QUERYJOIN).append(SellerInformationModel._TYPECODE)
				.append(AS_S).append(ON_S).append(SellerInformationModel.PRODUCTSOURCE).append(BRACET_P).append(ProductModel.PK)
				.append("} ").append(MarketplacecommerceservicesConstants.QUERYJOIN).append(CatalogVersionModel._TYPECODE)
				.append(" AS cat ").append("ON {p:").append(ProductModel.CATALOGVERSION).append("}={cat:")
				.append(CatalogVersionModel.PK).append("}}");

		final String inPart = P + ProductModel.CODE + "} = (?code) AND {cat:" + CatalogVersionModel.PK
				+ "} = ?catalogVersion and  sysdate between {s.startdate} and {s.enddate} ";
		stringBuilder.append(" WHERE ").append(inPart);

		LOG.debug("findProductsByCode: stringBuilder******* " + stringBuilder);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());

		query.addQueryParameter(CODE, code);

		query.addQueryParameter(CATALOG_VERSION_KEY, catalogVersion);
		query.setResultClassList(Collections.singletonList(ProductModel.class));
		final SearchResult<ProductModel> searchResult = getFlexibleSearchService().search(query);
		LOG.debug("findProductsByCode: searchResult********** " + searchResult);
		return searchResult.getResult();
	}

	@Override
	public List<ProductModel> findProductsByCodeNew(final String code)
	{
		List<ProductModel> productList = null;
		final CatalogVersionModel catalogVersion = catalogUtils.getSessionCatalogVersionForProduct();
		final StringBuilder stringBuilder = new StringBuilder(70);

		stringBuilder.append(SELECT_STRING).append(ProductModel.PK).append("} ");
		stringBuilder.append(FROM_STRING).append(ProductModel._TYPECODE).append(AS_P);
		stringBuilder.append(MarketplacecommerceservicesConstants.QUERYJOIN).append(SellerInformationModel._TYPECODE).append(AS_S);
		stringBuilder.append(ON_S).append(SellerInformationModel.PRODUCTSOURCE).append(BRACET_P).append(ProductModel.PK)
				.append("} } ");
		final String inPart = P + ProductModel.CODE + CODE_STRING + ProductModel.CATALOGVERSION + "} = ?catalogVersion";
		stringBuilder.append("WHERE ").append(inPart);
		LOG.debug(stringBuilder);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
		query.addQueryParameter(CODE, code);
		query.addQueryParameter(CATALOG_VERSION_KEY, catalogVersion);
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

	/*
	 * private CatalogVersionModel getCatalogVersion() { final CatalogVersionModel catalogVersionModel =
	 * catalogVersionService.getCatalogVersion( MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
	 * MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION); return catalogVersionModel; }
	 */

	@Override
	public List<ProductModel> findProductsByCodeHero(final String code)
	{
		LOG.debug("findProductsByCode: code********** " + code);
		//		Changed to support the luxProductCatalog
		final CatalogVersionModel catalogVersion = catalogUtils.getSessionCatalogVersionForProduct();
		final StringBuilder stringBuilder = new StringBuilder(70);
		stringBuilder.append(SELECT_STRING).append(ProductModel.PK).append("} ");
		stringBuilder.append(FROM_STRING).append(ProductModel._TYPECODE).append(AS_P);
		stringBuilder.append(MarketplacecommerceservicesConstants.QUERYJOIN).append(SellerInformationModel._TYPECODE).append(AS_S);
		stringBuilder.append(ON_S).append(SellerInformationModel.PRODUCTSOURCE).append(BRACET_P).append(ProductModel.PK)
				.append("} } ");
		//Changed to support the luxProductCatalog
		// No need to add the catalog version as Search restriction will add automatically into where clause
		final String inPart = P + ProductModel.CODE + CODE_STRING + ProductModel.CATALOGVERSION
				+ "} = ?catalogVersion and  sysdate between {s.startdate} and {s.enddate} ";
		//		final String inPart = P + ProductModel.CODE + CODE_STRING + " and  sysdate between {s.startdate} and {s.enddate} ";

		stringBuilder.append("WHERE ").append(inPart);
		LOG.debug("findProductsByCode: stringBuilder******* " + stringBuilder);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
		query.addQueryParameter(CODE, code);
		//		Changed to support the luxProductCatalog
		query.addQueryParameter(CATALOG_VERSION_KEY, catalogVersion);
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

			query.addQueryParameter(CATALOG_VERSION_KEY, catalogVersion);

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


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao#findProductData(java.lang.String)
	 */
	@Override
	public ProductModel findProductData(final String code)
	{

		ProductModel prod = new ProductModel();

		LOG.debug("findProductsByCode: code********** " + code);
		final String catalogVersion = "Online";
		final StringBuilder stringBuilder = new StringBuilder(70);
		stringBuilder.append(SELECT_STRING).append(ProductModel.PK).append("} ");
		stringBuilder.append(FROM_STRING).append(ProductModel._TYPECODE).append(AS_P);
		stringBuilder.append("JOIN ").append(SellerInformationModel._TYPECODE).append(AS_S);
		stringBuilder.append(ON_S).append(SellerInformationModel.PRODUCTSOURCE).append(BRACET_P).append(ProductModel.PK)
				.append("}} ");
		final String inPart = P + ProductModel.CODE + CODE_STRING + ProductModel.CATALOGVERSION + "} = ?catalogVersion";
		stringBuilder.append(" WHERE ").append(inPart);
		LOG.debug("findProductsByCode: stringBuilder******* " + stringBuilder);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
		query.addQueryParameter(CODE, code);
		query.addQueryParameter("catalogVersion", catalogVersion);
		query.setResultClassList(Collections.singletonList(ProductModel.class));
		final SearchResult<ProductModel> searchResult = getFlexibleSearchService().search(query);
		LOG.debug("findProductsByCode: searchResult********** " + searchResult);
		//	System.out.println("*************list Size" + searchResult.getResult().size());
		LOG.debug("*************list Size" + searchResult.getResult().size());
		if (CollectionUtils.isNotEmpty(searchResult.getResult()))
		{
			prod = searchResult.getResult().get(0);
		}
		return prod;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.product.MplProductDao#findProductForHasVariant(java.lang.String)
	 */
	@Override
	public List<ProductModel> findProductForHasVariant(final String code)
	{
		// YTODO Auto-generated method stub
		LOG.debug("findProductForHasVariant: code********** " + code);
		List<ProductModel> productString = null;
		try
		{
			final String query = "SELECT  distinct {p:" + ProductModel.PK + "} FROM {" + ProductModel._TYPECODE + " as p JOIN "
					+ SellerInformationModel._TYPECODE + " as s ON {s:" + SellerInformationModel.PRODUCTSOURCE + BRACET_P
					+ ProductModel.PK + "}} WHERE {p:" + ProductModel.CODE + "}=?code and {p.catalogversion}=?catalogversion";
			final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
			flexiQuery.addQueryParameter(CODE, code);
			flexiQuery.addQueryParameter("catalogversion", getCatalogVersion());

			searchRestrictionService.disableSearchRestrictions();
			final SearchResult<ProductModel> searchResult = getFlexibleSearchService().search(flexiQuery);
			LOG.debug("findProductForHasVariant: searchResult********** " + searchResult);
			productString = searchResult.getResult();
		}
		finally
		{
			searchRestrictionService.enableSearchRestrictions();
		}
		return productString;
	}

	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_ID,
				MarketplacecommerceservicesConstants.DEFAULT_IMPORT_CATALOG_VERSION);
		return catalogVersionModel;
	}

	@Override
	public String getSizeForSTWProduct(final String productCode)
	{
		List<String> size = null;
		final String query = "Select {pcmv.size} from {PcmProductVariant as pcmv} where {pcmv.code}=?productCode";

		try
		{
			final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
			flexiQuery.setResultClassList(Collections.singletonList(String.class));
			final SearchResult<String> searchResult = getFlexibleSearchService().search(flexiQuery);
			LOG.debug("findProductForHasVariant: searchResult********** " + searchResult);
			size = searchResult.getResult();
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		if (size != null)
		{
			return size.get(0);
		}
		else
		{
			return "";
		}
	}


	@Override
	public List<String> getVariantsForSTWProducts(final String productCode)
	{

		final String query = "select {pcm.colourHexCode} from {PcmProductVariant as pcm}}"
				+ " where {pcm.baseproduct} IN ({{SELECT {p.baseproduct} from {PcmProductVariant as p}})"
				+ " AND {pcm.pk} IN ({{select {p.pk} from {product as p} where {p.code} = '987654322' }})'";

		List<String> variants = null;
		try
		{
			final FlexibleSearchQuery flexiQuery = new FlexibleSearchQuery(query);
			//flexiQuery.setResultClassList(Collections.singletonList(String.class));
			final SearchResult<String> searchResult = getFlexibleSearchService().search(flexiQuery);
			LOG.debug("findProductForHasVariant: searchResult********** " + searchResult);
			variants = searchResult.getResult();
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
		if (variants != null)
		{
			return variants;
		}
		else
		{
			return new ArrayList<String>();
		}
	}
}
