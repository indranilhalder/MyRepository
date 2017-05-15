package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.enums.SellerAssociationStatusEnum;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */

public class BuyBoxDaoImpl extends AbstractItemDao implements BuyBoxDao
{



	private static final Logger LOG = Logger.getLogger(BuyBoxDaoImpl.class.getName());


	@Autowired
	private FlexibleSearchService flexibleSearchService;
	private static final String SELECT_CLASS = "SELECT {bb.PK} FROM {";
	private static final String AS_CLASS = " AS bb}";

	private static final String WHERE_CLASS = " WHERE {bb:";

	private static final String QUERY_CLASS = "queryString:";

	private static final String PRODUCT_PARAM = "{bb.product}=?productParam";





	/*
	 * This method is responsible for get the price for a buybox wining seller against a product code.
	 * 
	 * @param productCode
	 * 
	 * @return flexibleSearchService.<BuyBoxModel> search(query).getResult()
	 * 
	 * @throws EtailNonBusinessExceptions
	 */

	//Performance fix - use bind variable - TISPRD-9025
	@Override
	public List<BuyBoxModel> buyBoxPrice(final String productCode)
	{
		final StringBuilder productCodes = new StringBuilder(100);
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		try
		{
			//TISPRM-56
			if (productCode.indexOf(MarketplacecommerceservicesConstants.COMMA) != -1)//if multiple products
			{

				final String[] codes = productCode.split(MarketplacecommerceservicesConstants.COMMA);
				int cnt = 0;
				productCodes.append("( ");
				for (final String id : codes)
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
			else
			//if no variant
			{
				productCodes.append("( ");
				productCodes.append(" {bb.product}=?productParam1");
				queryParamMap.put("productParam1", productCode);
				productCodes.append(" )");
			}

			final String queryStringForPrice = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS + " Where " + productCodes.toString()
					+ " AND ( {bb:" + BuyBoxModel.DELISTED + "}  IS NULL OR {bb:" + BuyBoxModel.DELISTED + "}=0)    AND   {bb:"
					+ BuyBoxModel.AVAILABLE + "} > 0 AND (sysdate between  {bb:" + BuyBoxModel.SELLERSTARTDATE + "} and {bb:"
					+ BuyBoxModel.SELLERENDDATE + "}) AND {bb:" + BuyBoxModel.PRICE + "} > 0  ORDER BY {bb:" + BuyBoxModel.WEIGHTAGE
					+ "} DESC,{bb:" + BuyBoxModel.AVAILABLE + "} DESC";

			LOG.debug("QueryStringFetchingPrice" + queryStringForPrice);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStringForPrice);

			for (final Map.Entry<String, String> entry : queryParamMap.entrySet())
			{
				query.addQueryParameter(entry.getKey(), entry.getValue());
			}

			final List<BuyBoxModel> retList = flexibleSearchService.<BuyBoxModel> search(query).getResult();
			return retList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	//CKD:TPR-250:Start : Exactly same method as buyboxPrice only to have a different path till DAO
	//which uses a query without having clause for availability
	@Override
	public List<BuyBoxModel> buyboxPriceForMicrosite(final String productCode, final String sellerId)
	{
		final StringBuilder productCodes = new StringBuilder(100);
		final Map<String, String> queryParamMap = new HashMap<String, String>();
		try
		{
			//TISPRM-56
			if (productCode.indexOf(MarketplacecommerceservicesConstants.COMMA) != -1)//if multiple products
			{

				final String[] codes = productCode.split(MarketplacecommerceservicesConstants.COMMA);
				int cnt = 0;
				productCodes.append("( ");
				for (final String id : codes)
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
			else
			//if no variant
			{
				productCodes.append("( ");
				productCodes.append(" {bb.product}=?productParam1");
				queryParamMap.put("productParam1", productCode);
				queryParamMap.put("sellerid", sellerId);
				productCodes.append(" )");
			}

			final String queryStringForPrice = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS + " Where " + productCodes.toString()
					+ " AND ( {bb:" + BuyBoxModel.DELISTED + "}  IS NULL OR {bb:" + BuyBoxModel.DELISTED
					+ "}=0) AND (sysdate between  {bb:" + BuyBoxModel.SELLERSTARTDATE + "} and {bb:" + BuyBoxModel.SELLERENDDATE
					+ "}) AND {bb:" + BuyBoxModel.SELLERID + "}=?sellerid  ORDER BY {bb:" + BuyBoxModel.WEIGHTAGE + "} DESC,{bb:"
					+ BuyBoxModel.AVAILABLE + "} DESC";

			LOG.debug("QueryStringFetchingPrice" + queryStringForPrice);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStringForPrice);

			for (final Map.Entry<String, String> entry : queryParamMap.entrySet())
			{
				query.addQueryParameter(entry.getKey(), entry.getValue());
			}

			final List<BuyBoxModel> retList = flexibleSearchService.<BuyBoxModel> search(query).getResult();
			return retList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	//CKD:TPR-250:End

	//	@Override
	//	public List<BuyBoxModel> buyBoxPrice(String productCode)
	//	{
	//
	//		//final String COMMA_SEPARATED = ",";
	//
	//		final String SEMICOLON = "'";
	//		try
	//		{
	//			//TISPRM-56
	//			if (productCode.indexOf(MarketplacecommerceservicesConstants.COMMA) != -1)
	//			{
	//				final StringBuilder stringBuilder = new StringBuilder();
	//				final String[] codes = productCode.split(MarketplacecommerceservicesConstants.COMMA);
	//				for (final String id : codes)
	//				{
	//					stringBuilder.append('\'').append(escapeString(id)).append('\'')
	//							.append(MarketplacecommerceservicesConstants.COMMA);
	//				}
	//				final int index = stringBuilder.lastIndexOf(MarketplacecommerceservicesConstants.COMMA);
	//				productCode = stringBuilder.replace(index, index + 1, "").toString();
	//			}
	//
	//
	//			if (productCode.indexOf(SEMICOLON) == -1)
	//			{
	//				productCode = MarketplacecommerceservicesConstants.INVERTED_COMMA + productCode
	//						+ MarketplacecommerceservicesConstants.INVERTED_COMMA;
	//			}
	//
	//			final String queryStringForPrice = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS + WHERE_CLASS + BuyBoxModel.PRODUCT
	//					+ "} IN (" + productCode + ") AND ( {bb:" + BuyBoxModel.DELISTED + "}  IS NULL OR {bb:" + BuyBoxModel.DELISTED
	//					+ "}=0)    AND   {bb:" + BuyBoxModel.AVAILABLE + "} > 0 AND (sysdate between  {bb:" + BuyBoxModel.SELLERSTARTDATE
	//					+ "} and {bb:" + BuyBoxModel.SELLERENDDATE + "}) AND {bb:" + BuyBoxModel.PRICE + "} > 0  ORDER BY {bb:"
	//					+ BuyBoxModel.WEIGHTAGE + "} DESC,{bb:" + BuyBoxModel.AVAILABLE + "} DESC";
	//
	//			log.debug("QueryStringFetchingPrice" + queryStringForPrice);
	//			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStringForPrice);
	//			//query.addQueryParameter("productBuyBox", productCode);
	//			return flexibleSearchService.<BuyBoxModel> search(query).getResult();
	//		}
	//		catch (final FlexibleSearchException e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
	//		}
	//		catch (final UnknownIdentifierException e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
	//		}
	//		catch (final Exception e)
	//		{
	//			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
	//		}
	//	}


	/*
	 * This method is responsible for get the price for a buybox wining seller against a product code.
	 * 
	 * @param productCode
	 * 
	 * @return flexibleSearchService.<BuyBoxModel> search(query).getResult()
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<BuyBoxModel> getBuyboxPricesForSearch(String productCode) throws EtailNonBusinessExceptions
	{
		String priceQueryString = null;
		try
		{
			if (productCode.contains("'"))
			{
				productCode = productCode.replaceAll("\'", "");
			}

			//priceQueryString = "SELECT {bb.PK} FROM {BuyBox AS bb} where {bb.product}= ?product AND ( {bb.delisted}  IS NULL OR {bb.delisted} =0)  and (sysdate between {bb.sellerstartdate} and {bb.sellerenddate})     ORDER BY {bb.product} ASC, {bb.weightage} DESC";

			priceQueryString = "SELECT {bb.PK} FROM {BuyBox AS bb} where {bb.product}= ?product AND ( {bb.delisted}  IS NULL OR {bb.delisted} =0)  and (sysdate between {bb.sellerstartdate} and {bb.sellerenddate})     ORDER BY {bb.product} ASC, {bb.weightage} DESC";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(priceQueryString);
			query.addQueryParameter("product", productCode);
			final List<BuyBoxModel> buyBoxList = flexibleSearchService.<BuyBoxModel> search(query).getResult();
			return buyBoxList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	//INC144315542_INC144314878_INC_11113
	@Override
	public List<BuyBoxModel> getBuyboxPricesForSizeVariant(String productCode) throws EtailNonBusinessExceptions
	{
		String priceString = null;
		try
		{
			if (productCode.contains("'"))
			{
				productCode = productCode.replaceAll("\'", "");
			}
			//PRDI-49
			//priceQueryString = "SELECT {bb.PK} FROM {BuyBox AS bb} where {bb.product}= ?product AND ( {bb.delisted}  IS NULL OR {bb.delisted} =0)  and (sysdate between {bb.sellerstartdate} and {bb.sellerenddate})     ORDER BY {bb.product} ASC, {bb.weightage} DESC";

			priceString = "SELECT {bb.PK} FROM {BuyBox AS bb} where {bb.product}= ?product AND (sysdate between {bb.sellerstartdate} and {bb.sellerenddate})     ORDER BY {bb.product} ASC, {bb.weightage} DESC";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(priceString);
			query.addQueryParameter("product", productCode);
			final List<BuyBoxModel> buyBoxList = flexibleSearchService.<BuyBoxModel> search(query).getResult();
			return buyBoxList;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * This method is responsible for get the inventory for a buybox wining seller against a product code.
	 * 
	 * @param productCode
	 * 
	 * @return flexibleSearchService.<BuyBoxModel> search(query).getResult()
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public Integer getBuyboxAvailableInventoryForSearch(final String productCode, final String productType)
			throws EtailNonBusinessExceptions
	{
		try
		{

			//Integer available = new Integer(0);
			Integer available = Integer.valueOf(0);
			final StringBuffer inventoryQuery = new StringBuffer(700);
			inventoryQuery.append("SELECT SUM({bb.available}) FROM {BuyBox AS bb} where ");

			if (productType.equalsIgnoreCase("simple"))
			{
				inventoryQuery
						.append("{bb.product} = ?product AND ( {bb.delisted}  IS NULL or {bb.delisted} =0 )and (sysdate between {bb.sellerstartdate} and {bb.sellerenddate})  AND {bb.available} >=0 "); //  INC144316749 AND {bb.available} >=0 is added to exclude negative stock while taking sum
			}
			if (productType.equalsIgnoreCase("variant")) // INC144315893  Added fix (color condition added) for prod Issue of showing Product even though out of stock  - /*, {PcmProductVariant As pv} where {pprod.colour} = {pv.colour} and {pv.code} = ?product and */
			{
				inventoryQuery
						.append("( {bb.delisted}  IS NULL or {bb.delisted} =0 ) AND {bb.product} IN ({{ select distinct{pprod.code} from {PcmProductVariant As pprod}, {PcmProductVariant As pv} where {pprod.colour} = {pv.colour} and {pv.code} = ?product and {pprod.baseProduct} IN (	{{"

								+ " 	select distinct{p.baseProduct} from {PcmProductVariant as p} where {p.code} = ?product"

								+ " 	}})}})" + "AND {bb.available} >=0"); // INC144316749  AND {bb.available} >=0 is added to exclude negative stock while taking sum
			}

			final FlexibleSearchQuery instockQuery = new FlexibleSearchQuery(inventoryQuery.toString());
			final List resultClassList = new ArrayList();
			resultClassList.add(Integer.class);
			instockQuery.setResultClassList(resultClassList);
			instockQuery.addQueryParameter("product", productCode);
			final List<Integer> instockResultList = flexibleSearchService.<Integer> search(instockQuery).getResult();
			//if (instockResultList != null && instockResultList.size() > 0)
			if (CollectionUtils.isNotEmpty(instockResultList))
			{
				available = instockResultList.get(0);
			}

			return available;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * This method is responsible for invalidating pk of the buybox sellers in the cache.
	 * 
	 * @param productCode
	 * 
	 * @return flexibleSearchService.<BuyBoxModel> search(query).getResult()
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<BuyBoxModel> invalidatePkofBuybox(final Date currenttime) throws EtailNonBusinessExceptions
	{

		try
		{
			final String queryString = "SELECT {bb.pk} FROM {buybox as bb} WHERE ({bb.delisted}  IS NULL or {bb.delisted} =0 ) AND  ( sysdate between {bb.sellerstartdate} and {bb.sellerenddate} ) and ({bb.modifiedtime} > ?currenttime) ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("currenttime", currenttime);

			LOG.debug("Query" + queryString);

			final List<BuyBoxModel> buyBoxList = flexibleSearchService.<BuyBoxModel> search(query).getResult();


			if (null != buyBoxList && !buyBoxList.isEmpty())
			{
				LOG.debug("buyBoxList pks size" + buyBoxList.size());

				return buyBoxList;
			}
			else
			{
				return null;
			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/*
	 * This method is responsible for get the buybox price for given product code if all the seller has stock zero
	 * 
	 * @param productCode
	 * 
	 * @return List<BuyBoxModel>
	 */
	@Override
	public List<BuyBoxModel> buyBoxPriceNoStock(final String productCode) throws EtailNonBusinessExceptions
	{
		try
		{
			final String queryString = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS

			+ WHERE_CLASS + BuyBoxModel.PRODUCT + "}=?productNoStock" + " AND  ( {bb:" + BuyBoxModel.DELISTED + "}  IS NULL or {bb:"
					+ BuyBoxModel.DELISTED + "}=0) and (sysdate between {bb.sellerstartdate} and {bb.sellerenddate}  )   and   {bb:"
					+ BuyBoxModel.PRICE + "} > 0  ORDER BY {bb:" + BuyBoxModel.WEIGHTAGE + "} DESC,{bb:" + BuyBoxModel.AVAILABLE
					+ "} DESC";


			LOG.debug(QUERY_CLASS + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("productNoStock", productCode);

			return flexibleSearchService.<BuyBoxModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao#getRichAttributeData(java.lang.String)
	 */
	@Override
	public RichAttributeModel getRichAttributeData(final String ussid) throws EtailNonBusinessExceptions
	{
		try
		{
			final String queryString = "select {rich." + RichAttributeModel.PK + "} from {" + SellerInformationModel._TYPECODE
					+ " as seller JOIN " + RichAttributeModel._TYPECODE + " as rich " + " ON {seller." + SellerInformationModel.PK
					+ "}={rich." + RichAttributeModel.SELLERINFO + "} JOIN " + CatalogVersionModel._TYPECODE + " as cat ON {rich."
					+ RichAttributeModel.CATALOGVERSION + "}={cat." + CatalogVersionModel.PK + "}} " + " where {seller."
					+ SellerInformationModel.SELLERARTICLESKU + "}=?sellerArticleSKU and {cat." + CatalogVersionModel.VERSION
					+ "} = ?catalogVersion ";

			LOG.debug(QUERY_CLASS + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("sellerArticleSKU", ussid);
			query.addQueryParameter("catalogVersion", "Online");
			query.addQueryParameter("isAssociated", SellerAssociationStatusEnum.YES);

			return flexibleSearchService.<RichAttributeModel> search(query).getResult().get(0);
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * view seller details
	 */
	@Override
	public Set<Map<BuyBoxModel, RichAttributeModel>> getsellersDetails(final String productCode) throws EtailNonBusinessExceptions
	{
		final Set<Map<BuyBoxModel, RichAttributeModel>> buyboxdataset = new LinkedHashSet<Map<BuyBoxModel, RichAttributeModel>>();
		try
		{
			final String queryString = "select {b.pk},{rich.pk} from {"
					+ BuyBoxModel._TYPECODE
					+ " as b JOIN "
					+ SellerInformationModel._TYPECODE
					+ " as seller ON {b.sellerArticleSKU}={seller.sellerArticleSKU} "
					+ " JOIN CatalogVersion as cat ON {cat.pk}={seller.catalogversion} "
					+ " JOIN RichAttribute as rich  ON {seller.pk}={rich.sellerInfo} } "
					+ " where {cat.version}='Online' and {b.product} = ?productCode and( {b.delisted}  IS NULL or {b.delisted}=0 )  and (sysdate between {b.sellerstartdate} and {b.sellerenddate})     order by {b.weightage} desc,{b.available} desc";

			LOG.debug(QUERY_CLASS + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("productCode", productCode);
			query.addQueryParameter("isAssociated", SellerAssociationStatusEnum.YES);

			query.setResultClassList(Arrays.asList(BuyBoxModel.class, RichAttributeModel.class));

			final SearchResult<List<Object>> result = search(query);
			if (result.getResult().isEmpty())
			{
				throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B3000);
			}
			else
			{
				for (final List<Object> row : result.getResult())
				{
					final Map<BuyBoxModel, RichAttributeModel> resultMap = new HashMap<BuyBoxModel, RichAttributeModel>();
					final BuyBoxModel buyBox = (BuyBoxModel) row.get(0);
					final RichAttributeModel rich = (RichAttributeModel) row.get(1);
					resultMap.put(buyBox, rich);
					buyboxdataset.add(resultMap);
				}
			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return buyboxdataset;

	}

	/*
	 * This method is responsible for get the buybox price for given ussid if all the seller has stock zero
	 * 
	 * @param productCode
	 * 
	 * @return List<BuyBoxModel>
	 */
	@Override
	public List<BuyBoxModel> getBuyBoxPriceForUssId(final String ussid) throws EtailNonBusinessExceptions
	{

		final String queryString = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS

		+ WHERE_CLASS + BuyBoxModel.SELLERARTICLESKU + "}=?sellerArticleSKU" + " AND {bb:" + BuyBoxModel.PRICE + "} > 0  ";

		//Blocked for Perfomance Fix : TISPT-167
		//+ "ORDER BY {bb:" + BuyBoxModel.WEIGHTAGE + "} DESC,{bb:" + BuyBoxModel.AVAILABLE + "} DESC"


		LOG.debug(QUERY_CLASS + queryString);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("sellerArticleSKU", ussid);

		return flexibleSearchService.<BuyBoxModel> search(query).getResult();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao#priceForUssid(java.lang.String)
	 */
	@Override
	public BuyBoxModel priceForUssid(final String ussid)
	{
		try
		{
			final String queryString = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS

			+ WHERE_CLASS + BuyBoxModel.SELLERARTICLESKU + "}=?ussid";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("ussid", ussid);

			return flexibleSearchService.<BuyBoxModel> search(query).getResult().get(0);
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/*
	 * This method is responsible for get the price for a buybox wining seller against a product code.
	 * 
	 * @param productCode
	 * 
	 * @return flexibleSearchService.<BuyBoxModel> search(query).getResult()
	 * 
	 * @throws EtailNonBusinessExceptions
	 */

	@Override
	public List<BuyBoxModel> buyBoxStockForSeller(final String sellerID)
	{
		try
		{

			final String queryStringForStock = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS + WHERE_CLASS + BuyBoxModel.SELLERID
					+ "}=?sellerid" + " AND  {bb:" + BuyBoxModel.AVAILABLE + "}  > 0";

			LOG.debug("QueryFetchingStock" + queryStringForStock);
			final FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(queryStringForStock);
			flexQuery.addQueryParameter("sellerid", sellerID);
			return flexibleSearchService.<BuyBoxModel> search(flexQuery).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * Get Buybox data in respect of product code and seller id
	 *
	 * @param productCode
	 * @param sellerID
	 * @return
	 */
	@Override
	public BuyBoxModel buyBoxForSizeGuide(final String productCode, final String sellerID)
	{

		try
		{

			final String queryStringForSizeGuide = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS

			+ WHERE_CLASS + BuyBoxModel.PRODUCT + "}=?productSizeGuide" + " AND  {bb:" + BuyBoxModel.SELLERID + "}=?sellerid"
					+ " AND ( {bb:" + BuyBoxModel.DELISTED + "}  IS NULL OR {bb:" + BuyBoxModel.DELISTED
					+ "}=0) AND (sysdate between  {bb:" + BuyBoxModel.SELLERSTARTDATE + "} and {bb:" + BuyBoxModel.SELLERENDDATE
					+ "}) AND {bb:" + BuyBoxModel.PRICE + "} > 0";
			LOG.debug(String.format("buyboxForSizeGuide : Query fetching SizeGuide:  %s ", queryStringForSizeGuide));

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryStringForSizeGuide);
			query.addQueryParameter("productSizeGuide", productCode);
			query.addQueryParameter("sellerid", sellerID);
			return flexibleSearchService.<BuyBoxModel> searchUnique(query);
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	//	private String escapeString(final String id)
	//	{
	//		String escapedId = id;
	//		if (null != id)
	//		{
	//			escapedId = id.replaceAll("'", "''");
	//		}
	//
	//		return escapedId;
	//	}

	@Override
	public List<ClassAttributeAssignmentModel> getClassAttrAssignmentsForCode(final String code)
	{
		try
		{
			final String classAttrquery = "select {clattass.pk} from {ClassAttributeAssignment as clattass} "
					+ "									 where {clattass.classificationAttribute} " + "	 IN ({{"
					+ "												select {clattr.pk} from {ClassificationAttribute as clattr}  where {clattr.code} " + "		IN ('"
					+ code + "')}})";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(classAttrquery);
			return flexibleSearchService.<ClassAttributeAssignmentModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}



	/*
	 * Get Buybox data in respect of ussid (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao#getBuyBoxDataForUssids(java.util.List)
	 */
	//TPR-3736
	@Override
	public Map<String, List<Double>> getBuyBoxDataForUssids(final String ussidList) throws EtailNonBusinessExceptions
	{
		final Map<String, List<Double>> buyBoxData = new HashMap<String, List<Double>>();

		try
		{

			final String queryString = //
			"select {b.pk} from {" + BuyBoxModel._TYPECODE + " as b } " + " where {b.available}>0 and {b.mrp}>0"
					+ "  AND {b.sellerArticleSKU} in (" + ussidList + ")";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			final List<BuyBoxModel> buyBoxList = flexibleSearchService.<BuyBoxModel> search(query).getResult();
			if (CollectionUtils.isNotEmpty(buyBoxList))
			{
				for (final BuyBoxModel buyBox : buyBoxList)
				{
					final List<Double> priceList = new ArrayList<Double>();
					priceList.add(buyBox.getMrp());
					priceList.add(buyBox.getPrice() == null ? Double.valueOf(0.0D) : buyBox.getPrice());
					priceList.add(buyBox.getPrice() == null ? Double.valueOf(0.0D) : buyBox.getSpecialPrice());
					buyBoxData.put(buyBox.getSellerArticleSKU(), priceList);
					LOG.debug("#####################skuList" + buyBox.getSellerArticleSKU() + priceList);
				}
			}
			return buyBoxData;

		}


		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}


	}

}
