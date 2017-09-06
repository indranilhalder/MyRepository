/**
 *
 */
package com.tisl.mpl.promotion.dao.impl;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.PromotionalPriceRowModel;
import com.tisl.mpl.model.BuyABFreePrecentageDiscountModel;
import com.tisl.mpl.model.BuyAPercentageDiscountModel;
import com.tisl.mpl.model.MplConfigurationModel;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.promotion.dao.UpdatePromotionalPriceDao;
import com.tisl.mpl.promotion.helper.MplPromotionHelper;



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

	@Override
	public List<ProductPromotionModel> getSortedPromoListForProduct(final ProductModel product,
			final ProductPromotionModel promoCurrent)
	{
		final List<ProductPromotionModel> validPromosForProduct = new ArrayList<ProductPromotionModel>();
		final Collection<CategoryModel> categories = mplPromotionHelper.getcategoryData(product);

		if (promoCurrent instanceof BuyAPercentageDiscountModel)
		{
			final StringBuilder queryString = new StringBuilder("SELECT DISTINCT pprom.pk, pprom.prio FROM (");
			queryString.append(" {{ SELECT {p." + BuyAPercentageDiscountModel.PK + "} AS pk, {p."
					+ BuyAPercentageDiscountModel.PRIORITY + "} AS prio FROM {" + BuyAPercentageDiscountModel._TYPECODE + " AS p ");
			queryString.append(" JOIN " + GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION + " AS p2p ");
			queryString.append(" ON {p2p.target} = {p." + BuyAPercentageDiscountModel.PK + "}");
			queryString.append(" AND {p2p.source} = ?product }");
			//queryString.append(" WHERE {p." + BuyAPercentageDiscountModel.PROMOTIONGROUP + "} = ?promotionGroup ");
			//			queryString.append(" AND {p2p.source} IN (?product) ");
			queryString.append(" WHERE {p." + BuyAPercentageDiscountModel.PRIORITY + "} >= ?promoCurrPriority ");
			queryString.append(" AND {p." + BuyAPercentageDiscountModel.IMMUTABLEKEY + "} is NULL ");
			queryString.append(" AND {p." + BuyAPercentageDiscountModel.ENABLED + "} = ?true ");
			queryString.append(" AND {p." + BuyAPercentageDiscountModel.QUANTITY + "} = ?qualifyingCount ");//TODO quantity is for BuyAPercentageDiscount only, not for PRODUCTPROMOTION
			queryString.append(" AND {p." + BuyAPercentageDiscountModel.STARTDATE + "} <= ?sysdate ");
			queryString.append(" AND ?sysdate <= {p." + BuyAPercentageDiscountModel.ENDDATE + "} }}");

			if (CollectionUtils.isNotEmpty(categories))
			{
				queryString.append(MarketplacecommerceservicesConstants.QUERYUNION);
				queryString.append(" {{ SELECT {p." + BuyAPercentageDiscountModel.PK + "} AS pk, {p."
						+ BuyAPercentageDiscountModel.PRIORITY + "} AS prio FROM {" + BuyAPercentageDiscountModel._TYPECODE + " AS p ");
				queryString.append(" JOIN " + GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION + " AS c2p ");
				queryString.append(" ON {p." + BuyAPercentageDiscountModel.PK + "} = {c2p.target} ");
				queryString.append(" AND {c2p.source} IN (?categories) }");
				//queryString.append(" WHERE {p." + BuyAPercentageDiscountModel.PROMOTIONGROUP + "} = ?promotionGroup ");
				//				queryString.append(" AND {c2p.source} IN (?categories) ");
				queryString.append(" WHERE {p." + BuyAPercentageDiscountModel.PRIORITY + "} >= ?promoCurrPriority ");
				queryString.append(" AND {p." + BuyAPercentageDiscountModel.IMMUTABLEKEY + "} is NULL ");
				queryString.append(" AND {p." + BuyAPercentageDiscountModel.ENABLED + "} = ?true ");
				queryString.append(" AND {p." + BuyAPercentageDiscountModel.QUANTITY + "} = ?qualifyingCount ");//TODO quantity is for BuyAPercentageDiscount only, not for PRODUCTPROMOTION
				queryString.append(" AND {p." + BuyAPercentageDiscountModel.STARTDATE + "} <= ?sysdate ");
				queryString.append(" AND ?sysdate <= {p." + BuyAPercentageDiscountModel.ENDDATE + "} }}");

				if (!(Config.isOracleUsed()))
				{
					queryString.append(" ) AS pprom ORDER BY pprom.prio DESC");
				}
				else
				{
					queryString.append(" ) pprom ORDER BY pprom.prio DESC");
				}

				LOG.debug("QUERY>>>>>>" + queryString);
				final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

				query.addQueryParameter("product", product);
				query.addQueryParameter("categories", categories);
				//query.addQueryParameter("promotionGroup", "mplPromoGrp");
				query.addQueryParameter("promoCurrPriority", promoCurrent.getPriority());
				query.addQueryParameter("qualifyingCount", Integer.valueOf(1));
				query.addQueryParameter("sysdate", new Date());
				query.addQueryParameter("true", Boolean.TRUE);

				query.setResultClassList(Arrays.asList(ProductPromotionModel.class, Integer.class));

				LOG.debug("QUERY>>>>>>" + queryString);

				final SearchResult<List<Object>> result = flexibleSearchService.search(query);
				for (final List<Object> row : result.getResult())
				{
					validPromosForProduct.add((ProductPromotionModel) row.get(0));
					//final String priority = (String) row.get(1);
				}
			}
		}
		else if (promoCurrent instanceof BuyABFreePrecentageDiscountModel)
		{
			final StringBuilder queryString = new StringBuilder("SELECT DISTINCT pprom.pk, pprom.prio FROM (");
			queryString.append(" {{ SELECT {p." + BuyABFreePrecentageDiscountModel.PK + "} AS pk, {p."
					+ BuyABFreePrecentageDiscountModel.PRIORITY + "} AS prio FROM {" + BuyABFreePrecentageDiscountModel._TYPECODE
					+ " AS p ");
			queryString.append(" JOIN " + GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION + " AS p2p ");
			queryString.append(" ON {p2p.target} = {p." + BuyABFreePrecentageDiscountModel.PK + "}");
			queryString.append(" AND {p2p.source} = ?product }");
			//queryString.append(" WHERE {p." + BuyABFreePrecentageDiscountModel.PROMOTIONGROUP + "} = ?promotionGroup ");
			//			queryString.append(" AND {p2p.source} IN (?product) ");
			queryString.append(" WHERE {p." + BuyABFreePrecentageDiscountModel.PRIORITY + "} >= ?promoCurrPriority ");
			queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.IMMUTABLEKEY + "} is NULL ");
			queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.ENABLED + "} = ?true ");
			queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.QUANTITY + "} = ?qualifyingCount ");//TODO quantity is for BuyAPercentageDiscount only, not for PRODUCTPROMOTION
			queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.STARTDATE + "} <= ?sysdate ");
			queryString.append(" AND ?sysdate <= {p." + BuyABFreePrecentageDiscountModel.ENDDATE + "} }}");

			if (CollectionUtils.isNotEmpty(categories))
			{
				queryString.append(MarketplacecommerceservicesConstants.QUERYUNION);
				queryString.append(" {{ SELECT {p." + BuyABFreePrecentageDiscountModel.PK + "} AS pk, {p."
						+ BuyABFreePrecentageDiscountModel.PRIORITY + "} AS prio FROM {" + BuyABFreePrecentageDiscountModel._TYPECODE
						+ " AS p ");
				queryString.append(" JOIN " + GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION + " AS c2p ");
				queryString.append(" ON {p." + BuyABFreePrecentageDiscountModel.PK + "} = {c2p.target} ");
				queryString.append(" AND {c2p.source} IN (?categories) }");
				//queryString.append(" WHERE {p." + BuyABFreePrecentageDiscountModel.PROMOTIONGROUP + "} = ?promotionGroup ");
				//queryString.append(" WHERE {c2p.source} IN (?categories) ");
				queryString.append(" WHERE {p." + BuyABFreePrecentageDiscountModel.PRIORITY + "} >= ?promoCurrPriority ");
				queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.IMMUTABLEKEY + "} is NULL ");
				queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.ENABLED + "} = ?true ");
				queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.QUANTITY + "} = ?qualifyingCount ");//TODO quantity is for BuyAPercentageDiscount only, not for PRODUCTPROMOTION
				queryString.append(" AND {p." + BuyABFreePrecentageDiscountModel.STARTDATE + "} <= ?sysdate ");
				queryString.append(" AND ?sysdate <= {p." + BuyABFreePrecentageDiscountModel.ENDDATE + "} }}");
			}

			if (!(Config.isOracleUsed()))
			{
				queryString.append(" ) AS pprom ORDER BY pprom.prio DESC");
			}
			else
			{
				queryString.append(" ) pprom ORDER BY pprom.prio DESC");
			}

			LOG.debug("QUERY>>>>>>" + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter("product", product);
			query.addQueryParameter("categories", categories);
			//query.addQueryParameter("promotionGroup", "mplPromoGrp");
			query.addQueryParameter("promoCurrPriority", promoCurrent.getPriority());
			query.addQueryParameter("qualifyingCount", "1");
			query.addQueryParameter("sysdate", new Date());
			query.addQueryParameter("true", Boolean.TRUE);

			query.setResultClassList(Arrays.asList(ProductPromotionModel.class, Integer.class));

			LOG.debug("QUERY>>>>>>" + queryString);

			final SearchResult<List<Object>> result = flexibleSearchService.search(query);
			for (final List<Object> row : result.getResult())
			{
				validPromosForProduct.add((ProductPromotionModel) row.get(0));
				//final String priority = (String) row.get(1);
			}
		}
		/////////////////-------------------------//////////////////////////////////////////
		//		final List<ProductPromotionModel> productPromoData = new ArrayList<ProductPromotionModel>(product.getPromotions());
		//		final Collection<CategoryModel> categoriesList = getcategoryData(product);
		//		final List<ProductPromotionModel> validPromosForProduct = new ArrayList<ProductPromotionModel>();
		//
		//		//		final List<BuyAPercentageDiscountModel> validBuyAPerOffPromosForProduct = new ArrayList<BuyAPercentageDiscountModel>();
		//		//		final List<BuyABFreePrecentageDiscountModel> validBuyAgetBFreePerOffPromosForProduct = new ArrayList<BuyABFreePrecentageDiscountModel>();
		//
		//		if (CollectionUtils.isNotEmpty(categoriesList))
		//		{
		//			for (final CategoryModel category : categoriesList)
		//			{
		//				if (CollectionUtils.isNotEmpty(productPromoData))
		//				{
		//					productPromoData.addAll(category.getPromotions());
		//				}
		//			}
		//		}
		//
		//		if (promoCurrent instanceof BuyAPercentageDiscountModel)
		//		{
		//			for (final ProductPromotionModel promo : productPromoData)
		//			{
		//				final Date sysdate = new Date();
		//				if (promo instanceof BuyAPercentageDiscountModel)
		//				{
		//					final BuyAPercentageDiscountModel buyAgetPerOff = (BuyAPercentageDiscountModel) promo;
		//					if (StringUtils.isEmpty(buyAgetPerOff.getImmutableKey()) && buyAgetPerOff.getEnabled().booleanValue()
		//							&& buyAgetPerOff.getStartDate().compareTo(sysdate) <= 0
		//							&& buyAgetPerOff.getEndDate().compareTo(sysdate) >= 0
		//							&& buyAgetPerOff.getPriority().intValue() >= promoCurrent.getPriority().intValue()
		//							//					&& (promo.getStartDate().equals(sysdate) || promo.getStartDate().before(sysdate))
		//							//					&& (promo.getEndDate().equals(sysdate) || promo.getEndDate().after(sysdate))
		//							&& buyAgetPerOff.getQuantity().intValue() == 1)
		//					{
		//						//validBuyAPerOffPromosForProduct.add(buyAgetPerOff);
		//						validPromosForProduct.add(buyAgetPerOff);
		//					}
		//				}
		//			}
		//
		//			//Sorting promotion collection on priority DESC
		//			//			Collections.sort(validPromosForProduct, new Comparator<ProductPromotionModel>()
		//			//			{
		//			//				public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
		//			//				{
		//			//					if (o1.getPriority().intValue() > o2.getPriority().intValue())
		//			//					{
		//			//						return 1;
		//			//					}
		//			//					else
		//			//					{
		//			//						return -1;
		//			//					}
		//			//				}
		//			//			});
		//			//return validBuyAPerOffPromosForProduct;
		//		}
		//		else if (promoCurrent instanceof BuyABFreePrecentageDiscountModel)
		//		{
		//			for (final ProductPromotionModel promo : productPromoData)
		//			{
		//				final Date sysdate = new Date();
		//				if (promo instanceof BuyABFreePrecentageDiscountModel)
		//				{
		//					final BuyABFreePrecentageDiscountModel buyAgetBfreePerOff = (BuyABFreePrecentageDiscountModel) promo;
		//					if (StringUtils.isEmpty(buyAgetBfreePerOff.getImmutableKey()) && buyAgetBfreePerOff.getEnabled().booleanValue()
		//							&& buyAgetBfreePerOff.getStartDate().compareTo(sysdate) <= 0
		//							&& buyAgetBfreePerOff.getEndDate().compareTo(sysdate) >= 0
		//							&& buyAgetBfreePerOff.getPriority().intValue() >= promoCurrent.getPriority().intValue()
		//							//					&& (promo.getStartDate().equals(sysdate) || promo.getStartDate().before(sysdate))
		//							//					&& (promo.getEndDate().equals(sysdate) || promo.getEndDate().after(sysdate))
		//							&& buyAgetBfreePerOff.getQuantity().intValue() == 1)
		//					{
		//						//validBuyAgetBFreePerOffPromosForProduct.add(buyAgetBfreePerOff);
		//						validPromosForProduct.add(buyAgetBfreePerOff);
		//					}
		//				}
		//			}
		//
		//			//Sorting promotion collection on priority DESC
		//			//			Collections.sort(validPromosForProduct, new Comparator<ProductPromotionModel>()
		//			//			{
		//			//				public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
		//			//				{
		//			//					if (o1.getPriority().intValue() > o2.getPriority().intValue())
		//			//					{
		//			//						return 1;
		//			//					}
		//			//					else
		//			//					{
		//			//						return -1;
		//			//					}
		//			//				}
		//			//			});
		//			//return validBuyAgetBFreePerOffPromosForProduct;
		//		}
		//
		//		//Sorting promotion collection on priority DESC
		//		Collections.sort(validPromosForProduct, new Comparator<ProductPromotionModel>()
		//		{
		//			public int compare(final ProductPromotionModel o1, final ProductPromotionModel o2)
		//			{
		//				if (o1.getPriority().intValue() > o2.getPriority().intValue())
		//				{
		//					return 1;
		//				}
		//				else
		//				{
		//					return -1;
		//				}
		//			}
		//		});
		/////////////////-------------------------//////////////////////////////////////////

		return validPromosForProduct;
	}

	@Override
	public List<ProductModel> getProductsForCategory(final List<CategoryModel> categories, final List<ProductModel> exProductList,
			final List<CategoryModel> brands, final List<CategoryModel> rejectBrandList)
	{
		final List<CategoryModel> categoryList = mplPromotionHelper.getAllCategories(categories);
		final Map params = new HashMap();
		params.put("categories", categoryList);

		final StringBuilder queryString = new StringBuilder("SELECT DISTINCT pprom.pk FROM (");
		queryString.append(" {{ SELECT {c2p.target} as pk " + MarketplacecommerceservicesConstants.QUERYFROM);
		queryString.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION + " AS c2p }");
		queryString.append(" WHERE {c2p.source} IN (?categories)");

		if (CollectionUtils.isEmpty(exProductList) && CollectionUtils.isEmpty(brands) && CollectionUtils.isEmpty(rejectBrandList))
		{
			queryString.append(" }} ");
		}
		else
		{
			if (CollectionUtils.isNotEmpty(exProductList))
			{
				queryString.append(" AND {c2p.target} NOT IN (?exProducts) }}");
				params.put("exProducts", exProductList);
			}

			if (CollectionUtils.isNotEmpty(brands))
			{
				queryString.append(" }} INTERSECT ");
				queryString.append("{{ SELECT {cat2prod.target} as pk  ");
				queryString.append(MarketplacecommerceservicesConstants.QUERYFROM)
						.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION).append(" AS cat2prod } ");
				queryString.append(" WHERE {cat2prod.source} in (?brands) }} ");

				params.put("brands", brands);
			}
			else if (CollectionUtils.isNotEmpty(rejectBrandList))
			{
				queryString.append(" }} INTERSECT ");
				queryString.append("{{ SELECT {cat2prod:target} as pk  ");
				queryString.append(MarketplacecommerceservicesConstants.QUERYFROM).append(
						GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION);
				queryString.append(" AS cat2prod JOIN ").append(CategoryModel._TYPECODE)
						.append(" AS category on {cat2prod.source} = {category.pk}} ");
				queryString.append("WHERE {cat2prod:source} not in (?rejectBrands) AND {category.code} like '%")
						.append(MarketplacecommerceservicesConstants.BRAND_NAME_PREFIX).append("%' }} ");

				params.put("rejectBrands", rejectBrandList);
			}
		}

		if (!(Config.isOracleUsed()))
		{
			queryString.append(" ) AS pprom");
		}
		else
		{
			queryString.append(" ) pprom");
		}

		LOG.debug("QUERY>>>>>>" + queryString);

		final List<ProductModel> productValidList = flexibleSearchService.<ProductModel> search(queryString.toString(), params)
				.getResult();

		return productValidList;
	}

	@Override
	public List<SellerInformationModel> getValidSellersForPromotion(final ProductModel product,
			final List<String> promoSellersList, final List<String> promoRejectSellerList)
	{
		//Get valid promotional seller list for product
		final Map params = new HashMap();
		params.put("product", product.getPk());

		final StringBuilder queryString = new StringBuilder("SELECT {" + SellerInformationModel.PK + "} FROM {"
				+ SellerInformationModel._TYPECODE + " AS sellerInfo} " + " WHERE {sellerInfo."
				+ SellerInformationModel.PRODUCTSOURCE + "} = ?product ");

		if (CollectionUtils.isNotEmpty(promoSellersList))
		{
			queryString.append(" AND {" + SellerInformationModel.SELLERID + " } IN (?promotionSeller)");
			params.put("promotionSeller", promoSellersList);
		}
		else if (CollectionUtils.isNotEmpty(promoRejectSellerList))
		{
			queryString.append(" AND {" + SellerInformationModel.SELLERID + " } NOT IN (?promotionRejectedSeller)");
			params.put("promotionRejectedSeller", promoRejectSellerList);
		}

		LOG.debug("QUERY>>>>>>" + queryString);
		final List<SellerInformationModel> productValidSellerList = flexibleSearchService.<SellerInformationModel> search(
				queryString.toString(), params).getResult();
		return productValidSellerList;
	}

	@Override
	public CategoryModel getBrandForProduct(final ProductModel product)
	{
		final Map params = new HashMap();
		params.put("product", product);

		//		final StringBuilder queryString = new StringBuilder("SELECT {c2p.source} as pk " + MarketplacecommerceservicesConstants.QUERYFROM);
		//		queryString.append(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION + " AS c2p }");
		//		queryString.append(" WHERE {c2p.target} =?product)");

		final StringBuilder queryString = new StringBuilder("SELECT {cat2prod:source} as pk  ");
		queryString.append(MarketplacecommerceservicesConstants.QUERYFROM).append(
				GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION);
		queryString.append(" AS cat2prod JOIN ").append(CategoryModel._TYPECODE)
				.append(" AS category on {cat2prod.source} = {category.pk}} ");
		queryString.append("WHERE {cat2prod:target} =?product AND {category.code} like '%")
				.append(MarketplacecommerceservicesConstants.BRAND_NAME_PREFIX).append("%' ");

		LOG.debug("QUERY>>>>>>" + queryString);

		final List<CategoryModel> productValidList = flexibleSearchService.<CategoryModel> search(queryString.toString(), params)
				.getResult();

		return CollectionUtils.isNotEmpty(productValidList) ? productValidList.get(0) : null;


	}

	@Resource(name = "mplPromotionHelper")
	MplPromotionHelper mplPromotionHelper;

}