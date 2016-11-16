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
	public Map<String, Integer> getPromoInvalidationModelMap(final String codes, final boolean sellerFlag)
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
						+ " where {b.ussid} in (" + codes + ") group by {b.ussid}";
			}
			else
			{
				queryString = //
				"select {b.productCode},SUM({b.usedUpCount}) from {" + LimitedStockPromoInvalidationModel._TYPECODE + " as b } "
						+ " where {b.productCode} in (" + codes + ") group by {b.productCode}";
			}
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
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
			//	throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(e);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			LOG.error("exception getching the quantity count details aginst product/ussid" + e);
		}
		catch (final Exception e)
		{
			LOG.error(e);
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
			priceQueryString = "SELECT {promoInvalid.PK} FROM {PromotionInvalidation AS promoInvalid} where {promoInvalid.guid} =?guid";
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
}