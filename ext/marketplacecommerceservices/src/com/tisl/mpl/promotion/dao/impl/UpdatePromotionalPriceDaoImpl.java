/**
 *
 */
package com.tisl.mpl.promotion.dao.impl;

import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.PromotionalPriceRowModel;
import com.tisl.mpl.model.MplConfigurationModel;
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
	 * Fetch Price Row Details for Product(Staged + Online) Defect : Modified for TISPRD-938
	 *
	 * @param productList
	 */
	//@Override
	//	public List<PriceRowModel> fetchPricedData(final List<String> productList)
	//	{
	//		//Defect : Modified for TISPRD-938
	//		final int oracleSafetyLimit = 900; // Keeping Safety Limit as 900 (Max Permissible Value : 1000)
	//		int startIndex = 0;
	//		int endIndex = oracleSafetyLimit;
	//
	//		List<PriceRowModel> priceRow = null;
	//		final StringBuilder query = new StringBuilder(100);
	//		final String queryPart1 = "SELECT {pri." + PriceRowModel.PK + "} ";
	//		final String queryPart2 = "FROM {" + PriceRowModel._TYPECODE + " AS pri} ";
	//		final String queryPart3 = "WHERE {pri." + PriceRowModel.PRODUCT + "} in (?";
	//		final String queryPart4 = ")";
	//		final String queryHead = "SELECT * FROM (";
	//		final String queryTail = "	) query";
	//		final Map<String, Object> queryParams = new HashMap<String, Object>();
	//		int count = 0;
	//
	//		if (productList.size() > oracleSafetyLimit) // Logic Condition : Product PK List exceeds the oracleSafetyLimit
	//		{
	//			query.append(queryHead);
	//			while (true)
	//			{
	//				if (startIndex != 0)
	//				{
	//					query.append(" UNION ALL ");
	//				}
	//				query.append("{{");
	//				final List<String> subList = productList.subList(startIndex, endIndex);
	//				final String paramName = "param" + (++count);
	//				queryParams.put(paramName, subList);
	//				query.append(queryPart1).append(queryPart2).append(queryPart3).append(paramName).append(queryPart4);
	//				startIndex = endIndex;
	//				if ((endIndex + oracleSafetyLimit) > productList.size())
	//				{
	//					endIndex = productList.size();
	//				}
	//				else
	//				{
	//					endIndex = endIndex + oracleSafetyLimit;
	//				}
	//
	//				query.append("}}");
	//
	//				if (startIndex == endIndex)
	//				{
	//					break;
	//				}
	//
	//				LOG.info("Sub Query for Price Row Fetch->:" + query.toString());
	//			}
	//
	//			query.append(queryTail);
	//			LOG.info("--------FINAL-----------Query-->:" + query.toString());
	//		}
	//		else
	//		{
	//			// Logic Condition : Product PK List is less than the oracleSafetyLimit, Normal Query formation
	//			query.append("SELECT {priMdl." + PriceRowModel.PK + "} ");
	//			query.append("FROM {" + PriceRowModel._TYPECODE + " AS priMdl} ");
	//			query.append("WHERE {priMdl." + PriceRowModel.PRODUCT + "} in (?" + PriceRowModel.PRODUCT + ")");
	//
	//			queryParams.put(PriceRowModel.PRODUCT, productList);
	//		}
	//
	//		final SearchResult<PriceRowModel> searchRes = flexibleSearchService.search(query.toString(), queryParams);
	//		if (searchRes != null)
	//		{
	//			priceRow = searchRes.getResult();
	//		}
	//		return priceRow;
	//	}

	/**
	 * @Description : Fetch Cron Details for Last Run time
	 * @param: code
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		final String queryString = //
		"SELECT {cm:" + MplConfigurationModel.PK
				+ "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + MplConfigurationModel._TYPECODE + " AS cm } where" + "{cm."
				+ MplConfigurationModel.MPLCONFIGCODE + "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, code);
		return flexibleSearchService.<MplConfigurationModel> searchUnique(query);
	}

	/**
	 * Returns List of Price Row corresponding to a Promotion Code
	 *
	 * @param promoCode
	 */
	@Override
	public List<PromotionalPriceRowModel> fetchPromoPriceData(final String promoCode)
	{
		List<PromotionalPriceRowModel> priceRowList = null;
		if (StringUtils.isNotEmpty(promoCode))
		{
			final String queryString = "SELECT {pk} FROM {Promotionalpricerow} WHERE  {promotionIdentifier} = ?promoCode";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("promoCode", promoCode);
			priceRowList = flexibleSearchService.<PromotionalPriceRowModel> search(query).getResult();
		}
		return priceRowList;
	}

	/**
	 * The Method removes redundant data from DB
	 *
	 * @return priceRowList
	 */
	public List<PromotionalPriceRowModel> getRedundantData()
	{
		List<PromotionalPriceRowModel> priceRowList = null;

		try
		{
			final String queryString = "SELECT {pk} FROM {Promotionalpricerow} WHERE  {priceRow} IS NULL";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			priceRowList = flexibleSearchService.<PromotionalPriceRowModel> search(query).getResult();

			if (CollectionUtils.isNotEmpty(priceRowList))
			{
				return priceRowList;
			}
		}
		catch (final ModelNotFoundException exception)
		{
			LOG.error("Error in method" + "getRedundantData DAO Call");
			LOG.error("No Data Found" + exception);
		}
		catch (final Exception exception)
		{
			LOG.error("Error in method" + "getRedundantData DAO Call");
			LOG.error("No Data Found" + exception);
		}
		return null;
	}

	@Override
	public List<PriceRowModel> fetchPricedData(final List<String> ussidList)
	{
		//Defect : Modified for TISPRD-938
		final int oracleSafetyLimit = 900; // Keeping Safety Limit as 900 (Max Permissible Value : 1000)
		int startIndex = 0;
		int endIndex = oracleSafetyLimit;

		List<PriceRowModel> priceRow = null;
		final StringBuilder query = new StringBuilder(100);
		final String queryPart1 = "SELECT {pri." + PriceRowModel.PK + "} ";
		final String queryPart2 = "FROM {" + PriceRowModel._TYPECODE + " AS pri} ";
		final String queryPart3 = "WHERE {pri." + PriceRowModel.SELLERARTICLESKU + "} in (?";
		final String queryPart4 = ")";
		final String queryHead = "SELECT * FROM (";
		final String queryTail = "	) query";
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		int count = 0;

		if (ussidList.size() > oracleSafetyLimit) // Logic Condition : Product PK List exceeds the oracleSafetyLimit
		{
			query.append(queryHead);
			while (true)
			{
				if (startIndex != 0)
				{
					query.append(" UNION ALL ");
				}
				query.append("{{");
				final List<String> subList = ussidList.subList(startIndex, endIndex);
				final String paramName = "param" + (++count);
				queryParams.put(paramName, subList);
				query.append(queryPart1).append(queryPart2).append(queryPart3).append(paramName).append(queryPart4);
				startIndex = endIndex;
				if ((endIndex + oracleSafetyLimit) > ussidList.size())
				{
					endIndex = ussidList.size();
				}
				else
				{
					endIndex = endIndex + oracleSafetyLimit;
				}

				query.append("}}");

				if (startIndex == endIndex)
				{
					break;
				}

				LOG.info("Sub Query for Price Row Fetch->:" + query.toString());
			}

			query.append(queryTail);
			LOG.info("--------FINAL-----------Query-->:" + query.toString());
		}
		else
		{
			// Logic Condition : Product PK List is less than the oracleSafetyLimit, Normal Query formation
			query.append("SELECT {priMdl." + PriceRowModel.PK + "} ");
			query.append("FROM {" + PriceRowModel._TYPECODE + " AS priMdl} ");
			query.append("WHERE {priMdl." + PriceRowModel.SELLERARTICLESKU + "} in (?" + PriceRowModel.SELLERARTICLESKU + ")");

			queryParams.put(PriceRowModel.SELLERARTICLESKU, ussidList);
		}

		final SearchResult<PriceRowModel> searchRes = flexibleSearchService.search(query.toString(), queryParams);
		if (searchRes != null)
		{
			priceRow = searchRes.getResult();
		}
		return priceRow;
	}

}