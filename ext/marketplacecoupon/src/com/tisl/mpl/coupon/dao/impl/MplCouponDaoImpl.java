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
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			return getFlexibleSearchService().<VoucherModel> search(query).getResult();
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
			final StringBuilder queryBiulder = new StringBuilder(500);
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
							"select {v.pk} from {Promotionvoucher as v JOIN userrestriction as ur ON {v.pk}={ur.voucher} JOIN daterestriction as dr ON {v.pk}={dr.voucher}} where {dr.startdate} <= sysdate and sysdate<= {dr.enddate} ")
					//for checking current user and user group
					.append(" AND ( {ur.users} like ('%")
					.append(customer.getPk().getLongValue())
					.append("%')")
					.append(groupBiulder.toString())
					//check voucher invalidation table
					.append(" )AND {v.redemptionQuantityLimitPerUser} >")
					.append(" ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk} ")
					.append(" AND  {vin.user}='").append(customer.getPk().getLongValue()).append("' ").append(" }})")
					.append(" AND {v.redemptionQuantityLimit} >")
					.append(" ({{select count(*) from {VoucherInvalidation as vin} where {vin.voucher}={v.pk}}})")
					.append(" ORDER BY {dr.startdate} ASC");

			final String CLOSED_VOUCHER = queryBiulder.toString();
			final List sortQueries = Arrays.asList(new SortQueryData[]
			{ createSortQueryData(MarketplacecouponConstants.BYDATE, CLOSED_VOUCHER

			) });


			return getPagedFlexibleSearchService().search(sortQueries, MarketplacecouponConstants.BYDATE, queryParams, pageableData);

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

			final Map queryParams = new HashMap();
			queryParams.put(MarketplacecouponConstants.CUSTOMERPK, customer);
			final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			final String todayDate = formatter.format(new Date());

			final StringBuilder queryBiulder = new StringBuilder(500);
			queryBiulder.append("select {vi.pk} from {VoucherInvalidation as vi JOIN Order as odr ON {vi.order}={odr.pk}}")
					.append(" where {vi.user} like").append("('%").append(customer.getPk().getLongValue()).append("%')")
					.append("and {odr.date} > DATE_SUB(to_date('").append(todayDate).append("', 'MM/DD/YYYY'), INTERVAL 6 MONTH)");


			final String VOUCHER_HISTORY_QUERY = queryBiulder.toString();
			final List sortQueries = Arrays.asList(new SortQueryData[]
			{ createSortQueryData(MarketplacecouponConstants.BYDATE, VOUCHER_HISTORY_QUERY) });

			return getPagedFlexibleSearchService().search(sortQueries, MarketplacecouponConstants.BYDATE, queryParams, pageableData);

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
	public List<VoucherInvalidationModel> findVoucherHistoryAllInvalidations(final CustomerModel customer)
	{
		try
		{
			final StringBuilder queryBiulder = new StringBuilder(500);
			final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			final String todayDate = formatter.format(new Date());
			queryBiulder.append("select {vi.pk} from {VoucherInvalidation as vi JOIN Order as odr ON {vi.order}={odr.pk}}")
					.append(" where {vi.user} like").append("('%").append(customer.getPk().getLongValue()).append("%')")
					.append("and {odr.date} > DATE_SUB(to_date('").append(todayDate).append("', 'MM/DD/YYYY'), INTERVAL 6 MONTH)");

			final String queryString = queryBiulder.toString();
			//forming the flexible search query
			final FlexibleSearchQuery voucherInvalidationQuery = new FlexibleSearchQuery(queryString);

			//fetching bank list from DB using flexible search query
			final List<VoucherInvalidationModel> voucherInvalidationList = flexibleSearchService.<VoucherInvalidationModel> search(
					voucherInvalidationQuery).getResult();

			return voucherInvalidationList;
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
