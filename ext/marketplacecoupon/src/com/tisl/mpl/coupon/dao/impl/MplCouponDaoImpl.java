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

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.coupon.constants.MarketplacecouponConstants;
import com.tisl.mpl.coupon.dao.MplCouponDao;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;


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
			LOG.debug("queryString: " + queryString);
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
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
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
			for (final PrincipalGroupModel principal : groups)
			{
				groupBiulder.append(" OR {ur.users} like ('%").append(principal.getPk().getLongValue()).append("%') ");
			}

			final Map queryParams = new HashMap();
			queryParams.put(MarketplacecouponConstants.CUSTOMERPK, customer);

			queryBiulder
					.append(
							"select {v.pk} from {Promotionvoucher as v JOIN userrestriction as ur ON {v.pk}={ur.voucher} JOIN daterestriction as dr ON {v.pk}={dr.voucher}} where {dr.startdate} <= sysdate and sysdate<= {dr.enddate} AND ( {ur.users} like ('%")
					//for checking current user and user group
					//.append(" AND ( {ur.users} like ('%")
					.append(customer.getPk().getLongValue())
					.append("%')")
					.append(groupBiulder.toString())
					//check voucher invalidation table
					.append(
							" )AND {v.redemptionQuantityLimitPerUser} > ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk} AND {vin.user}='")
					//.append(" ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk} ")
					//.append(" AND  {vin.user}='")
					.append(customer.getPk().getLongValue())
					.append(
							"'  }}) AND {v.redemptionQuantityLimit} > ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk}}}) ORDER BY {dr.startdate} ASC");
			//.append(" }})")
			//.append(" AND {v.redemptionQuantityLimit} >")
			//.append(" ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk}}})")
			//.append(" ORDER BY {dr.startdate} ASC");

			final String CLOSED_VOUCHER = queryBiulder.toString();
			LOG.debug("queryString: " + CLOSED_VOUCHER);
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
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
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
			LOG.debug("queryString: " + VOUCHER_HISTORY_QUERY);
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
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
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
			final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			//final Calendar calendar = Calendar.getInstance();
			//calendar.add(Calendar.MONTH, -6);
			//final Date sixMonthsBeforeDate = calendar.getTime();
			//final String dateSixMonthsBefore = formatter.format(sixMonthsBeforeDate);
			final String currentDate = formatter.format(new Date());
			//final double totalSaving = 0;
			//final int totalCount = 0;

			queryBiulder.append("SELECT COUNT(distinct{vi.voucher}),SUM({vi.savedAmount}) FROM {VoucherInvalidation as vi JOIN ")
					.append("Order AS odr ON {vi.order}={odr.pk}} WHERE {vi.user} LIKE ").append("('%")
					.append(customer.getPk().getLongValue()).append("%')").append("AND {odr.date} > to_date('").append(currentDate)
					.append("', 'MM/DD/YYYY') - INTERVAL '6' MONTH");

			final String queryString = queryBiulder.toString();
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
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
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



}
