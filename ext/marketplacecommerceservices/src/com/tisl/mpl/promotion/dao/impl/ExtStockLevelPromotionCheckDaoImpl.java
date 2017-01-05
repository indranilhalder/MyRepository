/**
 *
 */
package com.tisl.mpl.promotion.dao.impl;

import de.hybris.platform.core.model.LimitedStockPromoInvalidationModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.promotion.dao.ExtStockLevelPromotionCheckDao;


/**
 * @author TCS
 *
 */
public class ExtStockLevelPromotionCheckDaoImpl extends AbstractItemDao implements ExtStockLevelPromotionCheckDao
{

	/*
	 * @see
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	private static final Logger LOG = Logger.getLogger(ExtStockLevelPromotionCheckDaoImpl.class);

	@Override
	public Map<String, Integer> getPromoInvalidationModelMap(final String codes, final String promoCode, final boolean sellerFlag)
	{
		// YTODO Auto-generated method stub
		final Map<String, Integer> stockCodeMap = new HashMap<String, Integer>();
		String queryString = "";
		try
		{
			if (sellerFlag)
			{
				queryString = //
				"select {b.ussid},SUM({b.usedUpCount}) from {" + LimitedStockPromoInvalidationModel._TYPECODE + " as b } "
						+ " where {b.promoCode}=?promoCode " + "  AND {b.ussid} in (" + codes + ") group by {b.ussid}";
			}
			else
			{
				queryString = //
				"select {b.productCode},SUM({b.usedUpCount}) from {" + LimitedStockPromoInvalidationModel._TYPECODE + " as b } "
						+ " where {b.promoCode}=?promoCode " + " AND {b.productCode} in (" + codes + ") group by {b.productCode}";
			}
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("promoCode", promoCode);
			query.setResultClassList(Arrays.asList(String.class, Integer.class));

			final SearchResult<List<Object>> result = search(query);
			if (CollectionUtils.isNotEmpty(result.getResult()))
			{

				for (final List<Object> row : result.getResult())
				{
					final String code = (String) row.get(0);
					final Integer stockCount = (Integer) row.get(1);
					stockCodeMap.put(code, stockCount);
				}
			}
		}

		catch (final FlexibleSearchException e)
		{
			LOG.error("error in search query" + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("exception getching the quantity count details aginst product/ussid" + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final Exception e)
		{
			LOG.error(e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		return stockCodeMap;

	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.promotion.dao.ExtStockLevelPromotionCheckDao#getPromoInvalidationList(java.lang.String)
	 */
	@Override
	public List<LimitedStockPromoInvalidationModel> getPromoInvalidationList(final String guid)
	{
		// YTODO Auto-generated method stub
		String priceQueryString = "";
		try
		{
			priceQueryString = "SELECT {promoInvalid.PK} FROM {LimitedStockPromoInvalidation AS promoInvalid} where {promoInvalid.guid} =?guid";
			final FlexibleSearchQuery query = new FlexibleSearchQuery(priceQueryString);
			query.addQueryParameter("guid", guid);

			return flexibleSearchService.<LimitedStockPromoInvalidationModel> search(query).getResult();
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

	//TPR-965 changes for price update
	@Override
	public List<String> getStockForPromotion(final String promoCode, final int stockCount)
	{
		// YTODO Auto-generated method stub
		final List<String> stockList = new ArrayList<String>();
		String queryString = "";
		try
		{

			queryString = //
			"select  {b.ussid}  from {" + LimitedStockPromoInvalidationModel._TYPECODE + " as b }"
					+ " where {b.promoCode}=?promoCode " + "group by {b.promoCode},{b.ussid}  having SUM({b.usedUpCount})=?maxStock";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("promoCode", promoCode);
			query.addQueryParameter("maxStock", Integer.valueOf(stockCount));
			query.setResultClassList(Arrays.asList(String.class));
			final SearchResult<List<Object>> result = search(query);
			if (CollectionUtils.isNotEmpty(result.getResult()))
			{

				for (final Object row : result.getResult())
				{
					final String code = (String) row;
					stockList.add(code);
				}
			}
		}

		catch (final FlexibleSearchException e)
		{
			LOG.error("error in search query" + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("exception getching the quantity count details aginst product/ussid" + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final Exception e)
		{
			LOG.error(e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		return stockList;
	}


	/**
	 * @param promoCode
	 * @param orginalUid
	 */
	@Override
	public int getCummulativeOrderCount(final String promoCode, final String orginalUid)
	{
		// YTODO Auto-generated method stub
		int count = 0;
		String queryString = "";
		try
		{
			queryString = "select {pK} from {LimitedStockPromoInvalidation} where {promoCode}=?promoCodeData and {customerID}=?orginalUid";

			final Map<String, Object> params = new HashMap<String, Object>(1);
			params.put("promoCodeData", promoCode);
			params.put("orginalUid", orginalUid);

			final SearchResult<LimitedStockPromoInvalidationModel> searchList = flexibleSearchService.search(queryString, params);
			if (null != searchList && searchList.getCount() > 0)
			{
				count = searchList.getCount();
			}
		}

		catch (final FlexibleSearchException e)
		{
			LOG.error("error in search query" + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("exception getching the quantity count details aginst product/ussid" + e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final Exception e)
		{
			LOG.error(e);
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		return count;
	}
}
