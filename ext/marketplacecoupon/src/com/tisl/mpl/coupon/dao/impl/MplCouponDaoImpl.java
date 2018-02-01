/**
 *
 */
package com.tisl.mpl.coupon.dao.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.dao.MplCouponDao;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.model.MplCartOfferVoucherModel;


/**
 * @author TCS
 *
 */
public class MplCouponDaoImpl implements MplCouponDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "pagedFlexibleSearchService")
	private PagedFlexibleSearchService pagedFlexibleSearchService;

	private static final Logger LOG = Logger.getLogger(MplCouponDaoImpl.class);

	@Autowired
	private ConfigurationService configurationService;

	private static final String QUERY_STRING = "queryString: ";

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}


	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


	/**
	 * This method is used to fetch the active coupons from the database
	 *
	 * @return List<VoucherModel>
	 *
	 */
	@Override
	public List<VoucherModel> findVoucher()
	{
		try
		{
			final String queryString = MarketplacecouponConstants.VOUCHERWITHINDATEQUERY;
			LOG.debug(QUERY_STRING + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			return getFlexibleSearchService().<VoucherModel> search(query).getResult();
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
	 * This method is used to find closed vouchers
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherModel>
	 */
	@Override
	public SearchPageData<VoucherModel> findClosedVoucher(final CustomerModel customer, final PageableData pageableData)
	{

		try
		{
			final StringBuilder queryBiulder = new StringBuilder(600);
			final StringBuilder groupBiulder = new StringBuilder(200);

			final Set<PrincipalGroupModel> groups = customer.getGroups();
			final Map queryParams = new HashMap();
			int count = 1;
			for (final PrincipalGroupModel principal : groups)
			{
				groupBiulder.append(" OR {ur.closedUser} = ?").append("customerGroupPk_").append(count);
				queryParams.put("customerGroupPk_" + count, principal.getPk().toString());
				count++;
			}

			queryParams.put(MarketplacecouponConstants.CUSTOMERPK, customer.getPk().toString());
			//queryParams.put("customerPkInvalidation", customer.getPk().toString());
			queryParams.put("isIncluded", "1");

			queryBiulder
					.append(
							"select {v.pk} from {Promotionvoucher as v JOIN CouponUserRestriction as ur ON {v.pk}={ur.voucher} JOIN daterestriction as dr ON {v.pk}={dr.voucher}} where {dr.startdate} <= sysdate and sysdate<= {dr.enddate} AND ( {ur.closedUser} = ?")
					.append(MarketplacecouponConstants.CUSTOMERPK)
					.append(groupBiulder.toString())
					.append(
							" )AND {ur.positive} = ?isIncluded AND {v.redemptionQuantityLimitPerUser} > ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk} AND {vin.user}=?")
					.append(MarketplacecouponConstants.CUSTOMERPK)
					.append(
							"  }}) AND {v.redemptionQuantityLimit} > ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk}}}) ORDER BY {dr.startdate} DESC");


			final String CLOSED_VOUCHER = queryBiulder.toString();
			LOG.debug(QUERY_STRING + CLOSED_VOUCHER);
			final List sortQueries = Arrays.asList(new SortQueryData[]
			{ createSortQueryData(MarketplacecouponConstants.BYDATE, CLOSED_VOUCHER

			) });


			return getPagedFlexibleSearchService().search(sortQueries, MarketplacecouponConstants.BYDATE, queryParams, pageableData);

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
	 * This method is used to find payment mode related offers from MplCartOfferVoucher TPR-7486
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherModel>
	 */
	@Override
	public List<MplCartOfferVoucherModel> getPaymentModerelatedVouchers()
	{
		try
		{

			final String queryString = MarketplacecouponConstants.ALLOFFERVOUCHERQUERY;
			LOG.debug(QUERY_STRING + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			return getFlexibleSearchService().<MplCartOfferVoucherModel> search(query).getResult();

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

	@Override
	public Map<String, Double> getPaymentModerelatedVoucherswithTotal()
	{
		final Map<String, Double> voucherdata = new HashMap<String, Double>();
		try
		{


			final String queryString = MarketplacecouponConstants.ALLOFFERVOUCHERQUERYTOTAL;
			LOG.debug(QUERY_STRING + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.setResultClassList(Arrays.asList(Long.class, Double.class));
			final SearchResult<List<Object>> result = getFlexibleSearchService().search(query);

			if (!result.getResult().isEmpty())
			{
				for (final List<Object> row : result.getResult())
				{
					//final Map<Long, Double> resultMap = new HashMap<Long, Double>();
					final String voucher = String.valueOf(row.get(0));
					final Double total = (Double) row.get(1);
					voucherdata.put(voucher, total);
				}
			}

			return voucherdata;
			//return getFlexibleSearchService().<VoucherModel> search(query).getResult();

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
	 * Method used to find voucher invalidations for a user
	 *
	 * @param customer
	 * @param pageableData
	 * @return SearchPageData<VoucherInvalidationModel>
	 */
	@Override
	public SearchPageData<VoucherInvalidationModel> findVoucherHistoryRedeemedOrders(final CustomerModel customer,
			final PageableData pageableData)
	{
		try
		{
			final StringBuilder queryBiulder = new StringBuilder(600);
			final Map queryParams = new HashMap();

			final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			//			final Calendar calendar = Calendar.getInstance();
			//			calendar.add(Calendar.MONTH, -6);
			//			final Date sixMonthsBeforeDate = calendar.getTime();
			//			final String dateSixMonthsBefore = formatter.format(sixMonthsBeforeDate);
			final String currentDate = formatter.format(new Date());

			queryBiulder.append("select {vi.pk} from {VoucherInvalidation as vi JOIN Order as odr ON {vi.order}={odr.pk}}")
					.append(" where {vi.user} like").append("('%").append(customer.getPk().getLongValue()).append("%')")
					.append("and {odr.date} > to_date('").append(currentDate).append("', 'MM/DD/YYYY') - INTERVAL '6' MONTH ")
					.append("ORDER BY {vi.creationtime} DESC");

			final String VOUCHER_HISTORY_QUERY = queryBiulder.toString();
			LOG.debug(QUERY_STRING + VOUCHER_HISTORY_QUERY);
			final List sortQueries = Arrays.asList(new SortQueryData[]
			{ createSortQueryData(MarketplacecouponConstants.BYDATE, VOUCHER_HISTORY_QUERY) });

			return getPagedFlexibleSearchService().search(sortQueries, MarketplacecouponConstants.BYDATE, queryParams, pageableData);

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
	 * Method used to find voucher invalidations for a user
	 *
	 * @param customer
	 * @return List<VoucherInvalidationModel>
	 *
	 */
	@Override
	public Map<String, Double> findVoucherHistoryAllInvalidations(final CustomerModel customer)
	{
		try
		{
			final StringBuilder queryBiulder = new StringBuilder(500);
			//final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			//final Calendar calendar = Calendar.getInstance();
			//calendar.add(Calendar.MONTH, -6);
			//final Date sixMonthsBeforeDate = calendar.getTime();
			//final String dateSixMonthsBefore = formatter.format(sixMonthsBeforeDate);
			//final String currentDate = formatter.format(new Date());
			//final double totalSaving = 0;
			//final int totalCount = 0;

			//Fix for TISPRO-530 --- Only work with Oracle DB
			queryBiulder.append("SELECT COUNT(distinct{vi.voucher}),SUM({vi.savedAmount}) FROM {VoucherInvalidation as vi JOIN ")
					.append("Order AS odr ON {vi.order}={odr.pk}} WHERE {vi.user} LIKE ")
					.append("('%")
					//.append(customer.getPk().getLongValue()).append("%')").append("AND {odr.date} > to_date('").append(currentDate)
					//.append("', 'MM/DD/YYYY') - INTERVAL '6' MONTH");
					.append(customer.getPk().getLongValue()).append("%')")
					.append(" AND {odr.date} > TRUNC (ADD_MONTHS (SYSDATE, -6), 'MM')");

			final String queryString = queryBiulder.toString();
			LOG.info("Query is ::: " + queryString);
			final FlexibleSearchQuery voucherInvalidationSumQuery = new FlexibleSearchQuery(queryString);
			voucherInvalidationSumQuery.setResultClassList(Arrays.asList(Integer.class, Double.class));
			final SearchResult<List<Object>> result = flexibleSearchService.search(voucherInvalidationSumQuery);

			final Map<String, Double> totalSavingSumMap = new HashMap<String, Double>();
			for (final List<Object> obj : result.getResult())
			{
				final Integer count = (Integer) obj.get(0);
				final Double saving = (Double) obj.get(1);

				totalSavingSumMap.put(String.valueOf(count), saving);
				//				final int countInt = Integer.parseInt(count);
				//				if (countInt > 1)
				//				{
				//					totalCount = totalCount + 1;
				//				}
				//				else
				//				{
				//					totalCount = totalCount + countInt;
				//				}
				//
				//				final Double saving = (Double) obj.get(1);
				//				totalSaving = totalSaving + saving.doubleValue();
			}
			//			if (totalCount > 0 && totalSaving > 0)
			//			{
			//				final Double totalSavingDouble = new Double(totalSaving);
			//				totalSavingSumMap.put(String.valueOf(totalCount), totalSavingDouble);
			//			}

			return totalSavingSumMap;

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
	 * Method used to create sort query data
	 *
	 * @param sortCode
	 * @param query
	 * @return SortQueryData
	 */
	protected SortQueryData createSortQueryData(final String sortCode, final String query)
	{
		final SortQueryData result = new SortQueryData();
		result.setSortCode(sortCode);
		result.setQuery(query);
		return result;
	}

	/**
	 * @return the pagedFlexibleSearchService
	 */
	public PagedFlexibleSearchService getPagedFlexibleSearchService()
	{
		return pagedFlexibleSearchService;
	}

	/**
	 * @param pagedFlexibleSearchService
	 *           the pagedFlexibleSearchService to set
	 */
	public void setPagedFlexibleSearchService(final PagedFlexibleSearchService pagedFlexibleSearchService)
	{
		this.pagedFlexibleSearchService = pagedFlexibleSearchService;
	}


	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}



	@Override
	public String getVoucherCode(final String manuallyselectedvoucher)
	{
		try
		{
			final String queryString = "select {pk} from {MplCartOfferVoucher} where {code}=?couponcode";

			final FlexibleSearchQuery auditQuery = new FlexibleSearchQuery(queryString);
			auditQuery.addQueryParameter("couponcode", manuallyselectedvoucher);

			final List<VoucherModel> voucherList = flexibleSearchService.<VoucherModel> search(auditQuery).getResult();
			if (CollectionUtils.isNotEmpty(voucherList))
			{
				//fetching the audit entries
				final MplCartOfferVoucherModel voucher = (MplCartOfferVoucherModel) voucherList.get(0);
				if (StringUtils.isNotEmpty(voucher.getVoucherCode()))
				{
					return voucher.getVoucherCode();
				}

			}
			else
			{
				return MarketplacecommerceservicesConstants.EMPTY;
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
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return MarketplacecommerceservicesConstants.EMPTY;

	}
}
