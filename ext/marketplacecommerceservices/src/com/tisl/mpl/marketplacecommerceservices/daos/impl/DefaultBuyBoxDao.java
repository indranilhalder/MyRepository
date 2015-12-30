package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.core.model.RichAttributeModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.BuyBoxDao;
import com.tisl.mpl.model.SellerInformationModel;


/**
 * @author TCS
 *
 */

public class DefaultBuyBoxDao extends AbstractItemDao implements BuyBoxDao
{



	private static final Logger log = Logger.getLogger(DefaultBuyBoxDao.class.getName());


	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final String SELECT_CLASS = "SELECT {bb.PK} FROM {";
	private static final String AS_CLASS = " AS bb}";

	private static final String WHERE_CLASS = " WHERE {bb:";

	private static final String QUERY_CLASS = "queryString:";
	private static final String DESC_CLASS = "} DESC";
	private static final String PRODUCT = "product";

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
	public List<BuyBoxModel> buyBoxPrice(final String productCode)
	{

		try
		{
			final String queryString = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS

			+ WHERE_CLASS + BuyBoxModel.PRODUCT + "}=?product" + " AND {bb:" + BuyBoxModel.AVAILABLE + "} > 0  AND {bb:"
					+ BuyBoxModel.PRICE + "} > 0  ORDER BY {bb:" + BuyBoxModel.WEIGHTAGE + DESC_CLASS;

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(PRODUCT, productCode);

			return getFlexibleSearchService().<BuyBoxModel> search(query).getResult();
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
	 * This method is responsible for get the price for a buybox wining seller against a product code.
	 * 
	 * @param productCode
	 * 
	 * @return flexibleSearchService.<BuyBoxModel> search(query).getResult()
	 * 
	 * @throws EtailNonBusinessExceptions
	 */
	@Override
	public List<BuyBoxModel> getBuyboxPricesForSearch(final String productCode) throws EtailNonBusinessExceptions
	{

		final String queryString = SELECT_CLASS + BuyBoxModel._TYPECODE + AS_CLASS

		+ WHERE_CLASS + BuyBoxModel.PRODUCT + "}=?product" + " ORDER BY {bb:" + BuyBoxModel.WEIGHTAGE + DESC_CLASS;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(PRODUCT, productCode);

		return getFlexibleSearchService().<BuyBoxModel> search(query).getResult();
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

		//Integer available = new Integer(0);  Critical Sonar fixes
		Integer available = Integer.valueOf(0);


		final StringBuffer inventoryQuery = new StringBuffer(350);
		inventoryQuery.append("SELECT SUM({bb.available}) FROM {BuyBox AS bb} where ");

		if (productType.equalsIgnoreCase("simple"))
		{
			inventoryQuery.append("{bb.product} = ?product");
		}
		if (productType.equalsIgnoreCase("variant"))
		{
			inventoryQuery
					.append("{bb.product} IN ({{ select distinct{pprod.code} from {PcmProductVariant As pprod} where {pprod.baseProduct} IN (	{{"

							+ " 	select distinct{p.baseProduct} from {PcmProductVariant as p} where {p.code} = ?product"

							+ " 	}})}})");
		}

		final FlexibleSearchQuery instockQuery = new FlexibleSearchQuery(inventoryQuery.toString());
		final List resultClassList = new ArrayList();
		resultClassList.add(Integer.class);
		instockQuery.setResultClassList(resultClassList);
		instockQuery.addQueryParameter(PRODUCT, productCode);
		final List<Integer> instockResultList = getFlexibleSearchService().<Integer> search(instockQuery).getResult();
		if (instockResultList != null && instockResultList.size() > 0)
		{
			available = instockResultList.get(0);
		}

		return available;
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
			final String queryString = "SELECT {bb.pk} FROM {buybox as bb} WHERE ({bb.modifiedtime} > ?currenttime) ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("currenttime", currenttime);

			log.debug("Query" + queryString);

			final List<BuyBoxModel> buyBoxList = getFlexibleSearchService().<BuyBoxModel> search(query).getResult();


			if (null != buyBoxList && !buyBoxList.isEmpty())
			{
				log.debug("buyBoxList pks size" + buyBoxList.size());

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

			+ WHERE_CLASS + BuyBoxModel.PRODUCT + "}=?product" + " AND {bb:" + BuyBoxModel.PRICE + "} > 0  ORDER BY {bb:"
					+ BuyBoxModel.WEIGHTAGE + DESC_CLASS;

			log.debug(QUERY_CLASS + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(PRODUCT, productCode);

			return getFlexibleSearchService().<BuyBoxModel> search(query).getResult();
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

			log.debug(QUERY_CLASS + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("sellerArticleSKU", ussid);
			query.addQueryParameter("catalogVersion", "Online");

			return getFlexibleSearchService().<RichAttributeModel> search(query).getResult().get(0);
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
			final String queryString = "select {b.pk},{rich.pk} from {" + BuyBoxModel._TYPECODE + " as b JOIN "
					+ SellerInformationModel._TYPECODE + " as seller ON {b.sellerArticleSKU}={seller.sellerArticleSKU} "
					+ " JOIN CatalogVersion as cat ON {cat.pk}={seller.catalogversion} "
					+ " JOIN RichAttribute as rich  ON {seller.pk}={rich.sellerInfo} } "
					+ " where {cat.version}='Online' and {b.product} = ?productCode order by {b.weightage} desc";

			log.debug(QUERY_CLASS + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("productCode", productCode);
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

		+ WHERE_CLASS + BuyBoxModel.SELLERARTICLESKU + "}=?sellerArticleSKU" + " AND {bb:" + BuyBoxModel.PRICE
				+ "} > 0  ORDER BY {bb:" + BuyBoxModel.WEIGHTAGE + DESC_CLASS;

		log.debug(QUERY_CLASS + queryString);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("sellerArticleSKU", ussid);

		return getFlexibleSearchService().<BuyBoxModel> search(query).getResult();
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

			return getFlexibleSearchService().<BuyBoxModel> search(query).getResult().get(0);
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
	 * @return the flexibleSearchService
	 */
	@Override
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}


	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	@Override
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}

