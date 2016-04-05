/**
 *
 */
package com.tisl.mpl.promotion.dao.impl;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.promotion.dao.UpdatePromotionalPriceDao;



/**
 * @author TCS
 *
 */
public class UpdatePromotionalPriceDaoImpl implements UpdatePromotionalPriceDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(UpdatePromotionalPriceDaoImpl.class.getName());

	/**
	 * Fetch Price Row Details for Product(Staged + Online)
	 *
	 * @param productList
	 */
	@Override
	public List<PriceRowModel> fetchPricedData(final List<String> productList)
	{
		final int oracleAlloweLimit = 1000;
		int startIndex = 0;
		int endIndex = oracleAlloweLimit;

		List<PriceRowModel> priceRow = null;
		final StringBuilder query = new StringBuilder(100);
		final String queryPart1 = "SELECT {pri." + PriceRowModel.PK + "} ";
		final String queryPart2 = "FROM {" + PriceRowModel._TYPECODE + " AS pri} ";
		final String queryPart3 = "WHERE {pri." + PriceRowModel.PRODUCT + "} in (?";
		final String queryPart4 = ")";
		final String queryHead = "SELECT * FROM (";
		final String queryTail = "	) query";
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		int count = 0;

		if (productList.size() > oracleAlloweLimit)
		{
			query.append(queryHead);
			while (true)
			{
				if (startIndex != 0)
				{
					query.append(" UNION ALL ");
				}
				query.append("{{");
				final List<String> subList = productList.subList(startIndex, endIndex);
				final String paramName = "param" + (++count);
				queryParams.put(paramName, subList);
				query.append(queryPart1).append(queryPart2).append(queryPart3).append(paramName).append(queryPart4);
				startIndex = endIndex;
				if ((endIndex + oracleAlloweLimit) > productList.size())
				{
					endIndex = productList.size();
				}
				else
				{
					endIndex = endIndex + oracleAlloweLimit;
				}

				query.append("}}");

				if (startIndex == endIndex)
				{
					break;
				}

				LOG.debug("Query--------------------->:" + query.toString());
			}

			query.append(queryTail);
			LOG.debug("--------FINAL-----------Query-->:" + query.toString());
		}
		else
		{
			query.append("SELECT {priMdl." + PriceRowModel.PK + "} ");
			query.append("FROM {" + PriceRowModel._TYPECODE + " AS priMdl} ");
			query.append("WHERE {priMdl." + PriceRowModel.PRODUCT + "} in (?" + PriceRowModel.PRODUCT + ")");

			queryParams.put(PriceRowModel.PRODUCT, productList);
		}

		final SearchResult<PriceRowModel> searchRes = flexibleSearchService.search(query.toString(), queryParams);
		if (searchRes != null)
		{
			priceRow = searchRes.getResult();
		}
		return priceRow;
	}
}
